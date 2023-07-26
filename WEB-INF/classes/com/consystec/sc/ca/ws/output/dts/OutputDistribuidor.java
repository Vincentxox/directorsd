package com.consystec.sc.ca.ws.output.dts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.dts.InputDistribuidor;
import com.consystec.sc.ca.ws.orm.Respuesta;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputBodegaDTS")
public class OutputDistribuidor {
	@XmlElement
	private String idDTS;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputDistribuidor> distribuidor;
    
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputDistribuidor> getDistribuidor() {
        return distribuidor;
    }
    public void setDistribuidor(List<InputDistribuidor> distribuidor) {
        this.distribuidor = distribuidor;
    }
	public String getIdDTS() {
		return idDTS;
	}
	public void setIdDTS(String idDTS) {
		this.idDTS = idDTS;
	}
    
    
}