package com.consystec.sc.ca.ws.input.fichacliente;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputFichaCliente")
public class InputFichaCliente {
    @XmlElement
    private String token;
    @XmlElement
    private String codArea;
    @XmlElement
    private String usuario;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String idFichaCliente;
    @XmlElement
    private String primerNombre;
    @XmlElement
    private String segundoNombre;
    @XmlElement
    private String primerApellido;
    @XmlElement
    private String segundoApellido;
    @XmlElement
    private String apellidoCasada;
    @XmlElement
    private String tipoDocumento;
    @XmlElement
    private String noDocumento;
    @XmlElement
    private String codCuenta;
    @XmlElement
    private String codCliente;
    @XmlElement
    private String numTelefono;
    @XmlElement
    private String idPdv;
    @XmlElement
    private String idDts;
    @XmlElement
    private String tipoCliente;
    @XmlElement
    private String email;
    @XmlElement
    private String telContacto;
    @XmlElement
    private String direccion;
    @XmlElement
    private String municipio;
    @XmlElement
    private String nit;
    @XmlElement
    private String ubicacion;
    @XmlElement
    private String departamento;
    
	public String getCodCliente() {
		return codCliente;
	}
	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelContacto() {
		return telContacto;
	}
	public void setTelContacto(String telContacto) {
		this.telContacto = telContacto;
	}
	public String getTipoCliente() {
		return tipoCliente;
	}
	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}
	public String getIdPdv() {
		return idPdv;
	}
	public void setIdPdv(String idPdv) {
		this.idPdv = idPdv;
	}
	public String getIdDts() {
		return idDts;
	}
	public void setIdDts(String idDts) {
		this.idDts = idDts;
	}
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
    public String getIdFichaCliente() {
        return idFichaCliente;
    }
    public void setIdFichaCliente(String idFichaCliente) {
        this.idFichaCliente = idFichaCliente;
    }
    public String getPrimerNombre() {
        return primerNombre;
    }
    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }
    public String getSegundoNombre() {
        return segundoNombre;
    }
    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }
    public String getPrimerApellido() {
        return primerApellido;
    }
    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }
    public String getSegundoApellido() {
        return segundoApellido;
    }
    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }
    public String getApellidoCasada() {
        return apellidoCasada;
    }
    public void setApellidoCasada(String apellidoCasada) {
        this.apellidoCasada = apellidoCasada;
    }
    public String getTipoDocumento() {
        return tipoDocumento;
    }
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    public String getNoDocumento() {
        return noDocumento;
    }
    public void setNoDocumento(String noDocumento) {
        this.noDocumento = noDocumento;
    }
    public String getCodCuenta() {
        return codCuenta;
    }
    public void setCodCuenta(String codCuenta) {
        this.codCuenta = codCuenta;
    }
    public String getNumTelefono() {
        return numTelefono;
    }
    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getNit() {
		return nit;
	}
	public void setNit(String nit) {
		this.nit = nit;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getCodDispositivo() {
		return codDispositivo;
	}
	public void setCodDispositivo(String codDispositivo) {
		this.codDispositivo = codDispositivo;
	}
	
}