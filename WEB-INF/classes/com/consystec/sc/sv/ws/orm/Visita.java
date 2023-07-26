package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Visita {
	public static final String N_TABLA = "TC_SC_VISITA";
	public static final String SEQUENCE = "TC_SC_VISITA_SQ.nextval";
	
	public final static String CAMPO_TCSCVISITAID = "tcscvisitaid";
	public final static String CAMPO_TCSCPUNTOVENTAID = "tcscpuntoventaid";
	public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	public final static String CAMPO_FECHA = "fecha";
	public final static String CAMPO_VENDEDOR = "vendedor";
	public final static String CAMPO_LATITUD = "latitud";
	public final static String CAMPO_LONGITUD = "longitud";
	public final static String CAMPO_TCSCVENTAID = "tcscventaid";
	public final static String CAMPO_GESTION = "gestion";
	public final static String CAMPO_CAUSA = "causa";
	public final static String CAMPO_OBSERVACIONES = "observaciones";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	public final static String CAMPO_TCSCJORNADAVENDID = "tcscjornadavenid";

	private BigDecimal tcscvisitaid;
	private BigDecimal tcsccatpaisid;
	private BigDecimal tcscpuntoventaid;
	private Timestamp fecha;
	private BigDecimal vendedor;
	private String latitud;
	private String longitud;
	private BigDecimal tcscventaid;
	private String gestion;
	private String causa;
	private String observaciones;
	private Timestamp creado_el;
	private String creado_por;
	private Timestamp modificado_el;
	private String modificado_por;
	private BigDecimal tcscjornadavendid;

    public BigDecimal getTcscvisitaid() {
        return tcscvisitaid;
    }
    public void setTcscvisitaid(BigDecimal tcscvisitaid) {
        this.tcscvisitaid = tcscvisitaid;
    }
    public BigDecimal getTcscpuntoventaid() {
        return tcscpuntoventaid;
    }
    public void setTcscpuntoventaid(BigDecimal tcscpuntoventaid) {
        this.tcscpuntoventaid = tcscpuntoventaid;
    }
    public Timestamp getFecha() {
        return fecha;
    }
    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
    public BigDecimal getVendedor() {
        return vendedor;
    }
    public void setVendedor(BigDecimal vendedor) {
        this.vendedor = vendedor;
    }
    public String getLatitud() {
        return latitud;
    }
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }
    public String getLongitud() {
        return longitud;
    }
    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
    public BigDecimal getTcscventaid() {
        return tcscventaid;
    }
    public void setTcscventaid(BigDecimal tcscventaid) {
        this.tcscventaid = tcscventaid;
    }
    public String getGestion() {
        return gestion;
    }
    public void setGestion(String gestion) {
        this.gestion = gestion;
    }
    public String getCausa() {
        return causa;
    }
    public void setCausa(String causa) {
        this.causa = causa;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
    public BigDecimal getTcscjornadavendid() {
        return tcscjornadavendid;
    }
    public void setTcscjornadavendid(BigDecimal tcscjornadavendid) {
        this.tcscjornadavendid = tcscjornadavendid;
    }
	public BigDecimal getTcsccatpaisid() {
		return tcsccatpaisid;
	}
	public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
	}
    
}