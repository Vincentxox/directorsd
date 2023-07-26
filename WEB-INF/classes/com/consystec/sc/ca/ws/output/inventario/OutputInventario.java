package com.consystec.sc.ca.ws.output.inventario;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.inventario.InputInventario;
import com.consystec.sc.ca.ws.input.inventario.InputInventarioArticulos;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputInventario")
public class OutputInventario {
    @XmlElement
    private String token;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputInventario> inventario;
    @XmlElement
    private List<InputInventarioArticulos> articulos;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputInventario> getInventario() {
        return inventario;
    }
    public void setInventario(List<InputInventario> inventario) {
        this.inventario = inventario;
    }
    public List<InputInventarioArticulos> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<InputInventarioArticulos> articulos) {
        this.articulos = articulos;
    }
}