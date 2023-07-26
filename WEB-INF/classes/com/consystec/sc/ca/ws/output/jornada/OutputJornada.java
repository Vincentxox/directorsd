package com.consystec.sc.ca.ws.output.jornada;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.dispositivo.InputDispositivo;
import com.consystec.sc.ca.ws.input.jornada.InputJornada;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputJornada")
public class OutputJornada {
    @XmlElement
    private String token;
    @XmlElement
    private String idJornada;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private InputJornada jornada;
    @XmlElement
    private String fechaCierre;
    @XmlElement
    private List<InputJornada> jornadas;
    @XmlElement
    private List<InputDispositivo> dispositivos;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getIdJornada() {
        return idJornada;
    }
    public void setIdJornada(String idJornada) {
        this.idJornada = idJornada;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public InputJornada getJornada() {
        return jornada;
    }
    public void setJornada(InputJornada jornada) {
        this.jornada = jornada;
    }
    public List<InputJornada> getJornadas() {
        return jornadas;
    }
    public void setJornadas(List<InputJornada> jornadas) {
        this.jornadas = jornadas;
    }
    public List<InputDispositivo> getDispositivos() {
        return dispositivos;
    }
    public void setDispositivos(List<InputDispositivo> dispositivos) {
        this.dispositivos = dispositivos;
    }
	public String getFechaCierre() {
		return fechaCierre;
	}
	public void setFechaCierre(String fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
    
}