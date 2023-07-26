package com.consystec.sc.ca.ws.orm;

import java.math.BigDecimal;

public class MensajeVW {
	public static final String N_TABLA = "TC_SC_MSJ_DIRECTOR_VW";
	public final static String CAMPO_TCSCMSJDIRECTORID = "tcscmsjdirectorid";
	public final static String CAMPO_CODRESULTADO = "codresultado";
	public final static String CAMPO_MOSTRAR = "mostrar";
	public final static String CAMPO_DESCRIPCION = "descripcion";
	public final static String CAMPO_ESTADO = "estado";
	public final static String CAMPO_TIPO_EXCEPCION = "tipo_excepcion";
	
	private BigDecimal tcscmsjdirectorid;
	private BigDecimal codresultado;
	private BigDecimal mostrar;
	private String descripcion;
	private String estado;
	private String tipo_excepcion;
	
	public BigDecimal getTcscmsjdirectorid() {
		return tcscmsjdirectorid;
	}
	public void setTcscmsjdirectorid(BigDecimal tcscmsjdirectorid) {
		this.tcscmsjdirectorid = tcscmsjdirectorid;
	}
	public BigDecimal getCodresultado() {
		return codresultado;
	}
	public void setCodresultado(BigDecimal codresultado) {
		this.codresultado = codresultado;
	}
	public BigDecimal getMostrar() {
		return mostrar;
	}
	public void setMostrar(BigDecimal mostrar) {
		this.mostrar = mostrar;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getTipo_excepcion() {
		return tipo_excepcion;
	}
	public void setTipo_excepcion(String tipo_excepcion) {
		this.tipo_excepcion = tipo_excepcion;
	}

	
	

}
