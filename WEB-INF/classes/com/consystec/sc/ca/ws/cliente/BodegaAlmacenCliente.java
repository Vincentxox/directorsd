package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.bodegadts.InputBodegaDTS;
import com.consystec.sc.ca.ws.output.bogegas.OutputBodegaDTS;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * @author susana barrios consystec 2015
 */
public class BodegaAlmacenCliente {

    String serverUrl;

    /**
     * Metodo para consumir ws para asociar bodegas del sistema comercial a
     * distribuidores
     */
    public OutputBodegaDTS asociarBodegaDTS(InputBodegaDTS objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputBodegaDTS.class, objeto);
    }

    /***
     * Metodo para consumir ws para modificar asociacion bodegas del sistema
     * comercial a distribuidores
     */
    public OutputBodegaDTS modificarBodegaDTS(InputBodegaDTS objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputBodegaDTS.class, objeto);
    }

    /***
     * Metodo para consumir ws para modificar asociacion bodegas del sistema
     * comercial a distribuidores
     */
    public OutputBodegaDTS eliminarBodegaDTS(InputBodegaDTS objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputBodegaDTS.class, objeto);
    }

    /***
     * Metodo para obtener asociacion de bodegas a DTS
     **/
    public OutputBodegaDTS getBodegaDTS(InputBodegaDTS objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputBodegaDTS.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}