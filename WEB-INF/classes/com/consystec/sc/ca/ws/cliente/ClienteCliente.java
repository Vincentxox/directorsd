package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.cliente.InputCliente;
import com.consystec.sc.ca.ws.output.cliente.OutputCliente;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ClienteCliente {

    String serverUrl;

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de rutas
     **/
    public OutputCliente crearCliente(InputCliente objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCliente.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificaci\u00F3n de rutas
     **/
    public OutputCliente modificaCliente(InputCliente objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCliente.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para cambiar de estado las rutas
     **/
    public OutputCliente bajaCliente(InputCliente objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCliente.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener informaci\u00F3n de rutas
     **/
    public OutputCliente getCliente(InputCliente objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCliente.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
