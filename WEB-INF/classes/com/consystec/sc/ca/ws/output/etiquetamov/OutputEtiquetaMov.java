package com.consystec.sc.ca.ws.output.etiquetamov;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputEtiquetaMov")
public class OutputEtiquetaMov {

	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<RListPantalla> pantallas;
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	public List<RListPantalla> getPantallas() {
		return pantallas;
	}
	public void setPantallas(List<RListPantalla> pantallas) {
		this.pantallas = pantallas;
	}
	
	
}
