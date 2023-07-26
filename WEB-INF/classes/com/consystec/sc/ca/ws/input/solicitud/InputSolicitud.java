package com.consystec.sc.ca.ws.input.solicitud;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.dispositivo.InputDispositivo;
import com.consystec.sc.ca.ws.input.jornada.InputObservacionesJornada;
import com.consystec.sc.ca.ws.input.remesa.InputRemesa;
import com.consystec.sc.ca.ws.input.venta.DetallePago;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputSolicitud")
public class InputSolicitud implements Cloneable {
    @XmlElement
    private String token;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String idSolicitud;
    @XmlElement
    private String idBodega;

    @XmlElement
    private String nombreBodega;
    @XmlElement
    private String idBodegaZona;
    @XmlElement
    private String nombreBodegaZona;
    @XmlElement
    private String idDTS;
    @XmlElement
    private String nombreDTS;
    @XmlElement
    private String buzonOrigen;
    @XmlElement
    private String idBuzon;
    @XmlElement
    private String nombreBuzon;
    @XmlElement
    private String idBuzonAnterior;
    @XmlElement
    private String idBuzonSiguiente;
    @XmlElement
    private String fecha;
    @XmlElement
    private String idVendedor;
    @XmlElement
    private String nombreVendedor;
    @XmlElement
    private String apellidoVendedor;
    @XmlElement
    private String usuarioVendedor;
    @XmlElement
    private String tipoSolicitud;
    @XmlElement
    private String tipoSiniestro;
    @XmlElement
    private String causaSolicitud;
    @XmlElement
    private String idPDV;
    @XmlElement
    private String idJornada;
    @XmlElement
    private String idTipo;
    @XmlElement
    private String tipo;
    @XmlElement
    private String nombreTipo;
    @XmlElement
    private String observaciones;
    @XmlElement
    private String seriado;
    @XmlElement
    private String origen;
    @XmlElement
    private String totalDeuda;
    @XmlElement
    private String tasaCambio;
    @XmlElement
    private String estado;
    @XmlElement
    private String origenCancelacion;
    @XmlElement
    private String obsCancelacion;
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
    private List<InputArticuloSolicitud> articulos;
    @XmlElement
    private List<InputDispositivo> dispositivos;
    @XmlElement
    private List<InputObservacionesJornada> obsSolicitud;
    @XmlElement
    private List<InputRemesa> remesas;
    @XmlElement
    private List<DetallePago> detallePagos;
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
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
    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getIdSolicitud() {
        return idSolicitud;
    }
    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }
    public String getIdBodega() {
        return idBodega;
    }
    public void setIdBodega(String idBodega) {
        this.idBodega = idBodega;
    }
    public String getNombreBodega() {
        return nombreBodega;
    }
    public void setNombreBodega(String nombreBodega) {
        this.nombreBodega = nombreBodega;
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
    public String getIdBuzon() {
        return idBuzon;
    }
    public void setIdBuzon(String idBuzon) {
        this.idBuzon = idBuzon;
    }
    public String getNombreBuzon() {
        return nombreBuzon;
    }
    public void setNombreBuzon(String nombreBuzon) {
        this.nombreBuzon = nombreBuzon;
    }
    public String getIdBuzonAnterior() {
        return idBuzonAnterior;
    }
    public void setIdBuzonAnterior(String idBuzonAnterior) {
        this.idBuzonAnterior = idBuzonAnterior;
    }
    public String getIdBuzonSiguiente() {
        return idBuzonSiguiente;
    }
    public void setIdBuzonSiguiente(String idBuzonSiguiente) {
        this.idBuzonSiguiente = idBuzonSiguiente;
    }
    public String getBuzonOrigen() {
        return buzonOrigen;
    }
    public void setBuzonOrigen(String buzonOrigen) {
        this.buzonOrigen = buzonOrigen;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
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
    public String getApellidoVendedor() {
        return apellidoVendedor;
    }
    public void setApellidoVendedor(String apellidoVendedor) {
        this.apellidoVendedor = apellidoVendedor;
    }
    public String getUsuarioVendedor() {
        return usuarioVendedor;
    }
    public void setUsuarioVendedor(String usuarioVendedor) {
        this.usuarioVendedor = usuarioVendedor;
    }
    public String getTipoSolicitud() {
        return tipoSolicitud;
    }
    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }
    public String getTipoSiniestro() {
        return tipoSiniestro;
    }
    public void setTipoSiniestro(String tipoSiniestro) {
        this.tipoSiniestro = tipoSiniestro;
    }
    public String getCausaSolicitud() {
        return causaSolicitud;
    }
    public void setCausaSolicitud(String causaSolicitud) {
        this.causaSolicitud = causaSolicitud;
    }
    public String getIdPDV() {
        return idPDV;
    }
    public void setIdPDV(String idPDV) {
        this.idPDV = idPDV;
    }
    public String getIdJornada() {
        return idJornada;
    }
    public void setIdJornada(String idJornada) {
        this.idJornada = idJornada;
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
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public String getSeriado() {
        return seriado;
    }
    public void setSeriado(String seriado) {
        this.seriado = seriado;
    }
    public String getOrigen() {
        return origen;
    }
    public void setOrigen(String origen) {
        this.origen = origen;
    }
    public String getTotalDeuda() {
        return totalDeuda;
    }
    public void setTotalDeuda(String totalDeuda) {
        this.totalDeuda = totalDeuda;
    }
    public String getTasaCambio() {
        return tasaCambio;
    }
    public void setTasaCambio(String tasaCambio) {
        this.tasaCambio = tasaCambio;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getOrigenCancelacion() {
        return origenCancelacion;
    }
    public void setOrigenCancelacion(String origenCancelacion) {
        this.origenCancelacion = origenCancelacion;
    }
    public String getObsCancelacion() {
        return obsCancelacion;
    }
    public void setObsCancelacion(String obsCancelacion) {
        this.obsCancelacion = obsCancelacion;
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
    public List<InputArticuloSolicitud> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<InputArticuloSolicitud> articulos) {
        this.articulos = articulos;
    }
    public List<InputDispositivo> getDispositivos() {
        return dispositivos;
    }
    public void setDispositivos(List<InputDispositivo> dispositivos) {
        this.dispositivos = dispositivos;
    }
    public List<InputObservacionesJornada> getObsSolicitud() {
        return obsSolicitud;
    }
    public void setObsSolicitud(List<InputObservacionesJornada> obsSolicitud) {
        this.obsSolicitud = obsSolicitud;
    }
    public List<InputRemesa> getRemesas() {
        return remesas;
    }
    public void setRemesas(List<InputRemesa> remesas) {
        this.remesas = remesas;
    }
    public List<DetallePago> getDetallePagos() {
        return detallePagos;
    }
    public void setDetallePagos(List<DetallePago> detallePagos) {
        this.detallePagos = detallePagos;
    }

	public String getIdBodegaZona() {
		return idBodegaZona;
	}

	public void setIdBodegaZona(String idBodegaZona) {
		this.idBodegaZona = idBodegaZona;
	}

	public String getNombreBodegaZona() {
		return nombreBodegaZona;
	}

	public void setNombreBodegaZona(String nombreBodegaZona) {
		this.nombreBodegaZona = nombreBodegaZona;
	}
    
}