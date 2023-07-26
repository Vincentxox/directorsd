package com.consystec.sc.ca.ws.output.reporte;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ComparacionVentaArticulo {

	@XmlElement
	private String idArticulo;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String tipoProducto;
	@XmlElement
	private String cantProductoFacturadoActual;
	@XmlElement
	private String cantProductoFacturadoAnterior;
	@XmlElement
	private String montoTotalFacturadoActual;
	@XmlElement
	private String montoTotalFacturadoAnterior;
	@XmlElement
	private String diferencia;
	@XmlElement
	private String variacion;
	private String diames;
	
	public String getDiames() {
		return diames;
	}
	public void setDiames(String diames) {
		this.diames = diames;
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
	public String getTipoProducto() {
		return tipoProducto;
	}
	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}
	public String getCantProductoFacturadoActual() {
		return cantProductoFacturadoActual;
	}
	public void setCantProductoFacturadoActual(String cantProductoFacturadoActual) {
		this.cantProductoFacturadoActual = cantProductoFacturadoActual;
	}
	public String getCantProductoFacturadoAnterior() {
		return cantProductoFacturadoAnterior;
	}
	public void setCantProductoFacturadoAnterior(String cantProductoFacturadoAnterior) {
		this.cantProductoFacturadoAnterior = cantProductoFacturadoAnterior;
	}
	public String getMontoTotalFacturadoActual() {
		return montoTotalFacturadoActual;
	}
	public void setMontoTotalFacturadoActual(String montoTotalFacturadoActual) {
		this.montoTotalFacturadoActual = montoTotalFacturadoActual;
	}
	public String getMontoTotalFacturadoAnterior() {
		return montoTotalFacturadoAnterior;
	}
	public void setMontoTotalFacturadoAnterior(String montoTotalFacturadoAnterior) {
		this.montoTotalFacturadoAnterior = montoTotalFacturadoAnterior;
	}
	public String getDiferencia() {
		return diferencia;
	}
	public void setDiferencia(String diferencia) {
		this.diferencia = diferencia;
	}
	public String getVariacion() {
		return variacion;
	}
	public void setVariacion(String variacion) {
		this.variacion = variacion;
	}
}
