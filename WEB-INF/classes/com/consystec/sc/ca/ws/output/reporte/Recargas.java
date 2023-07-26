package com.consystec.sc.ca.ws.output.reporte;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Recargas")
public class Recargas {
	@XmlElement
	private String jornada;
	@XmlElement
	private String estadoLiquidacionJornada;
	@XmlElement
	private String vendedor;
	@XmlElement
	private String totalFacturado;
	@XmlElement
	private String fecha;
	@XmlElement
	private String nombreDts;
	@XmlElement
	private String zona;
	@XmlElement
	private String nombrePanelRuta;
	@XmlElement
	private String tipo;
	
	
	public String getNombrePanelRuta() {
		return nombrePanelRuta;
	}
	public void setNombrePanelRuta(String nombrePanelRuta) {
		this.nombrePanelRuta = nombrePanelRuta;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getEstadoLiquidacionJornada() {
		return estadoLiquidacionJornada;
	}
	public void setEstadoLiquidacionJornada(String estadoLiquidacionJornada) {
		this.estadoLiquidacionJornada = estadoLiquidacionJornada;
	}
	public String getJornada() {
		return jornada;
	}
	public void setJornada(String jornada) {
		this.jornada = jornada;
	}
	public String getVendedor() {
		return vendedor;
	}
	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}
	public String getTotalFacturado() {
		return totalFacturado;
	}
	public void setTotalFacturado(String totalFacturado) {
		this.totalFacturado = totalFacturado;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getNombreDts() {
		return nombreDts;
	}
	public void setNombreDts(String nombreDts) {
		this.nombreDts = nombreDts;
	}
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}
	
}
