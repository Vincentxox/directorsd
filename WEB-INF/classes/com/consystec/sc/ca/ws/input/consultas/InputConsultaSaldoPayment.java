package com.consystec.sc.ca.ws.input.consultas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputConsultaSaldoPayment")
public class InputConsultaSaldoPayment {
	@XmlElement
	private String codArea;
	@XmlElement
	private String token;
	@XmlElement
	private String codDispositivo;
	@XmlElement
    private String usuario;
    @XmlElement
    private String numRecarga;
    @XmlElement
    private String pin;

    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getCodDispositivo() {
        return codDispositivo;
    }
    public void setCodDispositivo(String codDispositivo) {
        this.codDispositivo = codDispositivo;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getNumRecarga() {
        return numRecarga;
    }
    public void setNumRecarga(String numRecarga) {
        this.numRecarga = numRecarga;
    }
    public String getPin() {
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }
}