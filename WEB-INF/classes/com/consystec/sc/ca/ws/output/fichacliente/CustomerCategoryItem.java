package com.consystec.sc.ca.ws.output.fichacliente;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CustomerCategoryItem")
public class CustomerCategoryItem {
	@XmlElement
    private String IDCustomerCategory;
	@XmlElement
    private String nameCustomerCategory;
	public String getIDCustomerCategory() {
		return IDCustomerCategory;
	}
	public void setIDCustomerCategory(String iDCustomerCategory) {
		IDCustomerCategory = iDCustomerCategory;
	}
	public String getNameCustomerCategory() {
		return nameCustomerCategory;
	}
	public void setNameCustomerCategory(String nameCustomerCategory) {
		this.nameCustomerCategory = nameCustomerCategory;
	}
	
}
