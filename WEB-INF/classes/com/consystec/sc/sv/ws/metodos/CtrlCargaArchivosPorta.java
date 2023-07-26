package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.dbutils.DbUtils;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.portabilidad.InputCargaAdjuntoPorta;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.portabilidad.OutputAdjuntoPorta;
import com.consystec.sc.sv.ws.operaciones.OperacionAdjuntoPorta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;


public class CtrlCargaArchivosPorta extends ControladorBase {
	String nombreClase= new CurrentClassGetter().getClassName();
	private  String servicioPost = Conf.LOG_POST_ADJUNTO;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
	 public Respuesta validarDatos(InputCargaAdjuntoPorta input) {
	        Respuesta objRespuesta = new Respuesta();
	        String nombreMetodo = "validarDatos";
	    
			String datosErroneos="";
			boolean flag=false;
			
			if (input.getCodArea() == null || "".equals(input.getCodArea())) {
	            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_303, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
	        } else if (input.getCodArea().length() != 3){
	            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_LONG_304, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
	        } else if (!isNumeric(input.getCodArea())) {
	            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_NUM_305, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
	        }
			if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
	            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
	        }	
			
			if (input.getIdPortabilidad()==null || "".equals(input.getIdPortabilidad()))
			{
			    datosErroneos += getMensaje(Conf_Mensajes.MSJ_IDPORTABILIDAD_VACIO_674, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
			}
			
			if (input.getIdPortaMovil()==null || "".equals(input.getIdPortaMovil()))
			{
			    datosErroneos += getMensaje(Conf_Mensajes.MSJ_IDPORTAMOVIL_NULO_915, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
			}
			
			if (input.getNombreArchivo()==null || "".equals(input.getNombreArchivo()))
			{
			    datosErroneos += getMensaje(Conf_Mensajes.MSJ_NOMBREARCHIVO_NULO_272, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
			}
			if (input.getExtension()==null || "".equals(input.getExtension()))
			{
			    datosErroneos += getMensaje(Conf_Mensajes.MSJ_EXTENSION_NULO_270, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
			}
			if (input.getTipoArchivo()==null || "".equals(input.getTipoArchivo()))
			{
			    datosErroneos += getMensaje(Conf_Mensajes.MSJ_TIPOARCHIVO_NULO_273, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
			}
			if (input.getIdAttachment()==null || "".equals(input.getIdAttachment()))
			{
			    datosErroneos += getMensaje(Conf_Mensajes.MSJ_IDATTACHMENT_VACIO_675, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
			}
			
			if (input.getAdjunto()==null || "".equals(input.getAdjunto()))
			{
			    datosErroneos += getMensaje(Conf_Mensajes.MSJ_ADJUNTO_NULO_269, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
			}
			
			if (flag)
			{
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo, datosErroneos, input.getCodArea());
	        } else {
	        	objRespuesta.setDescripcion("OK");
	        	objRespuesta.setCodResultado("1");
	        	objRespuesta.setMostrar("0");
	        }

	        return objRespuesta;
	    }
	 
	 
	 public OutputAdjuntoPorta cargarAdjunto(InputCargaAdjuntoPorta input) {
	        String nombreMetodo = "cargarAdjunto";
	        Connection conn = null;
	        Respuesta objRespuesta = new Respuesta();
	        OutputAdjuntoPorta output = new OutputAdjuntoPorta();
	        COD_PAIS = input.getCodArea();
	        List<LogSidra> listaLog = new ArrayList<LogSidra>();

	       try {
	            objRespuesta = validarDatos(input);

	            if (objRespuesta == null) {
	                conn = getConnRegional();
	              ID_PAIS =  getIdPais(conn, input.getCodArea());

	                // Convirtiendo en un arreglo de bytes la imagen recibida en base 64
	                byte[] base64Decoded = DatatypeConverter.parseBase64Binary(input.getAdjunto());

	               log.trace("Tama\u00F1o en bytes de imagen:"+base64Decoded.length );
	                if (base64Decoded.length < 2097152){ 
		                BigDecimal idAdjunto = OperacionAdjuntoPorta.saveAdjunto(conn,  input);
		                if (idAdjunto != null) {
		                    objRespuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_ADJUNTO_64, null, nombreClase,
		                            nombreMetodo, null, input.getCodArea());
		
		                    output.setIdAdjunto(idAdjunto + "");
		                    output.setRespuesta(objRespuesta);
		
		                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioPost,
		                            idAdjunto + "", Conf.LOG_TIPO_ADJUNTO, "Se registr\u00F3 el adjunto con ID "
		                            + idAdjunto + " a la gesti\u00F3n con ID " + input.getIdPortabilidad() + ".", ""));
		                }
	                }else{
	                	 objRespuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_IMG_2MB_629, null, nombreClase,
		                            nombreMetodo, null, input.getCodArea());
		
		                    output.setIdAdjunto("");
		                    output.setRespuesta(objRespuesta);
		                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
		                            "Problema al registrar adjunto de gesti\u00F3n.", objRespuesta.getDescripcion()));
	                }

	            } else {
	                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
	                        "Problema al registrar adjunto de gesti\u00F3n.", objRespuesta.getDescripcion()));
	            }

	        } catch (SQLException e) {
	            log.error(e.getMessage(), e);
	            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());

	            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
	                    "Problema al registrar adjunto de gesti\u00F3n.", e.getMessage()));
	        } catch (Exception e) {
	            log.error(e.getMessage(), e);
	            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());

	            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
	                    "Problema al registrar adjunto de gesti\u00F3n.", e.getMessage()));
	        } finally {
	            DbUtils.closeQuietly(conn);
	            output.setRespuesta(objRespuesta);

	            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
	        }

	        return output;
	    }


}
