package com.consystec.sc.ca.ws.input.inventariomovil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputConsultaInventarioMovil")
public class InputConsultaInventarioMovil {
    @XmlElement
    private String token;
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String idBodega;
    @XmlElement
    private String idArticulo;
    @XmlElement
    private String serie;
    @XmlElement
    private String tipoInv;
    @XmlElement
    private String tipoGrupo;
    @XmlElement
    private String seriado;
    @XmlElement
    private String idVendedor;
    @XmlElement
    private String idTipo;
    @XmlElement
    private String tipo;
    @XmlElement
    private String version;
    @XmlElement
    private String tecnologia;
    @XmlElement
    private String estado;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
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
    public String getIdBodega() {
        return idBodega;
    }
    public void setIdBodega(String idBodega) {
        this.idBodega = idBodega;
    }
    public String getIdArticulo() {
        return idArticulo;
    }
    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }
    public String getSerie() {
        return serie;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }
    public String getTipoInv() {
        return tipoInv;
    }
    public void setTipoInv(String tipoInv) {
        this.tipoInv = tipoInv;
    }
    public String getTipoGrupo() {
        return tipoGrupo;
    }
    public void setTipoGrupo(String tipoGrupo) {
        this.tipoGrupo = tipoGrupo;
    }
    public String getSeriado() {
        return seriado;
    }
    public void setSeriado(String seriado) {
        this.seriado = seriado;
    }
    public String getIdVendedor() {
        return idVendedor;
    }
    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }
    public String getIdTipo() {
        return idTipo;
    }
    public void setIdTipo(String idTipo) {
        this.idTipo = idTipo;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
	public String getCodDispositivo() {
		return codDispositivo;
	}
	public void setCodDispositivo(String codDispositivo) {
		this.codDispositivo = codDispositivo;
	}
    public String getTecnologia() {
        return tecnologia;
    }
    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }
}