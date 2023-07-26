package com.consystec.sc.ca.ws.input.historico;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputHistoricoPromo")
public class InputHistoricoPromocionales {
    @XmlElement
    private String token;
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String idCampania;
    @XmlElement
    private String tipoCliente;
    @XmlElement
    private String idTipo;
    @XmlElement
    private String idRuta;
    @XmlElement
    private String idVendedor;
    
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
    public String getIdCampania() {
        return idCampania;
    }
    public void setIdCampania(String idCampania) {
        this.idCampania = idCampania;
    }
    public String getTipoCliente() {
        return tipoCliente;
    }
    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
    public String getIdTipo() {
        return idTipo;
    }
    public void setIdTipo(String idTipo) {
        this.idTipo = idTipo;
    }
    public String getIdRuta() {
        return idRuta;
    }
    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }
    public String getIdVendedor() {
        return idVendedor;
    }
    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }
	public String getCodDispositivo() {
		return codDispositivo;
	}
	public void setCodDispositivo(String codDispositivo) {
		this.codDispositivo = codDispositivo;
	}
    
}