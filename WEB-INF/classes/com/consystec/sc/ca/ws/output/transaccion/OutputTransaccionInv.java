package com.consystec.sc.ca.ws.output.transaccion;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.tipotransaccion.InputTransaccionInv;
import com.consystec.sc.ca.ws.orm.Respuesta;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputTransaccionInv")
public class OutputTransaccionInv {
	@XmlElement
	private String idTransaccion;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<InputTransaccionInv> tiposTransaccion;
	public String getIdTransaccion() {
		return idTransaccion;
	}
	public void setIdTransaccion(String idTransaccion) {
		this.idTransaccion = idTransaccion;
	}
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	public List<InputTransaccionInv> getTiposTransaccion() {
		return tiposTransaccion;
	}
	public void setTiposTransaccion(List<InputTransaccionInv> tiposTransaccion) {
		this.tiposTransaccion = tiposTransaccion;
	}
	
	
	
}
