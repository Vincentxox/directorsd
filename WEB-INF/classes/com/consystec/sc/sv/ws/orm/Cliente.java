package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Cliente {
    public static final String N_TABLA = "TC_SC_CLIENTE";
    public static final String SEQUENCE = "TC_SC_CLIENTE_SQ.nextval";

    public final static String CAMPO_TC_SC_CLIENTE_ID = "tcscclienteid";
    public final static String CAMPO_TC_SC_CATPAIS_ID = "tcsccatpaisid";
    public final static String CAMPO_NOMBRE = "nombre";
    public final static String CAMPO_APELLIDO = "apellido";
    public final static String CAMPO_NIT = "nit";
    public final static String CAMPO_DOCUMENTO_IDENTIFICACION = "documento_identificacion";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcscclienteid;
    private BigDecimal tcsccatpaisid;
    private String nombre;
    private String apellido;
    private String nit;
    private String documento_identificacion;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    
    public BigDecimal getTcscclienteid() {
        return tcscclienteid;
    }
    public void setTcscclienteid(BigDecimal tcscclienteid) {
        this.tcscclienteid = tcscclienteid;
    }
    public BigDecimal getTcsccatpaisid() {
        return tcsccatpaisid;
    }
    public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
        this.tcsccatpaisid = tcsccatpaisid;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getNit() {
        return nit;
    }
    public void setNit(String nit) {
        this.nit = nit;
    }
    public String getDocumento_identificacion() {
        return documento_identificacion;
    }
    public void setDocumento_identificacion(String documento_identificacion) {
        this.documento_identificacion = documento_identificacion;
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