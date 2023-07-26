package com.consystec.sc.ca.ws.output.pais;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputConsultaPais")
public class OutputConsultaPais {
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private String codArea;
    @XmlElement
    private String nombrePais;
    @XmlElement
    private String longMaxNumero;
    @XmlElement
    private String longMaxIdentificacion;
    @XmlElement
    private List<InputConsultaDepartamentos> departamentos;
    @XmlElement
    private List<InputConsultaRegion> regiones;
    
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputConsultaDepartamentos> getDepartamentos() {
        return departamentos;
    }
    public void setDepartamentos(List<InputConsultaDepartamentos> departamentos) {
        this.departamentos = departamentos;
    }
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getNombrePais() {
		return nombrePais;
	}
	public void setNombrePais(String nombrePais) {
		this.nombrePais = nombrePais;
	}
	public String getLongMaxNumero() {
		return longMaxNumero;
	}
	public void setLongMaxNumero(String longMaxNumero) {
		this.longMaxNumero = longMaxNumero;
	}
	public String getLongMaxIdentificacion() {
		return longMaxIdentificacion;
	}
	public void setLongMaxIdentificacion(String longMaxIdentificacion) {
		this.longMaxIdentificacion = longMaxIdentificacion;
	}
	public List<InputConsultaRegion> getRegiones() {
		return regiones;
	}
	public void setRegiones(List<InputConsultaRegion> regiones) {
		this.regiones = regiones;
	}
    
    
}