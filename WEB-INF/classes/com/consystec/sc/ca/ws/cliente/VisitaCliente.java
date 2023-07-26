package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.visita.InputGetVisita;
import com.consystec.sc.ca.ws.input.visita.InputVisita;
import com.consystec.sc.ca.ws.output.visita.OutputVisita;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class VisitaCliente {
    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para registrar visitas de pdv
     **/
    public OutputVisita registraVisita(InputVisita objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputVisita.class, objeto);
    }

    /**
     * M\u00E9todo cliente para obtener visitas realizadas
     **/
    public OutputVisita getVisita(InputGetVisita objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputVisita.class, objeto);
    }
}
