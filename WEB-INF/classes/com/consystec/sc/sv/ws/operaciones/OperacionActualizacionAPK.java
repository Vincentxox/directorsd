package com.consystec.sc.sv.ws.operaciones;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;


import com.consystec.sc.sv.ws.util.ControladorBase;
import com.ericsson.sc.ca.ws.dto.VersionApkDTO;

public class OperacionActualizacionAPK extends ControladorBase {
	
	private QueryRunner qr;
	
	
	private static final String SQLLASTVERSION = "SELECT VERSION version, UBICACION ubicacion, NOMBRE nombre, ACTUALIZAR actualizar FROM TC_SC_VERSION_APK WHERE  ESTADO = 'A' ";
	
	
	
	public OperacionActualizacionAPK(){
		qr = new QueryRunner();
		
	}
	
	
	public VersionApkDTO getLastVersionAPK(Connection conn)  throws SQLException{ 
		return qr.query(conn, SQLLASTVERSION , new BeanHandler<>(VersionApkDTO.class));
	}
	
	
		
	
	
	
}
