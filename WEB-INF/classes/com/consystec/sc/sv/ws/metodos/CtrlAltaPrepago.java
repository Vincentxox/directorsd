package com.consystec.sc.sv.ws.metodos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.altaprepago.InputAltaPrepago;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.altaprepago.OutputAltaPrepago;
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
public class CtrlAltaPrepago extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlAltaPrepago.class);
    private static List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioPost = Conf.LOG_POST_ALTA_PREPAGO;
    
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al servicio.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return Respuesta
     * @throws SQLException 
     */
    public Respuesta validarInput(Connection conn, InputAltaPrepago input, int metodo) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        
        log.debug("Validando datos...");
       
        String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
        
        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase,
                nombreMetodo, null,input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if(input.getIdVendedor() == null || !isNumeric(input.getIdVendedor())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_137, null, nombreClase,
                nombreMetodo, null,input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if(input.getIdJornada() == null || !isNumeric(input.getIdJornada())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDJORNADA_259, null, nombreClase,
                nombreMetodo, null,input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if(input.getIdFichaCliente() == null || !isNumeric(input.getIdFichaCliente())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDFICHACLIENTE_260, null, nombreClase,
                nombreMetodo, null,input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if(input.getPrimerNombre() == null || "".equals(input.getPrimerNombre())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_PRIMER_NOMBRE_261, null, nombreClase,
                nombreMetodo, null,input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if(input.getPrimerApellido() == null || "".equals(input.getPrimerApellido())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_PRIMER_APELLIDO_262, null, nombreClase,
                nombreMetodo, null,input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if(input.getNoDocumento() == null || "".equals(input.getNoDocumento())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NODOCUMENTO_258, null, nombreClase,
                nombreMetodo, null,input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if (input.getTipoDocumento() == null || "".equals(input.getTipoDocumento().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_DOCUMENTO_NULO_263, null, nombreClase,
                nombreMetodo, null,input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        } else {
            // Se verifica si el tipo de condición es válido
            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, Conf.GRUPO_DOC_IDENT_ALTA));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_VALOR, input.getTipoDocumento()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));

            String existencia = UtileriasBD.verificarExistencia(conn, Catalogo.N_TABLA, condiciones);
            if (new Integer(existencia) < 1) {
                log.error("No existe el tipo de documento.");
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_DOCUMENTO_266, null,
                        nombreClase, nombreMetodo, null,input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
        }
        
        if(input.getNumero() == null || !isNumeric(input.getNumero())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NUMERO_264, null, nombreClase,
                nombreMetodo, null,input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if(input.getIcc() == null || !isNumeric(input.getIcc())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ICC_265, null, nombreClase,
                nombreMetodo, null,input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if (flag ) {
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
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo 
     * @return output Respuesta y listado con los datos encontrados.
     */
    public OutputAltaPrepago getDatos(InputAltaPrepago input, int metodo) {
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = new Respuesta();
        OutputAltaPrepago output = new OutputAltaPrepago();

        Connection conn = null;
        try {
            conn =getConnRegional();
            
            // Validaci\u00F3n de datos en el input
            respuesta = validarInput(conn, input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + respuesta.getDescripcion());
            if (!"OK".equals(respuesta.getDescripcion())) {
                output.setRespuesta(respuesta);
                
                listaLog = new ArrayList<LogSidra>();
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ALTA_PREPAGO, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al registrar alta prepago.", respuesta.getDescripcion()));
                
                return output;
            }
            
            //TODO Cambiar por respuesta correcta del servicio
            respuesta = getMensaje(Conf_Mensajes.MSJ_RECURSO_CREADO, null, nombreClase, nombreMetodo, "", input.getCodArea());
            output.setRespuesta(respuesta);

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ALTA_PREPAGO, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Se registr\u00F3 alta prepago.", ""));
        } catch (SQLException e) {
            respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null,input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputAltaPrepago();
            output.setRespuesta(respuesta);

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ALTA_PREPAGO, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al registrar alta prepago.", e.getMessage()));
        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null,input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputAltaPrepago();
            output.setRespuesta(respuesta);

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ALTA_PREPAGO, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al registrar alta prepago.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
