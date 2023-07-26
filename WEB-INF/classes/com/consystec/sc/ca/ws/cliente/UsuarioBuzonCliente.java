package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.buzon.InputUsuarioBuzon;
import com.consystec.sc.ca.ws.output.buzon.OutputUsuarioBuzon;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class UsuarioBuzonCliente {
    String serverUrl;

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n
     **/
    public OutputUsuarioBuzon crearUsuarioBuzon(InputUsuarioBuzon objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputUsuarioBuzon.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificar
     **/
    public OutputUsuarioBuzon modificarUsuarioBuzon(InputUsuarioBuzon objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputUsuarioBuzon.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para dar de baja
     **/
    public OutputUsuarioBuzon bajaUsuarioBuzon(InputUsuarioBuzon objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputUsuarioBuzon.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para consultar
     **/
    public OutputUsuarioBuzon getUsuarioBuzon(InputUsuarioBuzon objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputUsuarioBuzon.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
