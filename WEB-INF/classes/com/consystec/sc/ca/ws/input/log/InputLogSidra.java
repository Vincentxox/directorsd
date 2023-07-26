package com.consystec.sc.ca.ws.input.log;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputLogSidra")
public class InputLogSidra {
    @XmlElement
    private String codArea;
	@XmlElement
	private String usuario;
	@XmlElement
	private List<LogSidra> log;

	public String getCodArea() {
        return codArea;
    }

    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }

    public List<LogSidra> getLog() {
		return log;
	}

	public void setLog(List<LogSidra> log) {
		this.log = log;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	
}
