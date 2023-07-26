package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.vendedorxdts.InputVendxdts;
import com.consystec.sc.ca.ws.input.vendedorxdts.ValidarVendedor;
import com.consystec.sc.ca.ws.output.vendedorxdts.Outputvendedorxdts;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class VendedorxDTSCliente {

    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo para consumir ws para obtener informaci\u00F3n de vendedores por dts
     */
    public Outputvendedorxdts getVendedorxDTS(InputVendxdts objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(Outputvendedorxdts.class, objeto);
    }

    /**
     * M\u00E9todo para consumir ws para obtener informaci\u00F3n de vendedores por dts
     */
    public Outputvendedorxdts getValidaVendedor(ValidarVendedor objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(Outputvendedorxdts.class, objeto);
    }
}
