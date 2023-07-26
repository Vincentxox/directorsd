package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.panel.InputPanel;
import com.consystec.sc.ca.ws.input.ruta.InputVendedor;
import com.consystec.sc.ca.ws.output.panel.OutputPanel;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class PanelCliente {

    String serverUrl;

    /**
     * M\u00E9todo cliente para consumir ws para creaci\u00F3n de paneles
     **/
    public OutputPanel crearPanel(InputPanel objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputPanel.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para modificar de puntos de venta
     **/
    public OutputPanel modificarPanel(InputPanel objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputPanel.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para dar de baja un pdv
     **/
    public OutputPanel cambiaEstadoPanel(InputPanel objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputPanel.class, objeto);
    }

    /**
     * M\u00E9todo cliente para obtener informaci\u00F3n de paneles
     **/
    public OutputPanel getPanel(InputPanel objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputPanel.class, objeto);
    }

    /**
     * M\u00E9todo cliente para obtener id de panel a la que pertenece un vendedor
     **/
    public OutputPanel getPanelbyVendedor(InputVendedor objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputPanel.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
