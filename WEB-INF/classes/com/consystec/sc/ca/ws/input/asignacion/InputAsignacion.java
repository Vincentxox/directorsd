package com.consystec.sc.ca.ws.input.asignacion;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputAsignacion")
public class InputAsignacion implements Cloneable {
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String idAsignacionReserva;
    @XmlElement
    private String tipo;
    @XmlElement
    private String idDTS;
    @XmlElement
    private String idVendedor;
    @XmlElement
    private String nombreVendedor;
    @XmlElement
    private String idBodegaOrigen;
    @XmlElement
    private String nombreBodegaOrigen;
    @XmlElement
    private String idBodegaDestino;
    @XmlElement
    private String nombreBodegaDestino;
    @XmlElement
    private String observaciones;
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
    private String fechaInicio;
    @XmlElement
    private String fechaFin;
    @XmlElement
    private List<InputArticuloAsignacion> articulos;
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
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
    public String getIdAsignacionReserva() {
        return idAsignacionReserva;
    }
    public void setIdAsignacionReserva(String idAsignacionReserva) {
        this.idAsignacionReserva = idAsignacionReserva;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getIdDTS() {
        return idDTS;
    }
    public void setIdDTS(String idDTS) {
        this.idDTS = idDTS;
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
    public String getIdBodegaOrigen() {
        return idBodegaOrigen;
    }
    public void setIdBodegaOrigen(String idBodegaOrigen) {
        this.idBodegaOrigen = idBodegaOrigen;
    }
    public String getNombreBodegaOrigen() {
        return nombreBodegaOrigen;
    }
    public void setNombreBodegaOrigen(String nombreBodegaOrigen) {
        this.nombreBodegaOrigen = nombreBodegaOrigen;
    }
    public String getIdBodegaDestino() {
        return idBodegaDestino;
    }
    public void setIdBodegaDestino(String idBodegaDestino) {
        this.idBodegaDestino = idBodegaDestino;
    }
    public String getNombreBodegaDestino() {
        return nombreBodegaDestino;
    }
    public void setNombreBodegaDestino(String nombreBodegaDestino) {
        this.nombreBodegaDestino = nombreBodegaDestino;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
    public String getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public String getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
    public List<InputArticuloAsignacion> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<InputArticuloAsignacion> articulos) {
        this.articulos = articulos;
    }
}