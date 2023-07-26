package com.consystec.sc.ca.ws.output.descuentoFS;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputDescuentoFS")
public class OutputDescuentoFS {
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<Descuentos> listaDescuentos;
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	public List<Descuentos> getListaDescuentos() {
		return listaDescuentos;
	}
	public void setListaDescuentos(List<Descuentos> listaDescuentos) {
		this.listaDescuentos = listaDescuentos;
	}
}
