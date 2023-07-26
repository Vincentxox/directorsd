package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class AnulacionVenta {
	public static final String N_TABLA = "TC_SC_ANULACION";
	public static final String SEQUENCE = "TC_SC_ANULACION_SQ.nextval";
	
	public final static String CAMPO_TCSCANULACIONID = "tcscanulacionid";
	public final static String CAMPO_TCSCVENTAID = "tcscventaid";
	public final static String CAMPO_TCSCJORNADAVENID = "tcscjornadavenid";
	public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	public final static String CAMPO_VENDEDOR = "vendedor";
	public final static String CAMPO_FECHA_ANULACION = "fecha_anulacion";
	public final static String CAMPO_RAZON_ANULACION = "razon_anulacion";
	public final static String CAMPO_OBSERVACIONES = "observaciones";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_CREADO_EL = "creado_el";
	
	private BigDecimal tcscanulacionid;
	private BigDecimal tcscventaid;
	private BigDecimal tcscjornadavenid;
	private BigDecimal tcsccatpaisid;
	private BigDecimal vendedor;
	private Timestamp fecha_anulacion;
	private String razon_anulacion;
	private String observaciones;
	private String creado_por;
	private Timestamp creado_el;
	
	public BigDecimal getTcscanulacionid() {
		return tcscanulacionid;
	}
	public void setTcscanulacionid(BigDecimal tcscanulacionid) {
		this.tcscanulacionid = tcscanulacionid;
	}
	public BigDecimal getTcscventaid() {
		return tcscventaid;
	}
	public void setTcscventaid(BigDecimal tcscventaid) {
		this.tcscventaid = tcscventaid;
	}
	public BigDecimal getTcscjornadavenid() {
		return tcscjornadavenid;
	}
	public void setTcscjornadavenid(BigDecimal tcscjornadavenid) {
		this.tcscjornadavenid = tcscjornadavenid;
	}
	public BigDecimal getTcsccatpaisid() {
		return tcsccatpaisid;
	}
	public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
	}
	public BigDecimal getVendedor() {
		return vendedor;
	}
	public void setVendedor(BigDecimal vendedor) {
		this.vendedor = vendedor;
	}
	public Timestamp getFecha_anulacion() {
		return fecha_anulacion;
	}
	public void setFecha_anulacion(Timestamp fecha_anulacion) {
		this.fecha_anulacion = fecha_anulacion;
	}
	public String getRazon_anulacion() {
		return razon_anulacion;
	}
	public void setRazon_anulacion(String razon_anulacion) {
		this.razon_anulacion = razon_anulacion;
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
}