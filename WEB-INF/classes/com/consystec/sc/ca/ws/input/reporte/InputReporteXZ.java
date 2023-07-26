package com.consystec.sc.ca.ws.input.reporte;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputReporteXZ")
public class InputReporteXZ {
	@XmlElement
	private String  token;
	@XmlElement
	private String  codArea;
	@XmlElement
	private String  usuario;
	@XmlElement
	private String codDispositivo;
	@XmlElement
	private String idDistribuidor;
	@XmlElement
	private String idJornada;
	@XmlElement
	private String idTipo;
	@XmlElement
	private String tipo;
	@XmlElement
	private String idVendedor;
	@XmlElement
	private String[] dispositivos;
	@XmlElement
	private String tipoReporte;
	@XmlElement
	private String origen; 
	@XmlElement
	private String fecha;
	@XmlElement
	private String idReporte;
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getIdDistribuidor() {
		return idDistribuidor;
	}

	public void setIdDistribuidor(String idDistribuidor) {
		this.idDistribuidor = idDistribuidor;
	}

	public String getIdJornada() {
		return idJornada;
	}

	public void setIdJornada(String idJornada) {
		this.idJornada = idJornada;
	}

	public String getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(String idTipo) {
		this.idTipo = idTipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getIdVendedor() {
		return idVendedor;
	}

	public void setIdVendedor(String idVendedor) {
		this.idVendedor = idVendedor;
	}

	public String getCodDispositivo() {
		return codDispositivo;
	}

	public void setCodDispositivo(String codDispositivo) {
		this.codDispositivo = codDispositivo;
	}

	public String getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCodArea() {
		return codArea;
	}

	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}

	public String getIdReporte() {
		return idReporte;
	}

	public void setIdReporte(String idReporte) {
		this.idReporte = idReporte;
	}

	public String[] getDispositivos() {
		return dispositivos;
	}

	public void setDispositivos(String[] dispositivos) {
		this.dispositivos = dispositivos;
	}
	
	
	
}
