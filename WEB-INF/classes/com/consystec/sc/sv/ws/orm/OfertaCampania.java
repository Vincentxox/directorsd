package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OfertaCampania {
    public static final String N_TABLA = "TC_SC_OFERTA_CAMPANIA";
    public static final String SEQUENCE = "TC_SC_OFERTA_CAMPANIA_SQ.nextval";

    public final static String CAMPO_TCSCOFERTACAMPANIAID = "tcscofertacampaniaid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TIPO = "tipo";
    public final static String CAMPO_NOMBRE = "nombre";
    public final static String CAMPO_DESCRIPCION = "descripcion";
    public final static String CAMPO_CANT_MAX_PROMOCIONALES = "cant_max_promocionales";
    public final static String CAMPO_FECHA_DESDE = "fecha_desde";
    public final static String CAMPO_FECHA_HASTA = "fecha_hasta";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcscofertacampaniaid;
    private BigDecimal tcScCatPaisId;
    private String tipo;
    private String nombre;
    private String descripcion;
    private BigDecimal cant_max_promocionales;
    private Timestamp fecha_desde;
    private Timestamp fecha_hasta;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    
    public BigDecimal getTcscofertacampaniaid() {
        return tcscofertacampaniaid;
    }
    public void setTcscofertacampaniaid(BigDecimal tcscofertacampaniaid) {
        this.tcscofertacampaniaid = tcscofertacampaniaid;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public BigDecimal getCant_max_promocionales() {
        return cant_max_promocionales;
    }
    public void setCant_max_promocionales(BigDecimal cant_max_promocionales) {
        this.cant_max_promocionales = cant_max_promocionales;
    }
    public Timestamp getFecha_desde() {
        return fecha_desde;
    }
    public void setFecha_desde(Timestamp fecha_desde) {
        this.fecha_desde = fecha_desde;
    }
    public Timestamp getFecha_hasta() {
        return fecha_hasta;
    }
    public void setFecha_hasta(Timestamp fecha_hasta) {
        this.fecha_hasta = fecha_hasta;
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