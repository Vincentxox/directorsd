package com.consystec.sc.ca.ws.output.reporte;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ArticuloVendido {

	@XmlElement
    private String tipoProducto;
	@XmlElement
    private String cantidadSugerida;
	@XmlElement
    private String montoSugerido;
	@XmlElement
    private String cantidadFacturada;
	@XmlElement
    private String montoFacturado;
	
	public String getTipoProducto() {
		return tipoProducto;
	}
	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}
	public String getCantidadSugerida() {
		return cantidadSugerida;
	}
	public void setCantidadSugerida(String cantidadSugerida) {
		this.cantidadSugerida = cantidadSugerida;
	}
	public String getMontoSugerido() {
		return montoSugerido;
	}
	public void setMontoSugerido(String montoSugerido) {
		this.montoSugerido = montoSugerido;
	}
	public String getCantidadFacturada() {
		return cantidadFacturada;
	}
	public void setCantidadFacturada(String cantidadFacturada) {
		this.cantidadFacturada = cantidadFacturada;
	}
	public String getMontoFacturado() {
		return montoFacturado;
	}
	public void setMontoFacturado(String montoFacturado) {
		this.montoFacturado = montoFacturado;
	}
}
