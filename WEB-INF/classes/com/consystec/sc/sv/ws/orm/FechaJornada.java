package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class FechaJornada {
    public static final String N_TABLA = "TC_SC_FECHA_JORNADA";
    public static final String SEQUENCE = "TC_SC_FECHA_JORNADA_SQ.nextval";

    public final static String CAMPO_TCSCFECHAJORNADAID = "tcscfechajornadaid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_VENDEDOR = "vendedor";
    public final static String CAMPO_FECHA = "fecha";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcscfechajornadaid;
    private BigDecimal tcScCatPaisId;
    private BigDecimal vendedor;
    private Timestamp fecha;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;

    public BigDecimal getTcscfechajornadaid() {
        return tcscfechajornadaid;
    }
    public void setTcscfechajornadaid(BigDecimal tcscfechajornadaid) {
        this.tcscfechajornadaid = tcscfechajornadaid;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
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