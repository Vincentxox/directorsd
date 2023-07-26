package com.consystec.sc.ca.ws.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class Util {
	private static final Logger log = Logger.getLogger(Util.class);

	private Util() {
	}

	public static String getURLWSLOCAL(Connection conn, String codArea, String servicio) {
		String result = "";
		String query = "";

		try {
			query = "SELECT valor " + "  FROM TC_SC_CONFIGURACION c, tc_sc_cat_pais p "
					+ " WHERE     c.GRUPO = 'WS_LOCAL' " + "       AND upper(c.nombre) =upper('" + servicio + "') "
					+ "       AND c.tcsccatpaisid = p.tcsccatpaisid " + "       AND p.area = '" + codArea + "'";

			result = (String) JavaUtils.getOneFieldFromDual(conn, query);
		} catch (SQLException e) {
			log.error(e, e);
			result = "";
		}
		return result;
		// return result.replace("172.31.4.69:7003", "localhost:7001");
		//return result.replace("http://10.213.226.73:7001", "http://localhost:7001");
	}

}