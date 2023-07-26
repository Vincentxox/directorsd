package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.vendedordts.InputVendedorDTS;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.vendedordts.OutputVendedorDTS;
import com.consystec.sc.sv.ws.operaciones.OperacionVendedorDTS;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.orm.VendedorPDV;
import com.consystec.sc.sv.ws.orm.BodegaVendedor;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Distribuidor;
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
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class CtrlVendedorDTS extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlVendedorDTS.class);
    private List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_VENDEDORDTS;
    private static String servicioPost = Conf.LOG_POST_VENDEDORDTS;
    private static String servicioPut = Conf.LOG_PUT_VENDEDORDTS;
    
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDe como recurso al servicio.
     * @param conn 
     * 
     * @param input
     * @param metodo
     * @param estadoAlta 
     * @return Respuesta
     * @throws SQLException 
     */
    public Respuesta validarInput(Connection conn, InputVendedorDTS input, int metodo, String estadoAlta, BigDecimal ID_PAIS)
            throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        List<Filtro> condiciones = new ArrayList<Filtro>();
        String existencia = "";

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        int longNumero = 0;
        longNumero = new Integer(UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.LONGITUD_TELEFONO, input.getCodArea()));
        log.debug("Validando datos...");

        if (metodo == Conf.METODO_POST /*|| metodo == Conf.METODO_GET*/) {
            if (input.getIdDistribuidor() == null || input.getIdDistribuidor().trim().equals("")) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_161, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdDistribuidor())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else {
                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Distribuidor.CAMPO_ESTADO, estadoAlta));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TC_SC_DTS_ID, input.getIdDistribuidor()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TCSCCATPAISID, ""+ID_PAIS));

                try {
                    existencia = UtileriasBD.verificarExistencia(conn, Distribuidor.N_TABLA, condiciones);
                    if (new Integer(existencia) <= 0) {
                        r = getMensaje(Conf_Mensajes.MSJ_IDDTS_NO_EXISTE_173, null, nombreClase, nombreMetodo, null, input.getCodArea());
                        datosErroneos += r.getDescripcion();
                        flag = true;
                    }
                } catch (SQLException e) {
                    log.error("Error al verificar existencia del Distribuidor. " + e);
                    r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_CONSULTA_DTS_174, null, nombreClase,
                            nombreMetodo, null, input.getCodArea());
                    datosErroneos = r.getDescripcion();
                    flag = true;
                }
            }
        }

        if (metodo == Conf.METODO_POST) {
            if (input.getUsuarioVendedor() == null || input.getUsuarioVendedor().trim().equals("")) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIOVEND_158, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (input.getIdVendedor() == null || input.getIdVendedor().trim().equals("")) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_137, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdVendedor())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
        }

        if (metodo == Conf.METODO_PUT && (input.getEstado() == null || input.getEstado().trim().equals(""))) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_134, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
        }

        if (metodo == Conf.METODO_POST || metodo == Conf.METODO_PUT) {
            if (input.getUsuario() == null || input.getUsuario().trim().equals("")) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

          

            if (input.getIdBodegaVirtual() == null || input.getIdBodegaVirtual().trim().equals("")) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODVIRTUAL_170, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdBodegaVirtual())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODVIRTUAL_NUM_171, null, nombreClase, nombreMetodo,
                        null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else {
                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BodegaVirtual.CAMPO_ESTADO,
                        estadoAlta));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                        BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, input.getIdBodegaVirtual()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TCSCCATPAISID, ""+ID_PAIS));

                try {
                    existencia = UtileriasBD.verificarExistencia(conn, BodegaVirtual.N_TABLA, condiciones);
                    if (new Integer(existencia) <= 0) {
                        log.error("No existe la bodega enviada.");
                        r = getMensaje(Conf_Mensajes.MSJ_IDBODVIRTUAL_NO_EXISTE_172, null, nombreClase, nombreMetodo,
                                null, input.getCodArea());
                        datosErroneos += " " + r.getDescripcion();
                        flag = true;
                    }
                } catch (SQLException e) {
                    log.error("Error al verificar existencia de la Bodega.", e);
                    r = getMensaje(Conf_Mensajes.MSJ_ERROR_VERIFICAR_BODEGA_100, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    datosErroneos += " " + r.getDescripcion();
                    flag = true;
                }
            }

            if (input.getNombres() == null || input.getNombres().trim().equals("")) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NOMBRES_156, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (input.getApellidos() == null || input.getApellidos().trim().equals("")) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_APELLIDOS_157, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (input.getEmail() == null || input.getEmail().trim().equals("")) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_EMAIL_159, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (input.getNumeroRecarga() == null || input.getNumeroRecarga().trim().equals("")) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_NUMRECARGA_VACIO_721, null, this.getClass().toString(),
                        nombreMetodo, "", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getNumeroRecarga())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_NUMRECARGA_722, null, this.getClass().toString(), nombreMetodo,
                        null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (input.getNumeroRecarga().length() != longNumero) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_NUMRECARGA_LONG_723, null, this.getClass().toString(),
                        nombreMetodo, null, input.getCodArea());
            }

            if (input.getPin() == null || input.getPin().trim().equals("") || !isNumeric(input.getPin())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_PIN_727, null, this.getClass().toString(), nombreMetodo,
                        null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (input.getPin().length() != Conf.LONG_PIN_RECAGA) {
                r = getMensaje(Conf_Mensajes.MSJ_ERROR_INPUT_LONG_PIN_898, null, this.getClass().toString(),
                        nombreMetodo, Conf.LONG_PIN_RECAGA + " dígitos.", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }


            if (input.getCanal() == null || input.getCanal().trim().equals("")) {
                r = getMensaje(Conf_Mensajes.MSJ_CANAL_NULO_483, null, this.getClass().toString(), nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (input.getSubcanal() == null || input.getSubcanal().trim().equals("")) {
                r = getMensaje(Conf_Mensajes.MSJ_SUBCANAL_NULO_484, null, this.getClass().toString(), nombreMetodo,
                        null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

        } else if (metodo == Conf.METODO_GET) {
            if ((input.getIdBodegaVirtual() != null && !input.getIdBodegaVirtual().trim().equals(""))&& !isNumeric(input.getIdBodegaVirtual())) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODVIRTUAL_NUM_171, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
            }

            if ((input.getIdVendedorDTS() != null && !input.getIdVendedorDTS().trim().equals(""))&& !isNumeric(input.getIdVendedorDTS())) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDDTS_NUM_155, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
            }

            if ((input.getIdDistribuidor() != null && !input.getIdDistribuidor().trim().equals(""))&& !isNumeric(input.getIdDistribuidor())) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
            }

            if ((input.getIdVendedor() != null && !"".equals(input.getIdVendedor().trim())) && !isNumeric(input.getIdVendedor())) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
            }

            if ((input.getSoloDisponibles() != null && !"".equals(input.getSoloDisponibles().trim())) && (!isNumeric(input.getSoloDisponibles())
                        || (!"0".equals(input.getSoloDisponibles()) && !"1".equals(input.getSoloDisponibles())))) {
                    r = getMensaje(Conf_Mensajes.MSJ_ERROR_MOSTRAR_DISPONIBLES_398, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
            }
        } else if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
            log.trace("ID del VendedorDTS: " + input.getIdVendedorDTS());
            if (input.getIdVendedorDTS() == null || "".equals(input.getIdVendedorDTS().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDDTS_154, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdVendedorDTS())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDDTS_NUM_155, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
            if (input.getIdDistribuidor() == null || "".equals(input.getIdDistribuidor().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_161, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdDistribuidor())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
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
     * Funci\u00F3n que indica los campos que se insertar\u00E9n en el m\u00E9todo POST.
     * 
     * @return
     */
    public static String[] obtenerCamposPost() {
        String campos[] = {
            VendedorDTS.CAMPO_TCSCVENDDTSID,
            VendedorDTS.CAMPO_TCSCDTSID,
            VendedorDTS.CAMPO_TCSCCATPAISID,
            VendedorDTS.CAMPO_TCSCBODEGAVIRTUALID,
            VendedorDTS.CAMPO_VENDEDOR,
            VendedorDTS.CAMPO_USUARIO,
            VendedorDTS.CAMPO_NOMBRE,
            VendedorDTS.CAMPO_APELLIDO,
            VendedorDTS.CAMPO_EMAIL,
            VendedorDTS.CAMPO_ESTADO,
            VendedorDTS.CAMPO_NUM_RECARGA,
            VendedorDTS.CAMPO_PIN,
            VendedorDTS.CAMPO_CANAL,
            VendedorDTS.CAMPO_SUBCANAL,
            VendedorDTS.CAMPO_COD_VENDEDOR,
            VendedorDTS.CAMPO_CREADO_EL,
            VendedorDTS.CAMPO_CREADO_POR
            //VendedorDTS.CAMPO_ENVIO_COD_VEND
        };
        return campos;
    }
    
    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en el m\u00E9todo GET.
     * @param tipoPanel 
     * 
     * @return
     */
    public static String[][] obtenerCamposGet(String tipoPanel, String estadoAlta, BigDecimal idPais) {
        String campos[][] = {
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_TCSCVENDDTSID },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_TCSCDTSID },
            { Distribuidor.N_TABLA, Distribuidor.CAMPO_NOMBRES + " AS NOMBREDTS" },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_TCSCBODEGAVIRTUALID },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_VENDEDOR },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_USUARIO },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_NOMBRE },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_APELLIDO },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_EMAIL },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_NUM_RECARGA },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_PIN },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_CANAL },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_SUBCANAL },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_COD_VENDEDOR },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_ESTADO },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_CREADO_EL },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_CREADO_POR },
            //{ VendedorDTS.N_TABLA, VendedorDTS.CAMPO_ENVIO_COD_VEND },
            { BodegaVirtual.N_TABLA, BodegaVirtual.CAMPO_NOMBRE + " AS NOMBREBODEGA" },
            
            { null, "(SELECT " + BodegaVendedor.CAMPO_TCSCBODEGAVIRTUALID + " FROM " + BodegaVendedor.N_TABLA
                    + " WHERE " + BodegaVendedor.CAMPO_TCSCCATPAISID + " = " + idPais
                    + " AND " + BodegaVendedor.CAMPO_VENDEDOR + " = " + VendedorDTS.N_TABLA + "."
                    + VendedorDTS.CAMPO_VENDEDOR + ") AS BODEGAVENDEDOR" },
            
    

            { null, "(SELECT " + Ruta.CAMPO_NOMBRE + " FROM " + Ruta.N_TABLA + " WHERE "
                    + Ruta.CAMPO_SEC_USUARIO_ID + " = " + VendedorDTS.N_TABLA + "." + VendedorDTS.CAMPO_VENDEDOR
                    + " AND UPPER(" + Ruta.CAMPO_ESTADO + ") = '" + estadoAlta.toUpperCase() + "' AND "
                        + Ruta.CAMPO_TCSCCATPAISID + " = " + idPais + ") AS RUTA" },
            
            { null, "(SELECT " + Panel.CAMPO_NOMBRE + " FROM " + Panel.N_TABLA + " WHERE "
                    + Panel.CAMPO_TCSCPANELID
                    + " IN ((SELECT " + VendedorPDV.CAMPO_IDTIPO + " FROM " + VendedorPDV.N_TABLA + " WHERE "
                        + "UPPER(" + VendedorPDV.CAMPO_TIPO + ") = '" + tipoPanel.toUpperCase() + "' AND "
                        + VendedorPDV.CAMPO_VENDEDOR + " = " + VendedorDTS.N_TABLA + "." + VendedorDTS.CAMPO_VENDEDOR
                        + " AND UPPER(" + VendedorPDV.CAMPO_ESTADO + ") = '" + estadoAlta.toUpperCase() + "' AND "
                        + VendedorPDV.CAMPO_TCSCCATPAISID + " = " + idPais+"))"
                    + " AND UPPER(" + Panel.CAMPO_ESTADO + ") = '" + estadoAlta.toUpperCase() + "' AND "
                        + Panel.CAMPO_TCSCCATPAISID + "=" + idPais + ") AS PANEL" }
        };

        return campos;
    }
    
    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
     * @param conn 
     * 
     * @param input
     * @param sequencia
     * @return inserts
     */
    public static List<String> obtenerInsertsPost(InputVendedorDTS input, String sequencia, String estadoAlta, BigDecimal idPais) {
        List<String> inserts = new ArrayList<String>();
        String valores = "";
        valores = "("
            + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdDistribuidor(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, ""+idPais, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdBodegaVirtual(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdVendedor(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuarioVendedor(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getNombres(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getApellidos(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getEmail(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getNumeroRecarga(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getPin(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getCanal(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getSubcanal(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getCodVendedor(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
            //+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getEnvioCodVend(), Conf.INSERT_SEPARADOR_NO)
            
        + ") ";
        log.debug("valores: "+valores);
        inserts.add(valores);
        
        return inserts;
    }
    
    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje.
     * 
     * @param input
     * @param metodo
     * @return condiciones
     */
    public static List<Filtro> obtenerCondiciones(InputVendedorDTS input, int metodo, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (metodo == Conf.METODO_DELETE) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_TCSCCATPAISID,
                    "" + idPais));
            if (!"".equals(input.getIdVendedorDTS().trim())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_TCSCVENDDTSID,
                        input.getIdVendedorDTS()));
            }
        }

        if (metodo == Conf.METODO_GET) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.N_TABLA,
                    VendedorDTS.CAMPO_TCSCCATPAISID, "" + idPais));
            if (input.getIdDistribuidor() != null && !"".equals(input.getIdDistribuidor().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.N_TABLA,
                        VendedorDTS.CAMPO_TCSCDTSID, input.getIdDistribuidor()));
            }
            if (input.getIdBodegaVirtual() != null && !"".equals(input.getIdBodegaVirtual().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.N_TABLA,
                        VendedorDTS.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodegaVirtual()));
            }
            if (input.getEstado() != null && !"".equals(input.getEstado().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, VendedorDTS.N_TABLA,
                        VendedorDTS.CAMPO_ESTADO, input.getEstado()));
            }
            if (input.getIdVendedor() != null && !"".equals(input.getIdVendedor().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.N_TABLA,
                        VendedorDTS.CAMPO_VENDEDOR, input.getIdVendedor()));
            }
            if (input.getUsuarioVendedor() != null && !"".equals(input.getUsuarioVendedor().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, VendedorDTS.N_TABLA,
                        VendedorDTS.CAMPO_USUARIO, input.getUsuarioVendedor()));
            }
       
        }
        return condiciones;
    }
    
    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las consultas de verificaci\u00F3n
     * de existencia del recurso.
     * 
     * @param input
     * @param metodo 
     * @param estadoAlta 
     * @return condiciones
     */
    public static List<Filtro> obtenerCondicionesExistencia(InputVendedorDTS input, int metodo, String estadoAlta, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (metodo == Conf.METODO_POST && !"".equals(input.getIdVendedor().trim())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_VENDEDOR, input.getIdVendedor()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorDTS.CAMPO_ESTADO, estadoAlta));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_TCSCCATPAISID, "" + idPais));
        }

        if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_TCSCVENDDTSID, input.getIdVendedorDTS()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_TCSCCATPAISID, "" + idPais));
        }

        return condiciones;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     * @param metodo
     * @return respuestaVendedorDTS
     */
    public OutputVendedorDTS getDatos(InputVendedorDTS input, int metodo) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<Respuesta> respuesta = new ArrayList<Respuesta>();

        Connection conn = null;
        OutputVendedorDTS output = null;
        Respuesta r = new Respuesta();

        log.trace("Usuario: " + input.getUsuario());

        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            r = validarInput(conn, input, metodo, estadoAlta, ID_PAIS);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!r.getDescripcion().equals("OK")) {
                respuesta.add(r);
                output = new OutputVendedorDTS();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_VENDEDOR_DTS, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Inconvenientes en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            if (metodo == Conf.METODO_GET) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionVendedorDTS.doGet(conn, input, metodo, estadoAlta, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de vendedores asignados a distribuidor.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputVendedorDTS();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO,
                            "Inconvenientes al consultar datos de vendedores asignados a distribuidor.",
                            e.getMessage()));
                }

            } else if (metodo == Conf.METODO_POST) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
                try {
                    output = OperacionVendedorDTS.doPost(conn, input, estadoAlta, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_VEND_DTS_61) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_VENDEDOR_DTS, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO,
                                "Se asign\u00F3 el vendedor ID " + input.getIdVendedor() + " al distribuidor ID "
                                        + input.getIdDistribuidor() + " y la bodega virtual ID "
                                        + input.getIdBodegaVirtual() + ".",
                                ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_VENDEDOR_DTS, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Inconvenientes al asignar vendedor a distribuidor.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputVendedorDTS();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_VENDEDOR_DTS, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Inconvenientes al asignar vendedor a distribuidor.",
                            e.getMessage()));
                }

            } else if (metodo == Conf.METODO_DELETE) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo DELETE
                try {
                    output = OperacionVendedorDTS.doPutDel(conn, input, metodo, estadoAlta, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_DEL_VEND_DTS_63) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_VENDEDOR_DTS, servicioPut,
                                input.getIdVendedorDTS(), Conf.LOG_TIPO_VENDEDOR_DTS,
                                "Se di\u00F3 de baja la asociaci\u00F3n de vendedor y distribuidor ID " + input.getIdVendedorDTS()
                                        + ".",
                                ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_VENDEDOR_DTS, servicioPut,
                                input.getIdVendedorDTS(), Conf.LOG_TIPO_VENDEDOR_DTS,
                                "Problema al dar de baja la asociaci\u00F3n de vendedor y distribuidor.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputVendedorDTS();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_VENDEDOR_DTS, servicioPut, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al dar de baja la asociaci\u00F3n de vendedor y distribuidor.",
                            e.getMessage()));
                }

            } else if (metodo == Conf.METODO_PUT) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo DELETE
                try {
                    output = OperacionVendedorDTS.doPutDel(conn, input, metodo, estadoAlta, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_MOD_VEND_DTS_62) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_VENDEDOR_DTS, servicioPut,
                                input.getIdVendedorDTS(), Conf.LOG_TIPO_VENDEDOR_DTS,
                                "Se modific\u00F3 la asociaci\u00F3n de vendedor y distribuidor ID " + input.getIdVendedorDTS()
                                        + ".",
                                ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_VENDEDOR_DTS, servicioPut,
                                input.getIdVendedorDTS(), Conf.LOG_TIPO_VENDEDOR_DTS,
                                "Inconvenientes al modificar la asociaci\u00F3n de vendedor y distribuidor.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputVendedorDTS();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_VENDEDOR_DTS, servicioPut, "0",
                            Conf.LOG_TIPO_NINGUNO,
                            "Inconvenientes al modificar la asociaci\u00F3n de vendedor y distribuidor.", e.getMessage()));
                }
            }

        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputVendedorDTS();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_VENDEDOR_DTS, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Inconvenientes en el servicio de asignaci\u00F3n de vendedores a distribuidor.",
                    e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
