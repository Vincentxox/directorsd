package com.consystec.sc.sv.ws.orm;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class AdjuntoPorta {
	public static final String N_TABLA = "TC_SC_ADJUNTO_PORTA";
	public static final String SEQUENCE = "TC_SC_ADJUNTO_PORTA_SQ.nextval";	
	public final static String CAMPO_TCSCADJUNTOPORTAID = "tcscadjuntoportaid";
	public final static String CAMPO_TCSCPORTABILIDADID = "tcscportabilidadid";
	public final static String CAMPO_IDPORTAMOVIL = "idportamovil";
	public final static String CAMPO_NOMBRE_ARCHIVO = "nombre_archivo";
	public final static String CAMPO_EXTENSION = "extension";
	public final static String CAMPO_TIPO_ARCHIVO = "tipo_archivo";
	public final static String CAMPO_IDATTACHMENT = "idattachment";
	public final static String CAMPO_URL_REPOSITORIO = "url_repositorio";
	public final static String CAMPO_CREADO_EL = "creado_el";
	public final static String CAMPO_CREADO_POR= "creado_por";
	public final static String CAMPO_MODIFICADO_EL= "modificado_el";
	public final static String CAMPO_MODIFICADO_POR= "modificado_por";
	
	private BigDecimal tcscadjuntoportaid;
	private BigDecimal tcscportabilidadid;
	private BigDecimal idportalmovil;
	private String nombre_archivo;
	private String extension;
	private String tipo_archivo;
	private BigDecimal idattachment;
	private String url_repositorio;
	private Timestamp creado_el;
	private String creado_por;
	private Timestamp modificado_el;
	private String modificado_por;
	private byte[] archvio;
	
	public BigDecimal getTcscadjuntoportaid() {
		return tcscadjuntoportaid;
	}
	public void setTcscadjuntoportaid(BigDecimal tcscadjuntoportaid) {
		this.tcscadjuntoportaid = tcscadjuntoportaid;
	}
	public BigDecimal getTcscportabilidadid() {
		return tcscportabilidadid;
	}
	public void setTcscportabilidadid(BigDecimal tcscportabilidadid) {
		this.tcscportabilidadid = tcscportabilidadid;
	}
	public BigDecimal getIdportalmovil() {
		return idportalmovil;
	}
	public void setIdportalmovil(BigDecimal idportalmovil) {
		this.idportalmovil = idportalmovil;
	}
	public String getNombre_archivo() {
		return nombre_archivo;
	}
	public void setNombre_archivo(String nombre_archivo) {
		this.nombre_archivo = nombre_archivo;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getTipo_archivo() {
		return tipo_archivo;
	}
	public void setTipo_archivo(String tipo_archivo) {
		this.tipo_archivo = tipo_archivo;
	}
	public BigDecimal getIdattachment() {
		return idattachment;
	}
	public void setIdattachment(BigDecimal idattachment) {
		this.idattachment = idattachment;
	}
	public String getUrl_repositorio() {
		return url_repositorio;
	}
	public void setUrl_repositorio(String url_repositorio) {
		this.url_repositorio = url_repositorio;
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
	public byte[] getArchvio() {
		return archvio;
	}
	public void setArchvio(byte[] archvio) {
		this.archvio = archvio;
	}
	
	

}
