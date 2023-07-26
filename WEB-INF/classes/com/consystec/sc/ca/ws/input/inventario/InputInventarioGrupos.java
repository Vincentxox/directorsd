package com.consystec.sc.ca.ws.input.inventario;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputInventarioGrupos")
public class InputInventarioGrupos {
    @XmlElement
    private String codArea;
    @XmlElement
    private String idBodega;
    @XmlElement
    private String grupo;
    @XmlElement
    private List<InputInventarioArticulos> articulos;
    
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
    public List<InputInventarioArticulos> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<InputInventarioArticulos> articulos) {
        this.articulos = articulos;
    }
}