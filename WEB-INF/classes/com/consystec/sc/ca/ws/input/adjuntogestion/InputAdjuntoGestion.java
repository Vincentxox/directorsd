package com.consystec.sc.ca.ws.input.adjuntogestion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputAdjuntoGestion")
public class InputAdjuntoGestion {
    @XmlElement
    private String token;
    @XmlElement
    private String usuario;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String codArea;
    @XmlElement
    private String idAdjunto;
    @XmlElement
    private String idGestion;
    @XmlElement
    private String gestion;
    @XmlElement
    private String adjunto;
    @XmlElement
    private byte[] archivo;
    @XmlElement
    private String nombreArchivo;
    @XmlElement
    private String tipoArchivo;
    @XmlElement
    private String extension;
    @XmlElement
    private String tipoDocumento;
    @XmlElement
    private String idPortaMovil;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
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
    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getIdAdjunto() {
        return idAdjunto;
    }
    public void setIdAdjunto(String idAdjunto) {
        this.idAdjunto = idAdjunto;
    }
    public String getIdGestion() {
        return idGestion;
    }
    public void setIdGestion(String idGestion) {
        this.idGestion = idGestion;
    }
    public String getGestion() {
        return gestion;
    }
    public void setGestion(String gestion) {
        this.gestion = gestion;
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
    public String getNombreArchivo() {
        return nombreArchivo;
    }
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    public String getTipoArchivo() {
        return tipoArchivo;
    }
    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }
    public String getExtension() {
        return extension;
    }
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public String getTipoDocumento() {
        return tipoDocumento;
    }
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
	public String getIdPortaMovil() {
		return idPortaMovil;
	}
	public void setIdPortaMovil(String idPortaMovil) {
		this.idPortaMovil = idPortaMovil;
	}
    
}