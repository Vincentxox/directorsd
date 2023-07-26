package com.consystec.sc.ca.ws.output.etiquetamov;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RListEtiquetas")
public class RListEtiquetas {

	@XmlElement
	private String nombreId;
	@XmlElement
	private String nombre;
	@XmlElement
	private String valorId;
	@XmlElement
	private String valor;
	@XmlElement
	private String orden;
	@XmlElement
	private String mostrar;
	@XmlElement
	private String obligatorio;
	
	
	public String getNombreId() {
		return nombreId;
	}


	public void setNombreId(String nombreId) {
		this.nombreId = nombreId;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getValorId() {
		return valorId;
	}


	public void setValorId(String valorId) {
		this.valorId = valorId;
	}


	public String getValor() {
		return valor;
	}


	public void setValor(String valor) {
		this.valor = valor;
	}


	public String getOrden() {
		return orden;
	}


	public void setOrden(String orden) {
		this.orden = orden;
	}


	public String getMostrar() {
		return mostrar;
	}


	public void setMostrar(String mostrar) {
		this.mostrar = mostrar;
	}


	public String getObligatorio() {
		return obligatorio;
	}


	public void setObligatorio(String obligatorio) {
		this.obligatorio = obligatorio;
	}





	public RListEtiquetas(String nombreId, String nombre, String valorId,
			String valor, String orden, String mostrar, String obligatorio) {
		super();
		this.nombreId = nombreId;
		this.nombre = nombre;
		this.valorId = valorId;
		this.valor = valor;
		this.orden = orden;
		this.mostrar = mostrar;
		this.obligatorio = obligatorio;
	}


	public RListEtiquetas() {
		super();
	}
	
	
}
