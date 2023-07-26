package com.consystec.sc.ca.ws.output.buzon;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.buzon.InputUsuarioBuzon;
import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputBuzonesUsuarios")
public class OutputUsuarioBuzon {
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputUsuarioBuzon> buzones;
    
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputUsuarioBuzon> getBuzones() {
        return buzones;
    }
    public void setBuzones(List<InputUsuarioBuzon> buzones) {
        this.buzones = buzones;
    }
}