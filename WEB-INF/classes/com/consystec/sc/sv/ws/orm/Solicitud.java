package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Solicitud {
    public static final String N_TABLA = "TC_SC_SOLICITUD";
    public static final String N_TABLA_ID = "TC_SC_SOLICITUD_ID";
    public static final String SEQUENCE = "TC_SC_SOLICITUD_SQ.nextval";

    public final static String CAMPO_TCSCSOLICITUDID = "tcscsolicitudid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TCSCDTSID = "tcscdtsid";
    public final static String CAMPO_TCSCBUZONID = "tcscbuzonid";
    public final static String CAMPO_TCSCBODEGAVIRTUALID = "tcscbodegavirtualid";
    public final static String CAMPO_FECHA = "fecha";
    public final static String CAMPO_TIPO_SOLICITUD = "tipo_solicitud";
    public final static String CAMPO_IDVENDEDOR = "idvendedor";
    public final static String CAMPO_CAUSA_SOLICITUD = "causa_solicitud";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_OBSERVACIONES = "observaciones";
    public final static String CAMPO_SERIADO = "seriado";
    public final static String CAMPO_ORIGEN = "origen";
    public final static String CAMPO_TIPO_SINIESTRO = "tipo_siniestro";
    public final static String CAMPO_IDTIPO = "idtipo";
    public final static String CAMPO_TIPO = "tipo";
    public final static String CAMPO_IDJORNADA = "idjornada";
    public final static String CAMPO_ORIGEN_CANCELACION = "origen_cancelacion";
    public final static String CAMPO_OBS_CANCELACION = "obs_cancelacion";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";
    public final static String CAMPO_BUZON_ANTERIOR = "buzon_anterior";
    public final static String CAMPO_TOTAL_DEUDA = "total_deuda";
    public final static String CAMPO_TASA_CAMBIO = "tasa_cambio";
    public final static String CAMPO_BUZON_ORIGEN = "buzon_origen";

    private BigDecimal tcscsolicitudid;
    private BigDecimal tcScCatPaisId;
    private BigDecimal tcscdtsid;
    private BigDecimal tcscbuzonid;
    private BigDecimal tcscbodegavirtualid;
    private Timestamp fecha;
    private String tipo_solicitud;
    private BigDecimal idvendedor;
    private String causa_solicitud;
    private String estado;
    private String observaciones;
    private BigDecimal seriado;
    private String origen;
    private String tipo_siniestro;
    private BigDecimal idtipo;
    private String tipo;
    private BigDecimal idjornada;
    private String origen_cancelacion;
    private String obs_cancelacion;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    private BigDecimal buzon_anterior;
    private BigDecimal total_deuda;
    private BigDecimal tasa_cambio;
    private BigDecimal buzon_origen;

    public BigDecimal getTcscsolicitudid() {
        return tcscsolicitudid;
    }
    public void setTcscsolicitudid(BigDecimal tcscsolicitudid) {
        this.tcscsolicitudid = tcscsolicitudid;
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
    public BigDecimal getTcscbuzonid() {
        return tcscbuzonid;
    }
    public void setTcscbuzonid(BigDecimal tcscbuzonid) {
        this.tcscbuzonid = tcscbuzonid;
    }
    public BigDecimal getTcscbodegavirtualid() {
        return tcscbodegavirtualid;
    }
    public void setTcscbodegavirtualid(BigDecimal tcscbodegavirtualid) {
        this.tcscbodegavirtualid = tcscbodegavirtualid;
    }
    public Timestamp getFecha() {
        return fecha;
    }
    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
    public String getTipo_solicitud() {
        return tipo_solicitud;
    }
    public void setTipo_solicitud(String tipo_solicitud) {
        this.tipo_solicitud = tipo_solicitud;
    }
    public BigDecimal getIdvendedor() {
        return idvendedor;
    }
    public void setIdvendedor(BigDecimal idvendedor) {
        this.idvendedor = idvendedor;
    }
    public String getCausa_solicitud() {
        return causa_solicitud;
    }
    public void setCausa_solicitud(String causa_solicitud) {
        this.causa_solicitud = causa_solicitud;
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
    public BigDecimal getSeriado() {
        return seriado;
    }
    public void setSeriado(BigDecimal seriado) {
        this.seriado = seriado;
    }
    public String getOrigen() {
        return origen;
    }
    public void setOrigen(String origen) {
        this.origen = origen;
    }
    public String getTipo_siniestro() {
        return tipo_siniestro;
    }
    public void setTipo_siniestro(String tipo_siniestro) {
        this.tipo_siniestro = tipo_siniestro;
    }
    public BigDecimal getIdtipo() {
        return idtipo;
    }
    public void setIdtipo(BigDecimal idtipo) {
        this.idtipo = idtipo;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public BigDecimal getIdjornada() {
        return idjornada;
    }
    public void setIdjornada(BigDecimal idjornada) {
        this.idjornada = idjornada;
    }
    public String getOrigen_cancelacion() {
        return origen_cancelacion;
    }
    public void setOrigen_cancelacion(String origen_cancelacion) {
        this.origen_cancelacion = origen_cancelacion;
    }
    public String getObs_cancelacion() {
        return obs_cancelacion;
    }
    public void setObs_cancelacion(String obs_cancelacion) {
        this.obs_cancelacion = obs_cancelacion;
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
    public BigDecimal getBuzon_anterior() {
        return buzon_anterior;
    }
    public void setBuzon_anterior(BigDecimal buzon_anterior) {
        this.buzon_anterior = buzon_anterior;
    }
    public BigDecimal getTotal_deuda() {
        return total_deuda;
    }
    public void setTotal_deuda(BigDecimal total_deuda) {
        this.total_deuda = total_deuda;
    }
    public BigDecimal getTasa_cambio() {
        return tasa_cambio;
    }
    public void setTasa_cambio(BigDecimal tasa_cambio) {
        this.tasa_cambio = tasa_cambio;
    }
	public BigDecimal getBuzon_origen() {
		return buzon_origen;
	}
	public void setBuzon_origen(BigDecimal buzon_origen) {
		this.buzon_origen = buzon_origen;
	}
    
}