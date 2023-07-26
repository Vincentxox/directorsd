package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class BuzonSidra {
	
	public static final String N_TABLA = "TC_SC_BUZONSIDRA";
	public static final String SEQUENCE = "TC_SC_BUZONSIDRA_SQ.nextval";

	public final static String CAMPO_TCSCBUZONID = "tcscbuzonid";
	public final static String CAMPO_TCSCCATPAIS_ID = "tcsccatpaisid"; 
	public final static String CAMPO_NOMBRE = "nombre";
	public final static String CAMPO_TCSCDTSID = "tcscdtsid";
	public final static String CAMPO_TCSCBODEGAVIRTUALID = "tcscbodegavirtualid";
	public final static String CAMPO_NIVEL = "nivel";
	public final static String CAMPO_TIPO_WORKFLOW = "tipo_workflow";
	public final static String CAMPO_ESTADO = "estado";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	
	private BigDecimal tcscbuzonid;
	private BigDecimal tcsccatpaisid;
	private String nombre;
	private BigDecimal tcscdtsid;
	private BigDecimal tcscbodegavirtualid;
	private BigDecimal nivel;
	private String tipo_workflow;
	private String estado;
	private String creado_por;
	private Timestamp creado_el;
	private Timestamp modificado_el;
	private String modificado_por;
	
	
	public BigDecimal getTcsccatpaisid() {
		return tcsccatpaisid;
	}
	public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
	}
	public BigDecimal getTcscbuzonid() {
		return tcscbuzonid;
	}
	public void setTcscbuzonid(BigDecimal tcscbuzonid) {
		this.tcscbuzonid = tcscbuzonid;
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
	public BigDecimal getTcscdtsid() {
		return tcscdtsid;
	}
	public void setTcscdtsid(BigDecimal tcscdtsid) {
		this.tcscdtsid = tcscdtsid;
	}
	public String getTipo_workflow() {
		return tipo_workflow;
	}
	public void setTipo_workflow(String tipo_workflow) {
		this.tipo_workflow = tipo_workflow;
	}
	public BigDecimal getNivel() {
		return nivel;
	}
	public void setNivel(BigDecimal nivel) {
		this.nivel = nivel;
	}
	public BigDecimal getTcscbodegavirtualid() {
		return tcscbodegavirtualid;
	}
	public void setTcscbodegavirtualid(BigDecimal tcscbodegavirtualid) {
		this.tcscbodegavirtualid = tcscbodegavirtualid;
	}
	
	
}
