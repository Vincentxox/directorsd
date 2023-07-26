package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.traslado.InputTraslado;
import com.consystec.sc.ca.ws.output.traslado.OutputTraslado;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class TrasladoCliente {
    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de buzones
     **/
    public OutputTraslado creaTraslado(InputTraslado objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputTraslado.class, objeto);
    }
}
