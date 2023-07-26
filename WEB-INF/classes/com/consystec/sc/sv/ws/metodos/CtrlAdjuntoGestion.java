package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.adjuntogestion.InputAdjuntoGestion;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.adjuntogestion.OutputAdjuntoGestion;
import com.consystec.sc.sv.ws.operaciones.OperacionAdjuntoGestion;
import com.consystec.sc.sv.ws.orm.Adjunto;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author susana barrios Consystec 2015
 **/
public class CtrlAdjuntoGestion extends ControladorBase {
	private static final Logger log = Logger.getLogger(CtrlAdjuntoGestion.class);
	private static String servicioPost = Conf.LOG_POST_ADJUNTO;
	private static String servicioGet = Conf.LOG_GET_ADJUNTO;
	private static String servicioDel = Conf.LOG_DEL_ADJUNTO;
	private static String nombreClase = new CurrentClassGetter().getClassName();

	/***
	 * Validando que no vengan par\u00E9metros nulos
	 * 
	 * @param metodo
	 */
	public Respuesta validarDatos(InputAdjuntoGestion objDatos) {
		Respuesta objRespuesta = null;
		String nombreMetodo = "validarDatos";

		if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
		}

		if (objDatos.getGestion() == null || "".equals(objDatos.getGestion())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_GESTION_NULO_90, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
		} else {
			if ("PORTABILIDAD".equalsIgnoreCase(objDatos.getGestion())&& (objDatos.getIdPortaMovil() == null || "".equals(objDatos.getIdPortaMovil()))) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDPORTAMOVIL_NULO_915, null, nombreClase, nombreMetodo,
							null, objDatos.getCodArea());
			}
		}

		if (!"PORTABILIDAD".equalsIgnoreCase(objDatos.getGestion())&& (objDatos.getIdGestion() == null || "".equals(objDatos.getIdGestion()))) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDGESTION_NULO_271, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
		}

		if (objDatos.getNombreArchivo() == null || "".equals(objDatos.getNombreArchivo())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOMBREARCHIVO_NULO_272, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
		}

		if (objDatos.getTipoDocumento() == null || "".equals(objDatos.getTipoDocumento())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPODOCUMENTO_NULO_274, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
		}

		if (objDatos.getAdjunto() == null || "".equals(objDatos.getAdjunto())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_ADJUNTO_NULO_269, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
		}

		if (objDatos.getTipoArchivo() == null || "".equals(objDatos.getTipoArchivo())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPOARCHIVO_NULO_273, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
		}

		if (objDatos.getExtension() == null || "".equals(objDatos.getExtension())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_EXTENSION_NULO_270, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
		}

		if (objRespuesta != null) {
			log.trace("Resultado: " + objRespuesta.getDescripcion());
		}

		return objRespuesta;
	}

	public OutputAdjuntoGestion cargarAdjunto(InputAdjuntoGestion input) {
		String nombreMetodo = "cargarAdjunto";
		Connection conn = null;
		Respuesta objRespuesta = new Respuesta();
		OutputAdjuntoGestion output = new OutputAdjuntoGestion();
		List<LogSidra> listaLog = new ArrayList<LogSidra>();

		try {
			objRespuesta = validarDatos(input);

			if (objRespuesta == null) {
				conn = getConnRegional();
				BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());

				// Convirtiendo en un arreglo de bytes la imagen recibida en
				// base 64
				byte[] base64Decoded = DatatypeConverter.parseBase64Binary(input.getAdjunto());
				

				log.trace("Tamaño en bytes de imagen:" + base64Decoded.length);
				String tamanoArchivo = UtileriasJava.getConfig(conn, "TAMANO_ADJUNTO", "TAMANO_MAXIMO_ADJUNTO", input.getCodArea());
				if (base64Decoded.length < Long.parseLong(tamanoArchivo)) {
					BigDecimal idAdjunto = OperacionAdjuntoGestion.saveAdjunto(conn, base64Decoded, input, ID_PAIS, input.getCodArea());
					if (idAdjunto != null) {
						objRespuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_ADJUNTO_64, null,
								nombreClase, nombreMetodo, null, input.getCodArea());

						output.setIdAdjunto(idAdjunto + "");
						output.setRespuesta(objRespuesta);

						listaLog.add(
								ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioPost,
										idAdjunto + "", Conf.LOG_TIPO_ADJUNTO, "Se registr\u00F3 el adjunto con ID "
												+ idAdjunto + " a la gesti\u00F3n con ID " + input.getIdGestion() + ".",
										""));

					}
				} else {
					objRespuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_IMG_2MB_629, null, nombreClase,
							nombreMetodo, null, input.getCodArea());

					output.setIdAdjunto("");
					output.setRespuesta(objRespuesta);
					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioPost, "0",
							Conf.LOG_TIPO_NINGUNO, "Problema al registrar adjunto de gesti\u00F3n.",
							objRespuesta.getDescripcion()));
				}

			} else {
				listaLog.add(
						ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
								"Problema al registrar adjunto de gesti\u00F3n.", objRespuesta.getDescripcion()));
			}

		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
					"Problema al registrar adjunto de gesti\u00F3n.", e.getMessage()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
					"Problema al registrar adjunto de gesti\u00F3n.", e.getMessage()));
		} finally {
			DbUtils.closeQuietly(conn);
			output.setRespuesta(objRespuesta);

			UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
		}

		return output;
	}

	public OutputAdjuntoGestion getAdjunto(InputAdjuntoGestion input) {
		Respuesta objRespuesta = null;
		OutputAdjuntoGestion output = new OutputAdjuntoGestion();
		InputAdjuntoGestion objAdjunto = new InputAdjuntoGestion();
		String nombreMetodo = "getAdjunto";
		Connection conn = null;
		BigDecimal idAdjunto = null;
		BigDecimal idGestion = null;
		String gestion = null;
		List<LogSidra> listaLog = new ArrayList<LogSidra>();

		try {
			if (input.getIdAdjunto() != null && !"".equals(input.getIdAdjunto())) {
				if (!isNumeric(input.getIdAdjunto())) {
					objRespuesta = new Respuesta();
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INPUT_IDADJUNTO_892, null, nombreClase,
							nombreMetodo, null, input.getCodArea());
				} else {
					idAdjunto = new BigDecimal(input.getIdAdjunto());
				}
			}

			if (input.getIdGestion() != null && !"".equals(input.getIdGestion())) {
				if (!isNumeric(input.getIdGestion())) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDGESTION_NULO_271, null, nombreClase, nombreMetodo,
							null, input.getCodArea());
				} else {
					if (input.getGestion() == null || "".equals(input.getGestion())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_GESTION_NULO_90, null, nombreClase, nombreMetodo,
								null, input.getCodArea());
					} else {
						idGestion = new BigDecimal(input.getIdGestion());
						gestion = input.getGestion();
					}
				}
			}

			if (objRespuesta == null && idAdjunto == null && idGestion == null) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INPUT_IDADJUNTO_892, null, nombreClase, nombreMetodo,
						null, input.getCodArea());
			}

			if (objRespuesta == null) {
				conn = getConnRegional();
				BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());

				objAdjunto = OperacionAdjuntoGestion.getAdjunto(conn, idAdjunto, idGestion, gestion, ID_PAIS);
				if (objAdjunto.getArchivo() == null) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_ADJUNTO_891, null, nombreClase,
							nombreMetodo, null, input.getCodArea());
				} else {
					String adjunto = new sun.misc.BASE64Encoder().encode(objAdjunto.getArchivo());

					// seteando valores para respuesta
					objAdjunto.setAdjunto(adjunto);
					objAdjunto.setArchivo(null);
					output.setAdjunto(objAdjunto);

					objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, input.getCodArea());

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
							Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de la gesti\u00F3n ID " + objAdjunto.getIdGestion()
									+ " de tipo " + input.getGestion() + ".",
							""));
				}
			} else {
				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
						Conf.LOG_TIPO_NINGUNO, "Problema al consultar adjunto de gesti\u00F3n.",
						objRespuesta.getDescripcion()));
			}

		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
					Conf.LOG_TIPO_NINGUNO, "Problema al registrar adjunto de gesti\u00F3n.", e.getMessage()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
					Conf.LOG_TIPO_NINGUNO, "Problema al registrar adjunto de gesti\u00F3n.", e.getMessage()));
		} finally {
			DbUtils.closeQuietly(conn);
			output.setRespuesta(objRespuesta);

			UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
		}

		return output;
	}

	public OutputAdjuntoGestion delAdjunto(InputAdjuntoGestion input) {
		String nombreMetodo = "delAdjunto";
		List<LogSidra> listaLog = new ArrayList<LogSidra>();
		Respuesta objRespuesta = new Respuesta();
		OutputAdjuntoGestion output = new OutputAdjuntoGestion();
		Connection conn = null;

		try {
			conn = getConnRegional();
			BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());

			if (input.getIdAdjunto() == null || "".equals(input.getIdAdjunto())) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INPUT_IDADJUNTO_892, null, nombreClase, nombreMetodo,
						null, input.getCodArea());

				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioDel, "0",
						Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

			} else if (input.getUsuario() == null || "".equals(input.getUsuario())) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null,  input.getCodArea());

				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioDel, "0",
						Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

			} else {
				// se valida que exista la imagen
				List<Filtro> condiciones = new ArrayList<Filtro>();
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Adjunto.CAMPO_TCSCADJUNTOID,
						input.getIdAdjunto()));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Adjunto.CAMPO_TCSCCATPAISID,
						ID_PAIS.toString()));

				String existencia = UtileriasBD.verificarExistencia(conn, Adjunto.N_TABLA, condiciones);
				if (new Integer(existencia) <= 0) {
					log.error("No existe el adjunto.");
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_ADJUNTO_891, null, null, nombreMetodo,
							null, input.getCodArea());

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioDel, input.getIdAdjunto(),
							Conf.LOG_TIPO_ADJUNTO, "No existe el adjunto de gesti\u00F3n.", ""));

					return output;
				}

				boolean update = OperacionAdjuntoGestion.delImagen(conn, new BigDecimal(input.getIdAdjunto()), ID_PAIS);

				if (update) {
					objRespuesta = getMensaje(Conf_Mensajes.OK_ELIMINA_ADJUNTO_79, null, nombreClase, nombreMetodo,
							null, input.getCodArea());

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioDel, input.getIdAdjunto(),
							Conf.LOG_TIPO_ADJUNTO, "Se elimin\u00F3 el adjunto de gesti\u00F3n.", ""));
				} else {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null, nombreClase, nombreMetodo,
							null, input.getCodArea());

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioDel, input.getIdAdjunto(),
							Conf.LOG_TIPO_NINGUNO, "Ocurrio un inconveniente al eliminar adjunto.", ""));
				}
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioDel, "0", Conf.LOG_TIPO_NINGUNO,
					"Problema al eliminar imagen asociada a PDV", e.getMessage()));

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioDel, "0", Conf.LOG_TIPO_NINGUNO,
					"Problema al eliminar imagen asociada a PDV", e.getMessage()));
		} finally {
			DbUtils.closeQuietly(conn);
			output.setRespuesta(objRespuesta);

			UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
		}

		return output;
	}
}
