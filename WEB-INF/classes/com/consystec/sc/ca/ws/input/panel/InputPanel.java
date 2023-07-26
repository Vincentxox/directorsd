package com.consystec.sc.ca.ws.input.panel;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.pdv.TelefonoRecargo;
import com.consystec.sc.ca.ws.input.ruta.InputVendedor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputPanel")
public class InputPanel {
    @XmlElement
    private String token;
    @XmlElement
    private String codDispositivo;
	@XmlElement
	private String codArea;
	@XmlElement
	private String usuario;
	@XmlElement
	private String idPanel;
	@XmlElement
    private String nombre;
	@XmlElement
	private String idDistribuidor;
	@XmlElement
	private String idBodegaVirtual;
	@XmlElement
    private String nombreDTS;
	@XmlElement
    private String tipoDTS;
	@XmlElement
	private String recargas;
	@XmlElement
	private TelefonoRecargo[] telefonoRecarga;
	@XmlElement
    private String[] vendedor;
	@XmlElement
    private String responsable;
	@XmlElement
    private String nombreResponsable;
	@XmlElement
    private String idBodResponsable;
	@XmlElement
    private String nombreBodResponsable;
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
    private List<InputVendedor> datosVendedor;
    @XmlElement
    private List<TelefonoRecargo> numerosRecarga;
    
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
    public String getIdPanel() {
        return idPanel;
    }
    public void setIdPanel(String idPanel) {
        this.idPanel = idPanel;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getIdDistribuidor() {
        return idDistribuidor;
    }
    public void setIdDistribuidor(String idDistribuidor) {
        this.idDistribuidor = idDistribuidor;
    }
    public String getIdBodegaVirtual() {
        return idBodegaVirtual;
    }
    public void setIdBodegaVirtual(String idBodegaVirtual) {
        this.idBodegaVirtual = idBodegaVirtual;
    }
    public String getNombreDTS() {
        return nombreDTS;
    }
    public void setNombreDTS(String nombreDTS) {
        this.nombreDTS = nombreDTS;
    }
    public String getTipoDTS() {
        return tipoDTS;
    }
    public void setTipoDTS(String tipoDTS) {
        this.tipoDTS = tipoDTS;
    }
    public String getRecargas() {
        return recargas;
    }
    public void setRecargas(String recargas) {
        this.recargas = recargas;
    }
    public TelefonoRecargo[] getTelefonoRecarga() {
        return telefonoRecarga;
    }
    public void setTelefonoRecarga(TelefonoRecargo[] telefonoRecarga) {
        this.telefonoRecarga = telefonoRecarga;
    }
    public String[] getVendedor() {
        return vendedor;
    }
    public void setVendedor(String[] vendedor) {
        this.vendedor = vendedor;
    }
    public String getResponsable() {
        return responsable;
    }
    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
    public String getNombreResponsable() {
        return nombreResponsable;
    }
    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }
    public String getIdBodResponsable() {
        return idBodResponsable;
    }
    public void setIdBodResponsable(String idBodResponsable) {
        this.idBodResponsable = idBodResponsable;
    }
    public String getNombreBodResponsable() {
        return nombreBodResponsable;
    }
    public void setNombreBodResponsable(String nombreBodResponsable) {
        this.nombreBodResponsable = nombreBodResponsable;
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
    public List<InputVendedor> getDatosVendedor() {
        return datosVendedor;
    }
    public void setDatosVendedor(List<InputVendedor> datosVendedor) {
        this.datosVendedor = datosVendedor;
    }
    public List<TelefonoRecargo> getNumerosRecarga() {
        return numerosRecarga;
    }
    public void setNumerosRecarga(List<TelefonoRecargo> numerosRecarga) {
        this.numerosRecarga = numerosRecarga;
    }
}