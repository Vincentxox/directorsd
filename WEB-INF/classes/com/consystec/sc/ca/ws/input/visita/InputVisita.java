package com.consystec.sc.ca.ws.input.visita;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputVisita")
public class InputVisita {
	@XmlElement
	private String token;
	@XmlElement
	private String codArea;
	@XmlElement
	private String usuario;
	@XmlElement
	private String codDispositivo;
	@XmlElement
	private String idVisita;
	@XmlElement
	private String idVendedor;
	@XmlElement
	private String nombreVendedor;
	@XmlElement
	private String idJornada;
	@XmlElement
	private String fecha;
	@XmlElement
	private String idPuntoVenta;
	@XmlElement
	private String nombrePuntoVenta;
	@XmlElement
	private String latitud;
	@XmlElement
	private String longitud;
	@XmlElement
	private String gestion;
    @XmlElement
    private String idVenta;
    @XmlElement
    private String serie;
	@XmlElement
    private String causa;
	@XmlElement
	private String folio;
	@XmlElement
	private String observaciones;
	@XmlElement
	private String creado_el;
	@XmlElement
	private String creado_por;
	@XmlElement
	private String modificado_el;
	@XmlElement
	private String modificado_por;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getIdVendedor() {
		return idVendedor;
	}
	public void setIdVendedor(String idVendedor) {
		this.idVendedor = idVendedor;
	}
	public String getIdJornada() {
		return idJornada;
	}
	public void setIdJornada(String idJornada) {
		this.idJornada = idJornada;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getIdPuntoVenta() {
		return idPuntoVenta;
	}
	public void setIdPuntoVenta(String idPuntoVenta) {
		this.idPuntoVenta = idPuntoVenta;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	public String getGestion() {
		return gestion;
	}
	public void setGestion(String gestion) {
		this.gestion = gestion;
	}
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getIdVisita() {
		return idVisita;
	}
	public void setIdVisita(String idVisita) {
		this.idVisita = idVisita;
	}
	public String getNombreVendedor() {
		return nombreVendedor;
	}
	public void setNombreVendedor(String nombreVendedor) {
		this.nombreVendedor = nombreVendedor;
	}
	public String getNombrePuntoVenta() {
		return nombrePuntoVenta;
	}
	public void setNombrePuntoVenta(String nombrePuntoVenta) {
		this.nombrePuntoVenta = nombrePuntoVenta;
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
	public String getCodDispositivo() {
		return codDispositivo;
	}
	public void setCodDispositivo(String codDispositivo) {
		this.codDispositivo = codDispositivo;
	}
    public String getCausa() {
        return causa;
    }
    public void setCausa(String causa) {
        this.causa = causa;
    }
	public String getIdVenta() {
		return idVenta;
	}
	public void setIdVenta(String idVenta) {
		this.idVenta = idVenta;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
    
}