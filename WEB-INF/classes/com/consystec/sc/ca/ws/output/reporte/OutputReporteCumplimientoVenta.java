package com.consystec.sc.ca.ws.output.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputReporteCumplimientoVenta")
public class OutputReporteCumplimientoVenta {

	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<CumplimientoVenta> cumplVenta;
	@XmlElement
	private String cantRegistros;
	
	public String getCantRegistros() {
		return cantRegistros;
	}
	public void setCantRegistros(String cantRegistros) {
		this.cantRegistros = cantRegistros;
	}
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	public List<CumplimientoVenta> getCumplVenta() {
		return cumplVenta;
	}
	public void setCumplVenta(List<CumplimientoVenta> cumplVenta) {
		this.cumplVenta = cumplVenta;
	}
}
