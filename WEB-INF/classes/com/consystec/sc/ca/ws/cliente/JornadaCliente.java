package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.jornada.InputJornada;
import com.consystec.sc.ca.ws.output.jornada.OutputJornada;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class JornadaCliente {
    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de jornadas
     **/
    public OutputJornada creaJornada(InputJornada objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputJornada.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificaci\u00F3n de jornadas
     **/
    public OutputJornada modJornada(InputJornada objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputJornada.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener jornadas
     **/
    public OutputJornada getJornada(InputJornada objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputJornada.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de jornadas masivas
     **/
    public OutputJornada creaJornadaMasiva(InputJornada objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputJornada.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener jornadas masivas
     **/
    public OutputJornada getJornadaMasiva(InputJornada objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputJornada.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificaci\u00F3n de jornadas
     **/
    public OutputJornada modJornadaMasiva(InputJornada objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputJornada.class, objeto);
    }


    /**
     * M\u00E9todo cliente para asignar fecha de cierre
     **/
    public OutputJornada asignaFechaCierre(InputJornada objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputJornada.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consultar fecha de cliente
     **/
    public OutputJornada getFechaCierre(InputJornada objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputJornada.class, objeto);
    }
}
