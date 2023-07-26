package com.consystec.sc.ca.ws.output.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class DetalleVendido {

	@XmlElement
	private String tipoProducto;
	@XmlElement
	private List<DetalleArticulo> articulos;
	
	public String getTipoProducto() {
		return tipoProducto;
	}
	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}
	public List<DetalleArticulo> getArticulos() {
		return articulos;
	}
	public void setArticulos(List<DetalleArticulo> articulos) {
		this.articulos = articulos;
	}
}
