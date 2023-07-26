package com.consystec.sc.ca.ws.input.condicion;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputCondicionPrincipal")
public class InputCondicionPrincipal {
    @XmlElement
    private String token;
    @XmlElement
    private String codArea;
	@XmlElement
	private String usuario;
	@XmlElement
    private String idCondicion;
	@XmlElement
    private String nombre;
	@XmlElement
	private String idOfertaCampania;
	@XmlElement
    private String tipoGestion;
	@XmlElement
    private String tipo;
	@XmlElement
    private String idArticulo;
    @XmlElement
    private String creadoEl;
    @XmlElement
    private String creadoPor;
    @XmlElement
    private String modificadoEl;
    @XmlElement
    private String modificadoPor;
	@XmlElement
	private List<InputCondicion> condiciones;
	
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
    public String getIdCondicion() {
        return idCondicion;
    }
    public void setIdCondicion(String idCondicion) {
        this.idCondicion = idCondicion;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getIdOfertaCampania() {
        return idOfertaCampania;
    }
    public void setIdOfertaCampania(String idOfertaCampania) {
        this.idOfertaCampania = idOfertaCampania;
    }
    public String getTipoGestion() {
        return tipoGestion;
    }
    public void setTipoGestion(String tipoGestion) {
        this.tipoGestion = tipoGestion;
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
    public String getCreadoEl() {
        return creadoEl;
    }
    public void setCreadoEl(String creadoEl) {
        this.creadoEl = creadoEl;
    }
    public String getCreadoPor() {
        return creadoPor;
    }
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }
    public String getModificadoEl() {
        return modificadoEl;
    }
    public void setModificadoEl(String modificadoEl) {
        this.modificadoEl = modificadoEl;
    }
    public String getModificadoPor() {
        return modificadoPor;
    }
    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }
    public List<InputCondicion> getCondiciones() {
        return condiciones;
    }
    public void setCondiciones(List<InputCondicion> condiciones) {
        this.condiciones = condiciones;
    }	
}