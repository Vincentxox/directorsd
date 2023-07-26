package com.consystec.sc.ca.ws.output.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputReporteXZ")
public class OutputReporteXZ {
	@XmlElement
	private String token;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<Reporte_XZ> datos;

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
    public List<Reporte_XZ> getDatos() {
        return datos;
    }
    public void setDatos(List<Reporte_XZ> datos) {
        this.datos = datos;
    }
}