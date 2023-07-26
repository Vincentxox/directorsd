package com.consystec.sc.ca.ws.input.venta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Descuento")
public class Descuento {
    @XmlElement
    private String idOfertaCampania;
    @XmlElement
    private String idCondicion;
    @XmlElement
    private String nombreOfertaCampania;
    @XmlElement
    private String descuento;
    @XmlElement
    private String tipoDescuento;

    public String getIdOfertaCampania() {
        return idOfertaCampania;
    }
    public void setIdOfertaCampania(String idOfertaCampania) {
        this.idOfertaCampania = idOfertaCampania;
    }
    public String getIdCondicion() {
        return idCondicion;
    }
    public void setIdCondicion(String idCondicion) {
        this.idCondicion = idCondicion;
    }
    public String getNombreOfertaCampania() {
        return nombreOfertaCampania;
    }
    public void setNombreOfertaCampania(String nombreOfertaCampania) {
        this.nombreOfertaCampania = nombreOfertaCampania;
    }
    public String getDescuento() {
        return descuento;
    }
    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }
    public String getTipoDescuento() {
        return tipoDescuento;
    }
    public void setTipoDescuento(String tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }
}