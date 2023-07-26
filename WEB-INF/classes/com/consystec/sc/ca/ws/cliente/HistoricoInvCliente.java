package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.inventario.InputHistoricoInv;
import com.consystec.sc.ca.ws.output.inventario.OutputHistorico;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class HistoricoInvCliente {

    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener informaci\u00F3n de historico de
     * inventario
     **/
    public OutputHistorico getHistorico(InputHistoricoInv objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputHistorico.class, objeto);
    }
}
