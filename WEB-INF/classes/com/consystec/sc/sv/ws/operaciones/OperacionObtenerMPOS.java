package com.consystec.sc.sv.ws.operaciones;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;


import com.consystec.sc.sv.ws.util.ControladorBase;
import com.ericsson.sc.ca.ws.dto.MposDTO;

 public class OperacionObtenerMPOS extends ControladorBase {
	
	private QueryRunner qr;
	
	
	private static final String SQLLASTVERSION = "SELECT VERSION version, UBICACION ubicacion, NOMBRE nombre FROM TC_SC_MPOS_APK WHERE  ESTADO = 'A' ";
	
	
	
	public OperacionObtenerMPOS(){
		qr = new QueryRunner();
		
	}
	
	
	public MposDTO getDataMPOS(Connection conn)  throws SQLException{ 
		return qr.query(conn, SQLLASTVERSION , new BeanHandler<>(MposDTO.class));
	}
	
	
		
	
	
	
}

