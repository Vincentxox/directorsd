package com.consystec.sc.ca.ws.output.portabilidad;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import com.consystec.sc.ca.ws.orm.Respuesta;
public class OutputEstadoPortabilidad {
	@XmlElement
    private String token;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
	private List<VentaPorta> ventaportadetalle;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	public List<VentaPorta> getVentaportadetalle() {
		return ventaportadetalle;
	}
	public void setVentaportadetalle(List<VentaPorta> ventaportadetalle) {
		this.ventaportadetalle = ventaportadetalle;
	}
	
}
