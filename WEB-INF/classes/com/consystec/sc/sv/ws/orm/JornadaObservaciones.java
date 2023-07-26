package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class JornadaObservaciones {
    public static final String N_TABLA = "TC_SC_OBS_JORNADA";
    public static final String SEQUENCE = "TC_SC_OBS_JORNADA_SQ.nextval";

    public final static String CAMPO_TCSCOBSJORNADAID = "tcscobsjornadaid";
    public final static String CAMPO_TCSCJORNADAID = "tcscjornadaid";
    public final static String CAMPO_OBSERVACION = "observacion";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";

    private BigDecimal tcscobsjornadaid;
    private BigDecimal tcscjornadaid;
    private String observacion;
    private Timestamp creado_el;
    private String creado_por;
    public BigDecimal getTcscobsjornadaid() {
        return tcscobsjornadaid;
    }
    public void setTcscobsjornadaid(BigDecimal tcscobsjornadaid) {
        this.tcscobsjornadaid = tcscobsjornadaid;
    }
    public BigDecimal getTcscjornadaid() {
        return tcscjornadaid;
    }
    public void setTcscjornadaid(BigDecimal tcscjornadaid) {
        this.tcscjornadaid = tcscjornadaid;
    }
    public String getObservacion() {
        return observacion;
    }
    public void setObservacion(String observacion) {
        this.observacion = observacion;
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