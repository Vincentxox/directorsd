package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.reporte.InputReporteXZ;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteXZ;
import com.consystec.sc.ca.ws.output.reporte.Reporte_XZ;
import com.consystec.sc.sv.ws.operaciones.OperacionReporteXZ;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.ReporteXZ;
import com.consystec.sc.sv.ws.orm.Venta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlReporteXZ extends ControladorBase {
	private List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_REPORTEXZ;
    private  String reporteX = "";
    private  String reporteZ ="";
    private  String reporteZM ="";
    private  String origenPC = "";
    private  String origenMOVIL = "";
	    
    /**
     * Método para validar input de reporte
     * @param objDatos
     * @return
     * @throws SQLException
     */
	public  Respuesta validaDatos(InputReporteXZ objDatos) throws SQLException{
		 Respuesta objRespuesta = null;
		 Connection conn =null;
		 String metodo = "validarDatos";
		 log.trace("ENTRA A VALIDAR DATOS");
		 
		 int mes=0;
		 int anio=0;
		 int mesActual=0;
		 int anioActual=0;
		 Calendar calActual = null;
		 Calendar cal = null;
		 Date dFecha;
		 
		 try{   		        	
	    	   	conn=getConnRegional();
	    	   	reporteX = UtileriasJava.getConfig(conn, Conf.TIPO_REPORTEX, Conf.TIPO_REPORTEX, objDatos.getCodArea());
	    		reporteZ = UtileriasJava.getConfig(conn, Conf.TIPO_REPORTEZ, Conf.TIPO_REPORTEZ, objDatos.getCodArea());    
	    		reporteZM = UtileriasJava.getConfig(conn, Conf.TIPO_REPORTEZ, Conf.TIPO_REPORTEZM, objDatos.getCodArea());    
	    		origenPC = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_ORIGEN, "PC", objDatos.getCodArea());
	    		origenMOVIL = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_ORIGEN,"MOVIL", objDatos.getCodArea()); 
	    		
	    		if(objDatos.getOrigen()==null || "".equals(objDatos.getOrigen().trim())){
	    			objRespuesta = new Respuesta();
		            objRespuesta = getMensaje(Conf_Mensajes.MSJ_ORIGEN_NULO_75, null, this.getClass().toString(), metodo,
		                    null, objDatos.getCodArea());
	    		}else{
	    		   
	    			
	    			if(objDatos.getOrigen().equalsIgnoreCase(origenPC)){
	    				if(objDatos.getFecha()==null || "".equals(objDatos.getFecha().trim())){
	    	    			objRespuesta = new Respuesta();
	    		            objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHANULO_40, null, this.getClass().toString(), metodo,
	    		                    null, objDatos.getCodArea());
	    	    		}
	    			}else if(objDatos.getOrigen().equalsIgnoreCase(origenMOVIL)){
	    				log.trace("es correcto");
	    			}
	    				else {
	    				objRespuesta = new Respuesta();
			            objRespuesta = getMensaje(Conf_Mensajes.MSJ_ORIGEN_INVALIDO_516, null, this.getClass().toString(), metodo,
			                    null, objDatos.getCodArea());
	    			}
	    		}
	    	
	    		
	    		if(objDatos.getTipoReporte()==null || "".equals(objDatos.getTipoReporte().trim())){
	    			objRespuesta = new Respuesta();
		            objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPOREPORTE_NULO_514, null, this.getClass().toString(), metodo,
		                    null, objDatos.getCodArea());
	    		}else{
	    			if((objDatos.getTipoReporte().equalsIgnoreCase(reporteZ)||objDatos.getTipoReporte().equalsIgnoreCase(reporteX))&& (objDatos.getIdJornada()==null || "".equals(objDatos.getIdJornada().trim()))){
			    			objRespuesta = new Respuesta();
				            objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDJORNADA_NULO_89, null, this.getClass().toString(), metodo,
				                    null, objDatos.getCodArea());
	    			}
	    			
	    			if(objDatos.getTipoReporte().equalsIgnoreCase(reporteZ) || objDatos.getTipoReporte().equalsIgnoreCase(reporteZM)){
	    		
	    	    		
	    	    		if(objDatos.getDispositivos()==null || objDatos.getDispositivos().length==0){
	    	    			objRespuesta = new Respuesta();
	    		            objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODDISPOSITIVO_NULO_67, null, this.getClass().toString(), metodo,
	    		                    null, objDatos.getCodArea());
	    	    		}
	    	    		if(objDatos.getFecha()==null || "".equals(objDatos.getFecha().trim())){
	    	    			objRespuesta = new Respuesta();
	    		            objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHANULO_40, null, this.getClass().toString(), metodo,
	    		                    null, objDatos.getCodArea());
	    	    		}else{
	    	    			if ( objDatos.getTipoReporte().equalsIgnoreCase(reporteZM)){
	    	    				/*1.obtengo fecha del día
	    	    				 *2. obtengo mes y anio de esa fecha 
	    	    				 *3. objengo mes y anio de la fecha ingresa
	    	    				 *4. comparo si es el mes y año actual no es un mes valido 
	    	    				 * */
	    	   			
	    					 
	    					  //obteniendo mes y anio actual
	    				     calActual=Calendar.getInstance();			 
	    					 anioActual = calActual.get(Calendar.YEAR);
	    				     mesActual = calActual.get(Calendar.MONTH);		
	    					 
	    					 
	    					  //obteniendo mes y anio ingresado
	    		    		 SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
	    					 dFecha=UtileriasJava.parseDate(objDatos.getFecha(), formatoFecha);  
	    					 cal=Calendar.getInstance();
	    					 cal.setTime(dFecha);	 
	    					 anio = cal.get(Calendar.YEAR);
	    				     mes = cal.get(Calendar.MONTH);		 
	    	    				
	    				      if (anioActual==anio && mesActual==mes){
	    				    	  objRespuesta = new Respuesta();
			    		            objRespuesta = getMensaje(Conf_Mensajes.MJS_MES_VENCIDO_ZM_914, null, this.getClass().toString(), metodo,
			    		                    null, objDatos.getCodArea());
	    				      }
	    	    			}
	    	    		}
	    			} else if(objDatos.getTipoReporte().equalsIgnoreCase(reporteX)){
	    				
	    				
	    				if(objDatos.getIdVendedor()==null || objDatos.getIdVendedor().trim().equals("")){
	    	    			objRespuesta = new Respuesta();
	    		            objRespuesta = getMensaje(Conf_Mensajes.MSJ_VENDEDOR_NULO_38, null, this.getClass().toString(), metodo,
	    		                    null, objDatos.getCodArea());
	    	    		}
	    			}else{
	    				objRespuesta = new Respuesta();
    		            objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPOREPORTE_INVALIDO_515, null, this.getClass().toString(), metodo,
    		                    null, objDatos.getCodArea());
	    			}
	    		}
		 } catch (Exception e) {
			  objRespuesta = new Respuesta();
              objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
              log.error(e.getMessage(), e);

              listaLog = new ArrayList<LogSidra>();
              listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                      Conf.LOG_TIPO_NINGUNO, "Problema al consultar control de precios.", e.getMessage()));
		}finally{
	        DbUtils.closeQuietly(conn);
		 }
		return objRespuesta;
	}
    	
     /**
     * Método para obtener los posibles filtros que puede tener la consulta
     * 
     * @param objDatos
     * @return
     */
    public List<Filtro> getFiltrosReporte(InputReporteXZ objDatos, BigDecimal ID_PAIS) {
    	SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA);
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        lstFiltros.add(new Filtro("R." + ReporteXZ.CAMPO_TCSCCATPAISID, Filtro.EQ, ID_PAIS));

        if (!(objDatos.getIdReporte() == null || "".equals(objDatos.getIdReporte().trim()))) {
            log.trace("entra a filtro IDTIPO");
            lstFiltros.add(new Filtro("R." + ReporteXZ.CAMPO_TCSCREPORTEXZID, Filtro.EQ, objDatos.getIdReporte()));
        }


        
        if (!(objDatos.getTipoReporte()== null || "".equals(objDatos.getTipoReporte().trim()))) {
            log.trace("entra a filtro TIPO REPORTE");
            lstFiltros.add(new Filtro("upper(R." + ReporteXZ.CAMPO_TIPO_REPORTE+")", Filtro.EQ,"'"+ objDatos.getTipoReporte().toUpperCase()+"'"));
        }

        if (!(objDatos.getIdVendedor() == null || "".equals(objDatos.getIdVendedor().trim()))) {
            log.trace("entra a filtro VENDEDOR");
            lstFiltros.add(new Filtro("R." + ReporteXZ.CAMPO_VENDEDOR , Filtro.EQ,
                    objDatos.getIdVendedor()));
        }
        
        if (!(objDatos.getIdJornada() == null || "".equals(objDatos.getIdJornada().trim()))) {
            log.trace("entra a filtro IDJORNADA");
            lstFiltros.add(new Filtro("R." + ReporteXZ.CAMPO_TCSCJORNADAVENID, Filtro.EQ, objDatos.getIdJornada()));
        }
        
        if (!(objDatos.getFecha()== null || "".equals(objDatos.getFecha().trim()))) {
            log.trace("entra a filtro FECHAS");
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(objDatos.getFecha(), formatoFecha);
            
            lstFiltros.add(new Filtro("TRUNC(R." + ReporteXZ.CAMPO_FECHA + ")", Filtro.GTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaInicio) + "','dd/MM/YYYY')"));
  
        }

        return lstFiltros;
    }
    
    public List<Filtro> getFiltros(InputReporteXZ objDatos, BigDecimal ID_PAIS) {
        List<Filtro> lstFiltros = new ArrayList<Filtro>();

        SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA);
        lstFiltros.add(new Filtro("V." + ReporteXZ.CAMPO_TCSCCATPAISID, Filtro.EQ, ID_PAIS));

        if (!(objDatos.getIdVendedor() == null || "".equals(objDatos.getIdVendedor().trim()))) {
            log.trace("entra a filtro VENDEDOR");
            lstFiltros.add(new Filtro("V." + Venta.CAMPO_VENDEDOR , Filtro.EQ,
                    objDatos.getIdVendedor()));
        }
        
        if (!(objDatos.getIdJornada() == null || "".equals(objDatos.getIdJornada().trim()))) {
            log.trace("entra a filtro IDJORNADA");
            lstFiltros.add(new Filtro("V." + Venta.CAMPO_TCSCJORNADAVENID, Filtro.EQ, objDatos.getIdJornada()));
        }
        
        if ("Z".equalsIgnoreCase(objDatos.getTipoReporte())&&  (objDatos.getDispositivos() != null && objDatos.getDispositivos().length>0)) {
                log.trace("entra a filtro cod dispositivo");
                lstFiltros.add(new Filtro("J." + Jornada.CAMPO_COD_DISPOSITIVO, Filtro.EQ, "'"+objDatos.getDispositivos()[0]+"'"));
        }
        
        if (!(objDatos.getFecha()== null || "".equals(objDatos.getFecha().trim()))) {
            log.trace("entra a filtro FECHAS");
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(objDatos.getFecha(), formatoFecha);
            
            lstFiltros.add(new Filtro("TRUNC(J." + Jornada.CAMPO_FECHA + ")", Filtro.GTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaInicio) + "','dd/MM/YYYY')"));
  
        }

        return lstFiltros;
    }
        
	/**
	 *Método para generar Reporte X/Z
	 * @param objDatos
	 * @return
	 */
	public OutputReporteXZ generarReporte(InputReporteXZ objDatos){
		OutputReporteXZ objReporte= new OutputReporteXZ();
		Respuesta objRespuesta = null;
		ReporteXZ objInsertReporte= new ReporteXZ();
		Reporte_XZ reporte = new Reporte_XZ();
		List<Reporte_XZ> lstReporte = new ArrayList<Reporte_XZ>();
		List<ReporteXZ> lstReporteInsert = new ArrayList<ReporteXZ>();
		List<BigDecimal> lstIdReporte = new ArrayList<BigDecimal>();
		Connection conn=null;
		String metodo="generarReporte";
		List<String> fechasZM = new ArrayList<String>();
		 String estadoFinJornada="";
		 String estadoInicioJornada="";
		 String estadoLiqJornada="";
		 BigDecimal jornadaValida=null;
		 List<Filtro> lstFiltros = new ArrayList<Filtro>();
		 BigDecimal idReporte=null;
		 String fecha;
		 Date dFecha;
		 List<BigDecimal> lstJornada = new ArrayList<BigDecimal>();
		 String dispositivo="";
		try {
			objRespuesta= validaDatos(objDatos);
			if(objRespuesta==null){
				log.trace("datos validados correctamente...");
				 conn = getConnRegional();
				 BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
				 
		    	 estadoFinJornada = UtileriasJava.getConfig(conn, Conf.GRUPO_JORNADA_ESTADOS, Conf.JORNADA_ESTADO_FINALIZADA, objDatos.getCodArea());
		    	 estadoInicioJornada = UtileriasJava.getConfig(conn, Conf.GRUPO_JORNADA_ESTADOS, Conf.JORNADA_ESTADO_INICIADA, objDatos.getCodArea()); 
		    	 estadoLiqJornada = UtileriasJava.getConfig(conn, Conf.GRUPO_JORNADA_ESTADOS_LIQ, Conf.JORNADA_ESTADO_LIQUIDADA, objDatos.getCodArea());
		    	 log.trace("ORIGEN PC:"+ origenPC);
		    	 log.trace(objDatos.getTipoReporte().equalsIgnoreCase(origenPC));
		    
		    	 //si es de  origen movil se procede a generar el reporte
		    	 if(objDatos.getOrigen().equalsIgnoreCase(origenMOVIL)){
		    		
		    		 //armarRegistro
		    		 if(objDatos.getTipoReporte().equalsIgnoreCase(reporteZ)){ //REPORTE Z
		    			 if(objDatos.getIdReporte()==null || "".equals(objDatos.getIdReporte().trim())){
		    				 
		    				 lstReporteInsert = new ArrayList<ReporteXZ>();
		    				 
		    			     SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
		    			     SimpleDateFormat formatoFecha2 = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA);
		    			     
		    				 dFecha=UtileriasJava.parseDate(objDatos.getFecha(), formatoFecha);  
		    				 fecha = formatoFecha2.format(dFecha);
		    				 lstJornada=OperacionReporteXZ.getJornadaFin(conn, estadoFinJornada, objDatos.getIdJornada(), fecha, objDatos.getCodArea());
			    	      
		    				 //verificando que las jornadas que puede utilizar el dispositivo del cual se generará el reporte z esten finalizadas
		    				 if(lstJornada.get(0).intValue()==lstJornada.get(1).intValue()){	
				    	     
						    		
		    					 lstReporteInsert=OperacionReporteXZ.getDatosReporteZ(conn,  objDatos);
				    				 //insertarRegistro
				    				 if(lstReporteInsert.isEmpty() || (lstReporteInsert.size()< objDatos.getDispositivos().length)){
				    					 log.trace("no encontro datos para reporte");
				    					 objRespuesta = new Respuesta();
				   		              	objRespuesta = getMensaje(Conf_Mensajes.MSJ_GETDATOS_REPORTEXZ_517, null, this.getClass().toString(), metodo, "", objDatos.getCodArea());
				   		             //REGISTRANDO LOG
					                    listaLog = new ArrayList<LogSidra>();
							            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
							                    Conf.LOG_TIPO_NINGUNO, "Problema al generar reporte X-Z.", objRespuesta.getDescripcion()));
				    				 }else{
				    					 conn.setAutoCommit(false);
				    					for( ReporteXZ obj: lstReporteInsert){
				    						idReporte=null;
				    						idReporte=OperacionReporteXZ.insertReporte(conn, obj, obj.getCod_dispositivo(), objDatos.getCodArea(), ID_PAIS);
				    						lstIdReporte.add(idReporte);
				    						if(idReporte.intValue()==Conf_Mensajes.MSJ_NO_HAY_ENCABEZAD_XZ_925){
				    							  objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_HAY_ENCABEZAD_XZ_925, null, this.getClass().toString(), metodo,
								                          null, objDatos.getCodArea());
				    							  objReporte.setRespuesta(objRespuesta);
				    							  return objReporte;
								                   
				    						}
				    					}
				    					conn.commit();
				    				 }
			    				 
		    				 }
		    				 else{
		    					 objRespuesta = new Respuesta();
				                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_ESTADO_JORNADA_368, null, this.getClass().toString(), metodo,
				                            ":"+estadoInicioJornada, objDatos.getCodArea());
				                   
				                    //REGISTRANDO LOG
				                    listaLog = new ArrayList<LogSidra>();
						            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
						                    Conf.LOG_TIPO_NINGUNO, "Problema al generar reporte X-Z.", objRespuesta.getDescripcion()));
		    				 }
		    			 }else{
		    				 idReporte= new BigDecimal(objDatos.getIdReporte());
		    			 }
		    			
		    		 }else if (objDatos.getTipoReporte().equalsIgnoreCase(reporteX)){ //REPORTE X
		    			 //se setean como null porque estos valores para este reporte no son necesarios
		    			 dispositivo=objDatos.getCodDispositivo();
		    			 objDatos.setFecha(null);
		    			 lstFiltros=getFiltros(objDatos, ID_PAIS);
				    		
		    			 //validando que la jornada se encuentre iniciada para reporte x
		    			 jornadaValida=OperacionReporteXZ.verificaJornada(conn, estadoInicioJornada, objDatos.getIdJornada(), objDatos.getCodArea());
		    			 if(jornadaValida.intValue()==0){
		    				 objRespuesta = new Respuesta();
			                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_ESTADO_JORNADA_368, null, this.getClass().toString(), metodo,
			                            ":"+estadoInicioJornada, objDatos.getCodArea());
			                   
			                    //REGISTRANDO LOG
			                    listaLog = new ArrayList<LogSidra>();
					            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
					                    Conf.LOG_TIPO_NINGUNO, "Problema al generar reporte X-Z.", objRespuesta.getDescripcion()));
		    			 }else{
		    				 objInsertReporte=OperacionReporteXZ.getDatosReporteX(conn, lstFiltros, objDatos);
		    				 
		    				 //insertarRegistro
		    				 if(objInsertReporte==null){
		    					 log.trace("no encontro datos para reporte");
		    					 objRespuesta = new Respuesta();
		   		              	objRespuesta = getMensaje(Conf_Mensajes.MSJ_GETDATOS_REPORTEXZ_517, null, this.getClass().toString(), metodo, "", objDatos.getCodArea());
		   		             //REGISTRANDO LOG
			                    listaLog = new ArrayList<LogSidra>();
					            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
					                    Conf.LOG_TIPO_NINGUNO, "Problema al generar reporte X-Z.", objRespuesta.getDescripcion()));
		    				 }else{
		    					 idReporte=OperacionReporteXZ.insertReporte(conn, objInsertReporte, dispositivo, objDatos.getCodArea(), ID_PAIS);
		    				 }
		    			 }
		    		 }
		    		 
		    		
		    	log.trace("idReporte:"+idReporte);
		    		if( idReporte==null && objRespuesta ==null) {
		    			
		    			 objRespuesta = new Respuesta();
			              objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_CREAR_REPORTEXZ_518, null, this.getClass().toString(), metodo, "", objDatos.getCodArea());
			              listaLog = new ArrayList<LogSidra>();
				            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
				                    Conf.LOG_TIPO_NINGUNO, "Problema al generar reporte X-Z.", objRespuesta.getDescripcion()));
		    		}else if(idReporte!=null){
		    			if(idReporte.intValue()==Conf_Mensajes.MSJ_NO_HAY_ENCABEZAD_XZ_925){
		    				  objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_HAY_ENCABEZAD_XZ_925, null, this.getClass().toString(), metodo, "", objDatos.getCodArea());
				              listaLog = new ArrayList<LogSidra>();
					            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
					                    Conf.LOG_TIPO_NINGUNO, "Problema al generar reporte X-Z.", objRespuesta.getDescripcion()));
		    			}else{
			    			lstFiltros = new ArrayList<Filtro>();
			    			lstFiltros.add(new Filtro("R." + ReporteXZ.CAMPO_TCSCREPORTEXZID, Filtro.EQ, idReporte));
				         	
			    			reporte = OperacionReporteXZ.getReporteXZ(conn, lstFiltros, ID_PAIS, objDatos.getCodArea());
			    			
				           
			    			if(reporte==null){
			    				  objRespuesta = new Respuesta();
				                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo,
				                            null, objDatos.getCodArea());
			    			}else{
			    				lstReporte = new ArrayList<Reporte_XZ>();
			    				lstReporte.add(reporte);
				    			objReporte.setDatos(lstReporte);
				    			objRespuesta = new Respuesta();
				                objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
			    			}
		    			}
		    		}
		    	
		    		 
		    		
		    		if(!lstIdReporte.isEmpty()){
		    			lstReporte = new ArrayList<Reporte_XZ>();
		    			for (int i=0; i<lstIdReporte.size(); i++){
		    				lstFiltros = new ArrayList<Filtro>();
			    			lstFiltros.add(new Filtro("R." + ReporteXZ.CAMPO_TCSCREPORTEXZID, Filtro.EQ, lstIdReporte.get(i)));
				         	
			    			reporte = OperacionReporteXZ.getReporteXZ(conn, lstFiltros, ID_PAIS, objDatos.getCodArea());
			    			lstReporte.add(reporte);
		    			}
		    			
		    			if(!(lstReporte.isEmpty()) && (lstReporte.size()==lstIdReporte.size())){
		    				objReporte.setDatos(lstReporte);
			    			objRespuesta = new Respuesta();
			                objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
		    			}else{
		    				objReporte.setDatos(null);
		    				  objRespuesta = new Respuesta();
			                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo,
			                            null, objDatos.getCodArea());
		    			}
		    		}
		    	 }
		    	 //Consultando solo el reporte
		    	 else if(objDatos.getOrigen().equalsIgnoreCase(origenPC)
		    			 && (objDatos.getTipoReporte().equalsIgnoreCase(reporteX)|| objDatos.getTipoReporte().equalsIgnoreCase(reporteZ))){
		    		 log.trace("obtiene filtros.");
		    		 lstFiltros=getFiltrosReporte(objDatos, ID_PAIS);
		    		 
		    		 if(objDatos.getTipoReporte().equalsIgnoreCase(reporteX)){
			    		 lstFiltros.add(new Filtro("R." + ReporteXZ.CAMPO_TCSCREPORTEXZID, Filtro.EQ, "(SELECT MAX(TCSCREPORTEXZID) FROM TC_SC_REPORTE_XZ WHERE vendedor = "+
			    		 objDatos.getIdVendedor()+"  AND Tcscjornadavenid = "+objDatos.getIdJornada()+" )"));
		    		 }
		    		 
		    		 reporte = OperacionReporteXZ.getReporteXZ(conn, lstFiltros, ID_PAIS, objDatos.getCodArea());
		    			log.trace("consulta reporte");
		    			if(reporte==null){
		    				log.trace("no encontro reporte");
		    				  objRespuesta = new Respuesta();
			                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo,
			                            null, objDatos.getCodArea());
		    			}else{
		    				log.trace("si hay reporte");
		    				lstReporte = new ArrayList<Reporte_XZ>();
		    				lstReporte.add(reporte);
			    			objReporte.setDatos(lstReporte);
			    			objRespuesta = new Respuesta();
			                objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
		    			}
		    	 }else if (objDatos.getOrigen().equalsIgnoreCase(origenPC)
		    			 && objDatos.getTipoReporte().equalsIgnoreCase(reporteZM)){ //Si es ZM la web lo genera y guarde reporte
		    		 
		    		 fechasZM = getFechaZmensual(objDatos.getFecha());
		    		 
		    		 lstReporteInsert=OperacionReporteXZ.getDatosReporteZMensual(conn, objDatos, fechasZM.get(0), fechasZM.get(1), estadoFinJornada, estadoLiqJornada);
    				 //insertarRegistro
    				 if(lstReporteInsert.isEmpty() || (lstReporteInsert.size()< objDatos.getDispositivos().length)){
    					 log.trace("no encontro datos para reporte");
    					 objRespuesta = new Respuesta();
   		              	objRespuesta = getMensaje(Conf_Mensajes.MSJ_GETDATOS_REPORTEXZ_517, null, this.getClass().toString(), metodo, "", objDatos.getCodArea());
   		             //REGISTRANDO LOG
	                    listaLog = new ArrayList<LogSidra>();
			            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
			                    Conf.LOG_TIPO_NINGUNO, "Problema al generar reporte X-Z.", objRespuesta.getDescripcion()));
    				 }else{
    					 conn.setAutoCommit(false);
    					for( ReporteXZ obj: lstReporteInsert){
    						idReporte=null;
    						idReporte=OperacionReporteXZ.insertReporte(conn, obj, obj.getCod_dispositivo(), objDatos.getCodArea(), ID_PAIS);
    						lstIdReporte.add(idReporte);
    						
    						if(idReporte.intValue()==Conf_Mensajes.MSJ_NO_HAY_ENCABEZAD_XZ_925){
    							  objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_HAY_ENCABEZAD_XZ_925, null, this.getClass().toString(), metodo,
				                          null, objDatos.getCodArea());
    							  objReporte.setRespuesta(objRespuesta);
    							  return objReporte;
				                   
    						}
    					}
    					conn.commit();
    				 }
 			    	log.trace("idReporte:"+idReporte);
		    		if( idReporte==null && objRespuesta ==null) {
		    			
		    			 objRespuesta = new Respuesta();
			              objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_CREAR_REPORTEXZ_518, null, this.getClass().toString(), metodo, "", objDatos.getCodArea());
			              listaLog = new ArrayList<LogSidra>();
				            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
				                    Conf.LOG_TIPO_NINGUNO, "Problema al generar reporte X-Z.", objRespuesta.getDescripcion()));
		    		}else if(idReporte!=null){
		    			lstFiltros = new ArrayList<Filtro>();
		    			lstFiltros.add(new Filtro("R." + ReporteXZ.CAMPO_TCSCREPORTEXZID, Filtro.EQ, idReporte));
			         	
		    			reporte = OperacionReporteXZ.getReporteXZ(conn, lstFiltros, ID_PAIS, objDatos.getCodArea());
		    			
			           
		    			if(reporte==null){
		    				  objRespuesta = new Respuesta();
			                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo,
			                            null, objDatos.getCodArea());
		    			}else{
		    				lstReporte = new ArrayList<Reporte_XZ>();
		    				lstReporte.add(reporte);
			    			objReporte.setDatos(lstReporte);
			    			objRespuesta = new Respuesta();
			                objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
		    			}
		    		}
		    	 }
			}else {
					            listaLog = new ArrayList<LogSidra>();
	            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
	                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar reporte X-Z.", objRespuesta.getDescripcion()));
	         
	        }
		}
		
		catch (SQLException e) {
			  objRespuesta = new Respuesta();
              objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
              log.error(e.getMessage(), e);

              listaLog = new ArrayList<LogSidra>();
              listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                      Conf.LOG_TIPO_NINGUNO, "Problema al consultar control de precios.", e.getMessage()));
			  log.error(e.getMessage(), e);
		} catch (ParseException e) {
			 objRespuesta = new Respuesta();
              objRespuesta = getMensaje(Conf_Mensajes.MSJ_GETDATOS_REPORTEXZ_517, e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
              listaLog = new ArrayList<LogSidra>();
              listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                      Conf.LOG_TIPO_NINGUNO, "Problema al consultar control de precios.", e.getMessage()));
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			 objRespuesta = new Respuesta();
              objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
			log.error(e.getMessage(), e);
			  listaLog = new ArrayList<LogSidra>();
              listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                      Conf.LOG_TIPO_NINGUNO, "Problema al consultar control de precios.", e.getMessage()));
		}finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
            objReporte.setRespuesta(objRespuesta);
        }
		
		return objReporte;
	}
	
	public List<String> getFechaZmensual(String fecha){
		List<String> lstFechas = new ArrayList<String>();
		 Date dFecha;
		 Date fechaInicio;
		 Date fechaFin;
		 int mes=0;
		 int anio=0;
		 int days =0;
		 int diaInicio=1;
		 Calendar cal = null;
		 Calendar calDias=null;
		 Calendar calFechaInicio=null;
		 String sFechaInicio="";
		 String sFechaFin="";
		 
		 //obteniendo mes y anio ingresado
		 SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
		 SimpleDateFormat formatoFecha2 = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA);
		   
		 dFecha=UtileriasJava.parseDate(fecha, formatoFecha);  
		 cal=Calendar.getInstance();
		 cal.setTime(dFecha);	 
		 anio = cal.get(Calendar.YEAR);
	     mes = cal.get(Calendar.MONTH);		 
	     mes++;
	     //obteniendo días de mes
	     calDias = new GregorianCalendar(anio, mes, 1); 
	     days = calDias.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
	     log.trace("fecha ingresada:"+ cal.getTime());
	     log.trace("cantidad dias:"+ days);
	     log.trace("MES:"+mes);
	     log.trace("anio:"+anio);
	     
	     if(mes<10){
	    	 sFechaInicio="0"+diaInicio+"/0"+mes+"/"+anio;
	     }else{
	    	 sFechaInicio="0"+diaInicio+"/"+mes+"/"+anio;
	     }
	     
	     //seteando fecha inicio y fecha fin
	    
	     fechaInicio=UtileriasJava.parseDate(sFechaInicio, formatoFecha2);  
	     calFechaInicio=Calendar.getInstance();
	     calFechaInicio.setTime(fechaInicio);
	     calFechaInicio.add(Calendar.DATE, (days-2));
	     log.trace(calFechaInicio.getTime());	     
	     fechaFin=calFechaInicio.getTime();
	     
	     
	     //conviertiendo fechas a estring
	     DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");
	     sFechaFin = fechaFormato.format(fechaFin);
	     
	     log.trace("fecha Inicio:"+ sFechaInicio);
	     log.trace("fecha fin:"+ sFechaFin);
	     
	     //agregando valores en lista a retornar
	     lstFechas.add(0, sFechaInicio);
	     lstFechas.add(1,sFechaFin);
	     
		return lstFechas;
	}
}
