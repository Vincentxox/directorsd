package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Remesa {
    public static final String N_TABLA = "TC_SC_REMESA";
    public static final String SEQUENCE = "TC_SC_REMESA_SQ.nextval";

    public final static String CAMPO_TCSCREMESAID = "tcscremesaid";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TCSCJORNADAVENID = "tcscjornadavenid";
    public final static String CAMPO_TCSCSOLICITUDID = "tcscsolicitudid";
    public final static String CAMPO_TCSCCTABANCARIAID = "tcscctabancariaid";
    public final static String CAMPO_BANCO = "banco";
    public final static String CAMPO_MONTO = "monto";
    public final static String CAMPO_TASA_CAMBIO = "tasa_cambio";
    public final static String CAMPO_NO_BOLETA = "no_boleta";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_ORIGEN = "origen";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";
    public final static String CAMPO_ID_REMESA_MOVIL="id_remesa_movil";
    public final static String CAMPO_CODIGO_DISPOSITIVO="codigo_dispositivo";

    private BigDecimal tcscremesaid;
    private BigDecimal tcscjornadavenid;
    private BigDecimal tcscsolicitudid;
    private BigDecimal tcscctabancariaid;
    private String banco;
    private BigDecimal monto;
    private BigDecimal tasa_cambio;
    private String no_boleta;
    private String estado;
    private String origen;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    private String id_remesa_movil;
    private String codigo_dispositivo;
    
    public BigDecimal getTcscremesaid() {
        return tcscremesaid;
    }
    public void setTcscremesaid(BigDecimal tcscremesaid) {
        this.tcscremesaid = tcscremesaid;
    }
    public BigDecimal getTcscjornadavenid() {
        return tcscjornadavenid;
    }
    public void setTcscjornadavenid(BigDecimal tcscjornadavenid) {
        this.tcscjornadavenid = tcscjornadavenid;
    }
    public BigDecimal getTcscsolicitudid() {
        return tcscsolicitudid;
    }
    public void setTcscsolicitudid(BigDecimal tcscsolicitudid) {
        this.tcscsolicitudid = tcscsolicitudid;
    }
    public BigDecimal getTcscctabancariaid() {
        return tcscctabancariaid;
    }
    public void setTcscctabancariaid(BigDecimal tcscctabancariaid) {
        this.tcscctabancariaid = tcscctabancariaid;
    }
    public String getBanco() {
        return banco;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }
    public BigDecimal getMonto() {
        return monto;
    }
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    public BigDecimal getTasa_cambio() {
        return tasa_cambio;
    }
    public void setTasa_cambio(BigDecimal tasa_cambio) {
        this.tasa_cambio = tasa_cambio;
    }
    public String getNo_boleta() {
        return no_boleta;
    }
    public void setNo_boleta(String no_boleta) {
        this.no_boleta = no_boleta;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getOrigen() {
        return origen;
    }
    public void setOrigen(String origen) {
        this.origen = origen;
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
	public String getId_remesa_movil() {
		return id_remesa_movil;
	}
	public void setId_remesa_movil(String id_remesa_movil) {
		this.id_remesa_movil = id_remesa_movil;
	}
	public String getCodigo_dispositivo() {
		return codigo_dispositivo;
	}
	public void setCodigo_dispositivo(String codigo_dispositivo) {
		this.codigo_dispositivo = codigo_dispositivo;
	}
	
}