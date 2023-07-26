package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.condicionoferta.InputCondicionPrincipalOferta;
import com.consystec.sc.ca.ws.output.condicionoferta.OutputCondicionOferta;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class CondicionOfertaCliente {
    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo para consumir ws para crear Condiciones
     */
    public OutputCondicionOferta crearCondicion(InputCondicionPrincipalOferta objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCondicionOferta.class, objeto);
    }

    /***
     * M\u00E9todo para consumir ws para modificar Condiciones
     */
    public OutputCondicionOferta modificarCondicion(InputCondicionPrincipalOferta objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCondicionOferta.class, objeto);
    }

    /***
     * Metodo para obtener Condiciones registradas
     **/
    public OutputCondicionOferta getCondicion(InputCondicionPrincipalOferta objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCondicionOferta.class, objeto);
    }
    
    /***
     * Metodo para obtener Condiciones masivas
     **/
    public OutputCondicionOferta getOfertasRuta(InputCondicionPrincipalOferta objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCondicionOferta.class, objeto);
    }
}
