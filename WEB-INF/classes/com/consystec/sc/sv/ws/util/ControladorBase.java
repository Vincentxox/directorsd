package com.consystec.sc.sv.ws.util;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.util.ConfiguracionExcepcion;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.HistoricoInv;
import com.consystec.sc.sv.ws.orm.MensajeVW;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.ericsson.sdr.utils.Country;
import com.ericsson.sdr.utils.JndiUtils;
import com.google.gson.GsonBuilder;

public class ControladorBase {
	protected static final Logger log = Logger.getLogger(ControladorBase.class);
	String nombreAplicacion = "APPMOVIL";
	private static final String NOMBRE_APLICACION = "WsDirectorSidra";// TODO cambiar nombre de aplicacion

	// *//utiles para registrar log de tiempo en servicio local
	public static Date horaInicio = null;
	public static Date horaFin = null;
	public static String jsonRequest = "";
	public static String jsonResponse = "";
	public static long horaInicioMillis = 0;
	public static long elapsedTimeService = 0;// */
	static HttpServletRequest requestGlobal;

	// LOG4J
	static PatternLayout layout = null;
	static DailyRollingFileAppender rollingAppender = null;
	static Logger rootLogger = null;

	public static void identificarProceso(Connection conn) {
		CallableStatement cs = null;
		try {
			cs = conn.prepareCall("{ call dbms_application_info.set_module(?,?) }");
			cs.setString(1, NOMBRE_APLICACION);
			cs.setString(2, null);
			cs.execute();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (cs != null)
					cs.close();
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * M\u00E9todo para obtener conexi\u00F3n a la BD regional de Sidra.
	 * 
	 * @return
	 */
	public DataSource obtenerDtsRegional() {
		DataSource dts = null;
		try {
			Context contexto = new InitialContext();
			dts = (DataSource) contexto.lookup(JndiUtils.JNDI_SIDRA);

		} catch (NamingException e) {
			log.error(e.getMessage(), e);
		}
		return dts;
	}

	public Connection getConnRegional() throws SQLException {
		Connection conn = obtenerDtsRegional().getConnection();
		identificarProceso(conn);
		return conn;
	}

	public DataSource obtenerDtsPorta() {
		DataSource dts = null;
		try {
			Context contexto = new InitialContext();
			dts = (DataSource) contexto.lookup("java:comp/env/jdbc/porta");

		} catch (NamingException e) {
			log.error(e.getMessage(), e);
		}
		return dts;
	}

	public Connection getConnPorta() throws SQLException {
		Connection conn = obtenerDtsPorta().getConnection();
		identificarProceso(conn);
		return conn;
	}

	/**
	 * Para obtener ID de pa\u00EDs segun c\u00F3digo de \u00E9rea.
	 * 
	 * @param conn
	 * @param codArea
	 * @throws Exception
	 */
	public BigDecimal getIdPais(Connection conn, String codArea) throws Exception {
		String sql;
		BigDecimal idPais = null;
		PreparedStatement stm = null;
		ResultSet rst = null;
		String mensaje = "El código de area no es correcto.";
		try {
			if (codArea == null || "".equals(codArea) || "0".equals(codArea)) {
				throw new Exception(mensaje);
			}

			sql = "SELECT TCSCCATPAISID FROM TC_SC_CAT_PAIS WHERE AREA =? ";
			stm = conn.prepareStatement(sql);
			stm.setString(1, codArea);
			rst = stm.executeQuery();

			if (rst.next()) {
				idPais = rst.getBigDecimal("TCSCCATPAISID");

			} else {
				idPais = BigDecimal.ZERO;
				List<LogSidra> listaLog = new ArrayList<LogSidra>();
				listaLog.add(ControladorBase.addLog(Conf.LOG_INSERT, "getIdPais", "0", Conf.LOG_TIPO_NINGUNO,
						"Problema al obtener ID de país.", mensaje));
				UtileriasJava.doInsertLog(listaLog, "WS_SIDRA", "0");
				throw new Exception(mensaje);
			}
			appLogDailyFile();
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(stm);
		}
		return idPais;
	}

	public static String getPais(String codArea) {
		String pais = "";
		switch (new Integer(codArea)) {
		case 502:
			pais = Conf.MAPAS_GUATEMALA;
			break;
		case 503:
			pais = Conf.MAPAS_EL_SALVADOR;
			break;
		case 505:
			pais = Conf.MAPAS_NICARAGUA;
			break;
		case 506:
			pais = Conf.MAPAS_COSTARICA;
			break;
		case 507:
			pais = Conf.MAPAS_PANAMA;
			break;
		}

		return pais;
	}

	public static String getParticion(String tabla, String tipoPart, String fechaId, String codArea) {
		String resultado = "";
		String fechaParticion = "";
		SimpleDateFormat FECHAHORA2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat FORMATO_FECHA_CORTA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
		if (Conf.PARTITION.equalsIgnoreCase(tipoPart)) {
			resultado = " " + tabla + " PARTITION(" + getPais(codArea) + ") ";
		} else if (Conf.SUBPARTITION.equalsIgnoreCase(tipoPart)) {
			fechaParticion = FORMATO_FECHA_CORTA.format(fechaId);
			resultado = " " + tabla + " SUBPARTITION(D" + getPais(codArea) + fechaParticion + ") ";
		} else if (Conf.PARTITION_DATE.equalsIgnoreCase(tipoPart)) {
			Date fecha = UtileriasJava.parseDate(fechaId, FECHAHORA2);
			fechaParticion = FORMATO_FECHA_CORTA.format(fecha);
			resultado = " " + tabla + " PARTITION(D" + fechaParticion + ") ";
		}

		return resultado;
	}

	public static void appLogDailyFile() {
		// HttpSession session =
		// com.consystec.sc.ca.ws.util.ControladorBase.getRequestGlobal().getSession();

		// String path = session.getServletContext().getInitParameter("PATH_LOG4J");
		// String level =
		// session.getServletContext().getInitParameter(Conf.LEVEL_LOG4J);
		// String file =
		// session.getServletContext().getInitParameter(Conf.LOG4J_FILENAME);
		// String sizeFile
		// =session.getServletContext().getInitParameter(Conf.LOG4J_SIZE);
		// String fileLimit =
		// session.getServletContext().getInitParameter(Conf.LOG4J_LIMIT);
		// String conversionPattern
		// =session.getServletContext().getInitParameter(Conf.LOG4J_CONVERSIONPATTERN);

		String path = "/app/wlcc/logs/sidra_cam/";
		String level = "ALL";
		String file = "sidradirector";
		String sizeFile = "50MB";
		String fileLimit = "15";
		String conversionPattern = "[%p] %d %c %M - %m%n";

		System.out.println("con variable context y sesion:" + path);

		try {
			Context ctx = new InitialContext();

			System.out.println("--SYSTEM.OUT--");
			System.out.println("logPath" + path);
			System.out.println("logfileName" + file);
			System.out.println("logPattern" + conversionPattern);
			System.out.println("logSizeFile" + sizeFile);
			System.out.println("logFileLimit" + fileLimit);
			System.out.println("loglevel" + level);
			System.out.println("--SYSTEM.OUT--");

			PatternLayout layout = new PatternLayout(conversionPattern);

			RollingFileAppender fileAppender = (RollingFileAppender) Logger.getRootLogger()
					.getAppender(NOMBRE_APLICACION);
			if (null == fileAppender) {
				fileAppender = new RollingFileAppender();
				fileAppender.setName(NOMBRE_APLICACION);
			}
			fileAppender.setFile(path + file + ".log");
			fileAppender.setLayout(layout);
			fileAppender.setMaxFileSize(sizeFile);
			fileAppender.setMaxBackupIndex(Integer.parseInt(fileLimit));
			fileAppender.setAppend(true);
			fileAppender.activateOptions();

			if (level != null && !"".equals(level) && Conf.LOG4J_OFF.equals(level)) {
				org.apache.log4j.Logger.getRootLogger().setLevel(Level.OFF);
			} else if (level != null && !"".equals(level) && Conf.LOG4J_FATAL.equals(level)) {
				org.apache.log4j.Logger.getRootLogger().setLevel(Level.FATAL);
			} else if (level != null && !"".equals(level) && Conf.LOG4J_ERROR.equals(level)) {
				org.apache.log4j.Logger.getRootLogger().setLevel(Level.ERROR);
			} else if (level != null && !"".equals(level) && Conf.LOG4J_WARN.equals(level)) {
				org.apache.log4j.Logger.getRootLogger().setLevel(Level.WARN);
			} else if (level != null && !"".equals(level) && Conf.LOG4J_INFO.equals(level)) {
				org.apache.log4j.Logger.getRootLogger().setLevel(Level.INFO);
			} else if (level != null && !"".equals(level) && Conf.LOG4J_DEBUG.equals(level)) {
				org.apache.log4j.Logger.getRootLogger().setLevel(Level.DEBUG);
			} else if (level != null && !"".equals(level) && Conf.LOG4J_TRACE.equals(level)) {
				org.apache.log4j.Logger.getRootLogger().setLevel(Level.TRACE);
			} else {
				org.apache.log4j.Logger.getRootLogger().setLevel(Level.ALL);
			}

			org.apache.log4j.Logger.getRootLogger().removeAppender(ConsoleAppender.SYSTEM_ERR);
			org.apache.log4j.Logger.getRootLogger().removeAppender(ConsoleAppender.SYSTEM_OUT);
			org.apache.log4j.Logger.getRootLogger().addAppender(fileAppender);
		} catch (Exception e) {
			log.error(e, e);
		}

	}

	/**
	 * @deprecated
	 */
	public static String getParticion(int tabla, String tipoPart, String id, String codArea) {
		String resultado = "";
		SimpleDateFormat FORMATO_FECHA_CORTA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
		String fecha = FORMATO_FECHA_CORTA.format(new Date().getTime());

		switch (new Integer(tabla)) {
		case Conf.TC_SC_ASIGNACION_DET:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_ASIGNACION_DET PARTITION(D" + fecha + ") ";
			}
			break;
		case Conf.TC_SC_ASIGNACION_RESERVA:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_ASIGNACION_RESERVA PARTITION(" + getPais(codArea) + ") ";
			} else if (Conf.SUBPARTITION.equals(tipoPart)) {
				resultado = " TC_SC_ASIGNACION_RESERVA SUBPARTITION(D" + getPais(codArea) + fecha + ") ";
			}
			break;
		case Conf.TC_SC_BODEGAVIRTUAL:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_BODEGAVIRTUAL PARTITION(" + getPais(codArea) + ") ";
			}
			break;
		case Conf.TC_SC_CANT_INV_JORNADA:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_CANT_INV_JORNADA PARTITION(" + getPais(codArea) + ") ";
			}
			break;
		case Conf.TC_SC_CLIENTEF_SCL:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_CLIENTEF_SCL PARTITION(" + getPais(codArea) + ") ";
			}
			break;
		case Conf.TC_SC_CTA_DEBE_DTS:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_CTA_DEBE_DTS PARTITION(" + getPais(codArea) + ") ";
			} else if (Conf.SUBPARTITION.equals(tipoPart)) {
				resultado = " TC_SC_CTA_DEBE_DTS SUBPARTITION(D" + getPais(codArea) + fecha + ") ";
			}
			break;
		case Conf.TC_SC_CTA_DTS_RESUMEN:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_CTA_DTS_RESUMEN PARTITION(" + getPais(codArea) + ") ";
			} else if (Conf.SUBPARTITION.equals(tipoPart)) {
				resultado = " TC_SC_CTA_DTS_RESUMEN SUBPARTITION(D" + getPais(codArea) + fecha + ") ";
			}
			break;
		case Conf.TC_SC_CTA_HABER_DTS:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_CTA_HABER_DTS PARTITION(" + getPais(codArea) + ") ";
			} else if (Conf.SUBPARTITION.equals(tipoPart)) {
				resultado = " TC_SC_CTA_HABER_DTS SUBPARTITION(D" + getPais(codArea) + fecha + ") ";
			}
			break;
		case Conf.TC_SC_HISTORICO_INVSIDRA:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_HISTORICO_INVSIDRA PARTITION(" + getPais(codArea) + ") ";
			} else if (Conf.SUBPARTITION.equals(tipoPart)) {
				resultado = " TC_SC_HISTORICO_INVSIDRA SUBPARTITION(D" + getPais(codArea) + fecha + ") ";
			}
			break;
		case Conf.TC_SC_INVENTARIO:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_INVENTARIO PARTITION(B" + id + ") ";
			}
			break;
		case Conf.TC_SC_JORNADA_VEND:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_JORNADA_VEND PARTITION(" + getPais(codArea) + ") ";
			} else if (Conf.SUBPARTITION.equals(tipoPart)) {
				resultado = " TC_SC_JORNADA_VEND SUBPARTITION(D" + getPais(codArea) + fecha + ") ";
			}
			break;
		case Conf.TC_SC_LOG_SIDRA:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_LOG_SIDRA PARTITION(" + getPais(codArea) + ") ";
			} else if (Conf.SUBPARTITION.equals(tipoPart)) {
				resultado = " TC_SC_LOG_SIDRA SUBPARTITION(D" + getPais(codArea) + fecha + ") ";
			}
			break;
		case Conf.TC_SC_OFERTA_CAMPANIA:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_OFERTA_CAMPANIA PARTITION(" + getPais(codArea) + ") ";
			}
			break;
		case Conf.TC_SC_PUNTOVENTA:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_PUNTOVENTA PARTITION(D" + id + ") ";
			}
			break;
		case Conf.TC_SC_REMESA:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_REMESA PARTITION(" + getPais(codArea) + ") ";
			} else if (Conf.SUBPARTITION.equals(tipoPart)) {
				resultado = " TC_SC_REMESA SUBPARTITION(D" + getPais(codArea) + fecha + ") ";
			}
			break;
		case Conf.TC_SC_REPORTE_XZ:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_REPORTE_XZ PARTITION(" + getPais(codArea) + ") ";
			} else if (Conf.SUBPARTITION.equals(tipoPart)) {
				resultado = " TC_SC_REPORTE_XZ SUBPARTITION(D" + getPais(codArea) + fecha + ") ";
			}
			break;
		case Conf.TC_SC_SINC_VENDEDOR:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = " TC_SC_SINC_VENDEDOR PARTITION(" + getPais(codArea) + ") ";
			} else if (Conf.SUBPARTITION.equals(tipoPart)) {
				resultado = " TC_SC_SINC_VENDEDOR SUBPARTITION(D" + getPais(codArea) + fecha + ") ";
			}
			break;
		case Conf.TC_SC_SOLICITUD:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = "";
			} else if (Conf.SUBPARTITION.equals(tipoPart)) {
				resultado = "";
			}
			break;
		case Conf.TC_SC_VEND_DTS:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = "";
			} else if (Conf.SUBPARTITION.equals(tipoPart)) {
				resultado = "";
			}
			break;
		case Conf.TC_SC_VENTA:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = "";
			} else if (Conf.SUBPARTITION.equals(tipoPart)) {
				resultado = "";
			}
			break;
		case Conf.TC_SC_VENTA_DET:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = "";
			} else if (Conf.SUBPARTITION.equals(tipoPart)) {
				resultado = "";
			}
			break;
		case Conf.TC_SC_VISITA:
			if (Conf.PARTITION.equals(tipoPart)) {
				resultado = "";
			} else if (Conf.SUBPARTITION.equals(tipoPart)) {
				resultado = "";
			}
			break;
		}

		return resultado;
	}

	/*****************************************************************************************
	 * Metodo para armar mensaje de Mensaje retornando un objeto de la clase Mensaje
	 * 
	 * @param
	 * @author sbarrios
	 ******************************************************************************************/
	public Respuesta getMensaje(int Adv, String Exception, String Clase, String Metodo, String razon, String codArea) {
		String metodo = "getMensaje";
		Respuesta objAdv = new Respuesta();

		String Razon = razon == null ? "" : " " + razon;
		Connection conn = null;
		try {
			conn = getConnRegional();
			log.trace("Cod Mensaje: " + Adv);
			/*
			 * obteniendo descripcion, codResultado, mostrar, tipoexcepcion de la tabla de
			 * advertencias y excepciones según la configuración para cada país
			 */

			List<MensajeVW> Registro = new MensajeVWDAO().select(conn, new BigDecimal(Adv), codArea);
			Iterator<MensajeVW> iAdv = null;
			MensajeVW advertencia = null;
			log.trace("Mensajes encontrados: " + Registro.size());
			if (Registro != null && Registro.size() > 0) {
				iAdv = Registro.iterator();

				while (iAdv.hasNext()) {
					advertencia = iAdv.next();
					objAdv.setCodResultado(advertencia.getCodresultado().equals("0") ? "0"
							: "" + advertencia.getCodresultado().intValue());
					objAdv.setDescripcion(advertencia.getDescripcion().toString() == null ? ""
							: "" + advertencia.getDescripcion().toString() + Razon);
					objAdv.setMostrar(
							advertencia.getMostrar().equals("0") ? "0" : "" + advertencia.getMostrar().intValue());
					objAdv.setTipoExepcion(
							advertencia.getTipo_excepcion() == null ? "" : advertencia.getTipo_excepcion());
				}
			} else {
				objAdv.setCodResultado("" + Conf_Mensajes.MENSAJE_DEFAULT);
				objAdv.setDescripcion(Conf_Mensajes.MENSAJE_DEFAULT_TXT + Razon);
				objAdv.setMostrar("1");
			}

			objAdv.setClase(Clase == null ? " " : Clase);
			objAdv.setMetodo(Metodo == null ? " " : Metodo);
			objAdv.setExcepcion(Exception == null ? " " : Exception);

		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			objAdv.setExcepcion(e.getMessage());
			objAdv.setClase(this.getClass().getName());
			objAdv.setMetodo(metodo);
			objAdv.setTipoExepcion("Excepcion SQL");
			objAdv.setCodResultado("" + Conf_Mensajes.MENSAJE_DEFAULT);
			objAdv.setDescripcion(Conf_Mensajes.MENSAJE_DEFAULT_TXT);

		} finally {
			DbUtils.closeQuietly(conn);
		}

		return objAdv;
	}

	/*************************************************
	 * Metodo para verificar si la fecha es correcta
	 *************************************************/
	public boolean isFecha(String fecha) {
		Pattern pat = null;
		Matcher mat = null;
		pat = Pattern.compile("^[0-3][0-9]\\/[0-1][1-9]\\/\\d{4}$");
		mat = pat.matcher(fecha);
		boolean resultado = mat.find();
		log.trace("ES FECHA: " + fecha + " " + resultado);
		return resultado;
	}

	/*************************************************
	 * Metodo para verificar si la fecha y hora es correcta
	 *************************************************/
	public boolean isFechaHora(String fecha) {
		Pattern pat = null;
		Matcher mat = null;
		pat = Pattern.compile("^[0-3][0-9]\\/[0-1][0-9]\\/\\d{4}(\\s(0[1-9]|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d))?$");
		mat = pat.matcher(fecha);
		boolean resultado = mat.find();
		log.trace("ES FECHA: " + fecha + " " + resultado);
		return resultado;
	}

	/*************************************************
	 * Metodo para verificar si es n\u00FAmero
	 *************************************************/
	public boolean isDecimal(String numero) {
		Pattern pat = null;
		Matcher mat = null;
		pat = Pattern.compile("^\\d{1,15}\\.*\\d*$");
		mat = pat.matcher(numero);
		boolean resultado = mat.find();
		log.trace("ES DECIMAL: " + numero + " " + resultado);
		return resultado;
	}

	/*************************************************************
	 * Metodo para verificar si es número con Notacion Cientificia
	 *************************************************************/
	public boolean isNotCientif(String numero) {
		Pattern pat = null;
		Matcher mat = null;
		pat = Pattern.compile("^[-+]?[0-9]*.?[0-9]+([eE][-+]?[0-9]+)?$");
		mat = pat.matcher(numero);
		boolean resultado = mat.find();
		log.trace("ES NOTACION CIENTIFICA: " + numero + " " + resultado);
		return resultado;
	}

	/*************************************************
	 * Metodo para verificar longitud y latitud
	 *************************************************/
	public boolean isValido(String numero) {
		Pattern pat = null;
		Matcher mat = null;
		pat = Pattern.compile("^-?[0-9]+(.[0-9]*)?$");
		mat = pat.matcher(numero);
		boolean resultado = mat.find();
		log.trace("ES LAT/LONG VALIDA: " + numero + " " + resultado);
		return resultado;

	}

	/*************************************************
	 * Metodo para verificar si es n\u00FAmero
	 *************************************************/
	public static boolean isNumeric(String numero) {
		Pattern pat = null;
		Matcher mat = null;
		pat = Pattern.compile("^\\d*$");
		mat = pat.matcher(numero);
		boolean resultado = mat.find();
		log.trace("ES NUMERO: " + numero + " " + resultado);
		return resultado;
	}

	/*************************************************
	 * Metodo para verificar si es contiene un nombre
	 *************************************************/
	public boolean isNombre(String cadena) {
		Pattern pat = null;
		Matcher mat = null;
		pat = Pattern.compile("^([A-Z][a-z]+(\\s)*)+$");
		mat = pat.matcher(cadena);
		boolean resultado = mat.find();
		log.trace("ES TEXTO CADENA: " + cadena + " " + resultado);
		return resultado;
	}

	/*************************************************
	 * Metodo para verificar si es contiene solo letras
	 *************************************************/
	public boolean isString(String cadena) {
		Pattern pat = null;
		Matcher mat = null;
		pat = Pattern.compile("^([A-Za-z0-9])+$");
		mat = pat.matcher(cadena);
		boolean resultado = mat.find();
		log.trace("ES TEXTO: " + resultado);
		return resultado;
	}

	/********************************************************
	 * M\u00E9todo para obtener Parametros est\u00E9ndar de la aplicaci\u00F3n
	 * 
	 * @param parametro:
	 *            nombre del parametro que se desea obtener, tipo: indica si son
	 *            parametros para estados de orden o generales, area: contiene el
	 *            codigo de area del pais que se esta trabajando
	 ************************************************************/
	public String getParametro(String parametro, int tipo, String codArea) throws InterruptedException {
		String resultado = "";

		Connection conn = null;
		try {
			try {
				conn = getConnRegional();

				if (tipo == Conf.TIPO_PARAMETRO) {
					resultado = getParametroConf(conn, parametro);
				}
			} finally {
				if (conn != null)
					DbUtils.closeQuietly(conn);
				conn = null;
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			Respuesta Advertencia = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().getName(),
					"getParametro", null, codArea);
			return Advertencia.toString();
		}
		return resultado;
	}

	public String getParametroConf(Connection conn, String nombre) throws SQLException {
		PreparedStatement stm = null;
		ResultSet rst = null;

		String ret = "";
		String sql = "SELECT NOMBRE, VALOR " + " FROM TC_SC_CONFIGURACION " + " WHERE  UPPER(NOMBRE) = UPPER(?)";

		try {
			stm = conn.prepareStatement(sql);
			stm.setString(1, nombre);
			rst = stm.executeQuery();
			if (rst.next()) {
				ret = rst.getString(2);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(stm);
		}
		return ret;
	}

	/*********************************************************************************************
	 * Metodo utilizado para obtener la longitud de n\u00FAmero de telefono y
	 * documento de identificaci\u00F3n configurada para cada pais
	 * 
	 * @throws SQLException
	 **********************************************************************************************/
	public int getLongPais(Connection conn, String longitud, BigDecimal area) throws SQLException {
		int ret = 0;

		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = " " + "SELECT " + longitud + " FROM TC_SC_CAT_PAIS " + "WHERE AREA=? ";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, area);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				ret = rst.getInt(1);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}

	/*********************************************************************************
	 * M\u00E9todo para registrar en el historico las transacciones de inventario de
	 * sidra
	 * 
	 * @return
	 * 
	 * @throws SQLException
	 *********************************************************************************/
	public static boolean insertaHistorico(Connection conn, List<HistoricoInv> listaObj) throws SQLException {
		boolean resp = false;
		String valores = "";
		String codNum = "";
		String serie = "";
		String serieA = "";
		String serieFinal = "";
		PreparedStatement pstmt = null;
		String insert = "";
		List<String> listaInserts = new ArrayList<String>();

		String campos[] = { HistoricoInv.CAMPO_TCSCLOGINVSIDRAID, HistoricoInv.CAMPO_TCSCCATPAISID,
				HistoricoInv.CAMPO_TCSCTIPOTRANSACCIONID, HistoricoInv.CAMPO_BODEGA_ORIGEN,
				HistoricoInv.CAMPO_BODEGA_DESTINO, HistoricoInv.CAMPO_ARTICULO, HistoricoInv.CAMPO_CANTIDAD,
				HistoricoInv.CAMPO_COD_NUM, HistoricoInv.CAMPO_SERIE, HistoricoInv.CAMPO_SERIE_ASOCIADA,
				HistoricoInv.CAMPO_ESTADO, HistoricoInv.CAMPO_TIPO_INV, HistoricoInv.CAMPO_SERIE_FINAL,
				HistoricoInv.CAMPO_TCSCTRASLADOID, HistoricoInv.CAMPO_CREADO_POR, HistoricoInv.CAMPO_CREADO_EL };

		// ARMANDO INSERTS
		if (listaObj != null && !listaObj.isEmpty()) {
			for (HistoricoInv obj : listaObj) {
				codNum = (String) (obj.getCod_num() == null ? "NULL" : obj.getCod_num());
				serie = obj.getSerie() == null ? "NULL" : "'" + obj.getSerie() + "'";
				serieA = obj.getSerie_asociada() == null ? "NULL" : "'" + obj.getSerie_asociada() + "'";
				if (obj.getSerie_final() == null || "".equals(obj.getSerie_final())) {
					serieFinal = "NULL";
				} else {
					serieFinal = "'" + obj.getSerie_final() + "'";
				}

				valores = UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, HistoricoInv.SEQUENCE,
						Conf.INSERT_SEPARADOR_SI) + obj.getTcScCatPaisId() + ", " + obj.getTcsctipotransaccionid()
						+ ", " + obj.getBodega_origen() + ", " + obj.getBodega_destino() + ", " + obj.getArticulo()
						+ ", " + obj.getCantidad() + ", " + codNum + ", " + serie + ", " + serieA + ", " + "'"
						+ obj.getEstado() + "', " + "'" + obj.getTipo_inv() + "', " + serieFinal + ", " + UtileriasJava
								.setInsert(Conf.INSERT_NUMERO, obj.getTcsctrasladoid() + "", Conf.INSERT_SEPARADOR_SI)
						+ "'" + obj.getCreado_por() + "', " + "sysdate ";

				listaInserts.add(valores);
			}
			// armando insert
			insert = UtileriasBD.armarQueryInsertAll(HistoricoInv.N_TABLA, campos, listaInserts);

			log.debug("sql insert historico: " + insert);
			try {
				pstmt = conn.prepareStatement(insert);
				pstmt.executeUpdate();
				resp = true;
			} catch (SQLException e) {
				log.error("Error al insertar histórico." + e);
				conn.rollback();
				resp = false;
			} finally {
				DbUtils.closeQuietly(pstmt);
			}
		}

		return resp;
	}

	public static boolean insertaHistorico2(Connection conn, List<HistoricoInv> listaObj) throws SQLException {
		boolean resp = false;
		PreparedStatement pstmt = null;
		String insert = "";
		insert = "INSERT INTO TC_SC_HISTORICO_INVSIDRA (tcscloginvsidraid, "
				+ "                                      tcsccatpaisid, "
				+ "                                      tcsctipotransaccionid, "
				+ "                                      bodega_origen, "
				+ "                                      bodega_destino, "
				+ "                                      articulo, "
				+ "                                      cantidad, " + "                                      cod_num, "
				+ "                                      serie, " + "                                      estado, "
				+ "                                      tipo_inv, "
				+ "                                      serie_final, "
				+ "                                      tcsctrasladoid, "
				+ "                                      creado_por, "
				+ "                                      creado_el) "
				+ "     VALUES (tc_sc_historico_invsidra_sq.NEXTVAL, " + "             ?, " + "             ?, "
				+ "             ?, " + "             ?, " + "             ?, " + "             ?, " + "             ?, "
				+ "             ?, " + "             ?, " + "             ?, " + "             ?, " + "             ?, "
				+ "             ?, " + "             SYSDATE) ";

		// ARMANDO INSERTS
		if (!listaObj.isEmpty()) {
			// armando insert
			try {
				pstmt = conn.prepareStatement(insert);

				for (HistoricoInv obj : listaObj) {

					pstmt.setBigDecimal(1, obj.getTcScCatPaisId());
					pstmt.setBigDecimal(2, obj.getTcsctipotransaccionid());
					pstmt.setBigDecimal(3, obj.getBodega_origen());
					pstmt.setBigDecimal(4, obj.getBodega_destino());
					pstmt.setBigDecimal(5, new BigDecimal(obj.getArticulo()));
					pstmt.setBigDecimal(6, obj.getCantidad());
					if (obj.getCod_num() == null) {
						pstmt.setBigDecimal(7, null);
					} else {
						pstmt.setBigDecimal(7, obj.getCod_num());
					}

					pstmt.setString(8, obj.getSerie());
					pstmt.setString(9, obj.getEstado());
					pstmt.setString(10, obj.getTipo_inv());
					pstmt.setString(11, obj.getSerie_final());
					pstmt.setBigDecimal(12, obj.getTcsctrasladoid());
					pstmt.setString(13, obj.getCreado_por());
					pstmt.addBatch();

				}

				int i[] = pstmt.executeBatch();
				log.trace("cantidad de inserts:" + i.length);
				resp = true;
			} catch (SQLException e) {
				log.error("Error al insertar histórico." + e);
				conn.rollback();
				resp = false;
			} finally {
				DbUtils.closeQuietly(pstmt);
			}
		}
		log.trace("inserta historico");
		return resp;
	}

	/**
	 * Método para obtener el id del tipo de transacción que se realiza en
	 * inventario
	 */
	public static BigDecimal getIdTransaccion(Connection conn, String tipoTransaccion, BigDecimal idPais)
			throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String query = "SELECT tcsctipotransaccionid" + " FROM TC_SC_TIPOTRANSACCION"
				+ " WHERE TCSCCATPAISID =? AND UPPER(CODIGO_TRANSACCION) =?";

		log.trace("qry transaccion: " + query);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, idPais);
			pstmt.setString(2, tipoTransaccion);

			rst = pstmt.executeQuery();
			if (rst.next()) {
				ret = rst.getBigDecimal(1);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}

	public static com.consystec.sc.ca.ws.input.log.LogSidra addLog(String tipoTransaccion, String origen, String id,
			String tipoId, String resultado, String descripcionError) {
		com.consystec.sc.ca.ws.input.log.LogSidra logSidra = new LogSidra();

		logSidra.setTipoTransaccion(tipoTransaccion);
		logSidra.setOrigen(origen);
		logSidra.setId(id);
		logSidra.setTipoId(tipoId);
		logSidra.setResultado(resultado);
		logSidra.setDescripcionError(descripcionError);

		return logSidra;
	}

	public static String getNombreVendedor(Connection conn, String idVendedor, BigDecimal idPais) throws SQLException {
		List<Filtro> condiciones = new ArrayList<Filtro>();
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_TCSCCATPAISID,
				idPais.toString()));
		condiciones
				.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_VENDEDOR, idVendedor));
		String nombreVendedor = UtileriasBD.getOneRecord(conn, VendedorDTS.CAMPO_NOMBRE, VendedorDTS.N_TABLA,
				condiciones);

		return nombreVendedor.equals("") ? Conf.NOMBRE_VENDEDOR + " ID " + idVendedor : nombreVendedor;
	}

	/**
	 * M\u00E9todo para obtener grupos de parametros
	 * 
	 * @param conn
	 * @param estadoAlta
	 * @param grupoParametro
	 * @return
	 * @throws SQLException
	 */
	public static HashMap<String, String> getGrupoParam(Connection conn, String estadoAlta, String grupoParametro,
			BigDecimal idPais) throws SQLException {
		HashMap<String, String> parametros = new HashMap<String, String>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = "SELECT lower(nombre) AS NOMBRE, valor FROM "
				+ "TC_SC_CONFIGURACION WHERE UPPER(grupo) = ? AND UPPER(estado) = ? AND TCSCCATPAISID = ?";

		log.trace("obteniendo grupo de parametros:" + query);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, grupoParametro.toUpperCase());
			pstmt.setString(2, estadoAlta.toUpperCase());
			pstmt.setBigDecimal(3, idPais);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				parametros.put(rst.getString(Catalogo.CAMPO_NOMBRE), rst.getString(Catalogo.CAMPO_VALOR));
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}
		return parametros;
	}

	public BigDecimal getLosSidraId(Connection conn) throws SQLException {
		String query = "SELECT tc_sc_log_sidra_sq.nextval FROM DUAL ";
		return (BigDecimal) JavaUtils.getOneFieldFromDual(conn, query);
	}

	// *//m\u00E9todos para registar log de tiempo por servicio
	public static boolean logResponseTime(String codArea, String servicio, String token, String usuario,
			HttpServletRequest req, Object objectResponse) {
		ControladorBase.setEndingTime();
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sqlInsert = "";
		try {
			conn = new ControladorBase().getConnRegional();
			jsonResponse = new GsonBuilder().setPrettyPrinting().create().toJson(objectResponse);
			log.debug("servicio: " + servicio + ", jsonRequest: " + jsonRequest);
			log.debug("servicio: " + servicio + ", jsonResponse: " + jsonResponse);

			String remoteAddr = getClientIpAddr(req);

			String insert = "INSERT INTO TC_SC_LOG_TIEMPOS"
					+ " (tcsclogid, cod_area, servicio, url, origen, ip_origen, hora_inicio,"
					+ " hora_fin, tiempo_ejecucion, peticion, respuesta, creado_el, creado_por)"
					+ " VALUES (TC_SC_LOG_TIEMPOS_SQ.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?)";
			sqlInsert = insert;
			pstmt = conn.prepareStatement(sqlInsert);

			pstmt.setString(1, codArea);
			pstmt.setString(2, servicio);
			pstmt.setString(3, req.getRequestURL().toString());
			pstmt.setString(4, token == null || token.equals("WEB") ? "PC" : "MOVIL");
			pstmt.setString(5, remoteAddr);
			pstmt.setTimestamp(6, new Timestamp(horaInicio.getTime()));
			pstmt.setTimestamp(7, new Timestamp(horaFin.getTime()));
			pstmt.setLong(8, elapsedTimeService);
			pstmt.setString(9, null);
			pstmt.setString(10, null);
			pstmt.setString(11, usuario);

			int res = pstmt.executeUpdate();
			if (res > 0) {
				return true;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(conn);
		}
		return false;
	}

	public static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_VIA");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static void setBeginningTime() {
		SimpleDateFormat FORMATO_TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss");
		log.trace("Inicia consumo de servicio.");
		try {

			horaInicio = FORMATO_TIMESTAMP.parse(FORMATO_TIMESTAMP.format(new Date()));
			horaInicioMillis = System.currentTimeMillis();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public static void setBeginningTime(Object request, HttpServletRequest req) {
		setBeginningTime();
		requestGlobal = req;
		jsonRequest = new GsonBuilder().setPrettyPrinting().create().toJson(request);
	}

	public static void setEndingTime() {
		SimpleDateFormat FORMATO_TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss");
		log.trace("Consumo de servicio completo.");
		try {
			horaFin = FORMATO_TIMESTAMP.parse(FORMATO_TIMESTAMP.format(new Date()));
			elapsedTimeService = System.currentTimeMillis() - horaInicioMillis;
			log.trace("Tiempo de ejecuci\u00F3n: " + elapsedTimeService + " millisegundos.");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}// */

	public static String validarParametroVacio(String valor, String nombre) throws ConfiguracionExcepcion {
		if (valor != null && !"".equals(valor.trim())) {
			return valor;
		} else {
			throw new ConfiguracionExcepcion("Parámetro de configuración " + nombre + " vacío.");
		}
	}

	public static HashMap<String, String> getGrupoParam(Connection conn, List<String> grupos, String codArea)
			throws SQLException {
		HashMap<String, String> parametros = new HashMap<String, String>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		StringBuilder query = new StringBuilder();
		query.append("SELECT UPPER(NOMBRE) as nombre, VALOR ");
		query.append("FROM  TC_SC_CONFIGURACION ");
		query.append("WHERE UPPER(GRUPO) IN (" + concatenarTexto(grupos) + ") ");
		query.append("AND UPPER (ESTADO) = 'ALTA' ");
		query.append("AND TCSCCATPAISID = (SELECT tcsccatpaisid FROM tc_sc_cat_pais WHERE area = " + codArea + ")");

		log.trace("GRUPO PARAMETROS FC: " + query.toString());
		try {
			pstmt = conn.prepareStatement(query.toString());
			rst = pstmt.executeQuery();
			while (rst.next()) {
				parametros.put(rst.getString("NOMBRE"), rst.getString("VALOR"));
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return parametros;
	}

	public static String concatenarTexto(List<String> lst) {
		String cadenaConcatenada = null;
		if (lst != null && !lst.isEmpty()) {
			cadenaConcatenada = "";
			for (int i = 0; i < lst.size(); i++) {
				cadenaConcatenada += "'" + lst.get(i) + "',";
			}
			cadenaConcatenada = cadenaConcatenada.substring(0, cadenaConcatenada.length() - 1);// eliminamos la coma
																								// final.
		}
		return cadenaConcatenada;
	}

	protected boolean isFullStack(String codArea) {
		return Country.SV.getCodArea().toString().compareTo(codArea) == 0
				|| Country.PA.getCodArea().toString().compareTo(codArea) == 0;
	}
}
