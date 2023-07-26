package com.consystec.sc.ca.ws.output.inventariopromo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.inventariopromo.InputGetArtPromInventario;
import com.consystec.sc.ca.ws.orm.Respuesta;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputArtPromInventario")
public class OutputArtPromInventario {
	 @XmlElement
	    private Respuesta respuesta;
	    @XmlElement
	    private String idBodegaVirtual;
	    @XmlElement
	    private String nombreBodegaVirtual;
	    @XmlElement
	    private List<InputGetArtPromInventario> ArticulosPromo;

	    public Respuesta getRespuesta() {
	        return respuesta;
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

	    public void setRespuesta(Respuesta respuesta) {
	        this.respuesta = respuesta;
	    }

	    public List<InputGetArtPromInventario> getArticulosPromo() {
	        return ArticulosPromo;
	    }

	    public void setArticulosPromo(List<InputGetArtPromInventario> articulosPromo) {
	        ArticulosPromo = articulosPromo;
	    }
}
