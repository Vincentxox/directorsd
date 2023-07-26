package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PromoOfertaCampania {
    public static final String N_TABLA = "TC_SC_DET_ARTPROMO";
    public static final String SEQUENCE = "TC_SC_DET_ARTPROMO_SQ.nextval";

    public final static String CAMPO_TCSCDETARTPROMOID = "tcscdetartpromoid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TCSCOFERTACAMPANIAID = "tcscofertacampaniaid";
    public final static String CAMPO_TCSCARTPROMOCIONALID = "tcscartpromocionalid";
    public final static String CAMPO_CANT_ARTICULOS = "cant_articulos";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcscdetartpromoid;
    private BigDecimal tcScCatPaisId;
    private BigDecimal tcscofertacampaniaid;
    private BigDecimal tcscartpromocionalid;
    private BigDecimal cant_articulos;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    
    public BigDecimal getTcscdetartpromoid() {
        return tcscdetartpromoid;
    }
    public void setTcscdetartpromoid(BigDecimal tcscdetartpromoid) {
        this.tcscdetartpromoid = tcscdetartpromoid;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public BigDecimal getTcscofertacampaniaid() {
        return tcscofertacampaniaid;
    }
    public void setTcscofertacampaniaid(BigDecimal tcscofertacampaniaid) {
        this.tcscofertacampaniaid = tcscofertacampaniaid;
    }
    public BigDecimal getTcscartpromocionalid() {
        return tcscartpromocionalid;
    }
    public void setTcscartpromocionalid(BigDecimal tcscartpromocionalid) {
        this.tcscartpromocionalid = tcscartpromocionalid;
    }
    public BigDecimal getCant_articulos() {
        return cant_articulos;
    }
    public void setCant_articulos(BigDecimal cant_articulos) {
        this.cant_articulos = cant_articulos;
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