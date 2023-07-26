package com.consystec.sc.ca.ws.output.pdv;

import javax.xml.bind.annotation.XmlElement;

import com.consystec.sc.ca.ws.orm.Respuesta;

public class OutputTelefonoRecargaPayment {

	@XmlElement
	private String token;

	@XmlElement
	private Respuesta respuesta;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Respuesta getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}

}
