package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ConsumoWebservice {
	
	public final static String TABLA = "TC_SC_CONSUMO_WEBSERVICE";
	public final static String C_TCSCCATPAISID = "tcsccatpaisid";
	public final static String C_TCSCLOGSIDRAID = "tcsclogsidraid";
	public final static String C_PETICION = "peticion";
	public final static String C_RESPUESTA = "respuesta";
	public final static String C_CREADO_EL = "creado_el";
	public final static String C_CREADO_POR = "creado_por";
	
	BigDecimal tcsccatpaisid;                  /* Longitud/Precision: NUMBER (22) */
	BigDecimal tcsclogsidraid;                  /* Longitud/Precision: NUMBER (22) */
	String peticion;                  /* Longitud/Precision: CLOB (4000) */
	String respuesta;                  /* Longitud/Precision: CLOB (4000) */
	Timestamp creado_el;                  /* Longitud/Precision: DATE (7) */
	String creado_por;                  /* Longitud/Precision: VARCHAR2 (50) */
	
	public BigDecimal getTcsccatpaisid() {
		return tcsccatpaisid;
	}
	public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
	}
	public BigDecimal getTcsclogsidraid() {
		return tcsclogsidraid;
	}
	public void setTcsclogsidraid(BigDecimal tcsclogsidraid) {
		this.tcsclogsidraid = tcsclogsidraid;
	}
	public String getPeticion() {
		return peticion;
	}
	public void setPeticion(String peticion) {
		this.peticion = peticion;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
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
	
	public void clear() {
		setPeticion("");
		setRespuesta("");
	}
}
