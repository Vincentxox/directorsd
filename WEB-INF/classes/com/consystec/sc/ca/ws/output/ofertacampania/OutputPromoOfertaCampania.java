package com.consystec.sc.ca.ws.output.ofertacampania;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.ofertacampania.InputPromoOfertaCampania;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputPromoOfertaCampania")
public class OutputPromoOfertaCampania {
    @XmlElement
    private String token;
    @XmlElement
    private String idOfertaCampania;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputPromoOfertaCampania> campanias;
    
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
    public List<InputPromoOfertaCampania> getCampanias() {
        return campanias;
    }
    public void setCampanias(List<InputPromoOfertaCampania> campanias) {
        this.campanias = campanias;
    }
}