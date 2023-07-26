package com.consystec.sc.sv.ws.orm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Respuesta")
public class Respuesta {
    @XmlElement
    private int codResultado;
    @XmlElement
    private int mostrar;
    @XmlElement
    private String descripcion;
    @XmlElement
    private String token;
    @XmlElement
    private String origen;
    @XmlElement
    private String clase;
    @XmlElement
    private String metodo;
    @XmlElement
    private String excepcion;
    @XmlElement
    private String tipoExepcion;

    public int getCodResultado() {
        return codResultado;
    }

    public void setCodResultado(int codResultado) {
        this.codResultado = codResultado;
    }

    public int getMostrar() {
        return mostrar;
    }

    public void setMostrar(int mostrar) {
        this.mostrar = mostrar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getExcepcion() {
        return excepcion;
    }

    public void setExcepcion(String excepcion) {
        this.excepcion = excepcion;
    }

    public String getTipoExepcion() {
        return tipoExepcion;
    }

    public void setTipoExepcion(String tipoExepcion) {
        this.tipoExepcion = tipoExepcion;
    }
}
