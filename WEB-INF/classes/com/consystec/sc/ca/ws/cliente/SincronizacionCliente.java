package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.sincronizacion.InputSincronizacion;
import com.consystec.sc.ca.ws.output.sincronizacion.OutputSincronizacion;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class SincronizacionCliente {
    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de Sincronizacions
     **/
    public OutputSincronizacion creaSincronizacion(InputSincronizacion objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputSincronizacion.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener Sincronizacions
     **/
    public OutputSincronizacion getSincronizacion(InputSincronizacion objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputSincronizacion.class, objeto);
    }
}
