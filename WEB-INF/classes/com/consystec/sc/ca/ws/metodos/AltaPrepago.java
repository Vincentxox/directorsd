package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.AltaPrepagoCliente;
import com.consystec.sc.ca.ws.input.altaprepago.InputAltaPrepago;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.altaprepago.OutputAltaPrepago;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;

public class AltaPrepago extends ControladorBase {
    String TOKEN = "";

    private static final Logger log = Logger.getLogger(AltaPrepago.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputAltaPrepago objDatos) {
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
        if (objDatos.getUsuario() == null ||  "".equals(objDatos.getUsuario())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        if (objDatos.getToken() == null || "".equals((objDatos.getToken().trim()))) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        } else {
            if (!"WEB".equals(objDatos.getToken()) && (objDatos.getCodDispositivo() == null || "".equals(objDatos.getCodDispositivo().trim()))) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                
            }
        }
        return objRespuesta;
    }

    /**
     * M\u00E9todo para consultar datos
     */
    public OutputAltaPrepago crearAltaPrepago(InputAltaPrepago objDatos) {
        OutputAltaPrepago objRespuestaSol = new OutputAltaPrepago();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearPDV";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        log.trace("2");
        if (objRespuesta == null) {
            log.trace("1");
            try {
                conn = getConnLocal();
                log.trace("Ingresa a operar para servicio web...");
                TOKEN = gettokenString(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                        objDatos.getCodDispositivo());
                log.trace("TOKEN:" + TOKEN);
       
               if(TOKEN.contains(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7+"")){ 
            	   String mensaje = TOKEN.replace("-7||", "");
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7,mensaje,
                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	TOKEN="";
                	objRespuestaSol.setRespuesta(objRespuesta);	
               }

                if ("LOGIN".equals(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaSol.setRespuesta(objRespuesta);
                } else if (objRespuesta != null) {
                    objRespuestaSol.setRespuesta(objRespuesta);
                } else {
                    // obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREA_ALTAPREPAGO);

                    log.trace("url:" + url);
                    if (url == null || "null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaSol.setRespuesta(objRespuesta);
                    } else {
                        AltaPrepagoCliente wsSol = new AltaPrepagoCliente();
                        wsSol.setServerUrl(url);
                        objRespuestaSol = wsSol.crearAltaPrepago(objDatos);
                    }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaSol.setRespuesta(objRespuesta);
                log.error(e,e);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaSol.setRespuesta(objRespuesta);
        }

        objRespuestaSol.setToken(TOKEN);
        return objRespuestaSol;
    }
}
