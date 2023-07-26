package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.output.solicitud.EncabezadoDeuda;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionGetDeuda extends ControladorBase {
    private static final Logger log = Logger.getLogger(OperacionGetDeuda.class);
     SimpleDateFormat FORMATO_FECHAHORA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHAHORA);
    /**
     * M\u00E9todo para obtener todas las deudas registradas de un dts o bodega virtual (zona comercial) de un rango de fechas obtenido no mayor a 30 d\u00EDas 
     * @param conn
     * @param filtros
     * @param diferenciaDias
     * @param fechaInicio
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public  static List<EncabezadoDeuda> getDeuda(Connection conn, List<Filtro> filtros, int diferenciaDias, String fechaInicio, String codArea, BigDecimal idPais) throws SQLException, ParseException{
    	List<EncabezadoDeuda> lstDeuda = new ArrayList<EncabezadoDeuda>();
    	List<EncabezadoDeuda> lstDeudaOficial = new ArrayList<EncabezadoDeuda>();
    	 SimpleDateFormat FORMATO_FECHA_CORTA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
    	List<String> lstFechas= new ArrayList<String>();
    	EncabezadoDeuda objDeuda = null;
    	PreparedStatement pstmt = null;
        ResultSet rst = null;
        boolean encontroFecha=false;
        SimpleDateFormat FORMATO_FECHAHORA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHAHORA);
    	
        String query = queryGetDeuda(filtros, codArea);
        log.trace("Query para obtener deuda:" + query);
        try{
	        pstmt = conn.prepareStatement(query);
	        pstmt.setBigDecimal(1, idPais);
	        rst = pstmt.executeQuery();
	
	        lstFechas=getFechasResumen(diferenciaDias, fechaInicio);
	        log.trace("Tamanio Fechas:"+ lstFechas.size());
	        
	
	        while (rst.next()) {
	        	objDeuda = new EncabezadoDeuda();
	        
	       
	        			objDeuda.setFecha(FORMATO_FECHAHORA.format(rst.getTimestamp("FECHA")));
	        			objDeuda.setEstado(rst.getString("ESTADO"));
	        			objDeuda.setCreado_el(FORMATO_FECHAHORA.format(rst.getTimestamp("CREADO_EL")));
	        			objDeuda.setCreado_por(rst.getString("CREADO_POR"));
	        			objDeuda.setIdBuzon(rst.getString("TCSCBUZONID"));
	        			objDeuda.setNombreBuzon(rst.getString("BUZON"));
	        			objDeuda.setIdDTS(rst.getString("TCSCDTSID"));
	        			objDeuda.setNombreDTS(rst.getString("DTS"));
	        			objDeuda.setIdSolicitud(rst.getString("TCSCSOLICITUDID"));
	        			objDeuda.setTipoSolicitud(rst.getString("TIPO_SOLICITUD"));
	        			objDeuda.setOrigen(rst.getString("ORIGEN"));
	        			objDeuda.setTotalDeuda(rst.getString("TOTAL_DEUDA"));
	        			objDeuda.setTasaCambio(rst.getString("TASA_CAMBIO"));
	        			
	        			 if (rst.getString("TCSCBODEGAVIRTUALID") == null || rst.getString("TCSCBODEGAVIRTUALID").equals("null")
		     	                    || rst.getString("TCSCBODEGAVIRTUALID").equals("")) {
	        				 objDeuda.setIdBodega("");
	        			 }else{
	        				 objDeuda.setIdBodega(rst.getString("TCSCBODEGAVIRTUALID"));
	        			 }
	        			 if (rst.getString("BODEGAVIRTUAL") == null || rst.getString("BODEGAVIRTUAL").equals("0")
		     	                    || rst.getString("BODEGAVIRTUAL").equals("")) {
	        				 objDeuda.setNombreBodega("");
	        			 }else{
	        				 objDeuda.setNombreBodega(rst.getString("BODEGAVIRTUAL"));
	        			 }
	        	
	        			
	        			
	        			 if (rst.getString("BUZON_ANTERIOR") == null || rst.getString("BUZON_ANTERIOR").equals("null")
		     	                    || rst.getString("BUZON_ANTERIOR").equals("")) {
	        				 objDeuda.setIdBuzonAnterior("");
	        			 }else{
	        				 objDeuda.setIdBuzonAnterior(rst.getString("BUZON_ANTERIOR"));
	        			 }
	        			 if (rst.getString("NOMBRE_BUZON_ANTERIOR") == null || rst.getString("NOMBRE_BUZON_ANTERIOR").equals("0")
		     	                    || rst.getString("NOMBRE_BUZON_ANTERIOR").equals("")) {
	        				 objDeuda.setNombreBuzonAnterior("");
	        			 }else{
	        				 objDeuda.setNombreBuzonAnterior(rst.getString("NOMBRE_BUZON_ANTERIOR"));
	        			 }
	        	
	        			
	        			 if (rst.getString("OBSERVACIONES") == null || rst.getString("OBSERVACIONES").equals("null")
		     	                    || rst.getString("OBSERVACIONES").equals("")) {
	        				 objDeuda.setObservaciones("");
	        			 }else{
	        				 objDeuda.setObservaciones(rst.getString("OBSERVACIONES"));
	        			 }
	        		
	        			 if (rst.getString("OBS_CANCELACION") == null || rst.getString("OBS_CANCELACION").equals("null")
		     	                    || rst.getString("OBS_CANCELACION").equals("")) {
	        				 objDeuda.setObsCancelacion("");
	        			 }else{
	        				 objDeuda.setObsCancelacion(rst.getString("OBS_CANCELACION"));
	        				 
	        			 }
	          			 if (rst.getString("ORIGEN_CANCELACION") == null || rst.getString("ORIGEN_CANCELACION").equals("null")
		     	                    || rst.getString("ORIGEN_CANCELACION").equals("")) {
	          				 objDeuda.setOrigenCancelacion("");
	          			 }else{
	          				objDeuda.setOrigenCancelacion(rst.getString("ORIGEN_CANCELACION"));
	          			 }
	        			 if (rst.getString("MODIFICADO_EL") == null || rst.getString("MODIFICADO_EL").equals("null")
	     	                    || rst.getString("MODIFICADO_EL").equals("")) {
	        				 objDeuda.setModificado_el("");
	        			 }else{
	        				 objDeuda.setModificado_el(FORMATO_FECHAHORA.format(rst.getTimestamp("MODIFICADO_EL")));
	        			 }
	        			 if (rst.getString("MODIFICADO_POR") == null || rst.getString("MODIFICADO_POR").equals("null")
		     	                    || rst.getString("MODIFICADO_POR").equals("")) {
	        				 objDeuda.setModificado_por("");
	        			 }else{
	        				 objDeuda.setModificado_por(rst.getString("MODIFICADO_POR"));
	        			 }

	
	        	lstDeuda.add(objDeuda);
	        }
	        
	    	
	    	String fecha="";
	    	String fechaSig="";
	        for (int a=0; a<lstFechas.size();a++){
	        	encontroFecha=false;
	        	log.trace("Fecha "+(a+1)+":" + lstFechas.get(a));
	        
	        	for (int b=0; b<lstDeuda.size(); b++){
	        		Date fechaSolicitud = UtileriasJava.parseDate(lstDeuda.get(b).getFecha(), FORMATO_FECHAHORA);    
	        		fecha=FORMATO_FECHA_CORTA.format(fechaSolicitud);
	        	
	        		if(lstFechas.get(a).equals(fecha)){
	        			log.trace("Encontro deuda");
	        			
	        			lstDeudaOficial.add(lstDeuda.get(b));
	        			encontroFecha=true;
	        			
	        			if((b+1)<lstDeuda.size())
	        			{
	        				Date fechaSolicitud1 = UtileriasJava.parseDate(lstDeuda.get(b+1).getFecha(), FORMATO_FECHAHORA);
	        				fechaSig=FORMATO_FECHA_CORTA.format(fechaSolicitud1);
	        				if(fechaSig.equals(fecha)){
			        			lstDeudaOficial.add(lstDeuda.get(b+1));
			        		}
	        			}
		        		
	        			
		        	
		        		
	        			break;
	        		}
	        	
	        	}
	        	log.trace("2");
	        	//agregando elementos con datos vacias
	         	if (!encontroFecha){
	         		objDeuda= new EncabezadoDeuda();
	         		String fech=lstFechas.get(a);
	         		log.trace("fech:"+fech);
	         		Date fechaSolicitudNull = UtileriasJava.parseDate(fech, FORMATO_FECHA_CORTA);  
	         		String fecha1=FORMATO_FECHAHORA.format(fechaSolicitudNull);
	         		log.trace("fecha:"+ fecha1);
	         		objDeuda.setFecha(fecha1);
        			objDeuda.setEstado("");
        			objDeuda.setCreado_el("");
        			objDeuda.setCreado_por("");
        			objDeuda.setIdBuzon("");
        			objDeuda.setNombreBuzon("");
        			objDeuda.setIdDTS("");
        			objDeuda.setNombreDTS("");
        			objDeuda.setIdSolicitud("");
        			objDeuda.setTipoSolicitud("");
        			objDeuda.setOrigen("");
        			objDeuda.setTotalDeuda("");
        			objDeuda.setTasaCambio("");
        			objDeuda.setIdBodega("");
        			objDeuda.setNombreBodega("");
        			objDeuda.setIdBuzonAnterior("");
        			objDeuda.setNombreBuzonAnterior("");
        			objDeuda.setObservaciones("");
        			objDeuda.setObsCancelacion("");
        			objDeuda.setOrigenCancelacion("");
        			objDeuda.setModificado_el("");
        			objDeuda.setModificado_por("");
        			lstDeudaOficial.add(objDeuda);
	        	}
	        }
	      
        } finally{
        	DbUtils.closeQuietly(pstmt);
        	DbUtils.closeQuietly(rst);
        }
        log.trace("DeudasEncontradas:"+ lstDeuda.size());
    	
    	return lstDeudaOficial;
    }
    
    public static String queryGetDeuda(List<Filtro> filtros, String codArea) {

    	String query = "  SELECT S.FECHA, " +
    			"         S.TCSCSOLICITUDID, " +
    			"         S.TCSCBODEGAVIRTUALID, " +
    			"         DECODE (s.tcscbodegavirtualid, " +
    			"                 NULL, '0', " +
    			"                 (SELECT nombre " +
    			"                    FROM tc_sc_bodegavirtual " +
    			"                   WHERE tcscbodegavirtualid = s.tcscbodegavirtualid)) " +
    			"            AS BODEGAVIRTUAL, " +
    			"         S.TCSCDTSID, " +
    			"         D.NOMBRES AS DTS, " +
    			"         S.TCSCBUZONID, " +
    			"         BS.NOMBRE AS BUZON, " +
    			"         S.BUZON_ANTERIOR, " +
    			"         DECODE (s.tcscbodegavirtualid, " +
    			"                 NULL, '0', " +
    			"                 (SELECT nombre " +
    			"                    FROM tc_sc_buzonsidra " +
    			"                   WHERE tcscbuzonid = s.buzon_anterior)) " +
    			"            AS nombre_buzon_anterior, " +
    			"         s.tipo_solicitud, " +
    			"         s.observaciones, " +
    			"         s.origen, " +
    			"         ROUND ( (s.total_deuda * NVL (tasa_cambio, 1)), 2) total_deuda, " +
    			"         NVL (s.tasa_cambio, 1) tasa_cambio, " +
    			"         s.estado, " +
    			"         s.origen_cancelacion, " +
    			"         s.obs_cancelacion, " +
    			"         s.creado_el, " +
    			"         s.creado_por, " +
    			"         s.modificado_el, " +
    			"         s.modificado_por " +
    			"    FROM tc_Sc_solicitud PARTITION ("+ControladorBase.getPais(codArea)+") s" +
    			"         INNER JOIN tc_sc_dts d " +
    			"            ON (s.tcscdtsid = d.tcscdtsid AND s.tcsccatpaisid = d.tcsccatpaisid) " +
    			"         INNER JOIN tc_sc_buzonsidra bs " +
    			"            ON     (bs.tcscbuzonid = s.tcscbuzonid) " +
    			"               AND TIPO_SOLICITUD = " +
    			"                      (SELECT VALOR " +
    			"                         FROM TC_SC_CONFIGURACION " +
    			"                        WHERE UPPER (NOMBRE) = 'DEUDA' AND TCSCCATPAISID = ?) "; 
    
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
    	
    	query+="ORDER BY s.fecha ASC ";
    	return query;
    }
    
    /**
     * M\u00E9todo para obtener el listado de fechas a retornar
     * @param diferenciaDias
     * @param fechaInicio
     * @return
     * @throws ParseException
     */
    public  static List<String> getFechasResumen(int diferenciaDias, String fechaInicio) throws ParseException{
    	List<String> lstFechas = new ArrayList<String>();
    	String fechaDiaria="";
    	SimpleDateFormat FORMATO_FECHA_GT = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA_GT);
    	for (int a=0; a<=diferenciaDias; a++){       
        	if(a==0){
        		  

        		fechaDiaria=fechaInicio;
        	}
        	if (a>0){
        	 	Calendar cal = Calendar.getInstance();

        	 	SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
        		Date fechaSolicitud = UtileriasJava.parseDate(fechaInicio, formatoFecha);        	
              
                cal.setTime(fechaSolicitud);               
                cal.add(Calendar.DAY_OF_YEAR, new Integer(a)); // se suman los dias configurados para el cierre
                fechaSolicitud = FORMATO_FECHA_GT.parse(FORMATO_FECHA_GT.format(cal.getTime()));           
                fechaDiaria=formatoFecha.format(fechaSolicitud);
                log.trace("Fecha solicitud: " + fechaDiaria);
        	}
        	lstFechas.add(fechaDiaria);
        	
    	}
    	
    	return lstFechas;
    }
}
