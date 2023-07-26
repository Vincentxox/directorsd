package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.cuenta.InputCuenta;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.cuenta.OutputCuenta;
import com.consystec.sc.sv.ws.operaciones.OperacionCuenta;
import com.consystec.sc.sv.ws.orm.Cuenta;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class CtrlCuenta extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlCuenta.class);
    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_CUENTA;
    private static String servicioPost = Conf.LOG_POST_CUENTA;
    private static String servicioPut = Conf.LOG_PUT_CUENTA;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al
     * servicio.
     * 
     * @param conn Objeto de tipo connetion con los datos de la conexi\u00F3n activa.
     * @param input Objeto con los datos enviados para validar.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @param estadoAlta Valor del estado de alta.
     * @param estadoBaja Valor del estado de baja.
     * 
     * @return respuesta Objeto con la respuesta en caso de error o nulo si todo esta correcto.
     */
    public Respuesta validarInput(Connection conn, InputCuenta input, int metodo, String estadoAlta,
            String estadoBaja) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        log.debug("Validando datos...");

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            return getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        if (metodo == Conf.METODO_PUT) {
            log.trace("ID Cuenta: " + input.getIdCuenta());
            if (input.getIdCuenta() == null || "".equals(input.getIdCuenta()) || !isNumeric(input.getIdCuenta())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDCUENTA_700, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }

            if (input.getEstado() == null || "".equals(input.getEstado().trim())) {
                return getMensaje(Conf_Mensajes.MSJ_ESTADO_NULO_11, null, nombreClase, nombreMetodo, null, input.getCodArea());
            } else {
                if (!input.getEstado().equalsIgnoreCase(estadoAlta) && !input.getEstado().equalsIgnoreCase(estadoBaja)) {
                    return getMensaje(Conf_Mensajes.MSJ_ESTADO_NOVALIDO_33, null, nombreClase, nombreMetodo, null, input.getCodArea());
                }
            }
        }

        if (metodo == Conf.METODO_POST || metodo == Conf.METODO_PUT) {
            if (input.getBanco() == null || "".equals(input.getBanco().trim())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_BANCO_701, null, nombreClase, nombreMetodo, null, input.getCodArea());
            } else {
                if (UtileriasJava.getCountConfigValor(conn, Conf.GRUPO_BANCOS, input.getBanco().toUpperCase(),
                        estadoAlta,input.getCodArea()) < 1) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_BANCO_TIPO_702, null, nombreClase, nombreMetodo,
                            null,input.getCodArea());
                }
            }

            if (input.getNoCuenta() == null || "".equals(input.getNoCuenta().trim())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NOCUENTA_703, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }

            if (input.getTipoCuenta() == null || "".equals(input.getTipoCuenta().trim())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPOCUENTA_704, null, nombreClase, nombreMetodo, null, input.getCodArea());
            } else {
                if (UtileriasJava.getCountConfigValor(conn, Conf.GRUPO_TIPO_CUENTA_BANCARIA, input.getTipoCuenta(),
                        estadoAlta,input.getCodArea()) < 1) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPOCUENTA_TIPO_705, null, nombreClase,
                            nombreMetodo, null, input.getCodArea());
                }
            }

            if (input.getNombreCuenta() == null || "".equals(input.getNombreCuenta().trim())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NOMBRE_CUENTA_706, null, nombreClase, nombreMetodo,
                        null,input.getCodArea());
            }
        }
        
        
        if (metodo == Conf.METODO_POST && (input.getIdDts()==null || "".equals(input.getIdDts().trim()))){
        	    return getMensaje(Conf_Mensajes.IDDTS_NULO_36, null, nombreClase, nombreMetodo,
                        null,input.getCodArea());
        	
        }

        if (metodo == Conf.METODO_GET) {
            if (input.getIdCuenta() != null && !"".equals(input.getIdCuenta()) && !isNumeric(input.getIdCuenta())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDCUENTA_700, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }

            if ((input.getBancosAsociados() != null && !"".equals(input.getBancosAsociados().trim())) && (!"0".equals(input.getBancosAsociados().trim()) && !"1".equals(input.getBancosAsociados().trim()))) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_BANCOASOCIADOS_709, null, nombreClase, nombreMetodo,
                            null,input.getCodArea());
            }
            
            if ((input.getNoCuenta()== null || "".equals(input.getNoCuenta().trim()))   &&(input.getIdDts()==null ||  "".equals(input.getIdDts().trim()))){
            	    return getMensaje(Conf_Mensajes.IDDTS_NULO_36, null, nombreClase, nombreMetodo,
                            null,input.getCodArea());
            }
         
        }

        return new Respuesta();
    }
    
    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET 
     * y en este caso tambi\u00E9n todos los que se insertar\u00E9n en el m\u00E9todo POST.
     * 
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * 
     * @return campos Array con los nombres de los campos.
     */
    public static String[][] obtenerCamposGet() {
        String campos[][] = {
        		{ Cuenta.N_TABLA, Cuenta.CAMPO_TCSCCTABANCARIAID},
        		{ Cuenta.N_TABLA, Cuenta.CAMPO_TCSCDTSID},
        		{ Distribuidor.N_TABLA, Distribuidor.CAMPO_NOMBRES},
        		{ Cuenta.N_TABLA, Cuenta.CAMPO_BANCO},
        		{ Cuenta.N_TABLA, Cuenta.CAMPO_NO_CUENTA},
        		{ Cuenta.N_TABLA, Cuenta.CAMPO_TIPO_CUENTA},
        		{ Cuenta.N_TABLA, Cuenta.CAMPO_NOMBRE_CUENTA},
        		{ Cuenta.N_TABLA, Cuenta.CAMPO_ESTADO},
        		{ Cuenta.N_TABLA, Cuenta.CAMPO_CREADO_EL},
        		{ Cuenta.N_TABLA, Cuenta.CAMPO_CREADO_POR},
        		{ Cuenta.N_TABLA, Cuenta.CAMPO_MODIFICADO_EL},
        		{ Cuenta.N_TABLA,Cuenta.CAMPO_MODIFICADO_POR}
        };

        return campos;
    }

    public static String[] obtenerCamposGetPost() {
        String campos[] = {
                Cuenta.CAMPO_TCSCCTABANCARIAID,
                Cuenta.CAMPO_TCSCDTSID,
                Cuenta.CAMPO_BANCO,
                Cuenta.CAMPO_NO_CUENTA,
                Cuenta.CAMPO_TIPO_CUENTA,
                Cuenta.CAMPO_NOMBRE_CUENTA,
                Cuenta.CAMPO_ESTADO,
                Cuenta.CAMPO_CREADO_EL,
                Cuenta.CAMPO_CREADO_POR,
                Cuenta.CAMPO_TCSCCATPAISID
        };

        return campos;
    }
    
    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param sequencia Nombre de la secuencia a utilizar para la inserci\u00F3n.
     * @param estadoAlta Estado inicial a utilizar para la inserci\u00F3n.
     * 
     * @return inserts Listado de cadenas con los valores a insertar.
     */
    public static List<String> obtenerInsertsPost(InputCuenta input, String sequencia, String estadoAlta, BigDecimal idPais) {
        List<String> inserts = new ArrayList<String>();
        String valores = "";
        
        valores = "("
                + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdDts(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getBanco(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getNoCuenta(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getTipoCuenta(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getNombreCuenta(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPais.toString(), Conf.INSERT_SEPARADOR_NO)
        + ") ";
        inserts.add(valores);
        
        return inserts;
    }
    
    /**
     * Funci\u00F3n que indica los campos que se utilizar\u00E9n para modificaciones  
     * al recurso en los m\u00E9todos PUT y DELETE.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @param estadoBaja Valor del estado de baja.
     * 
     * @return campos Array bidimensional que contiene el nombre de los campos a modificar
     *  y su respectivo valor a insertar.
     */
    public static String[][] obtenerCamposPut(InputCuenta input) {
        String campos[][] = {
            { Cuenta.CAMPO_BANCO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getBanco()) },
            { Cuenta.CAMPO_NO_CUENTA, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getNoCuenta()) },
            { Cuenta.CAMPO_TIPO_CUENTA, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getTipoCuenta()) },
            { Cuenta.CAMPO_NOMBRE_CUENTA, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getNombreCuenta()) },
            { Cuenta.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getEstado()) },
            { Cuenta.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
            { Cuenta.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
        };
        
        return campos;
    }
    
    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return condiciones Listado con los filtros indicados seg\u00FAn el m\u00E9todo deseado.
     */
    public static List<Filtro> obtenerCondiciones(InputCuenta input, int metodo, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (metodo == Conf.METODO_PUT) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Cuenta.CAMPO_TCSCCTABANCARIAID,
                    input.getIdCuenta()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Cuenta.CAMPO_TCSCCATPAISID,
                    idPais.toString()));
        }

        if (metodo == Conf.METODO_GET) {
            if (input.getBanco() != null && !"".equals(input.getIdCuenta())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Cuenta.N_TABLA,
                        Cuenta.CAMPO_TCSCCTABANCARIAID, input.getIdCuenta()));
            }

            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Cuenta.N_TABLA,
                    Cuenta.CAMPO_TCSCCATPAISID, idPais.toString()));

            if (input.getBanco() != null && !"".equals(input.getBanco())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Cuenta.N_TABLA,
                        Cuenta.CAMPO_BANCO, input.getBanco()));
            }

            if (input.getNoCuenta() != null && !"".equals(input.getNoCuenta())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Cuenta.N_TABLA,
                        Cuenta.CAMPO_NO_CUENTA, input.getNoCuenta()));
            }

            if (input.getTipoCuenta() != null && !"".equals(input.getTipoCuenta())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Cuenta.N_TABLA,
                        Cuenta.CAMPO_TIPO_CUENTA, input.getTipoCuenta()));
            }

            if (input.getNombreCuenta() != null && !"".equals(input.getNombreCuenta())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Cuenta.N_TABLA,
                        Cuenta.CAMPO_NOMBRE_CUENTA, input.getNombreCuenta()));
            }

            if (input.getEstado() != null && !"".equals(input.getEstado())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Cuenta.N_TABLA,
                        Cuenta.CAMPO_ESTADO, input.getEstado()));
            }

            if (input.getIdDts() != null && !"".equals(input.getIdDts().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Cuenta.N_TABLA,
                        Cuenta.CAMPO_TCSCDTSID, input.getIdDts()));
            }
        }

        return condiciones;
    }
    
    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las consultas de verificaci\u00F3n
     * de existencia del recurso.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return condiciones Listado con los filtros indicados seg\u00FAn el m\u00E9todo deseado.
     * @throws SQLException 
     */
    public static List<Filtro> obtenerCondicionesExistencia( InputCuenta input, int metodo,
            String estadoAlta, BigDecimal idPais) throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Cuenta.CAMPO_TCSCCTABANCARIAID, input.getIdCuenta()));
        }

        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Cuenta.CAMPO_TCSCCATPAISID, idPais.toString()));

        if (metodo == Conf.METODO_POST || metodo == Conf.METODO_PUT) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Cuenta.CAMPO_BANCO, input.getBanco()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Cuenta.CAMPO_NO_CUENTA, input.getNoCuenta()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Cuenta.CAMPO_TIPO_CUENTA, input.getTipoCuenta()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Cuenta.CAMPO_ESTADO, estadoAlta));
        }

        return condiciones;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo M\u00E9todo que indica las acciones a realizar.
     * @return output Respuesta y listado con los Cuentas encontrados.
     */
    public OutputCuenta getDatos(InputCuenta input, int metodo) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        COD_PAIS = input.getCodArea();
        
        OutputCuenta output = new OutputCuenta();
        Respuesta respuesta = new Respuesta();
        
        Connection conn = null;
        try {
            conn = getConnRegional();
            ID_PAIS = getIdPais(conn, input.getCodArea());
            log.trace("Usuario: " + input.getUsuario());
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
            String estadoBaja = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            respuesta = validarInput(conn, input, metodo, estadoAlta, estadoBaja);
            log.trace("Respuesta validaci\u00F3n: " + (respuesta.getDescripcion() == null ? "OK" : respuesta.getDescripcion()));
            if (respuesta.getCodResultado()==null) {
            	log.trace("no hay error");
            }else{
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CUENTA, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));

                return output;
            }

            if (metodo == Conf.METODO_GET) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionCuenta.doGet(conn, input, metodo, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de Cuentas.", ""));
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output.setRespuesta(respuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de Cuentas.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_POST) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
                try {
                    output = OperacionCuenta.doPost(conn, input, estadoAlta, ID_PAIS);

                    
                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_CUENTA_38) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CUENTA, servicioPost,
                                output.getIdCuenta(), Conf.LOG_TIPO_CUENTA,
                                "Se registr\u00F3 una nueva cuenta con el ID " + output.getIdCuenta() + ".", ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CUENTA, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema al crear Cuenta.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output.setRespuesta(respuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CUENTA, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al crear Cuentas.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_PUT) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT o DELETE
                try {
                    output = OperacionCuenta.doPut(conn, input, metodo, estadoAlta,ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_MOD_CUENTA_39) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CUENTA, servicioPut,
                                input.getIdCuenta(), Conf.LOG_TIPO_CUENTA,
                                "Se modificaron datos de la Cuenta con el ID " + input.getIdCuenta() + ".", ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CUENTA, servicioPut,
                                input.getIdCuenta(), Conf.LOG_TIPO_CUENTA, "Problema al modificar datos de Cuenta.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output.setRespuesta(respuesta);

                    listaLog.add(
                            ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CUENTA, servicioPut, input.getIdCuenta(),
                                    Conf.LOG_TIPO_CUENTA, "Problema al modificar Cuenta.", e.getMessage()));
                }
            }
        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CUENTA, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de Cuentas.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
