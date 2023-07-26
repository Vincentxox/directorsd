package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.ofertacampania.InputOfertaCampania;
import com.consystec.sc.ca.ws.input.ofertacampania.InputOfertaCampaniaDet;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.ofertacampania.OutputOfertaCampania;
import com.consystec.sc.sv.ws.operaciones.OperacionOfertaCampania;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.OfertaCampania;
import com.consystec.sc.sv.ws.orm.OfertaCampaniaDet;
import com.consystec.sc.sv.ws.orm.Panel;
import com.consystec.sc.sv.ws.orm.Ruta;
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
public class CtrlOfertaCampania extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlOfertaCampania.class);
    private static List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_OFERTA_CAMPANIA;
    private static String servicioPost = Conf.LOG_POST_OFERTA_CAMPANIA;
    private static String servicioPut = Conf.LOG_PUT_OFERTA_CAMPANIA;

    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al servicio.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @param estadoBaja 
     * @param estadoAlta 
     * @param tipoRuta 
     * @param tipoPanel 
     * @param oferta 
     * @param campania 
     * @return Respuesta
     * @throws SQLException 
     */
    public Respuesta validarInput(Connection conn, InputOfertaCampania input, int metodo, String campania,
            String oferta, String tipoPanel, String tipoRuta, String estadoAlta, String estadoBaja, BigDecimal ID_PAIS)
                    throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        int numeroPanel = 0;
        List<Filtro> condiciones = new ArrayList<Filtro>();
        log.debug("Validando datos...");

        if (metodo == Conf.METODO_GET) {
            if ((input.getIdOfertaCampania() != null && !"".equals(input.getIdOfertaCampania()))&& !isNumeric(input.getIdOfertaCampania())) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_OFERTACAMP_NUM_221, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
            }
            if ((input.getIdRuta() != null && !"".equals(input.getIdRuta())) && !isNumeric(input.getIdRuta())) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDRUTA_NUM_251, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
            }
            if ((input.getIdPanel() != null && !"".equals(input.getIdPanel())) && !isNumeric(input.getIdPanel())) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDPANEL_NUM_252, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
            }
            if ((input.getIdDTS() != null && !"".equals(input.getIdDTS()))&&!isNumeric(input.getIdDTS())) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
                }
        }

        if (metodo == Conf.METODO_PUT) {
            if (input.getEstado() == null || "".equals(input.getEstado().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_134, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!input.getEstado().equalsIgnoreCase(estadoAlta)
                    && !input.getEstado().equalsIgnoreCase(estadoBaja)) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_TIPO_135, null, nombreClase, nombreMetodo,
                        estadoAlta + " o " + estadoBaja + ".", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
        }

        if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
            log.trace("ID OfertaCampania: " + input.getIdOfertaCampania());
            if (input.getIdOfertaCampania() == null || "".equals(input.getIdOfertaCampania())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_OFERTACAMP_220, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdOfertaCampania())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_OFERTACAMP_NUM_221, null, nombreClase, nombreMetodo,
                        null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
        }

        if (metodo == Conf.METODO_POST || metodo == Conf.METODO_PUT) {
            if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
            if (input.getTipo() == null || "".equals(input.getTipo().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_NULO_150, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!input.getTipo().equalsIgnoreCase(campania) && !input.getTipo().equalsIgnoreCase(oferta)) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_136, null, nombreClase, nombreMetodo,
                        oferta + " o " + campania + ".", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (input.getNombre() == null || "".equals(input.getNombre().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NOMBRE_222, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
            if (input.getDescripcion() == null || "".equals(input.getDescripcion().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DESCRIPCION_223, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (input.getTipo().equalsIgnoreCase(campania)) {
                if (input.getCantMaxPromocionales() == null || "".equals(input.getCantMaxPromocionales().trim())) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_CANTPROMOS_227, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
                } else if (!isDecimal(input.getCantMaxPromocionales())) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_CANTPROMOS_NUM_228, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
                }
            }

            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_INPUT);
            Date fechaDesde = null;
            Date fechaHasta = null;
            if (input.getFechaDesde() == null || "".equals(input.getFechaDesde().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_DESDE_229, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else {
                fechaDesde = UtileriasJava.parseDate(input.getFechaDesde(), formatoFecha);
            }
            if (input.getFechaHasta() == null || "".equals(input.getFechaHasta().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_HASTA_230, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else {
                fechaHasta = UtileriasJava.parseDate(input.getFechaHasta(), formatoFecha);
            }

            Calendar fecha = new GregorianCalendar();
            int anio = fecha.get(Calendar.YEAR);
            int mes = fecha.get(Calendar.MONTH);
            int dia = fecha.get(Calendar.DAY_OF_MONTH);

            if (metodo == Conf.METODO_POST) {
                Date fechaHoy = UtileriasJava.parseDate(dia + "/" + (mes + 1) + "/" + anio, formatoFecha);
                if (fechaDesde != null && fechaDesde.compareTo(fechaHoy) < 0) {
                        r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_DESDE_ACTUAL_231, null, nombreClase,
                                nombreMetodo, null, input.getCodArea());
                        datosErroneos += r.getDescripcion();
                        flag = true;
                }
            }

            if ((fechaDesde != null && fechaHasta != null) && fechaDesde.compareTo(fechaHasta) > 0) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_DESDE_MENOR_232, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
            }

            if (input.getOfertaCampaniaDet() != null) {
                for (int i = 0; i < input.getOfertaCampaniaDet().size(); i++) {
                    if (flag) break;
                    String idTipo = "";
                    String tipo = "";

                    if (input.getOfertaCampaniaDet().get(i).getIdTipo() != null) {
                        idTipo = input.getOfertaCampaniaDet().get(i).getIdTipo().trim();
                    }
                    if (input.getOfertaCampaniaDet().get(i).getTipo() != null) {
                        tipo = input.getOfertaCampaniaDet().get(i).getTipo().trim();
                    }

                    numeroPanel = i + 1;
                    if (idTipo == null || "".equals(idTipo)) {
                        r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_OC_DET_IDTIPO_233, null, nombreClase, nombreMetodo,
                                numeroPanel + ".", input.getCodArea());
                        datosErroneos += r.getDescripcion();
                        flag = true;
                    } else if (!isNumeric(idTipo)) {
                        r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_OC_DET_IDTIPO_NUM_234, null, nombreClase,
                                nombreMetodo, numeroPanel + ".", input.getCodArea());
                        datosErroneos += r.getDescripcion();
                        flag = true;
                    }
                    if (tipo == null ||"".equals( tipo)) {
                        r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_OC_DET_TIPO_NULO_235, null, nombreClase,
                                nombreMetodo, numeroPanel + ".", input.getCodArea());
                        datosErroneos += r.getDescripcion();
                        flag = true;
                    } else if (!tipo.equalsIgnoreCase(tipoPanel) && !tipo.equalsIgnoreCase(tipoRuta)) {
                        r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_OC_DET_TIPO_236, null, nombreClase, nombreMetodo,
                                numeroPanel + ", tipo " + tipoPanel + " o " + tipoRuta + ".", input.getCodArea());
                        datosErroneos += r.getDescripcion();
                        flag = true;
                    }
                }

                numeroPanel = 1;
                for (InputOfertaCampaniaDet detActual : input.getOfertaCampaniaDet()) {
                    if (flag ) break;
                    int indexDet = 1;

                    for (InputOfertaCampaniaDet detalle : input.getOfertaCampaniaDet()) {
                        if (flag ) break;

                        if (indexDet != numeroPanel
                                && detalle.getTipo().toUpperCase().trim().equals(detActual.getTipo().toUpperCase().trim())
                                && Integer.valueOf(detalle.getIdTipo().trim()).equals(Integer.valueOf(detActual.getIdTipo())) ) {
                            log.error("El detalle #" + indexDet + " es igual al #" + numeroPanel);
                            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_OC_DET_IGUALES_237, null, nombreClase,
                                    nombreMetodo, null, input.getCodArea());
                            datosErroneos += r.getDescripcion();
                            flag = true;
                        }
                        indexDet++;
                    }
                    numeroPanel++;
                }
            } else {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_OC_DET_238, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (!flag) {
            	
            	boolean validarRuta=false;
            	if(metodo==Conf.METODO_PUT){
            		if(!input.getEstado().equalsIgnoreCase(estadoBaja)){
            			validarRuta=true;
            		}
            	}else if (metodo==Conf.METODO_POST){
            		validarRuta=true;
            	}
                String existencia;
                if(validarRuta){
	                for (InputOfertaCampaniaDet detalle : input.getOfertaCampaniaDet()) {
	                    if (flag) break;
	                    if (detalle.getTipo().equalsIgnoreCase(tipoPanel)) {
	                        // verifica si existe la panel enviada
	                        condiciones.clear();
	                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
	                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Panel.CAMPO_ESTADO, estadoAlta));
	                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCPANELID, detalle.getIdTipo()));
	
	                        try {
	                            existencia = UtileriasBD.verificarExistencia(conn, Panel.N_TABLA, condiciones);
	                            if (new Integer(existencia) <= 0) {
	                                log.error("No existe la panel enviada.");
	                                r = getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_PANEL_181, null, nombreClase,
	                                        nombreMetodo, null, input.getCodArea());
	                                datosErroneos += " " + r.getDescripcion();
	                                flag = true;
	                            }
	                        } catch (SQLException e) {
	                            log.error("Error al verificar existencia del tipo enviado.", e);
	                            r = getMensaje(Conf_Mensajes.MSJ_ERROR_VERIFICAR_TIPO_183, null, nombreClase, nombreMetodo,
	                                    null, input.getCodArea());
	                            datosErroneos += " " + r.getDescripcion();
	                            flag = true;
	                        }
	                    } else {
	                        //verifica si existe la ruta enviada
	                        condiciones.clear();
	                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
	                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Ruta.CAMPO_ESTADO, estadoAlta));
	                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TC_SC_RUTA_ID, detalle.getIdTipo()));
	
	                        try {
	                            existencia = UtileriasBD.verificarExistencia(conn, Ruta.N_TABLA, condiciones);
	                            if (new Integer(existencia) <= 0) {
	                                log.error("No existe la ruta enviada.");
	                                r = getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_RUTA_182, null, nombreClase,
	                                        nombreMetodo, null, input.getCodArea());
	                                datosErroneos += " " + r.getDescripcion();
	                                flag = true;
	                            }
	                        } catch (SQLException e) {
	                            log.error("Error al verificar existencia del tipo enviado.", e);
	                            r = getMensaje(Conf_Mensajes.MSJ_ERROR_VERIFICAR_TIPO_183, null, nombreClase, nombreMetodo,
	                                    null, input.getCodArea());
	                            datosErroneos += " " + r.getDescripcion();
	                            flag = true;
	                        }
	                    }
	                }
                }
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
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return campos Array con los nombres de los campos.
     */
    public static String[] obtenerCamposGetPost(int metodo) {
        if (metodo == Conf.METODO_GET) {
            String campos[] = {
                OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID,
                OfertaCampania.CAMPO_TIPO,
                OfertaCampania.CAMPO_NOMBRE,
                OfertaCampania.CAMPO_DESCRIPCION,
                OfertaCampania.CAMPO_CANT_MAX_PROMOCIONALES,
                OfertaCampania.CAMPO_FECHA_DESDE,
                OfertaCampania.CAMPO_FECHA_HASTA,
                OfertaCampania.CAMPO_ESTADO,
                OfertaCampania.CAMPO_CREADO_EL,
                OfertaCampania.CAMPO_CREADO_POR,
                OfertaCampania.CAMPO_MODIFICADO_EL,
                OfertaCampania.CAMPO_MODIFICADO_POR
            };
            return campos;
        } else {
            String campos[] = {
                OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID,
                OfertaCampania.CAMPO_TCSCCATPAISID,
                OfertaCampania.CAMPO_TIPO,
                OfertaCampania.CAMPO_NOMBRE,
                OfertaCampania.CAMPO_DESCRIPCION,
                OfertaCampania.CAMPO_CANT_MAX_PROMOCIONALES,
                OfertaCampania.CAMPO_FECHA_DESDE,
                OfertaCampania.CAMPO_FECHA_HASTA,
                OfertaCampania.CAMPO_ESTADO,
                OfertaCampania.CAMPO_CREADO_EL,
                OfertaCampania.CAMPO_CREADO_POR
            };
            return campos;
        }
    }

    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET 
     * y en este caso tambi\u00E9n todos los que se insertar\u00E9n en el m\u00E9todo POST
     * de la tabla relacionada.
     * 
     * @param metodo
     * @return
     */
    public static String[] obtenerCamposTablaHija(int metodo) {
        if (metodo == Conf.METODO_GET) {
            String campos[] = {
                OfertaCampaniaDet.CAMPO_TCSCDETPANELRUTAID,
                OfertaCampaniaDet.CAMPO_TCSCOFERTACAMPANIAID,
                OfertaCampaniaDet.CAMPO_TCSCTIPOID,
                OfertaCampaniaDet.CAMPO_TIPO,
                OfertaCampaniaDet.CAMPO_ESTADO,
                OfertaCampaniaDet.CAMPO_CREADO_EL,
                OfertaCampaniaDet.CAMPO_CREADO_POR,
                OfertaCampaniaDet.CAMPO_MODIFICADO_EL,
                OfertaCampaniaDet.CAMPO_MODIFICADO_POR
            };
            return campos;
        } else {
            String campos[] = {
                OfertaCampaniaDet.CAMPO_TCSCDETPANELRUTAID,
                OfertaCampaniaDet.CAMPO_TCSCCATPAISID,
                OfertaCampaniaDet.CAMPO_TCSCOFERTACAMPANIAID,
                OfertaCampaniaDet.CAMPO_TCSCTIPOID,
                OfertaCampaniaDet.CAMPO_TIPO,
                OfertaCampaniaDet.CAMPO_ESTADO,
                OfertaCampaniaDet.CAMPO_CREADO_EL,
                OfertaCampaniaDet.CAMPO_CREADO_POR
            };
            return campos;
        }
    }

    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param sequencia Nombre de la secuencia a utilizar para la inserci\u00F3n.
     * @param string 
     * @return inserts Listado de cadenas con los valores a insertar.
     */
    public static List<String> obtenerInsertsPost(InputOfertaCampania input, String sequencia,
            String estadoAlta, BigDecimal ID_PAIS) {
        List<String> inserts = new ArrayList<String>();

        String valores = "("
            + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, ID_PAIS.toString(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getTipo(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getNombre(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getDescripcion(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getCantMaxPromocionales(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsertFecha(Conf.INSERT_FECHA, input.getFechaDesde(), Conf.TXT_FORMATO_FECHA_INPUT, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsertFecha(Conf.INSERT_FECHA, input.getFechaHasta(), Conf.TXT_FORMATO_FECHA_INPUT, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
        + ") ";
        
        inserts.add(valores);
        
        return inserts;
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
    public static List<String> obtenerInsertsPostHijo(int idPadre, InputOfertaCampania input, String sequencia,
            String estadoAlta, BigDecimal ID_PAIS) {
        List<String> inserts = new ArrayList<String>();
        String valores = "";
        
        for (int i = 0; i < input.getOfertaCampaniaDet().size(); i++) {
            valores =
                UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, ID_PAIS.toString(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPadre + "", Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getOfertaCampaniaDet().get(i).getIdTipo(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getOfertaCampaniaDet().get(i).getTipo(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO);
            inserts.add(valores);
        }
        
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
     */
    public static String[][] obtenerCamposPutDel( InputOfertaCampania input) {
        String campos[][] = {
            { OfertaCampania.CAMPO_TIPO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getTipo()) },
            { OfertaCampania.CAMPO_NOMBRE, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getNombre()) },
            { OfertaCampania.CAMPO_DESCRIPCION, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getDescripcion()) },
            { OfertaCampania.CAMPO_CANT_MAX_PROMOCIONALES, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getCantMaxPromocionales()) },
            { OfertaCampania.CAMPO_FECHA_DESDE, UtileriasJava.setUpdateFecha(Conf.INSERT_FECHA, input.getFechaDesde(), Conf.TXT_FORMATO_FECHA_INPUT) },
            { OfertaCampania.CAMPO_FECHA_HASTA, UtileriasJava.setUpdateFecha(Conf.INSERT_FECHA, input.getFechaHasta(), Conf.TXT_FORMATO_FECHA_INPUT) },
            { OfertaCampania.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getEstado()) },
            { OfertaCampania.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
            { OfertaCampania.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
        };
        
        return campos;
    }

    /**
     * Funci\u00F3n que indica los campos que se utilizar\u00E9n para modificaciones  
     * al recurso relacionado en los m\u00E9todos PUT y DELETE.
     * @param metodo 
     * @param input 
     * @return campos
     */
    public static String[][] obtenerCamposPutDelHijo(InputOfertaCampania input, String estado) {
        String campos[][] = {
            { OfertaCampaniaDet.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estado) },
            { OfertaCampaniaDet.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
            { OfertaCampaniaDet.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
        };
        return campos;
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje.
     * 
     * @param conn 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @param tipoPanel
     * @param tipoRuta
     * @return
     * @throws SQLException
     */
    public static List<Filtro> obtenerCondiciones( InputOfertaCampania input, int metodo,
            String tipoPanel, String tipoRuta, BigDecimal ID_PAIS) throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        List<Filtro> condicionesExtra = new ArrayList<Filtro>();

        if (metodo == Conf.METODO_PUT) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, input.getIdOfertaCampania()));
        }
        
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampania.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));

        if (metodo == Conf.METODO_GET) {
            if (input.getIdOfertaCampania() != null && !"".equals(input.getIdOfertaCampania())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, input.getIdOfertaCampania()));
            }
            if (input.getTipo() != null && !"".equals(input.getTipo())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, OfertaCampania.CAMPO_TIPO, input.getTipo()));
            }
            if (input.getNombre() != null && !"".equals(input.getNombre())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, OfertaCampania.CAMPO_NOMBRE, input.getNombre()));
            }
            if (input.getEstado() != null && !"".equals(input.getEstado())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, OfertaCampania.CAMPO_ESTADO, input.getEstado()));
            }

            String[] campos = { OfertaCampaniaDet.CAMPO_TCSCOFERTACAMPANIAID };
            if (input.getIdPanel() != null && !"".equals(input.getIdPanel())) {
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampaniaDet.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampaniaDet.CAMPO_TCSCTIPOID, input.getIdPanel()));
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, OfertaCampaniaDet.CAMPO_TIPO, tipoPanel));

                String selectSQL = UtileriasBD.armarQuerySelect(OfertaCampaniaDet.N_TABLA, campos, condicionesExtra);
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, selectSQL));
            }

            if (input.getIdRuta() != null && !"".equals(input.getIdRuta())) {
                condicionesExtra.clear();
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampaniaDet.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampaniaDet.CAMPO_TCSCTIPOID, input.getIdRuta()));
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, OfertaCampaniaDet.CAMPO_TIPO, tipoRuta));

                String selectSQL = UtileriasBD.armarQuerySelect(OfertaCampaniaDet.N_TABLA, campos, condicionesExtra);
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, selectSQL));
            }

            String[] campoIdPanel = { Panel.CAMPO_TCSCPANELID };
            String[] campoIdRuta = { Ruta.CAMPO_TC_SC_RUTA_ID };
            if (input.getIdDTS() != null && !"".equals(input.getIdDTS())) {
                String selectSQL = "";
                condicionesExtra.clear();
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCDTSID, input.getIdDTS()));
                selectSQL = UtileriasBD.armarQuerySelect(Panel.N_TABLA, campoIdPanel, condicionesExtra);

                List<Filtro> condicionesDTS = new ArrayList<Filtro>();
                condicionesDTS.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampaniaDet.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condicionesDTS.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, OfertaCampaniaDet.CAMPO_TCSCTIPOID, selectSQL));
                condicionesDTS.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, OfertaCampaniaDet.CAMPO_TIPO, tipoPanel));

                condicionesExtra.clear();
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TC_SC_DTS_ID, input.getIdDTS()));
                selectSQL = UtileriasBD.armarQuerySelect(Ruta.N_TABLA, campoIdRuta, condicionesExtra);

                condicionesExtra.clear();
                condicionesExtra.add(new Filtro(Filtro.OR, "", "", UtileriasBD.agruparCondiciones(condicionesDTS)));

                condicionesDTS.clear();
                condicionesDTS.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampaniaDet.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condicionesDTS.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, OfertaCampaniaDet.CAMPO_TCSCTIPOID, selectSQL));
                condicionesDTS.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, OfertaCampaniaDet.CAMPO_TIPO, tipoRuta));

                condicionesExtra.add(new Filtro(Filtro.OR, "", "", UtileriasBD.agruparCondiciones(condicionesDTS)));

                selectSQL = UtileriasBD.armarQuerySelect(OfertaCampaniaDet.N_TABLA, campos, condicionesExtra);

                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, selectSQL));
            }

            if (input.getFechaDesde() != null && !"".equals(input.getFechaDesde())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IS_NULL_OR, OfertaCampania.CAMPO_FECHA_DESDE, null));
                condicionesExtra.clear();

                if (input.getFechaHasta() != null && !"".equals(input.getFechaHasta())) {
                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_GTE_AND, OfertaCampania.CAMPO_CREADO_EL,// OfertaCampania.CAMPO_FECHA_DESDE
                        input.getFechaDesde(), null, Conf.TXT_FORMATO_FECHA_CORTA));

                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_LTE_AND, OfertaCampania.CAMPO_CREADO_EL,
                        input.getFechaHasta(), null, Conf.TXT_FORMATO_FECHA_CORTA));

                } else {
                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_LTE_AND, OfertaCampania.CAMPO_CREADO_EL,
                        input.getFechaDesde(), null, Conf.TXT_FORMATO_FECHA_CORTA));

                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_GTE_AND, OfertaCampania.CAMPO_CREADO_EL,
                        input.getFechaDesde(), null, Conf.TXT_FORMATO_FECHA_CORTA));
                }

                condiciones.add(new Filtro(Filtro.OR, "", "", UtileriasBD.agruparCondiciones(condicionesExtra)));
            }
        }

        return condiciones;
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje para la tabla relacionada.
     * 
     * @param idPadre
     * @param input
     * @param metodo
     * @return
     */
    public static List<Filtro> obtenerCondicionesTablaHija(String idPadre, BigDecimal ID_PAIS) {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampaniaDet.CAMPO_TCSCOFERTACAMPANIAID, idPadre));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampaniaDet.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));

        return condiciones;
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las consultas de verificaci\u00F3n
     * de existencia del recurso.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return condiciones Listado con los filtros indicados seg\u00FAn el m\u00E9todo deseado.
     * @throws SQLException 
     */
    public static List<Filtro> obtenerCondicionesExistencia(Connection conn, InputOfertaCampania input, int metodo, BigDecimal ID_PAIS) throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampania.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));

        if (metodo == Conf.METODO_PUT) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, input.getIdOfertaCampania()));
        }

        if (metodo == Conf.METODO_POST) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, OfertaCampania.CAMPO_NOMBRE, input.getNombre()));
            condiciones.add(UtileriasJava.getCondicionDefault(Conf.FILTRO_ESTADO, OfertaCampania.CAMPO_ESTADO, conn, input.getCodArea()));
        }

        return condiciones;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo 
     * @return output Respuesta y listado con los OfertaCampanias encontrados.
     */
    public OutputOfertaCampania getDatos(InputOfertaCampania input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<Respuesta> respuesta = new ArrayList<Respuesta>();

        Respuesta r = new Respuesta();
        OutputOfertaCampania output = null;

        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());
            
            String campos[] = { Catalogo.CAMPO_NOMBRE, Catalogo.CAMPO_VALOR };
            String campania = "";
            String oferta = "";
            String tipoPanel = "";
            String tipoRuta = "";
            String estadoBaja = "";

            // Se obtienen todas las configuraciones.
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, ID_PAIS.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_OFERTA_COMERCIAL));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_PANELRUTA));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ESTADOS));

            List<Map<String, String>> datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, null);
            for (int i = 0; i < datosConfig.size(); i++) {
                if (Conf.TIPO_CAMPANIA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    campania = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_OFERTA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    oferta = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_PANEL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoPanel = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_RUTA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoRuta = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.ESTADO_BAJA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoBaja = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
            }

            // Validaci\u00F3n de datos en el input
            r = validarInput(conn, input, metodo, campania, oferta, tipoPanel, tipoRuta, estadoAlta, estadoBaja, ID_PAIS);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equals(r.getDescripcion())) {
                respuesta.add(r);
                output = new OutputOfertaCampania();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_OFERTA_CAMPANIA, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                try {
                    output = OperacionOfertaCampania.doGet(conn, input, metodo, tipoPanel.toUpperCase(), tipoRuta.toUpperCase(), input.getCodArea(), ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de ofertas/campa\u00F1as.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputOfertaCampania();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de ofertas/campa\u00F1as.", e.getMessage()));
                }
            
            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
            } else if (metodo == Conf.METODO_POST) {
                try {
                    output = OperacionOfertaCampania.doPost(conn, input, estadoAlta, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_OFERTACAMPANIA_47) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_OFERTA_CAMPANIA, servicioPost,
                                output.getIdOfertaCampania(), Conf.LOG_TIPO_OFERTA_CAMPANIA,
                                "Se cre\u00F3 nueva " + input.getTipo().toUpperCase() + " con ID "
                                        + output.getIdOfertaCampania() + " y nombre " + input.getNombre().toUpperCase()
                                        + ".",
                                ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_OFERTA_CAMPANIA, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema al crear oferta/campa\u00F1a.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputOfertaCampania();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_OFERTA_CAMPANIA, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al crear oferta/campa\u00F1a.", e.getMessage()));
                }
                
            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT
            } else if (metodo == Conf.METODO_PUT) {
                try {
                    output = OperacionOfertaCampania.doPutDel(conn, input, metodo, tipoPanel, tipoRuta, estadoAlta, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_MOD_OFERTACAMPANIA_48) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_OFERTA_CAMPANIA, servicioPut,
                                input.getIdOfertaCampania(), Conf.LOG_TIPO_OFERTA_CAMPANIA,
                                "Se modificaron datos de la oferta/campau00F1a ID " + input.getIdOfertaCampania() + ".", ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_OFERTA_CAMPANIA, servicioPut,
                                input.getIdOfertaCampania(), Conf.LOG_TIPO_OFERTA_CAMPANIA,
                                "Problema al modificar datos de ofertas/campa\u00F1as.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputOfertaCampania();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_OFERTA_CAMPANIA, servicioPut, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al modificar oferta/campa\u00F1a.", e.getMessage()));
                }
            }
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputOfertaCampania();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_OFERTA_CAMPANIA, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de ofertas/campa\u00F1as.", e.getMessage()));
        } finally {
        	DbUtils.closeQuietly(conn);

        	UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
