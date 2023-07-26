package com.consystec.sc.ca.ws.output.remesa;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.remesa.InputRemesa;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputRemesa")
public class OutputRemesa {
    @XmlElement
    private String token;
    @XmlElement
    private String idRemesa;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputRemesa> datos;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getIdRemesa() {
        return idRemesa;
    }
    public void setIdRemesa(String idRemesa) {
        this.idRemesa = idRemesa;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputRemesa> getDatos() {
        return datos;
    }
    public void setDatos(List<InputRemesa> datos) {
        this.datos = datos;
    }
}