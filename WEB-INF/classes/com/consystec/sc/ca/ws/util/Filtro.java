package com.consystec.sc.ca.ws.util;

public class Filtro {
	private String campo;
	private Object valor;
	private String operador;
	
	public static final String EQ      = "=";
	public static final String NEQ     = "!=";
	public static final String GT      = ">";
	public static final String LT      = "<";
	public static final String GTE     = ">=";
	public static final String LTE     = "<=";
	public static final String LIKE 	  = "like";
	public static final String ISNULL     = "is null";
	public static final String ISNOTNULL  = "is not null";
	public static final String IN		  = "in";
	public static final String NOTIN	  = "not in";
	
	public String getField() {
		return campo;
	}
	public void setField(String field) {
		this.campo = field;
	}
	public Object getValue() {
		return valor;
	}
	public void setValue(Object value) {
		this.valor = value;
	}
	
	public String getOperator() {
		return operador;
	}
	public void setOperator(String operator) {
		this.operador = operator;
	}
	
	public Filtro(String field, String operator, Object value) {
		this.campo 	  = field;
		this.operador = operator;
		this.valor 	  = value;
	}
	
}
