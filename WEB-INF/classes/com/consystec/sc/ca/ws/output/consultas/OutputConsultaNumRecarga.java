package com.consystec.sc.ca.ws.output.consultas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputConsultaNumRecarga")
public class OutputConsultaNumRecarga {
    @XmlElement
    private String codArea;
    @XmlElement
    private String token;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private String estado;
    
    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getCodDispositivo() {
        return codDispositivo;
    }
    public void setCodDispositivo(String codDispositivo) {
        this.codDispositivo = codDispositivo;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}