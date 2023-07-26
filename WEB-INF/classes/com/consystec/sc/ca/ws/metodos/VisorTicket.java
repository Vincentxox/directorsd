package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.ticket.InputTicket;
import com.consystec.sc.ca.ws.input.ticket.OutputTicket;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.metodos.CtrlVisorTicket;

public class VisorTicket extends ControladorBase{
	 private static final Logger log = Logger.getLogger(VisorTicket.class);
	    String TOKEN = "";
	    
	    /***
	     * Validando que no vengan par\u00E9metros nulos
	     */
	    public Respuesta validarDatos(InputTicket objDatos) {
	        Connection conn = null;
	        Respuesta objRespuesta = null;
	        String metodo = "validarDatos";

	        if (objDatos.getCodArea() == null || "".equals(objDatos.getCodArea())) {
	            objRespuesta = new Respuesta();
	            objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODAREA_NULO_1, null, this.getClass().toString(), metodo, null,
	                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);

	        } else if (objDatos.getCodArea().length() != Conf.LONG_CODPAIS) {
	            objRespuesta = new Respuesta();
	            objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONGINVALIDA_AREA_6, null, this.getClass().toString(), metodo,
	                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	        } else {
	            BigDecimal idPais = null;
	            try {
	                conn = getConnLocal();
	                idPais = getidpais(conn, objDatos.getCodArea());
	                if (idPais == null || idPais.intValue() == 0) {
	                    objRespuesta = new Respuesta();
	                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_AREAINVALIDA_8, null, this.getClass().toString(),
	                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                }
	            } catch (SQLException e) {
	                log.error(e,e);
	                objRespuesta = new Respuesta();
	                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
	                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	            } finally {
	                DbUtils.closeQuietly(conn);
	            }
	        }

	        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario())) {
	            objRespuesta = new Respuesta();
	            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
	                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	        }
	        
	        if (objDatos.getToken() == null || "".equals((objDatos.getToken().trim()))) {
	            objRespuesta = new Respuesta();
	            objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(), metodo,
	                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	        } else {
	            if (!"WEB".equalsIgnoreCase(objDatos.getToken())&& (objDatos.getCodDispositivo() == null || "".equals(objDatos.getCodDispositivo().trim()))) {
	                    objRespuesta = new Respuesta();
	                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(),
	                    		metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	            }
	        }

	        return objRespuesta;
	    }
	    
	    
	    
	    public OutputTicket registraVisorTCK(InputTicket objDatos) {
	    
	        Connection conn = null;
	        OutputTicket objReturn = new OutputTicket();
	        Respuesta objRespuesta = new Respuesta();
	        String metodo = "registraVisorTCK";
	        String url = "";
	        log.trace("inicia a validar valores...");
	        objRespuesta = validarDatos(objDatos);
	        if (objRespuesta == null) {
	            try {
	                conn = getConnLocal();
	                
	                TOKEN = gettokenString(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
	                        objDatos.getCodDispositivo());
	                log.trace("TOKEN:" + TOKEN);
	       
	               if(TOKEN.contains(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7+"")){ 
	            	   String mensaje = TOKEN.replace("-7||", "");
	                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7,mensaje,
	                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                	TOKEN="";
	                	  objReturn.setRespuesta(objRespuesta);
	               }
	               else if ("LOGIN".equals(TOKEN)) {
	                    log.trace("Usuario debe loguearse");
	                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
	                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                } else if (TOKEN.contains("ERROR")){
	                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
	                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                	String mensaje=TOKEN.replace("ERROR", "");
	                	objRespuesta.setExcepcion(mensaje);
	                	  objReturn.setRespuesta(objRespuesta);
	                	TOKEN="";
	                } else { 
	                	 if (isFullStack(objDatos.getCodArea()))
	                     { 
	                 		log.trace("consumir metodo");
	                 		CtrlVisorTicket recurso=new CtrlVisorTicket();
	                 		objRespuesta=recurso.registraTicket(objDatos);
	                 		  objReturn.setRespuesta(objRespuesta);
	                     }else{
	                    	  // obteniendo url de servicio
				                log.trace("url:" + url);
			
				                if ("".equals(url) || "null".equals(url) ) {
				                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
				                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                     }

		                }
	                }
	  	            } catch (SQLException e) {
	                log.error(e,e);
	                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
	                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                objReturn.setRespuesta(objRespuesta);
	            } catch (Exception e) {
	            	log.error(e,e);
	                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
	                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                objReturn.setRespuesta(objRespuesta);
	            } finally {
	                DbUtils.closeQuietly(conn);
	                objReturn.setToken(TOKEN);
	                objReturn.setRespuesta(objRespuesta);
	            }
	        } else {
	            log.trace("Advertencia:" + objRespuesta.getCodResultado());
	            log.trace("Descripcion:" + objRespuesta.getDescripcion());
	            objReturn.setRespuesta(objRespuesta);
	        }

	        return  objReturn;
	    }
	    
	    
	    public OutputTicket getVisorTCK(InputTicket objDatos) {
		    
	        Connection conn = null;
	        OutputTicket objReturn = new OutputTicket();
	        Respuesta objRespuesta = new Respuesta();
	        String metodo = "registraVisorTCK";
	        String url = "";
	        log.trace("inicia a validar valores...");
	        objRespuesta = validarDatos(objDatos);
	        if (objRespuesta == null) {
	            try {
	                conn = getConnLocal();
	                
	                TOKEN = gettokenString(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
	                        objDatos.getCodDispositivo());
	                log.trace("TOKEN:" + TOKEN);
	       
	               if(TOKEN.contains(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7+"")){ 
	            	   String mensaje = TOKEN.replace("-7||", "");
	                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7,mensaje,
	                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                	TOKEN="";
	                	 objReturn.setRespuesta(objRespuesta);
	               }
	               else if ("LOGIN".equals(TOKEN)) {
	                    log.trace("Usuario debe loguearse");
	                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
	                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objReturn.setRespuesta(objRespuesta);
	                } else if (TOKEN.contains("ERROR")){
	                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
	                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                	String mensaje=TOKEN.replace("ERROR", "");
	                	objRespuesta.setExcepcion(mensaje);
	                	 objReturn.setRespuesta(objRespuesta);
	                	TOKEN="";
	                } else { 
	                	 if (isFullStack(objDatos.getCodArea()))
	                     { 
	                 		log.trace("consumir metodo");
	                 		CtrlVisorTicket recurso=new CtrlVisorTicket();
	                 		objReturn=recurso.getTicket(objDatos);
	                     }else{
	                    	  // obteniendo url de servicio
				                log.trace("url:" + url);
			
				                if ("".equals(url) || "null".equals(url) ) {
				                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
				                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				                    objReturn.setRespuesta(objRespuesta);
	                     }

		                }
	                }
	  	            } catch (SQLException e) {
	                log.error(e,e);
	                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
	                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                objReturn.setRespuesta(objRespuesta);
	            } catch (Exception e) {
	            	log.error(e,e);
	                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
	                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                objReturn.setRespuesta(objRespuesta);
	            } finally {
	                DbUtils.closeQuietly(conn);
	                objReturn.setToken(TOKEN);
	               
	            }
	        } else {
	            log.trace("Advertencia:" + objRespuesta.getCodResultado());
	            log.trace("Descripcion:" + objRespuesta.getDescripcion());
	            objReturn.setRespuesta(objRespuesta);
	        }

	        return  objReturn;
	    }
}
