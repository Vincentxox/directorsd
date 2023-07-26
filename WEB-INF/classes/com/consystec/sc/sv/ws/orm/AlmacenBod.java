package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class AlmacenBod {
	public static final String N_TABLA = "TC_SC_ALMACEN_BOD";
	public static final String SEQUENCE = "TC_SC_ALMACEN_BOD_SQ.nextval";

	public final static String CAMPO_TCSCALMACENBODID = "TCSCALMACENBODID";
	public final static String CAMPO_TC_SC_DTS_ID = "TCSCDTSID";
	public final static String CAMPO_TC_SC_CATPAIS_ID = "TCSCCATPAISID";
	public final static String CAMPO_TC_BODEGA_SCL_ID = "TCBODEGASCLID";
	public final static String CAMPO_NOMBRE = "NOMBRE";
	public final static String CAMPO_ESTADO = "ESTADO";
	public final static String CAMPO_CREADO_EL = "CREADO_EL";
	public final static String CAMPO_CREADO_POR = "CREADO_POR";
	public final static String CAMPO_MODIFICADO_EL = "MODIFICADO_EL";
	public final static String CAMPO_MODIFICADO_POR = "MODIFICADO_POR";
	public static final String CAMPO_TCSCBODEGAVIRTUALID = "TCSCBODEGAVIRTUALID";

	private BigDecimal tcScAlmacenbodid;
	private BigDecimal tcScDtsId;
	private BigDecimal tcScCatPaisId;
	private BigDecimal tcBodegaSclId;
	private String nombre;
	private String estado;
	private Timestamp creado_el;
	private String creado_por;
	private Timestamp modificado_el;
	private String modificado_por;
	private BigDecimal tcscBodegaVirtualId;

	public BigDecimal getTcScDtsId() {
		return tcScDtsId;
	}

	public void setTcScDtsId(BigDecimal tcScDtsId) {
		this.tcScDtsId = tcScDtsId;
	}

	public BigDecimal getTcScCatPaisId() {
		return tcScCatPaisId;
	}

	public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
		this.tcScCatPaisId = tcScCatPaisId;
	}

	public BigDecimal getTcBodegaSclId() {
		return tcBodegaSclId;
	}

	public void setTcBodegaSclId(BigDecimal tcBodegaSclId) {
		this.tcBodegaSclId = tcBodegaSclId;
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

	public BigDecimal getTcScAlmacenbodid() {
		return tcScAlmacenbodid;
	}

	public void setTcScAlmacenbodid(BigDecimal tcScAlmacenbodid) {
		this.tcScAlmacenbodid = tcScAlmacenbodid;
	}

	public BigDecimal getTcscBodegaVirtualId() {
		return tcscBodegaVirtualId;
	}

	public void setTcscBodegaVirtualId(BigDecimal tcscBodegaVirtualId) {
		this.tcscBodegaVirtualId = tcscBodegaVirtualId;
	}

}
