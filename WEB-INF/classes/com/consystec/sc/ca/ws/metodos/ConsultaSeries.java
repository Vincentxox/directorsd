package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.ConsultaSeriesCliente;
import com.consystec.sc.ca.ws.input.inventario.InputConsultaSeries;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.inventario.OutputConsultaSeries;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlConsultaSeries;


public class ConsultaSeries extends ControladorBase {
    String TOKEN = "";

    private static final Logger log = Logger.getLogger(ConsultaSeries.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputConsultaSeries objDatos) {
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
        if (objDatos.getUsuario() == null ||"".equals(objDatos.getUsuario())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        if (objDatos.getToken() == null || "".equals(objDatos.getToken()) || "".equals((objDatos.getToken().trim()))) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        } else {
            if (!objDatos.getToken().equalsIgnoreCase("WEB")&&(objDatos.getCodDispositivo() == null || "".equals(objDatos.getCodDispositivo().trim()))) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            }
        }
        return objRespuesta;
    }

    /**
     * M\u00E9todo para modifica distribuidores
     */
    public OutputConsultaSeries getDatos(InputConsultaSeries input) {
        OutputConsultaSeries objRespuestaInventario = new OutputConsultaSeries();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getInventario";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(input);
        if (objRespuesta == null) {
            log.trace("valores validados");
            try {
                log.trace("obtiene conexion");
                conn = getConnLocal();
                try {
                    TOKEN = getToken(conn, input.getUsuario(), input.getToken(), input.getCodArea(),
                            input.getCodDispositivo(),0);
                    log.trace("TOKEN:" + TOKEN);
                } catch (Exception e) {

                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
                            this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    log.error(e,e);
                }
                if (TOKEN.equals("LOGIN")) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaInventario.setRespuesta(objRespuesta);
                }else if (TOKEN.contentEquals("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaInventario.setRespuesta(objRespuesta);
                	TOKEN="";
                } else if (objRespuesta != null) {
                    objRespuestaInventario.setRespuesta(objRespuesta);
                } else {
                	 if (isFullStack(input.getCodArea()))
                     { 
                 		log.trace("consumir metodo");
                 		CtrlConsultaSeries recurso=new CtrlConsultaSeries();
                 		objRespuestaInventario=recurso.getDatos(input , Conf.METODO_GET);
                     }
                     else
                     {
                    	 // obteniendo url de servicio
                         log.trace("obteniendo url");
                         url = Util.getURLWSLOCAL(conn, input.getCodArea(), Conf.SERVICIO_LOCAL_GET_SERIES);
                         log.trace("url:" + url);
                         if (url == null || url.equals("null") || "".equals(url)) {
                             objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                     metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                             objRespuestaInventario.setRespuesta(objRespuesta);
                         } else {
                             log.trace("url valida");
                             ConsultaSeriesCliente wsInventario = new ConsultaSeriesCliente();
                             wsInventario.setServerUrl(url);
                             log.trace("consume servicio");
                             objRespuestaInventario = wsInventario.getSeries(input);
                         }
                     }
                   
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaInventario.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	 log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaInventario.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaInventario.setRespuesta(objRespuesta);
        }
        objRespuestaInventario.setToken(TOKEN);
        return objRespuestaInventario;
    }
}
