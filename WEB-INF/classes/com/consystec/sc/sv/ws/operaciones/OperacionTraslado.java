package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.traslado.InputArticuloTraslado;
import com.consystec.sc.ca.ws.input.traslado.InputTraslado;
import com.consystec.sc.ca.ws.input.traslado.RespuestaTraslado;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.traslado.OutputTraslado;
import com.consystec.sc.sv.ws.metodos.CtrlTraslado;
import com.consystec.sc.sv.ws.orm.HistoricoInv;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.TCSCLOGSIDRA;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class OperacionTraslado {
	private OperacionTraslado() {
	}

	private static final Logger log = Logger.getLogger(OperacionTraslado.class);
	private static List<LogSidra> listaLog = new ArrayList<LogSidra>();
	private static String servicioPostTraslado = Conf.LOG_PUT_TRASLADO;

	/**
	 * Funci\u00F3n que arma el query a utilizar al modificar datos en m\u00E9todos
	 * PUT y DELETE.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @param estadoAlta
	 * @param estadoDisponible
	 * @param idTipoTransaccion
	 * @return
	 * @throws SQLException
	 * @throws CloneNotSupportedException
	 */
	public static OutputTraslado doPutDel(Connection conn, InputTraslado input, String estadoAlta,
			String estadoDisponible, BigDecimal idTipoTransaccion, BigDecimal idTraslado, BigDecimal ID_PAIS)
			throws SQLException, CloneNotSupportedException {
		String nombreMetodo = "doPutDel";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta;
		OutputTraslado output = new OutputTraslado();
		boolean insertSeriado = false;
		boolean insertNoSeriado = false;

		String detalleError = "";
		InputTraslado inputSeriado;
		InputTraslado inputNoSeriado;
		List<InputArticuloTraslado> articulosSeriados = new ArrayList<InputArticuloTraslado>();
		List<InputArticuloTraslado> articulosNoSeriados = new ArrayList<InputArticuloTraslado>();
		RespuestaTraslado respTrasladoSeriado = new RespuestaTraslado();
		RespuestaTraslado respTrasladoNoSeriado;

		inputSeriado = (InputTraslado) input.clone();
		inputNoSeriado = (InputTraslado) input.clone();

		for (int i = 0; i < input.getArticulos().size(); i++) {
			if (input.getArticulos().get(i).getSerie() != null && !input.getArticulos().get(i).getSerie().equals("")) {
				articulosSeriados.add(input.getArticulos().get(i));
			} else {
				articulosNoSeriados.add(input.getArticulos().get(i));
			}
		}
		inputSeriado.setArticulos(articulosSeriados);
		inputNoSeriado.setArticulos(articulosNoSeriados);

		if (inputSeriado.getArticulos().size() > 0) {
			// Operacion para mover series
			respTrasladoSeriado = trasladoSeriado(conn, inputSeriado, estadoAlta, estadoDisponible, idTipoTransaccion,
					idTraslado, ID_PAIS);

			if (respTrasladoSeriado.isResultado()) {
				detalleError = respTrasladoSeriado.getDescripcion();
				insertSeriado = respTrasladoSeriado.isResultado();

				if (respTrasladoSeriado.getDatos() != null) {
					output = respTrasladoSeriado.getDatos();
					output.setDescErrorSeries(respTrasladoSeriado.getDatos().getDescErrorSeries());
					output.setSeries(respTrasladoSeriado.getDatos().getSeries());
				}
			} else {
				if (respTrasladoSeriado.getDescripcion().startsWith("ERROR")) {
					respTrasladoSeriado.setDescripcion(respTrasladoSeriado.getDescripcion().replace("ERROR: ", ""));

					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
							nombreMetodo, respTrasladoSeriado.getDescripcion(), input.getCodArea());

					output.setRespuesta(respuesta);
					return output;
				}

				detalleError = respTrasladoSeriado.getDescripcion();
				insertSeriado = respTrasladoSeriado.isResultado();
				if (respTrasladoSeriado.getDatos() != null) {
					output = respTrasladoSeriado.getDatos();
				}
			}
		} else {
			detalleError += new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_ARTICULOS_SERIADOS_108, null,
					nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
		}

		if (inputNoSeriado.getArticulos().size() > 0) {
			// operacion para mover articulos
			respTrasladoNoSeriado = trasladoNoSeriado(conn, inputNoSeriado, estadoAlta, estadoDisponible,
					idTipoTransaccion, idTraslado, ID_PAIS);

			if (respTrasladoNoSeriado.isResultado()) {
				insertNoSeriado = respTrasladoNoSeriado.isResultado();
				detalleError += " " + respTrasladoNoSeriado.getDescripcion();

				output = respTrasladoNoSeriado.getDatos();
				if (respTrasladoSeriado.getDatos() != null) {
					output.setDescErrorSeries(respTrasladoSeriado.getDatos().getDescErrorSeries());
					output.setSeries(respTrasladoSeriado.getDatos().getSeries());
				}
				output.setDescErrorArticulos(respTrasladoNoSeriado.getDatos().getDescErrorArticulos());
				output.setArticulos(respTrasladoNoSeriado.getDatos().getArticulos());
				output.setDescErrorExistencias(respTrasladoNoSeriado.getDatos().getDescErrorExistencias());
				output.setExistencias(respTrasladoNoSeriado.getDatos().getExistencias());
			} else {
				if (respTrasladoNoSeriado.getDescripcion().startsWith("ERROR")) {
					respTrasladoNoSeriado.setDescripcion(respTrasladoNoSeriado.getDescripcion().replace("ERROR: ", ""));

					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
							nombreMetodo, respTrasladoNoSeriado.getDescripcion(), input.getCodArea());

					output.setRespuesta(respuesta);
					return output;
				}

				insertNoSeriado = respTrasladoNoSeriado.isResultado();
				detalleError += " " + respTrasladoNoSeriado.getDescripcion();
				if (respTrasladoSeriado.getDatos() != null) {
					output = respTrasladoNoSeriado.getDatos();
				}
			}
		} else {
			detalleError += " " + new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_ARTICULOS_NO_SERIADOS_109, null,
					nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
		}

		if (insertSeriado == true || insertNoSeriado == true) {
			conn.commit();
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_TRASLADO_67, null, nombreClase,
					nombreMetodo, detalleError, input.getCodArea());

			output.setRespuesta(respuesta);
		} else {
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
					nombreMetodo, "", input.getCodArea());
			respuesta.setDescripcion(detalleError);

			output.setRespuesta(respuesta);
			conn.rollback();
			log.debug("Rollback");
		}

		listaLog = new ArrayList<LogSidra>();
		listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, servicioPostTraslado, "0",
				Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(), output.getRespuesta().getExcepcion()));

		UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

		return output;
	}

	private static RespuestaTraslado trasladoSeriado(Connection conn, InputTraslado input, String estadoAlta,
			String estadoDisponible, BigDecimal idTipoTransaccion, BigDecimal idTraslado, BigDecimal ID_PAIS)
			throws SQLException {
		String nombreMetodo = "trasladoSeriado";
		String nombreClase = new CurrentClassGetter().getClassName();
		RespuestaTraslado respTraslado = new RespuestaTraslado();
		OutputTraslado output = new OutputTraslado();
		boolean insertHijo = false;
		String detalleError = "";

		List<InputArticuloTraslado> updSeries = doUpdSeries(conn, input, estadoAlta, estadoDisponible,
				idTipoTransaccion, idTraslado, ID_PAIS);

		if (updSeries.get(0).getEstado().equalsIgnoreCase("OK")) {
			insertHijo = true;
		} else if (updSeries.get(0).getEstado().equalsIgnoreCase("GOOD")) {
			if (updSeries.size() > 1) {
				updSeries.remove(0);

				detalleError += new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TITULO_TRASLADO_SERIADO_341, null,
						nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();

				output.setDescErrorSeries(
						new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_SERIES_VALIDAS_BODEGA_342, null,
								nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion());
				output.setSeries(updSeries);
			}
			insertHijo = true;
		} else if (updSeries.get(0).getEstado().equalsIgnoreCase("ERROR")) {
			detalleError += new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_SERIES_INVALIDAS_343, null,
					nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
			insertHijo = false;
		} else {
			// Cualquier caso de fallo

			detalleError = "ERROR: " + updSeries.get(0).getEstado();
			insertHijo = false;
		}

		respTraslado.setResultado(insertHijo);
		respTraslado.setDescripcion(detalleError);
		respTraslado.setDatos(output);

		return respTraslado;
	}

	private static List<InputArticuloTraslado> doUpdSeries(Connection conn, InputTraslado input, String estadoAlta,
			String estadoDisponible, BigDecimal idTipoTransaccion, BigDecimal idTraslado, BigDecimal ID_PAIS)
			throws SQLException {
		String nombreMetodo = "doUpdSeries";
		String nombreClase = new CurrentClassGetter().getClassName();
		listaLog = new ArrayList<LogSidra>();
		Statement stmtUpdates = conn.createStatement();
		List<HistoricoInv> listHistorico = new ArrayList<HistoricoInv>();
		int contBatchAdded = 0;
		String sql = "";
		String existencia = "";

		List<Filtro> condicionesExistencia = new ArrayList<Filtro>();
		List<InputArticuloTraslado> listItemSeriesFail = new ArrayList<InputArticuloTraslado>();
		InputArticuloTraslado itemSeriesFail = new InputArticuloTraslado();

		try {
			itemSeriesFail.setEstado("OK");
			listItemSeriesFail.add(itemSeriesFail);

			for (int j = 0; j < input.getArticulos().size(); j++) {
				if (input.getArticulos().get(j).getSerieFinal() != null
						&& !input.getArticulos().get(j).getSerieFinal().equals("")) {
					// operaciones para rangos de series
					String seriesInvalidas = "";
					boolean insertarSeries = false;
					/* VALIDACION DE NIVEL DE BODEGA ORIGEN PARA REALIZAR INSERT/UPDATE de series */
					if ("1".equalsIgnoreCase(input.getNvlBodegaOrigen())) {
						// Valida series para realizar insert
						seriesInvalidas = OperacionMovimientosInventario.validarSeriesLoteInsert(conn,
								input.getArticulos().get(j).getSerie(), input.getArticulos().get(j).getSerieFinal(),
								input.getArticulos().get(j).getIdArticulo(), input.getArticulos().get(j).getNoLote(),
								input.getBodegaOrigen(), 0, estadoDisponible, input.getArticulos().get(j).getTipoInv(),
								input.getCodArea());
						if (seriesInvalidas.equalsIgnoreCase("OK")) {
							insertarSeries = true;
						} else {
							// Valida series lote para realizar update
							seriesInvalidas = OperacionMovimientosInventario.validarSeriesLoteUpdate(conn,
									input.getArticulos().get(j).getSerie(), input.getArticulos().get(j).getSerieFinal(),
									input.getArticulos().get(j).getIdArticulo(),
									input.getArticulos().get(j).getNoLote(), input.getBodegaOrigen(), 0,
									estadoDisponible, input.getArticulos().get(j).getTipoInv(), input.getCodArea());
						}
					} else {
						// Valida series lote para realizar update
						seriesInvalidas = OperacionMovimientosInventario.validarSeries(conn,
								input.getArticulos().get(j).getSerie(), input.getArticulos().get(j).getSerieFinal(),
								input.getBodegaOrigen(), 0, estadoDisponible, input.getArticulos().get(j).getTipoInv(),
								input.getCodArea());
					}

					if (seriesInvalidas.equalsIgnoreCase("OK")) {
						String seriesActualizadas = "";
						if (insertarSeries) {
							// procesos para hacer el insert de las series
							int numCeros = OperacionMovimientosInventario
									.getDiferenciaCeros(input.getArticulos().get(j).getSerie());
							List<Filtro> condiciones = new ArrayList<Filtro>();
							condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
									Inventario.CAMPO_TCSCBODEGAVIRTUALID, input.getBodegaOrigen()));
							condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
									Inventario.CAMPO_ARTICULO, input.getArticulos().get(j).getIdArticulo()));
							condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_NO_LOTE,
									input.getArticulos().get(j).getNoLote()));
							condiciones.add(
									UtileriasJava.setCondicion(Conf.FILTRO_IS_NULL_AND, Inventario.CAMPO_SERIE, ""));
							condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
									Inventario.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));

							String descripcion = UtileriasBD.getOneField(conn, Inventario.CAMPO_DESCRIPCION,
									Inventario.N_TABLA, condiciones, null).get(0);
							String estadoComercialNuevo = UtileriasBD.getOneField(conn,
									Inventario.CAMPO_ESTADO_COMERCIAL, Inventario.N_TABLA, condiciones, null).get(0);
							String tipoGrupo = UtileriasBD.getOneField(conn, Inventario.CAMPO_TIPO_GRUPO,
									Inventario.N_TABLA, condiciones, null).get(0);
							String tipoGrupoSidra = UtileriasBD.getOneField(conn, Inventario.CAMPO_TIPO_GRUPO_SIDRA,
									Inventario.N_TABLA, condiciones, null).get(0);
							String invTelca = UtileriasBD
									.getOneField(conn, Inventario.CAMPO_TIPO_INV, Inventario.N_TABLA, condiciones, null)
									.get(0);
							String codBodega = UtileriasBD.getOneField(conn, Inventario.CAMPO_COD_BODEGA,
									Inventario.N_TABLA, condiciones, null).get(0);

							seriesActualizadas = OperacionMovimientosInventario.insertarSeriesTraslado(conn,
									new BigInteger(input.getArticulos().get(j).getSerie()),
									new BigInteger(input.getArticulos().get(j).getSerieFinal()),
									input.getArticulos().get(j).getIdArticulo(), descripcion, input.getBodegaDestino(),
									input.getUsuario(), numCeros, estadoComercialNuevo, estadoDisponible, tipoGrupo,
									tipoGrupoSidra, invTelca, input.getArticulos().get(j).getNoLote(), codBodega,
									ID_PAIS);
						} else {
							if ("1".equalsIgnoreCase(input.getNvlBodegaOrigen())) {
								// procesos para hacer el update de las series con no_lote
								seriesActualizadas = OperacionMovimientosInventario.actualizarSeriesLoteTraslado(conn,
										new BigInteger(input.getArticulos().get(j).getSerie()),
										new BigInteger(input.getArticulos().get(j).getSerieFinal()),
										input.getBodegaOrigen(), input.getBodegaDestino(),
										input.getArticulos().get(j).getIdArticulo(),
										input.getArticulos().get(j).getNoLote(), input.getUsuario(), estadoDisponible,
										ID_PAIS, input.getCodArea());
							} else {
								// procesos para hacer el update de las series
								seriesActualizadas = OperacionMovimientosInventario.actualizarSeriesTraslado(conn,
										new BigInteger(input.getArticulos().get(j).getSerie()),
										new BigInteger(input.getArticulos().get(j).getSerieFinal()),
										input.getBodegaOrigen(), input.getBodegaDestino(),
										input.getArticulos().get(j).getIdArticulo(), input.getUsuario(),
										estadoDisponible, input.getCodArea(), ID_PAIS);
							}
						}

						if (seriesActualizadas.equalsIgnoreCase("OK")) {
							if (insertarSeries) {
								// Update cantidad lote
								BigInteger cantidadTrasladada = BigInteger.ZERO;
								BigInteger serieFinal = new BigInteger(input.getArticulos().get(j).getSerieFinal());
								BigInteger serieInicial = new BigInteger(input.getArticulos().get(j).getSerie());
								for (BigInteger i = serieInicial; i.compareTo(serieFinal) <= 0; i = i
										.add(BigInteger.ONE)) {
									cantidadTrasladada = cantidadTrasladada.add(BigInteger.ONE);
								}
								OperacionMovimientosInventario.actualizarCantidadLote(conn,
										input.getArticulos().get(j).getIdArticulo(),
										input.getArticulos().get(j).getNoLote(), cantidadTrasladada, serieInicial,
										serieFinal, input.getBodegaOrigen(), input.getUsuario(), input.getCodArea(),
										ID_PAIS);
							}

							// Insert historico
							listHistorico.add(OperacionMovimientosInventario.getInsertHistoricoTraslado(
									input.getBodegaOrigen(), input.getBodegaDestino(), input.getArticulos().get(j),
									idTipoTransaccion, estadoAlta, input.getUsuario(), idTraslado + "",
									input.getCodArea(), ID_PAIS));

							if (!ControladorBase.insertaHistorico2(conn, listHistorico)) {
								listHistorico.clear();
								log.trace("fallo al insertar historico");

								listaLog = new ArrayList<LogSidra>();
								listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, servicioPostTraslado,
										Conf.LOG_TIPO_NINGUNO, "0",
										"Problema al insertar el hist\u00F3rico en traslado.", ""));
								conn.rollback();
								log.error("Rollback");
								listItemSeriesFail.get(0)
										.setEstado(new ControladorBase()
												.getMensaje(Conf_Mensajes.MSJ_ERROR_INSERTAR_HISTORICO_350, null,
														nombreClase, nombreMetodo, null, input.getCodArea())
												.getDescripcion());
							}
							listHistorico.clear();

							listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, servicioPostTraslado,
									"0", Conf.LOG_TIPO_NINGUNO,
									"Se traslad\u00F3 de bodega " + input.getBodegaOrigen() + " a bodega "
											+ input.getBodegaDestino() + " el rango de series "
											+ input.getArticulos().get(j).getSerie() + " - "
											+ input.getArticulos().get(j).getSerieFinal() + " del lote "
											+ input.getArticulos().get(j).getNoLote() + ".",
									""));
							conn.commit();
						} else {
							// rollback porque no fueron actualizadas todas las series
							conn.rollback();
							log.error("Rollback: problema al insertar las series.");
							Respuesta r = new ControladorBase().getMensaje(
									Conf_Mensajes.MSJ_ERROR_UPDATE_INVENTARIO_105, null, null, null,
									"Fallaron las series: " + seriesActualizadas, input.getCodArea());
							listItemSeriesFail.get(0).setEstado(r.getDescripcion());

							listaLog = new ArrayList<LogSidra>();
							listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, servicioPostTraslado,
									"0", Conf.LOG_TIPO_NINGUNO, listItemSeriesFail.get(0).getEstado(), ""));
							UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

							return listItemSeriesFail;
						}

					} else {
						listItemSeriesFail.get(0).setEstado("GOOD");
						itemSeriesFail = new InputArticuloTraslado();
						itemSeriesFail.setSerie(input.getArticulos().get(j).getSerie());
						itemSeriesFail.setSerieFinal(input.getArticulos().get(j).getSerieFinal());
						listItemSeriesFail.add(itemSeriesFail);
					}
				} else {
					// proceso normal de una serie
					condicionesExistencia.clear();
					condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
							Inventario.CAMPO_TCSCBODEGAVIRTUALID, input.getBodegaOrigen()));
					condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
							Inventario.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
					condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_ESTADO,
							estadoDisponible));
					condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND,
							Inventario.CAMPO_TIPO_INV, input.getArticulos().get(j).getTipoInv()));
					condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_SERIE,
							input.getArticulos().get(j).getSerie()));

					existencia = UtileriasBD.getOneRecord(
							conn, Inventario.CAMPO_ARTICULO, ControladorBase.getParticion(Inventario.N_TABLA,
									Conf.PARTITION, input.getBodegaOrigen(), input.getCodArea()),
							condicionesExistencia);
					if (!(existencia == null || "".equals(existencia))) {

						// Update
						String camposUpdate[][] = CtrlTraslado.obtenerCamposPutDel(input);
						sql = UtileriasBD.armarQueryUpdate(Inventario.N_TABLA, camposUpdate, condicionesExistencia);
						stmtUpdates.addBatch(sql);

						// Insert historico
						input.getArticulos().get(j).setIdArticulo(existencia);
						listHistorico
								.add(OperacionMovimientosInventario.getInsertHistoricoTraslado(input.getBodegaOrigen(),
										input.getBodegaDestino(), input.getArticulos().get(j), idTipoTransaccion,
										estadoAlta, input.getUsuario(), idTraslado + "", input.getCodArea(), ID_PAIS));

						if (++contBatchAdded % 500 == 0) {
							stmtUpdates.executeBatch();

							if (!listHistorico.isEmpty()) {
								if (!ControladorBase.insertaHistorico2(conn, listHistorico)) {
									listHistorico.clear();
									log.trace("fallo al insertar historico");

									listaLog = new ArrayList<LogSidra>();
									listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO,
											servicioPostTraslado, Conf.LOG_TIPO_NINGUNO, "0",
											"Problema al insertar el hist\u00F3rico en traslado.", ""));
									conn.rollback();
									log.error("Rollback");
									listItemSeriesFail.get(0)
											.setEstado(new ControladorBase()
													.getMensaje(Conf_Mensajes.MSJ_ERROR_INSERTAR_HISTORICO_350, null,
															nombreClase, nombreMetodo, null, input.getCodArea())
													.getDescripcion());
								}
								listHistorico.clear();
							}
							conn.commit();
							contBatchAdded = 1;
						} else if (contBatchAdded > 0 && contBatchAdded < 500
								&& (j + 1) == input.getArticulos().size()) {
							stmtUpdates.executeBatch();

							if (!listHistorico.isEmpty()) {
								if (!ControladorBase.insertaHistorico2(conn, listHistorico)) {
									listHistorico.clear();
									log.trace("fallo al insertar historico");

									listaLog = new ArrayList<LogSidra>();
									listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO,
											servicioPostTraslado, Conf.LOG_TIPO_NINGUNO, "0",
											"Problema al insertar el hist\u00F3rico en traslado.", ""));
									conn.rollback();
									log.error("Rollback");
									listItemSeriesFail.get(0)
											.setEstado(new ControladorBase()
													.getMensaje(Conf_Mensajes.MSJ_ERROR_INSERTAR_HISTORICO_350, null,
															nombreClase, nombreMetodo, null, input.getCodArea())
													.getDescripcion());
								}
								listHistorico.clear();
							}
							conn.commit();
						}

					} else {
						listItemSeriesFail.get(0).setEstado("GOOD");
						itemSeriesFail = new InputArticuloTraslado();
						itemSeriesFail.setSerie(input.getArticulos().get(j).getSerie());
						listItemSeriesFail.add(itemSeriesFail);
					}
				}
			}

			UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

			// verficar si no se inserto ningun articulo para avisar que se no guarda el
			// traslado
			int cantArticulosConError = (listItemSeriesFail.size() - 1);
			if (cantArticulosConError >= new Integer(input.getArticulos().size())) {
				// no se agrego ninguno
				listItemSeriesFail.clear();
				itemSeriesFail.setEstado("ERROR");
				listItemSeriesFail.add(itemSeriesFail);
				log.debug("Ninguna serie es v\u00E9lida para ingresar.");
			}
		} finally {
			DbUtils.closeQuietly(stmtUpdates);
		}

		return listItemSeriesFail;
	}

	private static RespuestaTraslado trasladoNoSeriado(Connection conn, InputTraslado input, String estadoAlta,
			String estadoDisponible, BigDecimal idTipoTransaccion, BigDecimal idTraslado, BigDecimal ID_PAIS)
			throws SQLException {
		String nombreMetodo = "trasladoNoSeriado";
		String nombreClase = new CurrentClassGetter().getClassName();
		RespuestaTraslado respTraslado = new RespuestaTraslado();
		Respuesta r;
		OutputTraslado output = new OutputTraslado();
		boolean insertHijo = false;
		String detalleError = "";

		OutputTraslado updArticulos = doUpdArticulos(conn, input, estadoAlta, estadoDisponible, idTipoTransaccion,
				idTraslado, ID_PAIS);
		if (updArticulos.getMensaje().equalsIgnoreCase("OK")) {
			// no hubieron errores con todos los articulos
			insertHijo = true;
		} else if (updArticulos.getMensaje().equalsIgnoreCase("GOOD")) {
			// se insertaron algunos
			detalleError += new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TITULO_TRASLADO_NO_SERIADO_344, null,
					nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();

			if (updArticulos.getArticulos().size() > 0) {
				r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TITULO_ARTICULOS_NO_EXISTEN_345, null,
						nombreClase, nombreMetodo, null, input.getCodArea());
				output.setDescErrorArticulos(r.getDescripcion());
				detalleError += r.getDescripcion();
				for (int i = 0; i < updArticulos.getArticulos().size(); i++) {
					detalleError += updArticulos.getArticulos().get(i).getIdArticulo();
					if (i < updArticulos.getArticulos().size() - 1) {
						detalleError += ", ";
					} else {
						detalleError += ". ";
					}
				}
			}

			if (updArticulos.getExistencias().size() > 0) {
				r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TITULO_ARTICULOS_STOCK_346, null, nombreClase,
						nombreMetodo, null, input.getCodArea());
				output.setDescErrorExistencias(r.getDescripcion());
				detalleError += r.getDescripcion();
				for (int i = 0; i < updArticulos.getExistencias().size(); i++) {
					detalleError += updArticulos.getExistencias().get(i).getIdArticulo();
					if (i < updArticulos.getExistencias().size() - 1) {
						detalleError += ", ";
					} else {
						detalleError += ". ";
					}
				}
			}

			output.setArticulos(updArticulos.getArticulos());
			output.setExistencias(updArticulos.getExistencias());

			insertHijo = true;
		} else if (updArticulos.getMensaje().equalsIgnoreCase("ERROR")) {
			// no se inserto nada
			insertHijo = false;
			detalleError += new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_VALIDO_TRASLADO_347, null,
					nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
		} else {
			// cualquier otro caso de fallo
			insertHijo = false;
			detalleError = "ERROR: " + updArticulos.getMensaje();
		}

		respTraslado.setResultado(insertHijo);
		respTraslado.setDescripcion(detalleError);
		respTraslado.setDatos(output);

		return respTraslado;
	}

	private static OutputTraslado doUpdArticulos(Connection conn, InputTraslado input, String estadoAlta,
			String estadoDisponible, BigDecimal idTipoTransaccion, BigDecimal idTraslado, BigDecimal ID_PAIS)
			throws SQLException {
		String nombreMetodo = "doUpdArticulos";
		String nombreClase = new CurrentClassGetter().getClassName();
		listaLog = new ArrayList<LogSidra>();
		int tipoActualizacion = 0;
		String sql = "";
		String existencia = "";

		QueryRunner Qr = new QueryRunner();

		List<Filtro> condicionesExistencia = new ArrayList<Filtro>();
		List<Filtro> condicionesExistenciaDestino = new ArrayList<Filtro>();
		List<InputArticuloTraslado> listItemArticulosFail = new ArrayList<InputArticuloTraslado>();
		InputArticuloTraslado itemArticulosFail = new InputArticuloTraslado();
		List<InputArticuloTraslado> listItemArticulosStock = new ArrayList<InputArticuloTraslado>();
		InputArticuloTraslado itemArticulosStock = new InputArticuloTraslado();
		List<HistoricoInv> listHistorico = new ArrayList<HistoricoInv>();

		OutputTraslado output = new OutputTraslado();
		output.setMensaje("OK");

		itemArticulosStock.setEstado(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_STOCK_348, null,
				nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion());
		listItemArticulosStock.add(itemArticulosStock);

		itemArticulosFail.setEstado(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_NO_EXISTENTE_349,
				null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion());
		listItemArticulosFail.add(itemArticulosFail);

		for (int j = 0; j < input.getArticulos().size(); j++) {
			condicionesExistencia.clear();
			condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO,
					input.getArticulos().get(j).getIdArticulo()));
			condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					Inventario.CAMPO_TCSCBODEGAVIRTUALID, input.getBodegaOrigen()));
			condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					Inventario.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
			condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_TIPO_INV,
					input.getArticulos().get(j).getTipoInv()));
			condicionesExistencia.add(
					UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoDisponible));

			condicionesExistenciaDestino.clear();
			condicionesExistenciaDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					Inventario.CAMPO_ARTICULO, input.getArticulos().get(j).getIdArticulo()));
			condicionesExistenciaDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					Inventario.CAMPO_TCSCBODEGAVIRTUALID, input.getBodegaDestino()));
			condicionesExistenciaDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					Inventario.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
			condicionesExistenciaDestino.add(
					UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoDisponible));
			condicionesExistenciaDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
					Inventario.CAMPO_TIPO_INV, input.getArticulos().get(j).getTipoInv()));

			existencia = UtileriasBD.verificarExistencia(conn, ControladorBase.getParticion(Inventario.N_TABLA,
					Conf.PARTITION, input.getBodegaOrigen(), input.getCodArea()), condicionesExistencia);
			if (new Integer(existencia) > 0) {
				String existenciaArticulos = UtileriasBD
						.getOneRecord(
								conn, Inventario.CAMPO_CANTIDAD, ControladorBase.getParticion(Inventario.N_TABLA,
										Conf.PARTITION, input.getBodegaOrigen(), input.getCodArea()),
								condicionesExistencia);
				if (existenciaArticulos.equals("")) {
					existenciaArticulos = "0";
				}
				;

				// comprobando inventario de bodega destino
				String existenciaArticulosDestino = UtileriasBD.getOneRecord(
						conn, Inventario.CAMPO_CANTIDAD, ControladorBase.getParticion(Inventario.N_TABLA,
								Conf.PARTITION, input.getBodegaDestino(), input.getCodArea()),
						condicionesExistenciaDestino);
				if (existenciaArticulosDestino.equals("")) {
					existenciaArticulosDestino = "0";
				}
				;
				if (new Integer(existenciaArticulosDestino) > 0) {
					// ya existe el articulo disponible en la bodega
					tipoActualizacion = 1;
				} else {
					// no existe, crearlo
					tipoActualizacion = 0;
				}

				// con existencia en la bodega origen y sin existencia en bodega destino
				int cantSolicitada = new Integer(input.getArticulos().get(j).getCantidad());
				if (cantSolicitada > new Integer(existenciaArticulos)) {
					output.setMensaje("GOOD");

					itemArticulosStock = new InputArticuloTraslado();
					itemArticulosStock.setIdArticulo(input.getArticulos().get(j).getIdArticulo());
					listItemArticulosStock.add(itemArticulosStock);

				} else if (cantSolicitada == new Integer(existenciaArticulos)) {
					if (tipoActualizacion == 1) {
						// Actualiza inventario en destino
						String camposUpdateDestino[][] = {
								{ Inventario.CAMPO_CANTIDAD,
										UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
												"(" + Inventario.CAMPO_CANTIDAD + " + "
														+ input.getArticulos().get(j).getCantidad() + ")") },
								{ Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
								{ Inventario.CAMPO_MODIFICADO_POR,
										UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) } };

						sql = UtileriasBD.armarQueryUpdate(
								ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION,
										input.getBodegaDestino(), input.getCodArea()),
								camposUpdateDestino, condicionesExistenciaDestino);

						try {
							Qr.update(conn, sql);
						} catch (SQLException e) {
							log.error("Rollback: " + e.getMessage(), e);
							conn.rollback();
							output.setMensaje(
									new ControladorBase()
											.getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INV_DESTINO_352, null,
													nombreClase, nombreMetodo, null, input.getCodArea())
											.getDescripcion());

							listaLog = new ArrayList<LogSidra>();
							listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, servicioPostTraslado,
									"0", Conf.LOG_TIPO_NINGUNO, output.getMensaje(), e.getMessage()));
							UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

							return output;
						}

						// Elimina el articulo de bodega origen
						sql = UtileriasBD.armarQueryDelete(ControladorBase.getParticion(Inventario.N_TABLA,
								Conf.PARTITION, input.getBodegaOrigen(), input.getCodArea()), condicionesExistencia);
						try {
							Qr.update(conn, sql);
						} catch (SQLException e) {
							log.error("Rollback: " + e.getMessage(), e);
							conn.rollback();
							output.setMensaje(
									new ControladorBase()
											.getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INV_ORIGEN_351, null,
													nombreClase, nombreMetodo, null, input.getCodArea())
											.getDescripcion());

							listaLog = new ArrayList<LogSidra>();
							listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, servicioPostTraslado,
									"0", Conf.LOG_TIPO_NINGUNO, output.getMensaje(), e.getMessage()));
							UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

							return output;
						}
					} else {
						// Actualiza inventario en origen para bodega destino
						String camposUpdate[][] = CtrlTraslado.obtenerCamposPutDel(input);
						sql = UtileriasBD.armarQueryUpdate(
								ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION,
										input.getBodegaOrigen(), input.getCodArea()),
								camposUpdate, condicionesExistencia);

						try {
							Qr.update(conn, sql);
						} catch (SQLException e) {
							log.error("Rollback: " + e.getMessage(), e);
							conn.rollback();
							output.setMensaje(
									new ControladorBase()
											.getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INVENTARIO_105, null,
													nombreClase, nombreMetodo, null, input.getCodArea())
											.getDescripcion());

							listaLog = new ArrayList<LogSidra>();
							listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, servicioPostTraslado,
									"0", Conf.LOG_TIPO_NINGUNO, output.getMensaje(), e.getMessage()));
							UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

							return output;
						}
					}

					// Insert historico
					listHistorico.add(OperacionMovimientosInventario.getInsertHistoricoTraslado(input.getBodegaOrigen(),
							input.getBodegaDestino(), input.getArticulos().get(j), idTipoTransaccion, estadoAlta,
							input.getUsuario(), idTraslado + "", input.getCodArea(), ID_PAIS));

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, servicioPostTraslado, "0",
							Conf.LOG_TIPO_NINGUNO,
							"Se trasladaron " + input.getArticulos().get(j).getCantidad()
									+ " artículos identificados con el ID "
									+ input.getArticulos().get(j).getIdArticulo() + " de la bodega "
									+ input.getBodegaOrigen() + " a la bodega " + input.getBodegaDestino() + ".",
							""));
				} else {
					// Actualiza inventario en origen
					String camposUpdate[][] = {
							{ Inventario.CAMPO_CANTIDAD,
									UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
											"(" + Inventario.CAMPO_CANTIDAD + " - "
													+ input.getArticulos().get(j).getCantidad() + ")") },
							{ Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
							{ Inventario.CAMPO_MODIFICADO_POR,
									UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) } };

					sql = UtileriasBD.armarQueryUpdate(ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION,
							input.getBodegaOrigen(), input.getCodArea()), camposUpdate, condicionesExistencia);

					try {
						Qr.update(conn, sql);
					} catch (SQLException e) {
						log.error("Rollback: " + e.getMessage(), e);
						conn.rollback();
						output.setMensaje(
								new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INV_ORIGEN_351, null,
										nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion());

						listaLog = new ArrayList<LogSidra>();
						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, servicioPostTraslado, "0",
								Conf.LOG_TIPO_NINGUNO, output.getMensaje(), e.getMessage()));
						UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

						return output;
					}

					if (tipoActualizacion == 1) {
						// Actualiza inventario en destino
						String camposUpdateDestino[][] = {
								{ Inventario.CAMPO_CANTIDAD,
										UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
												"(" + Inventario.CAMPO_CANTIDAD + " + "
														+ input.getArticulos().get(j).getCantidad() + ")") },
								{ Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
								{ Inventario.CAMPO_MODIFICADO_POR,
										UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) } };

						sql = UtileriasBD.armarQueryUpdate(
								ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION,
										input.getBodegaDestino(), input.getCodArea()),
								camposUpdateDestino, condicionesExistenciaDestino);

						try {
							Qr.update(conn, sql);
						} catch (SQLException e) {
							log.error("Rollback: " + e.getMessage(), e);
							conn.rollback();
							output.setMensaje(
									new ControladorBase()
											.getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INV_DESTINO_352, null,
													nombreClase, nombreMetodo, null, input.getCodArea())
											.getDescripcion());

							listaLog = new ArrayList<LogSidra>();
							listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, servicioPostTraslado,
									"0", Conf.LOG_TIPO_NINGUNO, output.getMensaje(), e.getMessage()));
							UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

							return output;
						}
					} else {
						// Insert inventario en destino
						sql = "INSERT INTO TC_SC_INVENTARIO (TCSCINVENTARIOINVID, TCSCBODEGAVIRTUALID, ARTICULO, SERIE, "
								+ "DESCRIPCION, CANTIDAD, ESTADO_COMERCIAL, SERIE_ASOCIADA, SERIADO, TIPO_INV, "
								+ "TIPO_GRUPO, TIPO_GRUPO_SIDRA, IDVENDEDOR, ESTADO, CREADO_EL, CREADO_POR, TCSCCATPAISID, COD_BODEGA) "
								+ "(SELECT TC_SC_INVENTARIO_SQ.NEXTVAL, " + input.getBodegaDestino()
								+ ", ARTICULO, SERIE, " + "DESCRIPCION, " + input.getArticulos().get(j).getCantidad()
								+ ", ESTADO_COMERCIAL, SERIE_ASOCIADA, SERIADO, TIPO_INV, "
								+ "TIPO_GRUPO, TIPO_GRUPO_SIDRA, IDVENDEDOR, ESTADO, sysdate, '" + input.getUsuario()
								+ "', " + ID_PAIS + ", COD_BODEGA " + " FROM "
								+ ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION,
										input.getBodegaOrigen(), input.getCodArea())
								+ " WHERE ARTICULO = " + input.getArticulos().get(j).getIdArticulo()
								+ " AND TCSCBODEGAVIRTUALID = " + input.getBodegaOrigen() + " AND UPPER(ESTADO) = '"
								+ estadoDisponible.toUpperCase() + "'" + " AND UPPER(TIPO_INV) = '"
								+ input.getArticulos().get(j).getTipoInv().toUpperCase() + "')";

						log.debug("Insert nuevo de inventario: " + sql);
						try {
							Qr.update(conn, sql);
						} catch (SQLException e) {
							log.error("Rollback: " + e.getMessage(), e);
							conn.rollback();
							output.setMensaje(
									new ControladorBase()
											.getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INVENTARIO_105, null,
													nombreClase, nombreMetodo, null, input.getCodArea())
											.getDescripcion());

							listaLog = new ArrayList<LogSidra>();
							listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, servicioPostTraslado,
									"0", Conf.LOG_TIPO_NINGUNO, output.getMensaje(), e.getMessage()));
							UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

							return output;
						}
					}

					// Insert historico
					listHistorico.add(OperacionMovimientosInventario.getInsertHistoricoTraslado(input.getBodegaOrigen(),
							input.getBodegaDestino(), input.getArticulos().get(j), idTipoTransaccion, estadoAlta,
							input.getUsuario(), idTraslado + "", input.getCodArea(), ID_PAIS));

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, servicioPostTraslado, "0",
							Conf.LOG_TIPO_NINGUNO,
							"Se trasladaron " + input.getArticulos().get(j).getCantidad()
									+ " art\u00EDculos identificados con el ID "
									+ input.getArticulos().get(j).getIdArticulo() + " de la bodega "
									+ input.getBodegaOrigen() + " a la bodega " + input.getBodegaDestino() + ".",
							""));
				}
			} else {
				output.setMensaje("GOOD");

				itemArticulosFail = new InputArticuloTraslado();
				itemArticulosFail.setIdArticulo(input.getArticulos().get(j).getIdArticulo());
				listItemArticulosFail.add(itemArticulosFail);
			}
			;
		}

		ControladorBase.insertaHistorico(conn, listHistorico);
		UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

		// verficar si no se inserto ningun articulo para avisar
		listItemArticulosFail.remove(0);
		listItemArticulosStock.remove(0);
		int cantArticulosConError = (listItemArticulosFail.size() + listItemArticulosStock.size());
		if (cantArticulosConError >= new Integer(input.getArticulos().size())) {
			// no se agrego ninguno
			output.setMensaje("ERROR");
			output.setArticulos(null);
			output.setExistencias(null);
			log.error("Ning\u00FAn art\u00EDculo es v\u00E9lido para crear el traslado.");
		} else {
			// se agrego al menos uno
			output.setArticulos(listItemArticulosFail);
			output.setExistencias(listItemArticulosStock);
		}

		return output;
	}

	public static OutputTraslado doTraspaso(Connection conn, InputTraslado input, String estadoAlta,
			String estadoDisponible, BigDecimal idTipoTransaccion, BigDecimal idTraslado, BigDecimal ID_PAIS)
			throws SQLException {
		String nombreMetodo = "doPutDel";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta respuesta = null;
		OutputTraslado output = new OutputTraslado();
		Statement stmt = null;
		int[] updateCounts;
		String sqlInsert = "";
		String sqlUpdate = "";
		try {

			stmt = conn.createStatement();

			// se consultan y arman los querys de historico y log
			String condiciones = " WHERE " + Inventario.CAMPO_TCSCBODEGAVIRTUALID + " = " + input.getBodegaOrigen()
					+ " AND TCSCCATPAISID = " + ID_PAIS + " AND NUM_TRASPASO_SCL = " + input.getNumTraspaso()
					+ " AND UPPER(" + Inventario.CAMPO_ESTADO + ") = '" + estadoDisponible.toUpperCase() + "'";

			if ("503".equals(input.getCodArea().trim())) {
				condiciones += " AND TIPO_GRUPO_SIDRA NOT IN ('TARJETASRASCA')";
			}
			String insert = inserTrasl(input, estadoAlta, idTipoTransaccion, idTraslado, condiciones, ID_PAIS);
			sqlInsert = insert;
			log.debug("insert historico traspaso: " + sqlInsert);
			stmt.addBatch(sqlInsert);

			insert = insTraslado2(input, condiciones, ID_PAIS);

			sqlInsert = insert;
			log.debug("insert log traspaso: " + sqlInsert);
			stmt.addBatch(sqlInsert);

			StringBuilder update = new StringBuilder();

			update.append(
					"UPDATE" + ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, "", input.getCodArea())
							+ " SET ");
			update.append(Inventario.CAMPO_TCSCBODEGAVIRTUALID + " = " + input.getBodegaDestino() + ", ");
			update.append(Inventario.CAMPO_MODIFICADO_POR + " = '" + input.getUsuario() + "', ");
			update.append(Inventario.CAMPO_MODIFICADO_EL + " = SYSDATE");
			update.append(condiciones);

			sqlUpdate = update.toString();
			log.debug("update traspaso: " + sqlUpdate);
			stmt.addBatch(sqlUpdate);
			updateCounts = stmt.executeBatch();

			if (updateCounts[0] == updateCounts[1] && updateCounts[0] == updateCounts[2]) {
				conn.commit();
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_TRASLADO_67, null, nombreClase,
						nombreMetodo, null, input.getCodArea());
				output.setRespuesta(respuesta);
			} else {
				conn.rollback();
				log.debug("Rollback: Uno de los updates/inserts devolvió un count diferente al deseado.");

				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
						nombreMetodo, "", input.getCodArea());
				output.setRespuesta(respuesta);
			}

			listaLog = new ArrayList<LogSidra>();
			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, servicioPostTraslado,
					input.getNumTraspaso(), Conf.LOG_TIPO_TRASPASO, output.getRespuesta().getDescripcion(),
					output.getRespuesta().getExcepcion()));

		} finally {
			DbUtils.closeQuietly(stmt);
			UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
		}

		return output;
	}

	public static String insTraslado2(InputTraslado input, String condiciones, BigDecimal ID_PAIS) {
		String insert = "";
		String camposLog[] = { TCSCLOGSIDRA.CAMPO_TCSCLOGSIDRAID, TCSCLOGSIDRA.CAMPO_TCSCCATPAISID,
				TCSCLOGSIDRA.CAMPO_TIPO_TRANSACCION, TCSCLOGSIDRA.CAMPO_ORIGEN, TCSCLOGSIDRA.CAMPO_ID,
				TCSCLOGSIDRA.CAMPO_TIPO_ID, TCSCLOGSIDRA.CAMPO_DESCRIPCION, TCSCLOGSIDRA.CAMPO_RESULTADO,
				TCSCLOGSIDRA.CAMPO_FECHA_LOG, TCSCLOGSIDRA.CAMPO_USUARIO };

		insert = "INSERT INTO " + TCSCLOGSIDRA.N_TABLA;
		insert += " (" + UtileriasBD.getCampos(camposLog) + ") " + "(SELECT "
				+ UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, TCSCLOGSIDRA.SEQUENCE, Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, ID_PAIS.toString(), Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, Conf.LOG_TRANSACCION_TRASLADO, Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, servicioPostTraslado, Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getNumTraspaso(), Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, Conf.LOG_TIPO_TRASPASO, Conf.INSERT_SEPARADOR_SI)
				+ "C.VALOR, "
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO,
						"Se trasladó la serie ' || serie || ' de bodega " + input.getBodegaOrigen() + " a bodega "
								+ input.getBodegaDestino() + " por número de traspaso " + input.getNumTraspaso() + ".",
						Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
				+ " FROM TC_SC_INVENTARIO, (SELECT VALOR FROM TC_SC_CONFIGURACION WHERE TCSCCATPAISID = " + ID_PAIS
				+ " AND UPPER(NOMBRE) = '" + Conf.LOG_TRANSACCION_TRASLADO + "' AND GRUPO = 'TRANSACCIONES_LOG') C "
				+ condiciones + ")";
		return insert;
	}

	public static String inserTrasl(InputTraslado input, String estadoAlta, BigDecimal idTipoTransaccion,
			BigDecimal idTraslado, String condiciones, BigDecimal ID_PAIS) {
		String camposHistorico[] = { HistoricoInv.CAMPO_TCSCLOGINVSIDRAID, HistoricoInv.CAMPO_TCSCCATPAISID,
				HistoricoInv.CAMPO_TCSCTIPOTRANSACCIONID, HistoricoInv.CAMPO_BODEGA_ORIGEN,
				HistoricoInv.CAMPO_BODEGA_DESTINO, HistoricoInv.CAMPO_ARTICULO, HistoricoInv.CAMPO_CANTIDAD,
				HistoricoInv.CAMPO_SERIE, HistoricoInv.CAMPO_SERIE_ASOCIADA, HistoricoInv.CAMPO_ESTADO,
				HistoricoInv.CAMPO_TIPO_INV, HistoricoInv.CAMPO_TCSCTRASLADOID, HistoricoInv.CAMPO_CREADO_POR,
				HistoricoInv.CAMPO_CREADO_EL

		};

		// insert histórico por serie
		String insert = "INSERT INTO " + HistoricoInv.N_TABLA;
		insert += " (" + UtileriasBD.getCampos(camposHistorico) + ") " + "(SELECT " + HistoricoInv.SEQUENCE + ", "
				+ ID_PAIS + ", " + idTipoTransaccion + ", " + input.getBodegaOrigen() + ", " + input.getBodegaDestino()
				+ ", " + Inventario.CAMPO_ARTICULO + ", " + Inventario.CAMPO_CANTIDAD + ", " + Inventario.CAMPO_SERIE
				+ ", " + Inventario.CAMPO_SERIE_ASOCIADA + ", " + "'" + estadoAlta + "', " + Inventario.CAMPO_TIPO_INV
				+ ", " + idTraslado + "," + "'" + input.getUsuario() + "', " + "SYSDATE FROM TC_SC_INVENTARIO"
				+ condiciones + ")";
		return insert;
	}
}
