package com.consystec.sc.ca.ws.output.vendedorxdts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Outputvendedorxdts")
public class Outputvendedorxdts {
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<VendedorxDTS> vendedores;

	public Respuesta getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}

	public List<VendedorxDTS> getVendedores() {
		return vendedores;
	}

	public void setVendedores(List<VendedorxDTS> vendedores) {
		this.vendedores = vendedores;
	}
	
	
	
}
