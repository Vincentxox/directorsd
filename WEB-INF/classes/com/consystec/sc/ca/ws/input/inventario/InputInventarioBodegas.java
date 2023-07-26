package com.consystec.sc.ca.ws.input.inventario;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputInventarioBodegas")
public class InputInventarioBodegas {
    @XmlElement
    private String codArea;
    @XmlElement
    private String idBodega;
    @XmlElement
    private String nombreBodega;
    @XmlElement
    private String nivel;
    @XmlElement
    private String grupo;
    @XmlElement
    private List<InputInventarioGrupos> grupos;
    
    public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getIdBodega() {
        return idBodega;
    }
    public void setIdBodega(String idBodega) {
        this.idBodega = idBodega;
    }
    public String getNombreBodega() {
        return nombreBodega;
    }
    public void setNombreBodega(String nombreBodega) {
        this.nombreBodega = nombreBodega;
    }
    public String getNivel() {
        return nivel;
    }
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
    public String getGrupo() {
        return grupo;
    }
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
    public List<InputInventarioGrupos> getGrupos() {
        return grupos;
    }
    public void setGrupos(List<InputInventarioGrupos> grupos) {
        this.grupos = grupos;
    }
}