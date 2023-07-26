package com.ericsson.sc.ca.ws.output.actualizacion.movil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="OutputDispositivoActualizado")

public class OutputDispositivoActualizado {
	@XmlElement
	private String nombre;
	 
	@XmlElement
	 private Respuesta respuesta;
	
    public Respuesta getRespuesta ()
    {
        return respuesta;
    }

    public void setRespuesta (Respuesta respuesta)
    {
        this.respuesta = respuesta;
    }
    
    public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
