package com.consystec.sc.ca.ws.input.venta;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "VentaDet")
public class VentaDet {
	@XmlElement
	private BigDecimal tcscventadetid;
	@XmlElement
	private BigDecimal tcscventaid;
	@XmlElement
	private BigDecimal tcscbodegavirtualid;
	@XmlElement
	private BigDecimal articulo;
	@XmlElement
	private String tipo_inv;
	@XmlElement
	private String serie;
	@XmlElement
	private String serie_asociada;
	@XmlElement
	private BigDecimal num_telefono;
	@XmlElement
	private BigDecimal cantidad;
	@XmlElement
	private BigDecimal precio_unitario;
	@XmlElement
	private BigDecimal precio_total;
	@XmlElement
	private BigDecimal impuesto;
	@XmlElement
	private BigDecimal descuento_scl;
	@XmlElement
	private BigDecimal descuento_sidra;
	@XmlElement
	private BigDecimal tcscofertacampaniaid;
	@XmlElement
	private BigDecimal precio_final;
	@XmlElement
	private String gestion;
	@XmlElement
	private String modalidad;
	@XmlElement
	private String estado;
	@XmlElement
	private Timestamp creado_el;
	@XmlElement
	private String creado_por;
	@XmlElement
	private Timestamp modificado_el;
	@XmlElement
	private String modificado_por;
	@XmlElement
	private BigDecimal bodega_panel_ruta;
	@XmlElement
	private List<PagoImpuesto> impuestoArticulo;
	
	
	public BigDecimal getTcscventadetid() {
		return tcscventadetid;
	}
	public void setTcscventadetid(BigDecimal tcscventadetid) {
		this.tcscventadetid = tcscventadetid;
	}
	public BigDecimal getTcscventaid() {
		return tcscventaid;
	}
	public void setTcscventaid(BigDecimal tcscventaid) {
		this.tcscventaid = tcscventaid;
	}
	public BigDecimal getTcscbodegavirtualid() {
		return tcscbodegavirtualid;
	}
	public void setTcscbodegavirtualid(BigDecimal tcscbodegavirtualid) {
		this.tcscbodegavirtualid = tcscbodegavirtualid;
	}
	public BigDecimal getArticulo() {
		return articulo;
	}
	public void setArticulo(BigDecimal articulo) {
		this.articulo = articulo;
	}
	public String getTipo_inv() {
		return tipo_inv;
	}
	public void setTipo_inv(String tipo_inv) {
		this.tipo_inv = tipo_inv;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getSerie_asociada() {
		return serie_asociada;
	}
	public void setSerie_asociada(String serie_asociada) {
		this.serie_asociada = serie_asociada;
	}
	public BigDecimal getNum_telefono() {
		return num_telefono;
	}
	public void setNum_telefono(BigDecimal num_telefono) {
		this.num_telefono = num_telefono;
	}
	public BigDecimal getCantidad() {
		return cantidad;
	}
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	public BigDecimal getPrecio_unitario() {
		return precio_unitario;
	}
	public void setPrecio_unitario(BigDecimal precio_unitario) {
		this.precio_unitario = precio_unitario;
	}
	public BigDecimal getPrecio_total() {
		return precio_total;
	}
	public void setPrecio_total(BigDecimal precio_total) {
		this.precio_total = precio_total;
	}
	public BigDecimal getImpuesto() {
		return impuesto;
	}
	public void setImpuesto(BigDecimal impuesto) {
		this.impuesto = impuesto;
	}
	public BigDecimal getDescuento_scl() {
		return descuento_scl;
	}
	public void setDescuento_scl(BigDecimal descuento_scl) {
		this.descuento_scl = descuento_scl;
	}
	public BigDecimal getDescuento_sidra() {
		return descuento_sidra;
	}
	public void setDescuento_sidra(BigDecimal descuento_sidra) {
		this.descuento_sidra = descuento_sidra;
	}
	public BigDecimal getTcscofertacampaniaid() {
		return tcscofertacampaniaid;
	}
	public void setTcscofertacampaniaid(BigDecimal tcscofertacampaniaid) {
		this.tcscofertacampaniaid = tcscofertacampaniaid;
	}
	public BigDecimal getPrecio_final() {
		return precio_final;
	}
	public void setPrecio_final(BigDecimal precio_final) {
		this.precio_final = precio_final;
	}
	public String getGestion() {
		return gestion;
	}
	public void setGestion(String gestion) {
		this.gestion = gestion;
	}
	public String getModalidad() {
		return modalidad;
	}
	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
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
	public BigDecimal getBodega_panel_ruta() {
		return bodega_panel_ruta;
	}
	public void setBodega_panel_ruta(BigDecimal bodega_panel_ruta) {
		this.bodega_panel_ruta = bodega_panel_ruta;
	}
	public List<PagoImpuesto> getImpuestoArticulo() {
		return impuestoArticulo;
	}
	public void setImpuestoArticulo(List<PagoImpuesto> impuestoArticulo) {
		this.impuestoArticulo = impuestoArticulo;
	}
	
	public static final String N_TABLA = "TC_SC_VENTA_DET";
    public static final String SEQUENCE = "TC_SC_VENTA_DET_SQ.nextval";

    public final static String CAMPO_TCSCVENTADETID = "tcscventadetid";
    public final static String CAMPO_TCSCVENTAID = "tcscventaid";
    public final static String CAMPO_TCSCBODEGAVIRTUALID = "tcscbodegavirtualid";
    public final static String CAMPO_ARTICULO = "articulo";
    public final static String CAMPO_TIPO_INV = "tipo_inv";
    public final static String CAMPO_SERIE = "serie";
    public final static String CAMPO_SERIE_ASOCIADA = "serie_asociada";
    public final static String CAMPO_NUM_TELEFONO = "num_telefono";
    public final static String CAMPO_CANTIDAD = "cantidad";
    public final static String CAMPO_PRECIO_UNITARIO = "precio_unitario";
    public final static String CAMPO_PRECIO_TOTAL = "precio_total";
    public final static String CAMPO_IMPUESTO = "impuesto";
    public final static String CAMPO_DESCUENTO_SCL = "descuento_scl";
    public final static String CAMPO_DESCUENTO_SIDRA = "descuento_sidra";
    public final static String CAMPO_TCSCOFERTACAMPANIAID = "tcscofertacampaniaid";
    public final static String CAMPO_PRECIO_FINAL = "precio_final";
    public final static String CAMPO_GESTION = "gestion";
    public final static String CAMPO_MODALIDAD = "modalidad";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";
    public final static String CAMPO_BODEGA_PANEL_RUTA = "bodega_panel_ruta";	
}