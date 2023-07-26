package com.consystec.sc.ca.ws.output.condicion;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.condicion.InputCondicion;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputCondicion")
public class OutputCondicion {
    @XmlElement
    private String token;
	@XmlElement
	private String idCondicion;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
    private List<InputCondicion> condiciones;
	
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getIdCondicion() {
        return idCondicion;
    }
    public void setIdCondicion(String idCondicion) {
        this.idCondicion = idCondicion;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputCondicion> getCondiciones() {
        return condiciones;
    }
    public void setCondiciones(List<InputCondicion> condiciones) {
        this.condiciones = condiciones;
    }
}