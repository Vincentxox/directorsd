package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCantInvJornada;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCantInvJornada;
import com.consystec.sc.sv.ws.operaciones.OperacionReporteCantInvJornada;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class CtrlReporteCantInvJornada extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlReporteCantInvJornada.class);
    private static String servicioGet = Conf.LOG_GET_REPORTE_CANT_VENDIDA;

    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al
     * servicio.
     * 
     * @return respuesta Objeto con la respuesta en caso de error o nulo si todo
     *         esta correcto.
     * @throws SQLException
     */
    public Respuesta validarInput(Connection conn, InputReporteCantInvJornada input) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        log.debug("Validando datos...");

        if (input.getUsuario() == null || input.getUsuario().trim().equals("")) {
            return getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        if (input.getToken().equals("WEB")) {
            // Obligatorio el dts en web
            if (input.getIdDTS() == null || input.getIdDTS().trim().equals("")) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_161, null, nombreClase, nombreMetodo, null, input.getCodArea());
            } else if (!isNumeric(input.getIdDTS())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }

        } else {
            // Obligatoria la jornada en movil
            if (input.getIdJornada() == null || input.getIdJornada().trim().equals("")) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDJORNADA_259, null, nombreClase, nombreMetodo, null, input.getCodArea());
            } else if (!isNumeric(input.getIdJornada())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDJORNADA_NUM_761, null, nombreClase, nombreMetodo,
                        null, input.getCodArea());
            }
        }

        if (input.getIdVendedor() != null && !input.getIdVendedor().equals("") && !isNumeric(input.getIdVendedor())) {
            return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        if (input.getIdRutaPanel() != null && !isNumeric(input.getIdRutaPanel())) {
            return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ITTIPO_RUTAPANEL_NUM_359, null, nombreClase, nombreMetodo,
                    null, input.getCodArea());
        } else if (input.getIdRutaPanel() != null && !input.getIdRutaPanel().equals("")) {
            if (input.getTipoRutaPanel() == null || input.getTipoRutaPanel().equals("")) {
                return getMensaje(Conf_Mensajes.MSJ_TIPO_NULO_431, null, nombreClase, nombreMetodo, null, input.getCodArea());
            } else {
                String tipoPanelRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA,
                        input.getTipoRutaPanel().toUpperCase(), input.getCodArea());

                if (tipoPanelRuta == null || tipoPanelRuta.equals("")) {
                    return getMensaje(Conf_Mensajes.MSJ_ERROR_TIPOOFERTA_NO_DEFINIDO_424, null, nombreClase,
                            nombreMetodo, null, input.getCodArea());
                }
            }
        }

        if (input.getIdArticulo() != null && !input.getIdArticulo().equals("") && !isNumeric(input.getIdArticulo())) {
            return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDARTICULO_NUM_131, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        return new Respuesta();
    }

    public static String getIdJornada(Connection conn, InputReporteCantInvJornada input, String estadoIniciada)
            throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        String idJornada = "";

        if (input.getIdJornada() != null && !input.getIdJornada().equals("")) {
            return input.getIdJornada();
        }

        if (input.getIdVendedor() != null && !input.getIdVendedor().equals("")) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_VENDEDOR, input.getIdVendedor()));
        }
        
        if (input.getIdRutaPanel() != null && !input.getIdRutaPanel().equals("")) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_IDTIPO, input.getIdRutaPanel()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Jornada.CAMPO_DESCRIPCION_TIPO, input.getTipoRutaPanel()));
        }
        
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Jornada.CAMPO_ESTADO, estadoIniciada));

        idJornada = UtileriasBD.getOneRecord(conn, Jornada.CAMPO_TCSCJORNADAVENID, Jornada.N_TABLA, condiciones);

        return idJornada;
    }
    
    public static String getCondicionDetalle(InputReporteCantInvJornada input) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (input.getIdArticulo() != null && !input.getIdArticulo().equals("")) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, "ARTICULO", input.getIdArticulo()));
        }
        if (input.getTipoGrupo() != null && !input.getTipoGrupo().equals("")) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, "TIPO_GRUPO_SIDRA", input.getTipoGrupo()));
        }
        if (input.getTipoInv() != null && !input.getTipoInv().equals("")) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, "TIPO_INV", input.getTipoInv()));
        }

        return UtileriasBD.getCondiciones(condiciones);
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     *            Objeto con los datos enviados mediante POST.
     * @param metodo
     *            M\u00E9todo que indica las acciones a realizar.
     * @return output Respuesta y listado con los ReporteCantVendidas encontrados.
     */
    public OutputReporteCantInvJornada getDatos(InputReporteCantInvJornada input) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        OutputReporteCantInvJornada output = new OutputReporteCantInvJornada();
        Respuesta respuesta = new Respuesta();

        Connection conn = null;
        try {
            conn = getConnRegional();
            log.trace("Usuario: " + input.getUsuario());
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            respuesta = validarInput(conn, input);
            log.trace("Respuesta validaci\u00F3n: " + (respuesta.getDescripcion() == null ? "OK" : respuesta.getDescripcion()));
            if (respuesta.getCodResultado()!=null) {
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));

                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            output = OperacionReporteCantInvJornada.doGet(conn, input, false, ID_PAIS);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Se consultaron datos del reporte de inventario por jornada.", ""));

        } catch (SQLException e) {
            respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO,
                    "Problema al consultar datos del reporte de inventario por jornada.", e.getMessage()));

        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio del reporte de inventario por jornada.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
