package com.consystec.sc.ca.ws.output.file;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.file.InputCargaFile;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputImagen")

public class OutputImagen {
    @XmlElement
    private String token;
    @XmlElement
    private String idImgPDV;
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputCargaFile> imagen;
    @XmlElement
    private List<InputCargaFile> imgAsociadas;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getIdImgPDV() {
        return idImgPDV;
    }
    public void setIdImgPDV(String idImgPDV) {
        this.idImgPDV = idImgPDV;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputCargaFile> getImagen() {
        return imagen;
    }
    public void setImagen(List<InputCargaFile> imagen) {
        this.imagen = imagen;
    }
    public List<InputCargaFile> getImgAsociadas() {
        return imgAsociadas;
    }
    public void setImgAsociadas(List<InputCargaFile> imgAsociadas) {
        this.imgAsociadas = imgAsociadas;
    }
}