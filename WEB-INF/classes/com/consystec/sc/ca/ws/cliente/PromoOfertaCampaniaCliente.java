package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.ofertacampania.InputPromoOfertaCampania;
import com.consystec.sc.ca.ws.output.ofertacampania.OutputPromoOfertaCampania;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class PromoOfertaCampaniaCliente {

    String serverUrl;

    /**
     * M\u00E9todo para consumir ws para asignar Promocionales a Campanias
     */
    public OutputPromoOfertaCampania crearOfertaCampania(InputPromoOfertaCampania objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputPromoOfertaCampania.class, objeto);
    }

    /***
     * M\u00E9todo para consumir ws para modificar Promocionales en Campanias
     */

    public OutputPromoOfertaCampania modificarOfertaCampania(InputPromoOfertaCampania objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputPromoOfertaCampania.class, objeto);
    }

    /***
     * Metodo para obtener las asignaciones de Promocionales a Campanias
     * registradas
     **/
    public OutputPromoOfertaCampania getOfertaCampania(InputPromoOfertaCampania objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputPromoOfertaCampania.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
