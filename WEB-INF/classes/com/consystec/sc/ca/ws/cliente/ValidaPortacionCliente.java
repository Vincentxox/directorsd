package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.portabilidad.InputPortabilidad;
import com.consystec.sc.ca.ws.output.portabilidad.OutputPortabilidad;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ValidaPortacionCliente {
	 String serverUrl;

	    public void setServerUrl(String serverUrl) {
	        this.serverUrl = serverUrl;
	    }

	    /**
	     * M\u00E9todo cliente para consumir ws para consulta portacion
	     **/
	    public OutputPortabilidad validaPortacion(InputPortabilidad objeto) {
	        Client client = Client.create();
	        WebResource resource = client.resource(this.serverUrl);
	        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
	                .post(OutputPortabilidad.class, objeto);
	    }
}
