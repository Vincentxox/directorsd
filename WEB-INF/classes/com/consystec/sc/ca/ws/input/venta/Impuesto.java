package com.consystec.sc.ca.ws.input.venta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Impuesto")
public class Impuesto {
	@XmlElement
	private String nombreImpuesto;
	@XmlElement
	private String valor;

	public String getNombreImpuesto() {
		return nombreImpuesto;
	}
	public void setNombreImpuesto(String nombreImpuesto) {
		this.nombreImpuesto = nombreImpuesto;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
}