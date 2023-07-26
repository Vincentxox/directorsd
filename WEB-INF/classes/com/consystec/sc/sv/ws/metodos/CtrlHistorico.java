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

import com.consystec.sc.ca.ws.input.inventario.InputHistoricoInv;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.inventario.Historico;
import com.consystec.sc.ca.ws.output.inventario.OutputHistorico;
import com.consystec.sc.sv.ws.operaciones.OperacionHistoricoInv;
import com.consystec.sc.sv.ws.orm.HistoricoInv;
import com.consystec.sc.sv.ws.orm.TipoTransaccionInv;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlHistorico extends ControladorBase {

    private static final Logger log = Logger.getLogger(CtrlHistorico.class);
    private static String servicioGet = Conf.LOG_GET_HISTORICO;

    /**
     * M\u00E9todo para obtener los posibles filtros que puede tener la consulta
     * 
     * @param objDatos
     * @return
     */
    public List<Filtro> getFiltros(InputHistoricoInv objDatos) {
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA);
        if (!(objDatos.getArticulo() == null || "".equals(objDatos.getArticulo().trim()))) {
            log.trace("entra a filtro ARTICULO");
            lstFiltros.add(new Filtro("H." + HistoricoInv.CAMPO_ARTICULO, Filtro.EQ, objDatos.getArticulo()));
        }

        if (!(objDatos.getBodegaOrigen() == null || "".equals(objDatos.getBodegaOrigen().trim()))) {
            log.trace("entra a filtro id BODEGA ORIGEN");
            lstFiltros.add(new Filtro("H." + HistoricoInv.CAMPO_BODEGA_ORIGEN, Filtro.EQ, objDatos.getBodegaOrigen()));
        }

        if (!(objDatos.getBodegaDestino() == null || "".equals(objDatos.getBodegaDestino().trim()))) {
            log.trace("entra a filtro BODEGA DESTINO");
            lstFiltros
                    .add(new Filtro("H." + HistoricoInv.CAMPO_BODEGA_DESTINO, Filtro.EQ, objDatos.getBodegaDestino()));
        }

        if (!(objDatos.getTipoMovimiento() == null || "".equals(objDatos.getTipoMovimiento().trim()))) {
            log.trace("entra a filtro TIPO MOVIMIENTO");
            lstFiltros.add(new Filtro("UPPER(T." + TipoTransaccionInv.CAMPO_TIPO_MOVIMIENTO + ")", Filtro.EQ,
                    "'" + objDatos.getTipoMovimiento().toUpperCase() + "'"));
        }

        if (!(objDatos.getSerie() == null || "".equals(objDatos.getSerie().trim()))) {
            log.trace("entra a filtro SERIE");
            lstFiltros.add(new Filtro("UPPER(H." + HistoricoInv.CAMPO_SERIE + ")", Filtro.EQ,
                    "'" + objDatos.getSerie().toUpperCase() + "'"));
        }
        
        if (!(objDatos.getSerie() == null || "".equals(objDatos.getSerie().trim()))
                && !(objDatos.getSerieFinal() == null || "".equals(objDatos.getSerieFinal().trim()))) {
            log.trace("entra a filtro SERIEFINAL");
            lstFiltros.add(new Filtro("UPPER(H." + HistoricoInv.CAMPO_SERIE_FINAL + ")", Filtro.EQ,
                    "'" + objDatos.getSerieFinal().toUpperCase() + "'"));
        }
        if (!(objDatos.getTipoInv() == null || "".equals(objDatos.getTipoInv().trim()))) {
            log.trace("entra a filtro TIPO INVENTARIO");
            lstFiltros.add(new Filtro("UPPER(H." + HistoricoInv.CAMPO_TIPO_INV + ")", Filtro.EQ,
                    "'" + objDatos.getTipoInv().toUpperCase() + "'"));
        }

        if (!(objDatos.getDescripcion() == null || "".equals(objDatos.getDescripcion().trim()))) {
            log.trace("entra a filtro DESCRIPCION");
            lstFiltros.add(new Filtro("UPPER(H." + HistoricoInv.CAMPO_TIPO_INV + ")", Filtro.LIKE,
                    "'%" + objDatos.getTipoInv().toUpperCase() + "%'"));
        }
        
        if (!(objDatos.getIdTraslado() == null || "".equals(objDatos.getIdTraslado().trim()))) {
            log.trace("entra a filtro idtraslado");
            lstFiltros.add(new Filtro("UPPER(H." + HistoricoInv.CAMPO_TCSCTRASLADOID + ")", Filtro.EQ,
                    objDatos.getIdTraslado().toUpperCase() ));
        }
        
        if (!(objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim()))
                && !(objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            log.trace("entra a filtro FECHAS");
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(objDatos.getFechaInicio(), formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(objDatos.getFechaFin(), formatoFecha);
            lstFiltros.add(new Filtro("TRUNC(H." + HistoricoInv.CAMPO_CREADO_EL + ")", Filtro.GTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaInicio) + "','dd/MM/YYYY')"));
            lstFiltros.add(new Filtro("TRUNC(H." + HistoricoInv.CAMPO_CREADO_EL + ")", Filtro.LTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaFin) + "','dd/MM/YYYY')"));
        }

        return lstFiltros;
    }

    /**
     * M\u00E9todo para obtener informaci\u00F3n del historico de todas las transacciones
     * de inventario realizadas en sidra.
     * 
     * @param objDatos
     * @return
     */
    public OutputHistorico getHistorico(InputHistoricoInv objDatos) {
        OutputHistorico objHistorico = new OutputHistorico();
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        Respuesta objRespuesta = null;
        List<Historico> lstHistorico = new ArrayList<Historico>();
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        String metodo = "getHistorico";
        Connection conn = null;
        // validando fechas
        if (!(objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim()))
                && !(objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(objDatos.getFechaInicio(), formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(objDatos.getFechaFin(), formatoFecha);

            if (fechaFin.before(fechaInicio)) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHAFIN_MENOR_62, null, this.getClass().toString(), metodo,
                        null, objDatos.getCodArea());
            }
        } else if (!(objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim()))
                && (objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, this.getClass().toString(), metodo,
                    null, objDatos.getCodArea());
        }

        // validando rango
        if (!(objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim()))
                && !(objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(objDatos.getFechaInicio(), formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(objDatos.getFechaFin(), formatoFecha);

            if (fechaFin.before(fechaInicio)) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHAFIN_MENOR_62, null, this.getClass().toString(), metodo,
                        null, objDatos.getCodArea());
            }
        } else if (!(objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim()))
                && (objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, this.getClass().toString(), metodo,
                    null, objDatos.getCodArea());
        }

        if (objRespuesta == null) {
            try {
                conn = getConnRegional();
                BigDecimal idPais = getIdPais(conn, objDatos.getCodArea());
                lstFiltros = getFiltros(objDatos);
                lstHistorico = OperacionHistoricoInv.getHistorico(conn, lstFiltros, objDatos.getCodArea(), idPais);
                if (lstHistorico.isEmpty()) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo,
                            null, objDatos.getCodArea());
                } else {
                    objHistorico.setHistorico(lstHistorico);
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
                }

                listaLog = new ArrayList<LogSidra>();
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Se consultaron datos del hist\u00F3rico de sidra.", ""));
            } catch (SQLException e) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
                log.error(e.getMessage(), e);

                listaLog = new ArrayList<LogSidra>();
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar hist\u00F3rico.", e.getMessage()));
            } catch (Exception e) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, "", objDatos.getCodArea());
                log.error(e.getMessage(), e);

                listaLog = new ArrayList<LogSidra>();
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar hist\u00F3rico.", e.getMessage()));
            } finally {
                DbUtils.closeQuietly(conn);

                UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
            }
        } else {
            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar hist\u00F3rico.", objRespuesta.getDescripcion()));
            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }
        objHistorico.setRespuesta(objRespuesta);

        return objHistorico;
    }
}
