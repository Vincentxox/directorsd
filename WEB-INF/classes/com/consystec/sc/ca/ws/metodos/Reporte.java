package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.ReporteCliente;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCantInvJornada;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCtaDTS;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCumplimientoVenta;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCumplimientoVisita;
import com.consystec.sc.ca.ws.input.reporte.InputReporteEfectividadVenta;
import com.consystec.sc.ca.ws.input.reporte.InputReporteInventarioVendido;
import com.consystec.sc.ca.ws.input.reporte.InputReportePDV;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCantInvJornada;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCtaDTS;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCumplimientoVenta;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCumplimientoVisita;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteEfectividadVenta;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteInventarioVendido;
import com.consystec.sc.ca.ws.output.reporte.OutputReportePDV;
import com.consystec.sc.ca.ws.output.reporte.OutputResumenInventarioVendido;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlReporte;
import com.consystec.sc.sv.ws.metodos.CtrlReporteCantInvJornada;
import com.consystec.sc.sv.ws.metodos.CtrlReportePDV;
import com.google.gson.GsonBuilder;

public class Reporte extends ControladorBase {
    private static final Logger log = Logger.getLogger(Reporte.class);
    String TOKEN = "";

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarAreaUsuario(String codArea, String usuario) {
        Connection conn = null;
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (codArea == null || "".equals(codArea)) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODAREA_NULO_1, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);

        } else if (codArea.length() != Conf.LONG_CODPAIS) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONGINVALIDA_AREA_6, null, this.getClass().toString(), metodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        } else {
            BigDecimal idPais = null;
            try {
                conn = getConnLocal();
                idPais = getidpais(conn, codArea);
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

        if (usuario == null || "".equals(usuario)) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        return objRespuesta;
    }

    public Respuesta validarDatos(InputReporteCantInvJornada objDatos, int metodo) {
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
            if (!"WEB".equalsIgnoreCase(objDatos.getToken()) && (objDatos.getCodDispositivo() == null || "".equals(objDatos.getCodDispositivo().trim()))) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(),
                            nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            }
        }

        return objRespuesta;
    }
    
    /**
     * M\u00E9todo que obtiene el listado con toda la informaci\u00F3n de cumplimiento
     * visitas
     */
    public OutputReporteCumplimientoVisita getReporteCumplimientoVisita(InputReporteCumplimientoVisita objDatos) {
        OutputReporteCumplimientoVisita objRespuestaCumpVisita = new OutputReporteCumplimientoVisita();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getReporteCumplimientoVisita";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarAreaUsuario(objDatos.getCodArea(), objDatos.getUsuario());

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea())){
                	CtrlReporte recurso = new CtrlReporte();
                	objRespuestaCumpVisita=recurso.getReporteCumplimientoVisita(objDatos);                   			
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETREPORTECUMPLIMIENTOVISITA);
	
	                log.trace("url:" + url);
	                if (url == null || url.equals("null") || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaCumpVisita.setRespuesta(objRespuesta);
	                } else {
	                    ReporteCliente wsCliente = new ReporteCliente();
	                    wsCliente.setServerUrl(url);
	                    objRespuestaCumpVisita = wsCliente.getReporteCumplimientoVisita(objDatos);
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCumpVisita.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCumpVisita.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCumpVisita.setRespuesta(objRespuesta);
        }

        return objRespuestaCumpVisita;
    }

    /**
     * M\u00E9todo que obtiene el listado con toda la informaci\u00F3n de cumplimiento
     * ventas
     */
    public OutputReporteCumplimientoVenta getReporteCumplimientoVenta(InputReporteCumplimientoVenta objDatos) {
        OutputReporteCumplimientoVenta objRespuestaCumpVenta = new OutputReporteCumplimientoVenta();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getReporteCumplimientoVenta";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarAreaUsuario(objDatos.getCodArea(), objDatos.getUsuario());

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea())){
                	CtrlReporte recurso = new CtrlReporte();
                	objRespuestaCumpVenta=recurso.getReporteCumplimientoVenta(objDatos);                 			
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETREPORTECUMPLIMIENTOVENTA);
	
	                log.trace("url:" + url);
	                if (url == null || url.equals("null") || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaCumpVenta.setRespuesta(objRespuesta);
	                } else {
	                    ReporteCliente wsCliente = new ReporteCliente();
	                    wsCliente.setServerUrl(url);
	                    objRespuestaCumpVenta = wsCliente.getReporteCumplimientoVenta(objDatos);
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCumpVenta.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCumpVenta.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCumpVenta.setRespuesta(objRespuesta);
        }

        return objRespuestaCumpVenta;
    }

    /**
     * M\u00E9todo que obtiene el n\u00FAmero total de registros de la informaci\u00F3n de
     * cumplimiento ventas
     */
    public OutputReporteCumplimientoVenta getCountCumplimientoVenta(InputReporteCumplimientoVenta objDatos) {
        OutputReporteCumplimientoVenta objRespuestaCumpVenta = new OutputReporteCumplimientoVenta();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getCountCumplimientoVenta";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarAreaUsuario(objDatos.getCodArea(), objDatos.getUsuario());

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea())){
                	CtrlReporte recurso = new CtrlReporte();
                	objRespuestaCumpVenta=recurso.getCountCumplimientoVenta(objDatos);                 			
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETCOUNTCUMPLIMIENTOVENTA);
	
	                log.trace("url:" + url);
	                if (url == null || url.equals("null") || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaCumpVenta.setRespuesta(objRespuesta);
	                } else {
	                    ReporteCliente wsCliente = new ReporteCliente();
	                    wsCliente.setServerUrl(url);
	                    objRespuestaCumpVenta = wsCliente.getCountCumplimientoVenta(objDatos);
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCumpVenta.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCumpVenta.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCumpVenta.setRespuesta(objRespuesta);
        }

        return objRespuestaCumpVenta;
    }

    /**
     * M\u00E9todo que obtiene el listado con toda la informaci\u00F3n de ventas
     * realizadas agrupadas por tipo de producto
     */
    public OutputReporteInventarioVendido getReporteInventarioVendido(InputReporteInventarioVendido objDatos) {
        OutputReporteInventarioVendido objRespuestaInvVendido = new OutputReporteInventarioVendido();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getReporteInventarioVendido";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarAreaUsuario(objDatos.getCodArea(), objDatos.getUsuario());

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                
                if (isFullStack(objDatos.getCodArea())){
                	CtrlReporte recurso = new CtrlReporte();
                	objRespuestaInvVendido=recurso.getReporteInventarioVendido(objDatos);            			
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETREPORTEINVENTARIOVENDIDO);
	
	                log.trace("url:" + url);
	                if (url == null || url.equals("null") || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaInvVendido.setRespuesta(objRespuesta);
	                } else {
	                    ReporteCliente wsCliente = new ReporteCliente();
	                    wsCliente.setServerUrl(url);
	                    objRespuestaInvVendido = wsCliente.getReporteInventarioVendido(objDatos);
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaInvVendido.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaInvVendido.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaInvVendido.setRespuesta(objRespuesta);
        }

        return objRespuestaInvVendido;
    }

    /**
     * M\u00E9todo para obtener el listado resumen de registros de articulos
     * vendidos.
     * 
     * @param objDatos
     * @return OutputResumenInventarioVendido
     */
    public OutputResumenInventarioVendido getResumenInventarioVendido(InputReporteInventarioVendido objDatos) {
        OutputResumenInventarioVendido objRespuestaResVendido = new OutputResumenInventarioVendido();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getResumenInventarioVendido";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarAreaUsuario(objDatos.getCodArea(), objDatos.getUsuario());

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea())){
                	CtrlReporte recurso = new CtrlReporte();
                	objRespuestaResVendido=recurso.getResumenInventarioVendido(objDatos);            			
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETRESUMENINVENTARIOVENDIDO);
	
	                log.trace("url:" + url);
	                if (url == null || url.equals("null") || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaResVendido.setRespuesta(objRespuesta);
	                } else {
	                    ReporteCliente wsCliente = new ReporteCliente();
	                    wsCliente.setServerUrl(url);
	                    objRespuestaResVendido = wsCliente.getResumenInventarioVendido(objDatos);
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaResVendido.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaResVendido.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaResVendido.setRespuesta(objRespuesta);
        }

        return objRespuestaResVendido;

    }

    /**
     * M\u00E9todo para obtener el listado resumen de registros de articulos
     * vendidos.
     * 
     * @param objDatos
     * @return OutputResumenInventarioVendido
     */
    public OutputReporteEfectividadVenta getReporteEfectividadVenta(InputReporteEfectividadVenta objDatos) {
        OutputReporteEfectividadVenta objRespuestaEfectividad = new OutputReporteEfectividadVenta();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getReporteEfectividadVenta";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarAreaUsuario(objDatos.getCodArea(), objDatos.getUsuario());

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea())){
                	CtrlReporte recurso = new CtrlReporte();
                	objRespuestaEfectividad=recurso.getReporteEfectividadVenta(objDatos);           			
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETREPORTEEFECTIVIDADVENTA);
	
	                log.trace("url:" + url);
	                if (url == null || url.equals("null") || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaEfectividad.setRespuesta(objRespuesta);
	                } else {
	                    ReporteCliente wsCliente = new ReporteCliente();
	                    wsCliente.setServerUrl(url);
	                    objRespuestaEfectividad = wsCliente.getReporteEfectividadVenta(objDatos);
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaEfectividad.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaEfectividad.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaEfectividad.setRespuesta(objRespuesta);
        }

        return objRespuestaEfectividad;

    }

    /**
     * M\u00E9todo que obtiene el listado de puntos de venta con detalle de ventas
     */
    public OutputReportePDV getReportePDV(InputReportePDV objDatos) {
        OutputReportePDV objRespuestaWS = new OutputReportePDV();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getReportePDV";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarAreaUsuario(objDatos.getCodArea(), objDatos.getUsuario());

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea())){
                	CtrlReportePDV recurso = new CtrlReportePDV();
                	objRespuestaWS=recurso.getDatos(objDatos, Conf.METODO_GET);        			
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_REPORTEPDV);
	
	                log.trace("url:" + url);
	                if (url == null || url.equals("null") || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaWS.setRespuesta(objRespuesta);
	                } else {
	                    ReporteCliente wsCliente = new ReporteCliente();
	                    wsCliente.setServerUrl(url);
	                    objRespuestaWS = wsCliente.getReportePDV(objDatos);
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
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaWS.setRespuesta(objRespuesta);
        }

        return objRespuestaWS;
    }

    /**
     * M\u00E9todo que obtiene el reporte de inventario de jornada.
     */
    public OutputReporteCantInvJornada getReporteInvJornada(InputReporteCantInvJornada objDatos) {
        OutputReporteCantInvJornada objRespuestaWS = new OutputReporteCantInvJornada();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getReporteInvJornada";
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
                //*/
                	if(isFullStack(objDatos.getCodArea())){
                		 CtrlReporteCantInvJornada recurso = new CtrlReporteCantInvJornada();
                		 objRespuestaWS= recurso.getDatos(objDatos);
                	}else{
	                    // obteniendo url de servicio
	                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_REPORTE_INVJORNADA);
	
	                    log.trace("url: " + url);
	                    log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));
	
	                    if (url == null || url.equals("null") || "".equals(url)) {
	                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
	                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                        objRespuestaWS.setRespuesta(objRespuesta);
	                    } else {
	                        ReporteCliente wsReporte = new ReporteCliente();
	                        wsReporte.setServerUrl(url);
	                        objRespuestaWS = wsReporte.getReporteInvJornada(objDatos);
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
     * M\u00E9todo que obtiene el debe del reporte de cuenta dts
     */
    public OutputReporteCtaDTS getCtaDtsDebe(InputReporteCtaDTS objDatos) {
        OutputReporteCtaDTS objRespuestaWS = new OutputReporteCtaDTS();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getCtaDtsDebe";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarAreaUsuario(objDatos.getCodArea(), objDatos.getUsuario());

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
    
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_REPORTE_DEBE);
	//                url="http://localhost:8181/WS_SIDRA_PA/sidra/consultasidra/getReporteDtsDebe";//TODO cambiar url quemada
	
	                log.trace("url: " + url);
	                if (url == null || url.equals("null") || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaWS.setRespuesta(objRespuesta);
	                } else {
	                    ReporteCliente wsCliente = new ReporteCliente();
	                    wsCliente.setServerUrl(url);
	                    objRespuestaWS = wsCliente.getReporteCtaDts(objDatos);
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
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaWS.setRespuesta(objRespuesta);
        }

        return objRespuestaWS;
    }

    /**
     * M\u00E9todo que obtiene el haber del reporte de cuenta dts
     */
    public OutputReporteCtaDTS getCtaDtsHaber(InputReporteCtaDTS objDatos) {
        OutputReporteCtaDTS objRespuestaWS = new OutputReporteCtaDTS();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getCtaDtsHaber";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarAreaUsuario(objDatos.getCodArea(), objDatos.getUsuario());

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                // obteniendo url de servicio
                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_REPORTE_HABER);
