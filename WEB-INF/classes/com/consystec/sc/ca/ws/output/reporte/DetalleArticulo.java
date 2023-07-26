package com.consystec.sc.ca.ws.output.reporte;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class DetalleArticulo {

	@XmlElement
	private String idArticulo;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String cantidadVendida;
	@XmlElement
	private String montoTotalVendido;
	@XmlElement
	private String tipoProducto;
	
	public String getTipoProducto() {
		return tipoProducto;
	}
	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}
	public String getIdArticulo() {
		return idArticulo;
	}
	public void setIdArticulo(String idArticulo) {
		this.idArticulo = idArticulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getCantidadVendida() {
		return cantidadVendida;
	}
	public void setCantidadVendida(String cantidadVendida) {
		this.cantidadVendida = cantidadVendida;
	}
	public String getMontoTotalVendido() {
		return montoTotalVendido;
	}
	public void setMontoTotalVendido(String montoTotalVendido) {
		this.montoTotalVendido = montoTotalVendido;
	}
}
