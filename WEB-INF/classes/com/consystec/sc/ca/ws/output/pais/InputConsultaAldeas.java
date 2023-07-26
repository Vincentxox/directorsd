package com.consystec.sc.ca.ws.output.pais;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputConsultaAldeas")
public class InputConsultaAldeas {
    @XmlElement
    private String municipio;
    @XmlElement
    private String nombreAldea;
    
    public String getMunicipio() {
        return municipio;
    }
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
    public String getNombreAldea() {
        return nombreAldea;
    }
    public void setNombreAldea(String nombre) {
        this.nombreAldea = nombre;
    }
}
