package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.reporte.InputReporteRecarga;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteRecarga;
import com.consystec.sc.ca.ws.output.reporte.Recargas;
import com.consystec.sc.sv.ws.operaciones.OperacionReporteRecarga;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlReporteRecarga extends ControladorBase{
	private final Logger log = Logger.getLogger(CtrlReporteRecarga.class);
    private  String servicioGet = Conf.LOG_REPORTE_RECARGA;
   
    public Respuesta validarInput(InputReporteRecarga input){
    	 String nombreMetodo = "validarInput";
         String nombreClase = new CurrentClassGetter().getClassName();
         com.consystec.sc.ca.ws.orm.Respuesta respuesta = null;
         
         log.debug("Validando datos...");

         if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
        	 respuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, input.getCodArea());
         }
         
         if (input.getIdTipo() != null && !"".equals(input.getIdTipo().trim())) {
             if (!isNumeric(input.getIdTipo())) {
            	 respuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDTIPO_NUM_167, null, nombreClase,   nombreMetodo, null, input.getCodArea());
             } else {
                 if (input.getTipo() == null || "".equals(input.getTipo())) {
                	 respuesta = getMensaje(Conf_Mensajes.MSJ_TIPO_NULO_431, null, nombreClase, nombreMetodo, null, input.getCodArea());
                 }
             }
         }
         
         if((input.getIdDts() != null && !"".equals(input.getIdDts()))&& !isNumeric(input.getIdDts())){
        		 respuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo, null, input.getCodArea());
         }
         
         
         if((input.getIdBodega() != null && !"".equals(input.getIdBodega()))&& !isNumeric(input.getIdBodega())) {
        		 respuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo, null, input.getCodArea());
         }

         if (!(input.getFechaInicio() == null || "".equals(input.getFechaInicio().trim())) && !(input.getFechaFin() == null || "".equals(input.getFechaFin().trim()))) {
             SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
             Date fechaInicio = UtileriasJava.parseDate(input.getFechaInicio(), formatoFecha);
             Date fechaFin = UtileriasJava.parseDate(input.getFechaFin(), formatoFecha);

             if (fechaFin.before(fechaInicio)) {
            	 respuesta = getMensaje(Conf_Mensajes.MSJ_FECHAFIN_MENOR_62, null, nombreClase, nombreMetodo, null, input.getCodArea());
             } else {

                 long diferenciaDias = fechaFin.getTime() - fechaInicio.getTime();
                 long dias = diferenciaDias / (1000 * 60 * 60 * 24);
                 int totalDias = (int) dias;
                 if (totalDias > 31) {
                	 respuesta = getMensaje(Conf_Mensajes.MSJ_RANGO_FECHA_INVALIDO_511, null, nombreClase, nombreMetodo, null, input.getCodArea());

                 }
             }
         } else if ((input.getFechaInicio() == null || "".equals(input.getFechaInicio().trim())) && (input.getFechaFin() == null || "".equals(input.getFechaFin().trim()))) {
             respuesta = getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, nombreClase, nombreMetodo, null, input.getCodArea());
         } 


         return respuesta;
    }
    
    public OutputReporteRecarga getReporteRecarga(InputReporteRecarga objDatos) {
    	List<LogSidra> listaLog = new ArrayList<LogSidra>();
    	OutputReporteRecarga respuesta = new OutputReporteRecarga();
        Respuesta objRespuesta = null;
        String metodo = "getReporteRecarga";
        Connection conn = null;
        String vendedores = "";
        List<Recargas> lstRecargas = new ArrayList<Recargas>();

        // validando filtros
        try {
            objRespuesta = validarInput(objDatos);
            if (objRespuesta == null) {
                conn = getConnRegional();
                BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
                
                if(objDatos.getIdTipo() != null && !"".equals(objDatos.getIdTipo())){
                	if("PANEL".equals( objDatos.getTipo())){
                		vendedores = vendedoresPanel(conn, objDatos.getIdTipo());
                	}else{
                		vendedores = vendedorRuta(conn, objDatos.getIdTipo());
                	}
                }
                
                lstRecargas = OperacionReporteRecarga.doGet(conn, objDatos, vendedores, ID_PAIS);

                if (lstRecargas.isEmpty()) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo, null, objDatos.getCodArea());
                } else {
                    respuesta.setReccargas(lstRecargas);
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
                }

            } else {
                listaLog = new ArrayList<LogSidra>();
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar reporte recargas.",
                        objRespuesta.getDescripcion()));

            }
        } catch (SQLException e) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar reporte recargas.", e.getMessage()));
        } catch (Exception e) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar reporte recargas.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
            respuesta.setRespuesta(objRespuesta);
        }
        return respuesta;
    }
    
    public String vendedoresPanel(Connection conn, String idPanel) throws SQLException{
    	String vendedores = "";
    	
    	PreparedStatement pstmt = null;
        ResultSet rst = null;
        String sql="";
        
        try {
        	sql = "SELECT vendedor " ;
        	sql = sql + "  FROM TC_SC_VEND_PANELPDV " ;
        	sql = sql + " WHERE idtipo = ? AND UPPER (tipo) = 'PANEL' ";
        	
            log.trace("QUERY VENDEDORES PANEL: " + sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, new BigDecimal(idPanel));
            rst = pstmt.executeQuery();

            if (rst.next()) {
            	vendedores += UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "VENDEDOR") + ", ";
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        
        return vendedores.substring(0, vendedores.length()-2);
    }
    
    public String vendedorRuta(Connection conn, String idRuta) throws SQLException{
    	String vendedor = "";
    	PreparedStatement pstmt = null;
        ResultSet rst = null;
        String sql="";
        
        try {
        	sql = "SELECT SECUSUARIOID " ;
        	sql = sql + "  FROM tc_sc_ruta " ;
        	sql = sql + " WHERE TCSCRUTAID = ?" ;

        	
            log.trace("QUERY VENDEDOR RUTA: " + sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, new BigDecimal(idRuta));
            rst = pstmt.executeQuery();

            if (rst.next()) {
            	vendedor = UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "VENDEDOR");
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
    	return vendedor;
    }
}
