package com.consystec.sc.ca.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Pais {

	public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	public final static String CAMPO_AREA = "area";
	public final static String CAMPO_NOMBRE = "nombre";
	public final static String CAMPO_NOMBRE_APP = "nombre_app";
	public final static String CAMPO_LONG_NUMERO = "long_numero";
	public final static String CAMPO_LONG_IDENTIFICACION = "long_identificacion";
	public final static String CAMPO_ESTADO = "estado";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	
	private BigDecimal tcsccatpaisid;
	private String area;
	private String nombre;
	private String nombre_app;
	private BigDecimal long_numero;
	private BigDecimal long_identificacion;
	private String estado;
	private Timestamp creado_el;
	private String creado_por;
	private Timestamp modificado_el;
	private String modificado_por;
	public BigDecimal getTcsccatpaisid() {
		return tcsccatpaisid;
	}
	public String getArea() {
		return area;
	}
	public String getNombre() {
		return nombre;
	}
	public String getNombre_app() {
		return nombre_app;
	}
	public BigDecimal getLong_numero() {
		return long_numero;
	}
	public BigDecimal getLong_identificacion() {
		return long_identificacion;
	}
	public String getEstado() {
		return estado;
	}
	public Timestamp getCreado_el() {
		return creado_el;
	}
	public String getCreado_por() {
		return creado_por;
	}
	public Timestamp getModificado_el() {
		return modificado_el;
	}
	public String getModificado_por() {
		return modificado_por;
	}
	public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setNombre_app(String nombre_app) {
		this.nombre_app = nombre_app;
	}
	public void setLong_numero(BigDecimal long_numero) {
		this.long_numero = long_numero;
	}
	public void setLong_identificacion(BigDecimal long_identificacion) {
		this.long_identificacion = long_identificacion;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public void setCreado_el(Timestamp creado_el) {
		this.creado_el = creado_el;
	}
	public void setCreado_por(String creado_por) {
		this.creado_por = creado_por;
	}
	public void setModificado_el(Timestamp modificado_el) {
		this.modificado_el = modificado_el;
	}
	public void setModificado_por(String modificado_por) {
		this.modificado_por = modificado_por;
	}
	
	
}
