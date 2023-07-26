package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.dispositivo.InputDispositivo;
import com.consystec.sc.ca.ws.output.dispositivo.OutputDispositivo;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class DispositivoCliente {

    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de dispositivos
     **/
    public OutputDispositivo crearDispositivo(InputDispositivo objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputDispositivo.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificar de dispositivos
     **/
    public OutputDispositivo modificarDispositivo(InputDispositivo objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputDispositivo.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener informaci\u00F3n de dispositivo
     **/
    public OutputDispositivo getDispositivo(InputDispositivo objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputDispositivo.class, objeto);
    }
}
