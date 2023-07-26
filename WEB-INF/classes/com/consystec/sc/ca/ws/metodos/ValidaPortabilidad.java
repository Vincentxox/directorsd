package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.ValidaPortacionCliente;
import com.consystec.sc.ca.ws.input.portabilidad.InputPortabilidad;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.portabilidad.OutputPortabilidad;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlValidarPortabilidad;
import com.google.gson.GsonBuilder;

public class ValidaPortabilidad extends ControladorBase {

    private static final Logger log = Logger.getLogger(Pais.class);
    String TOKEN = "";
    
    public Respuesta validarDatos(InputPortabilidad objDatos) {
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
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), metodo, metodo, null,
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
		
		if (objDatos.getToken() == null || "".equals(objDatos.getToken()) || "".equals((objDatos.getToken().trim()))) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(), metodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }else {
            if (!"WEB".equalsIgnoreCase(objDatos.getToken()) && (objDatos.getCodDispositivo() == null || "".equals(objDatos.getCodDispositivo().trim()))) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            }
        } 

        return objRespuesta;
    }
    
    public OutputPortabilidad validaPortabilidad(InputPortabilidad objDatos) {
    	OutputPortabilidad objRespuestaPortabilidad = new OutputPortabilidad();

        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "creaVentaRuta";
        String url = "";
        log.trace("inicia a validar valores...");

        try {
            conn = getConnLocal();
            objRespuesta = validarDatos(objDatos);

            if (objRespuesta == null) {
                try {
                    TOKEN = getToken(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                            objDatos.getCodDispositivo(), 0);
                    log.trace("TOKEN:" + TOKEN);
                } catch (Exception e) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
                            this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaPortabilidad.setRespuesta(objRespuesta);
                    log.error(e,e);
                }

                if (TOKEN.equals("LOGIN")) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaPortabilidad.setRespuesta(objRespuesta);
                } else if (TOKEN.contentEquals("ERROR")) {
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    String mensaje = TOKEN.replace("ERROR", "");
                    objRespuesta.setExcepcion(mensaje);
                    objRespuestaPortabilidad.setRespuesta(objRespuesta);
                    TOKEN = "";
                } else if (objRespuesta != null) {
                    objRespuestaPortabilidad.setRespuesta(objRespuesta);
                } else {
                	if (isFullStack(objDatos.getCodArea())){
                		  CtrlValidarPortabilidad recurso = new CtrlValidarPortabilidad();
                		  objRespuestaPortabilidad=recurso.getDatos(objDatos);
                	}else{
	                    // obteniendo url de servicio
	                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_VALIDA_PORTABILIDAD);
	                    //url = "http://localhost:8181/WS_SIDRA_PA/sidra/rest/creaventaruta/";// TODO cambiar url quemada
	                    log.trace("url: " + url);
	                    log.trace("json post: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));
	
	                    if (url == null || url.equals("null") || "".equals(url)) {
	                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
	                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                        objRespuestaPortabilidad.setRespuesta(objRespuesta);
	                    } else {
	                        ValidaPortacionCliente wsCliente = new ValidaPortacionCliente();
	                        wsCliente.setServerUrl(url);
	                        objRespuestaPortabilidad = wsCliente.validaPortacion(objDatos);
	                        log.trace("respuesta: " + objRespuestaPortabilidad.getRespuesta().getDescripcion());
	                        log.trace("json resp: " + new GsonBuilder().setPrettyPrinting().create().toJson(objRespuestaPortabilidad));
	                    }
                	}
                }
            } else {
                log.trace("Advertencia:" + objRespuesta.getCodResultado());
                log.trace("Descripcion:" + objRespuesta.getDescripcion());
                objRespuestaPortabilidad.setRespuesta(objRespuesta);
            }

        } catch (SQLException e) {
            log.error(e, e);
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            objRespuestaPortabilidad.setRespuesta(objRespuesta);

        } catch (Exception e) {
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            objRespuestaPortabilidad.setRespuesta(objRespuesta);

        } finally {
            DbUtils.closeQuietly(conn);
            objRespuestaPortabilidad.setToken(TOKEN);
        }

        return objRespuestaPortabilidad;
    }
}
