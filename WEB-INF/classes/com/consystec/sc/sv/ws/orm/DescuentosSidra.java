package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class DescuentosSidra {
    public static final String N_TABLA = "TC_SC_DESCUENTO_SIDRA";
    public static final String SEQUENCE = "TC_SC_DESCUENTO_SIDRA_SQ.nextval";

    public final static String CAMPO_TCSCDESCUENTOSIDRAID = "tcscdescuentosidraid";
    public final static String CAMPO_TCSCVENTAID = "tcscventaid";
    public final static String CAMPO_TCSCVENTADETID = "tcscventadetid";
    public final static String CAMPO_TCSCOFERTACAMPANIAID = "tcscofertacampaniaid";
    public final static String CAMPO_VALOR = "valor";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";
    public final static String CAMPO_ID_DESCUENTO="id_descuento";

    private BigDecimal tcscdescuentosidraid;
    private BigDecimal tcscventaid;
    private BigDecimal tcscventadetid;
    private BigDecimal tcscofertacampaniaid;
    private BigDecimal valor;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    private String tipoDescuento;
    private BigDecimal cantidad;
    private BigDecimal id_descuento;
    private String idCondicion;
    
    public BigDecimal getTcscdescuentosidraid() {
        return tcscdescuentosidraid;
    }
    public void setTcscdescuentosidraid(BigDecimal tcscdescuentosidraid) {
        this.tcscdescuentosidraid = tcscdescuentosidraid;
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
    public BigDecimal getTcscofertacampaniaid() {
        return tcscofertacampaniaid;
    }
    public void setTcscofertacampaniaid(BigDecimal tcscofertacampaniaid) {
        this.tcscofertacampaniaid = tcscofertacampaniaid;
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
    public String getTipoDescuento() {
        return tipoDescuento;
    }
    public void setTipoDescuento(String tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }
    public BigDecimal getCantidad() {
        return cantidad;
    }
    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }
	public BigDecimal getId_descuento() {
		return id_descuento;
	}
	public void setId_descuento(BigDecimal id_descuento) {
		this.id_descuento = id_descuento;
	}
	public String getIdCondicion() {
		return idCondicion;
	}
	public void setIdCondicion(String idCondicion) {
		this.idCondicion = idCondicion;
	}
	
    
}