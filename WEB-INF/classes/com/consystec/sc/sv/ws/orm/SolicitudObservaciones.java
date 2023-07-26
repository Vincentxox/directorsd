package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class SolicitudObservaciones {
    public static final String N_TABLA = "TC_SC_OBS_SOLICITUD";
    public static final String SEQUENCE = "TC_SC_OBS_SOLICITUD_SQ.nextval";

    public final static String CAMPO_TCSCOBSSOLICITUDID = "tcscobssolicitudid";
    public final static String CAMPO_TCSCSOLICITUD = "tcscsolicitud";
    public final static String CAMPO_OBSERVACION = "observacion";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";

    private BigDecimal tcscobssolicitudid;
    private BigDecimal tcscsolicitud;
    private String observacion;
    private Timestamp creado_el;
    private String creado_por;

    public BigDecimal getTcscobssolicitudid() {
        return tcscobssolicitudid;
    }
    public void setTcscobssolicitudid(BigDecimal tcscobssolicitudid) {
        this.tcscobssolicitudid = tcscobssolicitudid;
    }
    public BigDecimal getTcscsolicitud() {
        return tcscsolicitud;
    }
    public void setTcscsolicitud(BigDecimal tcscsolicitud) {
        this.tcscsolicitud = tcscsolicitud;
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