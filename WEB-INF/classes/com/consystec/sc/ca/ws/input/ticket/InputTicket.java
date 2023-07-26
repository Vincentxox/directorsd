package com.consystec.sc.ca.ws.input.ticket;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputTicket")
public class InputTicket {
	  @XmlElement
	    private String token;
	    @XmlElement
	    private String codArea;
	    @XmlElement
	    private String usuario;
	    @XmlElement
	    private String codDispositivo;
	    @XmlElement
	    private String idVenta;
	    @XmlElement
	    private List<Lineas> lineas;
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
		public String getUsuario() {
			return usuario;
		}
		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}
		public String getCodDispositivo() {
			return codDispositivo;
		}
		public void setCodDispositivo(String codDispositivo) {
			this.codDispositivo = codDispositivo;
		}
		public String getIdVenta() {
			return idVenta;
		}
		public void setIdVenta(String idVenta) {
			this.idVenta = idVenta;
		}
		public List<Lineas> getLineas() {
			return lineas;
		}
		public void setLineas(List<Lineas> lineas) {
			this.lineas = lineas;
		}
	    
	    
	    
}
