package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.BodegaVirtualCliente;
import com.consystec.sc.ca.ws.input.bodegadts.InputBodegaVirtual;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.bogegas.OutputBodegaVirtual;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlBodegaVirtual;

public class BodegaVirtual extends ControladorBase {

    private static final Logger log = Logger.getLogger(BodegaVirtual.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputBodegaVirtual objDatos) {
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
     * M\u00E9todo para crear bodegas virtuales
     */
    public OutputBodegaVirtual crearBodegaVirtual(InputBodegaVirtual objDatos) {
        OutputBodegaVirtual objRespuestaBodega = new OutputBodegaVirtual();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearBodegaVirtual";
        String url = "";
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if(isFullStack(objDatos.getCodArea()))
                {
                	log.trace("consumir metodo");
                	CtrlBodegaVirtual recurso= new CtrlBodegaVirtual();
                	objRespuestaBodega=recurso.getDatos(objDatos, Conf.METODO_POST);
                }
                else
                {
                	 // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREABODEGAVIRTUAL);
                    log.trace("url:" + url);
                    if (url == null || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaBodega.setRespuesta(objRespuesta);
                    } else {
                        BodegaVirtualCliente wsBodegaV = new BodegaVirtualCliente();
                        wsBodegaV.setServerUrl(url);
                        objRespuestaBodega = wsBodegaV.crearBodegaV(objDatos);
                    }
                }
               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaBodega.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaBodega.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaBodega.setRespuesta(objRespuesta);
            objRespuestaBodega.setRespuesta(objRespuesta);
        }

        return objRespuestaBodega;
    }

    /**
     * M\u00E9todo para modificar bodegas virtuales
     */
    public OutputBodegaVirtual modBodegaVirtual(InputBodegaVirtual objDatos) {
        OutputBodegaVirtual objRespuestaBodega = new OutputBodegaVirtual();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getBodegas";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                
                if(isFullStack(objDatos.getCodArea()))
                {
                	log.trace("consumir metodo");
                	CtrlBodegaVirtual recurso=new CtrlBodegaVirtual();
                	objRespuestaBodega=recurso.getDatos(objDatos, Conf.METODO_PUT);
                }
                else
                {
                	 // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MODBODEGAVIRTUAL);
                    log.trace("url:" + url);
                    if (url == null ||  "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaBodega.setRespuesta(objRespuesta);
                    } else {
                        BodegaVirtualCliente wsBodegaV = new BodegaVirtualCliente();
                        wsBodegaV.setServerUrl(url);
                        objRespuestaBodega = wsBodegaV.modificarBodegaV(objDatos);
                    }
                	
                }
               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaBodega.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	 log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaBodega.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaBodega.setRespuesta(objRespuesta);
        }

        return objRespuestaBodega;
    }

    /**
     * M\u00E9todo para dar de baja bodegas virtuales
     */
    public OutputBodegaVirtual bajaBodegaVirtual(InputBodegaVirtual objDatos) {
        OutputBodegaVirtual objRespuestaBodega = new OutputBodegaVirtual();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getBodegas";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if(isFullStack(objDatos.getCodArea()))
                {
                	log.trace("consumir metodo");
                	CtrlBodegaVirtual recurso=new CtrlBodegaVirtual();
                	objRespuestaBodega=recurso.getDatos(objDatos, Conf.METODO_DELETE);
                }
                else
                {
                	  // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_BAJABODEGAVIRTUAL);
                    log.trace("url:" + url);

                    if (url == null || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaBodega.setRespuesta(objRespuesta);
                    } else {
                        BodegaVirtualCliente wsBodegaV = new BodegaVirtualCliente();
                        wsBodegaV.setServerUrl(url);
                        objRespuestaBodega = wsBodegaV.bajaBodegaV(objDatos);
                    }
                }
              
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaBodega.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	 log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaBodega.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaBodega.setRespuesta(objRespuesta);
        }

        return objRespuestaBodega;
    }

    /**
     * M\u00E9todo para obtener bodegas virtuales
     */
    public OutputBodegaVirtual getBodegaVirtual(InputBodegaVirtual objDatos) {
        OutputBodegaVirtual objRespuestaBodega = new OutputBodegaVirtual();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getBodegas";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if(isFullStack(objDatos.getCodArea()))
                {
                	log.trace("consumir metodo");
                	CtrlBodegaVirtual recurso= new CtrlBodegaVirtual();
                	objRespuestaBodega=recurso.getDatos(objDatos, Conf.METODO_GET);
                }
                else
                {
                	 // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETBODEGAVIRTUAL);
                    log.trace("url:" + url);
                    if (url == null || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaBodega.setRespuesta(objRespuesta);
                    } else {
                        BodegaVirtualCliente wsBodegaV = new BodegaVirtualCliente();
                        wsBodegaV.setServerUrl(url);
                        objRespuestaBodega = wsBodegaV.getBodegaVirtual(objDatos);
                    }
                }
               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaBodega.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaBodega.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaBodega.setRespuesta(objRespuesta);
        }

        return objRespuestaBodega;
    }
}
