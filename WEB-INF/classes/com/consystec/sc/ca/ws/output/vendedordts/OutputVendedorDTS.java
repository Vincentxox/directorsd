package com.consystec.sc.ca.ws.output.vendedordts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.vendedordts.InputVendedorDTS;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputVendedorDTS")
public class OutputVendedorDTS {
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputVendedorDTS> vendedores;
    @XmlElement
    private List<InputVendedorDTS> oficinas;
    
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputVendedorDTS> getVendedores() {
        return vendedores;
    }
    public void setVendedores(List<InputVendedorDTS> vendedores) {
        this.vendedores = vendedores;
    }
    public List<InputVendedorDTS> getOficinas() {
        return oficinas;
    }
    public void setOficinas(List<InputVendedorDTS> oficinas) {
        this.oficinas = oficinas;
    }
}