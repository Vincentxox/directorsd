package com.consystec.sc.ca.ws.output.portabilidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InformacionClienteFS")
public class OutputInformacionClienteFS {
	@XmlElement
    private String IDCustomer;
	@XmlElement
    private String accountNumberCustomer;
	@XmlElement
    private String typeCustomer;
	@XmlElement
    private String IDCustomerAccountContact;
	@XmlElement
    private String givenNamesIndividualName;
	@XmlElement
    private String middleNamesIndividualName;
	@XmlElement
    private String firstLastNameIndividualName;
	@XmlElement
    private String secondLastNameIndividualName;
	@XmlElement
    private String IDIndividualIdentification;
	@XmlElement
    private String identificationNumberIndividualIdentification;
	@XmlElement
    private String identificationTypeIndividualIdentification;
	@XmlElement
	private String locationAttachment;
	@XmlElement
	private String IDSegmentCustomer;
	@XmlElement
	private String nameSegmentCustomer;
	@XmlElement
	private String instanceIdProduct;

	
	public String getIDCustomer() {
		return IDCustomer;
	}
	public void setIDCustomer(String iDCustomer) {
		IDCustomer = iDCustomer;
	}
	public String getAccountNumberCustomer() {
		return accountNumberCustomer;
	}
	public void setAccountNumberCustomer(String accountNumberCustomer) {
		this.accountNumberCustomer = accountNumberCustomer;
	}
	public String getTypeCustomer() {
		return typeCustomer;
	}
	public void setTypeCustomer(String typeCustomer) {
		this.typeCustomer = typeCustomer;
	}
	public String getIDCustomerAccountContact() {
		return IDCustomerAccountContact;
	}
	public void setIDCustomerAccountContact(String iDCustomerAccountContact) {
		IDCustomerAccountContact = iDCustomerAccountContact;
	}
	public String getGivenNamesIndividualName() {
		return givenNamesIndividualName;
	}
	public void setGivenNamesIndividualName(String givenNamesIndividualName) {
		this.givenNamesIndividualName = givenNamesIndividualName;
	}
	public String getMiddleNamesIndividualName() {
		return middleNamesIndividualName;
	}
	public void setMiddleNamesIndividualName(String middleNamesIndividualName) {
		this.middleNamesIndividualName = middleNamesIndividualName;
	}
	public String getFirstLastNameIndividualName() {
		return firstLastNameIndividualName;
	}
	public void setFirstLastNameIndividualName(String firstLastNameIndividualName) {
		this.firstLastNameIndividualName = firstLastNameIndividualName;
	}
	public String getSecondLastNameIndividualName() {
		return secondLastNameIndividualName;
	}
	public void setSecondLastNameIndividualName(String secondLastNameIndividualName) {
		this.secondLastNameIndividualName = secondLastNameIndividualName;
	}
	public String getIDIndividualIdentification() {
		return IDIndividualIdentification;
	}
	public void setIDIndividualIdentification(String iDIndividualIdentification) {
		IDIndividualIdentification = iDIndividualIdentification;
	}
	public String getIdentificationNumberIndividualIdentification() {
		return identificationNumberIndividualIdentification;
	}
	public void setIdentificationNumberIndividualIdentification(String identificationNumberIndividualIdentification) {
		this.identificationNumberIndividualIdentification = identificationNumberIndividualIdentification;
	}
	public String getIdentificationTypeIndividualIdentification() {
		return identificationTypeIndividualIdentification;
	}
	public void setIdentificationTypeIndividualIdentification(String identificationTypeIndividualIdentification) {
		this.identificationTypeIndividualIdentification = identificationTypeIndividualIdentification;
	}
	public String getLocationAttachment() {
		return locationAttachment;
	}
	public void setLocationAttachment(String locationAttachment) {
		this.locationAttachment = locationAttachment;
	}
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
	public String getInstanceIdProduct() {
		return instanceIdProduct;
	}
	public void setInstanceIdProduct(String instanceIdProduct) {
		this.instanceIdProduct = instanceIdProduct;
	}
	
	
	
}
