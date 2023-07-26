package com.consystec.sc.ca.ws.output.ofertacampania;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.ofertacampania.InputOfertaCampania;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputOfertaCampania")
public class OutputOfertaCampania {
    @XmlElement
    private String token;
    @XmlElement
    private String idOfertaCampania;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputOfertaCampania> ofertaCampania;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getIdOfertaCampania() {
        return idOfertaCampania;
    }
    public void setIdOfertaCampania(String idOfertaCampania) {
        this.idOfertaCampania = idOfertaCampania;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputOfertaCampania> getOfertaCampania() {
        return ofertaCampania;
    }
    public void setOfertaCampania(List<InputOfertaCampania> ofertaCampania) {
        this.ofertaCampania = ofertaCampania;
    }
}