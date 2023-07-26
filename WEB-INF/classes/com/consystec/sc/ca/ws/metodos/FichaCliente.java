package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.FichaClienteCliente;
import com.consystec.sc.ca.ws.input.fichacliente.InputFichaCliente;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.fichacliente.OutputFichaCliente;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlFichaCliente;

public class FichaCliente extends ControladorBase {
    String TOKEN = "";

    private static final Logger log = Logger.getLogger(FichaCliente.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputFichaCliente objDatos) {
        Connection conn = null;
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (objDatos.getCodArea() == null || "".equals(objDatos.getCodArea())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODAREA_NULO_1, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);

        } else if (objDatos.getCodArea().length() != Conf.LONG_CODPAIS) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONGINVALIDA_AREA_6, null, this.getClass().toString(), metodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        } else {
            BigDecimal idPais = null;
            try {
                conn = getConnLocal();
                idPais = getidpais(conn, objDatos.getCodArea());
                if (idPais == null || idPais.intValue() == 0) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_AREAINVALIDA_8, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        }
        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        if (objDatos.getToken() == null || "".equals(objDatos.getToken()) || "".equals((objDatos.getToken().trim()))) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        } else {
            if (!"WEB".equalsIgnoreCase(objDatos.getToken())&& (objDatos.getCodDispositivo() == null || "".equals(objDatos.getCodDispositivo().trim()))) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            }
        }
        return objRespuesta;
    }

    /**
     * M\u00E9todo para consultar datos
     */
    public OutputFichaCliente getFichaCliente(InputFichaCliente objDatos) {
        OutputFichaCliente objRespuestaFichaCliente = new OutputFichaCliente();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getFichaCliente";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            log.trace("valores validados");
            try {
                log.trace("obtiene conexion");
                conn = getConnLocal();
                try {
                    TOKEN = getToken(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                            objDatos.getCodDispositivo(),0);
                    log.trace("TOKEN:" + TOKEN);
                } catch (Exception e) {

                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
                            this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    log.error(e,e);
                }
                if (TOKEN.equals("LOGIN")) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaFichaCliente.setRespuesta(objRespuesta);
                }else if (TOKEN.contentEquals("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	TOKEN="";
                } else if (objRespuesta != null) {
                    objRespuestaFichaCliente.setRespuesta(objRespuesta);
                } else {
                	 if (isFullStack(objDatos.getCodArea()))
                     { 
                 		log.trace("consumir metodo");
                 		CtrlFichaCliente recurso=new CtrlFichaCliente();
                 		objRespuestaFichaCliente=recurso.getDatos(objDatos , Conf.METODO_GET, false,null);
                     }
                     else
                     {
                    	// obteniendo url de servicio
                         log.trace("obteniendo url");
                         url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_FICHACLIENTE);
                         
                         log.trace("url:" + url);

                         if (url == null || url.equals("null") || "".equals(url)) {
                             objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                     metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                             objRespuestaFichaCliente.setRespuesta(objRespuesta);
                         } else {
                             log.trace("url valida");
                             FichaClienteCliente wsFichaCliente = new FichaClienteCliente();
                             wsFichaCliente.setServerUrl(url);
                             log.trace("consume servicio");
                             objRespuestaFichaCliente = wsFichaCliente.getFichaCliente(objDatos);
                         }
                     }
                    
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaFichaCliente.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaFichaCliente.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaFichaCliente.setRespuesta(objRespuesta);
        }
        objRespuestaFichaCliente.setToken(TOKEN);
        return objRespuestaFichaCliente;
    }
    
    /**
     * Metodo para crear la estructura de CLIENTE en SCL
     * */
    public OutputFichaCliente crearFichaClienteSCL(InputFichaCliente objDatos) {
    	String metodo = "crearFichaClienteSCL";
    	OutputFichaCliente objRespuestaFichaClienteSCL = new OutputFichaCliente();
    	Respuesta objRespuesta = new Respuesta();
    	Connection conn = null;
    	String url = "";
    	
    	log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        
        if (objRespuesta ==  null) {
			try {
				conn = getConnLocal();
				
				try {
					TOKEN = getToken(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(), objDatos.getCodDispositivo(),0);
					log.trace("TOKEN:" + TOKEN);
				} catch (Exception e) {
					
                    
                    
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(), 
                    		this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    
                    log.error(e,e);
				}
				
				if (TOKEN.equals("LOGIN")) {
					log.trace("Usuario debe loguearse");
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
							metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				} else if (TOKEN.contentEquals("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	TOKEN="";
                }else if(objRespuesta == null) {
                	 if (isFullStack(objDatos.getCodArea()))
                     { 
                 		log.trace("consumir metodo");
                 		CtrlFichaCliente recurso=new CtrlFichaCliente();
                 		objRespuestaFichaClienteSCL=recurso.getDatos(objDatos , Conf.METODO_POST, false,null);
                     }
                     else
                     {
                    	// obteniendo url de servicio
                         log.trace("obteniendo url");
                         url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREAR_FICHACLIENTE_SCL);
                         log.trace("url:" + url);
                         
                         if (url == null || url.equals("null") || "".equals(url)) {
                         	objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                     metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
     					} else {
     						FichaClienteCliente wsFichaCliente = new FichaClienteCliente();
     						wsFichaCliente.setServerUrl(url);
     						log.trace("consume servicio");
     						objRespuestaFichaClienteSCL = wsFichaCliente.crearFichaClienteSCL(objDatos);
     					} 
                     }
					
				}
			} catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            } finally {
                DbUtils.closeQuietly(conn);
            }
		} else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
        }
        
        //solo cuando ocurre un error se llena el objeto respuesta
        if (objRespuesta != null) {
            objRespuestaFichaClienteSCL.setRespuesta(objRespuesta);
		}
        
        objRespuestaFichaClienteSCL.setToken(TOKEN);
        return objRespuestaFichaClienteSCL;
    }
}
