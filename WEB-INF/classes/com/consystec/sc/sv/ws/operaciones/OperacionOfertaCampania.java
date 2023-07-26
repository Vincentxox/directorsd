package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.ofertacampania.InputOfertaCampania;
import com.consystec.sc.ca.ws.input.ofertacampania.InputOfertaCampaniaDet;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.ofertacampania.OutputOfertaCampania;
import com.consystec.sc.sv.ws.metodos.CtrlOfertaCampania;
import com.consystec.sc.sv.ws.orm.Condicion;
import com.consystec.sc.sv.ws.orm.CondicionCampania;
import com.consystec.sc.sv.ws.orm.CondicionOferta;
import com.consystec.sc.sv.ws.orm.OfertaCampania;
import com.consystec.sc.sv.ws.orm.OfertaCampaniaDet;
import com.consystec.sc.sv.ws.orm.Panel;
import com.consystec.sc.sv.ws.orm.PromoOfertaCampania;
import com.consystec.sc.sv.ws.orm.Ruta;
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
public class OperacionOfertaCampania {
	private OperacionOfertaCampania() {
	}

	private static final Logger log = Logger.getLogger(OperacionOfertaCampania.class);

	/**
	 * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @param tipoRuta
	 * @param tipoPanel
	 * @return OutputOfertaCampania
	 * @throws SQLException
	 */
	public static OutputOfertaCampania doGet(Connection conn, InputOfertaCampania input, int metodo, String tipoPanel,
			String tipoRuta, String codArea, BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "doGet";
		String nombreClase = new CurrentClassGetter().getClassName();
		List<InputOfertaCampania> list = new ArrayList<InputOfertaCampania>();
		Respuesta respuesta = null;
		OutputOfertaCampania output = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String sql = null;
		SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA);
		SimpleDateFormat FECHAHORA2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String campos[] = CtrlOfertaCampania.obtenerCamposGetPost(metodo);
			List<Filtro> condiciones = CtrlOfertaCampania.obtenerCondiciones(input, metodo, tipoPanel, tipoRuta,
					ID_PAIS);

			List<Order> orden = new ArrayList<Order>();
			orden.add(new Order(OfertaCampania.CAMPO_NOMBRE, Order.ASC));

