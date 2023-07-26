package com.consystec.sc.ca.ws.output.bogegas;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.bodegadts.InputBodegaAlmacen;

import com.consystec.sc.ca.ws.orm.Respuesta;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputBodegaDTS")
public class OutputBodegaDTS {
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputBodegaAlmacen> bodegaDTS;

    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
	public List<InputBodegaAlmacen> getBodegaDTS() {
		return bodegaDTS;
	}
	public void setBodegaDTS(List<InputBodegaAlmacen> bodegaDTS) {
		this.bodegaDTS = bodegaDTS;
	}

}