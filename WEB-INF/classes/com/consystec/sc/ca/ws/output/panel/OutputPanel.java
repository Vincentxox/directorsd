package com.consystec.sc.ca.ws.output.panel;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.consystec.sc.ca.ws.input.panel.InputPanel;
import com.consystec.sc.ca.ws.orm.Respuesta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputPanel")
public class OutputPanel {
    @XmlElement
    private String  token;
	@XmlElement
	private String  idPanel;
	@XmlElement
	private Respuesta respuesta;
	@XmlElement
    private List<InputPanel> panel;
	
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getIdPanel() {
        return idPanel;
    }
    public void setIdPanel(String idPanel) {
        this.idPanel = idPanel;
    }
    public Respuesta getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
    public List<InputPanel> getPanel() {
        return panel;
    }
    public void setPanel(List<InputPanel> panel) {
        this.panel = panel;
    }
}
