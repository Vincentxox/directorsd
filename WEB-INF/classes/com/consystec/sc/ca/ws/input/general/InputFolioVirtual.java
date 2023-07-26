 package com.consystec.sc.ca.ws.input.general;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputFolioVirtual")
public class InputFolioVirtual {
	@XmlElement
	private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String idDTS;
    @XmlElement
    private String idTipo;
    @XmlElement
    private String tipo;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String idRangoFolio;
    @XmlElement
    private String estado;
    @XmlElement
    private String codOficina;
    @XmlElement
    private String codVendedor;
    @XmlElement
    private String cantFolios;
    @XmlElement
    private String serie;
    @XmlElement
    private String tipoDocumento;
    @XmlElement
    private String idDispositivo;
    @XmlElement
    private String idReserva;
    @XmlElement
    private List<InputFolioRutaPanel> folios;

    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getIdDTS() {
        return idDTS;
    }
    public void setIdDTS(String idDTS) {
        this.idDTS = idDTS;
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
    public String getCodDispositivo() {
        return codDispositivo;
    }
    public void setCodDispositivo(String codDispositivo) {
        this.codDispositivo = codDispositivo;
    }
    public String getIdRangoFolio() {
        return idRangoFolio;
    }
    public void setIdRangoFolio(String idRangoFolio) {
        this.idRangoFolio = idRangoFolio;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
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
    public List<InputFolioRutaPanel> getFolios() {
        return folios;
    }
    public void setFolios(List<InputFolioRutaPanel> folios) {
        this.folios = folios;
    }
    public String getCodArea() {
        return codArea;
    }
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
	public String getCantFolios() {
		return cantFolios;
	}
	public void setCantFolios(String cantFolios) {
		this.cantFolios = cantFolios;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getIdDispositivo() {
		return idDispositivo;
	}
	public void setIdDispositivo(String idDispositivo) {
		this.idDispositivo = idDispositivo;
	}
	public String getIdReserva() {
		return idReserva;
	}
	public void setIdReserva(String idReserva) {
		this.idReserva = idReserva;
	}
	
}