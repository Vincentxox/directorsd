package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PagoDet {
    public static final String N_TABLA = "TC_SC_DET_PAGO";
    public static final String SEQUENCE = "TC_SC_DET_PAGO_SQ.nextval";

    public final static String CAMPO_TCSCDETPAGOID = "tcscdetpagoid";
    public final static String CAMPO_TCSCVENTAID = "tcscventaid";
    public final static String CAMPO_FORMAPAGO = "formapago";
    public final static String CAMPO_MONTO = "monto";
    public final static String CAMPO_BANCO = "banco";
    public final static String CAMPO_NUM_REFERENCIA = "num_referencia";
    public final static String CAMPO_NUM_AUTORIZACION = "num_autorizacion";
    public final static String CAMPO_MARCA_TARJETA = "marca_tarjeta";
    public final static String CAMPO_DIGITOS_TARJETA = "digitos_tarjeta";
    public final static String CAMPO_NUM_CHEQUE = "num_cheque";
    public final static String CAMPO_FECHA_EMISION = "fecha_emision";
    public final static String CAMPO_NUM_RESERVA = "num_reserva";
    public final static String CAMPO_NO_CUENTA = "no_cuenta";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";
    public final static String CAMPO_ID_PAYMENT = "id_payment";
    public final static String CAMPO_ESTADO_REEMBOLSO="estado_reembolso";

    private BigDecimal tcscdetpagoid;
    private BigDecimal tcscventaid;
    private String formapago;
    private BigDecimal monto;
    private String banco;
    private BigDecimal num_referencia;
    private BigDecimal num_autorizacion;
    private String marca_tarjeta;
    private BigDecimal digitos_tarjeta;
    private String num_cheque;
    private Timestamp fecha_emision;
    private BigDecimal num_reserva;
    private String no_cuenta;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    private String idPayment;
    private BigDecimal estadoReembolso;
    
     
    public BigDecimal getEstadoReembolso() {
		return estadoReembolso;
	}
	public void setEstadoReembolso(BigDecimal estadoReembolso) {
		this.estadoReembolso = estadoReembolso;
	}
	public String getIdPayment() {
		return idPayment;
	}
	public void setIdPayment(String idPayment) {
		this.idPayment = idPayment;
	}
	public BigDecimal getTcscdetpagoid() {
        return tcscdetpagoid;
    }
    public void setTcscdetpagoid(BigDecimal tcscdetpagoid) {
        this.tcscdetpagoid = tcscdetpagoid;
    }
    public BigDecimal getTcscventaid() {
        return tcscventaid;
    }
    public void setTcscventaid(BigDecimal tcscventaid) {
        this.tcscventaid = tcscventaid;
    }
    public String getFormapago() {
        return formapago;
    }
    public void setFormapago(String formapago) {
        this.formapago = formapago;
    }
    public BigDecimal getMonto() {
        return monto;
    }
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    public String getBanco() {
        return banco;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }
    public BigDecimal getNum_referencia() {
        return num_referencia;
    }
    public void setNum_referencia(BigDecimal num_referencia) {
        this.num_referencia = num_referencia;
    }
    public BigDecimal getNum_autorizacion() {
        return num_autorizacion;
    }
    public void setNum_autorizacion(BigDecimal num_autorizacion) {
        this.num_autorizacion = num_autorizacion;
    }
    public String getMarca_tarjeta() {
        return marca_tarjeta;
    }
    public void setMarca_tarjeta(String marca_tarjeta) {
        this.marca_tarjeta = marca_tarjeta;
    }
    public BigDecimal getDigitos_tarjeta() {
        return digitos_tarjeta;
    }
    public void setDigitos_tarjeta(BigDecimal digitos_tarjeta) {
        this.digitos_tarjeta = digitos_tarjeta;
    }
    public String getNum_cheque() {
        return num_cheque;
    }
    public void setNum_cheque(String num_cheque) {
        this.num_cheque = num_cheque;
    }
    public Timestamp getFecha_emision() {
        return fecha_emision;
    }
    public void setFecha_emision(Timestamp fecha_emision) {
        this.fecha_emision = fecha_emision;
    }
    public BigDecimal getNum_reserva() {
        return num_reserva;
    }
    public void setNum_reserva(BigDecimal num_reserva) {
        this.num_reserva = num_reserva;
    }
    public String getNo_cuenta() {
        return no_cuenta;
    }
    public void setNo_cuenta(String no_cuenta) {
        this.no_cuenta = no_cuenta;
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