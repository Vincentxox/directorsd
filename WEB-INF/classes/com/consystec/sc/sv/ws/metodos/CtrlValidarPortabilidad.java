package com.consystec.sc.sv.ws.metodos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.portabilidad.InputPortabilidad;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.portabilidad.OutputPortabilidad;
import com.consystec.sc.sv.ws.operaciones.OperacionPortabilidad;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlValidarPortabilidad extends ControladorBase{
	private static final Logger log = Logger.getLogger(CtrlFolioVirtual.class);
	private static String servicioGet = Conf.LOG_GET_FOLIOVIRTUAL;
	
	/**
	 * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al
	 * servicio.
	 * 
	 * @param conn
	 * @param input
	 *            Objeto con los datos enviados mediante GET.
	 * @param metodo
	 *            Valor que indica tipo de operaci\u00F3n que se desea realizar en el
	 *            servicio.
	 * @return Respuesta
	 */
	public Respuesta validarInput(Connection conn, InputPortabilidad input) {
		String nombreMetodo = "validarInput";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta r = new Respuesta();
		String datosErroneos = "";
		boolean flag = false;
		log.debug("Validando datos...");

		if (input.getUsuario() == null || input.getUsuario().trim().equals("")) {
			r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo, null, input.getCodArea());
			datosErroneos += r.getDescripcion();
			flag = true;
		}
		
		if(!"2".equals(input.getValidar())){
			if (input.getNip() == null || input.getNip().trim().equals("")) {
				r = getMensaje(Conf_Mensajes.MSJ_NIP_VACIO_645, null, nombreClase, nombreMetodo, null, input.getCodArea());
				datosErroneos += r.getDescripcion();
				flag = true;
			} else if (!isNumeric(input.getNip())) {
				r = getMensaje(Conf_Mensajes.MSJ_NIP_NO_NUM_646, null, nombreClase, nombreMetodo, null, input.getCodArea());
				datosErroneos += r.getDescripcion();
				flag = true;
			}
		}
		
		if (input.getNumTelefono() == null || input.getNumTelefono().trim().equals("")) {
			r = getMensaje(Conf_Mensajes.MSJ_TELEFONO_VACIO_647, null, nombreClase, nombreMetodo, null, input.getCodArea());
			datosErroneos += r.getDescripcion();
			flag = true;
		} else if (!isNumeric(input.getNumTelefono())) {
			r = getMensaje(Conf_Mensajes.MSJ_TELEFONO_NO_NUM_648, null, nombreClase, nombreMetodo, null, input.getCodArea());
			datosErroneos += r.getDescripcion();
			flag = true;
		}
		
		if(input.getValidar() == null || "".equals(input.getValidar().trim())){
			r = getMensaje(Conf_Mensajes.MSJ_VALIDAR_VACIO_649, null, nombreClase, nombreMetodo, null, input.getCodArea());
			datosErroneos += r.getDescripcion();
			flag = true;
		}else if (!isNumeric(input.getValidar())) {
			r = getMensaje(Conf_Mensajes.MSJ_VALIDAR_NO_NUM_650, null, nombreClase, nombreMetodo, null, input.getCodArea());
			datosErroneos += r.getDescripcion();
			flag = true;
		}

		if (flag == true) {
			r = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo, datosErroneos, input.getCodArea());
		} else {
			r.setDescripcion("OK");
			r.setCodResultado("1");
			r.setMostrar("0");
		}
		return r;
	}
	
	public OutputPortabilidad getDatos(InputPortabilidad input){
		List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta respuesta = new Respuesta();
        OutputPortabilidad output = new OutputPortabilidad();
        
        Connection conn = null;
        try {
            conn = getConnRegional();

            // Validaci\u00F3n de datos en el input
            respuesta = validarInput(conn, input);
            log.trace("Respuesta validaci\u00F3n: " + respuesta.getDescripcion());
            if (!respuesta.getDescripcion().equals("OK")) {
                output.setRespuesta(respuesta);
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));
                return output;
            }
            
            output = OperacionPortabilidad.doGet(conn, input);
       
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_PDV, "Se valid\u00F3 el servicio no" + input.getValidar() + " en portabilidad.", ""));
        } catch (SQLException e) {
            respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
            log.error("Excepcion: " + e.getMessage(), e);
            output.setRespuesta(respuesta);
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de portabilidad.", e.getMessage()));
        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
            log.error("Excepcion: " + e.getMessage(), e);
            output.setRespuesta(respuesta);
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de portabilidad.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }
        return output;
    }
}
