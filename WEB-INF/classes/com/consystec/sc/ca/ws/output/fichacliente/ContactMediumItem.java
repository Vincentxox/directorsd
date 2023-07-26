package com.consystec.sc.ca.ws.output.fichacliente;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ContactMediumItem")
public class ContactMediumItem {
	@XmlElement
    private String IDContactMedium;
	@XmlElement
    private String nameContactMedium;
	@XmlElement
    private String typeContactMedium;
	@XmlElement
    private String valueContactMedium;
	public String getIDContactMedium() {
		return IDContactMedium;
	}
	public void setIDContactMedium(String iDContactMedium) {
		IDContactMedium = iDContactMedium;
	}
	public String getNameContactMedium() {
		return nameContactMedium;
	}
	public void setNameContactMedium(String nameContactMedium) {
		this.nameContactMedium = nameContactMedium;
	}
	public String getTypeContactMedium() {
		return typeContactMedium;
	}
	public void setTypeContactMedium(String typeContactMedium) {
		this.typeContactMedium = typeContactMedium;
	}
	public String getValueContactMedium() {
		return valueContactMedium;
	}
	public void setValueContactMedium(String valueContactMedium) {
		this.valueContactMedium = valueContactMedium;
	}
	
}
