package com.consystec.sc.ca.ws.output.consultas;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;




@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputgetVentaFE")
public class OutputgetVentaFE {

	@XmlElement
	private String token ;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<VentasFE> ventas;
	
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
	public List<VentasFE> getVentas() {
		return ventas;
	}
	public void setVentas(List<VentasFE> ventas) {
		this.ventas = ventas;
	}
	
	
	
}
