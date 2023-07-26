package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.ws.Holder;

import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.portabilidad.InputPortabilidad;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.portabilidad.OutputPortabilidad;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.FSUtil;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.telefonica.globalintegration.header.HeaderInType;
import com.telefonica.globalintegration.header.HeaderOutType;
import com.telefonica.globalintegration.services.getdonororganization.v1.GetDonorOrganizationRequestType;
import com.telefonica.globalintegration.services.getdonororganization.v1.GetDonorOrganizationResponseType;
import com.telefonica.globalintegration.services.soap.getdonororganization.v1.GetDonorOrganizationBindingQSService;
import com.telefonica.globalintegration.services.soap.getdonororganization.v1.GetDonorOrganizationV1;
import com.telefonica.globalintegration.soap.validatenip.v1.ValidateNipRequestType;
import com.telefonica.globalintegration.soap.validatenip.v1.ValidateNipResponseType;
import com.telefonica.wsdl.globalintegration.soap.basename.v1.validatenip.ValidateNip;
import com.telefonica.wsdl.globalintegration.soap.basename.v1.validatenip.ValidateNipBindingQSService;

public class OperacionPortabilidad {
	private OperacionPortabilidad(){}
	private static final Logger log = Logger.getLogger(OperacionPortabilidad.class);
	
	public static OutputPortabilidad doGet(Connection conn, InputPortabilidad input) throws MalformedURLException, SQLException{
		String nombreMetodo = "doGet";
    	String nombreClase = new CurrentClassGetter().getClassName();
    	Respuesta respuesta ;
		OutputPortabilidad out = new OutputPortabilidad();
		
		
		if(input.getValidar().trim().equals("1")){
			try {
				OutputPortabilidad validaCIP =  validarCip(conn, input);
				respuesta = validaCIP.getRespuesta();
				out.setRespuesta(respuesta);
			} catch (MalformedURLException e) {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
				log.error("Excepcion: " + e.getMessage(), e);
				out.setRespuesta(respuesta);
				log.error(e, e);
			} catch (SQLException e) {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
				log.error("Excepcion: " + e.getMessage(), e);
				out.setRespuesta(respuesta);
			}
		}else if(input.getValidar().trim().equals("2")){
			OutputPortabilidad validarDonate = validarDonante(conn, input); 
			
			if(validarDonate.getDonante() != null && !validarDonate.getDonante().trim().equals("")){
				out.setRespuesta(validarDonate.getRespuesta());
				out.setDonante(validarDonate.getDonante());
			}else{
				out.setRespuesta(validarDonate.getRespuesta());
			}
		}else if(input.getValidar().trim().equals("3")){
			OutputPortabilidad validaCIP3 =  validarCip(conn, input);
			
			if(new BigDecimal(validaCIP3.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.MSJ_OK_VALIDAR_CIP){
				OutputPortabilidad validarDonate3 = validarDonante(conn, input); 
				
				if(validarDonate3.getDonante() != null && !validarDonate3.getDonante().trim().equals("")){
					out.setRespuesta(validarDonate3.getRespuesta());
					out.setDonante(validarDonate3.getDonante());
				}else{
					out.setRespuesta(validarDonate3.getRespuesta());
				}
			}else{
				respuesta = validaCIP3.getRespuesta();
				out.setRespuesta(respuesta);
			}
		}else{
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NUM_VALIDACION_NO_EXISTE_644, null, nombreClase, nombreMetodo, " "+input.getValidar()+" no existe.", input.getCodArea());
			out.setRespuesta(respuesta);
		}
		
		return out;
    }
	
	public static OutputPortabilidad validarCip(Connection conn, InputPortabilidad input) throws MalformedURLException, SQLException{
		String nombreMetodo = "validaCip";
    	String nombreClase = new CurrentClassGetter().getClassName();
    	Respuesta respuesta = new Respuesta();
    	OutputPortabilidad output = new OutputPortabilidad();
    	
    	URL endpoint = new URL(UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_VALIDAR_CIP, input.getCodArea()));
    	
