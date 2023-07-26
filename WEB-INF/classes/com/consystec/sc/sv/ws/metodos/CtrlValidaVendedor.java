package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.vendedorxdts.ValidarVendedor;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.vendedorxdts.Outputvendedorxdts;
import com.consystec.sc.ca.ws.output.vendedorxdts.VendedorxDTS;
import com.consystec.sc.sv.ws.operaciones.OperacionJornadaMasiva;
import com.consystec.sc.sv.ws.operaciones.OperacionVendxDTS;
import com.consystec.sc.sv.ws.orm.BodegaVendedor;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlValidaVendedor extends ControladorBase {

    private static final Logger log = Logger.getLogger(CtrlValidaVendedor.class);
    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_VENDEDORXDTS;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    /**
     * M\u00E9todo para obtener informaci\u00F3n de vendedores por distribuidor
     ***/
    public Outputvendedorxdts getValidaVendedor(ValidarVendedor objDatos) {
        listaLog = new ArrayList<LogSidra>();
        Outputvendedorxdts respVendedor = new Outputvendedorxdts();
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        List<VendedorxDTS> lstVend = new ArrayList<VendedorxDTS>();
        VendedorxDTS objVend = new VendedorxDTS();
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getValidaVendedor";
        Connection conn = null;
        String estadoIniciada = "";
        String estadoAlta = "";
        String fechaCierre = "";
        List<String> validaDispositivo = null;
        BigDecimal validaJornada = null;
        int dispositivoValido = 0;
        COD_PAIS = objDatos.getCodArea();

        if (objDatos.getUsuario() == null || objDatos.getUsuario().trim().equals("")) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
            respVendedor.setRespuesta(objRespuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
        } else {
            log.trace("idVendedor:" + objDatos.getIdVendedor());
            if (!(objDatos.getIdVendedor() == null || "".equals(objDatos.getIdVendedor().trim()))) {
                log.trace("entra a filtro id Vendedor");
                lstFiltros.add(new Filtro("UPPER(" + BodegaVendedor.CAMPO_VENDEDOR + ")", Filtro.EQ, objDatos.getIdVendedor()));
            }

            try {
                conn = getConnRegional();
               ID_PAIS = getIdPais(conn, objDatos.getCodArea());
                estadoIniciada = UtileriasJava.getConfig(conn, Conf.GRUPO_JORNADA_ESTADOS, Conf.JORNADA_ESTADO_INICIADA,objDatos.getCodArea());
                estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA,objDatos.getCodArea());
                lstVend = OperacionVendxDTS.getValorVendedor(conn,  lstFiltros, ID_PAIS);

                if (lstVend.isEmpty()) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(),metodo, null, objDatos.getCodArea());

                } else {
                    fechaCierre = OperacionJornadaMasiva.getFechaCierre(conn, lstVend.get(0).getIdResponsable(), objDatos.getCodArea(), ID_PAIS);
                    objVend = lstVend.get(0);
                    // obtiene tasa de cambio para manejo de precios de art\u00EDculos
                    objVend.setTasaCambio(OperacionVendxDTS.getTasaCambio(objDatos.getCodArea()));
                    objVend.setFechaCierre(fechaCierre);

                    // validarDispositivo
                    validaDispositivo = OperacionVendxDTS.validaDispositivo(conn, objDatos.getCodDispositivo(), ID_PAIS);
                    log.trace("ID TIPO:" + lstVend.get(0).getIdTipo() + " TIPO:" + lstVend.get(0).getTipo());

                    if (validaDispositivo.isEmpty()) {
                        log.trace("No existe dispositivo");
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_DISPOSITIVO_239, null,
                                this.getClass().toString(),metodo, null, objDatos.getCodArea());

                    } else if (!validaDispositivo.get(0).equalsIgnoreCase(estadoAlta)) {
                        log.trace("Dispositivo no esta de alta");
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_DISPOSITIVO_REPORTADO_782, null,
                                this.getClass().toString(), metodo, validaDispositivo.get(0).toUpperCase() + ".", objDatos.getCodArea());

                    } else if ((validaDispositivo.get(1).equals(lstVend.get(0).getIdTipo()))
                            && (validaDispositivo.get(2).equalsIgnoreCase((lstVend.get(0).getTipo())))) {
                        log.trace("Dispositivo 0k");
                        dispositivoValido = 1;

                    } else if (dispositivoValido == 0) {
                        log.trace("Dispositivo no esta qasignado a vendedor");
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_DISPOSITIVO_VENDEDOR_783, null,
                                this.getClass().toString(),metodo, null, objDatos.getCodArea());
                    }

                    if (dispositivoValido == 1) {
                        log.trace("Obtenido de valida dispositivo ID TIPO:" + validaDispositivo.get(1) + " TIPO:" + validaDispositivo.get(2));
                        log.trace("Es vendedor Responsable:" + lstVend.get(0).getResponsable());

                        // validarSiesResponsable
                        if (lstVend.get(0).getResponsable().equals("0")) {
                            // noResponsable ver si tiene jornada iniciada
                            validaJornada = OperacionVendxDTS.verificaJornada(conn, objDatos.getIdVendedor(),
                                    lstVend.get(0).getIdTipo(), lstVend.get(0).getTipo(), estadoIniciada, objDatos.getCodArea());
                            if (validaJornada.intValue() == 0) {
                                objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_INICIO_JORNADA_510, null,
                                        this.getClass().toString(),metodo, null, objDatos.getCodArea());
                            } else {
                                objVend.setVendedorAsignado(validaDispositivo.get(3));
                                objVend.setIdDispositivo(validaDispositivo.get(4));
                                log.trace("id dispositivo: "+validaDispositivo.get(4));
                                lstVend.set(0, objVend);
                                log.trace("valida correctamente vendedor responsable");
                                respVendedor.setVendedores(lstVend);
                                //objVend.setUserToken(validaDispositivo.get(5));
                                //objVend.setDevelopToken(validaDispositivo.get(6));
                                objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
                            }
                        }else if (lstVend.get(0).getNumTelefono().equals("0") && lstVend.get(0).getNivelBuzon().equals("2")){
                        	 objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_HAY_CLIENTE_GENERICO_618, null,
                                     this.getClass().toString(),metodo, null, objDatos.getCodArea());
                        }else if (lstVend.get(0).getNumTelefono().equals("0") && lstVend.get(0).getNivelBuzon().equals("3")){
                       	 objRespuesta = getMensaje(Conf_Mensajes.MSJ_ZONA_NO_TIENE_CLIENTE_GENERICO_619, null,
                                 this.getClass().toString(),metodo, null, objDatos.getCodArea());
                    }
                        else {
                            objVend.setVendedorAsignado(validaDispositivo.get(3));
                            objVend.setIdDispositivo(validaDispositivo.get(4));
                            //objVend.setUserToken(validaDispositivo.get(5));
                            //objVend.setDevelopToken(validaDispositivo.get(6));
                            lstVend.set(0, objVend);
                            log.trace("no vendedor responsable retorna informacion ");
                            respVendedor.setVendedores(lstVend);
                            objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
                        }

                        log.trace("NO. MENSAJE:" + objRespuesta.getCodResultado());
                        log.trace("MENSAJE:" + objRespuesta.getDescripcion());
                    }
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

        log.trace("NO. MENSAJE:" + respVendedor.getRespuesta().getCodResultado());
        log.trace("MENSAJE:" + respVendedor.getRespuesta().getDescripcion());
        return respVendedor;
    }
}
