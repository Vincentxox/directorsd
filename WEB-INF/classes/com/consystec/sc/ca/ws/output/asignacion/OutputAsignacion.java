package com.consystec.sc.ca.ws.output.asignacion;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.asignacion.InputArticuloAsignacion;
import com.consystec.sc.ca.ws.input.asignacion.InputAsignacion;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputAsignacion")
public class OutputAsignacion {
	@XmlElement
	private String idAsignacion;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
    private String mensaje;
	@XmlElement
    private String descErrorSeries;
    @XmlElement
    private List<InputArticuloAsignacion> series;
    @XmlElement
    private String descErrorArticulos;
    @XmlElement
    private List<InputArticuloAsignacion> articulos;
    @XmlElement
    private String descErrorExistencias;
    @XmlElement
    private List<InputArticuloAsignacion> existencias;
    @XmlElement
    private List<InputAsignacion> asignacionReserva;
    
    public String getIdAsignacion() {
        return idAsignacion;
    }
    public void setIdAsignacion(String idAsignacion) {
        this.idAsignacion = idAsignacion;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public String getDescErrorSeries() {
        return descErrorSeries;
    }
    public void setDescErrorSeries(String descErrorSeries) {
        this.descErrorSeries = descErrorSeries;
    }
    public List<InputArticuloAsignacion> getSeries() {
        return series;
    }
    public void setSeries(List<InputArticuloAsignacion> series) {
        this.series = series;
    }
    public String getDescErrorArticulos() {
        return descErrorArticulos;
    }
    public void setDescErrorArticulos(String descErrorArticulos) {
        this.descErrorArticulos = descErrorArticulos;
    }
    public List<InputArticuloAsignacion> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<InputArticuloAsignacion> articulos) {
        this.articulos = articulos;
    }
    public String getDescErrorExistencias() {
        return descErrorExistencias;
    }
    public void setDescErrorExistencias(String descErrorExistencias) {
        this.descErrorExistencias = descErrorExistencias;
    }
    public List<InputArticuloAsignacion> getExistencias() {
        return existencias;
    }
    public void setExistencias(List<InputArticuloAsignacion> existencias) {
        this.existencias = existencias;
    }
    public List<InputAsignacion> getAsignacionReserva() {
        return asignacionReserva;
    }
    public void setAsignacionReserva(List<InputAsignacion> asignacionReserva) {
        this.asignacionReserva = asignacionReserva;
    }
}