package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.PaisCliente;
import com.consystec.sc.ca.ws.input.general.InputConsultaWeb;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.pais.OutputConsultaPais;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlConsultaPais;

public class Pais extends ControladorBase {

    private static final Logger log = Logger.getLogger(Pais.class);

    /***
     * Validando que no vengan parametros nulos
     */
    public Respuesta validarDatos(InputConsultaWeb objDatos) {
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

    public OutputConsultaPais getInfoPais(InputConsultaWeb objeto) {
        OutputConsultaPais objPais = new OutputConsultaPais();
        com.consystec.sc.ca.ws.orm.Pais datosPais = new com.consystec.sc.ca.ws.orm.Pais();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String url = "";
        String metodo = "getInfoPais";
        try {
            conn = getConnLocal();
            if (isFullStack(objeto.getCodArea()))
            { 
        		log.trace("consumir metodo");
        		CtrlConsultaPais recurso=new CtrlConsultaPais();
        		objPais=recurso.getDatos(objeto, Conf.METODO_GET);
        		datosPais = getPais(conn, objeto.getCodArea());
        		   if ("0".equals(objPais.getRespuesta().getCodResultado()) || "202".equals(objPais.getRespuesta().getCodResultado())) {
                       objPais.setNombrePais(datosPais.getNombre());
                       objPais.setLongMaxIdentificacion(datosPais.getLong_identificacion().toString());
                       objPais.setLongMaxNumero(datosPais.getLong_numero().toString());
                   }
            }
            else
            {
            	datosPais = getPais(conn, objeto.getCodArea());
                url = Util.getURLWSLOCAL(conn, objeto.getCodArea(), Conf.SERVICIO_LOCAL_GETDATOSPAIS);
                log.trace("url:" + url);
                if (url == null || "".equals(url)) {
                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo, null,
                            Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objPais.setRespuesta(objRespuesta);
                } else {
                    log.trace("ingresa a consumir ws");
                    // obteniendo informaci\u00F3n de paises

                    PaisCliente ws = new PaisCliente();
                    ws.setServerUrl(url);
                    objPais = ws.getPais(objeto);
                    if ("0".equals(objPais.getRespuesta().getCodResultado())) {
                        objPais.setNombrePais(datosPais.getNombre());
                        objPais.setLongMaxIdentificacion(datosPais.getLong_identificacion().toString());
                        objPais.setLongMaxNumero(datosPais.getLong_numero().toString());
                    }
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
