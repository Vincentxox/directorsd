package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ReporteXZ {
	public static final String N_TABLA = "TC_SC_REPORTE_XZ";
	public static final String SEQUENCE = "TC_SC_REPORTE_XZ_SQ.nextval";
	public final static String CAMPO_TCSCREPORTEXZID = "tcscreportexzid";
	public final static String CAMPO_TCSCJORNADAVENID = "tcscjornadavenid";
	public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
	public final static String CAMPO_TIPO_REPORTE = "tipo_reporte";
	public final static String CAMPO_COD_DISPOSITIVO = "cod_dispositivo";
	public final static String CAMPO_VENDEDOR = "vendedor";
	public final static String CAMPO_FECHA = "fecha";
	public final static String CAMPO_ACUMULADO_VENTAS = "acumulado_ventas";
	public final static String CAMPO_MES = "mes";
	public final static String CAMPO_CANT_VENTAS_BRUTAS = "cant_ventas_brutas";
	public final static String CAMPO_MONTO_VENTAS_BRUTAS = "monto_ventas_brutas";
	public final static String CAMPO_MONTO_FONDOS_INICIALES = "monto_fondos_iniciales";
	public final static String CAMPO_CANT_ENTREGA_PARCIAL = "cant_entrega_parcial";
	public final static String CAMPO_MONTO_ENTREGA_PARCIAL = "monto_entrega_parcial";
	public final static String CAMPO_CANT_PAGOS_TERCEROS = "cant_pagos_terceros";
	public final static String CAMPO_MONTO_PAGOS_TERCEROS = "monto_pagos_terceros";
	public final static String CAMPO_MONTO_DINERO_GAVETA = "monto_dinero_gaveta";
	public final static String CAMPO_CANT_DESCUENTOS = "cant_descuentos";
	public final static String CAMPO_MONTO_DESCUENTOS = "monto_descuentos";
	public final static String CAMPO_CANT_ANULACIONES = "cant_anulaciones";
	public final static String CAMPO_MONTO_ANULACIONES = "monto_anulaciones";
	public final static String CAMPO_CANT_NO_VENTAS = "cant_no_ventas";
	public final static String CAMPO_MONTO_NO_VENTAS = "monto_no_ventas";
	public final static String CAMPO_CANT_PAGO_EFECTIVO = "cant_pago_efectivo";
	public final static String CAMPO_MONTO_EFECTIVO = "monto_efectivo";
	public final static String CAMPO_CANT_PAGO_CHEQUE = "cant_pago_cheque";
	public final static String CAMPO_MONTO_CHEQUE = "monto_cheque";
	public final static String CAMPO_CANT_PAGO_TARJETA = "cant_pago_tarjeta";
	public final static String CAMPO_MONTO_TARJETA = "monto_tarjeta";
	public final static String CAMPO_CANT_PAGO_CREDITO = "cant_pago_credito";
	public final static String CAMPO_MONTO_CREDITO = "monto_credito";
	public final static String CAMPO_CANT_PAGO_MPOS = "cant_pago_mpos";
	public final static String CAMPO_MONTO_MPOS = "monto_mpos";
	public final static String CAMPO_TOTAL_GRAVADO = "total_gravado";
	public final static String CAMPO_TOTAL_EXENTO = "total_exento";
	public final static String CAMPO_TOTAL_NO_SUJETO = "total_no_sujeto";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_CREADO_POR = "creado_por";
	public final static String CAMPO_RESOLUCION = "resolucion";
	public final static String CAMPO_FECHA_RESOLUCION = "fecha_resolucion";
	public final static String CAMPO_RANGO_INICIAL = "rango_inicial";
	public final static String CAMPO_RANGO_FINAL = "rango_final";
	public final static String CAMPO_CAJA = "caja";
	public final static String CAMPO_TERMINAL = "terminal";
	public final static String CAMPO_ZONA = "zona";
	public final static String CAMPO_RANGO_INI_USADO = "rango_ini_usado";
	public final static String CAMPO_RANGO_FIN_USADO = "rango_fin_usado";

	private BigDecimal tcscreportexzid;
	private BigDecimal tcscjornadavenid;
	private BigDecimal tcsccatpaisid;
	private String tipo_reporte;
	private String cod_dispositivo;
	private BigDecimal vendedor;
	private Timestamp fecha;
	private BigDecimal acumulado_ventas;
	private BigDecimal cant_ventas_brutas;
	private BigDecimal monto_ventas_brutas;
	private BigDecimal monto_fondos_iniciales;
	private BigDecimal cant_entrega_parcial;
	private BigDecimal monto_entrega_parcial;
	private BigDecimal cant_pagos_terceros;
	private BigDecimal monto_pagos_terceros;
	private BigDecimal monto_dinero_gaveta;
	private BigDecimal cant_descuentos;
	private BigDecimal monto_descuentos;
	private BigDecimal cant_anulaciones;
	private BigDecimal monto_anulaciones;
	private BigDecimal cant_no_ventas;
	private BigDecimal monto_no_ventas;
	private BigDecimal cant_pago_efectivo;
	private BigDecimal monto_efectivo;
	private BigDecimal cant_pago_cheque;
	private BigDecimal monto_cheque;
	private BigDecimal cant_pago_tarjeta;
	private BigDecimal monto_tarjeta;
	private BigDecimal cant_pago_credito;
	private BigDecimal monto_credito;
	private BigDecimal cant_pago_mpos;
	private BigDecimal monto_mpos;
	private BigDecimal total_gravado;
	private BigDecimal total_exento;
	private BigDecimal total_no_sujeto;
	private Timestamp creado_el;
	private String creado_por;
	private String resolucion;
	private Timestamp fecha_resolucion;
	private BigDecimal rango_inicial;
	private BigDecimal rango_final;
	private String caja;
	private BigDecimal terminal;
	private String zona;
	private BigDecimal rango_ini_usado;
	private BigDecimal rango_fin_usado;

	
	
	public BigDecimal getCant_pago_mpos() {
		return cant_pago_mpos;
	}

	public void setCant_pago_mpos(BigDecimal cant_pago_mpos) {
		this.cant_pago_mpos = cant_pago_mpos;
	}

	public BigDecimal getMonto_mpos() {
		return monto_mpos;
	}

	public void setMonto_mpos(BigDecimal monto_mpos) {
		this.monto_mpos = monto_mpos;
	}

	public BigDecimal getTcscreportexzid() {
		return tcscreportexzid;
	}

	public void setTcscreportexzid(BigDecimal tcscreportexzid) {
		this.tcscreportexzid = tcscreportexzid;
	}

	public BigDecimal getTcscjornadavenid() {
		return tcscjornadavenid;
	}

	public void setTcscjornadavenid(BigDecimal tcscjornadavenid) {
		this.tcscjornadavenid = tcscjornadavenid;
	}

	public String getTipo_reporte() {
		return tipo_reporte;
	}

	public void setTipo_reporte(String tipo_reporte) {
		this.tipo_reporte = tipo_reporte;
	}

	public String getCod_dispositivo() {
		return cod_dispositivo;
	}

	public void setCod_dispositivo(String cod_dispositivo) {
		this.cod_dispositivo = cod_dispositivo;
	}

	public BigDecimal getVendedor() {
		return vendedor;
	}

	public void setVendedor(BigDecimal vendedor) {
		this.vendedor = vendedor;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getAcumulado_ventas() {
		return acumulado_ventas;
	}

	public void setAcumulado_ventas(BigDecimal acumulado_ventas) {
		this.acumulado_ventas = acumulado_ventas;
	}

	public BigDecimal getCant_ventas_brutas() {
		return cant_ventas_brutas;
	}

	public void setCant_ventas_brutas(BigDecimal cant_ventas_brutas) {
		this.cant_ventas_brutas = cant_ventas_brutas;
	}

	public BigDecimal getMonto_ventas_brutas() {
		return monto_ventas_brutas;
	}

	public void setMonto_ventas_brutas(BigDecimal monto_ventas_brutas) {
		this.monto_ventas_brutas = monto_ventas_brutas;
	}

	public BigDecimal getMonto_fondos_iniciales() {
		return monto_fondos_iniciales;
	}

	public void setMonto_fondos_iniciales(BigDecimal monto_fondos_iniciales) {
		this.monto_fondos_iniciales = monto_fondos_iniciales;
	}

	public BigDecimal getCant_entrega_parcial() {
		return cant_entrega_parcial;
	}

	public void setCant_entrega_parcial(BigDecimal cant_entrega_parcial) {
		this.cant_entrega_parcial = cant_entrega_parcial;
	}

	public BigDecimal getMonto_entrega_parcial() {
		return monto_entrega_parcial;
	}

	public void setMonto_entrega_parcial(BigDecimal monto_entrega_parcial) {
		this.monto_entrega_parcial = monto_entrega_parcial;
	}

	public BigDecimal getCant_pagos_terceros() {
		return cant_pagos_terceros;
	}

	public void setCant_pagos_terceros(BigDecimal cant_pagos_terceros) {
		this.cant_pagos_terceros = cant_pagos_terceros;
	}

	public BigDecimal getMonto_pagos_terceros() {
		return monto_pagos_terceros;
	}

	public void setMonto_pagos_terceros(BigDecimal monto_pagos_terceros) {
		this.monto_pagos_terceros = monto_pagos_terceros;
	}

	public BigDecimal getMonto_dinero_gaveta() {
		return monto_dinero_gaveta;
	}

	public void setMonto_dinero_gaveta(BigDecimal monto_dinero_gaveta) {
		this.monto_dinero_gaveta = monto_dinero_gaveta;
	}

	public BigDecimal getCant_descuentos() {
		return cant_descuentos;
	}

	public void setCant_descuentos(BigDecimal cant_descuentos) {
		this.cant_descuentos = cant_descuentos;
	}

	public BigDecimal getMonto_descuentos() {
		return monto_descuentos;
	}

	public void setMonto_descuentos(BigDecimal monto_descuentos) {
		this.monto_descuentos = monto_descuentos;
	}

	public BigDecimal getCant_anulaciones() {
		return cant_anulaciones;
	}

	public void setCant_anulaciones(BigDecimal cant_anulaciones) {
		this.cant_anulaciones = cant_anulaciones;
	}

	public BigDecimal getMonto_anulaciones() {
		return monto_anulaciones;
	}

	public void setMonto_anulaciones(BigDecimal monto_anulaciones) {
		this.monto_anulaciones = monto_anulaciones;
	}

	public BigDecimal getCant_no_ventas() {
		return cant_no_ventas;
	}

	public void setCant_no_ventas(BigDecimal cant_no_ventas) {
		this.cant_no_ventas = cant_no_ventas;
	}

	public BigDecimal getMonto_no_ventas() {
		return monto_no_ventas;
	}

	public void setMonto_no_ventas(BigDecimal monto_no_ventas) {
		this.monto_no_ventas = monto_no_ventas;
	}

	public BigDecimal getCant_pago_efectivo() {
		return cant_pago_efectivo;
	}

	public void setCant_pago_efectivo(BigDecimal cant_pago_efectivo) {
		this.cant_pago_efectivo = cant_pago_efectivo;
	}

	public BigDecimal getMonto_efectivo() {
		return monto_efectivo;
	}

	public void setMonto_efectivo(BigDecimal monto_efectivo) {
		this.monto_efectivo = monto_efectivo;
	}

	public BigDecimal getCant_pago_cheque() {
		return cant_pago_cheque;
	}

	public void setCant_pago_cheque(BigDecimal cant_pago_cheque) {
		this.cant_pago_cheque = cant_pago_cheque;
	}

	public BigDecimal getMonto_cheque() {
		return monto_cheque;
	}

	public void setMonto_cheque(BigDecimal monto_cheque) {
		this.monto_cheque = monto_cheque;
	}

	public BigDecimal getCant_pago_tarjeta() {
		return cant_pago_tarjeta;
	}

	public void setCant_pago_tarjeta(BigDecimal cant_pago_tarjeta) {
		this.cant_pago_tarjeta = cant_pago_tarjeta;
	}

	public BigDecimal getMonto_tarjeta() {
		return monto_tarjeta;
	}

	public void setMonto_tarjeta(BigDecimal monto_tarjeta) {
		this.monto_tarjeta = monto_tarjeta;
	}

	public BigDecimal getCant_pago_credito() {
		return cant_pago_credito;
	}

	public void setCant_pago_credito(BigDecimal cant_pago_credito) {
		this.cant_pago_credito = cant_pago_credito;
	}

	public BigDecimal getMonto_credito() {
		return monto_credito;
	}

	public void setMonto_credito(BigDecimal monto_credito) {
		this.monto_credito = monto_credito;
	}

	public BigDecimal getTotal_gravado() {
		return total_gravado;
	}

	public void setTotal_gravado(BigDecimal total_gravado) {
		this.total_gravado = total_gravado;
	}

	public BigDecimal getTotal_exento() {
		return total_exento;
	}

	public void setTotal_exento(BigDecimal total_exento) {
		this.total_exento = total_exento;
	}

	public BigDecimal getTotal_no_sujeto() {
		return total_no_sujeto;
	}

	public void setTotal_no_sujeto(BigDecimal total_no_sujeto) {
		this.total_no_sujeto = total_no_sujeto;
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

	public BigDecimal getTcsccatpaisid() {
		return tcsccatpaisid;
	}

	public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
		this.tcsccatpaisid = tcsccatpaisid;
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

	public BigDecimal getRango_inicial() {
		return rango_inicial;
	}

	public void setRango_inicial(BigDecimal rango_inicial) {
		this.rango_inicial = rango_inicial;
	}

	public BigDecimal getRango_final() {
		return rango_final;
	}

	public void setRango_final(BigDecimal rango_final) {
		this.rango_final = rango_final;
	}

	public String getCaja() {
		return caja;
	}

	public void setCaja(String caja) {
		this.caja = caja;
	}

	public BigDecimal getTerminal() {
		return terminal;
	}

	public void setTerminal(BigDecimal terminal) {
		this.terminal = terminal;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public BigDecimal getRango_ini_usado() {
		return rango_ini_usado;
	}

	public void setRango_ini_usado(BigDecimal rango_ini_usado) {
		this.rango_ini_usado = rango_ini_usado;
	}

	public BigDecimal getRango_fin_usado() {
		return rango_fin_usado;
	}

	public void setRango_fin_usado(BigDecimal rango_fin_usado) {
		this.rango_fin_usado = rango_fin_usado;
	}

}
