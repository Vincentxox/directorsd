package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Promocionales {
    public static final String N_TABLA = "TC_SC_ART_PROMOCIONAL";
    public static final String SEQUENCE = "TC_SC_ART_PROMOCIONAL_SQ.nextval";

    public final static String CAMPO_TCSCARTPROMOCIONALID = "tcscartpromocionalid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_COD_ARTICULO = "cod_articulo";
    public final static String CAMPO_DESCRIPCION = "descripcion";
    public final static String CAMPO_TIPO_GRUPO = "tipo_grupo";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_ID_PRODUCT_OFFERING = "id_product_offering";
    
    private BigDecimal tcscartpromocionalid;
    private BigDecimal tcScCatPaisId;
    private String cod_articulo;
    private String descripcion;
    private String tipo_grupo;
    private String estado;
    private BigDecimal id_product_offering;
    private String creado_por;
    private Timestamp creado_el;
    private String modificado_por;
    private Timestamp modificado_el;
    
    public BigDecimal getTcscartpromocionalid() {
        return tcscartpromocionalid;
    }
    public void setTcscartpromocionalid(BigDecimal tcscartpromocionalid) {
        this.tcscartpromocionalid = tcscartpromocionalid;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public String getCod_articulo() {
        return cod_articulo;
    }
    public void setCod_articulo(String cod_articulo) {
        this.cod_articulo = cod_articulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getTipo_grupo() {
        return tipo_grupo;
    }
    public void setTipo_grupo(String tipo_grupo) {
        this.tipo_grupo = tipo_grupo;
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
    public String getModificado_por() {
        return modificado_por;
    }
    public void setModificado_por(String modificado_por) {
        this.modificado_por = modificado_por;
    }
    public Timestamp getModificado_el() {
        return modificado_el;
    }
    public void setModificado_el(Timestamp modificado_el) {
        this.modificado_el = modificado_el;
    }
	public BigDecimal getId_product_offering() {
		return id_product_offering;
	}
	public void setId_product_offering(BigDecimal id_product_offering) {
		this.id_product_offering = id_product_offering;
	}
    
}