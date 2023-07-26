package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.inventario.InputConsultaInventario;
import com.consystec.sc.ca.ws.input.inventario.InputValidaArticulo;
import com.consystec.sc.ca.ws.output.inventario.OutputInventario;
import com.consystec.sc.ca.ws.output.inventario.OutputValidaInventario;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class InventarioCliente {
    String serverUrl;

    /**
     * M\u00E9todo cliente para consumir ws para dar de baja un pdv
     **/
    public OutputInventario getInventario(InputConsultaInventario objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputInventario.class, objDatos);
    }

    public OutputValidaInventario ValidaSeries(InputValidaArticulo objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputValidaInventario.class, objDatos);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
