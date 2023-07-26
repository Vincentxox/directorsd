package com.consystec.sc.sv.ws.metodos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.descuentoFS.InputDescuentoFS;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.descuentoFS.Descuentos;
import com.consystec.sc.ca.ws.output.descuentoFS.OutputDescuentoFS;
import com.consystec.sc.sv.ws.operaciones.OperacionDescuentoFS;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlDescuentoFS extends ControladorBase{
	private static final Logger log = Logger.getLogger(CtrlDescuentoFS.class);
	private static String servicioGet = Conf.LOG_GET_DESCUENTO_FS;

	public Respuesta validarInput(InputDescuentoFS input){
		String nombreMetodo = "validarInput";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta respuesta = null;

		log.debug("Validando datos...");

		if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) 
			respuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, input.getCodArea());
		


		return respuesta;
	}
	
	public OutputDescuentoFS getDescuentoFs(InputDescuentoFS objDatos) {
		OutputDescuentoFS respuesta = new OutputDescuentoFS();
        Respuesta objRespuesta = null;
        String metodo = "getReporteRecarga";
        Connection conn = null;
        List<InputDescuentoFS> ofertas = new ArrayList<InputDescuentoFS>();
    	List<LogSidra> listaLog = new ArrayList<LogSidra>();

        // validando filtros
        try {
            objRespuesta = validarInput(objDatos);
            if (objRespuesta == null) {
                conn = getConnRegional();
                getIdPais(conn, objDatos.getCodArea());

                ofertas = OperacionDescuentoFS.doGet(conn, objDatos);

                if (ofertas.isEmpty()) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo, null, objDatos.getCodArea());
                } else {
                    respuesta.setListaDescuentos(ordenarDescuentos(ofertas));
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
                }

            } else {
                listaLog = new ArrayList<LogSidra>();
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar reporte recargas.",
                        objRespuesta.getDescripcion()));

            }
        } catch (SQLException e) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar reporte recargas.", e.getMessage()));
        } catch (Exception e) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar reporte recargas.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
            respuesta.setRespuesta(objRespuesta);
        }
        return respuesta;
    }
	
	public List<Descuentos> ordenarDescuentos(List<InputDescuentoFS> lst){
		String idProductOffering = "";
		List<Descuentos> listaDescuentos = new ArrayList<Descuentos>();
		Descuentos descuento = null;
		InputDescuentoFS detalleDescuentos = null;
		List<InputDescuentoFS> lstDetalleDescuentos = null;
		int contador = 0;
		
		for (int i = 0; i < lst.size(); i++) {
			contador ++;
			if(!idProductOffering.equals(lst.get(i).getIdProductOffering())){
				idProductOffering = lst.get(i).getIdProductOffering();
				
				descuento = new Descuentos();
				descuento.setIdProductOffering(lst.get(i).getIdProductOffering());
				descuento.setNombre(lst.get(i).getNombreOferta());
				
				detalleDescuentos = new InputDescuentoFS();
				
				detalleDescuentos.setIdDescuento(lst.get(i).getIdDescuento());
				detalleDescuentos.setNombreDescuento(lst.get(i).getNombreDescuento());
				detalleDescuentos.setMontoDescuento(lst.get(i).getMontoDescuento());
				detalleDescuentos.setTipoDescuento(lst.get(i).getTipoDescuento());
				detalleDescuentos.setCreadoEl(lst.get(i).getCreadoEl());
				detalleDescuentos.setCreadoPor(lst.get(i).getCreadoPor());
				detalleDescuentos.setModificadoEl(lst.get(i).getModificadoEl());
				detalleDescuentos.setModificadoPor(lst.get(i).getModificadoPor());
				
				lstDetalleDescuentos = new ArrayList<InputDescuentoFS>();
				lstDetalleDescuentos.add(detalleDescuentos);
			}else{
				detalleDescuentos = new InputDescuentoFS();
				
				detalleDescuentos.setIdDescuento(lst.get(i).getIdDescuento());
				detalleDescuentos.setNombreDescuento(lst.get(i).getNombreDescuento());
				detalleDescuentos.setMontoDescuento(lst.get(i).getMontoDescuento());
				detalleDescuentos.setTipoDescuento(lst.get(i).getTipoDescuento());
				detalleDescuentos.setCreadoEl(lst.get(i).getCreadoEl());
				detalleDescuentos.setCreadoPor(lst.get(i).getCreadoPor());
				detalleDescuentos.setModificadoEl(lst.get(i).getModificadoEl());
				detalleDescuentos.setModificadoPor(lst.get(i).getModificadoPor());
				
				lstDetalleDescuentos.add(detalleDescuentos);
		
			}			
			if(contador < lst.size() && !idProductOffering.equals(lst.get(i+1).getIdProductOffering())){
				descuento.setDescuentos(lstDetalleDescuentos);
				
				listaDescuentos.add(descuento);
			}else if(contador == lst.size()){
				descuento.setDescuentos(lstDetalleDescuentos);
				listaDescuentos.add(descuento);
			}
		}
		
		return listaDescuentos;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
