package com.consystec.sc.sv.ws.metodos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.consultas.InputConsultaNumRecarga;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.consultas.OutputConsultaNumRecarga;
import com.consystec.sc.sv.ws.operaciones.OperacionConsultaNumRecarga;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class CtrlConsultaNumRecarga extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlConsultaNumRecarga.class);
    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_CONSULTA_NUM_RECARGA;
    String COD_PAIS="";

    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al
     * servicio.
     * 
     * @param conn
     * @param input
     *            Objeto con los datos enviados mediante POST.
     * @param metodo
     *            Valor que indica tipo de operaci\u00F3n que se desea realizar en el
     *            servicio.
     * @return Respuesta
     * @throws SQLException 
     * @throws NumberFormatException 
     */
    public Respuesta validarInput(Connection conn, InputConsultaNumRecarga input, int metodo) throws NumberFormatException, SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;

        log.debug("Validando datos...");

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (input.getIdPDV() == null || "".equals(input.getIdPDV()) || !isNumeric(input.getIdPDV())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDPDV_NUM_315, null,nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (input.getNumRecarga() == null ||"".equals( input.getNumRecarga()) || !isNumeric(input.getNumRecarga())
                || input.getNumRecarga().length() != new Integer(
                        UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.LONGITUD_TELEFONO, input.getCodArea()))) {
            r = getMensaje(Conf_Mensajes.MSJ_LONG_NUM_INVALIDA_504, null,nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (flag) {
            r = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo, datosErroneos, input.getCodArea());
        } else {
            r.setDescripcion("OK");
            r.setCodResultado("1");
            r.setMostrar("0");
        }
        return r;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST.
     * 
     * @param input
     *            Objeto con los datos enviados mediante POST.
     * @param metodo
     * @return output Respuesta y listado con los Descuentos encontrados.
     */
    public OutputConsultaNumRecarga getDatos(InputConsultaNumRecarga input, int metodo) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = new Respuesta();
        OutputConsultaNumRecarga output = new OutputConsultaNumRecarga();

        Connection conn = null;
        try {
            conn = getConnRegional();
            log.trace("Usuario: " + input.getUsuario());

            // Validaci\u00F3n de datos en el input
            respuesta = validarInput(conn, input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + respuesta.getDescripcion());
            if (!"OK".equalsIgnoreCase(respuesta.getDescripcion())) {
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));

                return output;
            }

            if (metodo == Conf.METODO_GET) {
                log.debug(nombreClase + " -> GET: Env\u00EDo de datos a m\u00E9todo desde REST");
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionConsultaNumRecarga.doGet(conn, input);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet,
                            input.getIdPDV(), Conf.LOG_TIPO_PDV,
                            "Se consult\u00F3 n\u00FAmero de recarga " + input.getNumRecarga() + ".", ""));
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(),nombreClase, nombreMetodo, null, input.getCodArea());
                    log.error("Excepcion: " + e.getMessage(), e);

                    output.setRespuesta(respuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar n\u00FAmero de recarga.", e.getMessage()));
                }
            }

        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(),nombreClase, nombreMetodo, null, input.getCodArea());
            log.error("Excepcion: " + e.getMessage(), e);

            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Inconvenientes en el servicio de consulta de n\u00FAmero de recarga.",
                    e.getMessage()));

        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
