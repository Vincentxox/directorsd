package com.consystec.sc.ca.ws.input.inventariomovil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputInventarioCampaniaCondiciones")
public class InputInventarioCampaniaCondiciones {
    @XmlElement
    private String codArea;
    @XmlElement
    private String idOfertaCampania;
    @XmlElement
    private String nombreCampania;
    @XmlElement
    private String idCondicion;
    @XmlElement
    private String nombreCondicion;
    @XmlElement
    private String tipoGestion;
    @XmlElement
    private String idArticulo;
    @XmlElement
    private String tipoCliente;
    @XmlElement
    private String tipoOferta;
    @XmlElement
    private String tecnologia;
    @XmlElement
    private String idPDV;
    @XmlElement
    private String categoria;
    @XmlElement
    private String zona;
    @XmlElement
    private String tipoDescuento;
    @XmlElement
    private String valorDescuento;

    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
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
    public String getTipoGestion() {
        return tipoGestion;
    }
    public void setTipoGestion(String tipoGestion) {
        this.tipoGestion = tipoGestion;
    }
    public String getIdArticulo() {
        return idArticulo;
    }
    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }
    public String getTipoCliente() {
        return tipoCliente;
    }
    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
    public String getTipoOferta() {
        return tipoOferta;
    }
    public void setTipoOferta(String tipoOferta) {
        this.tipoOferta = tipoOferta;
    }
    public String getTecnologia() {
        return tecnologia;
    }
    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
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
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getZona() {
        return zona;
    }
    public void setZona(String zona) {
        this.zona = zona;
    }
}