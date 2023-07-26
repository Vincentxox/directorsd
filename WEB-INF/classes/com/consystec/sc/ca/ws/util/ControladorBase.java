package com.consystec.sc.ca.ws.util;

import java.io.File;
import java.io.StringReader;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.consystec.sc.ca.ws.input.dispositivo.InputDispositivo;
import com.consystec.sc.ca.ws.metodos.Dispositivo;
import com.consystec.sc.ca.ws.orm.MensajeVW;
import com.consystec.sc.ca.ws.orm.Pais;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.orm.SesionUsuario;
import com.consystec.sc.ca.ws.output.dispositivo.OutputDispositivo;
import com.consystec.sc.sv.ws.util.Conf;
import com.ericsson.sdr.dao.PaisDAO;
import com.ericsson.sdr.dao.VendedorDtsDAO;
import com.ericsson.sdr.dto.PaisDTO;
import com.ericsson.sdr.dto.VendedorDtsDTO;
import com.ericsson.sdr.utils.Country;
import com.ericsson.sdr.utils.JndiUtils;
import com.google.gson.GsonBuilder;
import static com.ericsson.sdr.utils.Constants.*;

public class ControladorBase {
	private static final Logger log = Logger.getLogger(ControladorBase.class);
	private static final String NOMBRE_APLICACION = "WS_DIRECTOR_SIDRA";
	// String nombreAplicacion = "APPMOVIL";

	public static final String PAYMENT_CODE_SUCCESS = "00";

	public static final String KEY_LOGIN = "LOGIN";
	// log de tiempo
	private static Date horaInicio = null;
	private static Date horaFin = null;
	private static long horaInicioMillis = 0;
	private static long elapsedTimeService = 0;
	private static String jsonRequest = "";
	private static String jsonResponse = "";
	static HttpServletRequest requestGlobal;

	// LOG4J
	private static boolean log4j = false;
	private static String levelLog4j = "";
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
			cs.close();
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

	public Connection getConnLocal() throws SQLException {
		Connection conn = obtenerDtsLocal().getConnection();
		return conn;
	}

	public DataSource obtenerDtsLocal() {
		DataSource dts = null;
		try {
			Context contexto = new InitialContext();
			dts = (DataSource) contexto.lookup(JndiUtils.JNDI_SIDRA);

		} catch (NamingException e) {
			log.error(e.getMessage(), e);
		}
		return dts;
	}

	protected DataSource getDtsSeg() throws NamingException {
		DataSource dts = null;
		Context contexto = new InitialContext();
		Context env = (Context) contexto.lookup("java:comp/env");
		final String tipoSidra = (String) env.lookup(TIPO_SIDRA);
		String dsName = JndiUtils.JNDI_MSW;
		//if (TIPO_FS.equalsIgnoreCase(tipoSidra)) {
		//	dsName += TIPO_FS;
		//}
		log.info("tipo sidra " + tipoSidra);
		log.info("dsName " + dsName);
		dts = (DataSource) contexto.lookup(dsName.toLowerCase());
		return dts;
	}

	public Connection getConnSeg() throws SQLException, NamingException {
		Connection conn = getDtsSeg().getConnection();
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
		appLogDailyFile(conn);
		return conn;
	}

