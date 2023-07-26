package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class UsuarioBuzon {
    public static final String N_TABLA = "TC_SC_USUARIO_BUZON";
    public static final String SEQUENCE = "TC_SC_USUARIO_BUZON_SQ.nextval";

	public final static String CAMPO_TCSCUSUARIOBUZONID = "tcscusuariobuzonid";
	public final static String CAMPO_TCSCBUZONID = "tcscbuzonid";
	public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	public final static String CAMPO_SECUSUARIOID = "secusuarioid";
	public final static String CAMPO_NOMBRE_VEND = "nombre_vend";
	public final static String CAMPO_USUARIO_VEND = "usuario_vend";
	public final static String CAMPO_ESTADO = "estado";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	
	private BigDecimal tcscusuariobuzonid;
	private BigDecimal secusuarioid;
	private BigDecimal tcsccatpaisid;
	private String nombre_vend;
	private String usuario_vend;
	private String estado;
	private String creado_por;
	private Timestamp creado_el;
	private Timestamp modificado_el;
	private String modificado_por;
	private BigDecimal tcscbuzonid;

    public BigDecimal getTcsccatpaisid() {
		return tcsccatpaisid;
	}
	public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
	}
	public BigDecimal getTcscusuariobuzonid() {
        return tcscusuariobuzonid;
    }
    public void setTcscusuariobuzonid(BigDecimal tcscusuariobuzonid) {
        this.tcscusuariobuzonid = tcscusuariobuzonid;
    }
    public BigDecimal getSecusuarioid() {
        return secusuarioid;
    }
    public void setSecusuarioid(BigDecimal secusuarioid) {
        this.secusuarioid = secusuarioid;
    }
    public String getNombre_vend() {
        return nombre_vend;
    }
    public void setNombre_vend(String nombre_vend) {
        this.nombre_vend = nombre_vend;
    }
    public String getUsuario_vend() {
        return usuario_vend;
    }
    public void setUsuario_vend(String usuario_vend) {
        this.usuario_vend = usuario_vend;
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
    public BigDecimal getTcscbuzonid() {
        return tcscbuzonid;
    }
    public void setTcscbuzonid(BigDecimal tcscbuzonid) {
        this.tcscbuzonid = tcscbuzonid;
    }
}