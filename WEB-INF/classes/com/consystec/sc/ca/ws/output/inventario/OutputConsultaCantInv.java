package com.consystec.sc.ca.ws.output.inventario;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.inventario.InputConsultaCantInv;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputConsultaCantInv")
public class OutputConsultaCantInv {
    @XmlElement
    private String token;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputConsultaCantInv> datos;
    
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
    public List<InputConsultaCantInv> getDatos() {
        return datos;
    }
    public void setDatos(List<InputConsultaCantInv> datos) {
        this.datos = datos;
    }
}