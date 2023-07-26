package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.jornada.InputJornada;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.jornada.OutputJornada;
import com.consystec.sc.sv.ws.operaciones.OperacionJornada;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class CtrlJornada extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlJornada.class);
    List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioPostFecha = Conf.LOG_POST_FECHA_JORNADA;
    private static String servicioGetFecha = Conf.LOG_GET_FECHA_JORNADA;
    private static String servicioPut = Conf.LOG_PUT_JORNADA;

    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDe como recurso al servicio.
     * 
     * @param conn
     * @param input
     * @param tipoLiquidacion
     * @param tipoRechazo
     * @return
     */
    public Respuesta validarInput(Connection conn, InputJornada input, String tipoLiquidacion, String tipoRechazo) {
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

        if (input.getIdVendedor() == null || "".equals(input.getIdVendedor().trim()) || !isNumeric(input.getIdVendedor())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_137, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getIdJornada() == null || "".equals(input.getIdJornada().trim()) || !isNumeric(input.getIdJornada())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDJORNADA_259, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getTipoOperacion() == null || "".equals(input.getTipoOperacion().trim())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPOJORNADA_NULO_168, null, nombreClase,
                    nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        } else if (!input.getTipoOperacion().equalsIgnoreCase(tipoRechazo)
                && !input.getTipoOperacion().equalsIgnoreCase(tipoLiquidacion)) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPOJORNADA_169, null, nombreClase, nombreMetodo,
                    tipoRechazo + " o " + tipoLiquidacion + ".", input.getCodArea()).getDescripcion();
            flag = true;
        }

        if ((input.getTipoOperacion() != null && !input.getTipoOperacion().equalsIgnoreCase(tipoLiquidacion)) &&  (input.getObservaciones() == null || "".equals(input.getObservaciones().trim()))) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_OBSERVACIONES_LIQ_386, null, nombreClase,
                        nombreMetodo, "", input.getCodArea()).getDescripcion();
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
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     * @param metodo
     * @return respuestaJornada
     */
    public OutputJornada getDatos(InputJornada input) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta respuesta = null;
        OutputJornada output = null;
        String tipoLiquidacion = "";
        String tipoRechazo = "";
        String estadoFinalizada = "";
        String estadoRechazada = "";
        String estadoLiquidada = "";
        Connection conn = null;
        
        try {
            conn = getConnRegional();
            BigDecimal idPais = getIdPais(conn, input.getCodArea());

            String campos[] = { Catalogo.CAMPO_NOMBRE, Catalogo.CAMPO_VALOR };

            // Se obtienen todas las configuraciones.
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_JORNADA_TIPOS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_JORNADA_ESTADOS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_JORNADA_ESTADOS_LIQ));

            List<Map<String, String>> datosConfig = new ArrayList<Map<String, String>>();
            datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, null);

            for (int i = 0; i < datosConfig.size(); i++) {
                if (Conf.JORNADA_TIPO_LIQUIDACION.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoLiquidacion = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.JORNADA_TIPO_RECHAZO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoRechazo = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.JORNADA_ESTADO_FINALIZADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoFinalizada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.JORNADA_ESTADO_RECHAZADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoRechazada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.JORNADA_ESTADO_LIQUIDADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoLiquidada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
            }

            // Validaci\u00F3n de datos en el input
            respuesta = validarInput(conn, input, tipoLiquidacion, tipoRechazo);

            log.trace("Respuesta validaci\u00F3n: " + respuesta.getDescripcion());
            if (!"OK".equals(respuesta.getDescripcion())) {
                output = new OutputJornada();
                output.setRespuesta(respuesta);

                listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_JORNADA, servicioPut, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));

                return output;
            }

            output = OperacionJornada.doPut(conn, input, listaLog, Conf.LOG_TRANSACCION_MOD_JORNADA, servicioPut,
                    tipoLiquidacion, tipoRechazo, estadoFinalizada, estadoRechazada, estadoLiquidada, input.getCodArea(), idPais);

            if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_MOD_JORNADA_46) {
                listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_JORNADA, servicioPut, input.getIdJornada(),
                        Conf.LOG_TIPO_JORNADA, "Se modificaron datos de la jornada ID " + input.getIdJornada() + ".",
                        ""));
            } else {
                listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_JORNADA, servicioPut, input.getIdJornada(),
                        Conf.LOG_TIPO_JORNADA, "Problema al modificar datos de la jornada.",
                        output.getRespuesta().getDescripcion()));
            }

        } catch (SQLException e) {
            respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputJornada();
            output.setRespuesta(respuesta);

            listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_JORNADA, servicioPut, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al modificar jornada.", e.getMessage()));

        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputJornada();
            output.setRespuesta(respuesta);

            listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_JORNADA, servicioPut, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de jornadas.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }

    public Respuesta validarDatosHoraJornada(Connection conn, InputJornada input, String estadoAlta, int metodo, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        SimpleDateFormat FORMATO_FECHA_GT = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA_GT);
        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;

        log.debug("Validando datos...");

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (input.getIdVendedor() == null || "".equals(input.getIdVendedor().trim()) || !isNumeric(input.getIdVendedor())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_137, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        } else {
            if (metodo == Conf.METODO_POST) {
                String tipoPanel = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_PANEL, input.getCodArea());
                // validar que el vendedor sea responsable
                if (!OperacionJornada.validarResponsable(conn, input.getIdVendedor(), estadoAlta, tipoPanel, idPais)) {
                    return getMensaje(Conf_Mensajes.MSJ_ERROR_VEND_RESPONSABLE_848, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                }
            }
        }

        if (metodo == Conf.METODO_POST) {
            try {
                if (input.getFecha() == null || "".equals(input.getFecha().trim())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_314, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                } else {
                    Date fechaJornada = FORMATO_FECHA_GT.parse(input.getFecha());
                    Date fechaActual = FORMATO_FECHA_GT.parse(FORMATO_FECHA_GT.format(new Date()));

                    if (fechaJornada.before(fechaActual)) {
                        datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_ERROR_HORA_JORNADA_196, null, nombreClase,
                                nombreMetodo, null, input.getCodArea()).getDescripcion();
                        flag = true;
                    }
                }
            } catch (java.text.ParseException e) {
                log.error("Problema al convertir la fecha en clase " + nombreMetodo + " m\u00E9todo " + nombreClase + ".", e);
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_CONVERTIR_FECHA_165, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }
        }

        if (flag ) {
            r = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo, datosErroneos, input.getCodArea());
        } else {
            r.setDescripcion("OK");
        }
        return r;
    }

    public OutputJornada doFechaJornada(InputJornada input, int metodo) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "asignaFechaJornada";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta respuesta = null;
        OutputJornada output = null;
        Connection conn = null;

        log.trace("Usuario: " + input.getUsuario());

        try {
            conn = getConnRegional();
            BigDecimal idPais = getIdPais(conn, input.getCodArea());
            conn.setAutoCommit(false);

            // Se obtienen las configuraciones.
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            respuesta = validarDatosHoraJornada(conn, input, estadoAlta, metodo, idPais);

            log.trace("Respuesta validaci\u00F3n: " + respuesta.getDescripcion());
            if (!"OK".equals(respuesta.getDescripcion())) {
                output = new OutputJornada();
                output.setRespuesta(respuesta);

                listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_FECHA_JORNADA, servicioPostFecha, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));

                return output;
            }

            if (metodo == Conf.METODO_POST) {
                output = OperacionJornada.asignaFechaJornada(conn, input, input.getCodArea(), idPais);

                if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_FECHA_JORNADA_71) {
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_FECHA_JORNADA, servicioPostFecha,
                            input.getIdVendedor(), Conf.LOG_TIPO_VENDEDOR,
                            "Se registr\u00F3 la fecha de cierre de jornada del vendedor ID " + input.getIdVendedor() + ".",
                            ""));
                    conn.commit();
                }

            } else if (metodo == Conf.METODO_GET) {
                output = OperacionJornada.getFechaJornada(conn, input, idPais);

                if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_GET_FECHA_JORNADA_72) {
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGetFecha, input.getIdVendedor(),
                            Conf.LOG_TIPO_VENDEDOR,
                            "Se consult\u00F3 la fecha de cierre de jornada del vendedor ID " + input.getIdVendedor() + ".",
                            ""));
                }
            }

            if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() <= 0) {
                log.debug("Rollback");
                conn.rollback();

                listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_FECHA_JORNADA, servicioPostFecha, input.getIdVendedor(),
                        Conf.LOG_TIPO_VENDEDOR, "Problema al crear/consultar datos de fecha de jornada del vendedor ID "
                                + input.getIdVendedor() + ".",
                        output.getRespuesta().getDescripcion()));
            }

            conn.setAutoCommit(true);

        } catch (SQLException e) {
            respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputJornada();
            output.setRespuesta(respuesta);

            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_FECHA_JORNADA, servicioPostFecha, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al modificar jornada.", e.getMessage()));

        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputJornada();
            output.setRespuesta(respuesta);

            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_FECHA_JORNADA, servicioPostFecha, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de jornadas.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
