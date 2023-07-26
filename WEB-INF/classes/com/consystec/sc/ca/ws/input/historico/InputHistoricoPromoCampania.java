package com.consystec.sc.ca.ws.input.historico;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputHistoricoPromoCampania")
public class InputHistoricoPromoCampania {
    @XmlElement
    private String codArea;
    @XmlElement
    private String idTipo;
    @XmlElement
    private String idCampania;
    @XmlElement
    private String nombreCampania;
    @XmlElement
    private String cantPromocionales;
    @XmlElement
    private List<InputHistoricoPromoArt> articulos;
    
    public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getIdTipo() {
        return idTipo;
    }
    public void setIdTipo(String idTipo) {
        this.idTipo = idTipo;
    }
    public String getIdCampania() {
        return idCampania;
    }
    public void setIdCampania(String idCampania) {
        this.idCampania = idCampania;
    }
    public String getNombreCampania() {
        return nombreCampania;
    }
    public void setNombreCampania(String nombreCampania) {
        this.nombreCampania = nombreCampania;
    }
    public String getCantPromocionales() {
        return cantPromocionales;
    }
    public void setCantPromocionales(String cantPromocionales) {
        this.cantPromocionales = cantPromocionales;
    }
    public List<InputHistoricoPromoArt> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<InputHistoricoPromoArt> articulos) {
        this.articulos = articulos;
    }
}