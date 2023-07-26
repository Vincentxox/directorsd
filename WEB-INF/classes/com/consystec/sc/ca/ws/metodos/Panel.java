package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.PanelCliente;
import com.consystec.sc.ca.ws.input.panel.InputPanel;
import com.consystec.sc.ca.ws.input.ruta.InputVendedor;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.panel.OutputPanel;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlPanel;
import com.google.gson.GsonBuilder;

public class Panel extends ControladorBase {
    String TOKEN = "";
    private static final Logger log = Logger.getLogger(Panel.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     * 
     * @param metodo
     */
    public Respuesta validarDatos(InputPanel objDatos, int metodo) {
        Connection conn = null;
        Respuesta objRespuesta = null;
        String nombreMetodo = "validarDatos";

        if (objDatos.getCodArea() == null || "".equals(objDatos.getCodArea())) {
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

        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), nombreMetodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        if (metodo == Conf.METODO_GET) {
            if ( "".equals((objDatos.getToken().trim()))) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(),
                        nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            } else {
                if (!"WEB".equalsIgnoreCase(objDatos.getToken())&&(objDatos.getCodDispositivo() == null || "".equals(objDatos.getCodDispositivo().trim()))) {
                        objRespuesta = new Respuesta();
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null,
                                this.getClass().toString(), nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                }
            }
        }

        return objRespuesta;
    }

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatosVend(InputVendedor objDatos) {
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
     * M\u00E9todo para crear panel
     */
    public OutputPanel crearPanel(InputPanel objDatos) {
        OutputPanel objRespuestaPanel = new OutputPanel();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearPanel";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_POST);

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                
                if(isFullStack(objDatos.getCodArea())){
                	CtrlPanel recurso = new CtrlPanel();
                	objRespuestaPanel = recurso.insertarPanel(objDatos);
                }else{
                
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREAPANEL);
	
	                log.trace("url:" + url);
	                if (url == null || "null".equals(url) || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaPanel.setRespuesta(objRespuesta);
	                } else {
	                    PanelCliente wsPanel = new PanelCliente();
	                    wsPanel.setServerUrl(url);
	                    objRespuestaPanel = wsPanel.crearPanel(objDatos);
	                }
                }

            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPanel.setRespuesta(objRespuesta);

            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPanel.setRespuesta(objRespuesta);

            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaPanel.setRespuesta(objRespuesta);
        }

        return objRespuestaPanel;
    }

    /**
     * M\u00E9todo para modificar paneles
     */
    public OutputPanel modificarPanel(InputPanel objDatos) {
        OutputPanel objRespuestaPanel = new OutputPanel();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearPanel";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_PUT);

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                
                if(isFullStack(objDatos.getCodArea())){
                	CtrlPanel recurso = new CtrlPanel();
                	 objRespuestaPanel =recurso.modificarPanel(objDatos);
                	
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MODIFICAPANEL);
	
	                log.trace("url:" + url);
	                if (url == null || "null".equals(url) || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaPanel.setRespuesta(objRespuesta);
	                } else {
	                    PanelCliente wsPanel = new PanelCliente();
	                    wsPanel.setServerUrl(url);
	                    objRespuestaPanel = wsPanel.modificarPanel(objDatos);
	                }
                }

            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPanel.setRespuesta(objRespuesta);

            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPanel.setRespuesta(objRespuesta);

            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaPanel.setRespuesta(objRespuesta);
        }

        return objRespuestaPanel;
    }

    /**
     * M\u00E9todo para cambiar de estado a paneles
     */
    public OutputPanel cambiaestadoPanel(InputPanel objDatos) {
        OutputPanel objRespuestaPanel = new OutputPanel();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearPanel";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_PUT);

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                
                if(isFullStack(objDatos.getCodArea())){
                	CtrlPanel recurso = new CtrlPanel();
                	 objRespuestaPanel =recurso.cambiarEstadoPanel(objDatos);
                	
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CAMBIAESTADOPANEL);
	                log.trace("url:" + url);
	                if (url == null || "null".equals(url) || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaPanel.setRespuesta(objRespuesta);
	                } else {
	                    PanelCliente wsPanel = new PanelCliente();
	                    wsPanel.setServerUrl(url);
	                    objRespuestaPanel = wsPanel.modificarPanel(objDatos);
	                }
                }

            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPanel.setRespuesta(objRespuesta);

            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPanel.setRespuesta(objRespuesta);

            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaPanel.setRespuesta(objRespuesta);
        }

        return objRespuestaPanel;
    }

    /**
     * M\u00E9todo para obtener informaci\u00F3n de paneles
     */
    public OutputPanel getPanel(InputPanel objDatos) {
        OutputPanel objRespuestaWS = new OutputPanel();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getPanel";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_GET);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();

                TOKEN = gettokenString(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                        objDatos.getCodDispositivo());
                log.trace("TOKEN:" + TOKEN);
       
               if(TOKEN.contains(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7+"")){ 
            	   String mensaje = TOKEN.replace("-7||", "");
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7,mensaje,
                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	TOKEN="";
                	objRespuestaWS.setRespuesta(objRespuesta);
               }
               else  if ("LOGIN".equals(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaWS.setRespuesta(objRespuesta);

                }else if (TOKEN.contains("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaWS.setRespuesta(objRespuesta);
                	TOKEN="";
                }
                else {
                    if(isFullStack(objDatos.getCodArea())){
                    	CtrlPanel recurso = new CtrlPanel();
                    	objRespuestaWS =recurso.getDatos(objDatos, Conf.METODO_GET);
                    	
                    }else{
	                    // obteniendo url de servicio
	                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETPANEL);
	                    log.trace("url: " + url);
	                    log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));
	
	                    if (url == null || "null".equals(url) || "".equals(url)) {
	                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
	                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                        objRespuestaWS.setRespuesta(objRespuesta);
	                    } else {
	                        PanelCliente wsPanel = new PanelCliente();
	                        wsPanel.setServerUrl(url);
	                        objRespuestaWS = wsPanel.getPanel(objDatos);
	                    }
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
     * M\u00E9todo para obtener id de panel a traves del id del vendedor
     */
    public OutputPanel getPanelbyVendedor(InputVendedor objDatos) {
        OutputPanel objRespuestaPanel = new OutputPanel();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearPanel";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatosVend(objDatos);

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if(isFullStack(objDatos.getCodArea())){
                	CtrlPanel recurso = new CtrlPanel();
                	objRespuestaPanel =recurso.getDatos(objDatos);
                	
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETPANEL_VEND);
	                log.trace("url:" + url);
	                if (url == null || "null".equals(url) || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaPanel.setRespuesta(objRespuesta);
	                } else {
	                    PanelCliente wsPanel = new PanelCliente();
	                    wsPanel.setServerUrl(url);
	                    objRespuestaPanel = wsPanel.getPanelbyVendedor(objDatos);
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPanel.setRespuesta(objRespuesta);

            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPanel.setRespuesta(objRespuesta);

            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaPanel.setRespuesta(objRespuesta);
        }

        return objRespuestaPanel;
    }
}
