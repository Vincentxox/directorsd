package com.consystec.sc.ca.ws.input.pdv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputPDVDisponible")
public class InputPDVDisponible {
	@XmlElement
	private String token;
	@XmlElement
	private String codArea;
	@XmlElement
	private String usuario;
	@XmlElement
	private String idPDV;
	@XmlElement
	private String idDTS;
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getIdPDV() {
		return idPDV;
	}
	public void setIdPDV(String idPDV) {
		this.idPDV = idPDV;
	}
	public String getIdDTS() {
		return idDTS;
	}
	public void setIdDTS(String idDTS) {
		this.idDTS = idDTS;
	}
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
	
	
}
