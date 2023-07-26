package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CuentaResumenDTS {
    public static final String N_TABLA = "TC_SC_CTA_DTS_RESUMEN";
    public static final String SEQUENCE = "TC_SC_CTA_DTS_RESUMEN_SQ.nextval";

    public final static String CAMPO_TCSCCTADTSRESUMENID = "tcscctadtsresumenid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TCSCDTSID = "tcscdtsid";
    public final static String CAMPO_DEBE = "debe";
    public final static String CAMPO_HABER = "haber";
    public final static String CAMPO_SALDO = "saldo";
    public final static String CAMPO_MONEDA = "moneda";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_CREADO_EL = "creado_el";

    private BigDecimal tcscctadtsresumenid;
    private BigDecimal tcScCatPaisId;
    private BigDecimal tcscdtsid;
    private BigDecimal debe;
    private BigDecimal haber;
    private BigDecimal saldo;
    private String moneda;
    private String creado_por;
    private Timestamp creado_el;

    public BigDecimal getTcscctadtsresumenid() {
        return tcscctadtsresumenid;
    }
    public void setTcscctadtsresumenid(BigDecimal tcscctadtsresumenid) {
        this.tcscctadtsresumenid = tcscctadtsresumenid;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public BigDecimal getTcscdtsid() {
        return tcscdtsid;
    }
    public void setTcscdtsid(BigDecimal tcscdtsid) {
        this.tcscdtsid = tcscdtsid;
    }
    public BigDecimal getDebe() {
        return debe;
    }
    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }
    public BigDecimal getHaber() {
        return haber;
    }
    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }
    public BigDecimal getSaldo() {
        return saldo;
    }
    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
    public String getMoneda() {
        return moneda;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
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
}