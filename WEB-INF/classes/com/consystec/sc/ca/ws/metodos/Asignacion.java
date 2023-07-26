package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.AsignacionCliente;
import com.consystec.sc.ca.ws.input.asignacion.InputAsignacion;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.asignacion.OutputAsignacion;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlAsignacion;
import com.google.gson.GsonBuilder;

public class Asignacion extends ControladorBase {

    private static final Logger log = Logger.getLogger(Asignacion.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     * 
     * @param metodoGet
     */
    public Respuesta validarDatos(InputAsignacion objDatos, int metodoGet) {
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
     * M\u00E9todo para crear asignaciones o reservas
     */
    public OutputAsignacion creaAsignacion(InputAsignacion objDatos) {
        OutputAsignacion objRespuestaAsignacion = new OutputAsignacion();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "creaAsignacion";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_POST);
        if (objRespuesta == null) {
            log.trace("valores validados");
            try {
                log.trace("obtiene conexion");
                conn = getConnLocal();
                if(isFullStack(objDatos.getCodArea()))
                {
                	log.trace("consumir metodo");
                	CtrlAsignacion recurso =new CtrlAsignacion();
                	objRespuestaAsignacion=recurso.getDatos(objDatos, Conf.METODO_POST);
                }
                else
                {
                	// obteniendo url de servicio
                    log.trace("obteniendo url");
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREA_ASIGNACION);

                    log.trace("url:" + url);
                    log.trace("json:" + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));

                    if (url == null || "null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaAsignacion.setRespuesta(objRespuesta);
                    } else {
                        log.trace("url valida");
                        AsignacionCliente wsAsignacion = new AsignacionCliente();
                        wsAsignacion.setServerUrl(url);
                        log.trace("consume servicio");
                        objRespuestaAsignacion = wsAsignacion.creaAsignacion(objDatos);
                    }
                }
                               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaAsignacion.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaAsignacion.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaAsignacion.setRespuesta(objRespuesta);
        }

        return objRespuestaAsignacion;
    }

    /**
     * M\u00E9todo para obtener informaci\u00F3n de asignaciones o reservas
     */
    public OutputAsignacion getAsignacion(InputAsignacion objDatos) {
        OutputAsignacion objRespuestaSol = new OutputAsignacion();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getAsignacion";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_GET);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                
                if (isFullStack(objDatos.getCodArea()))
                {
                	log.trace("consumir metodo");
                	CtrlAsignacion recurso= new CtrlAsignacion();
                	objRespuestaSol=recurso.getDatos(objDatos, Conf.METODO_GET);
                }
                else
                {                	
                    // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_ASIGNACION);

                    log.trace("url:" + url);
                    log.trace("json:" + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));

                    if (url == null || "null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaSol.setRespuesta(objRespuesta);
                    } else {
                        AsignacionCliente wsSol = new AsignacionCliente();
                        wsSol.setServerUrl(url);
                        objRespuestaSol = wsSol.getAsignacion(objDatos);
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

        return objRespuestaSol;
    }

    /**
     * M\u00E9todo para modificar asignaciones o reservas
     */
    public OutputAsignacion modAsignacion(InputAsignacion objDatos) {
        OutputAsignacion objRespuestaAsignacion = new OutputAsignacion();

        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "modAsignacion";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_PUT);
        if (objRespuesta == null) {

            objRespuesta = new Respuesta();

            try {
            	if (isFullStack(objDatos.getCodArea()))
            	{
            		log.trace("consumir metodo");
                	CtrlAsignacion recurso =new CtrlAsignacion();
                	objRespuestaAsignacion=recurso.getDatos(objDatos, Conf.METODO_PUT);
            	}
            	else
            	{
            		// obteniendo url de servicio
                    log.trace("obteniendo url");
                    conn = getConnLocal();
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MOD_ASIGNACION);

                    log.trace("url:" + url);
                    log.trace("json:" + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));

                    if (url == null || "null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaAsignacion.setRespuesta(objRespuesta);
                    } else {
                        AsignacionCliente wsPDV = new AsignacionCliente();
                        wsPDV.setServerUrl(url);
                        objRespuestaAsignacion = wsPDV.modAsignacion(objDatos);
                    }
            	}
            	
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaAsignacion.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaAsignacion.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaAsignacion.setRespuesta(objRespuesta);
        }

        return objRespuestaAsignacion;
    }

    /**
     * M\u00E9todo para modificar asignaciones o reservas
     */
    public OutputAsignacion modDetAsignacion(InputAsignacion objDatos) {
        OutputAsignacion objRespuestaAsignacion = new OutputAsignacion();

        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "modDetAsignacion";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_PUT);
        if (objRespuesta == null) {

            objRespuesta = new Respuesta();

            try {
            	
            	if(isFullStack(objDatos.getCodArea()))
            	{
            		log.trace("consumir metodo");
                	CtrlAsignacion recurso =new CtrlAsignacion();
                	objRespuestaAsignacion=recurso.modDetAsignacionReserva(objDatos);
            		
            	}
            	else
            	{
            		// obteniendo url de servicio
                    log.trace("obteniendo url");
                    conn = getConnLocal();
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MOD_DET_ASIGNACION);

                    log.trace("url:" + url);
                    log.trace("json:" + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));

                    if (url == null || "null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaAsignacion.setRespuesta(objRespuesta);
                    } else {
                        AsignacionCliente wsPDV = new AsignacionCliente();
                        wsPDV.setServerUrl(url);
                        objRespuestaAsignacion = wsPDV.modAsignacion(objDatos);
                    }
            	}
            	
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaAsignacion.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaAsignacion.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaAsignacion.setRespuesta(objRespuesta);
        }

        return objRespuestaAsignacion;
    }
}
