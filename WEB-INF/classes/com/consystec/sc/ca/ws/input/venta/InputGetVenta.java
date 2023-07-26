package com.consystec.sc.ca.ws.input.venta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputGetVenta")
public class InputGetVenta {
    @XmlElement
    private String token;
    @XmlElement
    private String usuario;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String codArea;
    @XmlElement
    private String idVenta;
    @XmlElement
    private String idVendedor;
    @XmlElement
    private String idDTS;
    @XmlElement
    private String idJornada;
    @XmlElement
    private String idRutaPanel;
    @XmlElement
    private String tipoRutaPanel;
    @XmlElement
    private String idTipo;
    @XmlElement
    private String tipoCliente;
    @XmlElement
    private String tipoDocumento;
    @XmlElement
    private String serie;
    @XmlElement
    private String folio;
    @XmlElement
    private String serieSidra;
    @XmlElement
    private String folioSidra;
    @XmlElement
    private String nit;
    @XmlElement
    private String estado;
    @XmlElement
    private String primerNombreCliente;
    @XmlElement
    private String primerApellidoCliente;
    @XmlElement
    private String numTelefono;
    @XmlElement
    private String tipoDocumentoCliente;
    @XmlElement
    private String numDocCliente;
    @XmlElement
    private String fechaInicio;
    @XmlElement
    private String fechaFin;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getIdVenta() {
        return idVenta;
    }
    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }
    public String getIdVendedor() {
        return idVendedor;
    }
    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }
    public String getIdDTS() {
        return idDTS;
    }
    public void setIdDTS(String idDTS) {
        this.idDTS = idDTS;
    }
    public String getIdJornada() {
        return idJornada;
    }
    public void setIdJornada(String idJornada) {
        this.idJornada = idJornada;
    }
    public String getIdRutaPanel() {
        return idRutaPanel;
    }
    public void setIdRutaPanel(String idRutaPanel) {
        this.idRutaPanel = idRutaPanel;
    }
    public String getTipoRutaPanel() {
        return tipoRutaPanel;
    }
    public void setTipoRutaPanel(String tipoRutaPanel) {
        this.tipoRutaPanel = tipoRutaPanel;
    }
    public String getIdTipo() {
        return idTipo;
    }
    public void setIdTipo(String idTipo) {
        this.idTipo = idTipo;
    }
    public String getTipoCliente() {
        return tipoCliente;
    }
    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
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
    public String getFolio() {
        return folio;
    }
    public void setFolio(String folio) {
        this.folio = folio;
    }
    public String getSerieSidra() {
        return serieSidra;
    }
    public void setSerieSidra(String serieSidra) {
        this.serieSidra = serieSidra;
    }
    public String getFolioSidra() {
        return folioSidra;
    }
    public void setFolioSidra(String folioSidra) {
        this.folioSidra = folioSidra;
    }
    public String getNit() {
        return nit;
    }
    public void setNit(String nit) {
        this.nit = nit;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getPrimerNombreCliente() {
        return primerNombreCliente;
    }
    public void setPrimerNombreCliente(String primerNombreCliente) {
        this.primerNombreCliente = primerNombreCliente;
    }
    public String getPrimerApellidoCliente() {
        return primerApellidoCliente;
    }
    public void setPrimerApellidoCliente(String primerApellidoCliente) {
        this.primerApellidoCliente = primerApellidoCliente;
    }
    public String getNumTelefono() {
        return numTelefono;
    }
    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }
    public String getTipoDocumentoCliente() {
        return tipoDocumentoCliente;
    }
    public void setTipoDocumentoCliente(String tipoDocumentoCliente) {
        this.tipoDocumentoCliente = tipoDocumentoCliente;
    }
    public String getNumDocCliente() {
        return numDocCliente;
    }
    public void setNumDocCliente(String numDocCliente) {
        this.numDocCliente = numDocCliente;
    }
    public String getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public String getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
	public String getCodDispositivo() {
		return codDispositivo;
	}
	public void setCodDispositivo(String codDispositivo) {
		this.codDispositivo = codDispositivo;
	}
    
}