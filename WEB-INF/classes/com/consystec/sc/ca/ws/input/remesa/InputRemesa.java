package com.consystec.sc.ca.ws.input.remesa;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputRemesa")
public class InputRemesa {
    @XmlElement
    private String token;
    @XmlElement
    private String codArea;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String usuario;
    @XmlElement
    private String idRemesa;
    @XmlElement
    private String idApp;
    @XmlElement
    private String origen;
    @XmlElement
    private String idDistribuidor;
    @XmlElement
    private String nombreDistribuidor;
    @XmlElement
    private String idVendedor;
    @XmlElement
    private String nombreVendedor;
    @XmlElement
    private String idTipo;
    @XmlElement
    private String tipo;
    @XmlElement
    private String nombreTipo;
    @XmlElement
    private String idJornada;
    @XmlElement
    private String estadoJornada;
    @XmlElement
    private String fechaInicioJornada;
    @XmlElement
    private String estadoLiqJornada;
    @XmlElement
    private String fechaFinJornada;
    @XmlElement
    private String fechaLiqJornada;
    @XmlElement
    private String totalVentas;
    @XmlElement
    private String totaltarjetaCredito;
    @XmlElement
    private String totalCredito;
    @XmlElement
    private String totalMpos;
    @XmlElement
    private String cantVentas;
    @XmlElement
    private String monto;
    @XmlElement
    private String tasaCambio;
    @XmlElement
    private String noBoleta;
    @XmlElement
    private String banco;
    @XmlElement
    private String idDeuda;
    @XmlElement
    private String idCuenta;
    @XmlElement
    private String noCuenta;
    @XmlElement
    private String tipoCuenta;
    @XmlElement
    private String nombreCuenta;
    @XmlElement
    private String estado;
    @XmlElement
    private String creado_el;
    @XmlElement
    private String creado_por;
    @XmlElement
    private String modificado_el;
    @XmlElement
    private String modificado_por;
    @XmlElement
    private String fechaInicio;
    @XmlElement
    private String fechaFin;
    @XmlElement
    private String guardadoExitosamente;
    @XmlElement
    private List<InputRemesa> remesas;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getCodDispositivo() {
        return codDispositivo;
    }
    public void setCodDispositivo(String codDispositivo) {
        this.codDispositivo = codDispositivo;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getIdRemesa() {
        return idRemesa;
    }
    public void setIdRemesa(String idRemesa) {
        this.idRemesa = idRemesa;
    }
    public String getIdApp() {
        return idApp;
    }
    public void setIdApp(String idApp) {
        this.idApp = idApp;
    }
    public String getOrigen() {
        return origen;
    }
    public void setOrigen(String origen) {
        this.origen = origen;
    }
    public String getIdDistribuidor() {
        return idDistribuidor;
    }
    public void setIdDistribuidor(String idDistribuidor) {
        this.idDistribuidor = idDistribuidor;
    }
    public String getNombreDistribuidor() {
        return nombreDistribuidor;
    }
    public void setNombreDistribuidor(String nombreDistribuidor) {
        this.nombreDistribuidor = nombreDistribuidor;
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
    public String getNombreTipo() {
        return nombreTipo;
    }
    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }
    public String getIdJornada() {
        return idJornada;
    }
    public void setIdJornada(String idJornada) {
        this.idJornada = idJornada;
    }
    public String getEstadoJornada() {
        return estadoJornada;
    }
    public void setEstadoJornada(String estadoJornada) {
        this.estadoJornada = estadoJornada;
    }
    public String getFechaInicioJornada() {
        return fechaInicioJornada;
    }
    public void setFechaInicioJornada(String fechaInicioJornada) {
        this.fechaInicioJornada = fechaInicioJornada;
    }
    public String getEstadoLiqJornada() {
        return estadoLiqJornada;
    }
    public void setEstadoLiqJornada(String estadoLiqJornada) {
        this.estadoLiqJornada = estadoLiqJornada;
    }
    public String getFechaFinJornada() {
        return fechaFinJornada;
    }
    public void setFechaFinJornada(String fechaFinJornada) {
        this.fechaFinJornada = fechaFinJornada;
    }
    public String getFechaLiqJornada() {
        return fechaLiqJornada;
    }
    public void setFechaLiqJornada(String fechaLiqJornada) {
        this.fechaLiqJornada = fechaLiqJornada;
    }
    public String getTotalVentas() {
        return totalVentas;
    }
    public void setTotalVentas(String totalVentas) {
        this.totalVentas = totalVentas;
    }
    public String getTotaltarjetaCredito() {
        return totaltarjetaCredito;
    }
    public void setTotaltarjetaCredito(String totaltarjetaCredito) {
        this.totaltarjetaCredito = totaltarjetaCredito;
    }
    
    public String getTotalCredito() {
		return totalCredito;
	}
	public void setTotalCredito(String totalCredito) {
		this.totalCredito = totalCredito;
	}
	
	public String getTotalMpos() {
		return totalMpos;
	}
	public void setTotalMpos(String totalMpos) {
		this.totalMpos = totalMpos;
	}
	public String getCantVentas() {
        return cantVentas;
    }
    public void setCantVentas(String cantVentas) {
        this.cantVentas = cantVentas;
    }
    public String getMonto() {
        return monto;
    }
    public void setMonto(String monto) {
        this.monto = monto;
    }
    public String getTasaCambio() {
        return tasaCambio;
    }
    public void setTasaCambio(String tasaCambio) {
        this.tasaCambio = tasaCambio;
    }
    public String getNoBoleta() {
        return noBoleta;
    }
    public void setNoBoleta(String noBoleta) {
        this.noBoleta = noBoleta;
    }
    public String getBanco() {
        return banco;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }
    public String getIdDeuda() {
        return idDeuda;
    }
    public void setIdDeuda(String idDeuda) {
        this.idDeuda = idDeuda;
    }
    public String getIdCuenta() {
        return idCuenta;
    }
    public void setIdCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
    }
    public String getNoCuenta() {
        return noCuenta;
    }
    public void setNoCuenta(String noCuenta) {
        this.noCuenta = noCuenta;
    }
    public String getTipoCuenta() {
        return tipoCuenta;
    }
    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
    public String getNombreCuenta() {
        return nombreCuenta;
    }
    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
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
    public List<InputRemesa> getRemesas() {
        return remesas;
    }
    public void setRemesas(List<InputRemesa> remesas) {
        this.remesas = remesas;
    }
	public String getGuardadoExitosamente() {
		return guardadoExitosamente;
	}
	public void setGuardadoExitosamente(String guardadoExitosamente) {
		this.guardadoExitosamente = guardadoExitosamente;
	}
}