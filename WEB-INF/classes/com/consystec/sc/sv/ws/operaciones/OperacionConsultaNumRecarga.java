package com.consystec.sc.sv.ws.operaciones;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.consystec.sc.ca.ws.input.consultas.InputConsultaNumRecarga;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.consultas.OutputConsultaNumRecarga;
import com.consystec.sc.sv.ws.orm.NumRecarga;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class OperacionConsultaNumRecarga {
private OperacionConsultaNumRecarga(){}
    /**
     * Funci\u00F3n que consulta y devuelve el resultado encontrado.
     * 
     * @param conn
     * @param input
     * @return
     * @throws SQLException
     */
    public static OutputConsultaNumRecarga doGet(Connection conn, InputConsultaNumRecarga input) throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = new Respuesta();
        OutputConsultaNumRecarga output = new OutputConsultaNumRecarga();

        try {
            String estadoPendiente = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SOLICITUD, Conf.SOL_ESTADO_PENDIENTE, input.getCodArea());
            String estadoActivo = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_ESTADOS_PDV, Conf.ESTADO_ACTIVO, input.getCodArea());
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
            String tipoPDV = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELPDV, Conf.TIPO_PDV, input.getCodArea());

            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.CAMPO_TCSCPUNTOVENTAID, input.getIdPDV()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, PuntoVenta.CAMPO_ESTADO, estadoActivo));
            
            int existencias = UtileriasBD.selectCount(conn, PuntoVenta.N_TABLA, condiciones);
            if (existencias < 1) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_PDV_472, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());
            } else {
                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, NumRecarga.CAMPO_IDTIPO, input.getIdPDV()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, NumRecarga.CAMPO_TIPO, tipoPDV));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, NumRecarga.CAMPO_ESTADO, estadoAlta));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, NumRecarga.CAMPO_NUM_RECARGA, input.getNumRecarga()));

                String campo = "NVL(" + NumRecarga.CAMPO_ESTADO_PAYMENT + ", '" + estadoPendiente + "') "
                        + NumRecarga.CAMPO_ESTADO_PAYMENT;

                campo = UtileriasBD.getOneRecord(conn, campo, NumRecarga.N_TABLA, condiciones);

                if (campo == null || campo.equals("")) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NUMEROS_NO_EXISTEN_PDV_754, null,
                            nombreClase, nombreMetodo, input.getNumRecarga(), input.getCodArea());

                } else {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_GETDATOS12, null, nombreClase,
                            nombreMetodo, null, input.getCodArea());

                    output.setEstado(campo);
                }
            }

        } finally {
            output.setRespuesta(respuesta);
        }

        return output;
    }
}
