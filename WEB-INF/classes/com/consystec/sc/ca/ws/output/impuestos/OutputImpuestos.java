package com.consystec.sc.ca.ws.output.impuestos;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.impuestos.InputImpuestos;
import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputImpuestos")
public class OutputImpuestos {
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private String codArea;
    @XmlElement
    private List<InputImpuestos> impuestos;
    @XmlElement
    private List<InputImpuestos> descuentos;
    
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public List<InputImpuestos> getImpuestos() {
        return impuestos;
    }
    public void setImpuestos(List<InputImpuestos> impuestos) {
        this.impuestos = impuestos;
    }
    public List<InputImpuestos> getDescuentos() {
        return descuentos;
    }
    public void setDescuentos(List<InputImpuestos> descuentos) {
        this.descuentos = descuentos;
    }
}