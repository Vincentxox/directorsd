package com.consystec.sc.ca.ws.output.venta;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.venta.InputVenta;
import com.consystec.sc.ca.ws.mapas.InputMapas;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputVenta")
public class OutputVenta {
    @XmlElement
    private String token;
    @XmlElement
    private String idVenta;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<ArticuloIncorrecto> articulosIncorrectos;
    @XmlElement
    private List<InputMapas> articulosVendidos;
    @XmlElement
    private List<InputVenta> venta;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getIdVenta() {
        return idVenta;
    }
    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<ArticuloIncorrecto> getArticulosIncorrectos() {
        return articulosIncorrectos;
    }
    public void setArticulosIncorrectos(List<ArticuloIncorrecto> articulosIncorrectos) {
        this.articulosIncorrectos = articulosIncorrectos;
    }
    public List<InputMapas> getArticulosVendidos() {
        return articulosVendidos;
    }
    public void setArticulosVendidos(List<InputMapas> articulosVendidos) {
        this.articulosVendidos = articulosVendidos;
    }
    public List<InputVenta> getVenta() {
        return venta;
    }
    public void setVenta(List<InputVenta> venta) {
        this.venta = venta;
    }
}