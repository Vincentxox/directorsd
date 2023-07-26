package com.consystec.sc.ca.ws.output.venta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputControlPrecios")
public class OutputControlPrecios {

	@XmlElement
	private String idVenta;
	@XmlElement
	private String numero;
	@XmlElement
	private String serie;
	@XmlElement
	private String folioSidra;
	@XmlElement
	private String serieSidra;
	@XmlElement
	private String tipoDocumento;
	@XmlElement
	private String fecha;
	@XmlElement
	private String articulo;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String cantidad;
	@XmlElement
	private String serieArticulo;
	@XmlElement
	private String precioInicial;
	@XmlElement
	private String descuentoSCL;
	@XmlElement
	private String descuentoSIDRA;
	@XmlElement
	private String precioVenta;
	public String getIdVenta() {
		return idVenta;
	}
	public void setIdVenta(String idVenta) {
		this.idVenta = idVenta;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getArticulo() {
		return articulo;
	}
	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getCantidad() {
		return cantidad;
	}
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	public String getSerieArticulo() {
		return serieArticulo;
	}
	public void setSerieArticulo(String serieArticulo) {
		this.serieArticulo = serieArticulo;
	}
	public String getPrecioInicial() {
		return precioInicial;
	}
	public void setPrecioInicial(String precioInicial) {
		this.precioInicial = precioInicial;
	}
	public String getDescuentoSCL() {
		return descuentoSCL;
	}
	public void setDescuentoSCL(String descuentoSCL) {
		this.descuentoSCL = descuentoSCL;
	}
	public String getDescuentoSIDRA() {
		return descuentoSIDRA;
	}
	public void setDescuentoSIDRA(String descuentoSIDRA) {
		this.descuentoSIDRA = descuentoSIDRA;
	}
	public String getPrecioVenta() {
		return precioVenta;
	}
	public void setPrecioVenta(String precioVenta) {
		this.precioVenta = precioVenta;
	}
	public String getFolioSidra() {
		return folioSidra;
	}
	public void setFolioSidra(String folioSidra) {
		this.folioSidra = folioSidra;
	}
	public String getSerieSidra() {
		return serieSidra;
	}
	public void setSerieSidra(String serieSidra) {
		this.serieSidra = serieSidra;
	}
	
	
	
}
