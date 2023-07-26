package com.consystec.sc.ca.ws.input.historico;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputArticuloHistorico")
public class InputArticuloHistorico {
    @XmlElement
    private String idArticulo;
    @XmlElement
    private String serie;
    @XmlElement
    private String serieFinal;
    @XmlElement
    private String cantidad;
    @XmlElement
    private String serieAsociada;
    @XmlElement
    private String tipoInv;
    @XmlElement
    private String estado;
    @XmlElement
    private String creado_el;
    @XmlElement
    private String creado_por;
    
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
    public String getSerieFinal() {
        return serieFinal;
    }
    public void setSerieFinal(String serieFinal) {
        this.serieFinal = serieFinal;
    }
    public String getCantidad() {
        return cantidad;
    }
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
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
}