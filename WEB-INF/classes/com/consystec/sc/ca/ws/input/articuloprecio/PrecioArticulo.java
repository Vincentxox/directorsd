package com.consystec.sc.ca.ws.input.articuloprecio;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PrecioArticulo {
	public static final String N_TABLA = "TC_SC_PRECIO_ARTICULO";
	public static final String SEQUENCE = "TC_SC_PRECIO_ARTICULO_SQ.nextval";

	public final static String CAMPO_TCSCPRECIOARTICULOID = "TCSCPRECIOARTICULOID";
	public final static String CAMPO_TCSCCATPAIS_ID = "TCSCCATPAISID"; 
	public final static String CAMPO_ARTICULO = "ARTICULO";
	public final static String CAMPO_TIPO_GESTION = "TIPO_GESTION";
	public final static String CAMPO_PRECIO = "PRECIO";
	public final static String CAMPO_DESCUENTO = "DESCUENTO";
	public final static String CAMPO_COD_MONEDA = "COD_MONEDA";
	public final static String CAMPO_DES_MONEDA = "DES_MONEDA";
	public final static String CAMPO_TASA_CAMBIO = "TASA_CAMBIO";
	public final static String CAMPO_ESTADO = "ESTADO";
	public final static String CAMPO_VERSION = "VERSION";
	public final static String CAMPO_CREADO_EL = "CREADO_EL";
	public final static String CAMPO_CREADO_POR = "CREADO_POR";
	public final static String CAMPO_MODIFICADO_EL = "MODIFICADO_EL";
	public final static String CAMPO_MODIFICADO_POR = "MODIFICADO_POR";
	public final static String CAMPO_PRECIO_SIN_IMPUESTOS = "PRECIO_SIN_IMPUESTOS";
	public final static String CAMPO_ID_PRODUCT_OFFERING = "ID_PRODUCT_OFFERING";
	public final static String CAMPO_PRECIO_MIN = "PRECIO_MIN";
	public final static String CAMPO_PRECIO_MAX = "PRECIO_MAX";
	
	private BigDecimal tcscprecioarticuloid;
	private BigDecimal tcsccatpaisid;
	private BigDecimal articulo;
	private String tipo_gestion;
	private BigDecimal precio;
	private BigDecimal descuento;
	private String cod_moneda;
	private String des_moneda;
	private BigDecimal tasa_cambio;
	private String estado;
	private String version;
	private String creado_por;
	private Timestamp creado_el;
	private Timestamp modificado_el;
	private String modificado_por;
	private BigDecimal precio_sin_impuestos;
	private BigDecimal id_product_offering;
	private BigDecimal precio_min;
	private BigDecimal precio_max;
	
	
	public BigDecimal getPrecio_min() {
		return precio_min;
	}
	public void setPrecio_min(BigDecimal precio_min) {
		this.precio_min = precio_min;
	}
	public BigDecimal getPrecio_max() {
		return precio_max;
	}
	public void setPrecio_max(BigDecimal precio_max) {
		this.precio_max = precio_max;
	}
	
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
	public String getTipo_gestion() {
		return tipo_gestion;
	}
	public void setTipo_gestion(String tipo_gestion) {
		this.tipo_gestion = tipo_gestion;
	}
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	public BigDecimal getDescuento() {
		return descuento;
	}
	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}
	public String getCod_moneda() {
		return cod_moneda;
	}
	public void setCod_moneda(String cod_moneda) {
		this.cod_moneda = cod_moneda;
	}
	public String getDes_moneda() {
		return des_moneda;
	}
	public void setDes_moneda(String des_moneda) {
		this.des_moneda = des_moneda;
	}
	public BigDecimal getTasa_cambio() {
		return tasa_cambio;
	}
	public void setTasa_cambio(BigDecimal tasa_cambio) {
		this.tasa_cambio = tasa_cambio;
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
	public String getCreado_por() {
		return creado_por;
	}
	public void setCreado_por(String creado_por) {
		this.creado_por = creado_por;
	}
	public Timestamp getCreado_el() {
		return creado_el;
	}
	public void setCreado_el(Timestamp creado_el) {
		this.creado_el = creado_el;
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
	public BigDecimal getPrecio_sin_impuestos() {
		return precio_sin_impuestos;
	}
	public void setPrecio_sin_impuestos(BigDecimal precio_sin_impuestos) {
		this.precio_sin_impuestos = precio_sin_impuestos;
	}
	public BigDecimal getId_product_offering() {
		return id_product_offering;
	}
	public void setId_product_offering(BigDecimal id_product_offering) {
		this.id_product_offering = id_product_offering;
	}
	
	
}
