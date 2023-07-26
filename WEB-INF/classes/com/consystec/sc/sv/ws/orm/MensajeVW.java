package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;

public class MensajeVW {
    public static final String N_TABLA = "TC_SC_MENSAJE_VW";
    public final static String CAMPO_TCSCMENSAJEID = "tcscmensajeid";
    public final static String CAMPO_CODRESULTADO = "codresultado";
    public final static String CAMPO_MOSTRAR = "mostrar";
    public final static String CAMPO_DESCRIPCION = "descripcion";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_TIPO_EXCEPCION = "tipo_excepcion";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";

    private BigDecimal tcscmensajeid;
    private BigDecimal codresultado;
    private BigDecimal mostrar;
    private String descripcion;
    private BigDecimal estado;
    private String tipo_excepcion;
    private BigDecimal tcsccatpaisid;

    public BigDecimal getTcscmensajeid() {
        return tcscmensajeid;
    }

    public void setTcscmensajeid(BigDecimal tcscmensajeid) {
        this.tcscmensajeid = tcscmensajeid;
    }

    public BigDecimal getCodresultado() {
        return codresultado;
    }

    public void setCodresultado(BigDecimal codresultado) {
        this.codresultado = codresultado;
    }

    public BigDecimal getMostrar() {
        return mostrar;
    }

    public void setMostrar(BigDecimal mostrar) {
        this.mostrar = mostrar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getEstado() {
        return estado;
    }

    public void setEstado(BigDecimal estado) {
        this.estado = estado;
    }

    public String getTipo_excepcion() {
        return tipo_excepcion;
    }

    public void setTipo_excepcion(String tipo_excepcion) {
        this.tipo_excepcion = tipo_excepcion;
    }

    public BigDecimal getTcsccatpaisid() {
        return tcsccatpaisid;
    }

    public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
        this.tcsccatpaisid = tcsccatpaisid;
    }

}
