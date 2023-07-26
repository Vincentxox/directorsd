package com.consystec.sc.ca.ws.input.solicitudescip;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputSolicitudesCip")
public class InputSolicitudesCip {
	@XmlElement
	private String codArea;
	@XmlElement
	private String usuario;
	@XmlElement
	private String codDispositivo;
	@XmlElement
	private String token;
	@XmlElement
	private String numeroaPortar;
	@XmlElement
	private String tipoDocumento;
    @XmlElement
	private String numeroDocumento;
	
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getNumeroaPortar() {
		return numeroaPortar;
	}
	public void setNumeroaPortar(String numeroaPortar) {
		this.numeroaPortar = numeroaPortar;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

}
