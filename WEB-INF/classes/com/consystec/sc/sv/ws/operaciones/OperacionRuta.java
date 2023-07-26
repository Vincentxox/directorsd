package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.pdv.InputPDV;
import com.consystec.sc.ca.ws.input.ruta.InputRuta;
import com.consystec.sc.ca.ws.input.ruta.InputVendedor;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.ruta.OutputRuta;
import com.consystec.sc.sv.ws.metodos.CtrlRuta;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.orm.RutaPDV;
import com.consystec.sc.sv.ws.orm.VendedorPDV;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class OperacionRuta {
	private OperacionRuta() {
	}

	private static final Logger log = Logger.getLogger(OperacionRuta.class);

	/**
	 * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
	 * 
	 * @param input
	 * @param metodo
	 * @return OutputRuta
	 * @throws SQLException
	 */
	public static OutputRuta doGet(Connection conn, InputRuta input, int metodo, BigDecimal ID_PAIS)
			throws SQLException {
		String nombreMetodo = "doGet";
		String nombreClase = new CurrentClassGetter().getClassName();

		List<InputRuta> list = new ArrayList<InputRuta>();
		Respuesta respuesta = null;
		OutputRuta output = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String sql = null;

		try {

			String campos[][] = CtrlRuta.obtenerCamposGet(input.getCodArea(), ID_PAIS);

			String tablas[] = { Ruta.N_TABLA, Distribuidor.N_TABLA };

			List<Filtro> condiciones = CtrlRuta.obtenerCondiciones(input, metodo, ID_PAIS);

			condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Ruta.N_TABLA,
					Ruta.CAMPO_TC_SC_DTS_ID, Distribuidor.N_TABLA, Distribuidor.CAMPO_TC_SC_DTS_ID));

			List<Order> orden = new ArrayList<Order>();
			orden.add(new Order(Ruta.N_TABLA + "." + Ruta.CAMPO_NOMBRE, Order.ASC));

			sql = UtileriasBD.armarQueryGetMultiple(campos, tablas, condiciones, orden);
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst != null) {
				if (!rst.next()) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_RUTAS_822, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					output = new OutputRuta();
					output.setRespuesta(respuesta);
				} else {
					respuesta = new Respuesta();
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					String estadoActivo = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_ESTADOS_PDV,
							Conf.ESTADO_ACTIVO, input.getCodArea());

					do {
						InputRuta item = new InputRuta();
						String idVendedor = rst.getString(Ruta.CAMPO_SEC_USUARIO_ID);

						item.setIdRuta(rst.getString(Ruta.CAMPO_TC_SC_RUTA_ID));
						item.setNombreRuta(rst.getString(Ruta.CAMPO_NOMBRE));

						item.setIdDTS(rst.getString(Ruta.CAMPO_TC_SC_DTS_ID));
						item.setNombreDTS(rst.getString(Distribuidor.CAMPO_NOMBRES));
						item.setTipoDTS(rst.getString(Distribuidor.CAMPO_TIPO));

						item.setSecUsuarioId(idVendedor);
						item.setIdBodegaVendedor(rst.getString("BOD_VEND"));
						item.setIdBodegaVirtual(
								UtileriasJava.getValue(rst.getString(Ruta.CAMPO_TC_SC_BODEGAVIRTUAL_ID)));

						item.setEstado(rst.getString(Ruta.CAMPO_ESTADO));
						item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(Ruta.CAMPO_CREADO_EL)));
						item.setCreado_por(rst.getString(Ruta.CAMPO_CREADO_POR));
						item.setModificado_el(UtileriasJava.formatStringDate(rst.getString(Ruta.CAMPO_MODIFICADO_EL)));
						item.setModificado_por(rst.getString(Ruta.CAMPO_MODIFICADO_POR));

						InputVendedor datosVendedor = new InputVendedor();
						datosVendedor.setIdVendPanelPDV("");
						datosVendedor.setVendedor(idVendedor);
						datosVendedor.setNombre(rst.getString("NOMBRE_VEND"));
						datosVendedor
								.setCantInventario(rst.getString("CANT_INV") == null ? "0" : rst.getString("CANT_INV"));
						item.setDatosVendedor(datosVendedor);

						item.setPuntoVenta(
								getDatosPDV(conn, rst.getString(Ruta.CAMPO_TC_SC_RUTA_ID), estadoActivo, ID_PAIS));

						list.add(item);
					} while (rst.next());

					output = new OutputRuta();
					output.setRespuesta(respuesta);
					output.setRuta(list);
				}
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return output;
	}

	private static List<InputPDV> getDatosPDV(Connection conn, String idRuta, String estadoActivo, BigDecimal ID_PAIS)
			throws SQLException {
		List<InputPDV> list = null;
		InputPDV item = null;
		PreparedStatement pstmtIn = null;
		ResultSet rstIn = null;
		String sql = "";

		try {
			String campos[] = { PuntoVenta.N_TABLA_ID, PuntoVenta.CAMPO_NOMBRE, PuntoVenta.CAMPO_QR,
					"(SELECT NUM_RECARGA FROM TC_sC_numrecarga "
							+ "WHERE ESTADO='ALTA' AND ORDEN='1' AND IDTIPO=TC_SC_PUNTOVENTA.TCSCPUNTOVENTAID AND TIPO='PDV' ) AS NUMRECARGA" };

			List<Filtro> condiciones = new ArrayList<Filtro>();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, RutaPDV.CAMPO_TCSCRUTAID, idRuta));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, RutaPDV.CAMPO_TCSCCATPAISID,
					ID_PAIS.toString()));
			sql = UtileriasBD.armarQuerySelectField(RutaPDV.N_TABLA, RutaPDV.CAMPO_TCSCPUNTOVENTAID, condiciones, null);

			condiciones.clear();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, PuntoVenta.N_TABLA_ID, sql));
			condiciones.add(
					UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, PuntoVenta.CAMPO_ESTADO, estadoActivo));

			sql = UtileriasBD.armarQuerySelect(PuntoVenta.N_TABLA, campos, condiciones);

			pstmtIn = conn.prepareStatement(sql);
			rstIn = pstmtIn.executeQuery();

			if (rstIn != null) {
				if (!rstIn.next()) {
					log.debug("No existen registros en la tabla hija con esos par\u00E9metros.");
				} else {
					list = new ArrayList<InputPDV>();
					do {
						item = new InputPDV();
						item.setIdPDV(rstIn.getString(PuntoVenta.N_TABLA_ID));
						item.setNombrePDV(rstIn.getString(PuntoVenta.CAMPO_NOMBRE));
						item.setQr(rstIn.getString(PuntoVenta.CAMPO_QR));
						item.setNumRecargaOrden(rstIn.getString("NUMRECARGA"));
						list.add(item);
					} while (rstIn.next());
				}
			}
		} finally {
			DbUtils.closeQuietly(rstIn);
			DbUtils.closeQuietly(pstmtIn);
		}
		return list;
	}

	/**
	 * Funci\u00F3n que arma el query a utilizar al trabajar en m\u00E9todo POST.
	 * 
	 * @param conn
	 * @param input
	 * @return OutputRuta
	 * @throws SQLException
	 */
	public static OutputRuta doPost(Connection conn, InputRuta input, BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "doPost";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta = null;
		OutputRuta output = null;

		try {
			// Existencia en ruta
			List<Filtro> condicionesExistencia = CtrlRuta.obtenerCondicionesExistencia(input, Conf.METODO_POST, conn,
					ID_PAIS);
			String existencia = UtileriasBD.verificarExistencia(conn, Ruta.N_TABLA, condicionesExistencia);

			if (new Integer(existencia) > 0) {
				log.error("El ID de Usuario ya ha sido registrado en otra ruta.");
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_VENDEDOR_RUTA_840, null,
						nombreClase, nombreMetodo, null, input.getCodArea());

				output = new OutputRuta();
				output.setRespuesta(respuesta);

				return output;
			}

			// Existencia en panel
			condicionesExistencia.clear();
			String tipoPanel = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_PANEL, input.getCodArea());
			String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
			condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					VendedorPDV.CAMPO_VENDEDOR, input.getSecUsuarioId()));
			condicionesExistencia
					.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorPDV.CAMPO_TIPO, tipoPanel));
			condicionesExistencia
					.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorPDV.CAMPO_ESTADO, estadoAlta));
			condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					VendedorPDV.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
			existencia = UtileriasBD.verificarExistencia(conn, VendedorPDV.N_TABLA, condicionesExistencia);

			if (new Integer(existencia) > 0) {
				log.error("El ID de Usuario ya ha sido registrado en una panel.");
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_VENDEDOR_PANEL_841, null,
						nombreClase, nombreMetodo, null, input.getCodArea());

				output = new OutputRuta();
				output.setRespuesta(respuesta);

				return output;
			}

			int idRuta = 0;
			String sql = null;
			String campos[] = CtrlRuta.obtenerCamposPost();
			List<String> inserts = CtrlRuta.obtenerInsertsPost(input, Ruta.SEQUENCE, estadoAlta, ID_PAIS);

			sql = UtileriasBD.armarQueryInsert(Ruta.N_TABLA, campos, inserts);

			String generatedColumns[] = { Ruta.CAMPO_TC_SC_RUTA_ID };
			try (PreparedStatement pstmt = conn.prepareStatement(sql, generatedColumns);) {
				pstmt.executeUpdate();
				try (ResultSet rs = pstmt.getGeneratedKeys();) {
					if (rs.next()) {
						idRuta = rs.getInt(1);
					}
				}
			}
			log.debug("idRuta: " + idRuta);

			if (idRuta > 0) {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_RUTA_54, null, nombreClase,
						nombreMetodo, null, input.getCodArea());

				output = new OutputRuta();
				output.setIdRuta("" + idRuta);
				output.setRespuesta(respuesta);

				conn.commit();
			}

		} finally {
			conn.setAutoCommit(true);
		}

		return output;
	}

	/**
	 * Funci\u00F3n que arma el query a utilizar al modificar datos en m\u00E9todos
	 * PUT y DELETE.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @return OutputRuta
	 * @throws SQLException
	 */
	public static OutputRuta doPutDel(Connection conn, InputRuta input, int metodo, BigDecimal ID_PAIS)
			throws SQLException {
		String nombreMetodo = "doPutDel";
		String nombreClase = new CurrentClassGetter().getClassName();
		BigDecimal idVendedorActual = null;
		Respuesta respuesta = null;
		OutputRuta output = null;
		String estadoBaja = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, input.getCodArea());
		String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

		List<Filtro> condicionesExistencia = CtrlRuta.obtenerCondicionesExistencia(input, metodo, conn, ID_PAIS);
		String existencia = UtileriasBD.verificarExistencia(conn, Ruta.N_TABLA, condicionesExistencia);

		if (new Integer(existencia) < 1) {
			log.error("No existe el recurso.");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			output = new OutputRuta();
			output.setRespuesta(respuesta);

			return output;
		} else {
			String tipoRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_RUTA, input.getCodArea());
			if (OperacionJornada.existeJornadaSinLiquidar(conn, input.getIdRuta(), tipoRuta, input.getCodArea(),
					ID_PAIS)) {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_JORNADAS_NO_LIQUIDADAS_794, null,
						nombreClase, nombreMetodo, null, input.getCodArea());

				output = new OutputRuta();
				output.setRespuesta(respuesta);

				return output;
			}

			idVendedorActual = getidVendedorRuta(conn, new BigDecimal(input.getIdRuta()), ID_PAIS);

			if (metodo == Conf.METODO_PUT) {

				// Existencia de inventario del vendedor
				if (idVendedorActual != null
						&& idVendedorActual.intValue() != new BigDecimal(input.getSecUsuarioId()).intValue()
						|| input.getEstado().equalsIgnoreCase(estadoBaja)) {
					List<BigDecimal> lista = new ArrayList<BigDecimal>();
					lista = getInvRuta(conn, input.getIdRuta(), ID_PAIS);

					if (!lista.isEmpty() && lista.get(1).intValue() > 0) {
						respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_VEND_RUTA_TIENEINV_58, null,
								nombreClase, nombreMetodo, null, input.getCodArea());
						output = new OutputRuta();
						output.setRespuesta(respuesta);

						return output;
					}
				}

				// Existencia de vendedor en ruta
				condicionesExistencia.clear();
				condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_NOT_IN_AND, Ruta.CAMPO_TC_SC_RUTA_ID,
						input.getIdRuta()));
				condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						Ruta.CAMPO_SEC_USUARIO_ID, input.getSecUsuarioId()));
				condicionesExistencia
						.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Ruta.CAMPO_ESTADO, estadoAlta));
				condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						Ruta.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
				existencia = UtileriasBD.verificarExistencia(conn, Ruta.N_TABLA, condicionesExistencia);
				if (new Integer(existencia) > 0) {
					log.error("El ID de Usuario ya ha sido registrado en otra ruta.");
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_VENDEDOR_RUTA_840, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					output = new OutputRuta();
					output.setRespuesta(respuesta);

					return output;
				}

				// Existencia de vendedor en panel
				condicionesExistencia.clear();
				String tipoPanel = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_PANEL,
						input.getCodArea());
				condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						VendedorPDV.CAMPO_VENDEDOR, input.getSecUsuarioId()));
				condicionesExistencia.add(
						UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorPDV.CAMPO_TIPO, tipoPanel));
				condicionesExistencia.add(
						UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorPDV.CAMPO_ESTADO, estadoAlta));
				condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						VendedorPDV.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
				existencia = UtileriasBD.verificarExistencia(conn, VendedorPDV.N_TABLA, condicionesExistencia);
				if (new Integer(existencia) > 0) {
					log.error("El ID de Usuario ya ha sido registrado en una panel.");
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_VENDEDOR_PANEL_841, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					output = new OutputRuta();
					output.setRespuesta(respuesta);

					return output;
				}

			} else if (metodo == Conf.METODO_DELETE) {
				List<BigDecimal> lista = new ArrayList<BigDecimal>();
				lista = getInvRuta(conn, input.getIdRuta(), ID_PAIS);

				if (!lista.isEmpty() && lista.get(0).intValue() > 0) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RUTA_TIENEINV_57, null, nombreClase,
							nombreMetodo, null, input.getCodArea());
					output = new OutputRuta();
					output.setRespuesta(respuesta);
					return output;
				} else if (!lista.isEmpty() && lista.get(1).intValue() > 0) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_VEND_RUTA_TIENEINV_58, null,
							nombreClase, nombreMetodo, null, input.getCodArea());
					output = new OutputRuta();
					output.setRespuesta(respuesta);

					return output;
				}
			}
		}

		String campos[][] = CtrlRuta.obtenerCamposPutDel(input, metodo);
		List<Filtro> condiciones = CtrlRuta.obtenerCondiciones(input, metodo, ID_PAIS);

		String sql = UtileriasBD.armarQueryUpdate(Ruta.N_TABLA, campos, condiciones);

		try {
			int i = 0;
			conn.setAutoCommit(false);
			QueryRunner Qr = new QueryRunner();
			i = Qr.update(conn, sql);
			log.trace("objetos actualizados:" + i);
			if (i > 0) {

				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_MOD_RUTA_55, null, nombreClase,
						nombreMetodo, null, input.getCodArea());
			} else {
				respuesta = new Respuesta();
				respuesta.setCodResultado("0");

			}

			conn.commit();

		} finally {
			conn.setAutoCommit(true);

			output = new OutputRuta();
			output.setRespuesta(respuesta);
		}

		return output;
	}

	/**
	 * M\u00E9todo para obtener el vendedor actual de una ruta
	 * 
	 * @throws SQLException
	 */
	public static BigDecimal getidVendedorRuta(Connection conn, BigDecimal ruta, BigDecimal ID_PAIS)
			throws SQLException {
		BigDecimal vendedor = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = "SELECT SECUSUARIOID FROM TC_SC_RUTA WHERE TCSCCATPAISID = ?" + " AND TCSCRUTAID = ?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, ID_PAIS);
			pstmt.setBigDecimal(2, ruta);
			rst = pstmt.executeQuery();
			log.trace("Obteniendo id de vendedor de ruta: " + query);

			if (rst.next()) {
				vendedor = rst.getBigDecimal(1);
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}

		return vendedor;
	}

	/**
	 * M\u00E9todo para obtener la cantidad de inventario disponible en una ruta
	 */
	public static List<BigDecimal> getInvRuta(Connection conn, String id, BigDecimal ID_PAIS) throws SQLException {
		List<BigDecimal> lstRuta = new ArrayList<BigDecimal>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = "SELECT 0 INV_BODEGAVIRTUAL, " + " (SELECT COUNT (*)  " + " FROM TC_SC_INVENTARIO "
				+ " WHERE TCSCCATPAISID = ?" + " AND upper(ESTADO) NOT IN ('VENDIDO') "
				+ " AND IDVENDEDOR IN (SELECT SECUSUARIOID " + " FROM TC_SC_RUTA " + " WHERE TCSCCATPAISID = ?"
				+ " AND TCSCRUTAID =? )) INV_VENDEDOR FROM DUAL";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, ID_PAIS);
			pstmt.setBigDecimal(2, ID_PAIS);
			pstmt.setBigDecimal(3, new BigDecimal(id));
			rst = pstmt.executeQuery();
			log.trace("Verificando si la ruta tiene inventario: " + query);

			if (rst.next()) {
				lstRuta.add(0, rst.getBigDecimal(1));
				lstRuta.add(1, rst.getBigDecimal(2));
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}

		return lstRuta;
	}
}
