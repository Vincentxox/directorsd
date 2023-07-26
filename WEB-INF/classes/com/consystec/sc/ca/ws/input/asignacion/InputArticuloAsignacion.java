package com.consystec.sc.ca.ws.input.asignacion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputArticuloAsignacion")
public class InputArticuloAsignacion {
    @XmlElement
    private String idInventario;
    @XmlElement
    private String idAsignacionReservaDet;
    @XmlElement
    private String idAsignacionReserva;
    @XmlElement
    private String idArticulo;
    @XmlElement
    private String descripcion;
    @XmlElement
    private String serie;
    @XmlElement
    private String imei;
    @XmlElement
    private String icc;
    @XmlElement
    private String serieFinal;
    @XmlElement
    private String serieAsociada;
    @XmlElement
    private String cantidad;
    @XmlElement
    private String observaciones;
    @XmlElement
    private String tipoInv;
    @XmlElement
    private String tipoGrupoSidra;
    @XmlElement
    private String numTelefono;
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
    private String idTraslado;

    public String getIdInventario() {
        return idInventario;
    }
    public void setIdInventario(String idInventario) {
        this.idInventario = idInventario;
    }
    public String getIdAsignacionReservaDet() {
        return idAsignacionReservaDet;
    }
    public void setIdAsignacionReservaDet(String idAsignacionReservaDet) {
        this.idAsignacionReservaDet = idAsignacionReservaDet;
    }
    public String getIdAsignacionReserva() {
        return idAsignacionReserva;
    }
    public void setIdAsignacionReserva(String idAsignacionReserva) {
        this.idAsignacionReserva = idAsignacionReserva;
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
    public String getSerieFinal() {
        return serieFinal;
    }
    public void setSerieFinal(String serieFinal) {
        this.serieFinal = serieFinal;
    }
    public String getSerieAsociada() {
        return serieAsociada;
    }
    public void setSerieAsociada(String serieAsociada) {
        this.serieAsociada = serieAsociada;
    }
    public String getCantidad() {
        return cantidad;
    }
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public String getTipoInv() {
        return tipoInv;
    }
    public void setTipoInv(String tipoInv) {
        this.tipoInv = tipoInv;
    }
    public String getTipoGrupoSidra() {
        return tipoGrupoSidra;
    }
    public void setTipoGrupoSidra(String tipoGrupoSidra) {
        this.tipoGrupoSidra = tipoGrupoSidra;
    }
    public String getNumTelefono() {
        return numTelefono;
    }
    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
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
	public String getIdTraslado() {
		return idTraslado;
	}
	public void setIdTraslado(String idTraslado) {
		this.idTraslado = idTraslado;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getIcc() {
		return icc;
	}
	public void setIcc(String icc) {
		this.icc = icc;
	}
    
}