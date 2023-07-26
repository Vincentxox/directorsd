package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.inventario.InputConsultaSeries;
import com.consystec.sc.ca.ws.output.inventario.OutputConsultaSeries;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ConsultaSeriesCliente {
    String serverUrl;

    /**
     * M\u00E9todo cliente para consumir ws para consultar cantidad de articulos en
     * inventario.
     **/
    public OutputConsultaSeries getSeries(InputConsultaSeries objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputConsultaSeries.class, objDatos);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
