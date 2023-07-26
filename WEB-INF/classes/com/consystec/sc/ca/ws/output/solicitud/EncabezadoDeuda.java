package com.consystec.sc.ca.ws.output.solicitud;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "EncabezadoDeuda")
public class EncabezadoDeuda {
		@XmlElement
    	private String fecha;
		@XmlElement
	    private String idSolicitud;
	    @XmlElement
	    private String idBodega;
	    @XmlElement
	    private String nombreBodega;
	    @XmlElement
	    private String idDTS;
	    @XmlElement
	    private String nombreDTS;
	    @XmlElement
	    private String idBuzon;
	    @XmlElement
	    private String nombreBuzon;
	    @XmlElement
	    private String idBuzonAnterior;
	    @XmlElement
	    private String nombreBuzonAnterior;
	    @XmlElement
	    private String idBuzonSiguiente;	  
	    @XmlElement
	    private String tipoSolicitud;
	    @XmlElement
	    private String observaciones;
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
		public String getFecha() {
			return fecha;
		}
		public void setFecha(String fecha) {
			this.fecha = fecha;
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
		public String getNombreBuzonAnterior() {
			return nombreBuzonAnterior;
		}
		public void setNombreBuzonAnterior(String nombreBuzonAnterior) {
			this.nombreBuzonAnterior = nombreBuzonAnterior;
		}
		public String getIdBuzonSiguiente() {
			return idBuzonSiguiente;
		}
		public void setIdBuzonSiguiente(String idBuzonSiguiente) {
			this.idBuzonSiguiente = idBuzonSiguiente;
		}
		public String getTipoSolicitud() {
			return tipoSolicitud;
		}
		public void setTipoSolicitud(String tipoSolicitud) {
			this.tipoSolicitud = tipoSolicitud;
		}
		public String getObservaciones() {
			return observaciones;
		}
		public void setObservaciones(String observaciones) {
			this.observaciones = observaciones;
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
		public String getNombreBodega() {
			return nombreBodega;
		}
		public void setNombreBodega(String nombreBodega) {
			this.nombreBodega = nombreBodega;
		}
	    
	    
}
