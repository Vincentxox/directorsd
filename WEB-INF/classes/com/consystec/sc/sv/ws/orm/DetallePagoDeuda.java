package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class DetallePagoDeuda {
    public static final String N_TABLA = "TC_SC_DET_PAGO_DEUDA";
    public static final String SEQUENCE = "TC_SC_DET_PAGO_DEUDA_SQ.nextval";

    public final static String CAMPO_TCSCDETPAGOID = "tcscdetpagoid";
    public final static String CAMPO_TCSCSOLICITUDID = "tcscsolicitudid";
    public final static String CAMPO_FORMA_PAGO = "forma_pago";
    public final static String CAMPO_MONTO = "monto";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcscdetpagoid;
    private BigDecimal tcscsolicitudid;
    private String forma_pago;
    private String monto;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;

    public BigDecimal getTcscdetpagoid() {
        return tcscdetpagoid;
    }
    public void setTcscdetpagoid(BigDecimal tcscdetpagoid) {
        this.tcscdetpagoid = tcscdetpagoid;
    }
    public BigDecimal getTcscsolicitudid() {
        return tcscsolicitudid;
    }
    public void setTcscsolicitudid(BigDecimal tcscsolicitudid) {
        this.tcscsolicitudid = tcscsolicitudid;
    }
    public String getForma_pago() {
        return forma_pago;
    }
    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }
    public String getMonto() {
        return monto;
    }
    public void setMonto(String monto) {
        this.monto = monto;
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