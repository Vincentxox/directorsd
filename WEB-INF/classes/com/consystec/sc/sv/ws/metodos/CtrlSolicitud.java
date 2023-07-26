package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.dispositivo.InputDispositivo;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.solicitud.InputArticuloSolicitud;
import com.consystec.sc.ca.ws.input.solicitud.InputSolicitud;
import com.consystec.sc.ca.ws.input.solicitud.RespuestaSolicitud;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.solicitud.OutputSolicitud;
import com.consystec.sc.sv.ws.operaciones.OperacionSolicitud;
import com.consystec.sc.sv.ws.operaciones.OperacionVendxDTS;
import com.consystec.sc.sv.ws.orm.Solicitud;
import com.consystec.sc.sv.ws.orm.SolicitudDet;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.orm.ArticulosSidra;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.BuzonSidra;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.Panel;
import com.consystec.sc.sv.ws.orm.Promocionales;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.SendMail;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class CtrlSolicitud extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlSolicitud.class);
    private static String nombreServicio = "";
    static String tipoGrupo="";
    // ========================= M\u00E9todos para validaciones ============================//
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al
     * servicio.
     * 
     * @param conn
     * @param input
     * @param origenPC
     * @param origenMovil
     * @return
     */
    public Respuesta validarInputGet(Connection conn, InputSolicitud input, String origenPC, String origenMovil) {
        String nombreMetodo = "validarInputGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;

        // validaciones generales
        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if ((input.getOrigen() != null && !"".equals(input.getOrigen().trim())) && (!input.getOrigen().equalsIgnoreCase(origenPC) && !input.getOrigen().equalsIgnoreCase(origenMovil)
                    && !input.getOrigen().trim().equals(""))) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ORIGEN_TIPO_326, null, nombreClase,
                        nombreMetodo, origenPC + " o " + origenMovil + ".", input.getCodArea()).getDescripcion();
                flag = true;
        }

        if ((input.getIdVendedor() != null && !"".equals(input.getIdVendedor().trim())) && !isNumeric(input.getIdVendedor())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
        }

        if (input.getFechaInicio() != null && !"".equals(input.getFechaInicio().trim()) && input.getFechaFin() != null
                && !"".equals(input.getFechaFin().trim())) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(input.getFechaInicio(), formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(input.getFechaFin(), formatoFecha);

            if ((fechaInicio != null && fechaFin != null) && fechaInicio.compareTo(fechaFin) > 0) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_INICIOFIN_143, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }
        }

        if (input.getIdSolicitud() != null && "".equals(input.getIdSolicitud().trim())
                && !isNumeric(input.getIdSolicitud())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDSOLICITUD_NUM_325, null, nombreClase,
                    nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getIdBodega() != null && "".equals(input.getIdBodega().trim()) && !isNumeric(input.getIdBodega())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBOD_NUM_293, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getIdDTS() != null && input.getIdDTS().trim().equals("") && !isNumeric(input.getIdDTS())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getIdBuzon() != null && "".equals(input.getIdBuzon().trim()) && !isNumeric(input.getIdBuzon())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBUZON_NUM_331, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getIdJornada() != null && "".equals(input.getIdJornada().trim())
                && !isNumeric(input.getIdJornada())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDJORNADA_NUM_761, null, nombreClase,
                    nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getIdTipo() != null && "".equals(input.getIdTipo().trim()) && !isNumeric(input.getIdTipo())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDTIPO_NUM_167, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (flag == true) {
            respuesta = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo, datosErroneos, input.getCodArea());
        } else {
            respuesta.setDescripcion("OK");
        }

        return respuesta;
    }

    /**
     * M\u00E9todo que valida los datos al modificar una solicitud.
     * 
     * @param input
     * @param estadoCancelada
     * @param estadoAceptada
     * @param estadoRechazada
     * @param estadoFinalizada
     * @param estadoRechazadaTelca
     * @param estadoEnviado 
     * @return
     */
    private Respuesta validarInputUpd(InputSolicitud input, String estadoCancelada, String estadoAceptada,
            String estadoRechazada, String estadoFinalizada, String estadoRechazadaTelca, String estadoEnviado) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        log.debug("Validando datos...");

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getIdSolicitud() == null || "".equals(input.getIdSolicitud().trim())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDSOLICITUD_324, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        } else if (!isNumeric(input.getIdSolicitud())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDSOLICITUD_NUM_325, null, nombreClase,
                    nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getEstado() == null || "".equals(input.getEstado().trim())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_134, null, nombreClase, nombreMetodo, null, input.getCodArea())
                    .getDescripcion();
            flag = true;
        } else {
            if (!input.getEstado().equalsIgnoreCase(estadoAceptada)
                    && !input.getEstado().equalsIgnoreCase(estadoRechazada)
                    && !input.getEstado().equalsIgnoreCase(estadoCancelada)
                    && !input.getEstado().equalsIgnoreCase(estadoFinalizada)
                    && !input.getEstado().equalsIgnoreCase(estadoEnviado)
                    && !input.getEstado().equalsIgnoreCase(estadoRechazadaTelca)) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_TIPO_135, null, nombreClase,
                        nombreMetodo, estadoAceptada + ", " + estadoRechazada + ", " + estadoFinalizada + ", "
                                + estadoRechazadaTelca + ", " + estadoCancelada + " o " + estadoEnviado + ".", input.getCodArea()).getDescripcion();
                flag = true;
            }
        }

        if ((input.getToken() != null && !"WEB".equalsIgnoreCase(input.getToken())) &&  (input.getIdVendedor() == null || "".equals(input.getIdVendedor())
                    || !isNumeric(input.getIdVendedor()))) {
                return getMensaje(Conf_Mensajes.MSJ_VENDEDOR_NULO_38, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        if ((!flag  && input.getEstado().equalsIgnoreCase(estadoAceptada) && input.getIdBuzonSiguiente() != null
                && !"".equals(input.getIdBuzonSiguiente().trim()))&& !isNumeric(input.getIdBuzonSiguiente())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_IDBUZON_SIGUIENTE_804, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
        }

        if ((!flag && input.getEstado().equalsIgnoreCase(estadoEnviado)) && (input.getIdBuzonSiguiente() == null || "".equals(input.getIdBuzonSiguiente().trim()) || !isNumeric(input.getIdBuzonSiguiente()))) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_IDBUZON_SIGUIENTE_804, null, nombreClase,
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
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa an crear una
     * solicitud.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @param estadoAlta
     * @param estadoFalse
     * @param estadoTrue
     * @param estadoIniciada
     * @param estadoDisponible
     * @param estadoDevolucion
     * @param tipoDevolucion
     * @param tipoReserva
     * @param tipoPedido
     * @param tipoNumPayment
     * @param tipoSiniestro
     * @param tipoTodasSolicitudes
     * @param origenPC
     * @param origenMovil
     * @param tipoInvSidra
     * @param estadoActivo
     * @param tipoPDV
     * @param tipoPanel
     * @param tipoRuta
     * @param idBuzonPayment
     * @param longTelefono
     * @param estadoPendiente
     * @param estadoActivadoPayment
     * @param tipoSiniestroParcial
     * @param tipoSiniestroTotal
     * @param tipoSiniestroDispositivo
     * @param tipoDeuda
     * @param estadoEnviado 
     * @return
     * @throws CloneNotSupportedException
     * @throws SQLException
     */
    public RespuestaSolicitud validarInputPost(Connection conn, InputSolicitud input, int metodo, String estadoAlta,
            String estadoFalse, String estadoTrue, String estadoDisponible, String estadoDevolucion,
            String tipoDevolucion, String tipoReserva, String tipoPedido, String tipoNumPayment, String tipoSiniestro,
            String tipoTodasSolicitudes, String origenPC, String origenMovil, String tipoInvSidra, String estadoActivo,
            String tipoPDV, String tipoPanel, String tipoRuta, String idBuzonPayment, String longTelefono,
            String estadoPendiente, String estadoActivadoPayment, String tipoSiniestroParcial,
            String tipoSiniestroTotal, String tipoSiniestroDispositivo, String tipoDeuda, String estadoCancelada, BigDecimal ID_PAIS)
            throws CloneNotSupportedException, SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<Filtro> condiciones = new ArrayList<Filtro>();
        String tipoDts = "";
        RespuestaSolicitud respuestaSolicitud = new RespuestaSolicitud();
        String datosErroneos = "";
        boolean flag = false;
        String respListados = "";
        Integer existencia;
        String nivelBodega = "";
        String nivelBuzon = "";
        boolean banderaNoPromocionales = false;
        boolean banderaOrigenPC = false;
        boolean banderaOrigenMovil = false;
        boolean banderaPedido = false;
        boolean banderaReserva = false;
        boolean banderaDevolucion = false;
        boolean banderaPayment = false;
        boolean banderaSiniestro = false;
        boolean banderaSiniestroTotal = false;
        boolean banderaSiniestroDispositivo = false;
        boolean banderaSolicitudTelca = false;
        boolean banderaSolicitudDTS = false;
        boolean banderaDeuda = false;
        int dtsExterno=0;

        // validaciones generales
        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        // verificacion de origen
        if (input.getOrigen() == null || "".equals(input.getOrigen().trim())) {
            respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_ORIGEN_NULO_75, null, nombreClase, nombreMetodo, null, input.getCodArea()));
            return respuestaSolicitud;
        } else {
            banderaOrigenPC = input.getOrigen().equalsIgnoreCase(origenPC);
            banderaOrigenMovil = input.getOrigen().equalsIgnoreCase(origenMovil);

            if (!banderaOrigenPC && !banderaOrigenMovil) {
                respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ORIGEN_TIPO_326, null,
                        nombreClase, nombreMetodo, origenPC + " o " + origenMovil + ".", input.getCodArea()));
                return respuestaSolicitud;
            }
        }

        // verificacion de tipo de solicitud
        if (input.getTipoSolicitud() == null || "".equals(input.getTipoSolicitud().trim())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPOSOLICITUD_328, null, nombreClase,
                    nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        } else {
            banderaPedido = input.getTipoSolicitud().equalsIgnoreCase(tipoPedido);
            banderaReserva = input.getTipoSolicitud().equalsIgnoreCase(tipoReserva);
            banderaDevolucion = input.getTipoSolicitud().equalsIgnoreCase(tipoDevolucion);
            banderaPayment = input.getTipoSolicitud().equalsIgnoreCase(tipoNumPayment);
            banderaSiniestro = input.getTipoSolicitud().equalsIgnoreCase(tipoSiniestro);
            banderaDeuda = input.getTipoSolicitud().equalsIgnoreCase(tipoDeuda);

            if (!banderaDevolucion && !banderaReserva && !banderaPedido && !banderaPayment && !banderaSiniestro && !banderaDeuda) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPOSOLICITUD_TIPO_329, null, nombreClase,
                        nombreMetodo, tipoDevolucion + ", " + tipoReserva + ", " + tipoPedido + ", " + tipoNumPayment
                                + ", " + tipoSiniestro + " o " + tipoDeuda + ".", input.getCodArea()).getDescripcion();
                flag = true;
            }
        }

        // Verificaci\u00F3n de fecha
        try {
            if (input.getFecha() == null || "".equals(input.getFecha().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_314, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
                Date fechaSolicitud = sdf.parse(input.getFecha());
                Date fechaActual = sdf.parse(sdf.format(new Date()));
                if (fechaSolicitud.before(fechaActual) && !banderaDeuda) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_MAYOR_ACTUAL_338, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                }
            }
        } catch (java.text.ParseException e) {
            log.error("Problema al convertir la fecha en clase " + nombreMetodo + " m\u00E9todo " + nombreClase + ".", e);
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_CONVERTIR_FECHA_165, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getIdDTS() == null || input.getIdDTS().trim().equals("")) {
            respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.IDDTS_NULO_36, null, nombreClase, nombreMetodo, null, input.getCodArea()));
            return respuestaSolicitud;
        } else if (!isNumeric(input.getIdDTS())) {
            respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo, null, input.getCodArea()));
            return respuestaSolicitud;
        } else {
            // Validaci\u00F3n DTS
            condiciones.clear();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TC_SC_DTS_ID, input.getIdDTS()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Distribuidor.CAMPO_ESTADO, estadoAlta));

            tipoDts = UtileriasBD.getOneRecord(conn, Distribuidor.CAMPO_TIPO, Distribuidor.N_TABLA, condiciones);
            if (tipoDts == null || tipoDts.equals("")) {
                respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_IDDTS_NO_EXISTE_173, null, nombreClase, nombreMetodo, null, input.getCodArea()));
                return respuestaSolicitud;
            }
        }

        if (banderaPayment && flag == false) {
        	
            // si la solicitud es tipo payment no es necesario validar buzon, se setea el buzon configurado
        	if("503".equals(input.getCodArea())){
        		 condiciones.clear();
                 condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TC_SC_DTS_ID, input.getIdDTS()));
                 condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                 condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Distribuidor.CAMPO_ESTADO, estadoAlta));
                 condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Distribuidor.CAMPO_TIPO, "EXTERNO"));
                 dtsExterno = UtileriasBD.selectCount(conn, Distribuidor.N_TABLA, condiciones);
                 
                 if (dtsExterno>0){
                	 condiciones.clear();
                     condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_TCSCDTSID, input.getIdDTS()));
                     condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_TCSCCATPAIS_ID, ID_PAIS.toString()));
                     condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BuzonSidra.CAMPO_ESTADO, estadoAlta));
                     condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BuzonSidra.CAMPO_TIPO_WORKFLOW, "TODAS"));
                     condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BuzonSidra.CAMPO_NIVEL, "2"));
                     idBuzonPayment = UtileriasBD.getOneRecord(conn,  BuzonSidra.CAMPO_TCSCBUZONID,BuzonSidra.N_TABLA,  condiciones);
                     input.setIdBuzon(idBuzonPayment); 
                 }else{
                	 input.setIdBuzon(idBuzonPayment);
                 }
        	}else{

        		input.setIdBuzon(idBuzonPayment);
        	}
            // unicamente al ser tipo payment se debe verificar el idPDV
            if (input.getIdPDV() == null || input.getIdPDV().trim().equals("")) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_IDPDV_VACIO_8, null, nombreClase, nombreMetodo, null, input.getCodArea())
                        .getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdPDV())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDPDV_NUM_315, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else {
                // se valida que exista el pdv
                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.CAMPO_TCSCPUNTOVENTAID, input.getIdPDV()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, PuntoVenta.CAMPO_ESTADO, estadoActivo));

                existencia = UtileriasBD.selectCount(conn, getParticion(PuntoVenta.N_TABLA, Conf.PARTITION, input.getIdDTS(), input.getCodArea()), condiciones);
                if (existencia <= 0) {
                    datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_PDV_472, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                }
            }

        } else {
            // en cualquier otro tipo de solicitud se valida el buzon
            if (input.getIdBuzon() == null || input.getIdBuzon().trim().equals("")) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBUZON_330, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdBuzon())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBUZON_NUM_331, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else {
                // se valida el Buz\u00F3n
                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_TCSCBUZONID, input.getIdBuzon()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_TCSCCATPAIS_ID, ID_PAIS.toString()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BuzonSidra.CAMPO_ESTADO, estadoAlta));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, BuzonSidra.CAMPO_TIPO_WORKFLOW, input.getTipoSolicitud()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, BuzonSidra.CAMPO_TIPO_WORKFLOW, tipoTodasSolicitudes));

                if (input.getOrigen().equalsIgnoreCase(origenMovil)  ) {
                    // si es origen m\u00F3vil debe ser del mismo dts
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_TCSCDTSID, input.getIdDTS()));
                }

                nivelBuzon = UtileriasBD.getOneRecord(conn, BuzonSidra.CAMPO_NIVEL, BuzonSidra.N_TABLA, condiciones);
                if (nivelBuzon == null || nivelBuzon.equals("")) {
                    respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_BUZONSIG_INVALIDO_534, null, nombreClase, nombreMetodo, "", input.getCodArea()));
                    return respuestaSolicitud;

                } else if (banderaDeuda) {
                    if (new Integer(nivelBuzon) == Conf.NIVEL_BUZON_TELCA) {
                        respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_NIVEL_BUZON_INCORRECTO_615,
                                null, nombreClase, nombreMetodo, "", input.getCodArea()));
                        return respuestaSolicitud;
                    }

                } else if (tipoDts.equalsIgnoreCase(Conf.DTS_INTERNO)) {

                    if (new Integer(nivelBuzon) != Conf.NIVEL_ZONACOMERCIAL && input.getOrigen().equalsIgnoreCase(origenMovil)) {
                        respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_NIVEL_BUZON_INCORRECTO_615,
                                null, nombreClase, nombreMetodo, "", input.getCodArea()));
                        return respuestaSolicitud;
                    }

                    if (input.getIdVendedor() == null || input.getIdVendedor().trim().equals("")) {

                        if ((new Integer(nivelBuzon) == Conf.NIVEL_BUZON_DTS || new Integer(nivelBuzon) == Conf.NIVEL_BUZON_TELCA)
                                && input.getOrigen().equalsIgnoreCase(origenPC)) {
                            // OK EL NIVEL ES CORRECTO PORQUE SE HACE DESDE LA WEB Y DEBE SER ENVIADO A NIVEL 2
                            if (new Integer(nivelBuzon) == Conf.NIVEL_BUZON_TELCA) {
                                banderaSolicitudTelca = true;
                            }
                            if (new Integer(nivelBuzon) == Conf.NIVEL_BUZON_DTS) {
                                banderaSolicitudDTS = true;
                            }
                        } else {
                            // SINO ES ERROR PORQUE NO ENVIO EL NIVEL CORRECTO
                            respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_NIVEL_BUZON_INCORRECTO_615,
                                    null, nombreClase, nombreMetodo, "", input.getCodArea()));
                            return respuestaSolicitud;
                        }

                    } else {
                        if (new Integer(nivelBuzon) == Conf.NIVEL_ZONACOMERCIAL /*&& input.getOrigen().equalsIgnoreCase(origenPC)*/) {
                            // esta en lo correcto porque lo esta haciendo desde la web pero de parte de un vendedor
                            banderaSolicitudDTS = true;
                        }
                        if (new Integer(nivelBuzon) == Conf.NIVEL_BUZON_DTS /*&& input.getOrigen().equalsIgnoreCase(origenPC)*/) {
                            // esta en lo correcto porque lo esta haciendo desde la web pero de parte de un vendedor
                            banderaSolicitudDTS = true;
                        }
                    }

                } else if (tipoDts.equalsIgnoreCase(Conf.DTS_EXTERNO)) {
                    if (new Integer(nivelBuzon) != Conf.NIVEL_BUZON_DTS && input.getOrigen().equalsIgnoreCase(origenMovil)) {
                        respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_NIVEL_BUZON_INCORRECTO_615, null, nombreClase, nombreMetodo, "", input.getCodArea()));
                        return respuestaSolicitud;
                    }

                    // validando desde la web
                    if (input.getIdVendedor() == null || input.getIdVendedor().equals("")) {
                        if (new Integer(nivelBuzon) != Conf.NIVEL_BUZON_TELCA && input.getOrigen().equalsIgnoreCase(origenPC)) {
                            respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_NIVEL_BUZON_INCORRECTO_615, null, nombreClase, nombreMetodo, "", input.getCodArea()));
                            return respuestaSolicitud;
                        } else {
                            banderaSolicitudTelca = true;
                        }
                    } else {
                        if (new Integer(nivelBuzon) == Conf.NIVEL_ZONACOMERCIAL /*&& input.getOrigen().equalsIgnoreCase(origenPC)*/) {
                            // esta en lo correcto porque lo esta haciendo desde la web pero de parte de un vendedor
                            banderaSolicitudDTS = true;
                        }
                        if (new Integer(nivelBuzon) == Conf.NIVEL_BUZON_DTS /*&& input.getOrigen().equalsIgnoreCase(origenPC)*/) {
                            // esta en lo correcto porque lo esta haciendo desde la web pero de parte de un vendedor
                            banderaSolicitudDTS = true;
                        }
                    }
                }
            }

            if (banderaDeuda) {
                // si es solicitud tipo deuda se verifica origen y nivel de buzon
                if (!banderaOrigenPC) {
                    respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_ERROR_ORIGEN_DEUDA_895, null, nombreClase, nombreMetodo, "", input.getCodArea()));
                    return respuestaSolicitud;
                }

                condiciones.clear();
                if (input.getIdBodega() != null && !input.getIdBodega().trim().equals("")) {
                    //se verifica por la fecha y todas las jornadas de la bodega
                    if (!isNumeric(input.getIdBodega())) {
                        respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBOD_NUM_293, null, nombreClase, nombreMetodo, "", input.getCodArea()));
                        return respuestaSolicitud;
                    } else {
                        // Validaci\u00F3n Bodega
                        condiciones.clear();
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BodegaVirtual.CAMPO_ESTADO, estadoAlta));
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, input.getIdBodega()));

                        String bodegaOrigen = UtileriasBD.getOneRecord(conn, BodegaVirtual.CAMPO_IDBODEGA_ORIGEN, getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones);
                        if (bodegaOrigen == null || bodegaOrigen.equals("")) {
                            log.error("No existe la bodega enviada.");
                            respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_ERROR_NIVEL_BODEGA_DEUDA_904, null, nombreClase, nombreMetodo, "", input.getCodArea()));
                            return respuestaSolicitud;
                        } else {
                            condiciones.clear();
                            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TC_SC_DTS_ID, input.getIdDTS()));
                            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TCSCBODEGAVIRTUALID, bodegaOrigen));
                            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Distribuidor.CAMPO_ESTADO, estadoAlta));

                            existencia = UtileriasBD.selectCount(conn, Distribuidor.N_TABLA, condiciones);
                            if (existencia < 1) {
                                respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_ERROR_BODEGA_DTS_DEUDA_905, null, nombreClase, nombreMetodo, "", input.getCodArea()));
                                return respuestaSolicitud;
                            }
                        }

                        condiciones.clear();
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodega()));
                    }
                } else {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IS_NULL_AND, Solicitud.CAMPO_TCSCBODEGAVIRTUALID, null));
                }

                // se verifica que no existan deudas de esa fecha por dts
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_TRUNC, Solicitud.CAMPO_FECHA, input.getFecha(), null, Conf.TXT_FORMATO_FECHA_CORTA));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_TCSCDTSID, input.getIdDTS()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Solicitud.CAMPO_TIPO_SOLICITUD, tipoDeuda));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND_NEQ, Solicitud.CAMPO_ESTADO, estadoCancelada));

                existencia = UtileriasBD.selectCount(conn, getParticion(Solicitud.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones);
                if (existencia > 0) {
                    respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_ERROR_DEUDA_FECHA_899, null, nombreClase, nombreMetodo, "", input.getCodArea()));
                    return respuestaSolicitud;
                }

                if (input.getTotalDeuda() == null || input.getTotalDeuda().equals("") || !isDecimal(input.getTotalDeuda())) {
                    respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_ERROR_INPUT_TOTAL_DEUDA_896, null, nombreClase, nombreMetodo, "", input.getCodArea()));
                    return respuestaSolicitud;
                }

                if (input.getIdVendedor() != null && !input.getIdVendedor().trim().equals("") && !isNumeric(input.getIdVendedor())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                }

                if (input.getIdBuzonSiguiente() != null && !input.getIdBuzonSiguiente().trim().equals("")) {
                    if (!isNumeric(input.getIdBuzonSiguiente())) {
                        respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_BUZONSIGUIENTE_NULO_533, null, nombreClase, nombreMetodo, "", input.getCodArea()));
                        return respuestaSolicitud;
                    } else if (flag == false) {
                        // se valida el Buzón
                        condiciones.clear();
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_TCSCBUZONID, input.getIdBuzonSiguiente()));
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_TCSCCATPAIS_ID, ID_PAIS.toString()));
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BuzonSidra.CAMPO_ESTADO, estadoAlta));
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, BuzonSidra.CAMPO_TIPO_WORKFLOW, input.getTipoSolicitud()));
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, BuzonSidra.CAMPO_TIPO_WORKFLOW, tipoTodasSolicitudes));

                        if (input.getOrigen().equalsIgnoreCase(origenMovil)) {
                            // si es origen m\u00F3vil siempre van a buzon de nivel 2 y debe ser del mismo dts
                            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_TCSCDTSID, input.getIdDTS()));
                        }

                        String nivelBuzonSiguiente = UtileriasBD.getOneRecord(conn, BuzonSidra.CAMPO_NIVEL, BuzonSidra.N_TABLA, condiciones);
                        if (nivelBuzonSiguiente == null || nivelBuzonSiguiente.equals("")) {
                            datosErroneos += getMensaje(Conf_Mensajes.MSJ_BUZONSIG_INVALIDO_534, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                            flag = true;
                        } else {
                            if (new Integer(nivelBuzon) <= new Integer(nivelBuzonSiguiente)) {
                                datosErroneos += getMensaje(Conf_Mensajes.MSJ_BUZONSIG_INVALIDO_534, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                                flag = true;
                            }
                        }
                    }
                }

                if (input.getDetallePagos() == null || input.getDetallePagos().isEmpty()) {
                    respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_DETALLEPAGO_NULO_439, null, nombreClase, nombreMetodo, "", input.getCodArea()));
                    return respuestaSolicitud;
                } else {
                    for (int i = 0; i < input.getDetallePagos().size(); i++) {
                        if (input.getDetallePagos().get(i).getFormaPago() == null || input.getDetallePagos().get(i).getFormaPago().equals("")) {
                            respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_FORMAPAGO_DETPAGO_NULO_442, null, nombreClase, nombreMetodo, (i + 1) + "", input.getCodArea()));
                            return respuestaSolicitud;
                        }

                        if (input.getDetallePagos().get(i).getMonto() == null || input.getDetallePagos().get(i).getMonto().equals("")) {
                            respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_MONTO_DETPAGO_NULO_443, null, nombreClase, nombreMetodo, (i + 1) + "", input.getCodArea()));
                            return respuestaSolicitud;
                        } else if (!isDecimal(input.getDetallePagos().get(i).getMonto())) {
                            respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_DECIMAL_INVALIDO_434, null, nombreClase, nombreMetodo, "Elemento de Detalle Pago No. " + (i + 1), input.getCodArea()));
                            return respuestaSolicitud;
                        }
                    }
                }

            } else {
                // se verifica la bodega al ser diferente de solicitud tipo payment o deuda
                if (input.getIdBodega() == null || input.getIdBodega().trim().equals("")) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBOD_292, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                } else if (!isNumeric(input.getIdBodega())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBOD_NUM_293, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                } else {
                    // Validaci\u00F3n Bodega
                    condiciones.clear();
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BodegaVirtual.CAMPO_ESTADO, estadoAlta));
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, input.getIdBodega()));

                    nivelBodega = UtileriasBD.getOneRecord(conn, BodegaVirtual.CAMPO_NIVEL,
                            getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones);
                    if (nivelBodega == null || nivelBodega.equals("")) {
                        log.error("No existe la bodega enviada.");
                        datosErroneos += getMensaje(Conf_Mensajes.MSJ_IDBODVIRTUAL_NO_EXISTE_172, null, nombreClase,
                                nombreMetodo, null, input.getCodArea()).getDescripcion();
                        flag = true;
                    }
                }

                if (banderaDevolucion && nivelBodega.equalsIgnoreCase("1") && banderaSolicitudTelca) {
                    banderaNoPromocionales = true;
                }
            }
        }

        if ((banderaPedido || banderaReserva || banderaDevolucion) && (banderaSolicitudTelca || banderaSolicitudDTS)) {
            // no se pide vendedor ni ruta/panel
        } else {
            // se verifica vendedor y ruta/panel
            if (!banderaPayment && !banderaDeuda) {
                if (input.getIdVendedor() == null || "".equals(input.getIdVendedor().trim())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_137, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                } else if (!isNumeric(input.getIdVendedor())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                }

                if (input.getIdTipo() == null || "".equals(input.getIdTipo().trim())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDTIPO_166, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                } else if (!isNumeric(input.getIdTipo())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDTIPO_NUM_167, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                }

                if (input.getTipo() == null || "".equals(input.getTipo().trim())) {
                    datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_TIPO_NULO_98, null, nombreClase, nombreMetodo, null, input.getCodArea())
                            .getDescripcion();
                    flag = true;
                } else if (!input.getTipo().equalsIgnoreCase(tipoRuta)
                        && !input.getTipo().equalsIgnoreCase(tipoPanel)) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_TIPOOFERTA_NO_DEFINIDO_424, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                } else {
                    // Validaci\u00F3n de existencia de tipo Ruta o Panel
                    condiciones.clear();
                    String tablaBuscar = "";
                    int numMensaje = 0;
                    if (input.getTipo().trim().equalsIgnoreCase(tipoRuta)) {
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TC_SC_RUTA_ID, input.getIdTipo()));
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Ruta.CAMPO_ESTADO, estadoAlta));

                        tablaBuscar = Ruta.N_TABLA;
                        numMensaje = Conf_Mensajes.MSJ_ERROR_NO_EXISTE_RUTA_182;
                    } else {
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCPANELID, input.getIdTipo()));
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Panel.CAMPO_ESTADO, estadoAlta));

                        tablaBuscar = Panel.N_TABLA;
                        numMensaje = Conf_Mensajes.MSJ_ERROR_NO_EXISTE_PANEL_181;
                    }

                    existencia = UtileriasBD.selectCount(conn, tablaBuscar, condiciones);
                    if (existencia <= 0) {
                        datosErroneos += getMensaje(numMensaje, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                        flag = true;
                    }
                }
            }
        }

        // validaciones para siniestros
        if (banderaSiniestro) {
            if (input.getTipoSiniestro() == null || input.getTipoSiniestro().trim().equals("")) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPOSONIESTRO_759, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else {
                banderaSiniestroTotal = input.getTipoSiniestro().equalsIgnoreCase(tipoSiniestroTotal);
                banderaSiniestroDispositivo = input.getTipoSiniestro().equalsIgnoreCase(tipoSiniestroDispositivo);

                if (!banderaSiniestroTotal && !input.getTipoSiniestro().equalsIgnoreCase(tipoSiniestroParcial)
                        && !banderaSiniestroDispositivo) {
                    datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPOSONIESTRO_NOCOINCIDE_760, null,
                            nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                }
            }

            if (!flag ) {
                // se valida que el vendedor este asociado al distribuidor
                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_TCSCDTSID, input.getIdDTS()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_VENDEDOR, input.getIdVendedor()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorDTS.CAMPO_ESTADO, estadoAlta));

                existencia = UtileriasBD.selectCount(conn, getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, input.getIdDTS(), input.getCodArea()), condiciones);
                if (existencia <= 0) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_VEND_DTS_NO_EXISTE_179, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                }
            }

            // si es siniestro de dispositivos siempre se verifica el listado
            if (banderaSiniestroDispositivo && (input.getDispositivos() == null || input.getDispositivos().isEmpty())) {
                respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_INPUT_LIST_DISPOSITIVOS_776, null,
                        nombreClase, nombreMetodo, null, input.getCodArea()));
                return respuestaSolicitud;
            }

            // sin importar el origen se verifican los dispositivos si hubieran
            if (input.getDispositivos() != null && !input.getDispositivos().isEmpty()) {
                respListados = validaDispositivos(input);

                // si el listado de dispositivos es correcto se valida contra base de datos y se verifican los folios
                if (respListados.equals("")) {
                    respuestaSolicitud.setRespuesta(OperacionSolicitud.validarDispositivos(conn,
                            input.getDispositivos(), estadoAlta, input.getIdTipo(), input.getTipo(), input.getCodArea(), ID_PAIS));
                    if (respuestaSolicitud.getRespuesta() != null) {
                        // si la validacion de dispositivos genera error se retorna
                        return respuestaSolicitud;
                    }
                } else {
                    datosErroneos += respListados;
                    flag = true;
                }
            }
        }

        if (banderaDevolucion && (input.getCausaSolicitud() == null || "".equals(input.getCausaSolicitud().trim()))) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_CAUSA_SOLICITUD_334, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
        }

        // se verifica si es seriada o no en base al origen
        if (banderaOrigenMovil) {
            if (input.getSeriado() == null ||  "".equals(input.getSeriado().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_SERIADO_335, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getSeriado())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_SERIADO_NUM_336, null, nombreClase,
                        nombreMetodo, "(" + estadoTrue + " o " + estadoFalse + ").", input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!input.getSeriado().equals(estadoTrue) && !input.getSeriado().equals(estadoFalse)) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_SERIADO_TIPO_337, null, nombreClase,
                        nombreMetodo, "(" + estadoTrue + " o " + estadoFalse + ").", input.getCodArea()).getDescripcion();
                flag = true;
            }
        } else {
            input.setSeriado(estadoFalse);
        }

        if (!banderaDeuda) {
            // si es solicitud tipo siniestro y de tipo total o dispositivo no se verifican los articulos
            if (banderaSiniestro && (banderaSiniestroTotal || banderaSiniestroDispositivo)) {
                // no se verifica ningun articulo
                // en caso de ser tipo parcial u otro tipo de solicitud se verifican normal los articulos
            } else if (input.getArticulos() == null || input.getArticulos().isEmpty()) {
                // si el listado es nulo o esta vacio se genera error
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULOS_370, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else if (banderaPayment) {
                // cuando es tipo payment, sin importar el origen se validan numeros
                respListados = validaNumeros(input, longTelefono);

                // si todo el listado de numeros es correcto se valida contra base de datos
                if (respListados.equals("")) {
                    respuestaSolicitud.setRespuesta(OperacionSolicitud.validarListadoDB(conn, input, estadoAlta,
                            tipoPDV, estadoActivadoPayment, estadoPendiente, ID_PAIS));
                    if (respuestaSolicitud.getRespuesta() != null) {
                        return respuestaSolicitud;
                    }
                } else {
                    datosErroneos += respListados;
                    flag = true;
                }

            } else if (flag == false) {
                // se separan las tareas de validaci\u00F3n seg\u00FAn el origen, siniestros siempre envian listados mezclados
                if (banderaOrigenMovil && !banderaSiniestro) {
                    // origenMovil siempre env\u00EDa listados seriados o no seriados
                    if (input.getSeriado().equals(estadoTrue) && (banderaDevolucion || banderaSiniestro)) {
                        respListados = validaSeriados(input);
                    } else {
                        respListados = validaNoSeriados(input, tipoInvSidra, banderaNoPromocionales, banderaDevolucion,
                                banderaSiniestro);
                    }

                    if (!respListados.equals("")) {
                        datosErroneos += respListados;
                        flag = true;
                    }

                } else {
                    // origenPC (WEB) en cualquier solicitud y origenMovil en siniestros env\u00EDan mezclados los datos de los articulos
                    InputSolicitud inputSeriado;
                    InputSolicitud inputNoSeriado;

                    List<InputArticuloSolicitud> articulosSeriados = new ArrayList<InputArticuloSolicitud>();
                    List<InputArticuloSolicitud> articulosNoSeriados = new ArrayList<InputArticuloSolicitud>();
                    inputSeriado = (InputSolicitud) input.clone();
                    inputNoSeriado = (InputSolicitud) input.clone();

                    // se separan los listados seriados y no seriados
                    for (int i = 0; i < input.getArticulos().size(); i++) {
                        if (input.getArticulos().get(i).getSerie() != null && !input.getArticulos().get(i).getSerie().equals("")) {
                            articulosSeriados.add(input.getArticulos().get(i));
                        } else {
                            articulosNoSeriados.add(input.getArticulos().get(i));
                        }
                        inputSeriado.setArticulos(articulosSeriados);
                        inputNoSeriado.setArticulos(articulosNoSeriados);
                    }

                    // se valida cuando la solicitud sea tipo pedido o reserva ya que no se permiten articulos seriados
                    if ((input.getTipoSolicitud().equalsIgnoreCase(tipoPedido) || input.getTipoSolicitud().equalsIgnoreCase(tipoReserva))&& inputSeriado.getArticulos().size() > 0) {
                            datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_SOLICITUD_SERIADOS_339, null,
                                    nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                            flag = true;
                    }

                    // se valida el listado de articulos seriados
                    respListados = validaSeriados(inputSeriado);
                    if (!respListados.equals("")) {
                        datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_INPUT_LISTADO_SERIADO_144, null,
                                nombreClase, nombreMetodo, respListados, input.getCodArea()).getDescripcion();
                        flag = true;
                    }

                    // si es correcto el anterior listado, se valida el listado de articulos NO seriados
                    if (!flag) {
                        respListados = validaNoSeriados(inputNoSeriado, tipoInvSidra, banderaNoPromocionales,
                                banderaDevolucion, banderaSiniestro);
                        if (!respListados.equals("")) {
                            if (banderaNoPromocionales == true) {
                                datosErroneos = respListados;
                            } else {
                                datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_INPUT_LISTADO_NO_SERIADO_145, null,
                                        nombreClase, nombreMetodo, respListados, input.getCodArea()).getDescripcion();
                            }
                            flag = true;
                        }
                    }
                }
            }
        }

        if (flag == true) {
            respuestaSolicitud.setRespuesta(getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase,
                    nombreMetodo, datosErroneos, input.getCodArea()));



        } else {
            Respuesta respuesta = new Respuesta();
            respuesta.setDescripcion("OK");
            respuestaSolicitud.setRespuesta(respuesta);
        }

        respuestaSolicitud.setResultado(banderaSolicitudTelca);
        respuestaSolicitud.setResultadoDTS(banderaSolicitudDTS);
        return respuestaSolicitud;
    }

    /**
     * Funci\u00F3n que valida la parte no seriada del json de las solicitudes.
     * 
     * @param banderaSiniestro
     * @param banderaDevolucion
     * @param banderaNoPromocionales
     * @param tipoInvSidra
     * @param input
     * @param estadoDevolucion
     * @param estadoDisponible
     * 
     * @return
     */
    private String validaNoSeriados(InputSolicitud input, String tipoInvSidra, boolean banderaNoPromocionales,
            boolean banderaDevolucion, boolean banderaSiniestro) {
        String nombreMetodo = "validaNoSeriados";
        String nombreClase = new CurrentClassGetter().getClassName();

        String datosErroneos = "";
        boolean flag = false;
        int numeroArticulo = 0;

        // se comentan las partes de estado por cambios en siniestros al no permitirse siniestro de art\u00EDculos en devolucion
        for (int i = 0; i < input.getArticulos().size(); i++) {
            if (flag == true)
                break;

            String idArticulo = null;
            String cant = null;
            String tipoInv = null;

            numeroArticulo = i + 1;

            if ((!banderaDevolucion && !banderaSiniestro) && (input.getArticulos().get(i).getSerie() != null
                        && !"".equals(input.getArticulos().get(i).getSerie()))) {
                    datosErroneos += " No se permiten art\u00EDculos seriados, el art\u00EDculo #" + numeroArticulo
                            + " tiene serie " + input.getArticulos().get(i).getSerie() + ".";
                    flag = true;
                    break;
            }

            if (input.getArticulos().get(i).getIdArticulo() != null) {
                idArticulo = input.getArticulos().get(i).getIdArticulo().trim();
            }
            if (input.getArticulos().get(i).getCantidad() != null) {
                cant = input.getArticulos().get(i).getCantidad().trim();
            }
            if (input.getArticulos().get(i).getTipoInv() != null) {
                tipoInv = input.getArticulos().get(i).getTipoInv().trim();
            }
   

            if (idArticulo == null || "".equals(idArticulo)) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_IDART_146, null, nombreClase,
                        nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!isNumeric(idArticulo)) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_IDART_NUM_147, null, nombreClase,
                        nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            }

            if (cant == null || "".equals(cant)) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_CANT_148, null, nombreClase, nombreMetodo,
                        numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!isNumeric(cant)) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_CANT_NUM_149, null, nombreClase,
                        nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            } else if (new Integer(cant) <= 0) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_CANT0_151, null, nombreClase,
                        nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            }

            if (tipoInv == null || tipoInv.equals("")) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_TIPOINV_152, null, nombreClase,
                        nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            } else {
                if (tipoInv.equalsIgnoreCase(tipoInvSidra) && banderaNoPromocionales == true) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_DEVOLUCION_PROMO_193, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                }
            }

           
        }

        numeroArticulo = 1;
        for (InputArticuloSolicitud detActual : input.getArticulos()) {
            if (flag == true)
                break;
            int indexArt = 1;

            for (InputArticuloSolicitud detalle : input.getArticulos()) {
                if (flag == true)
                    break;

                if (indexArt != numeroArticulo
                        && detalle.getIdArticulo().toUpperCase().trim()
                                .equals(detActual.getIdArticulo().toUpperCase().trim())
                        && detalle.getTipoInv().toUpperCase().trim().equals(detActual.getTipoInv().toUpperCase().trim())
                        /* && detalle.getEstado().toUpperCase().trim().equals(detActual.getEstado().toUpperCase().trim())*/
                ) {
                    log.error("El Art\u00EDculo #" + indexArt + " es igual al #" + numeroArticulo);
                    datosErroneos += " Los datos del Art\u00EDculo #" + indexArt + " son iguales al Art\u00EDculo #"
                            + numeroArticulo + ", ID de art\u00EDculo " + detActual.getIdArticulo() + ".";

                    flag = true;
                }
                indexArt++;
            }
            numeroArticulo++;
        }

        return datosErroneos;
    }

    /**
     * Funci\u00F3n que valida la parte seriada del json de las solicitudes.
     * 
     * @param input
     * @param banderaSiniestro
     * @param estadoDevolucion
     * @param estadoDisponible
     * @return
     */
    private String validaSeriados(InputSolicitud input) {
        String nombreMetodo = "validaSeriados";
        String nombreClase = new CurrentClassGetter().getClassName();

        String datosErroneos = "";
        boolean flag = false;
        int numeroArticulo = 0;

        // se comentan las partes de estado por cambios en siniestros al no permitirse siniestro de art\u00EDculos en devolucion
        for (int i = 0; i < input.getArticulos().size(); i++) {
            if (flag == true)
                break;
            String serie = null;
            String serieFinal = null;
            String tipoInv = null;
            numeroArticulo = i + 1;

            if (input.getArticulos().get(i).getSerie() != null) {
                serie = input.getArticulos().get(i).getSerie().trim();
            }
            if (input.getArticulos().get(i).getSerieFinal() != null) {
                serieFinal = input.getArticulos().get(i).getSerieFinal().trim();
            }
            if (input.getArticulos().get(i).getTipoInv() != null) {
                tipoInv = input.getArticulos().get(i).getTipoInv().trim();
            }


            if (serie == null || serie.equals("")) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_SERIE_153, null, nombreClase,
                        nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            }

            if (serieFinal != null && !serieFinal.equals("")) {
                if (!isNumeric(serie) || !isNumeric(serieFinal)) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_RANGO_SERIES_NUM_363, null, nombreClase,
                            nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                    flag = true;
                } else if (new BigInteger(serie).compareTo(new BigInteger(serieFinal)) >= 0) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_RANGO_SERIES_INV_364, null, nombreClase,
                            nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                    flag = true;
                }
            }

            if (tipoInv == null || tipoInv.equals("")) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_TIPOINV_152, null, nombreClase,
                        nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            }

           
        }

        numeroArticulo = 1;
        for (InputArticuloSolicitud detActual : input.getArticulos()) {
            if (flag == true)
                break;
            int indexArt = 1;

            for (InputArticuloSolicitud detalle : input.getArticulos()) {
                if (flag == true)
                    break;

                if (indexArt != numeroArticulo && (detalle.getSerie().toUpperCase().trim()
                        .equals(detActual.getSerie().toUpperCase().trim()))) {
                    log.error(" Los Art\u00EDculos #" + indexArt + " y #" + numeroArticulo + " tienen la misma serie.");
                    datosErroneos += " Los Art\u00EDculos #" + indexArt + " y #" + numeroArticulo
                            + " tienen la misma serie: " + detActual.getSerie() + ".";

                    flag = true;
                }
                indexArt++;
            }
            numeroArticulo++;
        }

        return datosErroneos;
    }

    /**
     * Funci\u00F3n para validar numeros de payment.
     * 
     * @param input
     * @param longTelefono
     * @return
     */
    private String validaNumeros(InputSolicitud input, String longTelefono) {
        String nombreMetodo = "validaNumeros";
        String nombreClase = new CurrentClassGetter().getClassName();

        String datosErroneos = "";
        boolean flag = false;
        int numeroArticulo = 0;

        for (int i = 0; i < input.getArticulos().size(); i++) {
            if (flag == true)
                break;

            String numero = null;

            numeroArticulo = i + 1;

            if (input.getArticulos().get(i).getIdArticulo() != null) {
                numero = input.getArticulos().get(i).getIdArticulo().trim();
            }

            if (numero == null || numero.equals("")) {
                datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_INPUT_NUMRECARGA_VACIO_721, null, nombreClase,
                        nombreMetodo, "Detalle #" + numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!isNumeric(numero)) {
                datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_INPUT_NUMRECARGA_722, null, nombreClase,
                        nombreMetodo, "Detalle #" + numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            } else if (numero.length() != new Integer(longTelefono)) {
                datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_INPUT_NUMRECARGA_LONG_723, null, nombreClase,
                        nombreMetodo, "Detalle #" + numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            }
        }

        numeroArticulo = 1;
        for (InputArticuloSolicitud detActual : input.getArticulos()) {
            if (flag == true)
                break;
            int indexArt = 1;

            for (InputArticuloSolicitud detalle : input.getArticulos()) {
                if (flag == true)
                    break;

                if (indexArt != numeroArticulo && detalle.getIdArticulo().toUpperCase().trim()
                        .equals(detActual.getIdArticulo().toUpperCase().trim())) {
                    log.error("El n\u00FAmero #" + indexArt + " es igual al #" + numeroArticulo);
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_NUMRECARGA_IGUALES_725, null, nombreClase,
                            nombreMetodo, indexArt + " y " + numeroArticulo + ".", input.getCodArea()).getDescripcion();

                    flag = true;
                }
                indexArt++;
            }
            numeroArticulo++;
        }

        return datosErroneos;
    }

    /**
     * Funci\u00F3n para validar el listado de dispositivos siniestrados.
     * 
     * @param input
     * @return
     */
    private String validaDispositivos(InputSolicitud input) {
        String nombreMetodo = "validaDispositivos";
        String nombreClase = new CurrentClassGetter().getClassName();

        String datosErroneos = "";
        boolean flag = false;
        int numeroItem = 0;

        for (int i = 0; i < input.getDispositivos().size(); i++) {
            if (flag == true)
                break;

            String codDispositivo = null;

            numeroItem = i + 1;

            if (input.getDispositivos().get(i).getCodigoDispositivo() != null) {
                codDispositivo = input.getDispositivos().get(i).getCodigoDispositivo().trim();
            }

            if (codDispositivo == null || codDispositivo.equals("")) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_CODDISPOSITIVO_163, null, nombreClase,
                        nombreMetodo, "Detalle #" + numeroItem + ".", input.getCodArea()).getDescripcion();
                flag = true;
            }
        }

        numeroItem = 1;
        for (InputDispositivo detActual : input.getDispositivos()) {
            if (flag == true)
                break;
            int indexArt = 1;

            for (InputDispositivo detalle : input.getDispositivos()) {
                if (flag == true)
                    break;

                if (indexArt != numeroItem && detalle.getCodigoDispositivo().toUpperCase().trim()
                        .equals(detActual.getCodigoDispositivo().toUpperCase().trim())) {
                    log.error("El c\u00F3digo de dispositivo #" + indexArt + " es igual al #" + numeroItem);
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_CODDISPOSITIVO_IGUALES_771, null, nombreClase,
                            nombreMetodo, indexArt + " y " + numeroItem + ".", input.getCodArea()).getDescripcion();

                    flag = true;
                }
                indexArt++;
            }
            numeroItem++;
        }

        return datosErroneos;
    }
    // ========================= M\u00E9todos para validaciones ============================//

    // ========================= M\u00E9todos para consultar solicitudes ===================//
    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET.
     * 
     * @param tipoPDV
     * @param tipoRuta
     * @param tipoPanel
     * 
     * @return
     */
    public static String[] obtenerCamposGet(String tipoPanel, String tipoRuta, String tipoPDV, BigDecimal ID_PAIS) {
        String nombreBodegaZona="";
        String idBodegaZona="";

        	nombreBodegaZona=" (CASE TIPO " +
"          WHEN 'RUTA' " +
"          THEN " +
"             (SELECT nombre " +
"                FROM tc_sc_bodegavirtual " +
"               WHERE tcscbodegavirtualid = (SELECT tcscbodegavirtualid " +
"                                              FROM tc_sc_ruta " +
"                                             WHERE tcscrutaid = idtipo)) " +
"          WHEN 'PANEL' " +
"          THEN " +
"             (SELECT nombre " +
"                FROM tc_sc_bodegavirtual " +
"               WHERE tcscbodegavirtualid = (SELECT tcscbodegavirtualid " +
"                                              FROM tc_sc_panel " +
"                                             WHERE tcscpanelid = idtipo)) " +
"       END) " +
"          nombreBodegaZona " ;
        	
        	idBodegaZona="(CASE TIPO " +
        			"          WHEN 'RUTA' " +
        			"          THEN " +
        			"             (SELECT tcscbodegavirtualid " +
        			"                FROM tc_sc_ruta " +
        			"               WHERE tcscrutaid = idtipo) " +
        			"          WHEN 'PANEL' " +
        			"          THEN " +
        			"             (SELECT tcscbodegavirtualid " +
        			"                FROM tc_sc_panel " +
        			"               WHERE tcscpanelid = idtipo) " +
        			"       END) " +
        			"          idBodegaZona "; 

 
    	
    	String nombreTipo = "(CASE TIPO "
                + "WHEN '" + tipoPDV + "' THEN (" + "SELECT " + PuntoVenta.CAMPO_NOMBRE + " FROM " + PuntoVenta.N_TABLA
                    + " WHERE " + PuntoVenta.N_TABLA_ID + " = " + Solicitud.CAMPO_IDTIPO + " AND TCSCCATPAISID = " + ID_PAIS + ") "
                + "WHEN '" + tipoRuta + "' THEN (" + "SELECT " + Ruta.CAMPO_NOMBRE + " FROM " + Ruta.N_TABLA
                    + " WHERE " + Ruta.CAMPO_TC_SC_RUTA_ID + " = " + Solicitud.CAMPO_IDTIPO + " AND TCSCCATPAISID = " + ID_PAIS + ") "
                + "WHEN '" + tipoPanel + "' THEN (" + "SELECT " + Panel.CAMPO_NOMBRE + " FROM " + Panel.N_TABLA
                    + " WHERE " + Panel.CAMPO_TCSCPANELID + " = " + Solicitud.CAMPO_IDTIPO + " AND TCSCCATPAISID = " + ID_PAIS + ") "
                + "END) AS NOMBRETIPO";

        
        String campos[] = {
                Solicitud.CAMPO_TCSCSOLICITUDID,
                Solicitud.CAMPO_TCSCBODEGAVIRTUALID,
                "DECODE (S.tcscbodegavirtualid, " +
				"               NULL, NULL, " +
				"               (SELECT A.nombre " +
				"                  FROM tc_sc_bodegavirtual A " +
				"                 WHERE A.tcscbodegavirtualid = S.tcscbodegavirtualid)) " +
				"          AS NOMBRE_BODEGA ",
				idBodegaZona,
				nombreBodegaZona,
                Solicitud.CAMPO_TCSCDTSID,
                "(SELECT D." + Distribuidor.CAMPO_NOMBRES + " FROM " + Distribuidor.N_TABLA + " D WHERE D."
                    + Distribuidor.CAMPO_TC_SC_DTS_ID + " = S." + Solicitud.CAMPO_TCSCDTSID + " AND TCSCCATPAISID = " + ID_PAIS + ") AS NOMB_DTS",
                Solicitud.CAMPO_BUZON_ORIGEN,
                Solicitud.CAMPO_TCSCBUZONID,
                "(SELECT NOMBRE FROM TC_SC_BUZONSIDRA WHERE TCSCBUZONID = S.TCSCBUZONID AND TCSCCATPAISID = " + ID_PAIS + ") AS NOMB_BUZON",
                Solicitud.CAMPO_BUZON_ANTERIOR,
                Solicitud.CAMPO_FECHA,
                Solicitud.CAMPO_TIPO_SOLICITUD,
                Solicitud.CAMPO_IDVENDEDOR,
                    "(SELECT " + VendedorDTS.CAMPO_USUARIO + " FROM " + VendedorDTS.N_TABLA + " WHERE "
                            + VendedorDTS.CAMPO_VENDEDOR + " = " + Solicitud.CAMPO_IDVENDEDOR + " AND TCSCCATPAISID = " + ID_PAIS + ") AS "
                            + VendedorDTS.CAMPO_USUARIO,
                    "(SELECT " + VendedorDTS.CAMPO_NOMBRE + " FROM " + VendedorDTS.N_TABLA + " WHERE "
                            + VendedorDTS.CAMPO_VENDEDOR + " = " + Solicitud.CAMPO_IDVENDEDOR + " AND TCSCCATPAISID = " + ID_PAIS + ") AS "
                            + VendedorDTS.CAMPO_NOMBRE,
                    "(SELECT " + VendedorDTS.CAMPO_APELLIDO + " FROM " + VendedorDTS.N_TABLA + " WHERE "
                            + VendedorDTS.CAMPO_VENDEDOR + " = " + Solicitud.CAMPO_IDVENDEDOR + " AND TCSCCATPAISID = " + ID_PAIS + ") AS "
                            + VendedorDTS.CAMPO_APELLIDO,
                Solicitud.CAMPO_CAUSA_SOLICITUD,
                Solicitud.CAMPO_OBSERVACIONES,
                Solicitud.CAMPO_SERIADO,
                Solicitud.CAMPO_ORIGEN,
                Solicitud.CAMPO_TIPO_SINIESTRO,
                Solicitud.CAMPO_IDTIPO,
                Solicitud.CAMPO_TIPO,
                nombreTipo,
                Solicitud.CAMPO_IDJORNADA,
                Solicitud.CAMPO_ORIGEN_CANCELACION,
                Solicitud.CAMPO_OBS_CANCELACION,
                Solicitud.CAMPO_TOTAL_DEUDA,
                Solicitud.CAMPO_TASA_CAMBIO,
                Solicitud.CAMPO_ESTADO,
                Solicitud.CAMPO_CREADO_EL,
                Solicitud.CAMPO_CREADO_POR,
                Solicitud.CAMPO_MODIFICADO_EL,
                Solicitud.CAMPO_MODIFICADO_POR
        };

        return campos;
    }

    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET y en
     * este caso tambi\u00E9n todos los que se insertar\u00E9n en el m\u00E9todo POST de la
     * tabla relacionada.
     * 
     * @param metodo
     * @return
     */
    public static String[] obtenerCamposTablaHija(int metodo) {
        if (metodo == Conf.METODO_GET) {
            String campos[] = {
                    SolicitudDet.CAMPO_TCSCSOLICITUDDETID,
                    SolicitudDet.CAMPO_TCSCSOLICITUDID,
                    SolicitudDet.CAMPO_COD_DISPOSITIVO,
                    SolicitudDet.CAMPO_ARTICULO,
                    SolicitudDet.CAMPO_DESCRIPCION,
                    SolicitudDet.CAMPO_SERIE,
                    SolicitudDet.CAMPO_SERIE_FINAL,
                    SolicitudDet.CAMPO_SERIE_ASOCIADA,
                    SolicitudDet.CAMPO_CANTIDAD,
                    SolicitudDet.CAMPO_TIPO_INV,
                    SolicitudDet.CAMPO_OBSEVACIONES,
                    SolicitudDet.CAMPO_ESTADO,
                    SolicitudDet.CAMPO_CREADO_EL,
                    SolicitudDet.CAMPO_CREADO_POR,
                    SolicitudDet.CAMPO_MODIFICADO_EL,
                    SolicitudDet.CAMPO_MODIFICADO_POR
            };
            return campos;

        } else {
            String campos[] = {
                    SolicitudDet.CAMPO_TCSCSOLICITUDDETID,
                    SolicitudDet.CAMPO_TCSCSOLICITUDID,
                    SolicitudDet.CAMPO_ARTICULO,
                    SolicitudDet.CAMPO_DESCRIPCION,
                    SolicitudDet.CAMPO_SERIE,
                    SolicitudDet.CAMPO_SERIE_FINAL,
                    SolicitudDet.CAMPO_SERIE_ASOCIADA,
                    SolicitudDet.CAMPO_CANTIDAD,
                    SolicitudDet.CAMPO_TIPO_INV,
                    SolicitudDet.CAMPO_ESTADO,
                    SolicitudDet.CAMPO_CREADO_EL,
                    SolicitudDet.CAMPO_CREADO_POR
            };
            return campos;
        }
    }
    // ========================= M\u00E9todos para consultar solicitudes ==================//

    // ========================= M\u00E9todos para crear solicitudes =======================//
    /**
     * Funci\u00F3n que indica los campos que se insertar\u00E9n en el m\u00E9todo POST.
     * 
     * @return campos Array con los nombres de los campos.
     */
    public static String[] obtenerCamposPost() {
        String campos[] = {
                Solicitud.CAMPO_TCSCSOLICITUDID,
                Solicitud.CAMPO_TCSCCATPAISID,
                Solicitud.CAMPO_TCSCBODEGAVIRTUALID,
                Solicitud.CAMPO_TCSCDTSID,
                Solicitud.CAMPO_TCSCBUZONID,
                Solicitud.CAMPO_BUZON_ANTERIOR,
                Solicitud.CAMPO_FECHA,
                Solicitud.CAMPO_TIPO_SOLICITUD,
                Solicitud.CAMPO_IDVENDEDOR,
                Solicitud.CAMPO_CAUSA_SOLICITUD,
                Solicitud.CAMPO_OBSERVACIONES,
                Solicitud.CAMPO_SERIADO,
                Solicitud.CAMPO_ORIGEN,
                Solicitud.CAMPO_TIPO_SINIESTRO,
                Solicitud.CAMPO_IDTIPO,
                Solicitud.CAMPO_TIPO,
                Solicitud.CAMPO_IDJORNADA,
                Solicitud.CAMPO_TOTAL_DEUDA,
                Solicitud.CAMPO_TASA_CAMBIO,
                Solicitud.CAMPO_BUZON_ORIGEN,
                Solicitud.CAMPO_ESTADO,
                Solicitud.CAMPO_CREADO_EL,
                Solicitud.CAMPO_CREADO_POR
        };

        return campos;
    }

    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
     * 
     * @param conn
     * @param input
     *            Objeto con los datos enviados mediante POST.
     * @param sequencia
     *            Nombre de la secuencia a utilizar para la inserci\u00F3n.
     * @param tipoSiniestro
     * @param tipoNumPayment
     * @param banderaBuzonTelca
     * @param banderaOrigenPC
     * @param estadoAceptada
     * @param banderaDeuda 
     * @param estadoPendiente 
     * @param estadoEnviado 
     * @param banderaSolicitudDTS 
     * @return inserts Listado de cadenas con los valores a insertar.
     * @throws Exception 
     */
    public static List<String> obtenerInsertsPost(Connection conn, InputSolicitud input, String sequencia,
            String estadoAbierta, String tipoNumPayment, String tipoSiniestro, String tipoPDV,
            String idJornada, boolean banderaSolicitudTelca,  String estadoAceptada,
            boolean banderaDeuda, String estadoPendiente, String estadoEnviado, boolean banderaSolicitudDTS, BigDecimal ID_PAIS) throws Exception {
        List<String> inserts = new ArrayList<String>();
        String idVendedor = "";
        String idBodega = "";
        String insertIdJornada = "";
        String idTipo = "";
        String tipo = "";
        String estado = "";
        String totalDeuda = "";
        String idBuzon = "";
        String idBuzonAnterior = "";
        String observaciones = "";
        String insertTasaCambio = "";

        if (banderaDeuda) {
            MathContext mc = new MathContext(200, RoundingMode.HALF_UP);
            String tasaCambio = OperacionVendxDTS.getTasaCambio(input.getCodArea());
            UtileriasJava.validarParametroVacio(tasaCambio);
            BigDecimal deuda;
            int cantDecimalesBD = new Integer(UtileriasJava.getConfig(conn, Conf.GRUPO_PARAM_VENTA, Conf.CANT_DECIMALES_BD, input.getCodArea()));

            if (input.getIdBodega() != null && !input.getIdBodega().trim().equals("")) {
                idBodega = UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdBodega(), Conf.INSERT_SEPARADOR_SI);
            } else {
                idBodega = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
            }
            if (input.getIdVendedor() != null && !input.getIdVendedor().trim().equals("")) {
                idVendedor = UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdVendedor(), Conf.INSERT_SEPARADOR_SI);
            } else {
                idVendedor = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
            }

            idTipo = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
            tipo = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
            insertIdJornada = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);

            insertTasaCambio = UtileriasJava.setInsert(Conf.INSERT_NUMERO, tasaCambio, Conf.INSERT_SEPARADOR_SI);
            deuda = new BigDecimal(input.getTotalDeuda()).divide(new BigDecimal(tasaCambio), mc);
            totalDeuda = UtileriasJava.setInsert(Conf.INSERT_NUMERO, UtileriasJava.redondearBD(deuda, cantDecimalesBD) + "", Conf.INSERT_SEPARADOR_SI);
            observaciones = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);

            if (input.getIdBuzonSiguiente() != null && !input.getIdBuzonSiguiente().trim().equals("")) {
                idBuzon = UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdBuzonSiguiente(), Conf.INSERT_SEPARADOR_SI);
                idBuzonAnterior = UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdBuzon(), Conf.INSERT_SEPARADOR_SI);
                estado = estadoEnviado;
            } else {
                idBuzon = UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdBuzon(), Conf.INSERT_SEPARADOR_SI);
                idBuzonAnterior = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
                estado = estadoPendiente;
            }

        } else {
            insertTasaCambio = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
            totalDeuda = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
            idBuzonAnterior = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
            idBuzon = UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdBuzon(), Conf.INSERT_SEPARADOR_SI);
            observaciones = UtileriasJava.setInsert(Conf.INSERT_TEXTO, UtileriasJava.getValue(input.getObservaciones()), Conf.INSERT_SEPARADOR_SI);

            if (input.getTipoSolicitud().equalsIgnoreCase(tipoNumPayment)) {
                idBodega = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
                idVendedor = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
                idTipo = UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdPDV(), Conf.INSERT_SEPARADOR_SI);
                tipo = UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, tipoPDV, Conf.INSERT_SEPARADOR_SI);
                insertIdJornada = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
                estado = estadoAceptada;

            } else {
                idBodega = UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdBodega(), Conf.INSERT_SEPARADOR_SI);

                if (banderaSolicitudDTS) {
                    estado = estadoAbierta;
                }

                if (banderaSolicitudTelca) {
                    estado = estadoAceptada;
                } else {
                    estado = estadoAbierta;
                }

                log.trace("estado:"+estado);
                if (input.getIdVendedor() != null && !input.getIdVendedor().equals("")) {
                    idVendedor = UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdVendedor(),
                            Conf.INSERT_SEPARADOR_SI);
                } else {
                    idVendedor = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
                }

                if (input.getIdTipo() != null && !input.getIdTipo().equals("")) {
                    idTipo = UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdTipo(), Conf.INSERT_SEPARADOR_SI);
                } else {
                    idTipo = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
                }

                if (input.getTipo() != null && !input.getTipo().equals("")) {
                    tipo = UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getTipo(), Conf.INSERT_SEPARADOR_SI);
                } else {
                    tipo = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
                }

                if (idJornada != null && !idJornada.equals("")) {
                    insertIdJornada = UtileriasJava.setInsert(Conf.INSERT_NUMERO, idJornada, Conf.INSERT_SEPARADOR_SI);
                } else {
                    insertIdJornada = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
                }
            }
        }

        if (input.getTipoSolicitud().equalsIgnoreCase(tipoSiniestro)) {
            tipoSiniestro = UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getTipoSiniestro(),
                    Conf.INSERT_SEPARADOR_SI);
        } else {
            tipoSiniestro = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
        }

        String valores = "";
        valores = "("
                + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, ID_PAIS.toString(), Conf.INSERT_SEPARADOR_SI)
                + idBodega
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdDTS(), Conf.INSERT_SEPARADOR_SI)
                + idBuzon
                + idBuzonAnterior
                + UtileriasJava.setInsertFecha(Conf.INSERT_FECHA, input.getFecha(), Conf.TXT_FORMATO_FECHA_CORTA, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getTipoSolicitud().toUpperCase(), Conf.INSERT_SEPARADOR_SI)
                + idVendedor
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, UtileriasJava.getValue(input.getCausaSolicitud()), Conf.INSERT_SEPARADOR_SI)
                + observaciones
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getSeriado(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getOrigen().toUpperCase(), Conf.INSERT_SEPARADOR_SI)
                + tipoSiniestro
                + idTipo
                + tipo
                + insertIdJornada
                + totalDeuda
                + insertTasaCambio
                + idBuzon
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, estado.toUpperCase(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
        + ") ";
        inserts.add(valores);

        return inserts;
    }

    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST de la
     * tabla relacionada.
     * 
     * @param idPadre
     * @param input
     * @param sequencia
     * @param tipoNumPayment
     * @param tipoInvSidra
     * @param estadoPendiente
     * @return
     * @throws SQLException
     */
    public static List<String> obtenerInsertsPostHijo( int idPadre, InputSolicitud input,
            String sequencia,  String tipoNumPayment, String tipoInvSidra, String nombreNumPayment,
            String estadoPendiente, BigDecimal ID_PAIS) throws SQLException {
        List<String> inserts = new ArrayList<String>();
        String idArticulo = "";
        String descripcion = "";
        String valores = "";
        String tipoInv = "";
        String cantidad = "";

        for (int i = 0; i < input.getArticulos().size(); i++) {
            idArticulo = input.getArticulos().get(i).getIdArticulo();

            if (input.getTipoSolicitud().equalsIgnoreCase(tipoNumPayment)) {
                cantidad = UtileriasJava.setInsert(Conf.INSERT_NUMERO, "1", Conf.INSERT_SEPARADOR_SI);
                tipoInv = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
                descripcion = UtileriasJava.setInsert(Conf.INSERT_TEXTO, nombreNumPayment, Conf.INSERT_SEPARADOR_SI);

            } else {
                cantidad = UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getArticulos().get(i).getCantidad(),
                        Conf.INSERT_SEPARADOR_SI);
                tipoInv = UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getArticulos().get(i).getTipoInv(),
                        Conf.INSERT_SEPARADOR_SI);

                if (input.getArticulos().get(i).getTipoInv().equalsIgnoreCase(tipoInvSidra)) {
                    descripcion = "(SELECT " + Promocionales.CAMPO_DESCRIPCION + " FROM " + Promocionales.N_TABLA
                            + " WHERE " + Promocionales.CAMPO_TCSCARTPROMOCIONALID + " = " + idArticulo + " AND TCSCCATPAISID = " + ID_PAIS + "), ";
                } else {
                    descripcion = "(SELECT " + ArticulosSidra.CAMPO_DESCRIPCION + " FROM " + ArticulosSidra.N_TABLA
                            + " WHERE " + ArticulosSidra.CAMPO_ARTICULO + " = " + idArticulo + " AND TCSCCATPAISID = " + ID_PAIS + "), ";
                }
            }

            valores = UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPadre + "", Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idArticulo, Conf.INSERT_SEPARADOR_SI)
                    + descripcion
                    + UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI)
                    + cantidad
                    + tipoInv
                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO, estadoPendiente, Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO);
            inserts.add(valores);
        }

        return inserts;
    }

    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST de la
     * tabla relacionada despu\u00E9s de verificar la existencia.
     * 
     * @param conn
     * @param idPadre
     * @param input
     * @param sequencia
     * @param estado
     * @param tipoInvSidra
     * @param usuario
     * @return
     * @throws SQLException
     */
    public static List<String> obtenerInsertsPostHijoAfter(Connection conn, int idPadre, InputArticuloSolicitud input,
            String sequencia, String estado, String tipoInvSidra, String usuario, String origen, BigDecimal ID_PAIS) throws SQLException {
        List<String> inserts = new ArrayList<String>();
        String valores = "";
  
        String idArticulo2="";
        String idArticulo = null;
        if (input.getIdArticulo() != null && !input.getIdArticulo().equals("")) {
            idArticulo = input.getIdArticulo();
            idArticulo2=input.getIdArticulo();
        } else if (input.getSerie() != null && !input.getSerie().equals("")) {
            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_SERIE, input.getSerie()));
            idArticulo = "(" + UtileriasBD.armarQuerySelectField(Inventario.N_TABLA, Inventario.CAMPO_ARTICULO, condiciones, null) + ")";
            idArticulo2=UtileriasBD.getOneRecord(conn, Inventario.CAMPO_ARTICULO, Inventario.N_TABLA, condiciones);
            
            if(idArticulo2==null || "".equals(idArticulo2)){
            	condiciones = new ArrayList<Filtro>();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, "substr("+Inventario.CAMPO_SERIE+",1,18)", input.getSerie()));
                idArticulo = "(" + UtileriasBD.armarQuerySelectField(Inventario.N_TABLA, Inventario.CAMPO_ARTICULO, condiciones, null) + ")";
                idArticulo2=UtileriasBD.getOneRecord(conn, Inventario.CAMPO_ARTICULO, Inventario.N_TABLA, condiciones);
            }
        }
        
        List<Filtro> condiciones1 = new ArrayList<Filtro>();     
        condiciones1.clear();
        condiciones1.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ARTICULO, idArticulo2));
        condiciones1.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_TCSCCATPAISID, ""+ID_PAIS));
        tipoGrupo = UtileriasBD.getOneRecord(conn, Inventario.CAMPO_TIPO_GRUPO_SIDRA, Inventario.N_TABLA, condiciones1);

       
        
        String serie = "";
        if (input.getSerie() != null && !input.getSerie().equals("")) {
        	if (tipoGrupo.equals("SIMCARD") && origen.equalsIgnoreCase("MOVIL")){
        		serie = "(SELECT " + Inventario.CAMPO_SERIE + " FROM " + Inventario.N_TABLA + " WHERE "
                         + Inventario.CAMPO_ARTICULO + " = " + idArticulo + " AND TCSCCATPAISID = " + ID_PAIS + " AND SUBSTR(SERIE_COMPLETA,1,18)='"+input.getSerie()+"'), ";
        	}else{
        		serie = UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getSerie(), Conf.INSERT_SEPARADOR_SI);
        	}
        } else {
            serie = null + ", ";
        }

        String serieAsociada = "";
        if (input.getSerie() != null && !input.getSerie().equals("") && input.getSerieAsociada() != null
                && !input.getSerieAsociada().equals("")) {
        	
                    serieAsociada = UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getSerieAsociada(), Conf.INSERT_SEPARADOR_SI);
        } else {
            serieAsociada = null + ", ";
        }

        String cantidad = null;
        if ((input.getSerie() == null || input.getSerie().equals("")) && input.getCantidad() != null
                && !input.getCantidad().equals("")) {
            cantidad = input.getCantidad();
        } else {
            cantidad = "1";
        }

        String serieFinal = "";
        if (input.getSerie() != null && !input.getSerie().equals("") && input.getSerieFinal() != null
                && !input.getSerieFinal().equals("")) {
           	if (tipoGrupo.equals("SIMCARD") && origen.equalsIgnoreCase("MOVIL")){
           		serieFinal = "(SELECT " + Inventario.CAMPO_SERIE + " FROM " + Inventario.N_TABLA + " WHERE "
                         + Inventario.CAMPO_ARTICULO + " = " + idArticulo + " AND TCSCCATPAISID = " + ID_PAIS + " AND SUBSTR(SERIE_COMPLETA,1,18)='"+input.getSerieFinal()+"'), ";
        	}else{
        		serieFinal = UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getSerieFinal(),Conf.INSERT_SEPARADOR_SI);
        	}
            cantidad = (new BigInteger(input.getSerieFinal()).subtract(new BigInteger(input.getSerie()))
                    .add(BigInteger.ONE)) + "";

            serieAsociada = null + ", ";
        } else {
            serieFinal = null + ", ";
        }

        String descripcion = "";
        if (input.getTipoInv().equalsIgnoreCase(tipoInvSidra)) {
            descripcion = "(SELECT " + Promocionales.CAMPO_DESCRIPCION + " FROM " + Promocionales.N_TABLA + " WHERE "
                    + Promocionales.CAMPO_TCSCARTPROMOCIONALID + " = " + idArticulo + " AND TCSCCATPAISID = " + ID_PAIS + "), ";
        } else {
            descripcion = "(SELECT " + ArticulosSidra.CAMPO_DESCRIPCION + " FROM " + ArticulosSidra.N_TABLA + " WHERE "
                    + ArticulosSidra.CAMPO_ARTICULO + " = " + idArticulo + " AND TCSCCATPAISID = " + ID_PAIS + "), ";
        }

        valores = UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPadre + "", Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idArticulo, Conf.INSERT_SEPARADOR_SI)
                + descripcion
                + serie
                + serieFinal
                + serieAsociada
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, cantidad, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getTipoInv(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, estado, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, usuario, Conf.INSERT_SEPARADOR_NO);
        inserts.add(valores);

        return inserts;
    }
    // ========================= M\u00E9todos para crear solicitudes =======================//

    // ========================= M\u00E9todos para modificar solicitudes ===================//
    /**
     * Funci\u00F3n que retorna los campos a actualizar al modificar una solicitud.
     * 
     * @param conn
     * @param usuario
     * @param estado
     * @param origen
     * @param observacion
     * @param idBuzonSiguente
     * @return
     * @throws SQLException
     */
    public static String[][] obtenerCamposPutDel( String usuario, String estado, String origen,
            String observacion, String idBuzonSiguente) throws SQLException {
        String idBuzonAnterior = "";

        if (origen != null) {
            origen = UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, origen);
        } else {
            origen = UtileriasJava.setUpdate(Conf.INSERT_NULL, null);
        }

        if (observacion != null && !observacion.equals("")) {
            observacion = UtileriasJava.setUpdate(Conf.INSERT_TEXTO, observacion);
        } else {
            observacion = UtileriasJava.setUpdate(Conf.INSERT_NULL, null);
        }

        if (idBuzonSiguente != null && !idBuzonSiguente.equals("")) {
            idBuzonAnterior = Solicitud.CAMPO_TCSCBUZONID;
            idBuzonSiguente = UtileriasJava.setUpdate(Conf.INSERT_NUMERO, idBuzonSiguente);
        } else {
            idBuzonAnterior = Solicitud.CAMPO_BUZON_ANTERIOR;
            idBuzonSiguente = Solicitud.CAMPO_TCSCBUZONID;
        }

        String campos[][] = {
                { Solicitud.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estado) },
                { Solicitud.CAMPO_ORIGEN_CANCELACION, origen },
                { Solicitud.CAMPO_OBS_CANCELACION, observacion },
                { Solicitud.CAMPO_TCSCBUZONID, idBuzonSiguente },
                { Solicitud.CAMPO_BUZON_ANTERIOR, idBuzonAnterior },
                { Solicitud.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                { Solicitud.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) }
        };

        return campos;
    }
    // ========================= M\u00E9todos para modificar solicitudes ===================//

    // ========================= M\u00E9todos para obtener condiciones =====================//
    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes
     * consultas seg\u00FAn el m\u00E9todo que se trabaje.
     * 
     * @param input
     *            Objeto con los datos enviados mediante POST.
     * @param metodo
     *            Valor que indica tipo de operaci\u00F3n que se desea realizar en el
     *            servicio.
     * @return condiciones Listado con los filtros indicados seg\u00FAn el m\u00E9todo
     *         deseado.
     */
    public static List<Filtro> obtenerCondiciones(InputSolicitud input, int metodo, BigDecimal ID_PAIS) {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));

        if (metodo == Conf.METODO_GET) {
            if (input.getIdSolicitud() != null && !input.getIdSolicitud().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_TCSCSOLICITUDID,
                        input.getIdSolicitud()));
            }
            if (input.getOrigen() != null && !input.getOrigen().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Solicitud.CAMPO_ORIGEN,
                        input.getOrigen()));
            }
            if (input.getIdVendedor() != null && !input.getIdVendedor().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_IDVENDEDOR,
                        input.getIdVendedor()));
            }
            if (input.getIdBodega() != null && !input.getIdBodega().equals("")) {
                if (input.getIdBodega().equals("0")) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IS_NULL_AND, Solicitud.CAMPO_TCSCBODEGAVIRTUALID, null));
                } else {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodega()));
                }
            }
            if (input.getIdDTS() != null && !input.getIdDTS().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_TCSCDTSID,
                        input.getIdDTS()));
            }
            if (input.getIdBuzon() != null && !input.getIdBuzon().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_TCSCBUZONID,
                        input.getIdBuzon()));
            }
            if (input.getIdBuzonAnterior() != null && !input.getIdBuzonAnterior().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_BUZON_ANTERIOR,
                        input.getIdBuzonAnterior()));
            }
            if (input.getFechaInicio() != null && input.getFechaFin() != null && !input.getFechaInicio().equals("")
                    && !input.getFechaFin().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_RANGO_FECHAS, Solicitud.CAMPO_FECHA,
                        input.getFechaInicio(), input.getFechaFin(), Conf.TXT_FORMATO_FECHA_CORTA));
            }
            if (input.getTipoSolicitud() != null && !input.getTipoSolicitud().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Solicitud.CAMPO_TIPO_SOLICITUD,
                        input.getTipoSolicitud()));
            }
            if (input.getTipoSiniestro() != null && !input.getTipoSiniestro().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Solicitud.CAMPO_TIPO_SINIESTRO,
                        input.getTipoSiniestro()));
            }
            if (input.getSeriado() != null && !input.getSeriado().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_SERIADO,
                        input.getSeriado()));
            }
            if (input.getIdJornada() != null && !input.getIdJornada().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_IDJORNADA,
                        input.getIdJornada()));
            }
            if (input.getIdTipo() != null && !input.getIdTipo().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_IDTIPO,
                        input.getIdTipo()));
            }
            if (input.getTipo() != null && !input.getTipo().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Solicitud.CAMPO_TIPO, input.getTipo()));
            }
            if (input.getCreado_por() != null && !input.getCreado_por().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Solicitud.CAMPO_CREADO_POR,
                        input.getCreado_por()));
            }
            if (input.getEstado() != null && !input.getEstado().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Solicitud.CAMPO_ESTADO,
                        input.getEstado()));
            }
            if(input.getIdBodegaZona()!= null && !"".equals(input.getIdBodegaZona())){
            	condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Solicitud.CAMPO_IDTIPO, " (SELECT Tcscrutaid idtipo " +
            			"                         FROM TC_Sc_ruta " +
            			"                        WHERE tcscbodegavirtualid IN ("+input.getIdBodegaZona()+") " +
            			"                       UNION " +
            			"                       SELECT tcscpanelid idtipo " +
            			"                         FROM TC_SC_PANEL " +
            			"                        WHERE tcscbodegavirtualid IN ("+input.getIdBodegaZona()+")) " ));
            	
            	condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_UPPER_AND, Solicitud.CAMPO_TIPO, "'RUTA','PANEL'"));
            }
        }

        return condiciones;
    }

    /**
     * Funci\u00F3n para las condiciones al momento de modificar una solicitud.
     * 
     * @param conn
     * @param input
     * @return
     */
    public static List<Filtro> obtenerCondicionesExistencia( InputSolicitud input, BigDecimal ID_PAIS) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_TCSCSOLICITUDID, input.getIdSolicitud()));

        return condiciones;
    }
    // ========================= M\u00E9todos para obtener condiciones =====================//

    // ========================= M\u00E9todo principal de los servicios de solicitudes =====//
    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     *            Objeto con los datos enviados mediante POST.
     * @param metodo
     * @return output Respuesta y listado con los Solicituds encontrados.
     * @throws SQLException
     */
    public OutputSolicitud getDatos(InputSolicitud input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = new Respuesta();
        RespuestaSolicitud respuestaSolicitud = new RespuestaSolicitud();
        OutputSolicitud output = new OutputSolicitud();
        String nombreTransaccion = "";

        log.trace("Usuario: " + input.getUsuario());
        Connection conn = null;

        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());

            // se obtienen todas las configuraciones
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
            String estadoBaja = "";
            String estadoAbierta = "";
            String estadoCancelada = "";
            String estadoFalse = "";
            String estadoTrue = "";
            String estadoDisponible = "";
            String estadoDevolucion = "";
            String estadoPendiente = "";
            String estadoEnviado = "";
            String estadoActivo = "";
            String tipoDevolucion = "";
            String tipoReserva = "";
            String tipoPedido = "";
            String tipoNumPayment = "";
            String tipoSiniestro = "";
            String tipoDeuda = "";
            String origenPC = "";
            String origenMovil = "";
            String tipoInvSidra = "";
            String nombreBuzonPayment = "";
            String nombreNumPayment = "";
            String longTelefono = "";
            String tipoPDV = "";
            String tipoRuta = "";
            String tipoPanel = "";
            String estadoActivadoPayment = "";
            String estadoDesactivadoPayment = "";
            String estadoRechazadoPayment = "";
            String tipoSiniestroTotal = "";
            String tipoSiniestroParcial = "";
            String tipoSiniestroDispositivo = "";
            String estadoIniciada = "";
            String estadoSiniestro = "";
            String estadoAceptada = "";
            String estadoRechazada = "";
            String estadoFinalizada = "";
            String estadoRechazadaTelca = "";
            String tipoTodasSolicitudes = "";
            boolean banderaSolicitudTelca = false;
            boolean banderaSolicitudDTS = false;

            log.debug("Validando datos...");

            String campos[] = { Catalogo.CAMPO_NOMBRE, Catalogo.CAMPO_VALOR };

            // Se obtienen todas las configuraciones.
            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, ID_PAIS.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_SOLICITUDES_TIPO));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_SOLICITUDES_ORIGEN));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_SOLICITUDES_TIPOINV));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ESTADOS_SOLICITUD));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ESTADOS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_VALIDACIONES_DINAMICAS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ESTADOS_ESTADOS_PDV));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_PANELPDV));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_PANELRUTA));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_CONFIG_SIDRA));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ESTADOS_PAYMENT));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_TIPOS_SINIESTROS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.NOMBRE_BUZON));

            List<Map<String, String>> datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos,
                    condiciones, null);

            for (int i = 0; i < datosConfig.size(); i++) {
                // configuraciones de estados
                if (Conf.ESTADO_ALTA_BOOL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoTrue = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.ESTADO_BAJA_BOOL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoFalse = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.ESTADO_BAJA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoBaja = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }

                // configuraciones de estados de solicitud
                if (Conf.SOL_ESTADO_ABIERTA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoAbierta = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }

                if (Conf.SOL_ESTADO_PENDIENTE.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoPendiente = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }

                if (Conf.SOL_ESTADO_CANCELADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoCancelada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.SOL_ESTADO_ACEPTADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoAceptada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.SOL_ESTADO_RECHAZADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoRechazada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.SOL_ESTADO_FINALIZADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoFinalizada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.SOL_ESTADO_RECHAZADA_TELCA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoRechazadaTelca = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.SOL_ESTADO_ENVIADO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoEnviado = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }

                // configuraciones de tipos de solicitud
                if (Conf.SOL_TIPO_DEVOLUCION.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoDevolucion = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.SOL_TIPO_RESERVA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoReserva = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.SOL_TIPO_PEDIDO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoPedido = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.SOL_TIPO_NUMEROS_PAYMENT.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoNumPayment = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.SOL_TIPO_SINIESTRO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoSiniestro = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.SOL_TIPO_DEUDA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoDeuda = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.SOL_TIPO_TODAS.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoTodasSolicitudes = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }

                // configuraciones de origen
                if (Conf.SOL_ORIGEN_PC.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    origenPC = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.SOL_ORIGEN_MOVIL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    origenMovil = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }

                // configuraciones de tipo de inventario
                if (Conf.SOL_TIPOINV_SIDRA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoInvSidra = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }

                // configuraciones de sidra
                if (Conf.NOMBRE_NUM_PAYMENT.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    nombreNumPayment = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.LONGITUD_TELEFONO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    longTelefono = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }

                // configuraciones de buzon
                if (Conf.BUZON_PAYMENT.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    nombreBuzonPayment = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }

       
                // configuraciones de estados de pdv
                if (Conf.ESTADO_ACTIVO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoActivo = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }

                // configuracion de tipo pdv para solicitudes tipo payment
                if (Conf.TIPO_PDV.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoPDV = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }

                // configuracion de tipo panel o ruta para solicitudes tipo siniestro
                if (Conf.TIPO_RUTA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoRuta = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_PANEL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoPanel = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }

                // estados payment para los numeros de recarga
                if (Conf.PAYMENT_ESTADO_ACTIVADO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoActivadoPayment = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.PAYMENT_ESTADO_DESACTIVADO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoDesactivadoPayment = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.PAYMENT_ESTADO_RECHAZADO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoRechazadoPayment = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }

                // tipos de siniestros para solicitudes tipo siniestro
                if (Conf.SINIESTRO_PARCIAL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoSiniestroParcial = datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE);
                }
                if (Conf.SINIESTRO_TOTAL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoSiniestroTotal = datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE);
                }
                if (Conf.SINIESTRO_DISPOSITIVO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoSiniestroDispositivo = datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE);
                }
            }

            // configuraciones de estados de inventario
            estadoDisponible = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_DISPONIBLE, input.getCodArea());
            estadoDevolucion = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_DEVOLUCION, input.getCodArea());
            estadoSiniestro = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_SINIESTRO, input.getCodArea());
            estadoIniciada = UtileriasJava.getConfig(conn, Conf.GRUPO_JORNADA_ESTADOS, Conf.JORNADA_ESTADO_INICIADA, input.getCodArea());

       
            // se obtiene el id del buzon de payment
            condiciones.clear();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BuzonSidra.CAMPO_TCSCCATPAIS_ID, ID_PAIS.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BuzonSidra.CAMPO_NOMBRE, nombreBuzonPayment));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BuzonSidra.CAMPO_ESTADO, estadoAlta));
            String idBuzonPayment = UtileriasBD.getOneRecord(conn, BuzonSidra.CAMPO_TCSCBUZONID, BuzonSidra.N_TABLA, condiciones);
            UtileriasJava.validarParametroVacio(idBuzonPayment);

            // Validaci\u00F3n de datos en el input
            if (metodo == Conf.METODO_GET) {
                nombreServicio = Conf.LOG_GET_SOLICITUDES;
                nombreTransaccion = Conf.LOG_TRANSACCION_CREA_SOLICITUD;
                respuesta = validarInputGet(conn, input, origenPC, origenMovil);

            } else if (metodo == Conf.METODO_PUT) {
                nombreServicio = Conf.LOG_PUT_SOLICITUDES;
                nombreTransaccion = Conf.LOG_TRANSACCION_CREA_SOLICITUD;
                respuesta = validarInputUpd(input, estadoCancelada, estadoAceptada, estadoRechazada, estadoFinalizada,
                        estadoRechazadaTelca, estadoEnviado);

            } else {
                nombreServicio = Conf.LOG_POST_SOLICITUDES;
                nombreTransaccion = Conf.LOG_TRANSACCION_CREA_SOLICITUD;
                respuestaSolicitud = validarInputPost(conn, input, metodo, estadoAlta, estadoFalse, estadoTrue,
                        estadoDisponible, estadoDevolucion, tipoDevolucion, tipoReserva, tipoPedido,
                        tipoNumPayment, tipoSiniestro, tipoTodasSolicitudes, origenPC, origenMovil, tipoInvSidra,
                        estadoActivo, tipoPDV, tipoPanel, tipoRuta, idBuzonPayment, longTelefono, estadoPendiente,
                        estadoActivadoPayment, tipoSiniestroParcial, tipoSiniestroTotal, tipoSiniestroDispositivo,
                        tipoDeuda, estadoCancelada, ID_PAIS);

                respuesta = respuestaSolicitud.getRespuesta();
                banderaSolicitudTelca = respuestaSolicitud.isResultado();
                banderaSolicitudDTS = respuestaSolicitud.isResultadoDTS();
            }

            log.trace("Respuesta validaci\u00F3n: " + respuesta.getDescripcion());
            if (!respuesta.getDescripcion().equals("OK")) {
                output = new OutputSolicitud();
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, nombreServicio, "0",
                        Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(), ""));

                return output;
            }

            if (metodo == Conf.METODO_GET) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                output = OperacionSolicitud.doGet(conn, input, metodo, tipoPanel, tipoRuta, tipoPDV, tipoDeuda, estadoAlta, ID_PAIS);

            } else if (metodo == Conf.METODO_POST) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
                output = OperacionSolicitud.doPost(conn, input, estadoAlta, estadoTrue, estadoAbierta, estadoDisponible,
                        estadoDevolucion, estadoPendiente, estadoAceptada, origenPC,  tipoDevolucion,
                        tipoReserva, tipoPedido, tipoNumPayment, tipoSiniestro, tipoInvSidra, nombreNumPayment, tipoPDV,
                        tipoPanel, tipoSiniestroTotal, tipoSiniestroParcial, estadoSiniestro, estadoIniciada,
                        tipoSiniestroDispositivo, banderaSolicitudTelca, tipoDeuda, estadoEnviado, banderaSolicitudDTS, tipoRuta, tipoGrupo, ID_PAIS);
                        /* ,estadoCerradaSiniestro, estadoAnuladoSiniestro */

            } else if (metodo == Conf.METODO_PUT) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT
                output = OperacionSolicitud.doPut(conn, input,  estadoAbierta,
                        /* estadoCerrada, */ tipoDevolucion, tipoSiniestro, tipoNumPayment, estadoActivadoPayment,
                        estadoRechazadoPayment,  estadoCancelada, estadoDisponible, estadoAlta,
                        estadoTrue, estadoDevolucion, estadoSiniestro, tipoInvSidra, tipoPanel, origenMovil, origenPC,
                        tipoPedido, tipoReserva, estadoAceptada, estadoRechazada, estadoFinalizada,
                        estadoRechazadaTelca, tipoTodasSolicitudes, tipoDeuda, estadoEnviado, estadoPendiente, tipoRuta,
                        estadoBaja,tipoGrupo, ID_PAIS);
            }
        } catch (SQLException e) {
            respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputSolicitud();
            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(nombreTransaccion, nombreServicio, "0", Conf.LOG_TIPO_NINGUNO,
                    output.getRespuesta().getDescripcion(), output.getRespuesta().getExcepcion()));

        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputSolicitud();
            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, nombreServicio, "0",
                    Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(),
                    output.getRespuesta().getExcepcion()));

        } finally {
            DbUtils.closeQuietly(conn);
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
    // ========================= M\u00E9todo principal de los servicios de solicitudes =====//
    
    // ========================= M\u00E9todo para notificar a distribuidores ===============//
    public static void notificarDTS(Connection conn, String idDTS, String estadoSolicitud, String usuario,
            String idSolicitud, String codArea, BigDecimal ID_PAIS) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        DateFormat formato = DateFormat.getDateInstance(DateFormat.FULL);
        String fechaCorreo = (formato.format(new Date()));
        String asunto = "";
        String cuerpo = "";
        String sender = "";
        String host = "";
        String port = "";
        List<Filtro> condiciones = new ArrayList<Filtro>();

        try {
            // Se obtienen todas las configuraciones.
            String[] campos = { Catalogo.CAMPO_NOMBRE, Catalogo.CAMPO_VALOR };

            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, ID_PAIS.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_PLANTILLA_NOTIFICACION_DTS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_JORNADA_CONFIG_CORREO));
            List<Map<String, String>> datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, null);

            for (int i = 0; i < datosConfig.size(); i++) {
                if (Conf.CONFIG_CORREO_ASUNTO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    asunto = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.CONFIG_CORREO_CUERPO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    cuerpo = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
            
                // se obtienen los parametros del correo
                if (Conf.CONFIG_CORREO_HOST.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    host = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.CONFIG_CORREO_PORT.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    port = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.CONFIG_CORREO_SENDER.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    sender = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
            }

            // se obtienen los datos del dts
            condiciones.clear();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TC_SC_DTS_ID, idDTS));
    
            String[] camposDTS = {
                Distribuidor.CAMPO_NOMBRES,
                Distribuidor.CAMPO_EMAIL
            };
            Map<String, String> datosDTS = UtileriasBD.getSingleFirstData(conn, Distribuidor.N_TABLA, camposDTS, condiciones);

            // se agregan los datos generales a la plantilla de correo
            cuerpo = cuerpo.replace("@@fecha", fechaCorreo);
            cuerpo = cuerpo.replace("@@destinatario", datosDTS.get(Distribuidor.CAMPO_NOMBRES));
            cuerpo = cuerpo.replace("@@estado", estadoSolicitud);

            String resp = "";
            String[] lstCorreos = datosDTS.get(Distribuidor.CAMPO_EMAIL).split(";");

            List<String> listCorreosError = new ArrayList<String>();

            for (String correo : lstCorreos) {
                resp = SendMail.sendMail(sender, host, port, correo.trim(), asunto, cuerpo);

                if (resp.equals("OK")) {
                    // se envio
                    log.trace("envia correo ok");

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_OPERACION,
                            "WS SOLICITUDES - NOTIFICACION", idSolicitud, Conf.LOG_TIPO_SOLICITUD,
                            "Se envi\u00F3 correo de notificaci\u00F3n de la solicitud.", resp));
                } else {
                    // fallo al enviar
                    log.trace("fallo al enviar correo");
                    listCorreosError.add(correo);
                    // TODO listado de correos con error de envio, alguna opcion de reenvio?

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_OPERACION,
                            "WS SOLICITUDES - NOTIFICACION", idSolicitud, Conf.LOG_TIPO_SOLICITUD,
                            "Problema al enviar correo de notificaci\u00F3n.", resp));
                }
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_JORNADA, "WS JORNADA - VERIFICA ALARMA", "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al verificar alarmas.", e.getMessage()));

        } finally {
            UtileriasJava.doInsertLog(listaLog, usuario, codArea);
        }
    }
}
