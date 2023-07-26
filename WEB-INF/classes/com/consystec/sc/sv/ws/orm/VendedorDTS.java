package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class VendedorDTS {
    public static final String N_TABLA = "TC_SC_VEND_DTS";
    public static final String SEQUENCE = "TC_SC_VEND_DTS_SQ.nextval";

    public final static String CAMPO_TCSCVENDDTSID = "tcscvenddtsid";
    public final static String CAMPO_TCSCDTSID = "tcscdtsid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TCSCBODEGAVIRTUALID = "tcscbodegavirtualid";
    public final static String CAMPO_VENDEDOR = "vendedor";
    public final static String CAMPO_USUARIO = "usuario";
    public final static String CAMPO_NOMBRE = "nombre";
    public final static String CAMPO_APELLIDO = "apellido";
    public final static String CAMPO_EMAIL = "email";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_NUM_RECARGA = "num_recarga";
	public final static String CAMPO_PIN = "pin";
	public final static String CAMPO_DTS_FUENTE = "dts_fuente";
	public final static String CAMPO_CANAL = "canal";
	public final static String CAMPO_SUBCANAL = "subcanal";
	public final static String CAMPO_COD_OFICINA = "cod_oficina";
	public final static String CAMPO_COD_VENDEDOR = "cod_vendedor";
	public final static String CAMPO_ENVIO_COD_VEND = "ENVIO_COD_VEND";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	
    private BigDecimal tcscvenddtsid;
    private BigDecimal tcscdtsid;
    private BigDecimal tcsccatpaisid;
    private BigDecimal tcscbodegavirtualid;
    private BigDecimal vendedor;
    private String usuario;
    private String nombre;
    private String apellido;
    private String email;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
	private BigDecimal num_recarga;
    private BigDecimal pin;
    private String dts_fuente;
    private String canal;
    private String subcanal;
    private String cod_oficina;
    private String cod_vendedor;
    private Timestamp modificado_el;
    private String modificado_por;
    private String ENVIO_COD_VEND;
    
    public String getEnvioCodVend() {
    	return ENVIO_COD_VEND;
    }
    
    public void setEnvioCodVend(String ENVIO_COD_VEND) {
    	this.ENVIO_COD_VEND = ENVIO_COD_VEND;
    }
    
    public BigDecimal getTcscvenddtsid() {
        return tcscvenddtsid;
    }
    public void setTcscvenddtsid(BigDecimal tcscvenddtsid) {
        this.tcscvenddtsid = tcscvenddtsid;
    }
    public BigDecimal getTcscdtsid() {
        return tcscdtsid;
    }
    public void setTcscdtsid(BigDecimal tcscdtsid) {
        this.tcscdtsid = tcscdtsid;
    }
    public BigDecimal getTcscbodegavirtualid() {
        return tcscbodegavirtualid;
    }
    public void setTcscbodegavirtualid(BigDecimal tcscbodegavirtualid) {
        this.tcscbodegavirtualid = tcscbodegavirtualid;
    }
    public BigDecimal getVendedor() {
        return vendedor;
    }
    public void setVendedor(BigDecimal vendedor) {
        this.vendedor = vendedor;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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
    public BigDecimal getNum_recarga() {
        return num_recarga;
    }
    public void setNum_recarga(BigDecimal num_recarga) {
        this.num_recarga = num_recarga;
    }
    public BigDecimal getPin() {
        return pin;
    }
    public void setPin(BigDecimal pin) {
        this.pin = pin;
    }
    public String getDts_fuente() {
        return dts_fuente;
    }
    public void setDts_fuente(String dts_fuente) {
        this.dts_fuente = dts_fuente;
    }
    public String getCanal() {
        return canal;
    }
    public void setCanal(String canal) {
        this.canal = canal;
    }
    public String getSubcanal() {
        return subcanal;
    }
    public void setSubcanal(String subcanal) {
        this.subcanal = subcanal;
    }
    public String getCod_oficina() {
        return cod_oficina;
    }
    public void setCod_oficina(String cod_oficina) {
        this.cod_oficina = cod_oficina;
    }
    public String getCod_vendedor() {
        return cod_vendedor;
    }
    public void setCod_vendedor(String cod_vendedor) {
        this.cod_vendedor = cod_vendedor;
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
	public BigDecimal getTcsccatpaisid() {
		return tcsccatpaisid;
	}
	public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
	}
    
    
}