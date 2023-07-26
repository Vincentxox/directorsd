package com.consystec.sc.ca.ws.output.ofertacampania;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.ofertacampania.InputOfertaCampaniaMovil;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputOfertaCampaniaMovil")
public class OutputOfertaCampaniaMovil {
    @XmlElement
    private String token;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputOfertaCampaniaMovil> ofertaCampania;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputOfertaCampaniaMovil> getOfertaCampania() {
        return ofertaCampania;
    }
    public void setOfertaCampania(List<InputOfertaCampaniaMovil> ofertaCampania) {
        this.ofertaCampania = ofertaCampania;
    }
}