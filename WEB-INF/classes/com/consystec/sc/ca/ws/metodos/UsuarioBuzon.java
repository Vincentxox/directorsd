package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.UsuarioBuzonCliente;
import com.consystec.sc.ca.ws.input.buzon.InputUsuarioBuzon;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.buzon.OutputUsuarioBuzon;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlUsuarioBuzon;

public class UsuarioBuzon extends ControladorBase {

    private static final Logger log = Logger.getLogger(UsuarioBuzon.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputUsuarioBuzon objDatos) {
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
     * M\u00E9todo para asignar buzones
     */
    public OutputUsuarioBuzon crearUsuarioBuzon(InputUsuarioBuzon objDatos) {
        OutputUsuarioBuzon output = new OutputUsuarioBuzon();
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
            		CtrlUsuarioBuzon recurso=new CtrlUsuarioBuzon();
            		output=recurso.getDatos(objDatos, Conf.METODO_POST);
                }
                else
                {
                	  // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREABUZONUSER);
                    log.trace("url:" + url);
                    if (url == null ||"null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        output.setRespuesta(objRespuesta);
                    } else {
                        UsuarioBuzonCliente wsCliente = new UsuarioBuzonCliente();
                        wsCliente.setServerUrl(url);
                        output = wsCliente.crearUsuarioBuzon(objDatos);
                    }
                }
              
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                output.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                output.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            output.setRespuesta(objRespuesta);
        }

        return output;
    }

    /**
     * M\u00E9todo para modifica BuzonesUsuarioses
     */
    public OutputUsuarioBuzon modUsuarioBuzon(InputUsuarioBuzon objDatos) {
        OutputUsuarioBuzon output = new OutputUsuarioBuzon();
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
            		CtrlUsuarioBuzon recurso=new CtrlUsuarioBuzon();
            		output=recurso.getDatos(objDatos, Conf.METODO_PUT);
                }
                else
                {
                	// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MODBUZONUSER);
                    log.trace("url:" + url);
                    if (url == null || "null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        output.setRespuesta(objRespuesta);
                    } else {
                        UsuarioBuzonCliente wsCliente = new UsuarioBuzonCliente();
                        wsCliente.setServerUrl(url);
                        output = wsCliente.modificarUsuarioBuzon(objDatos);
                    }
                }
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                output.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                output.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            output.setRespuesta(objRespuesta);
        }

        return output;
    }

    /**
     * M\u00E9todo para modifica distribuidores
     */
    public OutputUsuarioBuzon getUsuarioBuzon(InputUsuarioBuzon objDatos) {
        OutputUsuarioBuzon output = new OutputUsuarioBuzon();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getUsuarioBuzon";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            log.trace("valores validados");
            try {
                log.trace("obtiene conexion");
                conn = getConnLocal();
                // obteniendo url de servicio
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlUsuarioBuzon recurso=new CtrlUsuarioBuzon();
            		output=recurso.getDatos(objDatos, Conf.METODO_GET);
                }
                else
                {
                	log.trace("obteniendo url");
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETBUZONUSER);

                    log.trace("url:" + url);
                    if (url == null || "null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        output.setRespuesta(objRespuesta);
                    } else {
                        log.trace("url valida");
                        UsuarioBuzonCliente wsCliente = new UsuarioBuzonCliente();
                        wsCliente.setServerUrl(url);
                        log.trace("consume servicio");
                        output = wsCliente.getUsuarioBuzon(objDatos);
                    }
                }
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                output.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                output.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            output.setRespuesta(objRespuesta);
        }

        return output;
    }

    /**
     * M\u00E9todo para dar de baja BuzonesUsuarioses
     */
    public OutputUsuarioBuzon bajaUsuarioBuzon(InputUsuarioBuzon objDatos) {
        OutputUsuarioBuzon output = new OutputUsuarioBuzon();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "bajaUsuarioBuzon";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlUsuarioBuzon recurso=new CtrlUsuarioBuzon();
            		output=recurso.getDatos(objDatos, Conf.METODO_DELETE);
                }
                else
                {
                	 // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_DELBUZONUSER);
                    log.trace("url:" + url);
                    if (url == null || "null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        output.setRespuesta(objRespuesta);
                    } else {
                        UsuarioBuzonCliente wsCliente = new UsuarioBuzonCliente();
                        wsCliente.setServerUrl(url);
                        output = wsCliente.bajaUsuarioBuzon(objDatos);
                        log.trace("Advertencia:" + output.getRespuesta().getCodResultado());
                    }
                }
               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                output.setRespuesta(objRespuesta);
            } catch (Exception e) {
                log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                output.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            output.setRespuesta(objRespuesta);
        }

        return output;
    }
}
