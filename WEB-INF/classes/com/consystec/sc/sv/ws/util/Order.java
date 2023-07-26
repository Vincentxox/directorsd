package com.consystec.sc.sv.ws.util;

public class Order {
	private String field;
	private String dir;
	
	public static final String ASC   = "ASC";
	public static final String DESC  = "DESC";
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public Order(String field, String dir) {
		this.field 	  = field;
		this.dir 	  = dir;
	}
}
