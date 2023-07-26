package com.consystec.sc.ca.ws.input.solicitud;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputTipoSolicitud")
public class InputTipoSolicitud {
    @XmlElement
    private String codArea;
    @XmlElement
    private String tipoSolicitud;
    @XmlElement
    private List<InputSolicitud> listaSolicitudes;
    
    public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getTipoSolicitud() {
        return tipoSolicitud;
    }
    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }
    public List<InputSolicitud> getListaSolicitudes() {
        return listaSolicitudes;
    }
    public void setListaSolicitudes(List<InputSolicitud> listaSolicitudes) {
        this.listaSolicitudes = listaSolicitudes;
    }
}