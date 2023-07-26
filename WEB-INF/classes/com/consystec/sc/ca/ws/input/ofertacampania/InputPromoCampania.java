package com.consystec.sc.ca.ws.input.ofertacampania;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputPromoCampania")
public class InputPromoCampania {
    @XmlElement
    private String codArea;
    @XmlElement
    private String token;
    @XmlElement
    private String idOfertaCampania;
    @XmlElement
    private String nombreCampania;
    @XmlElement
    private List<InputPromoOfertaCampania> articulosPromocionales;
    
    public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getIdOfertaCampania() {
        return idOfertaCampania;
    }
    public void setIdOfertaCampania(String idOfertaCampania) {
        this.idOfertaCampania = idOfertaCampania;
    }
    public String getNombreCampania() {
        return nombreCampania;
    }
    public void setNombreCampania(String nombreCampania) {
        this.nombreCampania = nombreCampania;
    }
    public List<InputPromoOfertaCampania> getArticulosPromocionales() {
        return articulosPromocionales;
    }
    public void setArticulosPromocionales(List<InputPromoOfertaCampania> articulosPromocionales) {
        this.articulosPromocionales = articulosPromocionales;
    }
}