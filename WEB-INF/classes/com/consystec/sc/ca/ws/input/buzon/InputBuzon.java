package com.consystec.sc.ca.ws.input.buzon;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputBuzon")
public class InputBuzon {

	@XmlElement
	private String codArea;
	@XmlElement
	private String idBuzon;
	@XmlElement
	private String usuario;
	@XmlElement
	private String nombre;
	@XmlElement
	private String idDistribuidor;
	@XmlElement
	private String nombreDistribuidor;
	@XmlElement
	private String idBodegaVirtual;
	@XmlElement
	private String nombreBodega;
	@XmlElement
	private String nivel;
	@XmlElement
	private String tipoWF;
	@XmlElement
	private String estado;
	@XmlElement
	private String creado_por;
	@XmlElement
	private String creado_el;
	@XmlElement
	private String modificado_por;
	@XmlElement
	private String modificado_el;
	public String getIdBuzon() {
		return idBuzon;
	}
	public void setIdBuzon(String idBuzon) {
		this.idBuzon = idBuzon;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCreado_por() {
		return creado_por;
	}
	public void setCreado_por(String creado_por) {
		this.creado_por = creado_por;
	}
	public String getCreado_el() {
		return creado_el;
	}
	public void setCreado_el(String creado_el) {
		this.creado_el = creado_el;
	}
	public String getModificado_por() {
		return modificado_por;
	}
	public void setModificado_por(String modificado_por) {
		this.modificado_por = modificado_por;
	}
	public String getModificado_el() {
		return modificado_el;
	}
	public void setModificado_el(String modificado_el) {
		this.modificado_el = modificado_el;
	}
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getIdDistribuidor() {
		return idDistribuidor;
	}
	public void setIdDistribuidor(String idDistribuidor) {
		this.idDistribuidor = idDistribuidor;
	}
	public String getNombreDistribuidor() {
		return nombreDistribuidor;
	}
	public void setNombreDistribuidor(String nombreDistribuidor) {
		this.nombreDistribuidor = nombreDistribuidor;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public String getTipoWF() {
		return tipoWF;
	}
	public void setTipoWF(String tipoWF) {
		this.tipoWF = tipoWF;
	}
	public String getIdBodegaVirtual() {
		return idBodegaVirtual;
	}
	public void setIdBodegaVirtual(String idBodegaVirtual) {
		this.idBodegaVirtual = idBodegaVirtual;
	}
	public String getNombreBodega() {
		return nombreBodega;
	}
	public void setNombreBodega(String nombreBodega) {
		this.nombreBodega = nombreBodega;
	}
	
	
}
