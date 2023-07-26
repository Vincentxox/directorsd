package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.cuenta.InputCuenta;
import com.consystec.sc.ca.ws.output.cuenta.OutputCuenta;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class CuentaCliente {
    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de Cuentas
     **/
    public OutputCuenta creaCuenta(InputCuenta objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCuenta.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificaci\u00F3n de Cuentas
     **/
    public OutputCuenta modCuenta(InputCuenta objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCuenta.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para obtener Cuentas
     **/
    public OutputCuenta getCuenta(InputCuenta objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputCuenta.class, objeto);
    }
}
