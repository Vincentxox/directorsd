package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OfertaCampaniaDet {
    public static final String N_TABLA = "TC_SC_DET_PANELRUTA";
    public static final String SEQUENCE = "TC_SC_DET_PANELRUTA_SQ.nextval";

    public final static String CAMPO_TCSCDETPANELRUTAID = "tcscdetpanelrutaid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TCSCOFERTACAMPANIAID = "tcscofertacampaniaid";
    public final static String CAMPO_TCSCTIPOID = "tcsctipoid";
    public final static String CAMPO_TIPO = "tipo";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcscdetpanelrutaid;
    private BigDecimal tcScCatPaisId;
    private BigDecimal tcscofertacampaniaid;
    private BigDecimal tcsctipoid;
    private String tipo;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    
    public BigDecimal getTcscdetpanelrutaid() {
        return tcscdetpanelrutaid;
    }
    public void setTcscdetpanelrutaid(BigDecimal tcscdetpanelrutaid) {
        this.tcscdetpanelrutaid = tcscdetpanelrutaid;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public BigDecimal getTcscofertacampaniaid() {
        return tcscofertacampaniaid;
    }
    public void setTcscofertacampaniaid(BigDecimal tcscofertacampaniaid) {
        this.tcscofertacampaniaid = tcscofertacampaniaid;
    }
    public BigDecimal getTcsctipoid() {
        return tcsctipoid;
    }
    public void setTcsctipoid(BigDecimal tcsctipoid) {
        this.tcsctipoid = tcsctipoid;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
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