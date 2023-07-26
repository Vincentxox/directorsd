package com.consystec.sc.ca.ws.input.catalogo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "inputCatalogo")
public class InputCatalogo {
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String grupoParametro;
    @XmlElement
    private List<InputParametro> parametros;

    public List<InputParametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<InputParametro> parametros) {
		this.parametros = parametros;
	}

	public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getGrupoParametro() {
        return grupoParametro;
    }

    public void setGrupoParametro(String grupoParametro) {
        this.grupoParametro = grupoParametro;
    }

    public String getCodArea() {
        return codArea;
    }

    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
}
