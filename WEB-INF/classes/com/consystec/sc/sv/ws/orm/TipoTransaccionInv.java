package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TipoTransaccionInv {
	public static final String N_TABLA = "TC_SC_TIPOTRANSACCION";
	public static final String SEQUENCE = "TC_SC_TIPOTRANSACCION_SQ.NEXTVAL";
	public final static String CAMPO_TCSCTIPOTRANSACCIONID = "tcsctipotransaccionid";
	public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	public final static String CAMPO_DESCRIPCION = "descripcion";
	public final static String CAMPO_TIPO_MOVIMIENTO = "tipo_movimiento";
	public final static String CAMPO_TIPO_AFECTA = "tipo_afecta";
	public final static String CAMPO_ESTADO = "estado";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";
	public final static String CAMPO_CODIGO_TRANSACCION = "codigo_transaccion";
	
	private BigDecimal tcsctipotransaccionid;
	private BigDecimal tcsccatpaisid;
	private String descripcion;
	private String tipo_movimiento;
	private String tipo_afecta;
	private String estado;
	private String creado_por;
	private Timestamp creado_el;
	private String modificado_por;
	private Timestamp modificado_el;
	private String codigo_transaccion;
	public BigDecimal getTcsctipotransaccionid() {
		return tcsctipotransaccionid;
	}
	public void setTcsctipotransaccionid(BigDecimal tcsctipotransaccionid) {
		this.tcsctipotransaccionid = tcsctipotransaccionid;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getTipo_movimiento() {
		return tipo_movimiento;
	}
	public void setTipo_movimiento(String tipo_movimiento) {
		this.tipo_movimiento = tipo_movimiento;
	}
	public String getTipo_afecta() {
		return tipo_afecta;
	}
	public void setTipo_afecta(String tipo_afecta) {
		this.tipo_afecta = tipo_afecta;
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
	public String getModificado_por() {
		return modificado_por;
	}
	public void setModificado_por(String modificado_por) {
		this.modificado_por = modificado_por;
	}
	public Timestamp getModificado_el() {
		return modificado_el;
	}
	public void setModificado_el(Timestamp modificado_el) {
		this.modificado_el = modificado_el;
	}
	public String getCodigo_transaccion() {
		return codigo_transaccion;
	}
	public void setCodigo_transaccion(String codigo_transaccion) {
		this.codigo_transaccion = codigo_transaccion;
	}
	public BigDecimal getTcsccatpaisid() {
		return tcsccatpaisid;
	}
	public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
	}
	
	
	
}
