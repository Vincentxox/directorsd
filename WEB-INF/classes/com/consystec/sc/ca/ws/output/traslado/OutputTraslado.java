package com.consystec.sc.ca.ws.output.traslado;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.traslado.InputArticuloTraslado;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputTraslado")
public class OutputTraslado {
	@XmlElement
	private String idTraslado;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
    private String mensaje;
	@XmlElement
    private String descErrorSeries;
    @XmlElement
    private List<InputArticuloTraslado> series;
    @XmlElement
    private String descErrorArticulos;
    @XmlElement
    private List<InputArticuloTraslado> articulos;
    @XmlElement
    private String descErrorExistencias;
    @XmlElement
    private List<InputArticuloTraslado> existencias;
    
    public String getIdTraslado() {
        return idTraslado;
    }
    public void setIdTraslado(String idTraslado) {
        this.idTraslado = idTraslado;
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
    public List<InputArticuloTraslado> getSeries() {
        return series;
    }
    public void setSeries(List<InputArticuloTraslado> series) {
        this.series = series;
    }
    public String getDescErrorArticulos() {
        return descErrorArticulos;
    }
    public void setDescErrorArticulos(String descErrorArticulos) {
        this.descErrorArticulos = descErrorArticulos;
    }
    public List<InputArticuloTraslado> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<InputArticuloTraslado> articulos) {
        this.articulos = articulos;
    }
    public String getDescErrorExistencias() {
        return descErrorExistencias;
    }
    public void setDescErrorExistencias(String descErrorExistencias) {
        this.descErrorExistencias = descErrorExistencias;
    }
    public List<InputArticuloTraslado> getExistencias() {
        return existencias;
    }
    public void setExistencias(List<InputArticuloTraslado> existencias) {
        this.existencias = existencias;
    }
}