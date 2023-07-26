package com.consystec.sc.ca.ws.input.inventario;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Series")
public class Series {
	@XmlElement
	private String idArticulo;
	@XmlElement
	private String tipoInventario;
	@XmlElement
	private String descripcionArticulo;
	@XmlElement
	private String serieInicial;
	@XmlElement
	private String serieFinal;
	@XmlElement
	private String grupo;
	@XmlElement
	private String estado;

	
	public String getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(String idArticulo) {
		this.idArticulo = idArticulo;
	}

	public String getSerieInicial() {
		return serieInicial;
	}

	public void setSerieInicial(String serieInicial) {
		this.serieInicial = serieInicial;
	}

	public String getSerieFinal() {
		return serieFinal;
	}

	public void setSerieFinal(String serieFinal) {
		this.serieFinal = serieFinal;
	}

	public String getTipoInventario() {
		return tipoInventario;
	}

	public void setTipoInventario(String tipoInventario) {
		this.tipoInventario = tipoInventario;
	}

	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
}
