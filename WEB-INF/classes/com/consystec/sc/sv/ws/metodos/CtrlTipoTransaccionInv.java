package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.tipotransaccion.InputTransaccionInv;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.transaccion.OutputTransaccionInv;
import com.consystec.sc.sv.ws.operaciones.OperacionTransaccion;
import com.consystec.sc.sv.ws.orm.TipoTransaccionInv;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author susana barrios Consystec 2015
 */
public class CtrlTipoTransaccionInv extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlTipoTransaccionInv.class);
    private static String servicioGet = Conf.LOG_GET_TIPO_TRANSACCION;
    private static String servicioPost = Conf.LOG_POST_TIPO_TRANSACCION;
    private static String servicioPut = Conf.LOG_PUT_TIPO_TRANSACCION;

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputTransaccionInv objDatos, int tipo) {
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";
        
        if (tipo == 1 || tipo == 2) {
            if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, this.getClass().toString(), null, metodo,
                        null, objDatos.getCodArea());
            }
            if (objDatos.getTipoAfecta() == null ||  "".equals(objDatos.getTipoAfecta().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPOAFECTA_NULO_82, this.getClass().toString(), null,
                        metodo, null, objDatos.getCodArea());
            }

            if (objDatos.getDescripcion() == null ||  "".equals(objDatos.getDescripcion().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_DESCRIPCION_NULO_69, this.getClass().toString(), null,
                        metodo, null, objDatos.getCodArea());
            }
            if (objDatos.getTipoMovimiento() == null ||  "".equals(objDatos.getTipoMovimiento().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPOMOV_NULO_83, this.getClass().toString(), null, metodo,
                        null, objDatos.getCodArea());
            }
        }

        if (tipo == 1 &&  (objDatos.getCodigoTransaccion() == null ||  "".equals(objDatos.getCodigoTransaccion().trim()))) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODTRANSACCION_NULO_84, this.getClass().toString(), null,
                        metodo, null, objDatos.getCodArea());

        }
        if (objRespuesta != null) {
            log.trace("resultado:" + objRespuesta.getDescripcion());
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para registrar TipoTransaccionInvs en SIDRA los cuales ser\u00E9n
     * utilizados por vendedores
     * 
     * @param objDatos
     * @return
     */
    public OutputTransaccionInv creaTipoTransaccionInv(InputTransaccionInv objDatos) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        OutputTransaccionInv respuesta = new OutputTransaccionInv();
        Respuesta objRespuesta = new Respuesta();
        String estadoAlta = "";
        BigDecimal existe = null;
        BigDecimal idTipoTransaccionInv = null;
        Connection conn = null;
        String metodo = "creaTipoTransaccionInv";
        TipoTransaccionInv objNuevo = new TipoTransaccionInv();
        objRespuesta = validarDatos(objDatos, 1);

        if (objRespuesta == null) {
            try {
                conn = getConnRegional();
                BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
                estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());

                // verificando si el codigo TipoTransaccionInv o numero de telefono ingresados ya existen
                existe = OperacionTransaccion.existeCodTipoTransaccionInv(conn, objDatos.getCodigoTransaccion(),
                        estadoAlta, new BigDecimal(0), ID_PAIS);

                if (existe.intValue() > 0) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODTRANSACCION_EXISTE_85, null,
                            this.getClass().toString(), metodo, "", objDatos.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_TIPO_TRANSACCION, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                            objRespuesta.getDescripcion()));
                } else {
                    // si no existen valores ingresados se procede a insertar
                    objNuevo.setCodigo_transaccion(objDatos.getCodigoTransaccion());
                    objNuevo.setDescripcion(objDatos.getDescripcion());
                    objNuevo.setTipo_afecta(objDatos.getTipoAfecta());
                    objNuevo.setCreado_por(objDatos.getUsuario());
                    objNuevo.setEstado(estadoAlta);
                    objNuevo.setTipo_movimiento(objDatos.getTipoMovimiento());
                    objNuevo.setTcsccatpaisid(ID_PAIS);
                    idTipoTransaccionInv = OperacionTransaccion.insertTipoTransaccionInv(conn, objNuevo);
                    respuesta.setIdTransaccion(idTipoTransaccionInv.toString());

                    objRespuesta = getMensaje(Conf_Mensajes.OK_CREA_TRANSACCION_57, null, null, null, "", objDatos.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_TIPO_TRANSACCION, servicioPost,
                            idTipoTransaccionInv + "", Conf.LOG_TIPO_TIPO_TRANSACCION,
                            "Se cre\u00F3 un nuevo tipo de transacci\u00F3n con ID " + idTipoTransaccionInv + " y c\u00F3digo "
                                    + objDatos.getCodigoTransaccion().toUpperCase() + ".",
                            ""));
                }
            } catch (SQLException e) {
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
                log.error(e.getMessage(), e);
                
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_TIPO_TRANSACCION, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al crear nuevo tipo de transacci\u00F3n.", e.getMessage()));
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, "", objDatos.getCodArea());
                log.error(e.getMessage(), e);
                
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_TIPO_TRANSACCION, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al crear nuevo tipo de transacci\u00F3n.", e.getMessage()));
            } finally {
                DbUtils.closeQuietly(conn);
                respuesta.setRespuesta(objRespuesta);
                
                UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
            }
        } else {
            respuesta.setRespuesta(objRespuesta);
            
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_TIPO_TRANSACCION, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }

        return respuesta;
    }

    /**
     * M\u00E9todo para modificar TipoTransaccionInvs en SIDRA los cuales ser\u00E9n
     * utilizados por vendedores
     * 
     * @param objDatos
     * @return
     */
    public OutputTransaccionInv modificaTipoTransaccionInv(InputTransaccionInv objDatos) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        OutputTransaccionInv respuesta = new OutputTransaccionInv();
        Respuesta objRespuesta = new Respuesta();
        String estadoAlta = "";
        BigDecimal existe = null;
        Connection conn = null;
        String metodo = "modificaTipoTransaccionInv";
        TipoTransaccionInv objNuevo = new TipoTransaccionInv();
        objRespuesta = validarDatos(objDatos, 2);

        if (objRespuesta == null) {
            if (objDatos.getIdTipoTransaccion() == null || objDatos.getIdTipoTransaccion().equals("")) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDTIPOTRANSACCION_NULO_86, this.getClass().toString(), null,
                        metodo, null, objDatos.getCodArea());

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_TIPO_TRANSACCION, servicioPut, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
            } else if (objDatos.getEstado() == null || objDatos.getEstado().equals("")) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ESTADO_NULO_11, this.getClass().toString(), null, metodo,
                        null, objDatos.getCodArea());

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_TIPO_TRANSACCION, servicioPut,
                        objDatos.getIdTipoTransaccion(), Conf.LOG_TIPO_TIPO_TRANSACCION,
                        "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
            }

            if (objRespuesta == null) {
                try {
                    conn = getConnRegional();
                    BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
                    estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());

                    // verificando si el codigo TipoTransaccionInv o numero de telefono ingresados ya existen
                    existe = OperacionTransaccion.existeCodTipoTransaccionInv(conn, "", estadoAlta,
                            new BigDecimal(objDatos.getIdTipoTransaccion()), ID_PAIS);

                    if (existe.intValue() > 0) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODTRANSACCION_EXISTE_85, null,
                                this.getClass().toString(), metodo, "", objDatos.getCodArea());

                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_TIPO_TRANSACCION, servicioPut,
                                objDatos.getIdTipoTransaccion(), Conf.LOG_TIPO_TIPO_TRANSACCION,
                                "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
                    } else {
                        // si no existen valores ingresados se procede a modificar

                        objNuevo.setDescripcion(objDatos.getDescripcion());
                        objNuevo.setModificado_por(objDatos.getUsuario());
                        objNuevo.setEstado(objDatos.getEstado().toUpperCase());
                        objNuevo.setTipo_afecta(objDatos.getTipoAfecta());
                        objNuevo.setTipo_movimiento(objDatos.getTipoMovimiento());
                        objNuevo.setTcsctipotransaccionid(new BigDecimal(objDatos.getIdTipoTransaccion()));
                        objNuevo.setTcsccatpaisid(ID_PAIS);
                        OperacionTransaccion.updateTipoTransaccionInv(conn, objNuevo);

                        objRespuesta = getMensaje(Conf_Mensajes.OK_MOD_TRANSACCION_58, null, null, null, "", objDatos.getCodArea());

                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_TIPO_TRANSACCION, servicioPut,
                                objDatos.getIdTipoTransaccion(), Conf.LOG_TIPO_TIPO_TRANSACCION,
                                "Se modificaron datos del tipo de transacci\u00F3n con ID " + objDatos.getIdTipoTransaccion()
                                        + ".",
                                ""));

                    }
                } catch (SQLException e) {
                    objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
                    log.error(e.getMessage(), e);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_TIPO_TRANSACCION, servicioPut,
                            objDatos.getIdTipoTransaccion(), Conf.LOG_TIPO_TIPO_TRANSACCION,
                            "Problema al crear nuevo tipo de transacci\u00F3n.", e.getMessage()));

                } catch (Exception e) {
                    objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                            metodo, "", objDatos.getCodArea());
                    log.error(e.getMessage(), e);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_TIPO_TRANSACCION, servicioPut,
                            objDatos.getIdTipoTransaccion(), Conf.LOG_TIPO_TIPO_TRANSACCION,
                            "Problema al crear nuevo tipo de transacci\u00F3n.", e.getMessage()));
                } finally {
                    DbUtils.closeQuietly(conn);
                    respuesta.setRespuesta(objRespuesta);

                    UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
                }
            }
        }
        respuesta.setRespuesta(objRespuesta);

        return respuesta;

    }

    /**
     * M\u00E9todo para obtener informaci\u00F3n de TipoTransaccionInvs
     ***/
    public OutputTransaccionInv getTipoTransaccionInv(InputTransaccionInv objDatos) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        OutputTransaccionInv respuesta = new OutputTransaccionInv();
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        List<InputTransaccionInv> lstTipoTransaccionInv = new ArrayList<InputTransaccionInv>();
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getTipoTransaccionInv";
        Connection conn = null;

        log.trace("entra a servicio local get TipoTransaccionInvs");

        if ((!(objDatos.getIdTipoTransaccion() == null || "".equals(objDatos.getIdTipoTransaccion().trim())))&& isNumeric(objDatos.getIdTipoTransaccion())){
                log.trace("entra a filtro id TipoTransaccionInv");
                lstFiltros.add(new Filtro(TipoTransaccionInv.CAMPO_TCSCTIPOTRANSACCIONID, Filtro.EQ,
                        objDatos.getIdTipoTransaccion()));
        }

        if (!(objDatos.getCodigoTransaccion() == null || "".equals(objDatos.getCodigoTransaccion().trim()))) {
            log.trace("entra a filtro numTelefono");
            lstFiltros.add(new Filtro("UPPER(" + TipoTransaccionInv.CAMPO_CODIGO_TRANSACCION + ")", Filtro.EQ,
                    "'" + objDatos.getCodigoTransaccion().toUpperCase() + "'"));
        }
        
        if (!(objDatos.getDescripcion() == null || "".equals(objDatos.getDescripcion().trim()))) {
            log.trace("entra a filtro descripcion");
            lstFiltros.add(new Filtro("UPPER(" + TipoTransaccionInv.CAMPO_DESCRIPCION + ")", Filtro.LIKE,
                    "'%" + objDatos.getDescripcion().toUpperCase() + "%'"));
        }

        if (!(objDatos.getEstado() == null || "".equals(objDatos.getEstado().trim()))) {
            log.trace("entra a filtro  estado");
            lstFiltros.add(new Filtro("UPPER(" + TipoTransaccionInv.CAMPO_ESTADO + ")", Filtro.EQ,
                    "'" + objDatos.getEstado().toUpperCase() + "'"));
        }
        
        if (!(objDatos.getTipoMovimiento() == null || "".equals(objDatos.getTipoMovimiento().trim()))) {
            log.trace("entra a filtro  codigo TipoTransaccionInv");
            lstFiltros.add(new Filtro("UPPER(" + TipoTransaccionInv.CAMPO_TIPO_MOVIMIENTO + ")", Filtro.EQ,
                    "'" + objDatos.getTipoMovimiento().toUpperCase() + "'"));
        }

        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
            lstFiltros.add(new Filtro(TipoTransaccionInv.CAMPO_TCSCCATPAISID, Filtro.EQ,
                    ID_PAIS));
            lstTipoTransaccionInv = OperacionTransaccion.getTipoTransaccionInv(conn, lstFiltros);
            if (lstTipoTransaccionInv.isEmpty()) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo,
                        null, objDatos.getCodArea());
            } else {
                respuesta.setTiposTransaccion(lstTipoTransaccionInv);
                objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
            }

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de tipos de transacci\u00F3n.", ""));
        } catch (SQLException e) {
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de tipos de transacci\u00F3n.", e.getMessage()));
        } catch (Exception e) {
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    "", objDatos.getCodArea());
            log.error(e.getMessage(), e);
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de tipos de transacci\u00F3n.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            respuesta.setRespuesta(objRespuesta);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }
        return respuesta;
    }
}
