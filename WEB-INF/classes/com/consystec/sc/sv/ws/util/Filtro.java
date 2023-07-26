package com.consystec.sc.sv.ws.util;

public class Filtro {
    private String comparacion;
    private String campo;
    private Object valor;
    private String operador;

    public static final String EQ = "=";
    public static final String NEQ = "!=";
    public static final String GT = ">";
    public static final String LT = "<";
    public static final String GTE = ">=";
    public static final String LTE = "<=";
    public static final String LIKE = "LIKE";
    public static final String ISNULL = "IS NULL";
    public static final String ISNOTNULL = "IS NOT NULL";
    public static final String IN = "IN";
    public static final String NOTIN = "NOT IN";
    public static final String BETWEEN = "BETWEEN";
    
    public static final String AND = "AND";
    public static final String OR = "OR";

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
    
    public String getComparacion() {
        return comparacion;
    }

    public void setComparacion(String comparacion) {
        this.comparacion = comparacion;
    }

    public Filtro(String field, String operator, Object value) {
        this.campo = field;
        this.operador = operator;
        this.valor = value;
    }
    
    public Filtro(String comparacion, String field, String operator, Object value) {
        this.comparacion = comparacion;
        this.campo = field;
        this.operador = operator;
        this.valor = value;
    }

}
