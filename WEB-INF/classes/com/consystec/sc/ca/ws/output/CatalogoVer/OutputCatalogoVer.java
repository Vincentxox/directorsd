package com.consystec.sc.ca.ws.output.CatalogoVer;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputCatalogoVer")
public class OutputCatalogoVer {

	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private String fecha;
	@XmlElement
	private List<RListCatalogo> catalogos;
	
	public Respuesta getMensaje() {
		return respuesta;
	}
	public void setMensaje(Respuesta mensaje) {
		this.respuesta = mensaje;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public List<RListCatalogo> getCatalogos() {
		return catalogos;
	}
	public void setCatalogos(List<RListCatalogo> catalogos) {
		this.catalogos = catalogos;
	}
	public OutputCatalogoVer(Respuesta respuesta, String fecha,
			List<RListCatalogo> catalogos) {
		super();
		this.respuesta = respuesta;
		this.fecha = fecha;
		this.catalogos = catalogos;
	}
	public OutputCatalogoVer() {
		super();
	}
	
	
}
