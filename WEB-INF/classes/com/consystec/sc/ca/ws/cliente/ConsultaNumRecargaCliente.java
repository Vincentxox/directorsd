package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.consultas.InputConsultaNumRecarga;
import com.consystec.sc.ca.ws.output.consultas.OutputConsultaNumRecarga;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ConsultaNumRecargaCliente {
    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener Remesas
     **/
    public OutputConsultaNumRecarga getEstadoNumRecarga(InputConsultaNumRecarga objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputConsultaNumRecarga.class, objeto);
    }
}
