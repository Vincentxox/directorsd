package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Venta {
    public static final String N_TABLA = "TC_SC_VENTA";
    public static final String SEQUENCE = "TC_SC_VENTA_SQ.nextval";

    public final static String CAMPO_TCSCVENTAID = "tcscventaid";
    public final static String CAMPO_TCSCJORNADAVENID = "tcscjornadavenid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_FECHA_EMISION = "fecha_emision";
    public final static String CAMPO_FECHA_PAGO = "fecha_pago";
    public final static String CAMPO_SERIE = "serie";
    public final static String CAMPO_NUMERO = "numero";
    public final static String CAMPO_VENDEDOR = "vendedor";
    public final static String CAMPO_TIPO_DOCUMENTO = "tipo_documento";
    public final static String CAMPO_FORMA_PAGO = "forma_pago";
    public final static String CAMPO_IDTIPO = "idtipo";
    public final static String CAMPO_TIPO = "tipo";
    public final static String CAMPO_NIT = "nit";
    public final static String CAMPO_NOMBRE = "nombre";
    public final static String CAMPO_DIRECCION = "direccion";
    public final static String CAMPO_MONTO_PAGADO = "monto_pagado";
    public final static String CAMPO_IMPUESTOS = "impuestos";
    public final static String CAMPO_DESCUENTOS = "descuentos";
    public final static String CAMPO_MONTO_FACTURA = "monto_factura";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_OBSERVACIONES = "observaciones";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";
    public final static String CAMPO_REGISTROFISCAL = "registrofiscal";
    public final static String CAMPO_GIRO = "giro";
    public final static String CAMPO_EXENTO = "exento";
    public final static String CAMPO_APELLIDO = "apellido";
    public final static String CAMPO_NOMBRE_FISCAL = "nombre_fiscal";
    public final static String CAMPO_SEGUNDO_NOMBRE = "segundo_nombre";
    public final static String CAMPO_SEGUNDO_APELLIDO = "segundo_apellido";
    public final static String CAMPO_TIPO_DOCCLIENTE = "tipo_doccliente";
    public final static String CAMPO_NUM_TELEFONO = "num_telefono";
    public final static String CAMPO_NUM_DOCCLIENTE = "num_doccliente";
    public final static String CAMPO_FOLIO_SIDRA = "folio_sidra";
    public final static String CAMPO_SERIE_SIDRA = "serie_sidra";
    public final static String CAMPO_COD_DISPOSITIVO = "cod_dispositivo";
    public final static String CAMPO_ENVIO_ALARMA = "envio_alarma";
    public final static String CAMPO_NOMBRES_FACTURA = "nombres_factura";
    public final static String CAMPO_APELLIDOS_FACTURA = "apellidos_factura";
    public final static String CAMPO_IDRANGO_FOLIO = "idrango_folio";
    public final static String CAMPO_IDOFERTACAMPANIA = "idofertacampania";
    public final static String CAMPO_DESC_MONTOVENTA = "desc_montoventa";
    public final static String CAMPO_LATITUD = "latitud";
    public final static String CAMPO_LONGITUD = "longitud";
    public final static String CAMPO_TASA_CAMBIO = "tasa_cambio";
    public final static String CAMPO_ESTADO_FACT = "estado_fact";
    public final static String CAMPO_COD_OFICINA = "cod_oficina";
    public final static String CAMPO_COD_VENDEDOR = "cod_vendedor";
    public final static String CAMPO_TXT_CORRELATIVO = "txt_correlativo";
    public final static String CAMPO_TXT_QR = "txt_qr";
    public final static String CAMPO_ID_VENTA_MOVIL = "id_venta_movil";

    private BigDecimal tcscventaid;
    private BigDecimal tcscjornadavenid;
    private BigDecimal tcsccatpaisid;
    private Timestamp fecha_emision;
    private Timestamp fecha_pago;
    private String serie;
    private String numero;
    private BigDecimal vendedor;
    private String tipo_documento;
    private String forma_pago;
    private BigDecimal idtipo;
    private String tipo;
    private String nit;
    private String nombre;
    private String direccion;
    private BigDecimal monto_pagado;
    private BigDecimal impuestos;
    private BigDecimal descuentos;
    private BigDecimal monto_factura;
    private String estado;
    private String observaciones;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    private String registrofiscal;
    private String giro;
    private String exento;
    private String apellido;
    private String nombre_fiscal;
    private String segundo_nombre;
    private String segundo_apellido;
    private String tipo_doccliente;
    private String num_doccliente;
    private String num_telefono;
    private BigDecimal folio_sidra;
    private String serie_sidra;
    private String cod_dispositivo;
    private BigDecimal envio_alarma;
    private String nombres_factura;
    private String apellidos_factura;
    private BigDecimal idrango_folio;
    private BigDecimal idofertacampania;
    private BigDecimal desc_montoventa;
    private String latitud;
    private String longitud;
    private BigDecimal tasa_cambio;
    private String cod_oficina;
    private String cod_vendedor;
    private String txt_correlativo;
    private String txt_qr;
    private BigDecimal id_venta_movil;
    
    public BigDecimal getTcsccatpaisid() {
		return tcsccatpaisid;
	}
	public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
	}
	public BigDecimal getTcscventaid() {
        return tcscventaid;
    }
    public void setTcscventaid(BigDecimal tcscventaid) {
        this.tcscventaid = tcscventaid;
    }
    public BigDecimal getTcscjornadavenid() {
        return tcscjornadavenid;
    }
    public void setTcscjornadavenid(BigDecimal tcscjornadavenid) {
        this.tcscjornadavenid = tcscjornadavenid;
    }
    public Timestamp getFecha_emision() {
        return fecha_emision;
    }
    public void setFecha_emision(Timestamp fecha_emision) {
        this.fecha_emision = fecha_emision;
    }
    public Timestamp getFecha_pago() {
        return fecha_pago;
    }
    public void setFecha_pago(Timestamp fecha_pago) {
        this.fecha_pago = fecha_pago;
    }
    public String getSerie() {
        return serie;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public BigDecimal getVendedor() {
        return vendedor;
    }
    public void setVendedor(BigDecimal vendedor) {
        this.vendedor = vendedor;
    }
    public String getTipo_documento() {
        return tipo_documento;
    }
    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }
    public String getForma_pago() {
        return forma_pago;
    }
    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }
    public BigDecimal getIdtipo() {
        return idtipo;
    }
    public void setIdtipo(BigDecimal idtipo) {
        this.idtipo = idtipo;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getNit() {
        return nit;
    }
    public void setNit(String nit) {
        this.nit = nit;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public BigDecimal getMonto_pagado() {
        return monto_pagado;
    }
    public void setMonto_pagado(BigDecimal monto_pagado) {
        this.monto_pagado = monto_pagado;
    }
    public BigDecimal getImpuestos() {
        return impuestos;
    }
    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }
    public BigDecimal getDescuentos() {
        return descuentos;
    }
    public void setDescuentos(BigDecimal descuentos) {
        this.descuentos = descuentos;
    }
    public BigDecimal getMonto_factura() {
        return monto_factura;
    }
    public void setMonto_factura(BigDecimal monto_factura) {
        this.monto_factura = monto_factura;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
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
    public String getRegistrofiscal() {
        return registrofiscal;
    }
    public void setRegistrofiscal(String registrofiscal) {
        this.registrofiscal = registrofiscal;
    }
    public String getGiro() {
        return giro;
    }
    public void setGiro(String giro) {
        this.giro = giro;
    }
    public String getExento() {
        return exento;
    }
    public void setExento(String exento) {
        this.exento = exento;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getNombre_fiscal() {
        return nombre_fiscal;
    }
    public void setNombre_fiscal(String nombre_fiscal) {
        this.nombre_fiscal = nombre_fiscal;
    }
    public String getSegundo_nombre() {
        return segundo_nombre;
    }
    public void setSegundo_nombre(String segundo_nombre) {
        this.segundo_nombre = segundo_nombre;
    }
    public String getSegundo_apellido() {
        return segundo_apellido;
    }
    public void setSegundo_apellido(String segundo_apellido) {
        this.segundo_apellido = segundo_apellido;
    }
    public String getTipo_doccliente() {
        return tipo_doccliente;
    }
    public void setTipo_doccliente(String tipo_doccliente) {
        this.tipo_doccliente = tipo_doccliente;
    }
    public String getNum_doccliente() {
        return num_doccliente;
    }
    public void setNum_doccliente(String num_doccliente) {
        this.num_doccliente = num_doccliente;
    }
    public String getNum_telefono() {
        return num_telefono;
    }
    public void setNum_telefono(String num_telefono) {
        this.num_telefono = num_telefono;
    }
    public BigDecimal getFolio_sidra() {
        return folio_sidra;
    }
    public void setFolio_sidra(BigDecimal folio_sidra) {
        this.folio_sidra = folio_sidra;
    }
    public String getSerie_sidra() {
        return serie_sidra;
    }
    public void setSerie_sidra(String serie_sidra) {
        this.serie_sidra = serie_sidra;
    }
    public String getCod_dispositivo() {
        return cod_dispositivo;
    }
    public void setCod_dispositivo(String cod_dispositivo) {
        this.cod_dispositivo = cod_dispositivo;
    }
    public BigDecimal getEnvio_alarma() {
        return envio_alarma;
    }
    public void setEnvio_alarma(BigDecimal envio_alarma) {
        this.envio_alarma = envio_alarma;
    }
    public String getNombres_factura() {
        return nombres_factura;
    }
    public void setNombres_factura(String nombres_factura) {
        this.nombres_factura = nombres_factura;
    }
    public String getApellidos_factura() {
        return apellidos_factura;
    }
    public void setApellidos_factura(String apellidos_factura) {
        this.apellidos_factura = apellidos_factura;
    }
    public BigDecimal getIdrango_folio() {
        return idrango_folio;
    }
    public void setIdrango_folio(BigDecimal idrango_folio) {
        this.idrango_folio = idrango_folio;
    }
    public BigDecimal getIdofertacampania() {
        return idofertacampania;
    }
    public void setIdofertacampania(BigDecimal idofertacampania) {
        this.idofertacampania = idofertacampania;
    }
    public BigDecimal getDesc_montoventa() {
        return desc_montoventa;
    }
    public void setDesc_montoventa(BigDecimal desc_montoventa) {
        this.desc_montoventa = desc_montoventa;
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
    public BigDecimal getTasa_cambio() {
        return tasa_cambio;
    }
    public void setTasa_cambio(BigDecimal tasa_cambio) {
        this.tasa_cambio = tasa_cambio;
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
	public String getTxt_correlativo() {
		return txt_correlativo;
	}
	public void setTxt_correlativo(String txt_correlativo) {
		this.txt_correlativo = txt_correlativo;
	}
	public String getTxt_qr() {
		return txt_qr;
	}
	public void setTxt_qr(String txt_qr) {
		this.txt_qr = txt_qr;
	}
	public BigDecimal getId_venta_movil() {
		return id_venta_movil;
	}
	public void setId_venta_movil(BigDecimal id_venta_movil) {
		this.id_venta_movil = id_venta_movil;
	}
    
}