package com.consystec.sc.ca.ws.output.cuenta;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.cuenta.InputCuenta;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputCuenta")
public class OutputCuenta {
    @XmlElement
    private String token;
    @XmlElement
    private String idCuenta;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private OutputDtsCuenta distribuidores;
    @XmlElement
    private List<InputCuenta> bancos;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getIdCuenta() {
        return idCuenta;
    }
    public void setIdCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }

    public OutputDtsCuenta getDistribuidores() {
		return distribuidores;
	}
	public void setDistribuidores(OutputDtsCuenta distribuidores) {
		this.distribuidores = distribuidores;
	}
	public List<InputCuenta> getBancos() {
        return bancos;
    }
    public void setBancos(List<InputCuenta> bancos) {
        this.bancos = bancos;
    }
}