package com.consystec.sc.sv.ws.operaciones;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.pdv.InputPDV;
import com.consystec.sc.ca.ws.input.solicitud.InputArticuloSolicitud;
import com.consystec.sc.sv.ws.metodos.CtrlPuntoVenta;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.DestinoAlarma;
import com.consystec.sc.sv.ws.orm.DiaVisita;
import com.consystec.sc.sv.ws.orm.EncargadoPDV;
import com.consystec.sc.sv.ws.orm.NumRecarga;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.orm.Solicitud;
import com.consystec.sc.sv.ws.orm.SolicitudDet;
import com.consystec.sc.sv.ws.orm.VendedorPDV;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.SendMail;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.ericsson.sdr.dao.ConfigurationDAO;
import com.icon.sqlUtils.DataTypeSQLException;

public class OperacionPuntoVenta {
	private OperacionPuntoVenta() {
	}

	private static final Logger log = Logger.getLogger(OperacionPuntoVenta.class);

	public static int insertarPdv(Connection conn, PuntoVenta objeto) throws SQLException {
		PreparedStatement pstmt = null;
		int ret;

		String insert = " INSERT INTO TC_SC_PUNTOVENTA( tcscpuntoventaid,tcscdtsid,nombre,estado,tipo_negocio,documento,nit,nombre_fiscal,"
				+ "registro_fiscal,giro_negocio,direccion,departamento,municipio,distrito,observaciones,tcsczonacomercialid,creado_el,"
				+ "creado_por,longitud,latitud,tipo_producto,canal,subcanal,categoria,calle,avenida,pasaje,casa,colonia,referencia,"
				+ "tipo_contribuyente,digito_validador,barrio,qr,tcsccatpaisid) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		log.debug("insertPDV: " + insert);
		try {
			pstmt = conn.prepareStatement(insert);
			pstmt.setBigDecimal(1, objeto.getTcscpuntoventaid());
			pstmt.setBigDecimal(2, objeto.getTcscdtsid());
			pstmt.setString(3, objeto.getNombre());
			pstmt.setString(4, objeto.getEstado());
			pstmt.setString(5, objeto.getTipo_negocio());
			pstmt.setString(6, objeto.getDocumento());
			pstmt.setString(7, objeto.getNit());
			pstmt.setString(8, objeto.getNombre_fiscal());
			pstmt.setString(9, objeto.getRegistro_fiscal());
			pstmt.setString(10, objeto.getGiro_negocio());
			pstmt.setString(11, objeto.getDireccion());
			pstmt.setString(12, objeto.getDepartamento());
			pstmt.setString(13, objeto.getMunicipio());
			pstmt.setString(14, objeto.getDistrito());
			pstmt.setString(15, objeto.getObservaciones());
			pstmt.setString(16, objeto.getTcsczonacomercialid());
			pstmt.setString(17, objeto.getCreado_por());
			pstmt.setString(18, objeto.getLongitud());
			pstmt.setString(19, objeto.getLatitud());
			pstmt.setString(20, objeto.getTipo_producto());
			pstmt.setString(21, objeto.getCanal());
			pstmt.setString(22, objeto.getSubcanal());
			pstmt.setString(23, objeto.getCategoria());
			pstmt.setString(24, objeto.getCalle());
			pstmt.setString(25, objeto.getAvenida());
			pstmt.setString(26, objeto.getPasaje());
			pstmt.setString(27, objeto.getCasa());
			pstmt.setString(28, objeto.getColonia());
			pstmt.setString(29, objeto.getReferencia());
			pstmt.setString(30, objeto.getTipo_contribuyente());
			pstmt.setBigDecimal(31, objeto.getDigito_validador());
			pstmt.setString(32, objeto.getBarrio());
			pstmt.setString(33, objeto.getQr());
			pstmt.setBigDecimal(34, objeto.getTcsccatpaisid());
			ret = pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}

		return ret;
	}

