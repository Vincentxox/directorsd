package com.consystec.sc.ca.ws.input.traslado;

import com.consystec.sc.ca.ws.output.traslado.OutputTraslado;


public class RespuestaTraslado {
    private boolean resultado;
    private String descripcion;
    private OutputTraslado datos;
    
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
    public OutputTraslado getDatos() {
        return datos;
    }
    public void setDatos(OutputTraslado datos) {
        this.datos = datos;
    }
}