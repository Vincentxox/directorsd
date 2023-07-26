package com.consystec.sc.ca.ws.output.descuentoFS;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.descuentoFS.InputDescuentoFS;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Descuentos")
public class Descuentos {
	@XmlElement
	private String idProductOffering;
	@XmlElement
	private String nombre;
	@XmlElement
	List<InputDescuentoFS> descuentos;
	public String getIdProductOffering() {
		return idProductOffering;
	}
	public void setIdProductOffering(String idProductOffering) {
		this.idProductOffering = idProductOffering;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<InputDescuentoFS> getDescuentos() {
		return descuentos;
	}
	public void setDescuentos(List<InputDescuentoFS> descuentos) {
		this.descuentos = descuentos;
	}
	
}
