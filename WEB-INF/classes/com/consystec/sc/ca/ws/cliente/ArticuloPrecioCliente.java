package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.articuloprecio.InputArticuloPrecio;
import com.consystec.sc.ca.ws.output.articuloprecio.OutputArticuloPrecio;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ArticuloPrecioCliente {
	String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para asociacion de precio-articulo
     **/
    public OutputArticuloPrecio crearArticuloPrecio(InputArticuloPrecio objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputArticuloPrecio.class, objeto);
    }
    
    public OutputArticuloPrecio modArticuloPrecio(InputArticuloPrecio objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputArticuloPrecio.class, objeto);
    }
    
    public OutputArticuloPrecio getArticuloPrecio(InputArticuloPrecio objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputArticuloPrecio.class, objeto);
    }
}
