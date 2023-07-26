package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.AnulacionCliente;
import com.consystec.sc.ca.ws.input.anulacion.InputAnulacion;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.anulacion.OutputAnulacion;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlAnulacion;

public class Anulacion extends ControladorBase {
    private static final Logger log = Logger.getLogger(Anulacion.class);
    String TOKEN = "";

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputAnulacion objDatos, int metodo) {
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

        if (objDatos.getUsuario() == null ||  "".equals(objDatos.getUsuario().trim())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), nombreMetodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        if (objDatos.getToken() == null ||  "".equals((objDatos.getToken().trim()))) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(), nombreMetodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        } else {
            if (!"WEB".equalsIgnoreCase(objDatos.getToken())&&(objDatos.getCodDispositivo() == null || "".equals(objDatos.getCodDispositivo().trim()))) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(),
                            nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                
            }
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para crear Anulacions
     */
    public OutputAnulacion crearAnulacion(InputAnulacion objDatos) {
        OutputAnulacion objRespuestaWS = new OutputAnulacion();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearAnulacion";
        String url = "";

        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_POST);

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                log.trace("Ingresa a operar para servicio web...");
                // Descomentar para solicitar token
                TOKEN = gettokenString(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                        objDatos.getCodDispositivo());
                log.trace("TOKEN:" + TOKEN);
       
               if(TOKEN.contains(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7+"")){ 
            	   String mensaje = TOKEN.replace("-7||", "");
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7,mensaje,
                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	TOKEN="";
                	objRespuestaWS.setRespuesta(objRespuesta);	
               }

                if ("LOGIN".equals(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaWS.setRespuesta(objRespuesta);

                } else if (TOKEN.contains("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaWS.setRespuesta(objRespuesta);
                	TOKEN="";
                } else {
                	
                	
                	if(isFullStack(objDatos.getCodArea())){
                		CtrlAnulacion recurso = new CtrlAnulacion();
                		objRespuestaWS =recurso.getDatos(objDatos, Conf.METODO_POST);
                		
                	}else{
		                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREA_ANULACION);
		                
		                log.trace("url: " + url);
		
		                if (url == null || "null".equals(url) || "".equals(url)) {
		                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
		                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
		                    objRespuestaWS.setRespuesta(objRespuesta);
		                } else {
		                    AnulacionCliente wsAnulacion = new AnulacionCliente();
		                    wsAnulacion.setServerUrl(url);
		                    objRespuestaWS = wsAnulacion.creaAnulacion(objDatos);
		                }
                	}
                } 

            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaWS.setRespuesta(objRespuesta);

            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaWS.setRespuesta(objRespuesta);
                log.error(e,e);

            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaWS.setRespuesta(objRespuesta);
        }

        objRespuestaWS.setToken(TOKEN);
        return objRespuestaWS;
    }

    /**
     * M\u00E9todo para obtener informaci\u00F3n Anulacions
     */
    public OutputAnulacion getAnulacion(InputAnulacion objDatos) {
        OutputAnulacion objRespuestaWS = new OutputAnulacion();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getAnulacion";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_GET);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();

                // Descomentar para solicitar token
                TOKEN = gettokenString(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                        objDatos.getCodDispositivo());
                log.trace("TOKEN:" + TOKEN);
       
               if(TOKEN.contains(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7+"")){ 
            	   String mensaje = TOKEN.replace("-7||", "");
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7,mensaje,
                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	TOKEN="";
                	objRespuestaWS.setRespuesta(objRespuesta);	
                	 objRespuestaWS.setToken(TOKEN);
               }

                if ("LOGIN".equals(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaWS.setRespuesta(objRespuesta);

                } else if (TOKEN.contains("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaWS.setRespuesta(objRespuesta);
                	TOKEN="";
                } else {
                	if(isFullStack(objDatos.getCodArea())){
                		CtrlAnulacion recurso = new CtrlAnulacion();
                		objRespuestaWS =recurso.getDatos(objDatos, Conf.METODO_GET);
                		
                	}else{
                	
	                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_ANULACION);
	
	                    log.trace("url: " + url);
	
	                    if (url == null || "null".equals(url) || "".equals(url)) {
	                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
	                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                        objRespuestaWS.setRespuesta(objRespuesta);
	                    } else {
	                        AnulacionCliente wsAnulacion = new AnulacionCliente();
	                        wsAnulacion.setServerUrl(url);
	                        objRespuestaWS = wsAnulacion.getAnulacion(objDatos);
	                    }
                	}
                } 

            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaWS.setRespuesta(objRespuesta);

            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaWS.setRespuesta(objRespuesta);
                log.error(e,e);

            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaWS.setRespuesta(objRespuesta);
        }

        objRespuestaWS.setToken(TOKEN);
        return objRespuestaWS;
    }
}
