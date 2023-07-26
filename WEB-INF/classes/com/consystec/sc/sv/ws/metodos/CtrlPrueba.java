package com.consystec.sc.sv.ws.metodos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.util.ControladorBase;

public class CtrlPrueba extends ControladorBase {

	private static final Logger log = Logger.getLogger(CtrlPrueba.class);

	public Map<String, Object> test() {
		Map<String, Object> map = new HashMap<>();

		try (Connection sidra = getConnLocal()) {
			map.put("CONEXION_SIDRA", "Conexion de sidra obtenida exitosamente");
		} catch (SQLException e) {
			log.error("error obteniendo conexion sidra...", e);
			map.put("ERROR_CONEXION_SIDRA", e);
		}

		try (Connection modsec = getConnSeg()) {
			map.put("CONEXION_MODSEC", "Conexion de modsec obtenida exitosamente");
		} catch (Exception e) {
			log.error("error obteniendo conexion sidra...", e);
			map.put("ERROR_CONEXION_MODSEC", e);
		}

		return map;
	}

}
