package com.consystec.sc.ca.ws.output.inventario;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Historico")
public class Historico {
	@XmlElement
	private String tcscloginvsidraid;
	@XmlElement
	private String idTraslado;
	@XmlElement
	private String codigoTransaccion;
	@XmlElement
	private String tipoMovimiento;
	@XmlElement
	private String bodegaOrigen;
	@XmlElement
	private String nombreBodegaOrigen;
	@XmlElement
	private String bodegaDestino;
	@XmlElement
	private String nombreBodegaDestino;
	@XmlElement
	private String articulo;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String precio;
	@XmlElement
	private String serie;
	@XmlElement
    private String serieFinal;
	@XmlElement
	private String cantidad;
	@XmlElement
	private String serieAsociada;
	@XmlElement
	private String codNum;
	@XmlElement
	private String estado;
	@XmlElement
	private String tipoInv;
	@XmlElement
	private String creado_por;
	@XmlElement
	private String creado_el;
	
    public String getTcscloginvsidraid() {
        return tcscloginvsidraid;
    }
    public void setTcscloginvsidraid(String tcscloginvsidraid) {
        this.tcscloginvsidraid = tcscloginvsidraid;
    }
    public String getCodigoTransaccion() {
        return codigoTransaccion;
    }
    public void setCodigoTransaccion(String codigoTransaccion) {
        this.codigoTransaccion = codigoTransaccion;
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
    public String getNombreBodegaOrigen() {
        return nombreBodegaOrigen;
    }
    public void setNombreBodegaOrigen(String nombreBodegaOrigen) {
        this.nombreBodegaOrigen = nombreBodegaOrigen;
    }
    public String getBodegaDestino() {
        return bodegaDestino;
    }
    public void setBodegaDestino(String bodegaDestino) {
        this.bodegaDestino = bodegaDestino;
    }
    public String getNombreBodegaDestino() {
        return nombreBodegaDestino;
    }
    public void setNombreBodegaDestino(String nombreBodegaDestino) {
        this.nombreBodegaDestino = nombreBodegaDestino;
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
    public String getCantidad() {
        return cantidad;
    }
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
    public String getSerieAsociada() {
        return serieAsociada;
    }
    public void setSerieAsociada(String serieAsociada) {
        this.serieAsociada = serieAsociada;
    }
    public String getCodNum() {
        return codNum;
    }
    public void setCodNum(String codNum) {
        this.codNum = codNum;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getTipoInv() {
        return tipoInv;
    }
    public void setTipoInv(String tipoInv) {
        this.tipoInv = tipoInv;
    }
    public String getCreado_por() {
        return creado_por;
    }
    public void setCreado_por(String creado_por) {
        this.creado_por = creado_por;
    }
    public String getCreado_el() {
        return creado_el;
    }
    public void setCreado_el(String creado_el) {
        this.creado_el = creado_el;
    }
	public String getIdTraslado() {
		return idTraslado;
	}
	public void setIdTraslado(String idTraslado) {
		this.idTraslado = idTraslado;
	}
	public String getPrecio() {
		return precio;
	}
	public void setPrecio(String precio) {
		this.precio = precio;
	}
    
}