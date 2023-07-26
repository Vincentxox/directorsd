package com.consystec.sc.ca.ws.input.descuento;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "inputDescuento")
public class InputDescuento {
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String idDescuento;
    @XmlElement
    private String tipo;
    @XmlElement
    private String nombre;
    @XmlElement
    private String descripcion;
    @XmlElement
    private String tipoDescuento;
    @XmlElement
    private String configuracion;
    @XmlElement
    private String descuento;
    @XmlElement
    private String confRecarga;
    @XmlElement
    private String recarga;
    @XmlElement
    private String confPrecio;
    @XmlElement
    private String precio;
    @XmlElement
    private String fechaDesde;
    @XmlElement
    private String fechaHasta;
    @XmlElement
    private String idArticulo;
    @XmlElement
    private String nombreArt;
    @XmlElement
    private String precioArt;
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
    private String idRuta;
    @XmlElement
    private String idPanel;
    @XmlElement
    private List<InputDescuentoDet> descuentoDet;
    
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
    public String getIdDescuento() {
        return idDescuento;
    }
    public void setIdDescuento(String idDescuento) {
        this.idDescuento = idDescuento;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getTipoDescuento() {
        return tipoDescuento;
    }
    public void setTipoDescuento(String tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }
    public String getConfiguracion() {
        return configuracion;
    }
    public void setConfiguracion(String configuracion) {
        this.configuracion = configuracion;
    }
    public String getDescuento() {
        return descuento;
    }
    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }
    public String getConfRecarga() {
        return confRecarga;
    }
    public void setConfRecarga(String confRecarga) {
        this.confRecarga = confRecarga;
    }
    public String getRecarga() {
        return recarga;
    }
    public void setRecarga(String recarga) {
        this.recarga = recarga;
    }
    public String getConfPrecio() {
        return confPrecio;
    }
    public void setConfPrecio(String confPrecio) {
        this.confPrecio = confPrecio;
    }
    public String getPrecio() {
        return precio;
    }
    public void setPrecio(String precio) {
        this.precio = precio;
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
    public String getIdArticulo() {
        return idArticulo;
    }
    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }
    public String getNombreArt() {
        return nombreArt;
    }
    public void setNombreArt(String nombreArt) {
        this.nombreArt = nombreArt;
    }
    public String getPrecioArt() {
        return precioArt;
    }
    public void setPrecioArt(String precioArt) {
        this.precioArt = precioArt;
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
    public List<InputDescuentoDet> getDescuentoDet() {
        return descuentoDet;
    }
    public void setDescuentoDet(List<InputDescuentoDet> descuentoDet) {
        this.descuentoDet = descuentoDet;
    }
	public String getIdRuta() {
		return idRuta;
	}
	public void setIdRuta(String idRuta) {
		this.idRuta = idRuta;
	}
	public String getIdPanel() {
		return idPanel;
	}
	public void setIdPanel(String idPanel) {
		this.idPanel = idPanel;
	}
    
    
}