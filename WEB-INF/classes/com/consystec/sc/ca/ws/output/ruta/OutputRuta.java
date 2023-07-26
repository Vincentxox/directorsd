package com.consystec.sc.ca.ws.output.ruta;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.ruta.InputRuta;
import com.consystec.sc.ca.ws.orm.Respuesta;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputRuta")
public class OutputRuta {
    @XmlElement
    private String idRuta;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputRuta> ruta;
    
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputRuta> getRuta() {
        return ruta;
    }
    public void setRuta(List<InputRuta> ruta) {
        this.ruta = ruta;
    }
	public String getIdRuta() {
		return idRuta;
	}
	public void setIdRuta(String idRuta) {
		this.idRuta = idRuta;
	}

    
    
}