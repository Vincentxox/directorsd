package com.consystec.sc.ca.ws.input.venta;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ArticuloVenta")
public class ArticuloVenta {
    @XmlElement
    private String codArea;
	@XmlElement
	private String idVentaDet;
	@XmlElement
    private String idTemporalArticulo;
	@XmlElement
	private String articulo;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String cantidad;
	@XmlElement
	private String seriado;
	@XmlElement
	private String serie;
	@XmlElement
	private String rango;
	@XmlElement
	private String serieInicial;
	@XmlElement
	private String serieFinal;
	@XmlElement
	private String serieAsociada;
	@XmlElement
	private String numTelefono;
	@XmlElement
	private String tipoInv;
	@XmlElement
    private String tipoGrupoSidra;
    @XmlElement
    private String gestion;
	@XmlElement
	private String precio;
	@XmlElement
	private String descuentoSCL;
	@XmlElement
	private String descuentoSidra;
	@XmlElement
    private List<Descuento> detalleDescuentosSidra;
	@XmlElement
	private String impuesto;
	@XmlElement
	private List<Impuesto> impuestosArticulo;
    @XmlElement
    private String precioTotal;
    @XmlElement
    private String modalidad;
    @XmlElement
    private String operadorDonante;
    @XmlElement
    private String numeroTemporal;
    @XmlElement
    private String numeroaPortar;
    @XmlElement
    private String cip;    
	@XmlElement
	private String tipoProductoDonante;
	@XmlElement
    private String estado;
    @XmlElement
    private String tasaCambio;
    @XmlElement
    private String observaciones;

    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getIdVentaDet() {
        return idVentaDet;
    }
    public void setIdVentaDet(String idVentaDet) {
        this.idVentaDet = idVentaDet;
    }
    public String getArticulo() {
        return articulo;
    }
    public void setArticulo(String articulo) {
        this.articulo = articulo;
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
    public String getSerie() {
        return serie;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }
    public String getRango() {
        return rango;
    }
    public void setRango(String rango) {
        this.rango = rango;
    }
    public String getSerieInicial() {
        return serieInicial;
    }
    public void setSerieInicial(String serieInicial) {
        this.serieInicial = serieInicial;
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
    public String getNumTelefono() {
        return numTelefono;
    }
    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
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
    public String getGestion() {
        return gestion;
    }
    public void setGestion(String gestion) {
        this.gestion = gestion;
    }
    public String getPrecio() {
        return precio;
    }
    public void setPrecio(String precio) {
        this.precio = precio;
    }
    public String getDescuentoSCL() {
        return descuentoSCL;
    }
    public void setDescuentoSCL(String descuentoSCL) {
        this.descuentoSCL = descuentoSCL;
    }
    public String getDescuentoSidra() {
        return descuentoSidra;
    }
    public void setDescuentoSidra(String descuentoSidra) {
        this.descuentoSidra = descuentoSidra;
    }
    public List<Descuento> getDetalleDescuentosSidra() {
        return detalleDescuentosSidra;
    }
    public void setDetalleDescuentosSidra(List<Descuento> detalleDescuentosSidra) {
        this.detalleDescuentosSidra = detalleDescuentosSidra;
    }
    public String getImpuesto() {
        return impuesto;
    }
    public void setImpuesto(String impuesto) {
        this.impuesto = impuesto;
    }
    public List<Impuesto> getImpuestosArticulo() {
        return impuestosArticulo;
    }
    public void setImpuestosArticulo(List<Impuesto> impuestosArticulo) {
        this.impuestosArticulo = impuestosArticulo;
    }
    public String getPrecioTotal() {
        return precioTotal;
    }
    public void setPrecioTotal(String precioTotal) {
        this.precioTotal = precioTotal;
    }
    public String getModalidad() {
        return modalidad;
    }
    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getTasaCambio() {
        return tasaCambio;
    }
    public void setTasaCambio(String tasaCambio) {
        this.tasaCambio = tasaCambio;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public String getIdTemporalArticulo() {
		return idTemporalArticulo;
	}
	public void setIdTemporalArticulo(String idTemporalArticulo) {
		this.idTemporalArticulo = idTemporalArticulo;
	}
	public String getOperadorDonante() {
		return operadorDonante;
	}
	public void setOperadorDonante(String operadorDonante) {
		this.operadorDonante = operadorDonante;
	}
	public String getNumeroTemporal() {
		return numeroTemporal;
	}
	public void setNumeroTemporal(String numeroTemporal) {
		this.numeroTemporal = numeroTemporal;
	}
	public String getNumeroaPortar() {
		return numeroaPortar;
	}
	public void setNumeroaPortar(String numeroaPortar) {
		this.numeroaPortar = numeroaPortar;
	}
	public String getCip() {
		return cip;
	}
	public void setCip(String cip) {
		this.cip = cip;
	}
	public String getTipoProductoDonante() {
		return tipoProductoDonante;
	}
	public void setTipoProductoDonante(String tipoProductoDonante) {
		this.tipoProductoDonante = tipoProductoDonante;
	}
}