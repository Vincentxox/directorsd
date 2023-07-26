package com.consystec.sc.ca.ws.output.bogegas;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.bodegadts.InputConfiguracionFolio;
import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputBodegaDTS")
public class OutputConfiguracionFolio {
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputConfiguracionFolio> configuracionFolio;
    
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputConfiguracionFolio> getConfiguracionFolio() {
        return configuracionFolio;
    }
    public void setConfiguracionFolio(List<InputConfiguracionFolio> configuracionFolio) {
        this.configuracionFolio = configuracionFolio;
    }
}