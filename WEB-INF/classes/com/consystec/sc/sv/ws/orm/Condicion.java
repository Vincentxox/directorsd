package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Condicion {
	public static final String N_TABLA = "TC_SC_CONDICION";
    public static final String SEQUENCE = "TC_SC_CONDICION_SQ.nextval";

    public final static String CAMPO_TCSCCONDICIONID = "tcsccondicionid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TCSCOFERTACAMPANIAID = "tcscofertacampaniaid";
    public final static String CAMPO_NOMBRE = "nombre";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";
    public final static String CAMPO_TIPO_GESTION = "tipo_gestion";
    public final static String CAMPO_TIPO_CONDICION = "tipo_condicion";
    public final static String CAMPO_TIPO_OFERTACAMPANIA = "tipo_ofertacampania";

    private BigDecimal tcsccondicionid;
    private BigDecimal tcScCatPaisId;
    private BigDecimal tcscofertacampaniaid;
    private String nombre;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    private String tipo_gestion;
    private String tipo_condicion;
    private String tipo_ofertacampania;

    public BigDecimal getTcsccondicionid() {
        return tcsccondicionid;
    }
    public void setTcsccondicionid(BigDecimal tcsccondicionid) {
        this.tcsccondicionid = tcsccondicionid;
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
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
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
    public String getTipo_gestion() {
        return tipo_gestion;
    }
    public void setTipo_gestion(String tipo_gestion) {
        this.tipo_gestion = tipo_gestion;
    }
    public String getTipo_condicion() {
        return tipo_condicion;
    }
    public void setTipo_condicion(String tipo_condicion) {
        this.tipo_condicion = tipo_condicion;
    }
    public String getTipo_ofertacampania() {
        return tipo_ofertacampania;
    }
    public void setTipo_ofertacampania(String tipo_ofertacampania) {
        this.tipo_ofertacampania = tipo_ofertacampania;
    }	
}