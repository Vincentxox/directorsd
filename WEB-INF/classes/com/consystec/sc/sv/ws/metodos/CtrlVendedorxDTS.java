package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.vendedorxdts.InputVendxdts;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.vendedorxdts.Outputvendedorxdts;
import com.consystec.sc.ca.ws.output.vendedorxdts.VendedorxDTS;
import com.consystec.sc.sv.ws.operaciones.OperacionVendxDTS;
import com.consystec.sc.sv.ws.orm.BodegaVendedor;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlVendedorxDTS extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlVendedorxDTS.class);
    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_VENDEDORXDTS;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    /**
     * M\u00E9todo para obtener informaci\u00F3n de vendedores por distribuidor
     ***/
    public Outputvendedorxdts getVendedorxDTS(InputVendxdts objDatos) {
        listaLog = new ArrayList<LogSidra>();
        Outputvendedorxdts respVendedor = new Outputvendedorxdts();
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        List<VendedorxDTS> lstVend = new ArrayList<VendedorxDTS>();
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getVendedorxDTS";
        COD_PAIS = objDatos.getCodArea();
        Connection conn = null;
        
        if (objDatos.getUsuario() == null || objDatos.getUsuario().trim().equals("")) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
            respVendedor.setRespuesta(objRespuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
        } else {
            if (!(objDatos.getIdDTS() == null || "".equals(objDatos.getIdDTS().trim()))) {
                log.trace("entra a filtro IDDTS");
                lstFiltros.add(new Filtro(Ruta.CAMPO_TC_SC_DTS_ID, Filtro.EQ, objDatos.getIdDTS()));
            }

            if (!(objDatos.getIdVendedor() == null || "".equals(objDatos.getIdVendedor().trim()))) {
                log.trace("entra a filtro id Vendedor");
                lstFiltros.add(new Filtro("UPPER(" + BodegaVendedor.CAMPO_VENDEDOR + ")", Filtro.EQ,
                        objDatos.getIdVendedor()));
            }

            try {
                conn = getConnRegional();
                ID_PAIS = getIdPais(conn, objDatos.getCodArea());

                // Filtrar por pais siempre
                lstFiltros.add(new Filtro(BodegaVendedor.CAMPO_TCSCCATPAISID, Filtro.EQ, ID_PAIS));

                lstVend = OperacionVendxDTS.getValorVendedor(conn,  lstFiltros,ID_PAIS);
                if (lstVend.isEmpty()) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo,
                            null, objDatos.getCodArea());
                } else {
                    respVendedor.setVendedores(lstVend);
                    objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
                }

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de vendedores por distribuidor.", ""));

            } catch (SQLException e) {
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
                log.error(e.getMessage(), e);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de vendedores por distribuidor.",
                        e.getMessage()));

            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, "", objDatos.getCodArea());
                log.error(e.getMessage(), e);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de vendedores por distribuidor.",
                        e.getMessage()));

            } finally {
                DbUtils.closeQuietly(conn);
                respVendedor.setRespuesta(objRespuesta);

                UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
            }
        }

        return respVendedor;
    }
}
