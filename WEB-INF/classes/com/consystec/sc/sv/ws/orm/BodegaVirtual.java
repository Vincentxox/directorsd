package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class BodegaVirtual {
    public static final String N_TABLA = "TC_SC_BODEGAVIRTUAL";
    public static final String SEQUENCE = "TC_SC_BODEGAVIRTUAL_SQ.nextval";

    public final static String CAMPO_TC_SC_BODEGA_VIRTUAL_ID = "TCSCBODEGAVIRTUALID";
    public final static String CAMPO_TCSCCATPAISID = "TCSCCATPAISID";
    public final static String CAMPO_NOMBRE = "NOMBRE";
    public final static String CAMPO_ESTADO = "ESTADO";
    public final static String CAMPO_CREADO_EL = "CREADO_EL";
    public final static String CAMPO_CREADO_POR = "CREADO_POR";
    public final static String CAMPO_MODIFICADO_EL = "MODIFICADO_EL";
    public final static String CAMPO_MODIFICADO_POR = "MODIFICADO_POR";
    public final static String CAMPO_IDBODEGA_PADRE = "IDBODEGA_PADRE";
    public final static String CAMPO_NIVEL = "NIVEL";
    public final static String CAMPO_LATITUD = "LATITUD";
    public final static String CAMPO_LONGITUD = "LONGITUD";
    public final static String CAMPO_IDBODEGA_ORIGEN = "IDBODEGA_ORIGEN";
    public final static String CAMPO_TIPO = "TIPO";
    public final static String CAMPO_DIRECCION = "DIRECCION";
    
    private BigDecimal tcscbodegavirtualid;
    private BigDecimal tcsccatpaisid;
    private String nombre;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    private String idbodega_padre;
    private String nivel;
    private String latitud;
    private String logitud;
    private BigDecimal idbodega_origen;
    private String direccion;

    public BigDecimal getTcscbodegavirtualid() {
        return tcscbodegavirtualid;
    }
    public void setTcscbodegavirtualid(BigDecimal tcscbodegavirtualid) {
        this.tcscbodegavirtualid = tcscbodegavirtualid;
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
    public String getIdbodega_padre() {
        return idbodega_padre;
    }
    public void setIdbodega_padre(String idbodega_padre) {
        this.idbodega_padre = idbodega_padre;
    }
    public String getNivel() {
        return nivel;
    }
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
    public String getLatitud() {
        return latitud;
    }
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }
    public String getLogitud() {
        return logitud;
    }
    public void setLogitud(String logitud) {
        this.logitud = logitud;
    }
    public BigDecimal getIdbodega_origen() {
        return idbodega_origen;
    }
    public void setIdbodega_origen(BigDecimal idbodega_origen) {
        this.idbodega_origen = idbodega_origen;
    }
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
    
}