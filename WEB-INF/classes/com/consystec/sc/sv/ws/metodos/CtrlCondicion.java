package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.condicion.InputCondicionPrincipal;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.condicion.OutputCondicion;
import com.consystec.sc.sv.ws.operaciones.OperacionCondicion;
import com.consystec.sc.sv.ws.orm.ArticulosSidra;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.Condicion;
import com.consystec.sc.sv.ws.orm.CondicionCampania;
import com.consystec.sc.sv.ws.orm.OfertaCampania;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes - Consystec - 2016
 *
 */
public class CtrlCondicion extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlCondicion.class);
    private List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_CONDICION;
    private static String servicioPost = Conf.LOG_POST_CONDICION;
    private static String servicioPut = Conf.LOG_PUT_CONDICION;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    
    /***
     * Validando que no vengan par\u00E9metros nulos
     * 
     * @param conn
     * @param metodo
     * @param estadoAlta 
     * @throws SQLException
     */
    public Respuesta validarDatos(Connection conn, InputCondicionPrincipal objDatos, int metodo, String estadoAlta, BigDecimal idPais)
            throws SQLException {
        Respuesta objRespuesta = null;
        String nombreMetodo = "validarDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, this.getClass().toString(), null, nombreMetodo,
                    null, objDatos.getCodArea());
            return objRespuesta;
        }

        if (metodo == Conf.METODO_POST) {
            if (objDatos.getIdOfertaCampania() == null || "".equals(objDatos.getIdOfertaCampania().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDOFERTACAMPANIA_NULO_95, this.getClass().toString(), null,
                        nombreMetodo, null, objDatos.getCodArea());
                return objRespuesta;
            } else {
                if (!isNumeric(objDatos.getIdOfertaCampania())) {
                    // error, no numerico
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_OFERTACAMP_NUM_221,
                            this.getClass().toString(), nombreClase, nombreMetodo, null, objDatos.getCodArea());
                    return objRespuesta;
                }
            }

            if (objDatos.getCondiciones() == null || objDatos.getCondiciones().isEmpty()) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_CONDICION_NULA_402, this.getClass().toString(), null,
                        nombreMetodo, null, objDatos.getCodArea());
                return objRespuesta;
            } else {
                for (int a = 0; a < objDatos.getCondiciones().size(); a++) {
                    if (objDatos.getCondiciones().get(a).getNombre() == null
                            || "".equals(objDatos.getCondiciones().get(a).getNombre().trim())) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOMBRE_NULO_96, this.getClass().toString(), null,
                                nombreMetodo, "En la condición " + (a + 1) + ".", objDatos.getCodArea());
                        return objRespuesta;
                    }

                    if (objDatos.getCondiciones().get(a).getTipoGestion() == null
                            || "".equals(objDatos.getCondiciones().get(a).getTipoGestion().trim())) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPOGESTION_NULO_97, this.getClass().toString(),
                                null, nombreMetodo, "En la condición " + (a + 1) + ".", objDatos.getCodArea());
                        return objRespuesta;
                    }

                    if (objDatos.getCondiciones().get(a).getTipoCondicion() == null
                            || "".equals(objDatos.getCondiciones().get(a).getTipoCondicion().trim())) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPO_CONDICION_NULO_414, this.getClass().toString(),
                                null, nombreMetodo, "En la condición " + (a + 1) + ".",objDatos.getCodArea());
                        return objRespuesta;
                    }

                    if (objDatos.getCondiciones().get(a).getDetalle() == null
                            || objDatos.getCondiciones().get(a).getDetalle().isEmpty()) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_DET_CONDICION_NULO_401, this.getClass().toString(),
                                null, nombreMetodo, "En la condición " + (a + 1) + ".",objDatos.getCodArea());
                        return objRespuesta;
                    } else {
                        if (objDatos.getCondiciones().get(a).getDetalle().size() > 2) {
                            // error, solo se pueden enviar dos detalles o menos
                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_CONDICION_MAX_DETALLE_417,
                                    this.getClass().toString(), null, nombreMetodo, "En la condición " + (a + 1) + ".",objDatos.getCodArea());
                            return objRespuesta;
                        }
                        for (int b = 0; b < objDatos.getCondiciones().get(a).getDetalle().size(); b++) {
                            String tipo = "";
                            String idArticulo = "";
                            String montoInicial = "";
                            String montoFinal = "";
                            String tecnologia = "";

                            if (objDatos.getCondiciones().get(a).getDetalle().get(b).getTipo() != null) {
                                tipo = objDatos.getCondiciones().get(a).getDetalle().get(b).getTipo().trim();
                            }

                            if (objDatos.getCondiciones().get(a).getDetalle().get(b).getIdArticulo() != null) {
                                idArticulo = objDatos.getCondiciones().get(a).getDetalle().get(b).getIdArticulo().trim();

                                if (!isNumeric(idArticulo)) {
                                    // error, no numerico
                                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDARTICULO_NUM_131,
                                            this.getClass().toString(), nombreClase, nombreMetodo,
                                            "En el detalle " + (b + 1) + " de la condicion " + (a + 1) + ".",objDatos.getCodArea());
                                    return objRespuesta;
                                }
                            }

                            // agregadas condiciones por tecnologia
                            if (objDatos.getCondiciones().get(a).getDetalle().get(b).getTecnologia() != null) {
                                tecnologia = objDatos.getCondiciones().get(a).getDetalle().get(b).getTecnologia().trim();

                                if ("".equals(tecnologia)) {
                                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TECNOLOGIA_842,
                                            this.getClass().toString(), nombreClase, nombreMetodo,
                                            "En el detalle " + (b + 1) + " de la condicion " + (a + 1) + ".",objDatos.getCodArea());
                                } else {
                                    // se verifica que exista configurado el tipo de tecnologia
                                    tecnologia = UtileriasJava.getConfig(conn, Conf.GRUPO_TIPO_TECNOLOGIA, tecnologia, objDatos.getCodArea());

                                    if (tecnologia == null || "".equals(tecnologia)) {
                                        return getMensaje(Conf_Mensajes.MSJ_ERROR_TIPO_TECNOLOGIA_843,
                                                this.getClass().toString(), nombreClase, nombreMetodo,
                                                "En el detalle " + (b + 1) + " de la condicion " + (a + 1) + ".",objDatos.getCodArea());
                                    }
                                }
                            }

                            if (objDatos.getCondiciones().get(a).getDetalle().get(b).getMontoInicial() != null
                                    && !"".equals(objDatos.getCondiciones().get(a).getDetalle().get(b).getMontoInicial())) {
                                montoInicial = objDatos.getCondiciones().get(a).getDetalle().get(b).getMontoInicial().trim();

                                if (!isDecimal(montoInicial)) {
                                    // error, no numerico
                                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_ELEMENTO_DET_MONTOINICIAL_404,
                                            this.getClass().toString(), nombreClase, nombreMetodo,
                                            "En el detalle " + (b + 1) + " de la condicion " + (a + 1) + ".",objDatos.getCodArea());
                                    return objRespuesta;
                                }
                            }

                            if ("".equals(idArticulo)&& (objDatos.getCondiciones().get(a).getDetalle().get(b).getMontoFinal() != null
                                        && !"".equals(objDatos.getCondiciones().get(a).getDetalle().get(b).getMontoFinal()))) {
                                    montoFinal = objDatos.getCondiciones().get(a).getDetalle().get(b).getMontoFinal().trim();

                                    if (!isDecimal(montoFinal)) {
                                        // error, no numerico
                                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_ELEMENTO_DET_MONTOFINAL_405,
                                                this.getClass().toString(), nombreClase, nombreMetodo,
                                                "En el detalle " + (b + 1) + " de la condicion " + (a + 1) + ".",objDatos.getCodArea());
                                        return objRespuesta;
                                    }
                            }

                            if (!"".equals(tipo)) {
                                // verificar que el tipo es v\u00E9lido
                                List<Filtro> condiciones = new ArrayList<Filtro>();
                                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
                                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, Conf.GRUPO_CONDICION_TIPOCAMPANIA));
                                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_VALOR, tipo));
                                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));

                                if (UtileriasBD.selectCount(conn, Catalogo.N_TABLA, condiciones) < 1) {
                                    log.error("No existe el tipo.");
                                    objRespuesta = new ControladorBase().getMensaje(
                                            Conf_Mensajes.MSJ_ERROR_TIPOOFERTA_NO_DEFINIDO_424, null, nombreClase,
                                            nombreMetodo, "En la condición " + (b + 1) + ".",objDatos.getCodArea());

                                    return objRespuesta;
                                }

                                if (!"".equals(idArticulo)) {
                                    //error no tiene que tener articulo porque ya tiene tipo
                                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_CONDICION_TIPO_ARTICULO_406,
                                            this.getClass().toString(), nombreClase, nombreMetodo,
                                            "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                    return objRespuesta;
                                }
                            }

                 
                        }
                    }
                }
            }
        }

        if (metodo == Conf.METODO_DELETE) {
            if (objDatos.getIdCondicion() == null || "".equals(objDatos.getIdCondicion().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_CONDICION_NULO_418, this.getClass().toString(), null,
                        nombreMetodo, null, objDatos.getCodArea());
                return objRespuesta;
            } else {
                if (!isNumeric(objDatos.getIdCondicion())) {
                    // error, no numerico
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_CONDICION_NUM_413, this.getClass().toString(),
                            nombreClase, nombreMetodo, null, objDatos.getCodArea());
                    return objRespuesta;
                }
            }
        }

        if (metodo == Conf.METODO_GET) {
            if ((objDatos.getIdOfertaCampania() != null && !"".equals(objDatos.getIdOfertaCampania().trim()))&&!isNumeric(objDatos.getIdOfertaCampania())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_OFERTACAMP_NUM_221,
                            this.getClass().toString(), nombreClase, nombreMetodo, null, objDatos.getCodArea());
                    return objRespuesta;
            }
            if ((objDatos.getIdCondicion() != null && !"".equals(objDatos.getIdCondicion().trim()))&& !isNumeric(objDatos.getIdCondicion())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_CONDICION_NUM_413, this.getClass().toString(),
                            nombreClase, nombreMetodo, null, objDatos.getCodArea());
                    return objRespuesta;
            }
            if ((objDatos.getIdArticulo() != null && !"".equals(objDatos.getIdArticulo().trim())) && !isNumeric(objDatos.getIdArticulo())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDARTICULO_NUM_131,
                            this.getClass().toString(), nombreClase, nombreMetodo, null, objDatos.getCodArea());
                    return objRespuesta;
            }
        }

        if (objRespuesta == null) {
            objRespuesta = new Respuesta();
            objRespuesta.setDescripcion("OK");
        }

        return objRespuesta;
    }
    
    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en el m\u00E9todo GET.
     * 
     * @return
     */
    public static String[][] obtenerCamposGet() {
        String campos[][] = {
            { Condicion.N_TABLA, Condicion.CAMPO_TCSCCONDICIONID },
            { Condicion.N_TABLA, Condicion.CAMPO_TCSCOFERTACAMPANIAID },
            { OfertaCampania.N_TABLA, OfertaCampania.CAMPO_NOMBRE + " AS NOMBRECAMPANIA" },
            { Condicion.N_TABLA, Condicion.CAMPO_NOMBRE },
            { Condicion.N_TABLA, Condicion.CAMPO_TIPO_GESTION },
            { Condicion.N_TABLA, Condicion.CAMPO_TIPO_CONDICION },
            { Condicion.N_TABLA, Condicion.CAMPO_ESTADO },
            { Condicion.N_TABLA, Condicion.CAMPO_CREADO_EL },
            { Condicion.N_TABLA, Condicion.CAMPO_CREADO_POR },
            { Condicion.N_TABLA, Condicion.CAMPO_MODIFICADO_EL },
            { Condicion.N_TABLA, Condicion.CAMPO_MODIFICADO_POR }
        };
        
        return campos;
    }
    
    /**
     * Funci\u00F3n que indica los campos que se insertar\u00E9n en el m\u00E9todo POST.
     * 
     * @return
     */
    public static String[] obtenerCamposPost() {
        String campos[] = {
            Condicion.CAMPO_TCSCCONDICIONID,
            Condicion.CAMPO_TCSCCATPAISID,
            Condicion.CAMPO_TCSCOFERTACAMPANIAID,
            Condicion.CAMPO_NOMBRE,
            Condicion.CAMPO_TIPO_GESTION,
            Condicion.CAMPO_TIPO_CONDICION,
            Condicion.CAMPO_TIPO_OFERTACAMPANIA,
            Condicion.CAMPO_ESTADO,
            Condicion.CAMPO_CREADO_EL,
            Condicion.CAMPO_CREADO_POR
        };
        return campos;
    }
    
    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET 
     * de la tabla relacionada.
     * 
     * @return
     */
    public static String[] obtenerCamposTablaHijaGet(BigDecimal idPais) {
        String campos[] = {
            CondicionCampania.CAMPO_TCSCDETCONDICIONCAMPANIAID,
            CondicionCampania.CAMPO_TCSCCONDICIONID,
            CondicionCampania.CAMPO_TIPO,
            CondicionCampania.CAMPO_ARTICULO,
                "(SELECT A." + ArticulosSidra.CAMPO_DESCRIPCION + " FROM " + ArticulosSidra.N_TABLA + " A WHERE TCSCCATPAISID = " + idPais.toString()
                + " AND A." + ArticulosSidra.CAMPO_ARTICULO + " = B." + CondicionCampania.CAMPO_ARTICULO + ") AS NOMBREART",
            CondicionCampania.CAMPO_TECNOLOGIA,
            CondicionCampania.CAMPO_MONTO_INICIAL,
            CondicionCampania.CAMPO_MONTO_FINAL,
            CondicionCampania.CAMPO_ESTADO,
            CondicionCampania.CAMPO_CREADO_EL,
            CondicionCampania.CAMPO_CREADO_POR,
            CondicionCampania.CAMPO_MODIFICADO_EL,
            CondicionCampania.CAMPO_MODIFICADO_POR
        };
        
        return campos;
    }

    /**
     * Funci\u00F3n que indica los campos que se insertar\u00E9n en el m\u00E9todo POST
     * de la tabla relacionada.
     * 
     * @return
     */
    public static String[] obtenerCamposTablaHijaPost() {
        String campos[] = {
            CondicionCampania.CAMPO_TCSCDETCONDICIONCAMPANIAID,
            CondicionCampania.CAMPO_TCSCCATPAISID,
            CondicionCampania.CAMPO_TCSCCONDICIONID,
            CondicionCampania.CAMPO_TIPO,
            CondicionCampania.CAMPO_ARTICULO,
            CondicionCampania.CAMPO_TECNOLOGIA,
            CondicionCampania.CAMPO_MONTO_INICIAL,
            CondicionCampania.CAMPO_MONTO_FINAL,
            CondicionCampania.CAMPO_ESTADO,
            CondicionCampania.CAMPO_CREADO_EL,
            CondicionCampania.CAMPO_CREADO_POR
        };
        
        return campos;
    }
    
    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return condiciones Listado con los filtros indicados seg\u00FAn el m\u00E9todo deseado.
     */
    public static List<Filtro> obtenerCondiciones( InputCondicionPrincipal input, int metodo, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        List<Filtro> condicionesExtra = new ArrayList<Filtro>();

        if (metodo == Conf.METODO_DELETE && !"".equals(input.getIdCondicion())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Condicion.CAMPO_TCSCCONDICIONID, input.getIdCondicion()));
        }

        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Condicion.N_TABLA, Condicion.CAMPO_TCSCCATPAISID, idPais.toString()));

        if (metodo == Conf.METODO_GET) {
            if (input.getIdCondicion() != null && !"".equals(input.getIdCondicion())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Condicion.N_TABLA, Condicion.CAMPO_TCSCCONDICIONID, input.getIdCondicion()));
            }
            if (input.getIdOfertaCampania() != null && !"".equals(input.getIdOfertaCampania())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Condicion.N_TABLA, Condicion.CAMPO_TCSCOFERTACAMPANIAID, input.getIdOfertaCampania()));
            }
            if (input.getNombre() != null && !"".equals(input.getNombre())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Condicion.N_TABLA, Condicion.CAMPO_NOMBRE, input.getNombre()));
            }
            if (input.getTipoGestion() != null && !"".equals(input.getTipoGestion())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Condicion.N_TABLA, Condicion.CAMPO_TIPO_GESTION, input.getTipoGestion()));
            }

            String[] campos = { CondicionCampania.CAMPO_TCSCCONDICIONID };
            if (input.getTipo() != null && !"".equals(input.getTipo())) {
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, CondicionCampania.CAMPO_TIPO, input.getTipo()));
            }
            if (input.getIdArticulo() != null && !"".equals(input.getIdArticulo())) {
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CondicionCampania.CAMPO_ARTICULO, input.getIdArticulo()));
            }

            if (!condicionesExtra.isEmpty()) {
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CondicionCampania.CAMPO_TCSCCATPAISID, idPais.toString()));
                String selectSQL = UtileriasBD.armarQuerySelect(CondicionCampania.N_TABLA, campos, condicionesExtra);
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, CondicionCampania.CAMPO_TCSCCONDICIONID, selectSQL));
            }
        }

        return condiciones;
    }
    
    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo 
     * @return output Respuesta y listado con los Condicions encontrados.
     */
    public OutputCondicion getDatos(InputCondicionPrincipal input, int metodo) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta r = new Respuesta();
        OutputCondicion output = null;
        COD_PAIS = input.getCodArea();

        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();
            ID_PAIS = getIdPais(conn, input.getCodArea());
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            r = validarDatos(conn, input, metodo, estadoAlta, ID_PAIS);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equalsIgnoreCase(r.getDescripcion())) {
                output = new OutputCondicion();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CONDICION, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            if (metodo == Conf.METODO_GET) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionCondicion.doGet(conn, input, metodo, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de condiciones de campa\u00F1a.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputCondicion();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de condiciones de campa\u00F1a.",
                            e.getMessage()));
                }

            } else if (metodo == Conf.METODO_POST) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
                try {
                    output = OperacionCondicion.doPost(conn, input, estadoAlta, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_CONDICION_35) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CONDICION, servicioPost,
                                output.getIdCondicion(), Conf.LOG_TIPO_CONDICION,
                                "Se cre\u00F3 condici\u00F3n con ID " + output.getIdCondicion() + " para la campa\u00F1a "
                                        + input.getIdOfertaCampania() + ".",
                                ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CONDICION, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema al crear condici\u00F3n de campa\u00F1a.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputCondicion();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CONDICION, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al crear condici\u00F3n de campa\u00F1a.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_DELETE) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo DELETE
                try {
                    output = OperacionCondicion.doDel(conn, input, metodo, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_DEL_CONDICION_37) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CONDICION, servicioPut,
                                input.getIdCondicion(), Conf.LOG_TIPO_CONDICION,
                                "Se di\u00F3 de baja la condici\u00F3n de campa\u00F1a con ID " + input.getIdCondicion() + ".", ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CONDICION, servicioPut,
                                input.getIdCondicion(), Conf.LOG_TIPO_CONDICION,
                                "Problema al modificar datos de condici\u00F3n de campa\u00F1a.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputCondicion();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CONDICION, servicioPut,
                            input.getIdCondicion(), Conf.LOG_TIPO_CONDICION,
                            "Problema al modificar condici\u00F3n de campa\u00F1a.", e.getMessage()));
                }
            }
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputCondicion();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CONDICION, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de condiciones de campa\u00F1a.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
