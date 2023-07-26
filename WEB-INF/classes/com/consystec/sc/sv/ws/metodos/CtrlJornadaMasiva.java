package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.jornada.InputJornada;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.vendedordts.InputVendedorDTS;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.jornada.OutputJornada;
import com.consystec.sc.sv.ws.operaciones.OperacionJornadaMasiva;
import com.consystec.sc.sv.ws.orm.BodegaVendedor;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.Panel;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.orm.Venta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 */
public class CtrlJornadaMasiva extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlJornadaMasiva.class);
    private static String servicioGet = Conf.LOG_GET_JORNADA;
    private static String servicioPost = Conf.LOG_POST_JORNADA;
    private static String servicioPut = Conf.LOG_PUT_JORNADA;

    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDe como recurso al
     * servicio.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @param tipoPanel
     * @param tipoRuta
     * @param tipoFinJornada
     * @param diferencia
     * @return
     * @throws SQLException 
     */
    public Respuesta validarInput(Connection conn, InputJornada input, int metodo, String tipoPanel, String tipoRuta,
            String tipoFinJornada, String diferencia) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        SimpleDateFormat FORMATO_TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss");
        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;

        log.debug("Validando datos...");

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (input.getIdDistribuidor() == null ||  "".equals(input.getIdDistribuidor().trim())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_161, null, nombreClase, nombreMetodo, null, input.getCodArea())
                    .getDescripcion();
            flag = true;
        } else if (!isNumeric(input.getIdDistribuidor())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (metodo == Conf.METODO_GET) {
            // Filtro rango de fechas de inicios de jornada
            if (input.getFechaDesde() != null && ! "".equals(input.getFechaDesde().trim())) {
                if (input.getFechaHasta() != null && ! "".equals(input.getFechaHasta().trim())) {
                    SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
                    Date fechaInicio = UtileriasJava.parseDate(input.getFechaDesde(), formatoFecha);
                    Date fechaFin = UtileriasJava.parseDate(input.getFechaHasta(), formatoFecha);

                    if ((fechaInicio != null && fechaFin != null) && fechaInicio.compareTo(fechaFin) > 0) {
                            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_DESDE_MENOR_232, null,
                                    nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                            flag = true;
                    }
                } else {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, nombreClase, nombreMetodo,
                            null, input.getCodArea()).getDescripcion();
                    flag = true;
                }
            }

            // Filtro rango de fechas de jornadas finalizadas
            if (input.getFechaFinDesde() != null && ! "".equals(input.getFechaFinDesde().trim())) {
                if (input.getFechaFinHasta() != null && ! "".equals(input.getFechaFinHasta().trim())) {
                    SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
                    Date fechaInicio = UtileriasJava.parseDate(input.getFechaFinDesde(), formatoFecha);
                    Date fechaFin = UtileriasJava.parseDate(input.getFechaFinHasta(), formatoFecha);

                    if ((fechaInicio != null && fechaFin != null) && fechaInicio.compareTo(fechaFin) > 0) {
                            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHAFINDESDE_MENOR_388, null,
                                    nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                            flag = true;
                    }
                } else {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, nombreClase, nombreMetodo,
                            "en Fin de Jornada", input.getCodArea()).getDescripcion();
                    flag = true;
                }
            }

            // Filtro rango de fechas de jornadas finalizadas
            if (input.getFechaLiqDesde() != null && ! "".equals(input.getFechaLiqDesde().trim())) {
                if (input.getFechaLiqHasta() != null && ! "".equals(input.getFechaLiqHasta().trim())) {
                    SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
                    Date fechaInicio = UtileriasJava.parseDate(input.getFechaLiqDesde(), formatoFecha);
                    Date fechaFin = UtileriasJava.parseDate(input.getFechaLiqHasta(), formatoFecha);

                    if ((fechaInicio != null && fechaFin != null) && fechaInicio.compareTo(fechaFin) > 0) {
                            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHALIQDESDE_MENOR_389, null,
                                    nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                            flag = true;
                    }
                } else {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, nombreClase, nombreMetodo,
                            "en Liquidacion", input.getCodArea()).getDescripcion();
                    flag = true;
                }
            }

            if ((input.getIdJornada() != null && ! "".equals(input.getIdJornada().trim())) && !isNumeric(input.getIdJornada())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDJORNADA_259, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }

            if ((input.getIdJornadaResponsable() != null && ! "".equals(input.getIdJornadaResponsable().trim())) && !isNumeric(input.getIdJornadaResponsable())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDJORNADARESP_740, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    
                    flag = true;
            }

            if ((input.getIdVendedor() != null && ! "".equals(input.getIdVendedor().trim()))&& !isNumeric(input.getIdVendedor())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }

            if (input.getIdTipo() != null && ! "".equals(input.getIdTipo().trim())) {
                if (!isNumeric(input.getIdTipo())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDTIPO_NUM_167, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                } else {
                    if (input.getTipo() == null ||  "".equals(input.getTipo())) {
                        return getMensaje(Conf_Mensajes.MSJ_TIPO_NULO_431, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    }
                }
            }

            if (input.getTipo() != null && ! "".equals(input.getTipo().trim())) {
                String tipoPanelRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA,
                        input.getTipo().toUpperCase(), input.getCodArea());

                if (tipoPanelRuta == null ||  "".equals(tipoPanelRuta)) {
                    return getMensaje(Conf_Mensajes.MSJ_ERROR_TIPOOFERTA_NO_DEFINIDO_424, null, nombreClase,
                            nombreMetodo, null, input.getCodArea());
                }
            }

            if ((input.getIdBodegaVirtual() != null && ! "".equals(input.getIdBodegaVirtual().trim()))&& !isNumeric(input.getIdBodegaVirtual())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODVIRTUAL_NUM_171, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }
            
            if ((input.getEnvioAlarma() != null && ! "".equals(input.getEnvioAlarma().trim())) && !isNumeric(input.getEnvioAlarma())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ENVIOALARMA_774, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }

            if ((input.getEnvioAlarmaFin() != null && ! "".equals(input.getEnvioAlarmaFin().trim()))&& !isNumeric(input.getEnvioAlarmaFin())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ENVIOALARMA_FIN_775, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }

        } else {
            if (input.getIdResponsable() == null ||  "".equals(input.getIdResponsable().trim())
                    || !isNumeric(input.getIdResponsable())) {
                datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_INPUT_RESPONSABLE_NUM_717, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }

            if (input.getCodDispositivo() == null ||  "".equals(input.getCodDispositivo().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_CODDISPOSITIVO_163, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }

            if (metodo == Conf.METODO_PUT) {
                // validaciones para modificar
                if (input.getIdJornada() == null ||  "".equals(input.getIdJornada().trim())
                        || !isNumeric(input.getIdJornada())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDJORNADA_259, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                }

            } else if (metodo == Conf.METODO_POST) {
                try {
                    if (input.getFecha() == null ||  "".equals(input.getFecha().trim())) {
                        datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_314, null, nombreClase,
                                nombreMetodo, null, input.getCodArea()).getDescripcion();
                        flag = true;
                    } else {
                        Calendar cal = Calendar.getInstance();

                        Date fechaJornada = FORMATO_TIMESTAMP.parse(input.getFecha());
                        log.trace("Hora jornada: " + fechaJornada);
                        Date fechaActual = FORMATO_TIMESTAMP.parse(FORMATO_TIMESTAMP.format(new Date()));
                        cal.setTime(fechaActual); // Establece fecha y hora actuales
                        cal.add(Calendar.MINUTE, new Integer(diferencia)); // Suma/resta la diferencia de horario
                        fechaActual = FORMATO_TIMESTAMP.parse(FORMATO_TIMESTAMP.format(cal.getTime()));
                        log.trace("Hora actual con diferencia de horario: " + fechaActual);

                        cal.setTime(fechaActual); // Establece fecha y hora actuales
                        cal.add(Calendar.HOUR_OF_DAY, -1); // Resta una hora por tiempo de holgura
                        Date fechaMinima = FORMATO_TIMESTAMP.parse(FORMATO_TIMESTAMP.format(cal.getTime()));
                        cal.setTime(fechaActual); // Establece fecha y hora actuales
                        cal.add(Calendar.MINUTE, 10); // Suma 10 minutos para el tiempo de holgura
                        Date fechaMaxima = FORMATO_TIMESTAMP.parse(FORMATO_TIMESTAMP.format(cal.getTime()));

                        if (fechaJornada.before(fechaMinima) || fechaJornada.after(fechaMaxima)) {
                            datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_ERROR_HORA_JORNADA_196, null,
                                    nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                            flag = true;
                        }
                    }
                } catch (java.text.ParseException e) {
                    log.error("Problema al convertir la fecha en clase " + nombreMetodo + " m\u00E9todo " + nombreClase + ".", e);

                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_CONVERTIR_FECHA_165, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                }

                if (input.getTipo() == null ||  "".equals(input.getTipo().trim())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_NULO_150, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                } else if (!input.getTipo().equalsIgnoreCase(tipoPanel)
                        && !input.getTipo().equalsIgnoreCase(tipoRuta)) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_136, null, nombreClase, nombreMetodo,
                            tipoPanel + " o " + tipoRuta + ".", input.getCodArea()).getDescripcion();
                    flag = true;
                }

                if (input.getIdBodegaVendedor() == null ||  "".equals(input.getIdBodegaVendedor().trim())
                        || !isNumeric(input.getIdBodegaVendedor())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_IDBOD_VENDEDOR_NULO_425, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                }

                // validacion de vendedores cuando es tipo panel
                if (!flag && input.getVendedores() != null && !input.getVendedores().isEmpty()) {
                    for (int i = 0; i < input.getVendedores().size(); i++) {
                        if (input.getVendedores().get(i).getIdVendedor() == null
                                ||  "".equals(input.getVendedores().get(i).getIdVendedor().trim())) {
                            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_VENDEDOR_VACIO_720, null, nombreClase,
                                    nombreMetodo, (i + 1) + "", input.getCodArea()).getDescripcion();
                            flag = true;
                            break;

                        } else if (!isNumeric(input.getVendedores().get(i).getIdVendedor())) {
                            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_VENDEDOR_NUM_726, null, nombreClase,
                                    nombreMetodo, (i + 1) + "", input.getCodArea()).getDescripcion();
                            flag = true;
                            break;

                        } else if (input.getVendedores().get(i).getIdVendedor().equals(input.getIdResponsable())) {
                            return getMensaje(Conf_Mensajes.MSJ_ERROR_RESPONSABLE_LIST_758, null, nombreClase,
                                    nombreMetodo, null, input.getCodArea());
                        }
                    }

                    int numeroVend = 1;
                    for (InputVendedorDTS vendActual : input.getVendedores()) {
                        if (flag)
                            break;
                        int indexVend = 1;

                        for (InputVendedorDTS detalle : input.getVendedores()) {
                            if (indexVend != numeroVend
                                    && detalle.getIdVendedor().trim().equals(vendActual.getIdVendedor().trim())) {
                                datosErroneos += " "
                                        + getMensaje(Conf_Mensajes.MSJ_INPUT_VENDEDORES_IGUALES_729, null, nombreClase,
                                                nombreMetodo, numeroVend + " y " + indexVend + ".", input.getCodArea()).getDescripcion();
                                flag = true;
                                break;
                            }

                            indexVend++;
                        }

                        numeroVend++;
                    }
                }
            }
        }

        if (flag) {
            r = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo, datosErroneos, input.getCodArea());
        } else {
            r.setDescripcion("OK");
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
            Jornada.CAMPO_TCSCJORNADAVENID,
            Jornada.CAMPO_TCSCCATPAISID,
            Jornada.CAMPO_FECHA,
            Jornada.CAMPO_IDTIPO,
            Jornada.CAMPO_DESCRIPCION_TIPO,
            Jornada.CAMPO_TCSCDTSID,
            Jornada.CAMPO_TCSCBODEGAVIRTUAL,
            Jornada.CAMPO_COD_DISPOSITIVO,
            Jornada.CAMPO_VENDEDOR,
            Jornada.CAMPO_OBSERVACIONES,
            Jornada.CAMPO_JORNADA_RESPONSABLE,
            Jornada.CAMPO_ESTADO,
            Jornada.CAMPO_ENVIO_ALARMA,
            Jornada.CAMPO_ENVIO_ALARMA_FIN,
            Jornada.CAMPO_CREADO_EL,
            Jornada.CAMPO_CREADO_POR
        };
        
        return campos;
    }
    
    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en el m\u00E9todo GET.
     * @param conn 
     * @param input 
     * 
     * @return
     * @throws SQLException 
     */
    public static String[][] obtenerCamposGet(Connection conn, String estadosAnulados, String codArea, BigDecimal idPais) throws SQLException {
        String tipoRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_RUTA, codArea);
        String nombreTipo = "CASE";

        nombreTipo += " WHEN " + Jornada.N_TABLA + "." + Jornada.CAMPO_DESCRIPCION_TIPO + " = '" + tipoRuta + "' THEN "
                + "(SELECT A." + Ruta.CAMPO_NOMBRE + " FROM " + Ruta.N_TABLA + " A WHERE A."
                + Ruta.CAMPO_TC_SC_RUTA_ID + " = " + Jornada.N_TABLA + "." + Jornada.CAMPO_IDTIPO
                + " AND A.TCSCCATPAISID = " + idPais +") ";

        nombreTipo += " WHEN " + Jornada.N_TABLA + "." + Jornada.CAMPO_DESCRIPCION_TIPO + " != '" + tipoRuta + "' THEN "
                + "(SELECT A." + Panel.CAMPO_NOMBRE + " FROM " + Panel.N_TABLA + " A WHERE A."
                + Panel.CAMPO_TCSCPANELID + " = " + Jornada.N_TABLA + "." + Jornada.CAMPO_IDTIPO
                + " AND A.TCSCCATPAISID = " + idPais +") ";

        nombreTipo += " END AS NOMBRETIPO";

        String campos[][] = {
            { Jornada.N_TABLA, Jornada.CAMPO_TCSCJORNADAVENID },
            { null, "NVL(" + Jornada.CAMPO_JORNADA_RESPONSABLE + ", " + Jornada.CAMPO_TCSCJORNADAVENID + ") AS "
                    + Jornada.CAMPO_JORNADA_RESPONSABLE },
            { Jornada.N_TABLA, Jornada.CAMPO_TCSCDTSID },
            { Distribuidor.N_TABLA, Distribuidor.CAMPO_NOMBRES + " AS NOMBREDTS" },
            { Jornada.N_TABLA, Jornada.CAMPO_VENDEDOR },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_NOMBRE + " || ' ' || "
                    + VendedorDTS.N_TABLA + "." + VendedorDTS.CAMPO_APELLIDO + " AS NOMBREVENDEDOR" },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_USUARIO + " AS USUARIOVENDEDOR" },
            { Jornada.N_TABLA, Jornada.CAMPO_COD_DISPOSITIVO },
            { Jornada.N_TABLA, Jornada.CAMPO_FECHA },
            { Jornada.N_TABLA, Jornada.CAMPO_IDTIPO },
            { Jornada.N_TABLA, Jornada.CAMPO_DESCRIPCION_TIPO },
            { null, nombreTipo },
            { Jornada.N_TABLA, Jornada.CAMPO_TCSCBODEGAVIRTUAL },
            { BodegaVirtual.N_TABLA, BodegaVirtual.CAMPO_NOMBRE + " AS NOMBREBODEGA" },
            { Jornada.N_TABLA, Jornada.CAMPO_OBSERVACIONES },
            { Jornada.N_TABLA, Jornada.CAMPO_ESTADO },
            { Jornada.N_TABLA, Jornada.CAMPO_FECHA_FINALIZACION },
            { Jornada.N_TABLA, Jornada.CAMPO_ESTADO_LIQUIDACION },
            { Jornada.N_TABLA, Jornada.CAMPO_FECHA_LIQUIDACION },
            { null, "(SELECT NVL(SUM(V.MONTO_FACTURA*V.TASA_CAMBIO),0) FROM " + getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea)
                    + " V WHERE V.TCSCCATPAISID = " + idPais + " AND V.TCSCJORNADAVENID = TC_SC_JORNADA_VEND.TCSCJORNADAVENID"
                    + " AND ESTADO NOT IN (" + estadosAnulados + ")) VENTAS" },
            { Jornada.N_TABLA, Jornada.CAMPO_COD_DISPOSITIVO_FIN },
            { Jornada.N_TABLA, Jornada.CAMPO_ENVIO_ALARMA },
            { Jornada.N_TABLA, Jornada.CAMPO_TIPO_ALARMA },
            { Jornada.N_TABLA, Jornada.CAMPO_ENVIO_ALARMA_FIN },
            { Jornada.N_TABLA, Jornada.CAMPO_TIPO_ALARMA_FIN },
            { Jornada.N_TABLA, Jornada.CAMPO_SALDO_PAYMENT },
            { Jornada.N_TABLA, Jornada.CAMPO_SOLICITUD_PAGO },
            { Jornada.N_TABLA, Jornada.CAMPO_ESTADO_PAGO },
            { Jornada.N_TABLA, Jornada.CAMPO_CREADO_EL },
            { Jornada.N_TABLA, Jornada.CAMPO_CREADO_POR },
            { Jornada.N_TABLA, Jornada.CAMPO_MODIFICADO_EL },
            { Jornada.N_TABLA, Jornada.CAMPO_MODIFICADO_POR },
            { null, "(SELECT " + BodegaVendedor.CAMPO_TCSCBODEGAVIRTUALID + " FROM " + BodegaVendedor.N_TABLA + " B"
                    + " WHERE B.TCSCCATPAISID = " + idPais + " AND B." + BodegaVendedor.CAMPO_VENDEDOR + " = "
                    + VendedorDTS.N_TABLA + "." + VendedorDTS.CAMPO_VENDEDOR + ") AS BODEGAVENDEDOR" },
            {null," CASE " +
            		"          WHEN upper(TC_SC_JORNADA_VEND.DESCRIPCION_TIPO) = 'RUTA' " +
            		"          THEN " +
            		"             (SELECT NOMBRE " +
            		"                FROM TC_SC_BODEGAVIRTUAL " +
            		"               WHERE TCSCBODEGAVIRTUALID = (SELECT TCSCBODEGAVIRTUALID " +
            		"                                              FROM tc_Sc_ruta " +
            		"                                             WHERE tcscrutaid = TC_SC_JORNADA_VEND.Idtipo)) " +
            		"       WHEN upper(TC_SC_JORNADA_VEND.DESCRIPCION_TIPO) = 'PANEL' " +
            		"          THEN " +
            		"             (SELECT NOMBRE " +
            		"                FROM TC_SC_BODEGAVIRTUAL " +
            		"               WHERE TCSCBODEGAVIRTUALID = (SELECT TCSCBODEGAVIRTUALID " +
            		"                                              FROM tc_Sc_PANEL " +
            		"                                             WHERE tcscpanelid = TC_SC_JORNADA_VEND.Idtipo))                                                   " +
            		"       END nombre_bodega_panelruta " 
            },
            {null, "CASE " +
            		"          WHEN upper(TC_SC_JORNADA_VEND.DESCRIPCION_TIPO) = 'RUTA' " +
            		"          THEN " +
            		"             (SELECT TCSCBODEGAVIRTUALID " +
            		"                FROM TC_SC_BODEGAVIRTUAL " +
            		"               WHERE TCSCBODEGAVIRTUALID = (SELECT TCSCBODEGAVIRTUALID " +
            		"                                              FROM tc_Sc_ruta " +
            		"                                             WHERE tcscrutaid = TC_SC_JORNADA_VEND.Idtipo)) " +
            		"       WHEN upper(TC_SC_JORNADA_VEND.DESCRIPCION_TIPO) = 'PANEL' " +
            		"          THEN " +
            		"             (SELECT TCSCBODEGAVIRTUALID " +
            		"                FROM TC_SC_BODEGAVIRTUAL " +
            		"               WHERE TCSCBODEGAVIRTUALID = (SELECT TCSCBODEGAVIRTUALID " +
            		"                                              FROM tc_Sc_PANEL " +
            		"                                             WHERE tcscpanelid = TC_SC_JORNADA_VEND.Idtipo))                                                   " +
            		"       END id_bodega_panelruta "  }
        };

        return campos;
    }
    
    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
     * 
     * @param input
     * @param sequencia
     * @param idTipo
     * @param idResponsable
     * @param idVendedor
     * @param estado
     * @return
     */
    public static List<String> obtenerInsertsPost(InputJornada input, String sequencia, String idTipo,
            String idResponsable, String idVendedor, String estado, BigDecimal idPais) {
        List<String> inserts = new ArrayList<String>();

        String responsable = idResponsable != null
                ? UtileriasJava.setInsert(Conf.INSERT_NUMERO, idResponsable, Conf.INSERT_SEPARADOR_SI) : "NULL, ";

        String valores = "("
            + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPais.toString(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsertFecha(Conf.INSERT_TIMESTAMP, input.getFecha(), Conf.TXT_FORMATO_TIMESTAMP, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idTipo, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getTipo(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdDistribuidor(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdBodegaVendedor(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getCodDispositivo(), Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idVendedor, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getObservaciones(), Conf.INSERT_SEPARADOR_SI)
            + responsable
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estado, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, "0", Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_NUMERO, "0", Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
            + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
        + ") ";
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
    public static List<Filtro> obtenerCondiciones(InputJornada input, int metodo, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Jornada.N_TABLA,
                Jornada.CAMPO_TCSCCATPAISID, idPais.toString()));

        if (metodo == Conf.METODO_GET) {
            if (input.getIdDistribuidor() != null && ! "".equals(input.getIdDistribuidor().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Jornada.N_TABLA,
                        Jornada.CAMPO_TCSCDTSID, input.getIdDistribuidor()));
            }

            if (input.getIdJornada() != null && ! "".equals(input.getIdJornada().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Jornada.N_TABLA,
                        Jornada.CAMPO_TCSCJORNADAVENID, input.getIdJornada()));
            }

            if (input.getIdJornadaResponsable() != null && ! "".equals(input.getIdJornadaResponsable().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_OR, Jornada.N_TABLA,
                        Jornada.CAMPO_JORNADA_RESPONSABLE, input.getIdJornadaResponsable()));
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_OR, Jornada.N_TABLA,
                        Jornada.CAMPO_TCSCJORNADAVENID, input.getIdJornadaResponsable()));
            }

            if (input.getIdVendedor() != null && ! "".equals(input.getIdVendedor().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Jornada.N_TABLA,
                        Jornada.CAMPO_VENDEDOR, input.getIdVendedor()));
            }

            if (input.getDispositivoJornada() != null && ! "".equals(input.getDispositivoJornada().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Jornada.N_TABLA,
                        Jornada.CAMPO_COD_DISPOSITIVO, input.getDispositivoJornada()));
            }

            if ((input.getFechaDesde() != null && input.getFechaHasta() != null) &&  (! "".equals(input.getFechaDesde().trim()) && ! "".equals(input.getFechaHasta().trim()))) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_RANGO_FECHAS,
                            Jornada.N_TABLA + "." + Jornada.CAMPO_FECHA, input.getFechaDesde(), input.getFechaHasta(),
                            Conf.TXT_FORMATO_FECHA_CORTA));
            }

            if ((input.getFechaFinDesde() != null && input.getFechaFinHasta() != null) &&  (! "".equals(input.getFechaFinDesde().trim()) && ! "".equals(input.getFechaFinHasta().trim()))) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_RANGO_FECHAS,
                            Jornada.N_TABLA + "." + Jornada.CAMPO_FECHA_FINALIZACION, input.getFechaFinDesde(),
                            input.getFechaFinHasta(), Conf.TXT_FORMATO_FECHA_CORTA));
            }

            if ((input.getFechaLiqDesde() != null && input.getFechaLiqHasta() != null) &&  (! "".equals(input.getFechaLiqDesde().trim()) && ! "".equals(input.getFechaLiqHasta().trim()))) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_RANGO_FECHAS,
                            Jornada.N_TABLA + "." + Jornada.CAMPO_FECHA_LIQUIDACION, input.getFechaLiqDesde(),
                            input.getFechaLiqHasta(), Conf.TXT_FORMATO_FECHA_CORTA));
            }

            if (input.getIdTipo() != null && ! "".equals(input.getIdTipo().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Jornada.N_TABLA,
                        Jornada.CAMPO_IDTIPO, input.getIdTipo()));
            }

            if (input.getTipo() != null && ! "".equals(input.getTipo().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Jornada.N_TABLA,
                        Jornada.CAMPO_DESCRIPCION_TIPO, input.getTipo()));
            }

            if (input.getIdBodegaVirtual() != null && ! "".equals(input.getIdBodegaVirtual().trim())) {
                String sqlFiltroBodega = "IN (WITH RUTAPANEL AS (SELECT TCSCRUTAID IDTIPO, 'RUTA' TIPO FROM TC_SC_RUTA WHERE TCSCBODEGAVIRTUALID = " + input.getIdBodegaVirtual() + " AND TCSCCATPAISID = " + idPais
                        + " UNION SELECT TCSCPANELID IDTIPO, 'PANEL' TIPO FROM TC_SC_PANEL WHERE TCSCBODEGAVIRTUALID = " + input.getIdBodegaVirtual() + " AND TCSCCATPAISID = " + idPais + ")"
                    + " SELECT TCSCJORNADAVENID FROM TC_SC_JORNADA_VEND J, RUTAPANEL WHERE TCSCCATPAISID = " + idPais + " AND J.IDTIPO = RUTAPANEL.IDTIPO AND J.DESCRIPCION_TIPO = RUTAPANEL.TIPO)";

                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_AVANZADO_AND, Jornada.CAMPO_TCSCJORNADAVENID, sqlFiltroBodega));
            }

            if (input.getEstado() != null && ! "".equals(input.getEstado().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Jornada.N_TABLA,
                        Jornada.CAMPO_ESTADO, input.getEstado()));
            }

            if (input.getEstadoLiquidacion() != null && ! "".equals(input.getEstadoLiquidacion().trim())) {
                if (!input.getEstadoLiquidacion().contains("'")) {
                    condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Jornada.N_TABLA,
                            Jornada.CAMPO_ESTADO_LIQUIDACION, input.getEstadoLiquidacion()));
                } else {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_UPPER_AND,
                            Jornada.N_TABLA + "." + Jornada.CAMPO_ESTADO_LIQUIDACION, input.getEstadoLiquidacion()));
                }
            }

            if (input.getEnvioAlarma() != null && ! "".equals(input.getEnvioAlarma().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Jornada.N_TABLA,
                        Jornada.CAMPO_ENVIO_ALARMA, input.getEnvioAlarma()));
            }

            if (input.getEnvioAlarmaFin() != null && ! "".equals(input.getEnvioAlarmaFin().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Jornada.N_TABLA,
                        Jornada.CAMPO_ENVIO_ALARMA_FIN, input.getEnvioAlarmaFin()));
            }

            if (input.getIdDeuda() != null && ! "".equals(input.getIdDeuda().trim())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Jornada.N_TABLA,
                        Jornada.CAMPO_SOLICITUD_PAGO, input.getIdDeuda()));
            }

            if (input.getEstadoPago() != null && ! "".equals(input.getEstadoPago().trim())) {
                if (!input.getEstadoPago().contains("'")) {
                    condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Jornada.N_TABLA,
                            Jornada.CAMPO_ESTADO_PAGO, input.getEstadoPago()));
                } else {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_UPPER_AND,
                            Jornada.N_TABLA + "." + Jornada.CAMPO_ESTADO_PAGO, input.getEstadoPago()));
                }
            }
        }

        return condiciones;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     * @param metodo
     * @param siniestro
     * @param connSiniestro
     * @return respuestaJornada
     */
    public OutputJornada getDatos(InputJornada input, int metodo, boolean siniestro, Connection connSiniestro) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta r = null;
        OutputJornada output = null;
        Connection conn = null;

        log.trace("Usuario: " + input.getUsuario());

        try {
            if (siniestro) {
                conn = connSiniestro;
            } else {
                conn = getConnRegional();
            }
            BigDecimal idPais = getIdPais(conn, input.getCodArea());

            // Se obtienen todas las configuraciones.
            String campos[] = { Catalogo.CAMPO_NOMBRE, Catalogo.CAMPO_VALOR };

            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_JORNADA_ESTADOS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_JORNADA_ESTADOS_LIQ));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_PANELRUTA));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ESTADOS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ESTADOS_SINIESTROS));

            List<Map<String, String>> datosConfig = new ArrayList<Map<String, String>>();
            try {
                datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, null);
            } catch (SQLException e) {
                log.error("Error al obtener par\u00E9metros de configuraci\u00F3n. " + e);
                r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_GET_CONFIG_103, null, nombreClase, nombreMetodo, null, input.getCodArea());

                output = new OutputJornada();
                output.setRespuesta(r);

                listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_JORNADA, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al obtener par\u00E9metros de configuraci\u00F3n la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            String tipoPanel = "";
            String tipoRuta = "";
            String estadoIniciada = "";
            String estadoFinalizada = "";
            String estadoPendiente = "";
            String estadoRechazada = "";
            String estadoLiquidada = "";
            String estadoProcesoSiniestro = "";
            String estadoSiniestrado = "";

            for (int i = 0; i < datosConfig.size(); i++) {
                if (Conf.TIPO_PANEL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoPanel = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_RUTA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoRuta = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.JORNADA_ESTADO_INICIADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoIniciada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.JORNADA_ESTADO_FINALIZADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoFinalizada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.JORNADA_ESTADO_PENDIENTE.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoPendiente = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.JORNADA_ESTADO_RECHAZADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoRechazada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.JORNADA_ESTADO_LIQUIDADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoLiquidada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.SINIESTRO_ESTADO_EN_PROCESO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoProcesoSiniestro = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.SINIESTRO_ESTADO_SINIESTRADO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoSiniestrado = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
            }

            UtileriasJava.validarParametroVacio(tipoPanel);
            UtileriasJava.validarParametroVacio(tipoRuta);
            UtileriasJava.validarParametroVacio(estadoIniciada);
            UtileriasJava.validarParametroVacio(estadoFinalizada);
            UtileriasJava.validarParametroVacio(estadoPendiente);
            UtileriasJava.validarParametroVacio(estadoRechazada);
            UtileriasJava.validarParametroVacio(estadoLiquidada);
            UtileriasJava.validarParametroVacio(estadoProcesoSiniestro);
            UtileriasJava.validarParametroVacio(estadoSiniestrado);
            String diferencia = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.DIFERENCIA_HORARIO, input.getCodArea());
            String tipoFinJornada = UtileriasJava.getConfig(conn, Conf.GRUPO_JORNADA_TIPOS, Conf.JORNADA_TIPO_FIN, input.getCodArea());

            r = validarInput(conn, input, metodo, tipoPanel, tipoRuta, tipoFinJornada, diferencia);

            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equals(r.getDescripcion())) {
                output = new OutputJornada();
                output.setRespuesta(r);

                listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_JORNADA, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            if (metodo == Conf.METODO_GET) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionJornadaMasiva.doGet(conn, input, metodo, estadoAlta, idPais);

                    listaLog.add(addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO,
                            "Se consultaron datos de jornadas.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputJornada();
                    output.setRespuesta(r);

                    listaLog.add(addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO,
                            "Problema al consultar datos de jornadas.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_POST) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
                try {
                    output = OperacionJornadaMasiva.doPost(conn, input, tipoPanel, tipoRuta, estadoAlta.toUpperCase(),
                            estadoIniciada, estadoFinalizada, estadoLiquidada, metodo, estadoProcesoSiniestro, idPais);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_JORNADA_45) {
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_JORNADA, servicioPost,
                                output.getJornadas().get(0).getIdJornada(), Conf.LOG_TIPO_JORNADA,
                                "Se crearon jornadas nuevas bajo la jornada de representante ID "
                                        + output.getJornadas().get(0).getIdJornada() + ", del vendedor responsable "
                                        + input.getIdResponsable() + " en el dispositivo " + input.getCodDispositivo()
                                        + ".",
                                ""));
                    } else {
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_JORNADA, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                                "Problema al crear jornada.", output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputJornada();
                    output.setRespuesta(r);

                    listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_JORNADA, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                            "Problema al crear jornada.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_PUT) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT
                try {
                    output = OperacionJornadaMasiva.doPut(conn, input, metodo, listaLog,
                            Conf.LOG_TRANSACCION_MOD_JORNADA, servicioPut, tipoFinJornada, estadoAlta, estadoIniciada,
                            estadoFinalizada, estadoPendiente,   tipoPanel, 
                            siniestro, idPais);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_MOD_JORNADA_46) {
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_JORNADA, servicioPut, input.getIdJornada(),
                                Conf.LOG_TIPO_JORNADA,
                                "Se modificaron datos de la jornada ID " + input.getIdJornada() + ".", ""));
                    } else {
                        listaLog.clear();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_JORNADA, servicioPut, input.getIdJornada(),
                                Conf.LOG_TIPO_JORNADA, "Problema al modificar datos de la jornada.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputJornada();
                    output.setRespuesta(r);

                    listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_JORNADA, servicioPut, "0", Conf.LOG_TIPO_NINGUNO,
                            "Problema al modificar jornada.", e.getMessage()));
                }
            }

        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputJornada();
            output.setRespuesta(r);

            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_JORNADA, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                    "Problema en el servicio de jornadas.", e.getMessage()));

        } finally {
            if (!siniestro) {
                DbUtils.closeQuietly(conn);
            }

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}
