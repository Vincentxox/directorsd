package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Cuenta {
    public static final String N_TABLA = "TC_SC_CTA_BANCARIA";
    public static final String SEQUENCE = "TC_SC_CTA_BANCARIA_SQ.nextval";

    public final static String CAMPO_TCSCCTABANCARIAID = "tcscctabancariaid";
    public final static String CAMPO_TCSCDTSID = "tcscdtsid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_BANCO = "banco";
    public final static String CAMPO_NO_CUENTA = "no_cuenta";
    public final static String CAMPO_TIPO_CUENTA = "tipo_cuenta";
    public final static String CAMPO_NOMBRE_CUENTA = "nombre_cuenta";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcscctabancariaid;
    private BigDecimal tcscdtsid;
    private String banco;
    private String no_cuenta;
    private String tipo_cuenta;
    private String nombre_cuenta;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    
    public BigDecimal getTcscctabancariaid() {
        return tcscctabancariaid;
    }
    public void setTcscctabancariaid(BigDecimal tcscctabancariaid) {
        this.tcscctabancariaid = tcscctabancariaid;
    }
    public String getBanco() {
        return banco;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }
    public String getNo_cuenta() {
        return no_cuenta;
    }
    public void setNo_cuenta(String no_cuenta) {
        this.no_cuenta = no_cuenta;
    }
    public String getTipo_cuenta() {
        return tipo_cuenta;
    }
    public void setTipo_cuenta(String tipo_cuenta) {
        this.tipo_cuenta = tipo_cuenta;
    }
    public String getNombre_cuenta() {
        return nombre_cuenta;
    }
    public void setNombre_cuenta(String nombre_cuenta) {
        this.nombre_cuenta = nombre_cuenta;
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
	public BigDecimal getTcscdtsid() {
		return tcscdtsid;
	}
	public void setTcscdtsid(BigDecimal tcscdtsid) {
		this.tcscdtsid = tcscdtsid;
	}
    
}