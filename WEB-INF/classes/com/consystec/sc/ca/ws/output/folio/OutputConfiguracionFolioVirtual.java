package com.consystec.sc.ca.ws.output.folio;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.folio.InputFolioVirtual;
import com.consystec.sc.ca.ws.input.general.InputFolioRutaPanel;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputConfiguracionFolioVirtual")
public class OutputConfiguracionFolioVirtual {
    @XmlElement
    private Respuesta respuesta;
    @XmlElement
    private List<InputFolioVirtual> configuracionFolio;
    @XmlElement
    private List<InputFolioRutaPanel> folios;
    
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputFolioVirtual> getConfiguracionFolio() {
        return configuracionFolio;
    }
    public void setConfiguracionFolio(List<InputFolioVirtual> configuracionFolio) {
        this.configuracionFolio = configuracionFolio;
    }
	public List<InputFolioRutaPanel> getFolios() {
		return folios;
	}
	public void setFolios(List<InputFolioRutaPanel> folios) {
		this.folios = folios;
	}

}