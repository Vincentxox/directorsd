package com.consystec.sc.ca.ws.output.bogegas;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="OutputBodegasSCL")
public class OutputBodegasSCL {

	
	 @XmlElement
	 private Respuesta respuesta;
	 @XmlElement
	private List<BodegaSCL> bodegas;


    public List<BodegaSCL> getBodegas ()
    {
        return bodegas;
    }

    public void setBodegas (List<BodegaSCL> bodegas)
    {
        this.bodegas = bodegas;
    }

    public Respuesta getRespuesta ()
    {
        return respuesta;
    }

    public void setRespuesta (Respuesta respuesta)
    {
        this.respuesta = respuesta;
    }
}
