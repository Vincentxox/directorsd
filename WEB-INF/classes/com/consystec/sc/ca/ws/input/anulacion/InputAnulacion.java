package com.consystec.sc.ca.ws.input.anulacion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputAnulacion")
public class InputAnulacion {
    @XmlElement
    private String token;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String codArea;
	@XmlElement
	private String usuario;
	@XmlElement
    private String idAnulacion;
	@XmlElement
    private String fechaAnulacion;
	@XmlElement
	private String idJornada;
	@XmlElement
	private String idVendedor;
	@XmlElement
    private String nombreVendedor;
	@XmlElement
	private String idVenta;
	@XmlElement
	private String razonAnulacion;
	@XmlElement
	private String observaciones;
	@XmlElement
    private String creado_el;
    @XmlElement
    private String creado_por;
    @XmlElement
    private String fechaInicio;
    @XmlElement
    private String fechaFin;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getCodDispositivo() {
        return codDispositivo;
    }
    public void setCodDispositivo(String codDispositivo) {
        this.codDispositivo = codDispositivo;
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
    public String getIdAnulacion() {
        return idAnulacion;
    }
    public void setIdAnulacion(String idAnulacion) {
        this.idAnulacion = idAnulacion;
    }
    public String getFechaAnulacion() {
        return fechaAnulacion;
    }
    public void setFechaAnulacion(String fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }
    public String getIdJornada() {
        return idJornada;
    }
    public void setIdJornada(String idJornada) {
        this.idJornada = idJornada;
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
    public String getIdVenta() {
        return idVenta;
    }
    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }
    public String getRazonAnulacion() {
        return razonAnulacion;
    }
    public void setRazonAnulacion(String razonAnulacion) {
        this.razonAnulacion = razonAnulacion;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
}