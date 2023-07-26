package com.consystec.sc.ca.ws.output.deuda;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.remesa.InputRemesa;
import com.consystec.sc.ca.ws.input.venta.DetallePago;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputTransDeuda")
public class OutputTransDeuda {
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputRemesa> remesas;
    @XmlElement
    private List<DetallePago> transaccionesTarjeta;
    @XmlElement
    private List<DetallePago> transaccionesCheque;

    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputRemesa> getRemesas() {
        return remesas;
    }
    public void setRemesas(List<InputRemesa> remesas) {
        this.remesas = remesas;
    }
    public List<DetallePago> getTransaccionesTarjeta() {
        return transaccionesTarjeta;
    }
    public void setTransaccionesTarjeta(List<DetallePago> transaccionesTarjeta) {
        this.transaccionesTarjeta = transaccionesTarjeta;
    }
    public List<DetallePago> getTransaccionesCheque() {
        return transaccionesCheque;
    }
    public void setTransaccionesCheque(List<DetallePago> transaccionesCheque) {
        this.transaccionesCheque = transaccionesCheque;
    }
}