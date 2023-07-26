package com.consystec.sc.ca.ws.input.ticket;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputTicket")
public class OutputTicket {
	@XmlElement
	private String token;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private String idVenta;
	@XmlElement
	private List<Lineas> lineas;
	@XmlElement
	private String linkOLS;
	
		public Respuesta getRespuesta() {
			return respuesta;
		}
		public void setRespuesta(Respuesta respuesta) {
			this.respuesta = respuesta;
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
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		
		public String getLinkOLS() {
			return linkOLS;
		}
		public void setLinkOLS(String linkOLS) {
			this.linkOLS = linkOLS;
		}
	    
}
