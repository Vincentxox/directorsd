package com.consystec.sc.ca.ws.output.oferCom;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.oferCom.InputConsultaArticulos;
import com.consystec.sc.ca.ws.orm.Respuesta;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputBodegasSCL")
public class OutputConsultaArticulos {
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputConsultaArticulos> articulos;
    
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputConsultaArticulos> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<InputConsultaArticulos> articulos) {
        this.articulos = articulos;
    }
}
