package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.visita.InputGetVisita;
import com.consystec.sc.ca.ws.input.visita.InputVisita;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.visita.OutputVisita;
import com.consystec.sc.sv.ws.operaciones.OperacionVisita;
import com.consystec.sc.sv.ws.orm.HistoricoInv;
import com.consystec.sc.sv.ws.orm.NumRecarga;
import com.consystec.sc.sv.ws.orm.Visita;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlVisitaGestion extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlVisitaGestion.class);
    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_VISITA;
    private static String servicioPost = Conf.LOG_POST_VISITA;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    /***
     * Validando que no vengan par\u00E9metros nulos
     * @param gestionNoVenta 
     * 
     * @throws ParseException
     */
    public Respuesta validarDatos(InputVisita objDatos, String gestionNoVenta) {
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";
        COD_PAIS=objDatos.getCodArea();
        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
        }

        if (objDatos.getIdJornada() == null ||  "".equals(objDatos.getIdJornada().trim())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDJORNADA_NULO_89, this.getClass().toString(), null, metodo,
                    null, objDatos.getCodArea());
        }

        if (objDatos.getIdPuntoVenta() == null ||  "".equals(objDatos.getIdPuntoVenta().trim())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDPDV_VACIO_8, this.getClass().toString(),null, metodo, null, objDatos.getCodArea());
        }

        if (objDatos.getIdVendedor() == null ||  "".equals(objDatos.getIdVendedor().trim())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_VENDEDOR_NULO_38, this.getClass().toString(), null, metodo,
                    null, objDatos.getCodArea());
        }

        if (objDatos.getGestion() == null ||  "".equals(objDatos.getGestion().trim())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_GESTION_NULO_90, this.getClass().toString(), null, metodo,
                    null, objDatos.getCodArea());
        }

        if (objDatos.getGestion().equalsIgnoreCase(gestionNoVenta)&&  (objDatos.getCausa() == null ||  "".equals(objDatos.getCausa().trim()))) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_CAUSA_872, this.getClass().toString(), null,
                        metodo, null, objDatos.getCodArea());
        }

        if (objDatos.getLongitud() == null ||  "".equals(objDatos.getLongitud().trim())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONG_NULO_26, this.getClass().toString(),null, metodo, null, objDatos.getCodArea());
        } else if (!isValido(objDatos.getLongitud())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONG_INVALIDA_28, this.getClass().toString(), null, metodo,
                    null, objDatos.getCodArea());
        }

        if (objDatos.getLatitud() == null ||  "".equals(objDatos.getLatitud().trim())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_LATITUD_NULO_25, this.getClass().toString(), null, metodo,
                    null, objDatos.getCodArea());
        } else if (!isValido(objDatos.getLatitud())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_LAT_INVALIDA_29, this.getClass().toString(), null, metodo,
                    null, objDatos.getCodArea());
        }

        if (objRespuesta != null) {
            log.trace("resultado:" + objRespuesta.getDescripcion());
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para registrar las visitas que realiza el vendedor a puntos de
     * venta
     * 
     * @param objDatos
     * @return
     */
    public OutputVisita registraVisita(InputVisita objDatos) {
        listaLog = new ArrayList<LogSidra>();

        Respuesta objRespuesta = new Respuesta();
        OutputVisita respuestaVisita = new OutputVisita();
        List<BigDecimal> lista = new ArrayList<BigDecimal>();
        BigDecimal idVisita = null;
        Connection conn = null;
        String metodo = "registraVisita";
        Visita objNuevo = new Visita();
        String gestionVenta = "";
        String gestionNoVenta = "";
        String gestionObservacion = "";
        String estadoAlta = "";
        String estadoActivo = "";
        String estadoIniciada = "";
        SimpleDateFormat FORMATO_TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            conn = getConnRegional();
           ID_PAIS = getIdPais(conn, objDatos.getCodArea());
            gestionNoVenta = UtileriasJava.getConfig(conn, Conf.TIPO_GESTION_GRUPO, Conf.GESTION_NO_VENTA, objDatos.getCodArea());
            objRespuesta = validarDatos(objDatos, gestionNoVenta);

            if (objRespuesta == null) {
                estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());
                estadoIniciada = UtileriasJava.getConfig(conn, Conf.GRUPO_JORNADA_ESTADOS, Conf.JORNADA_ESTADO_INICIADA, objDatos.getCodArea());
                gestionVenta = UtileriasJava.getConfig(conn, Conf.TIPO_GESTION_GRUPO, Conf.GESTION_VENTA, objDatos.getCodArea());
                estadoActivo = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_ESTADOS_PDV, Conf.ESTADO_ACTIVO, objDatos.getCodArea());
                gestionObservacion = UtileriasJava.getConfig(conn, Conf.TIPO_GESTION_GRUPO, Conf.GESTION_OBSERVACION, objDatos.getCodArea());

                if (objDatos.getGestion().equalsIgnoreCase(gestionVenta)
                        || objDatos.getGestion().equalsIgnoreCase(gestionNoVenta)
                        || objDatos.getGestion().equalsIgnoreCase(gestionObservacion)) {
                    // verificando si el codigo dispositivo o numero de telefono ingresados ya existen
                    lista = OperacionVisita.existeJornadaPDV(conn, objDatos, estadoAlta,  estadoIniciada, estadoActivo, ID_PAIS);

                    // verificando que el id de jornada ingresado exista
                    if (lista.get(0).intValue() == 0) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_JORNADA_NOEXISTE_93, null,
                                this.getClass().toString(), metodo, "", objDatos.getCodArea());

                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_VISITA_GESTION, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                                objRespuesta.getDescripcion()));
                    }
                    // verificando que el id de vendedor ingresado coincida con el id de jornada ingresada
                    else if (lista.get(1).intValue() == 0) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_VENDEDOR_INVALIDO_PARAJORNADA_92, null,
                                this.getClass().toString(), metodo, "", objDatos.getCodArea());

                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_VISITA_GESTION, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                                objRespuesta.getDescripcion()));
                    }
                    // verificando que el id de vendedor ingresado coincida con el id de pvd ingresado
                    else if (lista.get(2).intValue() == 0) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_VENDEDOR_INVALIDO_PARAPDV_91, null,
                                this.getClass().toString(), metodo, "", objDatos.getCodArea());

                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_VISITA_GESTION, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                                objRespuesta.getDescripcion()));
                    } else {
                        // si las validaciones son correctas se procede a insertar
                        Date fechaVisita = FORMATO_TIMESTAMP.parse(objDatos.getFecha());
                        objNuevo.setFecha(new Timestamp(fechaVisita.getTime()));
                        objNuevo.setGestion(objDatos.getGestion().toUpperCase());
                        objNuevo.setLatitud(objDatos.getLatitud());
                        objNuevo.setLongitud(objDatos.getLongitud());
                        objNuevo.setCausa(objDatos.getCausa());
                        objNuevo.setObservaciones(objDatos.getObservaciones());
                        objNuevo.setTcscpuntoventaid(new BigDecimal(objDatos.getIdPuntoVenta()));
                        objNuevo.setVendedor(new BigDecimal(objDatos.getIdVendedor()));
                        objNuevo.setCreado_por(objDatos.getUsuario());
                        objNuevo.setTcscjornadavendid(new BigDecimal(objDatos.getIdJornada()));
                        objNuevo.setTcsccatpaisid(ID_PAIS);
                        if (objDatos.getFolio() == null || "".equals(objDatos.getFolio())) {
                            objNuevo.setTcscventaid(null);
                        } else {
                            objNuevo.setTcscventaid(new BigDecimal(objDatos.getFolio()));
                        }

                        idVisita = OperacionVisita.insertVisita(conn, objNuevo);
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_OK_VISITA17, null, null, null, "", objDatos.getCodArea());

                        respuestaVisita.setIdVisita("" + idVisita);

                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_VISITA_GESTION, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Se registr\u00F3 visita del vendedor ID " + objDatos.getIdVendedor()
                                        + " al punto de venta ID " + objDatos.getIdPuntoVenta() + ".",
                                ""));
                    
                    }
                } else {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_GESTION_INVALIDA_94, null, this.getClass().toString(),
                            metodo, "", objDatos.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_VISITA_GESTION, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                            objRespuesta.getDescripcion()));
                }
            } else {
                respuestaVisita.setRespuesta(objRespuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_VISITA_GESTION, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

                UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
            }
        } catch (SQLException e) {
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_VISITA_GESTION, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al registrar nueva visita.", e.getMessage()));

        } catch (Exception e) {
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_VISITA_GESTION, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al registrar nueva visita.", e.getMessage()));

        } finally {
            DbUtils.closeQuietly(conn);
            respuestaVisita.setRespuesta(objRespuesta);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }

        return respuestaVisita;
    }

    /***
     * M\u00E9todo para obtener informaci\u00F3n de visitas
     */

    public OutputVisita getVisita(InputGetVisita objDatos) {
        listaLog = new ArrayList<LogSidra>();
        OutputVisita objRespuestaVisita = new OutputVisita();
        Respuesta objRespuesta = null;
        List<InputVisita> lstVisita = new ArrayList<InputVisita>();
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        String metodo = "getVisita";
        Connection conn = null;
        COD_PAIS=objDatos.getCodArea();
        // validando fechas
        if (!(objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim()))
                && !(objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(objDatos.getFechaInicio(), formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(objDatos.getFechaFin(), formatoFecha);

            if (fechaFin.before(fechaInicio)) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHAFIN_MENOR_62, null, this.getClass().toString(), metodo,
                        null, objDatos.getCodArea());
            }
        } else if (!(objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim()))
                && (objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, this.getClass().toString(), metodo,
                    null, objDatos.getCodArea());
        }

        if ((objDatos.getNumRecarga() != null && !objDatos.getNumRecarga().equals("")) && (!isNumeric(objDatos.getNumRecarga()) || objDatos.getNumRecarga().length() != Conf.LONG_NUMERO)) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONGNUM_INVALIDA_27, null, this.getClass().toString(),
                        metodo, null, objDatos.getCodArea());
        }

        if (objRespuesta == null) {
            try {
                conn = getConnRegional();
               ID_PAIS = getIdPais(conn, objDatos.getCodArea());
                lstFiltros = getFiltros(objDatos, conn);
                lstVisita = OperacionVisita.getVisita(conn, lstFiltros, objDatos.getCodArea());
                if (lstVisita.isEmpty()) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo,
                            null, objDatos.getCodArea());
                } else {
                    objRespuestaVisita.setVisitas(lstVisita);
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
                }

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de visitas.", ""));
                
            } catch (SQLException e) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
                log.error(e.getMessage(), e);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de visitas.", e.getMessage()));

            } catch (Exception e) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, "", objDatos.getCodArea());
                log.error(e.getMessage(), e);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de visitas.", e.getMessage()));
                
            } finally {
                DbUtils.closeQuietly(conn);

                UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
            }
        } else {
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
        }

        objRespuestaVisita.setRespuesta(objRespuesta);

        return objRespuestaVisita;
    }

    /**
     * M\u00E9todo para obtener los posibles filtros que puede tener la consulta
     * 
     * @param objDatos
     * @param conn 
     * @return
     * @throws SQLException 
     */
    public List<Filtro> getFiltros(InputGetVisita objDatos, Connection conn) throws SQLException {
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA);
        lstFiltros.add(new Filtro("V." + Visita.CAMPO_TCSCCATPAISID, Filtro.EQ, ID_PAIS));
        
        if (!(objDatos.getIdVendedor() == null || "".equals(objDatos.getIdVendedor().trim()))) {
            log.trace("entra a filtro ID VENDEDOR");
            lstFiltros.add(new Filtro("V." + Visita.CAMPO_VENDEDOR, Filtro.EQ, objDatos.getIdVendedor()));
        }

        if (!(objDatos.getIdPuntoVenta() == null || "".equals(objDatos.getIdPuntoVenta().trim()))) {
            log.trace("entra a filtro id PUNTO VENTA");
            lstFiltros.add(new Filtro("V." + Visita.CAMPO_TCSCPUNTOVENTAID, Filtro.EQ, objDatos.getIdPuntoVenta()));
        }

        if (!(objDatos.getIdJornada() == null || "".equals(objDatos.getIdJornada().trim()))) {
            log.trace("entra a filtro ID JORNADA");
            lstFiltros.add(new Filtro("V." + Visita.CAMPO_TCSCJORNADAVENDID, Filtro.EQ, objDatos.getIdJornada()));
        }

        if (!(objDatos.getGestion() == null || "".equals(objDatos.getGestion().trim()))) {
            log.trace("entra a filtro gestion");
            lstFiltros.add(new Filtro("UPPER(V." + Visita.CAMPO_GESTION + ")", Filtro.EQ,
                    "'" + objDatos.getGestion().toUpperCase() + "'"));
        }

        if (!(objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim()))
                && !(objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            log.trace("entra a filtro FECHAS");
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(objDatos.getFechaInicio(), formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(objDatos.getFechaFin(), formatoFecha);
            lstFiltros.add(new Filtro("TRUNC(V." + HistoricoInv.CAMPO_CREADO_EL + ")", Filtro.GTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaInicio) + "','dd/MM/YYYY')"));
            lstFiltros.add(new Filtro("TRUNC(V." + HistoricoInv.CAMPO_CREADO_EL + ")", Filtro.LTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaFin) + "','dd/MM/YYYY')"));
        }
        
        if (objDatos.getNumRecarga() != null && !objDatos.getNumRecarga().trim().equals("")) {
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());
            String tipoPDV = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELPDV, Conf.TIPO_PDV, objDatos.getCodArea());

            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, NumRecarga.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, NumRecarga.CAMPO_TIPO, tipoPDV));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, NumRecarga.CAMPO_NUM_RECARGA, objDatos.getNumRecarga()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, NumRecarga.CAMPO_ESTADO, estadoAlta));

            String idPDV = UtileriasBD.getOneRecord(conn, NumRecarga.CAMPO_IDTIPO, NumRecarga.N_TABLA, condiciones);
            lstFiltros.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, "V." + Visita.CAMPO_TCSCPUNTOVENTAID, idPDV));
        }

        return lstFiltros;
    }
}
