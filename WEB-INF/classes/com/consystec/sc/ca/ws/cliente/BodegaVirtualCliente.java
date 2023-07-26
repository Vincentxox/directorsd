package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.bodegadts.InputBodegaVirtual;
import com.consystec.sc.ca.ws.output.bogegas.OutputBodegaVirtual;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class BodegaVirtualCliente {

    String serverUrl;

    /**
     * Metodo para consumir ws para crear bodegas virtuales
     */
    public OutputBodegaVirtual crearBodegaV(InputBodegaVirtual objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputBodegaVirtual.class, objeto);
    }

    /***
     * Metodo para consumir ws para modificar bodegas virtuales
     */
    public OutputBodegaVirtual modificarBodegaV(InputBodegaVirtual objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputBodegaVirtual.class, objeto);
    }

    /***
     * Metodo para consumir ws para dar de baja bodegas virtuales
     */
    public OutputBodegaVirtual bajaBodegaV(InputBodegaVirtual objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputBodegaVirtual.class, objeto);
    }

    /***
     * Metodo para obtener todas las bodegas virtuales
     **/
    public OutputBodegaVirtual getBodegaVirtual(InputBodegaVirtual objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputBodegaVirtual.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}