package com.consystec.sc.ca.ws.input.ruta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputRutaPdv")

public class InputRutaPdv {

		@XmlElement
		private String codArea;
		@XmlElement
		private String usuario;
		@XmlElement
		private String idRuta;
		@XmlElement
		private String[] pdv;
		@XmlElement
		private String asociacion;
		
		public String getUsuario() {
			return usuario;
		}
		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}
		public String getIdRuta() {
			return idRuta;
		}
		public void setIdRuta(String idRuta) {
			this.idRuta = idRuta;
		}
		public String[] getPdv() {
			return pdv;
		}
		public void setPdv(String[] pdv) {
			this.pdv = pdv;
		}
		public String getAsociacion() {
			return asociacion;
		}
		public void setAsociacion(String asociacion) {
			this.asociacion = asociacion;
		}
		public String getCodArea() {
			return codArea;
		}
		public void setCodArea(String codArea) {
			this.codArea = codArea;
		}
		
}
