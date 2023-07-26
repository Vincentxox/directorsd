package com.consystec.sc.ca.ws.input.ofertacampania;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputPromoOfertaCampania")
public class InputPromoOfertaCampania {
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String idPromoCampania;
    @XmlElement
    private String idOfertaCampania;
    @XmlElement
    private String nombreCampania;
    @XmlElement
    private String idArtPromocional;
    @XmlElement
    private String nombreArticulo;
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
    @XmlElement
    private List<InputPromoOfertaCampania> articulosPromocionales;
    
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
    public String getIdPromoCampania() {
        return idPromoCampania;
    }
    public void setIdPromoCampania(String idPromoCampania) {
        this.idPromoCampania = idPromoCampania;
    }
    public String getIdOfertaCampania() {
        return idOfertaCampania;
    }
    public void setIdOfertaCampania(String idOfertaCampania) {
        this.idOfertaCampania = idOfertaCampania;
    }
    public String getNombreCampania() {
        return nombreCampania;
    }
    public void setNombreCampania(String nombreCampania) {
        this.nombreCampania = nombreCampania;
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
    public List<InputPromoOfertaCampania> getArticulosPromocionales() {
        return articulosPromocionales;
    }
    public void setArticulosPromocionales(List<InputPromoOfertaCampania> articulosPromocionales) {
        this.articulosPromocionales = articulosPromocionales;
    }
}