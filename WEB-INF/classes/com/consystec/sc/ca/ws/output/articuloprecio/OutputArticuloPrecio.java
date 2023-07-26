package com.consystec.sc.ca.ws.output.articuloprecio;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.articuloprecio.InputArticuloPrecio;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputArticuloPrecio")
public class OutputArticuloPrecio {

	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	List<InputArticuloPrecio> articulosPrecio;
	
	
	public List<InputArticuloPrecio> getArticulosPrecio() {
		return articulosPrecio;
	}
	public void setArticulosPrecio(List<InputArticuloPrecio> articulosPrecio) {
		this.articulosPrecio = articulosPrecio;
	}
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	
	

}
