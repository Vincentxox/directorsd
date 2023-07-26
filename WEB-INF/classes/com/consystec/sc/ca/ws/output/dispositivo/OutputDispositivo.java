package com.consystec.sc.ca.ws.output.dispositivo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.dispositivo.InputDispositivo;
import com.consystec.sc.ca.ws.orm.Respuesta;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputDispositivo")
public class OutputDispositivo {
	@XmlElement
	private String idDispositivo;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<InputDispositivo> dispositivos;

	public String getIdDispositivo() {
		return idDispositivo;
	}

	public void setIdDispositivo(String idDispositivo) {
		this.idDispositivo = idDispositivo;
	}

	public Respuesta getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}

	public List<InputDispositivo> getDispositivos() {
		return dispositivos;
	}

	public void setDispositivos(List<InputDispositivo> dispositivos) {
		this.dispositivos = dispositivos;
	}
	
	
}
