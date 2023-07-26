package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;

public class EncargadoPDV {
	public static final String N_TABLA = "TC_SC_ENCARGADO_PDV";
	public static final String N_TABLA_ID = "tcscencargadopdvid";
	public static final String SEQUENCE = "TC_SC_ENCARGADO_PDV_SQ.nextval";
	
	public final static String CAMPO_TCSCENCARGADOPDVID = "tcscencargadopdvid";
	public final static String CAMPO_TCSCPUNTOVENTAID = "tcscpuntoventaid";
	public final static String CAMPO_NOMBRES = "nombres";
	public final static String CAMPO_APELLIDOS = "apellidos";
	public final static String CAMPO_TELEFONO = "telefono";
	public final static String CAMPO_ESTADO = "estado";
	public final static String CAMPO_CEDULA = "cedula";
	public final static String CAMPO_TIPO_DOCUMENTO="tipo_documento";
	
	private BigDecimal tcscencargadopdvid;
	private BigDecimal tcscpuntoventaid;
	private String nombres;
	private String apellidos;
	private BigDecimal telefono;
	private String estado;
	private String cedula;
	private String tipoDocumento;
	
	
	
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public BigDecimal getTcscencargadopdvid() {
		return tcscencargadopdvid;
	}
	public void setTcscencargadopdvid(BigDecimal tcscencargadopdvid) {
		this.tcscencargadopdvid = tcscencargadopdvid;
	}
	public BigDecimal getTcscpuntoventaid() {
		return tcscpuntoventaid;
	}
	public void setTcscpuntoventaid(BigDecimal tcscpuntoventaid) {
		this.tcscpuntoventaid = tcscpuntoventaid;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public BigDecimal getTelefono() {
		return telefono;
	}
	public void setTelefono(BigDecimal telefono) {
		this.telefono = telefono;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCedula() {
		return cedula;
	}
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	
	
}
