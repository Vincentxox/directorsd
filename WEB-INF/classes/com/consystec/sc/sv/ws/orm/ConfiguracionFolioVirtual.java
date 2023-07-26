package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ConfiguracionFolioVirtual {
    public static final String N_TABLA = "TC_SC_FOLIO";
    public static final String SEQUENCE = "TC_SC_FOLIO_SQ.nextval";

    public final static String CAMPO_TC_SC_FOLIO_ID = "TCSCFOLIOID";
    public final static String CAMPO_TC_SC_CATPAISID = "TCSCCATPAISID";
    public final static String CAMPO_ID_TIPO = "IDTIPO";
    public final static String CAMPO_TIPO = "TIPO";
    public final static String CAMPO_TIPODOCUMENTO = "TIPODOCUMENTO";
    public final static String CAMPO_SERIE = "SERIE";
    public final static String CAMPO_NO_INICIAL_FOLIO = "NO_INICIALFOLIO";
    public final static String CAMPO_NO_FINAL_FOLIO = "NO_FINALFOLIO";
    public final static String CAMPO_CANT_UTILIZADOS = "CANT_UTILIZADOS";
    public final static String CAMPO_ULTIMO_UTILIZADO = "ULTIMO_UTILIZADO";
    public final static String CAMPO_FOLIO_SIGUIENTE = "FOLIO_SIGUIENTE";
    public final static String CAMPO_ESTADO = "ESTADO";
    public final static String CAMPO_CREADO_EL = "CREADO_EL";
    public final static String CAMPO_CREADO_POR = "CREADO_POR";
    public final static String CAMPO_MODIFICADO_EL = "MODIFICADO_EL";
    public final static String CAMPO_MODIFICADO_POR = "MODIFICADO_POR";
    public final static String CAMPO_ID_RESERVA = "ID_RESERVA";
    public final static String CAMPO_COD_OFICINA = "COD_OFICINA";
    public final static String CAMPO_COD_VENDE = "COD_VENDEDOR";
    public final static String CAMPO_RESOLUCION = "RESOLUCION";
    public final static String CAMPO_FECHA_RESOLUCION = "FECHA_RESOLUCION";

    private BigDecimal tcScFolioId;
    private BigDecimal tcscatpaisid;
    private String idTipo;
    private String Tipo;
    private String tipoDocuento;
    private String serie;
    private BigDecimal no_InicialFolio;
    private BigDecimal no_FinalFolio;
    private String cant_utilizados;
    private String ultimo_utilizado;
    private String folio_siguiente;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    private String resolucion;
    private Timestamp fecha_resolucion;
    
    public BigDecimal getTcScFolioId() {
        return tcScFolioId;
    }
    public void setTcScFolioId(BigDecimal tcScFolioId) {
        this.tcScFolioId = tcScFolioId;
    }
    public String getIdTipo() {
        return idTipo;
    }
    public void setIdTipo(String idTipo) {
        this.idTipo = idTipo;
    }
    public String getTipo() {
        return Tipo;
    }
    public void setTipo(String tipo) {
        Tipo = tipo;
    }
    public String getTipoDocuento() {
        return tipoDocuento;
    }
    public void setTipoDocuento(String tipoDocuento) {
        this.tipoDocuento = tipoDocuento;
    }
    public String getSerie() {
        return serie;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }
    public BigDecimal getNo_InicialFolio() {
        return no_InicialFolio;
    }
    public void setNo_InicialFolio(BigDecimal no_InicialFolio) {
        this.no_InicialFolio = no_InicialFolio;
    }
    public BigDecimal getNo_FinalFolio() {
        return no_FinalFolio;
    }
    public void setNo_FinalFolio(BigDecimal no_FinalFolio) {
        this.no_FinalFolio = no_FinalFolio;
    }
    public String getCant_utilizados() {
        return cant_utilizados;
    }
    public void setCant_utilizados(String cant_utilizados) {
        this.cant_utilizados = cant_utilizados;
    }
    public String getUltimo_utilizado() {
        return ultimo_utilizado;
    }
    public void setUltimo_utilizado(String ultimo_utilizado) {
        this.ultimo_utilizado = ultimo_utilizado;
    }
    public String getFolio_siguiente() {
        return folio_siguiente;
    }
    public void setFolio_siguiente(String folio_siguiente) {
        this.folio_siguiente = folio_siguiente;
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
	public BigDecimal getTcscatpaisid() {
		return tcscatpaisid;
	}
	public void setTcscatpaisid(BigDecimal tcscatpaisid) {
		this.tcscatpaisid = tcscatpaisid;
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
    
}