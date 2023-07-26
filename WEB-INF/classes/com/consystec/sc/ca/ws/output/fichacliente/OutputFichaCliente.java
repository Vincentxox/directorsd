package com.consystec.sc.ca.ws.output.fichacliente;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.fichacliente.InputFichaCliente;
import com.consystec.sc.ca.ws.orm.Respuesta;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputFichaCliente")
public class OutputFichaCliente {
    @XmlElement
    private String token;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private InputFichaCliente cliente;
    @XmlElement
    private String IDGeographicArea;
    @XmlElement
    private String IDCustomer;
    @XmlElement
    private String contactId;
    
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
	public InputFichaCliente getCliente() {
		return cliente;
	}
	public void setCliente(InputFichaCliente cliente) {
		this.cliente = cliente;
	}
	public String getIDGeographicArea() {
		return IDGeographicArea;
	}
	public void setIDGeographicArea(String iDGeographicArea) {
		IDGeographicArea = iDGeographicArea;
	}
	public String getIDCustomer() {
		return IDCustomer;
	}
	public void setIDCustomer(String iDCustomer) {
		IDCustomer = iDCustomer;
	}
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	
}