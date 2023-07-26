package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.dispositivo.InputDispositivo;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.sv.ws.orm.Dispositivo;
import com.consystec.sc.sv.ws.orm.Panel;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.JavaUtils;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionDispositivo {
	private OperacionDispositivo() {
	}

	private static final Logger log = Logger.getLogger(OperacionDispositivo.class);

	public static BigDecimal insertDispositivo(Connection conn, Dispositivo objeto) throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;

		String insert = " INSERT INTO TC_SC_DISPOSITIVO( " + "tcscdispositivoid," + "codigo_dispositivo," + "modelo,"
				+ "descripcion," + "num_telefono," + "caja_numero," + "zona," + "resolucion," + "fecha_resolucion,"
				+ "estado," + "creado_el," + "creado_por," + "tcsccatpaisid," + "tcscdtsid," + "responsable,"
				+ "tipo_responsable," + "vendedor_asignado," + "cod_oficina," + "cod_vendedor," + "id_plaza,"
				+ "id_puntoventa," + "userid,"
				+ "username ) VALUES (?,?,?,?,?,?,?,?,?,?,sysdate,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			log.trace("insert dispositivo: " + insert);
			pstmt = conn.prepareStatement(insert);
			ret = JavaUtils.getSecuencia(conn, Dispositivo.SEQUENCE);
			pstmt.setBigDecimal(1, ret);
			pstmt.setString(2, objeto.getCodigo_dispositivo().toUpperCase().trim());
			pstmt.setString(3, objeto.getModelo());
			pstmt.setString(4, objeto.getDescripcion());
			pstmt.setBigDecimal(5, objeto.getNum_telefono());
			pstmt.setBigDecimal(6, objeto.getCaja_numero());
			pstmt.setString(7, objeto.getZona());
			pstmt.setString(8, objeto.getResolucion());
			pstmt.setTimestamp(9, objeto.getFecha_resolucion());
			pstmt.setString(10, objeto.getEstado());
			pstmt.setString(11, objeto.getCreado_por());
			pstmt.setBigDecimal(12, objeto.getTcsccatpaisid());
			pstmt.setBigDecimal(13, objeto.getTcscdtsid());
			pstmt.setBigDecimal(14, objeto.getResponsable());
			pstmt.setString(15, objeto.getTipo_responsable());
			pstmt.setBigDecimal(16, objeto.getVendedor_asignado());
			pstmt.setString(17, objeto.getCod_oficina());
			pstmt.setString(18, objeto.getCod_vendedor());
			pstmt.setString(19, objeto.getId_plaza());
			pstmt.setString(20, objeto.getId_puntoventa());
			pstmt.setString(21, objeto.getUserid());
			pstmt.setString(22, objeto.getUsername());

			int res = pstmt.executeUpdate();
			if (res != 1) {
				ret = new BigDecimal(0);
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
		}

		return ret;
	}

	public static BigDecimal updateDispositivo(Connection conn, Dispositivo objeto, String codArea)
			throws SQLException {
		String estadoBaja = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, codArea);

		BigDecimal ret = null;
		PreparedStatement pstmt = null;

		String update = " UPDATE TC_SC_DISPOSITIVO SET " + "descripcion = ?, " + "estado = ?, " + "num_telefono = ?, "
				+ "responsable = ?, " + "tipo_responsable = UPPER(?), " + "caja_numero = ?, " + " zona= ?, "
				+ "resolucion = ?, " + "fecha_resolucion = ?, " + "tcscdtsid = ?, " + "cod_oficina = ?, "
				+ "cod_vendedor = ?, " + "modificado_el = SYSDATE, " + "modificado_por = ? ,"
				+ "vendedor_asignado = ? , " + "id_plaza = ? ," + "id_puntoventa = ? ," + "userid = ? ,"
				+ "username = ? " + " WHERE tcscdispositivoid = ?  AND tcsccatpaisid=? ";

		log.trace("UPDATE dispositivo: " + update);
		log.trace("nombre dispositivo: " + objeto.getDescripcion());
		log.trace("id dispositivo: " + objeto.getTcscdispositivoid());
		log.trace("estado dispositivo: " + objeto.getEstado());
		try {
			pstmt = conn.prepareStatement(update);
			pstmt.setString(1, objeto.getDescripcion());
			pstmt.setString(2, objeto.getEstado());
			pstmt.setBigDecimal(3, objeto.getNum_telefono());
			pstmt.setBigDecimal(4, objeto.getResponsable());
			pstmt.setString(5, objeto.getTipo_responsable());

			pstmt.setBigDecimal(6, objeto.getCaja_numero());
			pstmt.setString(7, objeto.getZona());
			pstmt.setString(8, objeto.getResolucion());
			pstmt.setTimestamp(9, objeto.getFecha_resolucion());
			pstmt.setBigDecimal(10, objeto.getTcscdtsid());
			pstmt.setString(11, objeto.getCod_oficina());
			pstmt.setString(12, objeto.getCod_vendedor());

			pstmt.setString(13, objeto.getModificado_por());
			pstmt.setBigDecimal(14, objeto.getVendedor_asignado());
			pstmt.setString(15, objeto.getId_plaza());
			pstmt.setString(16, objeto.getId_puntoventa());
			pstmt.setString(17, objeto.getUserid());
			pstmt.setString(18, objeto.getUsername());

			pstmt.setBigDecimal(19, objeto.getTcscdispositivoid());
			pstmt.setBigDecimal(20, objeto.getTcsccatpaisid());

			pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}

		if (objeto.getEstado().equalsIgnoreCase(estadoBaja)) {
			updateFoliosDispositivo(conn, objeto.getTcscdispositivoid(), objeto.getEstado(), objeto.getModificado_por(),
					objeto.getTcsccatpaisid());
		}

		return ret;
	}

	public static BigDecimal updateFoliosDispositivo(Connection conn, BigDecimal codDispositivo, String estado,
			String usuario, BigDecimal idPais) throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;

		String update = " UPDATE TC_SC_FOLIO " + " SET ESTADO = ?," + " MODIFICADO_EL = SYSDATE, MODIFICADO_POR = ?"
				+ " WHERE UPPER(IDTIPO ) = (SELECT CODIGO_DISPOSITIVO FROM TC_SC_DISPOSITIVO WHERE TCSCDISPOSITIVOID=? AND TCSCCATPAISID=?)"
				+ " AND TCSCCATPAISID=?";

		log.trace("UPDATE dispositivo: " + update);
		log.trace("id dispositivo: " + codDispositivo);
		try {
			pstmt = conn.prepareStatement(update);
			pstmt.setString(1, estado.toUpperCase());
			pstmt.setString(2, usuario);
			pstmt.setBigDecimal(3, codDispositivo);
			pstmt.setBigDecimal(4, idPais);
			pstmt.setBigDecimal(5, idPais);

			pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}

		return ret;
	}

	/**
	 * M\u00E9todo para verificar que el nombre de buzon a ingresar o modificar no
	 * exista
	 */
	public static List<BigDecimal> existeCodDispositivo(Connection conn, String codDispositivo, String estado,
			BigDecimal numTelefono, BigDecimal idDispositivo, BigDecimal idPais) throws SQLException {
		List<BigDecimal> ret = new ArrayList<BigDecimal>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		if (codDispositivo.trim().equals("")) {
			codDispositivo = "(SELECT CODIGO_DISPOSITIVO FROM TC_SC_DISPOSITIVO WHERE TCSCDISPOSITIVOID = "
					+ idDispositivo + " AND " + Dispositivo.CAMPO_TCSCCATPAISID + "=" + idPais + ")";
		} else {
			codDispositivo = "'" + codDispositivo.toUpperCase() + "' ";
		}

		String query = armaexisteDispositivo(codDispositivo);

		log.trace("valida codigo Dispositivo:" + query);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, idDispositivo);
			pstmt.setString(2, estado);
			pstmt.setBigDecimal(3, idPais);
			pstmt.setBigDecimal(4, numTelefono);
			pstmt.setBigDecimal(5, idDispositivo);
			pstmt.setString(6, estado);
			pstmt.setBigDecimal(7, idPais);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				ret.add(0, rst.getBigDecimal(1));
				ret.add(1, rst.getBigDecimal(2));
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}

	public static String armaexisteDispositivo(String codDispositivo) {
		String query = "SELECT (SELECT COUNT (1) " + "          FROM TC_SC_DISPOSITIVO "
				+ "         WHERE     UPPER (codigo_dispositivo) = " + codDispositivo
				+ "               AND tcscdispositivoid NOT IN (?) "
				+ "               AND estado = ? and tcsccatpaisid=?) " + "          codDispositivo, "
				+ "       (SELECT COUNT (1) " + "          FROM TC_SC_DISPOSITIVO "
				+ "         WHERE     num_telefono = ?" + "               AND tcscdispositivoid NOT IN (?) "
				+ "               AND estado = ? and tcsccatpaisid=?) " + "          telefono " + "  FROM DUAL ";

		return query;
	}

	public static List<InputDispositivo> getDispositivo(Connection conn, List<Filtro> filtros, String codArea,
			BigDecimal idPais) throws SQLException {
		List<InputDispositivo> lstBuzon = new ArrayList<InputDispositivo>();
		InputDispositivo objeto = new InputDispositivo();
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String sql = null;

		String tipoRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_RUTA, codArea);
		String nombreTipo = "CASE";

		nombreTipo += " WHEN " + Dispositivo.N_TABLA + "." + Dispositivo.CAMPO_TIPO_RESPONSABLE + " = '" + tipoRuta
				+ "' THEN " + "(SELECT A." + Ruta.CAMPO_NOMBRE + " FROM " + Ruta.N_TABLA + " A WHERE A."
				+ Ruta.CAMPO_TC_SC_RUTA_ID + " = " + Dispositivo.N_TABLA + "." + Dispositivo.CAMPO_RESPONSABLE
				+ " AND A." + Ruta.CAMPO_TCSCCATPAISID + "=" + idPais + " ) ";

		nombreTipo += " WHEN " + Dispositivo.N_TABLA + "." + Dispositivo.CAMPO_TIPO_RESPONSABLE + " != '" + tipoRuta
				+ "' THEN " + "(SELECT A." + Panel.CAMPO_NOMBRE + " FROM " + Panel.N_TABLA + " A WHERE A."
				+ Panel.CAMPO_TCSCPANELID + " = " + Dispositivo.N_TABLA + "." + Dispositivo.CAMPO_RESPONSABLE
				+ " AND A." + Ruta.CAMPO_TCSCCATPAISID + "=" + idPais + " ) ";

		nombreTipo += " END AS NOMBRE_TIPO";

		String campos[] = { Dispositivo.CAMPO_TCSCDISPOSITIVOID, Dispositivo.CAMPO_CODIGO_DISPOSITIVO,
				Dispositivo.CAMPO_MODELO, Dispositivo.CAMPO_DESCRIPCION, Dispositivo.CAMPO_NUM_TELEFONO,
				Dispositivo.CAMPO_ESTADO, Dispositivo.CAMPO_RESPONSABLE, Dispositivo.CAMPO_TIPO_RESPONSABLE, nombreTipo,
				Dispositivo.CAMPO_CAJA_NUMERO, Dispositivo.CAMPO_ZONA, Dispositivo.CAMPO_RESOLUCION,
				Dispositivo.CAMPO_FECHA_RESOLUCION, Dispositivo.CAMPO_TCSCDTSID, Dispositivo.CAMPO_COD_OFICINA,
				Dispositivo.CAMPO_COD_VENDEDOR, Dispositivo.CAMPO_VENDEDOR_ASIGNADO, Dispositivo.CAMPO_ID_PLAZA,
				Dispositivo.CAMPO_ID_PUNTOVENTA, Dispositivo.CAMPO_USERID, Dispositivo.CAMPO_USERNAME,
				Dispositivo.CAMPO_CREADO_EL, Dispositivo.CAMPO_CREADO_POR, Dispositivo.CAMPO_MODIFICADO_EL,
				Dispositivo.CAMPO_MODIFICADO_POR };

		List<Order> orden = new ArrayList<Order>();
		orden.add(new Order(Dispositivo.CAMPO_CODIGO_DISPOSITIVO, Order.ASC));
		sql = UtileriasBD.armarQuerySelect(Dispositivo.N_TABLA, campos, filtros, orden);
		try {
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst != null) {
				while (rst.next()) {
					objeto = new InputDispositivo();
					objeto.setIdDispositivo(
							UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Dispositivo.CAMPO_TCSCDISPOSITIVOID));
					objeto.setCodigoDispositivo(
							UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_CODIGO_DISPOSITIVO));
					objeto.setModelo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_MODELO));
					objeto.setDescripcion(
							UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_DESCRIPCION));
					objeto.setNumTelefono(
							UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Dispositivo.CAMPO_NUM_TELEFONO));
					objeto.setEstado(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_ESTADO));
					objeto.setResponsable(
							UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Dispositivo.CAMPO_RESPONSABLE));
					objeto.setTipoResponsable(
							UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_TIPO_RESPONSABLE));
					objeto.setNombreResponsable(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRE_TIPO"));
					objeto.setVendedorAsignado(
							UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_VENDEDOR_ASIGNADO));

					objeto.setCajaNumero(
							UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Dispositivo.CAMPO_CAJA_NUMERO));
					objeto.setZona(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_ZONA));
					objeto.setResolucion(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_RESOLUCION));
					objeto.setFechaResolucion(
							UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Dispositivo.CAMPO_FECHA_RESOLUCION));

					objeto.setIdDistribuidor(
							UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Dispositivo.CAMPO_TCSCDTSID));
					objeto.setCodOficina(
							UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_COD_OFICINA));
					objeto.setCodVendedor(
							UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_COD_VENDEDOR));

					objeto.setIdPlaza(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_ID_PLAZA));
					objeto.setIdPuntoVenta(
							UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_ID_PUNTOVENTA));
					objeto.setUserId(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_USERID));
					objeto.setUsername(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_USERNAME));

					objeto.setCreado_el(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Dispositivo.CAMPO_CREADO_EL));
					objeto.setCreado_por(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_CREADO_POR));
					objeto.setModificado_el(
							UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Dispositivo.CAMPO_MODIFICADO_EL));
					objeto.setModificado_por(
							UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Dispositivo.CAMPO_MODIFICADO_POR));

					lstBuzon.add(objeto);
				}
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return lstBuzon;
	}

	public static Respuesta validarPanelRuta(Connection conn, String idTipo, String tipo, String estadoAlta,
			String dispositivo, String idDTS, String codArea, BigDecimal idPais) throws SQLException {
		String nombreMetodo = "validarPanelRuta";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta;
		List<Filtro> condiciones = new ArrayList<Filtro>();

		String tipoRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_RUTA, codArea);
		String existencia = "";
		int mensaje = 0;

		if (tipo.equalsIgnoreCase(tipoRuta)) {
			log.trace("Entra a validar tipo Ruta");
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Ruta.CAMPO_ESTADO, estadoAlta));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TC_SC_RUTA_ID, idTipo));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TC_SC_DTS_ID, idDTS));
			condiciones.add(
					UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TCSCCATPAISID, "" + idPais));
			existencia = UtileriasBD.verificarExistencia(conn, Ruta.N_TABLA, condiciones);
			mensaje = Conf_Mensajes.MSJ_ERROR_NO_EXISTE_RUTA_182;

		} else {
			log.trace("Entra a validar tipo Panel");
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Panel.CAMPO_ESTADO, estadoAlta));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCPANELID, idTipo));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCDTSID, idDTS));
			condiciones.add(
					UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCCATPAISID, "" + idPais));
			existencia = UtileriasBD.verificarExistencia(conn, Panel.N_TABLA, condiciones);
			mensaje = Conf_Mensajes.MSJ_ERROR_NO_EXISTE_PANEL_181;
		}

		if (new Integer(existencia) <= 0) {
			respuesta = new ControladorBase().getMensaje(mensaje, null, nombreClase, nombreMetodo, null, codArea);
		} else {
			if (tipo.equalsIgnoreCase(tipoRuta)) {
				// se verifica que el dispositivo no este en otra ruta
				condiciones.clear();
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Dispositivo.CAMPO_TCSCCATPAISID,
						"" + idPais));
				condiciones.add(
						UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Dispositivo.CAMPO_ESTADO, estadoAlta));
				condiciones.add(
						UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Dispositivo.CAMPO_RESPONSABLE, idTipo));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
						Dispositivo.CAMPO_TIPO_RESPONSABLE, tipoRuta));
				if (dispositivo != null) {
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_NOT_IN_AND,
							Dispositivo.CAMPO_TCSCDISPOSITIVOID, dispositivo));
				}

				existencia = UtileriasBD.verificarExistencia(conn, Dispositivo.N_TABLA, condiciones);

				if (new Integer(existencia) > 0) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_DISPOSITIVO_RUTA_718, null,
							nombreClase, nombreMetodo, null, codArea);
				} else {
					return null;
				}
			} else {
				return null;
			}
		}

		return respuesta;
	}

	/**
	 * M\u00E9todo para validar si el vendedor pertenece a la panel o ruta que tiene
	 * asignada el dispositivo
	 * 
	 * @param conn
	 * @param tipoResponsable
	 * @param idVendedor
	 * @param estadoAlta
	 * @return Index 0: Indica si el vendedor pertenece a la panel o no Index 1:
	 *         Indica si el vendedor ya esta asingado a un dispositivo
	 * @throws SQLException
	 */
	public static List<BigDecimal> validaVendDispositivo(Connection conn, String tipoResponsable, String idVendedor,
			String estadoAlta, String idResponsable, String idDispositivo, String codArea, BigDecimal idPais)
			throws SQLException {
		List<BigDecimal> ret = new ArrayList<BigDecimal>();
		String query = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String filtroDispositivo = "";

		try {
			if (idDispositivo != null) {
				filtroDispositivo = " AND TCSCDISPOSITIVOID NOT IN (" + idDispositivo + ")";
			}

			String tipoRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_RUTA, codArea);
			String tipoPanel = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_PANEL, codArea);

			query = queryValidaDispositivo(tipoResponsable, idVendedor, estadoAlta, idResponsable, tipoRuta, tipoPanel,
					filtroDispositivo, idPais);
			log.trace("QUERY VALIDA VENDEDOR ASIGNADO: " + query);
			pstmt = conn.prepareStatement(query);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				ret.add(0, rst.getBigDecimal(1));
				ret.add(1, rst.getBigDecimal(2));
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}

	public static String queryValidaDispositivo(String tipoResponsable, String idVendedor, String estadoAlta,
			String idResponsable, String tipoRuta, String tipoPanel, String filtroDispositivo, BigDecimal idPais) {

		String query = "";
		if (tipoRuta.equalsIgnoreCase(tipoResponsable)) {
			query = "SELECT (SELECT COUNT (1)" + " FROM TC_SC_RUTA" + " WHERE SECUSUARIOID = " + idVendedor
					+ " AND UPPER(ESTADO) = '" + estadoAlta.toUpperCase() + "'" + " AND TCSCCATPAISID = " + idPais + ")"
					+ " V1," + "(SELECT COUNT (1)" + " FROM TC_SC_DISPOSITIVO" + " WHERE VENDEDOR_ASIGNADO = "
					+ idVendedor + " AND UPPER(ESTADO) = '" + estadoAlta.toUpperCase() + "'" + " AND TCSCCATPAISID = "
					+ idPais + filtroDispositivo + ")" + " V2" + " FROM DUAL ";
		}

		if (tipoPanel.equalsIgnoreCase(tipoResponsable)) {
			query = "SELECT (SELECT COUNT (1) " + " FROM TC_SC_VEND_PANELPDV" + " WHERE VENDEDOR = " + idVendedor
					+ " AND TCSCCATPAISID = " + idPais + " AND UPPER(ESTADO) = '" + estadoAlta.toUpperCase()
					+ "' AND IDTIPO = " + idResponsable + " AND UPPER(TIPO) = '" + tipoPanel.toUpperCase() + "')"
					+ " V1," + " (SELECT COUNT (1)" + " FROM TC_SC_DISPOSITIVO" + " WHERE VENDEDOR_ASIGNADO =  "
					+ idVendedor + " AND UPPER(ESTADO) = '" + estadoAlta.toUpperCase() + "'" + " AND TCSCCATPAISID = "
					+ idPais + filtroDispositivo + ")" + " V2" + " FROM DUAL";

		}

		return query;
	}

	public static String getCodOficina(String idDistribuidor, String codVendedor, BigDecimal idPais)
			throws SQLException {
		String query = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String codOficina = null;
		Connection conn = null;
		try {
			conn = new ControladorBase().getConnRegional();

			query = "SELECT COD_OFICINA " + "FROM VE_VENDEDORES O WHERE "
					+ "COD_VENDEDOR = (SELECT COD_VENDEDOR FROM VE_VENDALMAC WHERE COD_VENDEDOR =?"
					+ " AND COD_BODEGA = (SELECT TCBODEGASCLID FROM TC_SC_ALMACEN_BOD WHERE TCSCDTSID = ?"
					+ " AND TCSCCATPAISID = ? ) AND FEC_DESASIGNAC IS NULL)";

			log.trace("qry codOficina NI: " + query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, codVendedor);
			pstmt.setBigDecimal(2, new BigDecimal(idDistribuidor));
			pstmt.setBigDecimal(3, idPais);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				codOficina = rst.getString(1);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(conn);
		}
		return codOficina;
	}

	public static boolean validarCodVendedor(Connection conn, String codVendedor, String estadoAlta,
			String idDispositivo, BigDecimal idPais) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		int res = 0;
		String query = getQueryValidarCodVendedor(idDispositivo);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, idPais);
			pstmt.setString(2, codVendedor);
			pstmt.setString(3, estadoAlta.toUpperCase());
			if (idDispositivo != null && !idDispositivo.equals("")) {
				pstmt.setBigDecimal(4, new BigDecimal(idDispositivo));
			}
			rst = pstmt.executeQuery();

			if (rst.next()) {
				res = rst.getInt(1);
			}
			if (res > 0) {
				return true;
			} else {
				return false;
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
	}

	public static String getQueryValidarCodVendedor(String idDispositivo) {
		String query = "SELECT COUNT(1) FROM DUAL WHERE EXISTS (SELECT 1 FROM TC_SC_DISPOSITIVO WHERE TCSCCATPAISID = ? AND COD_VENDEDOR = ? AND UPPER(ESTADO) = ?";
		if (idDispositivo != null && !idDispositivo.equals("")) {
			query += " AND TCSCDISPOSITIVOID = ?";
		}
		query += ")";
		return query;
	}
}
