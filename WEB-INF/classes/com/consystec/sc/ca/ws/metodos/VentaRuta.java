package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.VentaRutaCliente;
import com.consystec.sc.ca.ws.input.venta.InputGetDetalle;
import com.consystec.sc.ca.ws.input.venta.InputGetVenta;
import com.consystec.sc.ca.ws.input.venta.InputVenta;
import com.consystec.sc.ca.ws.mapas.ClientMap;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.venta.OutputArticuloVenta;
import com.consystec.sc.ca.ws.output.venta.OutputVenta;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlVenta;
import com.consystec.sc.sv.ws.metodos.CtrlVentaRuta;
import com.ericsson.sdr.utils.Country;
import com.google.gson.GsonBuilder;

public class VentaRuta extends ControladorBase {

	private static final Logger log = Logger.getLogger(VentaRuta.class);
	String TOKEN = "";

	/**
	 * Validando que no vengan par\u00E9metros nulos.
	 * 
	 * @param objDatos
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public Respuesta validarDatos(InputVenta objDatos, Connection conn) throws SQLException {
		Respuesta objRespuesta = null;
		String nombreMetodo = "validarDatos";

		if (objDatos.getCodArea() == null || "".equals(objDatos.getCodArea().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODAREA_NULO_1, null, this.getClass().toString(), nombreMetodo,
					null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

		} else if (objDatos.getCodArea().length() != Conf.LONG_CODPAIS) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONGINVALIDA_AREA_6, null, this.getClass().toString(),
					nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

		} else {
			BigDecimal idPais = getidpais(conn, objDatos.getCodArea());
			if (idPais == null || idPais.intValue() == 0) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_AREAINVALIDA_8, null, this.getClass().toString(),
						nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
			}
		}

		if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), nombreMetodo,
					null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
		}

		if (objDatos.getToken() == null || "".equals((objDatos.getToken().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(), nombreMetodo,
					null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
		} else {
			if (!"WEB".equalsIgnoreCase(objDatos.getToken())) {
				if (objDatos.getCodDispositivo() == null || "".equals(objDatos.getCodDispositivo().trim())) {

					objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(),
							nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				}
			}
		}

		return objRespuesta;
	}

	public Respuesta validarDatosVenta(InputGetVenta objDatos) {
		Connection conn = null;
		Respuesta objRespuesta = null;
		String nombreMetodo = "validarDatos";

		if (objDatos.getCodArea() == null || "".equals(objDatos.getCodArea().trim())) {
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
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_AREAINVALIDA_8, null, this.getClass().toString(),
							nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				}
			} catch (SQLException e) {
				log.error(e, e);
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

		if (objDatos.getToken() == null || "".equals((objDatos.getToken().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(), nombreMetodo,
					null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
		} else {
			if (!"WEB".equalsIgnoreCase(objDatos.getToken())) {
				if (objDatos.getCodDispositivo() == null || "".equals(objDatos.getCodDispositivo().trim())) {

					objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(),
							nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				}
			}
		}
		return objRespuesta;
	}

	public Respuesta validarDatosDetalle(InputGetDetalle objDatos) {
		Connection conn = null;
		Respuesta objRespuesta = null;
		String nombreMetodo = "validarDatos";

		if (objDatos.getCodArea() == null || "".equals(objDatos.getCodArea().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODAREA_NULO_1, null, this.getClass().toString(), nombreMetodo,
					null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

		} else if (objDatos.getCodArea().length() != Conf.LONG_CODPAIS) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONGINVALIDA_AREA_6, null, this.getClass().toString(),
					nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

		} else {
			BigDecimal idPais = null;
			try {
				conn = getConnLocal();
				idPais = getidpais(conn, objDatos.getCodArea());
				if (idPais == null || idPais.intValue() == 0) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_AREAINVALIDA_8, null, this.getClass().toString(),
							nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				}
			} catch (SQLException e) {
				log.error(e, e);
				objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), nombreMetodo,
						null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
			} finally {
				DbUtils.closeQuietly(conn);
			}
		}

		if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), nombreMetodo,
					null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
		}

		if (objDatos.getToken() == null || "".equals((objDatos.getToken().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(), nombreMetodo,
					null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
		} else {
			if (!"WEB".equalsIgnoreCase(objDatos.getToken())) {
				if (objDatos.getCodDispositivo() == null || "".equals(objDatos.getCodDispositivo().trim())) {

					objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(),
							nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				}
			}
		}

		return objRespuesta;
	}

	/**
	 * M\u00E9todo para crear ventas a puntos de venta
	 */
	public OutputVenta creaVentaRuta(InputVenta objDatos) {
		OutputVenta objRespuestaVenta = new OutputVenta();

		Connection conn = null;
		Respuesta objRespuesta = new Respuesta();
		String metodo = "creaVentaRuta";
		String url = "";
		log.trace("inicia a validar valores...");

		try {
			conn = getConnLocal();
			objRespuesta = validarDatos(objDatos, conn);

			if (objRespuesta == null) {
				try {
					TOKEN = getToken(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
							objDatos.getCodDispositivo(), 0);
					log.trace("TOKEN:" + TOKEN);
				} catch (Exception e) {

					objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
							this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
					objRespuestaVenta.setRespuesta(objRespuesta);
					log.error(e, e);
				}

				if ("LOGIN".equals(TOKEN)) {
					log.trace("Usuario debe loguearse");
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
							metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
					objRespuestaVenta.setRespuesta(objRespuesta);
				} else if ("ERROR".contains(TOKEN)) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
							this.getClass().toString(), metodo, "Inconvenientes para obtener token",
							Conf_Mensajes.ORIGEN_SERVICIOS_OP);
					String mensaje = TOKEN.replace("ERROR", "");
					objRespuesta.setExcepcion(mensaje);
					objRespuestaVenta.setRespuesta(objRespuesta);
					TOKEN = "";
				} else if (objRespuesta != null) {
					objRespuestaVenta.setRespuesta(objRespuesta);
				} else {

					if (isFullStack(objDatos.getCodArea())) {
						CtrlVentaRuta recurso = new CtrlVentaRuta();
						objRespuestaVenta = recurso.creaVentaRuta(objDatos);
						if (Conf_Mensajes.OK_VENTA.equalsIgnoreCase(objRespuestaVenta.getRespuesta().getCodResultado())
								&& !"TKT".equalsIgnoreCase(objDatos.getTipoDocumento())
								&& Country.SV.getCodArea().toString().equalsIgnoreCase(objDatos.getCodArea())
								&& objDatos.getEnviarLinkFactura() != null) {
							recurso.notificarOLS(objDatos, objRespuestaVenta);
						}
					} else {
						// obteniendo url de servicio
						url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_CREA_VENTARUTA);
						log.trace("url: " + url);
						log.trace("json post: " + new GsonBuilder().setPrettyPrinting().create().toJson(objDatos));

						if (url == null || "null".equals(url) || "".equals(url)) {
							objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
									metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
							objRespuestaVenta.setRespuesta(objRespuesta);
						} else {
							VentaRutaCliente wsCliente = new VentaRutaCliente();
							wsCliente.setServerUrl(url);
							objRespuestaVenta = wsCliente.crearVentaRuta(objDatos);
							log.trace("respuesta: " + objRespuestaVenta.getRespuesta().getDescripcion());
							log.trace("json resp: "
									+ new GsonBuilder().setPrettyPrinting().create().toJson(objRespuestaVenta));

							if ("18".equals(objRespuestaVenta.getRespuesta().getCodResultado())) {
								log.trace("inicio envio venta a mapa");
								try {
									String uriSidra = new ControladorBase().getParametro(Conf.URI_SOCKET_VENTA,
											objDatos.getCodArea());
									log.trace("url socket venta: " + uriSidra);
									if (uriSidra.equals("NULL")) {
										log.trace("no se envia venta a mapa por url no configurada");
									} else {
										log.trace("si existe url");
										ClientMap.enviarVenta(objDatos.getIdTipo(), objDatos.getTipo(),
												objDatos.getNombre(), objDatos.getApellido(),
												objDatos.getNombresFacturacion(),
												objRespuestaVenta.getArticulosVendidos(), objDatos.getLatitud(),
												objDatos.getLongitud(), objDatos.getMontoPagado(), uriSidra);
									}
								} catch (Exception e) {
									log.error("no se pudo enviar la venta a servicio de mapa");
									log.error(e, e);
								} finally {
									log.trace("finaliza envio venta");
								}
							}
						}
					}
				}
			} else {
				log.trace("Advertencia:" + objRespuesta.getCodResultado());
				log.trace("Descripcion:" + objRespuesta.getDescripcion());
				objRespuestaVenta.setRespuesta(objRespuesta);
			}

		} catch (SQLException e) {
			log.error(e, e);
			objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
					Conf_Mensajes.ORIGEN_SERVICIOS_OP);
			objRespuestaVenta.setRespuesta(objRespuesta);

		} catch (Exception e) {
			log.error(e, e);
			objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
					null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
			objRespuestaVenta.setRespuesta(objRespuesta);

		} finally {
			DbUtils.closeQuietly(conn);
			objRespuestaVenta.setToken(TOKEN);
		}

		return objRespuestaVenta;
	}

	/**
	 * M\u00E9todo para consultar datos
	 */
	public OutputVenta getVenta(InputGetVenta objDatos) {
		OutputVenta objRespuestaInventario = new OutputVenta();
		Connection conn = null;
		Respuesta objRespuesta = new Respuesta();
		String metodo = "getVenta";
		String url = "";
		log.trace("inicia a validar valores...");
		objRespuesta = validarDatosVenta(objDatos);

		if (objRespuesta == null) {
			log.trace("valores validados");
			try {
				log.trace("obtiene conexion");
				conn = getConnLocal();
				try {
					TOKEN = getToken(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
							objDatos.getCodDispositivo(), 0);
					log.trace("TOKEN:" + TOKEN);
				} catch (Exception e) {

					objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
							this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
					log.error(e, e);
				}

				if ("LOGIN".equals(TOKEN)) {
					log.trace("Usuario debe loguearse");
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
							metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
					objRespuestaInventario.setRespuesta(objRespuesta);
				} else if (TOKEN.contentEquals("ERROR")) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
							this.getClass().toString(), metodo, "Inconvenientes para obtener token",
							Conf_Mensajes.ORIGEN_SERVICIOS_OP);
					String mensaje = TOKEN.replace("ERROR", "");
					objRespuesta.setExcepcion(mensaje);
					objRespuestaInventario.setRespuesta(objRespuesta);
					TOKEN = "";
				} else if (objRespuesta != null) {
					objRespuestaInventario.setRespuesta(objRespuesta);
				} else {

					if (isFullStack(objDatos.getCodArea())) {
						CtrlVenta recurso = new CtrlVenta();
						objRespuestaInventario = recurso.getVenta(objDatos);
					} else {
						// obteniendo url de servicio
						log.trace("obteniendo url");
						url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_VENTARUTA);

						log.trace("url:" + url);
						if (url == null || url.equals("null") || "".equals(url)) {
							objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
									metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
							objRespuestaInventario.setRespuesta(objRespuesta);
						} else {
							log.trace("url valida");
							VentaRutaCliente wsInventario = new VentaRutaCliente();
							wsInventario.setServerUrl(url);
							log.trace("consume servicio");
							objRespuestaInventario = wsInventario.getVenta(objDatos);
						}
					}
				}
			} catch (SQLException e) {
				log.error(e, e);
				objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
						Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				objRespuestaInventario.setRespuesta(objRespuesta);

			} catch (Exception e) {
				log.error(e, e);
				objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
						metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				objRespuestaInventario.setRespuesta(objRespuesta);

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

	/**
	 * M\u00E9todo para consultar datos
	 */
	public OutputArticuloVenta getDetalleVenta(InputGetDetalle objDatos) {
		OutputArticuloVenta objRespuestaInventario = new OutputArticuloVenta();
		Connection conn = null;
		Respuesta objRespuesta = new Respuesta();
		String metodo = "getDetalleVenta";
		String url = "";
		log.trace("inicia a validar valores...");
		objRespuesta = validarDatosDetalle(objDatos);

		if (objRespuesta == null) {
			log.trace("valores validados");
			try {
				log.trace("obtiene conexion");
				conn = getConnLocal();
				try {
					TOKEN = getToken(conn, objDatos.getUsuario(), objDatos.getToken(), objDatos.getCodArea(),
							objDatos.getCodDispositivo(), 0);
					log.trace("TOKEN:" + TOKEN);
				} catch (Exception e) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
							this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
					log.error(e, e);
				}

				if ("LOGIN".equals(TOKEN)) {
					log.trace("Usuario debe loguearse");
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null, this.getClass().toString(),
							metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
					objRespuestaInventario.setRespuesta(objRespuesta);
				} else if (TOKEN.contentEquals("ERROR")) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
							this.getClass().toString(), metodo, "Inconvenientes para obtener token",
							Conf_Mensajes.ORIGEN_SERVICIOS_OP);
					String mensaje = TOKEN.replace("ERROR", "");
					objRespuesta.setExcepcion(mensaje);
					objRespuestaInventario.setRespuesta(objRespuesta);
					TOKEN = "";
				} else if (objRespuesta != null) {
					objRespuestaInventario.setRespuesta(objRespuesta);
				} else {
					if (isFullStack(objDatos.getCodArea())) {
						CtrlVenta recurso = new CtrlVenta();
						objRespuestaInventario = recurso.getDetalleVenta(objDatos);
					} else {

						// obteniendo url de servicio
						log.trace("obteniendo url");
						url = Util.getURLWSLOCAL(conn, objDatos.getCodArea(), Conf.SERVICIO_LOCAL_GET_VENTADETALLE);

						log.trace("url:" + url);
						if (url == null || "null".equals(url) || "".equals(url)) {
							objRespuesta = getMensaje(Conf_Mensajes.URL_NOTFOUND_9, null, this.getClass().toString(),
									metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
							objRespuestaInventario.setRespuesta(objRespuesta);
						} else {
							log.trace("url valida");
							VentaRutaCliente wsInventario = new VentaRutaCliente();
							wsInventario.setServerUrl(url);
							log.trace("consume servicio");
							objRespuestaInventario = wsInventario.getDetalleVenta(objDatos);
						}
					}
				}
			} catch (SQLException e) {
				log.error(e, e);
				objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
						Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				objRespuestaInventario.setRespuesta(objRespuesta);

			} catch (Exception e) {
				log.error(e, e);
				objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
						metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				objRespuestaInventario.setRespuesta(objRespuesta);

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
