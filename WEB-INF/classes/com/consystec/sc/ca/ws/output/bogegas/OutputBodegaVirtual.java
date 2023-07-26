package com.consystec.sc.ca.ws.output.bogegas;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.bodegadts.InputBodegaVirtual;
import com.consystec.sc.ca.ws.orm.Respuesta;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputBodegaVirtual")
public class OutputBodegaVirtual {
	@XmlElement
	private String idBodegaVirtual;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputBodegaVirtual> bodegaVirtual;

    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputBodegaVirtual> getBodegaVirtual() {
        return bodegaVirtual;
    }
    public void setBodegaVirtual(List<InputBodegaVirtual> bodegaVirtual) {
        this.bodegaVirtual = bodegaVirtual;
    }
	public String getIdBodegaVirtual() {
		return idBodegaVirtual;
	}
	public void setIdBodegaVirtual(String idBodegaVirtual) {
		this.idBodegaVirtual = idBodegaVirtual;
	}
    
    
}