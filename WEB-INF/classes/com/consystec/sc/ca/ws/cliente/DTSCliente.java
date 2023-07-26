package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.dts.InputDistribuidor;
import com.consystec.sc.ca.ws.output.dts.OutputDistribuidor;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class DTSCliente {
    String serverUrl;

    /**
     * Metodo cliente para consumir ws para creacion de puntos de venta
     **/
    public OutputDistribuidor crearDTS(InputDistribuidor objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputDistribuidor.class, objeto);
    }

    /**
     * Metodo cliente para consumir ws para modificar de puntos de venta
     **/
    public OutputDistribuidor modificarDTS(InputDistribuidor objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputDistribuidor.class, objeto);
    }

    /**
     * Metodo cliente para consumir ws para modificar de puntos de venta
     **/
    public OutputDistribuidor bajaDTS(InputDistribuidor objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputDistribuidor.class, objeto);
    }

    /**
     * Metodo cliente para consumir ws para dar de baja un pdv
     **/
    public OutputDistribuidor getDTS(InputDistribuidor objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputDistribuidor.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}