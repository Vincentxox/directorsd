package com.consystec.sc.sv.ws.metodos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.folio.InputFolioVirtual;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.folio.OutputConfiguracionFolioVirtual;
import com.consystec.sc.sv.ws.operaciones.OperacionFoliosFS;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class CtrlFoliosFS extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlFoliosFS.class);
    private static String servicioGet = Conf.LOG_GET_FOLIOS_FS;
    private static String servicioPost = Conf.LOG_POST_FOLIOS_FS;

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
     */
    public Respuesta validarInput(Connection conn, InputFolioVirtual input, int metodo) {
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

        if (input.getCodDispositivo() == null || "".equals(input.getCodDispositivo())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_CODDISPOSITIVO_163, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (metodo == Conf.METODO_POST && (input.getIdRangoFolio() == null || "".equals(input.getIdRangoFolio())
                    || !isNumeric(input.getIdRangoFolio()))) {
                r = getMensaje(Conf_Mensajes.MSJ_ERROR_IDRANGO_FOLIO_385, null, nombreClase, nombreMetodo, null, input.getCodArea());
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
    public OutputConfiguracionFolioVirtual getDatos(InputFolioVirtual input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = new Respuesta();
        OutputConfiguracionFolioVirtual output = new OutputConfiguracionFolioVirtual();

        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();
           getIdPais(conn, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            respuesta = validarInput(conn, input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + respuesta.getDescripcion());
            if (!"OK".equals(respuesta.getDescripcion())) {
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));

                return output;
            }

            if (metodo == Conf.METODO_GET) {
                log.debug(nombreClase + " -> GET: Env\u00EDo de datos a m\u00E9todo desde REST");
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionFoliosFS.doGet(input.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_PDV, "Se consultaron folios de Full Stack para el dispositivo c\u00F3digo "
                                    + input.getCodDispositivo() + ".",
                            ""));
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
                    log.error("Excepcion: " + e.getMessage(), e);

                    output.setRespuesta(respuesta);
                    

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar n\u00FAmero de recarga.", e.getMessage()));
                }
            } else if (metodo == Conf.METODO_POST) {
                log.debug(nombreClase + " -> POST: Env\u00EDo de datos a m\u00E9todo desde REST");
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionFoliosFS.doPost(input.getCodArea());

                    listaLog.add(
                            ControladorBase
                                    .addLog(Conf.LOG_TRANSACCION_RESERVA_FOLIO_FS, servicioPost, "0",
                                            Conf.LOG_TIPO_FOLIO,
                                            "Se reserv\u00F3 el rango de folios de Full Stack ID " + input.getIdRangoFolio()
                                                    + " para el dispositivo c\u00F3digo " + input.getCodDispositivo() + ".",
                                            ""));
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
                    log.error("Excepcion: " + e.getMessage(), e);

                    output.setRespuesta(respuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar n\u00FAmero de recarga.", e.getMessage()));
                }
            }

        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
            log.error("Excepcion: " + e.getMessage(), e);

            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Inconvenientes en el servicio folios FS.",
                    e.getMessage()));

        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
