package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class AsignacionReserva {
    public static final String N_TABLA = "TC_SC_ASIGNACION_RESERVA";
    public static final String SEQUENCE = "TC_SC_ASIGNACION_RESERVA_SQ.nextval";

    public final static String CAMPO_TCSCASIGNACIONRESERVAID = "tcscasignacionreservaid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TIPO = "tipo";
    public final static String CAMPO_IDVENDEDOR = "idvendedor";
    public final static String CAMPO_BODEGA_ORIGEN = "bodega_origen";
    public final static String CAMPO_BODEGA_DESTINO = "bodega_destino";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_OBSERVACIONES = "observaciones";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcscasignacionreservaid;
    private BigDecimal tcScCatPaisId;
    private String tipo;
    private BigDecimal idvendedor;
    private BigDecimal bodega_origen;
    private BigDecimal bodega_destino;
    private String estado;
    private String observaciones;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;

    public BigDecimal getTcscasignacionreservaid() {
        return tcscasignacionreservaid;
    }
    public void setTcscasignacionreservaid(BigDecimal tcscasignacionreservaid) {
        this.tcscasignacionreservaid = tcscasignacionreservaid;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public BigDecimal getIdvendedor() {
        return idvendedor;
    }
    public void setIdvendedor(BigDecimal idvendedor) {
        this.idvendedor = idvendedor;
    }
    public BigDecimal getBodega_origen() {
        return bodega_origen;
    }
    public void setBodega_origen(BigDecimal bodega_origen) {
        this.bodega_origen = bodega_origen;
    }
    public BigDecimal getBodega_destino() {
        return bodega_destino;
    }
    public void setBodega_destino(BigDecimal bodega_destino) {
        this.bodega_destino = bodega_destino;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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