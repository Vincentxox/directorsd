package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.ticket.InputTicket;
import com.consystec.sc.ca.ws.input.ticket.OutputTicket;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.sv.ws.operaciones.OperacionVisorTicket;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.ericsson.sdr.dao.OlsDAO;

/**
 * @author Sbarrios 2018
 * */
public class CtrlVisorTicket  extends ControladorBase{

    private static final Logger log = Logger.getLogger(CtrlVisorTicket.class);
    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioPost = Conf.LOG_REGISTRA_VISOR_TCK;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    public Respuesta validaDatos (InputTicket objDatos){
    	Respuesta objRespuesta=null;
        String metodo = "validarDatos";
        
        if (objDatos.getUsuario() == null || objDatos.getUsuario().equals("")) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
        }

        if(objDatos.getIdVenta()==null || "".equals(objDatos.getIdVenta())){
        	  objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDVENTA_NULO_921, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
        }else if (!isNumeric(objDatos.getIdVenta())){
        	 objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDVENTA_INVALIDO_922, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
        }
        
        if(objDatos.getLineas()==null || objDatos.getLineas().isEmpty()){
        	 objRespuesta = getMensaje(Conf_Mensajes.MSJ_LINEAS_NULO_923, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
        }
    	return objRespuesta;
    }
    
    
    
    public Respuesta registraTicket(InputTicket objDatos){
    	Respuesta objRespuesta=null;
    	Connection conn=null;
    	 String metodo = "registraTicket";
         List<Integer> lstExisteVenta;
         int respDelete=0;
         
        COD_PAIS = objDatos.getCodArea();
    	objRespuesta=validaDatos(objDatos);
    	
    	
    	try {
    		if(objRespuesta==null){
    			conn=getConnRegional();
    	       ID_PAIS = getIdPais(conn, objDatos.getCodArea());
    	        
    	        //Validar si venta existe
    	        lstExisteVenta=OperacionVisorTicket.validarVenta(conn, objDatos.getIdVenta(),ID_PAIS);
    	        
    	        if(lstExisteVenta.get(0)==1){
    	        	//sino hay lineas registradas se procede a insertar detalle de visor
    	        	if(lstExisteVenta.get(1)==0){
    	        		OperacionVisorTicket.insertaLineaTicket(conn, objDatos.getLineas(), objDatos.getIdVenta(), objDatos.getUsuario(), objDatos.getCodDispositivo(),ID_PAIS);
    	        		listaLog.add(ControladorBase.addLog(Conf.LOG_REGISTRA_VISOR_TCK, servicioPost, objDatos.getIdVenta() ,
                                Conf.LOG_TIPO_VENTA, "Se registro visor de ticket para venta: " + objDatos.getIdVenta()+ ".", ""));
    	        		objRespuesta = getMensaje(Conf_Mensajes.OK_REGISTRO_VISOR_TCK_86, null, null, null, "", objDatos.getCodArea());
    	        	}else{
    	        		//si ya tiene lineas se proceder\u00E9 a eliminar visor existente y guardar la ultima versi\u00F3n
    	        		respDelete=OperacionVisorTicket.deleteVisor(conn,  objDatos.getIdVenta(), ID_PAIS);
    	        		
    	        		if(respDelete>0){
    	        			OperacionVisorTicket.insertaLineaTicket(conn, objDatos.getLineas(), objDatos.getIdVenta(), objDatos.getUsuario(),objDatos.getCodDispositivo(), ID_PAIS);
    	        			
    	        			objRespuesta = getMensaje(Conf_Mensajes.OK_REGISTRO_VISOR_TCK_86, null, null, null, "", objDatos.getCodArea());
    	        			  
    	        			listaLog.add(ControladorBase.addLog(Conf.LOG_REGISTRA_VISOR_TCK, servicioPost, objDatos.getIdVenta() ,
                                    Conf.LOG_TIPO_VENTA, "Se registro visor de ticket para venta: " + objDatos.getIdVenta()+ ".", ""));
    	        		}
    	        	}
    	        }else{
    	        	 objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_VENTA_924,null, this.getClass().toString(), metodo,
    		                    "", objDatos.getCodArea());
    	        	listaLog.add(ControladorBase.addLog(Conf.LOG_REGISTRA_VISOR_TCK, servicioPost, null ,
                            null, "Se intenta registrar un visor de ticket para una venta que no existe en BDD",""));
    	        }
    		}
    		
		} catch (SQLException e) {
			log.error(e,e);
			 objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());

