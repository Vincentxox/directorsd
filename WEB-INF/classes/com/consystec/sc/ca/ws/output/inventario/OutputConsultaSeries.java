package com.consystec.sc.ca.ws.output.inventario;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.inventario.InputConsultaSeries;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputConsultaSeries")
public class OutputConsultaSeries {
    @XmlElement
    private String token;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private String idArticulo;
    @XmlElement
    private String descripcion;
    @XmlElement
    private String tipoInv;
    @XmlElement
    private String precioScl;
    @XmlElement
    private List<InputConsultaSeries> series;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public String getIdArticulo() {
        return idArticulo;
    }
    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getTipoInv() {
        return tipoInv;
    }
    public void setTipoInv(String tipoInv) {
        this.tipoInv = tipoInv;
    }
    public List<InputConsultaSeries> getSeries() {
        return series;
    }
    public void setSeries(List<InputConsultaSeries> series) {
        this.series = series;
    }
	public String getPrecioScl() {
		return precioScl;
	}
	public void setPrecioScl(String precioScl) {
		this.precioScl = precioScl;
	}
    
}