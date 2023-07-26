package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.ofertacampania.InputOfertaCampania;
import com.consystec.sc.ca.ws.output.ofertacampania.OutputOfertaCampania;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class OfertaCampaniaCliente {

    String serverUrl;

    /**
     * M\u00E9todo para consumir ws para crear Ofertas o Campa\u00F1as
     */
    public OutputOfertaCampania crearOfertaCampania(InputOfertaCampania objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputOfertaCampania.class, objeto);
    }

    /***
     * M\u00E9todo para consumir ws para modificar Ofertas o Campa\u00F1as
     */

    public OutputOfertaCampania modificarOfertaCampania(InputOfertaCampania objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputOfertaCampania.class, objeto);
    }

    /***
     * Metodo para obtener Ofertas o Campa\u00F1as registradas
     **/
    public OutputOfertaCampania getOfertaCampania(InputOfertaCampania objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputOfertaCampania.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
