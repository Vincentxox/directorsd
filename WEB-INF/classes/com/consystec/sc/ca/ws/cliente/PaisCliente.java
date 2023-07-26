package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.general.InputConsultaWeb;
import com.consystec.sc.ca.ws.output.pais.OutputConsultaPais;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class PaisCliente {

    String serverUrl;

    /**
     * M\u00E9todo cliente para consumir ws para obtener ultimas versiones de
     * cat\u00E9logos
     **/
    public OutputConsultaPais getPais(InputConsultaWeb objeto) {
        System.out.println("URL:" + this.serverUrl);
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputConsultaPais.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
