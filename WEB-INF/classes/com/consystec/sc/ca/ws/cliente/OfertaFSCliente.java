package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.util.MediaType;
import com.consystec.sc.sv.ws.orm.ofertafs.input.InputOfertaFS;
import com.consystec.sc.sv.ws.orm.ofertafs.output.OutputOfertaFS;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class OfertaFSCliente {
	String serverUrl;
	
	/**
     * M\u00E9todo cliente para consumir ws para ofertas desde FS 
     **/
    public OutputOfertaFS getOfertaFS(InputOfertaFS objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputOfertaFS.class, objeto);
    }
    
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
