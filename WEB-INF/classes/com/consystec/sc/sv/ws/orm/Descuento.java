package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Descuento {
    public static final String N_TABLA = "TC_SC_DESCUENTO";
    public static final String SEQUENCE = "TC_SC_DESCUENTO_SQ.nextval";

    public final static String CAMPO_TC_SC_DESCUENTO_ID = "tcscdescuentoid";
    public final static String CAMPO_TIPO = "tipo";
    public final static String CAMPO_NOMBRE = "nombre";
    public final static String CAMPO_DESCRIPCION = "descripcion";
    public final static String CAMPO_TIPO_DESCUENTO = "tipo_descuento";
    public final static String CAMPO_CONFIGURACION = "configuracion";
    public final static String CAMPO_DESCUENTO = "descuento";
    public final static String CAMPO_CONF_RECARGA = "conf_recarga";
    public final static String CAMPO_RECARGA = "recarga";
    public final static String CAMPO_CONF_PRECIO = "conf_precio";
    public final static String CAMPO_PRECIO = "precio";
    public final static String CAMPO_FECHA_DESDE = "fecha_desde";
    public final static String CAMPO_FECHA_HASTA = "fecha_hasta";
    public final static String CAMPO_ARTICULOID = "articuloid";
    public final static String CAMPO_NOMBRE_ART = "nombre_art";
    public final static String CAMPO_PRECIO_ART = "precio_art";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcscdescuentoid;
    private String tipo;
    private String nombre;
    private String descripcion;
    private BigDecimal tipo_descuento;
    private String configuracion;
    private BigDecimal descuento;
    private String confRecarga;
    private BigDecimal recarga;
    private String confPrecio;
    private BigDecimal precio;
    private Timestamp fecha_desde;
    private Timestamp fecha_hasta;
    private BigDecimal articuloid;
    private String nombre_art;
    private BigDecimal precio_art;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    
    public BigDecimal getTcscdescuentoid() {
        return tcscdescuentoid;
    }
    public void setTcscdescuentoid(BigDecimal tcscdescuentoid) {
        this.tcscdescuentoid = tcscdescuentoid;
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
    public BigDecimal getTipo_descuento() {
        return tipo_descuento;
    }
    public void setTipo_descuento(BigDecimal tipo_descuento) {
        this.tipo_descuento = tipo_descuento;
    }
    public String getConfiguracion() {
        return configuracion;
    }
    public void setConfiguracion(String configuracion) {
        this.configuracion = configuracion;
    }
    public BigDecimal getDescuento() {
        return descuento;
    }
    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
    public String getConfRecarga() {
        return confRecarga;
    }
    public void setConfRecarga(String confRecarga) {
        this.confRecarga = confRecarga;
    }
    public BigDecimal getRecarga() {
        return recarga;
    }
    public void setRecarga(BigDecimal recarga) {
        this.recarga = recarga;
    }
    public String getConfPrecio() {
        return confPrecio;
    }
    public void setConfPrecio(String confPrecio) {
        this.confPrecio = confPrecio;
    }
    public BigDecimal getPrecio() {
        return precio;
    }
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
    public Timestamp getFecha_desde() {
        return fecha_desde;
    }
    public void setFecha_desde(Timestamp fecha_desde) {
        this.fecha_desde = fecha_desde;
    }
    public Timestamp getFecha_hasta() {
        return fecha_hasta;
    }
    public void setFecha_hasta(Timestamp fecha_hasta) {
        this.fecha_hasta = fecha_hasta;
    }
    public BigDecimal getArticuloid() {
        return articuloid;
    }
    public void setArticuloid(BigDecimal articuloid) {
        this.articuloid = articuloid;
    }
    public String getNombre_art() {
        return nombre_art;
    }
    public void setNombre_art(String nombre_art) {
        this.nombre_art = nombre_art;
    }
    public BigDecimal getPrecio_art() {
        return precio_art;
    }
    public void setPrecio_art(BigDecimal precio_art) {
        this.precio_art = precio_art;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public Timestamp getCreado_el() {
        return creado_el;
    }
    public void setCreado_el(Timestamp creado_el) {
        this.creado_el = creado_el;
    }
    public String getCreado_por() {
        return creado_por;
    }
    public void setCreado_por(String creado_por) {
        this.creado_por = creado_por;
    }
    public Timestamp getModificado_el() {
        return modificado_el;
    }
    public void setModificado_el(Timestamp modificado_el) {
        this.modificado_el = modificado_el;
    }
    public String getModificado_por() {
        return modificado_por;
    }
    public void setModificado_por(String modificado_por) {
        this.modificado_por = modificado_por;
    }
}