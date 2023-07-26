package com.consystec.sc.ca.ws.output.solicitudescip;

import javax.xml.bind.annotation.XmlElement;
import com.consystec.sc.ca.ws.orm.Respuesta;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputSolicitudesCip")
public class OutputSolicitudesCip {
	 	@XmlElement
		private String token ;
		@XmlElement	    
	    private Respuesta respuesta;
	    		
		public Respuesta getRespuesta() {
			return respuesta;
		}
		public void setRespuesta(Respuesta respuesta) {
			this.respuesta = respuesta;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}			
}
