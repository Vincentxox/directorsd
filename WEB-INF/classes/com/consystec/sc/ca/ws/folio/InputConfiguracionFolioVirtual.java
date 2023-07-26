 package com.consystec.sc.ca.ws.folio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputConfiguracionFolioVirtual")
public class InputConfiguracionFolioVirtual {
    @XmlElement
    private String idFolio;
    @XmlElement
    private String idTipo;
    @XmlElement
    private String tipo;
    @XmlElement
    private String tipoDocumento;
    @XmlElement
    private String serie;
    @XmlElement
    private String noInicialFolio;
    @XmlElement
    private String noFinalFolio;
    @XmlElement
    private String cant_utilizados;
    @XmlElement
    private String ultimo_utilizado;
    @XmlElement
    private String folio_siguiente;
    @XmlElement
    private String estado;
    @XmlElement
    private String codOficina;
    @XmlElement
    private String codVendedor;
    @XmlElement
    private String creado_el;
    @XmlElement
    private String creado_por;
    @XmlElement
    private String modificado_el;
    @XmlElement
    private String modificado_por;
    @XmlElement
    private String fecha_resolucion;
    @XmlElement
    private String resolucion;
    
    public String getFecha_resolucion() {
		return fecha_resolucion;
	}
	public void setFecha_resolucion(String fecha_resolucion) {
		this.fecha_resolucion = fecha_resolucion;
	}
	public String getResolucion() {
		return resolucion;
	}
	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}
	public String getIdFolio() {
        return idFolio;
    }
    public void setIdFolio(String idFolio) {
        this.idFolio = idFolio;
    }
    public String getIdTipo() {
        return idTipo;
    }
    public void setIdTipo(String idTipo) {
        this.idTipo = idTipo;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getTipoDocumento() {
        return tipoDocumento;
    }
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    public String getSerie() {
        return serie;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }
    public String getNoInicialFolio() {
        return noInicialFolio;
    }
    public void setNoInicialFolio(String noInicialFolio) {
        this.noInicialFolio = noInicialFolio;
    }
    public String getNoFinalFolio() {
        return noFinalFolio;
    }
    public void setNoFinalFolio(String noFinalFolio) {
        this.noFinalFolio = noFinalFolio;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getCreado_el() {
        return creado_el;
    }
    public void setCreado_el(String creado_el) {
        this.creado_el = creado_el;
    }
    public String getCreado_por() {
        return creado_por;
    }
    public void setCreado_por(String creado_por) {
        this.creado_por = creado_por;
    }
    public String getModificado_el() {
        return modificado_el;
    }
    public void setModificado_el(String modificado_el) {
        this.modificado_el = modificado_el;
    }
    public String getModificado_por() {
        return modificado_por;
    }
    public void setModificado_por(String modificado_por) {
        this.modificado_por = modificado_por;
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
    public String getCodOficina() {
        return codOficina;
    }
    public void setCodOficina(String codOficina) {
        this.codOficina = codOficina;
    }
    public String getCodVendedor() {
        return codVendedor;
    }
    public void setCodVendedor(String codVendedor) {
        this.codVendedor = codVendedor;
    }
}