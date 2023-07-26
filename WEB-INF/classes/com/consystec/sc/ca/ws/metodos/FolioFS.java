package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.FolioFSCliente;
import com.consystec.sc.ca.ws.folio.InputFolioVirtual;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.folio.OutputConfiguracionFolioVirtual;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlFoliosFS;
import com.google.gson.GsonBuilder;

public class FolioFS extends ControladorBase {
    private static final Logger log = Logger.getLogger(FolioFS.class);
    String TOKEN = "";

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputFolioVirtual objDatos, int metodo) {
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

        if (objDatos.getUsuario() == null ||  "".equals(objDatos.getUsuario().trim())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), nombreMetodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para crear FolioFSs
     */
    public OutputConfiguracionFolioVirtual crearFolioFS(InputFolioVirtual objDatos) {
        OutputConfiguracionFolioVirtual objRespuestaWS = new OutputConfiguracionFolioVirtual();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearFolioFS";
        String url = "";

        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_POST);

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                log.trace("Ingresa a operar para servicio web...");
                    // obteniendo url de servicio
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlFoliosFS recurso=new CtrlFoliosFS();
            		objRespuestaWS=recurso.getDatos(objDatos , Conf.METODO_POST);
                }
                else
                {
                	 url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_RESERVA_FOLIOFS);

                     log.trace("url: " + url);
                     log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));

                     if (url == null || url.equals("null") || "".equals(url)) {
                         objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                 metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                         objRespuestaWS.setRespuesta(objRespuesta);
                     } else {
                         FolioFSCliente wsFolioFS = new FolioFSCliente();
                         wsFolioFS.setServerUrl(url);
                         objRespuestaWS = wsFolioFS.creaFolioFS(objDatos);
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

        return objRespuestaWS;
    }

    /**
     * M\u00E9todo para obtener informaci\u00F3n FolioFSs
     */
    public OutputConfiguracionFolioVirtual getFolioFS(InputFolioVirtual objDatos) {
        OutputConfiguracionFolioVirtual objRespuestaWS = new OutputConfiguracionFolioVirtual();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getFolioFS";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_GET);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();

                // obteniendo url de servicio
                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlFoliosFS recurso=new CtrlFoliosFS();
            		objRespuestaWS=recurso.getDatos(objDatos , Conf.METODO_GET);
                }
                else
                {
                	 // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_FOLIOFS);

                    log.trace("url: " + url);
                    log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));

                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaWS.setRespuesta(objRespuesta);
                    } else {
                        FolioFSCliente wsFolioFS = new FolioFSCliente();
                        wsFolioFS.setServerUrl(url);
                        objRespuestaWS = wsFolioFS.getFolioFS(objDatos);
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

        return objRespuestaWS;
    }
}
