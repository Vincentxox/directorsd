package com.consystec.sc.ca.ws.input.ofertacampania;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputPromoOfertaCampaniaDet")
public class InputPromoOfertaCampaniaDet {
    @XmlElement
    private String codArea;
    @XmlElement
    private String idPromoOfertaCampania;
    @XmlElement
    private String idOfertaCampania;
    @XmlElement
    private String idArtPromocional;
    @XmlElement
    private String nombreArticulo;
    @XmlElement
    private String tipoInv;
    @XmlElement
    private String cantArticulos;
    @XmlElement
    private String estado;
    @XmlElement
    private String creado_el;
    @XmlElement
    private String creado_por;
    @XmlElement
    private String modificado_el;
    @XmlElement
    private String modificado_por;
    
    public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getIdPromoOfertaCampania() {
        return idPromoOfertaCampania;
    }
    public void setIdPromoOfertaCampania(String idPromoOfertaCampania) {
        this.idPromoOfertaCampania = idPromoOfertaCampania;
    }
    public String getIdOfertaCampania() {
        return idOfertaCampania;
    }
    public void setIdOfertaCampania(String idOfertaCampania) {
        this.idOfertaCampania = idOfertaCampania;
    }
    public String getIdArtPromocional() {
        return idArtPromocional;
    }
    public void setIdArtPromocional(String idArtPromocional) {
        this.idArtPromocional = idArtPromocional;
    }
    public String getNombreArticulo() {
        return nombreArticulo;
    }
    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }
    public String getTipoInv() {
        return tipoInv;
    }
    public void setTipoInv(String tipoInv) {
        this.tipoInv = tipoInv;
    }
    public String getCantArticulos() {
        return cantArticulos;
    }
    public void setCantArticulos(String cantArticulos) {
        this.cantArticulos = cantArticulos;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getCreado_el() {
        return creado_el;
    }
    public void setCreado_el(String creado_el) {
        this.creado_el = creado_el;
    }
    public String getCreado_por() {
        return creado_por;
    }
    public void setCreado_por(String creado_por) {
        this.creado_por = creado_por;
    }
    public String getModificado_el() {
        return modificado_el;
    }
    public void setModificado_el(String modificado_el) {
        this.modificado_el = modificado_el;
    }
    public String getModificado_por() {
        return modificado_por;
    }
    public void setModificado_por(String modificado_por) {
        this.modificado_por = modificado_por;
    }
}