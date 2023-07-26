package com.consystec.sc.ca.ws.output.cliente;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.cliente.InputCliente;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputCliente")
public class OutputCliente {
    @XmlElement
    private String token;
    @XmlElement
    private String idCliente;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputCliente> cliente;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputCliente> getCliente() {
        return cliente;
    }
    public void setCliente(List<InputCliente> cliente) {
        this.cliente = cliente;
    }
}