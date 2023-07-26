package com.consystec.sc.ca.ws.input.bodegadts;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputBodegaVirtual")
public class InputBodegaVirtual {
	@XmlElement
    private String codArea;
	@XmlElement
    private String idBodega;
    @XmlElement
    private String idDTS;
    @XmlElement
    private String idRuta;
    @XmlElement
    private String idPanel;
    @XmlElement
    private String nombre;
    @XmlElement
    private String idBodegaOrigen;
    @XmlElement
    private String idBodegaPadre;
    @XmlElement
    private String nivel;
    @XmlElement
    private String tipo;
    @XmlElement
    private String direccion;
    @XmlElement
    private String estado;
    @XmlElement
    private String usuario;
    @XmlElement
    private String latitud;
    @XmlElement
    private String longitud;
    @XmlElement
    private String creado_el;
    @XmlElement
    private String creado_por;
    @XmlElement
    private String modificado_el;
    @XmlElement
    private String modificado_por;

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
    public String getIdRuta() {
        return idRuta;
    }
    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }
    public String getIdPanel() {
        return idPanel;
    }
    public void setIdPanel(String idPanel) {
        this.idPanel = idPanel;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getIdBodegaOrigen() {
        return idBodegaOrigen;
    }
    public void setIdBodegaOrigen(String idBodegaOrigen) {
        this.idBodegaOrigen = idBodegaOrigen;
    }
    public String getIdBodegaPadre() {
        return idBodegaPadre;
    }
    public void setIdBodegaPadre(String idBodegaPadre) {
        this.idBodegaPadre = idBodegaPadre;
    }
    public String getNivel() {
        return nivel;
    }
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
    public String getTipo() {
 		return tipo;
 	}
 	public void setTipo(String tipo) {
 		this.tipo = tipo;
 	}
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getLatitud() {
        return latitud;
    }
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }
    public String getLongitud() {
        return longitud;
    }
    public void setLongitud(String longitud) {
        this.longitud = longitud;
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
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
    
}