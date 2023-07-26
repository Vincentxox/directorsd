package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.adjuntogestion.InputAdjuntoGestion;
import com.consystec.sc.ca.ws.output.adjuntogestion.OutputAdjuntoGestion;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class AdjuntoGestionCliente {
    String serverUrl;

    /**
     * M\u00E9todo cliente para cargar adjuntos
     **/
    public OutputAdjuntoGestion getAdjuntoGestion(InputAdjuntoGestion objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputAdjuntoGestion.class, objDatos);
    }
    
    public OutputAdjuntoGestion cargarAdjuntoGestion(InputAdjuntoGestion objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputAdjuntoGestion.class, objDatos);
    }
    
    public OutputAdjuntoGestion delAdjuntoGestion(InputAdjuntoGestion objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputAdjuntoGestion.class, objDatos);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
