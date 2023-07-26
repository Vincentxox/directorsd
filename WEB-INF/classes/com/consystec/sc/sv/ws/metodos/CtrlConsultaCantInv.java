package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.inventario.InputConsultaCantInv;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.inventario.OutputConsultaCantInv;
import com.consystec.sc.sv.ws.operaciones.OperacionConsultaCantInv;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class CtrlConsultaCantInv extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlConsultaCantInv.class);
    private static List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_CANT_INV;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al servicio.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return Respuesta
     */
    public Respuesta validarInput(Connection conn, InputConsultaCantInv input, int metodo) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;

        log.debug("Validando datos...");

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (input.getIdBodega() != null && !isNumeric(input.getIdBodega())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODVIRTUAL_NUM_171, null, nombreClase,nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (input.getNumTraspasoScl() != null && !isNumeric(input.getNumTraspasoScl())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_NUM_TRASPASO_911, null, nombreClase,nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (flag) {
            r = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo, datosErroneos, input.getCodArea());
        } else {
            r.setDescripcion("OK");
            r.setCodResultado("1");
            r.setMostrar("0");
        }
        return r;
    }

    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET 
     * y en este caso tambi\u00E9n todos los que se insertar\u00E9n en el m\u00E9todo POST.
     * 
     * @param metodo
     * @param idBodega 
     * @param idBodega0 
     * @return
     */
    public static String[] obtenerCamposGetPost( String idBodega, String idBodega0) {
        if (idBodega == null || idBodega.equals(idBodega0)) {
            String campos[] = {
                "COUNT(DISTINCT " + Inventario.CAMPO_ARTICULO + ") AS " + Inventario.CAMPO_ARTICULO,
                "COUNT(" + Inventario.CAMPO_ARTICULO + ") AS TOTALINV"
            };
            return campos;
        } else {
            String campos[] = {
                "COUNT(DISTINCT " + Inventario.CAMPO_ARTICULO + ") AS " + Inventario.CAMPO_ARTICULO,
                "COUNT(" + Inventario.CAMPO_ARTICULO + ") AS TOTALINV",
                Inventario.CAMPO_TCSCBODEGAVIRTUALID
            };
            return campos;
        }
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje.
     * 
     * @param input
     * @param metodo
     * @return condiciones
     */
    public static List<Filtro> obtenerCondiciones( InputConsultaCantInv input, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (input.getIdBodega() != null && !"".equals(input.getIdBodega())) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodega()));
        }

        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
        
        if (input.getEstado() != null && !"".equals(input.getEstado())) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, input.getEstado()));
        }

        if (input.getTipoInv() != null && !"".equals(input.getTipoInv())) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_TIPO_INV, input.getTipoInv()));
        }

        if (input.getIdArticulo() != null && !"".equals(input.getIdArticulo())) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO, input.getIdArticulo()));
        }

        if (input.getSerie() != null && !"".equals(input.getSerie())) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_LIKE_AND, Inventario.CAMPO_SERIE, input.getSerie()));
        }

        if (input.getDescripcion() != null && !"".equals(input.getDescripcion())) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_LIKE_AND, Inventario.CAMPO_DESCRIPCION, input.getDescripcion()));
        }

        if (input.getNumTraspasoScl() != null && !"".equals(input.getNumTraspasoScl())) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_NUM_TRASPASO_SCL, input.getNumTraspasoScl()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_NOT_IN_AND, Inventario.CAMPO_TIPO_GRUPO_SIDRA, "TARJETASRASCA"));
        }

        return condiciones;
    }
   
    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo 
     * @return output Respuesta y listado con los Descuentos encontrados.
     */
    public OutputConsultaCantInv getDatos(InputConsultaCantInv input, int metodo) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta r = new Respuesta();
        OutputConsultaCantInv output = null;
        COD_PAIS = input.getCodArea();

        log.trace("Usuario: " + input.getUsuario());
        Connection conn = null;
        try {
            conn = getConnRegional();
           ID_PAIS = getIdPais(conn, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            r = validarInput(conn, input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equalsIgnoreCase(r.getDescripcion())) {
                output = new OutputConsultaCantInv();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                try {
                    output = OperacionConsultaCantInv.doGet(conn, input, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consult\u00F3 cantidad de inventario.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase,nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputConsultaCantInv();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar cantidad de inventario.", e.getMessage()));
                }
            }
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase,nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputConsultaCantInv();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de cantidad de inventario.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
