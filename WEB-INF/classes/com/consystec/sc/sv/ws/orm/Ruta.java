package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Ruta {
    public static final String N_TABLA = "TC_SC_RUTA";
    public static final String SEQUENCE = "TC_SC_RUTA_SQ.nextval";

    public final static String CAMPO_TC_SC_RUTA_ID = "tcscrutaid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TC_SC_BODEGAVIRTUAL_ID = "tcscbodegavirtualid";
    public final static String CAMPO_TC_SC_DTS_ID = "tcscdtsid";
    public final static String CAMPO_NOMBRE = "nombre";
    public final static String CAMPO_SEC_USUARIO_ID = "secusuarioid";
    public final static String CAMPO_NUM_RECARGA = "num_recarga";
    public final static String CAMPO_PIN = "pin";
    public final static String CAMPO_DTS_FUENTE = "dts_fuente";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcScRutaId;
    private BigDecimal tcScCatPaisId;
    private BigDecimal tcScBodegaVirtualId;
    private BigDecimal tcScDtsId;
    private String nombre;
    private BigDecimal secusuarioid;
    private BigDecimal num_recarga;
    private BigDecimal pin;
    private String dts_fuente;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;

    public BigDecimal getTcScRutaId() {
        return tcScRutaId;
    }
    public void setTcScRutaId(BigDecimal tcScRutaId) {
        this.tcScRutaId = tcScRutaId;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public BigDecimal getTcScBodegaVirtualId() {
        return tcScBodegaVirtualId;
    }
    public void setTcScBodegaVirtualId(BigDecimal tcScBodegaVirtualId) {
        this.tcScBodegaVirtualId = tcScBodegaVirtualId;
    }
    public BigDecimal getTcScDtsId() {
        return tcScDtsId;
    }
    public void setTcScDtsId(BigDecimal tcScDtsId) {
        this.tcScDtsId = tcScDtsId;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public BigDecimal getSecusuarioid() {
        return secusuarioid;
    }
    public void setSecusuarioid(BigDecimal secusuarioid) {
        this.secusuarioid = secusuarioid;
    }
    public BigDecimal getNum_recarga() {
        return num_recarga;
    }
    public void setNum_recarga(BigDecimal num_recarga) {
        this.num_recarga = num_recarga;
    }
    public BigDecimal getPin() {
        return pin;
    }
    public void setPin(BigDecimal pin) {
        this.pin = pin;
    }
    public String getDts_fuente() {
        return dts_fuente;
    }
    public void setDts_fuente(String dts_fuente) {
        this.dts_fuente = dts_fuente;
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