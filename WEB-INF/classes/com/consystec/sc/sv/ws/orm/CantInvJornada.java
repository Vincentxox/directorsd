package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CantInvJornada {
    public static final String N_TABLA = "TC_SC_CANT_INV_JORNADA";
    public static final String SEQUENCE = "TC_SC_CANT_INV_JORNADA_SQ.nextval";

    public final static String CAMPO_TCSC_CANTINV_ID = "tcsc_cantinv_id";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_IDJORNADA_RESPONSABLE = "idjornada_responsable";
    public final static String CAMPO_VENDEDOR_RESPONSABLE = "vendedor_responsable";
    public final static String CAMPO_ARTICULO = "articulo";
    public final static String CAMPO_TIPO_INV = "tipo_inv";
    public final static String CAMPO_CANT_INICIAL = "cant_inicial";
    public final static String CAMPO_CANT_VENDIDA = "cant_vendida";
    public final static String CAMPO_CANT_RESERVADO = "cant_reservado";
    public final static String CAMPO_CANT_PROC_DEV = "cant_proc_dev";
    public final static String CAMPO_CANT_DEVOLUCION = "cant_devolucion";
    public final static String CAMPO_CANT_PROC_SINIESTRO = "cant_proc_siniestro";
    public final static String CAMPO_CANT_SINIESTRO = "cant_siniestro";
    public final static String CAMPO_CANT_DISPONIBLE = "cant_disponible";
    public final static String CAMPO_TIPO_GRUPO_SIDRA = "tipo_grupo_sidra";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcsc_cantinv_id;
    private BigDecimal tcScCatPaisId;
    private BigDecimal idjornada_responsable;
    private BigDecimal vendedor_responsable;
    private BigDecimal articulo;
    private String tipo_inv;
    private BigDecimal cant_inicial;
    private BigDecimal cant_vendida;
    private BigDecimal cant_reservado;
    private BigDecimal cant_proc_dev;
    private BigDecimal cant_devolucion;
    private BigDecimal cant_proc_siniestro;
    private BigDecimal cant_siniestro;
    private BigDecimal cant_disponible;
    private String tipo_grupo_sidra;
    private String creado_por;
    private Timestamp creado_el;
    private Timestamp modificado_el;
    private String modificado_por;

    public BigDecimal getTcsc_cantinv_id() {
        return tcsc_cantinv_id;
    }
    public void setTcsc_cantinv_id(BigDecimal tcsc_cantinv_id) {
        this.tcsc_cantinv_id = tcsc_cantinv_id;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public BigDecimal getIdjornada_responsable() {
        return idjornada_responsable;
    }
    public void setIdjornada_responsable(BigDecimal idjornada_responsable) {
        this.idjornada_responsable = idjornada_responsable;
    }
    public BigDecimal getVendedor_responsable() {
        return vendedor_responsable;
    }
    public void setVendedor_responsable(BigDecimal vendedor_responsable) {
        this.vendedor_responsable = vendedor_responsable;
    }
    public BigDecimal getArticulo() {
        return articulo;
    }
    public void setArticulo(BigDecimal articulo) {
        this.articulo = articulo;
    }
    public String getTipo_inv() {
        return tipo_inv;
    }
    public void setTipo_inv(String tipo_inv) {
        this.tipo_inv = tipo_inv;
    }
    public BigDecimal getCant_inicial() {
        return cant_inicial;
    }
    public void setCant_inicial(BigDecimal cant_inicial) {
        this.cant_inicial = cant_inicial;
    }
    public BigDecimal getCant_vendida() {
        return cant_vendida;
    }
    public void setCant_vendida(BigDecimal cant_vendida) {
        this.cant_vendida = cant_vendida;
    }
    public BigDecimal getCant_reservado() {
        return cant_reservado;
    }
    public void setCant_reservado(BigDecimal cant_reservado) {
        this.cant_reservado = cant_reservado;
    }
    public BigDecimal getCant_proc_dev() {
        return cant_proc_dev;
    }
    public void setCant_proc_dev(BigDecimal cant_proc_dev) {
        this.cant_proc_dev = cant_proc_dev;
    }
    public BigDecimal getCant_devolucion() {
        return cant_devolucion;
    }
    public void setCant_devolucion(BigDecimal cant_devolucion) {
        this.cant_devolucion = cant_devolucion;
    }
    public BigDecimal getCant_proc_siniestro() {
        return cant_proc_siniestro;
    }
    public void setCant_proc_siniestro(BigDecimal cant_proc_siniestro) {
        this.cant_proc_siniestro = cant_proc_siniestro;
    }
    public BigDecimal getCant_siniestro() {
        return cant_siniestro;
    }
    public void setCant_siniestro(BigDecimal cant_siniestro) {
        this.cant_siniestro = cant_siniestro;
    }
    public BigDecimal getCant_disponible() {
        return cant_disponible;
    }
    public void setCant_disponible(BigDecimal cant_disponible) {
        this.cant_disponible = cant_disponible;
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
    public String getTipo_grupo_sidra() {
        return tipo_grupo_sidra;
    }
    public void setTipo_grupo_sidra(String tipo_grupo_sidra) {
        this.tipo_grupo_sidra = tipo_grupo_sidra;
    }
}