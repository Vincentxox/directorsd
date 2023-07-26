package com.consystec.sc.ca.ws.output.portabilidad;


import javax.xml.bind.annotation.XmlElement;
import com.consystec.sc.ca.ws.orm.Respuesta;

public class OutputCreaPortabilidad {
	@XmlElement
	private String idSolicitudPorta;
	@XmlElement
	private Respuesta respuesta;

	public String getIdSolicitudPorta() {
		return idSolicitudPorta;
	}

	public void setIdSolicitudPorta(String idSolicitudPorta) {
		this.idSolicitudPorta = idSolicitudPorta;
	}

	public Respuesta getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	
	
}
