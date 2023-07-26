package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import com.consystec.sc.ca.ws.input.solicitudescip.InputSolicitudesCip;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.solicitudescip.OutputSolicitudesCip;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.metodos.CtrlCip;



public class SolicitudesCip extends ControladorBase {
	String serverUrl;
	
	 private static final Logger log = Logger.getLogger(Remesa.class);
	    String TOKEN = "";

	    /***
	     * Validando que no vengan par\u00E9metros nulos
	     */
	    public Respuesta validarDatos(InputSolicitudesCip objDatos) {
	        Connection conn = null;
	        Respuesta objRespuesta = null;
	        String nombreMetodo = "validarDatos";

	        if (objDatos.getCodArea() == null || "".equals(objDatos.getCodArea().trim())) {
	            objRespuesta = new Respuesta();
	            objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODAREA_NULO_1, null, this.getClass().toString(), nombreMetodo,
	                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

	        } else if (objDatos.getCodArea().length() != Conf.LONG_CODPAIS) {
	            objRespuesta = new Respuesta();
	            objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONGINVALIDA_AREA_6, null, this.getClass().toString(),
	                    nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

	        } else {
	            BigDecimal idPais = null;
	            try {
	                conn = getConnLocal();
	                idPais = getidpais(conn, objDatos.getCodArea());
	                if (idPais == null || idPais.intValue() == 0) {
	                    objRespuesta = new Respuesta();
	                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_AREAINVALIDA_8, null, this.getClass().toString(),
	                            nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                }
	            } catch (SQLException e) {
	                log.error(e,e);
	                objRespuesta = new Respuesta();
	                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), nombreMetodo,
	                        null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	            } finally {
	                DbUtils.closeQuietly(conn);
	            }
	        }

	        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
	            objRespuesta = new Respuesta();
	            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), nombreMetodo,
	                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	        }

	        if (objDatos.getToken() == null || "".equals(objDatos.getToken()) || "".equals((objDatos.getToken().trim()))) {
	            objRespuesta = new Respuesta();
	            objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(), nombreMetodo,
	                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	        } else {
	            if (!"WEB".equalsIgnoreCase(objDatos.getToken())&& (objDatos.getCodDispositivo() == null || "".equals(objDatos.getCodDispositivo().trim()))) {
	                    objRespuesta = new Respuesta();
	                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(),
	                            nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	            }
	        }

	        return objRespuesta;
	    }

	/**
     * M\u00E9todo cliente para la SolicitudClip
     **/
    public OutputSolicitudesCip getSolicitudesClip(InputSolicitudesCip objeto) {
    	OutputSolicitudesCip objRespuestaSolicitudesClip = new OutputSolicitudesCip();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getSolicitudesClip";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objeto);
        log.trace("Ingreso a consultar CIP" + objeto.getCodArea());
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                
                try {
                    TOKEN = getToken(conn, objeto.getUsuario(), objeto.getToken(), objeto.getCodArea(),
                    		objeto.getCodDispositivo(),0);
                    log.trace("TOKEN:" + TOKEN);
                } catch (Exception e) {

                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
                            this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaSolicitudesClip.setRespuesta(objRespuesta);
                    log.error(e,e);
                }

                if (TOKEN.equals("LOGIN")) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaSolicitudesClip.setRespuesta(objRespuesta);
                } else if (TOKEN.contentEquals("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaSolicitudesClip.setRespuesta(objRespuesta);
                	TOKEN="";
                }else if (objRespuesta != null) {
                	objRespuestaSolicitudesClip.setRespuesta(objRespuesta);
                } else { 
	              
                	if (isFullStack(objeto.getCodArea()))
                	{
                		log.trace("Ingreso a consultar CIP");
                		CtrlCip recurso = new CtrlCip();
                		objRespuestaSolicitudesClip=recurso.getDatos(objeto);
                	}
                	else
                	{
                		objRespuesta = new Respuesta();
	                    objRespuesta.setDescripcion("Servicio no Disponible para el codigo de Area Ingresado"); 
	                    objRespuesta.setCodResultado("0");
	                    objRespuestaSolicitudesClip.setRespuesta(objRespuesta);   
                	
                	}
                	     
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSolicitudesClip.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSolicitudesClip.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
                objRespuestaSolicitudesClip.setToken(TOKEN);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaSolicitudesClip.setRespuesta(objRespuesta);
        }
        return objRespuestaSolicitudesClip;
    }
    public void setServerUrl(String serverUrl) {

        this.serverUrl = serverUrl;
    }
    

	}
