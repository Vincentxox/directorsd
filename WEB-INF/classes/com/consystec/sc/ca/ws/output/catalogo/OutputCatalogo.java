package com.consystec.sc.ca.ws.output.catalogo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.catalogo.InputGrupoCatalogo;
import com.consystec.sc.ca.ws.input.catalogo.InputParametro;
import com.consystec.sc.ca.ws.orm.Respuesta;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputCatalogo")
public class OutputCatalogo {
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputGrupoCatalogo> grupo;
    @XmlElement
    private List<InputParametro> parametros;

    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputParametro> getParametros() {
        return parametros;
    }
    public void setParametros(List<InputParametro> parametros) {
        this.parametros = parametros;
    }
    public List<InputGrupoCatalogo> getGrupo() {
        return grupo;
    }
    public void setGrupo(List<InputGrupoCatalogo> grupo) {
        this.grupo = grupo;
    }
}