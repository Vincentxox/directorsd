package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class LogTiempos {
    public static final String N_TABLA = "TC_SC_LOG_TIEMPOS";
    public static final String SEQUENCE = "TC_SC_LOG_TIEMPOS_SQ.nextval";

    public final static String CAMPO_TCSCLOGID = "tcsclogid";
    public final static String CAMPO_COD_AREA = "cod_area";
    public final static String CAMPO_SERVICIO = "servicio";
    public final static String CAMPO_URL = "url";
    public final static String CAMPO_ORIGEN = "origen";
    public final static String CAMPO_IP_ORIGEN = "ip_origen";
    public final static String CAMPO_HORA_INICIO = "hora_inicio";
    public final static String CAMPO_HORA_FIN = "hora_fin";
    public final static String CAMPO_TIEMPO_EJECUCION = "tiempo_ejecucion";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";

    private BigDecimal tcsclogid;
    private BigDecimal cod_area;
    private String servicio;
    private String url;
    private String origen;
    private String ip_origen;
    private Timestamp hora_inicio;
    private Timestamp hora_fin;
    private BigDecimal tiempo_ejecucion;
    private Timestamp creado_el;
    private String creado_por;

    public BigDecimal getTcsclogid() {
        return tcsclogid;
    }
    public void setTcsclogid(BigDecimal tcsclogid) {
        this.tcsclogid = tcsclogid;
    }
    public BigDecimal getCod_area() {
        return cod_area;
    }
    public void setCod_area(BigDecimal cod_area) {
        this.cod_area = cod_area;
    }
    public String getServicio() {
        return servicio;
    }
    public void setServicio(String servicio) {
        this.servicio = servicio;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getOrigen() {
        return origen;
    }
    public void setOrigen(String origen) {
        this.origen = origen;
    }
    public String getIp_origen() {
        return ip_origen;
    }
    public void setIp_origen(String ip_origen) {
        this.ip_origen = ip_origen;
    }
    public Timestamp getHora_inicio() {
        return hora_inicio;
    }
    public void setHora_inicio(Timestamp hora_inicio) {
        this.hora_inicio = hora_inicio;
    }
    public Timestamp getHora_fin() {
        return hora_fin;
    }
    public void setHora_fin(Timestamp hora_fin) {
        this.hora_fin = hora_fin;
    }
    public BigDecimal getTiempo_ejecucion() {
        return tiempo_ejecucion;
    }
    public void setTiempo_ejecucion(BigDecimal tiempo_ejecucion) {
        this.tiempo_ejecucion = tiempo_ejecucion;
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