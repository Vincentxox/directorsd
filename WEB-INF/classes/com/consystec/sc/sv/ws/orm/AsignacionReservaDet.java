package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class AsignacionReservaDet {
    public static final String N_TABLA = "TC_SC_ASIGNACION_DET";
    public static final String N_TABLA_ID = "TCSCASIGNACIONDETID";
    public static final String SEQUENCE = "TC_SC_ASIGNACION_DET_SQ.nextval";

    public final static String CAMPO_TCSCASIGNACIONRESERVADETID = "tcscasignaciondetid";
    public final static String CAMPO_TCSCASIGNACIONID = "tcscasignacionreservaid";
    public final static String CAMPO_ARTICULO = "articulo";
    public final static String CAMPO_DESCRIPCION = "descripcion";
    public final static String CAMPO_SERIE = "serie";
    public final static String CAMPO_SERIE_FINAL = "serie_final";
    public final static String CAMPO_SERIE_ASOCIADA = "serie_asociada";
    public final static String CAMPO_CANTIDAD = "cantidad";
    public final static String CAMPO_TIPO_INV = "tipo_inv";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_OBSEVACIONES = "observaciones";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcscasignaciondetid;
    private BigDecimal tcscasignacionid;
    private BigDecimal articulo;
    private String descripcion;
    private String serie;
    private String serieAsociada;
    private BigDecimal cantidad;
    private String observaciones;
    private String tipo_inv;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    private String serie_final;
    
    public BigDecimal getTcscasignaciondetid() {
        return tcscasignaciondetid;
    }
    public void setTcscasignaciondetid(BigDecimal tcscasignaciondetid) {
        this.tcscasignaciondetid = tcscasignaciondetid;
    }
    public BigDecimal getTcscasignacionid() {
        return tcscasignacionid;
    }
    public void setTcscasignacionid(BigDecimal tcscasignacionid) {
        this.tcscasignacionid = tcscasignacionid;
    }
    public BigDecimal getArticulo() {
        return articulo;
    }
    public void setArticulo(BigDecimal articulo) {
        this.articulo = articulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getSerie() {
        return serie;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }
    public String getSerieAsociada() {
        return serieAsociada;
    }
    public void setSerieAsociada(String serieAsociada) {
        this.serieAsociada = serieAsociada;
    }
    public BigDecimal getCantidad() {
        return cantidad;
    }
    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public String getTipo_inv() {
        return tipo_inv;
    }
    public void setTipo_inv(String tipo_inv) {
        this.tipo_inv = tipo_inv;
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
    public String getSerie_final() {
        return serie_final;
    }
    public void setSerie_final(String serie_final) {
        this.serie_final = serie_final;
    }
}