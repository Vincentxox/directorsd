package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.ClienteCliente;
import com.consystec.sc.ca.ws.input.cliente.InputCliente;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.cliente.OutputCliente;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlCliente;

public class Cliente extends ControladorBase {

    private static final Logger log = Logger.getLogger(Cliente.class);
    String TOKEN = null;

    /***
     * Validando que no vengan parametros nulos
     */
    public Respuesta validarDatos(InputCliente objDatos) {
        Connection conn = null;
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (objDatos.getCodArea().equals(null) || objDatos.getCodArea().equals("")) {
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

        if (objDatos.getUsuario().equals(null) || objDatos.getUsuario().equals("")) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        if (objDatos.getToken().equals("") || "".equals((objDatos.getToken()))) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        return objRespuesta;
    }

    /**
     * Metodo para crear cliente
     */
    public OutputCliente crearCliente(InputCliente objDatos) {
        OutputCliente objRespuestaCliente = new OutputCliente();

        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearCliente";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();

        

                if (TOKEN.equals("LOGIN")) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaCliente.setRespuesta(objRespuesta);
                }else if (TOKEN.contentEquals("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaCliente.setRespuesta(objRespuesta);
                	TOKEN="";
                } else {
                if (isFullStack(objDatos.getCodArea()))
                    { 
                		log.trace("consumir metodo");
                		CtrlCliente recurso= new CtrlCliente ();
                		objRespuestaCliente=recurso.getDatos(objDatos, Conf.METODO_POST);
                   	log.trace("consume metodo");
                    }
                	else{
                		// obteniendo url de servicio
                        url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREACLIENTE);
                        log.trace("url:" + url);
                        if (url == null || url.equals("null") || "".equals(url)) {
                            objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                    metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                            objRespuestaCliente.setRespuesta(objRespuesta);
                        } else {
                            ClienteCliente wsPDV = new ClienteCliente();
                            wsPDV.setServerUrl(url);
                            objRespuestaCliente = wsPDV.crearCliente(objDatos);
                        }	
                	}
                    
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCliente.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCliente.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
                objRespuestaCliente.setToken(TOKEN);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCliente.setRespuesta(objRespuesta);
        }

        return objRespuestaCliente;
    }

    /**
     * Metodo para modificar cliente
     */
    public OutputCliente modCliente(InputCliente objDatos) {
        OutputCliente objRespuestaCliente = new OutputCliente();

        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "modCliente";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();

           

                if (TOKEN.equals("LOGIN")) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaCliente.setRespuesta(objRespuesta);
                } else if (TOKEN.contentEquals("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaCliente.setRespuesta(objRespuesta);
                	TOKEN="";
                } else {
                	if (isFullStack(objDatos.getCodArea()))
                    { 
                		log.trace("consumir metodo");
                		CtrlCliente recurso= new CtrlCliente ();
                		objRespuestaCliente=recurso.getDatos(objDatos, Conf.METODO_PUT);
                   	log.trace("consume metodo");
                    }
                	else{
                		 // obteniendo url de servicio
                        url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MODCLIENTE);
                        log.trace("url:" + url);
                        if (url == null || url.equals("null") || "".equals(url)) {
                            objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                    metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                            objRespuestaCliente.setRespuesta(objRespuesta);
                        } else {
                            ClienteCliente wsPDV = new ClienteCliente();
                            wsPDV.setServerUrl(url);
                            objRespuestaCliente = wsPDV.modificaCliente(objDatos);
                        }
                	}
                   
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCliente.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCliente.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
                objRespuestaCliente.setToken(TOKEN);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCliente.setRespuesta(objRespuesta);
        }

        return objRespuestaCliente;
    }

    /**
     * Metodo para dar baja cliente
     */
    public OutputCliente bajaCliente(InputCliente objDatos) {
        OutputCliente objRespuestaCliente = new OutputCliente();

        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "bajaCliente";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {

            try {
                conn = getConnLocal();

              

                if (TOKEN.equals("LOGIN")) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaCliente.setRespuesta(objRespuesta);
                } else if (TOKEN.contentEquals("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaCliente.setRespuesta(objRespuesta);
                	TOKEN="";
                } else {
                	if (isFullStack(objDatos.getCodArea()))
                    { 
                		log.trace("consumir metodo");
                		CtrlCliente recurso= new CtrlCliente ();
                		objRespuestaCliente=recurso.getDatos(objDatos, Conf.METODO_DELETE);
                   	log.trace("consume metodo");
                    }
                	else{
                		 // obteniendo url de servicio
                        url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_DELCLIENTE);
                        log.trace("url:" + url);
                        if (url == null || url.equals("null") || "".equals(url)) {
                            objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                    metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                            objRespuestaCliente.setRespuesta(objRespuesta);
                        } else {
                            ClienteCliente wsPDV = new ClienteCliente();
                            wsPDV.setServerUrl(url);
                            objRespuestaCliente = wsPDV.bajaCliente(objDatos);
                        }
                	}
                   
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCliente.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCliente.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
                objRespuestaCliente.setToken(TOKEN);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCliente.setRespuesta(objRespuesta);
        }

        return objRespuestaCliente;
    }

    /**
     * Metodo para dar consultar clientes
     */
    public OutputCliente getCliente(InputCliente objDatos) {
        OutputCliente objRespuestaCliente = new OutputCliente();

        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getCliente";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlCliente recurso= new CtrlCliente ();
            		objRespuestaCliente=recurso.getDatos(objDatos, Conf.METODO_GET);
               	log.trace("consume metodo");
                }
            	else{
            		// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETCLIENTE);
                    log.trace("url:" + url);
                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaCliente.setRespuesta(objRespuesta);
                    } else {
                        ClienteCliente wsPDV = new ClienteCliente();
                        wsPDV.setServerUrl(url);
                        objRespuestaCliente = wsPDV.getCliente(objDatos);
                    }
            	}
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCliente.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCliente.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
                objRespuestaCliente.setToken(TOKEN);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCliente.setRespuesta(objRespuesta);
        }

        return objRespuestaCliente;
    }
}