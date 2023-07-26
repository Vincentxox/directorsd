package com.consystec.sc.ca.ws.output.reporte;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DesgloseVentas")
public class DesgloseVentas {
	@XmlElement
	private String descripcion;
	@XmlElement
	private String monto;
	@XmlElement
	private String cantVentas;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getMonto() {
		return monto;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public String getCantVentas() {
		return cantVentas;
	}

	public void setCantVentas(String cantVentas) {
		this.cantVentas = cantVentas;
	}
	
}
