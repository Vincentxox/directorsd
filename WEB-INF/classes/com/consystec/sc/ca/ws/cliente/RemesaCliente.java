package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.remesa.InputRemesa;
import com.consystec.sc.ca.ws.output.remesa.OutputRemesa;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class RemesaCliente {
    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de Remesas
     **/
    public OutputRemesa creaRemesa(InputRemesa objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputRemesa.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificaci\u00F3n de Remesas
     **/
    public OutputRemesa modRemesa(InputRemesa objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputRemesa.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener Remesas
     **/
    public OutputRemesa getRemesa(InputRemesa objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputRemesa.class, objeto);
    }
}
