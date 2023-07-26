package com.consystec.sc.ca.ws.input.vendedorxdts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputVendxdts")
public class InputVendxdts {
	@XmlElement
	private String codArea;
	@XmlElement
	private String usuario;
	@XmlElement
	private String idDTS;
	@XmlElement
	private String idVendedor;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getIdDTS() {
		return idDTS;
	}
	public void setIdDTS(String idDTS) {
		this.idDTS = idDTS;
	}
	public String getIdVendedor() {
		return idVendedor;
	}
	public void setIdVendedor(String idVendedor) {
		this.idVendedor = idVendedor;
	}
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	

}
