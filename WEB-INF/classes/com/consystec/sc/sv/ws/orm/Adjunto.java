package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Adjunto {
	public static final String N_TABLA = "TC_SC_ADJUNTO";
	public static final String SEQUENCE = "TC_SC_ADJUNTO_SQ.nextval";
	
	public final static String CAMPO_TCSCADJUNTOID = "tcscadjuntoid";
	public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	public final static String CAMPO_TCSCGESTIONID = "tcscgestionid";
	public final static String CAMPO_GESTION = "gestion";
	public final static String CAMPO_ARCHIVO = "archivo";
	public final static String CAMPO_NOMBRE_ARCHIVO = "nombre_archivo";
	public final static String CAMPO_TIPO_ARCHIVO = "tipo_archivo";
	public final static String CAMPO_EXTENSION = "extension";
	public final static String CAMPO_ESTADO = "estado";
	public final static String CAMPO_TIPO_DOCUMENTO = "tipo_documento";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";

	private BigDecimal tcscadjuntoid;
	private BigDecimal tcsccatpaisid;
	private BigDecimal tcscgestionid;
	private String gestion;
	private byte[] archivo;
	private String nombre_archivo;
	private String tipo_archivo;
	private String extension;
	private String estado;
	private String tipo_documento;
	private Timestamp creado_el;
	private String creado_por;
	private Timestamp modificado_el;
	private String modificado_por;

    public BigDecimal getTcscadjuntoid() {
        return tcscadjuntoid;
    }
    public void setTcscadjuntoid(BigDecimal tcscadjuntoid) {
        this.tcscadjuntoid = tcscadjuntoid;
    }
    public BigDecimal getTcsccatpaisid() {
        return tcsccatpaisid;
    }
    public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
        this.tcsccatpaisid = tcsccatpaisid;
    }
    public BigDecimal getTcscgestionid() {
        return tcscgestionid;
    }
    public void setTcscgestionid(BigDecimal tcscgestionid) {
        this.tcscgestionid = tcscgestionid;
    }
    public String getGestion() {
        return gestion;
    }
    public void setGestion(String gestion) {
        this.gestion = gestion;
    }
    public byte[] getArchivo() {
        return archivo;
    }
    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }
    public String getNombre_archivo() {
        return nombre_archivo;
    }
    public void setNombre_archivo(String nombre_archivo) {
        this.nombre_archivo = nombre_archivo;
    }
    public String getTipo_archivo() {
        return tipo_archivo;
    }
    public void setTipo_archivo(String tipo_archivo) {
        this.tipo_archivo = tipo_archivo;
    }
    public String getExtension() {
        return extension;
    }
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getTipo_documento() {
        return tipo_documento;
    }
    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
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