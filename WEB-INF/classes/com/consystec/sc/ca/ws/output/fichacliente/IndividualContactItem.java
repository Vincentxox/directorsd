package com.consystec.sc.ca.ws.output.fichacliente;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "individualContactItem")
public class IndividualContactItem {
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
    private String aliveDuringIndividual;
	@XmlElement
    private String statusCustomerAccountContact;
	@XmlElement
    private IndividualIdentificationItem individualIdentificationItem;
	@XmlElement
    private String salaryIndividual;
	@XmlElement
    private List<ContactMediumItem> contactMedium;
	@XmlElement
    private String emailID;
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
	public String getAliveDuringIndividual() {
		return aliveDuringIndividual;
	}
	public void setAliveDuringIndividual(String aliveDuringIndividual) {
		this.aliveDuringIndividual = aliveDuringIndividual;
	}
	public String getStatusCustomerAccountContact() {
		return statusCustomerAccountContact;
	}
	public void setStatusCustomerAccountContact(String statusCustomerAccountContact) {
		this.statusCustomerAccountContact = statusCustomerAccountContact;
	}
	public IndividualIdentificationItem getIndividualIdentificationItem() {
		return individualIdentificationItem;
	}
	public void setIndividualIdentificationItem(
			IndividualIdentificationItem individualIdentificationItem) {
		this.individualIdentificationItem = individualIdentificationItem;
	}
	public String getSalaryIndividual() {
		return salaryIndividual;
	}
	public void setSalaryIndividual(String salaryIndividual) {
		this.salaryIndividual = salaryIndividual;
	}
	public List<ContactMediumItem> getContactMedium() {
		return contactMedium;
	}
	public void setContactMedium(List<ContactMediumItem> contactMedium) {
		this.contactMedium = contactMedium;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	
	
}
