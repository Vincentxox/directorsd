package com.consystec.sc.ca.ws.input.articuloprecio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputArticuloPrecio")
public class InputArticuloPrecio {

	@XmlElement
	private String codArea;
	@XmlElement
	private String usuario;
	@XmlElement
	private String idArticulo;
	@XmlElement
	private String tipoGestion;
	@XmlElement
	private String precio;
	@XmlElement
	private String idProductOffering;
	@XmlElement
	private String estado;
	@XmlElement
	private String nombreArticulo;
	@XmlElement
	private String version;
	@XmlElement
	private String creadoEl;
	@XmlElement
	private String creadoPor;
	@XmlElement
	private String modificadoEl;
	@XmlElement
	private String modificadoPor;
	@XmlElement
	private String precioMin;
	@XmlElement
	private String precioMax;
	@XmlElement
	private String nombreOferta;
	
	
	public String getNombreOferta() {
		return nombreOferta;
	}
	public void setNombreOferta(String nombreOferta) {
		this.nombreOferta = nombreOferta;
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
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getNombreArticulo() {
		return nombreArticulo;
	}
	public void setNombreArticulo(String nombreArticulo) {
		this.nombreArticulo = nombreArticulo;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
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
	public String getIdArticulo() {
		return idArticulo;
	}
	public void setIdArticulo(String idArticulo) {
		this.idArticulo = idArticulo;
	}
	public String getTipoGestion() {
		return tipoGestion;
	}
	public void setTipoGestion(String tipoGestion) {
		this.tipoGestion = tipoGestion;
	}
	public String getPrecio() {
		return precio;
	}
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	public String getIdProductOffering() {
		return idProductOffering;
	}
	public void setIdProductOffering(String idProductOffering) {
		this.idProductOffering = idProductOffering;
	}
	

}
