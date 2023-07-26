package com.consystec.sc.ca.ws.so.request;

public class ConsultaFinalizarOrdenVenta {

	private String idOrdenVenta;
	private String usuario;
	private String pais;
	
	public String getIdOrdenVenta() {
		return idOrdenVenta;
	}
	public void setIdOrdenVenta(String idOrdenVenta) {
		this.idOrdenVenta = idOrdenVenta;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
}