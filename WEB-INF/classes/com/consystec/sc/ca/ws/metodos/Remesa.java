package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.RemesaCliente;
import com.consystec.sc.ca.ws.input.remesa.InputRemesa;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.remesa.OutputRemesa;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlRemesa;
import com.google.gson.GsonBuilder;

public class Remesa extends ControladorBase {
    private static final Logger log = Logger.getLogger(Remesa.class);
    String TOKEN = "";

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputRemesa objDatos, int metodo) {
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
            if (!"WEB".equalsIgnoreCase(objDatos.getToken()) ){
            	if (objDatos.getCodDispositivo() == null || "".equals(objDatos.getCodDispositivo().trim())) {
            
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(),
                            nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            
            	}
            	}
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para crear Remesas
     */
    public OutputRemesa crearRemesa(InputRemesa objDatos) {
        OutputRemesa objRespuestaWS = new OutputRemesa();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearRemesa";
        String url = "";

        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_POST);

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                log.trace("Ingresa a operar para servicio web...");
                // * Descomentar para solicitar token
                try {
                    TOKEN = getToken(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                            objDatos.getCodDispositivo(),0);
                    log.trace("TOKEN:" + TOKEN);
                } catch (Exception e) {
                    log.trace("Excepcion..." + e.getMessage());
                    log.trace("Causa..." + e.getCause());

                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
                            this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

                    log.error(e,e);
                }

                if (TOKEN.equals("LOGIN")) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaWS.setRespuesta(objRespuesta);

                } else if (TOKEN.contentEquals("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                    objRespuestaWS.setRespuesta(objRespuesta);
                	TOKEN="";
                }else if (objRespuesta != null) {
                    objRespuestaWS.setRespuesta(objRespuesta);

                } else {
                    if (isFullStack(objDatos.getCodArea())){
                    	CtrlRemesa recurso = new CtrlRemesa();
                    	objRespuestaWS=recurso.getDatos(objDatos, Conf.METODO_POST);                   			
                    }else{
                    	
	                    // obteniendo url de servicio
	                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREA_REMESA);
	
	                    log.trace("url: " + url);
	                    log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));
	
	                    if (url == null || url.equals("null") || "".equals(url)) {
	                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
	                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                        objRespuestaWS.setRespuesta(objRespuesta);
	                    } else {
	                        RemesaCliente wsRemesa = new RemesaCliente();
	                        wsRemesa.setServerUrl(url);
	                        objRespuestaWS = wsRemesa.creaRemesa(objDatos);
	                    }
	                } // <--Des/Comentar para solicitar token
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
     * M\u00E9todo para modificar Remesas
     */
    public OutputRemesa modRemesa(InputRemesa objDatos) {
        OutputRemesa objRespuestaWS = new OutputRemesa();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "modRemesa";
        String url = "";

        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_POST);

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                log.trace("Ingresa a operar para servicio web...");

                // * Descomentar para solicitar token
                try {
                    TOKEN = getToken(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                            objDatos.getCodDispositivo(),0);
                    log.trace("TOKEN:" + TOKEN);
                } catch (Exception e) {
                    log.trace("Excepcion..." + e.getMessage());
                    log.trace("Causa..." + e.getCause());

                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
                            this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

                    log.error(e,e);
                }

                if (TOKEN.equals("LOGIN")) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaWS.setRespuesta(objRespuesta);

                }else if (TOKEN.contentEquals("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                    objRespuestaWS.setRespuesta(objRespuesta);
                	TOKEN="";
                } else if (objRespuesta != null) {
                    objRespuestaWS.setRespuesta(objRespuesta);

                } else {
                    if (isFullStack(objDatos.getCodArea())){
                    	CtrlRemesa recurso = new CtrlRemesa();
                    	objRespuestaWS=recurso.getDatos(objDatos, Conf.METODO_PUT);                   			
                    }else{
	                    // obteniendo url de servicio
	                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MOD_REMESA);
	
	                    log.trace("url:" + url);
	                    log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));
	
	                    if (url == null || url.equals("null") || "".equals(url)) {
	                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
	                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                        objRespuestaWS.setRespuesta(objRespuesta);
	                    } else {
	                        RemesaCliente wsRemesa = new RemesaCliente();
	                        wsRemesa.setServerUrl(url);
	                        objRespuestaWS = wsRemesa.creaRemesa(objDatos);
	                    }
	                } // <--Des/Comentar para solicitar token
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
     * M\u00E9todo para obtener informaci\u00F3n Remesas
     */
    public OutputRemesa getRemesa(InputRemesa objDatos) {
        OutputRemesa objRespuestaWS = new OutputRemesa();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getRemesa";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_GET);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();

                // * Descomentar para solicitar token
                try {
                    TOKEN = getToken(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                            objDatos.getCodDispositivo(),0);
                    log.trace("TOKEN:" + TOKEN);
                } catch (Exception e) {
                    log.trace("Excepcion..." + e.getMessage());
                    log.trace("Causa..." + e.getCause());

                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
                            this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

                    log.error(e,e);
                }

                if (TOKEN.equals("LOGIN")) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaWS.setRespuesta(objRespuesta);

                }else if (TOKEN.contentEquals("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                    objRespuestaWS.setRespuesta(objRespuesta);
                	TOKEN="";
                } else if (objRespuesta != null) {
                    objRespuestaWS.setRespuesta(objRespuesta);

                } else {
                    if (isFullStack(objDatos.getCodArea())){
                    	CtrlRemesa recurso = new CtrlRemesa();
                    	objRespuestaWS=recurso.getDatos(objDatos, Conf.METODO_GET);
                    	if(!"WEB".equalsIgnoreCase(objDatos.getToken())&& (objRespuestaWS.getDatos().get(0).getRemesas() == null || objRespuestaWS.getDatos().get(0).getRemesas().isEmpty()
	            				   ||  objRespuestaWS.getDatos().get(0).getRemesas().get(0).getIdRemesa()==null)) {
	                           objRespuestaWS.getDatos().get(0).setRemesas(new ArrayList<InputRemesa>());
                    	}
                    }else{
	                    // obteniendo url de servicio
	                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_REMESA);
	
	                    log.trace("url: " + url);
	
	                    if (url == null || url.equals("null") || "".equals(url)) {
	                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
	                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                        objRespuestaWS.setRespuesta(objRespuesta);
	                    } else {
	                        RemesaCliente wsRemesa = new RemesaCliente();
	                        wsRemesa.setServerUrl(url);
	                        objRespuestaWS = wsRemesa.getRemesa(objDatos);
	                        if (objRespuestaWS.getDatos() != null) {
	                        	if(objDatos.getCodArea().equals("502")){
	                        		   if (objRespuestaWS.getDatos().get(0).getRemesas() == null || objRespuestaWS.getDatos().get(0).getRemesas().isEmpty()
	                        				   ||  objRespuestaWS.getDatos().get(0).getRemesas().get(0).getIdRemesa()==null) {
	                                       objRespuestaWS.getDatos().get(0).setRemesas(new ArrayList<InputRemesa>());
	                                   }
	                        	}else{
		                            if (objRespuestaWS.getDatos().get(0).getRemesas() == null || objRespuestaWS.getDatos().get(0).getRemesas().isEmpty()) {
		                                objRespuestaWS.getDatos().get(0).setRemesas(new ArrayList<InputRemesa>());
		                            }
	                        	}
	                        }
	                    }
	                } // <--Des/Comentar para solicitar token
                }
            } catch (SQLException e) {
                log.error(e, e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaWS.setRespuesta(objRespuesta);

            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaWS.setRespuesta(objRespuesta);
                log.error(e, e);

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
