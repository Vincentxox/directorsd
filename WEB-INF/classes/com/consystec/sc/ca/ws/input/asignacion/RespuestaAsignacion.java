package com.consystec.sc.ca.ws.input.asignacion;

import java.util.List;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.output.asignacion.OutputAsignacion;

public class RespuestaAsignacion {
    private boolean resultado;
    private String descripcion;
    private OutputAsignacion datos;
    private List<LogSidra> listaLog;
    
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
    public OutputAsignacion getDatos() {
        return datos;
    }
    public void setDatos(OutputAsignacion datos) {
        this.datos = datos;
    }
    public List<LogSidra> getListaLog() {
        return listaLog;
    }
    public void setListaLog(List<LogSidra> listaLog) {
        this.listaLog = listaLog;
    }
}