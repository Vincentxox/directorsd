package com.consystec.sc.ca.ws.output.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputResumenInventarioVendido")
public class OutputResumenInventarioVendido {

	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<DetalleArticulo> resumen;
	
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	public List<DetalleArticulo> getResumen() {
		return resumen;
	}
	public void setResumen(List<DetalleArticulo> resumen) {
		this.resumen = resumen;
	}
}
