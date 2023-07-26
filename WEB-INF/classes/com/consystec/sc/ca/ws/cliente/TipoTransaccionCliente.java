package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.tipotransaccion.InputTransaccionInv;
import com.consystec.sc.ca.ws.output.transaccion.OutputTransaccionInv;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class TipoTransaccionCliente {
    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de tipo de transaccion
     **/
    public OutputTransaccionInv crearTipoTransaccion(InputTransaccionInv objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputTransaccionInv.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificar de tipo de transaccion
     **/
    public OutputTransaccionInv modificaTipoTransaccion(InputTransaccionInv objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputTransaccionInv.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener informaci\u00F3n de dispositivo
     **/
    public OutputTransaccionInv getTransacciones(InputTransaccionInv objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputTransaccionInv.class, objeto);
    }
}
