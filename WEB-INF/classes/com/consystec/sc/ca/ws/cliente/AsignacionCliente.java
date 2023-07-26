package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.asignacion.InputAsignacion;
import com.consystec.sc.ca.ws.output.asignacion.OutputAsignacion;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class AsignacionCliente {
    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de asignaciones o reservas
     **/
    public OutputAsignacion creaAsignacion(InputAsignacion objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputAsignacion.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener asignaciones o reservas
     **/
    public OutputAsignacion getAsignacion(InputAsignacion objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputAsignacion.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificaci\u00F3n o reservas
     **/
    public OutputAsignacion modAsignacion(InputAsignacion objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputAsignacion.class, objDatos);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificaci\u00F3n de detalle de
     * asignaciones o reservas
     **/
    public OutputAsignacion modDetAsignacion(InputAsignacion objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputAsignacion.class, objDatos);
    }
}
