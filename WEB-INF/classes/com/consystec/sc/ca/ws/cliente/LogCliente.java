package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.log.InputLogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class LogCliente {

    String serverUrl;

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de Logs
     **/
    public Respuesta crearLog(InputLogSidra objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(Respuesta.class, objDatos);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
