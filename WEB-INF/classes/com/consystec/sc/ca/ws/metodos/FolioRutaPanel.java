package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.FolioRutaPanelCliente;
import com.consystec.sc.ca.ws.folio.InputFolioVirtual;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.folio.OutputConfiguracionFolioVirtual;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlFolioVirtual;

public class FolioRutaPanel extends ControladorBase {

    private static final Logger log = Logger.getLogger(FolioRutaPanel.class);

    /***
     * Validando que no vengan parametros nulos
     */
    public Respuesta validarDatos(InputFolioVirtual objDatos) {
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
     * Metodo para crear folios de bodegas virtuales
     */
    public OutputConfiguracionFolioVirtual crearFolioBodVirtual(InputFolioVirtual objDatos) {
        OutputConfiguracionFolioVirtual objRespuestaFolio = new OutputConfiguracionFolioVirtual();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getBodegas";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlFolioVirtual recurso=new CtrlFolioVirtual();
            		objRespuestaFolio=recurso.getDatos(objDatos , Conf.METODO_POST);
                }
                else
                {
                	 // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_ADDFOLIOVIRTUAL);
                    log.trace("url:" + url);
                    if (url == null ||  "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaFolio.setRespuesta(objRespuesta);
                    } else {
                        FolioRutaPanelCliente wsFolioBodegaV = new FolioRutaPanelCliente();
                        wsFolioBodegaV.setServerUrl(url);
                        objRespuestaFolio = wsFolioBodegaV.crearFolioV(objDatos);
                    }
                }
               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaFolio.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaFolio.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaFolio.setRespuesta(objRespuesta);
        }

        return objRespuestaFolio;
    }

    /**
     * Metodo para dar de baja folios de bodegas virtuales
     */
    public OutputConfiguracionFolioVirtual bajaFolioBodVirtual(InputFolioVirtual objDatos) {
        OutputConfiguracionFolioVirtual objRespuestaFolio = new OutputConfiguracionFolioVirtual();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getBodegas";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlFolioVirtual recurso=new CtrlFolioVirtual();
            		objRespuestaFolio=recurso.getDatos(objDatos , Conf.METODO_DELETE);
                }
                else
                {
                	   // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_BAJAFOLIOVIRTUAL);
                    log.trace("url:" + url);
                    if (url == null ||  "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaFolio.setRespuesta(objRespuesta);
                    } else {
                        FolioRutaPanelCliente wsFolioBodegaV = new FolioRutaPanelCliente();
                        wsFolioBodegaV.setServerUrl(url);
                        objRespuestaFolio = wsFolioBodegaV.bajaFolioV(objDatos);
                    }
                	
                }
             
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaFolio.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaFolio.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaFolio.setRespuesta(objRespuesta);
        }

        return objRespuestaFolio;
    }

    /**
     * Metodo para dar de baja folios de bodegas virtuales
     */
    public OutputConfiguracionFolioVirtual getFolioBodVirtual(InputFolioVirtual objDatos) {
        OutputConfiguracionFolioVirtual objRespuestaFolio = new OutputConfiguracionFolioVirtual();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getBodegas";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlFolioVirtual recurso=new CtrlFolioVirtual();
            		objRespuestaFolio=recurso.getDatos(objDatos , Conf.METODO_GET);
                }
                else
                {
                	// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETFOLIOVIRTUAL);
                    log.trace("url:" + url);
                    if (url == null || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaFolio.setRespuesta(objRespuesta);
                    } else {
                        FolioRutaPanelCliente wsFolioBodegaV = new FolioRutaPanelCliente();
                        wsFolioBodegaV.setServerUrl(url);
                        objRespuestaFolio = wsFolioBodegaV.getFolioV(objDatos);
                    }
                }
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaFolio.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaFolio.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaFolio.setRespuesta(objRespuesta);
        }

        return objRespuestaFolio;
    }
}