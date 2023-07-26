package com.consystec.sc.ca.ws.output.cuenta;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.cuenta.InputCuenta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputDtsCuenta")
public class OutputDtsCuenta {

	@XmlElement
	private String idDts;
	@XmlElement
	private String nombre;
	@XmlElement
	private List<InputCuenta> cuentas;
	
	public String getIdDts() {
		return idDts;
	}
	public void setIdDts(String idDts) {
		this.idDts = idDts;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<InputCuenta> getCuentas() {
		return cuentas;
	}
	public void setCuentas(List<InputCuenta> cuentas) {
		this.cuentas = cuentas;
	}
	
	
}
