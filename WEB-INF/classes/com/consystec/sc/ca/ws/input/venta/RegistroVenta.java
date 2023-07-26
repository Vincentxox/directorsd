package com.consystec.sc.ca.ws.input.venta;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RegistroVenta")
public class RegistroVenta {
	@XmlElement
	private String codBodegaSCL;
	@XmlElement
	private List<VentaDet> articulos;
	
    public String getCodBodegaSCL() {
        return codBodegaSCL;
    }
    public void setCodBodegaSCL(String codBodegaSCL) {
        this.codBodegaSCL = codBodegaSCL;
    }
    public List<VentaDet> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<VentaDet> articulos) {
        this.articulos = articulos;
    }
}