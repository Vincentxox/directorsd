package com.consystec.sc.ca.ws.input.dispositivo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputDispositivo")
public class InputDispositivo {
    @XmlElement
    private String codArea;
	@XmlElement
	private String usuario;
	@XmlElement
	private String idDispositivo;
	@XmlElement
	private String codigoDispositivo;
	@XmlElement
	private String modelo;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String numTelefono;
	@XmlElement
	private String responsable;
	@XmlElement
	private String tipoResponsable;
	@XmlElement
	private String nombreResponsable;
	@XmlElement
	private String vendedorAsignado;
	@XmlElement
    private String cajaNumero;
	@XmlElement
    private String zona;
	@XmlElement
    private String resolucion;
	@XmlElement
    private String fechaResolucion;
	@XmlElement
    private String idDistribuidor;
	@XmlElement
    private String codOficina;
    @XmlElement
    private String codVendedor;
	@XmlElement
	private String idPlaza;
	@XmlElement
	private String idPuntoVenta;
	@XmlElement
	private String userId;
	@XmlElement
	private String username;
	@XmlElement
	private String estado;
	@XmlElement
	private String creado_el;
	@XmlElement
	private String creado_por;
	@XmlElement
	private String modificado_por;
	@XmlElement
	private String modificado_el;
	@XmlElement
	private String developToken;
	@XmlElement
	private String userToken;
	
	public String getDevelopToken() {
		return developToken;
	}
	public void setDevelopToken(String developToken) {
		this.developToken = developToken;
	}
	
	public String getUserToken() {
		return userToken;
	}
	
	public void setUserToken(String userToken) {
		this.userToken = userToken;
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
    public String getIdDispositivo() {
        return idDispositivo;
    }
    public void setIdDispositivo(String idDispositivo) {
        this.idDispositivo = idDispositivo;
    }
    public String getCodigoDispositivo() {
        return codigoDispositivo;
    }
    public void setCodigoDispositivo(String codigoDispositivo) {
        this.codigoDispositivo = codigoDispositivo;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getNumTelefono() {
        return numTelefono;
    }
    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }
    public String getResponsable() {
        return responsable;
    }
    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
    public String getTipoResponsable() {
        return tipoResponsable;
    }
    public void setTipoResponsable(String tipoResponsable) {
        this.tipoResponsable = tipoResponsable;
    }
    public String getNombreResponsable() {
        return nombreResponsable;
    }
    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }
    public String getCajaNumero() {
        return cajaNumero;
    }
    public void setCajaNumero(String cajaNumero) {
        this.cajaNumero = cajaNumero;
    }
    public String getZona() {
        return zona;
    }
    public void setZona(String zona) {
        this.zona = zona;
    }
    public String getResolucion() {
        return resolucion;
    }
    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }
    public String getFechaResolucion() {
        return fechaResolucion;
    }
    public void setFechaResolucion(String fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }
    public String getIdDistribuidor() {
        return idDistribuidor;
    }
    public void setIdDistribuidor(String idDistribuidor) {
        this.idDistribuidor = idDistribuidor;
    }
    public String getCodOficina() {
        return codOficina;
    }
    public void setCodOficina(String codOficina) {
        this.codOficina = codOficina;
    }
    public String getCodVendedor() {
        return codVendedor;
    }
    public void setCodVendedor(String codVendedor) {
        this.codVendedor = codVendedor;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
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
    public String getModificado_por() {
        return modificado_por;
    }
    public void setModificado_por(String modificado_por) {
        this.modificado_por = modificado_por;
    }
    public String getModificado_el() {
        return modificado_el;
    }
    public void setModificado_el(String modificado_el) {
        this.modificado_el = modificado_el;
    }
	public String getVendedorAsignado() {
		return vendedorAsignado;
	}
	public void setVendedorAsignado(String vendedorAsignado) {
		this.vendedorAsignado = vendedorAsignado;
	}
	public String getIdPlaza() {
		return idPlaza;
	}
	public void setIdPlaza(String idPlaza) {
		this.idPlaza = idPlaza;
	}
	public String getIdPuntoVenta() {
		return idPuntoVenta;
	}
	public void setIdPuntoVenta(String idPuntoVenta) {
		this.idPuntoVenta = idPuntoVenta;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
		
    
}