package com.consystec.sc.ca.ws.input.pdv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "inputConsultaPDV")
public class InputConsultaPDV {
    @XmlElement
    private String token;
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String idDTS;
    @XmlElement
    private String idPDV;
    @XmlElement
    private String nombre;
    @XmlElement
    private String idVendedor;
    @XmlElement
    private String idRuta;
    @XmlElement
    private String numRecarga;
    @XmlElement
    private String ruc_nit;
    @XmlElement
    private String departamento;
    @XmlElement
    private String municipio;
    @XmlElement
    private String distrito;
    @XmlElement
    private String categoria;
    @XmlElement
    private String estado;
    @XmlElement
    private String min;
    @XmlElement
    private String max;
    @XmlElement
    private String mostrarDatosVendedor;
    @XmlElement
    private String mostrarNumerosRecarga;
    @XmlElement
    private String mostrarDiasVisita;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getCodDispositivo() {
        return codDispositivo;
    }
    public void setCodDispositivo(String codDispositivo) {
        this.codDispositivo = codDispositivo;
    }
    public String getIdDTS() {
        return idDTS;
    }
    public void setIdDTS(String idDTS) {
        this.idDTS = idDTS;
    }
    public String getIdPDV() {
        return idPDV;
    }
    public void setIdPDV(String idPDV) {
        this.idPDV = idPDV;
    }
    public String getIdVendedor() {
        return idVendedor;
    }
    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }
    public String getIdRuta() {
        return idRuta;
    }
    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }
    public String getNumRecarga() {
        return numRecarga;
    }
    public void setNumRecarga(String numRecarga) {
        this.numRecarga = numRecarga;
    }
    public String getDepartamento() {
        return departamento;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    public String getMunicipio() {
        return municipio;
    }
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
    public String getDistrito() {
        return distrito;
    }
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getMin() {
        return min;
    }
    public void setMin(String min) {
        this.min = min;
    }
    public String getMax() {
        return max;
    }
    public void setMax(String max) {
        this.max = max;
    }
    public String getMostrarDatosVendedor() {
        return mostrarDatosVendedor;
    }
    public void setMostrarDatosVendedor(String mostrarDatosVendedor) {
        this.mostrarDatosVendedor = mostrarDatosVendedor;
    }
    public String getMostrarNumerosRecarga() {
        return mostrarNumerosRecarga;
    }
    public void setMostrarNumerosRecarga(String mostrarNumerosRecarga) {
        this.mostrarNumerosRecarga = mostrarNumerosRecarga;
    }
    public String getMostrarDiasVisita() {
        return mostrarDiasVisita;
    }
    public void setMostrarDiasVisita(String mostrarDiasVisita) {
        this.mostrarDiasVisita = mostrarDiasVisita;
    }
	public String getRuc_nit() {
		return ruc_nit;
	}
	public void setRuc_nit(String ruc_nit) {
		this.ruc_nit = ruc_nit;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
    
}