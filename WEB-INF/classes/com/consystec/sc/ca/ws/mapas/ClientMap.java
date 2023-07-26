package com.consystec.sc.ca.ws.mapas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import com.google.gson.GsonBuilder;

public class ClientMap {
	
    private ClientMap(){
    	
    }
    private static final Logger log = Logger.getLogger(ClientMap.class);

    public static void enviarVenta(String idTipo, String tipo, String nombre, String apellido, String nombreFacturacion,
            List<InputMapas> datosVenta, String latitud, String longitud,  String montoVenta, String uriSidra) {
        InputMapas datos = new InputMapas();
        List<InputMapas> productos = new ArrayList<InputMapas>();
        log.trace("inicia carga de datos para mapa");
        datos.setPais(datosVenta.get(0).getPais());
        if ("CF".equals(tipo)) {
            datos.setId(BigDecimal.ZERO);
            datos.setTipo(0);
            datos.setDescripcion(nombre + " " + apellido);
        } else {
            datos.setId(new BigDecimal(idTipo));
            datos.setTipo(1);
            datos.setDescripcion(nombreFacturacion);
        }
        
        log.trace("2");
        datos.setId_distribuidor(datosVenta.get(0).getId_distribuidor());
        log.trace("3");
        datos.setNombre_distribuidor(datosVenta.get(0).getNombre_distribuidor());
        log.trace("4");
        datos.setCanal(datosVenta.get(0).getCanal());
        log.trace("5");
        datos.setId_panelruta(datosVenta.get(0).getId_panelruta());
        log.trace("6");
        datos.setNombre_panelruta(datosVenta.get(0).getNombre_panelruta());
        log.trace("7");
        datos.setTipo_panelruta(datosVenta.get(0).getTipo_panelruta());
        log.trace("8");
        datos.setLat(latitud);
        datos.setLng(longitud);
        log.trace("asigno nuevo monto redondeado");
        if(datosVenta.get(0).getMonto()==null){
        	datos.setVenta_total(datosVenta.get(0).getMonto());
        }else{
        	log.trace("monto venta json:"+new BigDecimal(montoVenta));
        	datos.setVenta_total(new BigDecimal(montoVenta));
        }
        log.trace("inicia a agregar detalle");
        for (int i = 0; i < datosVenta.size(); i++) {
            InputMapas articulo = new InputMapas();
            articulo.setNombre_producto(datosVenta.get(i).getNombre_producto());
            articulo.setTecnologia(datosVenta.get(i).getTecnologia());
            articulo.setCantidad(datosVenta.get(i).getCantidad());
            log.trace("montos detallete:"+datosVenta.get(i).getMonto());
            articulo.setMonto(datosVenta.get(i).getMonto());
           
            productos.add(articulo);
        }
        datos.setProductos(productos);

        String json = new GsonBuilder().setPrettyPrinting().create().toJson(datos);
        log.debug("json venta mapa: " + json);

        try {
            ClientEndPoint client = new ClientEndPoint( uriSidra);
            client.sendMessage(json);
            client.session.close();
        } catch (Exception e) {
        	log.trace("trono aqui");
            log.error(e, e);
        }
    }
}