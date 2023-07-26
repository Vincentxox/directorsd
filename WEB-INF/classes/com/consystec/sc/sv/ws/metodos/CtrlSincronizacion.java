package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.sincronizacion.InputSincronizacion;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.sincronizacion.OutputSincronizacion;
import com.consystec.sc.sv.ws.operaciones.OperacionSincronizacion;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.Sincronizacion;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class CtrlSincronizacion extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlSincronizacion.class);
    private static String servicioGet = Conf.LOG_GET_SINCRONIZACION;
    private static String servicioPost = Conf.LOG_POST_SINCRONIZACION;

    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al
     * servicio.
     * 
     * @param conn Objeto de tipo connetion con los datos de la conexi\u00F3n activa.
     * @param input Objeto con los datos enviados para validar.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return respuesta Objeto con la respuesta en caso de error o nulo si todo esta correcto.
     * @throws SQLException 
     */
    public Respuesta validarInput(Connection conn, InputSincronizacion input, int metodo) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        log.debug("Validando datos...");

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            return getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        if (metodo == Conf.METODO_GET - 1) {
            if (input.getIdJornada() == null ||"".equals( input.getIdJornada()) || !isNumeric(input.getIdJornada())) {
                return getMensaje(Conf_Mensajes.MSJ_IDJORNADA_NULO_89, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }
        } else {
            if (metodo == Conf.METODO_GET) {
                if (input.getIdJornada() != null && !"".equals(input.getIdJornada())
                        && !isNumeric(input.getIdJornada())) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDJORNADA_259, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                }

                if (input.getIdVendedor() != null && !"".equals(input.getIdVendedor())
                        && !isNumeric(input.getIdVendedor())) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                }

            } else {
                if (input.getIdDispositivo() == null || "".equals(input.getIdDispositivo().trim())) {
                    return getMensaje(Conf_Mensajes.MSJ_IDDISPOSITIVO_NULO_66, null, nombreClase, nombreMetodo, null, input.getCodArea());
                }

                if (input.getIdTipo() != null && !"".equals(input.getIdTipo().trim())) {
                    if (!isNumeric(input.getIdTipo())) {
                        return getMensaje(Conf_Mensajes.MSJ_IDTIPO_NULO_430, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    } else {
                        if (input.getTipo() == null || "".equals(input.getTipo())) {
                            return getMensaje(Conf_Mensajes.MSJ_TIPO_NULO_431, null, nombreClase, nombreMetodo, null, input.getCodArea());
                        }
                    }
                }

                if (input.getTipo() != null && !"".equals(input.getTipo().trim())) {
                    String tipoPanelRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA,
                            input.getTipo().toUpperCase(), input.getCodArea());

                    if (tipoPanelRuta == null || "".equals(tipoPanelRuta)) {
                        return getMensaje(Conf_Mensajes.MSJ_ERROR_TIPOOFERTA_NO_DEFINIDO_424, null, nombreClase,
                                nombreMetodo, null, input.getCodArea());
                    }
                }

                for (int i = 0; i < input.getDatos().size(); i++) {
                    String idVendedor = input.getDatos().get(i).getIdVendedor();
                    String idJornada = input.getDatos().get(i).getIdJornada();

                    if (idVendedor == null || idVendedor.equals("") || !isNumeric(idVendedor)) {
                        return getMensaje(Conf_Mensajes.MSJ_VENDEDOR_NULO_38, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    }

                    if (idJornada == null || idJornada.equals("") || !isNumeric(idJornada)) {
                        return getMensaje(Conf_Mensajes.MSJ_IDJORNADA_NULO_89, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    }
                }

                if (input.getDatos() == null || input.getDatos().isEmpty()) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_DATOSVENDEDOR_719, null, nombreClase, nombreMetodo, null, input.getCodArea());

                } else {
                    for (int i = 0; i < input.getDatos().size(); i++) {
                        String idVendedor = input.getDatos().get(i).getIdVendedor();
                        String idJornada = input.getDatos().get(i).getIdJornada();

                        if (idVendedor == null || idVendedor.equals("") || !isNumeric(idVendedor)) {
                            return getMensaje(Conf_Mensajes.MSJ_ERROR_SINC_IDVENDEDOR_742, null, nombreClase,
                                    nombreMetodo, (i + 1) + "", input.getCodArea());
                        }

                        if (idJornada == null || idJornada.equals("") || !isNumeric(idJornada)) {
                            return getMensaje(Conf_Mensajes.MSJ_ERROR_SINC_IDJORNADA_743, null, nombreClase,
                                    nombreMetodo, (i + 1) + "", input.getCodArea());
                        }
                    }

                    int numeroVend = 1;
                    for (InputSincronizacion vendActual : input.getDatos()) {
                        int indexVend = 1;

                        for (InputSincronizacion detalle : input.getDatos()) {
                            if (indexVend != numeroVend
                                    && detalle.getIdVendedor().trim().equals(vendActual.getIdVendedor().trim())) {

                                return getMensaje(Conf_Mensajes.MSJ_INPUT_VENDEDORES_IGUALES_729, null, nombreClase,
                                        nombreMetodo, numeroVend + " y " + indexVend + ".", input.getCodArea());
                            }

                            indexVend++;
                        }

                        numeroVend++;
                    }
                }
            }
        }

        return new Respuesta();
    }
    
    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET 
     * y en este caso tambi\u00E9n todos los que se insertar\u00E9n en el m\u00E9todo POST.
     * 
     * @param conn 
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * 
     * @return campos Array con los nombres de los campos.
     */
    public static String[][] obtenerCamposGet() {
        String campos[][] = {
            { Sincronizacion.N_TABLA, Sincronizacion.CAMPO_TCSCSINCVENDEDORID },
            { Sincronizacion.N_TABLA, Sincronizacion.CAMPO_IDVENDEDOR },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_USUARIO },
            { Sincronizacion.N_TABLA, Sincronizacion.CAMPO_COD_DISPOSITIVO },
            { Sincronizacion.N_TABLA, Sincronizacion.CAMPO_TCSCJORNADAVENDID },
            { Sincronizacion.N_TABLA, Sincronizacion.CAMPO_CREADO_EL },
            { Sincronizacion.N_TABLA, Sincronizacion.CAMPO_CREADO_POR },
            { Sincronizacion.N_TABLA, Sincronizacion.CAMPO_MODIFICADO_EL },
            { Sincronizacion.N_TABLA, Sincronizacion.CAMPO_MODIFICADO_POR }
        };
        
        return campos;
    }
    
    public static String[] obtenerCamposPost() {
        String campos[] = {
            Sincronizacion.CAMPO_TCSCSINCVENDEDORID,
            Sincronizacion.CAMPO_TCSCCATPAISID,
            Sincronizacion.CAMPO_IDVENDEDOR,
            Sincronizacion.CAMPO_COD_DISPOSITIVO,
            Sincronizacion.CAMPO_TCSCJORNADAVENDID,
            Sincronizacion.CAMPO_CREADO_EL,
            Sincronizacion.CAMPO_CREADO_POR
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
    public static List<String> obtenerInsertsPost(InputSincronizacion input, String sequencia, 
            String idVendedor, String idJornada, BigDecimal ID_PAIS) {
        List<String> inserts = new ArrayList<String>();
        String valores = "";
        
        valores = "("
                + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, ID_PAIS.toString(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idVendedor, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getIdDispositivo(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idJornada, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
        + ") ";
        inserts.add(valores);
        
        return inserts;
    }
    
    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return condiciones Listado con los filtros indicados seg\u00FAn el m\u00E9todo deseado.
     */
    public static List<Filtro> obtenerCondiciones(InputSincronizacion input, int metodo, BigDecimal ID_PAIS) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (metodo == Conf.METODO_GET) {
            if (input.getIdSincronizacion() != null && !input.getIdSincronizacion().trim().equals("")) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Sincronizacion.N_TABLA,
                        Sincronizacion.CAMPO_TCSCSINCVENDEDORID, input.getIdSincronizacion()));
            }

            if (input.getIdJornada() != null && !input.getIdJornada().trim().equals("")) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Sincronizacion.N_TABLA,
                        Sincronizacion.CAMPO_TCSCJORNADAVENDID, input.getIdJornada()));
            }

            if (input.getIdDispositivo() != null && !input.getIdDispositivo().trim().equals("")) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Sincronizacion.N_TABLA,
                        Sincronizacion.CAMPO_COD_DISPOSITIVO, input.getIdDispositivo()));
            }

            if (input.getIdVendedor() != null && !input.getIdVendedor().trim().equals("")) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Sincronizacion.N_TABLA,
                        Sincronizacion.CAMPO_IDVENDEDOR, input.getIdVendedor()));
            }
        }

        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Sincronizacion.N_TABLA,
                Sincronizacion.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
        
        return condiciones;
    }
    
   /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo M\u00E9todo que indica las acciones a realizar.
     * @return output Respuesta y listado con los sincronizaciones encontrados.
     */
    public OutputSincronizacion getDatos(InputSincronizacion input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        OutputSincronizacion output = new OutputSincronizacion();
        Respuesta respuesta = new Respuesta();
        
        Connection conn = null;
        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());
            log.trace("Usuario: " + input.getUsuario());

            String campos[] = { Catalogo.CAMPO_NOMBRE, Catalogo.CAMPO_VALOR };

            // Se obtienen todas las configuraciones.
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_JORNADA_ESTADOS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_JORNADA_ESTADOS_LIQ));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_PANELRUTA));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ESTADOS));

            List<Map<String, String>> datosConfig = new ArrayList<Map<String, String>>();
            try {
                datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, null);
            } catch (SQLException e) {
                log.error("Error al obtener par\u00E9metros de configuraci\u00F3n. " + e);
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_GET_CONFIG_103, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output = new OutputSincronizacion();
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SINCRONIZACION, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO,
                        "Problema al obtener par\u00E9metros de configuraci\u00F3n la validaci\u00F3n de datos.",
                        respuesta.getDescripcion()));

                return output;
            }

            String tipoPanel = "";
            String tipoRuta = "";
            String estadoIniciada = "";
            String estadoLiquidada = "";

            for (int i = 0; i < datosConfig.size(); i++) {
                if (Conf.TIPO_PANEL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoPanel = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_RUTA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoRuta = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.JORNADA_ESTADO_INICIADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoIniciada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.JORNADA_ESTADO_LIQUIDADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoLiquidada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
            }

            // Validaci\u00F3n de datos en el input
            respuesta = validarInput(conn, input, metodo);
            log.trace("Respuesta validaci\u00F3n: "
                    + (respuesta.getDescripcion() == null ? "OK" : respuesta.getDescripcion()));
            if (respuesta.getCodResultado() != null) {
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SINCRONIZACION, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));
                log.trace("ingreso como error al validar datos");	
                return output;
            }

            if (metodo == Conf.METODO_GET) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionSincronizacion.doGet(conn, input, metodo, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de sincronizaciones.", ""));
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output.setRespuesta(respuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de sincronizaciones.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_GET - 1) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionSincronizacion.doGetFaltantes(conn, input, estadoAlta, tipoPanel, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de sincronizaciones.", ""));
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output.setRespuesta(respuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de sincronizaciones.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_POST) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
                try {
                    output = OperacionSincronizacion.doPost(conn, input, estadoAlta, tipoPanel, tipoRuta,
                            estadoIniciada,  estadoLiquidada, ID_PAIS);
                    log.trace("Respuesta metodo Post: ");
                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_SINCRONIZACION_56) {
                   
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SINCRONIZACION, servicioPost,
                                "0", Conf.LOG_TIPO_SINCRONIZACION,
                                "Se registr\u00F3 una nueva sincronizaci\u00F3n del dispositivo ID " + input.getIdDispositivo() + ".", ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SINCRONIZACION, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema al crear Sincronizacion.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output.setRespuesta(respuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SINCRONIZACION, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al crear sincronizaciones.", e.getMessage()));
                }

            }
        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SINCRONIZACION, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de sincronizaciones.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
