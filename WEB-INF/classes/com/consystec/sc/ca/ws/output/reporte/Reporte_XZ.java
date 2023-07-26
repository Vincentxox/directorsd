package com.consystec.sc.ca.ws.output.reporte;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ReporteXZ")
public class Reporte_XZ {
	@XmlElement
	private String idReporte;
	@XmlElement
	private String dispositivo;
	@XmlElement
	private String tipo;

	@XmlElement
	private String acumuladoVentas;
	@XmlElement
	private String fecha;
	@XmlElement
	private EncabezadoXZ encabezado; 
	@XmlElement
	private List<DesgloseVentas> ventas;
	@XmlElement
	private String totalVentasNetas;
	@XmlElement
	private List<DesgloseVentas> transaccionesMonetarias;
	@XmlElement
	private List<DesgloseVentas> otrasTransacciones;
	@XmlElement
	private List<FormasPago> formasPago;
	@XmlElement
	private List<TotalesVenta> totales;
	@XmlElement
	private String totalVenta;
	@XmlElement
	private EncabezadoXZ pie;
	
	public String getIdReporte() {
		return idReporte;
	}

	public void setIdReporte(String idReporte) {
		this.idReporte = idReporte;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	

	public String getAcumuladoVentas() {
		return acumuladoVentas;
	}

	public void setAcumuladoVentas(String acumuladoVentas) {
		this.acumuladoVentas = acumuladoVentas;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public List<DesgloseVentas> getVentas() {
		return ventas;
	}

	public void setVentas(List<DesgloseVentas> ventas) {
		this.ventas = ventas;
	}

	public String getTotalVentasNetas() {
		return totalVentasNetas;
	}

	public void setTotalVentasNetas(String totalVentasNetas) {
		this.totalVentasNetas = totalVentasNetas;
	}

	public List<DesgloseVentas> getTransaccionesMonetarias() {
		return transaccionesMonetarias;
	}

	public void setTransaccionesMonetarias(
			List<DesgloseVentas> transaccionesMonetarias) {
		this.transaccionesMonetarias = transaccionesMonetarias;
	}

	public List<DesgloseVentas> getOtrasTransacciones() {
		return otrasTransacciones;
	}

	public void setOtrasTransacciones(List<DesgloseVentas> otrasTransacciones) {
		this.otrasTransacciones = otrasTransacciones;
	}

	public List<FormasPago> getFormasPago() {
		return formasPago;
	}

	public void setFormasPago(List<FormasPago> formasPago) {
		this.formasPago = formasPago;
	}

	public List<TotalesVenta> getTotales() {
		return totales;
	}

	public void setTotales(List<TotalesVenta> totales) {
		this.totales = totales;
	}

	public String getTotalVenta() {
		return totalVenta;
	}

	public void setTotalVenta(String totalVenta) {
		this.totalVenta = totalVenta;
	}

	public String getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}

	public EncabezadoXZ getEncabezado() {
		return encabezado;
	}

	public void setEncabezado(EncabezadoXZ encabezado) {
		this.encabezado = encabezado;
	}

	public EncabezadoXZ getPie() {
		return pie;
	}

	public void setPie(EncabezadoXZ pie) {
		this.pie = pie;
	}
	
	
}
