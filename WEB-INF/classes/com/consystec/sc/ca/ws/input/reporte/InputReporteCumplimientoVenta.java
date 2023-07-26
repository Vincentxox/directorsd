package com.consystec.sc.ca.ws.input.reporte;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputReporteCumplimientoVenta")
public class InputReporteCumplimientoVenta {

	@XmlElement
    private String codArea;
	@XmlElement
    private String usuario;
	@XmlElement
    private String idPuntoVenta;
	@XmlElement
    private String idDistribuidor;
	@XmlElement
    private String idJornada;
	@XmlElement
    private String idRuta;
	@XmlElement
	private String fechaInicio;
	@XmlElement
	private String fechaFin;
	@XmlElement
	private String max;
	@XmlElement
	private String min;

	public String getIdPuntoVenta() {
		return idPuntoVenta;
	}
	public void setIdPuntoVenta(String idPuntoVenta) {
		this.idPuntoVenta = idPuntoVenta;
	}
	public String getIdRuta() {
		return idRuta;
	}
	public void setIdRuta(String idRuta) {
		this.idRuta = idRuta;
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
	public String getIdJornada() {
		return idJornada;
	}
	public void setIdJornada(String idJornada) {
		this.idJornada = idJornada;
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
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
}
