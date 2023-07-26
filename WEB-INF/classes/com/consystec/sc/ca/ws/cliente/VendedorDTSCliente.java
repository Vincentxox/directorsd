package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.vendedordts.InputVendedorDTS;
import com.consystec.sc.ca.ws.output.vendedordts.OutputVendedorDTS;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class VendedorDTSCliente {

    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo para consumir ws para obtener informaci\u00F3n de vendedores por dts
     */
    public OutputVendedorDTS getVendedorDTS(InputVendedorDTS objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputVendedorDTS.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n asociaciones de vendedores
     * por dts
     **/
    public OutputVendedorDTS asignaVendedorDTS(InputVendedorDTS objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputVendedorDTS.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para dar de baja asociaciones de
     * vendedores por dts
     **/
    public OutputVendedorDTS bajaVendedorDTS(InputVendedorDTS objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputVendedorDTS.class, objDatos);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificar asociaciones de vendedores
     * por dts
     **/
    public OutputVendedorDTS modificarVendedorDTS(InputVendedorDTS objDatos) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputVendedorDTS.class, objDatos);
    }
}
