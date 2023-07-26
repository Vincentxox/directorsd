package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.general.InputConsultaWeb;
import com.consystec.sc.ca.ws.output.bogegas.OutputBodegasSCL;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class BodegasCliente {

    String serverUrl;

    public OutputBodegasSCL getBodegaSCL(InputConsultaWeb objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputBodegasSCL.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
