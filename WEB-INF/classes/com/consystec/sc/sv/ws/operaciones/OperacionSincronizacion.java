package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.jornada.InputJornada;
import com.consystec.sc.ca.ws.input.sincronizacion.InputSincronizacion;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.jornada.OutputJornada;
import com.consystec.sc.ca.ws.output.sincronizacion.OutputSincronizacion;
import com.consystec.sc.sv.ws.metodos.CtrlSincronizacion;
import com.consystec.sc.sv.ws.orm.Dispositivo;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.Panel;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.orm.Sincronizacion;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.orm.VendedorPDV;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class OperacionSincronizacion {
	private OperacionSincronizacion() {
	}

	private static final Logger log = Logger.getLogger(OperacionSincronizacion.class);

	/**
	 * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @param estadoAlta
	 * @return
	 * @throws SQLException
	 */
	public static OutputSincronizacion doGet(Connection conn, InputSincronizacion input, int metodo, BigDecimal ID_PAIS)
			throws SQLException {
		String nombreMetodo = "doGet";
		String nombreClase = new CurrentClassGetter().getClassName();
		OutputSincronizacion output = new OutputSincronizacion();

		List<InputSincronizacion> list = new ArrayList<InputSincronizacion>();
		Respuesta respuesta = new Respuesta();

		PreparedStatement pstmt = null;
		ResultSet rst = null;

		List<Filtro> condiciones = new ArrayList<Filtro>();

		try {
			String tablas[] = {
					ControladorBase.getParticion(Sincronizacion.N_TABLA, Conf.PARTITION, "", input.getCodArea()),
					VendedorDTS.N_TABLA };

			String[][] campos = CtrlSincronizacion.obtenerCamposGet();

			condiciones = CtrlSincronizacion.obtenerCondiciones(input, metodo, ID_PAIS);
			condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Sincronizacion.N_TABLA,
					Sincronizacion.CAMPO_IDVENDEDOR, VendedorDTS.N_TABLA, VendedorDTS.CAMPO_VENDEDOR));

			String sql = UtileriasBD.armarQueryGetMultiple(campos, tablas, condiciones, null);

			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst != null) {
				if (!rst.next()) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_SINCRONIZACIONES_793,
							null, nombreClase, nombreMetodo, null, input.getCodArea());

					output.setRespuesta(respuesta);
				} else {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_GETDATOS12, null, nombreClase,
							nombreMetodo, null, input.getCodArea());

					do {
						InputSincronizacion item = new InputSincronizacion();
						item.setIdSincronizacion(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO,
								Sincronizacion.CAMPO_TCSCSINCVENDEDORID));
						item.setIdVendedor(
								UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Sincronizacion.CAMPO_IDVENDEDOR));
						item.setNombreVendedor(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, VendedorDTS.CAMPO_USUARIO));
						item.setIdDispositivo(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Sincronizacion.CAMPO_COD_DISPOSITIVO));
						item.setIdJornada(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO,
								Sincronizacion.CAMPO_TCSCJORNADAVENDID));
						item.setCreado_el(
								UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Sincronizacion.CAMPO_CREADO_EL));
						item.setCreado_por(rst.getString(Sincronizacion.CAMPO_CREADO_POR));
						item.setModificado_el(
								UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Sincronizacion.CAMPO_MODIFICADO_EL));
						item.setModificado_por(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Sincronizacion.CAMPO_MODIFICADO_POR));

						list.add(item);
					} while (rst.next());

					output = new OutputSincronizacion();
					output.setRespuesta(respuesta);
					output.setDatos(list);
				}
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return output;
	}

	/**
	 * Funci\u00F3n que arma el query a utilizar al trabajar en m\u00E9todo POST.
	 * 
	 * @param conn
	 * @param input
	 * @param estadoAlta
	 * @param tipoPanel
	 * @param tipoRuta
	 * @param estadoIniciada
	 * @param estadoFinalizada
	 * @param estadoLiquidada
	 * @return
	 * @throws SQLException
	 */
	public static OutputSincronizacion doPost(Connection conn, InputSincronizacion input, String estadoAlta,
			String tipoPanel, String tipoRuta, String estadoIniciada, String estadoLiquidada, BigDecimal ID_PAIS)
			throws SQLException {
		String nombreMetodo = "doPost";
		String nombreClase = new CurrentClassGetter().getClassName();
		String idTipo = "";
		String tipo = "";

		Respuesta respuesta = new Respuesta();
		OutputSincronizacion output = new OutputSincronizacion();
		String sql = null;

		try {
			conn.setAutoCommit(false);

			idTipo = input.getIdTipo();
			tipo = input.getTipo();

			// se valida que exista la ruta o panel
			if (tipo.equalsIgnoreCase(tipoRuta)) {
				sql = queryRutaPanel(input, estadoAlta, ID_PAIS);
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_RUTA_182, null,
						nombreClase, nombreMetodo, null, input.getCodArea());
			} else {
				sql = queryRutaPanel2(input, estadoAlta, ID_PAIS);
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_PANEL_181, null,
						nombreClase, nombreMetodo, null, input.getCodArea());
			}

			log.debug("Qry ruta-panel: " + sql);
			try (Statement stm = conn.createStatement(); ResultSet rst = stm.executeQuery(sql);) {

				if (rst.next()) {
					log.debug("Panel-Ruta: " + rst.getInt(1));
					if (rst.getInt(1) <= 0) {
						return output;
					}
				}
			}
			// se valida que exista el dispositivo y que pertenezca al tipo enviado
			sql = getQueryExisteDispositivo(input, estadoAlta, idTipo, tipo, ID_PAIS);
			log.debug("Qry dispositivo: " + sql);

			try (PreparedStatement pstmt = conn.prepareStatement(sql);) {
				try (ResultSet rst1 = pstmt.executeQuery();) {
					if (rst1.next() && rst1.getInt(1) <= 0) {
						log.debug("Dispositivo: " + rst1.getInt(1));
						respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_DISPOSITIVO_239,
								null, nombreClase, nombreMetodo, null, input.getCodArea());
						return output;
					}
				}

			}

			// se recorren el listado para validar elementos
			String vendedores = "";
			String jornadas = "";
			int numMensaje = 0;
			List<String> selects = new ArrayList<String>();
			for (int i = 0; i < input.getDatos().size(); i++) {
				vendedores += input.getDatos().get(i).getIdVendedor();
				jornadas += input.getDatos().get(i).getIdJornada();

				selects.add("SELECT COUNT(1), " + input.getDatos().get(i).getIdJornada() + " FROM "
						+ ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", input.getCodArea())
						+ " WHERE " + Jornada.CAMPO_VENDEDOR + " = " + input.getDatos().get(i).getIdVendedor() + " AND "
						+ Jornada.CAMPO_TCSCJORNADAVENID + " = " + input.getDatos().get(i).getIdJornada());

				if (i != input.getDatos().size() - 1) {
					vendedores += ",";
					jornadas += ",";
				}
			}

			for (int i = 0; i < selects.size(); i++) {
				log.trace("Select jornada: " + selects.get(i));
				try (PreparedStatement pstmt2 = conn.prepareStatement(selects.get(i));) {
					try (ResultSet rst2 = pstmt2.executeQuery();) {
						if (rst2.next()) {
							do {
								if (rst2.getInt(1) <= 0) {
									respuesta = new ControladorBase().getMensaje(
											Conf_Mensajes.MSJ_ERROR_JORNADA_VENDEDOR_744, null, nombreClase,
											nombreMetodo, rst2.getInt(2) + ".", input.getCodArea());
									return output;
								}
							} while (rst2.next());
						}
					}
				}
			}

			// se valida que los vendedores existan entre los permitidos de la panel o ruta

			if (tipo.equalsIgnoreCase(tipoPanel)) {
				// validacion de vendedores cuando es tipo panel
				sql = queryRutaPVend(idTipo, tipo, vendedores, tipoPanel, estadoAlta, ID_PAIS);
				numMensaje = Conf_Mensajes.MSJ_ERROR_VENDEDORES_PANEL_738;

			} else {
				// validacion de vendedores cuando es tipo ruta
				sql = queryRutaPVend(idTipo, tipo, vendedores, tipoPanel, estadoAlta, ID_PAIS);

				numMensaje = Conf_Mensajes.MSJ_ERROR_VEND_RUTA_195;
			}

			log.debug("Qry vendedores: " + sql);
			try (PreparedStatement pstmt3 = conn.prepareStatement(sql);) {
				try (ResultSet rst3 = pstmt3.executeQuery();) {
					vendedores = "";
					if (rst3.next()) {
						do {
							vendedores += ControladorBase.getNombreVendedor(conn, rst3.getString("VEND"), ID_PAIS)
									+ ", ";
						} while (rst3.next());
					}
				}
			}
			if (!vendedores.equals("")) {
				respuesta = new ControladorBase().getMensaje(numMensaje, null, nombreClase, nombreMetodo,
						vendedores.substring(0, vendedores.length() - 2), input.getCodArea());
				return output;
			}

			// se valida que la jornada pertenezca al vendedor y que este iniciada
			StringBuilder sql1 = new StringBuilder();
			sql1.append("SELECT TO_NUMBER(COLUMN_VALUE) AS JORN ");
			sql1.append("FROM TABLE(SYS.DBMS_DEBUG_VC2COLL(" + jornadas + ")) ");
			sql1.append("MINUS ");
			sql1.append("SELECT tcscjornadavenid");
			sql1.append(
					" FROM " + ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", input.getCodArea()));
			sql1.append(" WHERE idtipo = " + idTipo);
			sql1.append(" AND UPPER(descripcion_tipo) = UPPER('" + tipo + "')");
			sql1.append(" AND UPPER(estado) = UPPER('" + estadoIniciada + "')");
			sql1.append(" AND (UPPER(estado_liquidacion) != UPPER('" + estadoLiquidada + "')");
			sql1.append(" OR estado_liquidacion IS NULL" + ")");
			sql1.append(" AND tcsccatpaisid = " + ID_PAIS);

			log.debug("Qry jornadas: " + sql1);
			try (PreparedStatement pstmt4 = conn.prepareStatement(sql1.toString())) {
				try (ResultSet rst4 = pstmt4.executeQuery();) {
					jornadas = "";
					if (rst4.next()) {
						do {
							jornadas += rst4.getString("JORN") + ", ";
						} while (rst4.next());
					}
				}
			}

			if (!jornadas.equals("")) {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_JORNADAS_ESTADOS_745, null,
						nombreClase, nombreMetodo, jornadas.substring(0, jornadas.length() - 2), input.getCodArea());
				return output;
			}

			String campos[] = CtrlSincronizacion.obtenerCamposPost();

			try (Statement stmtInserts = conn.createStatement()) {
				for (int i = 0; i < input.getDatos().size(); i++) {
					List<String> inserts = CtrlSincronizacion.obtenerInsertsPost(input, Sincronizacion.SEQUENCE,
							input.getDatos().get(i).getIdVendedor(), input.getDatos().get(i).getIdJornada(), ID_PAIS);

					sql = UtileriasBD.armarQueryInsert(Sincronizacion.N_TABLA, campos, inserts);

					stmtInserts.addBatch(sql);
				}

				int[] insertCounts = stmtInserts.executeBatch();

				if (UtileriasJava.validarBatch(1, insertCounts)) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_SINCRONIZACION_56, null,
							nombreClase, nombreMetodo, null, input.getCodArea());
				} else {
					log.debug("Rollback");
					conn.rollback();
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
							nombreMetodo, null, input.getCodArea());
				}
			}

			conn.commit();

		} finally {
			output.setRespuesta(respuesta);

			conn.setAutoCommit(true);

		}

		return output;
	}

	public static String getQueryExisteDispositivo(InputSincronizacion input, String estadoAlta, String idTipo,
			String tipo, BigDecimal ID_PAIS) {
		String sql = "SELECT COUNT(1) FROM TC_SC_DISPOSITIVO WHERE UPPER(codigo_dispositivo) = UPPER('"
				+ input.getIdDispositivo() + "') AND UPPER(estado) = UPPER('" + estadoAlta + "') AND responsable = "
				+ idTipo + " AND UPPER(tipo_responsable) = UPPER('" + tipo + "') AND TCSCCATPAISID = " + ID_PAIS;
		return sql;
	}

	public static String queryRutaPVend(String idTipo, String tipo, String vendedores, String tipoPanel,
			String estadoAlta, BigDecimal ID_PAIS) {

		String sql = "";

		// se valida que los vendedores existan entre los permitidos de la panel o ruta
		sql = "SELECT TO_NUMBER(COLUMN_VALUE) AS VEND " + "FROM TABLE(SYS.DBMS_DEBUG_VC2COLL(" + vendedores + ")) "
				+ "MINUS ";
		if (tipo.equalsIgnoreCase(tipoPanel)) {
			// validacion de vendedores cuando es tipo panel
			sql += "SELECT " + VendedorPDV.CAMPO_VENDEDOR + " FROM " + VendedorPDV.N_TABLA + " WHERE "
					+ VendedorPDV.CAMPO_IDTIPO + " = " + idTipo + " AND UPPER(" + VendedorPDV.CAMPO_TIPO + ") = UPPER('"
					+ tipoPanel + "')" + " AND UPPER(" + VendedorPDV.CAMPO_ESTADO + ") = ('" + estadoAlta.toUpperCase()
					+ "')" + " AND TCSCCATPAISID = " + ID_PAIS;

		} else {
			// validacion de vendedores cuando es tipo ruta
			sql += "SELECT " + Ruta.CAMPO_SEC_USUARIO_ID + " AS VEND" + " FROM " + Ruta.N_TABLA + " WHERE "
					+ Ruta.CAMPO_TC_SC_RUTA_ID + " = " + idTipo + " AND UPPER(" + Ruta.CAMPO_ESTADO + ") = ('"
					+ estadoAlta.toUpperCase() + "')" + " AND TCSCCATPAISID = " + ID_PAIS;

		}

		return sql;
	}

	public static String queryRutaPanel(InputSincronizacion input, String estadoAlta, BigDecimal ID_PAIS) {
		String sql = "";

		sql = "SELECT COUNT(1) FROM " + Ruta.N_TABLA + " WHERE " + Ruta.CAMPO_TC_SC_RUTA_ID + " = " + input.getIdTipo()
				+ " AND UPPER(" + Ruta.CAMPO_ESTADO + ") = ('" + estadoAlta.toUpperCase() + "') AND TCSCCATPAISID = "
				+ ID_PAIS;

		return sql;
	}

	public static String queryRutaPanel2(InputSincronizacion input, String estadoAlta, BigDecimal ID_PAIS) {
		String sql = "";

		sql = "SELECT COUNT(1) FROM " + Panel.N_TABLA + " WHERE " + Panel.CAMPO_TCSCPANELID + " = " + input.getIdTipo()
				+ " AND UPPER(" + Panel.CAMPO_ESTADO + ") = ('" + estadoAlta.toUpperCase() + "') AND TCSCCATPAISID = "
				+ ID_PAIS;

		return sql;
	}

	public static OutputSincronizacion doGetFaltantes(Connection conn, InputSincronizacion input, String estadoAlta,
			String tipoPanel, BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "doGetFaltantes";
		String nombreClase = new CurrentClassGetter().getClassName();
		OutputSincronizacion output = new OutputSincronizacion();

		InputJornada inputJornada = new InputJornada();
		inputJornada.setCodArea(input.getCodArea());
		inputJornada.setUsuario(input.getUsuario());
		inputJornada.setIdJornada(input.getIdJornada());

		String estadoProcesoSiniestro = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SINIESTROS,
				Conf.SINIESTRO_ESTADO_EN_PROCESO, input.getCodArea());
		String estadoSiniestrado = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SINIESTROS,
				Conf.SINIESTRO_ESTADO_SINIESTRADO, input.getCodArea());
		String diferencia = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.DIFERENCIA_HORARIO,
				input.getCodArea());

		List<Filtro> condiciones = new ArrayList<Filtro>();
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCJORNADAVENID,
				input.getIdJornada()));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCCATPAISID,
				ID_PAIS.toString()));

		String[] campos = { Jornada.CAMPO_TCSCJORNADAVENID, Jornada.CAMPO_IDTIPO, Jornada.CAMPO_DESCRIPCION_TIPO,
				Jornada.CAMPO_FECHA, Jornada.CAMPO_TCSCDTSID };

		// se obtienen los datos de la jornada
		Map<String, String> datosJornada = UtileriasBD.getSingleFirstData(conn,
				ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", input.getCodArea()), campos,
				condiciones);
		if (datosJornada == null || datosJornada.isEmpty()) {
			output.setRespuesta(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTEN_JORNADAS_747, null,
					nombreClase, nombreMetodo, null, input.getCodArea()));

			return output;
		} else {
			OutputJornada respuesta = OperacionJornadaMasiva.validarSincronizaciones(conn, inputJornada, datosJornada,
					tipoPanel, estadoAlta, estadoProcesoSiniestro, estadoSiniestrado, diferencia, ID_PAIS);
			if (respuesta.getRespuesta().getDescripcion().equalsIgnoreCase("OK")) {
				output.setRespuesta(new ControladorBase().getMensaje(Conf_Mensajes.OK_GETDATOS12, null, nombreClase,
						nombreMetodo, null, input.getCodArea()));

			} else {
				output.setVendedores(respuesta.getJornadas());
				output.setRespuesta(respuesta.getRespuesta());
			}
		}

		return output;
	}
}
