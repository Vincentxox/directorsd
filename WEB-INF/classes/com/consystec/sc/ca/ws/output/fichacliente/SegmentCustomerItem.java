package com.consystec.sc.ca.ws.output.fichacliente;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputDatosClienteFS")
public class SegmentCustomerItem {
	@XmlElement
    private String IDSegmentCustomer;
	@XmlElement
    private String nameSegmentCustomer;
	public String getIDSegmentCustomer() {
		return IDSegmentCustomer;
	}
	public void setIDSegmentCustomer(String iDSegmentCustomer) {
		IDSegmentCustomer = iDSegmentCustomer;
	}
	public String getNameSegmentCustomer() {
		return nameSegmentCustomer;
	}
	public void setNameSegmentCustomer(String nameSegmentCustomer) {
		this.nameSegmentCustomer = nameSegmentCustomer;
	}
	
}
