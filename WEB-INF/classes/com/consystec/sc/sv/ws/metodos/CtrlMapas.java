package com.consystec.sc.sv.ws.metodos;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.mapas.InputMapas;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.orm.Visita;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.google.gson.GsonBuilder;

/**
 * @author Victor Cifuentes @ Consystec - 2017
 *
 */
public class CtrlMapas {
	private CtrlMapas(){}
    private static final Logger log = Logger.getLogger(CtrlMapas.class);

    public static Respuesta enviarDTS(Connection conn, String codArea, BigDecimal idDTS, String nombreDTS,
            String usuario, String estado, String canal) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "enviarDTS";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta respuesta = new Respuesta();
        InputMapas inputDTS = new InputMapas();

        try {
            inputDTS.setPais(ControladorBase.getPais(codArea));
            inputDTS.setId_distribuidor(idDTS);
            inputDTS.setNombre_distribuidor(nombreDTS);
            inputDTS.setCanal(canal);
            inputDTS.setEstado(estado);

            List<InputMapas> listDTS = new ArrayList<InputMapas>();
            listDTS.add(inputDTS);

            // obteniendo url de servicio
            String url = UtileriasJava.getConfig(conn, Conf.GRUPO_MAPAS_URL_SERVICIOS, Conf.URL_SERVICIO_DTS, codArea);
            String json = new GsonBuilder().setPrettyPrinting().create().toJson(listDTS);
            log.trace("json: " + json);

            if (url == null || "null".equals(url) || "".equals(url)) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_URL_NOENCONTRADA_861, null, nombreClase,
                        nombreMetodo, null, codArea);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_SERVICIOS_MAPA, nombreMetodo, idDTS.toString(),
                        Conf.LOG_TIPO_DISTRIBUIDOR,
                        "Problema al consumir servicio para registrar DTS de nombre " + nombreDTS + " en mapa.",
                        respuesta.getDescripcion()));
            } else {
                HttpURLConnection respWS = consumirWS(url, json);

                if (respWS.getResponseCode() != 200 && !"1".equals(respWS.getResponseMessage())) {
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_SERVICIOS_MAPA, nombreMetodo,
                            idDTS.toString(), Conf.LOG_TIPO_PDV, "No se pudo registrar en el mapa el DTS de nombre "
                                    + nombreDTS + ". C\u00F3digo: " + respWS.getResponseCode(),
                            respWS.getResponseMessage()));
                } else {
                    registrarEnvio(conn, Distribuidor.N_TABLA, Distribuidor.CAMPO_TC_SC_DTS_ID, idDTS);
                }

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_SERVICIOS_MAPA, nombreMetodo, idDTS.toString(),
                        Conf.LOG_TIPO_DISTRIBUIDOR,
                        "Se consume servicio para registrar DTS de nombre " + nombreDTS + " en mapa.", ""));
            }

        } catch (Exception e) {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase,
                    nombreMetodo, null, codArea);
            log.error("Excepcion: " + e.getMessage(), e);

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_SERVICIOS_MAPA, nombreMetodo, idDTS.toString(),
                    Conf.LOG_TIPO_DISTRIBUIDOR,
                    "Problema al consumir servicio para registrar DTS de nombre " + nombreDTS + " en mapa.",
                    e.getMessage()));

        } finally {
            UtileriasJava.doInsertLog(listaLog, usuario, codArea);
        }

        return respuesta;
    }

    public static Respuesta enviarPDV(Connection conn, InputMapas input, String codArea, String usuario) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "enviarPDV";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta respuesta = new Respuesta();

        try {
            List<InputMapas> list = new ArrayList<InputMapas>();
            input.setPais(ControladorBase.getPais(codArea));
            list.add(input);

            // obteniendo url de servicio
            String url = UtileriasJava.getConfig(conn, Conf.GRUPO_MAPAS_URL_SERVICIOS, Conf.URL_SERVICIO_PDV, codArea);
            String json = new GsonBuilder().setPrettyPrinting().create().toJson(list);
            log.trace("json: " + json);

            if (url == null || "null".equals(url) || "".equals(url)) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_URL_NOENCONTRADA_861, null, nombreClase,
                        nombreMetodo, null, codArea);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_SERVICIOS_MAPA, nombreMetodo,
                        input.getId_pdv().toString(), Conf.LOG_TIPO_PDV,
                        "Problema al consumir servicio para registrar PDV de nombre " + input.getNombre_pdv()
                                + " en mapa.",
                        respuesta.getDescripcion()));
            } else {
                HttpURLConnection respWS = consumirWS(url, json);

                if (respWS.getResponseCode() != 200 && !"1".equals(respWS.getResponseMessage())) {
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_SERVICIOS_MAPA, nombreMetodo,
                            input.getId_pdv().toString(),
                            Conf.LOG_TIPO_PDV, "No se pudo registrar en el mapa el PDV de nombre "
                                    + input.getNombre_pdv() + ". C\u00F3digo: " + respWS.getResponseCode(),
                            respWS.getResponseMessage()));
                } else {
                    registrarEnvio(conn, PuntoVenta.N_TABLA, PuntoVenta.CAMPO_TCSCPUNTOVENTAID, input.getId_pdv());
                }

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_SERVICIOS_MAPA, nombreMetodo,
                        input.getId_pdv().toString(), Conf.LOG_TIPO_PDV,
                        "Se consume servicio para registrar PDV de nombre " + input.getNombre_pdv() + " en mapa.", ""));
            }

        } catch (Exception e) {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase,
                    nombreMetodo, null, codArea);
            log.error("Excepcion: " + e.getMessage(), e);

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_SERVICIOS_MAPA, nombreMetodo,
                    input.getId_pdv().toString(), Conf.LOG_TIPO_PDV,
                    "Problema al consumir servicio para registrar PDV de nombre " + input.getNombre_pdv() + " en mapa.",
                    e.getMessage()));

        } finally {
            UtileriasJava.doInsertLog(listaLog, usuario, codArea);
        }

        return respuesta;
    }
    
    public static Respuesta enviarVisita(Connection conn, InputMapas input, String codArea, String usuario, BigDecimal idVisita) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "enviarVisita";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta respuesta = new Respuesta();
        SimpleDateFormat FECHAHORA2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat FORMATO_TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            List<InputMapas> list = new ArrayList<InputMapas>();
            input.setPais(ControladorBase.getPais(codArea));
            input.setUltima_visita(UtileriasJava.formatStringDate(input.getUltima_visita(), FORMATO_TIMESTAMP, FECHAHORA2));
            list.add(input);

            // obteniendo url de servicio
            String url = UtileriasJava.getConfig(conn, Conf.GRUPO_MAPAS_URL_SERVICIOS, Conf.URL_SERVICIO_VISITA, codArea);
            String json = new GsonBuilder().setPrettyPrinting().create().toJson(list);
            log.trace("json: " + json);

            if (url == null || "null".equals(url) || "".equals(url)) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_URL_NOENCONTRADA_861, null, nombreClase,
                        nombreMetodo, null, codArea);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_SERVICIOS_MAPA, nombreMetodo,
                        input.getId_pdv().toString(), Conf.LOG_TIPO_PDV,
                        "Problema al consumir servicio para registrar visita a PDV en mapa.",
                        respuesta.getDescripcion()));
            } else {
                HttpURLConnection respWS = consumirWS(url, json);

                if (respWS.getResponseCode() != 200 && !"1".equals(respWS.getResponseMessage())) {
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_SERVICIOS_MAPA, nombreMetodo,
                            input.getId_pdv().toString(), Conf.LOG_TIPO_PDV,
                            "No se pudo registrar visita a PDV el mapa. C\u00F3digo: " + respWS.getResponseCode(),
                            respWS.getResponseMessage()));
                } else {
                    // si todo es correcto se retorna una respuesta de OK
                    registrarEnvio(conn, Visita.N_TABLA, Visita.CAMPO_TCSCVISITAID, idVisita);
                }

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_SERVICIOS_MAPA, nombreMetodo,
                        input.getId_pdv().toString(), Conf.LOG_TIPO_PDV,
                        "Se consume servicio para registrar visita a PDV en mapa.", ""));
            }

        } catch (Exception e) {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase,
                    nombreMetodo, null, codArea);
            log.error("Excepcion: " + e.getMessage(), e);

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_SERVICIOS_MAPA, nombreMetodo,
                    input.getId_pdv().toString(), Conf.LOG_TIPO_PDV,
                    "Problema al consumir servicio para registrar visita a PDV en mapa.", e.getMessage()));

        } finally {
            UtileriasJava.doInsertLog(listaLog, usuario, codArea);
        }

        return respuesta;
    }

    private static HttpURLConnection consumirWS(String url, String json) throws IOException {
        URL urlWS = new URL(url);
        HttpURLConnection connWS = (HttpURLConnection) urlWS.openConnection();
        connWS.setConnectTimeout(5000);
        connWS.setRequestProperty("Content-Type", "application/json");
        connWS.setDoOutput(true);
        connWS.setRequestMethod("POST");

        OutputStream os = connWS.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        osw.write(json);
        osw.flush();
        log.trace("respuestaCode: " + connWS.getResponseCode() + ", respuesta: " + connWS.getResponseMessage());

        osw.close();
        connWS.disconnect();
        return connWS;
    }

    private static void registrarEnvio(Connection conn, String tabla, String campo, BigDecimal id)
            throws SQLException {
        PreparedStatement pstmt = null;
        String query = "UPDATE ? SET ENVIADO_MAPA = 1 WHERE ? = ?";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, tabla);
            pstmt.setString(2, campo);
            pstmt.setBigDecimal(3, id);
            pstmt.executeUpdate();
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }
}
