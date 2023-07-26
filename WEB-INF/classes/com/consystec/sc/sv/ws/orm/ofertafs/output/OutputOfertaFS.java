package com.consystec.sc.sv.ws.orm.ofertafs.output;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputOfertaFS")
public class OutputOfertaFS {
	@XmlElement
    private List<Ofertas> oferta;
	@XmlElement
	private Respuesta respuesta;
	public List<Ofertas> getOferta() {
		return oferta;
	}
	public void setOferta(List<Ofertas> oferta) {
		this.oferta = oferta;
	}
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	
}
