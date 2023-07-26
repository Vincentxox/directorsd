package com.consystec.sc.ca.ws.input.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputReportePDV")
public class InputReportePDV {
    @XmlElement
    private String codArea;
    @XmlElement
	private String usuario;
    @XmlElement
    private String fecha;
	@XmlElement
	private String idDistribuidor;
	@XmlElement
    private String nombreDistribuidor;
	@XmlElement
	private String idPDV;
	@XmlElement
    private String nombrePDV;
	@XmlElement
    private String idRuta;
    @XmlElement
    private String nombreRuta;
    @XmlElement
    private String idVendedor;
    @XmlElement
    private String canal;
    @XmlElement
    private String subcanal;
    @XmlElement
    private String categoria;
    @XmlElement
    private String departamento;
    @XmlElement
    private String municipio;
    @XmlElement
    private String distrito;
    @XmlElement
    private String estado;
    @XmlElement
    private String creado_por;
    @XmlElement
    private String creado_el;
    @XmlElement
    private String anio;
    @XmlElement
    private String mes;
    @XmlElement
    private String sumFacturacion;
	@XmlElement
	private String fechaDesde;
	@XmlElement
    private String fechaHasta;
    @XmlElement
    private List<InputReportePDV> puntosDeVenta;
    @XmlElement
    private List<InputReportePDV> ventas;

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
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getIdDistribuidor() {
        return idDistribuidor;
    }
    public void setIdDistribuidor(String idDistribuidor) {
        this.idDistribuidor = idDistribuidor;
    }
    public String getNombreDistribuidor() {
        return nombreDistribuidor;
    }
    public void setNombreDistribuidor(String nombreDistribuidor) {
        this.nombreDistribuidor = nombreDistribuidor;
    }
    public String getIdPDV() {
        return idPDV;
    }
    public void setIdPDV(String idPDV) {
        this.idPDV = idPDV;
    }
    public String getNombrePDV() {
        return nombrePDV;
    }
    public void setNombrePDV(String nombrePDV) {
        this.nombrePDV = nombrePDV;
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
    public String getIdVendedor() {
        return idVendedor;
    }
    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }
    public String getCanal() {
        return canal;
    }
    public void setCanal(String canal) {
        this.canal = canal;
    }
    public String getSubcanal() {
        return subcanal;
    }
    public void setSubcanal(String subcanal) {
        this.subcanal = subcanal;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
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
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
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
    public String getAnio() {
        return anio;
    }
    public void setAnio(String anio) {
        this.anio = anio;
    }
    public String getMes() {
        return mes;
    }
    public void setMes(String mes) {
        this.mes = mes;
    }
    public String getSumFacturacion() {
        return sumFacturacion;
    }
    public void setSumFacturacion(String sumFacturacion) {
        this.sumFacturacion = sumFacturacion;
    }
    public String getFechaDesde() {
        return fechaDesde;
    }
    public void setFechaDesde(String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }
    public String getFechaHasta() {
        return fechaHasta;
    }
    public void setFechaHasta(String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
    public List<InputReportePDV> getPuntosDeVenta() {
        return puntosDeVenta;
    }
    public void setPuntosDeVenta(List<InputReportePDV> puntosDeVenta) {
        this.puntosDeVenta = puntosDeVenta;
    }
    public List<InputReportePDV> getVentas() {
        return ventas;
    }
    public void setVentas(List<InputReportePDV> ventas) {
        this.ventas = ventas;
    }
}