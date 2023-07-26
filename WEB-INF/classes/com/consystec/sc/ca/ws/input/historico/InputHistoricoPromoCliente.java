package com.consystec.sc.ca.ws.input.historico;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputHistoricoPromoCliente")
public class InputHistoricoPromoCliente {
    @XmlElement
    private String codArea;
    @XmlElement
    private String tipoCliente;
    @XmlElement
    private String idTipo;
    @XmlElement
    private String idCampania;
    @XmlElement
    private List<InputHistoricoPromoCampania> campanias;
    
    public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
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
    public String getIdCampania() {
        return idCampania;
    }
    public void setIdCampania(String idCampania) {
        this.idCampania = idCampania;
    }
    public List<InputHistoricoPromoCampania> getCampanias() {
        return campanias;
    }
    public void setCampanias(List<InputHistoricoPromoCampania> campanias) {
        this.campanias = campanias;
    }
}