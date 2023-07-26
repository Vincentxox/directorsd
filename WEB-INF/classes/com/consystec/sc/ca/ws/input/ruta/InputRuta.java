package com.consystec.sc.ca.ws.input.ruta;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.pdv.InputPDV;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "inputRuta")
public class InputRuta {
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String idBodegaVirtual;
    @XmlElement
    private String idRuta;
    @XmlElement
    private String nombreRuta;
    @XmlElement
    private String idDTS;
    @XmlElement
    private String nombreDTS;
    @XmlElement
    private String tipoDTS;
    @XmlElement
    private String secUsuarioId;
    @XmlElement
    private String idBodegaVendedor;
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
    private InputVendedor datosVendedor;
    @XmlElement
    private List<InputPDV> puntoVenta;
    
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
    public String getIdBodegaVirtual() {
        return idBodegaVirtual;
    }
    public void setIdBodegaVirtual(String idBodegaVirtual) {
        this.idBodegaVirtual = idBodegaVirtual;
    }
    public String getIdRuta() {
        return idRuta;
    }
    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }
    public String getNombreRuta() {
        return nombreRuta;
    }
    public void setNombreRuta(String nombreRuta) {
        this.nombreRuta = nombreRuta;
    }
    public String getIdDTS() {
        return idDTS;
    }
    public void setIdDTS(String idDTS) {
        this.idDTS = idDTS;
    }
    public String getNombreDTS() {
        return nombreDTS;
    }
    public void setNombreDTS(String nombreDTS) {
        this.nombreDTS = nombreDTS;
    }
    public String getTipoDTS() {
        return tipoDTS;
    }
    public void setTipoDTS(String tipoDTS) {
        this.tipoDTS = tipoDTS;
    }
    public String getSecUsuarioId() {
        return secUsuarioId;
    }
    public void setSecUsuarioId(String secUsuarioId) {
        this.secUsuarioId = secUsuarioId;
    }
    public String getIdBodegaVendedor() {
        return idBodegaVendedor;
    }
    public void setIdBodegaVendedor(String idBodegaVendedor) {
        this.idBodegaVendedor = idBodegaVendedor;
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
    public InputVendedor getDatosVendedor() {
        return datosVendedor;
    }
    public void setDatosVendedor(InputVendedor datosVendedor) {
        this.datosVendedor = datosVendedor;
    }
    public List<InputPDV> getPuntoVenta() {
        return puntoVenta;
    }
    public void setPuntoVenta(List<InputPDV> puntoVenta) {
        this.puntoVenta = puntoVenta;
    }
}