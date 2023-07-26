package com.consystec.sc.ca.ws.input.ticket;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Lineas")
public class Lineas {
	@XmlElement
	public String izquierda;
	@XmlElement
	public String centro;
	@XmlElement
	public String derecha;
	@XmlElement
	public String alineacion;
	@XmlElement
	public String estilo;
	@XmlElement
	public String extra;
	public String getIzquierda() {
		return izquierda;
	}
	public void setIzquierda(String izquierda) {
		this.izquierda = izquierda;
	}
	public String getCentro() {
		return centro;
	}
	public void setCentro(String centro) {
		this.centro = centro;
	}
	public String getDerecha() {
		return derecha;
	}
	public void setDerecha(String derecha) {
		this.derecha = derecha;
	}
	public String getAlineacion() {
		return alineacion;
	}
	public void setAlineacion(String alineacion) {
		this.alineacion = alineacion;
	}
	public String getEstilo() {
		return estilo;
	}
	public void setEstilo(String estilo) {
		this.estilo = estilo;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	
	
	
}
