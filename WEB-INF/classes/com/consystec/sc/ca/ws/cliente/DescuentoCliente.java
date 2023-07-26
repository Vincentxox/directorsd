package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.descuento.InputDescuento;
import com.consystec.sc.ca.ws.output.descuento.OutputDescuento;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class DescuentoCliente {

    String serverUrl;

    /**
     * M\u00E9todo para consumir ws para crear promociones o descuentos
     */
    public OutputDescuento crearDescuento(InputDescuento objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputDescuento.class, objeto);
    }

    /***
     * M\u00E9todo para consumir ws para modificar promociones o descuentos
     */
    public OutputDescuento modificarDescuento(InputDescuento objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputDescuento.class, objeto);
    }

    /***
     * M\u00E9todo para consumir ws para dar de baja una promoci\u00F3n o descuento
     */
    public OutputDescuento bajaDescuento(InputDescuento objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputDescuento.class, objeto);
    }

    /***
     * Metodo para obtener descuentos o promociones registradas
     **/
    public OutputDescuento getDescuento(InputDescuento objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputDescuento.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
