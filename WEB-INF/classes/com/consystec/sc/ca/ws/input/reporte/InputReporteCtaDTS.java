package com.consystec.sc.ca.ws.input.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputReporteCtaDTS")
public class InputReporteCtaDTS {
    @XmlElement
    private String codArea;
    @XmlElement
	private String usuario;
    @XmlElement
    private String idDTS;
    @XmlElement
    private String nombreDTS;
    @XmlElement
    private String idVenta;
    @XmlElement
	private String idVendedor;
    @XmlElement
    private String usuarioVendedor;
	@XmlElement
    private String nombreVendedor;
	@XmlElement
    private String totalVendido;
	@XmlElement
    private String moneda;
	@XmlElement
    private String tasaCambio;
	@XmlElement
    private String fecha;
    @XmlElement
    private String tipoCliente;
    @XmlElement
    private String idRutaPanel;
    @XmlElement
    private String tipoRutaPanel;
    @XmlElement
    private String nombreRutaPanel;
    @XmlElement
    private String banco;
    @XmlElement
    private String cuenta;
    @XmlElement
    private String noDeposito;
    @XmlElement
    private String montoDeposito;
    @XmlElement
    private String fechaDeposito;
	@XmlElement
    private String soc;
    @XmlElement
    private String asignacion;
    @XmlElement
    private String referencia;
    @XmlElement
    private String usuarioHaber;
    @XmlElement
    private String ctaCP;
    @XmlElement
    private String feContab;
    @XmlElement
    private String fechaDoc;
    @XmlElement
    private String debe;
    @XmlElement
    private String haber;
    @XmlElement
    private String saldo;
    @XmlElement
    private String fechaCreacion;
    @XmlElement
    private String fechaGeneracion;
    @XmlElement
    private String creadoEl;
    @XmlElement
    private String creadoPor;
    @XmlElement
    private List<InputReporteCtaDTS> datos;

    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getUsuarioVendedor() {
        return usuarioVendedor;
    }
    public void setUsuarioVendedor(String usuarioVendedor) {
        this.usuarioVendedor = usuarioVendedor;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getIdDTS() {
        return idDTS;
    }
    public void setIdDTS(String idDTS) {
        this.idDTS = idDTS;
    }
    public String getNombreDTS() {
        return nombreDTS;
    }
    public void setNombreDTS(String nombreDTS) {
        this.nombreDTS = nombreDTS;
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
    public String getNombreVendedor() {
        return nombreVendedor;
    }
    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }
    public String getTotalVendido() {
        return totalVendido;
    }
    public void setTotalVendido(String totalVendido) {
        this.totalVendido = totalVendido;
    }
    public String getMoneda() {
        return moneda;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    public String getTasaCambio() {
        return tasaCambio;
    }
    public void setTasaCambio(String tasaCambio) {
        this.tasaCambio = tasaCambio;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getTipoCliente() {
        return tipoCliente;
    }
    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
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
    public String getNombreRutaPanel() {
        return nombreRutaPanel;
    }
    public void setNombreRutaPanel(String nombreRutaPanel) {
        this.nombreRutaPanel = nombreRutaPanel;
    }
    public String getBanco() {
        return banco;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }
    public String getCuenta() {
        return cuenta;
    }
    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }
    public String getNoDeposito() {
        return noDeposito;
    }
    public void setNoDeposito(String noDeposito) {
        this.noDeposito = noDeposito;
    }
    public String getMontoDeposito() {
        return montoDeposito;
    }
    public void setMontoDeposito(String montoDeposito) {
        this.montoDeposito = montoDeposito;
    }
    public String getFechaDeposito() {
        return fechaDeposito;
    }
    public void setFechaDeposito(String fechaDeposito) {
        this.fechaDeposito = fechaDeposito;
    }
    public String getSoc() {
        return soc;
    }
    public void setSoc(String soc) {
        this.soc = soc;
    }
    public String getAsignacion() {
        return asignacion;
    }
    public void setAsignacion(String asignacion) {
        this.asignacion = asignacion;
    }
    public String getReferencia() {
        return referencia;
    }
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
    public String getUsuarioHaber() {
        return usuarioHaber;
    }
    public void setUsuarioHaber(String usuarioHaber) {
        this.usuarioHaber = usuarioHaber;
    }
    public String getCtaCP() {
        return ctaCP;
    }
    public void setCtaCP(String ctaCP) {
        this.ctaCP = ctaCP;
    }
    public String getFeContab() {
        return feContab;
    }
    public void setFeContab(String feContab) {
        this.feContab = feContab;
    }
    public String getFechaDoc() {
        return fechaDoc;
    }
    public void setFechaDoc(String fechaDoc) {
        this.fechaDoc = fechaDoc;
    }
    public String getDebe() {
        return debe;
    }
    public void setDebe(String debe) {
        this.debe = debe;
    }
    public String getHaber() {
        return haber;
    }
    public void setHaber(String haber) {
        this.haber = haber;
    }
    public String getSaldo() {
        return saldo;
    }
    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }
    public String getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public String getFechaGeneracion() {
        return fechaGeneracion;
    }
    public void setFechaGeneracion(String fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
    public List<InputReporteCtaDTS> getDatos() {
        return datos;
    }
    public void setDatos(List<InputReporteCtaDTS> datos) {
        this.datos = datos;
    }
	public String getCreadoEl() {
		return creadoEl;
	}
	public void setCreadoEl(String creadoEl) {
		this.creadoEl = creadoEl;
	}
	public String getCreadoPor() {
		return creadoPor;
	}
	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}
    
    
}