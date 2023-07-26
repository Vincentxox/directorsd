package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.sv.ws.orm.TCSCLOGSIDRA;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.ericsson.sdr.dao.ConfigurationDAO;
import com.ericsson.sdr.security.Pais;

public class OperacionLogSidra {
	private OperacionLogSidra() {
	}

	private static final Logger log = Logger.getLogger(OperacionLogSidra.class);

	/*********************************************************************************
	 * M\u00E9todo para registrar en el log las transacciones de sidra
	 * 
	 * @return
	 * 
	 * @throws SQLException
	 *********************************************************************************/
	public static boolean insertaLog(Connection conn, List<LogSidra> listaObj, String usuario, BigDecimal idPais)
			throws SQLException {
		boolean resp = false;
		String valores = "";
		PreparedStatement pstmt = null;
		String insert = "";
		List<String> listaInserts = new ArrayList<>();
		String error = "";

		ConfigurationDAO objConfig = new ConfigurationDAO(null, Pais.getPais(idPais.longValue()));
		String loguear = objConfig.getValor(conn, ConfigurationDAO.NOMBRE_LOG_SIDRA);

		String campos[] = { TCSCLOGSIDRA.CAMPO_TCSCLOGSIDRAID, TCSCLOGSIDRA.CAMPO_TCSCCATPAISID,
				TCSCLOGSIDRA.CAMPO_TIPO_TRANSACCION, TCSCLOGSIDRA.CAMPO_ORIGEN, TCSCLOGSIDRA.CAMPO_ID,
				TCSCLOGSIDRA.CAMPO_TIPO_ID, TCSCLOGSIDRA.CAMPO_DESCRIPCION, TCSCLOGSIDRA.CAMPO_RESULTADO,
				TCSCLOGSIDRA.CAMPO_DESCRIPCION_ERROR, TCSCLOGSIDRA.CAMPO_FECHA_LOG, TCSCLOGSIDRA.CAMPO_USUARIO };

		// ARMANDO INSERTS
		if (!listaObj.isEmpty()) {
			for (LogSidra obj : listaObj) {
				error = (String) (obj.getDescripcionError() == null ? "NULL" : obj.getDescripcionError());
				valores = UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, TCSCLOGSIDRA.SEQUENCE,
						Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPais.toString(), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, obj.getTipoTransaccion(), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, obj.getOrigen(), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, obj.getId(), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, obj.getTipoId(), Conf.INSERT_SEPARADOR_SI)
						+ "(SELECT VALOR FROM TC_SC_CONFIGURACION WHERE TCSCCATPAISID = " + idPais
						+ " AND UPPER(NOMBRE) = '" + obj.getTipoTransaccion().toUpperCase()
						+ "' AND GRUPO = 'TRANSACCIONES_LOG'), "
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, obj.getResultado(), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, error.replace("'", "\""), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, usuario, Conf.INSERT_SEPARADOR_NO);

				listaInserts.add(valores);
			}

			// armando insert
			insert = UtileriasBD.armarQueryInsertAll(TCSCLOGSIDRA.N_TABLA, campos, listaInserts);

			if ("1".equalsIgnoreCase(loguear)) {
				try {
					pstmt = conn.prepareStatement(insert);
					pstmt.executeUpdate();
					resp = true;
					conn.commit();
				} catch (SQLException e) {
					log.error("Error al insertar log Sidra. " + e);
					conn.rollback();
					resp = false;
				} finally {
					DbUtils.closeQuietly(pstmt);
				}
			} else {
				log.debug("parametro de logueo TC_SC_LOG_SIDRA se encuentra apagado...");
			}
		}

		return resp;
	}

	public static boolean insertaLogIndividual(Connection conn, LogSidra obj, String usuario, BigDecimal idPais)
			throws SQLException {
		boolean resp = false;
		String valores = "";
		PreparedStatement pstmt = null;
		String insert = "";
		List<String> listaInserts = new ArrayList<String>();
		String error = "";

		String campos[] = { TCSCLOGSIDRA.CAMPO_TCSCLOGSIDRAID, TCSCLOGSIDRA.CAMPO_TCSCCATPAISID,
				TCSCLOGSIDRA.CAMPO_TIPO_TRANSACCION, TCSCLOGSIDRA.CAMPO_ORIGEN, TCSCLOGSIDRA.CAMPO_ID,
				TCSCLOGSIDRA.CAMPO_TIPO_ID, TCSCLOGSIDRA.CAMPO_DESCRIPCION, TCSCLOGSIDRA.CAMPO_RESULTADO,
				TCSCLOGSIDRA.CAMPO_DESCRIPCION_ERROR, TCSCLOGSIDRA.CAMPO_FECHA_LOG, TCSCLOGSIDRA.CAMPO_USUARIO };

		// ARMANDO INSERTS
		error = (String) (obj.getDescripcionError() == null ? "NULL" : obj.getDescripcionError());
		valores = UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, obj.getTcsclogsidraid().toString(),
				Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPais.toString(), Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, obj.getTipoTransaccion(), Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, obj.getOrigen(), Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, obj.getId(), Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, obj.getTipoId(), Conf.INSERT_SEPARADOR_SI)
				+ "(SELECT VALOR FROM TC_SC_CONFIGURACION WHERE TCSCCATPAISID = " + idPais + " AND UPPER(NOMBRE) = '"
				+ obj.getTipoTransaccion().toUpperCase() + "' AND GRUPO = 'TRANSACCIONES_LOG'), "
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, obj.getResultado(), Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, error.replace("'", "\""), Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, usuario, Conf.INSERT_SEPARADOR_NO);

		listaInserts.add(valores);

		// armando insert
		insert = UtileriasBD.armarQueryInsertAll(TCSCLOGSIDRA.N_TABLA, campos, listaInserts);

		try {
			pstmt = conn.prepareStatement(insert);
			pstmt.executeUpdate();
			resp = true;
			conn.commit();
		} catch (SQLException e) {
			log.error("Error al insertar log Sidra. " + e);
			conn.rollback();
			resp = false;
		} finally {
			DbUtils.closeQuietly(pstmt);
		}

		return resp;
	}
}
