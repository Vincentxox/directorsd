package com.consystec.sc.ca.ws.input.inventariomovil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputPrecioDescuentoArticulo")
public class InputPrecioDescuentoArticulo {
    @XmlElement
    private String codArea;
    @XmlElement
    private String idArticulo;
    @XmlElement
    private String precioSCL;
    @XmlElement
    private String descuentoSCL;
    @XmlElement
    private String tipo;
    @XmlElement
    private String tecnologia;
    @XmlElement
    private String tipoGestion;
    @XmlElement
    private String tipoCliente;
    @XmlElement
    private String version;
    @XmlElement
    private String tipoDescuento;
    @XmlElement
    private String valorDescuento;
    @XmlElement
    private String idOfertaCampania;
    @XmlElement
    private String nombreCampania;
    @XmlElement
    private String nombreCondicion;
	@XmlElement
    private String idCondicion;
    
	public String getIdCondicion() {
		return idCondicion;
	}
	public void setIdCondicion(String idCondicion) {
		this.idCondicion = idCondicion;
	}

	public String getNombreCondicion() {
		return nombreCondicion;
	}
	public void setNombreCondicion(String nombreCondicion) {
		this.nombreCondicion = nombreCondicion;
	}
	public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getIdArticulo() {
        return idArticulo;
    }
    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }
    public String getPrecioSCL() {
        return precioSCL;
    }
    public void setPrecioSCL(String precioSCL) {
        this.precioSCL = precioSCL;
    }
    public String getDescuentoSCL() {
        return descuentoSCL;
    }
    public void setDescuentoSCL(String descuentoSCL) {
        this.descuentoSCL = descuentoSCL;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getTecnologia() {
        return tecnologia;
    }
    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }
    public String getTipoGestion() {
        return tipoGestion;
    }
    public void setTipoGestion(String tipoGestion) {
        this.tipoGestion = tipoGestion;
    }
    public String getTipoCliente() {
        return tipoCliente;
    }
    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
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
}