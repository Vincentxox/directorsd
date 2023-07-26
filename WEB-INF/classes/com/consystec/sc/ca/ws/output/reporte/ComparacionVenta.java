package com.consystec.sc.ca.ws.output.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ComparacionVenta {

	@XmlElement
	private String diaSemana;
	@XmlElement
	private String diaMes;
	@XmlElement
	private String mes;
	@XmlElement
	private String anio;
	@XmlElement
	private String venta;
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
	@XmlElement
	private List<ComparacionVentaArticulo> comparacionArticulo;
	
	public List<ComparacionVentaArticulo> getComparacionArticulo() {
		return comparacionArticulo;
	}
	public void setComparacionArticulo(List<ComparacionVentaArticulo> comparacionArticulo) {
		this.comparacionArticulo = comparacionArticulo;
	}
	public String getDiaSemana() {
		return diaSemana;
	}
	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}
	public String getDiaMes() {
		return diaMes;
	}
	public void setDiaMes(String diaMes) {
		this.diaMes = diaMes;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getVenta() {
		return venta;
	}
	public void setVenta(String venta) {
		this.venta = venta;
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
