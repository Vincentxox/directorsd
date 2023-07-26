package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.VendedorxDTSCliente;
import com.consystec.sc.ca.ws.input.vendedorxdts.InputVendxdts;
import com.consystec.sc.ca.ws.input.vendedorxdts.ValidarVendedor;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.vendedorxdts.Outputvendedorxdts;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlValidaVendedor;
import com.consystec.sc.sv.ws.metodos.CtrlVendedorxDTS;

public class VendedorXDTS extends ControladorBase {

    private static final Logger log = Logger.getLogger(VendedorXDTS.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputVendxdts objDatos) {
        Connection conn = null;
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (objDatos.getCodArea() == null || objDatos.getCodArea().equals("")) {
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

        if (objDatos.getUsuario() == null || objDatos.getUsuario().equals("")) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        return objRespuesta;
    }

    /**
     * Validar datos del vendedor
     */
    public Respuesta validarDatosVend(ValidarVendedor objDatos) {
        Connection conn = null;
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (objDatos.getCodArea() == null || objDatos.getCodArea().equals("")) {
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

        if (objDatos.getUsuario() == null || objDatos.getUsuario().equals("")) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para obtener informaci\u00F3n de buzones
     */
    public Outputvendedorxdts getVendxdts(InputVendxdts objDatos) {
        Outputvendedorxdts objRespuestaVend = new Outputvendedorxdts();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getVendxdts";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                
                if(isFullStack(objDatos.getCodArea())){
                	CtrlVendedorxDTS recurso = new CtrlVendedorxDTS();
                	objRespuestaVend = recurso.getVendedorxDTS(objDatos);
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETVENDXDTS);
	
	                log.trace("url:" + url);
	                if (url == null || url.equals("null") || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaVend.setRespuesta(objRespuesta);
	                } else {
	                    VendedorxDTSCliente wsCliente = new VendedorxDTSCliente();
	                    wsCliente.setServerUrl(url);
	                    objRespuestaVend = wsCliente.getVendedorxDTS(objDatos);
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaVend.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaVend.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaVend.setRespuesta(objRespuesta);
        }

        return objRespuestaVend;
    }

    public Outputvendedorxdts getValidarVend(ValidarVendedor objDatos) {
        Outputvendedorxdts objRespuestaVend = new Outputvendedorxdts();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getValidarVend";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatosVend(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                
                if(isFullStack(objDatos.getCodArea())){
                	   CtrlValidaVendedor recurso = new CtrlValidaVendedor();
                	   objRespuestaVend =recurso.getValidaVendedor(objDatos);
                }else{
	                // obteniendo url de servicio
	                url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_VALIDA_VENDEDOR);
	                log.trace("url:" + url);
	
	                if (url == null || url.equals("null") || "".equals(url)) {
	                    objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(), metodo,
	                            null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
	                    objRespuestaVend.setRespuesta(objRespuesta);
	                } else {
	                    VendedorxDTSCliente wsCliente = new VendedorxDTSCliente();
	                    wsCliente.setServerUrl(url);
	                    objRespuestaVend = wsCliente.getValidaVendedor(objDatos);
	                    log.trace("DESCRIPCION MENSAJE:"+ objRespuestaVend.getRespuesta().getDescripcion());
	                    log.trace("NO. MENSAJE:"+ objRespuestaVend.getRespuesta().getCodResultado());
	                }
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaVend.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                objRespuestaVend.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaVend.setRespuesta(objRespuesta);
        }

        return objRespuestaVend;
    }
}
