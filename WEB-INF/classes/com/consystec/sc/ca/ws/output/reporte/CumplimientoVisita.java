package com.consystec.sc.ca.ws.output.reporte;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CumplimientoVisita {
    @XmlElement
    private String codPvd;
    @XmlElement
    private String codDistribuidor;
    @XmlElement
    private String distribuidor;
    @XmlElement
    private String codBodega;
    @XmlElement
    private String bodega;
    @XmlElement
    private String departamento;
    @XmlElement
    private String municipio;
    @XmlElement
    private String distrito;
    @XmlElement
    private String usuario;
    @XmlElement
    private String vendedor;
    @XmlElement
    private String ruta;
    @XmlElement
    private String nombrePdv;
    @XmlElement
    private String dia;
    @XmlElement
    private String mes;
    @XmlElement
    private String anio;
    @XmlElement
    private String fecha;
    @XmlElement
    private String asignado;
    @XmlElement
    private String gestion;
    @XmlElement
    private String observaciones;

    public String getCodPvd() {
        return codPvd;
    }
    public void setCodPvd(String codPvd) {
        this.codPvd = codPvd;
    }
    public String getCodDistribuidor() {
        return codDistribuidor;
    }
    public void setCodDistribuidor(String codDistribuidor) {
        this.codDistribuidor = codDistribuidor;
    }
    public String getDistribuidor() {
        return distribuidor;
    }
    public void setDistribuidor(String distribuidor) {
        this.distribuidor = distribuidor;
    }
    public String getCodBodega() {
        return codBodega;
    }
    public void setCodBodega(String codBodega) {
        this.codBodega = codBodega;
    }
    public String getBodega() {
        return bodega;
    }
    public void setBodega(String bodega) {
        this.bodega = bodega;
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
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getVendedor() {
        return vendedor;
    }
    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }
    public String getRuta() {
        return ruta;
    }
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
    public String getNombrePdv() {
        return nombrePdv;
    }
    public void setNombrePdv(String nombrePdv) {
        this.nombrePdv = nombrePdv;
    }
    public String getDia() {
        return dia;
    }
    public void setDia(String dia) {
        this.dia = dia;
    }
    public String getMes() {
        return mes;
    }
    public void setMes(String mes) {
        this.mes = mes;
    }
    public String getAnio() {
        return anio;
    }
    public void setAnio(String anio) {
        this.anio = anio;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getAsignado() {
        return asignado;
    }
    public void setAsignado(String asignado) {
        this.asignado = asignado;
    }
    public String getGestion() {
        return gestion;
    }
    public void setGestion(String gestion) {
        this.gestion = gestion;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}