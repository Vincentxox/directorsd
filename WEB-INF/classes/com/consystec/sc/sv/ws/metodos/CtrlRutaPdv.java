package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.ruta.InputRutaPdv;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.sv.ws.operaciones.OperacionRutaPdv;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.orm.RutaPDV;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlRutaPdv extends ControladorBase {
	   
	 public Respuesta validaDatos(InputRutaPdv objDatos) {
		 
		 Respuesta objRespuesta=null;
		 String nombreMetodo="";
		 
		 if(objDatos.getIdRuta()==null ||"".equals(objDatos.getIdRuta().trim())){
			 objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDRUTA_NULO_536, this.getClass().toString(), null, nombreMetodo,
	                    "", objDatos.getCodArea());
		 }
		 
		 if(objDatos.getPdv()==null || objDatos.getPdv().length==0){
			 objRespuesta = getMensaje(Conf_Mensajes.MSJ_PDV_ASOCIA_NULO_537, this.getClass().toString(), null, nombreMetodo,
	                    "", objDatos.getCodArea()); 
		 }
		 
		 if(objDatos.getAsociacion()==null || "".equals(objDatos.getAsociacion().trim())){
			 objRespuesta = getMensaje(Conf_Mensajes.MSJ_ASOCIACION_NULO_538, this.getClass().toString(), null, nombreMetodo,
	                    "", objDatos.getCodArea());
		 }
		 

		 
		 return objRespuesta;
	 }
	 
	 /**
	  * M\u00E9todo para asociar puntos de venta a rutas
	  * @param objDatos
	  * @return
	  */
	 public Respuesta asociaRutaPdv(InputRutaPdv objDatos) {
		 List<LogSidra> listaLog = new ArrayList<LogSidra>();
		 Respuesta objRespuesta = new Respuesta();
		 RutaPDV objeto = new RutaPDV();
		 Connection conn=null;
		 
		 String metodo="asociaRutaPdv";
		 String existencia="";
		 String estadoActivo="";
		 String estadoAlta="";
		 
		 String razon="";
		 String razon2="";
		 String razonFinal="";
		 List<String> lstValidaPdv = new ArrayList<String>();
		 List<Filtro> condicionesExistencia = new ArrayList<Filtro>();
		 
		 objRespuesta=validaDatos(objDatos);
		 
		 if(objRespuesta==null){ 
			 
			 if(new BigDecimal(objDatos.getAsociacion()).intValue()==Conf.ASOCIACION_PDV){ 
			 try {
				conn=getConnRegional();
	            BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
				
				estadoAlta=UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());
				estadoActivo=UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_ESTADOS_PDV, Conf.ESTADO_ACTIVO, objDatos.getCodArea());
				
				//validando ruta
				condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TC_SC_RUTA_ID, objDatos.getIdRuta()));
				condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
				condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Ruta.CAMPO_ESTADO, estadoAlta));
				existencia = UtileriasBD.verificarExistencia(conn, Ruta.N_TABLA, condicionesExistencia);	
				
				if(new BigDecimal(existencia).intValue()==0){
					 objRespuesta = new Respuesta();
					 objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_RUTA_182, this.getClass().toString(), null, metodo,
			                    "", objDatos.getCodArea());
				}else{
					
					//validando pdv's
					for(int a=0; a<objDatos.getPdv().length; a++){
						log.trace("ID de PDV:"+objDatos.getPdv()[a]);
						lstValidaPdv = new ArrayList<String>();
						lstValidaPdv=OperacionRutaPdv.pdvValido(conn, objDatos.getPdv()[a], estadoActivo, estadoAlta, ID_PAIS);
						
						if(new BigDecimal(lstValidaPdv.get(0)).intValue()==0){
							razon+= objDatos.getPdv()[a]+",";
						
						} 
						if(!(lstValidaPdv.get(1)==null || lstValidaPdv.get(1).trim().equals(""))){
							razon2+="PDV:"+objDatos.getPdv()[a] +" RUTA:"+ lstValidaPdv.get(1)+",";
									
						}
					}
					
					if("".equals(razon) && "".equals(razon2)){
						conn.setAutoCommit(false);
						for(int a=0; a<objDatos.getPdv().length; a++){
							//armando objeto asociacion
							objeto = new RutaPDV();
							objeto.setCreado_por(objDatos.getUsuario());
							objeto.setTcscrutaid(new BigDecimal(objDatos.getIdRuta()));
							objeto.setTcscpuntoventaid(new BigDecimal(objDatos.getPdv()[a]));
							
							//insertando asociacion
							OperacionRutaPdv.insertAsociacion(conn, objeto, estadoAlta, ID_PAIS);
							
							 listaLog.add(ControladorBase.addLog(Conf.LOG_ASOCIA_RUTA_PDV, Conf.LOG_CREA_ASOCIACION, "0",
				                        Conf.LOG_TIPO_NINGUNO, "Se asigno el pdv:"+objeto.getTcscpuntoventaid() +  " a la ruta:" + objeto.getTcscrutaid(), null));
							
						}
						conn.commit();
						 objRespuesta = getMensaje(Conf_Mensajes.OK_ASOCIACION_RUTAPDV23, null, null, null,"", objDatos.getCodArea());
					}else{
						if(!("".equals(razon))){
							log.trace("razon:"+razon);
							razonFinal+="Los siguientes pdv's no son válidos:"+ razon.substring(0, razon.length()-1);
						}
						
						if(!("".equals(razon2))){
							log.trace("razon2:"+razon2);
							razonFinal+="Los siguientes pdv's ya estan asociados a rutas:"+ razon2.substring(0, razon2.length()-1);
						}
						
						 objRespuesta = getMensaje(Conf_Mensajes.MSJ_ASOCIACION_PDVS_INVALIDA_540, this.getClass().toString(), null, metodo,
				                    razonFinal, objDatos.getCodArea());
						 
						 listaLog.add(ControladorBase.addLog(Conf.LOG_ASOCIA_RUTA_PDV, Conf.LOG_CREA_ASOCIACION, "0",
			                        Conf.LOG_TIPO_NINGUNO, "Problema al realizar asociaci\u00F3n.",objRespuesta.getDescripcion()));
					}
				}
				
				  
			} catch (SQLException e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, "", objDatos.getCodArea());
                log.error(e.getMessage(), e);

                listaLog.add(ControladorBase.addLog(Conf.LOG_ASOCIA_RUTA_PDV, Conf.LOG_CREA_ASOCIACION, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al realizar asociaci\u00F3n.", e.getMessage()));
                
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    log.error(e.getMessage(), e1);
                    listaLog.add(ControladorBase.addLog(Conf.LOG_ASOCIA_RUTA_PDV, Conf.LOG_CREA_ASOCIACION, "0",
                            Conf.LOG_TIPO_NINGUNO,
                            "Problema al realizar el rollback en el servicio de crear asociacion de ruta a pdv.",
                            e1.getMessage()));
                }
			} catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, "", objDatos.getCodArea());
                log.error(e.getMessage(), e);

                listaLog.add(ControladorBase.addLog(Conf.LOG_ASOCIA_RUTA_PDV, Conf.LOG_CREA_ASOCIACION, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al realizar asociación.", e.getMessage()));
			}finally{
				DbUtils.closeQuietly(conn);
				UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
			}

		 }else{
			 objRespuesta = new Respuesta();
			 objRespuesta = getMensaje(Conf_Mensajes.MSJ_ASOCIACION_INVALIDA_539, this.getClass().toString(), null, metodo,
	                    "", objDatos.getCodArea());
		 }
			 
		 }
		 
		 return objRespuesta;
	 }
	 
	 public Respuesta eliminarutapdv(InputRutaPdv objDatos) {
		 List<LogSidra> listaLog = new ArrayList<LogSidra>();
		 Respuesta objRespuesta = new Respuesta();
		 RutaPDV objeto = new RutaPDV();
		 Connection conn=null;
		 
		 List<Filtro> condicionesExistencia = new ArrayList<Filtro>();
		 
		 String metodo="eliminarutapdv";
		 String jornadaIniciada="";
		 String tipoRuta="";
		 String estadoIniciada="";
		 
		 
		 objRespuesta=validaDatos(objDatos);
		 
		 if(objRespuesta==null){ 
			 
			 if(new BigDecimal(objDatos.getAsociacion()).intValue()==Conf.NO_ASOCIACION_PDV){
				 try {
					conn=getConnRegional();
		            BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
					 
					 tipoRuta=UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_RUTA, objDatos.getCodArea());
					 estadoIniciada=UtileriasJava.getConfig(conn, Conf.GRUPO_JORNADA_ESTADOS, Conf.JORNADA_ESTADO_INICIADA, objDatos.getCodArea());
					 
					//validando ruta
					condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_IDTIPO, objDatos.getIdRuta()));
					condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
					condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Jornada.CAMPO_DESCRIPCION_TIPO, tipoRuta));
					condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Jornada.CAMPO_ESTADO, estadoIniciada));
					jornadaIniciada = UtileriasBD.verificarExistencia(conn, Jornada.N_TABLA, condicionesExistencia);	
	
					if(new BigDecimal(jornadaIniciada).intValue()==1){
						 objRespuesta = getMensaje(Conf_Mensajes.MSJ_RUTA_JORNADA_INICIADA_541, this.getClass().toString(), null, metodo, "", objDatos.getCodArea());
					}else{
						conn.setAutoCommit(false);
						for(int a=0; a<objDatos.getPdv().length; a++){
							//armando objeto
							objeto = new RutaPDV();
							objeto.setTcscrutaid(new BigDecimal(objDatos.getIdRuta()));
							objeto.setTcscpuntoventaid(new BigDecimal(objDatos.getPdv()[a]));
							objeto.setTcsccatpaisid(ID_PAIS);
							
							//insertando asociacion
							OperacionRutaPdv.deleteAsociacion(conn, objeto);
							
							 listaLog.add(ControladorBase.addLog(Conf.LOG_ASOCIA_RUTA_PDV, Conf.LOG_ELIMINA_ASOCIACION, "0",
				                        Conf.LOG_TIPO_NINGUNO, "Se desasigno el pdv:"+objeto.getTcscpuntoventaid() +  " a la ruta:" + objeto.getTcscrutaid(), null));
					
						}
						conn.commit();
						 objRespuesta = getMensaje(Conf_Mensajes.OK_ELIMINA_RUTAPDV24, null, null, null,"", objDatos.getCodArea());
					}
				
				 } catch (SQLException e) {
					 objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
		                        metodo, "", objDatos.getCodArea());
		                log.error(e.getMessage(), e);

		                listaLog.add(ControladorBase.addLog(Conf.LOG_ASOCIA_RUTA_PDV, Conf.LOG_ELIMINA_ASOCIACION, "0",
		                        Conf.LOG_TIPO_NINGUNO, "Problema al eliminar asociaci\u00F3n.", e.getMessage()));
		                try {
		                    conn.rollback();
		                } catch (SQLException e1) {
		                    log.error(e1.getMessage(), e1);
		                    listaLog.add(ControladorBase.addLog(Conf.LOG_ASOCIA_RUTA_PDV, Conf.LOG_ELIMINA_ASOCIACION, "0",
		                            Conf.LOG_TIPO_NINGUNO,
		                            "Problema al realizar el rollback en el servicio de eliminar asociacion de ruta a pdv.",
		                            e1.getMessage()));
		                }
				} catch (Exception e) {
					objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
	                log.error(e.getMessage(), e);
	                listaLog.add(ControladorBase.addLog(Conf.LOG_ASOCIA_RUTA_PDV, Conf.LOG_ELIMINA_ASOCIACION, "0", Conf.LOG_TIPO_NINGUNO, "Problema al eliminar asociación.", e.getMessage()));
				}finally{
						
						DbUtils.closeQuietly(conn);
						UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
					}

			 }else{
				 objRespuesta = new Respuesta();
				 objRespuesta = getMensaje(Conf_Mensajes.MSJ_ASOCIACION_INVALIDA_539, this.getClass().toString(), null, metodo, "", objDatos.getCodArea());
				 
			 }
				 
				 
			 }
		 
		 return objRespuesta;
	 }
	
}