    	ValidateNipBindingQSService ws = new ValidateNipBindingQSService(endpoint);
    	ValidateNip w = ws.getValidateNipBindingQSPort();
    	ValidateNipRequestType request = new ValidateNipRequestType();
    	ValidateNipResponseType response = new ValidateNipResponseType();
    	Holder<HeaderOutType> hres = new Holder<HeaderOutType>();
	 	Holder<ValidateNipResponseType> resp = new Holder<ValidateNipResponseType>();
   		
   		try {
   			request.setMsisdn(input.getNumTelefono());
   	    	request.setNIP(input.getNip());
   	   		HeaderInType h = FSUtil.instanciaHeader(input.getUsuario(), input.getCodArea());

   	   		w.validateNip(request, h, resp, hres);
			response = resp.value;
	    	
	   		if(response.getRequestId() != null || !response.getRequestId().trim().equals("")){
	   			respuesta =  new ControladorBase().getMensaje(Conf_Mensajes.MSJ_OK_VALIDAR_CIP, null, nombreClase, nombreMetodo, null, input.getCodArea());
	   			log.trace(response.getRequestId());
	   		}
		} catch (com.telefonica.wsdl.globalintegration.soap.basename.v1.validatenip.MessageFault e1) {
			
			respuesta =  new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_VALIDAR_CIP_642, e1.getFaultInfo().getAppDetail().getExceptionAppCode(), nombreClase, nombreMetodo, e1.getFaultInfo().getAppDetail().getExceptionAppMessage(), input.getCodArea());
			e1.printStackTrace();
		}
   		
    	output.setRespuesta(respuesta);
        return output;
	}
	
	public static OutputPortabilidad validarDonante(Connection conn, InputPortabilidad input) throws MalformedURLException, SQLException{
		String nombreMetodo = "validarDonante";
    	String nombreClase = new CurrentClassGetter().getClassName();
    	Respuesta respuesta = new Respuesta();
    	OutputPortabilidad output = new OutputPortabilidad();
    	
    	URL endpoint = new URL(UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_VALIDAR_DONANTE, input.getCodArea()));
    	
    	GetDonorOrganizationBindingQSService ws = new GetDonorOrganizationBindingQSService(endpoint);
    	GetDonorOrganizationV1 w = ws.getGetDonorOrganizationBindingQSPort();
    	GetDonorOrganizationRequestType request = new GetDonorOrganizationRequestType();
    	GetDonorOrganizationResponseType response = new GetDonorOrganizationResponseType();
    	
    	Holder<HeaderOutType> hres = new Holder<HeaderOutType>();
   		
   		try {
   			request.setMsisdn(input.getNumTelefono());
   	   		HeaderInType h = FSUtil.instanciaHeader(input.getUsuario(), input.getCodArea());
			response = w.getDonorOrganization(request, h, hres);
		} catch (com.telefonica.globalintegration.services.soap.getdonororganization.v1.MessageFault e) {
			if(e.getFaultInfo().getExceptionCode() == 1050){
				respuesta =  new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NO_OPERADOR_DONANTE, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
				log.debug("el numero de telefono no pertenece a ningun operador");
			}else{
				log.debug("mensaje excepcion:   " +e.getMessage());
				respuesta =  new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_VALIDAR_DONANTE_643, e.getMessage(), nombreClase, nombreMetodo, e.getMessage(), input.getCodArea());
				e.printStackTrace();
			}
		}
	   		
   		if(response.getDonorOperatorName() != null && !"".equals(response.getDonorOperatorName().trim())){
   			respuesta =  new ControladorBase().getMensaje(Conf_Mensajes.MSJ_OK_DONANTE_205, null, nombreClase, nombreMetodo, null, input.getCodArea());
   			output.setDonante(response.getDonorOperatorName());
   			
   			log.trace(response.getDonorOperatorName());
   			log.trace(response.getOwnerOperatorName());
   		}
   		
    	output.setRespuesta(respuesta);
        return output;
	}

	
}
