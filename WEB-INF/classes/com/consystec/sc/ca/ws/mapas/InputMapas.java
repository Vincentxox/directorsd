package com.consystec.sc.ca.ws.mapas;

import java.math.BigDecimal;
import java.util.List;

public class InputMapas {
    private String pais;
    private BigDecimal id_distribuidor;
    private String nombre_distribuidor;
    private String canal;
    private BigDecimal id_pdv;
    private String nombre_pdv;
    private String latitud;
    private String longitud;
    private String estado;
    private BigDecimal id_panelruta;
    private String nombre_panelruta;
    private String tipo_panelruta;
    private String ultima_visita;
    private BigDecimal id;
    private String descripcion;
    private String lat;
    private String lng;
    private String nombre_producto;
    private String tecnologia;
    private BigDecimal cantidad;
    private BigDecimal monto;
    private BigDecimal venta_total;
    private Integer tipo;
    private List<InputMapas> productos;

    public String getPais() {
        return pais;
    }
    public void setPais(String pais) {
        this.pais = pais;
    }
    public BigDecimal getId_distribuidor() {
        return id_distribuidor;
    }
    public void setId_distribuidor(BigDecimal id_distribuidor) {
        this.id_distribuidor = id_distribuidor;
    }
    public String getNombre_distribuidor() {
        return nombre_distribuidor;
    }
    public void setNombre_distribuidor(String nombre_distribuidor) {
        this.nombre_distribuidor = nombre_distribuidor;
    }
    public String getCanal() {
        return canal;
    }
    public void setCanal(String canal) {
        this.canal = canal;
    }
    public BigDecimal getId_pdv() {
        return id_pdv;
    }
    public void setId_pdv(BigDecimal id_pdv) {
        this.id_pdv = id_pdv;
    }
    public String getNombre_pdv() {
        return nombre_pdv;
    }
    public void setNombre_pdv(String nombre_pdv) {
        this.nombre_pdv = nombre_pdv;
    }
    public String getLatitud() {
        return latitud;
    }
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }
    public String getLongitud() {
        return longitud;
    }
    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public BigDecimal getId_panelruta() {
        return id_panelruta;
    }
    public void setId_panelruta(BigDecimal id_panelruta) {
        this.id_panelruta = id_panelruta;
    }
    public String getNombre_panelruta() {
        return nombre_panelruta;
    }
    public void setNombre_panelruta(String nombre_panelruta) {
        this.nombre_panelruta = nombre_panelruta;
    }
    public String getTipo_panelruta() {
        return tipo_panelruta;
    }
    public void setTipo_panelruta(String tipo_panelruta) {
        this.tipo_panelruta = tipo_panelruta;
    }
    public String getUltima_visita() {
        return ultima_visita;
    }
    public void setUltima_visita(String ultima_visita) {
        this.ultima_visita = ultima_visita;
    }
    public BigDecimal getId() {
        return id;
    }
    public void setId(BigDecimal id) {
        this.id = id;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLng() {
        return lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }
    public String getNombre_producto() {
        return nombre_producto;
    }
    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }
    public String getTecnologia() {
        return tecnologia;
    }
    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }
    public BigDecimal getCantidad() {
        return cantidad;
    }
    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }
    public BigDecimal getMonto() {
        return monto;
    }
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    public BigDecimal getVenta_total() {
        return venta_total;
    }
    public void setVenta_total(BigDecimal venta_total) {
        this.venta_total = venta_total;
    }
    public Integer getTipo() {
        return tipo;
    }
    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
    public List<InputMapas> getProductos() {
        return productos;
    }
    public void setProductos(List<InputMapas> productos) {
        this.productos = productos;
    }
}