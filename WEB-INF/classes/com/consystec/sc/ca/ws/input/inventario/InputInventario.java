package com.consystec.sc.ca.ws.input.inventario;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputInventario")
public class InputInventario {
    @XmlElement
    private String codArea;
    @XmlElement
    private List<InputInventarioBodegas> bodegas;

    public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public List<InputInventarioBodegas> getBodegas() {
        return bodegas;
    }
    public void setBodegas(List<InputInventarioBodegas> bodegas) {
        this.bodegas = bodegas;
    }
}