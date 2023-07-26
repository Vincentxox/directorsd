package com.consystec.sc.ca.ws.output.CatalogoVer;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputVersionCatalogo")
public class OutputVersionCatalogo {
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<VersionCatalogo> catalogos;
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	public List<VersionCatalogo> getCatalogos() {
		return catalogos;
	}
	public void setCatalogos(List<VersionCatalogo> catalogos) {
		this.catalogos = catalogos;
	}
	
	
}
