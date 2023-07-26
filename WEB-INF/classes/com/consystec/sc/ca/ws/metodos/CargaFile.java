package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.CFileCliente;
import com.consystec.sc.ca.ws.input.file.InputCargaFile;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.file.OutputImagen;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlCargaFile;

public class CargaFile extends ControladorBase {

    private static final Logger log = Logger.getLogger(CargaFile.class);
    String TOKEN = null;

    /***
     * Validando que no vengan parametros nulos
     */
    public Respuesta validarDatos(InputCargaFile objDatos) {
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

        if (objDatos.getToken().equals("") || "".equals((objDatos.getToken()))) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        } else {
            if (!"WEB".equalsIgnoreCase(objDatos.getToken())&&(objDatos.getCodDispositivo() == null || "".equals(objDatos.getCodDispositivo().trim()))) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            }
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para crear CargaFile
     */
    public OutputImagen crearCargaFile(InputCargaFile objDatos) {
        OutputImagen objRespuestaCargaFile = new OutputImagen();

        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearCargaFile";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
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
                    objRespuestaCargaFile.setRespuesta(objRespuesta);
                } else if (TOKEN.contentEquals("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaCargaFile.setRespuesta(objRespuesta);
                	TOKEN="";
                }else if (objRespuesta != null) {
                    objRespuestaCargaFile.setRespuesta(objRespuesta);
                } else {
                	
                	if (isFullStack(objDatos.getCodArea()))
                    {
                   	log.trace("consume metodo");
                   	CtrlCargaFile recurso=new CtrlCargaFile();
                   	objRespuestaCargaFile=recurso.cargarImagen(objDatos);
                    }
                   else
                   {
                	   // obteniendo url de servicio
                       url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CARGAFILE);
                       log.trace("url:" + url);
                       if (url == null || url.equals("null") || "".equals(url)) {
                           objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                   metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                           objRespuestaCargaFile.setRespuesta(objRespuesta);
                       } else {
                           CFileCliente wsPDV = new CFileCliente();
                           wsPDV.setServerUrl(url);
                           objRespuestaCargaFile = wsPDV.cargaImagen(objDatos);
                       } 
                   }
                  
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCargaFile.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCargaFile.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
                objRespuestaCargaFile.setToken(TOKEN);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCargaFile.setRespuesta(objRespuesta);
        }

        return objRespuestaCargaFile;
    }

    /**
     * M\u00E9todo para dar baja CargaFile
     */
    public OutputImagen bajaCargaFile(InputCargaFile objDatos) {
        OutputImagen objRespuestaCargaFile = new OutputImagen();

        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "bajaCargaFile";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);

        if (objRespuesta == null) {
            try {
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
                    objRespuestaCargaFile.setRespuesta(objRespuesta);
                }else if (TOKEN.contentEquals("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaCargaFile.setRespuesta(objRespuesta);
                	TOKEN="";
                } else if (objRespuesta != null) {
                    objRespuestaCargaFile.setRespuesta(objRespuesta);
                } else {
                	if (isFullStack(objDatos.getCodArea()))
                    {
                   	log.trace("consume metodo");
                   	CtrlCargaFile recurso=new CtrlCargaFile();
                   	objRespuestaCargaFile=recurso.delImagen(objDatos);
                    }
                   else
                   {
                	   // obteniendo url de servicio
                       url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_DEL_FILE);
                       log.trace("url:" + url);
                       if (url == null || url.equals("null") || "".equals(url)) {
                           objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                   metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                           objRespuestaCargaFile.setRespuesta(objRespuesta);
                       } else {
                           CFileCliente wsPDV = new CFileCliente();
                           wsPDV.setServerUrl(url);
                           objRespuestaCargaFile = wsPDV.delImagen(objDatos);
                       }  
                   }
                   
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCargaFile.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCargaFile.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
                objRespuestaCargaFile.setToken(TOKEN);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCargaFile.setRespuesta(objRespuesta);
        }

        return objRespuestaCargaFile;
    }

    /**
     * M\u00E9todo para dar consultar CargaFiles
     */
    public OutputImagen getCargaFile(InputCargaFile objDatos) {
        OutputImagen objRespuestaCargaFile = new OutputImagen();

        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getCargaFile";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                {
	               	log.trace("consume metodo");
	               	CtrlCargaFile recurso=new CtrlCargaFile();
	               	objRespuestaCargaFile=recurso.getImagen(objDatos);
                }
               else
               {
            	// obteniendo url de servicio
                   url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETFILE);
                   log.trace("url:" + url);
                   if (url == null || url.equals("null") || "".equals(url)) {
                       objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                               null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                       objRespuestaCargaFile.setRespuesta(objRespuesta);
                   } else {
                       CFileCliente wsPDV = new CFileCliente();
                       wsPDV.setServerUrl(url);
                       objRespuestaCargaFile = wsPDV.getImagen(objDatos);
                   }   
               }
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCargaFile.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCargaFile.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
                objRespuestaCargaFile.setToken(TOKEN);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCargaFile.setRespuesta(objRespuesta);
        }

        return objRespuestaCargaFile;
    }

    /**
     * M\u00E9todo para dar consultar CargaFiles
     */
    public OutputImagen getImagenVisita(InputCargaFile objDatos) {
        OutputImagen objRespuestaCargaFile = new OutputImagen();

        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getImagenVisita";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                if (isFullStack(objDatos.getCodArea()))
                {
               	log.trace("consume metodo");
               	CtrlCargaFile recurso=new CtrlCargaFile();
               	objRespuestaCargaFile=recurso.getImagenVisita(objDatos);
                }
               else
               {
            	// obteniendo url de servicio
                   url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_IMGSPDV);
                   log.trace("url:" + url);
                   if (url == null || url.equals("null") || "".equals(url)) {
                       objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
                               null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                       objRespuestaCargaFile.setRespuesta(objRespuesta);
                   } else {
                       CFileCliente wsPDV = new CFileCliente();
                       wsPDV.setServerUrl(url);
                       objRespuestaCargaFile = wsPDV.getImagenVisita(objDatos);
                   }  
               }
                
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCargaFile.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaCargaFile.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
                objRespuestaCargaFile.setToken(TOKEN);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaCargaFile.setRespuesta(objRespuesta);
        }

        return objRespuestaCargaFile;
    }
}
