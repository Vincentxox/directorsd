package com.consystec.sc.ca.ws.output.portabilidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "VentaPorta")
public class VentaPorta {
@XmlElement
 private String idVenta;
@XmlElement
 private String numeroaPortar;
@XmlElement
 private String numeroTemporal;
@XmlElement
private String operadorDonante;
@XmlElement
 private String estado;
public String getIdVenta() {
	return idVenta;
}
public void setIdVenta(String idVenta) {
	this.idVenta = idVenta;
}
public String getNumeroaPortar() {
	return numeroaPortar;
}
public void setNumeroaPortar(String numeroaPortar) {
	this.numeroaPortar = numeroaPortar;
}
public String getNumeroTemporal() {
	return numeroTemporal;
}
public void setNumeroTemporal(String numeroTemporal) {
	this.numeroTemporal = numeroTemporal;
}
public String getEstado() {
	return estado;
}
public void setEstado(String estado) {
	this.estado = estado;
}
public String getOperadorDonante() {
	return operadorDonante;
}
public void setOperadorDonante(String operadorDonante) {
	this.operadorDonante = operadorDonante;
}
 

}
