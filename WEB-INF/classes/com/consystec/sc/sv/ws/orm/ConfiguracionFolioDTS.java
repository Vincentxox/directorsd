package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ConfiguracionFolioDTS {
    public static final String N_TABLA = "TC_SC_FOLIOBODEGA";
    public static final String SEQUENCE = "TC_SC_FOLIOBODEGA_SQ.nextval";

    public final static String CAMPO_TC_SC_FOLIO_BODEGA_ID = "TCSCFOLIOBODEGAID";
    public final static String CAMPO_TC_SC_ALMACENBODID = "TCSCALMACENBODID";
    public final static String CAMPO_TIPODOCUMENTO = "TIPODOCUMENTO";
    public final static String CAMPO_SERIE = "SERIE";
    public final static String CAMPO_NO_INICIAL_FOLIO = "NO_INICIALFOLIO";
    public final static String CAMPO_NO_FINAL_FOLIO = "NO_FINALFOLIO";
    public final static String CAMPO_ESTADO = "ESTADO";
    public final static String CAMPO_CREADO_EL = "CREADO_EL";
    public final static String CAMPO_CREADO_POR = "CREADO_POR";
    public final static String CAMPO_MODIFICADO_EL = "MODIFICADO_EL";
    public final static String CAMPO_MODIFICADO_POR = "MODIFICADO_POR";

    private BigDecimal tcScFolioBodegaId;
    private BigDecimal tcScAlmacenBodID;
    private String tipoDocuento;
    private String serie;
    private BigDecimal no_InicialFolio;
    private BigDecimal no_FinalFolio;
    private String estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;
    private String modificado_por;
    
    public BigDecimal getTcScFolioBodegaId() {
        return tcScFolioBodegaId;
    }
    public void setTcScFolioBodegaId(BigDecimal tcScFolioBodegaId) {
        this.tcScFolioBodegaId = tcScFolioBodegaId;
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
	public BigDecimal getTcScAlmacenBodID() {
		return tcScAlmacenBodID;
	}
	public void setTcScAlmacenBodID(BigDecimal tcScAlmacenBodID) {
		this.tcScAlmacenBodID = tcScAlmacenBodID;
	}
    
    
}
