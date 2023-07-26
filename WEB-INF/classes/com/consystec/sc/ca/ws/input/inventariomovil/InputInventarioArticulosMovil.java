package com.consystec.sc.ca.ws.input.inventariomovil;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputInventarioArticulosMovil")
public class InputInventarioArticulosMovil {
    @XmlElement
    private String codArea;
    @XmlElement
    private String idBodega;
    @XmlElement
    private String grupo;
    @XmlElement
    private String idArticulo;
    @XmlElement
    private String descripcion;
    @XmlElement
    private String cantidad;
    @XmlElement
    private String seriado;
    @XmlElement
    private String estado;
    @XmlElement
    private String idSolicitud;
    @XmlElement
    private String idAsignacionReserva;
    @XmlElement
    private String tipoInv;
    @XmlElement
    private String tecnologia;
    @XmlElement
    private List<InputPrecioDescuentoArticulo> detallePrecios;
    @XmlElement
    private List<InputPrecioDescuentoArticulo> descuentos;
    @XmlElement
    private String tipoDescuento;
    @XmlElement
    private String valorDescuento;
    @XmlElement
    private String tipoGestion;
    @XmlElement
    private String idOfertaCampania;
    @XmlElement
    private String nombreCampania;
    @XmlElement
    private List<InputDatosInventarioMovil> detalleArticulo;
    
    public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getIdBodega() {
        return idBodega;
    }
    public void setIdBodega(String idBodega) {
        this.idBodega = idBodega;
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
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getCantidad() {
        return cantidad;
    }
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
    public String getSeriado() {
        return seriado;
    }
    public void setSeriado(String seriado) {
        this.seriado = seriado;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
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
    public String getTipoInv() {
        return tipoInv;
    }
    public void setTipoInv(String tipoInv) {
        this.tipoInv = tipoInv;
    }
    public String getTecnologia() {
        return tecnologia;
    }
    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }
    public List<InputPrecioDescuentoArticulo> getDetallePrecios() {
        return detallePrecios;
    }
    public void setDetallePrecios(List<InputPrecioDescuentoArticulo> detallePrecios) {
        this.detallePrecios = detallePrecios;
    }
    public List<InputPrecioDescuentoArticulo> getDescuentos() {
        return descuentos;
    }
    public void setDescuentos(List<InputPrecioDescuentoArticulo> descuentos) {
        this.descuentos = descuentos;
    }
    public String getTipoDescuento() {
        return tipoDescuento;
    }
    public void setTipoDescuento(String tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }
    public String getValorDescuento() {
        return valorDescuento;
    }
    public void setValorDescuento(String valorDescuento) {
        this.valorDescuento = valorDescuento;
    }
    public String getTipoGestion() {
        return tipoGestion;
    }
    public void setTipoGestion(String tipoGestion) {
        this.tipoGestion = tipoGestion;
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
    public List<InputDatosInventarioMovil> getDetalleArticulo() {
        return detalleArticulo;
    }
    public void setDetalleArticulo(List<InputDatosInventarioMovil> detalleArticulo) {
        this.detalleArticulo = detalleArticulo;
    }
}