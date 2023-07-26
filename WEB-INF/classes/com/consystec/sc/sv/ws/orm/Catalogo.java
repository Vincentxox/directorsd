package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Catalogo {
    public static final String N_TABLA = "TC_SC_CONFIGURACION";
    public static final String SEQUENCE = "TC_SC_CONFIGURACION_SQ.nextval";

    public final static String CAMPO_TC_SC_CATALOGO_ID = "tcscconfiguracionid";
    public final static String CAMPO_TC_SC_CATPAIS_ID = "tcsccatpaisid";
    public final static String CAMPO_GRUPO = "grupo";
    public final static String CAMPO_NOMBRE = "nombre";
    public final static String CAMPO_VALOR = "valor";
    public final static String CAMPO_DESCRIPCION = "descripcion";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";
    public final static String CAMPO_PARAM_INTERNO = "param_interno";
    public final static String CAMPO_TABLA = "tabla";
    public final static String CAMPO_CAMPO_TABLA = "campo_tabla";

    private BigDecimal tcscconfiguracionid;
    private BigDecimal tcsccatpaisid;
    private String grupo;
    private String nombre;
    private String valor;
    private String descripcion;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    private String param_interno;
    private String tabla;
    private String campo_tabla;
    
    public BigDecimal getTcscconfiguracionid() {
        return tcscconfiguracionid;
    }
    public void setTcscconfiguracionid(BigDecimal tcscconfiguracionid) {
        this.tcscconfiguracionid = tcscconfiguracionid;
    }
    public BigDecimal getTcsccatpaisid() {
        return tcsccatpaisid;
    }
    public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
        this.tcsccatpaisid = tcsccatpaisid;
    }
    public String getGrupo() {
        return grupo;
    }
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getValor() {
        return valor;
    }
    public void setValor(String valor) {
        this.valor = valor;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
    public String getParam_interno() {
        return param_interno;
    }
    public void setParam_interno(String param_interno) {
        this.param_interno = param_interno;
    }
    public String getTabla() {
        return tabla;
    }
    public void setTabla(String tabla) {
        this.tabla = tabla;
    }
    public String getCampo_tabla() {
        return campo_tabla;
    }
    public void setCampo_tabla(String campo_tabla) {
        this.campo_tabla = campo_tabla;
    }
}