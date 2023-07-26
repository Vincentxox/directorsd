package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PagoImpuesto {
	public static final String N_TABLA = "TC_SC_PAGO_IMPUESTO";
	public static final String SEQUENCE = "TC_SC_PAGO_IMPUESTO_SQ.nextval";

	public final static String CAMPO_TCSCPAGOIMPUESTOID = "tcscpagoimpuestoid";
	public final static String CAMPO_TCSCVENTAID = "tcscventaid";
	public final static String CAMPO_TCSCVENTADETID = "tcscventadetid";
	public final static String CAMPO_IMPUESTO = "impuesto";
	public final static String CAMPO_VALOR = "valor";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	
	private BigDecimal tcscpagoimpuestoid;
	private BigDecimal tcscventaid;
	private BigDecimal tcscventadetid;
	private String impuesto;
	private BigDecimal valor;
	private Timestamp creado_el;
	private String creado_por;
	private Timestamp modificado_el;
	private String modificado_por;
	
	public BigDecimal getTcscpagoimpuestoid() {
		return tcscpagoimpuestoid;
	}
	public void setTcscpagoimpuestoid(BigDecimal tcscpagoimpuestoid) {
		this.tcscpagoimpuestoid = tcscpagoimpuestoid;
	}
	public BigDecimal getTcscventaid() {
		return tcscventaid;
	}
	public void setTcscventaid(BigDecimal tcscventaid) {
		this.tcscventaid = tcscventaid;
	}
	public BigDecimal getTcscventadetid() {
		return tcscventadetid;
	}
	public void setTcscventadetid(BigDecimal tcscventadetid) {
		this.tcscventadetid = tcscventadetid;
	}
	public String getImpuesto() {
		return impuesto;
	}
	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
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
