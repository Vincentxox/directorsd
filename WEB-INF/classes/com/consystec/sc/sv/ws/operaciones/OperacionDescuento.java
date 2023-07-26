package com.consystec.sc.sv.ws.operaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.descuento.InputDescuento;
import com.consystec.sc.ca.ws.input.descuento.InputDescuentoDet;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.descuento.OutputDescuento;
import com.consystec.sc.sv.ws.metodos.CtrlDescuento;
import com.consystec.sc.sv.ws.orm.Descuento;
import com.consystec.sc.sv.ws.orm.DescuentoDet;
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
public class OperacionDescuento {
	private OperacionDescuento() {
	}

	private static final Logger log = Logger.getLogger(OperacionDescuento.class);

	/**
	 * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @return OutputDescuento
	 * @throws SQLException
	 */
	public static OutputDescuento doGet(Connection conn, InputDescuento input, int metodo) throws SQLException {
		String nombreMetodo = "doGet";
		String nombreClase = new CurrentClassGetter().getClassName();

		List<InputDescuento> list = new ArrayList<InputDescuento>();

		Respuesta respuesta = null;
		OutputDescuento output = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String sql = null;
		String campos[] = CtrlDescuento.obtenerCamposGetPost(metodo);

		List<Filtro> condiciones = CtrlDescuento.obtenerCondiciones(conn, input, metodo);

		List<Order> orden = new ArrayList<Order>();
		try {
			orden.add(new Order(Descuento.CAMPO_NOMBRE, Order.ASC));

			sql = UtileriasBD.armarQuerySelect(Descuento.N_TABLA, campos, condiciones, orden);
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst != null) {
				if (!rst.next()) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSOS, null, nombreClase,
							nombreMetodo, null, input.getCodArea());

					output = new OutputDescuento();
					output.setRespuesta(respuesta);
				} else {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					String idDescuento = "";
					do {
						InputDescuento item = new InputDescuento();
						idDescuento = rst.getString(Descuento.CAMPO_TC_SC_DESCUENTO_ID);
						item.setIdDescuento(idDescuento);
						item.setTipo(rst.getString(Descuento.CAMPO_TIPO));
						item.setNombre(rst.getString(Descuento.CAMPO_NOMBRE));
						item.setDescripcion(rst.getString(Descuento.CAMPO_DESCRIPCION));
						item.setTipoDescuento(rst.getString(Descuento.CAMPO_TIPO_DESCUENTO));
						item.setConfiguracion(rst.getString(Descuento.CAMPO_CONFIGURACION));
						item.setDescuento(rst.getString(Descuento.CAMPO_DESCUENTO));
						item.setConfRecarga(rst.getString(Descuento.CAMPO_CONF_RECARGA));
						item.setRecarga(rst.getString(Descuento.CAMPO_RECARGA));
						item.setConfPrecio(rst.getString(Descuento.CAMPO_CONF_PRECIO));
						item.setPrecio(rst.getString(Descuento.CAMPO_PRECIO));
						item.setFechaDesde(UtileriasJava.formatStringDate(rst.getString(Descuento.CAMPO_FECHA_DESDE)));
						item.setFechaHasta(UtileriasJava.formatStringDate(rst.getString(Descuento.CAMPO_FECHA_HASTA)));
						item.setIdArticulo(rst.getString(Descuento.CAMPO_ARTICULOID));
						item.setNombreArt(rst.getString(Descuento.CAMPO_NOMBRE_ART));
						item.setPrecioArt(rst.getString(Descuento.CAMPO_PRECIO_ART));
						item.setEstado(rst.getString(Descuento.CAMPO_ESTADO));
						item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(Descuento.CAMPO_CREADO_EL)));
						item.setCreado_por(rst.getString(Descuento.CAMPO_CREADO_POR));
						item.setModificado_el(
								UtileriasJava.formatStringDate(rst.getString(Descuento.CAMPO_MODIFICADO_EL)));
						item.setModificado_por(rst.getString(Descuento.CAMPO_MODIFICADO_POR));

						List<InputDescuentoDet> detalles = getDatosTablaHija(conn, idDescuento, metodo,
								input.getCodArea());

						item.setDescuentoDet(detalles);

