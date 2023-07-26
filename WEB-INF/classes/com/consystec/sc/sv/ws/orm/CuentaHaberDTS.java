package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CuentaHaberDTS {
    public static final String N_TABLA = "TC_SC_CTA_HABER_DTS";
    public static final String SEQUENCE = "TC_SC_CTA_HABER_DTS_SQ.nextval";

    public final static String CAMPO_TCSCCTAHABERDTSID = "tcscctahaberdtsid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TCSCDTSID = "tcscdtsid";
    public final static String CAMPO_BANCO = "banco";
    public final static String CAMPO_CUENTA = "cuenta";
    public final static String CAMPO_NO_DEPOSITO = "no_deposito";
    public final static String CAMPO_MONTO_DEPOSITO = "monto_deposito";
    public final static String CAMPO_FECHA_DEPOSITO = "fecha_deposito";
    public final static String CAMPO_MONEDA = "moneda";
    public final static String CAMPO_SOC = "soc";
    public final static String CAMPO_ASIGNACION = "asignacion";
    public final static String CAMPO_REFERENCIA = "referencia";
    public final static String CAMPO_USUARIO = "usuario";
    public final static String CAMPO_CTA_CP = "cta_cp";
    public final static String CAMPO_FE_CONTAB = "fe_contab";
    public final static String CAMPO_FECHA_DOC = "fecha_doc";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_CREADO_EL = "creado_el";

    private BigDecimal tcscctahaberdtsid;
    private BigDecimal tcScCatPaisId;
    private BigDecimal tcscdtsid;
    private String banco;
    private String cuenta;
    private String no_deposito;
    private String monto_deposito;
    private Timestamp fecha_deposito;
    private String moneda;
    private String soc;
    private BigDecimal asignacion;
    private BigDecimal referencia;
    private String usuario;
    private BigDecimal cta_cp;
    private Timestamp fe_contab;
    private Timestamp fecha_doc;
    private String creado_por;
    private Timestamp creado_el;

    public BigDecimal getTcscctahaberdtsid() {
        return tcscctahaberdtsid;
    }
    public void setTcscctahaberdtsid(BigDecimal tcscctahaberdtsid) {
        this.tcscctahaberdtsid = tcscctahaberdtsid;
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
    public String getBanco() {
        return banco;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }
    public String getCuenta() {
        return cuenta;
    }
    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }
    public String getNo_deposito() {
        return no_deposito;
    }
    public void setNo_deposito(String no_deposito) {
        this.no_deposito = no_deposito;
    }
    public String getMonto_deposito() {
        return monto_deposito;
    }
    public void setMonto_deposito(String monto_deposito) {
        this.monto_deposito = monto_deposito;
    }
    public Timestamp getFecha_deposito() {
        return fecha_deposito;
    }
    public void setFecha_deposito(Timestamp fecha_deposito) {
        this.fecha_deposito = fecha_deposito;
    }
    public String getMoneda() {
        return moneda;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    public String getSoc() {
        return soc;
    }
    public void setSoc(String soc) {
        this.soc = soc;
    }
    public BigDecimal getAsignacion() {
        return asignacion;
    }
    public void setAsignacion(BigDecimal asignacion) {
        this.asignacion = asignacion;
    }
    public BigDecimal getReferencia() {
        return referencia;
    }
    public void setReferencia(BigDecimal referencia) {
        this.referencia = referencia;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public BigDecimal getCta_cp() {
        return cta_cp;
    }
    public void setCta_cp(BigDecimal cta_cp) {
        this.cta_cp = cta_cp;
    }
    public Timestamp getFe_contab() {
        return fe_contab;
    }
    public void setFe_contab(Timestamp fe_contab) {
        this.fe_contab = fe_contab;
    }
    public Timestamp getFecha_doc() {
        return fecha_doc;
    }
    public void setFecha_doc(Timestamp fecha_doc) {
        this.fecha_doc = fecha_doc;
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