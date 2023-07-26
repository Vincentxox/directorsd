package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.oferCom.InputConsultaArticulos;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.oferCom.OutputConsultaArticulos;
import com.consystec.sc.sv.ws.operaciones.OperacionArticulo;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class CtrlArticulo extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlArticulo.class);
    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_ARTICULOS;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al servicio.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return Respuesta
     */
    public Respuesta validarInput(InputConsultaArticulos input, int metodo) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        
        log.debug("Validando datos...");
        
        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase,
                nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
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
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al servicio.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @return Respuesta
     */
    public Respuesta validarInput(InputConsultaArticulos input) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        
        log.debug("Validando datos...");
        
        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
        }
        
        if (input.getIdArticulo() == null || "".equals(input.getIdArticulo())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDARTICULO_130, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        } else if (!isNumeric(input.getIdArticulo())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDARTICULO_NUM_131, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
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
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return output Respuesta y listado con los Articuloss encontrados.
     */
    public OutputConsultaArticulos getDatos(InputConsultaArticulos input, int metodo) {
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        OutputConsultaArticulos output = null;
        COD_PAIS = input.getCodArea();
        
        Connection conn = null;
        Respuesta r = null;
        try {
            conn = getConnRegional();
            ID_PAIS= getIdPais(conn, input.getCodArea());
            // Validación de datos en el input
            r = validarInput(input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equals(r.getDescripcion())) {
                output = new OutputConsultaArticulos();
                output.setRespuesta(r);
                
                listaLog = new ArrayList<LogSidra>();
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar art\u00EDculos de sidra.", r.getDescripcion()));
                
                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                output = OperacionArticulo.doGet(conn, input.getCodArea(), ID_PAIS);
                
                listaLog = new ArrayList<LogSidra>();
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Se consultaron art\u00EDculos de Sidra.", ""));
            }
        } catch (SQLException e) {
            r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("ExcepcionSQL: " + e);
            output = new OutputConsultaArticulos();
            output.setRespuesta(r);
            
            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar art\u00EDculos de sidra.", e.getMessage()));
            
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputConsultaArticulos();
            output.setRespuesta(r);
            
            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar art\u00EDculos de sidra.", e.getMessage()));
            
        } finally {
            DbUtils.closeQuietly(conn);
            
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }
        
        return output;
    }

    public OutputConsultaArticulos getPrecioArticulo(InputConsultaArticulos input, int metodo) {
        String nombreMetodo = "getPrecioArticulo";
        String nombreClase = new CurrentClassGetter().getClassName();

        // Validaci\u00F3n de datos en el input
        Respuesta r = validarInput(input);
        OutputConsultaArticulos output = null;

        log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
        if (!"OK".equals(r.getDescripcion())) {
            output = new OutputConsultaArticulos();
            output.setRespuesta(r);
            return output;
        }

        log.trace("Usuario: " + input.getUsuario());
        log.trace("idArticulo/CodArticulo: " + input.getIdArticulo());

        Connection conn = null;
        try {
            conn = getConnRegional();

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                // TODO cambiar entre paises, en CR es respuesta default
                output = OperacionArticulo.doGetPrecio(conn, input);
                
            }
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputConsultaArticulos();
            output.setRespuesta(r);
        } finally {
            DbUtils.closeQuietly(conn);
        }

        return output;
    }
}