	// *****************************************************************************************
	/*
	 * Metodo para armar mensaje de Mensaje retornando un objeto de la clase Mensaje
	 * 
	 * @param
	 * 
	 * @author sbarrios
	 ******************************************************************************************/
	public Respuesta getMensaje(int Adv, String Exception, String Clase, String Metodo, String razon, String origen) {
		final String METHOD_NAME = "getMensaje()";
		String metodo = "getMensaje";
		Respuesta objAdv = new Respuesta();

		String Razon = null;
		Connection conn = null;
		try {
			log.debug(MessageFormat.format("{0} - requiring db connection", METHOD_NAME));
			conn = getConnLocal();
			log.trace("RAZON:" + razon);
			log.trace("ADV:" + Adv);
			/*
			 * obteniendo descripcion, codResultado, mostrar, tipoexcepcion de la tabla de
			 * advertencias y excepciones seg\u00FAn la configuraci\u00F3n para cada
			 * pa\u00EDs
			 */
			List<MensajeVW> Registro = new MensajeVWDAO().select(conn, new BigDecimal(Adv));
			Iterator<MensajeVW> iAdv = null;
			MensajeVW advertencia = null;
			log.trace("Advertencias encontradas:" + Registro.size());
			if (Registro != null && Registro.size() > 0) {
				iAdv = Registro.iterator();

				while (iAdv.hasNext()) {
					advertencia = iAdv.next();
					objAdv.setCodResultado(advertencia.getCodresultado().toString() == "0" ? "0"
							: advertencia.getCodresultado().toString());

					Razon = razon == null ? "" : razon;
					log.trace("RAZON FINAL" + Razon);

					objAdv.setDescripcion(advertencia.getDescripcion().toString() == null ? ""
							: advertencia.getDescripcion().toString() + Razon);

					objAdv.setMostrar(
							advertencia.getMostrar().toString() == "0" ? "0" : advertencia.getMostrar().toString());
					objAdv.setTipoExepcion(
							advertencia.getTipo_excepcion() == null ? "" : advertencia.getTipo_excepcion());
				}
			} else {
				objAdv.setCodResultado("" + Conf_Mensajes.MENSAJE_DEFAULT);
				objAdv.setDescripcion(Conf_Mensajes.MENSAJE_DEFAULT_TXT);
				objAdv.setMostrar("0");
			}

			objAdv.setClase(Clase == null ? " " : Clase);
			objAdv.setMetodo(Metodo == null ? " " : Metodo);
			objAdv.setExcepcion(Exception == null ? " " : Exception);
			objAdv.setOrigen(origen);

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

	/*
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

	/*
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

	/*
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

	/*
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

	/*
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

	/*
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

	/*
	 * Metodo para obtener Parametros est\u00E9ndar de la aplicaci\u00F3n
	 * 
	 * @param parametro: nombre del parametro que se desea obtener, tipo: indica si
	 * son parametros para estados de orden o generales, area: contiene el codigo de
	 * area del pais que se esta trabajando
	 ************************************************************/
	public String getParametro(String parametro, String area) {
		String resultado = "";
		BigDecimal idPais = null;
		Connection conn = null;
		try {
			try {
				conn = getConnLocal();
				idPais = getidpais(conn, area);

				resultado = getParametroConf(conn, parametro, idPais);

			} finally {
				if (conn == null) {
					conn = null;
				} else {
					DbUtils.closeQuietly(conn);
				}

			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			Respuesta Advertencia = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().getName(),
					"getParametro", null, Conf_Mensajes.MODULO_SERVICIO_DIRECTOR);
			return Advertencia.toString();
		}
		return resultado;
	}

	public String getParametroConf(Connection conn, String nombre, BigDecimal pais) throws SQLException {
		String ret = "";
		PreparedStatement pstm = null;
		ResultSet rst = null;
		String sql = "";

		try {
			sql = " select NOMBRE, VALOR " + " from   TC_SC_CONFIGURACION "
					+ " where  upper(NOMBRE) = upper(?) AND TCSCCATPAISID=?";

			pstm = conn.prepareStatement(sql);
			pstm.setString(1, nombre);
			pstm.setBigDecimal(2, pais);
			rst = pstm.executeQuery();

			if (rst.next()) {
				ret = rst.getString(2);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstm);
		}
		return ret;
	}

	public String getParametroReginal(Connection conn, String nombre) throws SQLException {
		PreparedStatement pstm = null;
		ResultSet rst = null;
		String ret = "";
		try {
			String sql = " select NOMBRE, VALOR " + " from   TC_SC_CONFIGURACION "
					+ " where  upper(NOMBRE) = upper(?) AND TCSCCATPAISID = 0";

			pstm = conn.prepareStatement(sql);
			pstm.setString(1, nombre);
			rst = pstm.executeQuery();
			if (rst.next()) {
				ret = rst.getString(2);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstm);
		}

		return ret;
	}

	/*
	 * Metodo utilizado para obtener el pa\u00EDs default de la aplicaci\u00F3n
	 * cuando en servicios de actualizaci\u00F3n no se recibe como par\u00E9metro
	 * codPais
	 * 
	 * @throws SQLException
	 */
	public BigDecimal getAreaDefault(String parametro) throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		Connection conn = null;
		try {
			conn = getConnLocal();
			log.trace("PARAMETRO:" + parametro);
			String sql = "";
			sql = "SELECT DESCRIPCION " + "  FROM TC_AC_PARAM_APP " + " WHERE UPPER (NOMBRE) = UPPER (?) ";
			log.debug("CONSULTA PARA OBTENER PAIS DEFAULT: " + sql);

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, parametro);
			rst = pstmt.executeQuery();

			if (rst.next())
				ret = new BigDecimal(rst.getString("DESCRIPCION"));

			log.trace("PAIS DEFAULT:" + ret);

		} finally {
			DbUtils.closeQuietly(conn);
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}

	/*
	 * Metodo para obtener la etiqueta del estado de Lotes
	 * 
	 * @throws SQLException
	 ***********************************************************/
	public String getEtiquetaLote(Connection conn, BigDecimal estado, String pais) throws SQLException {
		String ret = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		BigDecimal idPais = null;
		try {
			idPais = getidpais(conn, pais);
			String sql = "";
			sql = "SELECT NOMBRE " + "  FROM TC_AC_ESTADO_LOTE " + " WHERE TCACESTADOLOTEID = ? AND TCACCATPAISID = ? ";
			log.debug("CONSULTA PARA OBTENER ESTADO: " + sql);
			log.trace("estado:" + estado);
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1, estado);
			pstmt.setBigDecimal(2, idPais);
			rst = pstmt.executeQuery();

			if (rst.next())
				ret = rst.getString(1);
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}

	/*
	 * Metodo para obtener el nombre del pais ingresado como parametro
	 * 
	 * @throws SQLException
	 ***********************************************************/
	public String getNombrePais(Connection conn, BigDecimal pais) throws SQLException {
		String ret = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String sql = "";
		try {
			sql = "SELECT NOMBRE " + "  FROM TC_SC_CAT_PAIS " + " WHERE AREA = ? ";
			log.debug("CONSULTA PARA NOMBRE PAIS: " + sql);
			log.trace("area:" + pais);
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1, pais);

			rst = pstmt.executeQuery();

			if (rst.next()) {
				ret = rst.getString(1);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}

	/*
	 * Metodo para generar TOKEN
	 * 
	 * @throws SQLException
	 * 
	 * @throws GeneralSecurityException
	 **************************************/
	public SesionUsuario insertarTC_SC_SESION(Connection con, BigDecimal sesionid, BigDecimal codPais, String username,
			String token, BigDecimal estado, String dispositivo) throws SQLException, GeneralSecurityException {
		String query = "";
		String fechaAct = "TO_CHAR (SYSDATE, 'dd/mm/yyyy hh24:mi:ss')";
		String p_Token;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtInterno = null;// Agregado para evitar los warnings
		PreparedStatement pstmtInterno2 = null;// Agregado para evitar los warnings
		ResultSet rst = null;
		ResultSet rstInterno = null;// Agregado para evitar los warnings
		SesionUsuario sesion = null;

		try {
			sesion = new SesionUsuario();
			log.trace("username:" + username);
			log.trace("pais:" + codPais);
			log.trace("token:" + token);
			if (token == null || token.equals("")) {/* Se esta autenticando */
				log.trace("SELECT PARA AUTENTICAR");
				query = "SELECT TO_DATE (" + fechaAct + ", 'dd/mm/yyyy hh24:mi:ss') fecha_sesion, "
						+ "       NVL (a.modificado_el, a.creado_el) fecha_sesion_db, "
						+ "       TO_CHAR (SYSDATE, 'dd/mm/yyyy hh24:mi:ss') fecha_actual, "
						+ "       (SYSDATE - SYSDATE) diferencia, "
						+ "       a.USERNAME, b.TCSCCATPAISID, a.TCSESIONID, a.token, a.ESTADO "
						+ "  FROM tc_SC_sesion a, tc_SC_cat_pais b"
						+ " WHERE a.username = ? AND a.TCSCCATPAISID = b.TCSCCATPAISID AND b.area = ? AND a.COD_DISPOSITIVO=?";
				log.trace("QUERY SIN TOKEN:" + query);
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, username);
				pstmt.setBigDecimal(2, codPais);
				pstmt.setString(3, dispositivo);

			} else {

				log.trace("SELECT PARA RENOVAR TOKEN");
				query = "SELECT TO_DATE (?, 'dd/mm/yyyy hh24:mi:ss') fecha_sesion, "
						+ "       NVL (a.modificado_el, a.creado_el) fecha_sesion_db, "
						+ "       TO_CHAR (SYSDATE, 'dd/mm/yyyy hh24:mi:ss') fecha_actual, "
						+ "       (SYSDATE - TO_DATE (?, 'dd/mm/yyyy hh24:mi:ss')) * 60 * 24 diferencia, "
						+ "       a.USERNAME, b.TCSCCATPAISID, a.TCSESIONID, a.token, a.ESTADO  "
						+ "  FROM tc_sc_sesion a, tc_sc_cat_pais b "
						+ " WHERE a.username=? AND a.TCSCCATPAISID = b.TCSCCATPAISID AND b.area = ? AND a.COD_DISPOSITIVO=?";
				log.trace("QUERY CON TOKEN:" + query);
				pstmt = con.prepareStatement(query);
				p_Token = Cifrado.decrypt(token, username.toString());
				log.trace("TOKEN:" + Cifrado.decrypt(token, username.toString()));
				if (p_Token != null && !"".equals(p_Token)) {
					pstmt.setString(1, Cifrado.decrypt(token, username.toString()));
					pstmt.setString(2, Cifrado.decrypt(token, username.toString()));
					pstmt.setString(3, username);
					pstmt.setBigDecimal(4, codPais);
					pstmt.setString(5, dispositivo);
				}
			}

			rst = pstmt.executeQuery();

			if (rst.next()) { // si existe el registro
				log.trace("REGISTRO EXISTE");
				log.trace(rst.getBigDecimal("DIFERENCIA"));
				int validacion = rst.getBigDecimal("DIFERENCIA")
						.compareTo(new BigDecimal(getParametro(Conf.CANTIDAD_MINUTOS_SESION, codPais.toString())));
				if (validacion < 0) {
					log.trace("TOKEN NO HA VENCIDO");
					log.trace("TOKEN:" + token);
					if (token != null && !"".equals(token)) { // si viene el token quiere decir que no es el m\u00E9todo
																// de autenticar
						log.trace("TOKEN ES DIFERENTE DE NULL");
						codPais = rst.getBigDecimal("TCSCCATPAISID");
						sesionid = rst.getBigDecimal("TCSESIONID");
						estado = rst.getBigDecimal("ESTADO");
						username = rst.getString("USERNAME");

					}

					/* Se procede a generar el nuevo token */
					token = Cifrado.encrypt(rst.getString("fecha_actual"), username.toString());
					log.trace("NUEVO TOKEN:" + token);
					/* Actualizamos el registro */
					query = "UPDATE tc_sc_sesion " + "   SET token = ?, modificado_el = SYSDATE "
					// + " WHERE username = ? " +
							+ " WHERE tcsccatpaisid = ? " + " AND cod_dispositivo = ? " + " and upper(username) = ?";
					log.trace("ACTUALIZAR TOKEN:" + query);
					pstmtInterno = con.prepareStatement(query);
					pstmtInterno.setString(1, token);
					log.trace("PAIS:" + rst.getBigDecimal("TCSCCATPAISID"));
					log.trace("USERNAME:" + username);
					pstmtInterno.setBigDecimal(2, rst.getBigDecimal("TCSCCATPAISID"));
					pstmtInterno.setString(3, dispositivo);
					pstmtInterno.setString(4, username.toUpperCase());
					if (pstmtInterno.executeUpdate() > 0) {
						sesion.setToken(token);
						sesion.setTcsccatpaisid(codPais);
						sesion.setEstado(estado);
						sesion.setUsername(username);
						return sesion;
					}
				} else { // si la diferencia es mayor al tiempo establecido de sesion solicita login
					sesion.setToken("LOGIN");
					sesion.setTcsccatpaisid(codPais);
					sesion.setEstado(estado);
					sesion.setUsername(username);
					return sesion;
				}
			} else { // no existe y se procede a insertar
				log.trace("ES NUEVO TOKEN");
				query = "select TO_CHAR (SYSDATE, 'dd/mm/yyyy hh24:mi:ss') fecha_actual from dual";
				pstmtInterno = con.prepareStatement(query);
				rstInterno = pstmtInterno.executeQuery();

				if (rstInterno.next()) {
					token = Cifrado.encrypt(rstInterno.getString("fecha_actual"), username.toString());
				}
				log.trace("TOKEN A INSERTAR:" + token);
				query = "Insert into TC_SC_SESION "
						+ "   (TCSESIONID, USERNAME, TCSCCATPAISID, TOKEN, ESTADO, CREADO_EL, CREADO_POR, COD_DISPOSITIVO) "
						+ " Values "
						+ "   (TC_SC_SESION_SQ.NEXTVAL, ?,(SELECT TCSCCATPAISID FROM TC_SC_CAT_PAIS WHERE AREA=?), ?, ?, sysdate,?,?) ";
				log.trace("INSERTAR TOKEN: " + query);
				pstmtInterno2 = con.prepareStatement(query);
				pstmtInterno2.setString(1, username);
				pstmtInterno2.setString(2, codPais + "");
				pstmtInterno2.setString(3, token);
				pstmtInterno2.setBigDecimal(4, new BigDecimal(getParametro(Conf.ESTADO_ALTA_BOOL, codPais.toString())));
				pstmtInterno2.setString(5, username);
				pstmtInterno2.setString(6, dispositivo);

				if (pstmtInterno2.executeUpdate() > 0) {
					sesion.setToken(token);
					sesion.setTcsccatpaisid(codPais);
					sesion.setEstado(estado);
					sesion.setUsername(username);
					return sesion;
				}
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rstInterno);
			DbUtils.closeQuietly(pstmtInterno);
			DbUtils.closeQuietly(pstmtInterno2);
		}
		return sesion;
	}

	/************************************************************************
	 * PARA OBTENER PAIS
	 ************************************************************************/
	public BigDecimal getidpais(Connection conn, String area) throws SQLException {
		BigDecimal pais = null;
		String pArea = null;
		ResultSet rst = null;
		PreparedStatement pstm = null;
		String sSql = "";
		try {
			if (area == null && "0".equals("0")) {
				pArea = getAreaDefault(Conf.PAIS_DEFAULT).toString();
			} else {
				pArea = area;
			}

			sSql = "SELECT TCSCCATPAISID FROM TC_SC_CAT_PAIS WHERE AREA = ?";
			log.debug(sSql);
			pstm = conn.prepareStatement(sSql);
			pstm.setString(1, pArea);
			rst = pstm.executeQuery();

			if (rst.next()) {
				pais = rst.getBigDecimal("TCSCCATPAISID");
			} else {
				pais = new BigDecimal(0);
			}
			log.trace("pais:" + pais);
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstm);
		}
		return pais;
	}

