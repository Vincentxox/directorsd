package com.ericsson.sc.ca.ws.input.ventacredito;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AbonoVentaCredito")
public class AbonoVentaCredito {
	@XmlElement
	private String idAbonoVentaCredito;

	@XmlElement
	private String idventacredito;

	@XmlElement
	private String tcscventaid;

	@XmlElement
	private String fechapago;

	@XmlElement
	private String montopagado;

	public String getIdabonoventacredito() {
		return idAbonoVentaCredito;
	}

	public void setIdabonoventacredito(String idabonoventacredito) {
		this.idAbonoVentaCredito = idabonoventacredito;
	}

	public String getVentacreditoid() {
		return idventacredito;
	}

	public void setVentacreditoid(String ventacreditoid) {
		this.idventacredito = ventacreditoid;
	}

	public String getTcscventaid() {
		return tcscventaid;
	}

	public void setTcscventaid(String tcscventaid) {
		this.tcscventaid = tcscventaid;
	}

	public String getFechapago() {
		return fechapago;
	}

	public void setFechapago(String fechapago) {
		this.fechapago = fechapago;
	}

	public String getMontopagado() {
		return montopagado;
	}

	public void setMontopagado(String montopagado) {
		this.montopagado = montopagado;
	}

}
