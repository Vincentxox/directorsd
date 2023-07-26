package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.PromoOfertaCampaniaCliente;
import com.consystec.sc.ca.ws.input.ofertacampania.InputPromoOfertaCampania;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.ofertacampania.OutputPromoOfertaCampania;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlPromoOfertaCampania;

public class PromoOfertaCampania extends ControladorBase {

    private static final Logger log = Logger.getLogger(PromoOfertaCampania.class);

    /***
     * Validando que no vengan parametros nulos
     */
    public Respuesta validarDatos(InputPromoOfertaCampania objDatos) {
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
     * M\u00E9todo para crear OfertaCampanias
     */
    public OutputPromoOfertaCampania asignarPromoCampania(InputPromoOfertaCampania objDatos) {
        OutputPromoOfertaCampania objRespuestaOfertaCampania = new OutputPromoOfertaCampania();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearOfertaCampania";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if(isFullStack(objDatos.getCodArea())){
                	CtrlPromoOfertaCampania recurso = new CtrlPromoOfertaCampania();
                	 objRespuestaOfertaCampania =recurso.getDatos(objDatos, Conf.METODO_POST);
                	
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_ASIGNA_PROMO_CAMPANIA);
	                log.trace("url:" + url);
	                if (url == null || url.equals("null") || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaOfertaCampania.setRespuesta(objRespuesta);
	                } else {
	                    PromoOfertaCampaniaCliente wsOfertaCampania = new PromoOfertaCampaniaCliente();
	                    wsOfertaCampania.setServerUrl(url);
	                    objRespuestaOfertaCampania = wsOfertaCampania.crearOfertaCampania(objDatos);
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaOfertaCampania.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaOfertaCampania.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaOfertaCampania.setRespuesta(objRespuesta);
        }

        return objRespuestaOfertaCampania;
    }

    /**
     * M\u00E9todo para obtener OfertaCampanias
     */
    public OutputPromoOfertaCampania getPromoCampania(InputPromoOfertaCampania objDatos) {
        OutputPromoOfertaCampania objRespuestaOfertaCampania = new OutputPromoOfertaCampania();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearOfertaCampania";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                
                if(isFullStack(objDatos.getCodArea())){
                	CtrlPromoOfertaCampania recurso = new CtrlPromoOfertaCampania();
                	 objRespuestaOfertaCampania =recurso.getDatos(objDatos, Conf.METODO_GET);
                	
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_PROMO_CAMPANIA);
	                log.trace("url:" + url);
	                if (url == null || url.equals("null") || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaOfertaCampania.setRespuesta(objRespuesta);
	                } else {
	                    PromoOfertaCampaniaCliente wsOfertaCampania = new PromoOfertaCampaniaCliente();
	                    wsOfertaCampania.setServerUrl(url);
	                    objRespuestaOfertaCampania = wsOfertaCampania.getOfertaCampania(objDatos);
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaOfertaCampania.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaOfertaCampania.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaOfertaCampania.setRespuesta(objRespuesta);
        }

        return objRespuestaOfertaCampania;
    }
}
