package com.consystec.sc.ca.ws.input.inventario;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputDatosInventario")
public class InputDatosInventario {
    @XmlElement
    private String codArea;
    @XmlElement
    private String idInventario;
    @XmlElement
    private String idBodega;
    @XmlElement
    private String idSolicitud;
    @XmlElement
    private String idAsignacionReserva;
    @XmlElement
    private String grupo;
    @XmlElement
    private String idArticulo;
    @XmlElement
    private String cantidad;
    @XmlElement
    private String serie;
    @XmlElement
    private String estadoComercial;
    @XmlElement
    private String serieAsociada;
    @XmlElement
    private String tipoInv;
    @XmlElement
    private String numTraspasoScl;
    @XmlElement
    private String estado;
    @XmlElement
    private String numTelefono;
    @XmlElement
    private String icc;
    @XmlElement
    private String imei;
    @XmlElement
    private String creado_el;
    @XmlElement
    private String creado_por;
    @XmlElement
    private String modificado_el;
    @XmlElement
    private String modificado_por;

    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getIdInventario() {
        return idInventario;
    }
    public void setIdInventario(String idInventario) {
        this.idInventario = idInventario;
    }
    public String getIdBodega() {
        return idBodega;
    }
    public void setIdBodega(String idBodega) {
        this.idBodega = idBodega;
    }
    public String getIdSolicitud() {
        return idSolicitud;
    }
    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }
    public String getIdAsignacionReserva() {
        return idAsignacionReserva;
    }
    public void setIdAsignacionReserva(String idAsignacionReserva) {
        this.idAsignacionReserva = idAsignacionReserva;
    }
    public String getGrupo() {
        return grupo;
    }
    public void setGrupo(String grupo) {
        this.grupo = grupo;
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
    public String getEstadoComercial() {
        return estadoComercial;
    }
    public void setEstadoComercial(String estadoComercial) {
        this.estadoComercial = estadoComercial;
    }
    public String getSerieAsociada() {
        return serieAsociada;
    }
    public void setSerieAsociada(String serieAsociada) {
        this.serieAsociada = serieAsociada;
    }
    public String getTipoInv() {
        return tipoInv;
    }
    public void setTipoInv(String tipoInv) {
        this.tipoInv = tipoInv;
    }
    public String getNumTraspasoScl() {
        return numTraspasoScl;
    }
    public void setNumTraspasoScl(String numTraspasoScl) {
        this.numTraspasoScl = numTraspasoScl;
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
	public String getCantidad() {
		return cantidad;
	}
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	public String getNumTelefono() {
		return numTelefono;
	}
	public void setNumTelefono(String numTelefono) {
		this.numTelefono = numTelefono;
	}
	public String getIcc() {
		return icc;
	}
	public void setIcc(String icc) {
		this.icc = icc;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
}