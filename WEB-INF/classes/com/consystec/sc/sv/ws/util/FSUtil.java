package com.consystec.sc.sv.ws.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.consystec.sc.ca.ws.orm.Respuesta;
import com.telefonica.globalintegration.fault.OperationFaultType;
import com.telefonica.globalintegration.header.ArgType;
import com.telefonica.globalintegration.header.HeaderInType;
import com.telefonica.globalintegration.header.ValuesType;
import com.telefonica.globalintegration.header.VarArgType;

public class FSUtil extends ControladorBase{
	
	public final static String TXT_FORMATO_OSB_FECHA = "yyyy-MM-dd'T'HH:mm:ss'.0-06:00'";
	
	
	public final static String HOST = "localhost";
	
	public static HeaderInType instanciaHeader(String usuario, String area){
		final String COUNTRY = getPais(area);
		
		final String LANG = "es";//In
		final String ENTITY = "3nt1ty";
		final String SYSTEM = "SIDRA";
		final String SUBSYSTEM = "4";
		final String OPERATION = "11";
		
		
		HeaderInType header = new HeaderInType();
		header.setCountry(COUNTRY);
		header.setLang(LANG);
		header.setEntity(ENTITY);
		header.setSystem(SYSTEM);
		header.setSubsystem(SUBSYSTEM);
		header.setOriginator(COUNTRY+":"+ENTITY+":"+SYSTEM+":"+SUBSYSTEM);
		header.setUserId(usuario);
		header.setWsIp(getIp());
		header.setOperation(OPERATION);
		header.setDestination(COUNTRY+":"+ENTITY+":"+SYSTEM+":"+SUBSYSTEM);
		header.setPid(getUUID());
		header.setExecId(getUUID());

		header.setTimestamp(getTimestamp());
		
		
		
		//TODO IMPLEMENTACION JAR
		ArgType g = new ArgType();
		g.setKey("?");
		
		
		
		ValuesType values = new ValuesType();
		List<String> val = values.getValue();
		val.add("?");
		
		g.setValues(values);
		
		VarArgType var = new VarArgType();
		List<ArgType> args = var.getArg();
		args.add(g);
		
		header.setVarArg(var);
		
		return header;
		
	}
	
	private static String getTimestamp(){
		return ZonedDateTime.now( ZoneId.systemDefault()).format( DateTimeFormatter.ISO_OFFSET_DATE_TIME );
	}
	
	private static String getUUID(){
		 //generate random UUIDs
	    UUID idOne = UUID.randomUUID();
	    return idOne.toString();
	}
	


	private static String getIp() {
		InetAddress ownIP;
		String dir = "0.0.0.0:0";
		try {
			ownIP = InetAddress.getLocalHost();
			dir = ownIP.getHostAddress();
		} catch (UnknownHostException e) {
			log.error(e,e);
		}
		return dir;
	}
	
	/** Convertir ISO 8601 string to Date. */
    public static Date toDate(String iso8601string)
            throws ParseException {
    	 SimpleDateFormat FORMATO_OSB_FECHA = new SimpleDateFormat(TXT_FORMATO_OSB_FECHA); 
        return FORMATO_OSB_FECHA.parse(iso8601string);
    }
    
    /**
     * Parsea el codigo de uso del valor obtenido del ws OSB
     * @param desCodUso
     * @return
     */
    public static String parsearCodUso(String desCodUso){
    	String codUso = "";    	
    	if(desCodUso != null && !"".equals(desCodUso)){
    		if("postpaid".equals(desCodUso.toLowerCase())){
    			codUso = "2";
    		}
    		if("prepaid".equals(desCodUso.toLowerCase())){
    			codUso = "3";
    		}
    		if("hybrid".equals(desCodUso.toLowerCase())){
    			codUso = "10";
    		}
    	}
    	return codUso;
    }
    
    /**
     * 
     * @param mensajeOSB
     * @param origen
     * @param metodoActual
     * @return
     */
    public  Respuesta getMensajeErrorOSB(OperationFaultType mensajeOSB,
    		Class<?> origen,
    		String metodoActual, String codArea){
    	String nombreClase = new CurrentClassGetter().getClassName();
    	Respuesta mensaje ;
    	if(mensajeOSB != null){
    		mensaje = getMensaje(Conf_Mensajes.MSJ_ERROR_WS_OSB_913, null, origen.getName(), metodoActual, null, codArea);
    		 String errorGeneral = "(" + mensajeOSB.getExceptionCategory() + mensajeOSB.getExceptionCode() + ") " + mensajeOSB.getExceptionMessage();
    	      mensaje.setExcepcion(mensaje.getExcepcion() + " - " + errorGeneral + " - "
    	        + "ExceptionAppCode: " + mensajeOSB.getAppDetail().getExceptionAppCode() + " "
    	        + mensajeOSB.getAppDetail().getExceptionAppMessage() + ": " 
    	        + mensajeOSB.getAppDetail().getExceptionAppCause());
    		mensaje.setOrigen("SERVICIOS OSB");
    	}else{
    		mensaje = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, null, nombreClase, metodoActual, null,codArea);
    	}
    	return mensaje;
    }
}
