package com.consystec.sc.ca.ws.output.solicitud;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputDeuda")
public class OutputDeuda {
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<EncabezadoDeuda> listaSolicitudes;
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	public List<EncabezadoDeuda> getListaSolicitudes() {
		return listaSolicitudes;
	}
	public void setListaSolicitudes(List<EncabezadoDeuda> listaSolicitudes) {
		this.listaSolicitudes = listaSolicitudes;
	}
	
	
}
