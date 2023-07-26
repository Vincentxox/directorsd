package com.consystec.sc.ca.ws.input.ruta;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "inputVendedor")
public class InputVendedor {
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String idVendPanelPDV;
    @XmlElement
    private String vendedor;
    @XmlElement
    private String nombre;
    @XmlElement
    private String numRecarga;
    @XmlElement
    private String pin;
    @XmlElement
    private String dtsFuente;
    @XmlElement
    private String responsable;
    @XmlElement
    private String cantInventario;
    @XmlElement
    private String estado;
    
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
    public String getIdVendPanelPDV() {
        return idVendPanelPDV;
    }
    public void setIdVendPanelPDV(String idVendPanelPDV) {
        this.idVendPanelPDV = idVendPanelPDV;
    }
    public String getVendedor() {
        return vendedor;
    }
    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getNumRecarga() {
        return numRecarga;
    }
    public void setNumRecarga(String numRecarga) {
        this.numRecarga = numRecarga;
    }
    public String getPin() {
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }
    public String getDtsFuente() {
        return dtsFuente;
    }
    public void setDtsFuente(String dtsFuente) {
        this.dtsFuente = dtsFuente;
    }
    public String getResponsable() {
        return responsable;
    }
    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
    public String getCantInventario() {
        return cantInventario;
    }
    public void setCantInventario(String cantInventario) {
        this.cantInventario = cantInventario;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}