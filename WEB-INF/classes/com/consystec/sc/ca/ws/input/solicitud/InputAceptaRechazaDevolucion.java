package com.consystec.sc.ca.ws.input.solicitud;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputAceptaRechazaDevolucion")
public class InputAceptaRechazaDevolucion {
	@XmlElement
	private String codArea;
	@XmlElement
	private String usuario;
	@XmlElement
	private String idSolicitud;
	@XmlElement
	private String idBodega;
	@XmlElement
	private String observaciones;
	@XmlElement
	private String idBuzonSiguiente;
	@XmlElement
	private List<ArticulosDevolucion> articulos;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(String idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public List<ArticulosDevolucion> getArticulos() {
		return articulos;
	}
	public void setArticulos(List<ArticulosDevolucion> articulos) {
		this.articulos = articulos;
	}
	public String getIdBodega() {
		return idBodega;
	}
	public void setIdBodega(String idBodega) {
		this.idBodega = idBodega;
	}
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getIdBuzonSiguiente() {
		return idBuzonSiguiente;
	}
	public void setIdBuzonSiguiente(String idBuzonSiguiente) {
		this.idBuzonSiguiente = idBuzonSiguiente;
	}
	
	
	
	
}
