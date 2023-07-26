package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.CatalogosCliente;
import com.consystec.sc.ca.ws.input.catalogo.InputCatalogo;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.catalogo.OutputCatalogo;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlCatalogo;

public class Catalogos extends ControladorBase {

    private static final Logger log = Logger.getLogger(Catalogos.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputCatalogo objDatos) {
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
     * M\u00E9todo para crear cat\u00E9logos de configuraci\u00F3n
     */
    public OutputCatalogo crearCatalogo(InputCatalogo objDatos) {
        OutputCatalogo objRespuestaCat = new OutputCatalogo();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearCatalogo";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
            	if (isFullStack(objDatos.getCodArea()))
                {
            		CtrlCatalogo recurso= new CtrlCatalogo();
            		objRespuestaCat=recurso.getDatos(objDatos, Conf.METODO_POST);
               	log.trace("consume metodo");
                }
            	else{
            		  conn = getConnLocal();
                      // obteniendo url de servicio
                      url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREARCATALOGO);
                      log.trace("url:" + url);
                      if (url == null || url.equals("null") || "".equals(url)) {
                          objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                  null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                          objRespuestaCat.setRespuesta(objRespuesta);
                      } else {
                          CatalogosCliente wsCtalogos = new CatalogosCliente();
                          wsCtalogos.setServerUrl(url);
                          objRespuestaCat = wsCtalogos.crearCatalogo(objDatos);
                      }
            	}
              
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCat.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCat.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCat.setRespuesta(objRespuesta);
        }

        return objRespuestaCat;
    }

    /**
     * M\u00E9todo para modificar cat\u00E9logos de configuraci\u00F3n
     */
    public OutputCatalogo modificarCatalogo(InputCatalogo objDatos) {
        OutputCatalogo objRespuestaCat = new OutputCatalogo();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "modificarCatalogo";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                {
            		CtrlCatalogo recurso= new CtrlCatalogo();
            		objRespuestaCat=recurso.getDatos(objDatos, Conf.METODO_PUT);
               	log.trace("consume metodo");
                }
            	else{
            		 // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MODCATALOGO);
                    log.trace("url:" + url);
                    if (url == null || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaCat.setRespuesta(objRespuesta);
                    } else {
                        CatalogosCliente wsCtalogos = new CatalogosCliente();
                        wsCtalogos.setServerUrl(url);
                        objRespuestaCat = wsCtalogos.modificarCatalogo(objDatos);
                    }
            	}
               
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCat.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCat.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCat.setRespuesta(objRespuesta);
        }

        return objRespuestaCat;
    }

    /**
     * M\u00E9todo para consultar cat\u00E9logos de configuraci\u00F3n
     */
    public OutputCatalogo getCatalogo(InputCatalogo objDatos) {
        OutputCatalogo objRespuestaCat = new OutputCatalogo();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getCatalogo";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                {
            		CtrlCatalogo recurso= new CtrlCatalogo();
            		objRespuestaCat=recurso.getDatos(objDatos, Conf.METODO_GET);
               	log.trace("consume metodo");
                }
            	else{
            		// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETCATALOGO);
                    log.trace("url:" + url);
                    if (url == null || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaCat.setRespuesta(objRespuesta);
                    } else {
                        CatalogosCliente wsCtalogos = new CatalogosCliente();
                        wsCtalogos.setServerUrl(url);
                        objRespuestaCat = wsCtalogos.getCatalogo(objDatos);
                    }
            	}
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCat.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCat.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCat.setRespuesta(objRespuesta);
        }

        return objRespuestaCat;
    }

    /**
     * M\u00E9todo para dar de baja un p\u00E9rametro
     **/

    public OutputCatalogo bajaCatalogo(InputCatalogo objDatos) {
        OutputCatalogo objRespuestaCat = new OutputCatalogo();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "bajaCatalogo";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                {
            		CtrlCatalogo recurso= new CtrlCatalogo();
            		objRespuestaCat=recurso.getDatos(objDatos, Conf.METODO_DELETE);
               	log.trace("consume metodo");
                }
            	else{
            		// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_DELCATALOGO);
                    log.trace("url:" + url);
                    if (url == null || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaCat.setRespuesta(objRespuesta);
                    } else {
                        CatalogosCliente wsCtalogos = new CatalogosCliente();
                        wsCtalogos.setServerUrl(url);
                        objRespuestaCat = wsCtalogos.delCatalogo(objDatos);
                    }
            	}
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCat.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCat.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCat.setRespuesta(objRespuesta);
        }

        return objRespuestaCat;
    }
}
