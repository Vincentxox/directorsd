 package com.consystec.sc.ca.ws.input.general;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputFolioDTS")
public class InputFolioDTS {
    @XmlElement
    private String codArea;
    @XmlElement
    private String idBodega;
    @XmlElement
    private String tipoDocumento;
    @XmlElement
    private String serie;
    @XmlElement
    private String noInicialFolio;
    @XmlElement
    private String noFinalFolio;
    @XmlElement
    private String estado;
    @XmlElement
    private String usuario;
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
    public String getIdBodega() {
        return idBodega;
    }
    public void setIdBodega(String idBodega) {
        this.idBodega = idBodega;
    }
    public String getTipoDocumento() {
        return tipoDocumento;
    }
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    public String getSerie() {
        return serie;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }
    public String getNoInicialFolio() {
        return noInicialFolio;
    }
    public void setNoInicialFolio(String noInicialFolio) {
        this.noInicialFolio = noInicialFolio;
    }
    public String getNoFinalFolio() {
        return noFinalFolio;
    }
    public void setNoFinalFolio(String noFinalFolio) {
        this.noFinalFolio = noFinalFolio;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
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
