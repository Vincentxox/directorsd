package com.consystec.sc.ca.ws.input.reporte;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputReporteInventarioVendido")
public class InputReporteInventarioVendido {

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
    private String idVendedor;
	@XmlElement
    private String idTipo;
	@XmlElement
    private String tipo;
	@XmlElement
    private String fechaInicio;
	@XmlElement
    private String fechaFin;
	
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
	public String getIdVendedor() {
		return idVendedor;
	}
	public void setIdVendedor(String idVendedor) {
		this.idVendedor = idVendedor;
	}
	public String getIdTipo() {
		return idTipo;
	}
	public void setIdTipo(String idTipo) {
		this.idTipo = idTipo;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
}
