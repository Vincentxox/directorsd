package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.solicitud.InputAceptaRechazaDevolucion;
import com.consystec.sc.ca.ws.input.solicitud.InputSolicitud;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.solicitud.OutputSolicitud;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class SolicitudCliente {
    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de solicitudes
     **/
    public OutputSolicitud crearSolicitud(InputSolicitud objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputSolicitud.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener solicitudes
     **/
    public OutputSolicitud getSolicitud(InputSolicitud objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputSolicitud.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificar solicitudes
     **/
    public OutputSolicitud modificaSolicitud(InputSolicitud objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputSolicitud.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificar solicitudes
     **/
    public Respuesta aceptaDevolucion(InputAceptaRechazaDevolucion objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(Respuesta.class, objeto);
    }
}
