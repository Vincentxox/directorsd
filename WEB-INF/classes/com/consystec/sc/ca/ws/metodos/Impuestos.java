package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.ImpuestoCliente;
import com.consystec.sc.ca.ws.input.impuestos.InputConsultaImpuestos;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.impuestos.OutputImpuestos;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlImpuestos;

public class Impuestos extends ControladorBase {

    private static final Logger log = Logger.getLogger(Impuestos.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputConsultaImpuestos objDatos) {
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

    public OutputImpuestos getDatos(InputConsultaImpuestos objeto) {
        OutputImpuestos objPais = new OutputImpuestos();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String url = "";
        String metodo = "getInfoPais";
        try {
            conn = getConnLocal();
            if (isFullStack(objeto.getCodArea()))
            { 
        		log.trace("consumir metodo");
        		CtrlImpuestos recurso=new CtrlImpuestos();
        		objPais=recurso.getDatos(objeto, Conf.METODO_GET);
            }
            else
            {
            	url = Util.getURLWSLOCAL(conn, objeto.getCodArea(), Conf.SERVICIO_LOCAL_GETIMPUESTOS);
                log.trace("url:" + url);

                if (url == null || "".equals(url)) {
                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo, null,
                            Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objPais.setRespuesta(objRespuesta);
                } else {
                    log.trace("ingresa a consumir ws");
                    // obteniendo informaci\u00F3n de paises

                    ImpuestoCliente ws = new ImpuestoCliente();
                    ws.setServerUrl(url);
                    objPais = ws.getPais(objeto);
                }
            }
            

        } catch (SQLException e) {
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            log.error(e,e);
        } catch (Exception e) {
        	log.error(e,e);
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            objPais.setRespuesta(objRespuesta);
        } finally {
            DbUtils.closeQuietly(conn);
        }

        return objPais;
    }
}
