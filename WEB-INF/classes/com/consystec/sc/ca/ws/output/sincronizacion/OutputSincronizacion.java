package com.consystec.sc.ca.ws.output.sincronizacion;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.jornada.InputJornada;
import com.consystec.sc.ca.ws.input.sincronizacion.InputSincronizacion;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputSincronizacion")
public class OutputSincronizacion {
    @XmlElement
    private String token;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputSincronizacion> datos;
    @XmlElement
    private List<InputJornada> vendedores;
    
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
    public List<InputSincronizacion> getDatos() {
        return datos;
    }
    public void setDatos(List<InputSincronizacion> datos) {
        this.datos = datos;
    }
    public List<InputJornada> getVendedores() {
        return vendedores;
    }
    public void setVendedores(List<InputJornada> vendedores) {
        this.vendedores = vendedores;
    }
}