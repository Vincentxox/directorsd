package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.catalogo.InputCatalogo;
import com.consystec.sc.ca.ws.output.catalogo.OutputCatalogo;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * @author sbarrios Consystec 2015
 */

public class CatalogosCliente {

    String serverUrl;

    /**
     * Metodo cliente para consumir ws para creacion de catalogos de
     * configuracion
     **/
    public OutputCatalogo crearCatalogo(InputCatalogo objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCatalogo.class, objeto);
    }

    /**
     * Metodo cliente para consumir ws para modificar catalogo de configuracion
     **/
    public OutputCatalogo modificarCatalogo(InputCatalogo objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCatalogo.class, objeto);
    }

    /**
     * Metodo para consultar catalogo de configuracion
     */
    public OutputCatalogo getCatalogo(InputCatalogo objeto) {

        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);

        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCatalogo.class, objeto);
    }

    /**
     * Metodo para consultar catalogo de configuracion
     */
    public OutputCatalogo delCatalogo(InputCatalogo objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCatalogo.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}