package com.consystec.sc.ca.ws.output.venta;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.venta.DetallePago;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputVisita")
public class OutputDetallePago {
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<DetallePago> detallePago;

	public Respuesta getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}

	public List<DetallePago> getDetallePago() {
		return detallePago;
	}

	public void setDetallePago(List<DetallePago> detallePago) {
		this.detallePago = detallePago;
	}
}
