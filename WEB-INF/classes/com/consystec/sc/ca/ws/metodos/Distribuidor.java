package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.DTSCliente;
import com.consystec.sc.ca.ws.input.dts.InputDistribuidor;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.dts.OutputDistribuidor;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlDistribuidor;

public class Distribuidor extends ControladorBase {

    private static final Logger log = Logger.getLogger(Distribuidor.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputDistribuidor objDatos) {
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

        return objRespuesta;
    }

    /**
     * M\u00E9todo para crear distribuidores
     */
    public OutputDistribuidor crearDTS(InputDistribuidor objDatos) {
        OutputDistribuidor objRespuestaDTS = new OutputDistribuidor();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearCatalogo";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlDistribuidor recurso=new CtrlDistribuidor();
            		objRespuestaDTS=recurso.getDatos(objDatos , Conf.METODO_POST);
                }
                else
                {
                	 // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREARDTS);
                    log.trace("url:" + url);
                    if (url == null || "null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaDTS.setRespuesta(objRespuesta);
                    } else {
                        DTSCliente wsCtalogos = new DTSCliente();
                        wsCtalogos.setServerUrl(url);
                        objRespuestaDTS = wsCtalogos.crearDTS(objDatos);
                    }
                }
               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDTS.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDTS.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaDTS.setRespuesta(objRespuesta);
        }

        return objRespuestaDTS;
    }

    /**
     * M\u00E9todo para modifica distribuidores
     */
    public OutputDistribuidor modDTS(InputDistribuidor objDatos) {
        OutputDistribuidor objRespuestaDTS = new OutputDistribuidor();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearCatalogo";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlDistribuidor recurso=new CtrlDistribuidor();
            		objRespuestaDTS=recurso.getDatos(objDatos , Conf.METODO_PUT);
                }
                else
                {
                	// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MODDTS);
                    log.trace("url:" + url);
                    if (url == null || "null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaDTS.setRespuesta(objRespuesta);
                    } else {
                        DTSCliente wsCtalogos = new DTSCliente();
                        wsCtalogos.setServerUrl(url);
                        objRespuestaDTS = wsCtalogos.modificarDTS(objDatos);
                    }	
                }
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDTS.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDTS.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaDTS.setRespuesta(objRespuesta);
        }

        return objRespuestaDTS;
    }

    /**
     * M\u00E9todo para modifica distribuidores
     */
    public OutputDistribuidor getDTS(InputDistribuidor objDatos) {
        OutputDistribuidor objRespuestaDTS = new OutputDistribuidor();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getDTS";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            log.trace("valores validados");
            try {
                log.trace("obtiene conexion");
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlDistribuidor recurso=new CtrlDistribuidor();
            		objRespuestaDTS=recurso.getDatos(objDatos , Conf.METODO_GET);
                }
                else
                {
                	 // obteniendo url de servicio
                    log.trace("obteniendo url");
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETDTS);
                    log.trace("url:" + url);
                    if (url == null || "null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaDTS.setRespuesta(objRespuesta);
                    } else {
                        log.trace("url valida");
                        DTSCliente wsdts = new DTSCliente();
                        wsdts.setServerUrl(url);
                        log.trace("consume servicio");
                        objRespuestaDTS = wsdts.getDTS(objDatos);
                    }
                }
               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDTS.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDTS.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaDTS.setRespuesta(objRespuesta);
        }

        return objRespuestaDTS;
    }

    /**
     * M\u00E9todo para dar de baja distribuidores
     */
    public OutputDistribuidor bajaDTS(InputDistribuidor objDatos) {
        OutputDistribuidor objRespuestaDTS = new OutputDistribuidor();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "bajaDTS";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlDistribuidor recurso=new CtrlDistribuidor();
            		objRespuestaDTS=recurso.getDatos(objDatos , Conf.METODO_DELETE);
                }
                else
                {
                	// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_BAJADTS);
                    log.trace("url:" + url);
                    if (url == null || "null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaDTS.setRespuesta(objRespuesta);
                    } else {
                        DTSCliente wsCtalogos = new DTSCliente();
                        wsCtalogos.setServerUrl(url);
                        objRespuestaDTS = wsCtalogos.bajaDTS(objDatos);
                        log.trace("Advertencia:" + objRespuestaDTS.getRespuesta().getCodResultado());
                    }
                }
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDTS.setRespuesta(objRespuesta);
            } catch (Exception e) {
                log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDTS.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaDTS.setRespuesta(objRespuesta);
        }

        return objRespuestaDTS;
    }
}
