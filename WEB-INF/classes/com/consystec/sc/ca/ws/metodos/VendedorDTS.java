package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.VendedorDTSCliente;
import com.consystec.sc.ca.ws.input.vendedordts.InputVendedorDTS;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.vendedordts.OutputVendedorDTS;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlVendedorDTS;

public class VendedorDTS extends ControladorBase {

    private static final Logger log = Logger.getLogger(VendedorDTS.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputVendedorDTS objDatos) {
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
     * M\u00E9todo para obtener informaci\u00F3n de vendedores por dts
     */
    public OutputVendedorDTS getVendedorDTS(InputVendedorDTS objDatos) {
        OutputVendedorDTS objRespuestaVend = new OutputVendedorDTS();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getVendedorDTS";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlVendedorDTS recurso=new CtrlVendedorDTS();
            		objRespuestaVend=recurso.getDatos(objDatos, Conf.METODO_GET);
                }
                else
                {
                	 // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_VENDEDOR_DTS);

                    log.trace("url:" + url);
                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaVend.setRespuesta(objRespuesta);
                    } else {
                        VendedorDTSCliente wsCliente = new VendedorDTSCliente();
                        wsCliente.setServerUrl(url);
                        objRespuestaVend = wsCliente.getVendedorDTS(objDatos);
                    }
                }
               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaVend.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaVend.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaVend.setRespuesta(objRespuesta);
        }

        return objRespuestaVend;
    }

    /**
     * M\u00E9todo para asignar Vendedores por dts
     */
    public OutputVendedorDTS asignaVendedorDTS(InputVendedorDTS objDatos) {
        OutputVendedorDTS objRespuestaVendedorDTS = new OutputVendedorDTS();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearVendedorDTS";
        String url = "";
        log.trace("inicia a validar valores...");
        
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlVendedorDTS recurso=new CtrlVendedorDTS();
            		objRespuestaVendedorDTS=recurso.getDatos(objDatos, Conf.METODO_POST);
                }
                else
                {
                	// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_ASIGNA_VENDEDOR_DTS);
                    log.trace("url:" + url);
                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaVendedorDTS.setRespuesta(objRespuesta);
                    } else {
                        VendedorDTSCliente wsVendedorDTS = new VendedorDTSCliente();
                        wsVendedorDTS.setServerUrl(url);
                        objRespuestaVendedorDTS = wsVendedorDTS.asignaVendedorDTS(objDatos);
                    }	
                }
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaVendedorDTS.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaVendedorDTS.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaVendedorDTS.setRespuesta(objRespuesta);
        }

        return objRespuestaVendedorDTS;
    }

    /**
     * M\u00E9todo para dar de baja una VendedorDTS
     */
    public OutputVendedorDTS bajaVendedorDTS(InputVendedorDTS objDatos) {
        OutputVendedorDTS objRespuestaVendedorDTS = new OutputVendedorDTS();
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
            		log.trace("consumir metodo");
            		CtrlVendedorDTS recurso=new CtrlVendedorDTS();
            		objRespuestaVendedorDTS=recurso.getDatos(objDatos, Conf.METODO_DELETE);
                }
                else
                {
                	// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_BAJA_VENDEDOR_DTS);
                    log.trace("url:" + url);
                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaVendedorDTS.setRespuesta(objRespuesta);
                    } else {
                        VendedorDTSCliente wsVendedorDTS = new VendedorDTSCliente();
                        wsVendedorDTS.setServerUrl(url);
                        objRespuestaVendedorDTS = wsVendedorDTS.bajaVendedorDTS(objDatos);
                    }
                }
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaVendedorDTS.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaVendedorDTS.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaVendedorDTS.setRespuesta(objRespuesta);
        }

        return objRespuestaVendedorDTS;
    }

    /**
     * M\u00E9todo para asignar Vendedores por dts
     */
    public OutputVendedorDTS modVendedorDTS(InputVendedorDTS objDatos) {
        OutputVendedorDTS objRespuestaVendedorDTS = new OutputVendedorDTS();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "modVendedorDTS";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                // obteniendo url de servicio
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlVendedorDTS recurso=new CtrlVendedorDTS();
            		objRespuestaVendedorDTS=recurso.getDatos(objDatos, Conf.METODO_PUT);
                }
                else
                {
                	 url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MOD_VEND_DTS);
                     log.trace("url:" + url);
                     if (url == null || url.equals("null") || "".equals(url)) {
                         objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                 null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                         objRespuestaVendedorDTS.setRespuesta(objRespuesta);
                     } else {
                         VendedorDTSCliente wsVendedorDTS = new VendedorDTSCliente();
                         wsVendedorDTS.setServerUrl(url);
                         objRespuestaVendedorDTS = wsVendedorDTS.modificarVendedorDTS(objDatos);
                     }
                }
               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaVendedorDTS.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaVendedorDTS.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaVendedorDTS.setRespuesta(objRespuesta);
        }

        return objRespuestaVendedorDTS;
    }
}
