package com.consystec.sc.ca.ws.output.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CumplimientoVenta {
    @XmlElement
    private String fecha;
    @XmlElement
    private String idPuntoVenta;
    @XmlElement
    private String idDistribuidor;
    @XmlElement
    private String nombreDistribuidor;
    @XmlElement
    private String idBodegaVirtual;
    @XmlElement
    private String nombreBodega;
    @XmlElement
    private String departamento;
    @XmlElement
    private String municipio;
    @XmlElement
    private String distrito;
    @XmlElement
    private String idRuta;
    @XmlElement
    private String nombreRuta;
    @XmlElement
    private String idVendedor;
    @XmlElement
    private String usuario;
    @XmlElement
    private String nombreVendedor;
    @XmlElement
    private String nombrePdv;
    @XmlElement
    private String telPrimario;
    @XmlElement
    private String tipoProducto;
    @XmlElement
    private String tipoNegocio;
    @XmlElement
    private String idVenta;
    @XmlElement
    private List<ArticuloVendido> articulosVendidos;
    @XmlElement
    private List<TarjetaVendida> tarjetas;

    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getIdPuntoVenta() {
        return idPuntoVenta;
    }
    public void setIdPuntoVenta(String idPuntoVenta) {
        this.idPuntoVenta = idPuntoVenta;
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
    public String getIdBodegaVirtual() {
        return idBodegaVirtual;
    }
    public void setIdBodegaVirtual(String idBodegaVirtual) {
        this.idBodegaVirtual = idBodegaVirtual;
    }
    public String getNombreBodega() {
        return nombreBodega;
    }
    public void setNombreBodega(String nombreBodega) {
        this.nombreBodega = nombreBodega;
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
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getNombreVendedor() {
        return nombreVendedor;
    }
    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }
    public String getNombrePdv() {
        return nombrePdv;
    }
    public void setNombrePdv(String nombrePdv) {
        this.nombrePdv = nombrePdv;
    }
    public String getTelPrimario() {
        return telPrimario;
    }
    public void setTelPrimario(String telPrimario) {
        this.telPrimario = telPrimario;
    }
    public String getTipoProducto() {
        return tipoProducto;
    }
    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }
    public String getTipoNegocio() {
        return tipoNegocio;
    }
    public void setTipoNegocio(String tipoNegocio) {
        this.tipoNegocio = tipoNegocio;
    }
    public String getIdVenta() {
        return idVenta;
    }
    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }
    public List<ArticuloVendido> getArticulosVendidos() {
        return articulosVendidos;
    }
    public void setArticulosVendidos(List<ArticuloVendido> articulosVendidos) {
        this.articulosVendidos = articulosVendidos;
    }
    public List<TarjetaVendida> getTarjetas() {
        return tarjetas;
    }
    public void setTarjetas(List<TarjetaVendida> tarjetas) {
        this.tarjetas = tarjetas;
    }
}