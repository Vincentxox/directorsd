package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.pdv.InputBajaPDV;
import com.consystec.sc.ca.ws.input.pdv.InputConsultaPDV;
import com.consystec.sc.ca.ws.input.pdv.InputPDV;
import com.consystec.sc.ca.ws.output.pdv.OutputpdvDirec;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class PDVCliente {
    String serverUrl;

    /**
     * Metodo cliente para consumir ws para creacion de puntos de venta
     **/
    public OutputpdvDirec crearPDV(InputPDV objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputpdvDirec.class, objeto);
    }

    /**
     * Metodo cliente para consumir ws para modificar de puntos de venta
     **/
    public OutputpdvDirec modificarPDV(InputPDV objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputpdvDirec.class, objeto);
    }

    /**
     * Metodo cliente para consumir ws para dar de baja un pdv
     **/
    public OutputpdvDirec bajaPDV(InputBajaPDV objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputpdvDirec.class, objeto);
    }

    /**
     * Metodo cliente para consumir ws para dar de baja un pdv
     **/
    public OutputpdvDirec getPDV(InputConsultaPDV objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputpdvDirec.class, objeto);
    }

    /**
     * Metodo cliente para consumir ws para dar de baja un pdv
     **/
    public OutputpdvDirec getPDVDisponible(InputConsultaPDV objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputpdvDirec.class, objeto);
    }

    public OutputpdvDirec getCountPDVDisponible(InputConsultaPDV objeto) {
    	Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputpdvDirec.class, objeto);
    }
    
    public OutputpdvDirec getCountPDV(InputConsultaPDV objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputpdvDirec.class, objeto);
    }
    
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}