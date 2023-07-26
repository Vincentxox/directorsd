package com.consystec.sc.ca.ws.input.pdv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputTelefonoRecarga")
public class InputTelefonoRecargaPayment {

	@XmlElement
	private String codArea;

	@XmlElement
	private String usuario;

	@XmlElement
	private String token;

	@XmlElement
	private String numero;

	@XmlElement
	private String codDispositivo;

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCodDispositivo() {
		return codDispositivo;
	}

	public void setCodDispositivo(String codDispositivo) {
		this.codDispositivo = codDispositivo;
	}

}
