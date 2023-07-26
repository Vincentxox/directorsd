package com.consystec.sc.sv.ws.operaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;



import org.apache.log4j.Logger;


import com.consystec.sc.sv.ws.orm.Portabilidad;
import com.consystec.sc.sv.ws.util.Filtro;


public class OperacionEstadoPorta {
	private OperacionEstadoPorta(){}
	private static final Logger log = Logger.getLogger(OperacionEstadoPorta.class);
	public static String varNum="";	
	
	  public static  List<Portabilidad> getDatosPorta(Connection conn, List<Filtro> filtros)
	            throws SQLException {
	        PreparedStatement pstmt = null;
	        ResultSet rst = null;
	        List<Portabilidad>  datosPorta = new  ArrayList< Portabilidad>();
	        Portabilidad obj = new Portabilidad();
	   
	        String query="";
	      
		         query = "SELECT idportamovil, " +
		        		 "       num_portar, " +
		        		 "       num_temporal, " +
		        		 "       OPERADOR_DONANTE " +
		        		 "  FROM tc_Sc_portabilidad "
		        		 + "	where 1=1 " ;


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

	         try {
	            log.trace("Query para obtener datos portabilitad:" + query);
	            pstmt = conn.prepareStatement(query);
	            rst = pstmt.executeQuery();
	           
	            varNum="";
	            while (rst.next()) {
	            	obj = new Portabilidad();
	            	obj.setNum_portar(rst.getBigDecimal(Portabilidad.CAMPO_NUM_PORTAR));
	            	obj.setNum_temporal(""+rst.getBigDecimal(Portabilidad.CAMPO_NUM_TEMPORAL));
	            	obj.setOperador_donante(rst.getString(Portabilidad.CAMPO_OPERADOR_DONANTE));
	            	obj.setIdportamovil(rst.getBigDecimal(Portabilidad.CAMPO_IDPORTAMOVIL));
	            	datosPorta.add(obj);
	            
	            	varNum=varNum+obj.getNum_portar()+",";
	            	
	            }
	        } finally {
	            DbUtils.closeQuietly(pstmt);
	            DbUtils.closeQuietly(rst);
	        }

	        return datosPorta;
	    }

	  
	  public static HashMap <String, String> getEstado (Connection conn, String ids) throws SQLException{
		  HashMap <String, String> retDatos= new HashMap <String, String>();
		  String query="";
	      PreparedStatement pstmt = null;
	      ResultSet rst = null;
	      
	  
			  query= getqueryEstado(ids);
			 
			  log.trace("Query para obtener numero a portar:" + query);
		try{
			  pstmt = conn.prepareStatement(query);
	            rst = pstmt.executeQuery();
	            retDatos.put("12345678","PORTIN_NEW");
	            while (rst.next()) {
	            	log.trace("msisdn:"+rst.getString("msisdn"));
	            	log.trace("code:"+rst.getString("code"));
	            	retDatos.put(rst.getString("msisdn"),rst.getString("code"));
	            }
	      } finally {
	            DbUtils.closeQuietly(pstmt);
	            DbUtils.closeQuietly(rst);
	        }
		  return retDatos;
	  }
	  
	  public static String getqueryEstado(String ids) {
		  String query="";
		  query="SELECT a.msisdn, b.code " +
				  "  FROM TC_SPNMW_PORT_REQUEST_NUM a, TC_SPNMW_PORT_RQ_NUM_STATUS b " +
				  " WHERE a.tcspnmwportrqnstatusid = b.tcspnmwportrqnstatusid "
				  + "and a.msisdn in ("+ids+")" ;
		  return query;
	  }
}
