package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.oferCom.InputConsultaArticulos;
import com.consystec.sc.ca.ws.output.oferCom.OutputConsultaArticulos;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class OfertaComercialCliente {

    String serverUrl;

    /**
     * M\u00E9todo cliente para consumir ws para obtener articulos disponibles en
     * sistema comercial
     **/
    public OutputConsultaArticulos getArticulos(InputConsultaArticulos objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputConsultaArticulos.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener precio de un articulo
     * especifico
     **/
    public OutputConsultaArticulos getPrecioArticulo(InputConsultaArticulos objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputConsultaArticulos.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
