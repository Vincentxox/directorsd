package com.consystec.sc.ca.ws.so.response;

import com.consystec.sc.ca.ws.orm.MensajeVW;



public class RespuestaCrearOrdenVenta {

	String idOrdenVenta;
	String nombreOrdenVenta;
	MensajeVW mensaje;
	
	public String getIdOrdenVenta() {
		return idOrdenVenta;
	}
	public void setIdOrdenVenta(String idOrdenVenta) {
		this.idOrdenVenta = idOrdenVenta;
	}
	public String getNombreOrdenVenta() {
		return nombreOrdenVenta;
	}
	public void setNombreOrdenVenta(String nombreOrdenVenta) {
		this.nombreOrdenVenta = nombreOrdenVenta;
	}
	public MensajeVW getMensaje() {
		return mensaje;
	}
	public void setMensaje(MensajeVW mensaje) {
		this.mensaje = mensaje;
	}
}