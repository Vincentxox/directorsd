package com.consystec.sc.ca.ws.output.venta;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.venta.ArticuloVenta;
import com.consystec.sc.ca.ws.input.venta.Impuesto;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ArticuloVenta")
public class OutputArticuloVenta {
    @XmlElement
    private String token;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private String idVentaDet;
	@XmlElement
	private String articulo;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String cantidad;
	@XmlElement
	private String seriado;
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
	private String precio;
	@XmlElement
	private String descuentoSCL;
	@XmlElement
	private String descuentoSidra;
	@XmlElement
	private String idOfertaCampania;
	@XmlElement
	private String gestion;
	@XmlElement
	private String impuesto;
	@XmlElement
	private String precioTotal;
	@XmlElement
	private String modalidad;
	@XmlElement
	private String estado;
	@XmlElement
	private List<Impuesto> impuestosArticulo;
	@XmlElement
	private List<ArticuloVenta> detalleVenta;
	
	public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public List<ArticuloVenta> getDetalleVenta() {
		return detalleVenta;
	}
	public void setDetalleVenta(List<ArticuloVenta> detalleVenta) {
		this.detalleVenta = detalleVenta;
	}
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
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
	public String getIdOfertaCampania() {
		return idOfertaCampania;
	}
	public void setIdOfertaCampania(String idOfertaCampania) {
		this.idOfertaCampania = idOfertaCampania;
	}
	public String getGestion() {
		return gestion;
	}
	public void setGestion(String gestion) {
		this.gestion = gestion;
	}
	public String getImpuesto() {
		return impuesto;
	}
	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
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
	public List<Impuesto> getImpuestosArticulo() {
		return impuestosArticulo;
	}
	public void setImpuestosArticulo(List<Impuesto> impuestosArticulo) {
		this.impuestosArticulo = impuestosArticulo;
	}
}