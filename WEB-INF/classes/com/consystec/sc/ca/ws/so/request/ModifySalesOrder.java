package com.consystec.sc.ca.ws.so.request;

import java.util.List;

public class ModifySalesOrder {
	private String usuario;
	private String codArea;
	private String idCustomer;
	private String idCustomerOrder;
	private String idDistributionChanel;
	private String idLocation;
	private String idCustomerOrderitem;
	private List<AtributosModify> atributos;
	
	public String getIdCustomer() {
		return idCustomer;
	}
	public void setIdCustomer(String idCustomer) {
		this.idCustomer = idCustomer;
	}
	public String getIdDistributionChanel() {
		return idDistributionChanel;
	}
	public void setIdDistributionChanel(String idDistributionChanel) {
		this.idDistributionChanel = idDistributionChanel;
	}
	public String getIdLocation() {
		return idLocation;
	}
	public void setIdLocation(String idLocation) {
		this.idLocation = idLocation;
	}
	public String getIdCustomerOrder() {
		return idCustomerOrder;
	}
	public void setIdCustomerOrder(String idCustomerOrder) {
		this.idCustomerOrder = idCustomerOrder;
	}
	public List<AtributosModify> getAtributos() {
		return atributos;
	}
	public void setAtributos(List<AtributosModify> atributos) {
		this.atributos = atributos;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getIdCustomerOrderitem() {
		return idCustomerOrderitem;
	}
	public void setIdCustomerOrderitem(String idCustomerOrderitem) {
		this.idCustomerOrderitem = idCustomerOrderitem;
	}
}
