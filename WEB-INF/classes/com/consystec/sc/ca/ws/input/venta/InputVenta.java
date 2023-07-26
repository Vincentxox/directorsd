package com.consystec.sc.ca.ws.input.venta;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputVenta")
public class InputVenta {
    @XmlElement
    private String token;
    @XmlElement
    private String codArea;
    @XmlElement
    private String idVenta;
    @XmlElement
    private String idVentaMovil;
    @XmlElement
    private String usuario;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String idVendedor;
    @XmlElement
    private String nombreVendedor;
    @XmlElement
    private String idJornada;
    @XmlElement
    private String idBodegaVendedor;
    @XmlElement
    private String bodegaVendedor;
    @XmlElement
    private String fecha;
    @XmlElement
    private String folioManual;
    @XmlElement
    private String idRangoFolio;
    @XmlElement
    private String folio;
    @XmlElement
    private String serie;
    @XmlElement
    private String folioSidra;
    @XmlElement
    private String serieSidra;
    @XmlElement
    private String tipoDocumento;
    @XmlElement
    private String idTipo;
    @XmlElement
    private String tipo;
    @XmlElement
    private String nombrePDV;
    @XmlElement
    private String departamento;
    @XmlElement
    private String municipio;
    @XmlElement
    private String nit;
    @XmlElement
    private String registroFiscal;
    @XmlElement
    private String giro;
    @XmlElement
    private String nombreFiscal;
    @XmlElement
    private String nombre;
    @XmlElement
    private String segundoNombre;
    @XmlElement
    private String apellido;
    @XmlElement
    private String segundoApellido;
    @XmlElement
    private String direccion;
    @XmlElement
    private String numTelefono;
    @XmlElement
    private String tipoDocCliente;
    @XmlElement
    private String numDocCliente;
    @XmlElement
    private String nombresFacturacion;
    @XmlElement
    private String apellidosFacturacion;
    @XmlElement
    private String zonaComercial;
    @XmlElement
    private String exento;
    @XmlElement
    private List<Impuesto> impuestosExento;
    @XmlElement
    private String impuesto;
    @XmlElement
    private String idOfertaCampania;
    @XmlElement
    private String descuentoMontoVenta;
    @XmlElement
    private String descuentoTotal;
    @XmlElement
    private String estado;
    @XmlElement
    private String observaciones;
    @XmlElement
    private String montoFactura;
    @XmlElement
    private String montoPagado;
    @XmlElement
    private String nombrePanelRuta;
    @XmlElement
    private String creadoPor;
    @XmlElement
    private String creadoEl;
    @XmlElement
    private String modificadoPor;
    @XmlElement
    private String modificadoEl;
    @XmlElement
    private String fechaInicio;
    @XmlElement
    private String fechaFin;
    @XmlElement
    private String envioAlarma;
    @XmlElement
    private String latitud;
    @XmlElement
    private String longitud;
    @XmlElement
    private String latitudPdv;
    @XmlElement
    private String longitudPdv;
    @XmlElement
    private String tasaCambio;
    @XmlElement
    private String modoOnline;
    @XmlElement
    private String enviarLinkFactura;
    @XmlElement
    private String telFactura;
	@XmlElement
    private List<DetallePago> detallePago;
    @XmlElement
    private List<ArticuloVenta> articulos;
    @XmlElement
    private List<ArticuloPromocionalVenta> articulosPromocionales;
    @XmlElement
    private String appVersion;
    @XmlElement
    private String androidVersion;
    
