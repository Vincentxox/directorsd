package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Panel {

	public static final String N_TABLA = "TC_SC_PANEL";
	public static final String N_TABLA_ID = "TCSCPANELID";
	public static final String SEQUENCE = "TC_SC_PANEL_SQ.nextval";
	
	public final static String CAMPO_TCSCPANELID = "tcscpanelid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	public final static String CAMPO_TCSCDTSID = "tcscdtsid";
	public final static String CAMPO_TCSCBODEGAVIRTUALID= "tcscbodegavirtualid";
	public final static String CAMPO_NOMBRE = "nombre";
	public final static String CAMPO_RECARGAS = "recargas";
	public final static String CAMPO_ESTADO = "estado";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	
	private BigDecimal tcscpanelid;
    private BigDecimal tcsccatpaisid;
	private BigDecimal tcscdtsid;
	private BigDecimal tcscbodegavirtualid;
	private String nombre;
	private String recargas;
	private String estado;
	private Timestamp creado_el;
	private String creado_por;
	private Timestamp modificado_el;
	private String modificado_por;
	
	public BigDecimal getTcscpanelid() {
		return tcscpanelid;
	}
	public void setTcscpanelid(BigDecimal tcscpanelid) {
		this.tcscpanelid = tcscpanelid;
	}
	public BigDecimal getTcsccatpaisid() {
        return tcsccatpaisid;
    }
    public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
        this.tcsccatpaisid = tcsccatpaisid;
    }
    public BigDecimal getTcscdtsid() {
		return tcscdtsid;
	}
	public void setTcscdtsid(BigDecimal tcscdtsid) {
		this.tcscdtsid = tcscdtsid;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getRecargas() {
		return recargas;
	}
	public void setRecargas(String recargas) {
		this.recargas = recargas;
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
	public BigDecimal getTcscbodegavirtualid() {
		return tcscbodegavirtualid;
	}
	public void setTcscbodegavirtualid(BigDecimal tcscbodegavirtualid) {
		this.tcscbodegavirtualid = tcscbodegavirtualid;
	}
}