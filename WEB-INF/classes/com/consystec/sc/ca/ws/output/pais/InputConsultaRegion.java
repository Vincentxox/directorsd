package com.consystec.sc.ca.ws.output.pais;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputConsultaDepartamentos")
public class InputConsultaRegion {
	@XmlElement
	private String codArea;
	@XmlElement
	private String nombreRegion;
	@XmlElement
	private List<InputConsultaDepartamentos> departamentos;
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getNombreRegion() {
		return nombreRegion;
	}
	public void setNombreRegion(String nombreRegion) {
		this.nombreRegion = nombreRegion;
	}
	public List<InputConsultaDepartamentos> getDepartamentos() {
		return departamentos;
	}
	public void setDepartamentos(List<InputConsultaDepartamentos> departamentos) {
		this.departamentos = departamentos;
	}
}
