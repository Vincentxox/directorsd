package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.historico.InputHistoricoPromocionales;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.historico.OutputHistoricoPromo;
import com.consystec.sc.sv.ws.operaciones.OperacionHistoricoPromo;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class CtrlHistoricoPromo extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlHistoricoPromo.class);
    private static String servicioGet = Conf.LOG_TRANSACCION_CONSULTA_DATOS;

    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDe como recurso al
     * servicio.
     * 
     * @param conn
     * 
     * @param input
     * @param metodo
     * @return
     * @throws SQLException 
     */
    public Respuesta validarInput(Connection conn, InputHistoricoPromocionales input, int metodo) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        
        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getIdCampania() != null && !"".equals(input.getIdCampania()) && !isNumeric(input.getIdCampania())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_OFERTACAMP_NUM_221, null, nombreClase,
                    nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        // se valida el tipo de cliente
        if (input.getTipoCliente() == null || "".equals(input.getTipoCliente())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_TIPO_NULO_431, null, nombreClase, nombreMetodo, null, input.getCodArea())
                    .getDescripcion();
            flag = true;
        } else {
            String tipoCliente = UtileriasJava.getConfig(conn, Conf.GRUPO_TIPO_CLIENTE_VENTA,
                    input.getTipoCliente().toUpperCase(), input.getCodArea());

            if (tipoCliente == null || "".equals(tipoCliente)) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_TIPO_INVALIDO_432, null, nombreClase, nombreMetodo, null, input.getCodArea())
                        .getDescripcion();
                flag = true;
            }
        }

        if (input.getIdTipo() != null && !"".equals(input.getIdTipo()) && !isNumeric(input.getIdTipo())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDTIPO_NUM_167, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getIdVendedor() != null && !"".equals(input.getIdVendedor()) && !isNumeric(input.getIdVendedor())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase,
                    nombreMetodo, null, input.getCodArea()).getDescripcion();
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
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes
     * consultas seg\u00FAn el m\u00E9todo que se trabaje.
     * @param tipoRuta 
     * @param invSidra 
     * @param estadoAlta 
     * @param input 
     * @param input 
     * @param Tipo 
     * @param estadoAlta 
     * @param invSidra 
     * @return condiciones
     */
    public static String obtenerCondiciones(InputHistoricoPromocionales input, String estadoAlta, String invSidra, String tipoRuta, BigDecimal idPais) {
        String sql = " WHERE"
            + " B.TCSCVENTAID = A.TCSCVENTAID"
            + " AND B.TCSCCATPAISID = "+ idPais
            + " AND UPPER(A.TIPO_INV) = UPPER('" + invSidra + "')"
            + " AND UPPER(A.ESTADO) = UPPER('" + estadoAlta + "')"
            + " AND UPPER(B.TIPO) = UPPER('" + input.getTipoCliente() + "')";
        
        if (input.getIdTipo() != null && !"".equals(input.getIdTipo())) {
            sql += " AND B.IDTIPO IN (" + input.getIdTipo() + ")";
        }

        if (input.getIdCampania() != null && !"".equals(input.getIdCampania())) {
            sql += " AND A.TCSCOFERTACAMPANIAID = " + input.getIdCampania();
        }
        

        sql += " AND A.TCSCOFERTACAMPANIAID IN"
                    + " (SELECT D.TCSCOFERTACAMPANIAID "
                        + "FROM TC_SC_OFERTA_CAMPANIA PARTITION("+ControladorBase.getPais(input.getCodArea())+") D "
                        + "WHERE "
                            + "UPPER (D.ESTADO) = UPPER ('" + estadoAlta + "') "
                            + "AND TRUNC (SYSDATE) BETWEEN"
                            + " TRUNC (D.FECHA_DESDE) AND TRUNC (D.FECHA_HASTA)";

                            if (input.getIdRuta() != null && !input.getIdRuta().equals("")) {
                                sql += " AND D.TCSCOFERTACAMPANIAID IN"
                                        + " (SELECT C.TCSCOFERTACAMPANIAID "
                                            + "FROM TC_SC_DET_PANELRUTA C "
                                            + "WHERE "
                                                + "UPPER (C.TIPO) = UPPER ('" + tipoRuta + "') "
                                                + "AND UPPER (C.ESTADO) = UPPER ('" + estadoAlta + "') "
                                                + "AND C.TCSCTIPOID = " + input.getIdRuta() + ""
                                                		+ "AND TCSCCATPAISID="+idPais+")";
                            }
                            
        sql += ")";
        
        
        
        return sql;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     * @param metodo
     * @return respuestaHistoricoPromo
     */
    public OutputHistoricoPromo getDatos(InputHistoricoPromocionales input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<Respuesta> respuesta = new ArrayList<Respuesta>();
        OutputHistoricoPromo output = null;
        Respuesta r = new Respuesta();

        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();
            BigDecimal idPais = getIdPais(conn, input.getCodArea());
            // Validación de datos en el input
            r = validarInput(conn, input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!r.getDescripcion().equals("OK")) {
                respuesta.add(r);
                output = new OutputHistoricoPromo();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            output = OperacionHistoricoPromo.doGet(conn, input, idPais);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de hist\u00F3rico de promocionales.", ""));
        } catch (SQLException e) {
            r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputHistoricoPromo();
            output.setRespuesta(r);

            listaLog.add(
                    ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO,
                            "Problema al consultar datos de hist\u00F3rico de promocionales.", e.getMessage()));

        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputHistoricoPromo();
            output.setRespuesta(r);

            listaLog.add(
                    ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO,
                            "Problema en el servicio de consulta hist\u00F3rico de promocionales.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
