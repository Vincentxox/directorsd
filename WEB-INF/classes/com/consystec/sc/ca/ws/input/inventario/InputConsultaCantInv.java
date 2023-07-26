package com.consystec.sc.ca.ws.input.inventario;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputConsultaCantInv")
public class InputConsultaCantInv {
    @XmlElement
    private String token;
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String idBodega;
    @XmlElement
    private String cantInv;
    @XmlElement
    private String cantTotalInv;
    @XmlElement
    private String idArticulo;
    @XmlElement
    private String serie;
    @XmlElement
    private String descripcion;
    @XmlElement
    private String tipoInv;
    @XmlElement
    private String estado;
    @XmlElement
    private String numTraspasoScl;
    
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
    public String getIdBodega() {
        return idBodega;
    }
    public void setIdBodega(String idBodega) {
        this.idBodega = idBodega;
    }
    public String getCantInv() {
        return cantInv;
    }
    public void setCantInv(String cantInv) {
        this.cantInv = cantInv;
    }
    public String getCantTotalInv() {
        return cantTotalInv;
    }
    public void setCantTotalInv(String cantTotalInv) {
        this.cantTotalInv = cantTotalInv;
    }
    public String getIdArticulo() {
        return idArticulo;
    }
    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }
    public String getSerie() {
        return serie;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getTipoInv() {
        return tipoInv;
    }
    public void setTipoInv(String tipoInv) {
        this.tipoInv = tipoInv;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
	public String getCodDispositivo() {
		return codDispositivo;
	}
	public void setCodDispositivo(String codDispositivo) {
		this.codDispositivo = codDispositivo;
	}
    public String getNumTraspasoScl() {
        return numTraspasoScl;
    }
    public void setNumTraspasoScl(String numTraspasoScl) {
        this.numTraspasoScl = numTraspasoScl;
    }
}