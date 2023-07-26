package com.consystec.sc.ca.ws.input.inventario;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputConsultaInventario")
public class InputConsultaInventario {
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
    private String idArticulo;
    @XmlElement
    private String descripcion;
    @XmlElement
    private String serie;
    @XmlElement
    private String tipoInv;
    @XmlElement
    private String tipoGrupo;
    @XmlElement
    private String seriado;
    @XmlElement
    private String idVendedor;
    @XmlElement
    private String estado;
    @XmlElement
    private String tecnologia;
    @XmlElement
    private String numTraspasoScl;
    @XmlElement
    private String min;
    @XmlElement
    private String max;
    @XmlElement
    private String mostrarDetalle;
    @XmlElement
    private String datosWeb;
    @XmlElement
    private String mostrarRecarga;
    @XmlElement
    private String noLote;

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
    public String getCodDispositivo() {
        return codDispositivo;
    }
    public void setCodDispositivo(String codDispositivo) {
        this.codDispositivo = codDispositivo;
    }
    public String getIdBodega() {
        return idBodega;
    }
    public void setIdBodega(String idBodega) {
        this.idBodega = idBodega;
    }
    public String getIdArticulo() {
        return idArticulo;
    }
    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getSerie() {
        return serie;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }
    public String getTipoInv() {
        return tipoInv;
    }
    public void setTipoInv(String tipoInv) {
        this.tipoInv = tipoInv;
    }
    public String getTipoGrupo() {
        return tipoGrupo;
    }
    public void setTipoGrupo(String tipoGrupo) {
        this.tipoGrupo = tipoGrupo;
    }
    public String getSeriado() {
        return seriado;
    }
    public void setSeriado(String seriado) {
        this.seriado = seriado;
    }
    public String getIdVendedor() {
        return idVendedor;
    }
    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getTecnologia() {
        return tecnologia;
    }
    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }
    public String getNumTraspasoScl() {
        return numTraspasoScl;
    }
    public void setNumTraspasoScl(String numTraspasoScl) {
        this.numTraspasoScl = numTraspasoScl;
    }
    public String getMin() {
        return min;
    }
    public void setMin(String min) {
        this.min = min;
    }
    public String getMax() {
        return max;
    }
    public void setMax(String max) {
        this.max = max;
    }
    public String getMostrarDetalle() {
        return mostrarDetalle;
    }
    public void setMostrarDetalle(String mostrarDetalle) {
        this.mostrarDetalle = mostrarDetalle;
    }
    public String getDatosWeb() {
        return datosWeb;
    }
    public void setDatosWeb(String datosWeb) {
        this.datosWeb = datosWeb;
    }
    public String getMostrarRecarga() {
        return mostrarRecarga;
    }
    public void setMostrarRecarga(String mostrarRecarga) {
        this.mostrarRecarga = mostrarRecarga;
    }
	public String getNoLote() {
		return noLote;
	}
	public void setNoLote(String noLote) {
		this.noLote = noLote;
	}    
}