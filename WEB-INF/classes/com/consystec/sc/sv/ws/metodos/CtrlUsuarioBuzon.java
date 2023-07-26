package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.buzon.InputUsuarioBuzon;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.buzon.OutputUsuarioBuzon;
import com.consystec.sc.sv.ws.operaciones.OperacionUsuarioBuzon;
import com.consystec.sc.sv.ws.orm.UsuarioBuzon;
import com.consystec.sc.sv.ws.orm.BuzonSidra;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class CtrlUsuarioBuzon extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlUsuarioBuzon.class);
    private static String servicioGet = Conf.LOG_GET_USUARIO_BUZON;
    private static String servicioPost = Conf.LOG_POST_USUARIO_BUZON;
    private static String servicioPut = Conf.LOG_PUT_USUARIO_BUZON;

    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDe como recurso al
     * servicio.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return Respuesta
     * @throws SQLException
     */
    public Respuesta validarInput(Connection conn, InputUsuarioBuzon input, int metodo, BigDecimal ID_PAIS) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        log.debug("Validando datos...");

        if (metodo == Conf.METODO_GET) {
            if (input.getIdBuzon() != null && !isNumeric(input.getIdBuzon())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBUZON_NUM_331, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }
            if (input.getIdVendedor() != null && !isNumeric(input.getIdVendedor())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDUSUARIO_NUM_322, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }
        }

        if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
            if (input.getIdUsuarioBuzon() == null || "".equals(input.getIdUsuarioBuzon())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDUSUARIOBUZON_357, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdUsuarioBuzon())) {
                log.trace("ID Usuario Buzon: " + input.getIdUsuarioBuzon());
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDUSUARIOBUZON_NUM_358, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }
        }

        if (metodo == Conf.METODO_POST || metodo == Conf.METODO_PUT) {
            if (input.getIdBuzon() == null || "".equals(input.getIdBuzon())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBUZON_330, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdBuzon())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBUZON_NUM_331, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else {
                // Datos buzon
                List<Filtro> condiciones = new ArrayList<Filtro>();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_TCSCBUZONID,
                        input.getIdBuzon()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_TCSCCATPAIS_ID,
                        ID_PAIS.toString()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BuzonSidra.CAMPO_ESTADO,
                        UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea())));

                String existencia;
                try {
                    existencia = UtileriasBD.verificarExistencia(conn, BuzonSidra.N_TABLA, condiciones);
                    if (new Integer(existencia) <= 0) {
                        log.error("No existe el Buzon.");
                        datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_EXISTE_BUZON_332, null, nombreClase,
                                nombreMetodo, null, input.getCodArea()).getDescripcion();
                        flag = true;
                    }
                } catch (SQLException e) {
                    log.error("Error al verificar existencia del Buz\u00F3n.", e);
                    r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_VERIFICAR_BUZON_333, null, nombreClase,
                            nombreMetodo, null, input.getCodArea());
                }
            }

            if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            }
            
            if (input.getNombreVendedor() == null || "".equals(input.getNombreVendedor().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_VEND_NOMBRE_866, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            }

            if (input.getUsuarioVendedor() == null || "".equals(input.getUsuarioVendedor().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_VEND_USUARIO_867, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            }
        }

        if (metodo == Conf.METODO_POST) {
            if (input.getIdVendedor() == null || "".equals(input.getIdVendedor())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_137, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdVendedor())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }
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
     * @return
     */
    public static String[] obtenerCamposGetPost(int metodo) {
        if (metodo == Conf.METODO_GET) {
            String campos[] = {
                UsuarioBuzon.CAMPO_TCSCUSUARIOBUZONID,
                UsuarioBuzon.CAMPO_TCSCBUZONID,
                UsuarioBuzon.CAMPO_SECUSUARIOID,
                UsuarioBuzon.CAMPO_NOMBRE_VEND,
                UsuarioBuzon.CAMPO_USUARIO_VEND,
                UsuarioBuzon.CAMPO_ESTADO,
                UsuarioBuzon.CAMPO_CREADO_EL,
                UsuarioBuzon.CAMPO_CREADO_POR,
                UsuarioBuzon.CAMPO_MODIFICADO_EL,
                UsuarioBuzon.CAMPO_MODIFICADO_POR
            };
            return campos;
        } else {
            String campos[] = {
                UsuarioBuzon.CAMPO_TCSCUSUARIOBUZONID,
                UsuarioBuzon.CAMPO_TCSCCATPAISID,
                UsuarioBuzon.CAMPO_TCSCBUZONID,
                UsuarioBuzon.CAMPO_SECUSUARIOID,
                UsuarioBuzon.CAMPO_NOMBRE_VEND,
                UsuarioBuzon.CAMPO_USUARIO_VEND,
                UsuarioBuzon.CAMPO_ESTADO,
                UsuarioBuzon.CAMPO_CREADO_EL,
                UsuarioBuzon.CAMPO_CREADO_POR,
            };
            return campos;
        }
    }
    
    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
     * 
     * @param input
     * @param sequencia
     * @param estadoAlta 
     * @return inserts
     */
    public static List<String> obtenerInsertsPost(InputUsuarioBuzon input, String sequencia, String estadoAlta, BigDecimal ID_PAIS) {
        List<String> inserts = new ArrayList<String>();
        String valores = "";
        
        valores = "("
            + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, ID_PAIS.toString(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdBuzon(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdVendedor(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getNombreVendedor(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuarioVendedor(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
        + ") ";
        inserts.add(valores);
        
        return inserts;
    }
    
    /**
     * Funci\u00F3n que indica los campos que se utilizar\u00E9n para modificaciones  
     * al recurso en los m\u00E9todos PUT y DELETE.
     * 
     * @param input
     * @param metodo
     * @return campos
     * @throws SQLException 
     */
    public static String[][] obtenerCamposPutDel(InputUsuarioBuzon input, int metodo) throws SQLException {
        if (metodo == Conf.METODO_DELETE) {
            String campos[][] = {
                { UsuarioBuzon.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, UtileriasJava.getConfig(null, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, input.getCodArea())) },
                { UsuarioBuzon.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                { UsuarioBuzon.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
            };
            return campos;
        } else {
            String campos[][] = {               
                { UsuarioBuzon.CAMPO_TCSCBUZONID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getIdBuzon()) },
                { UsuarioBuzon.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                { UsuarioBuzon.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
            };
            return campos;
        }
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes
     * consultas seg\u00FAn el m\u00E9todo que se trabaje.
     * 
     * @param input
     * @param metodo
     * @return condiciones
     */
    public static List<Filtro> obtenerCondiciones(InputUsuarioBuzon input, int metodo, BigDecimal ID_PAIS) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if ((metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE) && !"".equals(input.getIdUsuarioBuzon())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                        UsuarioBuzon.CAMPO_TCSCUSUARIOBUZONID, input.getIdUsuarioBuzon()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                        UsuarioBuzon.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
        }

        if (metodo == Conf.METODO_GET) {
            if (input.getIdUsuarioBuzon() != null && !"".equals(input.getIdUsuarioBuzon())) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                            UsuarioBuzon.CAMPO_TCSCUSUARIOBUZONID, input.getIdUsuarioBuzon()));
            }
            
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                    UsuarioBuzon.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
            
            if (input.getIdBuzon() != null && !"".equals(input.getIdBuzon())) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                            UsuarioBuzon.CAMPO_TCSCBUZONID, input.getIdBuzon()));
            }
            if (input.getIdVendedor() != null && !"".equals(input.getIdVendedor())) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                            UsuarioBuzon.CAMPO_SECUSUARIOID, input.getIdVendedor()));
            }
            if (input.getEstado() != null && !"".equals(input.getEstado())) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, UsuarioBuzon.CAMPO_ESTADO,
                            input.getEstado()));
            }
        }
        return condiciones;
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las consultas de
     * verificaci\u00F3n de existencia del recurso.
     * 
     * @param input
     * @param metodo
     * @param estadoAlta
     * @return condiciones
     */
    public static List<Filtro> obtenerCondicionesExistencia(InputUsuarioBuzon input, int metodo,
            String estadoAlta, BigDecimal ID_PAIS) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (metodo == Conf.METODO_POST && !"".equals(input.getIdVendedor())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_OR, UsuarioBuzon.CAMPO_SECUSUARIOID,
                        input.getIdVendedor()));
        }

        if (metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                    UsuarioBuzon.CAMPO_TCSCUSUARIOBUZONID, input.getIdUsuarioBuzon()));
        }

        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, UsuarioBuzon.CAMPO_TCSCCATPAISID,
                ID_PAIS.toString()));

        if (metodo != Conf.METODO_PUT && metodo != Conf.METODO_DELETE) {
            condiciones.add(
                    UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, UsuarioBuzon.CAMPO_ESTADO, estadoAlta));
        }

        return condiciones;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     * @param metodo
     * @return respuestaUsuarioBuzon
     */
    public OutputUsuarioBuzon getDatos(InputUsuarioBuzon input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<Respuesta> respuesta = new ArrayList<Respuesta>();

        Respuesta r = new Respuesta();
        OutputUsuarioBuzon output = null;

        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            r = validarInput(conn, input, metodo, ID_PAIS);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!r.getDescripcion().equals("OK")) {
                respuesta.add(r);
                output = new OutputUsuarioBuzon();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_USUARIO_BUZON, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            if (metodo == Conf.METODO_GET) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionUsuarioBuzon.doGet(conn, input, metodo, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de usuarios asignados a buzones.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputUsuarioBuzon();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de usuarios asignados a buzones.",
                            e.getMessage()));
                }

            } else if (metodo == Conf.METODO_POST) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
                try {
                    output = OperacionUsuarioBuzon.doPost(conn, input, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_USUARIOBUZON_59) {
                        listaLog.add(
                                ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_USUARIO_BUZON,
                                        servicioPost, "0", Conf.LOG_TIPO_NINGUNO, "Se asign\u00F3 el usuario ID "
                                                + input.getIdVendedor() + " al buz\u00F3n ID " + input.getIdBuzon() + ".",
                                        ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_USUARIO_BUZON, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema al asignar usuario a buz\u00F3n.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputUsuarioBuzon();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_USUARIO_BUZON, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al asignar usuario a buz\u00F3n.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT o DELETE
                try {
                    output = OperacionUsuarioBuzon.doPutDel(conn, input, metodo, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_MOD_USUARIOBUZON_60) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_USUARIO_BUZON, servicioPut,
                                input.getIdUsuarioBuzon(), Conf.LOG_TIPO_USUARIO_BUZON,
                                "Se modificaron datos de la asociaci\u00F3n de usuario-buz\u00F3n con ID "
                                        + input.getIdUsuarioBuzon() + ".",
                                ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_USUARIO_BUZON, servicioPut,
                                input.getIdUsuarioBuzon(), Conf.LOG_TIPO_USUARIO_BUZON,
                                "Problema al modificar datos de la asociaci\u00F3n de usuario-buz\u00F3n.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputUsuarioBuzon();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_USUARIO_BUZON, servicioPut, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al modificar datos de la asociaci\u00F3n de usuario-buz\u00F3n.",
                            e.getMessage()));
                }
            }

        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputUsuarioBuzon();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_USUARIO_BUZON, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de asignaci\u00F3n de usuarios a buzones.",
                    e.getMessage()));

        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
