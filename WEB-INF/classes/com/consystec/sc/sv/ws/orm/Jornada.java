package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Jornada {
    public static final String N_TABLA = "TC_SC_JORNADA_VEND";
    public static final String SEQUENCE = "TC_SC_JORNADA_VEND_SQ.nextval";

    public final static String CAMPO_TCSCJORNADAVENID = "tcscjornadavenid";
    public final static String CAMPO_TCSCDTSID = "tcscdtsid";
    public final static String CAMPO_VENDEDOR = "vendedor";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_COD_DISPOSITIVO = "cod_dispositivo";
    public final static String CAMPO_FECHA = "fecha";
    public final static String CAMPO_IDTIPO = "idtipo";
    public final static String CAMPO_DESCRIPCION_TIPO = "descripcion_tipo";
    public final static String CAMPO_TCSCBODEGAVIRTUAL = "tcscbodegavirtual";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_FECHA_FINALIZACION = "fecha_finalizacion";
    public final static String CAMPO_OBSERVACIONES = "observaciones";
    public final static String CAMPO_ESTADO_LIQUIDACION = "estado_liquidacion";
    public final static String CAMPO_FECHA_LIQUIDACION = "fecha_liquidacion";
    public final static String CAMPO_JORNADA_RESPONSABLE = "jornada_responsable";
    public final static String CAMPO_COD_DISPOSITIVO_FIN = "cod_dispositivo_fin";
    public final static String CAMPO_ENVIO_ALARMA = "envio_alarma";
    public final static String CAMPO_TIPO_ALARMA = "tipo_alarma";
    public final static String CAMPO_ENVIO_ALARMA_FIN = "envio_alarma_fin";
    public final static String CAMPO_TIPO_ALARMA_FIN = "tipo_alarma_fin";
    public final static String CAMPO_SALDO_PAYMENT = "saldo_payment";
    public final static String CAMPO_SALDO_PAYMENT_FINAL = "saldo_payment_final";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";
    public final static String CAMPO_ESTADO_PAGO = "estado_pago";
    public final static String CAMPO_SOLICITUD_PAGO = "solicitud_pago";

    private BigDecimal tcscjornadavenid;
    private BigDecimal tcscdtsid;
    private BigDecimal vendedor;
    private BigDecimal tcsccatpaisid;
    private String cod_dispositivo;
    private Timestamp fecha;
    private BigDecimal idtipo;
    private String descripcion_tipo;
    private BigDecimal tcscbodegavirtual;
    private String estado;
    private String fecha_finalizacion;
    private String observaciones;
    private String estado_liquidacion;
    private String fecha_liquidacion;
    private BigDecimal jornada_responsable;
    private String cod_dispositivo_fin;
    private BigDecimal envio_alarma;
    private String tipo_alarma;
    private BigDecimal envio_alarma_fin;
    private String tipo_alarma_fin;
    private BigDecimal saldo_payment;
    private BigDecimal saldo_payment_final;
    private String estado_pago;
    private BigDecimal solicitud_pago;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;

    public BigDecimal getTcscjornadavenid() {
        return tcscjornadavenid;
    }
    public void setTcscjornadavenid(BigDecimal tcscjornadavenid) {
        this.tcscjornadavenid = tcscjornadavenid;
    }
    public BigDecimal getTcscdtsid() {
        return tcscdtsid;
    }
    public void setTcscdtsid(BigDecimal tcscdtsid) {
        this.tcscdtsid = tcscdtsid;
    }
    public BigDecimal getVendedor() {
        return vendedor;
    }
    public void setVendedor(BigDecimal vendedor) {
        this.vendedor = vendedor;
    }
    public BigDecimal getTcsccatpaisid() {
        return tcsccatpaisid;
    }
    public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
        this.tcsccatpaisid = tcsccatpaisid;
    }
    public String getCod_dispositivo() {
        return cod_dispositivo;
    }
    public void setCod_dispositivo(String cod_dispositivo) {
        this.cod_dispositivo = cod_dispositivo;
    }
    public Timestamp getFecha() {
        return fecha;
    }
    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
    public BigDecimal getIdtipo() {
        return idtipo;
    }
    public void setIdtipo(BigDecimal idtipo) {
        this.idtipo = idtipo;
    }
    public String getDescripcion_tipo() {
        return descripcion_tipo;
    }
    public void setDescripcion_tipo(String descripcion_tipo) {
        this.descripcion_tipo = descripcion_tipo;
    }
    public BigDecimal getTcscbodegavirtual() {
        return tcscbodegavirtual;
    }
    public void setTcscbodegavirtual(BigDecimal tcscbodegavirtual) {
        this.tcscbodegavirtual = tcscbodegavirtual;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getFecha_finalizacion() {
        return fecha_finalizacion;
    }
    public void setFecha_finalizacion(String fecha_finalizacion) {
        this.fecha_finalizacion = fecha_finalizacion;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public String getEstado_liquidacion() {
        return estado_liquidacion;
    }
    public void setEstado_liquidacion(String estado_liquidacion) {
        this.estado_liquidacion = estado_liquidacion;
    }
    public String getFecha_liquidacion() {
        return fecha_liquidacion;
    }
    public void setFecha_liquidacion(String fecha_liquidacion) {
        this.fecha_liquidacion = fecha_liquidacion;
    }
    public BigDecimal getJornada_responsable() {
        return jornada_responsable;
    }
    public void setJornada_responsable(BigDecimal jornada_responsable) {
        this.jornada_responsable = jornada_responsable;
    }
    public String getCod_dispositivo_fin() {
        return cod_dispositivo_fin;
    }
    public void setCod_dispositivo_fin(String cod_dispositivo_fin) {
        this.cod_dispositivo_fin = cod_dispositivo_fin;
    }
    public BigDecimal getEnvio_alarma() {
        return envio_alarma;
    }
    public void setEnvio_alarma(BigDecimal envio_alarma) {
        this.envio_alarma = envio_alarma;
    }
    public String getTipo_alarma() {
        return tipo_alarma;
    }
    public void setTipo_alarma(String tipo_alarma) {
        this.tipo_alarma = tipo_alarma;
    }
    public BigDecimal getEnvio_alarma_fin() {
        return envio_alarma_fin;
    }
    public void setEnvio_alarma_fin(BigDecimal envio_alarma_fin) {
        this.envio_alarma_fin = envio_alarma_fin;
    }
    public String getTipo_alarma_fin() {
        return tipo_alarma_fin;
    }
    public void setTipo_alarma_fin(String tipo_alarma_fin) {
        this.tipo_alarma_fin = tipo_alarma_fin;
    }
    public BigDecimal getSaldo_payment() {
        return saldo_payment;
    }
    public void setSaldo_payment(BigDecimal saldo_payment) {
        this.saldo_payment = saldo_payment;
    }
    public BigDecimal getSaldo_payment_final() {
        return saldo_payment_final;
    }
    public void setSaldo_payment_final(BigDecimal saldo_payment_final) {
        this.saldo_payment_final = saldo_payment_final;
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
    public String getEstado_pago() {
        return estado_pago;
    }
    public void setEstado_pago(String estado_pago) {
        this.estado_pago = estado_pago;
    }
    public BigDecimal getSolicitud_pago() {
        return solicitud_pago;
    }
    public void setSolicitud_pago(BigDecimal solicitud_pago) {
        this.solicitud_pago = solicitud_pago;
    }
}