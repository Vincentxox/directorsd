package com.ericsson.sc.ca.ws.output.reenviosms.movil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputReenvioSMS")
public class OutputReenvioSMS {
	@XmlElement
	private Respuesta respuesta;

	public Respuesta getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}

	@Override
	public String toString() {
		return "OutputReenvioSMS [respuesta=" + respuesta + "]";
	}

}
