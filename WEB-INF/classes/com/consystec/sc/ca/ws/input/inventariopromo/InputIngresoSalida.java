package com.consystec.sc.ca.ws.input.inventariopromo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputIngresoSalida")
public class InputIngresoSalida {
    @XmlElement
    private String codArea;
	 @XmlElement
	private String usuario;
	 @XmlElement
	private List<ArticulosIngresoSalida> articulos;

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

	public List<ArticulosIngresoSalida> getArticulos() {
		return articulos;
	}

	public void setArticulos(List<ArticulosIngresoSalida> articulos) {
		this.articulos = articulos;
	}
	
	
}
