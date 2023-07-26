package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.solicitud.InputSolicitud;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.solicitud.EncabezadoDeuda;
import com.consystec.sc.ca.ws.output.solicitud.OutputDeuda;
import com.consystec.sc.sv.ws.operaciones.OperacionGetDeuda;
import com.consystec.sc.sv.ws.orm.Solicitud;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlGetDeuda extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlGetDeuda.class);
    private static String servicioGet = Conf.LOG_GET_RESUMEN_DEUDA;

    private Respuesta validarInput(InputSolicitud input) {
        Respuesta resultado = null;
        String metodo = "validarInput";
        if (!(input.getFechaInicio() == null || "".equals(input.getFechaInicio().trim())) && !(input.getFechaFin() == null || "".equals(input.getFechaFin().trim()))) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(input.getFechaInicio(), formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(input.getFechaFin(), formatoFecha);

            if (fechaFin.before(fechaInicio)) {
                resultado = getMensaje(Conf_Mensajes.MSJ_FECHAFIN_MENOR_62, null, this.getClass().toString(), metodo, null, input.getCodArea());
            } else {

                long diferenciaDias = fechaFin.getTime() - fechaInicio.getTime();
                long dias = diferenciaDias / (1000 * 60 * 60 * 24);
                int totalDias = (int) dias;
                if (totalDias > 31) {
                    resultado = getMensaje(Conf_Mensajes.MSJ_RANGO_FECHA_INVALIDO_511, null, this.getClass().toString(),
                            metodo, null, input.getCodArea());

                }
            }
        } else if ((input.getFechaInicio() == null || "".equals(input.getFechaInicio().trim())) && (input.getFechaFin() == null || "".equals(input.getFechaFin().trim()))) {
            resultado = getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, this.getClass().toString(), metodo, null, input.getCodArea());
        } else if ((input.getIdDTS() == null || "".equals(input.getIdDTS().trim())) && ((input.getIdBodega()) == null || "".equals(input.getIdBodega().trim()))) {
            resultado = getMensaje(Conf_Mensajes.MSJ_DTS_AND_BODEGA_NULL, null, this.getClass().toString(), metodo, null, input.getCodArea());
        }

        return resultado;
    }

    /**
     * M\u00E9todo para obtener los posibles filtros que puede tener la consulta
     * 
     * @param objDatos
     * @return
     */
    public List<Filtro> getFiltros(InputSolicitud objDatos) {
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA);
        if (!(objDatos.getIdBodega() == null || "".equals(objDatos.getIdBodega().trim()) || "0".equals(objDatos.getIdBodega()))) {
            log.trace("entra a filtro id BODEGA VIRTUAL");
            lstFiltros.add(new Filtro("S." + Solicitud.CAMPO_TCSCBODEGAVIRTUALID, Filtro.EQ, objDatos.getIdBodega()));
        } else if ("0".equals(objDatos.getIdBodega())) {
            lstFiltros.add(new Filtro("S." + Solicitud.CAMPO_TCSCBODEGAVIRTUALID, Filtro.ISNULL, ""));
        }

        if (!(objDatos.getIdDTS() == null || "".equals(objDatos.getIdDTS().trim()))) {
            log.trace("entra a filtro BODEGA DESTINO");
            lstFiltros.add(new Filtro("s." + Solicitud.CAMPO_TCSCDTSID, Filtro.EQ, objDatos.getIdDTS()));
        }

        if (!(objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim())) && !(objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            log.trace("entra a filtro FECHAS");
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(objDatos.getFechaInicio(), formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(objDatos.getFechaFin(), formatoFecha);
            lstFiltros.add(new Filtro("TRUNC(S." + Solicitud.CAMPO_FECHA + ")", Filtro.GTE, "TO_DATE('" + FORMATO_FECHA.format(fechaInicio) + "','dd/MM/YYYY')"));
            lstFiltros.add(new Filtro("TRUNC(S." + Solicitud.CAMPO_FECHA + ")", Filtro.LTE, "TO_DATE('" + FORMATO_FECHA.format(fechaFin) + "','dd/MM/YYYY')"));
        }

        return lstFiltros;
    }

    public OutputDeuda getResumenDeuda(InputSolicitud input) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        OutputDeuda outReturn = new OutputDeuda();
        Respuesta objRespuesta = null;
        List<EncabezadoDeuda> lstDeuda = new ArrayList<EncabezadoDeuda>();
        List<Filtro> filtros = new ArrayList<Filtro>();
        objRespuesta = validarInput(input);
        String metodo = "getResumenDeuda";
        Connection conn = null;
        if (objRespuesta == null) {

            try {
                conn = getConnRegional();
                BigDecimal idPais = getIdPais(conn, input.getCodArea());

                filtros = getFiltros(input);
                SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
                Date fechaInicio = UtileriasJava.parseDate(input.getFechaInicio(), formatoFecha);
                Date fechaFin = UtileriasJava.parseDate(input.getFechaFin(), formatoFecha);
                long diferenciaDias = fechaFin.getTime() - fechaInicio.getTime();
                long dias = diferenciaDias / (1000 * 60 * 60 * 24);
                int totalDias = (int) dias;

                lstDeuda = OperacionGetDeuda.getDeuda(conn, filtros, totalDias, input.getFechaInicio(), input.getCodArea(), idPais);

                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, input.getCodArea());
                outReturn.setListaSolicitudes(lstDeuda);
                outReturn.setRespuesta(objRespuesta);

            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", input.getCodArea());

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PANEL, "", "0", Conf.LOG_TIPO_NINGUNO,
                        "Problema al obtener resumen deuda.", e.getMessage()));

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo, "", input.getCodArea());
                log.error(e.getMessage(), e);

                listaLog = new ArrayList<LogSidra>();
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar resumen deuda.", e.getMessage()));
            } finally {
                DbUtils.closeQuietly(conn);

                UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
                outReturn.setRespuesta(objRespuesta);
            }

        } else {
            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar resumen deuda.", objRespuesta.getDescripcion()));
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
            outReturn.setRespuesta(objRespuesta);
        }

        return outReturn;
    }
}
