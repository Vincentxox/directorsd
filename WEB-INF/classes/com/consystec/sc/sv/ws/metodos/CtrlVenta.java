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
import com.consystec.sc.ca.ws.input.venta.InputGetDetalle;
import com.consystec.sc.ca.ws.input.venta.InputGetVenta;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.venta.OutputArticuloVenta;
import com.consystec.sc.ca.ws.output.venta.OutputVenta;
import com.consystec.sc.sv.ws.operaciones.OperacionVentasCopia;
import com.consystec.sc.sv.ws.orm.VentaDet;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlVenta extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlVenta.class);
    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    /***
     * M\u00E9todo para obtener informaci\u00F3n de ventas
     * @throws SQLException 
     */
    public OutputVenta getVenta(InputGetVenta objDato) {
        String nombreMetodo = "getVenta";
        String nombreClase = new CurrentClassGetter().getClassName();
        String servicioGet = Conf.LOG_GET_VENTA;
        listaLog = new ArrayList<LogSidra>();

        OutputVenta objRespuestaVenta = new OutputVenta();
        Respuesta objRespuesta = null;
        OutputVenta ventas = null;
        Connection conn = null;
        COD_PAIS = objDato.getCodArea();

        try {
            conn = getConnRegional();
            ID_PAIS = getIdPais(conn, objDato.getCodArea());
            String longMaxNumTelefono = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.LONGITUD_TELEFONO, objDato.getCodArea());

            if (objDato.getIdVenta() != null && !isNumeric(objDato.getIdVenta())) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENTA_360, null, nombreClase, nombreMetodo,
                        null, objDato.getCodArea());
            }

            if (objDato.getIdJornada() != null && !isNumeric(objDato.getIdJornada())) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDJORNADA_NUM_761, null, nombreClase,
                        nombreMetodo, null, objDato.getCodArea());
            }

            if (objDato.getIdVendedor() != null && !isNumeric(objDato.getIdVendedor())) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase,
                        nombreMetodo, null, objDato.getCodArea());
            }

            if (objDato.getIdDTS() == null || objDato.getIdDTS().equals("") || !isNumeric(objDato.getIdDTS())) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo,
                        null, objDato.getCodArea());
            }

            if (objDato.getIdRutaPanel() != null && !isNumeric(objDato.getIdRutaPanel())) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ITTIPO_RUTAPANEL_NUM_359, null, nombreClase,
                        nombreMetodo, null, objDato.getCodArea());
            } else if (objDato.getIdRutaPanel() != null && !objDato.getIdRutaPanel().equals("")) {
                if (objDato.getTipoRutaPanel() == null || objDato.getTipoRutaPanel().equals("")) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPO_NULO_431, null, nombreClase, nombreMetodo, null, objDato.getCodArea());
                } else {
                    String tipoPanelRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, objDato.getTipoRutaPanel(), objDato.getCodArea());

                    if (tipoPanelRuta == null || tipoPanelRuta.equals("")) {
                        objRespuesta = new Respuesta();
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_TIPOOFERTA_NO_DEFINIDO_424, null, nombreClase,
                                nombreMetodo, null, objDato.getCodArea());
                    }
                }
            }

            if (objDato.getIdTipo() != null && !isNumeric(objDato.getIdTipo())) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDTIPO_NUM_167, null, nombreClase, nombreMetodo,
                        null, objDato.getCodArea());
            }

            if (objDato.getFolio() != null && !isNumeric(objDato.getFolio())) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FOLIO_NUM_379, null, nombreClase, nombreMetodo,
                        null, objDato.getCodArea());
            }

            if (objDato.getFolioSidra() != null && !isNumeric(objDato.getFolioSidra())) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FOLIO_NUM_379, null, nombreClase, nombreMetodo,
                        null, objDato.getCodArea());
            }

            if (objDato.getNumTelefono() != null && !objDato.getNumTelefono().equals("")) {
                if (!isNumeric(objDato.getNumTelefono())) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NUMTELEFONO_NUM_380, null, nombreClase,
                            nombreMetodo, null, objDato.getCodArea());
                } else {
                    if (objDato.getNumTelefono().length() != new Integer(longMaxNumTelefono)) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONG_NUM_INVALIDA_504, null, nombreClase,
                                nombreMetodo, null, objDato.getCodArea());
                    }
                }
            }

            // validando fechas
            if (!(objDato.getFechaInicio() == null || "".equals(objDato.getFechaInicio().trim()))
                    && !(objDato.getFechaFin() == null || "".equals(objDato.getFechaFin().trim()))) {
                SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
                Date fechaInicio = UtileriasJava.parseDate(objDato.getFechaInicio(), formatoFecha);
                Date fechaFin = UtileriasJava.parseDate(objDato.getFechaFin(), formatoFecha);

                if (fechaFin.before(fechaInicio)) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHAFIN_MENOR_62, null, nombreClase, nombreMetodo,
                            null, objDato.getCodArea());
                }
            } else if (!(objDato.getFechaInicio() == null || "".equals(objDato.getFechaInicio().trim()))
                    && (objDato.getFechaFin() == null || "".equals(objDato.getFechaFin().trim()))) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, nombreClase, nombreMetodo, null, objDato.getCodArea());
            }

            if (objRespuesta == null) {
                ventas = OperacionVentasCopia.doGet(conn, objDato, ID_PAIS);

                objRespuesta = new Respuesta();
                objRespuesta = ventas.getRespuesta();

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de ventas.", ""));
            } else {
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
            }
        } catch (SQLException e) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, "", objDato.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de consulta de ventas.", e.getMessage()));

        } catch (Exception e) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, "", objDato.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de consulta de ventas.", e.getMessage()));

        } finally {
            DbUtils.closeQuietly(conn);

            objRespuestaVenta.setRespuesta(objRespuesta);
            if (ventas != null) {
                objRespuestaVenta.setVenta(ventas.getVenta());
            }

            UtileriasJava.doInsertLog(listaLog, objDato.getUsuario(), objDato.getCodArea());
        }

        return objRespuestaVenta;
    }

    public OutputArticuloVenta getDetalleVenta(InputGetDetalle objDatos) {
        String nombreMetodo = "getDetalleVenta";
        String nombreClase = new CurrentClassGetter().getClassName();
        String servicioGet = Conf.LOG_GET_VENTA_DET;
        listaLog = new ArrayList<LogSidra>();
        OutputArticuloVenta objRespuestaDetalleVenta = new OutputArticuloVenta();
        Respuesta objRespuesta = null;
        COD_PAIS = objDatos.getCodArea();
        OutputArticuloVenta ventasDetalle = null;
        List<Filtro> lstFiltros = new ArrayList<Filtro>();

        Connection conn = null;
        try {
            conn = getConnRegional();
           ID_PAIS = getIdPais(conn, objDatos.getCodArea());
            String registrosMaximos = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.REGISTROS_MAXIMOS, objDatos.getCodArea());

            if (objDatos.getIdVenta() == null || objDatos.getIdVenta().equals("")
                    || !isNumeric(objDatos.getIdVenta())) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENTA_360, null, nombreClase, nombreMetodo,
                        null, objDatos.getCodArea());
            }

            if (objDatos.getMin() != null && !isNumeric(objDatos.getMin())) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MINIMO_198, null, nombreClase, nombreMetodo,
                        null, objDatos.getCodArea());
            }

            if (objDatos.getMax() != null && !isNumeric(objDatos.getMax())) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MAXIMO_217, null, nombreClase, nombreMetodo,
                        null, objDatos.getCodArea());
            }

            if (objDatos.getMin() != null && !objDatos.getMin().equals("") && objDatos.getMax() != null
                    && !objDatos.getMax().equals("") && objRespuesta == null) {
                if (new Integer(objDatos.getMin()) > new Integer(objDatos.getMax())) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MINIMO_MAYOR_218, null, nombreClase,
                            nombreMetodo, null, objDatos.getCodArea());
                } else if ((new Integer(objDatos.getMax()) - new Integer(objDatos.getMin())) > new Integer(
                        registrosMaximos)) {

                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_RANGO_MAX_219, null, nombreClase,
                            nombreMetodo, registrosMaximos, objDatos.getCodArea());
                }
            }

            if (objRespuesta == null) {
                lstFiltros = getFiltrosDetalle(objDatos);
                ventasDetalle = OperacionVentasCopia.getDetalleVenta(conn, lstFiltros, objDatos, ID_PAIS, objDatos.getCodArea());
                objRespuesta = ventasDetalle.getRespuesta();
                if (ventasDetalle.getDetalleVenta() != null) {
                    objRespuestaDetalleVenta.setDetalleVenta(ventasDetalle.getDetalleVenta());
                }

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO,
                        "Se consult\u00F3 el detalle de la venta con ID " + objDatos.getIdVenta() + ".", ""));

            } else {
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
            }
        } catch (SQLException e) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de consulta detalle de ventas.", e.getMessage()));

        } catch (Exception e) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de consulta detalle de ventas.", e.getMessage()));

        } finally {
            objRespuestaDetalleVenta.setRespuesta(objRespuesta);
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }

        return objRespuestaDetalleVenta;
    }

    public List<Filtro> getFiltrosDetalle(InputGetDetalle objDatos) {
        List<Filtro> lstFiltros = new ArrayList<Filtro>();

        if (!(objDatos.getIdVenta() == null || "".equals(objDatos.getIdVenta().trim()))) {
            log.trace("entra a filtro ID VENTA");
            lstFiltros.add(new Filtro(VentaDet.CAMPO_TCSCVENTAID, Filtro.EQ, objDatos.getIdVenta()));
        }

        return lstFiltros;
    }
}
