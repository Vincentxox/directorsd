package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.PDVCliente;
import com.consystec.sc.ca.ws.input.file.InputCargaFile;
import com.consystec.sc.ca.ws.input.pdv.InputBajaPDVDirec;
import com.consystec.sc.ca.ws.input.pdv.InputConsultaPDV;
import com.consystec.sc.ca.ws.input.pdv.InputPDVDirec;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.pdv.OutputpdvDirec;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlPDV;
import com.consystec.sc.sv.ws.metodos.CtrlPuntoVenta;
import com.ericsson.sdr.dto.PaisDTO;
import com.ericsson.sdr.dto.VendedorDtsDTO;
import com.ericsson.sdr.dto.payment.QueryDailyTotalTransactionRequest;
import com.ericsson.sdr.dto.payment.QueryDailyTotalTransactionResponse;
import com.ericsson.sdr.dto.ws.request.RequestNumeroPayment;
import com.ericsson.sdr.dto.ws.response.AbstractResponse;
import com.ericsson.sdr.exceptions.PaymentException;
import com.ericsson.sdr.exceptions.SdrException;
import com.ericsson.sdr.service.PaymentService;
import com.ericsson.sdr.service.PaymentServiceImpl;
import com.ericsson.sdr.utils.Country;
import com.google.gson.GsonBuilder;

public class PuntoVenta extends ControladorBase {

    private static final Logger log = Logger.getLogger(PuntoVenta.class);
    
    private static final String PAYMENT_CODE_SUCCESS = "00";
    
    String TOKEN = "";

