package com.consystec.sc.ca.ws.output.descuento;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.descuento.InputDescuento;
import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputDescuento")
public class OutputDescuento {
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputDescuento> descuento;
    
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputDescuento> getDescuento() {
        return descuento;
    }
    public void setDescuento(List<InputDescuento> descuento) {
        this.descuento = descuento;
    }
}