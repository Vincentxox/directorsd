package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.OfertaComercialCliente;
import com.consystec.sc.ca.ws.input.oferCom.InputConsultaArticulos;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.oferCom.OutputConsultaArticulos;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlArticulo;

public class OfertaComercial extends ControladorBase {

    private static final Logger log = Logger.getLogger(OfertaComercial.class);

    /***
     * Validando que no vengan parametros nulos
     */
    public Respuesta validarDatos(InputConsultaArticulos objDatos) {
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
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
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
     * Metodo para obtener art\u00EDculos del sistema comercial
     */
    public OutputConsultaArticulos getArticulos(InputConsultaArticulos objDatos) {
        OutputConsultaArticulos objRespuestaArticulo = new OutputConsultaArticulos();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getArticulos";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlArticulo recurso=new CtrlArticulo();
            		objRespuestaArticulo=recurso.getDatos(objDatos , Conf.METODO_GET);
                }
                else
                {
                	// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETARTICULO);
                    log.trace("url:" + url);
                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaArticulo.setRespuesta(objRespuesta);
                    } else {
                        OfertaComercialCliente wsOf = new OfertaComercialCliente();
                        wsOf.setServerUrl(url);
                        objRespuestaArticulo = wsOf.getArticulos(objDatos);
                    }	
                }
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaArticulo.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaArticulo.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaArticulo.setRespuesta(objRespuesta);
        }

        return objRespuestaArticulo;
    }

    /**
     * Metodo para obtener precio de un art\u00EDculo del sistema comercial
     */
    public OutputConsultaArticulos getPrecioArt(InputConsultaArticulos objDatos) {
        OutputConsultaArticulos objRespuestaArticulo = new OutputConsultaArticulos();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getPrecioArt";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlArticulo recurso=new CtrlArticulo();
            		objRespuestaArticulo=recurso.getPrecioArticulo(objDatos, Conf.METODO_GET);
                }
                else
                {
                	 // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETPRECIOART);
                    log.trace("url:" + url);
                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaArticulo.setRespuesta(objRespuesta);
                    } else {
                        OfertaComercialCliente wsOf = new OfertaComercialCliente();
                        wsOf.setServerUrl(url);
                        objRespuestaArticulo = wsOf.getPrecioArticulo(objDatos);
                    }
                }
               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaArticulo.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaArticulo.setRespuesta(objRespuesta);
        }

        return objRespuestaArticulo;
    }
}
