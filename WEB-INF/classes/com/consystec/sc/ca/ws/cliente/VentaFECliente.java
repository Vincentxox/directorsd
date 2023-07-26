package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.consultas.InputVentaFE;
import com.consystec.sc.ca.ws.output.consultas.OutputgetVentaFE;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class VentaFECliente {
	String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo para consumir ws para obtener informaci\u00F3n de vendedores por dts
     */
    public OutputgetVentaFE getVendedorFE(InputVentaFE objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputgetVentaFE.class, objeto);
    }
}
