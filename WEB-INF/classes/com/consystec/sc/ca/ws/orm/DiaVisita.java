package com.consystec.sc.ca.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class DiaVisita {

	public static final String N_TABLA = "TC_SC_DIAVISITA";
	public static final String N_TABLA_ID = "TCSCDIAVISITAID";
	public static final String SEQUENCE = "TC_SC_DIAVISITA.nextval";
	
	public final static String CAMPO_TCSCDIAVISITAID = "tcscdiavisitaid";
	public final static String CAMPO_TCSCPUNTOVENTAID = "tcscpuntoventaid";
	public final static String CAMPO_NOMBRE = "nombre";
	public final static String CAMPO_ESTADO = "estado";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	
	private BigDecimal tcscdiavisitaid;
	private BigDecimal tcscpuntoventaid;
	private String nombre;
	private String estado;
	private Timestamp creado_el;
	private String creado_por;
	private Timestamp modificado_el;
	private String modificado_por;
	public BigDecimal getTcscdiavisitaid() {
		return tcscdiavisitaid;
	}
	public void setTcscdiavisitaid(BigDecimal tcscdiavisitaid) {
		this.tcscdiavisitaid = tcscdiavisitaid;
	}
	public BigDecimal getTcscpuntoventaid() {
		return tcscpuntoventaid;
	}
	public void setTcscpuntoventaid(BigDecimal tcscpuntoventaid) {
		this.tcscpuntoventaid = tcscpuntoventaid;
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
	
	
	
}
