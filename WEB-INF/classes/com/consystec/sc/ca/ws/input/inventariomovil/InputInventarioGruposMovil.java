package com.consystec.sc.ca.ws.input.inventariomovil;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputInventarioGruposMovil")
public class InputInventarioGruposMovil {
    @XmlElement
    private String codArea;
    @XmlElement
    private String idBodega;
    @XmlElement
    private String grupo;
    @XmlElement
    private List<InputInventarioArticulosMovil> articulos;
    
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
    public String getGrupo() {
        return grupo;
    }
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
    public List<InputInventarioArticulosMovil> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<InputInventarioArticulosMovil> articulos) {
        this.articulos = articulos;
    }
}