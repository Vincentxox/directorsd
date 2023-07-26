package com.ericsson.sc.ca.ws.output.ventacredito;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ericsson.sc.ca.ws.input.ventacredito.AbonoVentaCredito;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OutputAbonoVentaCredito")
public class OutputAbonoVentaCredito {
	
@XmlElement
private AbonoVentaCredito abono;

public AbonoVentaCredito getAbono() {
	return abono;
}

public void setAbono(AbonoVentaCredito abono) {
	this.abono = abono;
}
	

}