			sql = UtileriasBD.armarQuerySelect(
					ControladorBase.getParticion(OfertaCampania.N_TABLA, Conf.PARTITION, "", input.getCodArea()),
					campos, condiciones, orden);
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst != null) {
				if (!rst.next()) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_OFERTASCAMPANIAS_819,
							null, nombreClase, nombreMetodo, null, input.getCodArea());

					output = new OutputOfertaCampania();
					output.setRespuesta(respuesta);
				} else {
					respuesta = new Respuesta();
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					tipoRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_RUTA, input.getCodArea());
					tipoPanel = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_PANEL,
							input.getCodArea());

					String idOfertaCampania = "";
					do {
						InputOfertaCampania item = new InputOfertaCampania();
						idOfertaCampania = rst.getString(OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID);
						item.setIdOfertaCampania(idOfertaCampania);
						item.setTipo(rst.getString(OfertaCampania.CAMPO_TIPO));
						item.setNombre(rst.getString(OfertaCampania.CAMPO_NOMBRE));
						item.setDescripcion(rst.getString(OfertaCampania.CAMPO_DESCRIPCION));
						item.setCantMaxPromocionales(rst.getString(OfertaCampania.CAMPO_CANT_MAX_PROMOCIONALES));
						item.setFechaDesde(UtileriasJava.formatStringDate(
								rst.getString(OfertaCampania.CAMPO_FECHA_DESDE), FECHAHORA2, FORMATO_FECHA));
						item.setFechaHasta(UtileriasJava.formatStringDate(
								rst.getString(OfertaCampania.CAMPO_FECHA_HASTA), FECHAHORA2, FORMATO_FECHA));
						item.setEstado(rst.getString(OfertaCampania.CAMPO_ESTADO));
						item.setCreado_el(
								UtileriasJava.formatStringDate(rst.getString(OfertaCampania.CAMPO_CREADO_EL)));
						item.setCreado_por(rst.getString(OfertaCampania.CAMPO_CREADO_POR));
						item.setModificado_el(
								UtileriasJava.formatStringDate(rst.getString(OfertaCampania.CAMPO_MODIFICADO_EL)));
						item.setModificado_por(rst.getString(OfertaCampania.CAMPO_MODIFICADO_POR));

						List<InputOfertaCampaniaDet> detalles = getDatosTablaHija(conn, idOfertaCampania, metodo,
								tipoPanel, tipoRuta, codArea, ID_PAIS);
						item.setOfertaCampaniaDet(detalles);

						list.add(item);
					} while (rst.next());

					output = new OutputOfertaCampania();
					output.setRespuesta(respuesta);
					output.setOfertaCampania(list);
				}
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return output;
	}

	/**
	 * Funci\u00F3n que obtiene los datos relacionados de la tabla hija mediante el
	 * id de la tabla padre.
	 * 
	 * @param conn
	 * @param idPadre
	 * @param input
	 * @param metodo
	 * @param tipoRuta
	 * @param tipoPanel
	 * @return OutputBodegaDTS
	 * @throws SQLException
	 */
	private static List<InputOfertaCampaniaDet> getDatosTablaHija(Connection conn, String idPadre, int metodo,
			String tipoPanel, String tipoRuta, String codArea, BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "getDatosTablaHija";
		String nombreClase = new CurrentClassGetter().getClassName();

		List<InputOfertaCampaniaDet> list = new ArrayList<InputOfertaCampaniaDet>();
		PreparedStatement pstmtIn = null;
		ResultSet rstIn = null;

		String camposInterno[] = CtrlOfertaCampania.obtenerCamposTablaHija(metodo);
		List<Filtro> condicionesInterno = CtrlOfertaCampania.obtenerCondicionesTablaHija(idPadre, ID_PAIS);

		String sql = queryDatosTHija(camposInterno, condicionesInterno);
		log.debug(sql);
		try {
			pstmtIn = conn.prepareStatement(sql);
			pstmtIn.setString(1, tipoPanel);
			pstmtIn.setBigDecimal(2, ID_PAIS);
			pstmtIn.setString(3, tipoRuta);
			pstmtIn.setBigDecimal(4, ID_PAIS);
			pstmtIn.setString(5, tipoPanel);
			pstmtIn.setBigDecimal(6, ID_PAIS);
			pstmtIn.setBigDecimal(7, ID_PAIS);
			pstmtIn.setString(8, tipoRuta);
			pstmtIn.setBigDecimal(9, ID_PAIS);
			pstmtIn.setBigDecimal(10, ID_PAIS);
			pstmtIn.setString(11, tipoPanel);
			pstmtIn.setBigDecimal(12, ID_PAIS);
			pstmtIn.setString(13, tipoRuta);
			pstmtIn.setBigDecimal(14, ID_PAIS);
			rstIn = pstmtIn.executeQuery();

			if (rstIn != null) {
				if (!rstIn.next()) {
					log.debug("No existen registros en la tabla hija con esos par\u00E9metros.");
					InputOfertaCampaniaDet item = new InputOfertaCampaniaDet();

					Respuesta respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSO_HIJO, null,
							nombreClase, nombreMetodo, null, codArea);

					item.setEstado(respuesta.getDescripcion());
					list.add(item);
				} else {
					do {
						InputOfertaCampaniaDet item = new InputOfertaCampaniaDet();
						item.setIdTipo(rstIn.getString(OfertaCampaniaDet.CAMPO_TCSCTIPOID));
						item.setTipo(rstIn.getString(OfertaCampaniaDet.CAMPO_TIPO));
						item.setNombreTipo(rstIn.getString("NOMBRETIPO"));
						item.setIdDTS(rstIn.getString("IDDTS"));
						item.setNombreDTS(rstIn.getString("NOMBREDTS"));
						item.setEstado(rstIn.getString(OfertaCampaniaDet.CAMPO_ESTADO));
						item.setCreado_por(rstIn.getString(OfertaCampaniaDet.CAMPO_CREADO_POR));
						item.setCreado_el(
								UtileriasJava.formatStringDate(rstIn.getString(OfertaCampaniaDet.CAMPO_CREADO_EL)));
						item.setModificado_por(rstIn.getString(OfertaCampaniaDet.CAMPO_MODIFICADO_POR));
						item.setModificado_el(
								UtileriasJava.formatStringDate(rstIn.getString(OfertaCampaniaDet.CAMPO_MODIFICADO_EL)));

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

	public static String queryDatosTHija(String camposInterno[], List<Filtro> condicionesInterno) {
		String sql = "SELECT ";
		sql += UtileriasBD.getCampos(camposInterno);
		sql += ", CASE " + "WHEN UPPER(TIPO) = ? THEN " + "(SELECT nombre FROM TC_SC_PANEL"
				+ " WHERE TCSCCATPAISID =?  AND tcscpanelid = tcsctipoid) " + "WHEN UPPER(TIPO) =? THEN "
				+ "(SELECT nombre FROM TC_SC_RUTA" + " WHERE TCSCCATPAISID = ? AND tcscrutaid =tcsctipoid) "
				+ "END AS NOMBRETIPO, " + "CASE " + "WHEN UPPER(TIPO) = ? THEN "
				+ "(SELECT NOMBRES FROM TC_SC_DTS WHERE TCSCCATPAISID = ?  AND TCSCDTSID = (SELECT TCSCDTSID FROM TC_SC_PANEL "
				+ "WHERE TCSCCATPAISID =? AND TCSCPANELID =tcsctipoid)) " + "WHEN UPPER(TIPO) =? THEN "
				+ "(SELECT NOMBRES FROM TC_SC_DTS WHERE TCSCCATPAISID = ? AND TCSCDTSID = (SELECT TCSCDTSID FROM TC_SC_RUTA "
				+ "WHERE TCSCCATPAISID = ? AND TCSCRUTAID = tcsctipoid)) " + "END AS NOMBREDTS, " + "CASE "
				+ "WHEN UPPER(TIPO) = ? THEN " + "(SELECT TCSCDTSID FROM TC_SC_PANEL "
				+ "WHERE TCSCCATPAISID =? AND TCSCPANELID =tcsctipoid) " + "WHEN UPPER(TIPO) = ? THEN "
				+ "(SELECT TCSCDTSID FROM TC_SC_RUTA " + "WHERE TCSCCATPAISID = ?  AND TCSCRUTAID = tcsctipoid) "
				+ "END AS IDDTS";

		sql += " FROM " + OfertaCampaniaDet.N_TABLA;
		sql += UtileriasBD.getCondiciones(condicionesInterno);

		return sql;
	}

	/**
	 * Funci\u00F3n que arma el query a utilizar al trabajar en m\u00E9todo POST.
	 * 
	 * @param conn
	 * @param input
	 * @param estadoAlta
	 * @return OutputOfertaCampania
	 * @throws SQLException
	 */
	public static OutputOfertaCampania doPost(Connection conn, InputOfertaCampania input, String estadoAlta,
			BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "doPost";
		String nombreClase = new CurrentClassGetter().getClassName();
		List<Filtro> condicionesExistencia = new ArrayList<Filtro>();
		Respuesta respuesta = null;
		OutputOfertaCampania output = new OutputOfertaCampania();
		boolean insertHijo = false;
		int idPadre = 0;

		condicionesExistencia = CtrlOfertaCampania.obtenerCondicionesExistencia(conn, input, Conf.METODO_POST, ID_PAIS);
		String existencia = UtileriasBD.verificarExistencia(conn,
				ControladorBase.getParticion(OfertaCampania.N_TABLA, Conf.PARTITION, "", input.getCodArea()),
				condicionesExistencia);
		if (new Integer(existencia) > 0) {
			log.error("Ya existe el recurso.");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_EXISTE_CAMPANIA_267, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			output = new OutputOfertaCampania();
			output.setRespuesta(respuesta);

			return output;
		}

		String sql = null;
		String campos[] = CtrlOfertaCampania.obtenerCamposGetPost(Conf.METODO_POST);
		List<String> inserts = CtrlOfertaCampania.obtenerInsertsPost(input, OfertaCampania.SEQUENCE, estadoAlta,
				ID_PAIS);

		sql = UtileriasBD.armarQueryInsert(OfertaCampania.N_TABLA, campos, inserts);

		try {
			conn.setAutoCommit(false);
			String generatedColumns[] = { OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID };
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
				insertHijo = doPostHijo(conn, idPadre, input, estadoAlta, ID_PAIS);

				if (insertHijo == true) {
					conn.commit();
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_OFERTACAMPANIA_47, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					output.setIdOfertaCampania(idPadre + "");
					output.setRespuesta(respuesta);
				} else {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
							nombreMetodo, null, input.getCodArea());

					output.setRespuesta(respuesta);
				}
			}
		} finally {
			if (insertHijo == false) {
				log.error("Rollback");
				conn.rollback();
			}
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
	private static boolean doPostHijo(Connection conn, int idPadre, InputOfertaCampania input, String estadoAlta,
			BigDecimal ID_PAIS) throws SQLException {
		String campos[] = CtrlOfertaCampania.obtenerCamposTablaHija(Conf.METODO_POST);
		List<String> inserts = CtrlOfertaCampania.obtenerInsertsPostHijo(idPadre, input, OfertaCampaniaDet.SEQUENCE,
				estadoAlta, ID_PAIS);

		String sql = UtileriasBD.armarQueryInsertAll(OfertaCampaniaDet.N_TABLA, campos, inserts);

		QueryRunner Qr = new QueryRunner();
		int res = Qr.update(conn, sql);
		if (res > 0) {
			return Qr != null;
		} else {
			conn.rollback();
			log.error("Rollback");
			return false;
		}
	}

	/**
	 * Funci\u00F3n que arma el query a utilizar al modificar datos en m\u00E9todos
	 * PUT y DELETE.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @param tipoRuta
	 * @param tipoPanel
	 * @param estadoAlta
	 * @return OutputOfertaCampania
	 * @throws SQLException
	 */
	public static OutputOfertaCampania doPutDel(Connection conn, InputOfertaCampania input, int metodo,
			String tipoPanel, String tipoRuta, String estadoAlta, BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "doPutDel";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta respuesta = null;
		OutputOfertaCampania output = null;
		boolean updDelHijo = false;
		boolean insertHijo = false;

		List<Filtro> condicionesExistencia = CtrlOfertaCampania.obtenerCondicionesExistencia(conn, input, metodo,
				ID_PAIS);
		String existencia = UtileriasBD.verificarExistencia(conn,
				ControladorBase.getParticion(OfertaCampania.N_TABLA, Conf.PARTITION, "", input.getCodArea()),
				condicionesExistencia);
		if (new Integer(existencia) < 1) {
			log.error("No existe el recurso.");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			output = new OutputOfertaCampania();
			output.setRespuesta(respuesta);

			return output;
		}

		String sql = null;

		String campos[][] = CtrlOfertaCampania.obtenerCamposPutDel(input);
		List<Filtro> condiciones = CtrlOfertaCampania.obtenerCondiciones(input, metodo, tipoPanel, tipoRuta, ID_PAIS);

		sql = UtileriasBD.armarQueryUpdate(OfertaCampania.N_TABLA, campos, condiciones);

		try {
			int i = 0;
			conn.setAutoCommit(false);
			QueryRunner Qr = new QueryRunner();
			i = Qr.update(conn, sql);

			if (i > 0) {
				updDelHijo = doPutDelHijo(conn, input.getIdOfertaCampania(), input, estadoAlta, input.getEstado(),
						ID_PAIS);

				if (updDelHijo == true) {
					if (input.getEstado().equalsIgnoreCase(estadoAlta)) {
						insertHijo = doPostHijo(conn, Integer.valueOf(input.getIdOfertaCampania()), input, estadoAlta,
								ID_PAIS);
					} else {
						insertHijo = true;
					}
				}

				if (insertHijo == true) {
					conn.commit();
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_MOD_OFERTACAMPANIA_48, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					output = new OutputOfertaCampania();
					output.setRespuesta(respuesta);
				} else {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					output = new OutputOfertaCampania();
					output.setRespuesta(respuesta);
				}
			} else {
				respuesta = new Respuesta();
				respuesta.setCodResultado("0");
				output = new OutputOfertaCampania();
				output.setRespuesta(respuesta);
			}
		} finally {
			if (updDelHijo == false || insertHijo == false) {
				log.error("Rollback");
				conn.rollback();
			}

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
	 * @param estadoAlta
	 * @param estado
	 * @return boolean
	 * @throws SQLException
	 */
	private static boolean doPutDelHijo(Connection conn, String idPadre, InputOfertaCampania input, String estadoAlta,
			String estado, BigDecimal ID_PAIS) throws SQLException {
		String sql = "";
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt4 = null;
		PreparedStatement pstmt5 = null;
		boolean resultadoOk = false;

		String campos[][] = CtrlOfertaCampania.obtenerCamposPutDelHijo(input, estado);

		List<Filtro> condiciones = new ArrayList<Filtro>();
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
				OfertaCampaniaDet.CAMPO_TCSCOFERTACAMPANIAID, idPadre));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampaniaDet.CAMPO_TCSCCATPAISID,
				ID_PAIS.toString()));

		if (estado.equalsIgnoreCase(estadoAlta)) {
			try {
				sql = UtileriasBD.armarQueryDelete(OfertaCampaniaDet.N_TABLA, condiciones);
				pstmt1 = conn.prepareStatement(sql);
				pstmt1.executeUpdate();
				resultadoOk = true;
			} finally {
				DbUtils.closeQuietly(pstmt1);
			}
		} else {
			try {
				sql = UtileriasBD.armarQueryUpdate(OfertaCampaniaDet.N_TABLA, campos, condiciones);
				pstmt3 = conn.prepareStatement(sql);
				pstmt3.executeUpdate();
			} finally {
				DbUtils.closeQuietly(pstmt3);
			}
			// se dan de baja los promocionales de la campania
			condiciones.clear();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					PromoOfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, idPadre));
			String camposUpdPromo[][] = {
					{ PromoOfertaCampania.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estado) },
					{ PromoOfertaCampania.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
					{ PromoOfertaCampania.CAMPO_MODIFICADO_POR,
							UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) } };
			sql = UtileriasBD.armarQueryUpdate(PromoOfertaCampania.N_TABLA, camposUpdPromo, condiciones);
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
			} finally {
				DbUtils.closeQuietly(pstmt);
			}
			// se obtienen todas las condiciones dependientes y se dan de baja
			condiciones.clear();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					Condicion.CAMPO_TCSCOFERTACAMPANIAID, idPadre));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Condicion.CAMPO_TCSCCATPAISID,
					ID_PAIS.toString()));
			String camposUpdCondiciones[][] = {
					{ Condicion.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estado) },
					{ Condicion.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
					{ Condicion.CAMPO_MODIFICADO_POR,
							UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) } };
			sql = UtileriasBD.armarQueryUpdate(Condicion.N_TABLA, camposUpdCondiciones, condiciones);
			try {
				pstmt2 = conn.prepareStatement(sql);
				pstmt2.executeUpdate();
			} finally {
				DbUtils.closeQuietly(pstmt2);
			}
			String[] campoSelect = { Condicion.CAMPO_TCSCCONDICIONID };
			String idsCondicion = UtileriasBD.armarQuerySelect(Condicion.N_TABLA, campoSelect, condiciones);

			// se dan de baja los detalles de las condiciones de la campania
			condiciones.clear();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, CondicionCampania.CAMPO_TCSCCONDICIONID,
					idsCondicion));
			String camposUpdCondCampania[][] = {
					{ CondicionCampania.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estado) },
					{ CondicionCampania.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
					{ CondicionCampania.CAMPO_MODIFICADO_POR,
							UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) } };
			try {
				sql = UtileriasBD.armarQueryUpdate(CondicionCampania.N_TABLA, camposUpdCondCampania, condiciones);
				pstmt4 = conn.prepareStatement(sql);
				pstmt4.executeUpdate();
			} finally {
				DbUtils.closeQuietly(pstmt4);
			}
			// se dan de baja los detalles de las condiciones de la oferta
			condiciones.clear();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, CondicionOferta.CAMPO_TCSCCONDICIONID,
					idsCondicion));
			String camposUpdCondOferta[][] = {
					{ CondicionOferta.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estado) },
					{ CondicionOferta.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
					{ CondicionOferta.CAMPO_MODIFICADO_POR,
							UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) } };
			try {
				sql = UtileriasBD.armarQueryUpdate(CondicionOferta.N_TABLA, camposUpdCondOferta, condiciones);
				pstmt5 = conn.prepareStatement(sql);
				pstmt5.executeUpdate();
			} finally {
				DbUtils.closeQuietly(pstmt5);
			}
			resultadoOk = true;
		}

		return resultadoOk;
	}
}
