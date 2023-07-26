package com.consystec.sc.ca.ws.input.portabilidad;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
public class InputCreaPortabilidad {
	@XmlElement
	private String usuario;
	@XmlElement
	private String tcsccatpaisid;
	@XmlElement
	private String idJornada;
	@XmlElement
	private String idVendedor;
	@XmlElement
	private String codArea;
	@XmlElement
	private String token;
	@XmlElement
	private String codDispositivo;
	@XmlElement
	private String idPortaMovil;
	@XmlElement
	private String numPortar;
	@XmlElement
	private String operadorDonante;
	@XmlElement
	private String cip;
	@XmlElement
	private String productoDonante;
	@XmlElement
	private String numTemporal;
	@XmlElement
	private String primerNombre;
	@XmlElement
	private String segundoNombre;
	@XmlElement
	private String primerApellido;
	@XmlElement
	private String segundoApellido;
	@XmlElement
	private String tipoDocumento;
	@XmlElement
	private String noDocumento;
	@XmlElement
    private List<InputCargaAdjuntoPorta> adjuntoporta;
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCodDispositivo() {
		return codDispositivo;
	}
	public void setCodDispositivo(String codDispositivo) {
		this.codDispositivo = codDispositivo;
	}
	public String getIdPortaMovil() {
		return idPortaMovil;
	}
	public void setIdPortaMovil(String idPortaMovil) {
		this.idPortaMovil = idPortaMovil;
	}
	public String getNumPortar() {
		return numPortar;
	}
	public void setNumPortar(String numPortar) {
		this.numPortar = numPortar;
	}
	public String getOperadorDonante() {
		return operadorDonante;
	}
	public void setOperadorDonante(String operadorDonante) {
		this.operadorDonante = operadorDonante;
	}
	public String getCip() {
		return cip;
	}
	public void setCip(String cip) {
		this.cip = cip;
	}
	public String getProductoDonante() {
		return productoDonante;
	}
	public void setProductoDonante(String productoDonante) {
		this.productoDonante = productoDonante;
	}
	public String getNumTemporal() {
		return numTemporal;
	}
	public void setNumTemporal(String numTemporal) {
		this.numTemporal = numTemporal;
	}
	public String getPrimerNombre() {
		return primerNombre;
	}
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}
	public String getSegundoNombre() {
		return segundoNombre;
	}
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}
	public String getPrimerApellido() {
		return primerApellido;
	}
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	public String getSegundoApellido() {
		return segundoApellido;
	}
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getNoDocumento() {
		return noDocumento;
	}
	public void setNoDocumento(String noDocumento) {
		this.noDocumento = noDocumento;
	}
	public String getTcsccatpaisid() {
		return tcsccatpaisid;
	}
	public void setTcsccatpaisid(String tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
	}
	public String getIdJornada() {
		return idJornada;
	}
	public void setIdJornada(String idJornada) {
		this.idJornada = idJornada;
	}
	public List<InputCargaAdjuntoPorta> getAdjuntoporta() {
		return adjuntoporta;
	}
	public void setAdjuntoporta(List<InputCargaAdjuntoPorta> adjuntoporta) {
		this.adjuntoporta = adjuntoporta;
	}
	public String getIdVendedor() {
		return idVendedor;
	}
	public void setIdVendedor(String idVendedor) {
		this.idVendedor = idVendedor;
	}

}
