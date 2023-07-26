package com.consystec.sc.ca.ws.input.pdv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputPDVDirec")
public class InputPDVDirec {
 
	@XmlElement
	private String token;
	@XmlElement
	private InputPDV pdv;
	

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public InputPDV getPdv() {
		return pdv;
	}
	public void setPdv(InputPDV pdv) {
		this.pdv = pdv;
	}
}