						list.add(item);
					} while (rst.next());

					output = new OutputDescuento();
					output.setRespuesta(respuesta);
					output.setDescuento(list);
				}
			}
		} finally {

			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return output;
	}

	/**
	 * Funcion que obtiene los datos relacionados de la tabla hija mediante el id de
	 * la tabla padre.
	 * 
	 * @param conn
	 * @param idPadre
	 * @param input
	 * @param metodo
	 * @return OutputBodegaDTS
	 * @throws SQLException
	 */
	private static List<InputDescuentoDet> getDatosTablaHija(Connection conn, String idPadre, int metodo,
			String codArea) throws SQLException {
		String nombreMetodo = "getDatosTablaHija";
		String nombreClase = new CurrentClassGetter().getClassName();

		List<InputDescuentoDet> list = new ArrayList<InputDescuentoDet>();
		PreparedStatement pstmtIn = null;
		ResultSet rstIn = null;

		String camposInterno[] = CtrlDescuento.obtenerCamposTablaHija(metodo);

		List<Filtro> condicionesInterno = CtrlDescuento.obtenerCondicionesTablaHija(idPadre, metodo);

		try {
			condicionesInterno.add(new Filtro(DescuentoDet.CAMPO_TCSCDESCUENTOID, Filtro.EQ, idPadre)); // Condicion
																										// para obtener
																										// las
																										// configuraciones
																										// de cada item
																										// padre.

			String sql = UtileriasBD.armarQuerySelect(DescuentoDet.N_TABLA, camposInterno, condicionesInterno);
			pstmtIn = conn.prepareStatement(sql);
			rstIn = pstmtIn.executeQuery();

			if (rstIn != null) {
				if (!rstIn.next()) {
					log.debug("No existen registros en la tabla hija con esos parametros.");
					InputDescuentoDet item = new InputDescuentoDet();

					Respuesta respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSO_HIJO, null,
							nombreClase, nombreMetodo, null, codArea);

					item.setEstado(respuesta.getDescripcion());

					list.add(item);
				} else {
					do {
						InputDescuentoDet item = new InputDescuentoDet();
						item.setIdTipo(rstIn.getString(DescuentoDet.CAMPO_TCSCTIPOID));
						item.setTipo(rstIn.getString(DescuentoDet.CAMPO_TIPO));
						item.setEstado(rstIn.getString(DescuentoDet.CAMPO_ESTADO));
						item.setCreado_por(rstIn.getString(DescuentoDet.CAMPO_CREADO_POR));
						item.setCreado_el(
								UtileriasJava.formatStringDate(rstIn.getString(DescuentoDet.CAMPO_CREADO_EL)));
						item.setModificado_por(rstIn.getString(DescuentoDet.CAMPO_MODIFICADO_POR));
						item.setModificado_el(
								UtileriasJava.formatStringDate(rstIn.getString(DescuentoDet.CAMPO_MODIFICADO_EL)));

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
	 * @return OutputDescuento
	 * @throws SQLException
	 */
	public static OutputDescuento doPost(Connection conn, InputDescuento input) throws SQLException {
		String nombreMetodo = "doPost";
		String nombreClase = new CurrentClassGetter().getClassName();
		String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

		Respuesta respuesta = null;
		OutputDescuento output = null;
		boolean insertHijo = false;

		List<Filtro> condicionesExistencia = CtrlDescuento.obtenerCondicionesExistencia(conn, input, Conf.METODO_POST);

		String existencia = UtileriasBD.verificarExistencia(conn, Descuento.N_TABLA, condicionesExistencia);
		if (new Integer(existencia) > 0) {
			log.error("Ya existe el recurso.");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_EXISTENTE, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			output = new OutputDescuento();
			output.setRespuesta(respuesta);

			return output;
		}
		;

		String sql = null;
		String campos[] = CtrlDescuento.obtenerCamposGetPost(Conf.METODO_POST);
		List<String> inserts = CtrlDescuento.obtenerInsertsPost(conn, input, Descuento.SEQUENCE, estadoAlta);
		int idPadre = 0;
		sql = UtileriasBD.armarQueryInsert(Descuento.N_TABLA, campos, inserts);

		try {
			conn.setAutoCommit(false);
			String generatedColumns[] = { Descuento.CAMPO_TC_SC_DESCUENTO_ID };
			try (PreparedStatement pstmt = conn.prepareStatement(sql, generatedColumns);) {
				pstmt.executeUpdate();
				try (ResultSet rs = pstmt.getGeneratedKeys();) {
					if (rs.next()) {
						idPadre = rs.getInt(1);
					}
				}
			}

			log.debug("idPadre: " + idPadre);
			if (idPadre > 0) {
				// Se valida que no exista el recurso hijo
				List<Filtro> condicionesExistenciaHijo = CtrlDescuento.obtenerCondicionesExistenciaHijo(conn, idPadre,
						input.getCodArea());
				String existenciaHijo = UtileriasBD.verificarExistencia(conn, DescuentoDet.N_TABLA,
						condicionesExistenciaHijo);
				if (new Integer(existenciaHijo) > 0) {
					log.error("Ya existe el recurso hijo.");
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_EXISTENTE, null, nombreClase,
							nombreMetodo, null, input.getCodArea());

					output = new OutputDescuento();
					output.setRespuesta(respuesta);

					conn.rollback();
				} else {
					if (input.getTipo().equalsIgnoreCase(UtileriasJava.getConfig(conn, Conf.GRUPO_DESCUENTOS,
							Conf.TIPO_CAMPANIA, input.getCodArea()))) {
						insertHijo = doPostHijo(conn, idPadre, input, estadoAlta);
					} else {
						insertHijo = true;
					}
				}

				if (insertHijo == true) {
					conn.commit();
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_CREADO, null, nombreClase,
							nombreMetodo, null, input.getCodArea());

					output = new OutputDescuento();
					output.setRespuesta(respuesta);
				} else {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
							nombreMetodo, null, input.getCodArea());

					output = new OutputDescuento();
					output.setRespuesta(respuesta);
				}
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			respuesta = new ControladorBase().getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo,
					null, input.getCodArea());

			output = new OutputDescuento();
			output.setRespuesta(respuesta);

			log.error("Rollback");
			conn.rollback();
		} finally {
			conn.setAutoCommit(true);

		}

		return output;
	}

	/**
	 * Funci\u00F3n de realiza las inserciones en la tabla relacionada.
	 * 
	 * @param conn
	 * @param idPadre
	 * @param input
	 * @param estadoAlta
	 * @return boolean
	 * @throws SQLException
	 */
	private static boolean doPostHijo(Connection conn, int idPadre, InputDescuento input, String estadoAlta)
			throws SQLException {
		String campos[] = CtrlDescuento.obtenerCamposTablaHija(Conf.METODO_POST);
		List<String> inserts = CtrlDescuento.obtenerInsertsPostHijo(idPadre, input, DescuentoDet.SEQUENCE, estadoAlta);

		String sql = UtileriasBD.armarQueryInsertAll(DescuentoDet.N_TABLA, campos, inserts);

		QueryRunner Qr = new QueryRunner();
		try {
			Qr.update(conn, sql);
		} catch (SQLException e) {
			conn.rollback();
			log.error("Rollback", e);
			return false;
		}
		return Qr != null;
	}

	/**
	 * Funci\u00F3n que arma el query a utilizar al modificar datos en m\u00E9todos
	 * PUT y DELETE.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @return OutputDescuento
	 * @throws SQLException
	 */
	public static OutputDescuento doPutDel(Connection conn, InputDescuento input, int metodo) throws SQLException {
		String nombreMetodo = "doPutDel";
		String nombreClase = new CurrentClassGetter().getClassName();
		String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

		Respuesta respuesta = null;
		OutputDescuento output = null;
		boolean updDelHijo = false;
		boolean insertHijo = false;

		List<Filtro> condicionesExistencia = CtrlDescuento.obtenerCondicionesExistencia(conn, input, metodo);
		String existencia = UtileriasBD.verificarExistencia(conn, Descuento.N_TABLA, condicionesExistencia);
		if (new Integer(existencia) < 1) {
			log.error("No existe el recurso.");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			output = new OutputDescuento();
			output.setRespuesta(respuesta);

			return output;
		}

		String sql = null;

		String campos[][] = CtrlDescuento.obtenerCamposPutDel(conn, input, metodo, estadoAlta);
		List<Filtro> condiciones = CtrlDescuento.obtenerCondiciones(conn, input, metodo);

		sql = UtileriasBD.armarQueryUpdate(Descuento.N_TABLA, campos, condiciones);

		try {
			int i = 0;
			conn.setAutoCommit(false);
			QueryRunner Qr = new QueryRunner();
			i = Qr.update(conn, sql);

			if (i > 0) {
				updDelHijo = doPutDelHijo(conn, input.getIdDescuento(), input, metodo);

				if (updDelHijo) {
					if (metodo == Conf.METODO_PUT && input.getTipo().equalsIgnoreCase(UtileriasJava.getConfig(conn,
							Conf.GRUPO_DESCUENTOS, Conf.TIPO_CAMPANIA, input.getCodArea()))) {
						insertHijo = doPostHijo(conn, Integer.valueOf(input.getIdDescuento()), input, estadoAlta);
					} else {
						insertHijo = true;
					}
				}

				if (insertHijo) {
					conn.commit();
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_MODIFICADO, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					output = new OutputDescuento();
					output.setRespuesta(respuesta);
				} else {
					conn.rollback();
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
							nombreMetodo, null, input.getCodArea());

					output = new OutputDescuento();
					output.setRespuesta(respuesta);

					log.error("Rollback");
				}
			} else {
				respuesta = new Respuesta();
				respuesta.setCodResultado("0");
			}
			conn.commit();
		} catch (SQLException e) {
			respuesta = new ControladorBase().getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo,
					null, input.getCodArea());

			output = new OutputDescuento();
			output.setRespuesta(respuesta);

			log.error("Rollback");
			conn.rollback();
		} finally {
			conn.setAutoCommit(true);

		}

		return output;
	}

	/**
	 * Funci\u00F3n de realiza las modificaciones en la tabla relacionada.
	 * 
	 * @param conn
	 * @param idPadre
	 * @param input
	 * @param metodo
	 * @return boolean
	 * @throws SQLException
	 */
	private static boolean doPutDelHijo(Connection conn, String idPadre, InputDescuento input, int metodo)
			throws SQLException {
		String campos[][] = CtrlDescuento.obtenerCamposPutDelHijo(input);
		List<Filtro> condiciones = CtrlDescuento.obtenerCondicionesTablaHija(idPadre, metodo);

		String sql = "";
		if (metodo == Conf.METODO_PUT) {
			sql = UtileriasBD.armarQueryDelete(DescuentoDet.N_TABLA, condiciones);
		} else {
			sql = UtileriasBD.armarQueryUpdate(DescuentoDet.N_TABLA, campos, condiciones);
		}

		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			return false;
		} finally {
			DbUtils.closeQuietly(pstmt);
		}

	}
}
