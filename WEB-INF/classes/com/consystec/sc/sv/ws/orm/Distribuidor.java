package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Distribuidor {
    public static final String N_TABLA = "TC_SC_DTS";
    public static final String SEQUENCE = "TC_SC_DTS_SQ.nextval";

    public final static String CAMPO_TC_SC_DTS_ID = "tcscdtsid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TIPO = "tipo";
    public final static String CAMPO_ID_FICHA_CLIENTE = "idfichacliente";
    public final static String CAMPO_TCSCBODEGAVIRTUALID = "tcscbodegavirtualid";
    public final static String CAMPO_NOMBRES = "nombres";
    public final static String CAMPO_NUMERO = "numero";
    public final static String CAMPO_EMAIL = "email";
    public final static String CAMPO_ADMINISTRADOR = "administrador";
    public final static String CAMPO_PAGO_AUTOMATICO = "pago_automatico";
    public final static String CAMPO_CANAL = "canal";
    public final static String CAMPO_NUM_CONVENIO = "num_convenio";
    public final static String CAMPO_COD_CLIENTE = "cod_cliente";
    public final static String CAMPO_COD_CUENTA = "cod_cuenta";
    public final static String CAMPO_RESULTADO_SCL = "resultado_scl";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";

    private BigDecimal tcScDtsId;
    private BigDecimal tcScCatPaisId;
    private BigDecimal tcscbodegavirtualid;
    private String tipo;
    private BigDecimal idFichaCliente;
    private String nombres;
    private BigDecimal numero;
    private String email;
    private String administrador;
    private BigDecimal pagoAutomatico;
    private String canal;
    private String num_convenio;
    private BigDecimal cod_cliente;
    private BigDecimal cod_cuenta;
    private BigDecimal resultado_scl;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    
    public BigDecimal getTcScDtsId() {
        return tcScDtsId;
    }
    public void setTcScDtsId(BigDecimal tcScDtsId) {
        this.tcScDtsId = tcScDtsId;
    }
    public BigDecimal getTcScCatPaisId() {
        return tcScCatPaisId;
    }
    public void setTcScCatPaisId(BigDecimal tcScCatPaisId) {
        this.tcScCatPaisId = tcScCatPaisId;
    }
    public BigDecimal getTcscbodegavirtualid() {
        return tcscbodegavirtualid;
    }
    public void setTcscbodegavirtualid(BigDecimal tcscbodegavirtualid) {
        this.tcscbodegavirtualid = tcscbodegavirtualid;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public BigDecimal getIdFichaCliente() {
        return idFichaCliente;
    }
    public void setIdFichaCliente(BigDecimal idFichaCliente) {
        this.idFichaCliente = idFichaCliente;
    }
    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    public BigDecimal getNumero() {
        return numero;
    }
    public void setNumero(BigDecimal numero) {
        this.numero = numero;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAdministrador() {
        return administrador;
    }
    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }
    public BigDecimal getPagoAutomatico() {
        return pagoAutomatico;
    }
    public void setPagoAutomatico(BigDecimal pagoAutomatico) {
        this.pagoAutomatico = pagoAutomatico;
    }
    public String getCanal() {
        return canal;
    }
    public void setCanal(String canal) {
        this.canal = canal;
    }
    public String getNum_convenio() {
        return num_convenio;
    }
    public void setNum_convenio(String num_convenio) {
        this.num_convenio = num_convenio;
    }
    public BigDecimal getCod_cliente() {
        return cod_cliente;
    }
    public void setCod_cliente(BigDecimal cod_cliente) {
        this.cod_cliente = cod_cliente;
    }
    public BigDecimal getCod_cuenta() {
        return cod_cuenta;
    }
    public void setCod_cuenta(BigDecimal cod_cuenta) {
        this.cod_cuenta = cod_cuenta;
    }
    public BigDecimal getResultado_scl() {
        return resultado_scl;
    }
    public void setResultado_scl(BigDecimal resultado_scl) {
        this.resultado_scl = resultado_scl;
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