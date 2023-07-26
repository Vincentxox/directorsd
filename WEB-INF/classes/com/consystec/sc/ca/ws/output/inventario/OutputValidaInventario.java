package com.consystec.sc.ca.ws.output.inventario;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.inventario.Series;
import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputValidaInventario")
public class OutputValidaInventario {
	@XmlElement
	private String token;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<Series> articulos;

	public Respuesta getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}

	public List<Series> getArticulos() {
		return articulos;
	}

	public void setArticulos(List<Series> articulos) {
		this.articulos = articulos;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	

}
