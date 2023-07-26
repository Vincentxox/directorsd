package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.DispositivoCliente;
import com.consystec.sc.ca.ws.input.dispositivo.InputDispositivo;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.dispositivo.OutputDispositivo;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlDispositivo;

public class Dispositivo extends ControladorBase {

    private static final Logger log = Logger.getLogger(Dispositivo.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputDispositivo objDatos) {
        Connection conn = null;
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (objDatos.getCodArea() == null || objDatos.getCodArea().equals("")) {
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

        if (objDatos.getUsuario() == null || objDatos.getUsuario().equals("")) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para crear dispositivos
     */
    public OutputDispositivo crearDispositivo(InputDispositivo objDatos) {
        OutputDispositivo objRespuestaDispositivo = new OutputDispositivo();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearDispositivo";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlDispositivo recurso=new CtrlDispositivo();
            		objRespuestaDispositivo=recurso.creaDispositivo(objDatos);
                }
                else
                {
                	 // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREADISPOSITIVO);

                    log.trace("url:" + url);
                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaDispositivo.setRespuesta(objRespuesta);
                    } else {
                        DispositivoCliente wsCliente = new DispositivoCliente();
                        wsCliente.setServerUrl(url);
                        objRespuestaDispositivo = wsCliente.crearDispositivo(objDatos);
                    }
                }
               

            } catch (SQLException e) {
                log.error(e,e);
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDispositivo.setRespuesta(objRespuesta);

            } catch (Exception e) {
                log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDispositivo.setRespuesta(objRespuesta);

            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaDispositivo.setRespuesta(objRespuesta);
        }

        return objRespuestaDispositivo;
    }

    /**
     * M\u00E9todo para modificar dispositivos
     */
    public OutputDispositivo modDispositivo(InputDispositivo objDatos) {
        OutputDispositivo objRespuestaDispositivo = new OutputDispositivo();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "modDispositivo";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlDispositivo recurso=new CtrlDispositivo();
            		objRespuestaDispositivo=recurso.modificaDispositivo(objDatos);
                }
                else
                {
                	 // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MOD_DISPOSITIVO);

                    log.trace("url:" + url);
                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaDispositivo.setRespuesta(objRespuesta);
                    } else {
                        DispositivoCliente wsCliente = new DispositivoCliente();
                        wsCliente.setServerUrl(url);
                        objRespuestaDispositivo = wsCliente.modificarDispositivo(objDatos);
                    }

                }
               
            } catch (SQLException e) {
                log.error(e,e);
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDispositivo.setRespuesta(objRespuesta);

            } catch (Exception e) {
                log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDispositivo.setRespuesta(objRespuesta);

            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaDispositivo.setRespuesta(objRespuesta);
        }

        return objRespuestaDispositivo;
    }

    /**
     * M\u00E9todo para obtener informaci\u00F3n de dispositivos
     */
    public OutputDispositivo getDispositivos(InputDispositivo objDatos) {
        OutputDispositivo objRespuestaDispositivo = new OutputDispositivo();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getDispositivos";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlDispositivo recurso=new CtrlDispositivo();
            		objRespuestaDispositivo=recurso.getDispositivo(objDatos);
                }
                else
                {   
                // obteniendo url de servicio
                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETDISPOSITIVO);

                log.trace("url:" + url);
                if (url == null || url.equals("null") || "".equals(url)) {
                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaDispositivo.setRespuesta(objRespuesta);
                } else {
                    DispositivoCliente wsCliente = new DispositivoCliente();
                    wsCliente.setServerUrl(url);
                    objRespuestaDispositivo = wsCliente.getDispositivo(objDatos);
                }
            }

            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDispositivo.setRespuesta(objRespuesta);

            } catch (Exception e) {
                log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDispositivo.setRespuesta(objRespuesta);

            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaDispositivo.setRespuesta(objRespuesta);
        }

        return objRespuestaDispositivo;
    }
}
