package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.ingresosalida.InputIngresoSalida;
import com.consystec.sc.ca.ws.output.ingresosalida.OutputIngresoSalida;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class IngresoSalidaCliente {

    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de buzones
     **/
    public OutputIngresoSalida realizaMovInv(InputIngresoSalida objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputIngresoSalida.class, objeto);
    }
}
