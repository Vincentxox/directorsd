package com.consystec.sc.ca.ws.input.inventariomovil;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputInventarioBodegasMovil")
public class InputInventarioBodegasMovil {
    @XmlElement
    private String codArea;
    @XmlElement
    private String idBodega;
    @XmlElement
    private String nombreBodega;
    @XmlElement
    private String grupo;
    @XmlElement
    private List<InputInventarioGruposMovil> grupos;
    
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
    public String getGrupo() {
        return grupo;
    }
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
    public List<InputInventarioGruposMovil> getGrupos() {
        return grupos;
    }
    public void setGrupos(List<InputInventarioGruposMovil> grupos) {
        this.grupos = grupos;
    }
}