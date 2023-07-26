package com.consystec.sc.ca.ws.input.condicion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputDetCondicionCampania")
public class InputDetCondicionCampania {
    @XmlElement
    private String codArea;
	@XmlElement
	private String idDetCondicion;
	@XmlElement
	private String idCondicion;
	@XmlElement
	private String tipo;
	@XmlElement
	private String idArticulo;
	@XmlElement
    private String nombreArticulo;
	@XmlElement
    private String tecnologia;
	@XmlElement
    private String tipoInv;
	@XmlElement
	private String montoInicial;
	@XmlElement
	private String montoFinal;
	@XmlElement
	private String estado;
	@XmlElement
	private String creadoPor;
	@XmlElement
	private String creadoEl;
	@XmlElement
	private String modificadoPor;
	@XmlElement
	private String modificadoEl;
	
    public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getIdDetCondicion() {
        return idDetCondicion;
    }
    public void setIdDetCondicion(String idDetCondicion) {
        this.idDetCondicion = idDetCondicion;
    }
    public String getIdCondicion() {
        return idCondicion;
    }
    public void setIdCondicion(String idCondicion) {
        this.idCondicion = idCondicion;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getIdArticulo() {
        return idArticulo;
    }
    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }
    public String getNombreArticulo() {
        return nombreArticulo;
    }
    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }
    public String getTecnologia() {
        return tecnologia;
    }
    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }
    public String getTipoInv() {
        return tipoInv;
    }
    public void setTipoInv(String tipoInv) {
        this.tipoInv = tipoInv;
    }
    public String getMontoInicial() {
        return montoInicial;
    }
    public void setMontoInicial(String montoInicial) {
        this.montoInicial = montoInicial;
    }
    public String getMontoFinal() {
        return montoFinal;
    }
    public void setMontoFinal(String montoFinal) {
        this.montoFinal = montoFinal;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getCreadoPor() {
        return creadoPor;
    }
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }
    public String getCreadoEl() {
        return creadoEl;
    }
    public void setCreadoEl(String creadoEl) {
        this.creadoEl = creadoEl;
    }
    public String getModificadoPor() {
        return modificadoPor;
    }
    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }
    public String getModificadoEl() {
        return modificadoEl;
    }
    public void setModificadoEl(String modificadoEl) {
        this.modificadoEl = modificadoEl;
    }
}