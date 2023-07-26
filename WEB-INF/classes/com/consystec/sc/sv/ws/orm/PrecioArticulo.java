package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PrecioArticulo {
    public static final String N_TABLA = "TC_SC_PRECIO_ARTICULO";
    public static final String SEQUENCE = "TC_SC_PRECIO_ARTICULO_SQ.nextval";
    
    public final static String CAMPO_TCSCPRECIOARTICULOID = "tcscprecioarticuloid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_ARTICULO = "articulo";
    public final static String CAMPO_PRECIO = "precio";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_VERSION = "version";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";
    public final static String CAMPO_DESCUENTO = "descuento";
    public final static String CAMPO_TIPO_GESTION = "tipo_gestion";
    public final static String CAMPO_TASA_CAMBIO = "tasa_cambio";
    public final static String CAMPO_ID_PRODUCT_OFFERING="id_product_offering";
    
    private BigDecimal tcscprecioarticuloid;
    private BigDecimal tcsccatpaisid;
    private BigDecimal articulo;
    private BigDecimal precio;
    private String estado;
    private String version;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    private BigDecimal descuento;
    private String tipo_gestion;
    private String id_product_offering;
    
    
    public BigDecimal getTcscprecioarticuloid() {
        return tcscprecioarticuloid;
    }
    public void setTcscprecioarticuloid(BigDecimal tcscprecioarticuloid) {
        this.tcscprecioarticuloid = tcscprecioarticuloid;
    }
    public BigDecimal getTcsccatpaisid() {
		return tcsccatpaisid;
	}
	public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
	}
	public BigDecimal getArticulo() {
        return articulo;
    }
    public void setArticulo(BigDecimal articulo) {
        this.articulo = articulo;
    }
    public BigDecimal getPrecio() {
        return precio;
    }
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
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
    public BigDecimal getDescuento() {
        return descuento;
    }
    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
    public String getTipo_gestion() {
        return tipo_gestion;
    }
    public void setTipo_gestion(String tipo_gestion) {
        this.tipo_gestion = tipo_gestion;
    }
	public String getId_product_offering() {
		return id_product_offering;
	}
	public void setId_product_offering(String id_product_offering) {
		this.id_product_offering = id_product_offering;
	}
    
}