package com.consystec.sc.ca.ws.output.generico;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="OutputServidor")
public class OutputServidor {

	@XmlElement
	private Respuesta respuesta;
	
	@XmlElement
	private List<UrlServidor> servidores;
	
	
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	public List<UrlServidor> getServidores() {
		return servidores;
	}
	public void setServidores(List<UrlServidor> servidores) {
		this.servidores = servidores;
	}


	
	
}
