package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.condicion.InputCondicionPrincipal;
import com.consystec.sc.ca.ws.output.condicion.OutputCondicion;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class CondicionCliente {

    String serverUrl;

    /**
     * M\u00E9todo para consumir ws para crear Condiciones
     */
    public OutputCondicion crearCondicion(InputCondicionPrincipal objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCondicion.class, objDatos);
    }

    /***
     * M\u00E9todo para consumir ws para modificar Condiciones
     */
    public OutputCondicion modificarCondicion(InputCondicionPrincipal objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCondicion.class, objeto);
    }

    /***
     * Metodo para obtener Condiciones registradas
     **/
    public OutputCondicion getCondicion(InputCondicionPrincipal objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCondicion.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
