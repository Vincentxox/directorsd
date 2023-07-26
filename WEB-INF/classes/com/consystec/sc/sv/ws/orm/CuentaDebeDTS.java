package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CuentaDebeDTS {
    public static final String N_TABLA = "TC_SC_CTA_DEBE_DTS";
    public static final String SEQUENCE = "TC_SC_CTA_DEBE_DTS_SQ.nextval";

    public final static String CAMPO_TCSCCTAHABERDTSID = "tcscctahaberdtsid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TCSCDTSID = "tcscdtsid";
    public final static String CAMPO_ID_RUTAPANEL = "id_rutapanel";
    public final static String CAMPO_TIPO_ID = "tipo_id";
    public final static String CAMPO_TCSCVENTAID = "tcscventaid";
    public final static String CAMPO_FOLIO_SCL = "folio_scl";
    public final static String CAMPO_VENDEDOR = "vendedor";
    public final static String CAMPO_FECHA = "fecha";
    public final static String CAMPO_MONTO_VENTA = "monto_venta";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_MONEDA = "moneda";
    public final static String CAMPO_TIPO_CLIENTE = "tipo_cliente";

    private BigDecimal tcscctahaberdtsid;
    private BigDecimal tcScCatPaisId;
    private BigDecimal tcscdtsid;
    private BigDecimal id_rutapanel;
    private String tipo_id;
    private BigDecimal tcscventaid;
    private String folio_scl;
    private BigDecimal vendedor;
    private Timestamp fecha;
    private BigDecimal monto_venta;
    private String creado_por;
    private Timestamp creado_el;
    private String moneda;
    private String tipo_cliente;

    public BigDecimal getTcscctahaberdtsid() {
        return tcscctahaberdtsid;
    }
    public void setTcscctahaberdtsid(BigDecimal tcscctahaberdtsid) {
        this.tcscctahaberdtsid = tcscctahaberdtsid;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public BigDecimal getTcscdtsid() {
        return tcscdtsid;
    }
    public void setTcscdtsid(BigDecimal tcscdtsid) {
        this.tcscdtsid = tcscdtsid;
    }
    public BigDecimal getId_rutapanel() {
        return id_rutapanel;
    }
    public void setId_rutapanel(BigDecimal id_rutapanel) {
        this.id_rutapanel = id_rutapanel;
    }
    public String getTipo_id() {
        return tipo_id;
    }
    public void setTipo_id(String tipo_id) {
        this.tipo_id = tipo_id;
    }
    public BigDecimal getTcscventaid() {
        return tcscventaid;
    }
    public void setTcscventaid(BigDecimal tcscventaid) {
        this.tcscventaid = tcscventaid;
    }
    public String getFolio_scl() {
        return folio_scl;
    }
    public void setFolio_scl(String folio_scl) {
        this.folio_scl = folio_scl;
    }
    public BigDecimal getVendedor() {
        return vendedor;
    }
    public void setVendedor(BigDecimal vendedor) {
        this.vendedor = vendedor;
    }
    public Timestamp getFecha() {
        return fecha;
    }
    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
    public BigDecimal getMonto_venta() {
        return monto_venta;
    }
    public void setMonto_venta(BigDecimal monto_venta) {
        this.monto_venta = monto_venta;
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
    public String getMoneda() {
        return moneda;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    public String getTipo_cliente() {
        return tipo_cliente;
    }
    public void setTipo_cliente(String tipo_cliente) {
        this.tipo_cliente = tipo_cliente;
    }
    public static String getnTabla() {
        return N_TABLA;
    }
    public static String getSequence() {
        return SEQUENCE;
    }
    public static String getCampoTcscctahaberdtsid() {
        return CAMPO_TCSCCTAHABERDTSID;
    }
    public static String getCampoTcscdtsid() {
        return CAMPO_TCSCDTSID;
    }
    public static String getCampoIdRutapanel() {
        return CAMPO_ID_RUTAPANEL;
    }
    public static String getCampoTipoId() {
        return CAMPO_TIPO_ID;
    }
    public static String getCampoTcscventaid() {
        return CAMPO_TCSCVENTAID;
    }
    public static String getCampoFolioScl() {
        return CAMPO_FOLIO_SCL;
    }
    public static String getCampoVendedor() {
        return CAMPO_VENDEDOR;
    }
    public static String getCampoFecha() {
        return CAMPO_FECHA;
    }
    public static String getCampoMontoVenta() {
        return CAMPO_MONTO_VENTA;
    }
    public static String getCampoCreadoPor() {
        return CAMPO_CREADO_POR;
    }
    public static String getCampoCreadoEl() {
        return CAMPO_CREADO_EL;
    }
    public static String getCampoMoneda() {
        return CAMPO_MONEDA;
    }
    public static String getCampoTipoCliente() {
        return CAMPO_TIPO_CLIENTE;
    }    
}