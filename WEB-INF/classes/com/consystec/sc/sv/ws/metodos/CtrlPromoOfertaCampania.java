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
import com.consystec.sc.ca.ws.input.ofertacampania.InputPromoOfertaCampania;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.ofertacampania.OutputPromoOfertaCampania;
import com.consystec.sc.sv.ws.operaciones.OperacionPromoOfertaCampania;
import com.consystec.sc.sv.ws.orm.OfertaCampania;
import com.consystec.sc.sv.ws.orm.PromoOfertaCampania;
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
public class CtrlPromoOfertaCampania extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlPromoOfertaCampania.class);
    private static String servicioGet = Conf.LOG_GET_PROMOCIONAL_CAMPANIA;
    private static String servicioPost = Conf.LOG_POST_PROMOCIONAL_CAMPANIA;
    
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al servicio.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @param estadoAlta 
     * @return Respuesta
     * @throws SQLException 
     */
    public Respuesta validarInput(Connection conn, InputPromoOfertaCampania input, int metodo, String estadoAlta, BigDecimal ID_PAIS)
            throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        int numeroDetalle = 0;
        String existencia;
        List<Filtro> condiciones = new ArrayList<Filtro>();

        log.debug("Validando datos...");

        String campania = UtileriasJava.getConfig(conn, Conf.GRUPO_OFERTA_COMERCIAL, Conf.TIPO_CAMPANIA, input.getCodArea());

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (metodo == Conf.METODO_POST) {
            if (input.getIdOfertaCampania() == null || "".equals(input.getIdOfertaCampania())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_OFERTACAMP_220, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdOfertaCampania())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_OFERTACAMP_NUM_221, null, nombreClase, nombreMetodo,
                        null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else {
                String camposCampania[] = { OfertaCampania.CAMPO_TIPO };

                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampania.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, OfertaCampania.CAMPO_ESTADO, estadoAlta));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, input.getIdOfertaCampania()));

                try {
                    List<Map<String, String>> datosCampania = UtileriasBD.getSingleData(conn, OfertaCampania.N_TABLA, camposCampania, condiciones, null);
                    if (datosCampania.size() <= 0) {
                        log.error("No existe la campa\u00F1a enviada.");
                        r = getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_CAMPANIA_242, null, nombreClase, nombreMetodo,
                                null, input.getCodArea());
                        datosErroneos += " " + r.getDescripcion();
                        flag = true;
                    } else {
                        if (!datosCampania.get(0).get(OfertaCampania.CAMPO_TIPO).equalsIgnoreCase(campania)) {
                            log.error("El ID no pertenece a campa\u00F1a.");
                            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_OFERTACAM_TIPO_248, null, nombreClase,
                                    nombreMetodo, null, input.getCodArea());
                            datosErroneos += " " + r.getDescripcion();
                            flag = true;
                        }
                    }
                } catch (SQLException e) {
                    log.error("Error al verificar existencia de la campa\u00F1a enviada.", e);
                    r = getMensaje(Conf_Mensajes.MSJ_ERROR_VERIFICAR_CAMPANIA_243, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                    datosErroneos += " " + r.getDescripcion();
                    flag = true;
                }
            }

            if (input.getArticulosPromocionales() != null) {
                for (int i = 0; i < input.getArticulosPromocionales().size(); i++) {
                    if (flag == true) break;
                    String idArtPromocional = "";
                    String cantArticulos = "";

                    if (input.getArticulosPromocionales().get(i).getIdArtPromocional() != null) {
                        idArtPromocional = input.getArticulosPromocionales().get(i).getIdArtPromocional().trim();
                    }
                    if (input.getArticulosPromocionales().get(i).getCantArticulos() != null) {
                        cantArticulos = input.getArticulosPromocionales().get(i).getCantArticulos().trim();
                    }

                    numeroDetalle = i + 1;
                    if (idArtPromocional == null || "".equals(idArtPromocional)) {
                        r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_IDART_146, null, nombreClase, nombreMetodo,
                                numeroDetalle + ".", input.getCodArea());
                        datosErroneos += r.getDescripcion();
                        flag = true;
                    } else if (!isNumeric(idArtPromocional)) {
                        r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_IDART_NUM_147, null, nombreClase, nombreMetodo,
                                numeroDetalle + ".", input.getCodArea());
                        datosErroneos += r.getDescripcion();
                        flag = true;
                    }
                    if (cantArticulos == null || "".equals(cantArticulos)) {
                        r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_CANT_148, null, nombreClase, nombreMetodo,
                                numeroDetalle + ".", input.getCodArea());
                        datosErroneos += r.getDescripcion();
                        flag = true;
                    } else if (!isNumeric(cantArticulos)) {
                        r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_CANT_NUM_149, null, nombreClase, nombreMetodo,
                                numeroDetalle + ".", input.getCodArea());
                        datosErroneos += r.getDescripcion();
                        flag = true;
                    }
                }

                numeroDetalle = 1;
                for (InputPromoOfertaCampania detActual : input.getArticulosPromocionales()) {
                    if (flag ) break;
                    int indexDet = 1;

                    for (InputPromoOfertaCampania detalle : input.getArticulosPromocionales()) {
                        if (flag) break;

                        if (indexDet != numeroDetalle && detalle.getIdArtPromocional().toUpperCase().trim()
                                .equals(detActual.getIdArtPromocional().toUpperCase().trim())) {
                            log.error("El detalle #" + indexDet + " es igual al #" + numeroDetalle);
                            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_OC_DET_IGUALES_237, null, nombreClase,
                                    nombreMetodo, numeroDetalle + " y " + indexDet + ".", input.getCodArea());
                            datosErroneos += r.getDescripcion();
                            flag = true;
                        }
                        indexDet++;
                    }
                    numeroDetalle++;
                }
            } else {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DETALLES_241, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (!flag) {
                numeroDetalle = 0;
                for (InputPromoOfertaCampania detalle: input.getArticulosPromocionales()) {
                    if (flag) break;
                    numeroDetalle++;
                    condiciones.clear();
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Promocionales.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Promocionales.CAMPO_ESTADO, estadoAlta));
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Promocionales.CAMPO_TCSCARTPROMOCIONALID, detalle.getIdArtPromocional()));

                    try {
                        existencia = UtileriasBD.verificarExistencia(conn, Promocionales.N_TABLA, condiciones);
                        if (new Integer(existencia) <= 0) {
                            log.error("No existe el art\u00EDculo promocional enviado.");
                            r = getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_PROMO_DET_244, null, nombreClase,
                                    nombreMetodo, numeroDetalle + ".", input.getCodArea());
                            datosErroneos += " " + r.getDescripcion();
                            flag = true;
                        }
                    } catch (SQLException e) {
                        log.error("Error al verificar existencia del art\u00EDculo promocional enviado.", e);
                        r = getMensaje(Conf_Mensajes.MSJ_ERROR_VERIFICAR_PROMOCIONAL_245, null, nombreClase,
                                nombreMetodo, null, input.getCodArea());
                        datosErroneos += " " + r.getDescripcion();
                        flag = true;
                    }
                }
            }
        }

        if (metodo == Conf.METODO_GET) {
            if ((input.getIdPromoCampania() != null && !"".equals(input.getIdPromoCampania())) && !isNumeric(input.getIdPromoCampania())) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_PROMOCAMPANIA_247, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
            }

            if ((input.getIdOfertaCampania() != null && !"".equals(input.getIdOfertaCampania())) && !isNumeric(input.getIdOfertaCampania())) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_OFERTACAMP_NUM_221, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
            }

            if ((input.getIdArtPromocional() != null && !"".equals(input.getIdArtPromocional()))&& !isNumeric(input.getIdArtPromocional())) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ARTPROMOCIONAL_246, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
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
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en el m\u00E9todo GET.
     * 
     * @return
     */
    public static String[][] obtenerCamposGet() {
        String campos[][] = {
            { PromoOfertaCampania.N_TABLA, PromoOfertaCampania.CAMPO_TCSCDETARTPROMOID },
            { PromoOfertaCampania.N_TABLA, PromoOfertaCampania.CAMPO_TCSCOFERTACAMPANIAID },
            { OfertaCampania.N_TABLA, OfertaCampania.CAMPO_NOMBRE + " AS " + OfertaCampania.CAMPO_NOMBRE },
            { PromoOfertaCampania.N_TABLA, PromoOfertaCampania.CAMPO_TCSCARTPROMOCIONALID },
            { Promocionales.N_TABLA, Promocionales.CAMPO_DESCRIPCION + " AS " + Promocionales.CAMPO_DESCRIPCION },
            { PromoOfertaCampania.N_TABLA, PromoOfertaCampania.CAMPO_CANT_ARTICULOS },
            { PromoOfertaCampania.N_TABLA, PromoOfertaCampania.CAMPO_ESTADO },
            { PromoOfertaCampania.N_TABLA, PromoOfertaCampania.CAMPO_CREADO_EL },
            { PromoOfertaCampania.N_TABLA, PromoOfertaCampania.CAMPO_CREADO_POR },
            { PromoOfertaCampania.N_TABLA, PromoOfertaCampania.CAMPO_MODIFICADO_EL },
            { PromoOfertaCampania.N_TABLA, PromoOfertaCampania.CAMPO_MODIFICADO_POR }
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
           PromoOfertaCampania.CAMPO_TCSCDETARTPROMOID,
           PromoOfertaCampania.CAMPO_TCSCCATPAISID,
           PromoOfertaCampania.CAMPO_TCSCOFERTACAMPANIAID,
           PromoOfertaCampania.CAMPO_TCSCARTPROMOCIONALID,
           PromoOfertaCampania.CAMPO_CANT_ARTICULOS,
           PromoOfertaCampania.CAMPO_ESTADO,
           PromoOfertaCampania.CAMPO_CREADO_EL,
           PromoOfertaCampania.CAMPO_CREADO_POR
       };
       
       return campos;
   }
    
    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST de la tabla relacionada.
     * 
     * @param idPadre
     * @param input
     * @param sequencia
     * @param estadoAlta 
     * @return
     */
    public static List<String> obtenerInsertsPost(InputPromoOfertaCampania input, String sequencia, String estadoAlta, BigDecimal ID_PAIS) {
        List<String> inserts = new ArrayList<String>();
        String valores = "";

        for (int i = 0; i < input.getArticulosPromocionales().size(); i++) {
            valores = UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, ID_PAIS.toString(), Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdOfertaCampania() + "", Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getArticulosPromocionales().get(i).getIdArtPromocional(), Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getArticulosPromocionales().get(i).getCantArticulos(), Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO);
            inserts.add(valores);
        }

        return inserts;
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje.
     * 
     * @param conn 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return condiciones Listado con los filtros indicados seg\u00FAn el m\u00E9todo deseado.
     */
    public static List<Filtro> obtenerCondiciones(InputPromoOfertaCampania input, int metodo, BigDecimal ID_PAIS) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (metodo == Conf.METODO_POST) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PromoOfertaCampania.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PromoOfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, input.getIdOfertaCampania()));
        }

        if (metodo == Conf.METODO_GET) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, PromoOfertaCampania.N_TABLA,
                    PromoOfertaCampania.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));

            if (input.getIdPromoCampania() != null && !"".equals(input.getIdPromoCampania())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, PromoOfertaCampania.N_TABLA,
                    PromoOfertaCampania.CAMPO_TCSCDETARTPROMOID, input.getIdPromoCampania()));
            }
            if (input.getIdOfertaCampania() != null && !"".equals(input.getIdOfertaCampania())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, PromoOfertaCampania.N_TABLA,
                    PromoOfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, input.getIdOfertaCampania()));
            }
            if (input.getIdArtPromocional() != null && !"".equals(input.getIdArtPromocional())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, PromoOfertaCampania.N_TABLA,
                    PromoOfertaCampania.CAMPO_TCSCARTPROMOCIONALID, input.getIdArtPromocional()));
            }
            if (input.getEstado() != null && !"".equals(input.getEstado())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, PromoOfertaCampania.N_TABLA,
                    PromoOfertaCampania.CAMPO_ESTADO, input.getEstado()));
            }
        }
        return condiciones;
    }
    
    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo 
     * @return output Respuesta y listado con los PromoOfertaCampanias encontrados.
     */
    public OutputPromoOfertaCampania getDatos(InputPromoOfertaCampania input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<Respuesta> respuesta = new ArrayList<Respuesta>();

        Respuesta r = new Respuesta();
        OutputPromoOfertaCampania output = null;

        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            r = validarInput(conn, input, metodo, estadoAlta, ID_PAIS);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equals(r.getDescripcion())) {
                respuesta.add(r);
                output = new OutputPromoOfertaCampania();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PROMOCIONAL_CAMPANIA, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            if (metodo == Conf.METODO_GET) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionPromoOfertaCampania.doGet(conn, input, metodo, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de promocionales de campa\u00F1a.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputPromoOfertaCampania();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de promocionales de campa\u00F1a.",
                            e.getMessage()));
                }

            } else if (metodo == Conf.METODO_POST) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
                try {
                    output = OperacionPromoOfertaCampania.doPost(conn, input, metodo, estadoAlta, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_PROMOCIONAL_CAMPANIA_51) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PROMOCIONAL_CAMPANIA, servicioPost,
                                "0", Conf.LOG_TIPO_NINGUNO,
                                "Se asignaron promocionales a la campa\u00F1a con ID " + input.getIdOfertaCampania() + ".",
                                ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PROMOCIONAL_CAMPANIA, servicioPost,
                                "0", Conf.LOG_TIPO_NINGUNO, "Problema al asignar promocionales a campa\u00F1a.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputPromoOfertaCampania();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PROMOCIONAL_CAMPANIA, servicioPost,
                            "0", Conf.LOG_TIPO_NINGUNO, "Problema al asignar promocionales a campa\u00F1a.",
                            e.getMessage()));
                }
            }
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputPromoOfertaCampania();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PROMOCIONAL_CAMPANIA, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de promocionales de campa\u00F1a.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
