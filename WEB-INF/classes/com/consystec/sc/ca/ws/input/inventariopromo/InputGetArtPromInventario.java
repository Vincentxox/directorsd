package com.consystec.sc.ca.ws.input.inventariopromo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputGetArtPromInventario")
public class InputGetArtPromInventario {

	 @XmlElement
	    private String codArea;
	    @XmlElement
	    private String usuario;
	    @XmlElement
	    private String idBodegaVirtual;
	    @XmlElement
	    private String idArticulo;
	    @XmlElement
	    private String descripcion;
	    @XmlElement
	    private String estado;
	    @XmlElement
	    private String nombreBodegaVirtual;
	    @XmlElement
	    private String cantidadArticulo;

	    public String getCantidadArticulo() {
	        return cantidadArticulo;
	    }

	    public void setCantidadArticulo(String cantidadArticulo) {
	        this.cantidadArticulo = cantidadArticulo;
	    }

	    public String getidArticulo() {
	        return idArticulo;
	    }

	    public void setidArticulo(String idArticulo) {
	        this.idArticulo = idArticulo;
	    }

	    public String getNombreBodegaVirtual() {
	        return nombreBodegaVirtual;
	    }

	    public void setNombreBodegaVirtual(String nombreBodegaVirtual) {
	        this.nombreBodegaVirtual = nombreBodegaVirtual;
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

	    public String getIdBodegaVirtual() {
	        return idBodegaVirtual;
	    }

	    public void setIdBodegaVirtual(String idBodegaVirtual) {
	        this.idBodegaVirtual = idBodegaVirtual;
	    }

	    public String getDescripcion() {
	        return descripcion;
	    }

	    public void setDescripcion(String descripcion) {
	        this.descripcion = descripcion;
	    }

	    public String getEstado() {
	        return estado;
	    }

	    public void setEstado(String estado) {
	        this.estado = estado;
	    }
}
