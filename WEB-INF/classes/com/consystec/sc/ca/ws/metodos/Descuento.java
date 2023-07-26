package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.DescuentoCliente;
import com.consystec.sc.ca.ws.input.descuento.InputDescuento;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.descuento.OutputDescuento;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;

public class Descuento extends ControladorBase {

    private static final Logger log = Logger.getLogger(Descuento.class);

    /***
     * Validando que no vengan parametros nulos
     */
    public Respuesta validarDatos(InputDescuento objDatos) {
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
     * M\u00E9todo para crear descuentos
     */
    public OutputDescuento crearDescuento(InputDescuento objDatos) {
        OutputDescuento objRespuestaDescuento = new OutputDescuento();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearDescuento";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                // obteniendo url de servicio
                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREADESCUENTO);

                log.trace("url:" + url);
                if (url == null || url.equals("null") || "".equals(url)) {
                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaDescuento.setRespuesta(objRespuesta);
                } else {
                    DescuentoCliente wsDescuento = new DescuentoCliente();
                    wsDescuento.setServerUrl(url);
                    objRespuestaDescuento = wsDescuento.crearDescuento(objDatos);
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDescuento.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDescuento.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaDescuento.setRespuesta(objRespuesta);
        }

        return objRespuestaDescuento;
    }

    /**
     * M\u00E9todo para modificar descuentos
     */
    public OutputDescuento modificaDescuento(InputDescuento objDatos) {
        OutputDescuento objRespuestaDescuento = new OutputDescuento();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearDescuento";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                // obteniendo url de servicio
                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MODDESCUENTO);
                log.trace("url:" + url);
                if (url == null || url.equals("null") || "".equals(url)) {
                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaDescuento.setRespuesta(objRespuesta);
                } else {
                    DescuentoCliente wsDescuento = new DescuentoCliente();
                    wsDescuento.setServerUrl(url);
                    objRespuestaDescuento = wsDescuento.modificarDescuento(objDatos);
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDescuento.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDescuento.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaDescuento.setRespuesta(objRespuesta);
        }

        return objRespuestaDescuento;
    }

    /**
     * M\u00E9todo para dar de baja descuentos
     */
    public OutputDescuento bajaDescuento(InputDescuento objDatos) {
        OutputDescuento objRespuestaDescuento = new OutputDescuento();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearDescuento";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                // obteniendo url de servicio
                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_BAJADESCUENTO);
                log.trace("url:" + url);
                if (url == null || url.equals("null") || "".equals(url)) {
                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaDescuento.setRespuesta(objRespuesta);
                } else {
                    DescuentoCliente wsDescuento = new DescuentoCliente();
                    wsDescuento.setServerUrl(url);
                    objRespuestaDescuento = wsDescuento.bajaDescuento(objDatos);
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDescuento.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDescuento.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaDescuento.setRespuesta(objRespuesta);
        }

        return objRespuestaDescuento;
    }

    /**
     * M\u00E9todo para obtener descuentos
     */
    public OutputDescuento getDescuento(InputDescuento objDatos) {
        OutputDescuento objRespuestaDescuento = new OutputDescuento();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearDescuento";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                // obteniendo url de servicio
                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETDESCUENTO);
                log.trace("url:" + url);
                if (url == null || url.equals("null") || "".equals(url)) {
                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaDescuento.setRespuesta(objRespuesta);
                } else {
                    DescuentoCliente wsDescuento = new DescuentoCliente();
                    wsDescuento.setServerUrl(url);
                    objRespuestaDescuento = wsDescuento.getDescuento(objDatos);
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDescuento.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaDescuento.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaDescuento.setRespuesta(objRespuesta);
        }

        return objRespuestaDescuento;
    }
}
