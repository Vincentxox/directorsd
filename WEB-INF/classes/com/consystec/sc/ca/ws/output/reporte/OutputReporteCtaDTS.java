package com.consystec.sc.ca.ws.output.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.reporte.InputReporteCtaDTS;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputReporteCtaDTS")
public class OutputReporteCtaDTS {
    @XmlElement
    private Respuesta respuesta;
	@XmlElement
    private List<InputReporteCtaDTS> datos;

    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputReporteCtaDTS> getDatos() {
        return datos;
    }
    public void setDatos(List<InputReporteCtaDTS> datos) {
        this.datos = datos;
    }
}