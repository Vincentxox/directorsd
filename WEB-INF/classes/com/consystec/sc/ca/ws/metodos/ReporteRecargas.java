package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.ReporteCliente;
import com.consystec.sc.ca.ws.input.reporte.InputReporteRecarga;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteRecarga;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;


public class ReporteRecargas extends ControladorBase{
	private static final Logger log = Logger.getLogger(Pais.class);
    String TOKEN = "";
    
    public Respuesta validarInput(InputReporteRecarga objDatos){
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
    			objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), metodo, metodo, null,
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
    
    public OutputReporteRecarga getReporteRecarga(InputReporteRecarga objDatos) {
    	OutputReporteRecarga objRespuestaReporte = new OutputReporteRecarga();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getReporteRecarga";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarInput(objDatos);

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                // obteniendo url de servicio
                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_REPORTE_RECARGAS);

                log.trace("url:" + url);
                if (url == null || url.equals("null") || "".equals(url)) {
                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaReporte.setRespuesta(objRespuesta);
                } else {
                    ReporteCliente wsReporte = new ReporteCliente();
                    wsReporte.setServerUrl(url);
                    objRespuestaReporte = wsReporte.getReporteRecargas(objDatos); 
                }

            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaReporte.setRespuesta(objRespuesta);

            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaReporte.setRespuesta(objRespuesta);

            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaReporte.setRespuesta(objRespuesta);
        }

        return objRespuestaReporte;
    }
}
