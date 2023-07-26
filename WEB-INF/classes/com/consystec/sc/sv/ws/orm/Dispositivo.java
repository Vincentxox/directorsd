package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Dispositivo {
	public static final String N_TABLA = "TC_SC_DISPOSITIVO";
	public static final String SEQUENCE = "TC_SC_DISPOSITIVO_SQ.nextval";

	public final static String CAMPO_TCSCDISPOSITIVOID = "tcscdispositivoid";
	public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	public final static String CAMPO_CODIGO_DISPOSITIVO = "codigo_dispositivo";
	public final static String CAMPO_MODELO = "modelo";
	public final static String CAMPO_DESCRIPCION = "descripcion";
	public final static String CAMPO_NUM_TELEFONO = "num_telefono";
	public final static String CAMPO_ESTADO = "estado";
	public final static String CAMPO_RESPONSABLE = "responsable";
	public final static String CAMPO_TIPO_RESPONSABLE = "tipo_responsable";
	public final static String CAMPO_NOMBRE_RESPONSABLE = "nombre_responsable";
	public final static String CAMPO_CAJA_NUMERO = "caja_numero";
	public final static String CAMPO_ZONA = "zona";
	public final static String CAMPO_RESOLUCION = "resolucion";
	public final static String CAMPO_FECHA_RESOLUCION = "fecha_resolucion";
	public final static String CAMPO_TCSCDTSID = "tcscdtsid";
	public final static String CAMPO_COD_OFICINA = "cod_oficina";
	public final static String CAMPO_COD_VENDEDOR = "cod_vendedor";
	public final static String CAMPO_VENDEDOR_ASIGNADO = "vendedor_asignado";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	public final static String CAMPO_ID_PLAZA = "id_plaza";
	public final static String CAMPO_ID_PUNTOVENTA = "id_puntoventa";
	public final static String CAMPO_USERID = "userid";
	public final static String CAMPO_USERNAME = "username";

	private BigDecimal tcscdispositivoid;
	private BigDecimal vendedor_asignado;
	private BigDecimal tcsccatpaisid;
	private String codigo_dispositivo;
	private String modelo;
	private String descripcion;
	private BigDecimal num_telefono;
	private String estado;
	private BigDecimal responsable;
	private String tipo_responsable;
	private String nombre_responsable;
	private BigDecimal caja_numero;
	private String zona;
	private String resolucion;
	private Timestamp fecha_resolucion;
	private BigDecimal tcscdtsid;
	private String cod_oficina;
	private String cod_vendedor;
	private Timestamp creado_el;
	private String creado_por;
	private Timestamp modificado_el;
	private String modificado_por;
	private String id_plaza;
	private String id_puntoventa;
	private String userid;
	private String username;
	private String developToken;
	private String userToken;

	public BigDecimal getTcscdispositivoid() {
		return tcscdispositivoid;
	}

	public void setTcscdispositivoid(BigDecimal tcscdispositivoid) {
		this.tcscdispositivoid = tcscdispositivoid;
	}

	public String getCodigo_dispositivo() {
		return codigo_dispositivo;
	}

	public void setCodigo_dispositivo(String codigo_dispositivo) {
		this.codigo_dispositivo = codigo_dispositivo;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getNum_telefono() {
		return num_telefono;
	}

	public void setNum_telefono(BigDecimal num_telefono) {
		this.num_telefono = num_telefono;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public BigDecimal getResponsable() {
		return responsable;
	}

	public void setResponsable(BigDecimal responsable) {
		this.responsable = responsable;
	}

	public String getTipo_responsable() {
		return tipo_responsable;
	}

	public void setTipo_responsable(String tipo_responsable) {
		this.tipo_responsable = tipo_responsable;
	}

	public String getNombre_responsable() {
		return nombre_responsable;
	}

	public void setNombre_responsable(String nombre_responsable) {
		this.nombre_responsable = nombre_responsable;
	}

	public BigDecimal getCaja_numero() {
		return caja_numero;
	}

	public void setCaja_numero(BigDecimal caja_numero) {
		this.caja_numero = caja_numero;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getResolucion() {
		return resolucion;
	}

	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}

	public Timestamp getFecha_resolucion() {
		return fecha_resolucion;
	}

	public void setFecha_resolucion(Timestamp fecha_resolucion) {
		this.fecha_resolucion = fecha_resolucion;
	}

	public BigDecimal getTcscdtsid() {
		return tcscdtsid;
	}

	public void setTcscdtsid(BigDecimal tcscdtsid) {
		this.tcscdtsid = tcscdtsid;
	}

	public String getCod_oficina() {
		return cod_oficina;
	}

	public void setCod_oficina(String cod_oficina) {
		this.cod_oficina = cod_oficina;
	}

	public String getCod_vendedor() {
		return cod_vendedor;
	}

	public void setCod_vendedor(String cod_vendedor) {
		this.cod_vendedor = cod_vendedor;
	}

	public Timestamp getCreado_el() {
		return creado_el;
	}

	public void setCreado_el(Timestamp creado_el) {
		this.creado_el = creado_el;
	}

	public String getCreado_por() {
		return creado_por;
	}

	public void setCreado_por(String creado_por) {
		this.creado_por = creado_por;
	}

	public Timestamp getModificado_el() {
		return modificado_el;
	}

	public void setModificado_el(Timestamp modificado_el) {
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

	public BigDecimal getVendedor_asignado() {
		return vendedor_asignado;
	}

	public void setVendedor_asignado(BigDecimal vendedor_asignado) {
		this.vendedor_asignado = vendedor_asignado;
	}

	public String getId_plaza() {
		return id_plaza;
	}

	public void setId_plaza(String id_plaza) {
		this.id_plaza = id_plaza;
	}

	public String getId_puntoventa() {
		return id_puntoventa;
	}

	public void setId_puntoventa(String id_puntoventa) {
		this.id_puntoventa = id_puntoventa;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDevelopToken() {
		return developToken;
	}

	public void setDevelopToken(String developToken) {
		this.developToken = developToken;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

}