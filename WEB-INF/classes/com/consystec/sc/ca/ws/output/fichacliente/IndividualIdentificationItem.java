package com.consystec.sc.ca.ws.output.fichacliente;

import javax.xml.bind.annotation.XmlElement;

public class IndividualIdentificationItem {
	@XmlElement
    private String IDIndividualIdentification;
	@XmlElement
    private String identificationTypeIndividualIdentification;
	@XmlElement
    private String identificationNumberIndividualIdentification;
	public String getIDIndividualIdentification() {
		return IDIndividualIdentification;
	}
	public void setIDIndividualIdentification(String iDIndividualIdentification) {
		IDIndividualIdentification = iDIndividualIdentification;
	}
	public String getIdentificationTypeIndividualIdentification() {
		return identificationTypeIndividualIdentification;
	}
	public void setIdentificationTypeIndividualIdentification(
			String identificationTypeIndividualIdentification) {
		this.identificationTypeIndividualIdentification = identificationTypeIndividualIdentification;
	}
	public String getIdentificationNumberIndividualIdentification() {
		return identificationNumberIndividualIdentification;
	}
	public void setIdentificationNumberIndividualIdentification(
			String identificationNumberIndividualIdentification) {
		this.identificationNumberIndividualIdentification = identificationNumberIndividualIdentification;
	}
	
}
