package com.consystec.sc.ca.ws.input.portabilidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputCargaAdjuntoPorta")
public class InputCargaAdjuntoPorta {

	private String codArea;
	@XmlElement
	private String usuario;
	@XmlElement
	private String token;
	@XmlElement
	private String codDispositivo;
	@XmlElement
	private String idPortabilidad;
	@XmlElement
	private String idPortaMovil;
	@XmlElement
	private String nombreArchivo;
	@XmlElement
	private String extension;
	@XmlElement
	private String tipoArchivo;
	@XmlElement
	private String idAttachment;
	@XmlElement
	private String adjunto;
	@XmlElement
	private byte[] archivo;
	@XmlElement
	private String tcscadjuntoid;

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

	public String getIdPortabilidad() {
		return idPortabilidad;
	}

	public void setIdPortabilidad(String idPortabilidad) {
		this.idPortabilidad = idPortabilidad;
	}

	public String getIdPortaMovil() {
		return idPortaMovil;
	}

	public void setIdPortaMovil(String idPortaMovil) {
		this.idPortaMovil = idPortaMovil;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getTipoArchivo() {
		return tipoArchivo;
	}

	public void setTipoArchivo(String tipoArchivo) {
		this.tipoArchivo = tipoArchivo;
	}

	public String getIdAttachment() {
		return idAttachment;
	}

	public void setIdAttachment(String idAttachment) {
		this.idAttachment = idAttachment;
	}

	public String getAdjunto() {
		return adjunto;
	}

	public void setAdjunto(String adjunto) {
		this.adjunto = adjunto;
	}

	public byte[] getArchivo() {
		return archivo;
	}

	public void setArchivo(byte[] archivo) {
		this.archivo = archivo;
	}

	public String getTcscadjuntoid() {
		return tcscadjuntoid;
	}

	public void setTcscadjuntoid(String tcscadjuntoid) {
		this.tcscadjuntoid = tcscadjuntoid;
	}

}
