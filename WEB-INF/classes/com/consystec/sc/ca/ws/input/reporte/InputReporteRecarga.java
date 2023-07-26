package com.consystec.sc.ca.ws.input.reporte;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputReporteRecarga")
public class InputReporteRecarga {
	@XmlElement
    private String codArea;
    @XmlElement
	private String usuario;
    @XmlElement
    private String idDts;
    @XmlElement
    private String tipo;
    @XmlElement
    private String idTipo;
    @XmlElement
    private String idBodega;
    @XmlElement
    private String fechaInicio;
    @XmlElement
    private String fechaFin;
    
	public String getIdDts() {
		return idDts;
	}
	public void setIdDts(String idDts) {
		this.idDts = idDts;
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
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getIdTipo() {
		return idTipo;
	}
	public void setIdTipo(String idTipo) {
		this.idTipo = idTipo;
	}
	public String getIdBodega() {
		return idBodega;
	}
	public void setIdBodega(String idBodega) {
		this.idBodega = idBodega;
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
