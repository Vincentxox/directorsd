package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.general.InputVersionCatalogo;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.CatalogoVer.OutputVersionCatalogo;
import com.consystec.sc.ca.ws.output.CatalogoVer.VersionCatalogo;
import com.consystec.sc.sv.ws.operaciones.OperacionVersionCatalogo;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlVersionCatalogo extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlVersionCatalogo.class);
    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_VERSION_CATALOGO;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    /***
     * Validando que no vengan par\u00E9metros nulos.
     */
    public Respuesta validarDatos(InputVersionCatalogo objDatos) {
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (objDatos.getFecha() == null || "".equals(objDatos.getFecha())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHANULO_40, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
        }

        if (objDatos.getGrupo() == null || "".equals(objDatos.getGrupo())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_GRUPONULO_41, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para obtener de las actualizaciones de catalogos.
     */
    public OutputVersionCatalogo getCatalogo(InputVersionCatalogo objDatos) {
        listaLog = new ArrayList<LogSidra>();
        List<VersionCatalogo> lstVersion = new ArrayList<VersionCatalogo>();
        OutputVersionCatalogo objRespuestaVersion = new OutputVersionCatalogo();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getCatalogo";
        COD_PAIS = objDatos.getCodArea();

        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        String usuario = "log.record";
        if (objRespuesta == null) {
            try {
                conn = getConnRegional();
               ID_PAIS = getIdPais(conn, objDatos.getCodArea());

                // obteniendo valores a retornar
                lstVersion = OperacionVersionCatalogo.getCatalogo(conn, objDatos, ID_PAIS);

                log.trace("obtuvo valores..");
                log.trace("lstVersion tamanio:" + lstVersion.size());
                objRespuestaVersion.setCatalogos(lstVersion);
                if (lstVersion.isEmpty()) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, null, null, null, objDatos.getCodArea());
                    objRespuestaVersion.setRespuesta(objRespuesta);
                } else {
                    objRespuesta = getMensaje(Conf_Mensajes.OK_VERSION9, null, null, null, null, objDatos.getCodArea());
                    objRespuestaVersion.setRespuesta(objRespuesta);
                }

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de versiones de catalogo.", ""));

            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
                objRespuestaVersion.setRespuesta(objRespuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de versiones de catalogo.",
                        e.getMessage()));

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
                objRespuestaVersion.setRespuesta(objRespuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de versiones de catalogo.",
                        e.getMessage()));
            } finally {
                DbUtils.closeQuietly(conn);

                UtileriasJava.doInsertLog(listaLog, usuario, objDatos.getCodArea());
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaVersion.setRespuesta(objRespuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

            UtileriasJava.doInsertLog(listaLog, usuario, objDatos.getCodArea());
        }

        log.trace("Descripcion:" + objRespuesta.getDescripcion());
        return objRespuestaVersion;
    }
}
