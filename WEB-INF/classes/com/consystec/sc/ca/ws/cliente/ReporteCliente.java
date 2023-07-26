package com.consystec.sc.ca.ws.cliente;

import com.consystec.sc.ca.ws.input.reporte.InputReporteCantInvJornada;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCtaDTS;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCumplimientoVenta;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCumplimientoVisita;
import com.consystec.sc.ca.ws.input.reporte.InputReporteEfectividadVenta;
import com.consystec.sc.ca.ws.input.reporte.InputReporteInventarioVendido;
import com.consystec.sc.ca.ws.input.reporte.InputReportePDV;
import com.consystec.sc.ca.ws.input.reporte.InputReporteRecarga;
import com.consystec.sc.ca.ws.input.reporte.InputReporteXZ;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCantInvJornada;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCtaDTS;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCumplimientoVenta;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCumplimientoVisita;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteEfectividadVenta;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteInventarioVendido;
import com.consystec.sc.ca.ws.output.reporte.OutputReportePDV;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteRecarga;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteXZ;
import com.consystec.sc.ca.ws.output.reporte.OutputResumenInventarioVendido;
import com.consystec.sc.ca.ws.util.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ReporteCliente {

    String serverUrl;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * M\u00E9todo cliente para consumir el ws de reporte cumplimiento de visitas
     */
    public OutputReporteCumplimientoVisita getReporteCumplimientoVisita(InputReporteCumplimientoVisita objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputReporteCumplimientoVisita.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir el ws de reporte inventario vendido
     */
    public OutputReporteInventarioVendido getReporteInventarioVendido(InputReporteInventarioVendido objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputReporteInventarioVendido.class, objeto);
    }

    /**
     * M\u00E9todo para obtener el listado resumen de registros de articulos
     * vendidos.
     */
    public OutputResumenInventarioVendido getResumenInventarioVendido(InputReporteInventarioVendido objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputResumenInventarioVendido.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir el ws de count cumplimiento de ventas
     */
    public OutputReporteCumplimientoVenta getCountCumplimientoVenta(InputReporteCumplimientoVenta objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputReporteCumplimientoVenta.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir el ws de reporte cumplimiento de ventas
     */
    public OutputReporteCumplimientoVenta getReporteCumplimientoVenta(InputReporteCumplimientoVenta objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputReporteCumplimientoVenta.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir el ws de reporte efectividad de ventas
     */
    public OutputReporteEfectividadVenta getReporteEfectividadVenta(InputReporteEfectividadVenta objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputReporteEfectividadVenta.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir el ws de reporte reporte_XZ
     */
    public OutputReporteXZ generaReporteXZ(InputReporteXZ objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputReporteXZ.class, objeto);
    }

    /**
     * M\u00E9todo cliente para consumir el ws de reporte de puntos de venta.
     */
    public OutputReportePDV getReportePDV(InputReportePDV objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputReportePDV.class, objeto);
    }
    
    /**
     * M\u00E9todo cliente para consumir el ws de reporte de inventario por jornada.
     */
    public OutputReporteCantInvJornada getReporteInvJornada(InputReporteCantInvJornada objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputReporteCantInvJornada.class, objeto);
    }
    
    /**
     * M\u00E9todo cliente para consumir el ws de reporte de inventario por jornada.
     */
    public OutputReporteCtaDTS getReporteCtaDts(InputReporteCtaDTS objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputReporteCtaDTS.class, objeto);
    }
    
    /**
     * M\u00E9todo cliente para consumir el ws de reporte de recargas.
     */
    public OutputReporteRecarga getReporteRecargas(InputReporteRecarga objeto) {
        Client client = Client.create();
        WebResource resource = client.resource(this.serverUrl);
        return resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_ENCODING)
                .post(OutputReporteRecarga.class, objeto);
    }
}