    public String getEnviarLinkFactura() {
		return enviarLinkFactura;
	}
	public void setEnviarLinkFactura(String enviarLinkFactura) {
		this.enviarLinkFactura = enviarLinkFactura;
	}
	public String getTelFactura() {
		return telFactura;
	}
	public void setTelFactura(String telFactura) {
		this.telFactura = telFactura;
	}
    
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
    public String getIdVenta() {
        return idVenta;
    }
    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getCodDispositivo() {
        return codDispositivo;
    }
    public void setCodDispositivo(String codDispositivo) {
        this.codDispositivo = codDispositivo;
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
    public String getIdJornada() {
        return idJornada;
    }
    public void setIdJornada(String idJornada) {
        this.idJornada = idJornada;
    }
    public String getIdBodegaVendedor() {
        return idBodegaVendedor;
    }
    public void setIdBodegaVendedor(String idBodegaVendedor) {
        this.idBodegaVendedor = idBodegaVendedor;
    }
    public String getBodegaVendedor() {
        return bodegaVendedor;
    }
    public void setBodegaVendedor(String bodegaVendedor) {
        this.bodegaVendedor = bodegaVendedor;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getFolioManual() {
        return folioManual;
    }
    public void setFolioManual(String folioManual) {
        this.folioManual = folioManual;
    }
    public String getIdRangoFolio() {
        return idRangoFolio;
    }
    public void setIdRangoFolio(String idRangoFolio) {
        this.idRangoFolio = idRangoFolio;
    }
    public String getFolio() {
        return folio;
    }
    public void setFolio(String folio) {
        this.folio = folio;
    }
    public String getSerie() {
        return serie;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }
    public String getFolioSidra() {
        return folioSidra;
    }
    public void setFolioSidra(String folioSidra) {
        this.folioSidra = folioSidra;
    }
    public String getSerieSidra() {
        return serieSidra;
    }
    public void setSerieSidra(String serieSidra) {
        this.serieSidra = serieSidra;
    }
    public String getTipoDocumento() {
        return tipoDocumento;
    }
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
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
    public String getNombrePDV() {
        return nombrePDV;
    }
    public void setNombrePDV(String nombrePDV) {
        this.nombrePDV = nombrePDV;
    }
    public String getDepartamento() {
        return departamento;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    public String getMunicipio() {
        return municipio;
    }
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
    public String getNit() {
        return nit;
    }
    public void setNit(String nit) {
        this.nit = nit;
    }
    public String getRegistroFiscal() {
        return registroFiscal;
    }
    public void setRegistroFiscal(String registroFiscal) {
        this.registroFiscal = registroFiscal;
    }
    public String getGiro() {
        return giro;
    }
    public void setGiro(String giro) {
        this.giro = giro;
    }
    public String getNombreFiscal() {
        return nombreFiscal;
    }
    public void setNombreFiscal(String nombreFiscal) {
        this.nombreFiscal = nombreFiscal;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getSegundoNombre() {
        return segundoNombre;
    }
    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getSegundoApellido() {
        return segundoApellido;
    }
    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getNumTelefono() {
        return numTelefono;
    }
    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }
    public String getTipoDocCliente() {
        return tipoDocCliente;
    }
    public void setTipoDocCliente(String tipoDocCliente) {
        this.tipoDocCliente = tipoDocCliente;
    }
    public String getNumDocCliente() {
        return numDocCliente;
    }
    public void setNumDocCliente(String numDocCliente) {
        this.numDocCliente = numDocCliente;
    }
    public String getNombresFacturacion() {
        return nombresFacturacion;
    }
    public void setNombresFacturacion(String nombresFacturacion) {
        this.nombresFacturacion = nombresFacturacion;
    }
    public String getApellidosFacturacion() {
        return apellidosFacturacion;
    }
    public void setApellidosFacturacion(String apellidosFacturacion) {
        this.apellidosFacturacion = apellidosFacturacion;
    }
    public String getZonaComercial() {
        return zonaComercial;
    }
    public void setZonaComercial(String zonaComercial) {
        this.zonaComercial = zonaComercial;
    }
    public String getExento() {
        return exento;
    }
    public void setExento(String exento) {
        this.exento = exento;
    }
    public List<Impuesto> getImpuestosExento() {
        return impuestosExento;
    }
    public void setImpuestosExento(List<Impuesto> impuestosExento) {
        this.impuestosExento = impuestosExento;
    }
    public String getImpuesto() {
        return impuesto;
    }
    public void setImpuesto(String impuesto) {
        this.impuesto = impuesto;
    }
    public String getIdOfertaCampania() {
        return idOfertaCampania;
    }
    public void setIdOfertaCampania(String idOfertaCampania) {
        this.idOfertaCampania = idOfertaCampania;
    }
    public String getDescuentoMontoVenta() {
        return descuentoMontoVenta;
    }
    public void setDescuentoMontoVenta(String descuentoMontoVenta) {
        this.descuentoMontoVenta = descuentoMontoVenta;
    }
    public String getDescuentoTotal() {
        return descuentoTotal;
    }
    public void setDescuentoTotal(String descuentoTotal) {
        this.descuentoTotal = descuentoTotal;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public String getMontoFactura() {
        return montoFactura;
    }
    public void setMontoFactura(String montoFactura) {
        this.montoFactura = montoFactura;
    }
    public String getMontoPagado() {
        return montoPagado;
    }
    public void setMontoPagado(String montoPagado) {
        this.montoPagado = montoPagado;
    }
    public String getNombrePanelRuta() {
        return nombrePanelRuta;
    }
    public void setNombrePanelRuta(String nombrePanelRuta) {
        this.nombrePanelRuta = nombrePanelRuta;
    }
    public String getCreadoPor() {
        return creadoPor;
    }
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }
    public String getCreadoEl() {
        return creadoEl;
    }
    public void setCreadoEl(String creadoEl) {
        this.creadoEl = creadoEl;
    }
    public String getModificadoPor() {
        return modificadoPor;
    }
    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }
    public String getModificadoEl() {
        return modificadoEl;
    }
    public void setModificadoEl(String modificadoEl) {
        this.modificadoEl = modificadoEl;
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
    public String getEnvioAlarma() {
        return envioAlarma;
    }
    public void setEnvioAlarma(String envioAlarma) {
        this.envioAlarma = envioAlarma;
    }
    public String getLatitud() {
        return latitud;
    }
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }
    public String getLongitud() {
        return longitud;
    }
    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
    public String getLatitudPdv() {
		return latitudPdv;
	}
	public void setLatitudPdv(String latitudPdv) {
		this.latitudPdv = latitudPdv;
	}
	public String getLongitudPdv() {
		return longitudPdv;
	}
	public void setLongitudPdv(String longitudPdv) {
		this.longitudPdv = longitudPdv;
	}
	public String getTasaCambio() {
        return tasaCambio;
    }
    public void setTasaCambio(String tasaCambio) {
        this.tasaCambio = tasaCambio;
    }
    public String getModoOnline() {
        return modoOnline;
    }
    public void setModoOnline(String modoOnline) {
        this.modoOnline = modoOnline;
    }
    public List<DetallePago> getDetallePago() {
        return detallePago;
    }
    public void setDetallePago(List<DetallePago> detallePago) {
        this.detallePago = detallePago;
    }
    public List<ArticuloVenta> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<ArticuloVenta> articulos) {
        this.articulos = articulos;
    }
    public List<ArticuloPromocionalVenta> getArticulosPromocionales() {
        return articulosPromocionales;
    }
    public void setArticulosPromocionales(List<ArticuloPromocionalVenta> articulosPromocionales) {
        this.articulosPromocionales = articulosPromocionales;
    }
	public String getIdVentaMovil() {
		return idVentaMovil;
	}
	public void setIdVentaMovil(String idVentaMovil) {
		this.idVentaMovil = idVentaMovil;
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