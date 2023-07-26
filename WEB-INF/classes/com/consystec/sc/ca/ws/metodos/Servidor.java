package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;

import com.consystec.sc.ca.ws.input.general.InputConsultaWeb;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.generico.OutputServidor;
import com.consystec.sc.ca.ws.output.generico.UrlServidor;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;

public class Servidor extends ControladorBase {

    private static final Log log = LogFactory.getLog(Servidor.class);

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

    public OutputServidor getServidor(InputConsultaWeb objeto) {
        DOMConfigurator.configure(Servidor.class.getClassLoader().getResource("log4j.xml"));
        OutputServidor objServer = new OutputServidor();
        String metodo = "getServidor";
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String query = "";
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        List<UrlServidor> LstServidor = new ArrayList<UrlServidor>();

        try {
            conn = getConnLocal();

            query = "SELECT * FROM TC_SC_SERVIDOR ";

            pstmt = conn.prepareStatement(query);
            rst = pstmt.executeQuery();

            LstServidor = new ArrayList<UrlServidor>();

            while (rst.next()) {

                UrlServidor obj = new UrlServidor();
                obj.setPuerto(rst.getString("PUERTO"));
                obj.setTipo(rst.getString("TIPO"));
                obj.setUrl(rst.getString("URL"));

                LstServidor.add(obj);

            }

            if (LstServidor.isEmpty()) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_DATOSNOENCONTRADOS_10, null, null,
                        this.getClass().getName(), metodo, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objServer.setServidores(LstServidor);
            } else {
                log.info("tama\u00F1o elementos:" + LstServidor.size());
                objRespuesta = getMensaje(Conf_Mensajes.OK_ACTUALIZACION2, null, null, null, null, null);
                objServer.setRespuesta(objRespuesta);
                objServer.setServidores(LstServidor);
            }
        } catch (SQLException e) {
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            log.error(e,e);
        } catch (Exception e) {
        	log.error(e,e);
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            objServer.setRespuesta(objRespuesta);
        } finally {
        	DbUtils.closeQuietly(pstmt);
        	DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(conn);
        }

        return objServer;
    }
}
