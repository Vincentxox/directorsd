package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CondicionOferta {
    public static final String N_TABLA = "TC_SC_DET_CONDICION_OFERTA";
    public static final String SEQUENCE = "TC_SC_DET_CONDICION_OFERTA_SQ.nextval";
    
    public final static String CAMPO_TCSCDETCONDICIONOFERTAID = "tcscdetcondicionofertaid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TCSCCONDICIONID = "tcsccondicionid";
    public final static String CAMPO_TIPO_OFERTA = "tipo_oferta";
    public final static String CAMPO_ARTICULO = "articulo";
    public final static String CAMPO_TIPO_CLIENTE= "tipo_cliente";
    public final static String CAMPO_TECNOLOGIA = "tecnologia";
    public final static String CAMPO_MONTO_INICIAL = "monto_inicial";
    public final static String CAMPO_MONTO_FINAL = "monto_final";
    public final static String CAMPO_OPERADOR = "operador";
    public final static String CAMPO_TIPO_DESCUENTO = "tipo_descuento";
    public final static String CAMPO_VALOR_DESCUENTO = "valor_descuento";
    public final static String CAMPO_TCSCPUNTOVENTAID = "tcscpuntoventaid";
    public final static String CAMPO_CATEGORIA = "categoria";
    public final static String CAMPO_ZONACOMERCIAL = "zonacomercial";
    public final static String CAMPO_ARTICULO_REGALO = "articulo_regalo";
    public final static String CAMPO_CANT_ARTICULO_REGALO = "cant_articulo_regalo";
    public final static String CAMPO_TIPO_DESC_REGALO = "tipo_desc_regalo";
    public final static String CAMPO_VALOR_DESC_REGALO = "valor_desc_regalo";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";
    public final static String CAMPO_ID_DESCUENTO = "id_descuento";
    
    private BigDecimal tcscdetcondicionofertaid;
    private BigDecimal tcScCatPaisId;
    private BigDecimal tcsccondicionid;
    private String tecnologia;
    private BigDecimal articulo;
    private String tipo_oferta;
    private String tipo_cliente;
    private BigDecimal monto_inicial;
    private BigDecimal monto_final;
    private String operador;
    private String tipo_descuento;
    private BigDecimal valor_descuento;
    private BigDecimal tcscpuntoventaid;
    private String categoria;
    private BigDecimal zonacomercial;
    private BigDecimal articulo_regalo;
    private BigDecimal cant_articulo_regalo;
    private String tipo_desc_regalo;
    private BigDecimal valor_desc_regalo;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    private BigDecimal id_descuento;

    public BigDecimal getTcscdetcondicionofertaid() {
        return tcscdetcondicionofertaid;
    }
    public void setTcscdetcondicionofertaid(BigDecimal tcscdetcondicionofertaid) {
        this.tcscdetcondicionofertaid = tcscdetcondicionofertaid;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public BigDecimal getTcsccondicionid() {
        return tcsccondicionid;
    }
    public void setTcsccondicionid(BigDecimal tcsccondicionid) {
        this.tcsccondicionid = tcsccondicionid;
    }
    public String getTecnologia() {
        return tecnologia;
    }
    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }
    public BigDecimal getArticulo() {
        return articulo;
    }
    public void setArticulo(BigDecimal articulo) {
        this.articulo = articulo;
    }
    public String getTipo_oferta() {
        return tipo_oferta;
    }
    public void setTipo_oferta(String tipo_oferta) {
        this.tipo_oferta = tipo_oferta;
    }
    public String getTipo_cliente() {
        return tipo_cliente;
    }
    public void setTipo_cliente(String tipo_cliente) {
        this.tipo_cliente = tipo_cliente;
    }
    public BigDecimal getMonto_inicial() {
        return monto_inicial;
    }
    public void setMonto_inicial(BigDecimal monto_inicial) {
        this.monto_inicial = monto_inicial;
    }
    public BigDecimal getMonto_final() {
        return monto_final;
    }
    public void setMonto_final(BigDecimal monto_final) {
        this.monto_final = monto_final;
    }
    public String getOperador() {
        return operador;
    }
    public void setOperador(String operador) {
        this.operador = operador;
    }
    public String getTipo_descuento() {
        return tipo_descuento;
    }
    public void setTipo_descuento(String tipo_descuento) {
        this.tipo_descuento = tipo_descuento;
    }
    public BigDecimal getValor_descuento() {
        return valor_descuento;
    }
    public void setValor_descuento(BigDecimal valor_descuento) {
        this.valor_descuento = valor_descuento;
    }
    public BigDecimal getTcscpuntoventaid() {
        return tcscpuntoventaid;
    }
    public void setTcscpuntoventaid(BigDecimal tcscpuntoventaid) {
        this.tcscpuntoventaid = tcscpuntoventaid;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public BigDecimal getZonacomercial() {
        return zonacomercial;
    }
    public void setZonacomercial(BigDecimal zonacomercial) {
        this.zonacomercial = zonacomercial;
    }
    public BigDecimal getArticulo_regalo() {
        return articulo_regalo;
    }
    public void setArticulo_regalo(BigDecimal articulo_regalo) {
        this.articulo_regalo = articulo_regalo;
    }
    public BigDecimal getCant_articulo_regalo() {
        return cant_articulo_regalo;
    }
    public void setCant_articulo_regalo(BigDecimal cant_articulo_regalo) {
        this.cant_articulo_regalo = cant_articulo_regalo;
    }
    public String getTipo_desc_regalo() {
        return tipo_desc_regalo;
    }
    public void setTipo_desc_regalo(String tipo_desc_regalo) {
        this.tipo_desc_regalo = tipo_desc_regalo;
    }
    public BigDecimal getValor_desc_regalo() {
        return valor_desc_regalo;
    }
    public void setValor_desc_regalo(BigDecimal valor_desc_regalo) {
        this.valor_desc_regalo = valor_desc_regalo;
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
	public BigDecimal getId_descuento() {
		return id_descuento;
	}
	public void setId_descuento(BigDecimal id_descuento) {
		this.id_descuento = id_descuento;
	}
    
}