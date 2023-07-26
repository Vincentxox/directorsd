package com.consystec.sc.ca.ws.output.fichacliente;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputDatosClienteFS")
public class OutputDatosClienteFS {
	@XmlElement
    private String IdCustomer;
	@XmlElement
    private String nameCustomer;
	@XmlElement
    private String accountNumberCustomer;
	@XmlElement
    private List<IndividualContactItem> individualContactItem;
	@XmlElement
    private String taxId;
	@XmlElement
    private SegmentCustomerItem segmentCustomerItem;
	@XmlElement
    private LegalAddressItem legalAddresItem;
	@XmlElement
    private CustomerCategoryItem customerCategoryItem;
	@XmlElement
    private String customerStatusCustomer;
	@XmlElement
    private String preferredIDCustomerAccountContact;
	@XmlElement
	private String idGeographicLocation;
	
	public String getIdCustomer() {
		return IdCustomer;
	}
	public void setIdCustomer(String idCustomer) {
		IdCustomer = idCustomer;
	}
	public String getNameCustomer() {
		return nameCustomer;
	}
	public void setNameCustomer(String nameCustomer) {
		this.nameCustomer = nameCustomer;
	}
	public String getAccountNumberCustomer() {
		return accountNumberCustomer;
	}
	public void setAccountNumberCustomer(String accountNumberCustomer) {
		this.accountNumberCustomer = accountNumberCustomer;
	}
	public List<IndividualContactItem> getIndividualContactItem() {
		return individualContactItem;
	}
	public void setIndividualContactItem(
			List<IndividualContactItem> individualContactItem) {
		this.individualContactItem = individualContactItem;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public SegmentCustomerItem getSegmentCustomerItem() {
		return segmentCustomerItem;
	}
	public void setSegmentCustomerItem(SegmentCustomerItem segmentCustomerItem) {
		this.segmentCustomerItem = segmentCustomerItem;
	}
	public LegalAddressItem getLegalAddresItem() {
		return legalAddresItem;
	}
	public void setLegalAddresItem(LegalAddressItem legalAddresItem) {
		this.legalAddresItem = legalAddresItem;
	}
	public CustomerCategoryItem getCustomerCategoryItem() {
		return customerCategoryItem;
	}
	public void setCustomerCategoryItem(CustomerCategoryItem customerCategoryItem) {
		this.customerCategoryItem = customerCategoryItem;
	}
	public String getCustomerStatusCustomer() {
		return customerStatusCustomer;
	}
	public void setCustomerStatusCustomer(String customerStatusCustomer) {
		this.customerStatusCustomer = customerStatusCustomer;
	}
	public String getPreferredIDCustomerAccountContact() {
		return preferredIDCustomerAccountContact;
	}
	public void setPreferredIDCustomerAccountContact(
			String preferredIDCustomerAccountContact) {
		this.preferredIDCustomerAccountContact = preferredIDCustomerAccountContact;
	}
	public String getIdGeographicLocation() {
		return idGeographicLocation;
	}
	public void setIdGeographicLocation(String idGeographicLocation) {
		this.idGeographicLocation = idGeographicLocation;
	}
	
	
}	