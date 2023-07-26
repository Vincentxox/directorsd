package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.BuzonCliente;
import com.consystec.sc.ca.ws.input.buzon.InputBuzon;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.buzon.OutputBuzon;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlBuzonSidra;

public class BuzonSidra extends ControladorBase {

    private static final Logger log = Logger.getLogger(BuzonSidra.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputBuzon objDatos) {
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

        if (objDatos.getUsuario() == null ||  "".equals(objDatos.getUsuario())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para crear buzones
     */
    public OutputBuzon crearBuzon(InputBuzon objDatos) {
        OutputBuzon objRespuestaBuzon = new OutputBuzon();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearPanel";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                
                if (isFullStack(objDatos.getCodArea()))
                 {
                	log.trace("consume metodo");
                	CtrlBuzonSidra recurso=new CtrlBuzonSidra();
                	objRespuestaBuzon=recurso.crearBuzon(objDatos);
                	log.trace("respuesta de servicio:"+ objRespuestaBuzon.getRespuesta().getDescripcion());
                 }
                else
                {
                	 // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREABUZON);

                    log.trace("url:" + url);
                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaBuzon.setRespuesta(objRespuesta);
                    } else {
                        BuzonCliente wsCliente = new BuzonCliente();
                        wsCliente.setServerUrl(url);
                        objRespuestaBuzon = wsCliente.crearBuzon(objDatos);
                    }
                }
               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaBuzon.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaBuzon.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaBuzon.setRespuesta(objRespuesta);
        }
        log.trace("prepara para return");

        return objRespuestaBuzon;
    }

    /**
     * M\u00E9todo para modificar buzones
     */
    public OutputBuzon modBuzon(InputBuzon objDatos) {
        OutputBuzon objRespuestaBuzon = new OutputBuzon();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearPanel";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                
                if (isFullStack(objDatos.getCodArea()))
                {
               	log.trace("consume metodo");
               	CtrlBuzonSidra recurso=new CtrlBuzonSidra();
               	objRespuestaBuzon=recurso.modBuzon(objDatos);
                }
               else
               {
            	   // obteniendo url de servicio
                   url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MODBUZON);

                   log.trace("url:" + url);
                   if (url == null || url.equals("null") || "".equals(url)) {
                       objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                               null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                       objRespuestaBuzon.setRespuesta(objRespuesta);
                   } else {
                       BuzonCliente wsCliente = new BuzonCliente();
                       wsCliente.setServerUrl(url);
                       objRespuestaBuzon = wsCliente.modificarBuzon(objDatos);
                   }  
               }
               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaBuzon.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaBuzon.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaBuzon.setRespuesta(objRespuesta);
        }

        return objRespuestaBuzon;
    }

    /**
     * M\u00E9todo para obtener informaci\u00F3n de buzones
     */
    public OutputBuzon getBuzon(InputBuzon objDatos) {
        OutputBuzon objRespuestaBuzon = new OutputBuzon();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearPanel";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                {
               	log.trace("consume metodo");
               	CtrlBuzonSidra recurso=new CtrlBuzonSidra();
               	objRespuestaBuzon=recurso.getBuzon(objDatos);
                }
               else
               {
            	   // obteniendo url de servicio
                   url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETBUZON);

                   log.trace("url:" + url);
                   if (url == null || url.equals("null") || "".equals(url)) {
                       objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                               null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                       objRespuestaBuzon.setRespuesta(objRespuesta);
                   } else {
                       BuzonCliente wsCliente = new BuzonCliente();
                       wsCliente.setServerUrl(url);
                       objRespuestaBuzon = wsCliente.getBuzon(objDatos);
                   }   
               }
               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaBuzon.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaBuzon.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaBuzon.setRespuesta(objRespuesta);
        }

        return objRespuestaBuzon;
    }
}
