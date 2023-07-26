package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.JornadaCliente;
import com.consystec.sc.ca.ws.input.jornada.InputJornada;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.jornada.OutputJornada;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlJornada;
import com.google.gson.GsonBuilder;

public class Jornada extends ControladorBase {

    private static final Logger log = Logger.getLogger(Jornada.class);
    String TOKEN = "";

    /**
     * Validando que no vengan par\u00E9metros nulos.
     * 
     * @param objDatos
     * @param metodo
     * @return
     */
    public Respuesta validarDatos(InputJornada objDatos, int metodo) {
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
                log.error(e, e);
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

        if (metodo == Conf.METODO_PUT && (objDatos.getToken() == null || "".equals(objDatos.getToken()) || "".equals(objDatos.getToken().trim()))) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(),
                        nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para crear jornadas
     */
    public OutputJornada crearJornada(InputJornada objDatos) {
        OutputJornada objRespuestaSol = new OutputJornada();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearJornada";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_POST);
        log.trace("2");
        if (objRespuesta == null) {
            log.trace("1");
            try {
                conn = getConnLocal();
                log.trace("Ingresa a operar para servicio web...");
                try {
                    TOKEN = getToken(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                            objDatos.getCodDispositivo(), 0);
                    log.trace("TOKEN:" + TOKEN);
                } catch (Exception e) {

                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
                            this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

                    log.error(e, e);
                }

                if (TOKEN.equals("LOGIN")) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaSol.setRespuesta(objRespuesta);
                } else if (TOKEN.contentEquals("ERROR")) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token",
                            Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    String mensaje = TOKEN.replace("ERROR", "");
                    objRespuesta.setExcepcion(mensaje);
                    objRespuestaSol.setRespuesta(objRespuesta);
                    TOKEN = "";
                } else if (objRespuesta != null) {
                    objRespuestaSol.setRespuesta(objRespuesta);
                } else {
                	
                    	// obteniendo url de servicio
                         url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREA_JORNADA);

                         log.trace("url:" + url);
                         if (url == null || url.equals("null") || "".equals(url)) {
                             objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                     metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                             objRespuestaSol.setRespuesta(objRespuesta);
                         } else {
                             JornadaCliente wsSol = new JornadaCliente();
                             wsSol.setServerUrl(url);
                             objRespuestaSol = wsSol.creaJornada(objDatos);
                         } 

                }
            } catch (SQLException e) {
                log.error(e, e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
            } catch (Exception e) {

                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
                log.error(e, e);
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
     * M\u00E9todo para modificar jornadas
     */
    public OutputJornada modJornada(InputJornada objDatos) {
        OutputJornada objRespuestaSol = new OutputJornada();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "modJornada";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_PUT);
        log.trace("2");
        if (objRespuesta == null) {
            log.trace("1");
            try {
                conn = getConnLocal();
                log.trace("Ingresa a operar para servicio web...");
                try {
                    TOKEN = getToken(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                            objDatos.getCodDispositivo(), 0);
                    log.trace("TOKEN:" + TOKEN);
                } catch (Exception e) {

                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
                            this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

                    log.error(e, e);
                }

                if (TOKEN.equals("LOGIN")) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaSol.setRespuesta(objRespuesta);
                } else if (TOKEN.contentEquals("ERROR")) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token",
                            Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    String mensaje = TOKEN.replace("ERROR", "");
                    objRespuesta.setExcepcion(mensaje);
                    objRespuestaSol.setRespuesta(objRespuesta);
                    TOKEN = "";
                } else if (objRespuesta != null) {
                    objRespuestaSol.setRespuesta(objRespuesta);
                } else {
                	if (isFullStack(objDatos.getCodArea()))
                    { 
                		log.trace("consumir metodo");
                		CtrlJornada recurso=new CtrlJornada();
                		objRespuestaSol=recurso.getDatos(objDatos);
                    }
                    else
                    {
                    	// obteniendo url de servicio
                        url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MOD_JORNADA);

                        log.trace("url:" + url);
                        if (url == null || url.equals("null") || "".equals(url)) {
                            objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                    metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                            objRespuestaSol.setRespuesta(objRespuesta);
                        } else {
                            JornadaCliente wsSol = new JornadaCliente();
                            wsSol.setServerUrl(url);
                            objRespuestaSol = wsSol.creaJornada(objDatos);
                        }
                    }
                    
                }
            } catch (SQLException e) {
                log.error(e, e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
            } catch (Exception e) {

                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
                log.error(e, e);
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
     * M\u00E9todo para obtener informaci\u00F3n Jornadas
     */
    public OutputJornada getJornada(InputJornada objDatos) {
        OutputJornada objRespuestaSol = new OutputJornada();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getJornada";
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
                try {
                    TOKEN = getToken(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                            objDatos.getCodDispositivo(), 0);
                    log.trace("TOKEN:" + TOKEN);
                } catch (Exception e) {

                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
                            this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

                    log.error(e, e);
                }

                if (TOKEN.equals("LOGIN")) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaSol.setRespuesta(objRespuesta);
                } else if (TOKEN.contentEquals("ERROR")) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token",
                            Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    String mensaje = TOKEN.replace("ERROR", "");
                    objRespuesta.setExcepcion(mensaje);
                    objRespuestaSol.setRespuesta(objRespuesta);
                    TOKEN = "";
                } else if (objRespuesta != null) {
                    objRespuestaSol.setRespuesta(objRespuesta);
                } else {                
                    	 // obteniendo url de servicio
                        url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_JORNADA);

                        log.trace("url:" + url);
                        if (url == null || url.equals("null") || "".equals(url)) {
                            objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                    metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                            objRespuestaSol.setRespuesta(objRespuesta);
                        } else {
                            JornadaCliente wsSol = new JornadaCliente();
                            wsSol.setServerUrl(url);
                            objRespuestaSol = wsSol.getJornada(objDatos);
                        }	                    
                }
            } catch (SQLException e) {
                log.error(e, e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
            } catch (Exception e) {

                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
                log.error(e, e);
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
     * M\u00E9todo para asignar fecha de cierre de jornada a un vendedor.
     */
    public OutputJornada asignaFechaJornada(InputJornada objDatos) {
        OutputJornada objRespuestaSol = new OutputJornada();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "asignaFechaJornada";
        String url = "";
        objRespuesta = validarDatos(objDatos, Conf.METODO_POST);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlJornada recurso=new CtrlJornada();
            		objRespuestaSol=recurso.doFechaJornada(objDatos, Conf.METODO_POST);
                }
                else
                {
                	// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_ASIGNA_FECHA_JOR);
                    log.trace("url:" + url);

                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaSol.setRespuesta(objRespuesta);
                    } else {
                        JornadaCliente wsSol = new JornadaCliente();
                        wsSol.setServerUrl(url);
                        objRespuestaSol = wsSol.asignaFechaCierre(objDatos);
                    }
                }
                

            } catch (SQLException e) {
                log.error(e, e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
            } catch (Exception e) {

                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
                log.error(e, e);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            objRespuestaSol.setRespuesta(objRespuesta);
        }

        return objRespuestaSol;
    }

    /**
     * M\u00E9todo para consultar la fecha de cierre de jornada de un vendedor.
     */
    public OutputJornada getFechaJornada(InputJornada objDatos) {
        OutputJornada objRespuestaSol = new OutputJornada();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getFechaJornada";
        String url = "";
        objRespuesta = validarDatos(objDatos, Conf.METODO_GET);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlJornada recurso=new CtrlJornada();
            		objRespuestaSol=recurso.doFechaJornada(objDatos, Conf.METODO_GET);
                }
                else
                {
                	 // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_FECHA_JOR);
                    log.trace("url:" + url);

                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaSol.setRespuesta(objRespuesta);
                    } else {
                        JornadaCliente wsSol = new JornadaCliente();
                        wsSol.setServerUrl(url);
                        objRespuestaSol = wsSol.getFechaCierre(objDatos);
                    }
                }
               

            } catch (SQLException e) {
                log.error(e, e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
            } catch (Exception e) {

                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
                log.error(e, e);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            objRespuestaSol.setRespuesta(objRespuesta);
        }

        return objRespuestaSol;
    }
}
