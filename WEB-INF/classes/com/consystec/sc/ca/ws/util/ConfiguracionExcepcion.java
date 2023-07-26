package com.consystec.sc.ca.ws.util;

public class ConfiguracionExcepcion extends Exception{
private static final long serialVersionUID = 1L;
	
	public ConfiguracionExcepcion() {
		super();
	}
	
	public ConfiguracionExcepcion(String message, Throwable cause){
		super(message, cause);
	};
	
	public ConfiguracionExcepcion(String message){
		super(message);
	};
}
