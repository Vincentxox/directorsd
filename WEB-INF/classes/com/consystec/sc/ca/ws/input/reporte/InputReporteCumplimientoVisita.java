package com.consystec.sc.ca.ws.input.reporte;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputReporteCumplimientoVisita")
public class InputReporteCumplimientoVisita {
	@XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
	private String idDistribuidor;
	@XmlElement
	private String idRuta;
	@XmlElement
	private String idPuntoVenta;
	@XmlElement
	private String departamento;
	@XmlElement
    private String municipio;
	@XmlElement
    private String distrito;
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
    public String getIdDistribuidor() {
        return idDistribuidor;
    }
    public void setIdDistribuidor(String idDistribuidor) {
        this.idDistribuidor = idDistribuidor;
    }
    public String getIdRuta() {
        return idRuta;
    }
    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }
    public String getIdPuntoVenta() {
        return idPuntoVenta;
    }
    public void setIdPuntoVenta(String idPuntoVenta) {
        this.idPuntoVenta = idPuntoVenta;
    }
    public String getDepartamento() {
        return departamento;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    public String getMunicipio() {
        return municipio;
    }
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
    public String getDistrito() {
        return distrito;
    }
    public void setDistrito(String distrito) {
        this.distrito = distrito;
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