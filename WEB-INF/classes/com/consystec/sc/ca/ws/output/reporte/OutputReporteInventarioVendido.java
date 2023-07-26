package com.consystec.sc.ca.ws.output.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputInventarioVendido")
public class OutputReporteInventarioVendido {

	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<DetalleVendido> detalle;
	
	public Respuesta getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}

	public List<DetalleVendido> getDetalle() {
		return detalle;
	}

	public void setDetalle(List<DetalleVendido> detalle) {
		this.detalle = detalle;
	}
}
