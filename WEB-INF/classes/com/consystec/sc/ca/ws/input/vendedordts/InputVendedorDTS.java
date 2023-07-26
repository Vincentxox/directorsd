package com.consystec.sc.ca.ws.input.vendedordts;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputVendedorDTS")
public class InputVendedorDTS {
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String idVendedorDTS;
    @XmlElement
    private String idDistribuidor;
    @XmlElement
    private String nombreDistribuidor;
    @XmlElement
    private String idBodegaVirtual;
    @XmlElement
    private String nombreBodegaVirtual;
    @XmlElement
    private String idBodegaVendedor;
    @XmlElement
    private String idVendedor;
    @XmlElement
    private String usuarioVendedor;
    @XmlElement
    private String nombres;
    @XmlElement
    private String apellidos;
    @XmlElement
    private String canal;
    @XmlElement
    private String subcanal;
    @XmlElement
    private String numeroRecarga;
    @XmlElement
    private String pin;
    @XmlElement
    private String dtsFuente;
    @XmlElement
    private String email;
    @XmlElement
    private String estado;
    @XmlElement
    private String tipoAsociado;
    @XmlElement
    private String nombreAsociado;
    @XmlElement
    private String soloDisponibles;
    @XmlElement
    private String tienePDV;
    @XmlElement
    private String codOficina;
    @XmlElement
    private String codVendedor;
    @XmlElement
    private String creado_el;
    @XmlElement
    private String creado_por;
    @XmlElement
    private String modificado_el;
    @XmlElement
    private String modificado_por;
    @XmlElement
    private String envio_cod_vend;
    
    public String getEnvioCodVend() {
    	return envio_cod_vend;
    }
    
    public void setEnvioCodVend(String ecv) {
    	this.envio_cod_vend = ecv;
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
    public String getIdVendedorDTS() {
        return idVendedorDTS;
    }
    public void setIdVendedorDTS(String idVendedorDTS) {
        this.idVendedorDTS = idVendedorDTS;
    }
    public String getIdDistribuidor() {
        return idDistribuidor;
    }
    public void setIdDistribuidor(String idDistribuidor) {
        this.idDistribuidor = idDistribuidor;
    }
    public String getNombreDistribuidor() {
        return nombreDistribuidor;
    }
    public void setNombreDistribuidor(String nombreDistribuidor) {
        this.nombreDistribuidor = nombreDistribuidor;
    }
    public String getIdBodegaVirtual() {
        return idBodegaVirtual;
    }
    public void setIdBodegaVirtual(String idBodegaVirtual) {
        this.idBodegaVirtual = idBodegaVirtual;
    }
    public String getNombreBodegaVirtual() {
        return nombreBodegaVirtual;
    }
    public void setNombreBodegaVirtual(String nombreBodegaVirtual) {
        this.nombreBodegaVirtual = nombreBodegaVirtual;
    }
    public String getIdBodegaVendedor() {
        return idBodegaVendedor;
    }
    public void setIdBodegaVendedor(String idBodegaVendedor) {
        this.idBodegaVendedor = idBodegaVendedor;
    }
    public String getIdVendedor() {
        return idVendedor;
    }
    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }
    public String getUsuarioVendedor() {
        return usuarioVendedor;
    }
    public void setUsuarioVendedor(String usuarioVendedor) {
        this.usuarioVendedor = usuarioVendedor;
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
    public String getNumeroRecarga() {
        return numeroRecarga;
    }
    public void setNumeroRecarga(String numeroRecarga) {
        this.numeroRecarga = numeroRecarga;
    }
    public String getPin() {
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }
    public String getDtsFuente() {
        return dtsFuente;
    }
    public void setDtsFuente(String dtsFuente) {
        this.dtsFuente = dtsFuente;
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
    public String getTipoAsociado() {
        return tipoAsociado;
    }
    public void setTipoAsociado(String tipoAsociado) {
        this.tipoAsociado = tipoAsociado;
    }
    public String getNombreAsociado() {
        return nombreAsociado;
    }
    public void setNombreAsociado(String nombreAsociado) {
        this.nombreAsociado = nombreAsociado;
    }
    public String getSoloDisponibles() {
        return soloDisponibles;
    }
    public void setSoloDisponibles(String soloDisponibles) {
        this.soloDisponibles = soloDisponibles;
    }
    public String getTienePDV() {
        return tienePDV;
    }
    public void setTienePDV(String tienePDV) {
        this.tienePDV = tienePDV;
    }
    public String getCodOficina() {
        return codOficina;
    }
    public void setCodOficina(String codOficina) {
        this.codOficina = codOficina;
    }
    public String getCodVendedor() {
        return codVendedor;
    }
    public void setCodVendedor(String codVendedor) {
        this.codVendedor = codVendedor;
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