package com.consystec.sc.ca.ws.output.pais;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputConsultaMunicipios")
public class InputConsultaMunicipios {
    @XmlElement
    private String nombreMunicipio;
    @XmlElement
    private List<InputConsultaAldeas> aldeas;
    
    public String getNombreMunicipio() {
        return nombreMunicipio;
    }
    public void setNombreMunicipio(String nombre) {
        this.nombreMunicipio = nombre;
    }
    public List<InputConsultaAldeas> getAldeas() {
        return aldeas;
    }
    public void setAldeas(List<InputConsultaAldeas> aldeas) {
        this.aldeas = aldeas;
    }
}
