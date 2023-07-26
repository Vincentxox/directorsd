package com.consystec.sc.ca.ws.input.traslado;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputTraslado")
public class InputTraslado implements Cloneable {
    @XmlElement
    private String token;
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String numTraspaso;
    @XmlElement
    private String bodegaOrigen;
    @XmlElement
    private String nvlBodegaOrigen;
    @XmlElement
    private String bodegaDestino;
    @XmlElement
    private String fecha;
    @XmlElement
    private String creado_el;
    @XmlElement
    private String creado_por;
    @XmlElement
    private List<InputArticuloTraslado> articulos;
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getNumTraspaso() {
        return numTraspaso;
    }
    public void setNumTraspaso(String numTraspaso) {
        this.numTraspaso = numTraspaso;
    }
    public String getBodegaOrigen() {
        return bodegaOrigen;
    }
    public void setBodegaOrigen(String bodegaOrigen) {
        this.bodegaOrigen = bodegaOrigen;
    }
    public String getNvlBodegaOrigen() {
		return nvlBodegaOrigen;
	}
	public void setNvlBodegaOrigen(String nvlBodegaOrigen) {
		this.nvlBodegaOrigen = nvlBodegaOrigen;
	}
    public String getBodegaDestino() {
        return bodegaDestino;
    }
    public void setBodegaDestino(String bodegaDestino) {
        this.bodegaDestino = bodegaDestino;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
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
    public List<InputArticuloTraslado> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<InputArticuloTraslado> articulos) {
        this.articulos = articulos;
    }
}