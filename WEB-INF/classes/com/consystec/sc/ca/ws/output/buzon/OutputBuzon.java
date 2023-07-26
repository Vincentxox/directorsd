package com.consystec.sc.ca.ws.output.buzon;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.buzon.InputBuzon;
import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputBuzon")
public class OutputBuzon {

	@XmlElement
	private String idBuzon;
	@XmlElement
	private Respuesta respuesta;
	
	@XmlElement
	private List<InputBuzon> buzones;
	
	public String getIdBuzon() {
		return idBuzon;
	}
	public void setIdBuzon(String idBuzon) {
		this.idBuzon = idBuzon;
	}
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	public List<InputBuzon> getBuzones() {
		return buzones;
	}
	public void setBuzones(List<InputBuzon> buzones) {
		this.buzones = buzones;
	}
	
	
}
