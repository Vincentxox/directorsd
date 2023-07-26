package com.consystec.sc.ca.ws.output.pdv;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.pdv.InputPDV;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputPDV")
public class OutputPDV {
	@XmlElement
	private String  idPDV;
	@XmlElement
	private String  zonaComercial;
	@XmlElement
	private String  idEncargadoPDV;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
    private List<InputPDV> PuntoDeVenta;
	@XmlElement
	private String cantRegistros;
	
	public String getIdPDV() {
		return idPDV;
	}
	public void setIdPDV(String idPDV) {
		this.idPDV = idPDV;
	}
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
    public List<InputPDV> getPuntoDeVenta() {
        return PuntoDeVenta;
    }
    public void setPuntoDeVenta(List<InputPDV> puntoDeVenta) {
        PuntoDeVenta = puntoDeVenta;
    }
	public String getZonaComercial() {
		return zonaComercial;
	}
	public void setZonaComercial(String zonaComercial) {
		this.zonaComercial = zonaComercial;
	}
	public String getCantRegistros() {
		return cantRegistros;
	}
	public void setCantRegistros(String cantRegistros) {
		this.cantRegistros = cantRegistros;
	}
	public String getIdEncargadoPDV() {
		return idEncargadoPDV;
	}
	public void setIdEncargadoPDV(String idEncargadoPDV) {
		this.idEncargadoPDV = idEncargadoPDV;
	}
    
}