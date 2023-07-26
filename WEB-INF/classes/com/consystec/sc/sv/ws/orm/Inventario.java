package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Inventario {
    public static final String N_TABLA = "TC_SC_INVENTARIO";
    public static final String N_TABLA_ID = "tcscinventarioinvid";
    public static final String SEQUENCE = "TC_SC_INVENTARIO_SQ.nextval";

    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TCSCBODEGAVIRTUALID = "tcscbodegavirtualid";
    public final static String CAMPO_ARTICULO = "articulo";
    public final static String CAMPO_SERIE = "serie";
    public final static String CAMPO_DESCRIPCION = "descripcion";
    public final static String CAMPO_CANTIDAD = "cantidad";
    public final static String CAMPO_ESTADO_COMERCIAL = "estado_comercial";
    public final static String CAMPO_SERIE_ASOCIADA = "serie_asociada";
    public final static String CAMPO_SERIADO = "seriado";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";
    public final static String CAMPO_TIPO_INV = "tipo_inv";
    public final static String CAMPO_TIPO_GRUPO = "tipo_grupo";
    public final static String CAMPO_IDVENDEDOR = "idvendedor";
    public final static String CAMPO_TCSCSOLICITUDID = "tcscsolicitudid";
    public final static String CAMPO_TCSCASIGNACIONRESERVAID = "tcscasignacionreservaid";
    public final static String CAMPO_TIPO_GRUPO_SIDRA = "tipo_grupo_sidra";
    public final static String CAMPO_TCSCVENTAID = "tcscventaid";
    public final static String CAMPO_NUM_TELEFONO = "num_telefono";
    public final static String CAMPO_TECNOLOGIA = "tecnologia";
    public final static String CAMPO_NUM_TRASPASO_SCL = "num_traspaso_scl";
    public final static String CAMPO_NO_LOTE = "no_lote";
    public final static String CAMPO_ICC = "icc";
    public final static String CAMPO_IMEI = "imei";
    public final static String CAMPO_COD_BODEGA = "COD_BODEGA";

    private BigDecimal tcscinventarioinvid;
    private BigDecimal tcsccatpaisid;
    private BigDecimal articulo;
    private BigDecimal tcscbodegavirtualid;
    private String serie;
    private String descripcion;
    private BigDecimal cantidad;
    private String estado_comercial;
    private String serie_asociada;
    private String seriado;
    private String tipo_inv;
    private String tipo_grupo;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    private BigDecimal tcscsolicitudid;
    private BigDecimal tcscasignacionreservaid;
    private String tipo_grupo_sidra;
    private BigDecimal tcscventaid;
    private String seriefinal;
    private BigDecimal num_telefono;
    private BigDecimal idVendedor;
    private String tecnologia;
    private BigDecimal num_traspaso_scl;
    private String no_lote;
    private BigDecimal icc;
    private String imei;
    
    public BigDecimal getTcscinventarioinvid() {
        return tcscinventarioinvid;
    }
    public void setTcscinventarioinvid(BigDecimal tcscinventarioinvid) {
        this.tcscinventarioinvid = tcscinventarioinvid;
    }
    public BigDecimal getTcsccatpaisid() {
        return tcsccatpaisid;
    }
    public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
        this.tcsccatpaisid = tcsccatpaisid;
    }
    public BigDecimal getArticulo() {
        return articulo;
    }
    public void setArticulo(BigDecimal articulo) {
        this.articulo = articulo;
    }
    public BigDecimal getTcscbodegavirtualid() {
        return tcscbodegavirtualid;
    }
    public void setTcscbodegavirtualid(BigDecimal tcscbodegavirtualid) {
        this.tcscbodegavirtualid = tcscbodegavirtualid;
    }
    public String getSerie() {
        return serie;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public BigDecimal getCantidad() {
        return cantidad;
    }
    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }
    public String getEstado_comercial() {
        return estado_comercial;
    }
    public void setEstado_comercial(String estado_comercial) {
        this.estado_comercial = estado_comercial;
    }
    public String getSerie_asociada() {
        return serie_asociada;
    }
    public void setSerie_asociada(String serie_asociada) {
        this.serie_asociada = serie_asociada;
    }
    public String getSeriado() {
        return seriado;
    }
    public void setSeriado(String seriado) {
        this.seriado = seriado;
    }
    public String getTipo_inv() {
        return tipo_inv;
    }
    public void setTipo_inv(String tipo_inv) {
        this.tipo_inv = tipo_inv;
    }
    public String getTipo_grupo() {
        return tipo_grupo;
    }
    public void setTipo_grupo(String tipo_grupo) {
        this.tipo_grupo = tipo_grupo;
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
    public BigDecimal getTcscsolicitudid() {
        return tcscsolicitudid;
    }
    public void setTcscsolicitudid(BigDecimal tcscsolicitudid) {
        this.tcscsolicitudid = tcscsolicitudid;
    }
    public BigDecimal getTcscasignacionreservaid() {
        return tcscasignacionreservaid;
    }
    public void setTcscasignacionreservaid(BigDecimal tcscasignacionreservaid) {
        this.tcscasignacionreservaid = tcscasignacionreservaid;
    }
    public String getTipo_grupo_sidra() {
        return tipo_grupo_sidra;
    }
    public void setTipo_grupo_sidra(String tipo_grupo_sidra) {
        this.tipo_grupo_sidra = tipo_grupo_sidra;
    }
    public BigDecimal getTcscventaid() {
        return tcscventaid;
    }
    public void setTcscventaid(BigDecimal tcscventaid) {
        this.tcscventaid = tcscventaid;
    }
    public String getSeriefinal() {
        return seriefinal;
    }
    public void setSeriefinal(String seriefinal) {
        this.seriefinal = seriefinal;
    }
    public BigDecimal getNum_telefono() {
        return num_telefono;
    }
    public void setNum_telefono(BigDecimal num_telefono) {
        this.num_telefono = num_telefono;
    }
    public String getTecnologia() {
        return tecnologia;
    }
    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }
    public BigDecimal getNum_traspaso_scl() {
        return num_traspaso_scl;
    }
    public void setNum_traspaso_scl(BigDecimal num_traspaso_scl) {
        this.num_traspaso_scl = num_traspaso_scl;
    }
	public String getNo_lote() {
		return no_lote;
	}
	public void setNo_lote(String no_lote) {
		this.no_lote = no_lote;
	}
	public BigDecimal getIcc() {
		return icc;
	}
	public void setIcc(BigDecimal icc) {
		this.icc = icc;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public BigDecimal getIdVendedor() {
		return idVendedor;
	}
	public void setIdVendedor(BigDecimal idVendedor) {
		this.idVendedor = idVendedor;
	}
	
	
}