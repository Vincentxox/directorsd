package com.consystec.sc.ca.ws.output.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.reporte.InputReportePDV;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputReportePDV")
public class OutputReportePDV {
    @XmlElement
    private String codArea;
    @XmlElement
	private String usuario;
    @XmlElement
    private Respuesta respuesta;
	@XmlElement
    private List<InputReportePDV> datos;
	
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
    public List<InputReportePDV> getDatos() {
        return datos;
    }
    public void setDatos(List<InputReportePDV> datos) {
        this.datos = datos;
    }	
}