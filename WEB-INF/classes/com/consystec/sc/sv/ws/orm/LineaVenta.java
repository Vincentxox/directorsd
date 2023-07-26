package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class LineaVenta {
	public static final String N_TABLA = "TC_SC_LINEA_VENTA";
	public final static String CAMPO_TCSCLINEAVENTAID = "tcsclineaventaid";
	public final static String CAMPO_TCSCVENTAID = "tcscventaid";
	public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	public final static String CAMPO_COD_DISPOSITIVO = "cod_dispositivo";
	public final static String CAMPO_IZQUIERDA = "izquierda";
	public final static String CAMPO_CENTRO = "centro";
	public final static String CAMPO_DERECHA = "derecha";
	public final static String CAMPO_ALINEACION = "alineacion";
	public final static String CAMPO_ESTILO = "estilo";
	public final static String CAMPO_EXTRA = "extra";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_CREADO_POR = "creado_por";
	
	
	private BigDecimal tcsclineaventaid;
	private BigDecimal tcscventaid;
	private BigDecimal tcsccatpaisid;
	private String cod_dispositivo;
	private String izquierda;
	private String centro;
	private String derecha;
	private String alineacion;
	private String estilo;
	private String extra;
	private Timestamp creado_el;
	private String creado_por;
	public BigDecimal getTcsclineaventaid() {
		return tcsclineaventaid;
	}
	public void setTcsclineaventaid(BigDecimal tcsclineaventaid) {
		this.tcsclineaventaid = tcsclineaventaid;
	}
	public BigDecimal getTcscventaid() {
		return tcscventaid;
	}
	public void setTcscventaid(BigDecimal tcscventaid) {
		this.tcscventaid = tcscventaid;
	}
	public BigDecimal getTcsccatpaisid() {
		return tcsccatpaisid;
	}
	public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
	}
	public String getCod_dispositivo() {
		return cod_dispositivo;
	}
	public void setCod_dispositivo(String cod_dispositivo) {
		this.cod_dispositivo = cod_dispositivo;
	}
	public String getIzquierda() {
		return izquierda;
	}
	public void setIzquierda(String izquierda) {
		this.izquierda = izquierda;
	}
	public String getCentro() {
		return centro;
	}
	public void setCentro(String centro) {
		this.centro = centro;
	}
	public String getDerecha() {
		return derecha;
	}
	public void setDerecha(String derecha) {
		this.derecha = derecha;
	}
	public String getAlineacion() {
		return alineacion;
	}
	public void setAlineacion(String alineacion) {
		this.alineacion = alineacion;
	}
	public String getEstilo() {
		return estilo;
	}
	public void setEstilo(String estilo) {
		this.estilo = estilo;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
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
	
	
}
