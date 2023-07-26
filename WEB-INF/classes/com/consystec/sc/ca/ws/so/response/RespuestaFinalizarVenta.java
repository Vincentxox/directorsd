package com.consystec.sc.ca.ws.so.response;

import com.consystec.sc.ca.ws.orm.MensajeVW;


public class RespuestaFinalizarVenta {
	
	String idOrdenVenta;
	MensajeVW mensaje;
	
	public String getIdOrdenVenta() {
		return idOrdenVenta;
	}
	public void setIdOrdenVenta(String idOrdenVenta) {
		this.idOrdenVenta = idOrdenVenta;
	}
	public MensajeVW getMensaje() {
		return mensaje;
	}
	public void setMensaje(MensajeVW mensaje) {
		this.mensaje = mensaje;
	}
}