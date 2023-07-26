package com.consystec.sc.ca.ws.output.ingresosalida;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputIngresoSalida")
public class OutputIngresoSalida {
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<ArticulosIncorrectos> datosIncorrectos;

	public Respuesta getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}

	public List<ArticulosIncorrectos> getDatosIncorrectos() {
		return datosIncorrectos;
	}

	public void setDatosIncorrectos(List<ArticulosIncorrectos> datosIncorrectos) {
		this.datosIncorrectos = datosIncorrectos;
	}
	
	
	

}
