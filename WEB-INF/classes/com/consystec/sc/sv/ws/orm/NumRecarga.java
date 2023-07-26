package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class NumRecarga {
    public static final String N_TABLA = "TC_SC_NUMRECARGA";
    public static final String N_TABLA_ID = "TCSCNUMRECARGAID";
    public static final String SEQUENCE = "TC_SC_NUMRECARGA_SQ.NEXTVAL";

    public final static String CAMPO_TCSCNUMRECARGAID = "tcscnumrecargaid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_IDTIPO = "idtipo";
    public final static String CAMPO_TIPO = "tipo";
    public final static String CAMPO_NUM_RECARGA = "num_recarga";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_ESTADO_PAYMENT = "estado_payment";
    public final static String CAMPO_TCSCSOLICITUDID = "tcscsolicitudid";
    public final static String CAMPO_ORDEN = "orden";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcscnumrecargaid;
    private BigDecimal tcScCatPaisId;
    private BigDecimal idtipo;
	private String tipo;
	private BigDecimal num_recarga;
    private String estado;
    private String estado_payment;
    private BigDecimal tcscsolicitudid;
    private BigDecimal orden;
    private Timestamp creado_el;
    private String creado_por;
	private Timestamp modificado_el;
	private String modificado_por;
	
    public BigDecimal getTcscnumrecargaid() {
        return tcscnumrecargaid;
    }
    public void setTcscnumrecargaid(BigDecimal tcscnumrecargaid) {
        this.tcscnumrecargaid = tcscnumrecargaid;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public BigDecimal getIdtipo() {
        return idtipo;
    }
    public void setIdtipo(BigDecimal idtipo) {
        this.idtipo = idtipo;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public BigDecimal getNum_recarga() {
        return num_recarga;
    }
    public void setNum_recarga(BigDecimal num_recarga) {
        this.num_recarga = num_recarga;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getEstado_payment() {
        return estado_payment;
    }
    public void setEstado_payment(String estado_payment) {
        this.estado_payment = estado_payment;
    }
    public BigDecimal getTcscsolicitudid() {
        return tcscsolicitudid;
    }
    public void setTcscsolicitudid(BigDecimal tcscsolicitudid) {
        this.tcscsolicitudid = tcscsolicitudid;
    }
    public BigDecimal getOrden() {
        return orden;
    }
    public void setOrden(BigDecimal orden) {
        this.orden = orden;
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