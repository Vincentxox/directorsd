package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class DetallePago {
	public static final String N_TABLA = "TC_SC_DET_PAGO";
	public static final String SEQUENCE = "TC_SC_DET_PAGO_SQ_SQ.nextval";

	public final static String CAMPO_TCSCDETPAGOID = "tcscdetpagoid";
	public final static String CAMPO_TCSCVENTAID = "tcscventaid";
	public final static String CAMPO_FORMAPAGO = "formapago";
	public final static String CAMPO_MONTO = "monto";
	public final static String CAMPO_VOUCHER = "voucher";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	
	private BigDecimal tcscdetpagoid;
	private BigDecimal tcscventaid;
	private String formapago;
	private BigDecimal monto;
	private BigDecimal voucher;
	private Timestamp creado_el;
	private String creado_por;
	private Timestamp modificado_el;
	private String modificado_por;
	
	public BigDecimal getTcscdetpagoid() {
		return tcscdetpagoid;
	}
	public void setTcscdetpagoid(BigDecimal tcscdetpagoid) {
		this.tcscdetpagoid = tcscdetpagoid;
	}
	public BigDecimal getTcscventaid() {
		return tcscventaid;
	}
	public void setTcscventaid(BigDecimal tcscventaid) {
		this.tcscventaid = tcscventaid;
	}
	public String getFormapago() {
		return formapago;
	}
	public void setFormapago(String formapago) {
		this.formapago = formapago;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public BigDecimal getVoucher() {
		return voucher;
	}
	public void setvoucher(BigDecimal voucher) {
		this.voucher = voucher;
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
