package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class HistoricoInv {
    public static final String N_TABLA = "TC_SC_HISTORICO_INVSIDRA";
    public static final String SEQUENCE = "TC_SC_HISTORICO_INVSIDRA_SQ.nextval";
    public static final String SEQUENCE_TRASLADO = "TC_SC_TRASLADO_SQ.nextval";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TCSCLOGINVSIDRAID = "tcscloginvsidraid";
    public final static String CAMPO_TCSCTIPOTRANSACCIONID = "tcsctipotransaccionid";
    public final static String CAMPO_BODEGA_ORIGEN = "bodega_origen";
    public final static String CAMPO_BODEGA_DESTINO = "bodega_destino";
    public final static String CAMPO_ARTICULO = "articulo";
    public final static String CAMPO_SERIE = "serie";
    public final static String CAMPO_CANTIDAD = "cantidad";
    public final static String CAMPO_SERIE_ASOCIADA = "serie_asociada";
    public final static String CAMPO_COD_NUM = "cod_num";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_TIPO_INV = "tipo_inv";
    public final static String CAMPO_SERIE_FINAL = "serie_final";
    public final static String CAMPO_TCSCTRASLADOID = "tcsctrasladoid";
    public final static String CAMPO_precio = "precio";
    
    private BigDecimal tcscloginvsidraid;
    private BigDecimal tcScCatPaisId;
    private BigDecimal tcsctipotransaccionid;
    private BigDecimal bodega_origen;
    private BigDecimal bodega_destino;
    private String articulo;
    private String serie;
    private BigDecimal cantidad;
    private String serie_asociada;
    private BigDecimal cod_num;
    private String estado;
    private String creado_por;
    private Timestamp creado_el;
    private String tipo_inv;
    private String serie_final;
    private BigDecimal tcsctrasladoid;
    private BigDecimal precio;
    
	public BigDecimal getTcscloginvsidraid() {
		return tcscloginvsidraid;
	}
	public void setTcscloginvsidraid(BigDecimal tcscloginvsidraid) {
        this.tcscloginvsidraid = tcscloginvsidraid;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public BigDecimal getTcsctipotransaccionid() {
        return tcsctipotransaccionid;
	}
	public void setTcsctipotransaccionid(BigDecimal tcsctipotransaccionid) {
		this.tcsctipotransaccionid = tcsctipotransaccionid;
	}
	public BigDecimal getBodega_origen() {
		return bodega_origen;
	}
	public void setBodega_origen(BigDecimal bodega_origen) {
		this.bodega_origen = bodega_origen;
	}
	public BigDecimal getBodega_destino() {
		return bodega_destino;
	}
	public void setBodega_destino(BigDecimal bodega_destino) {
		this.bodega_destino = bodega_destino;
	}
	public String getArticulo() {
		return articulo;
	}
	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public BigDecimal getCantidad() {
		return cantidad;
	}
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	public String getSerie_asociada() {
		return serie_asociada;
	}
	public void setSerie_asociada(String serie_asociada) {
		this.serie_asociada = serie_asociada;
	}
	public BigDecimal getCod_num() {
		return cod_num;
	}
	public void setCod_num(BigDecimal cod_num) {
		this.cod_num = cod_num;
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
	public Timestamp getCreado_el() {
		return creado_el;
	}
	public void setCreado_el(Timestamp creado_el) {
		this.creado_el = creado_el;
	}
	public String getTipo_inv() {
		return tipo_inv;
	}
	public void setTipo_inv(String tipo_inv) {
		this.tipo_inv = tipo_inv;
	}
	public String getSerie_final() {
		return serie_final;
	}
	public void setSerie_final(String serie_final) {
		this.serie_final = serie_final;
	}
	public BigDecimal getTcsctrasladoid() {
		return tcsctrasladoid;
	}
	public void setTcsctrasladoid(BigDecimal tcsctrasladoid) {
		this.tcsctrasladoid = tcsctrasladoid;
	}
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	
	
}
