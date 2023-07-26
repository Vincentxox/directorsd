package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class GestionVenta {
	public static final String N_TABLA = "TC_SC_GESTION_VENTA";
	public static final String SEQUENCE = "TC_SC_GESTION_VENTA_SQ.nextval";
	
	public final static String CAMPO_TCSCGESTIONVENTAID = "tcscgestionventaid";
	public final static String CAMPO_TCSCVENTADETID = "tcscventadetid";
	public final static String CAMPO_TIPO_GESTION = "tipo_gestion";
	public final static String CAMPO_NO_INTENTO = "no_intento";
	public final static String CAMPO_PRIMERNOMBRE = "primernombre";
	public final static String CAMPO_SEGUNDONOMBRE = "segundonombre";
	public final static String CAMPO_PRIMERAPELLIDO = "primerapellido";
	public final static String CAMPO_SEGUNDOAPELLIDO = "segundoapellido";
	public final static String CAMPO_TIPO_DOCUMENTO = "tipo_documento";
	public final static String CAMPO_NO_DOCUMENTO = "no_documento";
	public final static String CAMPO_ICC = "icc";
	public final static String CAMPO_IMEI = "imei";
	public final static String CAMPO_NUM_TELEFONO = "num_telefono";
	public final static String CAMPO_ESTADO = "estado";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	
	private BigDecimal tcscgestionventaid;
	private BigDecimal tcscventadetid;
	private String tipo_gestion;
	private BigDecimal no_intento;
	private String primernombre;
	private String segundonombre;
	private String primerapellido;
	private String segundoapellido;
	private String tipo_documento;
	private String no_documento;
	private String icc;
	private String imei;
	private BigDecimal num_telefono;
	private String estado;
	private Timestamp creado_el;
	private String creado_por;
	private Timestamp modificado_el;
	private String modificado_por;
	
	
	public BigDecimal getTcscgestionventaid() {
		return tcscgestionventaid;
	}
	public void setTcscgestionventaid(BigDecimal tcscgestionventaid) {
		this.tcscgestionventaid = tcscgestionventaid;
	}
	public BigDecimal getTcscventadetid() {
		return tcscventadetid;
	}
	public void setTcscventadetid(BigDecimal tcscventadetid) {
		this.tcscventadetid = tcscventadetid;
	}
	public String getTipo_gestion() {
		return tipo_gestion;
	}
	public void setTipo_gestion(String tipo_gestion) {
		this.tipo_gestion = tipo_gestion;
	}
	public BigDecimal getNo_intento() {
		return no_intento;
	}
	public void setNo_intento(BigDecimal no_intento) {
		this.no_intento = no_intento;
	}
	public String getPrimernombre() {
		return primernombre;
	}
	public void setPrimernombre(String primernombre) {
		this.primernombre = primernombre;
	}
	public String getSegundonombre() {
		return segundonombre;
	}
	public void setSegundonombre(String segundonombre) {
		this.segundonombre = segundonombre;
	}
	public String getPrimerapellido() {
		return primerapellido;
	}
	public void setPrimerapellido(String primerapellido) {
		this.primerapellido = primerapellido;
	}
	public String getSegundoapellido() {
		return segundoapellido;
	}
	public void setSegundoapellido(String segundoapellido) {
		this.segundoapellido = segundoapellido;
	}
	public String getTipo_documento() {
		return tipo_documento;
	}
	public void setTipo_documento(String tipo_documento) {
		this.tipo_documento = tipo_documento;
	}
	public String getNo_documento() {
		return no_documento;
	}
	public void setNo_documento(String no_documento) {
		this.no_documento = no_documento;
	}
	public String getIcc() {
		return icc;
	}
	public void setIcc(String icc) {
		this.icc = icc;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public BigDecimal getNum_telefono() {
		return num_telefono;
	}
	public void setNum_telefono(BigDecimal num_telefono) {
		this.num_telefono = num_telefono;
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
