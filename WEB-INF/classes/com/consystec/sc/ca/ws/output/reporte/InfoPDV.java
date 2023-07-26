package com.consystec.sc.ca.ws.output.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class InfoPDV {
	@XmlElement
	private String idPuntoVenta;
	@XmlElement
	private String departamento;
	@XmlElement
	private String municipio;
	@XmlElement
	private String distrito;
	@XmlElement
	private String tcsczonacomercialid;
	@XmlElement
	private String nombrePdv;
	@XmlElement
	private String nombreDuenio;
	@XmlElement
	private String documentoDuenio;
	@XmlElement
	private String numeroRecarga;
	@XmlElement
	private String qr;
	@XmlElement
	private List<ComparacionVenta> comparacion;

    public String getIdPuntoVenta() {
        return idPuntoVenta;
    }
    public void setIdPuntoVenta(String idPuntoVenta) {
        this.idPuntoVenta = idPuntoVenta;
    }
    public String getDepartamento() {
        return departamento;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    public String getMunicipio() {
        return municipio;
    }
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
    public String getDistrito() {
        return distrito;
    }
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }
    public String getTcsczonacomercialid() {
        return tcsczonacomercialid;
    }
    public void setTcsczonacomercialid(String tcsczonacomercialid) {
        this.tcsczonacomercialid = tcsczonacomercialid;
    }
    public String getNombrePdv() {
        return nombrePdv;
    }
    public void setNombrePdv(String nombrePdv) {
        this.nombrePdv = nombrePdv;
    }
    public String getNombreDuenio() {
		return nombreDuenio;
	}
	public void setNombreDuenio(String nombreDuenio) {
		this.nombreDuenio = nombreDuenio;
	}
	public String getDocumentoDuenio() {
		return documentoDuenio;
	}
	public void setDocumentoDuenio(String documentoDuenio) {
		this.documentoDuenio = documentoDuenio;
	}
	public String getNumeroRecarga() {
		return numeroRecarga;
	}
	public void setNumeroRecarga(String numeroRecarga) {
		this.numeroRecarga = numeroRecarga;
	}
	public String getQr() {
		return qr;
	}
	public void setQr(String qr) {
		this.qr = qr;
	}
	public List<ComparacionVenta> getComparacion() {
        return comparacion;
    }
    public void setComparacion(List<ComparacionVenta> comparacion) {
        this.comparacion = comparacion;
    }
}