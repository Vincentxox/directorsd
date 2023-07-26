package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.ingresosalida.ArticulosIngresoSalida;
import com.consystec.sc.ca.ws.input.ingresosalida.InputIngresoSalida;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.ingresosalida.ArticulosIncorrectos;
import com.consystec.sc.ca.ws.output.ingresosalida.OutputIngresoSalida;
import com.consystec.sc.sv.ws.operaciones.OperacionIngresoSalida;
import com.consystec.sc.sv.ws.orm.HistoricoInv;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlIngresoSalidaInvPromocional extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlIngresoSalidaInvPromocional.class);
    private static String servicioPost = Conf.LOG_WS_INGRESO_SALIDAD_PROMOCIONALES;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputIngresoSalida objDatos) {
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
        }

        if (objDatos.getArticulos() == null) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_ARTICULOS_NULO_48, this.getClass().toString(), null, metodo,
                    null, objDatos.getCodArea());
        } else {
            for (int a = 0; a < objDatos.getArticulos().size(); a++) {
                if (objDatos.getArticulos().get(a).getIdArticulo() == null
                        || "".equals(objDatos.getArticulos().get(a).getIdArticulo())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_ARTICULO_INCOMPLETO_59, this.getClass().toString(),
                            null, metodo, "No. Elemento:" + (a + 1) + ".El campo idArt\u00EDculo esta vac\u00EDo", objDatos.getCodArea());
                }
                if (objDatos.getArticulos().get(a).getTipo() == null
                        || "".equals(objDatos.getArticulos().get(a).getTipo())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_ARTICULO_INCOMPLETO_59, this.getClass().toString(),
                            null, metodo, "No. Elemento:" + (a + 1) + ".El campo tipo esta vac\u00EDo", objDatos.getCodArea());
                }
                if (objDatos.getArticulos().get(a).getIdBodegaVirtual() == null
                        || "".equals(objDatos.getArticulos().get(a).getIdBodegaVirtual())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_ARTICULO_INCOMPLETO_59, this.getClass().toString(),
                            null, metodo, "No. Elemento:" + (a + 1) + ".El campo idBodegaVirtual esta vac\u00EDo", objDatos.getCodArea());
                }
                if (objDatos.getArticulos().get(a).getCantidad() == null
                        || "".equals(objDatos.getArticulos().get(a).getCantidad())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_ARTICULO_INCOMPLETO_59, this.getClass().toString(),
                            null, metodo, "No. Elemento:" + (a + 1) + ".El campo cantidad esta vac\u00EDo", objDatos.getCodArea());
                }
            }
        }

        return objRespuesta;
    }

    public OutputIngresoSalida revisaArticulos(InputIngresoSalida objDatos) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        Respuesta objRespuesta = new Respuesta();
        OutputIngresoSalida respuestaWS = new OutputIngresoSalida();
        List<ArticulosIngresoSalida> listanoValida = new ArrayList<ArticulosIngresoSalida>();
        List<ArticulosIngresoSalida> listanoValida2 = new ArrayList<ArticulosIngresoSalida>();
        List<ArticulosIngresoSalida> listanoValida3 = new ArrayList<ArticulosIngresoSalida>();
        List<BigDecimal> lstExiste = new ArrayList<BigDecimal>();
        ArticulosIncorrectos artNoExisten = new ArticulosIncorrectos();
        ArticulosIncorrectos artDemasiadaExistencia = new ArticulosIncorrectos();
        ArticulosIncorrectos artnoHayInv = new ArticulosIncorrectos();
        List<ArticulosIncorrectos> lista = new ArrayList<ArticulosIncorrectos>();
        List<HistoricoInv> listaHistorico = new ArrayList<HistoricoInv>();

        Connection conn = null;
        String metodo = "revisaArticulos";
        String tipoInv = "";
        String estadoDisponible = "";
        String tipoSalida = "";
        String tipoIngreso = "";
        String estadoAlta = "";
        String razonResultado1 = "";
        String razonResultado2 = "";
        String razonResultado3 = "";
        String razonFinal = "";
        BigDecimal tIngreso = null;
        BigDecimal tSalida = null;

        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnRegional();
                conn.setAutoCommit(false);
                ID_PAIS = getIdPais(conn, objDatos.getCodArea());
                
                tipoInv = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPOINV, Conf.SOL_TIPOINV_SIDRA, objDatos.getCodArea());
                estadoDisponible = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_DISPONIBLE, objDatos.getCodArea());
                tipoSalida = UtileriasJava.getConfig(conn, Conf.GRUPO_MOV_INV_PROMOCIONAL, Conf.SALIDA, objDatos.getCodArea());
                tipoIngreso = UtileriasJava.getConfig(conn, Conf.GRUPO_MOV_INV_PROMOCIONAL, Conf.INGRESO, objDatos.getCodArea());
                estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());
                tIngreso = getIdTransaccion(conn, Conf.T_INGRESO, ID_PAIS);
                tSalida = getIdTransaccion(conn, Conf.T_SALIDA, ID_PAIS);

                for (ArticulosIngresoSalida obj : objDatos.getArticulos()) {
                    lstExiste = new ArrayList<BigDecimal>();

                    // verificando si existe el art\u00EDculo ingresado
                    lstExiste = OperacionIngresoSalida.existeArtPromo(conn, obj.getIdArticulo(), estadoAlta,
                            estadoDisponible, obj.getIdBodegaVirtual(), tipoInv, objDatos.getCodArea(), ID_PAIS);

                    log.trace("IDARTICULO:" + obj.getIdArticulo());
                    log.trace("tipo movimiento:" + obj.getTipo());
                    if (obj.getTipo().equalsIgnoreCase(tipoIngreso)) {
                        // existe en catalogo
                        if (lstExiste.get(0).intValue() == 1) {
                            if (lstExiste.get(1).intValue() > 0) {
                                // existe en inventario realizo update
                                log.trace("Actualiza ingreso");
                                OperacionIngresoSalida.updateCantInventario(conn, obj.getIdArticulo(),
                                        new BigDecimal(obj.getCantidad()).intValue(), estadoDisponible,
                                        obj.getIdBodegaVirtual(), tipoInv, objDatos.getUsuario(), 1, objDatos.getCodArea(), ID_PAIS);
                                listaHistorico.add(armaHistorico(obj, objDatos.getUsuario(), estadoAlta, tipoInv, tIngreso,ID_PAIS));
                            } else {
                                // no existe realizo insert
                                log.trace("Inserta ingreso");
                                Inventario objInv = new Inventario();
                                objInv.setArticulo(new BigDecimal(obj.getIdArticulo()));
                                objInv.setTcscbodegavirtualid(new BigDecimal(obj.getIdBodegaVirtual()));
                                objInv.setCantidad(new BigDecimal(obj.getCantidad()));
                                objInv.setEstado(estadoDisponible);
                                objInv.setSeriado("0");
                                objInv.setCreado_por(objDatos.getUsuario());
                                objInv.setTipo_inv(tipoInv);
                                OperacionIngresoSalida.insertInv(conn, objInv, ID_PAIS);
                                listaHistorico.add(armaHistorico(obj, objDatos.getUsuario(), estadoAlta, tipoInv, tIngreso,ID_PAIS));
                            }

                            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_INGRESO_PROMO,
                                    servicioPost, obj.getIdArticulo(),
                                    Conf.LOG_TIPO_ARTICULO,
                                    "Se registr\u00F3 ingreso de " + obj.getCantidad() + " art\u00EDculo(s).", ""));
                        } else {
                            artNoExisten.setRazon("Los art\u00EDculos siguientes no existen en el Sistema de SIDRA.");
                            ArticulosIngresoSalida objNoExiste = new ArticulosIngresoSalida();
                            objNoExiste.setIdArticulo(obj.getIdArticulo());
                            objNoExiste.setTipo(tipoIngreso);
                            listanoValida.add(objNoExiste);
                            razonResultado1 += " Tipo Operaci\u00F3n: " + tipoIngreso + ", Articulo: " + obj.getIdArticulo() + ".";
                            
                            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_INGRESO_PROMO,
                                    servicioPost, obj.getIdArticulo(),
                                    Conf.LOG_TIPO_ARTICULO, "No se logr\u00F3 realizar " + tipoIngreso + " del art\u00EDculo "
                                            + obj.getIdArticulo() + " dado que no existe en el Sistema de SIDRA.",
                                    ""));
                        }
                    }

                    if (obj.getTipo().equalsIgnoreCase(tipoSalida)) {
                        if (lstExiste.get(0).intValue() == 1) {
                            if (lstExiste.get(1).intValue() > 0) {
                                if (lstExiste.get(1).intValue() < new BigDecimal(obj.getCantidad()).intValue()) {
                                    log.trace("Salida no v\u00E9lida");
                                    // si la cantidad a egresar excede de la disponible articulo no es insertado
                                    artDemasiadaExistencia.setRazon("Los artículos siguientes exceden de la cantidad disponible para egresar.");
                                    ArticulosIngresoSalida objNoExiste = new ArticulosIngresoSalida();
                                    objNoExiste.setIdArticulo(obj.getIdArticulo());
                                    objNoExiste.setTipo(tipoSalida);
                                    objNoExiste.setCantidadDisponible("" + lstExiste.get(1));
                                    objNoExiste.setCantidad(obj.getCantidad());
                                    listanoValida2.add(objNoExiste);
                                    razonResultado2 += " Tipo Operaci\u00F3n: " + tipoSalida + ", Articulo: " + obj.getIdArticulo() + ".";

                                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_INGRESO_PROMO,
                                            servicioPost, obj.getIdArticulo(), Conf.LOG_TIPO_ARTICULO,
                                            "No se logr\u00F3 realizar " + tipoSalida + " del art\u00EDculo "
                                                    + obj.getIdArticulo() + " dado que no existe en el inventario.",
                                            ""));
                                } else if (lstExiste.get(1).intValue() == new BigDecimal(obj.getCantidad()).intValue()) {
                                    // si la cantidad es igual se elimina el registro
                                    log.trace("elimina registro");
                                    OperacionIngresoSalida.deleteInv(conn, obj.getIdArticulo(), estadoDisponible,
                                            obj.getIdBodegaVirtual(), tipoInv, objDatos.getCodArea(), ID_PAIS);
                                    listaHistorico.add(armaHistorico(obj, objDatos.getUsuario(), estadoAlta, tipoInv, tSalida,ID_PAIS));
                                    
                                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_SALIDA_PROMO,
                                            servicioPost, obj.getIdArticulo(),
                                            Conf.LOG_TIPO_ARTICULO,
                                            "Se registr\u00F3 salida de " + obj.getCantidad() + " art\u00EDculo(s).", ""));
                                } else if (lstExiste.get(1).intValue() > new BigDecimal(obj.getCantidad()).intValue()) {
                                    // si la cantidad es menor a la disponible solo se resta del inventario
                                    log.trace("Actualiza salida");
                                    OperacionIngresoSalida.updateCantInventario(conn, obj.getIdArticulo(),
                                            new BigDecimal(obj.getCantidad()).intValue(), estadoDisponible,
                                            obj.getIdBodegaVirtual(), tipoInv, objDatos.getUsuario(), 2, objDatos.getCodArea(), ID_PAIS);
                                    listaHistorico.add(armaHistorico(obj, objDatos.getUsuario(), estadoAlta, tipoInv, tSalida,ID_PAIS));
                                    
                                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_SALIDA_PROMO,
                                            servicioPost, obj.getIdArticulo(),
                                            Conf.LOG_TIPO_ARTICULO,
                                            "Se registr\u00F3 salida de " + obj.getCantidad() + " art\u00EDculo(s).", ""));
                                }
                            } else {
                                artnoHayInv.setRazon("Los art\u00EDculos siguientes no existen en inventario.");
                                ArticulosIngresoSalida objNoExiste = new ArticulosIngresoSalida();
                                objNoExiste.setIdArticulo(obj.getIdArticulo());
                                objNoExiste.setTipo(tipoSalida);
                                objNoExiste.setCantidadDisponible("" + lstExiste.get(1));
                                objNoExiste.setCantidad(obj.getCantidad());
                                listanoValida3.add(objNoExiste);
                                razonResultado3 += " Tipo Operaci\u00F3n: " + tipoSalida + ", Articulo: " + obj.getIdArticulo() + ".";
                                
                                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_INGRESO_PROMO,
                                        servicioPost, obj.getIdArticulo(),
                                        Conf.LOG_TIPO_ARTICULO, "No se logr\u00F3 realizar " + tipoSalida + " del art\u00EDculo "
                                                + obj.getIdArticulo() + " dado que no existe en el inventario.",
                                        ""));
                            }
                        } else {
                            artNoExisten.setRazon("Los art\u00EDculos siguientes no existen en el Sistema de SIDRA.");
                            ArticulosIngresoSalida objNoExiste = new ArticulosIngresoSalida();
                            objNoExiste.setIdArticulo(obj.getIdArticulo());
                            objNoExiste.setTipo(tipoSalida);
                            listanoValida.add(objNoExiste);
                            razonResultado1 += " Tipo Operaci\u00F3n: " + tipoSalida + ", Articulo: " + obj.getIdArticulo() + ".";
                            
                            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_INGRESO_PROMO,
                                    servicioPost, obj.getIdArticulo(),
                                    Conf.LOG_TIPO_ARTICULO, "No se logr\u00F3 realizar " + tipoSalida + " del art\u00EDculo "
                                            + obj.getIdArticulo() + " dado que no existe en el Sistema de SIDRA.",
                                    ""));
                        }
                    }
                }

                if (objRespuesta == null) {
                    String razon = "";
                    if (!(listanoValida.isEmpty() && listanoValida2.isEmpty() && listanoValida3.isEmpty())) {
                        razon = "Los siguientes art\u00EDculos no pudieron operarse correctamente.";
                    }

                    int totalnoprocesados = 0;
                    totalnoprocesados = listanoValida.size() + listanoValida2.size() + listanoValida3.size();
                    log.trace("TOTAL DE ARTICULOS NO PROCESADOS:" + totalnoprocesados);
                    if (totalnoprocesados == objDatos.getArticulos().size()) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_HIZO_TRANSACCION_60, null,
                                this.getClass().toString(), metodo, razon, objDatos.getCodArea());

                    } else {
                        if (!listaHistorico.isEmpty()) {
                            insertaHistorico(conn, listaHistorico);
                        }

                        objRespuesta = getMensaje(Conf_Mensajes.OK_TRANSACCION14, null, null, null, razon, objDatos.getCodArea());
                    }
                }

                // inserta log
                if (!"".equals(razonResultado1)) {
                    razonFinal += " Los art\u00EDculos siguientes no existen en el Sistema de SIDRA." + razonResultado1;
                } else if (!"".equals(razonResultado2)) {
                    razonFinal += " Los art\u00EDculos siguientes exceden de la cantidad disponible para egresar." + razonResultado2;
                } else if (!"".equals(razonResultado3)) {
                    razonFinal += " Los art\u00EDculos siguientes no existen en inventario." + razonResultado3;
                }

                conn.commit();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());

                try {
                    conn.rollback();

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_INGRESO_PROMO, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de Ingreso/Salida de promocionales.",
                            e.getMessage()));
                } catch (SQLException e1) {
                    log.error(e1.getMessage(), e1);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_INGRESO_PROMO, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO,
                            "Problema al realizar el rollback en el servicio de Ingreso/Salida de promocionales.",
                            e1.getMessage()));
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_INGRESO_PROMO, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de Ingreso/Salida de promocionales.",
                        e.getMessage()));

            } finally {
                DbUtils.closeQuietly(conn);

                respuestaWS.setRespuesta(objRespuesta);
                if (!listanoValida.isEmpty()) {
                    artNoExisten.setArticulos(listanoValida);
                    lista.add(artNoExisten);
                }
                if (!listanoValida2.isEmpty()) {
                    artDemasiadaExistencia.setArticulos(listanoValida2);
                    lista.add(artDemasiadaExistencia);
                }
                if (!listanoValida3.isEmpty()) {
                    artnoHayInv.setArticulos(listanoValida3);
                    lista.add(artnoHayInv);
                }
                respuestaWS.setDatosIncorrectos(lista);
                // inserta log
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_INGRESO_SALIDA_PROMO, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, objRespuesta.getDescripcion() + razonFinal,
                        objRespuesta.getExcepcion()));
            }
        } else {
            respuestaWS.setRespuesta(objRespuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_INGRESO_PROMO, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
        }

        UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());

        return respuestaWS;
    }

    /**
     * M\u00E9todo para armar objeto de historico de inventario
     * 
     * @param conn
     * @param obj
     * @param usuario
     * @param estado
     * @param tipoInv
     * @return
     */
    public HistoricoInv armaHistorico(ArticulosIngresoSalida obj, String usuario, String estado, String tipoInv,
            BigDecimal idTransaccion, BigDecimal idPais) {
        HistoricoInv objHist = new HistoricoInv();
        objHist.setArticulo(obj.getIdArticulo());
        objHist.setBodega_origen(new BigDecimal(obj.getIdBodegaVirtual()));
        objHist.setCantidad(new BigDecimal(obj.getCantidad()));
        objHist.setCreado_por(usuario);
        objHist.setTipo_inv(tipoInv);
        objHist.setEstado(estado);
        objHist.setTcsctipotransaccionid(idTransaccion);
        objHist.setSerie(null);
        objHist.setSerie_asociada(null);
        objHist.setTcScCatPaisId(idPais);
        return objHist;
    }
}
