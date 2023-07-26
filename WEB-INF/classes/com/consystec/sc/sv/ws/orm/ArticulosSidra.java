package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ArticulosSidra {
    public static final String N_TABLA = "TC_SC_ARTICULO_INV";
    public static final String SEQUENCE = "TC_SC_ARTICULO_INV_SQ.nextval";

    public final static String CAMPO_TCSCARTICULOINVID = "tcscarticuloinvid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_ARTICULO = "articulo";
    public final static String CAMPO_DESCRIPCION = "descripcion";
    public final static String CAMPO_TIPO_GRUPO_SIDRA = "tipo_grupo_sidra";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcscarticuloinvid;
    private BigDecimal tcScCatPaisId;
    private BigDecimal articulo;
    private String descripcion;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    private String tipo_grupo_sidra;
    
    public BigDecimal getTcscarticuloinvid() {
        return tcscarticuloinvid;
    }
    public void setTcscarticuloinvid(BigDecimal tcscarticuloinvid) {
        this.tcscarticuloinvid = tcscarticuloinvid;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public BigDecimal getArticulo() {
        return articulo;
    }
    public void setArticulo(BigDecimal articulo) {
        this.articulo = articulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
	public String getTipo_grupo_sidra() {
		return tipo_grupo_sidra;
	}
	public void setTipo_grupo_sidra(String tipo_grupo_sidra) {
		this.tipo_grupo_sidra = tipo_grupo_sidra;
	}
    
    
}