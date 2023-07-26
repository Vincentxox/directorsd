package com.consystec.sc.ca.ws.output.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class DatosEfectividad {

	@XmlElement
	private String idDistribuidor;
	@XmlElement
	private String nombreDistribuidor;
	@XmlElement
	private String idBodegaVirtual;
	@XmlElement
	private String nombreBodega;
	@XmlElement
	private String idRuta;
	@XmlElement
	private String nombreRuta;
	@XmlElement
	private List<InfoPDV> dpv;
	
	public List<InfoPDV> getDpv() {
		return dpv;
	}
	public void setDpv(List<InfoPDV> dpv) {
		this.dpv = dpv;
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
	public String getIdRuta() {
		return idRuta;
	}
	public void setIdRuta(String idRuta) {
		this.idRuta = idRuta;
	}
	public String getNombreRuta() {
		return nombreRuta;
	}
	public void setNombreRuta(String nombreRuta) {
		this.nombreRuta = nombreRuta;
	}
}
