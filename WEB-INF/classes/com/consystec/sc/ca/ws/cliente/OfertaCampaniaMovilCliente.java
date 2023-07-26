package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.ofertacampania.InputOfertaCampaniaMovil;
import com.consystec.sc.ca.ws.output.ofertacampania.OutputOfertaCampaniaMovil;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class OfertaCampaniaMovilCliente {
    String serverUrl;

    /***
     * Metodo para obtener Ofertas o Campanias registradas
     **/
    public OutputOfertaCampaniaMovil getOfertaCampania(InputOfertaCampaniaMovil objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputOfertaCampaniaMovil.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}