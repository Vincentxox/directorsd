package com.consystec.sc.ca.ws.output.promocionales;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.promocionales.InputGrupoPromocionales;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputPromocionales")
public class OutputPromocionales {
    @XmlElement
    private String token;
    @XmlElement
    private String idArtPromocional;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputGrupoPromocionales> grupos;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getIdArtPromocional() {
        return idArtPromocional;
    }
    public void setIdArtPromocional(String idArtPromocional) {
        this.idArtPromocional = idArtPromocional;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputGrupoPromocionales> getGrupos() {
        return grupos;
    }
    public void setGrupos(List<InputGrupoPromocionales> grupos) {
        this.grupos = grupos;
    }
}