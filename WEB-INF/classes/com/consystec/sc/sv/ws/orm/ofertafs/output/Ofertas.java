package com.consystec.sc.sv.ws.orm.ofertafs.output;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputOfertaFS")
public class Ofertas {
	@XmlElement
	private String idProductOffering;
	@XmlElement
	private String nombre;
	@XmlElement
	private String precio;
	@XmlElement
	private String precioMin;
	@XmlElement
	private String precioMax;
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
	public String getPrecio() {
		return precio;
	}
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	public String getPrecioMin() {
		return precioMin;
	}
	public void setPrecioMin(String precioMin) {
		this.precioMin = precioMin;
	}
	public String getPrecioMax() {
		return precioMax;
	}
	public void setPrecioMax(String precioMax) {
		this.precioMax = precioMax;
	}
	
}
