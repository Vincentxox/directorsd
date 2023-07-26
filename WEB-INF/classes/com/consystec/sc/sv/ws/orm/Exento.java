package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Exento {
    public static final String N_TABLA = "TC_SC_EXENTO";
    public static final String SEQUENCE = "TC_SC_EXENTO_SQ.nextval";

    public final static String CAMPO_TCSCEXENTOID = "tcscexentoid";
    public final static String CAMPO_TCSCVENTAID = "tcscventaid";
    public final static String CAMPO_DESCRIPCION = "descripcion";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";

    private BigDecimal tcscexentoid;
    private BigDecimal tcscventaid;
    private String descripcion;
    private Timestamp creado_el;
    private String creado_por;

    public BigDecimal getTcscexentoid() {
        return tcscexentoid;
    }
    public void setTcscexentoid(BigDecimal tcscexentoid) {
        this.tcscexentoid = tcscexentoid;
    }
    public BigDecimal getTcscventaid() {
        return tcscventaid;
    }
    public void setTcscventaid(BigDecimal tcscventaid) {
        this.tcscventaid = tcscventaid;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
}