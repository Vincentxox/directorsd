package com.consystec.sc.ca.ws.output.consultas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputConsultaSaldoPayment")
public class OutputConsultaSaldoPayment {

	@XmlElement
	private String token;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private String saldoPayment;
	public String getSaldoPayment() {
		return saldoPayment;
	}
	public void setSaldoPayment(String saldoPayment) {
		this.saldoPayment = saldoPayment;
	}
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	
	
}
