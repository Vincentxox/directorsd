package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Sincronizacion {
    public static final String N_TABLA = "TC_SC_SINC_VENDEDOR";
    public static final String SEQUENCE = "TC_SC_SINC_VENDEDOR_SQ.nextval";

    public final static String CAMPO_TCSCSINCVENDEDORID = "tcscsincvendedorid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_IDVENDEDOR = "idvendedor";
    public final static String CAMPO_COD_DISPOSITIVO = "cod_dispositivo";
    public final static String CAMPO_TCSCJORNADAVENDID = "tcscjornadavendid";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcscsincvendedorid;
    private BigDecimal tcScCatPaisId;
    private BigDecimal idvendedor;
    private String cod_dispositivo;
    private BigDecimal tcscjornadavendid;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    
    public BigDecimal getTcscsincvendedorid() {
        return tcscsincvendedorid;
    }
    public void setTcscsincvendedorid(BigDecimal tcscsincvendedorid) {
        this.tcscsincvendedorid = tcscsincvendedorid;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public BigDecimal getIdvendedor() {
        return idvendedor;
    }
    public void setIdvendedor(BigDecimal idvendedor) {
        this.idvendedor = idvendedor;
    }
    public String getCod_dispositivo() {
        return cod_dispositivo;
    }
    public void setCod_dispositivo(String cod_dispositivo) {
        this.cod_dispositivo = cod_dispositivo;
    }
    public BigDecimal getTcscjornadavendid() {
        return tcscjornadavendid;
    }
    public void setTcscjornadavendid(BigDecimal tcscjornadavendid) {
        this.tcscjornadavendid = tcscjornadavendid;
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