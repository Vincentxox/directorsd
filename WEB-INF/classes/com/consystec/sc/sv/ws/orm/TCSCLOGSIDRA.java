package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TCSCLOGSIDRA {
	public static final String N_TABLA = "TC_SC_LOG_SIDRA";
	public static final String SEQUENCE = "TC_SC_LOG_SIDRA_SQ.nextval";
	
	public final static String CAMPO_TCSCLOGSIDRAID = "tcsclogsidraid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	public final static String CAMPO_TIPO_TRANSACCION = "tipo_transaccion";
	public final static String CAMPO_ORIGEN = "origen";
	public final static String CAMPO_ID = "id";
	public final static String CAMPO_TIPO_ID = "tipo_id";
	public final static String CAMPO_DESCRIPCION = "descripcion";
	public final static String CAMPO_RESULTADO = "resultado";
	public final static String CAMPO_FECHA_LOG = "fecha_log";
	public final static String CAMPO_USUARIO = "usuario";
	public final static String CAMPO_DESCRIPCION_ERROR = "descripcion_error";

	private BigDecimal tcsclogsidraid;
    private BigDecimal tcsccatpaisid;
	private String tipo_transaccion;
	private String origen;
	private BigDecimal id;
	private String tipo_id;
	private String descripcion;
	private String resultado;
	private Timestamp fecha_log;
	private String usuario;
	private String descripcion_error;
	public BigDecimal getTcsclogsidraid() {
		return tcsclogsidraid;
	}
	public void setTcsclogsidraid(BigDecimal tcsclogsidraid) {
		this.tcsclogsidraid = tcsclogsidraid;
	}
	public BigDecimal getTcsccatpaisid() {
        return tcsccatpaisid;
    }
    public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
        this.tcsccatpaisid = tcsccatpaisid;
    }
    public String getTipo_transaccion() {
		return tipo_transaccion;
	}
	public void setTipo_transaccion(String tipo_transaccion) {
		this.tipo_transaccion = tipo_transaccion;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public BigDecimal getId() {
		return id;
	}
	public void setId(BigDecimal id) {
		this.id = id;
	}
	public String getTipo_id() {
		return tipo_id;
	}
	public void setTipo_id(String tipo_id) {
		this.tipo_id = tipo_id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public Timestamp getFecha_log() {
		return fecha_log;
	}
	public void setFecha_log(Timestamp fecha_log) {
		this.fecha_log = fecha_log;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getDescripcion_error() {
		return descripcion_error;
	}
	public void setDescripcion_error(String descripcion_error) {
		this.descripcion_error = descripcion_error;
	}
	
	
}
