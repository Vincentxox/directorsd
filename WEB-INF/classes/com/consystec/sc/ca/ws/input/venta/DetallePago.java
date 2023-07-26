package com.consystec.sc.ca.ws.input.venta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DetallePago")
public class DetallePago {
    @XmlElement
    private String codArea;
	@XmlElement
	private String formaPago;
	@XmlElement
    private String banco;
	@XmlElement
	private String monto;
	@XmlElement
	private String numAutorizacion;
	@XmlElement
    private String marcaTarjeta;
	@XmlElement
    private String digitosTarjeta;
	@XmlElement
    private String numeroCheque;
	@XmlElement
    private String fechaEmision;
	@XmlElement
    private String numeroReserva;
	@XmlElement
    private String cuentaCliente;
    @XmlElement
    private String estado;
    @XmlElement
    private String creado_el;
    @XmlElement
    private String creado_por;
    @XmlElement
    private String modificado_el;
    @XmlElement
    private String modificado_por;
    @XmlElement
    private String idJornada;
    @XmlElement
    private String idVendedor;
    @XmlElement
    private String nombreVendedor;
    @XmlElement
    private String idTipo;
    @XmlElement
    private String tipo;
    @XmlElement
    private String nombreTipo;
    @XmlElement
    private String idPayment;
    @XmlElement
    private String estadoReembolso;
    @XmlElement
    private String estadoSincronizacion;
        

    public String getEstadoSincronizacion() {
		return estadoSincronizacion;
	}
	public void setEstadoSincronizacion(String estadoSincronizacion) {
		this.estadoSincronizacion = estadoSincronizacion;
	}
	public String getIdPayment() {
		return idPayment;
	}
	public void setIdPayment(String idPayment) {
		this.idPayment = idPayment;
	}
	public String getEstadoReembolso() {
		return estadoReembolso;
	}
	public void setEstadoReembolso(String estadoReembolso) {
		this.estadoReembolso = estadoReembolso;
	}
	public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getFormaPago() {
        return formaPago;
    }
    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }
    public String getBanco() {
        return banco;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }
    public String getMonto() {
        return monto;
    }
    public void setMonto(String monto) {
        this.monto = monto;
    }
    public String getNumAutorizacion() {
        return numAutorizacion;
    }
    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }
    public String getMarcaTarjeta() {
        return marcaTarjeta;
    }
    public void setMarcaTarjeta(String marcaTarjeta) {
        this.marcaTarjeta = marcaTarjeta;
    }
    public String getDigitosTarjeta() {
        return digitosTarjeta;
    }
    public void setDigitosTarjeta(String digitosTarjeta) {
        this.digitosTarjeta = digitosTarjeta;
    }
    public String getNumeroCheque() {
        return numeroCheque;
    }
    public void setNumeroCheque(String numeroCheque) {
        this.numeroCheque = numeroCheque;
    }
    public String getFechaEmision() {
        return fechaEmision;
    }
    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    public String getNumeroReserva() {
        return numeroReserva;
    }
    public void setNumeroReserva(String numeroReserva) {
        this.numeroReserva = numeroReserva;
    }
    public String getCuentaCliente() {
        return cuentaCliente;
    }
    public void setCuentaCliente(String cuentaCliente) {
        this.cuentaCliente = cuentaCliente;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getCreado_el() {
        return creado_el;
    }
    public void setCreado_el(String creado_el) {
        this.creado_el = creado_el;
    }
    public String getCreado_por() {
        return creado_por;
    }
    public void setCreado_por(String creado_por) {
        this.creado_por = creado_por;
    }
    public String getModificado_el() {
        return modificado_el;
    }
    public void setModificado_el(String modificado_el) {
        this.modificado_el = modificado_el;
    }
    public String getModificado_por() {
        return modificado_por;
    }
    public void setModificado_por(String modificado_por) {
        this.modificado_por = modificado_por;
    }
    public String getIdJornada() {
        return idJornada;
    }
    public void setIdJornada(String idJornada) {
        this.idJornada = idJornada;
    }
    public String getIdVendedor() {
        return idVendedor;
    }
    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }
    public String getNombreVendedor() {
        return nombreVendedor;
    }
    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }
    public String getIdTipo() {
        return idTipo;
    }
    public void setIdTipo(String idTipo) {
        this.idTipo = idTipo;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getNombreTipo() {
        return nombreTipo;
    }
    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }
}