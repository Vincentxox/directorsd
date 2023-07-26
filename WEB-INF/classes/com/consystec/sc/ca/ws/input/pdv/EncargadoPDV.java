package com.consystec.sc.ca.ws.input.pdv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "EncargadoPDV")
public class EncargadoPDV {
    @XmlElement
    private String codArea;
	@XmlElement
	private String idEncargadoPDV;
	@XmlElement
	private String nombres;
	@XmlElement
	private String apellidos;
	@XmlElement
	private String telefono;
	@XmlElement
	private String cedula;
	@XmlElement
	private String tipoDocumento;
	@XmlElement
	private String estado;
	
    public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getIdEncargadoPDV() {
        return idEncargadoPDV;
    }
    public void setIdEncargadoPDV(String idEncargadoPDV) {
        this.idEncargadoPDV = idEncargadoPDV;
    }
    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getCedula() {
        return cedula;
    }
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}