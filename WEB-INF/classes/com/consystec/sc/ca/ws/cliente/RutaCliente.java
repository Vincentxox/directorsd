package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.ruta.InputRuta;
import com.consystec.sc.ca.ws.output.ruta.OutputRuta;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class RutaCliente {

    String serverUrl;

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de rutas
     **/
    public OutputRuta crearRuta(InputRuta objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputRuta.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificaci\u00F3n de rutas
     **/
    public OutputRuta modificaRuta(InputRuta objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputRuta.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para cambiar de estado las rutas
     **/
    public OutputRuta bajaRuta(InputRuta objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputRuta.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener informaci\u00F3n de rutas
     **/
    public OutputRuta getRuta(InputRuta objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputRuta.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
