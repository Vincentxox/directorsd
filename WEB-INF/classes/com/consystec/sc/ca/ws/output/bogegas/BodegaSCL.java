package com.consystec.sc.ca.ws.output.bogegas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="BodegaSCL")
public class BodegaSCL {

	
	 @XmlElement
    private String idBodega;
	 @XmlElement
    private String codBodega;
	 @XmlElement
    private String nombreBodega;

    public String getNombreBodega ()
    {
        return nombreBodega;
    }

    public void setNombreBodega (String nombreBodega)
    {
        this.nombreBodega = nombreBodega;
    }

    public String getIdBodega ()
    {
        return idBodega;
    }

    public void setIdBodega (String idBodega)
    {
        this.idBodega = idBodega;
    }

    public String getCodBodega ()
    {
        return codBodega;
    }

    public void setCodBodega (String codBodega)
    {
        this.codBodega = codBodega;
    }

}
