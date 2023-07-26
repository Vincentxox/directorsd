package com.consystec.sc.ca.ws.input.condicionoferta;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputCondicion")
public class InputCondicionOferta {
    @XmlElement
    private String codArea;
	@XmlElement
	private String idCondicion;
    @XmlElement
    private String idOfertaCampania;
    @XmlElement
    private String nombreCampania;
    @XmlElement
    private String nombre;
    @XmlElement
    private String tipoGestion;
    @XmlElement
    private String tipoCondicion;
	@XmlElement
	private String estado;
	@XmlElement
	private String creadoEl;
	@XmlElement
	private String creadoPor;
	@XmlElement
	private String modificadoEl;
	@XmlElement
	private String modificadoPor;
	@XmlElement
	private List<InputDetCondicionOferta> detalle;
	
    public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getIdCondicion() {
        return idCondicion;
    }
    public void setIdCondicion(String idCondicion) {
        this.idCondicion = idCondicion;
    }
    public String getIdOfertaCampania() {
        return idOfertaCampania;
    }
    public void setIdOfertaCampania(String idOfertaCampania) {
        this.idOfertaCampania = idOfertaCampania;
    }
    public String getNombreCampania() {
        return nombreCampania;
    }
    public void setNombreCampania(String nombreCampania) {
        this.nombreCampania = nombreCampania;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getTipoGestion() {
        return tipoGestion;
    }
    public void setTipoGestion(String tipoGestion) {
        this.tipoGestion = tipoGestion;
    }
    public String getTipoCondicion() {
        return tipoCondicion;
    }
    public void setTipoCondicion(String tipoCondicion) {
        this.tipoCondicion = tipoCondicion;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
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
    public List<InputDetCondicionOferta> getDetalle() {
        return detalle;
    }
    public void setDetalle(List<InputDetCondicionOferta> detalle) {
        this.detalle = detalle;
    }	
}