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

import com.consystec.sc.ca.ws.input.general.InputVersionCatalogo;
import com.consystec.sc.ca.ws.output.CatalogoVer.VersionCatalogo;
import com.consystec.sc.sv.ws.util.Conf;

public class OperacionVersionCatalogo {
	private OperacionVersionCatalogo() {
	}

	private static final Logger log = Logger.getLogger(OperacionVersionCatalogo.class);

	public static String armaQuery(String catalogo) {
		String query = " SELECT CATALOGO, URL  " + "  FROM TC_SC_CATALOGO_VERLOCAL  "
				+ " WHERE TO_DATE (version, 'yyyymmddHH24MiSS') >=  "
				+ "          TO_DATE (?, 'yyyymmddHH24MiSS')  AND ESTADO='1' AND UPPER(CATALOGO) IN(" + catalogo + ")"
				+ "  AND TCSCCATPAISID = ?" + " UNION " + "SELECT ?, " + "       CASE "
				+ "          WHEN (SELECT COUNT (1) " + "                  FROM TC_SC_CATALOGO_VERLOCAL "
				+ "                 WHERE     TO_DATE (version, 'yyyymmddHH24MiSS') >= "
				+ "                              TO_DATE (?, 'yyyymmddHH24MiSS') "
				+ "                       AND ESTADO = '1' " + "  AND TCSCCATPAISID = ?"
				+ "                       AND CATALOGO IN ( (SELECT NOMBRE "
				+ "                                            FROM TC_SC_CONFIGURACION "
				+ "                                           WHERE GRUPO = ?" + " AND TCSCCATPAISID = ?))) > 0 "
				+ "          THEN " + "             (SELECT valor " + "                FROM tc_sc_configuracion "
				+ "               WHERE nombre = 'URL_PAIS'" + "   AND TCSCCATPAISID = ?) " + "       END "
				+ "          URL " + "  FROM DUAL " + "UNION " + "  SELECT ?, " + "       CASE "
				+ "          WHEN (SELECT COUNT (1) " + "                  FROM TC_SC_CATALOGO_VERLOCAL "
				+ "                 WHERE     TO_DATE (version, 'yyyymmddHH24MiSS') >= "
				+ "                              TO_DATE (?, 'yyyymmddHH24MiSS') "
				+ "                       AND ESTADO = '1' " + "  AND TCSCCATPAISID = ?"
				+ "                       AND CATALOGO IN ( (SELECT GRUPO "
				+ "                                            FROM TC_SC_CONFIGURACION "
				+ "                                           WHERE GRUPO like 'IMPUESTO%' AND TCSCCATPAISID = ?"
				+ " UNION"
				+ " select NOMBRE from tc_sC_configuracion where nombre IN (SELECT NOMBRE FROM TC_SC_CONFIGURACION where grupo='IMPUESTO_PAIS' and TCSCCATPAISID = ?))) "
				+ "                                           AND TCSCCATPAISID =?) > 0 " + "          THEN "
				+ "             (SELECT valor " + "                FROM tc_sc_configuracion "
				+ "               WHERE nombre = 'URL_IMPUESTO' " + "                AND TCSCCATPAISID = ?) "
				+ "       END " + "          URL " + "  FROM DUAL ";

		return query;
	}

	public static List<VersionCatalogo> getCatalogo(Connection conn, InputVersionCatalogo param, BigDecimal idPais)
			throws SQLException {
		List<VersionCatalogo> lista = new ArrayList<VersionCatalogo>();
		VersionCatalogo objVersion = new VersionCatalogo();

		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String query = "";

		try {
			query = armaQuery(param.getGrupo().toUpperCase());

			log.trace("query:" + query);
			log.trace("CATALOGO:" + param.getGrupo().toUpperCase());
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, param.getFecha());

			log.trace("fecha: " + param.getFecha());
			
			pstmt.setBigDecimal(2, idPais);
			pstmt.setString(3, Conf.PAIS);
			pstmt.setBigDecimal(4, idPais);
			pstmt.setString(5, param.getFecha());
			pstmt.setBigDecimal(6, idPais);
			pstmt.setString(7, Conf.PAIS);
			pstmt.setBigDecimal(7, idPais);
			pstmt.setBigDecimal(8, idPais);
			pstmt.setString(9, Conf.IMPUESTO);
			pstmt.setString(10, param.getFecha());
			pstmt.setBigDecimal(11, idPais);
			pstmt.setBigDecimal(12, idPais);
			pstmt.setBigDecimal(13, idPais);
			pstmt.setBigDecimal(14, idPais);
			pstmt.setBigDecimal(15, idPais);
			rst = pstmt.executeQuery();

			while (rst.next()) {
				log.trace("Encontro información:" + rst.getString("CATALOGO"));
				objVersion = new VersionCatalogo();
				objVersion.setNombre(rst.getString("CATALOGO"));
				objVersion.setUrl(rst.getString("URL"));

				lista.add(objVersion);
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}
		return lista;
	}

}
