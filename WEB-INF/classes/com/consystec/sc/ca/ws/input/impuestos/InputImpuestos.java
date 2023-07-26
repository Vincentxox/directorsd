package com.consystec.sc.ca.ws.input.impuestos;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputImpuestos")
public class InputImpuestos {
    @XmlElement
    private String codArea;
    @XmlElement
    private String nombre;
    @XmlElement
    private String valor;
    @XmlElement
    private String tipoCliente;
    @XmlElement
    private String despuesDeDescuento;
    @XmlElement
    private String porcentaje;
    @XmlElement
    private String estado;
    @XmlElement
    private List<InputImpuestosGrupos> grupos;
    
    public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getValor() {
        return valor;
    }
    public void setValor(String valor) {
        this.valor = valor;
    }
    public String getTipoCliente() {
        return tipoCliente;
    }
    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
    public String getDespuesDeDescuento() {
        return despuesDeDescuento;
    }
    public void setDespuesDeDescuento(String despuesDeDescuento) {
        this.despuesDeDescuento = despuesDeDescuento;
    }
    public String getPorcentaje() {
        return porcentaje;
    }
    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public List<InputImpuestosGrupos> getGrupos() {
        return grupos;
    }
    public void setGrupos(List<InputImpuestosGrupos> grupos) {
        this.grupos = grupos;
    }
}