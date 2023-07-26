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
import com.consystec.sc.ca.ws.input.reporte.InputReportePDV;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.reporte.OutputReportePDV;
import com.consystec.sc.sv.ws.operaciones.OperacionReportePDV;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class CtrlReportePDV extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlReportePDV.class);
    private static String servicioGet = Conf.LOG_GET_REPORTE_PDV;

    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al
     * servicio.
     * 
     * @return respuesta Objeto con la respuesta en caso de error o nulo si todo
     *         esta correcto.
     * @throws SQLException
     */
    public Respuesta validarInput(Connection conn, InputReportePDV input) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        log.debug("Validando datos...");

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            return getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        if (input.getIdDistribuidor() != null && !"".equals(input.getIdDistribuidor())
                && !isNumeric(input.getIdDistribuidor())) {
            return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        if (input.getIdPDV() != null && !"".equals(input.getIdPDV()) && !isNumeric(input.getIdPDV())) {
            return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDPDV_NUM_315, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        if (input.getIdVendedor() != null && !"".equals(input.getIdVendedor()) && !isNumeric(input.getIdVendedor())) {
            return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        if (input.getIdRuta() != null && !"".equals(input.getIdRuta()) && !isNumeric(input.getIdRuta())) {
            return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDRUTA_NUM_251, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_INPUT);
        Date fechaDesde = null;
        Date fechaHasta = null;
        if (input.getFechaDesde() == null || "".equals(input.getFechaDesde().trim())) {
            return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_DESDE_229, null, nombreClase, nombreMetodo, null, input.getCodArea());
        } else {
            fechaDesde = UtileriasJava.parseDate(input.getFechaDesde(), formatoFecha);
        }
        if (input.getFechaHasta() == null || "".equals(input.getFechaHasta().trim())) {
            return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_HASTA_230, null, nombreClase, nombreMetodo, null, input.getCodArea());
        } else {
            fechaHasta = UtileriasJava.parseDate(input.getFechaHasta(), formatoFecha);
        }

        if (fechaDesde != null && fechaHasta != null) {
            if (fechaDesde.compareTo(fechaHasta) > 0) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_DESDE_MENOR_232, null, nombreClase, nombreMetodo,
                        null, input.getCodArea());
            } else {
                long diff = fechaHasta.getTime() - fechaDesde.getTime();

            
                long diffDays = diff / (24 * 60 * 60 * 1000);

                log.trace("Dias de consulta " + diffDays);
                if (diffDays > 186) {
                    return getMensaje(Conf_Mensajes.MSJ_MAXIMO_TIEMPO_CONSULTA_769, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                }
            }
        }

        return new Respuesta();
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes
     * consultas seg\u00FAn el m\u00E9todo que se trabaje.
     * 
     * @param input
     *            Objeto con los datos enviados mediante POST.
     * @param metodo
     *            Valor que indica tipo de operaci\u00F3n que se desea realizar en el
     *            servicio.
     * @return condiciones Listado con los filtros indicados seg\u00FAn el m\u00E9todo
     *         deseado.
     */
    public static List<Filtro> obtenerCondiciones(InputReportePDV input, BigDecimal ID_PAIS) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (input.getIdPDV() != null && !"".equals(input.getIdPDV().trim())) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                    "PV." + PuntoVenta.CAMPO_TCSCPUNTOVENTAID, input.getIdPDV()));
        }

        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                "PV." + PuntoVenta.CAMPO_TCSCCATPAISID, ID_PAIS.toString())); //Filtrar siempre por pais
        
        if (input.getIdDistribuidor() != null && !"".equals(input.getIdDistribuidor().trim())) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, "PV." + PuntoVenta.CAMPO_TCSCDTSID,
                    input.getIdDistribuidor()));
        }

        if (input.getIdVendedor() != null && !input.getIdVendedor().trim().equals("")) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, "R." + Ruta.CAMPO_SEC_USUARIO_ID,
                    input.getIdVendedor()));
        }

        if (input.getIdRuta() != null && !input.getIdRuta().trim().equals("")) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, "R." + Ruta.CAMPO_TC_SC_RUTA_ID,
                    input.getIdRuta()));
        }

        if (input.getDepartamento() != null && !input.getDepartamento().trim().equals("")) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
                    "PV." + PuntoVenta.CAMPO_DEPARTAMENTO, input.getDepartamento()));
        }

        if (input.getMunicipio() != null && !input.getMunicipio().trim().equals("")) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, "PV." + PuntoVenta.CAMPO_MUNICIPIO,
                    input.getMunicipio()));
        }

        if (input.getDistrito() != null && !input.getDistrito().trim().equals("")) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, "PV." + PuntoVenta.CAMPO_DISTRITO,
                    input.getDistrito()));
        }

        if (input.getCategoria() != null && !input.getCategoria().trim().equals("")) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, "PV." + PuntoVenta.CAMPO_CATEGORIA,
                    input.getCategoria()));
        }

        if (input.getEstado() != null && !input.getEstado().trim().equals("")) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, "PV." + PuntoVenta.CAMPO_ESTADO,
                    input.getEstado()));
        }

        return condiciones;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     *            Objeto con los datos enviados mediante POST.
     * @param metodo
     *            M\u00E9todo que indica las acciones a realizar.
     * @return output Respuesta y listado con los ReportePDVs encontrados.
     */
    public OutputReportePDV getDatos(InputReportePDV input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        OutputReportePDV output = new OutputReportePDV();
        Respuesta respuesta = new Respuesta();

        Connection conn = null;
        try {
            conn = getConnRegional();
            log.trace("Usuario: " + input.getUsuario());
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());
            // Validación de datos en el input
            respuesta = validarInput(conn, input);
            log.trace("Respuesta validaci\u00F3n: "
                    + (respuesta.getDescripcion() == null ? "OK" : respuesta.getDescripcion()));
            if (respuesta.getCodResultado()!=null) {
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));

                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            try {
                output = OperacionReportePDV.doGet(conn, input, ID_PAIS);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de ReportePDVs.", ""));
            } catch (SQLException e) {
                respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                log.error("Excepcion: " + e.getMessage(), e);
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de ReportePDVs.", e.getMessage()));
            }

        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de ReportePDVs.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
