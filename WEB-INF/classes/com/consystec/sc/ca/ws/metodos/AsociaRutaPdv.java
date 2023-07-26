package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.AsociaRutaPdvCliente;
import com.consystec.sc.ca.ws.input.ruta.InputRutaPdv;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlRutaPdv;


public class AsociaRutaPdv extends ControladorBase {

    private static final Logger log = Logger.getLogger(AsociaRutaPdv.class);

    /***
     * Validando que no vengan parametros nulos
     */
    public Respuesta validarDatos(InputRutaPdv objDatos) {
        Connection conn = null;
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (objDatos.getCodArea().equals(null) || "".equals(objDatos.getCodArea())) {
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
        if (objDatos.getUsuario().equals(null) || "".equals(objDatos.getUsuario())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        return objRespuesta;
    }

    /***
     * Metodo para asignar puntos de venta a rutas
     * 
     * @param objDatos
     * @return
     */
    public Respuesta asociaRutaPdv(InputRutaPdv objDatos) {
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "asociaRutaPdv";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {

            objRespuesta = new Respuesta();

            try {            	
                conn = getConnLocal();
                if(isFullStack(objDatos.getCodArea()))
                {
                	log.trace("consumir metodo");
                	CtrlRutaPdv recurso =new CtrlRutaPdv();
                	objRespuesta=recurso.asociaRutaPdv(objDatos);
                	                	
                }
                else
                {
                	 // obteniendo url de servicio
                    log.trace("obteniendo url");
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_ASOCIA_RUTAPDV);
                    log.trace("url:" + url);

                    if (url == null || "null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

                    } else {
                        AsociaRutaPdvCliente wsPDV = new AsociaRutaPdvCliente();
                        wsPDV.setServerUrl(url);
                        objRespuesta = wsPDV.creaAsociacionRutaPdv(objDatos);
                    }
                }
              
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);

            } catch (Exception e) {
            	 log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());

        }

        return objRespuesta;
    }

    /***
     * Metodo para asignar puntos de venta a rutas
     * 
     * @param objDatos
     * @return
     */
    public Respuesta eliminaRutaPdv(InputRutaPdv objDatos) {
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "asociaRutaPdv";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {

            objRespuesta = new Respuesta();

            try {
                conn = getConnLocal();
                
                if(isFullStack(objDatos.getCodArea()))
                {
                	log.trace("consumir metodo");
                	CtrlRutaPdv recurso =new CtrlRutaPdv();
                	objRespuesta=recurso.eliminarutapdv(objDatos);
                }
                else
                {
                	url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_ELIMINA_RUTAPDV);
                    log.trace("url:" + url);

                    if (url == null || "null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

                    } else {
                        AsociaRutaPdvCliente wsPDV = new AsociaRutaPdvCliente();
                        wsPDV.setServerUrl(url);
                        objRespuesta = wsPDV.desasignaRutaPdv(objDatos);
                    }
                }
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);

            } catch (Exception e) {
            	 log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());

        }

        return objRespuesta;
    }
}