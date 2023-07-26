package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.descuentoFS.InputDescuentoFS;
import com.consystec.sc.ca.ws.output.descuentoFS.OutputDescuentoFS;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class DescuentoFSCliente {
	String serverUrl;
	
    public OutputDescuentoFS getDescuentoFS(InputDescuentoFS objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputDescuentoFS.class, objeto);
    }
    
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
