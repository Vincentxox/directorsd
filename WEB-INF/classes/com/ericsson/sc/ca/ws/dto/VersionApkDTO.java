package com.ericsson.sc.ca.ws.dto;

public class VersionApkDTO {
	private String version;
	private String nombre;
	private String ubicacion;
	private int actualizar;
	
	public int getActualizar() {
		return actualizar;
	}
	public void setActualizar(int actualizar) {
		this.actualizar = actualizar;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

}
