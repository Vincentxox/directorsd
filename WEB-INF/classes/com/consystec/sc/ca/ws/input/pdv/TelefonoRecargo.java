package com.consystec.sc.ca.ws.input.pdv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "TelefonoRecargo")
public class TelefonoRecargo {
    @XmlElement
    private String idNumero;
    @XmlElement
    private String numero;
	@XmlElement
	private String orden;
	@XmlElement
	private String estado;
	@XmlElement
    private String estadoPayment;
	@XmlElement
    private String idSolicitud;
	
    public String getIdNumero() {
        return idNumero;
    }
    public void setIdNumero(String idNumero) {
        this.idNumero = idNumero;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getOrden() {
        return orden;
    }
    public void setOrden(String orden) {
        this.orden = orden;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getEstadoPayment() {
        return estadoPayment;
    }
    public void setEstadoPayment(String estadoPayment) {
        this.estadoPayment = estadoPayment;
    }
    public String getIdSolicitud() {
        return idSolicitud;
    }
    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }
}