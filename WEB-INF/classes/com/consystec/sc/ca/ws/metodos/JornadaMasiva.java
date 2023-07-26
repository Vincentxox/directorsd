package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.JornadaCliente;
import com.consystec.sc.ca.ws.input.jornada.InputJornada;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.jornada.OutputJornada;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlJornadaMasiva;
import com.google.gson.GsonBuilder;

public class JornadaMasiva extends ControladorBase {
    private static final Logger log = Logger.getLogger(JornadaMasiva.class);
    String TOKEN = "";

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputJornada objDatos, int metodo) {
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

        if (metodo == Conf.METODO_GET && (objDatos.getToken() == null || "".equals(objDatos.getToken())
        		|| "".equals((objDatos.getToken().trim())))) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(),
                        nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para crear Jornadas
     */
    public OutputJornada crearJornada(InputJornada objDatos) {
        OutputJornada objRespuestaWS = new OutputJornada();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "crearJornada";
        String url = "";

        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_POST);

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                log.trace("Ingresa a operar para servicio web...");
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

                } else if ("ERROR".contentEquals(TOKEN)){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaWS.setRespuesta(objRespuesta);
                	TOKEN="";
                }  else {
                	if (isFullStack(objDatos.getCodArea()))
                    { 
                		log.trace("consumir metodo");
                		CtrlJornadaMasiva recurso=new CtrlJornadaMasiva();
                		objRespuestaWS=recurso.getDatos(objDatos, Conf.METODO_POST, false, null);
                    }
                    else
                    {
                    	// */
                        // obteniendo url de servicio
                        url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREA_JORNADA_MAS);

                        log.trace("url: " + url);
                        log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));

                        if (url == null || url.equals("null") || "".equals(url)) {
                            objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                    metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                            objRespuestaWS.setRespuesta(objRespuesta);
                        } else {
                            JornadaCliente wsJornada = new JornadaCliente();
                            wsJornada.setServerUrl(url);
                            objRespuestaWS = wsJornada.creaJornada(objDatos);
                        }
                    }
                    
                } // <--Des/Comentar para solicitar token

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
     * M\u00E9todo para modificar Jornadas
     */
    public OutputJornada modJornada(InputJornada objDatos) {
        OutputJornada objRespuestaWS = new OutputJornada();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "modJornada";
        String url = "";

        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_POST);

        if (objRespuesta == null) {
            try {
                conn = getConnLocal();
                log.trace("Ingresa a operar para servicio web...");

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
                } else {
                	if (isFullStack(objDatos.getCodArea()))
                    { 
                		log.trace("consumir metodo");
                		CtrlJornadaMasiva recurso=new CtrlJornadaMasiva();
                		objRespuestaWS=recurso.getDatos(objDatos, Conf.METODO_PUT, false, null);
                    }
                    else
                    {
                    	 //*/
                        // obteniendo url de servicio
                        url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_MOD_JORNADA_MAS);
//                        url="http://localhost:8181/WS_SIDRA_PA/sidra/rest/putJornadaMasiva";//TODO cambiar url quemada

                        log.trace("url:" + url);
                        log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));

                        if (url == null || "null".equals(url) || "".equals(url)) {
                            objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                    metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                            objRespuestaWS.setRespuesta(objRespuesta);
                        } else {
                            JornadaCliente wsJornada = new JornadaCliente();
                            wsJornada.setServerUrl(url);
                            objRespuestaWS = wsJornada.creaJornada(objDatos);
                        }
                    }
               
                } // <--Des/Comentar para solicitar token

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
     * M\u00E9todo para obtener informaci\u00F3n Jornadas
     */
    public OutputJornada getJornada(InputJornada objDatos) {
        OutputJornada objRespuestaWS = new OutputJornada();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getJornada";
        String url = "";
        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos, Conf.METODO_GET);
        if (objRespuesta == null) {
            try {
                conn = getConnLocal();

                // * Des/Comentar para solicitar token
                try {
                    TOKEN = getToken(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
                            objDatos.getCodDispositivo(),0);
                    log.trace("TOKEN:" + TOKEN);
                } catch (Exception e) {
                    log.trace("Excepcion..." + e.getMessage());
                    log.trace("Causa..." + e.getCause());

                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
                            this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

                    log.error(e,e);
                }

                if ("LOGIN".equals(TOKEN)) {
                    log.trace("Usuario debe loguearse");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                    objRespuestaWS.setRespuesta(objRespuesta);

                }else if (TOKEN.contentEquals("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaWS.setRespuesta(objRespuesta);
                	TOKEN="";
                } else if (objRespuesta != null) {
                    objRespuestaWS.setRespuesta(objRespuesta);

                } else {
                	if (isFullStack(objDatos.getCodArea()))
                    { 
                		log.trace("consumir metodo");
                		CtrlJornadaMasiva recurso=new CtrlJornadaMasiva();
                		objRespuestaWS=recurso.getDatos(objDatos, Conf.METODO_GET, false, null);
                    }
                    else
                    {
                    	// */
                        // obteniendo url de servicio
                        url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_JORNADA_MAS);

                        log.trace("url: " + url);
                        log.trace("json: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));

                        if (url == null || "null".equals(url) || "".equals(url)) {
                            objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                    metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                            objRespuestaWS.setRespuesta(objRespuesta);
                        } else {
                            JornadaCliente wsJornada = new JornadaCliente();
                            wsJornada.setServerUrl(url);
                            objRespuestaWS = wsJornada.getJornadaMasiva(objDatos);
                        }
                    }                
                } // <--Des/Comentar para solicitar token

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

    public OutputJornada obtenerSesiones(InputJornada objDatos) {
        Respuesta objRespuesta = null;
        String nombreMetodo = "obtenerSesiones";
        Connection conn = null;
        Statement stm = null;
        ResultSet rst = null;
        OutputJornada output = new OutputJornada();
        List<InputJornada> listSesiones = new ArrayList<InputJornada>();

        try {
            objRespuesta = validarDatos(objDatos, Conf.METODO_POST);
            if (objRespuesta == null) {
                objRespuesta = new Respuesta();
                if (objDatos.getIdVendedor() == null || "".equals(objDatos.getIdVendedor().trim())) {
                    objRespuesta.setDescripcion("vendedores");
                    return output;
                }
                if (objDatos.getFecha() == null || "".equals(objDatos.getFecha().trim())) {
                    objRespuesta.setDescripcion("fecha");
                    return output;
                }

                conn = getConnLocal();

                String difHorario = getParametro(Conf.DIFERENCIA_HORARIO, objDatos.getCodArea());
                String qryDifHorario = "";
                if (difHorario != null && !difHorario.equals("") && !difHorario.equals("0")) {
                    qryDifHorario = " - (" + difHorario + " / 1440) ";
                }

                String sql = "SELECT "
                    + "DISTINCT(cod_dispositivo) AS cod_dispositivo, "
                    + "username "
                    + "FROM TC_SC_SESION "
                    + "WHERE tcsccatpaisid = (SELECT TCSCCATPAISID FROM TC_SC_CAT_PAIS WHERE AREA = " + objDatos.getCodArea() + ") "
                    + "AND username IN (" + objDatos.getIdVendedor() + ") "
                    + "AND ((creado_el >= TO_TIMESTAMP ('" + objDatos.getFecha() + "', 'YYYY-MM-DD HH24:MI:SS.FF')" + qryDifHorario
                        + "AND modificado_el IS NULL) OR modificado_el >= TO_TIMESTAMP ('" + objDatos.getFecha() + "', 'YYYY-MM-DD HH24:MI:SS.FF')"
                        + qryDifHorario + ")";

                log.trace(sql);
                stm = conn.createStatement();
                rst = stm.executeQuery(sql);
                if (rst.next()) {
                    objRespuesta.setDescripcion("OK");

                    do {
                        InputJornada item = new InputJornada();
                        item.setCodDispositivo(rst.getString("cod_dispositivo"));
                        item.setUsuario(rst.getString("username"));

                        listSesiones.add(item);
                        output.setJornadas(listSesiones);
                    } while (rst.next());
                } else {
                    objRespuesta.setDescripcion("FAIL");
                    objRespuesta.setExcepcion("Ocurri\u00F3 un inconveniente al consultar sesiones.");
                }
            }
     
        } catch (SQLException e) {
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), nombreMetodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);

        } catch (Exception e) {
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                    nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(stm);

            output.setRespuesta(objRespuesta);
        }

        return output;
    }
}
