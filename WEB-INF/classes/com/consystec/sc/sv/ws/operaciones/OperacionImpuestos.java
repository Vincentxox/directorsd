package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.consystec.sc.ca.ws.input.impuestos.InputConsultaImpuestos;
import com.consystec.sc.ca.ws.input.impuestos.InputImpuestos;
import com.consystec.sc.ca.ws.input.impuestos.InputImpuestosGrupos;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.impuestos.OutputImpuestos;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class OperacionImpuestos {
	private OperacionImpuestos(){}
    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputCatalogo
     * @throws SQLException 
     * @throws CloneNotSupportedException 
     */
    public static OutputImpuestos doGet(Connection conn, InputConsultaImpuestos input, BigDecimal idPais) throws SQLException, CloneNotSupportedException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();
        InputImpuestos impuesto =null;
        List<InputImpuestos> listImpuestos;
        List<InputImpuestos> listDescuentos = new ArrayList<InputImpuestos>();
        List<InputImpuestosGrupos> listImpuestosGrupos ;
        List<InputImpuestosGrupos> listDescuentosGrupos ;
        String nombre = "";
        String prefijoDescuento = "DESCUENTO_";
        String prefijoCliente = "CLIENTE_";
        Respuesta respuesta = null;
        OutputImpuestos output = null;
        boolean duplicar = false;
        String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

        List<Order> orden = new ArrayList<Order>();
        orden.add(new Order(Catalogo.CAMPO_NOMBRE, Order.ASC));

        String campos[] = {
            Catalogo.CAMPO_GRUPO,
            Catalogo.CAMPO_NOMBRE,
            Catalogo.CAMPO_VALOR,
            Catalogo.CAMPO_ESTADO
        };

        // Se obtiene el listado de impuestos
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, Conf.IMPUESTOS_NOMBRE));

        List<String> listStringImpuestos;
        listStringImpuestos = UtileriasBD.getOneField(conn, Catalogo.CAMPO_NOMBRE, Catalogo.N_TABLA, condiciones, orden);

        // Se obtienen los datos de impuestos por pais.
        List<Map<String, String>> datosImpuestosPais;
        datosImpuestosPais = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, orden);

        // Se obtienen los datos de cada impuesto.
        String selectImpuestos = UtileriasJava.listToString(Conf.TIPO_TEXTO, listStringImpuestos, Conf.INSERT_TEXTO_SEPARADOR);
        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Catalogo.CAMPO_GRUPO, selectImpuestos));

        List<Map<String, String>> datosImpuestos;
        datosImpuestos = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, orden);
        
        // Se obtienen los datos en que momento se aplica el impuesto (antes o despu\u00E9s de descuento).
        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, Conf.IMPUESTOS_DESCUENTO));
        List<Map<String, String>> descuentoImpuestos ;
        descuentoImpuestos = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, orden);

        // Se imprime respuesta.
        respuesta=new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null, input.getCodArea());

        // Se crea output.
        output = new OutputImpuestos();
        output.setRespuesta(respuesta);
        output.setCodArea(input.getCodArea());

        // Se ordenan y agrupan los datos respectivamente.
        listImpuestos = new ArrayList<InputImpuestos>();
        for (int i = 0; i < listStringImpuestos.size(); i++) {
            nombre = listStringImpuestos.get(i);

            for (int j = 0; j < datosImpuestosPais.size(); j++) {
                if (datosImpuestosPais.get(j).get(Catalogo.CAMPO_NOMBRE).equals(nombre)) {
                    impuesto = new InputImpuestos();
                    impuesto.setNombre(nombre);
                    impuesto.setValor(datosImpuestosPais.get(j).get(Catalogo.CAMPO_NOMBRE));
                    impuesto.setPorcentaje(datosImpuestosPais.get(j).get(Catalogo.CAMPO_VALOR));
                    impuesto.setEstado(datosImpuestosPais.get(j).get(Catalogo.CAMPO_ESTADO));

                    List<Map<String, String>> datosImpuestosCliente ;
                    // Se obtienen los impuestos donde aplican casos especiales de cliente
                    condiciones.clear();
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Catalogo.CAMPO_GRUPO, "'" + prefijoCliente + listStringImpuestos.get(i) + "'"));
                    datosImpuestosCliente = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, orden);

                    if (datosImpuestosCliente != null && !datosImpuestosCliente.isEmpty()) {
                        duplicar = true;
                    } else {
                        for (int k = 0; k < descuentoImpuestos.size(); k++) {
                            if (descuentoImpuestos.get(k).get(Catalogo.CAMPO_NOMBRE).equals(nombre)) {
                                duplicar = false;
                                impuesto.setTipoCliente("");
                                impuesto.setDespuesDeDescuento(descuentoImpuestos.get(k).get(Catalogo.CAMPO_VALOR));
                            }
                        }
                    }

                    listImpuestosGrupos = new ArrayList<InputImpuestosGrupos>();
                    for (int k = 0; k < datosImpuestos.size(); k++) {
                        if (datosImpuestos.get(k).get(Catalogo.CAMPO_GRUPO).equals(nombre)) {
                            InputImpuestosGrupos grupo = new InputImpuestosGrupos();

                            grupo.setNombre(datosImpuestos.get(k).get(Catalogo.CAMPO_NOMBRE));
                            grupo.setValor(datosImpuestos.get(k).get(Catalogo.CAMPO_VALOR));
                            listImpuestosGrupos.add(grupo);
                        }
                    }
                    impuesto.setGrupos(listImpuestosGrupos);

                    if (duplicar) {
                        for (int m = 0; m < datosImpuestosCliente.size(); m++) {
                        	InputImpuestos impuestoClone = (InputImpuestos) impuesto;
                            impuestoClone.setTipoCliente(datosImpuestosCliente.get(m).get(Catalogo.CAMPO_NOMBRE));
                            impuestoClone.setDespuesDeDescuento(datosImpuestosCliente.get(m).get(Catalogo.CAMPO_VALOR));
                            listImpuestos.add(impuestoClone);
                        }
                     
                    } else {
                        listImpuestos.add(impuesto);
                    }
                }
            }
        }
        output.setImpuestos(listImpuestos);

        /*-------------------------------------------------------------------*/
        //Se obtiene el listado de descuentos.
        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, Conf.GRUPO_DESCUENTO_PAIS));

        List<String> listStringDescuentos;
        listStringDescuentos = UtileriasBD.getOneField(conn, Catalogo.CAMPO_NOMBRE, Catalogo.N_TABLA, condiciones, orden);

        List<Map<String, String>> datosDescuentosPais;
        List<Map<String, String>> datosDescuentos = new ArrayList<Map<String, String>>();
        
        // Se obtienen los datos de descuentos por pais.
        datosDescuentosPais = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, orden);

        if (!listStringDescuentos.isEmpty()) {
            // Se obtienen los datos de cada descuento.
            String selectDescuentos = "";
            for (int i = 0; i < listStringDescuentos.size(); i++) {
                selectDescuentos += "'" + prefijoDescuento + listStringDescuentos.get(i) + "'";

                if (i != listStringDescuentos.size() - 1)
                    selectDescuentos += ",";
            }

            condiciones.clear();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Catalogo.CAMPO_GRUPO, selectDescuentos));
            datosDescuentos = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, orden);
        }

        // Se ordenan y agrupan los datos respectivamente.
        listImpuestos = new ArrayList<InputImpuestos>();
        for (int i = 0; i < listStringDescuentos.size(); i++) {
            nombre = listStringDescuentos.get(i);

            for (int j = 0; j < datosDescuentosPais.size(); j++) {
                if (datosDescuentosPais.get(j).get(Catalogo.CAMPO_NOMBRE).equals(nombre)) {
                    InputImpuestos descuento = new InputImpuestos();
                    descuento.setNombre(nombre);
                    descuento.setValor(datosDescuentosPais.get(j).get(Catalogo.CAMPO_NOMBRE));
                    descuento.setPorcentaje(datosDescuentosPais.get(j).get(Catalogo.CAMPO_VALOR));
                    descuento.setEstado(datosDescuentosPais.get(j).get(Catalogo.CAMPO_ESTADO));
                    descuento.setDespuesDeDescuento("");

                    listDescuentosGrupos = new ArrayList<InputImpuestosGrupos>();
                    for (int k = 0; k < datosDescuentos.size(); k++) {
                        if (datosDescuentos.get(k).get(Catalogo.CAMPO_GRUPO).equals(prefijoDescuento + nombre)) {
                            InputImpuestosGrupos grupo = new InputImpuestosGrupos();

                            grupo.setNombre(datosDescuentos.get(k).get(Catalogo.CAMPO_NOMBRE));
                            grupo.setValor(datosDescuentos.get(k).get(Catalogo.CAMPO_VALOR));
                            listDescuentosGrupos.add(grupo);
                        }
                    }
                    descuento.setGrupos(listDescuentosGrupos);

                    listDescuentos.add(descuento);
                }
            }
        }
        output.setDescuentos(listDescuentos);

        return output;
    }
}
