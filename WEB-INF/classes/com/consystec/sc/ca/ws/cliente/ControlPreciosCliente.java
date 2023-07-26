package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.venta.InputControlPrecios;
import com.consystec.sc.ca.ws.output.venta.RespuestaControlPrecios;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ControlPreciosCliente {

    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener informaci\u00F3n de control de
     * precios
     **/
    public RespuestaControlPrecios getCountCPrecioss(InputControlPrecios objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(RespuestaControlPrecios.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener informaci\u00F3n de control de
     * precios
     **/
    public RespuestaControlPrecios getCPrecioss(InputControlPrecios objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(RespuestaControlPrecios.class, objeto);
    }
}
