package com.consystec.sc.ca.ws.input.general;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="sesionparametros")
public class InputSesionParametros {

	@XmlElement
	private String usuario;
	@XmlElement
	private String codArea;
	@XmlElement
	private String password;
	@XmlElement
	private String codDispositivo;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getCodDispositivo() {
		return codDispositivo;
	}
	public void setCodDispositivo(String codDispositivo) {
		this.codDispositivo = codDispositivo;
	}


	
	
}
