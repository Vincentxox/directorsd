package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ImgPDV {
	public static final String N_TABLA = "TC_SC_IMG_PDV";
	public static final String SEQUENCE = "TC_SC_IMG_PDV_SQ.nextval";
	
	public final static String CAMPO_TCSCIMGPDVID = "tcscimgpdvid";
	public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	public final static String CAMPO_TCSCPUNTOVENTAID = "tcscpuntoventaid";
	public final static String CAMPO_TCSCVISITAID = "tcscvisitaid";
	public final static String CAMPO_ARCHIVO = "archivo";
	public final static String CAMPO_NOMBRE_ARCHIVO = "nombre_archivo";
	public final static String CAMPO_EXTENSION_ARCHIVO = "extension_archivo";
	public final static String CAMPO_OBSERVACIONES = "observaciones";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";

    private BigDecimal tcscimgpdvid;
    private BigDecimal tcScCatPaisId;
    private BigDecimal tcscpuntoventaid;
    private BigDecimal tcscvisitaid;
    private byte[] archivo;
    private String nombre_archivo;
    private String extension_archivo;
    private String observaciones;
    private String creado_por;
    private Timestamp creado_el;
    private String modificado_por;
    private Timestamp modificado_el;
    
    public BigDecimal getTcscimgpdvid() {
        return tcscimgpdvid;
    }
    public void setTcscimgpdvid(BigDecimal tcscimgpdvid) {
        this.tcscimgpdvid = tcscimgpdvid;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public BigDecimal getTcscpuntoventaid() {
        return tcscpuntoventaid;
    }
    public void setTcscpuntoventaid(BigDecimal tcscpuntoventaid) {
        this.tcscpuntoventaid = tcscpuntoventaid;
    }
    public BigDecimal getTcscvisitaid() {
        return tcscvisitaid;
    }
    public void setTcscvisitaid(BigDecimal tcscvisitaid) {
        this.tcscvisitaid = tcscvisitaid;
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
    public String getExtension_archivo() {
        return extension_archivo;
    }
    public void setExtension_archivo(String extension_archivo) {
        this.extension_archivo = extension_archivo;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
    public String getModificado_por() {
        return modificado_por;
    }
    public void setModificado_por(String modificado_por) {
        this.modificado_por = modificado_por;
    }
    public Timestamp getModificado_el() {
        return modificado_el;
    }
    public void setModificado_el(Timestamp modificado_el) {
        this.modificado_el = modificado_el;
    }
}