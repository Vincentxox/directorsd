package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.impuestos.InputConsultaImpuestos;
import com.consystec.sc.ca.ws.output.impuestos.OutputImpuestos;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ImpuestoCliente {

    String serverUrl;

    /**
     * M\u00E9todo cliente para consumir ws para obtener datos de impuestos
     **/
    public OutputImpuestos getPais(InputConsultaImpuestos objeto) {
        System.out.println("URL:" + this.serverUrl);
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputImpuestos.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
