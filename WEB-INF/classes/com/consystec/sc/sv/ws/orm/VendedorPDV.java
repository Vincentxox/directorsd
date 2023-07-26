package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class VendedorPDV {
	public static final String N_TABLA = "TC_SC_VEND_PANELPDV";
	public static final String N_TABLA_ID = "TCSCVENDPANELPDVID";
	public static final String SEQUENCE = "TC_SC_VEND_PANELPDV_SQ.nextval";
	
	public final static String CAMPO_TCSCVENDPANELPDVID = "tcscvendpanelpdvid";
	public final static String CAMPO_IDTIPO = "idtipo";
	public final static String CAMPO_TIPO = "tipo";
	public final static String CAMPO_VENDEDOR = "vendedor";
	public final static String CAMPO_NUM_RECARGA = "num_recarga";
	public final static String CAMPO_PIN = "pin";
	public final static String CAMPO_DTS_FUENTE = "dts_fuente";
	public final static String CAMPO_RESPONSABLE = "responsable";
	public final static String CAMPO_ESTADO = "estado";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	
	private BigDecimal tcscvendpanelpdvid;
	private BigDecimal idtipo;
	private String tipo;
	private BigDecimal vendedor;
	private BigDecimal num_recarga;
	private BigDecimal pin;
	private String dts_fuente;
	private BigDecimal responsable;
	private String estado;
	private Timestamp creado_el;
	private String creado_por;
	private Timestamp modificado_el;
	private String modificado_por;
	private String tcsccatpaisid;
	
    public String getTcsccatpaisid() {
		return tcsccatpaisid;
	}
	public void setTcsccatpaisid(String tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
	}
	public BigDecimal getTcscvendpanelpdvid() {
        return tcscvendpanelpdvid;
    }
    public void setTcscvendpanelpdvid(BigDecimal tcscvendpanelpdvid) {
        this.tcscvendpanelpdvid = tcscvendpanelpdvid;
    }
    public BigDecimal getIdtipo() {
        return idtipo;
    }
    public void setIdtipo(BigDecimal idtipo) {
        this.idtipo = idtipo;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public BigDecimal getVendedor() {
        return vendedor;
    }
    public void setVendedor(BigDecimal vendedor) {
        this.vendedor = vendedor;
    }
    public BigDecimal getNum_recarga() {
        return num_recarga;
    }
    public void setNum_recarga(BigDecimal num_recarga) {
        this.num_recarga = num_recarga;
    }
    public BigDecimal getPin() {
        return pin;
    }
    public void setPin(BigDecimal pin) {
        this.pin = pin;
    }
    public String getDts_fuente() {
        return dts_fuente;
    }
    public void setDts_fuente(String dts_fuente) {
        this.dts_fuente = dts_fuente;
    }
    public BigDecimal getResponsable() {
        return responsable;
    }
    public void setResponsable(BigDecimal responsable) {
        this.responsable = responsable;
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
}