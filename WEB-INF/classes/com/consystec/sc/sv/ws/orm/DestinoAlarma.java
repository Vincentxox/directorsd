package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class DestinoAlarma {
    public static final String N_TABLA = "TC_SC_DESTINO_ALARMA";
    public static final String SEQUENCE = "TC_SC_DESTINO_ALARMA_SQ.nextval";

    public final static String CAMPO_TCSCDESTINOALARMAID = "tcscdestinoalarmaid";
    public final static String CAMPO_ALARMA = "alarma";
    public final static String CAMPO_CORREO = "correo";
    public final static String CAMPO_NOMBRE_DESTINATARIO = "nombre_destinatario";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_CREADO_EL = "creado_el";

    private BigDecimal tcscdestinoalarmaid;
    private String alarma;
    private String correo;
    private String nombre_destinatario;
    private String estado;
    private String creado_por;
    private Timestamp creado_el;

    public BigDecimal getTcscdestinoalarmaid() {
        return tcscdestinoalarmaid;
    }
    public void setTcscdestinoalarmaid(BigDecimal tcscdestinoalarmaid) {
        this.tcscdestinoalarmaid = tcscdestinoalarmaid;
    }
    public String getAlarma() {
        return alarma;
    }
    public void setAlarma(String alarma) {
        this.alarma = alarma;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getNombre_destinatario() {
        return nombre_destinatario;
    }
    public void setNombre_destinatario(String nombre_destinatario) {
        this.nombre_destinatario = nombre_destinatario;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getCreado_por() {
        return creado_por;
    }
    public void setCreado_por(String creado_por) {
        this.creado_por = creado_por;
    }
    public Timestamp getCreado_el() {
        return creado_el;
    }
    public void setCreado_el(Timestamp creado_el) {
        this.creado_el = creado_el;
    }
}