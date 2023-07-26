package com.consystec.sc.ca.ws.input.inventariomovil;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputInventarioMovil")
public class InputInventarioMovil {
    @XmlElement
    private String codArea;
    @XmlElement
    private List<InputInventarioBodegasMovil> bodegas;

    public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public List<InputInventarioBodegasMovil> getBodegas() {
        return bodegas;
    }
    public void setBodegas(List<InputInventarioBodegasMovil> bodegas) {
        this.bodegas = bodegas;
    }
}