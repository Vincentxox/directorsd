package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.InventarioMovilCliente;
import com.consystec.sc.ca.ws.input.inventariomovil.InputConsultaInventarioMovil;
import com.consystec.sc.ca.ws.input.inventariomovil.InputPrecioDescuentoArticulo;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.inventariomovil.OutputInventarioMovil;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlInventarioMovil;

public class InventarioMovil extends ControladorBase {
    String TOKEN = "";

    private static final Logger log = Logger.getLogger(InventarioMovil.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputConsultaInventarioMovil objDatos) {
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
     * M\u00E9todo para modifica distribuidores
     */
    public OutputInventarioMovil getInventario(InputConsultaInventarioMovil objDatos) {
        OutputInventarioMovil objRespuestaInventario = new OutputInventarioMovil();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getInventario";
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
                    objRespuestaInventario.setRespuesta(objRespuesta);
                }else if (TOKEN.contentEquals("ERROR")){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                            this.getClass().toString(), metodo, "Inconvenientes para obtener token", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                	String mensaje=TOKEN.replace("ERROR", "");
                	objRespuesta.setExcepcion(mensaje);
                	objRespuestaInventario.setRespuesta(objRespuesta);
                	TOKEN="";
                } else if (objRespuesta != null) {
                    objRespuestaInventario.setRespuesta(objRespuesta);
                } else {
                	  if (isFullStack(objDatos.getCodArea()))
                      { 
                  		log.trace("consumir metodo");
                  		CtrlInventarioMovil recurso=new CtrlInventarioMovil();
                  		objRespuestaInventario=recurso.getDatos(objDatos, Conf.METODO_GET);
                  		
                        if (objRespuestaInventario.getInventario() != null
                                && objRespuestaInventario.getInventario().size() > 0) {
                            for (int i = 0; i < objRespuestaInventario.getInventario().size(); i++) {
                                for (int j = 0; j < objRespuestaInventario.getInventario().get(i).getBodegas()
                                        .size(); j++) {
                                    for (int k = 0; k < objRespuestaInventario.getInventario().get(i).getBodegas()
                                            .get(j).getGrupos().size(); k++) {
                                        for (int l = 0; l < objRespuestaInventario.getInventario().get(i).getBodegas()
                                                .get(j).getGrupos().get(k).getArticulos().size(); l++) {
                                            if (objRespuestaInventario.getInventario().get(i).getBodegas().get(j).getGrupos().get(k).getArticulos().get(l).getDetallePrecios() == null
                                            		|| objRespuestaInventario.getInventario().get(i).getBodegas().get(j).getGrupos().get(k).getArticulos().get(l).getDetallePrecios().get(0) == null
                                            		|| objRespuestaInventario.getInventario().get(i).getBodegas().get(j).getGrupos().get(k).getArticulos().get(l).getDetallePrecios().get(0).getPrecioSCL() == null) {

                                                List<InputPrecioDescuentoArticulo> preciosArticulo = new ArrayList<InputPrecioDescuentoArticulo>();

                                                objRespuestaInventario.getInventario().get(i).getBodegas().get(j)
                                                        .getGrupos().get(k).getArticulos().get(l)
                                                        .setDetallePrecios(preciosArticulo);
                                            }
                                                                                 	
	                                            if (objRespuestaInventario.getInventario().get(i).getBodegas().get(j).getGrupos().get(k).getArticulos().get(l).getDescuentos() == null || 
	                                                objRespuestaInventario.getInventario().get(i).getBodegas().get(j).getGrupos().get(k).getArticulos().get(l).getDescuentos().get(0)==null
	                                                  ||objRespuestaInventario.getInventario().get(i).getBodegas().get(j).getGrupos().get(k).getArticulos().get(l).getDescuentos().get(0).getValorDescuento()==null) {
	
	                                                List<InputPrecioDescuentoArticulo> descuentosArticulo =new ArrayList<InputPrecioDescuentoArticulo>();
	                                                log.trace("aqui viene null");
	                                                log.trace(objRespuestaInventario.getInventario().get(i).getBodegas().get(j)
	                                                        .getGrupos().get(k).getArticulos().get(l).getDescuentos().size());
	
	                                                objRespuestaInventario.getInventario().get(i).getBodegas().get(j)
	                                                        .getGrupos().get(k).getArticulos().get(l).setDescuentos(descuentosArticulo);
	                                                      
	                                            }
                                          
                                        }
                                    }
                                }
                            }
                        }
                      }
                      else
                      {
                    	  // obteniendo url de servicio
                          log.trace("obteniendo url");
                          url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GETINVENTARIO_MOVIL);

                          log.trace("url:" + url);
                          if (url == null || url.equals("null") || "".equals(url)) {
                              objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
                                      metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                              objRespuestaInventario.setRespuesta(objRespuesta);
                          } else {
                              log.trace("url valida");
                              InventarioMovilCliente wsInventario = new InventarioMovilCliente();
                              wsInventario.setServerUrl(url);
                              log.trace("consume servicio");
                              objRespuestaInventario = wsInventario.getInventario(objDatos);

                 
                              if (objRespuestaInventario.getInventario() != null
                                      && objRespuestaInventario.getInventario().size() > 0) {
                                  for (int i = 0; i < objRespuestaInventario.getInventario().size(); i++) {
                                      for (int j = 0; j < objRespuestaInventario.getInventario().get(i).getBodegas()
                                              .size(); j++) {
                                          for (int k = 0; k < objRespuestaInventario.getInventario().get(i).getBodegas()
                                                  .get(j).getGrupos().size(); k++) {
                                              for (int l = 0; l < objRespuestaInventario.getInventario().get(i).getBodegas()
                                                      .get(j).getGrupos().get(k).getArticulos().size(); l++) {
                                                  if (objRespuestaInventario.getInventario().get(i).getBodegas().get(j)
                                                          .getGrupos().get(k).getArticulos().get(l)
                                                          .getDetallePrecios() == null) {

                                                      List<InputPrecioDescuentoArticulo> preciosArticulo = new ArrayList<InputPrecioDescuentoArticulo>();

                                                      objRespuestaInventario.getInventario().get(i).getBodegas().get(j)
                                                              .getGrupos().get(k).getArticulos().get(l)
                                                              .setDetallePrecios(preciosArticulo);
                                                  }
                                                 if(objDatos.getCodArea().equals("502")|| isFullStack(objDatos.getCodArea())){
                                              	   log.trace("setea para guate");
      	                                            if (objRespuestaInventario.getInventario().get(i).getBodegas().get(j)
      	                                                    .getGrupos().get(k).getArticulos().get(l).getDescuentos() == null || objRespuestaInventario.getInventario().get(i).getBodegas().get(j)
      	                                                            .getGrupos().get(k).getArticulos().get(l).getDescuentos().isEmpty() ||
      	                                                            objRespuestaInventario.getInventario().get(i).getBodegas().get(j)
      	                                                            .getGrupos().get(k).getArticulos().get(l).getDescuentos().get(0)==null
      	                                                            ||objRespuestaInventario.getInventario().get(i).getBodegas().get(j)
      	                                                            .getGrupos().get(k).getArticulos().get(l).getDescuentos().get(0).getValorDescuento()==null) {
      	
      	                                                List<InputPrecioDescuentoArticulo> descuentosArticulo =new ArrayList<InputPrecioDescuentoArticulo>();
      	                                                log.trace("aqui viene null");
      	                                                log.trace(objRespuestaInventario.getInventario().get(i).getBodegas().get(j)
      	                                                        .getGrupos().get(k).getArticulos().get(l).getDescuentos().size());
      	
      	                                                objRespuestaInventario.getInventario().get(i).getBodegas().get(j)
      	                                                        .getGrupos().get(k).getArticulos().get(l).setDescuentos(descuentosArticulo);
      	                                                      
      	                                            }
                                                 }else{
                                              	   log.trace("setea para el resto de paises");
                                              	   if (objRespuestaInventario.getInventario().get(i).getBodegas().get(j)
      	                                                    .getGrupos().get(k).getArticulos().get(l).getDescuentos() == null  ) {
      	
      	                                                List<InputPrecioDescuentoArticulo> descuentosArticulo =new ArrayList<InputPrecioDescuentoArticulo>();
      	                                                log.trace("aqui viene null");
      	         	
      	                                                objRespuestaInventario.getInventario().get(i).getBodegas().get(j)
      	                                                        .getGrupos().get(k).getArticulos().get(l).setDescuentos(descuentosArticulo);
      	                                                      
      	                                            }

                                                 }
                                              }
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
                objRespuestaInventario.setRespuesta(objRespuesta);
            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                log.trace("esta dando problemas aqui");
                objRespuestaInventario.setRespuesta(objRespuesta);
                log.error(e,e);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            objRespuestaInventario.setRespuesta(objRespuesta);
        }

        objRespuestaInventario.setToken(TOKEN);
        return objRespuestaInventario;
    }
}
