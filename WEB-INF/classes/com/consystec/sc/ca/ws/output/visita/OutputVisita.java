package com.consystec.sc.ca.ws.output.visita;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.visita.InputVisita;
import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputVisita")
public class OutputVisita {
	@XmlElement
	private String token;
	@XmlElement
	private String idVisita;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<InputVisita> visitas ;

	public Respuesta getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}

	public List<InputVisita> getVisitas() {
		return visitas;
	}

	public void setVisitas(List<InputVisita> visitas) {
		this.visitas = visitas;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getIdVisita() {
		return idVisita;
	}

	public void setIdVisita(String idVisita) {
		this.idVisita = idVisita;
	}
	
	
}
