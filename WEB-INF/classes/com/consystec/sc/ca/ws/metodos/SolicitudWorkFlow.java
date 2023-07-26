package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.SolicitudCliente;
import com.consystec.sc.ca.ws.input.solicitud.InputSolicitud;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.solicitud.OutputSolicitud;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlSolicitud;
import com.google.gson.GsonBuilder;

public class SolicitudWorkFlow extends ControladorBase {

    private static final Logger log = Logger.getLogger(SolicitudWorkFlow.class);
    String TOKEN = "";

    /**
     * Validando que no vengan par\u00E9metros nulos.
     * 
     * @param objDatos
     * @param metodo
     * @return
     */
    public Respuesta validarDatos(InputSolicitud objDatos, int metodo) {
        Connection conn = null;
        Respuesta objRespuesta = null;
        String nombreMetodo = "validarDatos";

        if (objDatos.getCodArea() == null ||  "".equals(objDatos.getCodArea().trim())) {
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

        if (objDatos.getToken() == null || "".equals((objDatos.getToken().trim()))) {
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
     * M\u00E9todo para crear solicitudes
     */
    public OutputSolicitud crearSolicitud(InputSolicitud objDatos) {
        OutputSolicitud objRespuestaSol = new OutputSolicitud();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearSolicitud";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_POST);
        log.trace("2");
        if (objRespuesta == null) {
            log.trace("1");
            try {
                conn = getConnLocal();
                log.trace("Ingresa a operar para servicio web...");
                TOKEN = gettokenString(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                        objDatos.getCodDispositivo());
                log.trace("TOKEN:" + TOKEN);
       
               if(TOKEN.contains(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7+"")){ 
            	   String mensaje = TOKEN.replace("-7||", "");
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7,mensaje,
                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	TOKEN="";
                	objRespuestaSol.setRespuesta(objRespuesta);
               }
               else if ("LOGIN".equals(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaSol.setRespuesta(objRespuesta);
                } else if (TOKEN.contains("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaSol.setRespuesta(objRespuesta);
                	TOKEN="";
                }else {
                	if (isFullStack(objDatos.getCodArea()))
                    { 
                		log.trace("consumir metodo");
                		CtrlSolicitud recurso=new CtrlSolicitud();
                		objRespuestaSol=recurso.getDatos(objDatos , Conf.METODO_POST);
                    }
                    else
                    {
                    	// obteniendo url de servicio
                        url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREASOLICITUD);
//                        url="http://localhost:8181/WS_SIDRA_PA/sidra/rest/postSolicitud/";//TODO cambiar url quemada

                        log.trace("url:" + url);
                        log.trace("json:" + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));

                        if (url == null || "null".equals(url) || "".equals(url)) {
                            objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                    metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                            objRespuestaSol.setRespuesta(objRespuesta);
                        } else {
                            SolicitudCliente wsSol = new SolicitudCliente();
                            wsSol.setServerUrl(url);
                            objRespuestaSol = wsSol.crearSolicitud(objDatos);
                        }
                    }
                    
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
            } catch (Exception e) {
                
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
                log.error(e,e);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaSol.setRespuesta(objRespuesta);
        }

        objRespuestaSol.setToken(TOKEN);
        return objRespuestaSol;
    }

    /**
     * M\u00E9todo para modificar cliente
     */
    public OutputSolicitud modSolicitud(InputSolicitud objDatos) {
        OutputSolicitud objRespuestaWS = new OutputSolicitud();
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
               else  if ("LOGIN".equals(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaWS.setRespuesta(objRespuesta);

                }else if (TOKEN.contains("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                    objRespuestaWS.setRespuesta(objRespuesta);
                	TOKEN="";
                }  else {
                	if (isFullStack(objDatos.getCodArea()))
                    { 
                		log.trace("consumir metodo");
                		CtrlSolicitud recurso=new CtrlSolicitud();
                		objRespuestaWS=recurso.getDatos(objDatos , Conf.METODO_PUT);
                    }
                    else
                    {
                    	 // obteniendo url de servicio
                        url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MODSOLICITUD);

                        log.trace("url:" + url);
                        log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));

                        if (url == null || "null".equals(url) || "".equals(url)) {
                            objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                    metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                            objRespuestaWS.setRespuesta(objRespuesta);
                        } else {
                            SolicitudCliente wsRemesa = new SolicitudCliente();
                            wsRemesa.setServerUrl(url);
                            objRespuestaWS = wsRemesa.modificaSolicitud(objDatos);
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
     * M\u00E9todo para obtener informaci\u00F3n solicitudes
     */
    public OutputSolicitud getSolicitud(InputSolicitud objDatos) {
        OutputSolicitud objRespuestaSol = new OutputSolicitud();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getSolicitud";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_GET);
        log.trace("2");
        if (objRespuesta == null) {
            log.trace("1");
            try {
                conn = getConnLocal();
                log.trace("Ingresa a operar para servicio web...");
                log.trace("json:" + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));
                
                TOKEN = gettokenString(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                        objDatos.getCodDispositivo());
                log.trace("TOKEN:" + TOKEN);
       
               if(TOKEN.contains(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7+"")){ 
            	   String mensaje = TOKEN.replace("-7||", "");
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7,mensaje,
                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	TOKEN="";
                	objRespuestaSol.setRespuesta(objRespuesta);
               }
               else

                if ("LOGIN".equals(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaSol.setRespuesta(objRespuesta);
                } else if (TOKEN.contains("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaSol.setRespuesta(objRespuesta);
                	TOKEN="";
                } else {
                	if (isFullStack(objDatos.getCodArea()))
                    { 
                		log.trace("consumir metodo");
                		CtrlSolicitud recurso=new CtrlSolicitud();
                		objRespuestaSol=recurso.getDatos(objDatos , Conf.METODO_GET);
                    }
                    else
                    {
                    	 // obteniendo url de servicio
                        url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETSOLICITUD);

                        log.trace("url:" + url);
                        if (url == null || "null".equals(url) || "".equals(url)) {
                            objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                    metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                            objRespuestaSol.setRespuesta(objRespuesta);
                        } else {
                            SolicitudCliente wsSol = new SolicitudCliente();
                            wsSol.setServerUrl(url);
                            objRespuestaSol = wsSol.getSolicitud(objDatos);
                        }
                    }
                   
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
            } catch (Exception e) {
                
                
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
                log.error(e,e);
            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaSol.setRespuesta(objRespuesta);
        }

        objRespuestaSol.setToken(TOKEN);
        return objRespuestaSol;
    }
}
