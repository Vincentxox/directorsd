package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.bodegadts.InputConfiguracionFolio;
import com.consystec.sc.ca.ws.output.bogegas.OutputConfiguracionFolio;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class FolioDTSCliente {

    String serverUrl;

    /**
     * Metodo para consumir ws para crear folios de bodegas DTS
     */
    public OutputConfiguracionFolio crearFolioBodDTS(InputConfiguracionFolio objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputConfiguracionFolio.class, objeto);
    }

    /***
     * Metodo para consumir ws para dar de baja folios de bodegas DTS
     */
    public OutputConfiguracionFolio bajaFolioBodDTS(InputConfiguracionFolio objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputConfiguracionFolio.class, objeto);
    }

    /***
     * M\u00E9todo para consumir ws para obtener folios de bodegas DTS
     */
    public OutputConfiguracionFolio getFolioBodDTS(InputConfiguracionFolio objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputConfiguracionFolio.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
