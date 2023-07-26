package com.consystec.sc.ca.ws.output.condicionoferta;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.condicionoferta.InputCondicionOferta;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputCondicionOferta")
public class OutputCondicionOferta {
    @XmlElement
    private String token;
	@XmlElement
	private String idCondicion;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
    private List<InputCondicionOferta> condiciones;
	@XmlElement
    private List<InputCondicionOferta> condicionesPdv;
	@XmlElement
    private List<InputCondicionOferta> condicionesZonaCat;
	
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
    public List<InputCondicionOferta> getCondiciones() {
        return condiciones;
    }
    public void setCondiciones(List<InputCondicionOferta> condiciones) {
        this.condiciones = condiciones;
    }
    public List<InputCondicionOferta> getCondicionesPdv() {
        return condicionesPdv;
    }
    public void setCondicionesPdv(List<InputCondicionOferta> condicionesPdv) {
        this.condicionesPdv = condicionesPdv;
    }
    public List<InputCondicionOferta> getCondicionesZonaCat() {
        return condicionesZonaCat;
    }
    public void setCondicionesZonaCat(List<InputCondicionOferta> condicionesZonaCat) {
        this.condicionesZonaCat = condicionesZonaCat;
    }
}