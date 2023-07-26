package com.consystec.sc.ca.ws.input.inventario;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputHistoricoInv")
public class InputHistoricoInv {
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String idTraslado;
    @XmlElement
    private String tipoMovimiento;
    @XmlElement
    private String bodegaOrigen;
    @XmlElement
    private String bodegaDestino;
    @XmlElement
    private String articulo;
    @XmlElement
    private String descripcion;
    @XmlElement
    private String serie;
    @XmlElement
    private String serieFinal;
    @XmlElement
    private String tipoInv;
    @XmlElement
    private String fechaInicio;
    @XmlElement
    private String fechaFin;
    
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
    public String getTipoMovimiento() {
        return tipoMovimiento;
    }
    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
    public String getBodegaOrigen() {
        return bodegaOrigen;
    }
    public void setBodegaOrigen(String bodegaOrigen) {
        this.bodegaOrigen = bodegaOrigen;
    }
    public String getBodegaDestino() {
        return bodegaDestino;
    }
    public void setBodegaDestino(String bodegaDestino) {
        this.bodegaDestino = bodegaDestino;
    }
    public String getArticulo() {
        return articulo;
    }
    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getSerie() {
        return serie;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }
    public String getSerieFinal() {
        return serieFinal;
    }
    public void setSerieFinal(String serieFinal) {
        this.serieFinal = serieFinal;
    }
    public String getTipoInv() {
        return tipoInv;
    }
    public void setTipoInv(String tipoInv) {
        this.tipoInv = tipoInv;
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
	public String getIdTraslado() {
		return idTraslado;
	}
	public void setIdTraslado(String idTraslado) {
		this.idTraslado = idTraslado;
	}
    
}