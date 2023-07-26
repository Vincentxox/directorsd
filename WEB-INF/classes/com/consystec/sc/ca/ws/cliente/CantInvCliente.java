package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.inventario.InputConsultaCantInv;
import com.consystec.sc.ca.ws.output.inventario.OutputConsultaCantInv;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class CantInvCliente {
    String serverUrl;

    /**
     * M\u00E9todo cliente para consumir ws para consultar cantidad de articulos en
     * inventario.
     **/
    public OutputConsultaCantInv getInventario(InputConsultaCantInv objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputConsultaCantInv.class, objDatos);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
