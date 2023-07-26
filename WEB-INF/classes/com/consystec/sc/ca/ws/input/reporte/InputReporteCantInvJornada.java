package com.consystec.sc.ca.ws.input.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InputReporteCantInvJornada")
public class InputReporteCantInvJornada {
    @XmlElement
    private String token;
    @XmlElement
    private String codDispositivo;
    @XmlElement
    private String codArea;
    @XmlElement
	private String usuario;
    @XmlElement
    private String idDTS;
    @XmlElement
    private String nombreDTS;
    @XmlElement
    private String idJornada;
	@XmlElement
	private String idVendedor;
	@XmlElement
    private String nombreVendedor;
	@XmlElement
    private String usuarioVendedor;
	@XmlElement
    private String idRutaPanel;
	@XmlElement
	private String tipoRutaPanel;
	@XmlElement
    private String nombreRutaPanel;
	@XmlElement
    private String idArticulo;
    @XmlElement
    private String descripcion;
    @XmlElement
    private String tipoGrupo;
    @XmlElement
    private String tipoInv;
    @XmlElement
    private String cantInicial;
    @XmlElement
    private String cantReservada;
    @XmlElement
    private String cantVendida;
    @XmlElement
    private String cantProcDevolucion;
    @XmlElement
    private String cantDevuelta;
    @XmlElement
    private String cantProcSiniestro;
    @XmlElement
    private String cantSiniestrada;
    @XmlElement
    private String cantFinal;
    @XmlElement
    private List<InputReporteCantInvJornada> articulos;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getCodDispositivo() {
        return codDispositivo;
    }
    public void setCodDispositivo(String codDispositivo) {
        this.codDispositivo = codDispositivo;
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
    public String getIdDTS() {
        return idDTS;
    }
    public void setIdDTS(String idDTS) {
        this.idDTS = idDTS;
    }
    public String getNombreDTS() {
        return nombreDTS;
    }
    public void setNombreDTS(String nombreDTS) {
        this.nombreDTS = nombreDTS;
    }
    public String getIdJornada() {
        return idJornada;
    }
    public void setIdJornada(String idJornada) {
        this.idJornada = idJornada;
    }
    public String getIdVendedor() {
        return idVendedor;
    }
    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }
    public String getNombreVendedor() {
        return nombreVendedor;
    }
    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }
    public String getUsuarioVendedor() {
        return usuarioVendedor;
    }
    public void setUsuarioVendedor(String usuarioVendedor) {
        this.usuarioVendedor = usuarioVendedor;
    }
    public String getIdRutaPanel() {
        return idRutaPanel;
    }
    public void setIdRutaPanel(String idRutaPanel) {
        this.idRutaPanel = idRutaPanel;
    }
    public String getTipoRutaPanel() {
        return tipoRutaPanel;
    }
    public void setTipoRutaPanel(String tipoRutaPanel) {
        this.tipoRutaPanel = tipoRutaPanel;
    }
    public String getNombreRutaPanel() {
        return nombreRutaPanel;
    }
    public void setNombreRutaPanel(String nombreRutaPanel) {
        this.nombreRutaPanel = nombreRutaPanel;
    }
    public String getIdArticulo() {
        return idArticulo;
    }
    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getTipoGrupo() {
        return tipoGrupo;
    }
    public void setTipoGrupo(String tipoGrupo) {
        this.tipoGrupo = tipoGrupo;
    }
    public String getTipoInv() {
        return tipoInv;
    }
    public void setTipoInv(String tipoInv) {
        this.tipoInv = tipoInv;
    }
    public String getCantInicial() {
        return cantInicial;
    }
    public void setCantInicial(String cantInicial) {
        this.cantInicial = cantInicial;
    }
    public String getCantReservada() {
        return cantReservada;
    }
    public void setCantReservada(String cantReservada) {
        this.cantReservada = cantReservada;
    }
    public String getCantVendida() {
        return cantVendida;
    }
    public void setCantVendida(String cantVendida) {
        this.cantVendida = cantVendida;
    }
    public String getCantProcDevolucion() {
        return cantProcDevolucion;
    }
    public void setCantProcDevolucion(String cantProcDevolucion) {
        this.cantProcDevolucion = cantProcDevolucion;
    }
    public String getCantDevuelta() {
        return cantDevuelta;
    }
    public void setCantDevuelta(String cantDevuelta) {
        this.cantDevuelta = cantDevuelta;
    }
    public String getCantProcSiniestro() {
        return cantProcSiniestro;
    }
    public void setCantProcSiniestro(String cantProcSiniestro) {
        this.cantProcSiniestro = cantProcSiniestro;
    }
    public String getCantSiniestrada() {
        return cantSiniestrada;
    }
    public void setCantSiniestrada(String cantSiniestrada) {
        this.cantSiniestrada = cantSiniestrada;
    }
    public String getCantFinal() {
        return cantFinal;
    }
    public void setCantFinal(String cantFinal) {
        this.cantFinal = cantFinal;
    }
    public List<InputReporteCantInvJornada> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<InputReporteCantInvJornada> articulos) {
        this.articulos = articulos;
    }
}