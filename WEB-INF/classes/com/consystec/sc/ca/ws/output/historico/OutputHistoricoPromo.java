package com.consystec.sc.ca.ws.output.historico;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.historico.InputHistoricoPromoCliente;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputHistoricoPromo")
public class OutputHistoricoPromo {
    @XmlElement
    private String token;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputHistoricoPromoCliente> clientes;
    
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
    public List<InputHistoricoPromoCliente> getClientes() {
        return clientes;
    }
    public void setClientes(List<InputHistoricoPromoCliente> clientes) {
        this.clientes = clientes;
    }
}