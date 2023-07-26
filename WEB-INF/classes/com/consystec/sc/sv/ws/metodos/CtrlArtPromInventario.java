package com.consystec.sc.sv.ws.metodos;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.inventariopromo.InputGetArtPromInventario;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.inventariopromo.OutputArtPromInventario;
import com.consystec.sc.sv.ws.operaciones.OperacionConsultaArticulosPromocionales;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.consystec.sc.sv.ws.util.ControladorBase;
public class CtrlArtPromInventario extends ControladorBase  {

	 private static final Logger log = Logger.getLogger(CtrlArtPromInventario.class);
	    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
	    private static String servicioGet = Conf.LOG_GET_ARTICULOS;
	    String COD_PAIS="";
	    BigDecimal ID_PAIS =null;
	    public Respuesta validarInput(InputGetArtPromInventario input, int metodo) {
	        String nombreMetodo = "validarInput";
	        String nombreClase = new CurrentClassGetter().getClassName();
	        Connection conn = null;
	        int VerificaBodega = 0;
	        Respuesta r = new Respuesta();
	        String datosErroneos = "";
	        boolean flag = false;

	        log.debug("Validando datos...");

	        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
	            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo, null, input.getCodArea());
	            datosErroneos += r.getDescripcion();
	            flag = true;
	        }

	        if (input.getCodArea() == null || "".equals(input.getCodArea().trim())) {
	            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_303, null, nombreClase, nombreMetodo, null,input.getCodArea());
	            datosErroneos += r.getDescripcion();
	            flag = true;
	        }

	        if (input.getIdBodegaVirtual() == null || "".equals(input.getIdBodegaVirtual().trim())) {

	            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODORIGEN_139, null, nombreClase, nombreMetodo, null,input.getCodArea());
	            datosErroneos += r.getDescripcion();
	            flag = true;
	        } else {
	            try {
	                conn = getConnRegional();
	                VerificaBodega = OperacionConsultaArticulosPromocionales.validarBodega(conn, input, ID_PAIS);
	                // Validamos la bodega que exista para el id pais
	                if (VerificaBodega == 0) {
	                    r = getMensaje(Conf_Mensajes.MSJ_IDBODVIRTUAL_NO_EXISTE_172, null, nombreClase, nombreMetodo, null,input.getCodArea());
	                    datosErroneos += r.getDescripcion();
	                    flag = true;
	                }
	            } catch (SQLException e) {
	                r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null,input.getCodArea());
	                log.error("ExcepcionSQL: " + e);
	                listaLog = new ArrayList<LogSidra>();
	                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
	                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar la bodega virtual.", e.getMessage()));
	            }finally{
	            	DbUtils.closeQuietly(conn);
	            }

	        }

	        if (flag) {
	            r = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo, datosErroneos, input.getCodArea());
	        } else {
	            r.setDescripcion("OK");
	            r.setCodResultado("1");
	            r.setMostrar("0");
	            return r;
	        }
	        return r;
	    }

	    public OutputArtPromInventario getRespuesta(InputGetArtPromInventario input, int metodo) {
	        String nombreMetodo = "getRespuesta";
	        String nombreClase = new CurrentClassGetter().getClassName();

	        OutputArtPromInventario output = null;
	        COD_PAIS = input.getCodArea();

	        Connection conn = null;
	        Respuesta r = null;

	        try {
	            conn = getConnRegional();
	            getIdPais(conn, input.getCodArea());
	            r = validarInput(input, metodo);
	            log.trace("Respuesta Validaci\u00F3n: " + r.getDescripcion());
	            if (!"OK".equals(r.getDescripcion())) {
	                output = new OutputArtPromInventario();
	                output.setRespuesta(r);

	                listaLog = new ArrayList<LogSidra>();
	                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
	                        Conf.LOG_TIPO_NINGUNO, "Problemas al consultar lista de productos promocionales.",
	                        r.getDescripcion()));
	                return output;
	            }
	            if (metodo == Conf.METODO_GET) {
	                output = OperacionConsultaArticulosPromocionales.Articulos(conn, input, ID_PAIS);
	                listaLog = new ArrayList<LogSidra>();
	                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
	                        Conf.LOG_TIPO_NINGUNO, "Se consulto los articulos correctamente.", ""));
	            }

	        } catch (SQLException e) {
	            r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null,input.getCodArea());

	            log.error("ExcepcionSQL: " + e);
	            output = new OutputArtPromInventario();
	            output.setRespuesta(r);

	            listaLog = new ArrayList<LogSidra>();
	            listaLog.add(
	                    ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO,
	                            "Problema al consultar la lista de productos promocionales.", e.getMessage()));

	        } catch (Exception e) {
	        	log.error(e, e);
	        } finally {
	            DbUtils.closeQuietly(conn);
	            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

	        }

	        return output;
	    }
}
