package com.consystec.sc.ca.ws.input.descuentoFS;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputDescuentoFS")
public class InputDescuentoFS {
	@XmlElement
	private String codArea;
	@XmlElement
	private String usuario;
	@XmlElement
	private String idProductOffering;
	@XmlElement
	private String idDescuento;
	@XmlElement
	private String nombreDescuento;
	@XmlElement
	private String montoDescuento;
	@XmlElement
	private String creadoEl;
	@XmlElement
	private String creadoPor;
	@XmlElement
	private String modificadoEl;
	@XmlElement
	private String modificadoPor;
	@XmlElement
	private String nombreOferta;
	@XmlElement
	private String tipoDescuento;
	
	
	public String getTipoDescuento() {
		return tipoDescuento;
	}
	public void setTipoDescuento(String tipoDescuento) {
		this.tipoDescuento = tipoDescuento;
	}
	public String getNombreOferta() {
		return nombreOferta;
	}
	public void setNombreOferta(String nombreOferta) {
		this.nombreOferta = nombreOferta;
	}
	public String getCreadoEl() {
		return creadoEl;
	}
	public void setCreadoEl(String creadoEl) {
		this.creadoEl = creadoEl;
	}
	public String getCreadoPor() {
		return creadoPor;
	}
	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}
	public String getModificadoEl() {
		return modificadoEl;
	}
	public void setModificadoEl(String modificadoEl) {
		this.modificadoEl = modificadoEl;
	}
	public String getModificadoPor() {
		return modificadoPor;
	}
	public void setModificadoPor(String modificadoPor) {
		this.modificadoPor = modificadoPor;
	}
	public String getMontoDescuento() {
		return montoDescuento;
	}
	public void setMontoDescuento(String montoDescuento) {
		this.montoDescuento = montoDescuento;
	}
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getIdProductOffering() {
		return idProductOffering;
	}
	public void setIdProductOffering(String idProductOffering) {
		this.idProductOffering = idProductOffering;
	}
	public String getIdDescuento() {
		return idDescuento;
	}
	public void setIdDescuento(String idDescuento) {
		this.idDescuento = idDescuento;
	}
	public String getNombreDescuento() {
		return nombreDescuento;
	}
	public void setNombreDescuento(String nombreDescuento) {
		this.nombreDescuento = nombreDescuento;
	}
}
