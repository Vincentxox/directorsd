package com.consystec.sc.ca.ws.input.portabilidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputProductoCustomer")
public class InputProductoCustomer {
	@XmlElement
	private String statusProduct;
	@XmlElement
	private String parentIDProduct;
	public String getStatusProduct() {
		return statusProduct;
	}
	public void setStatusProduct(String statusProduct) {
		this.statusProduct = statusProduct;
	}
	public String getParentIDProduct() {
		return parentIDProduct;
	}
	public void setParentIDProduct(String parentIDProduct) {
		this.parentIDProduct = parentIDProduct;
	}
	
	
}
