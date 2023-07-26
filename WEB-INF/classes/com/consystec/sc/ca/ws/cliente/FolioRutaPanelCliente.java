package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.folio.InputFolioVirtual;
import com.consystec.sc.ca.ws.output.folio.OutputConfiguracionFolioVirtual;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class FolioRutaPanelCliente {

    String serverUrl;

    /**
     * M\u00E9todo para consumir ws para crear folios de bodegas virtuales
     */
    public OutputConfiguracionFolioVirtual crearFolioV(InputFolioVirtual objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputConfiguracionFolioVirtual.class, objeto);
    }

    /***
     * M\u00E9todo para consumir ws para dar de baja folios de bodegas virtuales
     */
    public OutputConfiguracionFolioVirtual bajaFolioV(InputFolioVirtual objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputConfiguracionFolioVirtual.class, objeto);
    }

    /***
     * M\u00E9todo para consumir ws para obtener folios de bodegas virtuales
     */
    public OutputConfiguracionFolioVirtual getFolioV(InputFolioVirtual objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputConfiguracionFolioVirtual.class, objeto);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
