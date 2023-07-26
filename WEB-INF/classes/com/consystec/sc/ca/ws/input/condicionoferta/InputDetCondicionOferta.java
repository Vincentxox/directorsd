package com.consystec.sc.ca.ws.input.condicionoferta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputDetCondicionOferta")
public class InputDetCondicionOferta {
    @XmlElement
    private String codArea;
	@XmlElement
	private String idDetCondicion;
	@XmlElement
	private String idCondicion;
	@XmlElement
    private String idOfertaCampania;
	@XmlElement
	private String tipo;
	@XmlElement
	private String idArticulo;
	@XmlElement
    private String nombreArticulo;
	@XmlElement
    private String tipoCliente;
	@XmlElement
    private String tecnologia;
	@XmlElement
    private String tipoInv;
	@XmlElement
	private String montoInicial;
	@XmlElement
	private String montoFinal;
	@XmlElement
    private String tipoDescuento;
	@XmlElement
    private String valorDescuento;
	@XmlElement
    private String idPDV;
	@XmlElement
    private String nombrePDV;
    @XmlElement
    private String zonaComercialPDV;
    @XmlElement
    private String categoriaPDV;
    @XmlElement
    private String idArticuloRegalo;
    @XmlElement
    private String nombreArticuloRegalo;
    @XmlElement
    private String cantidadArticuloRegalo;
    @XmlElement
    private String tipoDescuentoRegalo;
    @XmlElement
    private String valorDescuentoRegalo;
    @XmlElement
    private String estado;
    @XmlElement
    private String creadoPor;
    @XmlElement
    private String creadoEl;
    @XmlElement
    private String modificadoPor;
    @XmlElement
    private String modificadoEl;
    @XmlElement
    private String idDescuento;
    @XmlElement
    private String idProductOffering;

    public String getIdDetCondicion() {
        return idDetCondicion;
    }
    public void setIdDetCondicion(String idDetCondicion) {
        this.idDetCondicion = idDetCondicion;
    }
    public String getIdCondicion() {
        return idCondicion;
    }
    public void setIdCondicion(String idCondicion) {
        this.idCondicion = idCondicion;
    }
    public String getIdOfertaCampania() {
        return idOfertaCampania;
    }
    public void setIdOfertaCampania(String idOfertaCampania) {
        this.idOfertaCampania = idOfertaCampania;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getIdArticulo() {
        return idArticulo;
    }
    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }
    public String getNombreArticulo() {
        return nombreArticulo;
    }
    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }
    public String getTipoCliente() {
        return tipoCliente;
    }
    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
    public String getTecnologia() {
        return tecnologia;
    }
    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }
    public String getTipoInv() {
        return tipoInv;
    }
    public void setTipoInv(String tipoInv) {
        this.tipoInv = tipoInv;
    }
    public String getMontoInicial() {
        return montoInicial;
    }
    public void setMontoInicial(String montoInicial) {
        this.montoInicial = montoInicial;
    }
    public String getMontoFinal() {
        return montoFinal;
    }
    public void setMontoFinal(String montoFinal) {
        this.montoFinal = montoFinal;
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
    public String getIdPDV() {
        return idPDV;
    }
    public void setIdPDV(String idPDV) {
        this.idPDV = idPDV;
    }
    public String getNombrePDV() {
        return nombrePDV;
    }
    public void setNombrePDV(String nombrePDV) {
        this.nombrePDV = nombrePDV;
    }
    public String getZonaComercialPDV() {
        return zonaComercialPDV;
    }
    public void setZonaComercialPDV(String zonaComercialPDV) {
        this.zonaComercialPDV = zonaComercialPDV;
    }
    public String getCategoriaPDV() {
        return categoriaPDV;
    }
    public void setCategoriaPDV(String categoriaPDV) {
        this.categoriaPDV = categoriaPDV;
    }
    public String getIdArticuloRegalo() {
        return idArticuloRegalo;
    }
    public void setIdArticuloRegalo(String idArticuloRegalo) {
        this.idArticuloRegalo = idArticuloRegalo;
    }
    public String getNombreArticuloRegalo() {
        return nombreArticuloRegalo;
    }
    public void setNombreArticuloRegalo(String nombreArticuloRegalo) {
        this.nombreArticuloRegalo = nombreArticuloRegalo;
    }
    public String getCantidadArticuloRegalo() {
        return cantidadArticuloRegalo;
    }
    public void setCantidadArticuloRegalo(String cantidadArticuloRegalo) {
        this.cantidadArticuloRegalo = cantidadArticuloRegalo;
    }
    public String getTipoDescuentoRegalo() {
        return tipoDescuentoRegalo;
    }
    public void setTipoDescuentoRegalo(String tipoDescuentoRegalo) {
        this.tipoDescuentoRegalo = tipoDescuentoRegalo;
    }
    public String getValorDescuentoRegalo() {
        return valorDescuentoRegalo;
    }
    public void setValorDescuentoRegalo(String valorDescuentoRegalo) {
        this.valorDescuentoRegalo = valorDescuentoRegalo;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getCreadoPor() {
        return creadoPor;
    }
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }
    public String getCreadoEl() {
        return creadoEl;
    }
    public void setCreadoEl(String creadoEl) {
        this.creadoEl = creadoEl;
    }
    public String getModificadoPor() {
        return modificadoPor;
    }
    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }
    public String getModificadoEl() {
        return modificadoEl;
    }
    public void setModificadoEl(String modificadoEl) {
        this.modificadoEl = modificadoEl;
    }
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getIdDescuento() {
		return idDescuento;
	}
	public void setIdDescuento(String idDescuento) {
		this.idDescuento = idDescuento;
	}
	public String getIdProductOffering() {
		return idProductOffering;
	}
	public void setIdProductOffering(String idProductOffering) {
		this.idProductOffering = idProductOffering;
	}
    
    
}