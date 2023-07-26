package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.dispositivo.InputDispositivo;
import com.consystec.sc.ca.ws.input.jornada.InputJornada;
import com.consystec.sc.ca.ws.input.jornada.InputObservacionesJornada;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.venta.DetallePago;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.jornada.OutputJornada;
import com.consystec.sc.sv.ws.metodos.CtrlJornadaMasiva;
import com.consystec.sc.sv.ws.metodos.CtrlSaldoPayment;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.DestinoAlarma;
import com.consystec.sc.sv.ws.orm.DiasFestivos;
import com.consystec.sc.sv.ws.orm.Dispositivo;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.FechaJornada;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.JornadaObservaciones;
import com.consystec.sc.sv.ws.orm.Panel;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.orm.Sincronizacion;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.orm.VendedorPDV;
import com.consystec.sc.sv.ws.orm.Venta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.SendMail;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class OperacionJornadaMasiva extends ControladorBase {
	private static final Logger log = Logger.getLogger(OperacionJornadaMasiva.class);

	/**
	 * Funci\u00F3n que realiza las operaciones al consultar en m\u00E9todo GET.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @param estadoAlta
	 * @return OutputJornada
	 * @throws SQLException
	 */
	public static OutputJornada doGet(Connection conn, InputJornada input, int metodo, String estadoAlta,
			BigDecimal idPais) throws SQLException {
		String nombreMetodo = "doGet";
		String nombreClase = new CurrentClassGetter().getClassName();
		List<InputJornada> list = new ArrayList<InputJornada>();
		Respuesta respuesta = null;
		OutputJornada output = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String sql = null;
		List<Order> orden = new ArrayList<Order>();

		try {
			orden.add(new Order(Jornada.N_TABLA + "." + Jornada.CAMPO_CREADO_EL, Order.DESC));

			String bancosEfectivo = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.BANCOS_EFECTIVO,
					input.getCodArea());
			String estadoAnulado = UtileriasJava.getConfig(conn, Conf.ESTADOS_ANULADOS, Conf.ESTADOS_ANULADOS,
					input.getCodArea());

			String[][] campos = CtrlJornadaMasiva.obtenerCamposGet(conn, estadoAnulado, input.getCodArea(), idPais);
			String tablas[] = { getParticion(Jornada.N_TABLA, Conf.PARTITION, "", input.getCodArea()),
					getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", input.getCodArea()), Distribuidor.N_TABLA,
					getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, "", input.getCodArea()) };

			List<Filtro> condiciones = CtrlJornadaMasiva.obtenerCondiciones(input, metodo, idPais);
			condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.N_TABLA,
					BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, Jornada.N_TABLA, Jornada.CAMPO_TCSCBODEGAVIRTUAL));
			condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.N_TABLA,
					Distribuidor.CAMPO_TC_SC_DTS_ID, Jornada.N_TABLA, Jornada.CAMPO_TCSCDTSID));
			condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.N_TABLA,
					VendedorDTS.CAMPO_VENDEDOR, Jornada.N_TABLA, Jornada.CAMPO_VENDEDOR));

			sql = UtileriasBD.armarQueryGetMultiple(campos, tablas, condiciones, orden);
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst != null) {
				if (!rst.next()) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTEN_JORNADAS_747, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					output = new OutputJornada();
					output.setRespuesta(respuesta);
				} else {
					respuesta = new Respuesta();
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					do {
						InputJornada item = new InputJornada();
						String idJornada = rst.getString(Jornada.CAMPO_TCSCJORNADAVENID);
						item.setIdJornada(idJornada);
						item.setIdJornadaResponsable(
								UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Jornada.CAMPO_JORNADA_RESPONSABLE));
						item.setCodDispositivo(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_COD_DISPOSITIVO));
						item.setIdVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Jornada.CAMPO_VENDEDOR));
						item.setNombreVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBREVENDEDOR"));
						item.setUsuarioVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "USUARIOVENDEDOR"));
						item.setIdDistribuidor(
								UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Jornada.CAMPO_TCSCDTSID));
						item.setNombreDistribuidor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBREDTS"));
						item.setFecha(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Jornada.CAMPO_FECHA));
						item.setIdTipo(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Jornada.CAMPO_IDTIPO));
						item.setTipo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_DESCRIPCION_TIPO));
						item.setNombreTipo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRETIPO"));
						item.setIdBodegaVirtual(
								UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Jornada.CAMPO_TCSCBODEGAVIRTUAL));
						item.setNombreBodegaVirtual(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBREBODEGA"));
						item.setIdBodegaVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "BODEGAVENDEDOR"));
						item.setEstado(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_ESTADO));
						item.setObservaciones(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_OBSERVACIONES));
						item.setFechaFinalizacion(
								UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Jornada.CAMPO_FECHA_FINALIZACION));
						item.setEstadoLiquidacion(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_ESTADO_LIQUIDACION));
						item.setFechaLiquidacion(
								UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Jornada.CAMPO_FECHA_LIQUIDACION));
						item.setCodDispositivoFinalizacion(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_COD_DISPOSITIVO_FIN));
						item.setObsLiquidacion(getObservacionesLiquidacion(conn, idJornada));
						item.setEnvioAlarma(
								UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Jornada.CAMPO_ENVIO_ALARMA));
						item.setTipoAlarma(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_TIPO_ALARMA));
						item.setEnvioAlarmaFin(
								UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Jornada.CAMPO_ENVIO_ALARMA_FIN));
						item.setTipoAlarmaFin(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_TIPO_ALARMA_FIN));
						item.setSaldoInicial(
								UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Jornada.CAMPO_SALDO_PAYMENT));
						item.setDetallePagos(getDetallePagos(conn, idJornada, estadoAlta, estadoAnulado, bancosEfectivo,
								input.getCodArea(), idPais));
						item.setIdDeuda(
								UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Jornada.CAMPO_SOLICITUD_PAGO));
						item.setEstadoPago(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_ESTADO_PAGO));
						item.setCreado_el(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Jornada.CAMPO_CREADO_EL));
						item.setCreado_por(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_CREADO_POR));
						item.setModificado_el(
								UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Jornada.CAMPO_MODIFICADO_EL));
						item.setModificado_por(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_MODIFICADO_POR));
						item.setIdBodegaPanelRuta(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "id_bodega_panelruta"));
						item.setNombreBodegaPanelRuta(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "nombre_bodega_panelruta"));
						list.add(item);
					} while (rst.next());

					output = new OutputJornada();
					output.setRespuesta(respuesta);
					output.setJornadas(list);
				}
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return output;
	}

	private static List<DetallePago> getDetallePagos(Connection conn, String idJornada, String estadoAlta,
			String estadoAnulado, String bancosEfectivo, String codArea, BigDecimal idPais) throws SQLException {
		List<DetallePago> list = null;

		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String sql = queryDetallePago(idJornada, estadoAlta, estadoAnulado, bancosEfectivo, codArea, idPais);

		log.debug("Qry consulta pagos venta: " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				list = new ArrayList<DetallePago>();
				do {
					DetallePago item = new DetallePago();
					item.setFormaPago(rst.getString("FORMAPAGO"));
					item.setMonto(rst.getString("TOTAL"));
					if (!item.getMonto().equals("0")) {
						list.add(item);
					}

				} while (rst.next());
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return list;
	}

	public static String queryDetallePago(String idJornada, String estadoAlta, String estadoAnulado,
			String bancosEfectivo, String codArea, BigDecimal ID_PAIS) {
		String sql = "SELECT UPPER(D.FORMAPAGO) FORMAPAGO, NVL(SUM(ROUND(D.MONTO * V.TASA_CAMBIO , 2)), 0) TOTAL"
				+ " FROM " + getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea) + " V, TC_SC_DET_PAGO D"
				+ " WHERE V.TCSCJORNADAVENID = " + idJornada + " AND UPPER(V.ESTADO) NOT IN (" + estadoAnulado
				+ ") AND V.TCSCCATPAISID = " + ID_PAIS + " AND V.TCSCVENTAID = D.TCSCVENTAID GROUP BY FORMAPAGO"
				+ " UNION" + " SELECT 'CAJA', NVL(SUM(ROUND(R.MONTO * R.TASA_CAMBIO , 2)), 0) TOTAL FROM TC_SC_REMESA R"
				+ " WHERE R.TCSCCATPAISID = " + ID_PAIS + " AND UPPER(R.ESTADO) = '" + estadoAlta + "'"
				+ " AND R.BANCO IN (" + bancosEfectivo + ") AND R.TCSCJORNADAVENID = " + idJornada + " UNION"
				+ " SELECT 'REMESAS', NVL(SUM(ROUND(R.MONTO * R.TASA_CAMBIO , 2)), 0) TOTAL FROM TC_SC_REMESA R"
				+ " WHERE R.TCSCCATPAISID = " + ID_PAIS + " AND UPPER(R.ESTADO) = '" + estadoAlta + "'"
				+ " AND R.BANCO NOT IN (" + bancosEfectivo + ") AND R.TCSCJORNADAVENID = " + idJornada;

		return sql;
	}

	private static List<InputObservacionesJornada> getObservacionesLiquidacion(Connection conn, String idJornada)
			throws SQLException {
		List<InputObservacionesJornada> list = null;

		PreparedStatement pstmt = null;
		ResultSet rst = null;

		try {
			String[] campos = { JornadaObservaciones.CAMPO_OBSERVACION, JornadaObservaciones.CAMPO_CREADO_EL,
					JornadaObservaciones.CAMPO_CREADO_POR };

			List<Filtro> condiciones = new ArrayList<Filtro>();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					JornadaObservaciones.CAMPO_TCSCJORNADAID, idJornada));
			String sql = UtileriasBD.armarQuerySelect(JornadaObservaciones.N_TABLA, campos, condiciones);

			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				list = new ArrayList<InputObservacionesJornada>();
				do {
					InputObservacionesJornada item = new InputObservacionesJornada();
					item.setObservacion(rst.getString(JornadaObservaciones.CAMPO_OBSERVACION));
					item.setCreado_el(
							UtileriasJava.formatStringDate(rst.getString(JornadaObservaciones.CAMPO_CREADO_EL)));
					item.setCreado_por(rst.getString(JornadaObservaciones.CAMPO_CREADO_POR));

					list.add(item);
				} while (rst.next());
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return list;
	}

	/**
	 * Funci\u00F3n que realiza las operaciones al trabajar en m\u00E9todo POST.
	 * 
	 * @param conn
	 * @param input
	 * @param tipoPanel
	 * @param tipoRuta
	 * @param estadoAlta
	 * @param estadoIniciada
	 * @param estadoFinalizada
	 * @param estadoLiquidada
	 * @param metodo
	 * @param estadoProcesoSiniestro
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static OutputJornada doPost(Connection conn, InputJornada input, String tipoPanel, String tipoRuta,
			String estadoAlta, String estadoIniciada, String estadoFinalizada, String estadoLiquidada, int metodo,
			String estadoProcesoSiniestro, BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "doPost";
		String nombreClase = new CurrentClassGetter().getClassName();
		String idTipo = "";
		String tipo = "";
		String idResponsable = null;
		String idJornada = null;
		List<InputJornada> jornadas = new ArrayList<InputJornada>();
		InputJornada jornada = new InputJornada();
		String fechaCierre = "";
		List<String> lstIdTipos;
		Respuesta respuesta = new Respuesta();
		OutputJornada output = new OutputJornada();
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		Statement stmtInserts = null;
		SimpleDateFormat FORMATO_FECHA_GT = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA_GT);
		SimpleDateFormat FORMATO_TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			conn.setAutoCommit(false);
			lstIdTipos = getIdPanelRuta(conn, input, estadoAlta, tipoPanel, tipoRuta, ID_PAIS);

			if (!lstIdTipos.isEmpty()) {
				idTipo = lstIdTipos.get(0);
				tipo = lstIdTipos.get(1);
			}

			if (!tipo.equalsIgnoreCase(input.getTipo())) {
				// el tipo enviado y el tipo del responsable no coinciden
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_TIPOS_JORNADA_737, null,
						nombreClase, nombreMetodo, null, input.getCodArea());
				return output;
			}

			if (idTipo == null || idTipo.equals("") || tipo == null || tipo.equals("")) {
				// responsable no tiene ningun tipo asociado
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_RUTA_PANEL_RESPONSABLE_736, null,
						nombreClase, nombreMetodo, null, input.getCodArea());
				return output;
			} else {
				// validaciones de jornadas existentes
				output = validarJornadasExistentes(conn, Conf.METODO_POST, input.getIdResponsable(), estadoFinalizada,
						estadoLiquidada, null, null, input.getCodArea(), ID_PAIS);
				if (output != null) {
					respuesta = output.getRespuesta();
					return output;
				} else {
					output = new OutputJornada();
				}

				// validaciones de dts, bodega, dispositivo y responsable
				sql = "SELECT (SELECT COUNT(1) FROM TC_SC_DTS WHERE tcscdtsid = ?"
						+ " AND TCSCCATPAISID = ? AND UPPER(estado) =?) AS DTS,"
						+ " (SELECT COUNT(1) FROM TC_SC_BODEGA_VENDEDOR " + " WHERE tcscbodegavirtualid = ?"
						+ " AND vendedor = ?) AS BODRESP," + " (SELECT COUNT(1) FROM "
						+ getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", input.getCodArea())
						+ " WHERE TCSCBODEGAVIRTUALID = ?" + " AND TCSCCATPAISID =? AND UPPER(ESTADO) = ?) AS BOD,"
						+ " (SELECT COUNT(1) FROM TC_SC_DISPOSITIVO" + " WHERE UPPER(codigo_dispositivo) = ?"
						+ " AND TCSCCATPAISID =?  AND UPPER(estado) = ?" + " AND responsable = ?"
						+ " AND UPPER(tipo_responsable) =?) AS DISP," + " (SELECT vendedor FROM TC_SC_VEND_PANELPDV"
						+ " WHERE idtipo  = ?" + " AND TCSCCATPAISID = ? AND UPPER(tipo) = ?" + " AND UPPER(estado) = ?"
						+ " AND responsable = 1) AS VEND" + " FROM DUAL";

				log.debug("Qry validaciones: " + sql);
				try (PreparedStatement pstmt1 = conn.prepareStatement(sql);) {

					pstmt1.setBigDecimal(1, new BigDecimal(input.getIdDistribuidor()));
					pstmt1.setBigDecimal(2, ID_PAIS);
					pstmt1.setString(3, estadoAlta);
					pstmt1.setBigDecimal(4, new BigDecimal(input.getIdBodegaVendedor()));
					pstmt1.setBigDecimal(5, new BigDecimal(input.getIdResponsable()));
					pstmt1.setBigDecimal(6, new BigDecimal(input.getIdBodegaVendedor()));
					pstmt1.setBigDecimal(7, ID_PAIS);
					pstmt1.setString(8, estadoAlta);
					pstmt1.setString(9, input.getCodDispositivo().toUpperCase());
					pstmt1.setBigDecimal(10, ID_PAIS);
					pstmt1.setString(11, estadoAlta);
					pstmt1.setString(12, idTipo);
					pstmt1.setString(13, tipo.toUpperCase());
					pstmt1.setString(14, idTipo);
					pstmt1.setBigDecimal(15, ID_PAIS);
					pstmt1.setString(16, tipoPanel.toUpperCase());
					pstmt1.setString(17, estadoAlta);

					try (ResultSet rst1 = pstmt1.executeQuery()) {
						if (rst1.next()) {
							idResponsable = rst1.getBigDecimal("VEND") != null ? rst1.getBigDecimal("VEND").toString()
									: "";

							if (rst1.getInt("DTS") != 1) {
								// el distribuidor no existe
								respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_IDDTS_NO_EXISTE_173,
										null, nombreClase, nombreMetodo, null, input.getCodArea());
								return output;

							} else if (rst1.getInt("BODRESP") != 1) {
								// la bodega no esta asociada al vendedor responsable
								respuesta = new ControladorBase().getMensaje(
										Conf_Mensajes.MSJ_ERROR_BODEGA_RESPONSABLE_765, null, nombreClase, nombreMetodo,
										null, input.getCodArea());
								return output;

							} else if (rst1.getInt("BOD") != 1) {
								// error de bodega
								respuesta = new ControladorBase().getMensaje(
										Conf_Mensajes.MSJ_IDBODVIRTUAL_NO_EXISTE_172, null, nombreClase, nombreMetodo,
										null, input.getCodArea());
								return output;

							} else if (rst1.getInt("DISP") != 1) {
								// error de disp
								respuesta = new ControladorBase().getMensaje(
										Conf_Mensajes.MSJ_ERROR_NO_EXISTE_DISPOSITIVO_239, null, nombreClase,
										nombreMetodo, null, input.getCodArea());
								return output;

							} else if (tipo.equalsIgnoreCase(tipoPanel)
									&& !idResponsable.equals(input.getIdResponsable())) {
								respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_RESPONSABLE_739,
										null, nombreClase, nombreMetodo, null, input.getCodArea());
								return output;
							}
						}
					}

				}
			}

			// validacion de vendedores cuando es tipo panel
			if (tipo.equalsIgnoreCase(tipoPanel) && input.getVendedores() != null && input.getVendedores().size() > 0) {
				String vendedores = "";

				for (int i = 0; i < input.getVendedores().size(); i++) {
					vendedores += input.getVendedores().get(i).getIdVendedor();
					if (i != input.getVendedores().size() - 1)
						vendedores += ",";
				}

				sql = "SELECT TO_NUMBER(COLUMN_VALUE) AS VEND " + "FROM TABLE(SYS.DBMS_DEBUG_VC2COLL(" + vendedores
						+ ")) " + "MINUS " + "SELECT vendedor" + " FROM TC_SC_VEND_PANELPDV" + " WHERE idtipo = ?"
						+ " AND TCSCCATPAISID = ? AND UPPER(tipo) = ?" + " AND UPPER(estado) =?";

				log.debug("Qry vendedores: " + sql);
				try {
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, idTipo);
					pstmt.setBigDecimal(2, ID_PAIS);
					pstmt.setString(3, tipoPanel.toUpperCase());
					pstmt.setString(4, estadoAlta);
					rst = pstmt.executeQuery();
					vendedores = "";

					if (rst.next()) {
						do {
							vendedores += getNombreVendedor(conn, rst.getString("VEND"), ID_PAIS) + ", ";
						} while (rst.next());
					}
				} finally {
					DbUtils.closeQuietly(rst);
					DbUtils.closeQuietly(pstmt);
				}

				if (!vendedores.equals("")) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_VENDEDORES_PANEL_738, null,
							nombreClase, nombreMetodo, vendedores.substring(0, vendedores.length() - 2),
							input.getCodArea());
					return output;
				}
			}

			// se inserta la jornada del responsable \u00FAnicamente
			String campos[] = CtrlJornadaMasiva.obtenerCamposPost();
			List<String> inserts = CtrlJornadaMasiva.obtenerInsertsPost(input, Jornada.SEQUENCE, idTipo, null,
					input.getIdResponsable(), estadoIniciada, ID_PAIS);
			idJornada = insertJornada(conn, campos, inserts);

			if (idJornada != null || !"".equals(idJornada)) {
				log.debug("idJornadaResponsable: " + idJornada);

				// agregado fecha de cierre al iniciar jornada
				// se inserta la fecha de cierre
				String diasFechaCierre = UtileriasJava.getConfig(conn, Conf.GRUPO_FECHA_CIERRE_JORNADA, input.getTipo(),
						input.getCodArea());
				InputJornada inputFecha = new InputJornada();
				inputFecha.setIdVendedor(input.getIdResponsable());
				inputFecha.setUsuario(input.getUsuario());
				try {
					Calendar cal = Calendar.getInstance();

					Date fechaCierreJornada = FORMATO_TIMESTAMP.parse(input.getFecha()); // fecha de jornada
					cal.setTime(fechaCierreJornada);
					log.trace("Fecha jornada: " + fechaCierreJornada);
					cal.add(Calendar.DAY_OF_YEAR, new Integer(diasFechaCierre)); // se suman los dias configurados para
																					// el cierre
					fechaCierreJornada = FORMATO_FECHA_GT.parse(FORMATO_FECHA_GT.format(cal.getTime()));
					log.trace("Fecha cierre: " + fechaCierreJornada);
					inputFecha.setFecha(FORMATO_FECHA_GT.format(fechaCierreJornada));
				} catch (java.text.ParseException e) {
					log.error("Problema al convertir la fecha en clase " + nombreMetodo + " m\u00E9todo " + nombreClase
							+ ".", e);
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_CONVERTIR_FECHA_165, null,
							nombreClase, nombreMetodo, null, input.getCodArea());
					return output;
				}

				OutputJornada respuestaFecha = OperacionJornada.asignaFechaJornada(conn, inputFecha, input.getCodArea(),
						ID_PAIS);
				if (new BigDecimal(respuestaFecha.getRespuesta().getCodResultado())
						.intValue() != Conf_Mensajes.OK_FECHA_JORNADA_71) {
					respuesta = respuestaFecha.getRespuesta();
					return output;
				} else {
					fechaCierre = inputFecha.getFecha();
				}
				// agregado fecha de cierre al iniciar jornada

				if (tipo.equalsIgnoreCase(tipoPanel) && input.getVendedores() != null
						&& input.getVendedores().size() > 0) {
					/*
					 * se creo la jornada del responsable, se procede a insertar las jornadas con
					 * los datos de vendedores y de la jornada del responsable creada
					 */
					stmtInserts = conn.createStatement();
					for (int i = 0; i < input.getVendedores().size(); i++) {
						inserts = CtrlJornadaMasiva.obtenerInsertsPost(input, Jornada.SEQUENCE, idTipo, idJornada,
								input.getVendedores().get(i).getIdVendedor(), estadoIniciada, ID_PAIS);

						sql = UtileriasBD.armarQueryInsert(Jornada.N_TABLA, campos, inserts);

						stmtInserts.addBatch(sql);
					}

					int[] insertCounts = stmtInserts.executeBatch();

					if (UtileriasJava.validarBatch(1, insertCounts)) {
						// todo se inserto bien
						jornada.setIdJornada(idJornada);
						jornada.setIdVendedor(input.getIdResponsable());
						jornada.setSaldoInicial(new CtrlSaldoPayment().getSaldoPayment(conn, idJornada,
								input.getIdResponsable(), metodo, input.getCodArea()));
						jornadas.add(jornada);

						String camposJ[] = { Jornada.CAMPO_TCSCJORNADAVENID, Jornada.CAMPO_VENDEDOR };
						List<Filtro> condiciones = new ArrayList<Filtro>();
						condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
								Jornada.CAMPO_JORNADA_RESPONSABLE, idJornada));
						List<Map<String, String>> datosJornadas = UtileriasBD.getSingleData(conn,
								getParticion(Jornada.N_TABLA, Conf.PARTITION, "", input.getCodArea()), camposJ,
								condiciones, null);

						for (int i = 0; i < datosJornadas.size(); i++) {
							jornada = new InputJornada();
							jornada.setIdJornada(datosJornadas.get(i).get(Jornada.CAMPO_TCSCJORNADAVENID));
							jornada.setIdVendedor(datosJornadas.get(i).get(Jornada.CAMPO_VENDEDOR));
							jornada.setSaldoInicial(new CtrlSaldoPayment().getSaldoPayment(conn,
									datosJornadas.get(i).get(Jornada.CAMPO_TCSCJORNADAVENID),
									datosJornadas.get(i).get(Jornada.CAMPO_VENDEDOR), metodo, input.getCodArea()));
							jornadas.add(jornada);
						}

					} else {
						// fallo alguna insercion
						log.debug("Rollback");
						conn.rollback();
						respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null,
								nombreClase, nombreMetodo, null, input.getCodArea());
						return output;
					}

				} else {
					jornada.setIdJornada(idJornada);
					jornada.setIdVendedor(input.getIdResponsable());
					jornada.setSaldoInicial(new CtrlSaldoPayment().getSaldoPayment(conn, idJornada,
							input.getIdResponsable(), metodo, input.getCodArea()));
					jornadas.add(jornada);
				}

				output.setJornadas(jornadas);
				output.setFechaCierre(fechaCierre);

				// se realiza proceso de inventario inicial
				boolean invInicial = OperacionJornada.insertaInvJornada(conn, idJornada, input.getIdResponsable(),
						input.getIdBodegaVendedor(), input.getUsuario(), estadoProcesoSiniestro, input.getCodArea(),
						ID_PAIS);
				if (!invInicial) {

					int datos = 0;
					datos = getInvPrincipalRecarga(conn, input.getIdDistribuidor(), ID_PAIS);

					if (datos == 0) {
						log.debug("Rollback");
						conn.rollback();
						respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_PROCESO_INV_INICIAL_846,
								null, nombreClase, nombreMetodo, null, input.getCodArea());
						output = new OutputJornada();
						return output;
					}

					else {
						respuesta.setDescripcion("FAIL");
						respuesta.setExcepcion(
								"Ocurri\u00F3 un inconveniente al consultar recargas en inventrio par el id distribuidor "
										+ input.getIdDistribuidor());
					}

				}

				boolean alarma = verificarAlarmas(conn, input, estadoAlta, false, jornadas, null, ID_PAIS);
				if (!alarma) {
					log.debug("Rollback");
					conn.rollback();
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_VERIFICAR_ALARMAS_JORNADA_845,
							null, nombreClase, nombreMetodo, null, input.getCodArea());
					output = new OutputJornada();
					return output;
				}

				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_JORNADA_45, null, nombreClase,
						nombreMetodo, null, input.getCodArea());

				conn.commit();
			} else {
				log.debug("Rollback");
				conn.rollback();
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
						nombreMetodo, null, input.getCodArea());
			}

		} catch (Exception e) {
			log.debug("Rollback: " + e, e);
			conn.rollback();
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

		} finally {
			output.setRespuesta(respuesta);
			DbUtils.closeQuietly(stmtInserts);
			conn.setAutoCommit(true);

		}

		return output;
	}

	/**
	 * M\u00E9todo para obtener fecha de cierre de jornada de vendedor
	 * 
	 * @param conn
	 * @param vendedor
	 * @return
	 * @throws SQLException
	 */
	public static String getFechaCierre(Connection conn, String vendedor, String codArea, BigDecimal ID_PAIS)
			throws SQLException {
		String respuesta = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		StringBuilder query = new StringBuilder();

		query.append("SELECT TO_CHAR (fecha, 'ddMMyyyy') FROM ");
		query.append(ControladorBase.getParticion(FechaJornada.N_TABLA, Conf.PARTITION, "", codArea));
		query.append(" WHERE VENDEDOR =?  AND TCSCCATPAISID = ?");
		try {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setBigDecimal(1, new BigDecimal(vendedor));
			pstmt.setBigDecimal(2, ID_PAIS);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				respuesta = rst.getString(1);
			}

		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}

		return respuesta == null ? "" : respuesta;
	}

	/**
	 * Funcion que valida que no existan jornadas en estados incorrecto del
	 * responsable o dependientes.
	 * 
	 * @param conn
	 * @param metodo
	 * @param idResponsable
	 * @param estado
	 * @param estadoLiquidada
	 * @param jornadaResponsable
	 * @param tipoLiquidacion
	 * @return
	 * @throws SQLException
	 */
	private static OutputJornada validarJornadasExistentes(Connection conn, int metodo, String idResponsable,
			String estado, String estadoLiquidada, String jornadaResponsable, String tipoLiquidacion, String codArea,
			BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "validarJornadasExistentes";
		String nombreClase = new CurrentClassGetter().getClassName();
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String idJornada = jornadaResponsable != null && !jornadaResponsable.trim().equals("") ? jornadaResponsable
				: "SELECT " + Jornada.CAMPO_TCSCJORNADAVENID + " FROM "
						+ getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea) + " WHERE TCSCCATPAISID = "
						+ ID_PAIS + " AND " + Jornada.CAMPO_VENDEDOR + " = " + idResponsable;

		String sql = queryJornadasExistentes(metodo, estado, estadoLiquidada, tipoLiquidacion, idJornada, codArea);

		log.debug("Qry jornadas: " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				// alguna jornada de ese responsable no esta liquidada

				List<InputJornada> jornadas = new ArrayList<InputJornada>();
				do {
					InputJornada jornada = new InputJornada();
					jornada.setIdJornada(
							UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Jornada.CAMPO_TCSCJORNADAVENID));
					jornada.setIdVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Jornada.CAMPO_VENDEDOR));
					jornada.setEstado(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_ESTADO));
					jornada.setEstadoLiquidacion(
							UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_ESTADO_LIQUIDACION));

					jornadas.add(jornada);
				} while (rst.next());

				OutputJornada output = new OutputJornada();

				Respuesta respuesta = new ControladorBase().getMensaje(
						Conf_Mensajes.MSJ_ERROR_JORNADAS_SIN_LIQUIDAR_741, null, nombreClase, nombreMetodo, null,
						codArea);
				output.setJornadas(jornadas);
				output.setRespuesta(respuesta);

				return output;
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}

		return null;
	}

	public static String queryJornadasExistentes(int metodo, String estado, String estadoLiquidada,
			String tipoLiquidacion, String idJornada, String codArea) {
		String sql = "SELECT " + Jornada.CAMPO_TCSCJORNADAVENID + ", " + Jornada.CAMPO_VENDEDOR + ", "
				+ Jornada.CAMPO_ESTADO + ", " + Jornada.CAMPO_ESTADO_LIQUIDACION + " FROM "
				+ getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea) + " WHERE (" + "UPPER("
				+ Jornada.CAMPO_ESTADO + ") != '" + estado.toUpperCase() + "'";
		if (metodo == Conf.METODO_PUT && tipoLiquidacion != null && !tipoLiquidacion.equals("")) {
			sql += " OR UPPER(" + Jornada.CAMPO_ESTADO_LIQUIDACION + ") = '" + estadoLiquidada.toUpperCase() + "'"
					+ " OR " + Jornada.CAMPO_ESTADO_LIQUIDACION + " IS NULL";
		}
		if (metodo == Conf.METODO_POST) {
			sql += " OR " + Jornada.CAMPO_ESTADO + " IS NULL" + " OR UPPER(" + Jornada.CAMPO_ESTADO_LIQUIDACION
					+ ") != '" + estadoLiquidada.toUpperCase() + "'" + " OR " + Jornada.CAMPO_ESTADO_LIQUIDACION
					+ " IS NULL";
		}
		sql += ")" + " AND (" + Jornada.CAMPO_TCSCJORNADAVENID + " IN (" + idJornada + ")" + " OR "
				+ Jornada.CAMPO_JORNADA_RESPONSABLE + " IN (" + idJornada + "))";

		return sql;
	}

	/**
	 * Funci\u00F3n que realiza las operaciones al trabajar en m\u00E9todo PUT.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @param listaLog
	 * @param servicioPut
	 * @param logTransaccionModJornada
	 * @param estadoLiquidada
	 * @param estadoRechazada
	 * @param estadoPendiente
	 * @param estadoFinalizada
	 * @param estadoIniciada
	 * @param estadoAlta
	 * @param tipoRechazo
	 * @param tipoLiquidacion
	 * @param tipoFinJornada
	 * @param tipoRuta
	 * @param tipoPanel
	 * @param siniestro
	 * @param estadoSiniestrado
	 * @param diferencia
	 * @return
	 * @throws SQLException
	 */
	public static OutputJornada doPut(Connection conn, InputJornada input, int metodo, List<LogSidra> listaLog,
			String logTransaccionModJornada, String servicioPut, String tipoFinJornada, String estadoAlta,
			String estadoIniciada, String estadoFinalizada, String estadoPendiente, String tipoPanel, boolean siniestro,
			BigDecimal ID_PAIS) throws SQLException, Exception {
		String nombreMetodo = "doPut";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta respuesta = null;
		OutputJornada output = new OutputJornada();
		Statement stmtUpdate = null;
		Statement stmtInsert = null;
		String sql = null;
		boolean update = false;

		try {
			stmtUpdate = conn.createStatement();
			stmtInsert = conn.createStatement();
			conn.setAutoCommit(false);
			List<Filtro> condiciones = new ArrayList<Filtro>();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCJORNADAVENID,
					input.getIdJornada()));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCCATPAISID,
					ID_PAIS.toString()));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_VENDEDOR,
					input.getIdResponsable()));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCDTSID,
					input.getIdDistribuidor()));

			String[] campos = { Jornada.CAMPO_TCSCJORNADAVENID, Jornada.CAMPO_IDTIPO, Jornada.CAMPO_DESCRIPCION_TIPO,
					Jornada.CAMPO_ESTADO, Jornada.CAMPO_ESTADO_LIQUIDACION, Jornada.CAMPO_FECHA,
					Jornada.CAMPO_TCSCDTSID };

			// se obtienen los datos de la jornada
			Map<String, String> datosJornada = UtileriasBD.getSingleFirstData(conn,
					getParticion(Jornada.N_TABLA, Conf.PARTITION, "", input.getCodArea()), campos, condiciones);

			if (datosJornada == null || datosJornada.isEmpty()) {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTEN_JORNADAS_747, null,
						nombreClase, nombreMetodo, null, input.getCodArea());
				return output;

			} else {
				// se verifica que el responsable y la jornada coincidan en panel o en ruta con
				// el vendedor
				String responsable = "";
				condiciones.clear();
				if (datosJornada.get(Jornada.CAMPO_DESCRIPCION_TIPO).equalsIgnoreCase(tipoPanel)) {
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
							VendedorPDV.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorPDV.CAMPO_IDTIPO,
							datosJornada.get(Jornada.CAMPO_IDTIPO)));
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorPDV.CAMPO_VENDEDOR,
							input.getIdResponsable()));
					condiciones.add(
							UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorPDV.CAMPO_TIPO, tipoPanel));
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorPDV.CAMPO_ESTADO,
							estadoAlta));
					responsable = UtileriasBD.getOneRecord(conn, VendedorPDV.CAMPO_RESPONSABLE, VendedorPDV.N_TABLA,
							condiciones);

					if (responsable == null || !responsable.equals("1")) {
						respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_RESPONSABLE_JORNADA_746,
								null, nombreClase, nombreMetodo, null, input.getCodArea());
						return output;
					}

				} else {
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TCSCCATPAISID,
							ID_PAIS.toString()));
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TC_SC_RUTA_ID,
							datosJornada.get(Jornada.CAMPO_IDTIPO)));
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_SEC_USUARIO_ID,
							input.getIdResponsable()));
					condiciones.add(
							UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Ruta.CAMPO_ESTADO, estadoAlta));
					responsable = UtileriasBD.getOneRecord(conn, Ruta.CAMPO_SEC_USUARIO_ID, Ruta.N_TABLA, condiciones);

					if (responsable == null) {
						respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_RESPONSABLE_JORNADA_746,
								null, nombreClase, nombreMetodo, null, input.getCodArea());
						return output;
					}
				}
			}

			// se establecen las jornadas que se van a actualizar
			condiciones.clear();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCCATPAISID,
					ID_PAIS.toString()));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_OR, Jornada.CAMPO_TCSCJORNADAVENID,
					input.getIdJornada()));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_OR, Jornada.CAMPO_JORNADA_RESPONSABLE,
					input.getIdJornada()));
			String[] camposJornada = { Jornada.CAMPO_TCSCJORNADAVENID, Jornada.CAMPO_VENDEDOR };
			List<Map<String, String>> lstDatosJornadas = UtileriasBD.getSingleData(conn,
					getParticion(Jornada.N_TABLA, Conf.PARTITION, "", input.getCodArea()), camposJornada, condiciones,
					null);

			// procesos para finalizar jornada
			log.debug("la jornada se va a finalizar");

			// se valida que todas las jornadas dependientes esten correctamente
			output = validarJornadasExistentes(conn, Conf.METODO_PUT, input.getIdResponsable(), estadoIniciada, null,
					input.getIdJornada(), null, input.getCodArea(), ID_PAIS);
			if (output != null) {
				respuesta = output.getRespuesta();
				return output;
			} else {
				output = new OutputJornada();
			}

			if (!datosJornada.get(Jornada.CAMPO_ESTADO).equalsIgnoreCase(estadoIniciada)) {
				// la jornada no esta en un estado correcto
				log.debug("la jornada no se encuentra iniciada");
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_ESTADO_JORNADA_368, null,
						nombreClase, nombreMetodo, estadoIniciada + ".", input.getCodArea());
			} else {

				// se actualizan datos de jornada
				String observaciones = null;
				if (input.getObservaciones().trim().equalsIgnoreCase("")) {
					observaciones = Jornada.CAMPO_OBSERVACIONES;
				} else {
					observaciones = Jornada.CAMPO_OBSERVACIONES + " || ' " + tipoFinJornada + ": ' || "
							+ UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getObservaciones());
				}

				String camposUpd[][] = {
						{ Jornada.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estadoFinalizada) },
						{ Jornada.CAMPO_OBSERVACIONES, observaciones },
						{ Jornada.CAMPO_COD_DISPOSITIVO_FIN,
								UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getCodDispositivo()) },
						{ Jornada.CAMPO_FECHA_FINALIZACION, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
						{ Jornada.CAMPO_ESTADO_LIQUIDACION,
								UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estadoPendiente) },
						{ Jornada.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
						{ Jornada.CAMPO_MODIFICADO_POR,
								UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) },
						{ Jornada.CAMPO_SALDO_PAYMENT_FINAL, "0" }, };

				for (int i = 0; i < lstDatosJornadas.size(); i++) {
					// se establece el saldo final a insertar por vendedor
					camposUpd[7][1] = UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
							new CtrlSaldoPayment().getSaldoPayment(conn,
									lstDatosJornadas.get(i).get(Jornada.CAMPO_TCSCJORNADAVENID),
									lstDatosJornadas.get(i).get(Jornada.CAMPO_VENDEDOR), metodo, input.getCodArea()));

					condiciones.clear();
					condiciones
							.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_OR, Jornada.CAMPO_TCSCJORNADAVENID,
									lstDatosJornadas.get(i).get(Jornada.CAMPO_TCSCJORNADAVENID)));
					sql = UtileriasBD.armarQueryUpdate(
							getParticion(Jornada.N_TABLA, Conf.PARTITION, "", input.getCodArea()), camposUpd,
							condiciones);
					stmtUpdate.addBatch(sql);

					// log
					listaLog.add(addLog(logTransaccionModJornada, servicioPut,
							lstDatosJornadas.get(i).get(Jornada.CAMPO_TCSCJORNADAVENID), Conf.LOG_TIPO_JORNADA,
							"Se cambi\u00F3 el estado de " + datosJornada.get(Jornada.CAMPO_ESTADO) + " a "
									+ estadoFinalizada + ".",
							""));
				}
			}

			// se realizan los procesos de actualizaci\u00F3n de registros de inventario
			// inicial
			boolean actualizarInvJornada = OperacionJornada.actualizarInvJornada(conn, input.getIdJornada(),
					input.getIdResponsable(), input.getUsuario(), input.getCodArea(), ID_PAIS);
			if (!actualizarInvJornada) {

				int datos = 0;
				datos = getInvPrincipalRecarga(conn, input.getIdDistribuidor(), ID_PAIS);
				if (datos == 0) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null,
							nombreClase, nombreMetodo, "Inconveniente con el inventario inicial.", input.getCodArea());
					return output;
				}
			}

			// se realizan las operaciones de la jornada
			int[] updateCounts = stmtUpdate.executeBatch();
			int[] insertCounts = stmtInsert.executeBatch();
			DbUtils.closeQuietly(stmtUpdate);
			DbUtils.closeQuietly(stmtInsert);

			update = UtileriasJava.validarBatch(1, updateCounts) && UtileriasJava.validarBatch(1, insertCounts);

			if (update) {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_MOD_JORNADA_46, null, nombreClase,
						nombreMetodo, null, input.getCodArea());

				// se borra la fecha de cierre de jornada
				boolean borraFecha = OperacionJornada.borrarFechaCierre(conn, input.getIdResponsable(),
						input.getUsuario(), input.getCodArea(), ID_PAIS);
				if (!borraFecha) {
					log.debug("Rollback");
					conn.rollback();
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null,
							nombreClase, nombreMetodo, "Inconveniente con la fecha de cierre.", input.getCodArea());
					return output;
				}

				if (!siniestro) {
					// se verifican alarmas de jornada
					boolean alarma = verificarAlarmas(conn, input, estadoAlta, true, null, lstDatosJornadas, ID_PAIS);
					if (!alarma) {
						log.debug("Rollback");
						conn.rollback();
						respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null,
								nombreClase, nombreMetodo, "Inconveniente con las alarmas.", input.getCodArea());
						return output;
					}

					conn.commit();
				}
			} else {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null, nombreClase,
						nombreMetodo, null, input.getCodArea());
			}

		} finally {
			DbUtils.closeQuietly(stmtInsert);
			DbUtils.closeQuietly(stmtUpdate);
			if (!siniestro) {
				conn.setAutoCommit(true);
			}

			if (!update) {
				log.debug("Rollback");
				conn.rollback();
			}

			output.setRespuesta(respuesta);
		}

		return output;
	}

	public static OutputJornada validarSincronizaciones(Connection conn, InputJornada input,
			Map<String, String> datosJornada, String tipoPanel, String estadoAlta, String estadoProcesoSiniestro,
			String estadoSiniestrado, String diferencia, BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "validarSincronizaciones";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta respuesta = null;
		OutputJornada output = new OutputJornada();
		List<Filtro> condiciones = new ArrayList<Filtro>();
		List<InputJornada> jornadas = new ArrayList<InputJornada>();
		;
		List<InputDispositivo> dispUsados = new ArrayList<InputDispositivo>();
		// se obtienen todos los vendedores de la jornada
		String selectIdVendedores = "";

		if (datosJornada.get(Jornada.CAMPO_DESCRIPCION_TIPO).equalsIgnoreCase(tipoPanel)) {
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorPDV.CAMPO_TCSCCATPAISID,
					ID_PAIS.toString()));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorPDV.CAMPO_IDTIPO,
					datosJornada.get(Jornada.CAMPO_IDTIPO)));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorPDV.CAMPO_TIPO, tipoPanel));
			condiciones
					.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorPDV.CAMPO_ESTADO, estadoAlta));
			selectIdVendedores = UtileriasBD.armarQuerySelectField(VendedorPDV.N_TABLA, VendedorPDV.CAMPO_VENDEDOR,
					condiciones, null);
		} else {
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TCSCCATPAISID,
					ID_PAIS.toString()));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TC_SC_RUTA_ID,
					datosJornada.get(Jornada.CAMPO_IDTIPO)));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Ruta.CAMPO_ESTADO, estadoAlta));
			selectIdVendedores = UtileriasBD.armarQuerySelectField(Ruta.N_TABLA, Ruta.CAMPO_SEC_USUARIO_ID, condiciones,
					null);
		}

		condiciones.clear();
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, VendedorDTS.CAMPO_VENDEDOR, selectIdVendedores));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorDTS.CAMPO_ESTADO, estadoAlta));

		// este elemento contiene todos los vendedores que tiene la ruta o panel de la
		// jornada enviada
		String vendedores = UtileriasJava
				.listToString(Conf.TIPO_TEXTO,
						UtileriasBD.getOneField(conn, VendedorDTS.CAMPO_USUARIO,
								getParticion(VendedorDTS.N_TABLA, Conf.PARTITION,
										datosJornada.get(Jornada.CAMPO_TCSCDTSID), input.getCodArea()),
								condiciones, null),
						",");
		log.trace("Se obtuvieron vendedores asociados.");

		// se obtienen todos los dispositivos que han iniciado sesion
		OutputJornada listSesiones = obtenerSesiones(conn, input.getCodArea(), vendedores,
				datosJornada.get(Jornada.CAMPO_FECHA), diferencia);
		/*
		 * este listado contiene todos los usuarios y dispositivos en donde se ha
		 * iniciado sesi\u00F3n despu\u00E9s de que se cre\u00F3 la jornada
		 */
		if (!listSesiones.getRespuesta().getDescripcion().equals("OK")) {
			log.debug("el listado de sesiones no es correcto");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_LISTADO_SESIONES_764, null,
					nombreClase, nombreMetodo, null, input.getCodArea());
			output.setRespuesta(respuesta);
			return output;
		}

		// se obtiene el listado de jornadas de responsable y dependientes
		condiciones.clear();
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCCATPAISID,
				ID_PAIS.toString()));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_OR, Jornada.CAMPO_TCSCJORNADAVENID,
				input.getIdJornada()));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_OR, Jornada.CAMPO_JORNADA_RESPONSABLE,
				input.getIdJornada()));
		String lstIdsJornadas = UtileriasJava.listToString(Conf.TIPO_NUMERICO,
				UtileriasBD.getOneField(conn, Jornada.CAMPO_TCSCJORNADAVENID,
						getParticion(Jornada.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones, null),
				",");
		log.trace("Se obtuvieron las jornadas a verificar.");

		// se obtienen todos los registros que ya se han sincronizado en bd
		condiciones.clear();
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Sincronizacion.CAMPO_TCSCCATPAISID,
				ID_PAIS.toString()));
		condiciones.add(
				UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Sincronizacion.CAMPO_TCSCJORNADAVENDID, lstIdsJornadas));

		condiciones.add(new Filtro(Filtro.AND, Sincronizacion.CAMPO_CREADO_EL, Filtro.GTE,
				"TO_TIMESTAMP('" + datosJornada.get(Jornada.CAMPO_FECHA) + "', 'YYYY-MM-DD HH24:MI:SS.FF')" + " - (("
						+ diferencia + " + 3) / 1440)"));

		String[] camposSinc = { Sincronizacion.CAMPO_IDVENDEDOR,
				"(SELECT A." + VendedorDTS.CAMPO_USUARIO + " FROM " + VendedorDTS.N_TABLA + " A WHERE"
						+ " A.TCSCCATPAISID = " + ID_PAIS + " AND A." + VendedorDTS.CAMPO_VENDEDOR + " = "
						+ Sincronizacion.CAMPO_IDVENDEDOR + ") AS " + VendedorDTS.CAMPO_USUARIO,
				Sincronizacion.CAMPO_COD_DISPOSITIVO,
				"(SELECT " + Dispositivo.CAMPO_ESTADO + " FROM " + Dispositivo.N_TABLA + " WHERE " + " TCSCCATPAISID = "
						+ ID_PAIS + " AND " + Dispositivo.CAMPO_CODIGO_DISPOSITIVO + " = "
						+ Sincronizacion.CAMPO_COD_DISPOSITIVO + " AND UPPER(ESTADO) = '" + estadoAlta.toUpperCase()
						+ "' AND TCSCCATPAISID = " + ID_PAIS + ") AS " + Dispositivo.CAMPO_ESTADO };

		// este listado contiene todos registros de vendedores y dispositivos que han
		// finalizado y sincronizado operaciones de la jornada
		List<Map<String, String>> listSincVendedorDispositivo = UtileriasBD.getSingleData(conn,
				getParticion(Sincronizacion.N_TABLA, Conf.PARTITION, "", input.getCodArea()), camposSinc, condiciones,
				null);
		log.trace("Se obtuvieron vendedores y dispositivos donde se sincronizaron operaciones.");

		if (!listSincVendedorDispositivo.isEmpty()) {

			// se verifica que todos los dispositivos que han iniciado sesion y que todos
			// los vendedores hayan sincronizado sus operaciones
			boolean flag = false;
			for (int i = 0; i < listSesiones.getJornadas().size(); i++) {
				flag = false;
				String dispositivoSesion = listSesiones.getJornadas().get(i).getCodDispositivo();
				String usuarioSesion = listSesiones.getJornadas().get(i).getUsuario();

				for (int j = 0; j < listSincVendedorDispositivo.size(); j++) {
					if (listSincVendedorDispositivo.get(j).get(Dispositivo.CAMPO_ESTADO)
							.equalsIgnoreCase(estadoProcesoSiniestro)
							|| listSincVendedorDispositivo.get(j).get(Dispositivo.CAMPO_ESTADO)
									.equalsIgnoreCase(estadoSiniestrado)) {
						// el dispositivo esta marcado como siniestrado o en proceso de siniestro, se
						// omite y se acepta para finalizar jornada
						log.info("SINIESTRADO. vendedor: " + usuarioSesion + ", dispositivo: " + dispositivoSesion);
						listSincVendedorDispositivo.remove(j);
						flag = true;
						break;

					} else {
						String dispositivoSinc = listSincVendedorDispositivo.get(j)
								.get(Sincronizacion.CAMPO_COD_DISPOSITIVO);
						String usuarioSinc = listSincVendedorDispositivo.get(j).get(VendedorDTS.CAMPO_USUARIO);

						if (dispositivoSesion.equalsIgnoreCase(dispositivoSinc)
								&& usuarioSesion.equalsIgnoreCase(usuarioSinc)) {
							InputDispositivo disp = new InputDispositivo();
							disp.setCodigoDispositivo(dispositivoSesion);
							if (!dispUsados.contains(disp)) {
								dispUsados.add(disp);
							}
							listSincVendedorDispositivo.remove(j);
							flag = true;
							break;
						} else {
							flag = false;
						}
					}
				}

				if (!flag) {
					log.trace("--->FAIL. vendedor: " + usuarioSesion + ", dispositivo: " + dispositivoSesion);
					InputJornada jornada = new InputJornada();
					jornada.setCodDispositivo(dispositivoSesion);
					jornada.setUsuarioVendedor(usuarioSesion);
					jornada.setIdJornada(datosJornada.get(Jornada.CAMPO_TCSCJORNADAVENID));
					jornada.setIdVendedor(getIdVendedor(conn, usuarioSesion, datosJornada.get(Jornada.CAMPO_TCSCDTSID),
							input.getCodArea(), ID_PAIS));
					jornadas.add(jornada);
				}
			}
		} else {
			for (int i = 0; i < listSesiones.getJornadas().size(); i++) {
				log.trace("--->FAIL. vendedor: " + listSesiones.getJornadas().get(i).getCodDispositivo()
						+ ", dispositivo: " + listSesiones.getJornadas().get(i).getUsuario());
				InputJornada jornada = new InputJornada();
				jornada.setCodDispositivo(listSesiones.getJornadas().get(i).getCodDispositivo());
				jornada.setUsuarioVendedor(listSesiones.getJornadas().get(i).getUsuario());
				jornada.setIdJornada(datosJornada.get(Jornada.CAMPO_TCSCJORNADAVENID));
				jornada.setIdVendedor(getIdVendedor(conn, listSesiones.getJornadas().get(i).getUsuario(),
						datosJornada.get(Jornada.CAMPO_TCSCDTSID), input.getCodArea(), ID_PAIS));
				jornadas.add(jornada);

			}
		}

		if (!jornadas.isEmpty()) {
			log.debug("alguna sesion no ha sincronizado datos");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_SINCRONIZACION_JORNADA_763, null,
					nombreClase, nombreMetodo, null, input.getCodArea());
			output.setJornadas(jornadas);
			output.setRespuesta(respuesta);
			return output;

		} else {
			respuesta = new Respuesta();
			respuesta.setDescripcion("OK");
			respuesta.setCodResultado("0");
			output.setDispositivos(dispUsados);
			output.setRespuesta(respuesta);
		}

		return output;
	}

	private static String getIdVendedor(Connection conn, String usuario, String idDTS, String codArea,
			BigDecimal ID_PAIS) throws SQLException {
		List<Filtro> condiciones = new ArrayList<Filtro>();
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_TCSCCATPAISID,
				ID_PAIS.toString()));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorDTS.CAMPO_USUARIO, usuario));

		return UtileriasBD.getOneRecord(conn, VendedorDTS.CAMPO_VENDEDOR,
				getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, idDTS, codArea), condiciones);
	}

	private static OutputJornada obtenerSesiones(Connection conn, String codArea, String vendedores,
			String fechaJornada, String diferencia) {
		String nombreMetodo = "obtenerSesiones";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta objRespuesta = new Respuesta();
		PreparedStatement pstm = null;
		ResultSet rst = null;
		OutputJornada output = new OutputJornada();
		List<InputJornada> listSesiones = new ArrayList<InputJornada>();

		String qryDifHorario = "";
		if (diferencia != null && !diferencia.equals("") && !diferencia.equals("0")) {
			qryDifHorario = " - (" + diferencia + " / 1440) ";
		}

		String sql = queryObtenerSesiones(vendedores, fechaJornada, qryDifHorario);

		log.trace(sql);
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, codArea);
			rst = pstm.executeQuery();
			if (rst.next()) {
				objRespuesta.setDescripcion("OK");

				do {
					InputJornada item = new InputJornada();
					item.setCodDispositivo(rst.getString("cod_dispositivo"));
					item.setUsuario(rst.getString("username"));

					listSesiones.add(item);
					output.setJornadas(listSesiones);
				} while (rst.next());
			} else {
				objRespuesta.setDescripcion("FAIL");
				objRespuesta.setExcepcion("Ocurri\u00F3 un inconveniente al consultar sesiones.");
			}

		} catch (SQLException e) {
			objRespuesta = new ControladorBase().getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo,
					"", codArea);

		} catch (Exception e) {
			objRespuesta = new ControladorBase().getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase,
					nombreMetodo, "", codArea);

		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstm);

			output.setRespuesta(objRespuesta);
		}

		return output;
	}

	public static String queryObtenerSesiones(String vendedores, String fechaJornada, String qryDifHorario) {
		String sql = "SELECT " + "DISTINCT(cod_dispositivo) AS cod_dispositivo, " + "username " + "FROM TC_SC_SESION "
				+ "WHERE tcsccatpaisid = (SELECT TCSCCATPAISID FROM TC_SC_CAT_PAIS WHERE AREA =?) "
				+ "AND username IN (" + vendedores + ") " + "AND ((creado_el >= TO_TIMESTAMP ('" + fechaJornada
				+ "', 'YYYY-MM-DD HH24:MI:SS.FF')" + qryDifHorario
				+ "AND modificado_el IS NULL) OR modificado_el >= TO_TIMESTAMP ('" + fechaJornada
				+ "', 'YYYY-MM-DD HH24:MI:SS.FF')" + qryDifHorario + ")";

		return sql;
	}

	/**
	 * Funci\u00F3n que permite verificar si la creaci\u00F3n o modificaci\u00F3n de
	 * jornada genera alguna alarma configurada.
	 * 
	 * @param conn
	 * @param input
	 * @param estadoAlta
	 * @param finJornada
	 * @param jornadasCreadas
	 * @param jornadasModificadas
	 * @return
	 */
	private static boolean verificarAlarmas(Connection conn, InputJornada input, String estadoAlta, boolean finJornada,
			List<InputJornada> jornadasCreadas, List<Map<String, String>> jornadasModificadas, BigDecimal ID_PAIS) {
		List<LogSidra> listaLog = new ArrayList<LogSidra>();
		Locale currentLocale = new Locale("es");
		DateFormat formato = DateFormat.getDateInstance(DateFormat.FULL, currentLocale);
		String fechaCorreo = (formato.format(new Date()));
		List<Filtro> condiciones = new ArrayList<Filtro>();
		SimpleDateFormat FORMATO_TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat FORMATO_HORA = new SimpleDateFormat("hh:mma");
		String asunto = "";
		String cuerpo = "";
		String filasVendedores = "";
		String cuerpoCompleto = "";
		String alarmaHorario = "";
		String alarmaFecha = "";
		String alarmaAmbas = "";
		String tipoAlarma = "";
		String motivoHorario = "";
		String motivoFecha = "";
		String horarioInicial = "";
		String horarioFinal = "";
		boolean banderaHorario = false;
		boolean banderaFecha = false;

		try {
			// Se obtienen todas las configuraciones.
			String[] campos = { Catalogo.CAMPO_NOMBRE, Catalogo.CAMPO_VALOR };

			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO,
					Conf.GRUPO_JORNADA_HORARIOS));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO,
					Conf.GRUPO_JORNADA_TIPO_ALARMA));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO,
					Conf.GRUPO_JORNADA_PLANTILLA_ALARMA));

			List<Map<String, String>> datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos,
					condiciones, null);

			for (int i = 0; i < datosConfig.size(); i++) {
				if (Conf.JORNADA_HORARIO_INICIO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
					horarioInicial = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
				}
				if (Conf.JORNADA_HORARIO_FINAL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
					horarioFinal = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
				}
				if (Conf.JORNADA_ALARMA_HORARIO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
					alarmaHorario = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
				}
				if (Conf.JORNADA_ALARMA_FECHA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
					alarmaFecha = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
				}
				if (Conf.JORNADA_ALARMA_AMBAS.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
					alarmaAmbas = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
				}
				if (Conf.CONFIG_CORREO_ASUNTO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
					asunto = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
				}
				if (Conf.CONFIG_CORREO_CUERPO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
					cuerpo = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
				}
				if (Conf.CONFIG_CORREO_MOTIVO_HORARIO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
					motivoHorario = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
				}
				if (Conf.CONFIG_CORREO_MOTIVO_FECHA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
					motivoFecha = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
				}
			}

			// se obtiene el listado de correos
			condiciones.clear();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, DestinoAlarma.CAMPO_ALARMA,
					Conf.JORNADA_NOMBRE_ALARMA));

			String[] camposDestinatario = { DestinoAlarma.CAMPO_NOMBRE_DESTINATARIO, DestinoAlarma.CAMPO_CORREO };
			List<Map<String, String>> lstCorreos = UtileriasBD.getSingleData(conn, DestinoAlarma.N_TABLA,
					camposDestinatario, condiciones, null);

			// se agregan los datos generales a la plantilla de correo
			cuerpo = cuerpo.replace("@@fecha", fechaCorreo);
			cuerpo = cuerpo.replace("@@resp", getNombreVendedor(conn, input.getIdResponsable(), ID_PAIS));
			cuerpo = cuerpo.replace("@@disp", input.getCodDispositivo());

			OutputJornada datosJornada = obtenerDatosJornada(conn, finJornada, jornadasCreadas, jornadasModificadas,
					ID_PAIS);
			if (new BigDecimal(datosJornada.getRespuesta().getCodResultado()).intValue() == 0) {
				// no se encontraron los datos de las jornadas
				listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_JORNADA, "WS JORNADA - VERIFICA ALARMA", "0",
						Conf.LOG_TIPO_NINGUNO, "Problema al actualiar jornadas por env\u00EDo de alarma.", ""));

				return false;
			} else {
				for (InputJornada jornada : datosJornada.getJornadas()) {
					filasVendedores += "<tr><td>" + jornada.getNombreVendedor() + "</td><td>"
							+ jornada.getUsuarioVendedor() + "</td></tr>";
				}

				cuerpo = cuerpo.replace("@@dts", datosJornada.getJornadas().get(0).getNombreDistribuidor());
				cuerpo = cuerpo.replace("@@tipo", datosJornada.getJornadas().get(0).getTipo().charAt(0)
						+ datosJornada.getJornadas().get(0).getTipo().substring(1).toLowerCase());
				cuerpo = cuerpo.replace("@@nombTipo", datosJornada.getJornadas().get(0).getNombreTipo());
				cuerpo = cuerpo.replace("@@filas", filasVendedores);
			}

			// se obtiene el horario actual de la jornada y motivo de la alarma
			Calendar calHoraJornada = Calendar.getInstance();
			if (finJornada) {
				cuerpo = cuerpo.replace("@@operacion", "finalizado");
				calHoraJornada.setTime(FORMATO_TIMESTAMP.parse(FORMATO_TIMESTAMP.format(new Date())));
			} else {
				cuerpo = cuerpo.replace("@@operacion", "iniciado");
				calHoraJornada.setTime(FORMATO_TIMESTAMP.parse(input.getFecha()));
			}

			// se obtiene el horario permitido
			Calendar calHoraInicio = Calendar.getInstance();
			calHoraInicio.setTime(FORMATO_HORA.parse(horarioInicial));

			Calendar calHoraFinal = Calendar.getInstance();
			calHoraFinal.setTime(FORMATO_HORA.parse(horarioFinal));

			log.trace("horaJornada:" + calHoraJornada.get(Calendar.HOUR_OF_DAY));
			log.trace("horaInicio:" + calHoraInicio.get(Calendar.HOUR_OF_DAY));
			log.trace("horaFinal:" + calHoraFinal.get(Calendar.HOUR_OF_DAY));
			if (calHoraJornada.get(Calendar.HOUR_OF_DAY) < calHoraInicio.get(Calendar.HOUR_OF_DAY)
					|| calHoraJornada.get(Calendar.HOUR_OF_DAY) >= calHoraFinal.get(Calendar.HOUR_OF_DAY)) {
				// la hora de la jornada es no laboral
				log.trace("la hora de la jornada es no laboral, envia correo");

				banderaHorario = true;
			}

			condiciones.clear();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, DiasFestivos.CAMPO_TCSCCATPAISID,
					ID_PAIS.toString()));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_SYSDATE_TRUNC_AND, DiasFestivos.CAMPO_FECHA, null,
					null, null));
			condiciones.add(
					UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, DiasFestivos.CAMPO_ESTADO, estadoAlta));
			int existencias = UtileriasBD.selectCount(conn, DiasFestivos.N_TABLA, condiciones);
			if (existencias > 0) {
				// la fecha de la jornada es igual a un dia festivo
				log.trace("la fecha de la jornada es igual a un dia festivo");

				banderaFecha = true;
			}

			if (banderaHorario == true && banderaFecha == true) {
				cuerpo = cuerpo.replace("@@motivo", motivoHorario + " y " + motivoFecha);
				tipoAlarma = alarmaAmbas;

			} else if (banderaHorario == true) {
				cuerpo = cuerpo.replace("@@motivo", motivoHorario);
				tipoAlarma = alarmaHorario;

			} else if (banderaFecha == true) {
				cuerpo = cuerpo.replace("@@motivo", motivoFecha);
				tipoAlarma = alarmaFecha;

			} else {
				// no se gener\u00F3 ninguna alarma
				return true;
			}

			// se obtienen los parametros del correo
			condiciones.clear();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,
					ID_PAIS.toString()));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO,
					Conf.GRUPO_JORNADA_CONFIG_CORREO));

			datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, null);

			String sender = "";
			String host = "";
			String port = "";

			for (int i = 0; i < datosConfig.size(); i++) {
				if (Conf.CONFIG_CORREO_HOST.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
					host = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
				}
				if (Conf.CONFIG_CORREO_PORT.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
					port = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
				}
				if (Conf.CONFIG_CORREO_SENDER.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
					sender = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
				}
			}

			String resp = "";
			List<String> listCorreosError = new ArrayList<String>();

			for (Map<String, String> correo : lstCorreos) {
				cuerpoCompleto = cuerpo.replace("@@destinatario", correo.get(DestinoAlarma.CAMPO_NOMBRE_DESTINATARIO));
				resp = SendMail.sendMail(sender, host, port, correo.get(DestinoAlarma.CAMPO_CORREO), asunto,
						cuerpoCompleto);

				if (resp.equals("OK")) {
					// se envio
					log.trace("envia correo ok");

					listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_JORNADA, "WS JORNADA - VERIFICA ALARMA", "0",
							Conf.LOG_TIPO_NINGUNO, "Se envi\u00F3 correo de alarma.", resp));
				} else {
					// fallo al enviar
					log.trace("fallo al enviar correo");
					listCorreosError.add(correo.get(Catalogo.CAMPO_VALOR));
					// TODO listado de correos con error de envio, alguna opcion de reenvio?

					listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_JORNADA, "WS JORNADA - VERIFICA ALARMA", "0",
							Conf.LOG_TIPO_NINGUNO, "Problema al enviar correo de alarma.", resp));
				}
			}

			// se actualiza la jornada para marcar como jornada con envio de alarma
			boolean updateJornada = updateJornadaAlarma(conn, finJornada, jornadasCreadas, jornadasModificadas,
					tipoAlarma, input.getUsuario(), input.getCodArea(), ID_PAIS);
			if (!updateJornada) {
				listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_JORNADA, "WS JORNADA - VERIFICA ALARMA", "0",
						Conf.LOG_TIPO_NINGUNO, "Problema al actualiar jornadas por env\u00EDo de alarma.", resp));

				return false;
			}

		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_JORNADA, "WS JORNADA - VERIFICA ALARMA", "0",
					Conf.LOG_TIPO_NINGUNO, "Problema al verificar alarmas.", e.getMessage()));

		} catch (ParseException e) {
			log.error(e.getMessage(), e);
			listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_JORNADA, "WS JORNADA - VERIFICA ALARMA", "0",
					Conf.LOG_TIPO_NINGUNO, "Problema al verificar alarmas.", e.getMessage()));

		} finally {
			UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
		}

		return true;
	}

	/**
	 * Funci\u00F3n que obtiene los datos de las jornadas que se van a actualizar al
	 * generar alarma.
	 * 
	 * @param conn
	 * @param finJornada
	 * @param jornadasCreadas
	 * @param jornadasModificadas
	 * @return
	 * @throws SQLException
	 */
	private static OutputJornada obtenerDatosJornada(Connection conn, boolean finJornada,
			List<InputJornada> jornadasCreadas, List<Map<String, String>> jornadasModificadas, BigDecimal ID_PAIS)
			throws SQLException {
		OutputJornada output = new OutputJornada();
		List<InputJornada> list = new ArrayList<InputJornada>();
		InputJornada item = new InputJornada();
		List<Filtro> condiciones = new ArrayList<Filtro>();
		List<Order> orden = new ArrayList<Order>();

		Respuesta respuesta = new Respuesta();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String sql = null;
		String jornadas = "";

		try {
			String nombreTipo = "NVL ((SELECT " + Ruta.CAMPO_NOMBRE + " FROM " + Ruta.N_TABLA + " WHERE "
					+ Ruta.CAMPO_TC_SC_RUTA_ID + " = " + Jornada.CAMPO_IDTIPO + " AND TCSCCATPAISID = " + ID_PAIS
					+ "), " + "NVL ((SELECT " + Panel.CAMPO_NOMBRE + " FROM " + Panel.N_TABLA + " WHERE "
					+ Panel.CAMPO_TCSCPANELID + " = " + Jornada.CAMPO_IDTIPO + " AND TCSCCATPAISID = " + ID_PAIS
					+ "), '')) AS NOMBRETIPO";

			String campos[] = {
					Jornada.CAMPO_DESCRIPCION_TIPO, nombreTipo, "(SELECT D." + Distribuidor.CAMPO_NOMBRES + " FROM "
							+ Distribuidor.N_TABLA + " D WHERE D." + Distribuidor.CAMPO_TC_SC_DTS_ID + " = J."
							+ Jornada.CAMPO_TCSCDTSID + " AND D.TCSCCATPAISID = " + ID_PAIS + ") AS NOMB_DTS",
					Jornada.CAMPO_VENDEDOR,
					"(SELECT " + VendedorDTS.CAMPO_USUARIO + " FROM " + VendedorDTS.N_TABLA
							+ " V WHERE V.TCSCCATPAISID = " + ID_PAIS + " AND " + VendedorDTS.CAMPO_VENDEDOR + " = J."
							+ Jornada.CAMPO_VENDEDOR + ") AS " + VendedorDTS.CAMPO_USUARIO,
					"(SELECT " + VendedorDTS.CAMPO_NOMBRE + " FROM " + VendedorDTS.N_TABLA
							+ " V WHERE V.TCSCCATPAISID = " + ID_PAIS + " AND " + VendedorDTS.CAMPO_VENDEDOR + " = J."
							+ Jornada.CAMPO_VENDEDOR + ") AS " + VendedorDTS.CAMPO_NOMBRE,
					"(SELECT " + VendedorDTS.CAMPO_APELLIDO + " FROM " + VendedorDTS.N_TABLA
							+ " V WHERE V.TCSCCATPAISID = " + ID_PAIS + " AND " + VendedorDTS.CAMPO_VENDEDOR + " = J."
							+ Jornada.CAMPO_VENDEDOR + ") AS " + VendedorDTS.CAMPO_APELLIDO };

			if (finJornada) {
				for (int i = 0; i < jornadasModificadas.size(); i++) {
					jornadas += jornadasModificadas.get(i).get(Jornada.CAMPO_TCSCJORNADAVENID);

					if (i != jornadasModificadas.size() - 1)
						jornadas += ",";
				}
			} else {
				for (int i = 0; i < jornadasCreadas.size(); i++) {
					jornadas += jornadasCreadas.get(i).getIdJornada();

					if (i != jornadasCreadas.size() - 1)
						jornadas += ",";
				}
			}
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Jornada.CAMPO_TCSCJORNADAVENID, jornadas));

			orden.add(new Order(Jornada.CAMPO_TCSCJORNADAVENID, Order.ASC));

			sql = UtileriasBD.armarQuerySelect(Jornada.N_TABLA + " J", campos, condiciones, orden);
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst != null) {
				if (!rst.next()) {
					respuesta.setCodResultado("0");
					output.setRespuesta(respuesta);
				} else {
					respuesta.setCodResultado("1");

					do {
						item = new InputJornada();
						item.setNombreDistribuidor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMB_DTS"));
						item.setIdVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Jornada.CAMPO_VENDEDOR));
						item.setNombreVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, VendedorDTS.CAMPO_NOMBRE)
								+ " " + UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, VendedorDTS.CAMPO_APELLIDO));
						item.setUsuarioVendedor(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, VendedorDTS.CAMPO_USUARIO));
						item.setTipo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_DESCRIPCION_TIPO));
						item.setNombreTipo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRETIPO"));

						list.add(item);
					} while (rst.next());

					output.setRespuesta(respuesta);
					output.setJornadas(list);
				}
			}
		} finally {

			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return output;
	}

	/**
	 * Funci\u00F3n que actualiza las jornadas tras generarse una alarma.
	 * 
	 * @param conn
	 * @param finJornada
	 * @param jornadasCreadas
	 * @param jornadasModificadas
	 * @param tipoAlarma
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private static boolean updateJornadaAlarma(Connection conn, boolean finJornada, List<InputJornada> jornadasCreadas,
			List<Map<String, String>> jornadasModificadas, String tipoAlarma, String usuario, String codArea,
			BigDecimal ID_PAIS) throws SQLException {
		List<LogSidra> listaLog = new ArrayList<LogSidra>();
		List<Filtro> condiciones = new ArrayList<Filtro>();
		Statement stmtUpdate = conn.createStatement();
		String sql = "";
		int[] updateCounts;

		try {
			if (!finJornada) {
				String camposUpd[][] = {
						{ Jornada.CAMPO_ENVIO_ALARMA, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, "1") },
						{ Jornada.CAMPO_TIPO_ALARMA, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, tipoAlarma) } };

				for (int i = 0; i < jornadasCreadas.size(); i++) {
					condiciones.clear();
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_OR,
							Jornada.CAMPO_TCSCJORNADAVENID, jornadasCreadas.get(i).getIdJornada()));
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCCATPAISID,
							ID_PAIS.toString()));
					sql = UtileriasBD.armarQueryUpdate(getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea),
							camposUpd, condiciones);
					stmtUpdate.addBatch(sql);

					listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_JORNADA, "WS JORNADA - VERIFICA ALARMA",
							jornadasCreadas.get(i).getIdJornada(), Conf.LOG_TIPO_JORNADA,
							"Se actualizaron datos de jornada por env\u00EDo de alarma.", ""));
				}

			} else {
				String camposUpd[][] = {
						{ Jornada.CAMPO_ENVIO_ALARMA_FIN, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, "1") },
						{ Jornada.CAMPO_TIPO_ALARMA_FIN, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, tipoAlarma) } };

				for (int i = 0; i < jornadasModificadas.size(); i++) {
					condiciones.clear();
					condiciones
							.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_OR, Jornada.CAMPO_TCSCJORNADAVENID,
									jornadasModificadas.get(i).get(Jornada.CAMPO_TCSCJORNADAVENID)));
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCCATPAISID,
							ID_PAIS.toString()));
					sql = UtileriasBD.armarQueryUpdate(getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea),
							camposUpd, condiciones);
					stmtUpdate.addBatch(sql);

					listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_JORNADA, "WS JORNADA - VERIFICA ALARMA",
							jornadasModificadas.get(i).get(Jornada.CAMPO_TCSCJORNADAVENID), Conf.LOG_TIPO_JORNADA,
							"Se actualizaron datos de jornada por env\u00EDo de alarma.", ""));
				}
			}

			updateCounts = stmtUpdate.executeBatch();

		} finally {
			DbUtils.closeQuietly(stmtUpdate);
			UtileriasJava.doInsertLog(listaLog, usuario, codArea);
		}

		return UtileriasJava.validarBatch(1, updateCounts);
	}

	public static String insertJornada(Connection conn, String campos[], List<String> inserts) throws SQLException {
		// se inserta la jornada del responsable \u00FAnicamente
		String sql = "";
		String idJornada = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		try {
			sql = UtileriasBD.armarQueryInsert(Jornada.N_TABLA, campos, inserts);
			String generatedColumns[] = { Jornada.CAMPO_TCSCJORNADAVENID };
			pstmt = conn.prepareStatement(sql, generatedColumns);

			pstmt.executeUpdate();
			rst = pstmt.getGeneratedKeys();

			if (rst.next()) {
				idJornada = rst.getString(1);
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}
		return idJornada;
	}

	public static List<String> getIdPanelRuta(Connection conn, InputJornada input, String estadoAlta, String tipoPanel,
			String tipoRuta, BigDecimal ID_PAIS) throws SQLException {
		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		List<String> idTipo = new ArrayList<String>();
		sql = "SELECT " + "(SELECT tcscrutaid FROM TC_SC_RUTA" + " WHERE secusuarioid = ?"
				+ " AND TCSCCATPAISID = ? AND UPPER(ESTADO) = ?) AS idRuta, "
				+ "(SELECT idtipo FROM TC_SC_VEND_PANELPDV" + " WHERE TCSCCATPAISID =?  AND vendedor = ?"
				+ " AND UPPER(tipo) = ?" + " AND UPPER(estado) = ?) AS idPanel " + "FROM DUAL";

		log.debug("Qry ruta-panel: " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1, new BigDecimal(input.getIdResponsable()));
			pstmt.setBigDecimal(2, ID_PAIS);
			pstmt.setString(3, estadoAlta);
			pstmt.setBigDecimal(4, ID_PAIS);
			pstmt.setBigDecimal(5, new BigDecimal(input.getIdResponsable()));
			pstmt.setString(6, tipoPanel.toUpperCase());
			pstmt.setString(7, estadoAlta);
			rst = pstmt.executeQuery();
			if (rst.next()) {
				if (rst.getBigDecimal("idRuta") != null) {
					idTipo.add(0, rst.getBigDecimal("idRuta").toString());
					idTipo.add(1, tipoRuta);
				} else if (rst.getBigDecimal("idPanel") != null) {
					idTipo.add(0, rst.getBigDecimal("idPanel").toString());
					idTipo.add(1, tipoPanel);
				}
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}

		return idTipo;

	}

	/**
	 * M\u00E9todo para verificar si el vendedor que no posee inventario asignado
	 * por lo menos tiene recarga. Este es util cuando un vendedor quiere vender
	 * solo recargas y no posee otro tipo de inventario Agregado 16-04-18
	 */
	public static int getInvPrincipalRecarga(Connection conn, String idDistribuidor, BigDecimal ID_PAIS)
			throws SQLException {
		String query = "";
		PreparedStatement pstmt2 = null;
		ResultSet rst2 = null;
		int datos = 0;
		// se verifica si la bodega contiene recargas creado por jcsimon 20/11/2017 **
		query = "select count(b.cantidad) as CANTIDAD from tc_sc_dts a " + "inner join tc_sc_inventario b "
				+ "on a.TCSCBODEGAVIRTUALID = b.TCSCBODEGAVIRTUALID " + "where a.tcsccatpaisid=? "
				+ "and a.tcscdtsid = ? " + "and b.TIPO_GRUPO_SIDRA = 'RECARGA' ";

		log.debug("Qry vendedores: " + query);
		try {
			pstmt2 = conn.prepareStatement(query);
			pstmt2.setBigDecimal(1, ID_PAIS);
			pstmt2.setBigDecimal(2, new BigDecimal(idDistribuidor));
			rst2 = pstmt2.executeQuery();

			if (rst2.next()) {

				log.debug("ressultado de bodega : " + rst2.getString(1));
				datos = Integer.parseInt(rst2.getString(1));
			}
		} finally {
			DbUtils.closeQuietly(rst2);
			DbUtils.closeQuietly(pstmt2);
		}
		return datos;
	}
}
