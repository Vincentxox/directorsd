package com.consystec.sc.ca.ws.input.portabilidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputEstadoPortabilidad")
public class InputEstadoPortabilidad {
	@XmlElement
	private String token;
	@XmlElement
	private String codArea;
	@XmlElement
	private String usuario;
	@XmlElement
	private String codDispositivo;
	@XmlElement
	private String idJornada;
	@XmlElement
	private String idVendedor;
	@XmlElement
	private String numeroaPortar;
	@XmlElement
	private String numeroTemporal;
	@XmlElement
	private String fechaInicio;
	@XmlElement
	private String fechaFin;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
	public String getCodDispositivo() {
		return codDispositivo;
	}
	public void setCodDispositivo(String codDispositivo) {
		this.codDispositivo = codDispositivo;
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
	public String getNumeroaPortar() {
		return numeroaPortar;
	}
	public void setNumeroaPortar(String numeroaPortar) {
		this.numeroaPortar = numeroaPortar;
	}
	public String getNumeroTemporal() {
		return numeroTemporal;
	}
	public void setNumeroTemporal(String numeroTemporal) {
		this.numeroTemporal = numeroTemporal;
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
