package com.consystec.sc.ca.ws.output.portabilidad;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputPortabilidad")
public class OutputPortabilidad {
	@XmlElement
    private String token;
	@XmlElement
    private String donante;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<VentaPorta> ventas;
	
	public String getDonante() {
		return donante;
	}
	public void setDonante(String donante) {
		this.donante = donante;
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
	public List<VentaPorta> getVentas() {
		return ventas;
	}
	public void setVentas(List<VentaPorta> ventas) {
		this.ventas = ventas;
	}
	
}