package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.portabilidad.InputCargaAdjuntoPorta;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.portabilidad.OutputAdjuntoPorta;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlCargaArchivosPorta;
public class AdjuntoPortabilidad extends ControladorBase{
	private static final Logger log = Logger.getLogger(OfertaComercial.class);
	String TOKEN = "";
	public Respuesta validarDatos(InputCargaAdjuntoPorta objDatos) {
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
            } catch (Exception e) {
                log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
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
	
	public OutputAdjuntoPorta cargarAdjunto(InputCargaAdjuntoPorta input){
		OutputAdjuntoPorta objRespuestaPortabilidad = new OutputAdjuntoPorta();
		Respuesta objRespuesta = new Respuesta();
		Connection conn = null;
	    String metodo = "getDescuentos";
	    String url = "";
	    log.trace("inicia a validar valores...");	    
	    objRespuesta = validarDatos(input);
	    if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                try {
                    TOKEN = getToken(conn, input.getUsuario(), input.getToken(), input.getCodArea(),
                    		input.getCodDispositivo(),0);
                    log.trace("TOKEN:" + TOKEN);
                } catch (Exception e) {

                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
                            this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaPortabilidad.setRespuesta(objRespuesta);
                    log.error(e,e);
                }

                if ("LOGIN".equalsIgnoreCase(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaPortabilidad.setRespuesta(objRespuesta);
                } else if (TOKEN.contentEquals("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaPortabilidad.setRespuesta(objRespuesta);
                	TOKEN="";
                }else if (objRespuesta != null) {
                	objRespuestaPortabilidad.setRespuesta(objRespuesta);
                } else { 
	              
                if (isFullStack(input.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlCargaArchivosPorta recurso=new CtrlCargaArchivosPorta();
            		objRespuestaPortabilidad = recurso.cargarAdjunto(input);
                }
                else
                {
                	// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, input.getCodArea(), Conf.SERVICIO_LOCAL_DESCUENTO_FS);
                    log.trace("url:" + url);
                    if (url == null || "null".equals(url) || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                                null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaPortabilidad.setRespuesta(objRespuesta);
                    } else {
                    	objRespuesta = new Respuesta();
	                    objRespuesta.setDescripcion("Servicio no Disponible para el codigo de Area Ingresado"); 
	                    objRespuesta.setCodResultado("0");
	                    objRespuestaPortabilidad.setRespuesta(objRespuesta);   
                    }	
                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPortabilidad.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e, e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPortabilidad.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaPortabilidad.setRespuesta(objRespuesta);
        }
	    return objRespuestaPortabilidad;
}
}
