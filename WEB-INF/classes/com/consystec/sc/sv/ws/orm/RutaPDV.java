package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class RutaPDV {
    public static final String N_TABLA = "TC_SC_RUTA_PDV";
    public static final String SEQUENCE = "TC_SC_RUTA_PDV_SQ.nextval";

    public final static String CAMPO_TCSCPUNTOVENTAID = "tcscpuntoventaid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TCSCRUTAID = "tcscrutaid";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";

    private BigDecimal tcscpuntoventaid;
    private BigDecimal tcsccatpaisid;
    private BigDecimal tcscrutaid;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    
    public BigDecimal getTcscpuntoventaid() {
        return tcscpuntoventaid;
    }
    public void setTcscpuntoventaid(BigDecimal tcscpuntoventaid) {
        this.tcscpuntoventaid = tcscpuntoventaid;
    }
    public BigDecimal getTcsccatpaisid() {
		return tcsccatpaisid;
	}
	public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
	}
	public BigDecimal getTcscrutaid() {
        return tcscrutaid;
    }
    public void setTcscrutaid(BigDecimal tcscrutaid) {
        this.tcscrutaid = tcscrutaid;
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
    public static String getnTabla() {
        return N_TABLA;
    }
    public static String getSequence() {
        return SEQUENCE;
    }
    public static String getCampoTcscpuntoventaid() {
        return CAMPO_TCSCPUNTOVENTAID;
    }
    public static String getCampoTcscrutaid() {
        return CAMPO_TCSCRUTAID;
    }
    public static String getCampoEstado() {
        return CAMPO_ESTADO;
    }
    public static String getCampoCreadoEl() {
        return CAMPO_CREADO_EL;
    }
    public static String getCampoCreadoPor() {
        return CAMPO_CREADO_POR;
    }
}