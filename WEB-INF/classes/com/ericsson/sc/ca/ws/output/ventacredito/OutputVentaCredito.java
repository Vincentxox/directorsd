package com.ericsson.sc.ca.ws.output.ventacredito;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputVentaCredito")
public class OutputVentaCredito {
	@XmlElement
	private List<VentaCredito> listaventascredito;

	public List<VentaCredito> getListaventascredito() {
		return listaventascredito;
	}

	public void setListaventascredito(List<VentaCredito> listaventascredito) {
		this.listaventascredito = listaventascredito;
	}

}
