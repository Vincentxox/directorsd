package com.consystec.sc.ca.ws.input.oferCom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputConsultaArticulos")
public class InputConsultaArticulos {
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String idArticulo;
    @XmlElement
    private String codArticulo;
    @XmlElement
    private String descArticulo;
    @XmlElement
    private String desTipoPrecio;
    @XmlElement
    private String precioArticulo;
    @XmlElement
    private String tipoGestion;
    @XmlElement
    private String tipoGrupoSidra;
    
    public String getTipoGrupoSidra() {
		return tipoGrupoSidra;
	}
	public void setTipoGrupoSidra(String tipoGrupoSidra) {
		this.tipoGrupoSidra = tipoGrupoSidra;
	}
    
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
    public String getIdArticulo() {
        return idArticulo;
    }
    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }
    public String getCodArticulo() {
        return codArticulo;
    }
    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
    }
    public String getDescArticulo() {
        return descArticulo;
    }
    public void setDescArticulo(String descArticulo) {
        this.descArticulo = descArticulo;
    }
    public String getDesTipoPrecio() {
        return desTipoPrecio;
    }
    public void setDesTipoPrecio(String desTipoPrecio) {
        this.desTipoPrecio = desTipoPrecio;
    }
    public String getPrecioArticulo() {
        return precioArticulo;
    }
    public void setPrecioArticulo(String precioArticulo) {
        this.precioArticulo = precioArticulo;
    }
    public String getTipoGestion() {
        return tipoGestion;
    }
    public void setTipoGestion(String tipoGestion) {
        this.tipoGestion = tipoGestion;
    }
}
