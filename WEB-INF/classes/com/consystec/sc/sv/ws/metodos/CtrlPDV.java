package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.pdv.InputConsultaPDV;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.pdv.OutputpdvDirec;
import com.consystec.sc.sv.ws.operaciones.OperacionPDV;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 */
public class CtrlPDV extends ControladorBase{
	private static final Logger log = Logger.getLogger(CtrlPDV.class);
    private static String servicioGet = Conf.LOG_GET_PDV;
    private static String servicioCount = Conf.LOG_COUNT_PDV;
	
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDe como recurso al servicio.
     * 
     * @param input
     * @param metodo
     * @return Respuesta
     * @throws SQLException 
     */
    public Respuesta validarInput(InputConsultaPDV input, int metodo) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        log.debug("Validando datos...");

        String estadoAltaBool = UtileriasJava.getConfig(null, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA_BOOL, input.getCodArea());
        String estadoBajaBool = UtileriasJava.getConfig(null, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA_BOOL, input.getCodArea());

        if (input.getIdDTS() != null && !isNumeric(input.getIdDTS())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getIdPDV() != null && !isNumeric(input.getIdPDV())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDPDV_NUM_315, null, nombreClase, nombreMetodo, null, input.getCodArea())
                    .getDescripcion();
            flag = true;
        }

        if (input.getIdVendedor() != null && !isNumeric(input.getIdVendedor())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase,
                    nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getIdRuta() != null && !isNumeric(input.getIdRuta())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDRUTA_NUM_251, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getNumRecarga() != null && !isNumeric(input.getNumRecarga())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NUMERO_264, null, nombreClase, nombreMetodo, null, input.getCodArea())
                    .getDescripcion();
            flag = true;
        }

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }


        if (metodo == Conf.METODO_GET) { 
        	if (input.getMostrarNumerosRecarga() == null || !isNumeric(input.getMostrarNumerosRecarga())) {
        		datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_PDV_RECARGA_NUM_318, null, nombreClase,
        				nombreMetodo, null, input.getCodArea()).getDescripcion();
        		flag = true;
        	} else if (!input.getMostrarNumerosRecarga().equals(estadoAltaBool)
        			&& !input.getMostrarNumerosRecarga().equals(estadoBajaBool)) {
        		datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_PDV_RECARGA_TIPO_319, null, nombreClase,
        				nombreMetodo, "(" + estadoAltaBool + " o " + estadoBajaBool + ").", input.getCodArea()).getDescripcion();
        		flag = true;
        	}

        	if (input.getMostrarDiasVisita() == null || !isNumeric(input.getMostrarDiasVisita())) {
        		datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_PDV_DIAS_NUM_320, null, nombreClase, nombreMetodo,
        				null, input.getCodArea()).getDescripcion();
        		flag = true;
        	} else if (!input.getMostrarDiasVisita().equals(estadoAltaBool)
        			&& !input.getMostrarDiasVisita().equals(estadoBajaBool)) {
        		datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_PDV_DIAS_TIPO_321, null, nombreClase,
        				nombreMetodo, "(" + estadoAltaBool + " o " + estadoBajaBool + ").", input.getCodArea()).getDescripcion();
        		flag = true;
        	}
        	
        	if((input.getMin() != null && !"".equals(input.getMin())) && !isNumeric(input.getMin())){
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MINIMO_198, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
            }
            
            if(input.getMax() != null && !"".equals(input.getMax())) {
                if(!isNumeric(input.getMax())){
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MAXIMO_217, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
                } else if ((input.getMin() != null && !"".equals(input.getMin())) && Integer.parseInt(input.getMin()) > Integer.parseInt(input.getMax())) {
						r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MINIMO_MAYOR_218, null, nombreClase,
		                        nombreMetodo, null, input.getCodArea());
		                    datosErroneos += r.getDescripcion();
		                    flag = true;
				}
            }
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
     * @return respuestaRuta
     */
    public OutputpdvDirec getDatos(InputConsultaPDV input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        List<Respuesta> respuesta = new ArrayList<Respuesta>();
        OutputpdvDirec output = null;
        Respuesta r = new Respuesta();

        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());
            // Validación de datos en el input
            r = validarInput(input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equals(r.getDescripcion())) {
                respuesta.add(r);
                output = new OutputpdvDirec();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                try {
                    output = OperacionPDV.doGet(conn, input, metodo, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de puntos de venta.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputpdvDirec();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de puntos de venta.", e.getMessage()));
                }
            } else if (metodo == Conf.METODO_COUNT) {
            	try {
                    output = OperacionPDV.doGet(conn, input, metodo, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioCount, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consulto el conteo total de puntos de venta.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputpdvDirec();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema en el conteo de puntos de venta.", e.getMessage()));
                }
			}
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputpdvDirec();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de consulta datos de PDVs.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
