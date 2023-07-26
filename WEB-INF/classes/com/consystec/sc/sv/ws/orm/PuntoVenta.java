package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PuntoVenta {

	public static final String N_TABLA = "TC_SC_PUNTOVENTA";
	public static final String N_TABLA_ID = "TCSCPUNTOVENTAID";
	public static final String SEQUENCE = "TC_SC_PUNTOVENTA_SQ.nextval";
	
	public final static String CAMPO_TCSCPUNTOVENTAID = "tcscpuntoventaid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	public final static String CAMPO_TCSCDTSID = "tcscdtsid";
	public final static String CAMPO_NOMBRE = "nombre";
	public final static String CAMPO_ESTADO = "estado";
	public final static String CAMPO_TIPO_NEGOCIO = "tipo_negocio";
	public final static String CAMPO_DOCUMENTO = "documento";
	public final static String CAMPO_NIT = "nit";
	public final static String CAMPO_NOMBRE_FISCAL = "nombre_fiscal";
	public final static String CAMPO_REGISTRO_FISCAL = "registro_fiscal";
	public final static String CAMPO_GIRO_NEGOCIO = "giro_negocio";
	public final static String CAMPO_DIRECCION = "direccion";
	public final static String CAMPO_TCSCZONACOMERCIALID = "tcsczonacomercialid";
	public final static String CAMPO_DEPARTAMENTO = "departamento";
	public final static String CAMPO_MUNICIPIO = "municipio";
	public final static String CAMPO_DISTRITO = "distrito";
	public final static String CAMPO_OBSERVACIONES = "observaciones";
	public final static String CAMPO_LONGITUD = "longitud";
	public final static String CAMPO_LATITUD = "latitud";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_MODIFICADO_EL = "modificado_el";
	public final static String CAMPO_MODIFICADO_POR = "modificado_por";
	public final static String CAMPO_TIPO_PRODUCTO = "tipo_producto";
	public final static String CAMPO_CANAL = "canal";
	public final static String CAMPO_SUBCANAL = "subcanal";
	public final static String CAMPO_CATEGORIA = "categoria";
	public final static String CAMPO_CALLE = "calle";
	public final static String CAMPO_AVENIDA = "avenida";
	public final static String CAMPO_PASAJE = "pasaje";
	public final static String CAMPO_COLONIA = "colonia";
	public final static String CAMPO_REFERENCIA = "referencia";
	public final static String CAMPO_TIPO_CONTRIBUYENTE = "tipo_contribuyente";
	public final static String CAMPO_DIGITO_VALIDADOR = "digito_validador";
	public final static String CAMPO_BARRIO = "barrio";
	public final static String CAMPO_CASA = "casa";
	public final static String CAMPO_COD_CLIENTE = "cod_cliente";
	public final static String CAMPO_RESULTADO_SCL = "resultado_scl";
	public final static String CAMPO_QR = "qr";
	public final static String CAMPO_NUM_RECARGA = "num_recarga";
	
	private BigDecimal tcscpuntoventaid;
    private BigDecimal tcsccatpaisid;
	private BigDecimal tcscdtsid;
	private String nombre;
	private String estado;
	private String tipo_negocio;
	private String documento;
	private String nit;
	private String nombre_fiscal;
	private String registro_fiscal;
	private String giro_negocio;
	private String direccion;
	private String tcsczonacomercialid;
	private String departamento;
	private String municipio;
	private String distrito;
	private String latitud;
	private String longitud;
	private String observaciones;
	private Timestamp creado_el;
	private String creado_por;
	private Timestamp modificado_el;
	private String modificado_por;
	private String tipo_producto;
	private String canal;
	private String subcanal;
	private String categoria;
	private String calle;
	private String avenida;
	private String pasaje;
	private String colonia;
	private String referencia;
	private String tipo_contribuyente;
	private BigDecimal digito_validador;
	private String barrio;
	private String casa;
	private String cod_cliente;
	private String resultado_scl;
	private String qr;
	
	public BigDecimal getTcsccatpaisid() {
        return tcsccatpaisid;
    }
    public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
        this.tcsccatpaisid = tcsccatpaisid;
    }
	public BigDecimal getTcscpuntoventaid() {
		return tcscpuntoventaid;
	}
	public void setTcscpuntoventaid(BigDecimal tcscpuntoventaid) {
		this.tcscpuntoventaid = tcscpuntoventaid;
	}
	public BigDecimal getTcscdtsid() {
		return tcscdtsid;
	}
	public void setTcscdtsid(BigDecimal tcscdtsid) {
		this.tcscdtsid = tcscdtsid;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getTipo_negocio() {
		return tipo_negocio;
	}
	public void setTipo_negocio(String tipo_negocio) {
		this.tipo_negocio = tipo_negocio;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getNit() {
		return nit;
	}
	public void setNit(String nit) {
		this.nit = nit;
	}
	public String getNombre_fiscal() {
		return nombre_fiscal;
	}
	public void setNombre_fiscal(String nombre_fiscal) {
		this.nombre_fiscal = nombre_fiscal;
	}
	public String getRegistro_fiscal() {
		return registro_fiscal;
	}
	public void setRegistro_fiscal(String registro_fiscal) {
		this.registro_fiscal = registro_fiscal;
	}
	public String getGiro_negocio() {
		return giro_negocio;
	}
	public void setGiro_negocio(String giro_negocio) {
		this.giro_negocio = giro_negocio;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public String getTcsczonacomercialid() {
		return tcsczonacomercialid;
	}
	public void setTcsczonacomercialid(String tcsczonacomercialid) {
		this.tcsczonacomercialid = tcsczonacomercialid;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
    public String getDistrito() {
        return distrito;
    }
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	public String getTipo_producto() {
		return tipo_producto;
	}
	public void setTipo_producto(String tipo_producto) {
		this.tipo_producto = tipo_producto;
	}
	public String getCanal() {
		return canal;
	}
	public void setCanal(String canal) {
		this.canal = canal;
	}
	public String getSubcanal() {
		return subcanal;
	}
	public void setSubcanal(String subcanal) {
		this.subcanal = subcanal;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getAvenida() {
		return avenida;
	}
	public void setAvenida(String avenida) {
		this.avenida = avenida;
	}
	public String getPasaje() {
		return pasaje;
	}
	public void setPasaje(String pasaje) {
		this.pasaje = pasaje;
	}
	public String getColonia() {
		return colonia;
	}
	public void setColonia(String colonia) {
		this.colonia = colonia;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getTipo_contribuyente() {
		return tipo_contribuyente;
	}
	public void setTipo_contribuyente(String tipo_contribuyente) {
		this.tipo_contribuyente = tipo_contribuyente;
	}
	public BigDecimal getDigito_validador() {
		return digito_validador;
	}
	public void setDigito_validador(BigDecimal digito_validador) {
		this.digito_validador = digito_validador;
	}
	public String getBarrio() {
		return barrio;
	}
	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}
	public String getCasa() {
		return casa;
	}
	public void setCasa(String casa) {
		this.casa = casa;
	}
    public String getCod_cliente() {
        return cod_cliente;
    }
    public void setCod_cliente(String cod_cliente) {
        this.cod_cliente = cod_cliente;
    }
    public String getResultado_scl() {
        return resultado_scl;
    }
    public void setResultado_scl(String resultado_scl) {
        this.resultado_scl = resultado_scl;
    }
    public String getQr() {
        return qr;
    }
    public void setQr(String qr) {
        this.qr = qr;
    }
}