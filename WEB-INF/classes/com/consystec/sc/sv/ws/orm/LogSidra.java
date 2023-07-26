package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LogSidra")
public class LogSidra {
    @XmlElement
    private String codArea;
    @XmlElement
    private String tipoTransaccion;
    @XmlElement
    private String origen;
    @XmlElement
    private String id;
    @XmlElement
    private String tipoId;
    @XmlElement
    private String resultado;
    @XmlElement
    private String descripcionError;
    @XmlElement
    private BigDecimal tcsclogsidraid;

    public String getCodArea() {
		return codArea;
	}

	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}

	public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipoId() {
        return tipoId;
    }

    public void setTipoId(String tipoId) {
        this.tipoId = tipoId;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getDescripcionError() {
        return descripcionError;
    }

    public void setDescripcionError(String descripcionError) {
        this.descripcionError = descripcionError;
    }

	public BigDecimal getTcsclogsidraid() {
		return tcsclogsidraid;
	}

	public void setTcsclogsidraid(BigDecimal tcsclogsidraid) {
		this.tcsclogsidraid = tcsclogsidraid;
	}
}