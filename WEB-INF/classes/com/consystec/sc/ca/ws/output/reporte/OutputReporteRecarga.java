package com.consystec.sc.ca.ws.output.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputReporteRecarga")
public class OutputReporteRecarga {
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<Recargas> recargas;
	
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	public List<Recargas> getReccargas() {
		return recargas;
	}
	public void setReccargas(List<Recargas> reccargas) {
		this.recargas = reccargas;
	}
}
