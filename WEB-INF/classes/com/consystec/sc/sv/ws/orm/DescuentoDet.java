package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class DescuentoDet {
    public static final String N_TABLA = "TC_SC_DESCUENTO_DET";
    public static final String SEQUENCE = "TC_SC_DESCUENTO_DET_SQ.nextval";

    public final static String CAMPO_TCSCDESCUENTOSDETID = "tcscdescuentosdetid";
    public final static String CAMPO_TCSCDESCUENTOID = "tcscdescuentoid";
    public final static String CAMPO_TCSCTIPOID = "tcsctipoid";
    public final static String CAMPO_TIPO = "tipo";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcscdescuentosdetid;
    private BigDecimal tcscdescuentoid;
    private BigDecimal tcsctipoid;
    private String tipo;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    
    public BigDecimal getTcscdescuentosdetid() {
        return tcscdescuentosdetid;
    }
    public void setTcscdescuentosdetid(BigDecimal tcscdescuentosdetid) {
        this.tcscdescuentosdetid = tcscdescuentosdetid;
    }
    public BigDecimal getTcscdescuentoid() {
        return tcscdescuentoid;
    }
    public void setTcscdescuentoid(BigDecimal tcscdescuentoid) {
        this.tcscdescuentoid = tcscdescuentoid;
    }
    public BigDecimal getTcsctipoid() {
        return tcsctipoid;
    }
    public void setTcsctipoid(BigDecimal tcsctipoid) {
        this.tcsctipoid = tcsctipoid;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
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