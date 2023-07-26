package com.consystec.sc.ca.ws.input.pdv;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.file.InputCargaFile;
import com.consystec.sc.ca.ws.input.ruta.InputVendedor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputPDV")
public class InputPDV {
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String origen;
    @XmlElement
    private String idRuta;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String idPDV;
    @XmlElement
    private String tipoProducto;
    @XmlElement
    private String nombrePDV;
    @XmlElement
    private String canal;
    @XmlElement
    private String subcanal;
    @XmlElement
    private String categoria;
    @XmlElement
    private String distribuidorAsociado;
    @XmlElement
    private String nombreDTS;
    @XmlElement
    private String tipoDTS;
    @XmlElement
    private String tipoNegocio;
    @XmlElement
    private String documento;
    @XmlElement
    private String nit;
    @XmlElement
    private String nombreFiscal;
    @XmlElement
    private String registroFiscal;
    @XmlElement
    private String giroNegocio;
    @XmlElement
    private String tipoContribuyente;
    @XmlElement
    private String calle;
    @XmlElement
    private String avenida;
    @XmlElement
    private String pasaje;
    @XmlElement
    private String casa;
    @XmlElement
    private String colonia;
    @XmlElement
    private String referencia;
    @XmlElement
    private String barrio;
    @XmlElement
    private String direccion;
    @XmlElement
    private String zonaComercial;
    @XmlElement
    private String departamento;
    @XmlElement
    private String municipio;
    @XmlElement
    private String distrito;
    @XmlElement
    private String observaciones;
    @XmlElement
    private String digitoValidador;
    @XmlElement
    private String[] dias;
    @XmlElement
    private TelefonoRecargo[] telefonoRecargo;
    @XmlElement
    private String latitud;
    @XmlElement
    private String longitud;
    @XmlElement
    private String codCliente;
    @XmlElement
    private String resultadoSCL;
    @XmlElement
    private String[] vendedor;
    @XmlElement
    private EncargadoPDV encargado;
    @XmlElement
    private List<InputCargaFile> imgAsociadas;
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
    private List<InputVendedor> datosVendedor;
    @XmlElement
    private List<TelefonoRecargo> numerosRecarga;
    @XmlElement
    private List<InputDiaVisita> diasVisita;
    @XmlElement
    private String qr;
    @XmlElement
    private String numRecargaOrden;

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
    public String getOrigen() {
        return origen;
    }
    public void setOrigen(String origen) {
        this.origen = origen;
    }
    public String getIdRuta() {
        return idRuta;
    }
    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }
    public String getCodDispositivo() {
        return codDispositivo;
    }
    public void setCodDispositivo(String codDispositivo) {
        this.codDispositivo = codDispositivo;
    }
    public String getIdPDV() {
        return idPDV;
    }
    public void setIdPDV(String idPDV) {
        this.idPDV = idPDV;
    }
    public String getTipoProducto() {
        return tipoProducto;
    }
    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }
    public String getNombrePDV() {
        return nombrePDV;
    }
    public void setNombrePDV(String nombrePDV) {
        this.nombrePDV = nombrePDV;
    }
    public String getCanal() {
        return canal;
    }
    public void setCanal(String canal) {
        this.canal = canal;
    }
    public String getSubcanal() {
        return subcanal;
    }
    public void setSubcanal(String subcanal) {
        this.subcanal = subcanal;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getDistribuidorAsociado() {
        return distribuidorAsociado;
    }
    public void setDistribuidorAsociado(String distribuidorAsociado) {
        this.distribuidorAsociado = distribuidorAsociado;
    }
    public String getNombreDTS() {
        return nombreDTS;
    }
    public void setNombreDTS(String nombreDTS) {
        this.nombreDTS = nombreDTS;
    }
    public String getTipoDTS() {
        return tipoDTS;
    }
    public void setTipoDTS(String tipoDTS) {
        this.tipoDTS = tipoDTS;
    }
    public String getTipoNegocio() {
        return tipoNegocio;
    }
    public void setTipoNegocio(String tipoNegocio) {
        this.tipoNegocio = tipoNegocio;
    }
    public String getDocumento() {
        return documento;
    }
    public void setDocumento(String documento) {
        this.documento = documento;
    }
    public String getNit() {
        return nit;
    }
    public void setNit(String nit) {
        this.nit = nit;
    }
    public String getNombreFiscal() {
        return nombreFiscal;
    }
    public void setNombreFiscal(String nombreFiscal) {
        this.nombreFiscal = nombreFiscal;
    }
    public String getRegistroFiscal() {
        return registroFiscal;
    }
    public void setRegistroFiscal(String registroFiscal) {
        this.registroFiscal = registroFiscal;
    }
    public String getGiroNegocio() {
        return giroNegocio;
    }
    public void setGiroNegocio(String giroNegocio) {
        this.giroNegocio = giroNegocio;
    }
    public String getTipoContribuyente() {
        return tipoContribuyente;
    }
    public void setTipoContribuyente(String tipoContribuyente) {
        this.tipoContribuyente = tipoContribuyente;
    }
    public String getCalle() {
        return calle;
    }
    public void setCalle(String calle) {
        this.calle = calle;
    }
    public String getAvenida() {
        return avenida;
    }
    public void setAvenida(String avenida) {
        this.avenida = avenida;
    }
    public String getPasaje() {
        return pasaje;
    }
    public void setPasaje(String pasaje) {
        this.pasaje = pasaje;
    }
    public String getCasa() {
        return casa;
    }
    public void setCasa(String casa) {
        this.casa = casa;
    }
    public String getColonia() {
        return colonia;
    }
    public void setColonia(String colonia) {
        this.colonia = colonia;
    }
    public String getReferencia() {
        return referencia;
    }
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
    public String getBarrio() {
        return barrio;
    }
    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getZonaComercial() {
        return zonaComercial;
    }
    public void setZonaComercial(String zonaComercial) {
        this.zonaComercial = zonaComercial;
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
    public String getDistrito() {
        return distrito;
    }
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public String getDigitoValidador() {
        return digitoValidador;
    }
    public void setDigitoValidador(String digitoValidador) {
        this.digitoValidador = digitoValidador;
    }
    public String[] getDias() {
        return dias;
    }
    public void setDias(String[] dias) {
        this.dias = dias;
    }
    public TelefonoRecargo[] getTelefonoRecargo() {
        return telefonoRecargo;
    }
    public void setTelefonoRecargo(TelefonoRecargo[] telefonoRecargo) {
        this.telefonoRecargo = telefonoRecargo;
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
    public String getCodCliente() {
        return codCliente;
    }
    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }
    public String getResultadoSCL() {
        return resultadoSCL;
    }
    public void setResultadoSCL(String resultadoSCL) {
        this.resultadoSCL = resultadoSCL;
    }
    public String[] getVendedor() {
        return vendedor;
    }
    public void setVendedor(String[] vendedor) {
        this.vendedor = vendedor;
    }
    public EncargadoPDV getEncargado() {
        return encargado;
    }
    public void setEncargado(EncargadoPDV encargado) {
        this.encargado = encargado;
    }
    public List<InputCargaFile> getImgAsociadas() {
        return imgAsociadas;
    }
    public void setImgAsociadas(List<InputCargaFile> imgAsociadas) {
        this.imgAsociadas = imgAsociadas;
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
    public List<InputVendedor> getDatosVendedor() {
        return datosVendedor;
    }
    public void setDatosVendedor(List<InputVendedor> datosVendedor) {
        this.datosVendedor = datosVendedor;
    }
    public List<TelefonoRecargo> getNumerosRecarga() {
        return numerosRecarga;
    }
    public void setNumerosRecarga(List<TelefonoRecargo> numerosRecarga) {
        this.numerosRecarga = numerosRecarga;
    }
    public List<InputDiaVisita> getDiasVisita() {
        return diasVisita;
    }
    public void setDiasVisita(List<InputDiaVisita> diasVisita) {
        this.diasVisita = diasVisita;
    }
    public String getQr() {
        return qr;
    }
    public void setQr(String qr) {
        this.qr = qr;
    }
	public String getNumRecargaOrden() {
		return numRecargaOrden;
	}
	public void setNumRecargaOrden(String numRecargaOrden) {
		this.numRecargaOrden = numRecargaOrden;
	}
    
}