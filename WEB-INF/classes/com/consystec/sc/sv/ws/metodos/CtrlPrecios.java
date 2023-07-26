package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.venta.InputControlPrecios;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.venta.OutputControlPrecios;
import com.consystec.sc.ca.ws.output.venta.RespuestaControlPrecios;
import com.consystec.sc.sv.ws.operaciones.OperacionControlPrecios;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.Venta;
import com.consystec.sc.sv.ws.orm.VentaDet;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlPrecios extends ControladorBase {
    private static String servicioGet = Conf.LOG_GET_CONTROL_PRECIOS;

    /**
     * M\u00E9todo para validar input de reporte
     * 
     * @param objDatos
     * @param tipoConsulta
     *            : Valor 0 para indicar que no necesito pagineo, 1 para indicar
     *            que necesito pagineo
     * @return
     * @throws Exception 
     */
    public Respuesta validaDatos(InputControlPrecios objDatos, int tipoConsulta) throws Exception {
        Respuesta objRespuesta = null;
        Connection conn = null;
        String metodo = "validarDatos";
        
        log.trace("ENTRA A VALIDAR DATOS");
        log.trace("fechas:" + objDatos.getFechaFin() + "," + objDatos.getFechaInicio());
        // validando fechas
        if (!(objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim()))
                && !(objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(objDatos.getFechaInicio(), formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(objDatos.getFechaFin(), formatoFecha);

            if (fechaFin.before(fechaInicio)) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHAFIN_MENOR_62, null, this.getClass().toString(), metodo,
                        null, objDatos.getCodArea());
            } else {
                long diferenciaDias = 0;

                diferenciaDias = fechaFin.getTime() - fechaInicio.getTime();
                long dias = diferenciaDias / (1000 * 60 * 60 * 24);
                int totalDias = (int) dias;
                if (totalDias > 31) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_RANGO_FECHA_INVALIDO_511, null,
                            this.getClass().toString(), metodo, null, objDatos.getCodArea());
                }
            }
        } else if ((objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim()))
                || (objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            log.trace("fecha nulas");
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, this.getClass().toString(), metodo,
                    null, objDatos.getCodArea());
        }

        // validando par\u00E9metros de pagineo
        if (tipoConsulta == 1) {
            if (objDatos.getMin() == null || "".equals(objDatos.getMin())) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_MIN_NULO_512, null, this.getClass().toString(), metodo,
                        null, objDatos.getCodArea());
            } else {
                if (!isNumeric(objDatos.getMin())) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MINIMO_198, null,
                            this.getClass().toString(), metodo, null, objDatos.getCodArea());
                }
            }

            if (objDatos.getMax() == null || "".equals(objDatos.getMax())) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_MAX_NULO_513, null, this.getClass().toString(), metodo,
                        null, objDatos.getCodArea());
            } else {
                if (!isNumeric(objDatos.getMax())) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MAXIMO_217, null,
                            this.getClass().toString(), metodo, null, objDatos.getCodArea());
                }
            }

            if (!(objDatos.getMin() == null || "".equals(objDatos.getMin()))
                    && !(objDatos.getMax() == null || "".equals(objDatos.getMax()))) {
                BigDecimal minimo = null;
                BigDecimal maximo = null;
                String registrosMaximos = "";

                minimo = new BigDecimal(objDatos.getMin());
                maximo = new BigDecimal(objDatos.getMax());

                try{
	                conn = getConnRegional();
	                getIdPais(conn, objDatos.getCodArea());
	                registrosMaximos = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.REGISTROS_MAXIMOS, objDatos.getCodArea());
	
	                if (minimo.intValue() > maximo.intValue()) {
	                    objRespuesta = new Respuesta();
	                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MINIMO_MAYOR_218, null,
	                            this.getClass().toString(), metodo, null, objDatos.getCodArea());
	                } else if ((maximo.intValue() - minimo.intValue()) > new Integer(registrosMaximos)) {
	                    objRespuesta = new Respuesta();
	                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_RANGO_MAX_219, null,
	                            this.getClass().toString(), metodo, registrosMaximos, objDatos.getCodArea());
	                }
                }finally{
                	  DbUtils.closeQuietly(conn);
                }
            }
        }
        return objRespuesta;
    }

    /**
     * M\u00E9todo para obtener los posibles filtros que puede tener la consulta
     * 
     * @param objDatos
     * @return
     */
    public List<Filtro> getFiltros(InputControlPrecios objDatos) {
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA);
        if (!(objDatos.getArticulo() == null || "".equals(objDatos.getArticulo().trim()))) {
            log.trace("entra a filtro ARTICULO");
            lstFiltros.add(new Filtro("DV." +VentaDet.CAMPO_ARTICULO, Filtro.EQ, objDatos.getArticulo()));
        }

        if (!(objDatos.getIdDts() == null || "".equals(objDatos.getIdDts().trim()))) {
            log.trace("entra a filtro id DISTRIBUIDOR");
            lstFiltros.add(new Filtro("J." + Jornada.CAMPO_TCSCDTSID, Filtro.EQ, objDatos.getIdDts()));
        }

        if (!(objDatos.getIdTipo() == null || "".equals(objDatos.getIdTipo().trim()))) {
            log.trace("entra a filtro IDTIPO");
            lstFiltros.add(new Filtro("J." + Jornada.CAMPO_IDTIPO, Filtro.EQ, objDatos.getIdTipo()));
        }

        if (!(objDatos.getTipo()== null || "".equals(objDatos.getTipo().trim()))) {
            log.trace("entra a filtro TIPO ");
            lstFiltros.add(new Filtro("UPPER(J." + Jornada.CAMPO_DESCRIPCION_TIPO+")", Filtro.EQ,"'" +objDatos.getTipo().toUpperCase()+"'"));
        }

        if (!(objDatos.getVendedor() == null || "".equals(objDatos.getVendedor().trim()))) {
            log.trace("entra a filtro VENDEDOR");
            lstFiltros.add(new Filtro("V." + Venta.CAMPO_VENDEDOR , Filtro.EQ,
                    objDatos.getVendedor().toUpperCase()));
        }
        
        if (!(objDatos.getIdBodegaVirtual() == null || "".equals(objDatos.getIdBodegaVirtual().trim()))) {
            log.trace("entra a filtro IDTIPO");
            lstFiltros.add(new Filtro("DV." + VentaDet.CAMPO_TCSCBODEGAVIRTUALID, Filtro.EQ, objDatos.getIdTipo()));
        }
        
        if (!(objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim()))
                && !(objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            log.trace("entra a filtro FECHAS");
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(objDatos.getFechaInicio(), formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(objDatos.getFechaFin(), formatoFecha);
            lstFiltros.add(new Filtro("TRUNC(V." + Venta.CAMPO_FECHA_EMISION + ")", Filtro.GTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaInicio) + "','dd/MM/YYYY')"));
            lstFiltros.add(new Filtro("TRUNC(V." + Venta.CAMPO_FECHA_EMISION  + ")", Filtro.LTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaFin) + "','dd/MM/YYYY')"));
        }

        return lstFiltros;
    }

    /**
     * M\u00E9todo para obtener la cantidad de registros a retornar en una consulta
     * de control de precios
     * 
     * @param objDatos
     * @return
     */
    public RespuestaControlPrecios getCountCPrecios(InputControlPrecios objDatos) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        RespuestaControlPrecios respuesta = new RespuestaControlPrecios();
        Respuesta objRespuesta = null;
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        BigDecimal count = null;
        String metodo = "getCountCPrecios";
        Connection conn = null;

        // validando filtros
        try {
            objRespuesta = validaDatos(objDatos, 0);
            if (objRespuesta == null) {
                conn = getConnRegional();
                getIdPais(conn, objDatos.getCodArea());
                 lstFiltros = getFiltros(objDatos);
                count = OperacionControlPrecios.getCountDatos(conn, lstFiltros, objDatos.getCodArea());
                respuesta.setCantRegistros(count.toString());

                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
            } else {
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar control de precios.",
                        objRespuesta.getDescripcion()));

            }
        } catch (SQLException e) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar control de precios.", e.getMessage()));
        } catch (Exception e) {
        	 objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                     metodo, "", objDatos.getCodArea());
             log.error(e.getMessage(), e);

             listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_DISPOSITIVO, servicioGet, "0",
                     Conf.LOG_TIPO_NINGUNO, "Problema al crear nuevo dispositivo.", e.getMessage()));
		} finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
            respuesta.setRespuesta(objRespuesta);
        }
        return respuesta;
    }

    public RespuestaControlPrecios getControlPrecios(InputControlPrecios objDatos) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        RespuestaControlPrecios respuesta = new RespuestaControlPrecios();
        Respuesta objRespuesta = null;
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        List<OutputControlPrecios> lstArticuloPrecio = new ArrayList<OutputControlPrecios>();
        String metodo = "getCountCPrecios";
        Connection conn = null;

        // validando filtros
        try {
            objRespuesta = validaDatos(objDatos, 0);
            if (objRespuesta == null) {
                conn = getConnRegional();
                getIdPais(conn, objDatos.getCodArea());
                lstFiltros = getFiltros(objDatos);
                lstArticuloPrecio = OperacionControlPrecios.getDatos(conn, lstFiltros, new Integer(objDatos.getMax()),
                        new Integer(objDatos.getMin()), objDatos.getCodArea());

                if (lstArticuloPrecio.isEmpty()) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo,
                            null, objDatos.getCodArea());
                } else {
                    respuesta.setArticulos(lstArticuloPrecio);
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
                }

            } else {
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar control de precios.",
                        objRespuesta.getDescripcion()));

            }
        } catch (SQLException e) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar control de precios.", e.getMessage()));
        } catch (Exception e) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar control de precios.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
            respuesta.setRespuesta(objRespuesta);
        }
        return respuesta;
    }
}
