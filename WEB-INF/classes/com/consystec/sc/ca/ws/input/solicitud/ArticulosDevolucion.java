package com.consystec.sc.ca.ws.input.solicitud;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ArticulosDevolucion")
public class ArticulosDevolucion {
    @XmlElement
    private String tcscSolicitudId;
    @XmlElement
    private String idArticulo;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String aceptado;
    @XmlElement
    private String observaciones;

    public String getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getAceptado() {
        return aceptado;
    }

    public void setAceptado(String aceptado) {
        this.aceptado = aceptado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTcscSolicitudId() {
        return tcscSolicitudId;
    }

    public void setTcscSolicitudId(String tcscSolicitudId) {
        this.tcscSolicitudId = tcscSolicitudId;
    }

    public String getCodDispositivo() {
        return codDispositivo;
    }

    public void setCodDispositivo(String codDispositivo) {
        this.codDispositivo = codDispositivo;
    }
}