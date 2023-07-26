package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.articuloprecio.InputArticuloPrecio;
import com.consystec.sc.ca.ws.input.articuloprecio.PrecioArticulo;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.output.articuloprecio.OutputArticuloPrecio;
import com.consystec.sc.sv.ws.operaciones.OperacionArticuloPrecio;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlArticuloPrecio extends ControladorBase {
	
	private static final Logger log = Logger.getLogger(CtrlArticuloPrecio.class);
	private List<LogSidra> listaLog = new ArrayList<LogSidra>();
	private static String servicioGet = Conf.LOG_GET_ARTICULO_PRECIO;
	private static String servicioPost = Conf.LOG_POST_ARTICULO_PRECIO;
	private static String servicioPut = Conf.LOG_PUT_ARTICULO_PRECIO;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    
	public Respuesta validarInput(InputArticuloPrecio input, int metodo){
		String nombreMetodo = "validarInput";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta respuesta = null;
		COD_PAIS = input.getCodArea();

		log.debug("Validando datos...");

		if(metodo != Conf.METODO_GET){
			if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) 
				respuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, input.getCodArea());
			

			if (input.getTipoGestion() == null || "".equals(input.getTipoGestion())) {
				respuesta = getMensaje(Conf_Mensajes.MSJ_TIPOGESTION_NULO_97, null, nombreClase, nombreMetodo, null,input.getCodArea());
			} 
			
			if (input.getPrecio() == null || "".equals(input.getPrecio())) {
				respuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_PRECIO_VACIO_668, null, nombreClase, nombreMetodo, null,input.getCodArea());
			}
			
			if (input.getIdProductOffering() == null || "".equals(input.getIdProductOffering())) {
				respuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_ID_PRODUCTO_OFFERING_VACIO_665, null, nombreClase, nombreMetodo, null,input.getCodArea());
			} else if (!isNumeric(input.getIdProductOffering())) {
				respuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_ID_PRODUCTO_OFFERING_NO_NUMERICO_666, null, nombreClase, nombreMetodo, null,input.getCodArea());
			}
		}else{
			if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) 
				respuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null,input.getCodArea());
		}
		return respuesta;
	}
	
	public OutputArticuloPrecio crearArticuloPrecio(InputArticuloPrecio objDatos) {

        listaLog = new ArrayList<LogSidra>();
        OutputArticuloPrecio objReturn = new OutputArticuloPrecio();
        BigDecimal idArticuloPrecio = null;
        Respuesta objRespuesta = new Respuesta();
        PrecioArticulo objArtPrecio = new PrecioArticulo();
         
        Connection conn = null;
        String metodo = "crearArticuloPrecio";
        String estadoAlta = "";
        String cod_moneda = "";
        String desMoneda = "";
        COD_PAIS = objDatos.getCodArea();

        log.trace("inicia a validar valores...");

        try {
            conn = getConnRegional();
           ID_PAIS = getIdPais(conn, objDatos.getCodArea());
            objRespuesta = validarInput(objDatos, Conf.METODO_POST);

            if (objRespuesta == null) {
                estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());
                cod_moneda = UtileriasJava.getConfig(conn, Conf.GRUPO_MONEDAS_PRECIO, Conf.COD_MONEDA_DOLAR, objDatos.getCodArea());
                desMoneda = UtileriasJava.getConfig(conn, Conf.GRUPO_MONEDAS_PRECIO, Conf.DESC_MONEDA, objDatos.getCodArea());

                objArtPrecio.setTcsccatpaisid(ID_PAIS);
                objArtPrecio.setArticulo(new BigDecimal(objDatos.getIdArticulo()));
                objArtPrecio.setTipo_gestion(objDatos.getTipoGestion());
                objArtPrecio.setPrecio(new BigDecimal(objDatos.getPrecio()));
                objArtPrecio.setCod_moneda(cod_moneda);
                objArtPrecio.setDes_moneda(desMoneda);
                objArtPrecio.setEstado(estadoAlta);
                objArtPrecio.setCreado_por(objDatos.getUsuario());
                objArtPrecio.setId_product_offering(new BigDecimal(objDatos.getIdProductOffering()));
                
                if (objDatos.getPrecioMin() != null && !"".equals(objDatos.getPrecioMin())) 
					objArtPrecio.setPrecio_min(new BigDecimal(objDatos.getPrecioMin()));
                else
                	objArtPrecio.setPrecio_min(null);
                
                if (objDatos.getPrecioMax() != null && !"".equals(objDatos.getPrecioMax())) 
                	objArtPrecio.setPrecio_max(new BigDecimal(objDatos.getPrecioMax()));
                else
                	objArtPrecio.setPrecio_max(null);
                
                boolean existeArtPrecio = OperacionArticuloPrecio.existeArticuloPrecio(conn, objDatos, ID_PAIS);
                boolean existeOferta = OperacionArticuloPrecio.existeOferta(conn, objDatos, ID_PAIS);
                boolean existeArticulo = OperacionArticuloPrecio.existeArticulo(conn, objDatos, ID_PAIS);
                boolean existePecioBD = OperacionArticuloPrecio.existePecio(conn, objDatos, ID_PAIS); 
                
                HashMap<String, String> param = getGrupoParam(conn, "ALTA", "TIPO_GESTION_VENTA",  ID_PAIS);
                
                if(param.get(objDatos.getTipoGestion().toLowerCase()) == null || "".equals(param.get(objDatos.getTipoGestion().toLowerCase()))){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPO_GESTION_NO_EXISTE_671, "", this.getClass().toString(), metodo, "",  objDatos.getCodArea());
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ARTICULO_PRECIO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO, "Tipo de gestion no v\u00E9lido.", objRespuesta.getDescripcion()));
                }else if(!existeArtPrecio){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_ASOSIACION_YAEXISTE_669, "", this.getClass().toString(), metodo, "", objDatos.getCodArea());
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ARTICULO_PRECIO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO, "La asosiaci\u00F3n articulo precio ya existe.", objRespuesta.getDescripcion()));
                }else if(!existeOferta){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_OFERTA_NO_EXISTE_672, "", this.getClass().toString(), metodo, "", objDatos.getCodArea());
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ARTICULO_PRECIO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO, "El IdProductOffering no existe.", objRespuesta.getDescripcion()));
                }else if(!existeArticulo){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_ARTICULO_NO_EXISTE_673, "", this.getClass().toString(), metodo, "", objDatos.getCodArea());
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ARTICULO_PRECIO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO, "El articulo no existe.", objRespuesta.getDescripcion()));
                }else if(existePecioBD){
                	objRespuesta = getMensaje(Conf_Mensajes.MSJ_ASOSIACION_YAEXISTE_669, "", this.getClass().toString(), metodo, "", objDatos.getCodArea());
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ARTICULO_PRECIO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO, "La asosiaci\u00F3n articulo precio ya existe.", objRespuesta.getDescripcion()));
                }else{
                	idArticuloPrecio = OperacionArticuloPrecio.insertArticuloPrecio(conn, objArtPrecio);
                	log.trace("idAsosiaci\u00F3n Log "+idArticuloPrecio);
                    objRespuesta = getMensaje(Conf_Mensajes.OK_CREA_ARTICULO_PRECIO_81, null, null, null, "", objDatos.getCodArea());
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ARTICULO_PRECIO, servicioPost, objDatos.getIdArticulo() + "", Conf.LOG_TIPO_ARTICULO_PRECIO, "Se cre\u00F3 asociaci\u00F3n, art\u00EDculo - oferta con ID " + idArticuloPrecio + ".", ""));
                }


            } else {
                objReturn.setRespuesta(objRespuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ARTICULO_PRECIO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ARTICULO_PRECIO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO, "Problema al crear asociaci\u00F3n articulo precio.", e.getMessage()));
        } catch (Exception e) {
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ARTICULO_PRECIO, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al crear asociaci\u00F3n articulo precio.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            objReturn.setRespuesta(objRespuesta);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }

        return objReturn;
    }
	
	public OutputArticuloPrecio getArticuloPrecio(InputArticuloPrecio objDatos) {
        listaLog = new ArrayList<LogSidra>();
        OutputArticuloPrecio respArtPrecio = new OutputArticuloPrecio();
        List<InputArticuloPrecio> lstArticulosPrecio = new ArrayList<InputArticuloPrecio>();
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getArticuloPrecio";
        Connection conn = null;
        COD_PAIS = objDatos.getCodArea();

        try {
            conn = getConnRegional();
           ID_PAIS = getIdPais(conn, objDatos.getCodArea());

            objRespuesta = validarInput(objDatos, Conf.METODO_GET);
            if (objRespuesta == null) {

            	lstArticulosPrecio = OperacionArticuloPrecio.doGet(conn, objDatos, ID_PAIS);

            	if (lstArticulosPrecio.isEmpty()) {
            		objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo, null, objDatos.getCodArea());
            	} else {
            		respArtPrecio.setArticulosPrecio(lstArticulosPrecio);
            		objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
            	}

            	listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
            			Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de buzones.", ""));

            } else {
            	respArtPrecio.setRespuesta(objRespuesta);
            }
        } catch (SQLException e) {
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de buzones.", e.getMessage()));
        } catch (Exception e) {
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de buzones.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            respArtPrecio.setRespuesta(objRespuesta);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }

        return respArtPrecio;
    }
	
	public OutputArticuloPrecio putArticuloPrecio(InputArticuloPrecio objDatos) {
		listaLog = new ArrayList<LogSidra>();
		OutputArticuloPrecio objReturn = new OutputArticuloPrecio();
		Respuesta objRespuesta = new Respuesta();
		Connection conn = null;
		String metodo = "putArticuloPrecio";
		COD_PAIS = objDatos.getCodArea();
		log.trace("inicia a validar valores...");

		try {
			conn = getConnRegional();
			getIdPais(conn, objDatos.getCodArea());

			objRespuesta = validarInput(objDatos, Conf.METODO_PUT);
			if (objRespuesta == null) {
				List<Filtro> condiciones = new ArrayList<Filtro>();
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, com.consystec.sc.sv.ws.orm.PrecioArticulo.CAMPO_ARTICULO, objDatos.getIdArticulo()));
				String existeIdArticulo = UtileriasBD.verificarExistencia(conn, com.consystec.sc.sv.ws.orm.PrecioArticulo.N_TABLA, condiciones);

				if (new Integer(existeIdArticulo) < 1) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_ID_ARTICULO_NO_EXISTE_670, "", this.getClass().toString(), metodo, "", objDatos.getCodArea());
					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_ARTICULO_PRECIO, servicioPut, objDatos.getIdArticulo(), Conf.LOG_TIPO_ARTICULO_PRECIO, "Problema al modificar datos de buzón.", objRespuesta.getDescripcion()));
				} else {

					int idArticulo = OperacionArticuloPrecio.updateArticuloPrecio(conn, objDatos, ID_PAIS);

					if (idArticulo < 1) {
						// no se modifico articulo precio
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null, null, null, "", objDatos.getCodArea());
						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_ARTICULO_PRECIO, servicioPut,
								objDatos.getIdArticulo(), Conf.LOG_TIPO_ARTICULO_PRECIO,
								"No se modificaron datos de articulo con ID " + objDatos.getIdArticulo()+ ".",
								"Error al Modificar"));
					} else {
						// se modifico articulo precio correctamente
						objRespuesta = getMensaje(Conf_Mensajes.OK_MOD_ARTICULO_PRECIO_82, null, null, null, "", objDatos.getCodArea());
						listaLog.add(
								ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_ARTICULO_PRECIO, servicioPut,
										objDatos.getIdArticulo(), Conf.LOG_TIPO_ARTICULO_PRECIO,
										"Se modificaron datos de articulo precio con ID ARTICULO " + objDatos.getIdArticulo()+ ".","Se modifico exitosamente"));
					}
				}
			} else {
				objReturn.setRespuesta(objRespuesta);

				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_ARTICULO_PRECIO, servicioPut, objDatos.getIdArticulo(),
						Conf.LOG_TIPO_ARTICULO_PRECIO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_ARTICULO_PRECIO, servicioPut, objDatos.getIdArticulo(),
					Conf.LOG_TIPO_BUZON, "Problema al modificar art\u00EDculo precio.", e.getMessage()));
		} catch (Exception e) {
			objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
					"", objDatos.getCodArea());
			log.error(e.getMessage(), e);

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_ARTICULO_PRECIO, servicioPut, objDatos.getIdArticulo(),
					Conf.LOG_TIPO_BUZON, "Problema al modificar Art\u00EDculo Precio.", e.getMessage()));
		} finally {
			DbUtils.closeQuietly(conn);
			UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
			objReturn.setRespuesta(objRespuesta);
		}
		return objReturn;
	}
}