//                url="http://localhost:8181/WS_SIDRA_PA/sidra/consultasidra/getReporteDtsHaber";//TODO cambiar url quemada

                log.trace("url: " + url);
                if (url == null || url.equals("null") || "".equals(url)) {
                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaWS.setRespuesta(objRespuesta);
                } else {
                    ReporteCliente wsCliente = new ReporteCliente();
                    wsCliente.setServerUrl(url);
                    objRespuestaWS = wsCliente.getReporteCtaDts(objDatos);
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
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaWS.setRespuesta(objRespuesta);
        }

        return objRespuestaWS;
    }

    /**
     * M\u00E9todo que obtiene el resumen del reporte de cuenta dts
     */
    public OutputReporteCtaDTS getCtaDtsResumen(InputReporteCtaDTS objDatos) {
        OutputReporteCtaDTS objRespuestaWS = new OutputReporteCtaDTS();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getCtaDtsResumen";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarAreaUsuario(objDatos.getCodArea(), objDatos.getUsuario());

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                // obteniendo url de servicio
                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_REPORTE_RESUMEN);
//                url="http://localhost:8181/WS_SIDRA_PA/sidra/consultasidra/getReporteDtsResumen";//TODO cambiar url quemada

                log.trace("url: " + url);
                if (url == null || url.equals("null") || "".equals(url)) {
                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaWS.setRespuesta(objRespuesta);
                } else {
                    ReporteCliente wsCliente = new ReporteCliente();
                    wsCliente.setServerUrl(url);
                    objRespuestaWS = wsCliente.getReporteCtaDts(objDatos);
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
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaWS.setRespuesta(objRespuesta);
        }

        return objRespuestaWS;
    }
    
    /**
     * M\u00E9todo que obtiene el resumen del reporte de cuenta dts
     */
    public OutputReporteCtaDTS postCtaDtsHaber(InputReporteCtaDTS objDatos) {
        OutputReporteCtaDTS objRespuestaWS = new OutputReporteCtaDTS();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "postCtaDtsHaber";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarAreaUsuario(objDatos.getCodArea(), objDatos.getUsuario());

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                // obteniendo url de servicio
                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_POST_REPORTE_RESUMEN);
                //url="http://localhost:8181/WS_SIDRA_PA/sidra/rest/agregaHaberDts";//TODO cambiar url quemada

                log.trace("url: " + url);
                if (url == null || url.equals("null") || "".equals(url)) {
                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaWS.setRespuesta(objRespuesta);
                } else {
                    ReporteCliente wsCliente = new ReporteCliente();
                    wsCliente.setServerUrl(url);
                    objRespuestaWS = wsCliente.getReporteCtaDts(objDatos);
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
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaWS.setRespuesta(objRespuesta);
        }

        return objRespuestaWS;
    }
}
