package com.consystec.sc.sv.ws.metodos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.general.InputFolioDTS;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.bogegas.OutputConfiguracionFolio;
import com.consystec.sc.sv.ws.operaciones.OperacionFolioDTS;
import com.consystec.sc.sv.ws.orm.ConfiguracionFolioDTS;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 * <br>Servicios que no se utilizan.
 */
public class CtrlFolioDTS extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlFolioDTS.class);
    
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDe como recurso al servicio.
     * 
     * @param input
     * @param metodo
     * @return
     */
    public Respuesta validarInput(InputFolioDTS input, int metodo) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        
        if (input.getIdBodega() == null || "".equals(input.getIdBodega().trim())) {
            datosErroneos += " Bodega.";
            flag = true;
        } else if (!isNumeric(input.getIdBodega())) {
            datosErroneos += " Datos no num\u00E9ricos Bodega.";
            flag = true;
        }

        if (metodo != Conf.METODO_GET) {
            if (input.getTipoDocumento() == null || "".equals(input.getTipoDocumento().trim())) {
                datosErroneos += " Tipo de documento.";
                flag = true;
            }
            if (input.getSerie() == null || "".equals(input.getSerie().trim())) {
                datosErroneos += " Serie.";
                flag = true;
            }
            if (input.getNoInicialFolio() == null || "".equals(input.getNoInicialFolio().trim())) {
                datosErroneos += " # Inicial de folio.";
                flag = true;
            }
            if (input.getNoFinalFolio() == null || "".equals(input.getNoFinalFolio().trim())) {
                datosErroneos += " # Final de folio.";
                flag = true;
            }
            if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
                datosErroneos += " Usuario.";
                flag = true;
            }
            if (input.getNoInicialFolio() == null || input.getNoFinalFolio() == null
                    || !isNumeric(input.getNoInicialFolio()) || !isNumeric(input.getNoFinalFolio())) {
                datosErroneos += " Datos no num\u00E9ricos en el rango.";
                flag = true;
            } else {
                if (new Integer(input.getNoInicialFolio()) >= new Integer(input.getNoFinalFolio())) {
                    datosErroneos += " El # Final del Folio debe ser mayor.";
                    flag = true;
                }
            }
        }

        if (flag) {
            r = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase,
                nombreMetodo, datosErroneos, input.getCodArea());
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
                ConfiguracionFolioDTS.CAMPO_TC_SC_ALMACENBODID,
                ConfiguracionFolioDTS.CAMPO_TIPODOCUMENTO,
                ConfiguracionFolioDTS.CAMPO_SERIE,
                ConfiguracionFolioDTS.CAMPO_NO_INICIAL_FOLIO,
                ConfiguracionFolioDTS.CAMPO_NO_FINAL_FOLIO,
                ConfiguracionFolioDTS.CAMPO_CREADO_POR,
                ConfiguracionFolioDTS.CAMPO_ESTADO,
                ConfiguracionFolioDTS.CAMPO_CREADO_EL,
                ConfiguracionFolioDTS.CAMPO_CREADO_POR,
                ConfiguracionFolioDTS.CAMPO_MODIFICADO_EL,
                ConfiguracionFolioDTS.CAMPO_MODIFICADO_POR
            };
            return campos;
        } else {
            String campos[] = {
                ConfiguracionFolioDTS.CAMPO_TC_SC_FOLIO_BODEGA_ID,
                ConfiguracionFolioDTS.CAMPO_TC_SC_ALMACENBODID,
                ConfiguracionFolioDTS.CAMPO_TIPODOCUMENTO,
                ConfiguracionFolioDTS.CAMPO_SERIE,
                ConfiguracionFolioDTS.CAMPO_NO_INICIAL_FOLIO,
                ConfiguracionFolioDTS.CAMPO_NO_FINAL_FOLIO,
                ConfiguracionFolioDTS.CAMPO_CREADO_EL,
                ConfiguracionFolioDTS.CAMPO_CREADO_POR
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
     */
    public static List<String> obtenerInsertsPost(InputFolioDTS input, String sequencia) {
        List<String> inserts = new ArrayList<String>();
        String valores = "";
        
        valores = "("
                + "(extractvalue(dbms_xmlgen.getxmltype('select " + sequencia + " from dual'),'//text()')), "
                + "" + input.getIdBodega() + ", "
                + "UPPER('" + input.getTipoDocumento() + "'), "
                + "UPPER('" + input.getSerie() + "'), "
                + "" + input.getNoInicialFolio() + ", "
                + "" + input.getNoFinalFolio() + ", "
                + "sysdate, "
                + "'" + input.getUsuario() + "' " 
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
    public static String[][] obtenerCamposPutDel(InputFolioDTS input) throws SQLException {
        //Los valores que sean tipo texto deben ir entre ap\u00F3strofres o comillas simples.
        String campos[][] = {
            { ConfiguracionFolioDTS.CAMPO_ESTADO, "'" + UtileriasJava.getConfig(null, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, input.getCodArea()) + "'" },
            { ConfiguracionFolioDTS.CAMPO_MODIFICADO_EL, "sysdate" },
            { ConfiguracionFolioDTS.CAMPO_MODIFICADO_POR, "'" + input.getUsuario() + "'" }
        };
        return campos;
    }
    
    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje para la tabla relacionada.
     * 
     * @param input
     * @param metodo
     * @return
     */
    public static List<Filtro> obtenerCondiciones(InputFolioDTS input, int metodo) {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        if (metodo==Conf.METODO_GET){
            if (!"".equals(input.getIdBodega())) {
                condiciones.add(new Filtro(Filtro.AND, ConfiguracionFolioDTS.CAMPO_TC_SC_ALMACENBODID,
                    Filtro.EQ, input.getIdBodega()));
            }
            if (!"".equals(input.getTipoDocumento())) {
                condiciones.add(new Filtro(Filtro.AND, ConfiguracionFolioDTS.CAMPO_TIPODOCUMENTO,
                    Filtro.EQ, "UPPER('" + input.getTipoDocumento() + "')"));
            }
            if (!"".equals(input.getSerie())) {
                condiciones.add(new Filtro(Filtro.AND, ConfiguracionFolioDTS.CAMPO_SERIE,
                    Filtro.EQ, "UPPER('" + input.getSerie() + "')"));
            }
        }
        if (metodo==Conf.METODO_DELETE){
            condiciones.add(new Filtro(Filtro.AND, ConfiguracionFolioDTS.CAMPO_TC_SC_ALMACENBODID,
                Filtro.EQ, input.getIdBodega()));

            condiciones.add(new Filtro(Filtro.AND, ConfiguracionFolioDTS.CAMPO_TIPODOCUMENTO,
                Filtro.EQ, "UPPER('" + input.getTipoDocumento() + "')"));

            condiciones.add(new Filtro(Filtro.AND, ConfiguracionFolioDTS.CAMPO_SERIE,
                Filtro.EQ, "UPPER('" + input.getSerie() + "')"));
            
            condiciones.add(new Filtro(Filtro.AND, ConfiguracionFolioDTS.CAMPO_NO_INICIAL_FOLIO,
                Filtro.EQ, input.getNoInicialFolio()));
            
            condiciones.add(new Filtro(Filtro.AND, ConfiguracionFolioDTS.CAMPO_NO_FINAL_FOLIO,
                Filtro.EQ, input.getNoFinalFolio()));
        }
        
        return condiciones;
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las consultas de verificaci\u00F3n
     * de existencia del recurso.
     * @param conn 
     * 
     * @param idPadre
     * @param input
     * @return
     * @throws SQLException 
     */
    public static List<Filtro> obtenerCondicionesExistencia(Connection conn, InputFolioDTS input) throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, ConfiguracionFolioDTS.CAMPO_TIPODOCUMENTO, input.getTipoDocumento()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, ConfiguracionFolioDTS.CAMPO_SERIE, input.getSerie()));
        condiciones.add(new Filtro(Filtro.AND, ConfiguracionFolioDTS.CAMPO_NO_FINAL_FOLIO, Filtro.GTE, input.getNoInicialFolio()));
        condiciones.add(UtileriasJava.getCondicionDefault(Conf.FILTRO_ESTADO, ConfiguracionFolioDTS.CAMPO_ESTADO, conn, input.getCodArea()));

        return condiciones;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     * @param metodo
     * @return
     */
    public OutputConfiguracionFolio getDatos(InputFolioDTS input, int metodo) {
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        List<Respuesta> respuesta = new ArrayList<Respuesta>();

        // Validaci\u00F3n de datos en el input
        Respuesta r = validarInput(input, metodo);
        OutputConfiguracionFolio output = null;
        log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
        if (!"OK".equals(r.getDescripcion())) {
            respuesta.add(r);
            output = new OutputConfiguracionFolio();
            output.setRespuesta(r);
            return output;
        }

        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                try {
                    output = OperacionFolioDTS.doGet(conn, input, metodo);
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputConfiguracionFolio();
                    output.setRespuesta(r);
                }
            
            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
            } else if (metodo == Conf.METODO_POST) {
                try {
                    output = OperacionFolioDTS.doPost(conn, input);
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputConfiguracionFolio();
                    output.setRespuesta(r);
                }
                
            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT o DELETE
            } else if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
                try {
                    output = OperacionFolioDTS.doPutDel(conn, input, metodo);
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputConfiguracionFolio();
                    output.setRespuesta(r);
                }
            }

        
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputConfiguracionFolio();
            output.setRespuesta(r);
        }finally{
            DbUtils.closeQuietly(conn);
        }

        return output;
    }
}
