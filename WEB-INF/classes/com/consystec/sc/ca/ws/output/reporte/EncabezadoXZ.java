package com.consystec.sc.ca.ws.output.reporte;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "EncabezadoXZ")
public class EncabezadoXZ {

	@XmlElement
	private String codDispositivo;
	@XmlElement
	private String resolucion;
	@XmlElement
	private String fechaResolucion;
	@XmlElement
	private String rangoInicial;
	@XmlElement
	private String rangoFinal;
	@XmlElement
	private String caja;
	@XmlElement
	private String terminal;
	@XmlElement
	private String zona;
	
	public String getCodDispositivo() {
		return codDispositivo;
	}
	public void setCodDispositivo(String codDispositivo) {
		this.codDispositivo = codDispositivo;
	}
	public String getResolucion() {
		return resolucion;
	}
	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}
	public String getFechaResolucion() {
		return fechaResolucion;
	}
	public void setFechaResolucion(String fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}
	public String getRangoInicial() {
		return rangoInicial;
	}
	public void setRangoInicial(String rangoInicial) {
		this.rangoInicial = rangoInicial;
	}
	public String getRangoFinal() {
		return rangoFinal;
	}
	public void setRangoFinal(String rangoFinal) {
		this.rangoFinal = rangoFinal;
	}
	public String getCaja() {
		return caja;
	}
	public void setCaja(String caja) {
		this.caja = caja;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}
	
	
}
