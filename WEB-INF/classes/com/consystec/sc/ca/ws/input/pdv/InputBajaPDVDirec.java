package com.consystec.sc.ca.ws.input.pdv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputBajaPDVDirec")
public class InputBajaPDVDirec {

    @XmlElement
    private String codArea;
	@XmlElement
	private String token;
	@XmlElement
	private String codDispositivo;
	@XmlElement
	private InputBajaPDV datos;
	
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public InputBajaPDV getDatos() {
		return datos;
	}
	public void setDatos(InputBajaPDV datos) {
		this.datos = datos;
	}
	public String getCodDispositivo() {
		return codDispositivo;
	}
	public void setCodDispositivo(String codDispositivo) {
		this.codDispositivo = codDispositivo;
	}
	
	
	
}
