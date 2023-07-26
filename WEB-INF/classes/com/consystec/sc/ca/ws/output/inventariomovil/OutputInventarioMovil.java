package com.consystec.sc.ca.ws.output.inventariomovil;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.inventariomovil.InputInventarioMovil;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputInventarioMovil")
public class OutputInventarioMovil {
    @XmlElement
    private String token;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputInventarioMovil> inventario;
    
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
    public List<InputInventarioMovil> getInventario() {
        return inventario;
    }
    public void setInventario(List<InputInventarioMovil> inventario) {
        this.inventario = inventario;
    }
}