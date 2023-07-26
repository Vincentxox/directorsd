package com.consystec.sc.ca.ws.input.inventariopromo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ArticulosIngresoSalida")
public class ArticulosIngresoSalida {

    @XmlElement
    private String codArea;
	@XmlElement
	private String idBodegaVirtual;
	@XmlElement
	private String tipo;
	@XmlElement
	private String idArticulo;
	@XmlElement
	private String cantidad;
	@XmlElement
	private String cantidadDisponible;
	@XmlElement
	private String observaciones;
	
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getIdBodegaVirtual() {
		return idBodegaVirtual;
	}
	public void setIdBodegaVirtual(String idBodegaVirtual) {
		this.idBodegaVirtual = idBodegaVirtual;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getIdArticulo() {
		return idArticulo;
	}
	public void setIdArticulo(String idArticulo) {
		this.idArticulo = idArticulo;
	}
	public String getCantidad() {
		return cantidad;
	}
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getCantidadDisponible() {
		return cantidadDisponible;
	}
	public void setCantidadDisponible(String cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}

	
}
