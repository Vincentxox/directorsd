package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCtaDTS;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCtaDTS;
import com.consystec.sc.sv.ws.operaciones.OperacionReporteCtaDTS;
import com.consystec.sc.sv.ws.orm.CuentaHaberDTS;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2017
 *
 */
public class CtrlReporteCtaDTS extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlReporteCtaDTS.class);

    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al
     * servicio.
     * @param tipo 
     * 
     * @return respuesta Objeto con la respuesta en caso de error o nulo si todo
     *         esta correcto.
     * @throws SQLException
     */
    public Respuesta validarInput(Connection conn, InputReporteCtaDTS input, int tipo) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        log.debug("Validando datos...");

        if (input.getUsuario() == null || input.getUsuario().trim().equals("")) {
            return getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        if (tipo != 3) {
            // validaciones al consultar cualquiera
            if (input.getIdDTS() == null || input.getIdDTS().equals("") || !isNumeric(input.getIdDTS())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }
            
            if (input.getFecha() == null || input.getFecha().trim().equals("")) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_314, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }
        } else {
            for (int i = 0; i < input.getDatos().size(); i++) {
                // validaciones al insertar
                if (input.getDatos().get(i).getIdDTS() == null || input.getDatos().get(i).getIdDTS().equals("")
                        || !isNumeric(input.getDatos().get(i).getIdDTS())) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo,
                            "Elemento No. " + (i + 1), input.getCodArea());
                }

                if (input.getDatos().get(i).getMontoDeposito() == null
                        || input.getDatos().get(i).getMontoDeposito().trim().equals("")
                        || !isDecimal(input.getDatos().get(i).getMontoDeposito())) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MONTO_REMESA_711, null, nombreClase, nombreMetodo,
                            "Elemento No. " + (i + 1), input.getCodArea());
                }
            }
        }

        return new Respuesta();
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST.
     * 
     * @param input
     * @param tipo
     * @param nombreMetodo
     * @param servicio
     * @return
     */
    public OutputReporteCtaDTS getDatosReporte(InputReporteCtaDTS input, int tipo, String nombreMetodo,
            String servicio) {
        String nombreClase = new CurrentClassGetter().getClassName();
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        OutputReporteCtaDTS output = new OutputReporteCtaDTS();
        Respuesta respuesta = new Respuesta();
        Connection conn = null;

        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());
            log.trace("Usuario: " + input.getUsuario());

            // Validaci\u00F3n de datos en el input
            respuesta = validarInput(conn, input, tipo);
            log.trace("Respuesta validaci\u00F3n: " + (respuesta.getDescripcion() == null ? "OK" : respuesta.getDescripcion()));
            if (new BigDecimal(respuesta.getCodResultado()).intValue() != 0) {
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicio, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));

                return output;
            }

            // Tipo: 0 = Datos Debe, 1 = Datos Haber, 2 = Datos Resumen, 3 = Insertar Haber
            switch (tipo) {
            case 0:
                output = OperacionReporteCtaDTS.doGetDebe(conn, input, ID_PAIS);
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicio, input.getIdDTS(),
                        Conf.LOG_TIPO_DISTRIBUIDOR, "Se consultaron datos del DEBE del reporte de cuenta DTS.", ""));
                break;

            case 1:
                output = OperacionReporteCtaDTS.doGetHaber(conn, input, ID_PAIS);
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicio, input.getIdDTS(),
                        Conf.LOG_TIPO_DISTRIBUIDOR, "Se consultaron datos del HABER del reporte de cuenta DTS.", ""));
                break;

            case 2:
                output = OperacionReporteCtaDTS.doGetResumen(conn, input, ID_PAIS);
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicio, input.getIdDTS(),
                        Conf.LOG_TIPO_DISTRIBUIDOR, "Se consultaron datos del RESUMEN del reporte de cuenta DTS.", ""));
                break;

            case 3:
                output = OperacionReporteCtaDTS.postHaber(conn, input, ID_PAIS);
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicio, "0",
                        Conf.LOG_TIPO_NINGUNO, "Se insertaron datos del HABER del reporte de cuenta DTS.", ""));
                break;
            }

        } catch (SQLException e) {
            respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicio, "0", Conf.LOG_TIPO_NINGUNO,
                    "Problema al consultar datos del reporte de inventario por jornada.", e.getMessage()));

        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicio, "0", Conf.LOG_TIPO_NINGUNO,
                    "Problema en el servicio del reporte de inventario por jornada.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
    
    public OutputReporteCtaDTS getDatosDebe(InputReporteCtaDTS input) {
        String nombreMetodo = "getDatosDebe";

        return getDatosReporte(input, 0, nombreMetodo, Conf.LOG_GET_REPORTE_CTA_DTS_DEBE);
    }
    
    public OutputReporteCtaDTS getDatosHaber(InputReporteCtaDTS input) {
        String nombreMetodo = "getDatosHaber";

        return getDatosReporte(input, 1, nombreMetodo, Conf.LOG_GET_REPORTE_CTA_DTS_HABER);
    }
    
    public OutputReporteCtaDTS getDatosResumen(InputReporteCtaDTS input) {
        String nombreMetodo = "getDatosResumen";

        return getDatosReporte(input, 2, nombreMetodo, Conf.LOG_GET_REPORTE_CTA_DTS_RESUMEN);
    }
    
    public OutputReporteCtaDTS postDatosResumen(InputReporteCtaDTS input) {
        String nombreMetodo = "postDatosResumen";

        return getDatosReporte(input, 3, nombreMetodo, Conf.LOG_POST_REPORTE_CTA_DTS_RESUMEN);
    }

    public static String[] obtenerCamposGetPost() {
        String[] campos = {
                CuentaHaberDTS.CAMPO_TCSCCTAHABERDTSID,
                CuentaHaberDTS.CAMPO_TCSCCATPAISID,
                CuentaHaberDTS.CAMPO_TCSCDTSID,
                CuentaHaberDTS.CAMPO_CUENTA,
                CuentaHaberDTS.CAMPO_BANCO,
                CuentaHaberDTS.CAMPO_NO_DEPOSITO,
                CuentaHaberDTS.CAMPO_MONTO_DEPOSITO,
                CuentaHaberDTS.CAMPO_FECHA_DEPOSITO,
                CuentaHaberDTS.CAMPO_MONEDA,
                CuentaHaberDTS.CAMPO_SOC,
                CuentaHaberDTS.CAMPO_ASIGNACION,
                CuentaHaberDTS.CAMPO_REFERENCIA,
                CuentaHaberDTS.CAMPO_USUARIO,
                CuentaHaberDTS.CAMPO_CTA_CP,
                CuentaHaberDTS.CAMPO_FE_CONTAB,
                CuentaHaberDTS.CAMPO_FECHA_DOC,
                CuentaHaberDTS.CAMPO_CREADO_EL,
                CuentaHaberDTS.CAMPO_CREADO_POR
        };

        return campos;
    }
    
    public static List<String> obtenerInsertsPost(InputReporteCtaDTS input, String sequencia, BigDecimal ID_PAIS) {
        List<String> inserts = new ArrayList<String>();
        String valores = "";
        for (int i = 0; i < input.getDatos().size(); i++) {
            valores = 
                  UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, ID_PAIS.toString(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getDatos().get(i).getIdDTS(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getDatos().get(i).getCuenta(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getDatos().get(i).getBanco(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getDatos().get(i).getNoDeposito(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getDatos().get(i).getMontoDeposito(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsertFecha(Conf.INSERT_TIMESTAMP, input.getDatos().get(i).getFechaDeposito(), Conf.TXT_FORMATO_FECHAHORA2, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getDatos().get(i).getMoneda(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getDatos().get(i).getSoc(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getDatos().get(i).getAsignacion(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getDatos().get(i).getReferencia(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getDatos().get(i).getUsuarioHaber(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getDatos().get(i).getCtaCP(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsertFecha(Conf.INSERT_TIMESTAMP, input.getDatos().get(i).getFeContab(), Conf.TXT_FORMATO_FECHAHORA2, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsertFecha(Conf.INSERT_TIMESTAMP, input.getDatos().get(i).getFechaDoc(), Conf.TXT_FORMATO_FECHAHORA2, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
            + " ";
            inserts.add(valores);
        }
        
        return inserts;
    }
}
