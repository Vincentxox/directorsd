package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.catalogo.InputCatalogo;
import com.consystec.sc.ca.ws.input.catalogo.InputParametro;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.catalogo.OutputCatalogo;
import com.consystec.sc.sv.ws.operaciones.OperacionCatalogo;
import com.consystec.sc.sv.ws.orm.Catalogo;
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
public class CtrlCatalogo extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlCatalogo.class);
    private static List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_CATALOGO;
    private static String servicioPost = Conf.LOG_POST_CATALOGO;
    private static String servicioPut = Conf.LOG_PUT_CATALOGO;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDe como recurso al
     * servicio.
     * 
     * @param input
     * @param metodo
     * @param estadoBaja 
     * @param estadoAlta 
     * @return
     * @throws SQLException
     */
    public Respuesta validarInput(InputCatalogo input, int metodo, String estadoAlta, String estadoBaja)
            throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        int index = 0;
        COD_PAIS = input.getCodArea();

        if (metodo == Conf.METODO_GET) {
        		log.trace("Metodo get");
        } else {
            if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
            if (input.getGrupoParametro() == null || "".equals(input.getGrupoParametro().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_GRUPO_289, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
        }

        if (metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE) {
            log.debug("Validando datos para edici\u00F3n en m\u00E9todos PUT o DELETE.");
            if (input.getParametros() != null && input.getParametros().size() > 1) {
                datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_UPD_RECURSO_UNITARIO, null, nombreClase,
                    nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }
        }

        if (metodo == Conf.METODO_DELETE) {
            log.debug("Validando datos para dar de baja.");
            if (input.getParametros() != null) {
                for (int i = 0; i < input.getParametros().size(); i++) {
                    String nombre = "";
                    if (input.getParametros().get(i).getNombre()!= null) {
                        nombre = input.getParametros().get(i).getNombre();
                    }

                    if (nombre == null || "".equals(nombre.trim())) {
                        index ++;
                        datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_PARAM_NOMBRE_283, null, nombreClase,
                                nombreMetodo, null, input.getCodArea()).getDescripcion();
                        flag = true;
                    }
                }
            } else {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_PARAM_284, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            }
        }

        if (metodo == Conf.METODO_PUT) {
            if (input.getParametros().get(0).getEstado() != null) {
                if (!input.getParametros().get(0).getEstado().equalsIgnoreCase(estadoBaja)
                        && !input.getParametros().get(0).getEstado().equalsIgnoreCase(estadoAlta)) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_TIPO_135, null, nombreClase,
                            nombreMetodo, estadoAlta + " o " + estadoBaja + ".", input.getCodArea()).getDescripcion();
                    flag = true;
                }
            } else {
                input.getParametros().get(0).setEstado(estadoAlta);
            }
        }

        //Se valida que los datos del recurso asociado no sean nulos.
        if (metodo == Conf.METODO_POST || metodo == Conf.METODO_PUT) {
            log.debug("Validando datos para creaci\u00F3n o modificaci\u00F3n.");
            if (input.getParametros() != null) {
                for (int i = 0; i < input.getParametros().size(); i++) {
                    String nombre = "";
                    String valor = "";
                    String descripcion = "";
                    if (flag){
                        break;
                    }
                    index++;

                    if (input.getParametros().get(i).getNombre() != null) {
                        nombre = input.getParametros().get(i).getNombre().trim();
                    }
                    if (input.getParametros().get(i).getValor() != null) {
                        valor = input.getParametros().get(i).getValor().trim();
                    }
                    if (input.getParametros().get(i).getDescripcion() != null) {
                        descripcion = input.getParametros().get(i).getDescripcion().trim();
                    }

                    if (nombre == null || "".equals(nombre)) {
                        datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_CONFIG_NOMBRE_285, null, nombreClase,
                                nombreMetodo, index + ".", input.getCodArea()).getDescripcion();
                        flag = true;
                    } else if (nombre.equalsIgnoreCase(Conf.TIPO_GRUPO_BONO)) {
                        return getMensaje(Conf_Mensajes.MSJ_ERROR_CONFIG_INVENTARIO_890, null, nombreClase,
                                nombreMetodo, "(" + Conf.TIPO_GRUPO_BONO + ")", input.getCodArea());
                    }
                    if (valor == null || "".equals(valor)) {
                        datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_CONFIG_VALOR_286, null, nombreClase,
                                nombreMetodo, index + ".", input.getCodArea()).getDescripcion();
                        flag = true;
                    } else if (valor.equalsIgnoreCase(Conf.TIPO_GRUPO_BONO)) {
                        return getMensaje(Conf_Mensajes.MSJ_ERROR_CONFIG_INVENTARIO_890, null, nombreClase,
                                nombreMetodo, "(" + Conf.TIPO_GRUPO_BONO + ")", input.getCodArea());
                    }
                    if (descripcion == null ||  "".equals(descripcion)) {
                        datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_CONFIG_DESC_287, null, nombreClase,
                                nombreMetodo, index + ".", input.getCodArea()).getDescripcion();
                        flag = true;
                    }
                }

                index = 1;
                for (InputParametro configActual : input.getParametros()) {
                    if (flag){
                        break;
                    }
                    int indexFolio = 1;

                    for (InputParametro config : input.getParametros()) {
                        if (flag){
                            break;
                        }

                        if (indexFolio != index && config.getNombre().toUpperCase().trim()
                                .equals(configActual.getNombre().toUpperCase().trim())) {
                            log.error("La configuraci\u00F3n #" + indexFolio + " es igual a la #" + index);

                            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_CONFIG_IGUALES_288, null,
                                    nombreClase, nombreMetodo, index + " y " + indexFolio, input.getCodArea()).getDescripcion();

                            flag = true;
                        }
                        indexFolio++;
                    }
                    index++;
                }
            } else {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_PARAM_284, null, nombreClase, nombreMetodo,
                        datosErroneos, input.getCodArea()).getDescripcion();
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
                Catalogo.CAMPO_GRUPO,
                Catalogo.CAMPO_NOMBRE,
                Catalogo.CAMPO_VALOR,
                Catalogo.CAMPO_DESCRIPCION,
                Catalogo.CAMPO_ESTADO,
                Catalogo.CAMPO_CREADO_EL,
                Catalogo.CAMPO_CREADO_POR,
                Catalogo.CAMPO_MODIFICADO_EL,
                Catalogo.CAMPO_MODIFICADO_POR,
                Catalogo.CAMPO_PARAM_INTERNO,
                Catalogo.CAMPO_TABLA,
                Catalogo.CAMPO_CAMPO_TABLA
            };
            return campos;
        } else {
            String campos[] = {
                Catalogo.CAMPO_TC_SC_CATALOGO_ID,
                Catalogo.CAMPO_TC_SC_CATPAIS_ID,
                Catalogo.CAMPO_GRUPO,
                Catalogo.CAMPO_NOMBRE,
                Catalogo.CAMPO_VALOR,
                Catalogo.CAMPO_DESCRIPCION,
                Catalogo.CAMPO_ESTADO,
                Catalogo.CAMPO_CREADO_EL,
                Catalogo.CAMPO_CREADO_POR,
                Catalogo.CAMPO_PARAM_INTERNO,
                Catalogo.CAMPO_TABLA,
                Catalogo.CAMPO_CAMPO_TABLA
            };
            return campos;
        }
    }
    
    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
     * @param conn 
     * 
     * @param input
     * @param sequencia
     * @return inserts
     * @throws SQLException 
     */
    public static List<String> obtenerInsertsPost(Connection conn, InputCatalogo input, String sequencia, BigDecimal idPais) throws SQLException {
        List<String> inserts = new ArrayList<String>();
        String valores = "";
        String nombreTabla = "";
        String nombreCampo = "";
        
        String[] campos = {
            Catalogo.CAMPO_PARAM_INTERNO,
            Catalogo.CAMPO_TABLA,
            Catalogo.CAMPO_CAMPO_TABLA
        };
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, input.getGrupoParametro()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
        
        Map<String, String> datosParametro = UtileriasBD.getSingleFirstData(conn, Catalogo.N_TABLA, campos, condiciones);
        
        String tipoParametro = datosParametro.get(Catalogo.CAMPO_PARAM_INTERNO);

        /* 
         * Tipos:
         * 0 = Externo
         * 1 = Interno
         * 2 = H\u00EDbrido
         */
        if ("0".equals(tipoParametro)) {
            nombreTabla = datosParametro.get(Catalogo.CAMPO_TABLA);
            nombreCampo = datosParametro.get(Catalogo.CAMPO_CAMPO_TABLA);
        } else {
            nombreTabla = Conf.NOMBRE_GENERICO;
            nombreCampo = Conf.NOMBRE_GENERICO;
        }
        
        
        for (int i = 0; i < input.getParametros().size(); i++) {
            valores = ""
                + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPais.toString(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getGrupoParametro(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getParametros().get(i).getNombre(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getParametros().get(i).getValor(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getParametros().get(i).getDescripcion(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea()), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, tipoParametro, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, nombreTabla, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, nombreCampo, Conf.INSERT_SEPARADOR_NO)
            + " ";
            inserts.add(valores);
        }
        
        return inserts;
    }
    
    /**
     * Funci\u00F3n que indica los campos que se utilizar\u00E9n para modificaciones  
     * al recurso en los m\u00E9todos PUT y DELETE.
     * 
     * @param input
     * @param metodo
     * @return campos
     */
    public static String[][] obtenerCamposPutDel(InputCatalogo input, int metodo, String estadoBaja) {
        //Los valores que sean tipo texto deben ir entre ap\u00F3strofres o comillas simples.
        if (metodo == Conf.METODO_DELETE) {
            String campos[][] = {
                { Catalogo.CAMPO_ESTADO, "'" + estadoBaja + "'" },
                { Catalogo.CAMPO_MODIFICADO_EL, "sysdate" },
                { Catalogo.CAMPO_MODIFICADO_POR, "'" + input.getUsuario() + "'" }
            };
            return campos;
        } else {
            String campos[][] = {
                { Catalogo.CAMPO_VALOR, "'" + input.getParametros().get(0).getValor() + "'" },
                { Catalogo.CAMPO_DESCRIPCION, "'" + input.getParametros().get(0).getDescripcion() + "'" },
                { Catalogo.CAMPO_ESTADO, "UPPER('" + input.getParametros().get(0).getEstado() + "')" },
                { Catalogo.CAMPO_MODIFICADO_EL, "sysdate" },
                { Catalogo.CAMPO_MODIFICADO_POR, "'" + input.getUsuario() + "'" }
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
    public static List<Filtro> obtenerCondiciones(InputCatalogo input, int metodo, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));

        if (input.getGrupoParametro() != null && ! "".equals(input.getGrupoParametro())) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO,
                    input.getGrupoParametro()));
        }
        if (input.getParametros() != null && (input.getParametros().get(0).getNombre() != null && ! "".equals(input.getParametros().get(0).getNombre()))) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE,
                        input.getParametros().get(0).getNombre()));
        }

        if (metodo == Conf.METODO_GET && input.getParametros() != null) {
                if (input.getParametros().get(0).getEstado() != null && ! "".equals(input.getParametros().get(0).getEstado())) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO,
                            input.getParametros().get(0).getEstado()));
                }
                if (input.getParametros().get(0).getTipoParametro() != null
                        && ! "".equals(input.getParametros().get(0).getTipoParametro())) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Catalogo.CAMPO_PARAM_INTERNO,
                            input.getParametros().get(0).getTipoParametro()));
                }
        }
        return condiciones;
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las consultas de verificaci\u00F3n
     * de existencia del recurso.
     * 
     * @param input
     * @return condiciones
     */
    public static List<Filtro> obtenerCondicionesExistencia(InputCatalogo input, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));

        if (!"".equals(input.getGrupoParametro())) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, input.getGrupoParametro()));
        }

        for (int i = 0; i < input.getParametros().size(); i++) {
            if (!"".equals(input.getParametros().get(i).getNombre())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE, input.getParametros().get(i).getNombre()));
            }
        }

        return condiciones;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     * @param metodo
     * @return respuestaCatalogo
     */
    public OutputCatalogo getDatos(InputCatalogo input, int metodo) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        OutputCatalogo output = null;
        Respuesta r = new Respuesta();

        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();
            ID_PAIS = getIdPais(conn, input.getCodArea());
            
            String estadoBaja = UtileriasJava.getConfig(null, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, input.getCodArea());
            String estadoAlta = UtileriasJava.getConfig(null, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
            
            // Validaci\u00F3n de datos en el input
            r = validarInput(input, metodo, estadoAlta, estadoBaja);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equalsIgnoreCase(r.getDescripcion())) {
                output = new OutputCatalogo();
                output.setRespuesta(r);

                listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_CATALOGO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                        "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                try {
                    output = OperacionCatalogo.doGet(conn, input, metodo, ID_PAIS);

                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputCatalogo();
                    output.setRespuesta(r);
                    
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO,
                            "Problema al consultar datos de cat\u00E9logos.", e.getMessage()));
                }
            
            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
            } else if (metodo == Conf.METODO_POST) {
                try {
                    output = OperacionCatalogo.doPost(conn, input, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_CATALOGO_33) {
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_CATALOGO, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Se crearon cat\u00E9logos de configuraci\u00F3n en el grupo de nombre "
                                        + input.getGrupoParametro().toUpperCase() + ".",
                                ""));
                    } else {
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_CATALOGO, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema al crear cat\u00E9logos.", output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputCatalogo();
                    output.setRespuesta(r);

                    listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_CATALOGO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                            "Problema al crear cat\u00E9logos.", e.getMessage()));
                }
                
            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT o DELETE
            } else if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
                try {
                    output = OperacionCatalogo.doPutDel(conn, input, metodo,  estadoBaja, ID_PAIS);
                    
                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_MOD_CATALOGO_34) {
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_CATALOGO, servicioPut, "0", Conf.LOG_TIPO_NINGUNO,
                                "Se modificaron datos del cat\u00E9logo de nombre "
                                        + input.getParametros().get(0).getNombre().toUpperCase() + ".",
                                ""));
                    } else {
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_CATALOGO, servicioPut, "0", Conf.LOG_TIPO_NINGUNO,
                                "Problema al modificar datos de cat\u00E9logos.", output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputCatalogo();
                    output.setRespuesta(r);

                    listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_CATALOGO, servicioPut, "0", Conf.LOG_TIPO_NINGUNO,
                            "Problema al modificar cat\u00E9logo.", e.getMessage()));
                }
            }
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputCatalogo();
            output.setRespuesta(r);

            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_CATALOGO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                    "Problema en el servicio de cat\u00E9logos.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
