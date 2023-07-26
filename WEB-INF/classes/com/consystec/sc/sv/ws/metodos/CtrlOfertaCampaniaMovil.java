package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.ofertacampania.InputOfertaCampaniaMovil;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.ofertacampania.OutputOfertaCampaniaMovil;
import com.consystec.sc.sv.ws.operaciones.OperacionOfertaCampaniaMovil;
import com.consystec.sc.sv.ws.orm.OfertaCampania;
import com.consystec.sc.sv.ws.orm.OfertaCampaniaDet;
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
 * @author Victor Cifuentes - Consystec - 2016
 *
 */
public class CtrlOfertaCampaniaMovil extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlOfertaCampaniaMovil.class);
    private static String servicioGet = Conf.LOG_GET_OFERTA_CAMPANIA_MOVIL;
    
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al servicio.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return Respuesta
     */
    public Respuesta validarInput(Connection conn, InputOfertaCampaniaMovil input, int metodo) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;

        log.debug("Validando datos...");

        if (metodo == Conf.METODO_GET) {
            if ((input.getIdOfertaCampania() != null && !"".equals(input.getIdOfertaCampania()))&& !isNumeric(input.getIdOfertaCampania())) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_OFERTACAMP_NUM_221, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
            }
            if ((input.getIdRuta() != null && !"".equals(input.getIdRuta()))&& !isNumeric(input.getIdRuta())) {
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
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET.
     * 
     * @return campos Array con los nombres de los campos.
     */
    public static String[] obtenerCamposGetPost() {
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
    }
    
    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET 
     * de la tabla relacionada.
     * 
     * @return
     */
    public static String[][] obtenerCamposTablaHija() {
        String campos[][] = {
            { PromoOfertaCampania.N_TABLA, PromoOfertaCampania.CAMPO_TCSCDETARTPROMOID },
            { PromoOfertaCampania.N_TABLA, PromoOfertaCampania.CAMPO_TCSCOFERTACAMPANIAID },
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
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return condiciones Listado con los filtros indicados seg\u00FAn el m\u00E9todo deseado.
     * @throws SQLException 
     */
    public static List<Filtro> obtenerCondiciones(Connection conn, InputOfertaCampaniaMovil input, int metodo, BigDecimal ID_PAIS)
            throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        List<Filtro> condicionesExtra = new ArrayList<Filtro>();
        String tipoPanel = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_PANEL, input.getCodArea());
        String tipoRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_RUTA, input.getCodArea());
        
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

            if (input.getFechaDesde() != null && !"".equals(input.getFechaDesde())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IS_NULL_OR, OfertaCampania.CAMPO_FECHA_DESDE, null));
                condicionesExtra.clear();

                if (input.getFechaHasta() != null && !"".equals(input.getFechaHasta())) {
                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_GTE_AND, OfertaCampania.CAMPO_FECHA_DESDE,
                        input.getFechaDesde(), null, Conf.TXT_FORMATO_FECHA_CORTA));
                    
                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_LTE_AND, OfertaCampania.CAMPO_FECHA_HASTA,
                        input.getFechaHasta(), null, Conf.TXT_FORMATO_FECHA_CORTA));
                } else {
                    
                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_LTE_AND, OfertaCampania.CAMPO_FECHA_DESDE,
                        input.getFechaDesde(), null, Conf.TXT_FORMATO_FECHA_CORTA));
                        
                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_GTE_AND, OfertaCampania.CAMPO_FECHA_HASTA,
                        input.getFechaDesde(), null, Conf.TXT_FORMATO_FECHA_CORTA));
                }

                condiciones.add(new Filtro(Filtro.OR, "", "", UtileriasBD.agruparCondiciones(condicionesExtra)));
            }
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
    public OutputOfertaCampaniaMovil getDatos(InputOfertaCampaniaMovil input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        List<Respuesta> respuesta = new ArrayList<Respuesta>();
        Respuesta r = new Respuesta();
        OutputOfertaCampaniaMovil output = null;
        log.trace("Usuario: " + input.getUsuario());
        Connection conn = null;

        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());
            // Validación de datos en el input
            r = validarInput(conn, input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equals(r.getDescripcion())) {
                respuesta.add(r);
                output = new OutputOfertaCampaniaMovil();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                try {
                    output = OperacionOfertaCampaniaMovil.doGet(conn, input, metodo, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de ofertas/campa\u00F1as m\u00F3vil.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputOfertaCampaniaMovil();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de ofertas/campa\u00F1as m\u00F3vil.",
                            e.getMessage()));
                }
            }
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputOfertaCampaniaMovil();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de ofertas/campa\u00F1as m\u00F3vil.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
