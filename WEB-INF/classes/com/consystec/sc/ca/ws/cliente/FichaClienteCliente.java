package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.fichacliente.InputFichaCliente;
import com.consystec.sc.ca.ws.output.fichacliente.OutputFichaCliente;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class FichaClienteCliente {
    String serverUrl;

    /**
     * M\u00E9todo cliente para obtener fichas de cliente
     **/
    public OutputFichaCliente getFichaCliente(InputFichaCliente objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputFichaCliente.class, objDatos);
    }
    
    /**
     * Metodo para crear la estructura de CLIENTE en SCL
     * */
    public OutputFichaCliente crearFichaClienteSCL(InputFichaCliente objDatos) {
    	Client client = Client.create();
    	WebResource resource = client.resource(this.serverUrl);
    	return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
    			.post(OutputFichaCliente.class, objDatos);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
