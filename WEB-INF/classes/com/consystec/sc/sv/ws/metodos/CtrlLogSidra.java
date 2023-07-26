package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.InputLogSidra;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.sv.ws.operaciones.OperacionLogSidra;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;

public class CtrlLogSidra extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlLogSidra.class);
    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputLogSidra objDatos) {
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";
        
        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
        }

        if (objDatos.getLog() == null) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_LOG_VACIO_73, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
        } else {
            int a = 0;
            for (LogSidra obj : objDatos.getLog()) {
                if (obj.getTipoTransaccion() == null ||  "".equals(obj.getTipoTransaccion().trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPOTRANSACCION_NULO_74, this.getClass().toString(),
                            null, metodo, "El elemento " + a + " de la lista se encuentra incompleto.", objDatos.getCodArea());
                }
                if (obj.getOrigen() == null ||  "".equals(obj.getOrigen().trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_ORIGEN_NULO_75, this.getClass().toString(), null,
                            metodo, "El elemento " + a + " de la lista se encuentra incompleto.", objDatos.getCodArea());
                }

                if (obj.getId() == null ||  "".equals(obj.getId().trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDPDV_VACIO_8, this.getClass().toString(), null, metodo,
                            "El elemento " + a + " de la lista se encuentra incompleto.", objDatos.getCodArea());
                }
                if (obj.getTipoId() == null ||  "".equals(obj.getTipoId().trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPOID_NULO_77, this.getClass().toString(), null,
                            metodo, "El elemento " + a + " de la lista se encuentra incompleto.", objDatos.getCodArea());
                }

                if (obj.getResultado() == null ||  "".equals(obj.getResultado().trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_RESULTADO_NULO_78, this.getClass().toString(), null,
                            metodo, "El elemento " + a + " de la lista se encuentra incompleto.", objDatos.getCodArea());
                }
            }
        }

        if (objRespuesta != null) {
            log.trace("Resultado: " + objRespuesta.getDescripcion());
        }

        return objRespuesta;
    }

    /**
     * Servicio para registrar log de transacciones sidra
     * 
     * @param objDatos
     * @param origenFuncion 
     * @return
     */
    public Respuesta insertaLog(InputLogSidra objDatos, boolean origenFuncion) {
        Respuesta objRespuesta = new Respuesta();
        Connection conn = null;
        objRespuesta = validarDatos(objDatos);
        String metodo = "insertaLog";

        if (objRespuesta == null) {
            try {
                conn = getConnRegional();
                BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
                OperacionLogSidra.insertaLog(conn, objDatos.getLog(), objDatos.getUsuario(), ID_PAIS);
                if (!origenFuncion) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_RECURSO_CREADO, null, null, null, "", objDatos.getCodArea());
                }

            } catch (SQLException e) {
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
                log.error(e.getMessage(), e);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, "", objDatos.getCodArea());
                log.error(e.getMessage(), e);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        }

        return objRespuesta;
    }
}
