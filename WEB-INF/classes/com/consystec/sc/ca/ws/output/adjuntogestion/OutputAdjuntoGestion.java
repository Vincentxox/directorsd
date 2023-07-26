package com.consystec.sc.ca.ws.output.adjuntogestion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.adjuntogestion.InputAdjuntoGestion;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputImagen")
public class OutputAdjuntoGestion {
    @XmlElement
    private String token;
    @XmlElement
    private String idAdjunto;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private InputAdjuntoGestion adjunto;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getIdAdjunto() {
        return idAdjunto;
    }
    public void setIdAdjunto(String idAdjunto) {
        this.idAdjunto = idAdjunto;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public InputAdjuntoGestion getAdjunto() {
        return adjunto;
    }
    public void setAdjunto(InputAdjuntoGestion adjunto) {
        this.adjunto = adjunto;
    }
}