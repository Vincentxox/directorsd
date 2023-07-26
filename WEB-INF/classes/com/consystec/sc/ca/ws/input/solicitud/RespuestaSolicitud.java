package com.consystec.sc.ca.ws.input.solicitud;

import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.solicitud.OutputSolicitud;

public class RespuestaSolicitud {
    private boolean resultado;
    private boolean resultadoDTS;
    private String descripcion;
    private Respuesta respuesta;
    private OutputSolicitud datos;
    
    public boolean isResultado() {
        return resultado;
    }
    public void setResultado(boolean resultado) {
        this.resultado = resultado;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public OutputSolicitud getDatos() {
        return datos;
    }
    public void setDatos(OutputSolicitud datos) {
        this.datos = datos;
    }
	public boolean isResultadoDTS() {
		return resultadoDTS;
	}
	public void setResultadoDTS(boolean resultadoDTS) {
		this.resultadoDTS = resultadoDTS;
	}
    
}