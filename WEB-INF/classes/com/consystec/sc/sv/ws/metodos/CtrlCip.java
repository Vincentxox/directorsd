package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.solicitudescip.InputSolicitudesCip;
import com.consystec.sc.ca.ws.output.solicitudescip.OutputSolicitudesCip;
import com.consystec.sc.sv.ws.operaciones.OperacionesCip;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlCip extends ControladorBase{
	private static final Logger log = Logger.getLogger(CtrlFolioVirtual.class);
	private static List<LogSidra> listaLog = new ArrayList<LogSidra>();
	private static String servicioGet = Conf.LOG_GET_SOLICITUDES_CIP;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
	 public Respuesta validarInput(Connection conn, InputSolicitudesCip input, BigDecimal idPais) {
		 String nombreMetodo = "validarInput";
	     String nombreClase = new CurrentClassGetter().getClassName();
	     String estadoAlta="";
	    
	        Respuesta r = new Respuesta();
	        String datosErroneos = "";
	        boolean flag = false;
		 
	        if (input.getCodArea() == null || "".equals(input.getCodArea())) {
	            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_303, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
	        } else if (input.getCodArea().length() != 3){
	            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_LONG_304, null, nombreClase,
	                   nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
	        } else if (!isNumeric(input.getCodArea())) {
	            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_NUM_305, null, nombreClase,
	                   nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
	        }
	        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
	            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase,
	                   nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
	        }
	       
	        
	        if(input.getNumeroaPortar()==null || "".equals(input.getNumeroaPortar().trim()))
	        {
	        	datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_NUMEROAPORTAR_VACIO_656, null, nombreClase,
	                   nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
	        	
	        }
	        else if (!isNumeric(input.getNumeroaPortar()))
	        {
	        	 datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_NUMEROAPORTAR_NO_NUMERICO_657, null, nombreClase,
		                   nombreMetodo, null, input.getCodArea()).getDescripcion();
		            flag = true;
	        }
	        
	        if (input.getTipoDocumento() == null || "".equals(input.getTipoDocumento().trim())) {
	            datosErroneos += getMensaje(Conf_Mensajes.MSJ_TIPODOCUMENTO_NULO_274, null, nombreClase,
	                   nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
	           }
	        else
	        {
	           try
		        {
	        	 estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
	        	List<Filtro> condiciones = new ArrayList<Filtro>();
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE, input.getTipoDocumento()));
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, Conf.GRUPO_TIPO_DOCUMENTO));
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));	         
	            int existeTipoDoc = UtileriasBD.selectCount(conn, Catalogo.N_TABLA, condiciones);

	            if (existeTipoDoc <= 0) {
	            	datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_DOCUMENTO_266, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
	            	flag=true;
	            }	
	           }
	           catch(Exception e)
	           {log.error(e,e);
	        	   datosErroneos +=  getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
	           }
	        }
	        
	        if (input.getNumeroDocumento() == null || "".equals(input.getNumeroDocumento().trim())) {
	            datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_NUMERO_DOCUMENTO_658, null, nombreClase,
	                   nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
	        }
	     
	        if (flag) {
	        	 r = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo, datosErroneos, input.getCodArea());
	        } else {
	            r.setDescripcion("OK");
	            r.setCodResultado("1");
	            r.setMostrar("0");
	        }
		 return r;
	 }

	   public OutputSolicitudesCip getDatos(InputSolicitudesCip input) {
		    listaLog = new ArrayList<LogSidra>();
	        String nombreMetodo = "getDatos";
	        String nombreClase = new CurrentClassGetter().getClassName();
	        Respuesta respuesta = new Respuesta();
	        OutputSolicitudesCip output = new OutputSolicitudesCip();
	        COD_PAIS = input.getCodArea();
	        String RespuestaWS="";
	        Connection conn = null;
	        try
	        {
	        	 conn=getConnRegional();
	        	ID_PAIS = getIdPais(conn, input.getCodArea());	        	 
	        	 RespuestaWS=UtileriasJava.getConfig(conn, Conf.GRUPO_CONSULTA_CIP, Conf.RESPUESTA_CERVICIO, input.getCodArea());
	             // Validación de datos en el input
	             respuesta = validarInput(conn, input,  ID_PAIS);
	             log.trace("Respuesta validación: " + respuesta.getDescripcion());
	             if (!"OK".equalsIgnoreCase(respuesta.getDescripcion())) {
	                 output.setRespuesta(respuesta);
	                 listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));
	                 return output;
	             }	            	            
	             Respuesta respuestaws;
	             respuestaws = OperacionesCip.doGet(conn, input, ID_PAIS);

	           if (respuestaws.getDescripcion().equalsIgnoreCase(RespuestaWS))
	        		   {	        	 
	        	   respuesta=getMensaje(Conf_Mensajes.OK_SERVICIO_SOLICITADO_CIP_203, null, nombreClase, nombreMetodo, respuestaws.getDescripcion(), input.getCodArea());	        		        	        	   
	            }	
	           else
	           {		        	  	        	 
	        	   respuesta=getMensaje(Conf_Mensajes.MSJ_NO_SE_ENCONTRO_CODIGO_CIP_660, null, nombreClase, nombreMetodo, respuestaws.getDescripcion(), input.getCodArea());
	           }  
	           output.setRespuesta(respuesta);
	        }catch(Exception e)
	        {
	        	respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
	            log.error("Excepcion: " + e.getMessage(), e);
	            output.setRespuesta(respuesta);
	            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet , "0", Conf.LOG_TIPO_NINGUNO, "Problema en al consultar Cip.", e.getMessage()));
	        }		        
	        finally
	        {
	        	log.trace("Descripcion: " + output.getRespuesta());
	            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
	        	 DbUtils.closeQuietly(conn);
	        	
	        }
	        return  output;
	   }
	   
}
