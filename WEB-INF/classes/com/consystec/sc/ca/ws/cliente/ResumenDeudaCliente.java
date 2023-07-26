package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.deuda.InputTransDeuda;
import com.consystec.sc.ca.ws.input.solicitud.InputSolicitud;
import com.consystec.sc.ca.ws.output.deuda.OutputTransDeuda;
import com.consystec.sc.ca.ws.output.solicitud.OutputDeuda;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ResumenDeudaCliente {
    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir el ws de reporte cumplimiento de visitas
     */
    public OutputDeuda getResumenDeuda(InputSolicitud objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputDeuda.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir el ws transacciones de pago de ventas para
     * deuda
     */
    public OutputTransDeuda getTransaccionesDeuda(InputTransDeuda objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputTransDeuda.class, objeto);
    }
}
