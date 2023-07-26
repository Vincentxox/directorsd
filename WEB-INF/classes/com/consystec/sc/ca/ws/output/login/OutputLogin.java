package com.consystec.sc.ca.ws.output.login;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputLogin")
public class OutputLogin {
	
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private String token;
	@XmlElement
	private String idVendedor;
	@XmlElement
	private String responsable;
	@XmlElement
	private String idResponsable;
	@XmlElement
	private String idBodegaVirtual;
	@XmlElement
	private String idBodegaVendedor;
	@XmlElement
	private String idDTS;
	@XmlElement
	private String nombreDistribuidor;
	@XmlElement
	private String tipo;
	@XmlElement
	private String idTipo;
	@XmlElement
	private String folioManual;
	@XmlElement
	private String longitud;
	@XmlElement
	private String latitud;
	@XmlElement
	private String nombreTipo;

	@XmlElement
	private String fechaCierre;
	@XmlElement
	private String numConvenio;
	@XmlElement
	private String tasaCambio;
	@XmlElement
	private String vendedorAsignado;
	@XmlElement
	private String numRecarga;
	@XmlElement
	private String pin;
	@XmlElement
	private String nivelBuzon;
	@XmlElement
	private String idDispositivo;
	@XmlElement
	private String numIdentificacion;
	@XmlElement
	private String tipoIdentificacion;
	@XmlElement
	private String numTelefono;
	
	@XmlElement
	private String userToken;
	@XmlElement
	private String developToken;
	
	
	
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getDevelopToken() {
		return developToken;
	}
	public void setDevelopToken(String developToken) {
		this.developToken = developToken;
	}
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getIdVendedor() {
		return idVendedor;
	}
	public void setIdVendedor(String idVendedor) {
		this.idVendedor = idVendedor;
	}
	public String getIdBodegaVirtual() {
		return idBodegaVirtual;
	}
	public void setIdBodegaVirtual(String idBodegaVirtual) {
		this.idBodegaVirtual = idBodegaVirtual;
	}
	public String getIdBodegaVendedor() {
		return idBodegaVendedor;
	}
	public void setIdBodegaVendedor(String idBodegaVendedor) {
		this.idBodegaVendedor = idBodegaVendedor;
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
	public String getNombreDistribuidor() {
		return nombreDistribuidor;
	}
	public void setNombreDistribuidor(String nombreDistribuidor) {
		this.nombreDistribuidor = nombreDistribuidor;
	}
	public String getIdTipo() {
		return idTipo;
	}
	public void setIdTipo(String idTipo) {
		this.idTipo = idTipo;
	}
	public String getFolioManual() {
		return folioManual;
	}
	public void setFolioManual(String folioManual) {
		this.folioManual = folioManual;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	public String getIdResponsable() {
		return idResponsable;
	}
	public void setIdResponsable(String idResponsable) {
		this.idResponsable = idResponsable;
	}
	public String getNombreTipo() {
		return nombreTipo;
	}
	public void setNombreTipo(String nombreTipo) {
		this.nombreTipo = nombreTipo;
	}
	public String getNumRecarga() {
		return numRecarga;
	}
	public void setNumRecarga(String numRecarga) {
		this.numRecarga = numRecarga;
	}
	public String getFechaCierre() {
		return fechaCierre;
	}
	public void setFechaCierre(String fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
	public String getNumConvenio() {
		return numConvenio;
	}
	public void setNumConvenio(String numConvenio) {
		this.numConvenio = numConvenio;
	}
	public String getTasaCambio() {
		return tasaCambio;
	}
	public void setTasaCambio(String tasaCambio) {
		this.tasaCambio = tasaCambio;
	}
	public String getVendedorAsignado() {
		return vendedorAsignado;
	}
	public void setVendedorAsignado(String vendedorAsignado) {
		this.vendedorAsignado = vendedorAsignado;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getNivelBuzon() {
		return nivelBuzon;
	}
	public void setNivelBuzon(String nivelBuzon) {
		this.nivelBuzon = nivelBuzon;
	}
	public String getIdDispositivo() {
		return idDispositivo;
	}
	public void setIdDispositivo(String idDispositivo) {
		this.idDispositivo = idDispositivo;
	}
	public String getNumIdentificacion() {
		return numIdentificacion;
	}
	public void setNumIdentificacion(String numIdentificacion) {
		this.numIdentificacion = numIdentificacion;
	}
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}
	public String getNumTelefono() {
		return numTelefono;
	}
	public void setNumTelefono(String numTelefono) {
		this.numTelefono = numTelefono;
	}
	
	
}