	/*********************************************************************************************
	 * Metodo utilizado para obtener la longitud de n\u00FAmero de telefono y
	 * documento de identificaci\u00F3n configurada para cada pais
	 * 
	 * @throws SQLException
	 **********************************************************************************************/
	public int getLongPais(Connection conn, String longitud) throws SQLException {
		int ret = 0;
		Statement stm = conn.createStatement();
		ResultSet rst = null;
		String query = "";

		try {
			query = "SELECT " + longitud + " FROM TC_SC_CAT_PAIS " + "WHERE AREA=?";
			rst = stm.executeQuery(query);
			if (rst.next())
				ret = rst.getInt(1);
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(stm);

		}
		return ret;
	}

	/**
	 * M\u00E9todo para obtener idUsuario del M\u00F3dulo de Seguridad
	 * 
	 * @throws SQLException
	 * @throws NamingException
	 */

	public Pais getPais(Connection conn, String area) throws SQLException {
		Pais datosPais = new Pais();
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = "";
		try {
			query = "SELECT * FROM TC_SC_CAT_PAIS WHERE AREA = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, area);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				datosPais.setNombre(rst.getString(Pais.CAMPO_NOMBRE));
				datosPais.setLong_numero(rst.getBigDecimal(Pais.CAMPO_LONG_NUMERO));
				datosPais.setLong_identificacion(rst.getBigDecimal(Pais.CAMPO_LONG_IDENTIFICACION));
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return datosPais;
	}

	/*
	 * Metodo para obtener Token de Sesion, si este no es obtenido con exito no
	 * podra realizarse ninguna operacion en el Servicio tipo=1 nuevo 0=renovar
	 * token
	 * 
	 * @throws Exception
	 * 
	 * @throws GeneralSecurityException
	 * 
	 * @throws BadPaddingException
	 * 
	 * @throws IllegalBlockSizeException
	 * 
	 * @throws NoSuchPaddingException
	 * 
	 * @throws NoSuchAlgorithmException
	 * 
	 * @throws InvalidKeyException
	 * 
	 ***************************************************************************************/
	public String getToken(Connection conn, String usuario, String token, String pais, String dispositivo, int tipo)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, GeneralSecurityException, SQLException {

		SesionUsuario sesionToken = new SesionUsuario();
		String Token = null;
		String estadoAlta = "";
		InputDispositivo objInput = new InputDispositivo();
		OutputDispositivo objDispositivo;

		log.trace("Ingresa a  obtener token");
		log.trace("Valor actual token:" + token);
		if (token.equals("WEB")) { // para pruebas de desarrollo
			return "WEB";

		} else {
			estadoAlta = getParametro(Conf.ESTADO_ALTA_TXT, pais);

			// obteniendo informaci\u00F3n de dispositivo para saber si no fue robado
			objInput.setCodArea(pais);
			objInput.setUsuario(usuario);
			objInput.setCodigoDispositivo(dispositivo);
			objInput.setEstado(estadoAlta);

			Dispositivo ws = new Dispositivo();
			objDispositivo = ws.getDispositivos(objInput);

			if (objDispositivo.getRespuesta().getCodResultado().equals("12")) {
				// si el dispositivo esta de alta renueva token, de lo contrario, indicara que
				// la sesi\u00F3n expir\u00F3
				if (objDispositivo.getDispositivos().get(0).getEstado().equalsIgnoreCase(estadoAlta)) {
					/*
					 * se almacena el registro de sesion devuelto por el m\u00E9todo
					 * insertarTC_SC_SESION en un objeto de Tipo Sesion
					 */
					if (tipo == 0) {
						log.trace("inserta nuevo token ... login");
						sesionToken = insertarTC_SC_SESION(conn, null, new BigDecimal(pais), usuario, token, null,
								dispositivo);
					} else if (tipo == 1) {
						log.trace("Actualiza token ... login");
						sesionToken = insertarTC_SC_SESION(conn, null, new BigDecimal(pais), usuario, "", null,
								dispositivo);
					}
					Token = (sesionToken.getToken());
				} else {
					Token = "ERROR:" + "El dispositivo ingresado no es v\u00E9lido o se encuentra de baja.";
				}
			} else {
				Token = "ERROR:" + objDispositivo.getRespuesta().getDescripcion();
			}
			log.trace("TOKEN OBTENIDO:" + Token);
			return Token;

		}
	}

	public static HttpServletRequest getRequestGlobal() {
		return requestGlobal;
	}

	public static void setRequestGlobal(HttpServletRequest requestGlobal) {
		ControladorBase.requestGlobal = requestGlobal;
	}

	public static boolean logResponseTime(String codArea, String servicio, String token, String usuario,
			HttpServletRequest req, Object objectRequest, Object objectResponse, Date beginTime) {
		Date endingTime = new Date();
		Connection conn = null;
		PreparedStatement pstmt = null;
		String insert = "";
		try {
			conn = new ControladorBase().getConnLocal();
			String jsonRequest = new GsonBuilder().setPrettyPrinting().create().toJson(objectRequest);
			String jsonResponse = new GsonBuilder().setPrettyPrinting().create().toJson(objectResponse);
			String remoteAddr = getClientIpAddr(req);

			insert = "INSERT INTO TC_SC_LOG_TIEMPOS"
					+ " (tcsclogid, cod_area, servicio, url, origen, ip_origen, hora_inicio,"
					+ " hora_fin, tiempo_ejecucion, peticion, respuesta, creado_el, creado_por)"
					+ " VALUES (TC_SC_LOG_TIEMPOS_SQ.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?)";

			pstmt = conn.prepareStatement(insert);

			pstmt.setString(1, codArea);
			pstmt.setString(2, servicio);
			pstmt.setString(3, req.getRequestURL().toString());
			pstmt.setString(4, token == null || token.equals("WEB") ? "PC" : "MOVIL");
			pstmt.setString(5, remoteAddr);
			pstmt.setTimestamp(6, new Timestamp(beginTime.getTime()));
			pstmt.setTimestamp(7, new Timestamp(endingTime.getTime()));
			pstmt.setLong(8, endingTime.getTime() - beginTime.getTime());
			pstmt.setCharacterStream(9, new StringReader(jsonRequest), jsonRequest.length());
			pstmt.setCharacterStream(10, new StringReader(jsonResponse), jsonResponse.length());
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

	public String gettokenString(Connection conn, String usuario, String token, String codArea, String codDispositivo) {
		String Token = "";
		try {
			Token = getToken(conn, usuario, token, codArea, codDispositivo, 0);
			log.trace("TOKEN:" + Token);
		} catch (Exception e) {
			Token = Conf_Mensajes.MSJ_TOKEN_INVALIDO_7 + "||" + e.getMessage();
			log.error(e, e);
		}
		return Token;
	}

	public static void appLogDailyFile(Connection conn) {

		HttpSession session = requestGlobal.getSession();

		String path1 = session.getServletContext().getInitParameter(Conf.PATH_LOG4J.toLowerCase());
		String level1 = session.getServletContext().getInitParameter(Conf.LEVEL_LOG4J);
		String file = session.getServletContext().getInitParameter(Conf.LOG4J_FILENAME);
		String sizeFile = session.getServletContext().getInitParameter(Conf.LOG4J_SIZE);
		String fileLimit = session.getServletContext().getInitParameter(Conf.LOG4J_LIMIT);
		String conversionPattern1 = session.getServletContext().getInitParameter(Conf.LOG4J_CONVERSIONPATTERN);

		try {
			String level = new ControladorBase().getParametroReginal(conn, Conf.LEVEL_LOG4J);

			String path = "";
			if (!log4j) {

				System.out.println("--SYSTEM.OUT1--");
				System.out.println("logPath1" + path1);
				System.out.println("logfileName" + file);
				System.out.println("logPattern" + conversionPattern1);
				System.out.println("logSizeFile" + sizeFile);
				System.out.println("logFileLimit" + fileLimit);
				System.out.println("loglevel1" + level1);
				System.out.println("--SYSTEM.OUT--");

				path = new ControladorBase().getParametroReginal(conn, Conf.PATH_LOG4J);
				System.out.println("path:" + path);

				// creates pattern layout
				layout = new PatternLayout();
				String conversionPattern = "[%p] %d %c %M - %m%n";
				layout.setConversionPattern(conversionPattern);

				// creates daily rolling file appender
				rollingAppender = new DailyRollingFileAppender();
				rollingAppender.setFile(path + File.separator + ControladorBase.NOMBRE_APLICACION + ".log");
				rollingAppender.setDatePattern("'.'yyyy-MM-dd");
				rollingAppender.setLayout(layout);
				rollingAppender.activateOptions();

				// configures the root logger
				rootLogger = Logger.getRootLogger();
				if (level != null && !"".equals(level) && Conf.LOG4J_OFF.equals(level)) {
					rootLogger.setLevel(Level.OFF);
				} else if (level != null && !"".equals(level) && Conf.LOG4J_FATAL.equals(level)) {
					rootLogger.setLevel(Level.FATAL);
				} else if (level != null && !"".equals(level) && Conf.LOG4J_ERROR.equals(level)) {
					rootLogger.setLevel(Level.ERROR);
				} else if (level != null && !"".equals(level) && Conf.LOG4J_WARN.equals(level)) {
					rootLogger.setLevel(Level.WARN);
				} else if (level != null && !"".equals(level) && Conf.LOG4J_INFO.equals(level)) {
					rootLogger.setLevel(Level.INFO);
				} else if (level != null && !"".equals(level) && Conf.LOG4J_DEBUG.equals(level)) {
					rootLogger.setLevel(Level.DEBUG);
				} else if (level != null && !"".equals(level) && Conf.LOG4J_TRACE.equals(level)) {
					rootLogger.setLevel(Level.TRACE);
				} else {
					rootLogger.setLevel(Level.ALL);
				}

				rootLogger.addAppender(rollingAppender);
				log4j = true;
				levelLog4j = level;
			} else if (!levelLog4j.equals(level)) {
				if (level != null && !"".equals(level) && Conf.LOG4J_OFF.equals(level)) {
					rootLogger.setLevel(Level.OFF);
				} else if (level != null && !"".equals(level) && Conf.LOG4J_FATAL.equals(level)) {
					rootLogger.setLevel(Level.FATAL);
				} else if (level != null && !"".equals(level) && Conf.LOG4J_ERROR.equals(level)) {
					rootLogger.setLevel(Level.ERROR);
				} else if (level != null && !"".equals(level) && Conf.LOG4J_WARN.equals(level)) {
					rootLogger.setLevel(Level.WARN);
				} else if (level != null && !"".equals(level) && Conf.LOG4J_INFO.equals(level)) {
					rootLogger.setLevel(Level.INFO);
				} else if (level != null && !"".equals(level) && Conf.LOG4J_DEBUG.equals(level)) {
					rootLogger.setLevel(Level.DEBUG);
				} else if (level != null && !"".equals(level) && Conf.LOG4J_TRACE.equals(level)) {
					rootLogger.setLevel(Level.TRACE);
				} else {
					rootLogger.setLevel(Level.ALL);
				}

				rootLogger.addAppender(rollingAppender);
				log4j = true;
				levelLog4j = level;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * <p>
	 * 
	 * Valida si el codigo de area es valido para los paises que ya implementaron
	 * 
	 * fullstack
	 * </p>
	 * 
	 * 
	 * 
	 * @param codArea
	 *                <p>
	 * 
	 *                Recibe el codigo de area del pais a validar
	 *                </p>
	 * 
	 * @return boolean
	 * 
	 *         <p>
	 *         Retorna true si el pais ya tiene la implementacion con fullstack
	 * 
	 *         </p>
	 * 
	 * 
	 */
	protected boolean isFullStack(String codArea) {
		return Country.SV.getCodArea().toString().compareTo(codArea) == 0
				|| Country.PA.getCodArea().toString().compareTo(codArea) == 0;
	}

	/**
	 * 
	 * <p>
	 * Obtiene el objeto para manejar la información del pais
	 * </p>
	 * 
	 * @param {@link Connection} sdr
	 *        <p>
	 *        Conexion hacia la base de datos donde se ejecutara la consulta
	 *        </p>
	 * @param {@link String} codArea
	 *        <p>
	 *        Codigo de area del pais que se quiere obtener
	 *        </p>
	 * @return {@link PaisDTO}
	 * 
	 * @throws SQLException
	 */
	protected PaisDTO getPaisDto(Connection sdr, String codArea) throws SQLException {
		return new PaisDAO().getPais(sdr, codArea);
	}

	/**
	 * 
	 * <p>
	 * Obtiene la información del vendedor por medio del usuario
	 * </p>
	 * 
	 * @param {@link Connection} sdr
	 *        <p>
	 *        Conexion de la base de datos en la que se ejecutara la consulta
	 *        </p>
	 * @param {@link String} usuario
	 *        <p>
	 *        Usuario del vendedor
	 *        </p>
	 * @return {@link VendedorDtsDTO}
	 * @throws SQLException
	 */
	protected VendedorDtsDTO getVendedorDts(Connection sdr, String usuario) throws SQLException {
		return new VendedorDtsDAO().getVendedorByUsuario(sdr, usuario);
	}

	protected String getPathAPK(String path) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		String dirBaseWB = cl.getResource("/").getFile();
		// dirBaseWB = dirBaseWB + "../../version/";
		// dirBaseWB = dirBaseWB + path;
		// dirBaseWB = dirBaseWB.replace("%20", " ");
		// dirBaseWB = dirBaseWB.replace("../../","WebContent/");
		dirBaseWB = path;
		return dirBaseWB;
	}

}
