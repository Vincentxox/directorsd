package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.venta.InputGetDetalle;
import com.consystec.sc.ca.ws.input.venta.InputGetVenta;
import com.consystec.sc.ca.ws.input.venta.InputVenta;
import com.consystec.sc.ca.ws.output.venta.OutputArticuloVenta;
import com.consystec.sc.ca.ws.output.venta.OutputVenta;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class VentaRutaCliente {

    String serverUrl;

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de paneles
     **/
    public OutputVenta crearVentaRuta(InputVenta objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputVenta.class, objeto);
    }

    public OutputVenta getVenta(InputGetVenta objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputVenta.class, objDatos);
    }

    public OutputArticuloVenta getDetalleVenta(InputGetDetalle objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputArticuloVenta.class, objDatos);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
