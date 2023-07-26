package com.consystec.sc.ca.ws.output.inventariopromo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.ingresosalida.ArticulosIngresoSalida;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ArticulosIncorrectos")
public class ArticulosIncorrectos {
	@XmlElement
	private String razon;
	@XmlElement
	private List<ArticulosIngresoSalida> articulos;

	public String getRazon() {
		return razon;
	}

	public void setRazon(String razon) {
		this.razon = razon;
	}

	public List<ArticulosIngresoSalida> getArticulos() {
		return articulos;
	}

	public void setArticulos(List<ArticulosIngresoSalida> articulos) {
		this.articulos = articulos;
	}
	
	
}
