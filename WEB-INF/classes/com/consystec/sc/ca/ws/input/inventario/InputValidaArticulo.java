package com.consystec.sc.ca.ws.input.inventario;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputValidaArticulo")
public class InputValidaArticulo {
	@XmlElement
	private String token;
	@XmlElement
	private String codArea;
	@XmlElement
	private String usuario;
	@XmlElement
	private String codDispositivo;
	@XmlElement
	private String idBodegaVendedor;
	@XmlElement
	private List<Series> listadoSeries;
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getCodDispositivo() {
		return codDispositivo;
	}
	public void setCodDispositivo(String codDispositivo) {
		this.codDispositivo = codDispositivo;
	}
	public String getIdBodegaVendedor() {
		return idBodegaVendedor;
	}
	public void setIdBodegaVendedor(String idBodegaVendedor) {
		this.idBodegaVendedor = idBodegaVendedor;
	}
	public List<Series> getListadoSeries() {
		return listadoSeries;
	}
	public void setListadoSeries(List<Series> listadoSeries) {
		this.listadoSeries = listadoSeries;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	
	
}
