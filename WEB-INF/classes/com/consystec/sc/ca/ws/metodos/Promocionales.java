package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.PromocionalesCliente;
import com.consystec.sc.ca.ws.input.inventariopromo.InputGetArtPromInventario;
import com.consystec.sc.ca.ws.input.promocionales.InputPromocionales;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.inventariopromo.OutputArtPromInventario;
import com.consystec.sc.ca.ws.output.promocionales.OutputPromocionales;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlArtPromInventario;
import com.consystec.sc.sv.ws.metodos.CtrlPromocionales;

public class Promocionales extends ControladorBase {

    private static final Logger log = Logger.getLogger(Promocionales.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputPromocionales objDatos) {
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

    public Respuesta validarDatosProductos(InputGetArtPromInventario objDatos)
    {
    	
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
     * M\u00E9todo para crear Promocionales
     */
    public OutputPromocionales crearPromocionales(InputPromocionales objDatos) {
        OutputPromocionales objRespuestaPromocionales = new OutputPromocionales();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearPanel";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea())){
                	CtrlPromocionales recurso = new CtrlPromocionales();
                	objRespuestaPromocionales=recurso.getDatos(objDatos, Conf.METODO_POST);
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREAPROMOCIONAL);
	                log.trace("url:" + url);
	                if (url == null || "null".equals(url) || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaPromocionales.setRespuesta(objRespuesta);
	                } else {
	                    PromocionalesCliente wsPromocionales = new PromocionalesCliente();
	                    wsPromocionales.setServerUrl(url);
	                    objRespuestaPromocionales = wsPromocionales.crearPromocionales(objDatos);
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPromocionales.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPromocionales.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaPromocionales.setRespuesta(objRespuesta);
        }

        return objRespuestaPromocionales;
    }

    /**
     * M\u00E9todo para modificar Promocionales
     */
    public OutputPromocionales modificaPromocionales(InputPromocionales objDatos) {
        OutputPromocionales objRespuestaPromocionales = new OutputPromocionales();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearPanel";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea())){
                	CtrlPromocionales recurso = new CtrlPromocionales();
                	objRespuestaPromocionales=recurso.getDatos(objDatos, Conf.METODO_PUT);
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MODPROMOCIONAL);
	                log.trace("url:" + url);
	                if (url == null || "null".equals(url) || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaPromocionales.setRespuesta(objRespuesta);
	                } else {
	                    PromocionalesCliente wsPromocionales = new PromocionalesCliente();
	                    wsPromocionales.setServerUrl(url);
	                    objRespuestaPromocionales = wsPromocionales.crearPromocionales(objDatos);
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPromocionales.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPromocionales.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaPromocionales.setRespuesta(objRespuesta);
        }

        return objRespuestaPromocionales;
    }

    /**
     * M\u00E9todo para dar de baja una Promocionales
     */
    public OutputPromocionales bajaPromocionales(InputPromocionales objDatos) {
        OutputPromocionales objRespuestaPromocionales = new OutputPromocionales();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearPanel";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea())){
                	CtrlPromocionales recurso = new CtrlPromocionales();
                	objRespuestaPromocionales=recurso.getDatos(objDatos, Conf.METODO_DELETE);
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_DELPROMOCIONAL);
	                log.trace("url:" + url);
	                if (url == null || "null".equals(url) || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaPromocionales.setRespuesta(objRespuesta);
	                } else {
	                    PromocionalesCliente wsPromocionales = new PromocionalesCliente();
	                    wsPromocionales.setServerUrl(url);
	                    objRespuestaPromocionales = wsPromocionales.crearPromocionales(objDatos);
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPromocionales.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPromocionales.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaPromocionales.setRespuesta(objRespuesta);
        }

        return objRespuestaPromocionales;
    }

    /************************************************
     * M\u00E9todo para obtener informaci\u00F3n de Promocionaless
     ************************************************/
    public OutputPromocionales getPromocionales(InputPromocionales objDatos) {
        OutputPromocionales objRespuestaPromocionales = new OutputPromocionales();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getPromocionales";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea())){
                	CtrlPromocionales recurso = new CtrlPromocionales();
                	objRespuestaPromocionales=recurso.getDatos(objDatos, Conf.METODO_GET);
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETPROMOCIONAL);
	                log.trace("url:" + url);
	                if (url == null || "null".equals(url) || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaPromocionales.setRespuesta(objRespuesta);
	                } else {
	                    PromocionalesCliente wsPromocionales = new PromocionalesCliente();
	                    wsPromocionales.setServerUrl(url);
	                    objRespuestaPromocionales = wsPromocionales.crearPromocionales(objDatos);
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPromocionales.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPromocionales.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaPromocionales.setRespuesta(objRespuesta);
        }

        return objRespuestaPromocionales;
    }
    
    
    /************************************************
     * M\u00E9todo para obtener informaci\u00F3n de Promocionales por Bodega 
     * 
     ************************************************/
    public OutputArtPromInventario getPromocionalesPorBodega(InputGetArtPromInventario objDatos)
    {
    	
    	OutputArtPromInventario objRespuestaPromocionales = new OutputArtPromInventario();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getPromocionalesPorBodega";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatosProductos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea())){
                	 CtrlArtPromInventario recurso = new CtrlArtPromInventario();
                	objRespuestaPromocionales=recurso.getRespuesta(objDatos, Conf.METODO_GET);
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_CONSULTA_PRODUCTOS_PROM);
	                log.trace("url:" + url);
	                if (url == null || "null".equals(url) || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaPromocionales.setRespuesta(objRespuesta);
	                } else {
	                    PromocionalesCliente wsPromocionales = new PromocionalesCliente();
	                    wsPromocionales.setServerUrl(url);
	                    objRespuestaPromocionales = wsPromocionales.getPromocionalesPorBodega(objDatos);
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPromocionales.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPromocionales.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaPromocionales.setRespuesta(objRespuesta);
        }

        return objRespuestaPromocionales;
    	 
    	
    }
    
}
