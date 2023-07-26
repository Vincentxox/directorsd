package com.consystec.sc.ca.ws.output.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputReporteEfectividadVenta")
public class OutputReporteEfectividadVenta {

	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<DatosEfectividad> datosEfectividad;
	
	public List<DatosEfectividad> getDatosEfectividad() {
		return datosEfectividad;
	}

	public void setDatosEfectividad(List<DatosEfectividad> datosEfectividad) {
		this.datosEfectividad = datosEfectividad;
	}

	public Respuesta getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
}
