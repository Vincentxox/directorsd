package com.consystec.sc.ca.ws.output.fichacliente;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LegalAddressItem")
public class LegalAddressItem {
	@XmlElement
    private String IDGeographicArea;
	@XmlElement
    private String nameGeographicArea;
	public String getIDGeographicArea() {
		return IDGeographicArea;
	}
	public void setIDGeographicArea(String iDGeographicArea) {
		IDGeographicArea = iDGeographicArea;
	}
	public String getNameGeographicArea() {
		return nameGeographicArea;
	}
	public void setNameGeographicArea(String nameGeographicArea) {
		this.nameGeographicArea = nameGeographicArea;
	}
	
}
