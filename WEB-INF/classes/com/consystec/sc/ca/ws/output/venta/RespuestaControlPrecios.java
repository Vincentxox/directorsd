package com.consystec.sc.ca.ws.output.venta;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RepuestaControlPrecios")
public class RespuestaControlPrecios {
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<OutputControlPrecios> articulos ;
	@XmlElement
	private String cantRegistros;

	public Respuesta getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}



	public List<OutputControlPrecios> getArticulos() {
		return articulos;
	}

	public void setArticulos(List<OutputControlPrecios> articulos) {
		this.articulos = articulos;
	}

	public String getCantRegistros() {
		return cantRegistros;
	}

	public void setCantRegistros(String cantRegistros) {
		this.cantRegistros = cantRegistros;
	}
	
	

}
