package com.consystec.sc.sv.ws.metodos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.general.InputConsultaWeb;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.bogegas.BodegaSCL;
import com.consystec.sc.ca.ws.output.bogegas.OutputBodegasSCL;
import com.consystec.sc.sv.ws.operaciones.OperacionBodegaSCL;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.FSUtil;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.telefonica.globalintegration.services.getwarehouse.v1.GetWarehouseResponseType;

/**
 * @author sBarrios Consystec 2015
 */
public class CtrlBodegaSCL extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlBodegaSCL.class);
    private static String servicioGet = Conf.LOG_GET_BODEGAS_SCL;

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputConsultaWeb objDatos) {
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para obtener las bodegas activas en vantive
     */
    public OutputBodegasSCL getBodegas(InputConsultaWeb objDatos) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        List<BodegaSCL> lstBodegas = new ArrayList<BodegaSCL>();
        OutputBodegasSCL objRespuestaBodega = new OutputBodegasSCL();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getBodegas";

        try {
            log.trace("inicia a validar valores...");
            objRespuesta = validarDatos(objDatos);

            if (objRespuesta == null) {
                conn = getConnRegional();
                getIdPais(conn, objDatos.getCodArea());
                   

                // obteniendo valores a retornar
                lstBodegas = OperacionBodegaSCL.getBodegaSCL(conn, objDatos.getUsuario(), objDatos.getCodArea());
                objRespuesta = getMensaje(Conf_Mensajes.OK_GETBOD_SCL1, null, null, null, null, objDatos.getCodArea());

                objRespuestaBodega.setBodegas(lstBodegas);
                objRespuestaBodega.setRespuesta(objRespuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Se consultaron bodegas del sistema comercial.", ""));

            } else {
                log.trace("Advertencia: " + objRespuesta.getCodResultado());
                objRespuestaBodega.setRespuesta(objRespuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar bodegas del sistema comercial.",
                        objRespuesta.getDescripcion()));
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            objRespuestaBodega.setRespuesta(objRespuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar bodegas del sistema comercial.", e.getMessage()));

        } catch (com.telefonica.globalintegration.services.soap.getwarehouse.v1.MessageFault e) {
           log.error(e);
           objRespuesta = new FSUtil().getMensajeErrorOSB(e.getFaultInfo(), GetWarehouseResponseType.class, metodo, objDatos.getCodArea()); 
           objRespuestaBodega.setRespuesta(objRespuesta);
           } 
        catch (Exception e) {
            log.error(e.getMessage(), e);
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, Conf_Mensajes.MENSAJE_DEFAULT_TXT,
                    this.getClass().toString(), metodo, e.getMessage(), objDatos.getCodArea());
            objRespuestaBodega.setRespuesta(objRespuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar bodegas del sistema comercial.", e.getMessage()));
        } finally {
            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
            DbUtils.closeQuietly(conn);
        }

        return objRespuestaBodega;
    }
}
