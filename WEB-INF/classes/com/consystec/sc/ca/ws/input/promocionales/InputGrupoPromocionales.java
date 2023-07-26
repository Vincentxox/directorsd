package com.consystec.sc.ca.ws.input.promocionales;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputGrupoPromocionales")
public class InputGrupoPromocionales {
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String grupo;
    @XmlElement
    private List<InputPromocionales> articulos;
    
    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getGrupo() {
        return grupo;
    }
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
    public List<InputPromocionales> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<InputPromocionales> articulos) {
        this.articulos = articulos;
    }
}