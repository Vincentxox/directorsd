package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.fichacliente.InputFichaCliente;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.fichacliente.OutputFichaCliente;
import com.consystec.sc.sv.ws.operaciones.OperacionFichaCliente;
import com.consystec.sc.sv.ws.operaciones.OperacionLogSidra;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class CtrlFichaCliente extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlFichaCliente.class);
    private static String servicioGet = Conf.LOG_GET_FICHA_CLIENTE;
    private static String servicioPost = Conf.LOG_POST_FICHA_CLIENTE;

    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al
     * servicio.
     * 
     * @param conn
     * 
     * @param input
     *            Objeto con los datos enviados mediante POST.
     * @param metodo
     *            Valor que indica tipo de operaci\u00F3n que se desea realizar en el
     *            servicio.
     * @return Respuesta
     */
    public Respuesta validarInput(InputFichaCliente input, int metodo) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;

        log.debug("Validando datos...");

        if (metodo == Conf.METODO_GET) {

        	
        	if (input.getTipoCliente() == null || "".equals(input.getTipoCliente().trim())) {
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_CLIENTE_600, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
				flag = true;
			} else if(Conf.FICHA_CLIENTE_TIPO_DTS.equalsIgnoreCase(input.getTipoCliente())
					|| Conf.FICHA_CLIENTE_TIPO_PDV.equalsIgnoreCase(input.getTipoCliente())) {
				if (input.getCodCliente() == null || "".equals(input.getCodCliente().trim())) {
					datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_CLIENTE_607, null, nombreClase,
	                        nombreMetodo, null, input.getCodArea()).getDescripcion();
					flag = true;
				}
			} else if(Conf.FICHA_CLIENTE_TIPO_CF.equalsIgnoreCase(input.getTipoCliente())) {
				if ((input.getTipoDocumento() != null && !"".equals(input.getTipoDocumento().trim())) && (input.getNoDocumento() == null || "".equals(input.getNoDocumento().trim()))) {
						datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NODOCUMENTO_258, null, nombreClase,
								nombreMetodo, null, input.getCodArea()).getDescripcion();
						flag = true;
				}
				if ((input.getNoDocumento() != null && !"".equals(input.getNoDocumento().trim()))&&(input.getTipoDocumento() == null || "".equals(input.getTipoDocumento().trim()))){
						datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_DOCUMENTO_266, null, nombreClase,
								nombreMetodo, null, input.getCodArea()).getDescripcion();
						flag = true;
				}
			} else {
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_CLIENTE_INVALIDO_610, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
				flag = true;
			}
        } else if (metodo == Conf.METODO_POST) {
			if (input.getTipoCliente() == null || "".equals(input.getTipoCliente().trim())) {
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_CLIENTE_600, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
				flag = true;
			} else if (Conf.FICHA_CLIENTE_TIPO_PDV.equalsIgnoreCase(input.getTipoCliente())) {
				if (input.getIdPdv() == null || "".equals(input.getIdPdv().trim())) {
					datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDPDV_601, null, nombreClase,
	                        nombreMetodo, null, input.getCodArea()).getDescripcion();
					flag = true;
				} else if(!isNumeric(input.getIdPdv())) {
					datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDPDV_NUM_315, null, nombreClase,
	                        nombreMetodo, null, input.getCodArea()).getDescripcion();
					flag = true;
				}
			}
        	if (input.getIdDts() == null || "".equals(input.getIdDts().trim())) {
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_161, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
				flag = true;
			} else if(!isNumeric(input.getIdDts())) {
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
				flag = true;
			}

        	if (input.getNoDocumento() == null || "".equals(input.getNoDocumento())) {
        		datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NODOCUMENTO_258, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
			}
        	if (input.getPrimerNombre() == null || "".equals(input.getPrimerNombre().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_PRIMER_NOMBRE_261, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }
            if (input.getPrimerApellido() == null || "".equals(input.getPrimerApellido().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_PRIMER_APELLIDO_262, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }
            if (input.getTelContacto() == null || "".equals(input.getTelContacto().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TEL_CONTACTO_605, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
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
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST.
     * 
     */
    public OutputFichaCliente getDatos(InputFichaCliente input, int metodo, boolean desdePDV, Connection connPDV) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = new Respuesta();
        OutputFichaCliente output = new OutputFichaCliente();

        log.trace("Usuario: " + input.getUsuario());
        log.trace("Token: " + input.getToken());

        Connection conn = null;
        try {
        	if (desdePDV) {
				conn = connPDV;
			} else {
	            conn = getConnRegional();
			}
        	
            BigDecimal idPais = getIdPais(conn, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            respuesta = validarInput(input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + respuesta.getDescripcion());
            if (!"OK".equals(respuesta.getDescripcion())) {
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));

                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                try {
                    output = OperacionFichaCliente.doGet(input.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de fichas de cliente.", ""));
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputFichaCliente();
                    output.setRespuesta(respuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de fichas de cliente.",
                            e.getMessage()));
                }
            } else if (metodo == Conf.METODO_POST) {
				try {
					
					BigDecimal logId = getLosSidraId(conn);
					log.trace("logId := " + logId);
					output = OperacionFichaCliente.doPost(conn, input);
					
					if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_FICHA_CLIENTE_68) {

						
						LogSidra log1 = ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CLIENTE, servicioPost,
								"0", Conf.LOG_TIPO_FICHA_CLIENTE,
								"Se registr\u00F3 una ficha de cliente scl del Distribuidor " + output.getCliente().getIdDts()
								+ " o Punto de Venta  " + output.getCliente().getIdPdv()
								+ ".",
								"");
						log1.setTcsclogsidraid(logId);
                        OperacionLogSidra.insertaLogIndividual(conn, log1, input.getUsuario(), idPais);
					} else {

                        
                        LogSidra log2 = ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CLIENTE, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema al crear cliente.",
                                output.getRespuesta().getDescripcion());
						log2.setTcsclogsidraid(logId);
                        OperacionLogSidra.insertaLogIndividual(conn, log2, input.getUsuario(), idPais);
                    }
				} catch (SQLException e) {
					respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputFichaCliente();
                    output.setRespuesta(respuesta);
                    
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CLIENTE, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al crear la ficha de cliente en scl.", e.getMessage()));
				}
			}
            
        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputFichaCliente();
            output.setRespuesta(respuesta);

            listaLog.add(
                    ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO,
                            "Problema en el servicio de condiciones de ficha de cliente.", e.getMessage()));
        } finally {
        	if (!desdePDV) {
                DbUtils.closeQuietly(conn);
			}

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
    
    
}
