package com.consystec.sc.ca.ws.input.jornada;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.vendedordts.InputVendedorDTS;
import com.consystec.sc.ca.ws.input.venta.DetallePago;

import javax.xml.bind.annotation.XmlAccessType;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputJornada")
public class InputJornada {
    @XmlElement
    private String codArea;
    @XmlElement
    private String token;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String usuario;
    @XmlElement
    private String idJornada;
    @XmlElement
    private String idJornadaResponsable;
    @XmlElement
    private String idVendedor;
    @XmlElement
    private String nombreVendedor;
    @XmlElement
    private String usuarioVendedor;
    @XmlElement
    private String dispositivoJornada;
    @XmlElement
    private String idResponsable;
    @XmlElement
    private String nombreResponsable;
    @XmlElement
    private String idDistribuidor;
    @XmlElement
    private String nombreDistribuidor;
    @XmlElement
    private String idTipo;
    @XmlElement
    private String tipo;
    @XmlElement
    private String nombreTipo;
    @XmlElement
    private String idBodegaVirtual;
    @XmlElement
    private String nombreBodegaVirtual;
    @XmlElement
    private String idBodegaVendedor;
    @XmlElement
    private String idBodegaPanelRuta;
    @XmlElement
    private String nombreBodegaPanelRuta;
    @XmlElement
    private String tipoOperacion;
    @XmlElement
    private String saldoInicial;
    @XmlElement
    private String estado;
    @XmlElement
    private String envioAlarma;
    @XmlElement
    private String tipoAlarma;
    @XmlElement
    private String envioAlarmaFin;
    @XmlElement
    private String tipoAlarmaFin;
    @XmlElement
    private String fecha;
    @XmlElement
    private String fechaCierre;
    @XmlElement
    private String fechaDesde;
    @XmlElement
    private String fechaHasta;
    @XmlElement
    private String observaciones;
    @XmlElement
    private String fechaFinalizacion;
    @XmlElement
    private String codDispositivoFinalizacion;
    @XmlElement
    private String fechaFinDesde;
    @XmlElement
    private String fechaFinHasta;
    @XmlElement
    private String estadoLiquidacion;
    @XmlElement
    private String fechaLiquidacion;
    @XmlElement
    private String fechaLiqDesde;
    @XmlElement
    private String fechaLiqHasta;
    @XmlElement
    private String totalVentas;
    @XmlElement
    private List<DetallePago> detallePagos;
    @XmlElement
    private String idDeuda;
    @XmlElement
    private String estadoPago;
    @XmlElement
    private List<InputObservacionesJornada> obsLiquidacion;
    @XmlElement
    private List<InputVendedorDTS> vendedores;
    @XmlElement
    private String creado_el;
    @XmlElement
    private String creado_por;
    @XmlElement
    private String modificado_el;
    @XmlElement
    private String modificado_por;
    @XmlElement
    private String mostrarDetPago;
    @XmlElement
    private String mostrarObservacion;
    @XmlElement
    private String appVersion;
    @XmlElement
    private String androidVersion;
    
    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
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
    public String getIdJornada() {
        return idJornada;
    }
    public void setIdJornada(String idJornada) {
        this.idJornada = idJornada;
    }
    public String getIdJornadaResponsable() {
        return idJornadaResponsable;
    }
    public void setIdJornadaResponsable(String idJornadaResponsable) {
        this.idJornadaResponsable = idJornadaResponsable;
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
    public String getUsuarioVendedor() {
        return usuarioVendedor;
    }
    public void setUsuarioVendedor(String usuarioVendedor) {
        this.usuarioVendedor = usuarioVendedor;
    }
    public String getDispositivoJornada() {
        return dispositivoJornada;
    }
    public void setDispositivoJornada(String dispositivoJornada) {
        this.dispositivoJornada = dispositivoJornada;
    }
    public String getIdResponsable() {
        return idResponsable;
    }
    public void setIdResponsable(String idResponsable) {
        this.idResponsable = idResponsable;
    }
    public String getNombreResponsable() {
        return nombreResponsable;
    }
    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
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
    public String getIdBodegaVirtual() {
        return idBodegaVirtual;
    }
    public void setIdBodegaVirtual(String idBodegaVirtual) {
        this.idBodegaVirtual = idBodegaVirtual;
    }
    public String getNombreBodegaVirtual() {
        return nombreBodegaVirtual;
    }
    public void setNombreBodegaVirtual(String nombreBodegaVirtual) {
        this.nombreBodegaVirtual = nombreBodegaVirtual;
    }
    public String getIdBodegaVendedor() {
        return idBodegaVendedor;
    }
    public void setIdBodegaVendedor(String idBodegaVendedor) {
        this.idBodegaVendedor = idBodegaVendedor;
    }
    public String getTipoOperacion() {
        return tipoOperacion;
    }
    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }
    public String getSaldoInicial() {
        return saldoInicial;
    }
    public void setSaldoInicial(String saldoInicial) {
        this.saldoInicial = saldoInicial;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getEnvioAlarma() {
        return envioAlarma;
    }
    public void setEnvioAlarma(String envioAlarma) {
        this.envioAlarma = envioAlarma;
    }
    public String getTipoAlarma() {
        return tipoAlarma;
    }
    public void setTipoAlarma(String tipoAlarma) {
        this.tipoAlarma = tipoAlarma;
    }
    public String getEnvioAlarmaFin() {
        return envioAlarmaFin;
    }
    public void setEnvioAlarmaFin(String envioAlarmaFin) {
        this.envioAlarmaFin = envioAlarmaFin;
    }
    public String getTipoAlarmaFin() {
        return tipoAlarmaFin;
    }
    public void setTipoAlarmaFin(String tipoAlarmaFin) {
        this.tipoAlarmaFin = tipoAlarmaFin;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getFechaCierre() {
        return fechaCierre;
    }
    public void setFechaCierre(String fechaCierre) {
        this.fechaCierre = fechaCierre;
    }
    public String getFechaDesde() {
        return fechaDesde;
    }
    public void setFechaDesde(String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }
    public String getFechaHasta() {
        return fechaHasta;
    }
    public void setFechaHasta(String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public String getFechaFinalizacion() {
        return fechaFinalizacion;
    }
    public void setFechaFinalizacion(String fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }
    public String getCodDispositivoFinalizacion() {
        return codDispositivoFinalizacion;
    }
    public void setCodDispositivoFinalizacion(String codDispositivoFinalizacion) {
        this.codDispositivoFinalizacion = codDispositivoFinalizacion;
    }
    public String getFechaFinDesde() {
        return fechaFinDesde;
    }
    public void setFechaFinDesde(String fechaFinDesde) {
        this.fechaFinDesde = fechaFinDesde;
    }
    public String getFechaFinHasta() {
        return fechaFinHasta;
    }
    public void setFechaFinHasta(String fechaFinHasta) {
        this.fechaFinHasta = fechaFinHasta;
    }
    public String getEstadoLiquidacion() {
        return estadoLiquidacion;
    }
    public void setEstadoLiquidacion(String estadoLiquidacion) {
        this.estadoLiquidacion = estadoLiquidacion;
    }
    public String getFechaLiquidacion() {
        return fechaLiquidacion;
    }
    public void setFechaLiquidacion(String fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }
    public String getFechaLiqDesde() {
        return fechaLiqDesde;
    }
    public void setFechaLiqDesde(String fechaLiqDesde) {
        this.fechaLiqDesde = fechaLiqDesde;
    }
    public String getFechaLiqHasta() {
        return fechaLiqHasta;
    }
    public void setFechaLiqHasta(String fechaLiqHasta) {
        this.fechaLiqHasta = fechaLiqHasta;
    }
    public String getTotalVentas() {
        return totalVentas;
    }
    public void setTotalVentas(String totalVentas) {
        this.totalVentas = totalVentas;
    }
    public String getIdDeuda() {
        return idDeuda;
    }
    public void setIdDeuda(String idDeuda) {
        this.idDeuda = idDeuda;
    }
    public String getEstadoPago() {
        return estadoPago;
    }
    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }
    public List<InputObservacionesJornada> getObsLiquidacion() {
        return obsLiquidacion;
    }
    public void setObsLiquidacion(List<InputObservacionesJornada> obsLiquidacion) {
        this.obsLiquidacion = obsLiquidacion;
    }
    public List<InputVendedorDTS> getVendedores() {
        return vendedores;
    }
    public void setVendedores(List<InputVendedorDTS> vendedores) {
        this.vendedores = vendedores;
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
    public List<DetallePago> getDetallePagos() {
        return detallePagos;
    }
    public void setDetallePagos(List<DetallePago> detallePagos) {
        this.detallePagos = detallePagos;
    }
	public String getMostrarDetPago() {
		return mostrarDetPago;
	}
	public void setMostrarDetPago(String mostrarDetPago) {
		this.mostrarDetPago = mostrarDetPago;
	}
	public String getMostrarObservacion() {
		return mostrarObservacion;
	}
	public void setMostrarObservacion(String mostarObservacion) {
		this.mostrarObservacion = mostarObservacion;
	}
	public String getIdBodegaPanelRuta() {
		return idBodegaPanelRuta;
	}
	public void setIdBodegaPanelRuta(String idBodegaPanelRuta) {
		this.idBodegaPanelRuta = idBodegaPanelRuta;
	}
	public String getNombreBodegaPanelRuta() {
		return nombreBodegaPanelRuta;
	}
	public void setNombreBodegaPanelRuta(String nombreBodegaPanelRuta) {
		this.nombreBodegaPanelRuta = nombreBodegaPanelRuta;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getAndroidVersion() {
		return androidVersion;
	}
	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}
    
}