	public static int updatePdv(Connection conn, PuntoVenta objeto) throws SQLException {
		PreparedStatement pstmt = null;
		int ret;

		String insert = "  UPDATE TC_SC_PUNTOVENTA SET tcscdtsid=?,nombre=?,estado=?,tipo_negocio=?,documento=?,"
				+ "nit=?,nombre_fiscal=?,registro_fiscal=?,giro_negocio=?,direccion=?,departamento=?,municipio=?,"
				+ "distrito=?,observaciones=?,tcsczonacomercialid=?,modificado_el=SYSDATE,modificado_por=?, longitud=?,"
				+ "latitud=?,tipo_producto=?,canal=?,subcanal=?,categoria=?,calle=?,avenida=?,pasaje=?,casa=?,colonia=?,referencia=?,"
				+ "tipo_contribuyente=?,barrio=?,digito_validador=?,qr=?  WHERE tcscpuntoventaid=? AND tcsccatpaisid=?";
		try {
			pstmt = conn.prepareStatement(insert);

			pstmt.setBigDecimal(1, objeto.getTcscdtsid());
			pstmt.setString(2, objeto.getNombre());
			pstmt.setString(3, objeto.getEstado());
			pstmt.setString(4, objeto.getTipo_negocio());
			pstmt.setString(5, objeto.getDocumento());
			pstmt.setString(6, objeto.getNit());
			pstmt.setString(7, objeto.getNombre_fiscal());
			pstmt.setString(8, objeto.getRegistro_fiscal());
			pstmt.setString(9, objeto.getGiro_negocio());
			pstmt.setString(10, objeto.getDireccion());
			pstmt.setString(11, objeto.getDepartamento());
			pstmt.setString(12, objeto.getMunicipio());
			pstmt.setString(13, objeto.getDistrito());
			pstmt.setString(14, objeto.getObservaciones());
			pstmt.setString(15, objeto.getTcsczonacomercialid());
			pstmt.setString(16, objeto.getModificado_por());
			pstmt.setString(17, objeto.getLongitud());
			pstmt.setString(18, objeto.getLatitud());
			pstmt.setString(19, objeto.getTipo_producto());
			pstmt.setString(20, objeto.getCanal());
			pstmt.setString(21, objeto.getSubcanal());
			pstmt.setString(22, objeto.getCategoria());
			pstmt.setString(23, objeto.getCalle());
			pstmt.setString(24, objeto.getAvenida());
			pstmt.setString(25, objeto.getPasaje());
			pstmt.setString(26, objeto.getCasa());
			pstmt.setString(27, objeto.getColonia());
			pstmt.setString(28, objeto.getReferencia());
			pstmt.setString(29, objeto.getTipo_contribuyente());
			pstmt.setString(30, objeto.getBarrio());
			pstmt.setBigDecimal(31, objeto.getDigito_validador());
			pstmt.setString(32, objeto.getQr());
			pstmt.setBigDecimal(33, objeto.getTcscpuntoventaid());
			pstmt.setBigDecimal(34, objeto.getTcsccatpaisid());
			ret = pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}

	public static void insertDiaVisita(Connection conn, List<DiaVisita> lista) throws SQLException {
		PreparedStatement pstmt = null;

		String insert = "INSERT INTO TC_SC_DIAVISITA (tcscdiavisitaid, "
				+ "                             tcscpuntoventaid, " + "                             nombre, "
				+ "                             estado, " + "                             creado_el, "
				+ "                             creado_por) " + "     VALUES (TC_SC_DIAVISITA_SQ.NEXTVAL, "
				+ "             ?, " + "             ?, " + "             ?, " + "             SYSDATE, "
				+ "             ?) ";

		for (DiaVisita obj : lista) {
			try {
				pstmt = conn.prepareStatement(insert);
				pstmt.setBigDecimal(1, obj.getTcscpuntoventaid());
				pstmt.setString(2, obj.getNombre());
				pstmt.setString(3, obj.getEstado());
				pstmt.setString(4, obj.getCreado_por());
				pstmt.executeUpdate();
			} finally {

				DbUtils.closeQuietly(pstmt);
			}
		}

	}

	public static void insertVendedorPDV(Connection conn, List<VendedorPDV> lista, BigDecimal ID_PAIS)
			throws SQLException {
		PreparedStatement pstmt = null;

		String insert = "INSERT INTO TC_SC_VEND_PANELPDV (tcscvendpanelpdvid, "
				+ "                                 idtipo, " + "                                 tipo, "
				+ "                                 vendedor, " + "                                 responsable, "
				+ "                                 estado, " + "                                 creado_el, "
				+ "                                 creado_por, " + "                                 tcsccatpaisid) "
				+ "     VALUES (TC_SC_VEND_PANELPDV_SQ.NEXTVAL, " + "             ?, " + "             ?, "
				+ "             ?,                                                     /*?,?,?,*/ " + "             ?, "
				+ "             ?, " + "             SYSDATE, " + "             ?, " + "             ?) ";

		for (VendedorPDV obj : lista) {
			try {
				pstmt = conn.prepareStatement(insert);
				pstmt.setBigDecimal(1, obj.getIdtipo());
				pstmt.setString(2, obj.getTipo());
				pstmt.setBigDecimal(3, obj.getVendedor());
				pstmt.setBigDecimal(4, obj.getResponsable());
				pstmt.setString(5, obj.getEstado());
				pstmt.setString(6, obj.getCreado_por());
				pstmt.setBigDecimal(7, ID_PAIS);
				pstmt.executeUpdate();
			} finally {
				DbUtils.closeQuietly(pstmt);
			}
		}

	}

	/**
	 * M\u00E9todo para insertar n\u00FAmeros de recarga de un pdv
	 * 
	 * @param conn
	 * @param lista
	 * @param estadoBaja
	 * @param estadoAlta
	 * @param tipoInsert:
	 *            0 para crear nuevo pdv, 1 para modificaci\u00F3n de pdv
	 * @throws SQLException
	 */
	@SuppressWarnings("unlikely-arg-type")
	public static void insertNumRecarga(Connection conn, List<NumRecarga> lista, String estadoBaja, String estadoAlta,
			int tipoInsert, String idPdv, String codArea, BigDecimal ID_PAIS, List<LogSidra> listaLog)
			throws SQLException, Exception {
		PreparedStatement pstmt = null;
		List<BigDecimal> numerosUpdate = new ArrayList<BigDecimal>();
		List<BigDecimal> numerosExiste = new ArrayList<BigDecimal>();
		List<BigDecimal> numerosDelete = new ArrayList<BigDecimal>();
		List<NumRecarga> numerosBorrarList = new ArrayList<NumRecarga>();
		String condicionNumeros = "";

		BigDecimal idTipo = lista.get(0).getIdtipo();
		String tipo = lista.get(0).getTipo();

		for (int a = 0; a < lista.size(); a++) {
			condicionNumeros += lista.get(a).getNum_recarga() + ",";
		}
		condicionNumeros = condicionNumeros.substring(0, condicionNumeros.length() - 1);

		// se obtiene el listado de numeros de baja a actualizar a estado alta
		numerosUpdate = getNumerosUpdate(conn, condicionNumeros, estadoBaja, ID_PAIS);

		if (tipoInsert == 1) {
			// se obtiene el listado de numero de alta a modificar a estado baja
			numerosDelete = getNumerosDelete(conn, condicionNumeros, idTipo, tipo, ID_PAIS);

			// se obtiene el listado de numeros existen y solo se actualiza el orden
			numerosExiste = getNumerosUpdate(conn, condicionNumeros, estadoAlta, ID_PAIS);
		}

		// verificando si ya existe un n\u00FAmero y si es modificaci\u00F3n se
		// actualiza el orden
		if (!numerosExiste.isEmpty()) {
			for (int b = 0; b < lista.size(); b++) {
				for (int a = 0; a < numerosExiste.size(); a++) {
					if (lista.get(b).getNum_recarga().equals(numerosExiste.get(a))) {

						updateNumeroRecarga(conn, lista.get(b));
						if (!numerosBorrarList.contains(lista.get(b).getNum_recarga())) {
							numerosBorrarList.add(lista.get(b));
						}
					}
				}
			}
		}

		// se borra del listado los elementos ya comprobados
		lista.removeAll(numerosBorrarList);

		// se reinicia el listado de elementos a borrar
		numerosBorrarList.clear();

		// verificando si ya existe un n\u00FAmero y solo darlo de alta
		if (!numerosUpdate.isEmpty()) {
			for (int b = 0; b < lista.size(); b++) {
				for (int a = 0; a < numerosUpdate.size(); a++) {
					if (lista.get(b).getNum_recarga().equals(numerosUpdate.get(a))) {

						updateNumeroRecarga(conn, lista.get(b));
						if (!numerosBorrarList.contains(lista.get(b).getNum_recarga())) {
							numerosBorrarList.add(lista.get(b));
						}
					}
				}
			}
		}

		// se borra del listado los elementos ya comprobados
		lista.removeAll(numerosBorrarList);

		// verificando si al modificar pdv se debe dar un n\u00FAmero de baja
		if (!numerosDelete.isEmpty()) {
			for (int a = 0; a < numerosDelete.size(); a++) {
				log.trace("NUMERO A BORRAR: " + numerosDelete.get(a));
				OperacionPuntoVenta.deleteTableNumRecarga(conn, NumRecarga.N_TABLA, idTipo, tipo, numerosDelete.get(a),
						ID_PAIS);
				procesarSolicitudNumRecarga(conn, idPdv, numerosDelete.get(a) + "", codArea, ID_PAIS, listaLog);
			}
		}

		// si la lista no esta vacia, indica que existen n\u00FAmeros para insertar
		if (!lista.isEmpty()) {
			String insert = "INSERT INTO TC_SC_NUMRECARGA (tcscnumrecargaid, "
					+ "                              idtipo, " + "                              num_recarga, "
					+ "                              orden, " + "                              tipo, "
					+ "                              estado, " + "                              creado_el, "
					+ "                              creado_por, " + "                              estado_payment, "
					+ "                              tcsccatpaisid) " + "     VALUES (TC_SC_NUMRECARGA_SQ.NEXTVAL, "
					+ "             ?, " + "             ?, " + "             ?, " + "             ?, "
					+ "             ?, " + "             SYSDATE, " + "             ?, " + "             ?, "
					+ "             ?) ";

			for (NumRecarga obj : lista) {
				try {
					pstmt = conn.prepareStatement(insert);
					pstmt.setBigDecimal(1, obj.getIdtipo());
					pstmt.setBigDecimal(2, obj.getNum_recarga());
					pstmt.setBigDecimal(3, obj.getOrden());
					pstmt.setString(4, obj.getTipo());
					pstmt.setString(5, obj.getEstado());
					pstmt.setString(6, obj.getCreado_por());
					pstmt.setString(7, obj.getEstado_payment());
					pstmt.setBigDecimal(8, ID_PAIS);
					int res = pstmt.executeUpdate();
					log.debug("Numero insertado: " + obj.getNum_recarga() + " - Resultado: " + res);
				} finally {
					DbUtils.closeQuietly(pstmt);
				}
			}
		}

	}

	/**
	 * M\u00E9todo para verificar si el nit ingresado ya existe en otro pdv
	 */
	public static BigDecimal validarNit(Connection conn, String nit, String estado, BigDecimal idpvd,
			BigDecimal ID_PAIS) throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = "SELECT COUNT (*) " + "  FROM tc_sc_puntoventa " + " WHERE upper(estado) =?"
				+ " AND NIT = ?  AND tcscpuntoventaid not in (?) and tcsccatpaisid=?";

		log.trace("select para validar si ya existe nit: " + query);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, estado.toUpperCase());
			pstmt.setString(2, nit);
			pstmt.setBigDecimal(3, idpvd);
			pstmt.setBigDecimal(4, ID_PAIS);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				ret = rst.getBigDecimal(1);
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}
		return ret;
	}

	public static int validaNit(Connection con, InputPDV inputPdv, String estado) throws Exception {
		int resultado = 0;
		long dtsId = 0;
		String nombreDts = "";
		String numeroDts = "";
		String nombrePdv = "";
		String query = "SELECT a.tcscdtsid DTS_ID, b.nombres DTS_NAME, b.numero DTS_NUMBER, a.nombre NOMBRE_PDV"
				+ "	FROM tc_sc_puntoventa a, tc_sc_dts b" + "	WHERE upper(a.estado) = ?" + "	AND a.nit = ?"
				+ "	and a.tcsccatpaisid = ?" + "	AND a.tcsccatpaisid = b.tcsccatpaisid"
				+ "	AND a.tcscdtsid = b.tcscdtsid";
		PreparedStatement pst = con.prepareStatement(query);
		try {
			pst.setString(1, estado.toUpperCase());
			pst.setString(2, inputPdv.getNit());
			pst.setLong(3, new ControladorBase().getIdPais(con, inputPdv.getCodArea()).longValue());
			ResultSet rs = pst.executeQuery();
			try {
				if (rs.next()) {
					resultado++;
					dtsId = rs.getLong("DTS_ID");
					nombreDts = rs.getString("DTS_NAME");
					numeroDts = rs.getString("DTS_NUMBER");
					nombrePdv = rs.getString("NOMBRE_PDV");
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			log.error("Error validando nit", e);
		} finally {
			pst.close();
		}
		if (resultado > 0) {
			Long idPais = new ControladorBase().getIdPais(con, inputPdv.getCodArea()).longValue();
			Map<String, String> configuracion = ConfigurationDAO.getConfigGroup(con,
					ConfigurationDAO.GROUP_NAME_MAIL_CONFIG, idPais);
			String sender = configuracion.get(Conf.CONFIG_CORREO_SENDER);
			String host = configuracion.get(Conf.CONFIG_CORREO_HOST);
			String port = configuracion.get(Conf.CONFIG_CORREO_PORT);
			String asunto = configuracion.get(Conf.CONFIG_CORREO_ASUNTO_PDV_DUP);
			String cuerpo = configuracion.get(Conf.CONFIG_CORREO_CUERPO_PDV_DUP);
			String cuerpoCompleto = MessageFormat.format(cuerpo, inputPdv.getNombreDTS(), inputPdv.getNombrePDV(),
					nombreDts, String.valueOf(dtsId), inputPdv.getNit(), nombrePdv, numeroDts);
			List<String> receptores = ConfigurationDAO.getAlarmRecipients(con, "ALARMA_PDV_DUPLICADO", idPais);
			List<LogSidra> listaLog = new ArrayList<>();
			for (String receptor : receptores) {
				String resp = SendMail.sendMail(sender, host, port, receptor, asunto, cuerpoCompleto);
				if (!resp.equals("OK")) {
					log.trace("fallo al enviar correo");
					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PDV,
							"WS CREA PDV - ENVIO ALARMA PDV DUPLICADO", "0", Conf.LOG_TIPO_NINGUNO,
							"Problema al enviar correo de alarma.", resp));
				}
			}
			UtileriasJava.doInsertLog(listaLog, inputPdv.getUsuario(), inputPdv.getCodArea());
		}
		return resultado;
	}

	/**
	 * M\u00E9todo para validar que el n\u00FAmero de recarga no se encuentre activo
	 * para otro punto de venta
	 */
	public static BigDecimal validarNumRecarga(Connection conn, String numRecarga, String estado, BigDecimal idpvd,
			String tipo, BigDecimal ID_PAIS) throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = "SELECT COUNT (*) " + "FROM TC_SC_NUMRECARGA " + "WHERE TCSCCATPAISID = ?"
				+ " AND NUM_RECARGA = ? AND UPPER(ESTADO) =?" + " AND TCSCNUMRECARGAID NOT IN "
				+ "(SELECT TCSCNUMRECARGAID FROM TC_SC_NUMRECARGA" + " WHERE TCSCCATPAISID = ?"
				+ " AND NUM_RECARGA =? AND UPPER(TIPO) = ?  AND IDTIPO = ?)";

		log.trace("query para validar n\u00FAmeros recarga: " + query);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, ID_PAIS);
			pstmt.setBigDecimal(2, new BigDecimal(numRecarga));
			pstmt.setString(3, estado.toUpperCase());
			pstmt.setBigDecimal(4, ID_PAIS);
			pstmt.setBigDecimal(5, new BigDecimal(numRecarga));
			pstmt.setString(6, tipo);
			pstmt.setBigDecimal(7, idpvd);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				ret = rst.getBigDecimal(1);
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}

		return ret;
	}

	/**
	 * M\u00E9todo para validar que el n\u00FAmero de recarga no se encuentre activo
	 * para otro punto de venta
	 */
	public static BigDecimal validarNUMRECARGAESTADO(Connection conn, String estado, BigDecimal idpvd, String tipo,
			BigDecimal ID_PAIS) throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = "SELECT COUNT (*) " + "FROM TC_SC_NUMRECARGA " + "WHERE TCSCCATPAISID = ?"
				+ " AND NUM_RECARGA IN (SELECT NUM_RECARGA FROM TC_SC_NUMRECARGA "
				+ "WHERE TCSCCATPAISID = ?  AND IDTIPO = ?" + " AND UPPER(TIPO) = ?) "
				+ "AND UPPER(ESTADO) =? AND TCSCNUMRECARGAID NOT IN "
				+ "(SELECT TCSCNUMRECARGAID FROM TC_SC_NUMRECARGA WHERE " + "TCSCCATPAISID = ?  AND NUM_RECARGA IN "
				+ "(SELECT NUM_RECARGA FROM TC_SC_NUMRECARGA WHERE TCSCCATPAISID = ?"
				+ " AND IDTIPO =?  AND UPPER(TIPO) = ?)" + " AND UPPER(TIPO) =? AND IDTIPO =?)";

		log.trace("query para validar n\u00FAmeros recarga:_" + query);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, ID_PAIS);
			pstmt.setBigDecimal(2, ID_PAIS);
			pstmt.setBigDecimal(3, idpvd);
			pstmt.setString(4, tipo.toUpperCase());
			pstmt.setString(5, estado.toUpperCase());
			pstmt.setBigDecimal(6, ID_PAIS);
			pstmt.setBigDecimal(7, ID_PAIS);
			pstmt.setBigDecimal(8, idpvd);
			pstmt.setString(9, tipo.toUpperCase());
			pstmt.setString(10, tipo.toUpperCase());
			pstmt.setBigDecimal(11, idpvd);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				ret = rst.getBigDecimal(1);
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}
		return ret;
	}

	/**
	 * M\u00E9todo utilizado para cuando se cambie de estado un pdv
	 * 
	 * @throws SQLException
	 **/
	public static void updateTable(Connection conn, String tabla, String estado, BigDecimal idPDV, String usuario)
			throws SQLException {
		PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(tabla);
		query.append(" set estado=?,  MODIFICADO_EL=SYSDATE, MODIFICADO_POR=? WHERE TCSCPUNTOVENTAID=?");

		log.trace("update estado pdv:" + query);
		try {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, estado.toUpperCase());
			pstmt.setString(2, usuario);
			pstmt.setBigDecimal(3, idPDV);
			pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
	}

	public static void updateEncargadoPDV(Connection conn, String tabla, String estado, BigDecimal idPDV)
			throws SQLException {
		PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(tabla);
		query.append(" set estado=?  WHERE TCSCPUNTOVENTAID=?");
		log.trace("update estado pdv:" + query);
		try {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, estado.toUpperCase());
			pstmt.setBigDecimal(2, idPDV);
			pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
	}

	/**
	 * M\u00E9todo utilizado para cuando se cambie de estado un pdv
	 * 
	 * @throws SQLException
	 **/
	public static void updateTableRecarga(Connection conn, String tabla, String estado, BigDecimal idPDV,
			String usuario, String tipo, String numRecarga, BigDecimal ID_PAIS) throws SQLException {
		PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(tabla);
		query.append(
				" SET ESTADO = ?, MODIFICADO_POR = ?, MODIFICADO_EL = SYSDATE WHERE TCSCCATPAISID = ? AND IDTIPO = ? AND UPPER(TIPO) = ?");
		query.append(numRecarga);

		log.trace("update estado pdv:" + query);
		try {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, estado.toUpperCase());
			pstmt.setString(2, usuario);
			pstmt.setBigDecimal(3, ID_PAIS);
			pstmt.setBigDecimal(4, idPDV);
			pstmt.setString(5, tipo.toUpperCase());

			pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
	}

	/**
	 * M\u00E9todo utilizado para cuando se actualice un pdv esto con el fin de
	 * eliminar la configuracion de dias o vendedores anterior y agregrar la nueva
	 * conf.
	 * 
	 * @throws SQLException
	 **/
	public static void deleteTable(Connection conn, String tabla, BigDecimal idPDV) throws SQLException {
		PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("delete from ");
		query.append(tabla);
		query.append(" where TCSCPUNTOVENTAID=?");

		log.trace("update estado pdv:" + query);
		try {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setBigDecimal(1, idPDV);
			pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
	}

	/**
	 * M\u00E9todo utilizado para cuando se actualice un pdv
	 * 
	 * @throws SQLException
	 **/
	public static void deleteTableRecarga(Connection conn, String tabla, BigDecimal idPDV, String tipo)
			throws SQLException {
		PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("delete from ");
		query.append(tabla);
		query.append(" where idtipo=? AND upper(TIPO)= upper(?)");
		log.trace("update estado pdv:" + query);
		try {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setBigDecimal(1, idPDV);
			pstmt.setString(2, tipo);
			pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
	}

	/**
	 * M\u00E9todo utilizado para eliminar vendedores de una panel
	 * 
	 * @throws SQLException
	 **/
	public static void deleteVendedor(Connection conn, String tabla, BigDecimal idPDV, String tipo, List<String> idVend)
			throws SQLException {
		PreparedStatement pstmt = null;

		for (int a = 0; a < idVend.size(); a++) {
			StringBuilder query = new StringBuilder();
			query.append("delete from ");
			query.append(tabla);
			query.append(" where idtipo=? AND upper(TIPO)= upper(?) and VENDEDOR=?");
			log.trace("delete vendedores:" + query);
			try {
				pstmt = conn.prepareStatement(query.toString());
				pstmt.setBigDecimal(1, idPDV);
				pstmt.setString(2, tipo);
				pstmt.setBigDecimal(3, new BigDecimal(idVend.get(a)));
				pstmt.executeUpdate();
			} finally {
				DbUtils.closeQuietly(pstmt);
			}
		}

	}

	public static void deleteVendedor2(Connection conn, String tabla, BigDecimal idPDV, String tipo,
			List<VendedorPDV> idVend) throws SQLException {
		PreparedStatement pstmt = null;

		for (int a = 0; a < idVend.size(); a++) {
			StringBuilder query = new StringBuilder();
			query.append("delete from ");
			query.append(tabla);
			query.append(" where idtipo=? AND upper(TIPO)= upper(?) and VENDEDOR=?");
			log.trace("delete vendedores:" + query);
			try {
				pstmt = conn.prepareStatement(query.toString());
				pstmt.setBigDecimal(1, idPDV);
				pstmt.setString(2, tipo);
				pstmt.setBigDecimal(3, idVend.get(a).getVendedor());
				pstmt.executeUpdate();
			} finally {
				DbUtils.closeQuietly(pstmt);
			}
		}

	}

	/**
	 * M\u00E9todo para validar si el punto de venta a modificar existe o no en
	 * sidra
	 * 
	 * @throws SQLException
	 **/
	public static BigDecimal existePdv(Connection conn, BigDecimal id, BigDecimal ID_PAIS) throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String sql = "select count(*) from tc_sc_puntoventa where tcscpuntoventaid=? and tcsccatpaisid=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1, id);
			pstmt.setBigDecimal(2, ID_PAIS);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				ret = rst.getBigDecimal(1);
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}

		return ret;
	}

	/**
	 * M\u00E9todo para validar si el distribuidor a asociar a un punto de venta o
	 * panel existe y se encuentra de Alta
	 * 
	 * @throws SQLException
	 **/
	public static BigDecimal existeDts(Connection conn, String id, String estado, BigDecimal ID_PAIS)
			throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String sql = getQueryExisteDts(id);
		log.trace("query existeDts " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, estado.toUpperCase());
			pstmt.setBigDecimal(2, ID_PAIS);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				ret = rst.getBigDecimal(1);
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}
		return ret;
	}

	public static String getQueryExisteDts(String id) {
		return "select count(*) from tc_sc_dts where tcscdtsid=" + id + " and upper(estado)=? and tcsccatpaisid=?";
	}

	/**
	 * M\u00E9todo para registrar informaci\u00F3n del encargado del pdv
	 * 
	 * @param conn
	 * @param objeto
	 * @return
	 * @throws SQLException
	 */
	public static int inserEncargadoPdv(Connection conn, EncargadoPDV objeto) throws SQLException {
		PreparedStatement pstmt = null;
		int ret;

		String insert = "INSERT INTO TC_SC_ENCARGADO_PDV (tcscencargadopdvid, "
				+ "                                 tcscpuntoventaid, " + "                                 nombres, "
				+ "                                 apellidos, " + "                                 telefono, "
				+ "                                 estado, " + "                                 cedula, "
				+ "tipo_documento) " + "     VALUES (?, " + "             ?, " + "             ?, " + "             ?, "
				+ "             ?, " + "             ?, " + "             ?,?) ";

		try {
			pstmt = conn.prepareStatement(insert);
			pstmt.setBigDecimal(1, objeto.getTcscencargadopdvid());
			pstmt.setBigDecimal(2, objeto.getTcscpuntoventaid());
			pstmt.setString(3, objeto.getNombres());
			pstmt.setString(4, objeto.getApellidos());
			pstmt.setBigDecimal(5, objeto.getTelefono());
			pstmt.setString(6, objeto.getEstado());
			pstmt.setString(7, objeto.getCedula());
			pstmt.setString(8, objeto.getTipoDocumento());
			ret = pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}

		return ret;
	}

	public static int updateEncargadoPdv(Connection conn, EncargadoPDV objeto) throws SQLException {
		PreparedStatement pstmt = null;
		int ret;

		String insert = "UPDATE TC_SC_ENCARGADO_PDV " + "   SET tcscpuntoventaid = ?, " + "       nombres = ?, "
				+ "       apellidos = ?, " + "       telefono = ?, " + "       estado = ?, "
				+ "       cedula = ? , tipo_documento=? " + " WHERE tcscencargadopdvid = ? ";

		log.trace("nombre encargado: " + objeto.getNombres());
		log.trace("apellido encargado: " + objeto.getApellidos());
		try {
			pstmt = conn.prepareStatement(insert);
			pstmt.setBigDecimal(1, objeto.getTcscpuntoventaid());
			pstmt.setString(2, objeto.getNombres());
			pstmt.setString(3, objeto.getApellidos());
			pstmt.setBigDecimal(4, objeto.getTelefono());
			pstmt.setString(5, objeto.getEstado());
			pstmt.setString(6, objeto.getCedula());
			pstmt.setString(7, objeto.getTipoDocumento());
			pstmt.setBigDecimal(8, objeto.getTcscencargadopdvid());

			ret = pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}

		return ret;
	}

	/**
	 * M\u00E9todo para verificar que los n\u00FAmeros de recarga a insertar no
	 * existan de baja en sidra
	 * 
	 * @param conn
	 * @param condicionNumeros
	 * @param estadoBaja
	 * @return
	 * @throws SQLException
	 */
	public static List<BigDecimal> getNumerosUpdate(Connection conn, String condicionNumeros, String estadoBaja,
			BigDecimal ID_PAIS) throws SQLException {
		List<BigDecimal> numerosUpdate = new ArrayList<BigDecimal>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		StringBuilder query = new StringBuilder();
		query.append(
				"SELECT NUM_RECARGA  FROM TC_SC_NUMRECARGA  WHERE TCSCCATPAISID = ?  AND UPPER(ESTADO) = ?  AND NUM_RECARGA IN (");
		query.append(condicionNumeros);
		query.append(") ");

		log.trace("query para obtener n\u00FAmeros a actualizar: " + query);
		try {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setBigDecimal(1, ID_PAIS);
			pstmt.setString(2, estadoBaja.toUpperCase());

			rst = pstmt.executeQuery();

			while (rst.next()) {
				numerosUpdate.add(rst.getBigDecimal(1));
			}
		} finally {

			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return numerosUpdate;
	}

	/**
	 * Update para n\u00FAmeros de recarga que se encuentran de baja y han sido
	 * utilizados para otro pdv
	 * 
	 * @param conn
	 * @param objeto
	 * @return
	 * @throws SQLException
	 */
	public static int updateNumeroRecarga(Connection conn, NumRecarga objeto) throws SQLException {
		PreparedStatement pstmt = null;
		int ret;

		String update = "UPDATE TC_SC_NUMRECARGA " + "   SET idtipo = ?, " + "       tipo = ?, " + "       estado = ?, "
				+ "       orden = ?, " + "       modificado_por = ?, " + "       modificado_el = SYSDATE "
				+ " WHERE tcsccatpaisid = NULL AND num_recarga = ? ";
		try {
			pstmt = conn.prepareStatement(update);
			pstmt.setBigDecimal(1, objeto.getIdtipo());
			pstmt.setString(2, objeto.getTipo());
			pstmt.setString(3, objeto.getEstado());
			pstmt.setBigDecimal(4, objeto.getOrden());
			pstmt.setString(5, objeto.getCreado_por());
			pstmt.setBigDecimal(6, objeto.getNum_recarga());
			ret = pstmt.executeUpdate();
			log.trace("Query update n\u00FAmero: " + update + " - Resultado: " + ret);
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}

	public static List<BigDecimal> getNumerosDelete(Connection conn, String condicionNumeros, BigDecimal idPDV,
			String tipoPDV, BigDecimal ID_PAIS) throws SQLException {
		List<BigDecimal> numerosUpdate = new ArrayList<BigDecimal>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String condicion = "";

		if (condicionNumeros == null || "".equals(condicionNumeros)) {
			condicion = "";
		} else {
			condicion = " AND NUM_RECARGA NOT IN (" + condicionNumeros + ")";
		}

		String query = queryNumDelete(condicion, idPDV, tipoPDV, ID_PAIS);

		try {
			log.trace("query para obtener n\u00FAmeros a borrar: " + query);
			pstmt = conn.prepareStatement(query);
			rst = pstmt.executeQuery();

			while (rst.next()) {
				numerosUpdate.add(rst.getBigDecimal(1));
			}
		} finally {

			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return numerosUpdate;
	}

	public static String queryNumDelete(String condicion, BigDecimal idPDV, String tipoPDV, BigDecimal ID_PAIS) {
		String query = "SELECT NUM_RECARGA " + "FROM TC_SC_NUMRECARGA " + "WHERE TCSCCATPAISID = " + ID_PAIS + condicion
				+ " AND IDTIPO = " + idPDV + " AND UPPER(TIPO) = '" + tipoPDV.toUpperCase() + "'";

		return query;
	}

	public static List<InputPDV> getPdvDisponibles(Connection conn, List<Filtro> filtros, String estadoActivo, int max,
			int min, BigDecimal ID_PAIS) throws SQLException {
		List<InputPDV> lstPdv = new ArrayList<InputPDV>();
		InputPDV objPdv = new InputPDV();
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = queryPdvDisponibles(filtros, max, min);
		log.trace("Query para obtener pdvDisponibles:" + query);

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, estadoActivo.toUpperCase());
			pstmt.setBigDecimal(2, ID_PAIS);
			pstmt.setBigDecimal(3, ID_PAIS);
			rst = pstmt.executeQuery();

			while (rst.next()) {
				objPdv = new InputPDV();

				objPdv.setIdPDV(rst.getString(PuntoVenta.CAMPO_TCSCPUNTOVENTAID));
				objPdv.setNombrePDV(rst.getString(PuntoVenta.CAMPO_NOMBRE));
				objPdv.setQr(rst.getString(PuntoVenta.CAMPO_QR));
				if (rst.getString(PuntoVenta.CAMPO_NUM_RECARGA) == null
						|| "".equals(rst.getString(PuntoVenta.CAMPO_NUM_RECARGA))) {
					objPdv.setNumRecargaOrden("");
				} else {
					objPdv.setNumRecargaOrden(rst.getString(PuntoVenta.CAMPO_NUM_RECARGA));
				}

				lstPdv.add(objPdv);

			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}

		return lstPdv;
	}

	public static String queryPdvDisponibles(List<Filtro> filtros, int max, int min) {
		String query = "SELECT PV.TCSCPUNTOVENTAID, " + "       PV.NOMBRE, " + "       PV.QR, "
				+ "       (SELECT NR.NUM_RECARGA " + "          FROM TC_SC_NUMRECARGA NR "
				+ "         WHERE     NR.ORDEN = 1 " + "               AND NR.tipo = UPPER ('PDV') "
				+ "               AND NR.IDTIPO = PV.TCSCPUNTOVENTAID " + "               AND NR.ESTADO = 'ALTA' "
				+ "               AND ROWNUM = 1" + "			    AND NR.tcsccatpaisid = PV.TCSCCATPAISID) "
				+ "          NUM_RECARGA " + "  FROM TC_SC_PUNTOVENTA PV " + " WHERE     UPPER (PV.ESTADO) = ? "
				+ "	AND PV.tcsccatpaisid = ?" + "       AND PV.TCSCPUNTOVENTAID NOT IN (SELECT RP.TCSCPUNTOVENTAID "
				+ "                                         FROM TC_SC_RUTA_PDV RP "
				+ "                                        WHERE RP.tcsccatpaisid = ?) ";

		if (!filtros.isEmpty()) {
			for (int i = 0; i < filtros.size(); i++) {
				if (filtros.get(i).getOperator().toString() == "between") {
					query += " AND " + filtros.get(i).getField() + " " + filtros.get(i).getOperator()
							+ filtros.get(i).getValue();
				} else {

					query += " AND " + filtros.get(i).getField() + " " + filtros.get(i).getOperator() + ""
							+ filtros.get(i).getValue() + "";
				}
			}
		}

		if (min == max && min > 0) {
			query = UtileriasBD.getRowNum(query, min);
		} else {
			query = UtileriasBD.getLimit(query, min, max);
		}
		return query;
	}

	public static int getCountPdvDisponibles(Connection conn, List<Filtro> filtros, String estadoActivo,
			BigDecimal ID_PAIS) throws SQLException {
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String query = queryCountPdvDisponibles(filtros);
		log.trace("Query para obtener count pdvDisponibles:" + query);

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, estadoActivo.toUpperCase());
			pstmt.setBigDecimal(2, ID_PAIS);
			rst = pstmt.executeQuery();

			while (rst.next()) {
				count = rst.getInt(1);
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}
		return count;
	}

	public static String queryCountPdvDisponibles(List<Filtro> filtros) {
		String query = "SELECT COUNT(1) " + "  FROM TC_SC_PUNTOVENTA PV " + " WHERE UPPER (PV.ESTADO) = ? "
				+ "       AND PV.TCSCPUNTOVENTAID NOT IN (SELECT RP.TCSCPUNTOVENTAID FROM TC_SC_RUTA_PDV RP) "
				+ "	AND TCSCCATPAISID = ?";

		if (!filtros.isEmpty()) {
			for (int i = 0; i < filtros.size(); i++) {
				if (filtros.get(i).getOperator().toString() == "between") {
					query += " AND " + filtros.get(i).getField() + " " + filtros.get(i).getOperator()
							+ filtros.get(i).getValue();
				} else {

					query += " AND " + filtros.get(i).getField() + " " + filtros.get(i).getOperator() + ""
							+ filtros.get(i).getValue() + "";
				}
			}
		}
		return query;
	}

	/**
	 * M\u00E9todo utilizado para cuando se actualice un pdv
	 * 
	 * @throws SQLException
	 **/
	public static void deleteTableNumRecarga(Connection conn, String tabla, BigDecimal idPDV, String tipo,
			BigDecimal numRecarga, BigDecimal ID_PAIS) throws SQLException {
		PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("DELETE FROM ");
		query.append(tabla);
		query.append(" WHERE TCSCCATPAISID = ?  AND IDTIPO = ? AND UPPER(TIPO)= ?  AND NUM_RECARGA =? ");

		log.trace("delete numRecarga:" + query);
		try {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setBigDecimal(1, ID_PAIS);
			pstmt.setBigDecimal(2, idPDV);
			pstmt.setString(3, tipo.toUpperCase());
			pstmt.setBigDecimal(4, numRecarga);
			pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
	}

	public static void procesarSolicitudNumRecarga(Connection conn, String pdv, String numRecarga, String codArea,
			BigDecimal ID_PAIS, List<LogSidra> listaLog) throws SQLException, Exception {
		PreparedStatement pstmt = null;
		String sql = "";

		String estadoSol = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SOLICITUD, Conf.SOL_ESTADO_PENDIENTE,
				codArea);

		sql = "SELECT A.tcscsolicitudid, " + "         A.tcscsolicitudDETid, "
				+ "         (SELECT COUNT (tcscsolicitudid) " + "            FROM tc_sc_solicitud_det "
				+ "           WHERE tcscsolicitudid = A.tcscsolicitudid) " + "            CONTEO "
				+ "    FROM tc_sc_solicitud_det A, tc_sc_solicitud B "
				+ "   WHERE     A.tcscsolicitudid = B.tcscsolicitudid " + "         AND B.tcsccatpaisid = ? "
				+ "         AND A.ARTICULO = ? " + "         AND B.idtipo = ? " + "         AND A.ESTADO = ? "
				+ "GROUP BY A.tcscsolicitudid, A.tcscsolicituddetid ";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1, ID_PAIS);
			pstmt.setString(2, numRecarga);
			pstmt.setString(3, pdv);
			pstmt.setString(4, estadoSol);
			try (ResultSet rst = pstmt.executeQuery()) {
				if (rst != null) {
					int conteo = 0;
					String solicitud = "";
					String solDetalle = "";
					while (rst.next()) {
						conteo = rst.getInt(3);
						solicitud = rst.getString(1);
						solDetalle = rst.getString(2);

						if (conteo > 1) {
							// Eliminar el articulo que coincida con el numRecarga
							delSolicitudDet(conn, solDetalle, numRecarga);
							CtrlPuntoVenta.addLog("Se elimino el detalle de Solicitud del número recarga" + numRecarga,
									solicitud, listaLog);
						} else {
							// La solicitud solo tiene un articulo y debe actualizarse
							String estado = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SOLICITUD,
									Conf.SOL_ESTADO_CANCELADA, codArea);
							updSolicitud(conn, solicitud, solDetalle, numRecarga, estado, codArea, ID_PAIS);
							CtrlPuntoVenta.addLog(
									"Cambio de estado de Solicitud numero " + solicitud + " y su detalle " + solDetalle,
									solicitud, listaLog);
						}
					}
				}
			}
		} finally {
			DbUtils.close(pstmt);
		}

	}

	public static String consultarSolicitud(Connection conn, String pdv, String codArea, BigDecimal ID_PAIS)
			throws SQLException, Exception {
		String solicitud = "";
		String estadoSol = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SOLICITUD, Conf.SOL_ESTADO_ACEPTADA,
				codArea);
		List<Filtro> condiciones = new ArrayList<Filtro>();
		condiciones.add(
				UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_TCSCCATPAISID, "" + ID_PAIS));
		condiciones.add(
				UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_ESTADO, "'" + estadoSol + "'"));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_IDTIPO, pdv));

		List<String> idsSol = UtileriasBD.getOneField(conn, Solicitud.CAMPO_TCSCSOLICITUDID,
				ControladorBase.getParticion(Solicitud.N_TABLA, Conf.PARTITION, "", codArea), condiciones, null);

		if (idsSol != null && !idsSol.isEmpty()) {
			solicitud = idsSol.get(0);
		}

		return solicitud;
	}

	public static List<InputArticuloSolicitud> consultarSolicitudDet(Connection conn, String solicitud)
			throws SQLException, Exception {
		List<InputArticuloSolicitud> lst = null;
		PreparedStatement pstmt = null;

		String query = "SELECT tcscsolicituddetid, articulo " + " FROM TC_SC_SOLICITUD_DET"
				+ " WHERE tcscsolicitudid =?";

		try {
			log.trace("Query para consultar Detalle Solicitud: " + query);

			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, new BigDecimal(solicitud));
			try (ResultSet rst = pstmt.executeQuery()) {
				if (rst != null) {
					lst = new ArrayList<>();
					while (rst.next()) {
						InputArticuloSolicitud detSol = new InputArticuloSolicitud();
						detSol.setIdSolicitudDet(rst.getString(1));
						detSol.setIdArticulo(rst.getString(2));

						lst.add(detSol);
					}
				}
			}
		} finally {
			DbUtils.close(pstmt);
		}

		return lst;
	}

	public static String delSolicitudDet(Connection conn, String detId, String numRecarga) throws SQLException {
		String res = "";
		PreparedStatement stmt = null;
		String query = "DELETE FROM TC_SC_SOLICITUD_DET" + " WHERE tcscsolicituddetid = ?" + " AND articulo = ?";

		try {
			log.trace("Query para eliminar Detalle Solicitud :" + query);
			stmt = conn.prepareStatement(query);
			stmt.setBigDecimal(1, new BigDecimal(detId));
			stmt.setBigDecimal(2, new BigDecimal(numRecarga));
			int del = stmt.executeUpdate();
			if (del > 0) {
				res = "OK";
			}
		} finally {
			DbUtils.close(stmt);
		}

		return res;
	}

	public static String updSolicitud(Connection conn, String solicitud, String detId, String numRecarga, String estado,
			String codArea, BigDecimal ID_PAIS) throws SQLException {
		String res = "";
		Statement stmt = null;
		String query = queryUpdSolicitud(solicitud, estado, codArea, ID_PAIS);

		try {
			log.trace("Query para actualizar Solicitud :" + query);
			stmt = conn.createStatement();
			int del = stmt.executeUpdate(query);
			if (del > 0) {
				query = queryudpSol(solicitud, detId, numRecarga, estado);
				log.trace("Query para actualizar Solicitud :" + query);
				int del2 = stmt.executeUpdate(query);
				if (del2 > 0) {
					res = "OK";
				}
			}
		} finally {
			DbUtils.close(stmt);
		}

		return res;
	}

	public static String queryudpSol(String solicitud, String detId, String numRecarga, String estado) {
		String query = "";
		query = "UPDATE " + SolicitudDet.N_TABLA + " SET " + SolicitudDet.CAMPO_ESTADO + " = '" + estado + "' "
				+ " WHERE " + SolicitudDet.CAMPO_TCSCSOLICITUDDETID + " = " + detId + " AND "
				+ SolicitudDet.CAMPO_ARTICULO + " = " + numRecarga;
		return query;
	}

	public static String queryUpdSolicitud(String solicitud, String estado, String codArea, BigDecimal ID_PAIS) {
		String query = "UPDATE " + ControladorBase.getParticion(Solicitud.N_TABLA, Conf.PARTITION, "", codArea)
				+ " SET " + Solicitud.CAMPO_ESTADO + " = '" + estado + "' " + " WHERE "
				+ Solicitud.CAMPO_TCSCSOLICITUDID + " = " + solicitud + " AND " + Solicitud.CAMPO_TCSCCATPAISID + " = "
				+ ID_PAIS;

		return query;
	}

	/*
	 * Metodo para borrar n\u00FAmeros de telefono cuando un pdv se convierte en
	 * fisico
	 */
	public static void borrarNum(Connection conn, BigDecimal idPDV, String tipoPDV, String codArea, BigDecimal ID_PAIS,
			List<LogSidra> listaLog) throws SQLException, Exception {
		List<BigDecimal> numerosDelete;
		// se obtiene el listado de numero de alta a modificar a estado baja
		numerosDelete = getNumerosDelete(conn, null, idPDV, tipoPDV, ID_PAIS);

		// verificando si al modificar pdv se debe dar un n\u00FAmero de baja
		if (!numerosDelete.isEmpty()) {
			for (int a = 0; a < numerosDelete.size(); a++) {
				log.trace("NUMERO A BORRAR: " + numerosDelete.get(a));
				OperacionPuntoVenta.deleteTableNumRecarga(conn, NumRecarga.N_TABLA, idPDV, tipoPDV,
						numerosDelete.get(a), ID_PAIS);
				procesarSolicitudNumRecarga(conn, "" + idPDV, numerosDelete.get(a) + "", codArea, ID_PAIS, listaLog);
			}
		}
	}

}