    /***
     * Validando que no vengan parametros nulos
     */
    public Respuesta validarDatos(InputPDVDirec objDatos) {
        Connection conn = null;
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (objDatos.getPdv().getCodArea() == null || "".equals(objDatos.getPdv().getCodArea().trim())){
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODAREA_NULO_1, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);

        } else if (objDatos.getPdv().getCodArea().length() != Conf.LONG_CODPAIS) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONGINVALIDA_AREA_6, null, this.getClass().toString(), metodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        } else {
            BigDecimal idPais = null;
            try {
                conn = getConnLocal();
                idPais = getidpais(conn, objDatos.getPdv().getCodArea());
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

        if (objDatos.getPdv().getUsuario() == null || "".equals(objDatos.getPdv().getUsuario().trim())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        if ("".equals((objDatos.getToken().trim()))) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        } else {
            if (!"WEB".equalsIgnoreCase(objDatos.getToken())&& (objDatos.getPdv().getCodDispositivo() == null
                        || "".equals(objDatos.getPdv().getCodDispositivo().trim()))) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            }
        }

        return objRespuesta;
    }

    /***
     * Validando que no vengan parametros nulos
     */
    public Respuesta validarDatosBajas(InputBajaPDVDirec objDatos) {
        Connection conn = null;
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (objDatos.getDatos().getCodArea() == null || "".equals(objDatos.getDatos().getCodArea().trim())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODAREA_NULO_1, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);

        } else if (objDatos.getDatos().getCodArea().length() != Conf.LONG_CODPAIS) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONGINVALIDA_AREA_6, null, this.getClass().toString(), metodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        } else {
            BigDecimal idPais = null;
            try {
                conn = getConnLocal();
                idPais = getidpais(conn, objDatos.getDatos().getCodArea());
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
        if (objDatos.getDatos().getUsuario() == null || "".equals(objDatos.getDatos().getUsuario().trim())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        if (objDatos.getToken() == null || "".equals((objDatos.getToken().trim()))) {
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

    /***
     * Validando que no vengan parametros nulos
     */
    public Respuesta validarDatosConsulta(InputConsultaPDV objDatos) {
        Connection conn = null;
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (objDatos.getCodArea() == null || "".equals(objDatos.getCodArea().trim())) {
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

        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        if (objDatos.getToken() == null || "".equals(objDatos.getToken().trim())) {
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
     * Metodo para crear pdv
     */
    public OutputpdvDirec crearPDV(InputPDVDirec objDatos) {
        OutputpdvDirec objRespuestaPDV = new OutputpdvDirec();
     
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearPDV";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        log.trace("2");
        if (objRespuesta == null) {
            log.trace("1");
            try {
                conn = getConnLocal();
                log.trace("Ingresa a operar para servicio web...");
                TOKEN = gettokenString(conn, objDatos.getPdv().getUsuario(), objDatos.getToken(), objDatos.getPdv().getCodArea(),
                		objDatos.getPdv().getCodDispositivo());
                log.trace("TOKEN:" + TOKEN);
       
               if(TOKEN.contains(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7+"")){ 
            	   String mensaje = TOKEN.replace("-7||", "");
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7,mensaje,
                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	TOKEN="";
                	objRespuestaPDV.setRespuesta(objRespuesta);	
               }
               else      if ("LOGIN".equals(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaPDV.setRespuesta(objRespuesta);
                }  else if (TOKEN.contains("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaPDV.setRespuesta(objRespuesta);
                	TOKEN="";
                }else {
                	
                	if(isFullStack(objDatos.getPdv().getCodArea())){
                		CtrlPuntoVenta recurso = new CtrlPuntoVenta();
                		objRespuestaPDV = recurso.insertarPDV(objDatos.getPdv());               		
                	}else{
                    // obteniendo url de servicio
	                    url = Util.getURLWSLOCAL(conn, objDatos.getPdv().getCodArea(), Conf.SERVICIO_LOCAL_CREARPDV);
	
	                    log.trace("url:" + url);
	                    if (url == null || "null".equals(url) || "".equals(url)) {
	                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
	                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                        objRespuestaPDV.setRespuesta(objRespuesta);
	                    } else {
	                        PDVCliente wsPDV = new PDVCliente();
	                        wsPDV.setServerUrl(url);
	                        objRespuestaPDV = wsPDV.crearPDV(objDatos.getPdv());
	
	                        log.trace(objRespuestaPDV.getRespuesta().getDescripcion());
	                    }
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPDV.setRespuesta(objRespuesta);
            } catch (Exception e) {
                
                
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPDV.setRespuesta(objRespuesta);
                log.error(e,e);
            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaPDV.setRespuesta(objRespuesta);
        }

        objRespuestaPDV.setToken(TOKEN);
        return objRespuestaPDV;
    }

    /**
     * M\u00E9todo para modificar pdv
     */
    public OutputpdvDirec modificarPDV(InputPDVDirec objDatos) {
        OutputpdvDirec objRespuestaPDV = new OutputpdvDirec();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearPDV";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {

            try {
                conn = getConnLocal();

                TOKEN = gettokenString(conn, objDatos.getPdv().getUsuario(), objDatos.getToken(), objDatos.getPdv().getCodArea(),
                		objDatos.getPdv().getCodDispositivo());
                log.trace("TOKEN:" + TOKEN);
       
               if(TOKEN.contains(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7+"")){ 
            	   String mensaje = TOKEN.replace("-7||", "");
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7,mensaje,
                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	TOKEN="";
                	objRespuestaPDV.setRespuesta(objRespuesta);	
               }
               else  if ("LOGIN".equals(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaPDV.setRespuesta(objRespuesta);
                }  else if (TOKEN.contains("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaPDV.setRespuesta(objRespuesta);
                	TOKEN="";
                } else {
                	if(isFullStack(objDatos.getPdv().getCodArea())){
                		CtrlPuntoVenta recurso = new CtrlPuntoVenta();
                		objRespuestaPDV = recurso.modificarPDV(objDatos.getPdv());              		
                	}else{
                    // obteniendo url de servicio
	                    url = Util.getURLWSLOCAL(conn, objDatos.getPdv().getCodArea(), Conf.SERVICIO_LOCAL_MODPDV);
	//                    url="http://localhost:8181/WS_SIDRA_PA/sidra/rest/modificaPDV/";//TODO cambiar url quemada
	
	                    log.trace("url:" + url);
	                    if (url == null || "null".equals(url) || "".equals(url)) {
	                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
	                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                        objRespuestaPDV.setRespuesta(objRespuesta);
	                    } else {
	
	                        PDVCliente wsCtalogos = new PDVCliente();
	                        wsCtalogos.setServerUrl(url);
	                        objRespuestaPDV = wsCtalogos.modificarPDV(objDatos.getPdv());
	                        log.trace("objRespuesta: " + objRespuestaPDV.getRespuesta().getDescripcion());
	                    }
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPDV.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPDV.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaPDV.setRespuesta(objRespuesta);
        }
        objRespuestaPDV.setToken(TOKEN);
        return objRespuestaPDV;
    }

    /**
     * M\u00E9todo para dar de baja pdv
     */
    public OutputpdvDirec bajaPDV(InputBajaPDVDirec objDatos) {
        OutputpdvDirec objRespuestaPDV = new OutputpdvDirec();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearPDV";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatosBajas(objDatos);
        if (objRespuesta == null) {

            try {
                conn = getConnLocal();

                TOKEN = gettokenString(conn, objDatos.getDatos().getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                        objDatos.getCodDispositivo());
                log.trace("TOKEN:" + TOKEN);
       
               if(TOKEN.contains(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7+"")){ 
            	   String mensaje = TOKEN.replace("-7||", "");
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7,mensaje,
                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	TOKEN="";
                	objRespuestaPDV.setRespuesta(objRespuesta);
               }
               else 

                if ("LOGIN".equals(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaPDV.setRespuesta(objRespuesta);
                } else if (TOKEN.contains("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaPDV.setRespuesta(objRespuesta);
                	TOKEN="";
                }else {
                	if(isFullStack(objDatos.getDatos().getCodArea())){
                		CtrlPuntoVenta recurso = new CtrlPuntoVenta();
                		objRespuestaPDV = recurso.cambiarEstadoPDV(objDatos.getDatos());              		
                	}else{
	                    // obteniendo url de servicio
	                    url = Util.getURLWSLOCAL(conn, objDatos.getDatos().getCodArea(), Conf.SERVICIO_LOCAL_CAMBIAESTADOPDV);
	//                    url="http://localhost:8181/WS_SIDRA_PA/sidra/rest/cambiaestadoPDV/";//TODO cambiar url quemada
	
	                    log.trace("url:" + url);
	                    if (url == null || "null".equals(url) || "".equals(url)) {
	                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
	                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                        objRespuestaPDV.setRespuesta(objRespuesta);
	
	                    } else {
	                        PDVCliente wsCtalogos = new PDVCliente();
	                        wsCtalogos.setServerUrl(url);
	                        objRespuestaPDV = wsCtalogos.bajaPDV(objDatos.getDatos());
	                        log.trace("objRespuesta:" + objRespuestaPDV.getRespuesta().getDescripcion());
	                    }
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPDV.setRespuesta(objRespuesta);
            } catch (Exception e) {
            	log.error(e,e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaPDV.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaPDV.setRespuesta(objRespuesta);
        }

        objRespuestaPDV.setToken(TOKEN);
        return objRespuestaPDV;
    }

    /**
     * M\u00E9todo para obtener puntos de venta
     */
    public OutputpdvDirec getPDV(InputConsultaPDV objDatos) {
        OutputpdvDirec objRespuestaWS = new OutputpdvDirec();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getPDV";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatosConsulta(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();

                TOKEN = gettokenString(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                        objDatos.getCodDispositivo());
                log.trace("TOKEN:" + TOKEN);
       
               if(TOKEN.contains(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7+"")){ 
            	   String mensaje = TOKEN.replace("-7||", "");
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7,mensaje,
                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	TOKEN="";
                	objRespuestaWS.setRespuesta(objRespuesta);
               }
               else 
                if ("LOGIN".equals(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaWS.setRespuesta(objRespuesta);

                }else if (TOKEN.contains("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                    objRespuestaWS.setRespuesta(objRespuesta);
                	TOKEN="";
                }  else {
                  	if(isFullStack(objDatos.getCodArea())){
                		CtrlPDV recurso = new CtrlPDV();
                		objRespuestaWS = recurso.getDatos(objDatos, Conf.METODO_GET);          		
                	}else{
                	
	                    // obteniendo url de servicio
	                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETPDV);
	
	                    log.trace("url: " + url);
	                    log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));
	
	                    if (url == null || "null".equals(url) || "".equals(url)) {
	                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
	                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                        objRespuestaWS.setRespuesta(objRespuesta);
	                    } else {
	                        PDVCliente wsPDV = new PDVCliente();
	                        wsPDV.setServerUrl(url);
	                        objRespuestaWS = wsPDV.getPDV(objDatos);
	
	                        if (objRespuestaWS.getPuntoDeVenta() != null) {
	                            for (int i = 0; i < objRespuestaWS.getPuntoDeVenta().size(); i++) {
	                            	if("502".equals(objDatos.getCodArea())){
		                                if (objRespuestaWS.getPuntoDeVenta().get(i).getImgAsociadas() == null ||
		                                		objRespuestaWS.getPuntoDeVenta().get(i).getImgAsociadas().isEmpty()||
		                                		objRespuestaWS.getPuntoDeVenta().get(i).getImgAsociadas().get(0).getIdImgPDV()==null) {
		                                    objRespuestaWS.getPuntoDeVenta().get(i)
		                                            .setImgAsociadas(new ArrayList<InputCargaFile>());
		                                }
	                            	}else{
	                            	    if (objRespuestaWS.getPuntoDeVenta().get(i).getImgAsociadas() == null) {
		                                    objRespuestaWS.getPuntoDeVenta().get(i)
		                                            .setImgAsociadas(new ArrayList<InputCargaFile>());
		                                }
	                            	}
	                            }
	                        }
	                    }
	                } 
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaWS.setRespuesta(objRespuesta);

            } catch (Exception e) {
                
                
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaWS.setRespuesta(objRespuesta);
                log.error(e,e);

            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaWS.setRespuesta(objRespuesta);
        }

        objRespuestaWS.setToken(TOKEN);
        return objRespuestaWS;
    }

    /**
     * Metodo para obtener puntos de venta Disponibles
     */
    public OutputpdvDirec getPDVDisponible(InputConsultaPDV objDatos) {
        OutputpdvDirec objRespuestaWS = new OutputpdvDirec();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getPDVDisponible";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatosConsulta(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();

                TOKEN = gettokenString(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                        objDatos.getCodDispositivo());
                log.trace("TOKEN:" + TOKEN);
       
               if(TOKEN.contains(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7+"")){ 
            	   String mensaje = TOKEN.replace("-7||", "");
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7,mensaje,
                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	TOKEN="";
                	objRespuestaWS.setRespuesta(objRespuesta);
               }
               else 

                if ("LOGIN".equals(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaWS.setRespuesta(objRespuesta);

                }  else if (TOKEN.contains("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                    objRespuestaWS.setRespuesta(objRespuesta);
                	TOKEN="";
                }else {
                	if (isFullStack(objDatos.getCodArea())){
                		CtrlPuntoVenta recurso = new CtrlPuntoVenta();
                		objRespuestaWS =recurso.getPDVDisponible(objDatos, Conf.METODO_GET);
                	}else{
	                		
	                    // obteniendo url de servicio
	                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_PDV_DISPONIBLES);
	
	                    log.trace("url: " + url);
	                    log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));
	
	                    if (url == null || "null".equals(url) || "".equals(url)) {
	                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
	                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                        objRespuestaWS.setRespuesta(objRespuesta);
	                    } else {
	                        PDVCliente wsPDV = new PDVCliente();
	                        wsPDV.setServerUrl(url);
	                        objRespuestaWS = wsPDV.getPDVDisponible(objDatos);
	
	                    }
	                } 
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaWS.setRespuesta(objRespuesta);

            } catch (Exception e) {
                
                
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaWS.setRespuesta(objRespuesta);
                log.error(e,e);

            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaWS.setRespuesta(objRespuesta);
        }

        objRespuestaWS.setToken(TOKEN);
        return objRespuestaWS;
    }

    public OutputpdvDirec getCountPDVDisponible(InputConsultaPDV objDatos) {
        OutputpdvDirec objRespuestaWS = new OutputpdvDirec();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getCountPDVDisponible";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatosConsulta(objDatos);
        if (objRespuesta == null) {
            try {
                conn = obtenerDtsLocal().getConnection();

                TOKEN = gettokenString(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                        objDatos.getCodDispositivo());
                log.trace("TOKEN:" + TOKEN);
       
               if(TOKEN.contains(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7+"")){ 
            	   String mensaje = TOKEN.replace("-7||", "");
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7,mensaje,
                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	TOKEN="";
                	objRespuestaWS.setRespuesta(objRespuesta);
               }
               else     if ("LOGIN".equals(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaWS.setRespuesta(objRespuesta);

                } else {
                	if (isFullStack(objDatos.getCodArea())){
                		CtrlPuntoVenta recurso = new CtrlPuntoVenta();
                		objRespuestaWS =recurso.getPDVDisponible(objDatos, Conf.METODO_COUNT);
                	}else{
	                    // obteniendo url de servicio
	                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_COUNT_PDV_DISPONIBLES);
	
	                    log.trace("url: " + url);
	                    log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));
	
	                    if (url == null || "null".equals(url) || "".equals(url)) {
	                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
	                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                        objRespuestaWS.setRespuesta(objRespuesta);
	                    } else {
	                        PDVCliente wsPDV = new PDVCliente();
	                        wsPDV.setServerUrl(url);
	                        objRespuestaWS = wsPDV.getCountPDVDisponible(objDatos);
	
	                    }
	                } 
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaWS.setRespuesta(objRespuesta);

            } catch (Exception e) {
                
                
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaWS.setRespuesta(objRespuesta);
                log.error(e,e);

            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaWS.setRespuesta(objRespuesta);
        }

        objRespuestaWS.setToken(TOKEN);
        return objRespuestaWS;
    }

    public OutputpdvDirec getCountPDV(InputConsultaPDV objDatos) {
        OutputpdvDirec objRespuestaWS = new OutputpdvDirec();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getCountPDV";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatosConsulta(objDatos);
        if (objRespuesta == null) {
            try {
                conn = obtenerDtsLocal().getConnection();

                TOKEN = gettokenString(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                        objDatos.getCodDispositivo());
                log.trace("TOKEN:" + TOKEN);
       
               if(TOKEN.contains(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7+"")){ 
            	   String mensaje = TOKEN.replace("-7||", "");
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7,mensaje,
                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	TOKEN="";
                	objRespuestaWS.setRespuesta(objRespuesta);
               }
               else 
                if ("LOGIN".equals(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaWS.setRespuesta(objRespuesta);

                }else {
                   	if(isFullStack(objDatos.getCodArea())){
                		CtrlPDV recurso = new CtrlPDV();
                		objRespuestaWS = recurso.getDatos(objDatos, Conf.METODO_COUNT);          		
                	}else{
	                    // obteniendo url de servicio
	                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_COUNT_PDV);
	
	                    log.trace("url: " + url);
	                    log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));
	
	                    if (url == null || "null".equals(url) || "".equals(url)) {
	                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
	                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                        objRespuestaWS.setRespuesta(objRespuesta);
	                    } else {
	                        PDVCliente wsPDV = new PDVCliente();
	                        wsPDV.setServerUrl(url);
	                        objRespuestaWS = wsPDV.getCountPDV(objDatos);
	                    }
	                } 
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaWS.setRespuesta(objRespuesta);

            } catch (Exception e) {
                
                
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaWS.setRespuesta(objRespuesta);
                log.error(e,e);

            } finally {
                DbUtils.closeQuietly(conn);
            }

        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaWS.setRespuesta(objRespuesta);
        }

        objRespuestaWS.setToken(TOKEN);
        return objRespuestaWS;
    }
    
    
    
    /**
     * 
     * <p>
     * Metodo utilizado para consultar si el número telefónico existe 
     * en payment
     * </p>
     * 
     * @param dto
     * @return
     */
	public AbstractResponse validaNumeroRecargaPayment(RequestNumeroPayment dto) {
		AbstractResponse response = new AbstractResponse();
		Respuesta respuesta = new Respuesta();
		PaisDTO pais = null;
		VendedorDtsDTO vendedor = null;
		QueryDailyTotalTransactionRequest request = null;
		PaymentService servicePayment = new PaymentServiceImpl();
		String metodo = "validaTelRecargaPayment";

		// Se configura la respuesta
		respuesta.setClase(this.getClass().toString());
		respuesta.setOrigen(Conf_Mensajes.MODULO_SERVICIO_DIRECTOR);
		respuesta.setMetodo(metodo);
		respuesta.setMostrar("0");

		try {

			if (dto == null) {
				throw new SdrException("La información para el request es invalida");
			}
			if (dto.getCodArea() == null || !Country.isCodAreaValid(dto.getCodArea())) {
				respuesta = getMensaje(Conf_Mensajes.MSJ_CODAREA_NULO_1, null, this.getClass().toString(), metodo, null,
						Conf_Mensajes.ORIGEN_SERVICIOS_OP);
			} else {

				// Se obtiene el token
				String resultToken = null;
				try (Connection conn = getConnSeg()) {
					resultToken = gettokenString(conn, dto.getUsuario(), dto.getToken(), dto.getCodArea(),
							dto.getCodDispositivo());
					log.trace("resultToken:" + resultToken);
				}
				// Se valida el token obtennido
				if (resultToken.contains(String.valueOf(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7))) {

					String mensaje = resultToken.replace("-7||", "");
					respuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, mensaje, this.getClass().toString(),
							metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

				} else if (KEY_LOGIN.equals(resultToken)) {

					log.trace("Usuario debe loguearse");
					respuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(), metodo,
							null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

				} else {

					// Se ejecuta la consulta en payment
					request = new QueryDailyTotalTransactionRequest();
					servicePayment = new PaymentServiceImpl();

					try (Connection sdr = getConnLocal()) {
						// Se consulta la informacion del pais
						pais = getPaisDto(sdr, dto.getCodArea());
						// Se consulta la informacion del vendedor
						vendedor = getVendedorDts(sdr, dto.getUsuario());

						log.debug("Se configura el pais, codArea: " + pais.getArea() + " - nombre " + pais.getNombre());
						request.setCountry(pais);

						log.debug("El pin obtenido para configurar el request, pin " + vendedor.getPin());
						request.setPin(vendedor.getPin().toString());

						request.setDialogueId(0L);

						request.setProviderId(0L);

						request.setVoucherPin(null);

						log.debug("Realizando el request");
						QueryDailyTotalTransactionResponse transactionRepsonse = servicePayment
								.queryDailyTotalTransaction(request, sdr);

						if (PAYMENT_CODE_SUCCESS
								.compareTo(transactionRepsonse.getResponseTotalTrnsaction().getResponseCode()) == 0) {
							respuesta.setCodResultado(
									transactionRepsonse.getResponseTotalTrnsaction().getResponseCode());
							response.setResultado(true);
						} else {
							respuesta.setCodResultado("-1");
							respuesta.setDescripcion("No fue posible validar el número de recarga en payment");
						}
					}

				}

			}

		} catch (SdrException e) {
			log.error("Error al validar del número de recarga para payment", e);
			respuesta.setCodResultado(String.valueOf(Conf_Mensajes.CODE_SDR_EXCEPTION));
			respuesta.setExcepcion(e.getClass().toString());
			respuesta.setDescripcion(e.getMessage());
		} catch (SQLException e) {
			log.error("", e);
			respuesta.setExcepcion(e.getClass().toString());
			respuesta.setCodResultado(String.valueOf(Conf_Mensajes.CODE_SDR_EXCEPTION));
			respuesta.setDescripcion("Se presento un error al validar el token");
		} catch (NamingException e) {
			log.error("", e);
			respuesta.setExcepcion(e.getClass().toString());
			respuesta.setCodResultado(String.valueOf(Conf_Mensajes.CODE_SDR_EXCEPTION));
			respuesta.setDescripcion("Se presento un error al validar el token");
		} catch (PaymentException e) {
			log.error("Se presento un error al consultar el estado del número de recarga en payment", e);
			respuesta.setExcepcion(e.getClass().toString());
			respuesta.setCodResultado(String.valueOf(Conf_Mensajes.CODE_SDR_EXCEPTION));
			respuesta.setDescripcion(e.getMessageText());
		}

		com.ericsson.sdr.dto.ws.response.Respuesta respuestaDto = new com.ericsson.sdr.dto.ws.response.Respuesta();
		respuestaDto.setClase(respuesta.getClase());
		respuestaDto.setCodResultado(respuesta.getCodResultado());
		respuestaDto.setDescripcion(respuesta.getDescripcion());
		respuestaDto.setExcepcion(respuesta.getExcepcion());
		respuestaDto.setMetodo(respuesta.getMetodo());
		respuestaDto.setMostrar(respuesta.getMostrar());
		respuestaDto.setOrigen(respuesta.getOrigen());
		respuestaDto.setTipoExepcion(respuesta.getTipoExepcion());
		respuestaDto.setToken(respuesta.getToken());

		response.setRespuesta(respuestaDto);
		return response;
	}
    
}
