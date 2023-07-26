package com.consystec.sc.ca.ws.output.etiquetamov;


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RListPantalla")
public class RListPantalla {
	
	@XmlElement
	private String  paisId;
	@XmlElement
	private String id;
	@XmlElement
	private String nombre;
	@XmlElement
	private List<RListEtiquetas> etiquetas;
	
	
	public String getPaisId() {
		return paisId;
	}
	public void setPaisId(String paisId) {
		this.paisId = paisId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<RListEtiquetas> getEtiquetas() {
		return etiquetas;
	}
	public void setEtiquetas(List<RListEtiquetas> etiquetas) {
		this.etiquetas = etiquetas;
	}
	
	
	public RListPantalla(String paisId, String id, String nombre,
			List<RListEtiquetas> etiquetas) {
		super();
		this.paisId = paisId;
		this.id = id;
		this.nombre = nombre;
		this.etiquetas = etiquetas;
	}
	public RListPantalla() {
		super();
	}
	
	
}
