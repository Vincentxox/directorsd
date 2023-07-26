package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.ruta.InputRutaPdv;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class AsociaRutaPdvCliente {

    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de asociaciones de rutas a
     * pdv
     **/
    public Respuesta creaAsociacionRutaPdv(InputRutaPdv objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(Respuesta.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para desasignar pdv's de rutas
     **/
    public Respuesta desasignaRutaPdv(InputRutaPdv objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(Respuesta.class, objeto);
    }
}
