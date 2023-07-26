package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.buzon.InputBuzon;
import com.consystec.sc.ca.ws.output.buzon.OutputBuzon;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class BuzonCliente {
    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de buzones
     **/
    public OutputBuzon crearBuzon(InputBuzon objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputBuzon.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificar de puntos de venta
     **/
    public OutputBuzon modificarBuzon(InputBuzon objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputBuzon.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para dar de baja un pdv
     **/
    public OutputBuzon getBuzon(InputBuzon objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputBuzon.class, objeto);
    }
}
