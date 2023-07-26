package com.consystec.sc.ca.ws.output.anulacion;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.anulacion.InputAnulacion;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputAsignacion")
public class OutputAnulacion {
    @XmlElement
    private String token;
	@XmlElement
	private String idAnulacion;
	@XmlElement
	private Respuesta respuesta;
    @XmlElement
    private List<InputAnulacion> anulaciones;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getIdAnulacion() {
        return idAnulacion;
    }
    public void setIdAnulacion(String idAnulacion) {
        this.idAnulacion = idAnulacion;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputAnulacion> getAnulaciones() {
        return anulaciones;
    }
    public void setAnulaciones(List<InputAnulacion> anulaciones) {
        this.anulaciones = anulaciones;
    }
}