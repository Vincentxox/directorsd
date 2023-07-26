package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.anulacion.InputAnulacion;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.anulacion.OutputAnulacion;
import com.consystec.sc.sv.ws.operaciones.OperacionAnulacion;
import com.consystec.sc.sv.ws.orm.AnulacionVenta;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.orm.Venta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.ericsson.sdr.dao.OlsDAO;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class CtrlAnulacion extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlAnulacion.class);
    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_ANULACION;
    private static String servicioPost = Conf.LOG_POST_ANULACION;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al
     * servicio.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return
     * @throws SQLException
     */
    public Respuesta validarInput(Connection conn, InputAnulacion input, int metodo) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        List<Filtro> condiciones = new ArrayList<Filtro>();
        String fechaRegistroVenta = "";
        SimpleDateFormat FECHAHORA2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat FORMATO_TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss");
        log.debug("Validando datos...");

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            return getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        if (metodo == Conf.METODO_POST) {
            //se valida la jornada
            if (input.getIdJornada() == null || "".equals(input.getIdJornada())) {
                return getMensaje(Conf_Mensajes.MSJ_IDJORNADA_NULO_89, null, nombreClase, nombreMetodo, null, input.getCodArea());

            } else if (!isNumeric(input.getIdJornada())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDJORNADA_NUM_761, null, nombreClase, nombreMetodo, null, input.getCodArea());

            } else {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCJORNADAVENID, input.getIdJornada()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condiciones.add(UtileriasJava.setCondicionConfig(Jornada.CAMPO_ESTADO, Conf.GRUPO_JORNADA_ESTADOS, Conf.JORNADA_ESTADO_INICIADA, input.getCodArea()));

                if (UtileriasBD.selectCount(conn, ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones) <= 0) {
                    return getMensaje(Conf_Mensajes.MSJ_JORNADA_NOEXISTE_93, null, nombreClase, nombreMetodo, null, input.getCodArea());
                }
            }

            // se valida el vendedor y la venta
            if (input.getIdVendedor() == null || "".equals(input.getIdVendedor())) {
                return getMensaje(Conf_Mensajes.MSJ_VENDEDOR_NULO_38, null, nombreClase, nombreMetodo, null, input.getCodArea());

            } else if (!isNumeric(input.getIdVendedor())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase, nombreMetodo, null, input.getCodArea());

            } else if (input.getIdVenta() == null || "".equals(input.getIdVenta()) || !isNumeric(input.getIdVenta())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENTA_360, null, nombreClase, nombreMetodo, null, input.getCodArea());

            } else {
                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Venta.CAMPO_TCSCVENTAID, input.getIdVenta()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Venta.CAMPO_VENDEDOR, input.getIdVendedor()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Venta.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));

                fechaRegistroVenta = UtileriasBD.getOneRecord(conn, Venta.CAMPO_FECHA_EMISION, ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones);
                if (fechaRegistroVenta == null || "".equals(fechaRegistroVenta)) {
                    return getMensaje(Conf_Mensajes.MSJ_ERROR_VENTA_ANULACION_799, null, nombreClase, nombreMetodo, null, input.getCodArea());
                }

                condiciones.add(UtileriasJava.setCondicionConfig(Venta.CAMPO_ESTADO, Conf.GRUPO_ESTADOS_VENTA, Conf.VENTA_ANULADO, input.getCodArea()));

                fechaRegistroVenta = UtileriasBD.getOneRecord(conn, Venta.CAMPO_FECHA_EMISION, ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones);
                if (fechaRegistroVenta != null && !"".equals(fechaRegistroVenta)) {
                    return getMensaje(Conf_Mensajes.MSJ_ERROR_VENTA_ANULADA_912, null, nombreClase, nombreMetodo, null, input.getCodArea());
                }

                condiciones.remove(3);
                condiciones.add(UtileriasJava.setCondicionConfig(Venta.CAMPO_ESTADO, Conf.GRUPO_ESTADOS_VENTA, Conf.VENTA_REGISTRADO_SIDRA, input.getCodArea()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IS_NULL_AND, Venta.CAMPO_ESTADO_FACT, null));

                fechaRegistroVenta = UtileriasBD.getOneRecord(conn, Venta.CAMPO_FECHA_EMISION, ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones);
                if (fechaRegistroVenta == null || "".equals(fechaRegistroVenta)) {
                    return getMensaje(Conf_Mensajes.MSJ_ERROR_VENTA_PROC_FACT_863, null, nombreClase, nombreMetodo, null, input.getCodArea());
                }
            }

            // Verificaci\u00F3n de fecha
            try {
                if (input.getFechaAnulacion() == null || "".equals(input.getFechaAnulacion().trim())) {
                    return getMensaje(Conf_Mensajes.MSJ_FECHANULO_40, null, nombreClase, nombreMetodo, null, input.getCodArea());

                } else {
                    Date fechaAnulacion = FORMATO_TIMESTAMP.parse(input.getFechaAnulacion());
                    log.trace("Fecha anulacion: " + fechaAnulacion);
                    Date fechaVenta = FECHAHORA2.parse(fechaRegistroVenta);
                    log.trace("Fecha de venta: " + fechaVenta);
                    if (fechaAnulacion.before(fechaVenta)) {
                        return getMensaje(Conf_Mensajes.MSJ_ERROR_FECHA_ANULACION_800, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    }
                }
            } catch (java.text.ParseException e) {
                log.error("Inconveniente al convertir la fecha en clase " + nombreMetodo + " m\u00E9todo " + nombreClase + ".", e);

                return getMensaje(Conf_Mensajes.MSJ_ERROR_CONVERTIR_FECHA_165, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }
        }

        if (metodo == Conf.METODO_GET) {
            if (input.getIdJornada() != null && !"".equals(input.getIdJornada()) && !isNumeric(input.getIdJornada())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDJORNADA_NUM_761, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }

            if (input.getIdVendedor() != null && !"".equals(input.getIdVendedor()) && !isNumeric(input.getIdVendedor())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }

            if (input.getIdVenta() != null && !"".equals(input.getIdVenta())) {
                if (!isNumeric(input.getIdVenta())) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENTA_360, null, nombreClase, nombreMetodo, null, input.getCodArea());
                }
            } else {
                // Filtro rango de fechas
                if (input.getFechaInicio() == null || "".equals(input.getFechaInicio().trim())
                        || input.getFechaFin() == null || "".equals(input.getFechaFin().trim())) {
                    return getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, nombreClase, nombreMetodo, null, input.getCodArea());
                } else {
                    SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
                    Date fechaInicio = UtileriasJava.parseDate(input.getFechaInicio(), formatoFecha);
                    Date fechaFin = UtileriasJava.parseDate(input.getFechaFin(), formatoFecha);
                    long diferenciaDias = 0;
                    if (fechaInicio != null && fechaFin != null) {
                        if (fechaInicio.after(fechaFin)) {
                            return getMensaje(Conf_Mensajes.MSJ_FECHAFIN_MENOR_62, null, nombreClase, nombreMetodo, null, input.getCodArea());

                        } else {
                            diferenciaDias = fechaFin.getTime() - fechaInicio.getTime();
                            long dias = diferenciaDias / (1000 * 60 * 60 * 24);
                            int totalDias = (int) dias;
                            if (totalDias > 31) {
                                return getMensaje(Conf_Mensajes.MSJ_RANGO_FECHA_INVALIDO_511, null, nombreClase, nombreMetodo, null, input.getCodArea());
                            }
                        }
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
     * @return campos Array con los nombres de los campos.
     */
    public static String[][] obtenerCamposGet() {
        String campos[][] = {
            { AnulacionVenta.N_TABLA, AnulacionVenta.CAMPO_TCSCANULACIONID },
            { AnulacionVenta.N_TABLA, AnulacionVenta.CAMPO_TCSCJORNADAVENID },
            { AnulacionVenta.N_TABLA, AnulacionVenta.CAMPO_FECHA_ANULACION },
            { AnulacionVenta.N_TABLA, AnulacionVenta.CAMPO_VENDEDOR },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_USUARIO },
            { AnulacionVenta.N_TABLA, AnulacionVenta.CAMPO_TCSCVENTAID },
            { AnulacionVenta.N_TABLA, AnulacionVenta.CAMPO_OBSERVACIONES },
            { AnulacionVenta.N_TABLA, AnulacionVenta.CAMPO_CREADO_EL },
            { AnulacionVenta.N_TABLA, AnulacionVenta.CAMPO_CREADO_POR }
        };
        
        return campos;
    }
    
    public static String[] obtenerCamposPost() {
        String campos[] = {
            AnulacionVenta.CAMPO_TCSCANULACIONID,
            AnulacionVenta.CAMPO_TCSCCATPAISID,
            AnulacionVenta.CAMPO_TCSCJORNADAVENID,
            AnulacionVenta.CAMPO_FECHA_ANULACION,
            AnulacionVenta.CAMPO_VENDEDOR,
            AnulacionVenta.CAMPO_TCSCVENTAID,
            AnulacionVenta.CAMPO_OBSERVACIONES,
            AnulacionVenta.CAMPO_CREADO_EL,
            AnulacionVenta.CAMPO_CREADO_POR
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
    public static List<String> obtenerInsertsPost(InputAnulacion input, String sequencia, BigDecimal idPais) {
        List<String> inserts = new ArrayList<String>();
        String valores = "";
        
        valores = "("
                + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, idPais.toString(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdJornada(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsertFecha(Conf.INSERT_TIMESTAMP, input.getFechaAnulacion(), Conf.TXT_FORMATO_TIMESTAMP, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getIdVendedor(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getIdVenta(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getObservaciones(), Conf.INSERT_SEPARADOR_SI)
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
    public static List<Filtro> obtenerCondiciones(InputAnulacion input, int metodo, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (metodo == Conf.METODO_GET) {
            if (input.getIdJornada() != null && !"".equals(input.getIdJornada().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, AnulacionVenta.N_TABLA,
                        AnulacionVenta.CAMPO_TCSCJORNADAVENID, input.getIdJornada()));
            }
            
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, AnulacionVenta.N_TABLA, AnulacionVenta.CAMPO_TCSCCATPAISID, idPais.toString())); //Filtrar siempre por pais

            if (input.getIdVendedor() != null && !"".equals(input.getIdVendedor().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, AnulacionVenta.N_TABLA,
                        AnulacionVenta.CAMPO_VENDEDOR, input.getIdVendedor()));
            }
            if (input.getIdVenta() != null && !"".equals(input.getIdVenta().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, AnulacionVenta.N_TABLA,
                        AnulacionVenta.CAMPO_TCSCVENTAID, input.getIdVenta()));
            }

            if ((input.getFechaFin() != null && input.getFechaInicio() != null) && (!"".equals(input.getFechaFin().trim()) && !"".equals(input.getFechaInicio().trim()))) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_RANGO_FECHAS,
                            AnulacionVenta.N_TABLA + "." + AnulacionVenta.CAMPO_FECHA_ANULACION, input.getFechaInicio(),
                            input.getFechaFin(), Conf.TXT_FORMATO_FECHA_CORTA));
            }
        }

        return condiciones;
    }
    
    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo M\u00E9todo que indica las acciones a realizar.
     * @return output Respuesta y listado con los Anulacions encontrados.
     */
    public OutputAnulacion getDatos(InputAnulacion input, int metodo) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        COD_PAIS = input.getCodArea();

        OutputAnulacion output = new OutputAnulacion();
        Respuesta respuesta = new Respuesta();

        Connection conn = null;
        try {
            conn = getConnRegional();
           ID_PAIS = getIdPais(conn, input.getCodArea());
            log.trace("Usuario: " + input.getUsuario());

            // Validaci\u00F3n de datos en el input
            respuesta = validarInput(conn, input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + (respuesta.getDescripcion() == null ? "OK" : respuesta.getDescripcion()));
            if (respuesta.getCodResultado()!=null) {
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ANULACION, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Inconveniente en la validaci\u00F3n de datos.", respuesta.getDescripcion()));

                return output;
            }

            if (metodo == Conf.METODO_GET) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionAnulacion.doGet(conn, input, metodo, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de Anulaciones.", ""));
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output.setRespuesta(respuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Inconveniente al consultar datos de Anulaciones.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_POST) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
                try {
                    output = OperacionAnulacion.doPost(conn, input, listaLog, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_ANULACION_28) {
                    	
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ANULACION, servicioPost, output.getIdAnulacion(),
                                Conf.LOG_TIPO_ANULACION,
                                "Se registr\u00F3 anulaci\u00F3n de la venta con ID " + input.getIdVenta() + ".", ""));
                        cancelBillOLS(new BigDecimal(input.getIdVenta()));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ANULACION, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Inconveniente al crear Anulacion de Venta.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output.setRespuesta(respuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ANULACION, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Inconveniente al crear Anulaciones.", e.getMessage()));
                }
            }
        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ANULACION, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Inconveniente en el servicio de Anulaciones.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
    
    public void cancelBillOLS(BigDecimal tcscventaid) {
    	try (Connection conn = getConnRegional()){
    		OlsDAO ols = new OlsDAO();
    		ols.updateStatusBill(conn, tcscventaid,"ANULADA");
    	} catch (SQLException e) {
			log.error("Ocurrio un problema al anular la factura en OLS",e);
		}
    	
    }
}
