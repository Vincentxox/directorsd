package com.consystec.sc.ca.ws.output.solicitud;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.solicitud.InputArticuloSolicitud;
import com.consystec.sc.ca.ws.input.solicitud.InputTipoSolicitud;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputSolicitud")
public class OutputSolicitud {
    @XmlElement
    private String token;
	@XmlElement
	private String idSolicitud;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
    private String tipoSolicitud;
	@XmlElement
    private String mensaje;
	@XmlElement
    private String descErrorSeries;
    @XmlElement
    private List<InputArticuloSolicitud> series;
	@XmlElement
    private String descErrorArticulos;
	@XmlElement
    private List<InputArticuloSolicitud> articulos;
	@XmlElement
	private String descErrorExistencias;
	@XmlElement
    private List<InputArticuloSolicitud> existencias;
	@XmlElement
    private List<InputTipoSolicitud> solicitudes;
	
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getIdSolicitud() {
        return idSolicitud;
    }
    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public String getTipoSolicitud() {
        return tipoSolicitud;
    }
    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
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
    public List<InputArticuloSolicitud> getSeries() {
        return series;
    }
    public void setSeries(List<InputArticuloSolicitud> series) {
        this.series = series;
    }
    public String getDescErrorArticulos() {
        return descErrorArticulos;
    }
    public void setDescErrorArticulos(String descErrorArticulos) {
        this.descErrorArticulos = descErrorArticulos;
    }
    public List<InputArticuloSolicitud> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<InputArticuloSolicitud> articulos) {
        this.articulos = articulos;
    }
    public String getDescErrorExistencias() {
        return descErrorExistencias;
    }
    public void setDescErrorExistencias(String descErrorExistencias) {
        this.descErrorExistencias = descErrorExistencias;
    }
    public List<InputArticuloSolicitud> getExistencias() {
        return existencias;
    }
    public void setExistencias(List<InputArticuloSolicitud> existencias) {
        this.existencias = existencias;
    }
    public List<InputTipoSolicitud> getSolicitudes() {
        return solicitudes;
    }
    public void setSolicitudes(List<InputTipoSolicitud> solicitudes) {
        this.solicitudes = solicitudes;
    }
}