package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.FolioSCLCliente;
import com.consystec.sc.ca.ws.folio.InputFolioVirtual;
import com.consystec.sc.ca.ws.input.vendedordts.InputVendedorDTS;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.folio.OutputConfiguracionFolioVirtual;
import com.consystec.sc.ca.ws.output.vendedordts.OutputVendedorDTS;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlFoliosSCL;
import com.consystec.sc.sv.ws.metodos.CtrlOficinasSCL;
import com.google.gson.GsonBuilder;

public class FolioSCL extends ControladorBase {
    private static final Logger log = Logger.getLogger(FolioSCL.class);
    String TOKEN = "";

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputFolioVirtual objDatos, int metodo) {
        Connection conn = null;
        Respuesta objRespuesta = null;
        String nombreMetodo = "validarDatos";

        if (objDatos.getCodArea() == null || "".equals(objDatos.getCodArea().trim())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODAREA_NULO_1, null, this.getClass().toString(), nombreMetodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

        } else if (objDatos.getCodArea().length() != Conf.LONG_CODPAIS) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONGINVALIDA_AREA_6, null, this.getClass().toString(),
                    nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

        } else {
            BigDecimal idPais = null;
            try {
                conn = getConnLocal();
                idPais = getidpais(conn, objDatos.getCodArea());
                if (idPais == null || idPais.intValue() == 0) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_AREAINVALIDA_8, null, this.getClass().toString(),
                            nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), nombreMetodo,
                        null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        }

        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), nombreMetodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para obtener informaci\u00F3n FolioSCL
     */
    public OutputConfiguracionFolioVirtual getFolioSCL(InputFolioVirtual objDatos) {
        OutputConfiguracionFolioVirtual objRespuestaWS = new OutputConfiguracionFolioVirtual();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getFolioSCL";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_GET);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();

                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlFoliosSCL recurso=new CtrlFoliosSCL();
            		objRespuestaWS=recurso.getDatos(objDatos);
                }
                else
                {
                	// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_FOLIOS_SCL);

                    log.trace("url: " + url);
                    log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));

                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaWS.setRespuesta(objRespuesta);
                    } else {
                        FolioSCLCliente wsFolioSCL = new FolioSCLCliente();
                        wsFolioSCL.setServerUrl(url);
                        objRespuestaWS = wsFolioSCL.getFolioSCL(objDatos);
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

        return objRespuestaWS;
    }
    
    /**
     * M\u00E9todo para obtener informaci\u00F3n FolioSCL
     */
    public OutputVendedorDTS getOficinasSCL(InputVendedorDTS objDatos) {
        OutputVendedorDTS objRespuestaWS = new OutputVendedorDTS();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getFolioSCL";
        String url = "";
        log.trace("inicia a validar valores...");
        InputFolioVirtual obj = new InputFolioVirtual();
        obj.setCodArea(objDatos.getCodArea());
        obj.setUsuario(objDatos.getUsuario());
        objRespuesta = validarDatos(obj, Conf.METODO_GET);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();

                if (isFullStack(objDatos.getCodArea()))
                { 
            		log.trace("consumir metodo");
            		CtrlOficinasSCL recurso=new CtrlOficinasSCL();
            		objRespuestaWS=recurso.getDatos(objDatos);
                }
                else
                {
                	// obteniendo url de servicio
                    url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_OFICINAS_SCL);

                    log.trace("url: " + url);
                    log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));

                    if (url == null || url.equals("null") || "".equals(url)) {
                        objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        objRespuestaWS.setRespuesta(objRespuesta);
                    } else {
                        FolioSCLCliente wsFolioSCL = new FolioSCLCliente();
                        wsFolioSCL.setServerUrl(url);
                        objRespuestaWS = wsFolioSCL.getOficinasSCL(objDatos);
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

        return objRespuestaWS;
    }
}
