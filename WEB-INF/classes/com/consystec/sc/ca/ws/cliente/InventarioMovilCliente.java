package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.inventariomovil.InputConsultaInventarioMovil;
import com.consystec.sc.ca.ws.output.inventariomovil.OutputInventarioMovil;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class InventarioMovilCliente {
    String serverUrl;

    /**
     * M\u00E9todo cliente para consultar datos de inventario.
     **/
    public OutputInventarioMovil getInventario(InputConsultaInventarioMovil objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputInventarioMovil.class, objDatos);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
