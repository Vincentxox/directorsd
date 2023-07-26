package com.consystec.sc.ca.ws.output.reporte;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.reporte.InputReporteCantInvJornada;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputReporteCantInvJornada")
public class OutputReporteCantInvJornada {
    @XmlElement
    private String token;
    @XmlElement
    private String codArea;
    @XmlElement
	private String usuario;
    @XmlElement
    private Respuesta respuesta;
	@XmlElement
    private InputReporteCantInvJornada datos;
	
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public InputReporteCantInvJornada getDatos() {
        return datos;
    }
    public void setDatos(InputReporteCantInvJornada datos) {
        this.datos = datos;
    }	
}