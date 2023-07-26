package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.inventariopromo.InputGetArtPromInventario;
import com.consystec.sc.ca.ws.input.promocionales.InputPromocionales;
import com.consystec.sc.ca.ws.output.inventariopromo.OutputArtPromInventario;
import com.consystec.sc.ca.ws.output.promocionales.OutputPromocionales;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class PromocionalesCliente {
    String serverUrl;

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de Promocionaless
     **/
    public OutputPromocionales crearPromocionales(InputPromocionales objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputPromocionales.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificaci\u00F3n de Promocionaless
     **/
    public OutputPromocionales modificaPromocionales(InputPromocionales objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputPromocionales.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para cambiar de estado las Promocionaless
     **/
    public OutputPromocionales bajaPromocionales(InputPromocionales objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputPromocionales.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener informaci\u00F3n de
     * Promocionaless
     **/
    public OutputPromocionales getPromocionales(InputPromocionales objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputPromocionales.class, objeto);
    }
    
    /**
     * M\u00E9todo cliente para consumir ws para obtener informaci\u00F3n de
     * Promocionaless por bodega
     **/
    public OutputArtPromInventario getPromocionalesPorBodega(InputGetArtPromInventario objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputArtPromInventario.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
