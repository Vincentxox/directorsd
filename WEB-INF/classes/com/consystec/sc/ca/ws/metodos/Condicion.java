package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.CondicionCliente;
import com.consystec.sc.ca.ws.input.condicion.InputCondicionPrincipal;
import com.consystec.sc.ca.ws.input.condicion.InputDetCondicionCampania;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.condicion.OutputCondicion;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlCondicion;

public class Condicion extends ControladorBase {

    private static final Logger log = Logger.getLogger(Condicion.class);
    String TOKEN = "";

    /***
     * Validando que no vengan parametros nulos
     */
    public Respuesta validarDatos(InputCondicionPrincipal objDatos) {
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
     * M\u00E9todo para crear Condiciones
     */
    public OutputCondicion crearCondicion(InputCondicionPrincipal objDatos) {
        OutputCondicion objRespuestaCondicion = new OutputCondicion();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearCondicion";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlCondicion recurso=new CtrlCondicion();
            		objRespuestaCondicion=recurso.getDatos(objDatos , Conf.METODO_POST);
                }
                else
                {
                	// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREA_CONDICION_CAMPANIA);
                    log.trace("url:" + url);
                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaCondicion.setRespuesta(objRespuesta);
                    } else {
                        CondicionCliente wsCondicion = new CondicionCliente();
                        wsCondicion.setServerUrl(url);
                        objRespuestaCondicion = wsCondicion.crearCondicion(objDatos);
                    }
                }
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCondicion.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCondicion.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCondicion.setRespuesta(objRespuesta);
        }

        return objRespuestaCondicion;
    }

    /**
     * M\u00E9todo para modificar Condiciones
     */
    public OutputCondicion modCondicion(InputCondicionPrincipal objDatos) {
        OutputCondicion objRespuestaCondicion = new OutputCondicion();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "modCondicion";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlCondicion recurso=new CtrlCondicion();
            		objRespuestaCondicion=recurso.getDatos(objDatos , Conf.METODO_DELETE);
                }
                else
                {
                	 // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MOD_CONDICION_CAMPANIA);
                    log.trace("url:" + url);
                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaCondicion.setRespuesta(objRespuesta);
                    } else {
                        CondicionCliente wsCondicion = new CondicionCliente();
                        wsCondicion.setServerUrl(url);
                        objRespuestaCondicion = wsCondicion.modificarCondicion(objDatos);
                    }
                }
               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCondicion.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCondicion.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCondicion.setRespuesta(objRespuesta);
        }

        return objRespuestaCondicion;
    }

    /**
     * M\u00E9todo para consultar Condiciones
     */
    public OutputCondicion getCondicion(InputCondicionPrincipal objDatos) {
        OutputCondicion objRespuestaCondicion = new OutputCondicion();

        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getCondicion";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlCondicion recurso=new CtrlCondicion();
            		objRespuestaCondicion=recurso.getDatos(objDatos , Conf.METODO_GET);
                }
                else
                {
                	// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_CONDICION_CAMPANIA);
                    log.trace("url:" + url);
                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaCondicion.setRespuesta(objRespuesta);
                    } else {
                        CondicionCliente wsPDV = new CondicionCliente();
                        wsPDV.setServerUrl(url);
                        objRespuestaCondicion = wsPDV.getCondicion(objDatos);
                        
                        if(objDatos.getCodArea().equals("502")){
                        	if (objRespuestaCondicion.getCondiciones() != null) {
                                for (int i = 0; i < objRespuestaCondicion.getCondiciones().size(); i++) {
                                    if (objRespuestaCondicion.getCondiciones().get(i).getDetalle() == null || objRespuestaCondicion.getCondiciones().get(i).getDetalle().isEmpty()
                                    		|| objRespuestaCondicion.getCondiciones().get(i).getDetalle().get(0)==null) {
                                    	
                                        List<InputDetCondicionCampania> detalle = new ArrayList<InputDetCondicionCampania>();
                                        objRespuestaCondicion.getCondiciones().get(i).setDetalle(detalle);
                                    }
                                }
                            }
                        }else{
                        	if (objRespuestaCondicion.getCondiciones() != null) {
                                for (int i = 0; i < objRespuestaCondicion.getCondiciones().size(); i++) {
                                    if (objRespuestaCondicion.getCondiciones().get(i).getDetalle() == null) {
                                        List<InputDetCondicionCampania> detalle = new ArrayList<InputDetCondicionCampania>();
                                        objRespuestaCondicion.getCondiciones().get(i).setDetalle(detalle);
                                    }
                                }
                            }
                        }
                    }
                }
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCondicion.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCondicion.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
                objRespuestaCondicion.setToken(TOKEN);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCondicion.setRespuesta(objRespuesta);
        }

        return objRespuestaCondicion;
    }
}
