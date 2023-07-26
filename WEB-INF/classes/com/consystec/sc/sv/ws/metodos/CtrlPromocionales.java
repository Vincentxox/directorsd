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
import com.consystec.sc.ca.ws.input.promocionales.InputPromocionales;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.promocionales.OutputPromocionales;
import com.consystec.sc.sv.ws.operaciones.OperacionPromocionales;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.Promocionales;
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
public class CtrlPromocionales extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlPromocionales.class);
    private static String servicioGet = Conf.LOG_GET_PROMOCIONALES;
    private static String servicioPost = Conf.LOG_POST_PROMOCIONALES;
    private static String servicioPut = Conf.LOG_PUT_PROMOCIONALES;
    
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDe como recurso al servicio.
     * 
     * @param input
     * @param metodo
     * @return
     * @throws SQLException 
     */
    public Respuesta validarInput(Connection conn, InputPromocionales input, int metodo, BigDecimal ID_PAIS) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            datosErroneos += " Usuario.";
            flag = true;
        }

        if (metodo == Conf.METODO_POST || metodo == Conf.METODO_PUT) {
            log.debug("Validando datos para edici\u00F3n en m\u00E9todos POST.");

            if("503".equals(input.getCodArea())){
            	log.trace("validando idProductOffering");
	            if (input.getIdOferta() == null || "".equals(input.getIdOferta().trim())) {
	                datosErroneos += " IdProductOffering.";
	                flag = true;
	            }
            }
            if (input.getDescripcion() == null || "".equals(input.getDescripcion().trim())) {
                datosErroneos += " Descripcion.";
                flag = true;
            }

            List<Filtro> condicionesExistencia = new ArrayList<Filtro>();
            List<Map<String, String>> grupo;
            String existencia = "";
            if (!flag) {
                //Existencia del nombre.
                condicionesExistencia.clear();
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Promocionales.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Promocionales.CAMPO_DESCRIPCION, input.getDescripcion()));
                if (metodo == Conf.METODO_PUT) {
                    condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_NOT_IN_AND, Promocionales.CAMPO_TCSCARTPROMOCIONALID, input.getIdArtPromocional()));
                }
                existencia = UtileriasBD.verificarExistencia(conn, Promocionales.N_TABLA, condicionesExistencia);
                
                if (new Integer(existencia) > 0) {
                    log.error("Ya existe el recurso.");
                    String campos[] = {
                        Promocionales.CAMPO_DESCRIPCION,
                        Promocionales.CAMPO_TIPO_GRUPO,
                        Promocionales.CAMPO_ESTADO
                    };
                    grupo = UtileriasBD.getSingleData(conn, Promocionales.N_TABLA, campos,
                            condicionesExistencia, null);
                    datosErroneos += " El nombre enviado se encontr\u00F3 con el c\u00F3digo '"
                                    + grupo.get(0).get(Promocionales.CAMPO_DESCRIPCION) + "', en el grupo: "
                                    + grupo.get(0).get(Promocionales.CAMPO_TIPO_GRUPO) + " y con el estado: "
                                    + grupo.get(0).get(Promocionales.CAMPO_ESTADO);
                    flag = true;
                }
            }
            
            
            if (input.getTipoGrupo() == null ||"".equals(input.getTipoGrupo().trim())) {
                datosErroneos += " Tipo Grupo.";
                flag = true;
            }
            if (!flag) {
                //Se obtienen las configuraciones de grupos permitidos.
                condicionesExistencia.clear();
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, ID_PAIS.toString()));
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ART_PROMOCIONALES));
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE, input.getTipoGrupo()));
                existencia = UtileriasBD.verificarExistencia(conn, Catalogo.N_TABLA, condicionesExistencia);
                
                if (new Integer(existencia) < 1) {
                    log.error("No existe el grupo entre las configuraciones.");
                    datosErroneos += " Tipo Grupo no se encontr\u00F3 entre los grupos configurados, ingrese un grupo correcto.";
                    flag = true;
                }
            }
        }

        if (metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE) {
            log.debug("Validando datos para edici\u00F3n en m\u00E9todos PUT o DELETE.");
            if (input.getIdArtPromocional() == null || "".equals(input.getIdArtPromocional())
                    || !isNumeric(input.getIdArtPromocional())) {
                datosErroneos += " ID ArtPromocional";
                flag = true;
            }
        }

        if (flag ) {
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
                Promocionales.CAMPO_TCSCARTPROMOCIONALID,
                Promocionales.CAMPO_DESCRIPCION,
                Promocionales.CAMPO_TIPO_GRUPO,
                Promocionales.CAMPO_ID_PRODUCT_OFFERING,
                "(select O.NOMBRE from tc_sc_oferta_sidra_Fs O WHERE O.ID_PRODUCT_OFFERING=TC_SC_ART_PROMOCIONAL.ID_PRODUCT_OFFERING) NOMBRE_OFERTA",
                Promocionales.CAMPO_ESTADO,
                Promocionales.CAMPO_CREADO_EL,
                Promocionales.CAMPO_CREADO_POR,
                Promocionales.CAMPO_MODIFICADO_EL,
                Promocionales.CAMPO_MODIFICADO_POR
            };
            return campos;
        } else {
            String campos[] = {
                Promocionales.CAMPO_TCSCARTPROMOCIONALID,
                Promocionales.CAMPO_TCSCCATPAISID,
                Promocionales.CAMPO_DESCRIPCION,
                Promocionales.CAMPO_TIPO_GRUPO,
                Promocionales.CAMPO_ESTADO,
                Promocionales.CAMPO_ID_PRODUCT_OFFERING,
                Promocionales.CAMPO_CREADO_EL,
                Promocionales.CAMPO_CREADO_POR
            };
            return campos;
        }
    }
    
    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
     * 
     * @param input
     * @param sequencia
     * @return inserts
     * @throws SQLException 
     */
    public static List<String> obtenerInsertsPost(Connection conn, InputPromocionales input, String sequencia, BigDecimal ID_PAIS) throws SQLException {
        List<String> inserts = new ArrayList<String>();
        String valores = "";
        
        valores = "("
            + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, ID_PAIS.toString(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getDescripcion(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getTipoGrupo(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea()), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdOferta(), Conf.INSERT_SEPARADOR_SI)
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
    public static String[][] obtenerCamposPutDel(Connection conn, InputPromocionales input, int metodo) throws SQLException {
        if (metodo == Conf.METODO_DELETE) {
            String campos[][] = {
                { Promocionales.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, input.getCodArea())) },
                { Promocionales.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                { Promocionales.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
            };
            return campos;
        } else {
            String campos[][] = {
                { Promocionales.CAMPO_DESCRIPCION, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getDescripcion()) },
                { Promocionales.CAMPO_TIPO_GRUPO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getTipoGrupo()) },
                { Promocionales.CAMPO_ID_PRODUCT_OFFERING, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getIdOferta()) },
                { Promocionales.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getEstado()) },
                { Promocionales.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                { Promocionales.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
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
    public static List<Filtro> obtenerCondiciones(InputPromocionales input, int metodo, BigDecimal ID_PAIS) {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Promocionales.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
        
        if (metodo == Conf.METODO_GET) {
            if (input.getIdArtPromocional() != null && !"".equals(input.getIdArtPromocional())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                        Promocionales.CAMPO_TCSCARTPROMOCIONALID, input.getIdArtPromocional()));
            }
            if (input.getDescripcion() != null && !"".equals(input.getDescripcion())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Promocionales.CAMPO_DESCRIPCION, input.getDescripcion()));
            }
            if (input.getTipoGrupo() != null && !"".equals(input.getTipoGrupo())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Promocionales.CAMPO_TIPO_GRUPO, input.getTipoGrupo()));
            }
            if (input.getEstado() != null && !"".equals(input.getEstado())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Promocionales.CAMPO_ESTADO, input.getEstado()));
            }
        }

        if (metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                    Promocionales.CAMPO_TCSCARTPROMOCIONALID, input.getIdArtPromocional()));
        }

        return condiciones;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     * @param metodo
     * @return respuestaPromocionales
     */
    public OutputPromocionales getDatos(InputPromocionales input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta respuesta = null;
        OutputPromocionales output = null;
        Connection conn = null;

        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            respuesta = validarInput(conn, input, metodo, ID_PAIS);
            log.trace("Respuesta validaci\u00F3n: " + respuesta.getDescripcion());
            if (!"OK".equals(respuesta.getDescripcion())) {
                output = new OutputPromocionales();
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ARTICULO_PROMOCIONAL, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));

                return output;
            }

            if (metodo == Conf.METODO_GET) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionPromocionales.doGet(conn, input, metodo, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de art\u00EDculos promocionales.", ""));
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputPromocionales();
                    output.setRespuesta(respuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de art\u00EDculos promocionales.",
                            e.getMessage()));
                }

            } else if (metodo == Conf.METODO_POST) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
                try {
                    output = OperacionPromocionales.doPost(conn, input, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_ART_PROMOCIONAL_49) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ARTICULO_PROMOCIONAL,
                                servicioPost, output.getIdArtPromocional(), Conf.LOG_TIPO_ARTICULO_PROMOCIONAL,
                                "Se cre\u00F3 nuevo art\u00EDculo promocional con ID " + output.getIdArtPromocional()
                                        + " y descripci\u00F3n " + input.getDescripcion().toUpperCase() + ".",
                                ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ARTICULO_PROMOCIONAL,
                                servicioPost, "0", Conf.LOG_TIPO_NINGUNO, "Problema al crear art\u00EDculo promocional.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputPromocionales();
                    output.setRespuesta(respuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ARTICULO_PROMOCIONAL, servicioPost,
                            "0", Conf.LOG_TIPO_NINGUNO, "Problema al crear art\u00EDculo promocional.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT o DELETE
                try {
                    output = OperacionPromocionales.doPutDel(conn, input, metodo, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_MOD_ART_PROMOCIONAL_50) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_ARTICULO_PROMOCIONAL, servicioPut,
                                input.getIdArtPromocional(), Conf.LOG_TIPO_ARTICULO_PROMOCIONAL,
                                "Se modificaron datos del art\u00EDculo promocional ID " + input.getIdArtPromocional() + ".",
                                ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_ARTICULO_PROMOCIONAL, servicioPut,
                                input.getIdArtPromocional(), Conf.LOG_TIPO_ARTICULO_PROMOCIONAL,
                                "Problema al modificar datos del art\u00EDculo promocional.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputPromocionales();
                    output.setRespuesta(respuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_ARTICULO_PROMOCIONAL, servicioPut, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al modificar art\u00EDculo promocional.", e.getMessage()));
                }
            }

        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputPromocionales();
            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ARTICULO_PROMOCIONAL, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de art\u00EDculos promocionales.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
