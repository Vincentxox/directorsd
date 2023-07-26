package com.consystec.sc.ca.ws.input.buzon;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputBuzonesUsuarios")
public class InputUsuarioBuzon {
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String idUsuarioBuzon;
    @XmlElement
    private String idBuzon;
    @XmlElement
    private String nombreBuzon;
    @XmlElement
    private String nivelBuzon;
    @XmlElement
    private String idVendedor;
    @XmlElement
    private String nombreVendedor;
    @XmlElement
    private String usuarioVendedor;
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
    private List<InputUsuarioBuzon> buzones;
    
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getIdUsuarioBuzon() {
        return idUsuarioBuzon;
    }
    public void setIdUsuarioBuzon(String idUsuarioBuzon) {
        this.idUsuarioBuzon = idUsuarioBuzon;
    }
    public String getIdBuzon() {
        return idBuzon;
    }
    public void setIdBuzon(String idBuzon) {
        this.idBuzon = idBuzon;
    }
    public String getNombreBuzon() {
        return nombreBuzon;
    }
    public void setNombreBuzon(String nombreBuzon) {
        this.nombreBuzon = nombreBuzon;
    }
    public String getIdVendedor() {
        return idVendedor;
    }
    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }
    public String getNombreVendedor() {
        return nombreVendedor;
    }
    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }
    public String getUsuarioVendedor() {
        return usuarioVendedor;
    }
    public void setUsuarioVendedor(String usuarioVendedor) {
        this.usuarioVendedor = usuarioVendedor;
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
    public List<InputUsuarioBuzon> getBuzones() {
        return buzones;
    }
    public void setBuzones(List<InputUsuarioBuzon> buzones) {
        this.buzones = buzones;
    }
    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getNivelBuzon() {
        return nivelBuzon;
    }
    public void setNivelBuzon(String nivelBuzon) {
        this.nivelBuzon = nivelBuzon;
    }
}