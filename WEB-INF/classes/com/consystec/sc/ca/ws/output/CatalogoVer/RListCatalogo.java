package com.consystec.sc.ca.ws.output.CatalogoVer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RListCatalogo")
public class RListCatalogo {
	
	@XmlElement
	private String nombre;
	@XmlElement
	private String url;
	@XmlElement
	private String codPais;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	public String getCodPais() {
		return codPais;
	}
	public void setCodPais(String codPais) {
		this.codPais = codPais;
	}
	public RListCatalogo() {
		super();
	}
	public RListCatalogo(String nombre, String url, String codPais) {
		super();
		this.nombre = nombre;
		this.url = url;
		this.codPais = codPais;
	}
	
	

}
