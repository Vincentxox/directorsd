package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.file.InputCargaFile;
import com.consystec.sc.ca.ws.output.file.OutputImagen;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class CFileCliente {

    String serverUrl;

    /**
     * M\u00E9todo cliente para consumir ws para adjuntar un archivo
     **/
    public OutputImagen cargaImagen(InputCargaFile objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputImagen.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para consultar un archivo
     **/
    public OutputImagen getImagen(InputCargaFile objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputImagen.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para eliminar un archivo
     **/
    public OutputImagen delImagen(InputCargaFile objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputImagen.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir ws para consultar imagenes asociadas a
     * visita o pdv
     **/
    public OutputImagen getImagenVisita(InputCargaFile objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputImagen.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
