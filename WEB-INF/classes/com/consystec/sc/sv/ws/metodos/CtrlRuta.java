package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.ruta.InputRuta;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.ruta.OutputRuta;
import com.consystec.sc.sv.ws.operaciones.OperacionRuta;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.orm.BodegaVendedor;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.Inventario;
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
public class CtrlRuta extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlRuta.class);
    private static String servicioGet = Conf.LOG_GET_RUTA;
    private static String servicioPost = Conf.LOG_POST_RUTA;
    private static String servicioPut = Conf.LOG_PUT_RUTA;

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
    public Respuesta validarInput(Connection conn, InputRuta input, int metodo, BigDecimal ID_PAIS) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        log.debug("Validando datos...");

        if (metodo == Conf.METODO_GET) {
            if (input.getIdDTS() != null && !isNumeric(input.getIdDTS())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }
            if (input.getSecUsuarioId() != null && !isNumeric(input.getSecUsuarioId())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDUSUARIO_NUM_322, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }
        }

        if ((metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT)&& (input.getIdRuta() == null || !isNumeric(input.getIdRuta()))) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDRUTA_NUM_251, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
        }

        if (metodo == Conf.METODO_POST || metodo == Conf.METODO_PUT) {
            List<Filtro> condiciones = new ArrayList<Filtro>();
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
            String existencia = "";
            
            if (input.getIdBodegaVirtual() == null || input.getIdBodegaVirtual().equals("")) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODVIRTUAL_170, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdBodegaVirtual())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODVIRTUAL_NUM_171, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }

            if (input.getIdDTS() == null || input.getIdDTS().equals("")) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_161, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdDTS())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else {
                // se verifica dts
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TC_SC_DTS_ID, input.getIdDTS()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Distribuidor.CAMPO_ESTADO, estadoAlta));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
    
                existencia = UtileriasBD.verificarExistencia(conn, Distribuidor.N_TABLA, condiciones);
                if (new Integer(existencia) <= 0) {
                    log.error("No existe el DTS.");
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_IDDTS_NO_EXISTE_173, null, nombreClase, nombreMetodo,
                            null, input.getCodArea()).getDescripcion();
                    flag = true;
                }
            }

            if (input.getNombreRuta() == null || input.getNombreRuta().trim().equals("")) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NOMBRE_222, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            }

            if (input.getSecUsuarioId() == null || input.getSecUsuarioId().equals("")) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDUSUARIO_323, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getSecUsuarioId())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDUSUARIO_NUM_322, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else {
                // se verifica vendedor
            	condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_TCSCDTSID, input.getIdDTS()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_VENDEDOR, input.getSecUsuarioId()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorDTS.CAMPO_ESTADO, estadoAlta));

                existencia = UtileriasBD.verificarExistencia(conn, VendedorDTS.N_TABLA, condiciones);
                if (new Integer(existencia) <= 0) {
                    log.error("No existe el vendedor.");
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_EXISTENCIA_VENDEDOR_361, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                }
            }

            if (input.getUsuario() == null || input.getUsuario().trim().equals("")) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            }
        }

        if (metodo == Conf.METODO_PUT &&  (input.getEstado() == null || input.getEstado().trim().equals(""))) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_134, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
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
     * @return
     */
    public static String[] obtenerCamposPost() {
        String campos[] = {
            Ruta.CAMPO_TC_SC_RUTA_ID,
            Ruta.CAMPO_TCSCCATPAISID,
            Ruta.CAMPO_TC_SC_BODEGAVIRTUAL_ID,
            Ruta.CAMPO_TC_SC_DTS_ID,
            Ruta.CAMPO_NOMBRE,
            Ruta.CAMPO_SEC_USUARIO_ID,
            Ruta.CAMPO_ESTADO,
            Ruta.CAMPO_CREADO_EL,
            Ruta.CAMPO_CREADO_POR
        };
        
        return campos;
    }
    
    public static String[][] obtenerCamposGet(String codArea, BigDecimal ID_PAIS) {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_IDVENDEDOR,
                Ruta.N_TABLA + "." + Ruta.CAMPO_SEC_USUARIO_ID));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID,
                ID_PAIS.toString()));
        
        String campos[][] = {
            { Ruta.N_TABLA, Ruta.CAMPO_TC_SC_RUTA_ID },
            { Ruta.N_TABLA, Ruta.CAMPO_TC_SC_BODEGAVIRTUAL_ID },
            { Ruta.N_TABLA, Ruta.CAMPO_TC_SC_DTS_ID },
            { Distribuidor.N_TABLA, Distribuidor.CAMPO_NOMBRES },
            { Distribuidor.N_TABLA, Distribuidor.CAMPO_TIPO },
            
            { Ruta.N_TABLA, Ruta.CAMPO_NOMBRE },
            { Ruta.N_TABLA, Ruta.CAMPO_SEC_USUARIO_ID },
            { Ruta.N_TABLA, Ruta.CAMPO_ESTADO },
            { Ruta.N_TABLA, Ruta.CAMPO_CREADO_EL },
            { Ruta.N_TABLA, Ruta.CAMPO_CREADO_POR },
            { Ruta.N_TABLA, Ruta.CAMPO_MODIFICADO_EL },
            { Ruta.N_TABLA, Ruta.CAMPO_MODIFICADO_POR },
            
            { null, "(SELECT " + VendedorDTS.CAMPO_USUARIO 
                + " FROM " + ControladorBase.getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, "", codArea) 
                + " WHERE " + VendedorDTS.CAMPO_VENDEDOR + " = " + Ruta.N_TABLA + "." + Ruta.CAMPO_SEC_USUARIO_ID
                + " AND " + VendedorDTS.CAMPO_TCSCCATPAISID + " = " + ID_PAIS
                    + ") AS NOMBRE_VEND" },
            
            { null, "("
                + UtileriasBD.armarQuerySelectField(ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, "", codArea),
                        UtileriasJava.setSelect(Conf.SELECT_SUM, Inventario.CAMPO_CANTIDAD), condiciones, null)
                + ") AS CANT_INV" },
            
            { null, "(SELECT " + BodegaVendedor.N_TABLA + "." + BodegaVendedor.CAMPO_TCSCBODEGAVIRTUALID
                + " FROM " + BodegaVendedor.N_TABLA 
                + " WHERE " + BodegaVendedor.N_TABLA + "." + BodegaVendedor.CAMPO_VENDEDOR + " = "
                    + Ruta.N_TABLA + "." + Ruta.CAMPO_SEC_USUARIO_ID 
                    + " AND " + BodegaVendedor.CAMPO_TCSCCATPAISID + " = " + ID_PAIS
                    + ") AS BOD_VEND" }
        };

        return campos;
    }

    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
     * 
     * @param input
     * @param sequencia
     * @param estadoAlta 
     * @return inserts
     */
    public static List<String> obtenerInsertsPost(InputRuta input, String sequencia, String estadoAlta, BigDecimal ID_PAIS) {
        List<String> inserts = new ArrayList<String>();
        String valores = "";

        valores = "("
            + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, ID_PAIS.toString(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdBodegaVirtual(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdDTS(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getNombreRuta(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getSecUsuarioId(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, estadoAlta, Conf.INSERT_SEPARADOR_SI)
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
    public static String[][] obtenerCamposPutDel(InputRuta input, int metodo) throws SQLException {
        if (metodo == Conf.METODO_DELETE) {
            String campos[][] = {
                { Ruta.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, UtileriasJava.getConfig(null, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, input.getCodArea())) },
                { Ruta.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                { Ruta.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
            };
            return campos;
        } else {
            String campos[][] = {               
                { Ruta.CAMPO_TC_SC_DTS_ID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getIdDTS()) },
                { Ruta.CAMPO_TC_SC_BODEGAVIRTUAL_ID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getIdBodegaVirtual()) },
                { Ruta.CAMPO_NOMBRE, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getNombreRuta()) },
                { Ruta.CAMPO_SEC_USUARIO_ID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getSecUsuarioId()) },
                { Ruta.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getEstado()) },
                { Ruta.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                { Ruta.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
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
    public static List<Filtro> obtenerCondiciones(InputRuta input, int metodo, BigDecimal ID_PAIS) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if( (metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE) && !input.getIdRuta().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TC_SC_RUTA_ID, input.getIdRuta()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
        }

        if (metodo == Conf.METODO_GET) {
            if (input.getIdRuta() != null && !input.getIdRuta().equals("")) {
                    condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Ruta.N_TABLA, Ruta.CAMPO_TC_SC_RUTA_ID, input.getIdRuta()));
            }

            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Ruta.N_TABLA, Ruta.CAMPO_TCSCCATPAISID, ID_PAIS.toString())); 
            
            if (input.getIdDTS() != null && !input.getIdDTS().equals("")) {
                    condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Ruta.N_TABLA, Ruta.CAMPO_TC_SC_DTS_ID, input.getIdDTS()));
            }
            if (input.getIdBodegaVirtual() != null && !input.getIdBodegaVirtual().equals("")) {
                    condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Ruta.N_TABLA, Ruta.CAMPO_TC_SC_BODEGAVIRTUAL_ID, input.getIdBodegaVirtual()));
            }
            if (input.getNombreRuta() != null && !input.getNombreRuta().equals("")) {
                    condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Ruta.N_TABLA, Ruta.CAMPO_NOMBRE, input.getNombreRuta()));
            }
            if (input.getSecUsuarioId() != null && !input.getSecUsuarioId().equals("")) {
                    condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Ruta.N_TABLA, Ruta.CAMPO_SEC_USUARIO_ID, input.getSecUsuarioId()));
            }
            if (input.getEstado() != null && !input.getEstado().equals("")) {
                    condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Ruta.N_TABLA, Ruta.CAMPO_ESTADO, input.getEstado()));
            }
        }
        
        return condiciones;
    }
    
    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las consultas de verificaci\u00F3n
     * de existencia del recurso.
     * 
     * @param input
     * @param metodo 
     * @return condiciones
     * @throws SQLException 
     */
    public static List<Filtro> obtenerCondicionesExistencia(InputRuta input, int metodo, Connection conn, BigDecimal ID_PAIS) throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TCSCCATPAISID, ID_PAIS.toString())); //Filtrar por pais cada peticion
        if (metodo == Conf.METODO_POST && !input.getSecUsuarioId().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_SEC_USUARIO_ID, input.getSecUsuarioId()));
        }
        
        if (metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE){
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TC_SC_RUTA_ID, input.getIdRuta()));
        }

        if (metodo != Conf.METODO_PUT){
            condiciones.add(UtileriasJava.getCondicionDefault(Conf.FILTRO_ESTADO, Ruta.CAMPO_ESTADO, conn, input.getCodArea()));
        }
        
        return condiciones;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     * @param metodo
     * @return respuestaRuta
     */
    public OutputRuta getDatos(InputRuta input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<Respuesta> respuesta = new ArrayList<Respuesta>();

        Respuesta r = new Respuesta();
        OutputRuta output = null;
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
                output = new OutputRuta();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_RUTA, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            log.debug("Distribuidor: " + input.getIdDTS());
            log.debug("SecUsuarioID: " + input.getSecUsuarioId());

            if (metodo == Conf.METODO_GET) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionRuta.doGet(conn, input, metodo, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de rutas.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputRuta();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de rutas.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_POST) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
                try {
                    output = OperacionRuta.doPost(conn, input, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_RUTA_54) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_RUTA, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO,
                                "Se cre\u00F3 nueva ruta de nombre " + input.getNombreRuta().toUpperCase() + ".", ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_RUTA, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema al crear ruta.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputRuta();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_RUTA, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al crear ruta.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT o DELETE
                try {
                    output = OperacionRuta.doPutDel(conn, input, metodo, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_MOD_RUTA_55) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_RUTA, servicioPut,
                                input.getIdRuta(), Conf.LOG_TIPO_RUTA,
                                "Se modificaron datos de la ruta con ID " + input.getIdRuta() + ".", ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_RUTA, servicioPut,
                                input.getIdRuta(), Conf.LOG_TIPO_RUTA, "Problema al modificar datos de rutas.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputRuta();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_RUTA, servicioPut, input.getIdRuta(),
                            Conf.LOG_TIPO_RUTA, "Problema al modificar rutas.", e.getMessage()));
                }
            }

        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputRuta();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_RUTA, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de rutas.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
