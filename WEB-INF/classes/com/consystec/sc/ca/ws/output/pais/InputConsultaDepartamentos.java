package com.consystec.sc.ca.ws.output.pais;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputConsultaDepartamentos")
public class InputConsultaDepartamentos {
    @XmlElement
    private String nombreDepartamento;
    @XmlElement
    private List<InputConsultaMunicipios> municipios;

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }
    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }
    public List<InputConsultaMunicipios> getMunicipios() {
        return municipios;
    }
    public void setMunicipios(List<InputConsultaMunicipios> municipios) {
        this.municipios = municipios;
    }
}
