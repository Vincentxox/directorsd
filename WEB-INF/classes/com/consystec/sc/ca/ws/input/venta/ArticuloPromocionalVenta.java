package com.consystec.sc.ca.ws.input.venta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ArticuloPromocionalVenta")
public class ArticuloPromocionalVenta {
	@XmlElement
	private String idOfertaCampania;
	@XmlElement
	private String articuloPromocional;
	@XmlElement
	private String cantidad;
	@XmlElement
	private String tipoInv;
	@XmlElement
    private String tipoGrupoSidra;
	
	public String getArticuloPromocional() {
		return articuloPromocional;
	}
	public void setArticuloPromocional(String articuloPromocional) {
		this.articuloPromocional = articuloPromocional;
	}
	public String getCantidad() {
		return cantidad;
	}
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	public String getIdOfertaCampania() {
		return idOfertaCampania;
	}
	public void setIdOfertaCampania(String idOfertaCampania) {
		this.idOfertaCampania = idOfertaCampania;
	}
	public String getTipoInv() {
		return tipoInv;
	}
	public void setTipoInv(String tipoInv) {
		this.tipoInv = tipoInv;
	}
    public String getTipoGrupoSidra() {
        return tipoGrupoSidra;
    }
    public void setTipoGrupoSidra(String tipoGrupoSidra) {
        this.tipoGrupoSidra = tipoGrupoSidra;
    }
}