	            listaLog.add(ControladorBase.addLog(Conf.LOG_REGISTRA_VISOR_TCK, servicioPost, "0",
	                    Conf.LOG_TIPO_NINGUNO, "Problema al registrar visor de ticket.", e.getMessage()));
		} catch (Exception e) {
			log.error(e,e);
			   objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
	                    "", objDatos.getCodArea());
	            listaLog.add(ControladorBase.addLog(Conf.LOG_REGISTRA_VISOR_TCK, servicioPost, "0",
	                    Conf.LOG_TIPO_NINGUNO, "Problema al registrar visor de ticket.", e.getMessage()));
		}finally{
			DbUtils.closeQuietly(conn);
		}
    	
    	
    	return objRespuesta;
    }
    
    public OutputTicket getTicket (InputTicket objDatos){
    	OutputTicket objReturn = new OutputTicket();
    	Respuesta objRespuesta;
    	String metodo="getTicket";
    	Connection conn=null;
    	 List<Integer> lstExisteVenta;
    	try {
    		conn=getConnRegional();
    		ID_PAIS =  getIdPais(conn, objDatos.getCodArea());	
    	
	    	if(objDatos.getIdVenta()==null || "".equals(objDatos.getIdVenta())){
	      	  objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDVENTA_NULO_921, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
	
	      }else if (!isNumeric(objDatos.getIdVenta())){
	      	 objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDVENTA_INVALIDO_922, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
	     	  
	      }else{
	    	  
	    	  //Validar si venta existe
  	        lstExisteVenta=OperacionVisorTicket.validarVenta(conn, objDatos.getIdVenta(), ID_PAIS);
  	        
  	        if(lstExisteVenta.get(0)==1){
		    	  objReturn=OperacionVisorTicket.getLineas(conn, objDatos.getIdVenta(), objDatos.getCodArea(),ID_PAIS);
		    	  OlsDAO ols = new OlsDAO();
		    	  if(ols.getVentaSincronizada(conn, new BigDecimal(objDatos.getIdVenta())).intValue() != 0) {
		    		  if(ols.getBillLinkOLS(conn, new BigDecimal(objDatos.getIdVenta()))==null) {
		    			  objReturn.setLinkOLS("PENDIENTE");
		    		  }else {
		    			  objReturn.setLinkOLS(ols.getBillLinkOLS(conn, new BigDecimal(objDatos.getIdVenta())));
		    		  }
		    	  }
		    	  
		    	  objRespuesta = getMensaje(Conf_Mensajes.OK_GET_TICKET_87, null, null, null, "", objDatos.getCodArea());
				  
	  			listaLog.add(ControladorBase.addLog(Conf.LOG_OBTIENE_VISOR_TCK, servicioPost, objDatos.getIdVenta() ,
	                      Conf.LOG_TIPO_VENTA, "Se registro visor de ticket para venta: " + objDatos.getIdVenta()+ ".", ""));
  	        }else{
	        	 objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_VENTA_924,null, this.getClass().toString(), metodo,
		                    "", objDatos.getCodArea());
	        	listaLog.add(ControladorBase.addLog(Conf.LOG_OBTIENE_VISOR_TCK, servicioPost, null ,
                     null, "Se intenta registrar un visor de ticket para una venta que no existe en BDD",""));
	        }
	      }
    	}catch (SQLException e) {
			log.error(e,e);
			 objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());

	            listaLog.add(ControladorBase.addLog(Conf.LOG_OBTIENE_VISOR_TCK, servicioPost, "0",
	                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar visor de ticket.", e.getMessage()));
		} catch (Exception e) {
			log.error(e,e);
			   objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
	                    "", objDatos.getCodArea());
	            listaLog.add(ControladorBase.addLog(Conf.LOG_OBTIENE_VISOR_TCK, servicioPost, "0",
	                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar visor de ticket.", e.getMessage()));
		}finally{
			DbUtils.closeQuietly(conn);
		}
    	objReturn.setRespuesta(objRespuesta);
    	
    	
    	return objReturn;
    }
    
}
