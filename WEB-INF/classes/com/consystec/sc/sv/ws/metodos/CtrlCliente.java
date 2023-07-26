package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.cliente.InputCliente;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.cliente.OutputCliente;
import com.consystec.sc.sv.ws.operaciones.OperacionCliente;
import com.consystec.sc.sv.ws.orm.Cliente;
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
public class CtrlCliente extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlCliente.class);
    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_CLIENTE;
    private static String servicioPost = Conf.LOG_POST_CLIENTE;
    private static String servicioPut = Conf.LOG_PUT_CLIENTE;
    
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al servicio.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return Respuesta
     */
    public Respuesta validarInput(InputCliente input, int metodo) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        
        log.debug("Validando datos...");
        
        if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
            log.trace("ID del Cliente: " + input.getIdCliente());
            if (input.getIdCliente() == null || "".equals(input.getIdCliente())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_CLIENTE_299, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else if(!isNumeric(input.getIdCliente())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_CLIENTE_NUM_300, null, nombreClase,
                        nombreMetodo, null,input.getCodArea()).getDescripcion();
                flag = true;
            }
        }
        
        if (metodo == Conf.METODO_POST || metodo == Conf.METODO_PUT) {
            if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase,
                        nombreMetodo, null,input.getCodArea()).getDescripcion();
                flag = true;
            }
            if (input.getNombres() == null || "".equals(input.getNombres().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NOMBRES_156, null, nombreClase,
                        nombreMetodo, null,input.getCodArea()).getDescripcion();
                flag = true;
            }
            if (input.getApellidos() == null || "".equals(input.getApellidos().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_APELLIDOS_157, null, nombreClase,
                        nombreMetodo, null,input.getCodArea()).getDescripcion();
                flag = true;
            }
            if (input.getNit() == null || "".equals(input.getNit().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NIT_301, null, nombreClase,
                        nombreMetodo, null,input.getCodArea()).getDescripcion();
                flag = true;
            }
            if (input.getDocIdentificacion() == null ||"".equals( input.getDocIdentificacion().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DOC_IDENTIFICACION_302, null, nombreClase,
                        nombreMetodo, null,input.getCodArea()).getDescripcion();
                flag = true;
            }
        }
        
        if (metodo == Conf.METODO_PUT && (input.getEstado() == null || "".equals(input.getEstado().trim()))) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_134, null, nombreClase,
                        nombreMetodo, null,input.getCodArea()).getDescripcion();
                flag = true;
        }
        
        if (flag ) {
            r = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo, datosErroneos,  input.getCodArea());
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
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return campos Array con los nombres de los campos.
     */
    public static String[] obtenerCamposGetPost(int metodo) {
        if (metodo == Conf.METODO_GET) {
            String campos[] = {
                Cliente.CAMPO_TC_SC_CLIENTE_ID,
                Cliente.CAMPO_NOMBRE,
                Cliente.CAMPO_APELLIDO,
                Cliente.CAMPO_NIT,
                Cliente.CAMPO_DOCUMENTO_IDENTIFICACION,
                Cliente.CAMPO_ESTADO,
                Cliente.CAMPO_CREADO_EL,
                Cliente.CAMPO_CREADO_POR,
                Cliente.CAMPO_MODIFICADO_EL,
                Cliente.CAMPO_MODIFICADO_POR
            };
            return campos;
        } else {
            String campos[] = {
                Cliente.CAMPO_TC_SC_CLIENTE_ID,
                Cliente.CAMPO_NOMBRE,
                Cliente.CAMPO_APELLIDO,
                Cliente.CAMPO_NIT,
                Cliente.CAMPO_DOCUMENTO_IDENTIFICACION,
                Cliente.CAMPO_ESTADO,
                Cliente.CAMPO_CREADO_EL,
                Cliente.CAMPO_CREADO_POR
            };
            return campos;
        }
    }
    
    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param sequencia Nombre de la secuencia a utilizar para la inserci\u00F3n.
     * @return inserts Listado de cadenas con los valores a insertar.
     * @throws SQLException 
     */
    public static List<String> obtenerInsertsPost(InputCliente input, String sequencia) throws SQLException {
        List<String> inserts = new ArrayList<String>();
        String valores = "";
        
        valores = "("
            + "(extractvalue(dbms_xmlgen.getxmltype('select " + sequencia + " from dual'),'//text()')), "
            + "'" + input.getNombres() + "', "
            + "'" + input.getApellidos() + "', "
            + "UPPER('" + input.getNit() + "'), "
            + "UPPER('" + input.getDocIdentificacion() + "'), "
            + "UPPER('" + UtileriasJava.getConfig(null, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea()) + "'), "
            + "sysdate, "
            + "'" + input.getUsuario() + "'"
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
     * @return campos Array bidimensional que contiene el nombre de los campos a modificar
     *  y su respectivo valor a insertar.
     * @throws SQLException 
     */
    public static String[][] obtenerCamposPutDel(InputCliente input, int metodo) throws SQLException {
        //Los valores que sean tipo texto deben ir entre ap\u00F3strofres o comillas simples.
        if (metodo == Conf.METODO_DELETE) {
            String campos[][] = {
                { Cliente.CAMPO_ESTADO, "UPPER('" + UtileriasJava.getConfig(null, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, input.getCodArea()) + "')" },
                { Cliente.CAMPO_MODIFICADO_EL, "sysdate" },
                { Cliente.CAMPO_MODIFICADO_POR, "'" + input.getUsuario() + "'" }
            };
            return campos;
        } else {
            String campos[][] = {
                { Cliente.CAMPO_NOMBRE, "'" + input.getNombres() + "'" },
                { Cliente.CAMPO_APELLIDO, "'" + input.getApellidos() + "'" },
                { Cliente.CAMPO_NIT, "'" + input.getNit() + "'" },
                { Cliente.CAMPO_DOCUMENTO_IDENTIFICACION, "'" + input.getDocIdentificacion() + "'" },
                { Cliente.CAMPO_ESTADO, "UPPER('" + input.getEstado() + "')" },
                { Cliente.CAMPO_MODIFICADO_EL, "sysdate" },
                { Cliente.CAMPO_MODIFICADO_POR, "'" + input.getUsuario() + "'" }
            };
            return campos;
        }
    }
    
    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return condiciones Listado con los filtros indicados seg\u00FAn el m\u00E9todo deseado.
     */
    public static List<Filtro> obtenerCondiciones( InputCliente input, int metodo) {
        List<Filtro> condiciones = new ArrayList<Filtro>();
  
        
        if ((metodo==Conf.METODO_PUT || metodo==Conf.METODO_DELETE) && !"".equals(input.getIdCliente())) {
                condiciones.add(UtileriasJava.setCondicion(
                    Conf.FILTRO_ID_NUMERICO_AND, Cliente.CAMPO_TC_SC_CLIENTE_ID, input.getIdCliente()));
        }
        
        if (metodo==Conf.METODO_GET){
            if (input.getNombres() != null && !"".equals(input.getNombres())) {
                    condiciones.add(UtileriasJava.setCondicion(
                        Conf.FILTRO_TEXTO_AND, Cliente.CAMPO_NOMBRE, input.getNombres()));
            }
            if (input.getApellidos() != null && !"".equals(input.getApellidos())) {
                    condiciones.add(UtileriasJava.setCondicion(
                        Conf.FILTRO_TEXTO_AND, Cliente.CAMPO_APELLIDO, input.getApellidos()));
            }
            if (input.getNit() != null && !"".equals(input.getNit())) {
                    condiciones.add(UtileriasJava.setCondicion(
                        Conf.FILTRO_TEXTO_UPPER_AND, Cliente.CAMPO_NIT, input.getNit()));
            }
            if (input.getDocIdentificacion() != null && !"".equals(input.getDocIdentificacion())) {
                    condiciones.add(UtileriasJava.setCondicion(
                        Conf.FILTRO_TEXTO_UPPER_AND, Cliente.CAMPO_DOCUMENTO_IDENTIFICACION, input.getDocIdentificacion()));
            }
            if (input.getEstado() != null && !"".equals(input.getEstado())) {
                    condiciones.add(UtileriasJava.setCondicion(
                        Conf.FILTRO_TEXTO_UPPER_AND, Cliente.CAMPO_ESTADO, input.getEstado()));
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
    public static List<Filtro> obtenerCondicionesExistencia(Connection conn, InputCliente input, int metodo) throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (metodo == Conf.METODO_POST){
            if (! "".equals(input.getNit())) {
                condiciones.add(UtileriasJava.setCondicion(
                    Conf.FILTRO_TEXTO_UPPER_OR, Cliente.CAMPO_NIT, input.getNit()));
            }
            if (!"".equals(input.getDocIdentificacion())) {
                condiciones.add(UtileriasJava.setCondicion(
                    Conf.FILTRO_TEXTO_UPPER_OR, Cliente.CAMPO_DOCUMENTO_IDENTIFICACION, input.getDocIdentificacion()));
            }
        }
        
        if (metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE){
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                Cliente.CAMPO_TC_SC_CLIENTE_ID, input.getIdCliente()));
        }

        if (metodo != Conf.METODO_PUT){
            condiciones.add(UtileriasJava.getCondicionDefault(Conf.FILTRO_ESTADO, Cliente.CAMPO_ESTADO, conn, input.getCodArea()));
        }
        
        return condiciones;
    }
    
    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo M\u00E9todo que indica las acciones a realizar.
     * @return output Respuesta y listado con los clientes encontrados.
     */
    public OutputCliente getDatos(InputCliente input, int metodo) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        OutputCliente output = null;
        Respuesta r = new Respuesta();

        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();
            
            // Validaci\u00F3n de datos en el input
            r = validarInput(input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equals(r.getDescripcion())) {
                output = new OutputCliente();
                output.setRespuesta(r);
                
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CLIENTE, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));
                
                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                try {
                    output = OperacionCliente.doGet(conn, input, metodo);
                    
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de clientes.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputCliente();
                    output.setRespuesta(r);
                    
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de clientes.",
                            e.getMessage()));
                }
            
            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
            } else if (metodo == Conf.METODO_POST) {
                try {
                    output = OperacionCliente.doPost(conn, input);
                    
                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.MSJ_RECURSO_CREADO) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CLIENTE, servicioPost,
                                output.getIdCliente(), Conf.LOG_TIPO_CLIENTE,
                                "Se registr\u00F3 un nuevo cliente con el ID " + output.getIdCliente()
                                        + " y documento de identificaci\u00F3n " + input.getDocIdentificacion()
                                        + ".",
                                ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CLIENTE, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema al crear cliente.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputCliente();
                    output.setRespuesta(r);
                    
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CLIENTE, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al crear clientes.", e.getMessage()));
                }
                
            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT o DELETE
            } else if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
                try {
                    output = OperacionCliente.doPutDel(conn, input, metodo);
                    
                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.MSJ_RECURSO_MODIFICADO) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CLIENTE, servicioPut, input.getIdCliente(),
                                Conf.LOG_TIPO_CLIENTE, "Se modificaron datos del cliente con el ID "
                                        + input.getIdCliente() + ".",
                                ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CLIENTE, servicioPut,
                                input.getIdCliente(), Conf.LOG_TIPO_CLIENTE,
                                "Problema al modificar datos de cliente.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputCliente();
                    output.setRespuesta(r);
                    
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CLIENTE, servicioPut,
                            input.getIdCliente(), Conf.LOG_TIPO_CLIENTE,
                            "Problema al modificar cliente.", e.getMessage()));
                }
            }

          
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputCliente();
            output.setRespuesta(r);
            
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CLIENTE, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de clientes.", e.getMessage()));
        }finally{
            DbUtils.closeQuietly(conn);
            
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
