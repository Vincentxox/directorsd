package com.consystec.sc.ca.ws.input.dts;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "inputDistribuidor")
public class InputDistribuidor {
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String idDTS;
    @XmlElement
    private String tipo;
    @XmlElement
    private String idfichacliente;
    @XmlElement
    private String idBodegaVirtual;
    @XmlElement
    private String idAlmacenBod;
    @XmlElement
    private String idBodegaSCL;
    @XmlElement
    private String nombres;
    @XmlElement
    private String apellidos;
    @XmlElement
    private String numero;
    @XmlElement
    private String email;
    @XmlElement
    private String administrador;
    @XmlElement
    private String pagoautomatico;
    @XmlElement
    private String canal;
    @XmlElement
    private String numConvenio;
    @XmlElement
    private String codCliente;
    @XmlElement
    private String codCuenta;
    @XmlElement
    private String resultadoSCL;
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
    public String getIdDTS() {
        return idDTS;
    }
    public void setIdDTS(String idDTS) {
        this.idDTS = idDTS;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getIdfichacliente() {
        return idfichacliente;
    }
    public void setIdfichacliente(String idfichacliente) {
        this.idfichacliente = idfichacliente;
    }
    public String getIdBodegaVirtual() {
        return idBodegaVirtual;
    }
    public void setIdBodegaVirtual(String idBodegaVirtual) {
        this.idBodegaVirtual = idBodegaVirtual;
    }
    public String getIdAlmacenBod() {
        return idAlmacenBod;
    }
    public void setIdAlmacenBod(String idAlmacenBod) {
        this.idAlmacenBod = idAlmacenBod;
    }
    public String getIdBodegaSCL() {
        return idBodegaSCL;
    }
    public void setIdBodegaSCL(String idBodegaSCL) {
        this.idBodegaSCL = idBodegaSCL;
    }
    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
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
    public String getPagoAutomatico() {
        return pagoautomatico;
    }
    public void setPagoAutomatico(String pagoautomatico) {
        this.pagoautomatico = pagoautomatico;
    }
    public String getCanal() {
        return canal;
    }
    public void setCanal(String canal) {
        this.canal = canal;
    }
    public String getNumConvenio() {
        return numConvenio;
    }
    public void setNumConvenio(String numConvenio) {
        this.numConvenio = numConvenio;
    }
    public String getCodCliente() {
        return codCliente;
    }
    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }
    public String getCodCuenta() {
        return codCuenta;
    }
    public void setCodCuenta(String codCuenta) {
        this.codCuenta = codCuenta;
    }
    public String getResultadoSCL() {
        return resultadoSCL;
    }
    public void setResultadoSCL(String resultadoSCL) {
        this.resultadoSCL = resultadoSCL;
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
}