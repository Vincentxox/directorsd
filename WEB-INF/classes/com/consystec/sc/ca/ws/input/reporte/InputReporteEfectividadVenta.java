package com.consystec.sc.ca.ws.input.reporte;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputReporteEfectividadVenta")
public class InputReporteEfectividadVenta {

	@XmlElement
    private String codArea;
	@XmlElement
    private String usuario;
	@XmlElement
    private String idDistribuidor;
	@XmlElement
    private String idBodegaVirtual;
	@XmlElement
    private String idJornada;
	@XmlElement
    private String idRuta;
	@XmlElement
    private String zona;
	@XmlElement
    private String fechaInicioMesActual;
	@XmlElement
    private String fechaFinMesActual;
	@XmlElement
    private String fechaInicioMesAnterior;
	@XmlElement
    private String fechaFinMesAnterior;
	
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getIdDistribuidor() {
		return idDistribuidor;
	}
	public void setIdDistribuidor(String idDistribuidor) {
		this.idDistribuidor = idDistribuidor;
	}
	public String getIdBodegaVirtual() {
		return idBodegaVirtual;
	}
	public void setIdBodegaVirtual(String idBodegaVirtual) {
		this.idBodegaVirtual = idBodegaVirtual;
	}
	public String getIdJornada() {
		return idJornada;
	}
	public void setIdJornada(String idJornada) {
		this.idJornada = idJornada;
	}
	public String getIdRuta() {
		return idRuta;
	}
	public void setIdRuta(String idRuta) {
		this.idRuta = idRuta;
	}
	public String getFechaInicioMesActual() {
		return fechaInicioMesActual;
	}
	public void setFechaInicioMesActual(String fechaInicioMesActual) {
		this.fechaInicioMesActual = fechaInicioMesActual;
	}
	public String getFechaFinMesActual() {
		return fechaFinMesActual;
	}
	public void setFechaFinMesActual(String fechaFinMesActual) {
		this.fechaFinMesActual = fechaFinMesActual;
	}
	public String getFechaInicioMesAnterior() {
		return fechaInicioMesAnterior;
	}
	public void setFechaInicioMesAnterior(String fechaInicioMesAnterior) {
		this.fechaInicioMesAnterior = fechaInicioMesAnterior;
	}
	public String getFechaFinMesAnterior() {
		return fechaFinMesAnterior;
	}
	public void setFechaFinMesAnterior(String fechaFinMesAnterior) {
		this.fechaFinMesAnterior = fechaFinMesAnterior;
	}
}
