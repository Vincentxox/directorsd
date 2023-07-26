package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CondicionCampania {
	public static final String N_TABLA = "TC_SC_DET_CONDICION_CAMPANIA";
	public static final String SEQUENCE = "TC_SC_DET_COND_CAMPANIA_SQ.nextval";
	
	public final static String CAMPO_TCSCDETCONDICIONCAMPANIAID = "tcscdetcondicioncampaniaid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	public final static String CAMPO_TCSCCONDICIONID = "tcsccondicionid";
	public final static String CAMPO_TIPO = "tipo";
	public final static String CAMPO_ARTICULO = "articulo";
	public final static String CAMPO_TECNOLOGIA = "tecnologia";	
	public final static String CAMPO_MONTO_INICIAL = "monto_inicial";
	public final static String CAMPO_MONTO_FINAL = "monto_final";
	public final static String CAMPO_ESTADO = "estado";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	
	private BigDecimal tcscdetcondicioncampaniaid;
    private BigDecimal tcScCatPaisId;
	private BigDecimal tcsccondicionid;
	private String tipo;
	private BigDecimal articulo;
	private String tecnologia;
	private BigDecimal monto_inicial;
	private BigDecimal monto_final;
	private String estado;
	private Timestamp creado_el;
	private String creado_por;
	private Timestamp modificado_el;
	private String modificado_por;
	
    public BigDecimal getTcscdetcondicioncampaniaid() {
        return tcscdetcondicioncampaniaid;
    }
    public void setTcscdetcondicioncampaniaid(BigDecimal tcscdetcondicioncampaniaid) {
        this.tcscdetcondicioncampaniaid = tcscdetcondicioncampaniaid;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public BigDecimal getTcsccondicionid() {
        return tcsccondicionid;
    }
    public void setTcsccondicionid(BigDecimal tcsccondicionid) {
        this.tcsccondicionid = tcsccondicionid;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public BigDecimal getArticulo() {
        return articulo;
    }
    public void setArticulo(BigDecimal articulo) {
        this.articulo = articulo;
    }
    public String getTecnologia() {
        return tecnologia;
    }
    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }
    public BigDecimal getMonto_inicial() {
        return monto_inicial;
    }
    public void setMonto_inicial(BigDecimal monto_inicial) {
        this.monto_inicial = monto_inicial;
    }
    public BigDecimal getMonto_final() {
        return monto_final;
    }
    public void setMonto_final(BigDecimal monto_final) {
        this.monto_final = monto_final;
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