package com.consystec.sc.sv.ws.orm;
import java.math.BigDecimal;
public class Portabilidad {
	
	public static final String N_TABLA = "TC_SC_PORTABILIDAD";
	public static final String SEQUENCE = "TC_SC_PORTABILIDAD_SQ.nextval";

	 public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	public final static String CAMPO_TCSCPORTABILIDADID="tcscportabilidadid";
	public final static String CAMPO_IDPORTAMOVIL="idportamovil";
	public final static String CAMPO_SALESORDERID="salesorderid";
    public final static String CAMPO_TCSCJORNADAVENID="tcscjornadavenid";
    public final static String CAMPO_COD_DISPOSITIVO="cod_dispositivo";
	public final static String CAMPO_NUM_PORTAR="num_portar";
	public final static String CAMPO_CIP="cip";
	public final static String CAMPO_TIPO_PRODUCTO="tipo_producto";
	public final static String CAMPO_NUM_TEMPORAL="num_temporal";
	public final static String CAMPO_OBJECT_ID_NUM_TEMPORAL="object_id_num_temporal";
	public final static String CAMPO_PRIMER_NOMBRE="primer_nombre";
	public final static String CAMPO_SEGUNDO_NOMBRE="segundo_nombre";
	public final static String CAMPO_PRIMER_APELLIDO="primer_apellido";
	public final static String CAMPO_SEGUNDO_APELLIDO="segundo_apellido";
	public final static String CAMPO_TIPO_DOCUMENTO="tipo_documento";
	public final static String CAMPO_NO_DOCUMENTO="no_documento";
	public final static String CAMPO_CREADO_EL="creado_el";
	public final static String CAMPO_CREADO_POR="creado_por";
	public final static String CAMPO_MODIFICADO_EL="modificado_el";
	public final static String CAMPO_MODIFICADO_POR="modificado_por";
	public final static String CAMPO_ID_VENDEDOR="id_vendedor";
	public final static String CAMPO_OPERADOR_DONANTE="operador_donante";
	
	private BigDecimal tcsccatpaisid;
	private BigDecimal tcscportabilidadid;
	private BigDecimal idportamovil;
	private BigDecimal salesorderid;
	private BigDecimal tcscjornadavenid;
	private String cod_dispositivo;
	private BigDecimal num_portar;
	private BigDecimal cip;
	private String tipo_producto;
	private String num_temporal;
	private BigDecimal object_id_num_temporal;
	private String primer_nombre;
	private String segundo_nombre;
	private String primer_apellido;
	private String segundo_apellido;
	private String tipo_documento;
	private String no_documento;
	private String creado_el;
	private String creado_por;
	private String modificado_el;
	private String modificado_por;
	private String operador_donante;
	private BigDecimal id_vendedor;
	public BigDecimal getTcscportabilidadid() {
		return tcscportabilidadid;
	}
	public void setTcscportabilidadid(BigDecimal tcscportabilidadid) {
		this.tcscportabilidadid = tcscportabilidadid;
	}
	public BigDecimal getIdportamovil() {
		return idportamovil;
	}
	public void setIdportamovil(BigDecimal idportamovil) {
		this.idportamovil = idportamovil;
	}
	public BigDecimal getSalesorderid() {
		return salesorderid;
	}
	public void setSalesorderid(BigDecimal salesorderid) {
		this.salesorderid = salesorderid;
	}
	public BigDecimal getNum_portar() {
		return num_portar;
	}
	public void setNum_portar(BigDecimal num_portar) {
		this.num_portar = num_portar;
	}
	public BigDecimal getCip() {
		return cip;
	}
	public void setCip(BigDecimal cip) {
		this.cip = cip;
	}
	public String getTipo_producto() {
		return tipo_producto;
	}
	public void setTipo_producto(String tipo_producto) {
		this.tipo_producto = tipo_producto;
	}
	public String getNum_temporal() {
		return num_temporal;
	}
	public void setNum_temporal(String num_temporal) {
		this.num_temporal = num_temporal;
	}
	public BigDecimal getObject_id_num_temporal() {
		return object_id_num_temporal;
	}
	public void setObject_id_num_temporal(BigDecimal object_id_num_temporal) {
		this.object_id_num_temporal = object_id_num_temporal;
	}
	public String getPrimer_nombre() {
		return primer_nombre;
	}
	public void setPrimer_nombre(String primer_nombre) {
		this.primer_nombre = primer_nombre;
	}
	public String getSegundo_nombre() {
		return segundo_nombre;
	}
	public void setSegundo_nombre(String segundo_nombre) {
		this.segundo_nombre = segundo_nombre;
	}
	public String getPrimer_apellido() {
		return primer_apellido;
	}
	public void setPrimer_apellido(String primer_apellido) {
		this.primer_apellido = primer_apellido;
	}
	public String getSegundo_apellido() {
		return segundo_apellido;
	}
	public void setSegundo_apellido(String segundo_apellido) {
		this.segundo_apellido = segundo_apellido;
	}
	public String getTipo_documento() {
		return tipo_documento;
	}
	public void setTipo_documento(String tipo_documento) {
		this.tipo_documento = tipo_documento;
	}
	public String getNo_documento() {
		return no_documento;
	}
	public void setNo_documento(String no_documento) {
		this.no_documento = no_documento;
	}
	public String getCreado_el() {
		return creado_el;
	}
	public void setCreado_el(String creado_el) {
		this.creado_el = creado_el;
	}
	public String getCreado_por() {
		return creado_por;
	}
	public void setCreado_por(String creado_por) {
		this.creado_por = creado_por;
	}
	public String getModificado_el() {
		return modificado_el;
	}
	public void setModificado_el(String modificado_el) {
		this.modificado_el = modificado_el;
	}
	public String getModificado_por() {
		return modificado_por;
	}
	public void setModificado_por(String modificado_por) {
		this.modificado_por = modificado_por;
	}
	public BigDecimal getTcsccatpaisid() {
		return tcsccatpaisid;
	}
	public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
	}
	public BigDecimal getTcscjornadavenid() {
		return tcscjornadavenid;
	}
	public void setTcscjornadavenid(BigDecimal tcscjornadavenid) {
		this.tcscjornadavenid = tcscjornadavenid;
	}
	public String getCod_dispositivo() {
		return cod_dispositivo;
	}
	public void setCod_dispositivo(String cod_dispositivo) {
		this.cod_dispositivo = cod_dispositivo;
	}
	public String getOperador_donante() {
		return operador_donante;
	}
	public void setOperador_donante(String operador_donante) {
		this.operador_donante = operador_donante;
	}
	public BigDecimal getId_vendedor() {
		return id_vendedor;
	}
	public void setId_vendedor(BigDecimal id_vendedor) {
		this.id_vendedor = id_vendedor;
	}
	
	
	
}

