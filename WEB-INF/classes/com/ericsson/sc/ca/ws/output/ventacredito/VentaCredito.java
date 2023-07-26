package com.ericsson.sc.ca.ws.output.ventacredito;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ericsson.sc.ca.ws.input.ventacredito.AbonoVentaCredito;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "VentaCredito")
public class VentaCredito {

	@XmlElement
	private String idventacredito;

	@XmlElement
	private String iddispositivo;

	@XmlElement
	private String idventa;
	@XmlElement
	private String idpdv;
	@XmlElement
	private String idpais;
	@XmlElement
	private String idusuario;
	@XmlElement
	private String serie;
	@XmlElement
	private String numeroFactura;
	@XmlElement
	private String fechaVenta;
	@XmlElement
	private String monto;
	@XmlElement
	private String saldo;
	
	@XmlElement
	private List<AbonoVentaCredito> listabonos;
	
		
	public List<AbonoVentaCredito> getListabonos() {
		return listabonos;
	}

	public void setListabonos(List<AbonoVentaCredito> listabonos) {
		this.listabonos = listabonos;
	}

	public String getIddispositivo() {
		return iddispositivo;
	}

	public void setIddispositivo(String iddispositivo) {
		this.iddispositivo = iddispositivo;
	}

	public String getIdventacredito() {
		return idventacredito;
	}

	public void setIdventacredito(String idventacredito) {
		this.idventacredito = idventacredito;
	}

	public String getIdventa() {
		return idventa;
	}

	public void setIdventa(String idventa) {
		this.idventa = idventa;
	}

	public String getIdpdv() {
		return idpdv;
	}

	public void setIdpdv(String idpdv) {
		this.idpdv = idpdv;
	}

	public String getIdpais() {
		return idpais;
	}

	public void setIdpais(String idpais) {
		this.idpais = idpais;
	}

	public String getIdusuario() {
		return idusuario;
	}

	public void setIdusuario(String idusuario) {
		this.idusuario = idusuario;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public String getFechaVenta() {
		return fechaVenta;
	}

	public void setFechaVenta(String fechaVenta) {
		this.fechaVenta = fechaVenta;
	}

	public String getMonto() {
		return monto;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public String getSaldo() {
		return saldo;
	}

	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

}
