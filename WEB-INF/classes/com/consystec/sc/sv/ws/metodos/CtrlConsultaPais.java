package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;



import com.consystec.sc.ca.ws.input.general.InputConsultaWeb;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.pais.OutputConsultaPais;
import com.consystec.sc.sv.ws.operaciones.OperacionConsultaPais;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class CtrlConsultaPais extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlConsultaPais.class);
    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_DATOS_PAIS;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al servicio.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return Respuesta
     */
    public Respuesta validarInput(InputConsultaWeb input, int metodo) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        
        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase,
                    nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        }
        if (input.getCodArea() == null || "".equals(input.getCodArea())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_303, null, nombreClase,
                    nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        } else if (input.getCodArea().length() != 3){
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_LONG_304, null, nombreClase,
                    nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        } else if (!isNumeric(input.getCodArea())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_NUM_305, null, nombreClase,
                    nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
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
    public static String[] obtenerCamposGetPost() {
        String campos[] = {
            Catalogo.CAMPO_GRUPO,
            Catalogo.CAMPO_NOMBRE,
            Catalogo.CAMPO_VALOR,
            Catalogo.CAMPO_ESTADO,
            Catalogo.CAMPO_CREADO_EL,
            Catalogo.CAMPO_CREADO_POR,
            Catalogo.CAMPO_MODIFICADO_EL,
            Catalogo.CAMPO_MODIFICADO_POR
        };
        return campos;
    }
    
    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo M\u00E9todo que indica las acciones a realizar.
     * @return output Respuesta y listado con los clientes encontrados.
     */
    public OutputConsultaPais getDatos(InputConsultaWeb input, int metodo) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        OutputConsultaPais output = null;
        Respuesta r = new Respuesta();
        COD_PAIS = input.getCodArea();

        Connection conn = null;
        try {
            conn = getConnRegional();
           ID_PAIS = getIdPais(conn, input.getCodArea());
            
            // Validaci\u00F3n de datos en el input
            r = validarInput(input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equalsIgnoreCase(r.getDescripcion())) {
                output = new OutputConsultaPais();
                output.setRespuesta(r);
                
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));
                
                return output;
            }
            
            log.trace("Usuario: " + input.getUsuario());
            log.trace("CodArea: " + input.getCodArea());

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                output = OperacionConsultaPais.doGet(conn, input, ID_PAIS);

                if (output.getRespuesta().getExcepcion() != null && !"".equals(output.getRespuesta().getExcepcion())) {
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO,
                            "Problema al consultar datos del pa\u00EDs: " + output.getRespuesta().getDescripcion(),
                            output.getRespuesta().getExcepcion()));
                } else {
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos del pa\u00EDs.", ""));
                }
            }
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputConsultaPais();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos del pa\u00EDs.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
