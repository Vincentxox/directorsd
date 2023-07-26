package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.condicionoferta.InputCondicionOferta;
import com.consystec.sc.ca.ws.input.condicionoferta.InputCondicionPrincipalOferta;
import com.consystec.sc.ca.ws.input.condicionoferta.InputDetCondicionOferta;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.condicionoferta.OutputCondicionOferta;
import com.consystec.sc.sv.ws.metodos.CtrlCondicionOferta;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.Condicion;
import com.consystec.sc.sv.ws.orm.CondicionOferta;
import com.consystec.sc.sv.ws.orm.OfertaCampania;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes - Consystec - 2016
 *
 */
public class OperacionCondicionOferta {
	private OperacionCondicionOferta() {
	}

	private static final Logger log = Logger.getLogger(OperacionCondicionOferta.class);

	/**
	 * Funcion que arma el query a utilizar al consultar en m\u00E9todo GET.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @param esMasivoPDV
	 * @param esMasivoZona
	 * @return OutputCondicion
	 * @throws SQLException
	 */
	public static OutputCondicionOferta doGet(Connection conn, InputCondicionPrincipalOferta input, int metodo,
			boolean esMasivoPDV, boolean esMasivoZona, BigDecimal idPais) throws SQLException {
		String nombreMetodo = "doGet";
		String nombreClase = new CurrentClassGetter().getClassName();
		String oferta = UtileriasJava.getConfig(conn, Conf.GRUPO_OFERTA_COMERCIAL, Conf.TIPO_OFERTA,
				input.getCodArea());
		String invTelca = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPOINV, Conf.SOL_TIPOINV_TELCA,
				input.getCodArea());

		List<InputCondicionOferta> list = new ArrayList<InputCondicionOferta>();

		Respuesta respuesta = null;
		OutputCondicionOferta output = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		try {
			String tablas[] = { Condicion.N_TABLA,
					ControladorBase.getParticion(OfertaCampania.N_TABLA, Conf.PARTITION, "", input.getCodArea()) };
			String[][] campos = CtrlCondicionOferta.obtenerCamposGet();

			List<Filtro> condiciones = new ArrayList<Filtro>();
			if (esMasivoPDV) {
				List<Filtro> condicionesExtra = new ArrayList<Filtro>();
				String[] camposExtra = { CondicionOferta.CAMPO_TCSCCONDICIONID };

				condicionesExtra
						.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, CondicionOferta.CAMPO_TCSCPUNTOVENTAID,
								"SELECT TCSCPUNTOVENTAID FROM TC_SC_RUTA_PDV WHERE TCSCRUTAID = " + input.getIdRuta()
										+ " AND TCSCCATPAISID = " + idPais));

				String selectSQL = UtileriasBD.armarQuerySelect(CondicionOferta.N_TABLA, camposExtra, condicionesExtra);
				condiciones.add(
						UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Condicion.CAMPO_TCSCCONDICIONID, selectSQL));

			} else if (esMasivoZona) {
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Condicion.CAMPO_TCSCCONDICIONID,
						"WITH DATOS AS (SELECT DISTINCT CATEGORIA, TCSCZONACOMERCIALID FROM TC_SC_PUNTOVENTA WHERE TCSCPUNTOVENTAID IN ("
								+ "SELECT TCSCPUNTOVENTAID FROM TC_SC_RUTA_PDV WHERE TCSCRUTAID = " + input.getIdRuta()
								+ " AND TCSCCATPAISID = " + idPais + ")) "
								+ "SELECT TCSCCONDICIONID FROM TC_SC_DET_CONDICION_OFERTA C, DATOS D WHERE C.CATEGORIA = D.CATEGORIA "
								+ "AND C.ZONACOMERCIAL = D.TCSCZONACOMERCIALID AND TCSCCATPAISID = " + idPais));

			} else {
				condiciones = CtrlCondicionOferta.obtenerCondiciones(input, metodo, idPais);
			}

			condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampania.N_TABLA,
					OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, Condicion.N_TABLA,
					Condicion.CAMPO_TCSCOFERTACAMPANIAID));
			condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Condicion.N_TABLA,
					Condicion.CAMPO_TIPO_OFERTACAMPANIA, oferta));
			condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Condicion.N_TABLA,
					Condicion.CAMPO_TCSCCATPAISID, idPais.toString()));

			List<Order> orden = new ArrayList<Order>();
			orden.add(new Order(Condicion.N_TABLA + "." + Condicion.CAMPO_NOMBRE, Order.ASC));

			String sql = UtileriasBD.armarQueryGetMultiple(campos, tablas, condiciones, orden);
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst != null) {
				if (!rst.next()) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_CONDICIONES_OFERTA_396,
							null, nombreClase, nombreMetodo, null, input.getCodArea());

					output = new OutputCondicionOferta();
					output.setRespuesta(respuesta);
				} else {
					respuesta = new Respuesta();
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					String idCondicion = "";
					do {
						InputCondicionOferta item = new InputCondicionOferta();
						idCondicion = rst.getString(Condicion.CAMPO_TCSCCONDICIONID);
						item.setIdCondicion(idCondicion);
						item.setTipoGestion(rst.getString(Condicion.CAMPO_TIPO_GESTION));
						item.setTipoCondicion(rst.getString(Condicion.CAMPO_TIPO_CONDICION));
						item.setNombre(rst.getString(Condicion.CAMPO_NOMBRE));
						item.setIdOfertaCampania(rst.getString(Condicion.CAMPO_TCSCOFERTACAMPANIAID));
						item.setNombreCampania(rst.getString("NOMBRECAMPANIA"));
						item.setEstado(rst.getString(Condicion.CAMPO_ESTADO));
						item.setCreadoEl(UtileriasJava.formatStringDate(rst.getString(Condicion.CAMPO_CREADO_EL)));
						item.setCreadoPor(rst.getString(Condicion.CAMPO_CREADO_POR));
						item.setModificadoEl(
								UtileriasJava.formatStringDate(rst.getString(Condicion.CAMPO_MODIFICADO_EL)));
						item.setModificadoPor(rst.getString(Condicion.CAMPO_MODIFICADO_POR));

						List<InputDetCondicionOferta> detalles = getDatosTablaHija(conn, idCondicion, invTelca, idPais);
						item.setDetalle(detalles);

						list.add(item);
					} while (rst.next());

					output = new OutputCondicionOferta();
					output.setRespuesta(respuesta);
					output.setCondiciones(list);
				}
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return output;
	}

	/**
	 * Funcion que obtiene los datos relacionados de la tabla hija mediante el id de
	 * la tabla padre.
	 * 
	 * @param conn
	 * @param idPadre
	 * @param input
	 * @param invTelca
	 * @return OutputBodegaDTS
	 * @throws SQLException
	 */
	private static List<InputDetCondicionOferta> getDatosTablaHija(Connection conn, String idPadre, String invTelca,
			BigDecimal idPais) throws SQLException {
		List<InputDetCondicionOferta> list = new ArrayList<InputDetCondicionOferta>();
		PreparedStatement pstmtIn = null;
		ResultSet rstIn = null;
		String idArticulo = "";
		String tipoInv = "";
		try {
			String camposInterno[] = CtrlCondicionOferta.obtenerCamposTablaHijaGet(idPais);

			List<Filtro> condicionesInterno = new ArrayList<Filtro>();
			condicionesInterno.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					CondicionOferta.CAMPO_TCSCCONDICIONID, idPadre));

			String sql = UtileriasBD.armarQuerySelect(CondicionOferta.N_TABLA + " B", camposInterno, condicionesInterno,
					null);
			pstmtIn = conn.prepareStatement(sql);
			rstIn = pstmtIn.executeQuery();

			if (rstIn != null) {
				if (!rstIn.next()) {
					log.debug("No existen registros en la tabla hija con esos parametros.");
					InputDetCondicionOferta item = new InputDetCondicionOferta();
					list.add(item);
				} else {
					do {
						InputDetCondicionOferta item = new InputDetCondicionOferta();
						item.setIdCondicion(UtileriasJava.getRstValue(rstIn, Conf.TIPO_NUMERICO,
								CondicionOferta.CAMPO_TCSCCONDICIONID));
						item.setTipo(
								UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO, CondicionOferta.CAMPO_TIPO_OFERTA));
						idArticulo = UtileriasJava.getRstValue(rstIn, Conf.TIPO_NUMERICO,
								CondicionOferta.CAMPO_ARTICULO);
						item.setIdArticulo(idArticulo);
						item.setTecnologia(
								UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO, CondicionOferta.CAMPO_TECNOLOGIA));
						item.setNombreArticulo(UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO, "NOMBREART"));
						if (!idArticulo.equals("")) {
							tipoInv = invTelca;
						} else {
							tipoInv = "";
						}
						item.setTipoInv(tipoInv);
						item.setTipoCliente(
								UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO, CondicionOferta.CAMPO_TIPO_CLIENTE));
						item.setMontoInicial(UtileriasJava.getRstValue(rstIn, Conf.TIPO_NUMERICO,
								CondicionOferta.CAMPO_MONTO_INICIAL));
						item.setMontoFinal(UtileriasJava.getRstValue(rstIn, Conf.TIPO_NUMERICO,
								CondicionOferta.CAMPO_MONTO_FINAL));
						item.setTipoDescuento(UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO,
								CondicionOferta.CAMPO_TIPO_DESCUENTO));
						item.setValorDescuento(UtileriasJava.getRstValue(rstIn, Conf.TIPO_NUMERICO,
								CondicionOferta.CAMPO_VALOR_DESCUENTO));
						item.setIdPDV(UtileriasJava.getRstValue(rstIn, Conf.TIPO_NUMERICO,
								CondicionOferta.CAMPO_TCSCPUNTOVENTAID));
						item.setNombrePDV(UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO, "NOMBREPDV"));
						item.setZonaComercialPDV(
								UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO, CondicionOferta.CAMPO_ZONACOMERCIAL));
						item.setCategoriaPDV(
								UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO, CondicionOferta.CAMPO_CATEGORIA));
						item.setIdArticuloRegalo(UtileriasJava.getRstValue(rstIn, Conf.TIPO_NUMERICO,
								CondicionOferta.CAMPO_ARTICULO_REGALO));
						item.setNombreArticuloRegalo(
								UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO, "NOMBREART_REGALO"));
						item.setCantidadArticuloRegalo(UtileriasJava.getRstValue(rstIn, Conf.TIPO_NUMERICO,
								CondicionOferta.CAMPO_CANT_ARTICULO_REGALO));
						item.setTipoDescuentoRegalo(UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO,
								CondicionOferta.CAMPO_TIPO_DESC_REGALO));
						item.setValorDescuentoRegalo(UtileriasJava.getRstValue(rstIn, Conf.TIPO_NUMERICO,
								CondicionOferta.CAMPO_VALOR_DESC_REGALO));
						item.setEstado(UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO, CondicionOferta.CAMPO_ESTADO));
						item.setCreadoEl(
								UtileriasJava.getRstValue(rstIn, Conf.TIPO_FECHA, CondicionOferta.CAMPO_CREADO_EL));
						item.setCreadoPor(
								UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO, CondicionOferta.CAMPO_CREADO_POR));
						item.setModificadoEl(
								UtileriasJava.getRstValue(rstIn, Conf.TIPO_FECHA, CondicionOferta.CAMPO_MODIFICADO_EL));
						item.setModificadoPor(UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO,
								CondicionOferta.CAMPO_MODIFICADO_POR));

						list.add(item);
					} while (rstIn.next());
				}
			}
		} finally {
			DbUtils.closeQuietly(rstIn);
			DbUtils.closeQuietly(pstmtIn);
		}
		return list;
	}

	/**
	 * Funcion que arma el query a utilizar al trabajar en metodo POST.
	 * 
	 * @param conn
	 * @param input
	 * @param estadoAlta
	 * @param tipoOferta
	 * @return OutputCondicion
	 * @throws SQLException
	 */
	public static OutputCondicionOferta doPost(Connection conn, InputCondicionPrincipalOferta input, String tipoOferta,
			String estadoAlta, BigDecimal idPais) throws SQLException {
		String nombreMetodo = "doPost";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta = null;
		OutputCondicionOferta output = new OutputCondicionOferta();
		int idPadre = 0;
		boolean insertHijo = false;
		boolean insertAll = false;

		List<Filtro> condiciones = new ArrayList<Filtro>();
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
				OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, input.getIdOfertaCampania()));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampania.CAMPO_TCSCCATPAISID,
				idPais.toString()));
		int existencias = UtileriasBD.selectCount(conn,
				ControladorBase.getParticion(OfertaCampania.N_TABLA, Conf.PARTITION, "", input.getCodArea()),
				condiciones);
		if (existencias < 1) {
			log.error("No existe la campa\u00F1a.");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_CAMPANIA_242, null,
					nombreClase, nombreMetodo, null, input.getCodArea());

			output = new OutputCondicionOferta();
			output.setRespuesta(respuesta);

			return output;
		}

		try {
			conn.setAutoCommit(false);
			String generatedColumns[] = { Condicion.CAMPO_TCSCCONDICIONID };

			String sql = null;
			String campos[] = CtrlCondicionOferta.obtenerCamposPost();

			List<String> inserts = new ArrayList<String>();

			// Se verifica si el idOfertaCampania pertenece a oferta y esta de alta
			condiciones.clear();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, input.getIdOfertaCampania()));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampania.CAMPO_TCSCCATPAISID,
					idPais.toString()));
			String camposCampania[] = { OfertaCampania.CAMPO_TIPO, OfertaCampania.CAMPO_ESTADO };

			List<Map<String, String>> datosCampania = UtileriasBD.getSingleData(conn,
					ControladorBase.getParticion(OfertaCampania.N_TABLA, Conf.PARTITION, "", input.getCodArea()),
					camposCampania, condiciones, null);
			String tipoOfertaCampania = datosCampania.get(0).get(OfertaCampania.CAMPO_TIPO);
			if (!tipoOfertaCampania.equalsIgnoreCase(tipoOferta)) {
				// no es tipo oferta
				log.error("El ID no corresponde a una oferta.");
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_OFERTA_TIPO_250, null,
						nombreClase, nombreMetodo, null, input.getCodArea());

				output = new OutputCondicionOferta();
				output.setRespuesta(respuesta);

				return output;

			} else if (!datosCampania.get(0).get(OfertaCampania.CAMPO_ESTADO).equalsIgnoreCase(estadoAlta)) {
				// es tipo oferta pero no esta de alta
				log.error("El ID corresponde a una oferta pero no esta de alta.");
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_OFERTACAMPANIA_BAJA_249, null,
						nombreClase, nombreMetodo, null, input.getCodArea());

				output = new OutputCondicionOferta();
				output.setRespuesta(respuesta);

				return output;
			}

			for (int i = 0; i < input.getCondiciones().size(); i++) {
				// Se verifica si ya existe una condicion con ese nombre
				condiciones.clear();
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Condicion.CAMPO_NOMBRE,
						input.getCondiciones().get(i).getNombre()));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						Condicion.CAMPO_TCSCOFERTACAMPANIAID, input.getIdOfertaCampania()));
				condiciones.add(
						UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Condicion.CAMPO_ESTADO, estadoAlta));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Condicion.CAMPO_TCSCCATPAISID,
						idPais.toString()));

				existencias = UtileriasBD.selectCount(conn, Condicion.N_TABLA, condiciones);
				if (existencias > 0) {
					log.error("Ya existe el nombre de condicion.");
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_EXISTE_CONDICION_412, null,
							nombreClase, nombreMetodo, "En la condici\u00F3n " + (i + 1) + ".", input.getCodArea());

					output = new OutputCondicionOferta();
					output.setRespuesta(respuesta);

					return output;
				}

				// Se verifica si el tipo gestion es v\u00E9lido
				condiciones.clear();
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO,
						Conf.GRUPO_TIPO_GESTION_VENTA));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_NOMBRE,
						input.getCondiciones().get(i).getTipoGestion()));
				condiciones.add(
						UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,
						idPais.toString()));

				existencias = UtileriasBD.selectCount(conn, Catalogo.N_TABLA, condiciones);
				if (existencias < 1) {
					log.error("No existe el tipo de gesti\u00F3n.");
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TIPO_GESTION_NO_DEFINIDA_415, null,
							nombreClase, nombreMetodo, "En la condici\u00F3n " + (i + 1) + ".", input.getCodArea());

					output = new OutputCondicionOferta();
					output.setRespuesta(respuesta);

					return output;
				}

				// Se verifica si el tipo de condici\u00F3n es valido
				condiciones.clear();
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO,
						Conf.GRUPO_CONDICION_TIPO));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_NOMBRE,
						input.getCondiciones().get(i).getTipoCondicion()));
				condiciones.add(
						UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,
						idPais.toString()));

				existencias = UtileriasBD.selectCount(conn, Catalogo.N_TABLA, condiciones);
				if (existencias < 1) {
					log.error("No existe el tipo de condici\u00F3n.");
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TIPO_CONDICION_NO_DEFINIDA_416, null,
							nombreClase, nombreMetodo, "En la condici\u00F3n " + (i + 1) + ".", input.getCodArea());

					output = new OutputCondicionOferta();
					output.setRespuesta(respuesta);

					return output;
				}

				// Insert header
				inserts.clear();
				String valores = " ("
						+ UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, Condicion.SEQUENCE, Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPais.toString(), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdOfertaCampania(),
								Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getCondiciones().get(i).getNombre(),
								Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER,
								input.getCondiciones().get(i).getTipoGestion(), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER,
								input.getCondiciones().get(i).getTipoCondicion(), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, tipoOfertaCampania, Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
						+ ")";
				inserts.add(valores);

				sql = UtileriasBD.armarQueryInsert(Condicion.N_TABLA, campos, inserts);

				try (PreparedStatement pstmt = conn.prepareStatement(sql, generatedColumns)) {
					pstmt.executeUpdate();
					try (ResultSet rs = pstmt.getGeneratedKeys()) {
						if (rs.next()) {
							idPadre = rs.getInt(1);
						}
					}
				}

				log.debug("idPadre: " + idPadre);
				if (idPadre != 0) {
					// Inserts detail
					insertHijo = doPostHijo(conn, idPadre, input.getCondiciones().get(i), estadoAlta,
							input.getUsuario(), idPais);

					if (insertHijo) {
						insertAll = true;
					} else {
						insertAll = false;
						respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null,
								nombreClase, nombreMetodo, "Problema al insertar un detalle.", input.getCodArea());

						output.setRespuesta(respuesta);

						return output;
					}
				}
			}

			if (insertAll) {
				conn.commit();
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_CONDICION_OFERTA_36, null,
						nombreClase, nombreMetodo, null, input.getCodArea());

				output.setRespuesta(respuesta);
				output.setIdCondicion(idPadre + "");
			} else {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
						nombreMetodo, null, input.getCodArea());

				output.setRespuesta(respuesta);
			}
		} finally {
			if (!insertAll) {
				log.error("Rollback");
				conn.rollback();
			}

			conn.setAutoCommit(true);
		}

		return output;
	}

	/**
	 * Funcion de realiza las inserciones en la tabla relacionada.
	 * 
	 * @param conn
	 * @param idPadre
	 * @param inputCondicion
	 * @param estadoAlta
	 * @param usuario
	 * @return boolean
	 * @throws SQLException
	 */
	private static boolean doPostHijo(Connection conn, int idPadre, InputCondicionOferta inputCondicion,
			String estadoAlta, String usuario, BigDecimal idPais) throws SQLException {
		String campos[] = CtrlCondicionOferta.obtenerCamposTablaHijaPost();
		List<String> inserts = new ArrayList<String>();
		QueryRunner Qr = new QueryRunner();
		String sql = null;

		for (int j = 0; j < inputCondicion.getDetalle().size(); j++) {
			String valores = null;
			String tipo = null;
			String insertIdArticulo = null;
			String idArticulo = null;
			String insertMontoInicial = null;
			String montoInicial = null;
			String insertMontoFinal = null;
			String montoFinal = null;
			String idPDV = "";
			String zonaPDV = "";
			String categoriaPDV = "";
			String tecnologia = "";
			String tipoCliente = "";
			String tipoDescuento = "";
			String valorDescuento = "";
			String idArtRegalo = "";
			String cantArtRegalo = "";
			String tipoDescuentoRegalo = "";
			String valorDescuentoRegalo = "";
			String id_descuento = "";

			tipo = inputCondicion.getDetalle().get(j).getTipo();
			if (tipo == null || tipo.equals("")) {
				tipo = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
			} else {
				tipo = UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, inputCondicion.getDetalle().get(j).getTipo(),
						Conf.INSERT_SEPARADOR_SI);
			}

			idArticulo = inputCondicion.getDetalle().get(j).getIdArticulo();
			if (idArticulo == null || idArticulo.equals("")) {
				insertIdArticulo = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
			} else {
				insertIdArticulo = UtileriasJava.setInsert(Conf.INSERT_NUMERO, idArticulo, Conf.INSERT_SEPARADOR_SI);
			}

			montoInicial = inputCondicion.getDetalle().get(j).getMontoInicial();
			if (montoInicial == null || montoInicial.equals("")) {
				insertMontoInicial = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
			} else {
				insertMontoInicial = UtileriasJava.setInsert(Conf.INSERT_NUMERO, montoInicial,
						Conf.INSERT_SEPARADOR_SI);
			}

			montoFinal = inputCondicion.getDetalle().get(j).getMontoFinal();
			if (montoFinal == null || montoFinal.equals("")) {
				insertMontoFinal = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
			} else {
				insertMontoFinal = UtileriasJava.setInsert(Conf.INSERT_NUMERO, montoFinal, Conf.INSERT_SEPARADOR_SI);
			}

			idPDV = inputCondicion.getDetalle().get(j).getIdPDV();
			if (idPDV == null || idPDV.equals("")) {
				idPDV = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
			} else {
				idPDV = UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPDV, Conf.INSERT_SEPARADOR_SI);
			}

			zonaPDV = inputCondicion.getDetalle().get(j).getZonaComercialPDV();
			if (zonaPDV == null || zonaPDV.equals("")) {
				zonaPDV = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
			} else {
				zonaPDV = UtileriasJava.setInsert(Conf.INSERT_TEXTO, zonaPDV, Conf.INSERT_SEPARADOR_SI);
			}

			categoriaPDV = inputCondicion.getDetalle().get(j).getCategoriaPDV();
			if (categoriaPDV == null || categoriaPDV.equals("")) {
				categoriaPDV = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
			} else {
				categoriaPDV = UtileriasJava.setInsert(Conf.INSERT_TEXTO, categoriaPDV, Conf.INSERT_SEPARADOR_SI);
			}

			tecnologia = inputCondicion.getDetalle().get(j).getTecnologia();
			if (tecnologia == null || tecnologia.equals("")) {
				tecnologia = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
			} else {
				tecnologia = UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, tecnologia, Conf.INSERT_SEPARADOR_SI);
			}

			tipoCliente = inputCondicion.getDetalle().get(j).getTipoCliente();
			if (tipoCliente == null || tipoCliente.equals("")) {
				tipoCliente = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
			} else {
				tipoCliente = UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, tipoCliente, Conf.INSERT_SEPARADOR_SI);
			}

			tipoDescuento = inputCondicion.getDetalle().get(j).getTipoDescuento();
			if (tipoDescuento == null || tipoDescuento.equals("")) {
				tipoDescuento = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
			} else {
				tipoDescuento = UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, tipoDescuento,
						Conf.INSERT_SEPARADOR_SI);
			}

			valorDescuento = inputCondicion.getDetalle().get(j).getValorDescuento();
			if (valorDescuento == null || valorDescuento.equals("")) {
				valorDescuento = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
			} else {
				valorDescuento = UtileriasJava.setInsert(Conf.INSERT_NUMERO, valorDescuento, Conf.INSERT_SEPARADOR_SI);
			}

			idArtRegalo = inputCondicion.getDetalle().get(j).getIdArticuloRegalo();
			if (idArtRegalo == null || idArtRegalo.equals("")) {
				idArtRegalo = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
				tipoDescuentoRegalo = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
				valorDescuentoRegalo = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
			} else {
				idArtRegalo = UtileriasJava.setInsert(Conf.INSERT_NUMERO, idArtRegalo, Conf.INSERT_SEPARADOR_SI);
				tipoDescuentoRegalo = UtileriasJava.setInsert(Conf.INSERT_TEXTO, "PORCENTAJE",
						Conf.INSERT_SEPARADOR_SI);
				valorDescuentoRegalo = UtileriasJava.setInsert(Conf.INSERT_NUMERO, "100", Conf.INSERT_SEPARADOR_SI);
			}

			cantArtRegalo = inputCondicion.getDetalle().get(j).getCantidadArticuloRegalo();
			if (cantArtRegalo == null || cantArtRegalo.equals("")) {
				cantArtRegalo = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
			} else {
				cantArtRegalo = UtileriasJava.setInsert(Conf.INSERT_NUMERO, cantArtRegalo, Conf.INSERT_SEPARADOR_SI);
			}
			id_descuento = inputCondicion.getDetalle().get(j).getIdDescuento();
			if (id_descuento == null || id_descuento.equals("")) {
				id_descuento = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_NO);
			} else {
				id_descuento = UtileriasJava.setInsert(Conf.INSERT_NUMERO, id_descuento, Conf.INSERT_SEPARADOR_NO);
			}

			valores = UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, CondicionOferta.SEQUENCE, Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPais + "", Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPadre + "", Conf.INSERT_SEPARADOR_SI) + tipo
					+ insertIdArticulo + tipoCliente + tecnologia + insertMontoInicial + insertMontoFinal
					+ tipoDescuento + valorDescuento + idPDV + zonaPDV + categoriaPDV + idArtRegalo + cantArtRegalo
					+ tipoDescuentoRegalo + valorDescuentoRegalo
					+ UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, usuario, Conf.INSERT_SEPARADOR_SI) + id_descuento;
			inserts.add(valores);
		}

		sql = UtileriasBD.armarQueryInsertAll(CondicionOferta.N_TABLA, campos, inserts);

		int res = Qr.update(conn, sql);
		if (res > 0) {
			return Qr != null;
		} else {
			return false;
		}
	}

	/**
	 * Funcion que arma el query a utilizar al modificar datos en m\u00E9todos PUT y
	 * DELETE.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @param tipoOferta
	 * @return OutputCondicion
	 * @throws SQLException
	 */
	public static OutputCondicionOferta doDel(Connection conn, InputCondicionPrincipalOferta input, int metodo,
			String tipoOferta, BigDecimal idPais) throws SQLException {
		String nombreMetodo = "doDel";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta = null;
		OutputCondicionOferta output = null;
		List<Filtro> condiciones = new ArrayList<Filtro>();
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Condicion.CAMPO_TCSCCONDICIONID,
				input.getIdCondicion()));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Condicion.CAMPO_TIPO_OFERTACAMPANIA,
				tipoOferta));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Condicion.CAMPO_TCSCCATPAISID,
				idPais.toString()));
		int existencias = UtileriasBD.selectCount(conn, Condicion.N_TABLA, condiciones);
		if (existencias < 1) {
			log.error("No existe el recurso.");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			output = new OutputCondicionOferta();
			output.setRespuesta(respuesta);

			return output;
		}

		condiciones.clear();
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CondicionOferta.CAMPO_TCSCCONDICIONID,
				input.getIdCondicion()));
		String sql = UtileriasBD.armarQueryDelete(CondicionOferta.N_TABLA, condiciones);

		try {
			conn.setAutoCommit(false);
			QueryRunner Qr = new QueryRunner();
			int res = Qr.update(conn, sql);

			if (res >= 0) {
				condiciones = CtrlCondicionOferta.obtenerCondiciones(input, metodo, idPais);
				sql = UtileriasBD.armarQueryDelete(Condicion.N_TABLA, condiciones);

				res = Qr.update(conn, sql);
				if (res >= 0) {
					conn.commit();
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_DEL_CONDICION_37, null, nombreClase,
							nombreMetodo, null, input.getCodArea());

					output = new OutputCondicionOferta();
					output.setRespuesta(respuesta);
				} else {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					output = new OutputCondicionOferta();
					output.setRespuesta(respuesta);

					log.error("Rollback");
					conn.rollback();
				}
			} else {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null, nombreClase,
						nombreMetodo, null, input.getCodArea());

				output = new OutputCondicionOferta();
				output.setRespuesta(respuesta);

				log.error("Rollback");
				conn.rollback();
			}
		} finally {
			conn.setAutoCommit(true);

		}

		return output;
	}

	public static List<BigDecimal> validarArticuloCondicion(Connection conn, String idOfertaCampania, String idArticulo,
			String tipoOferta, String tipoCampania, String tipoRuta, String tipoPanel, String estadoAlta,
			String tipoGestion, String tipoCliente, String tipoClientePDV, String tipoClienteFinal,
			String tipoClienteAmbos, boolean validarCondicionPDV, String tipoOfertaCondicion, String idPDV,
			String idArticuloRegalo, String montoInicial, String montoFinal, BigDecimal idPais) throws SQLException {
		List<BigDecimal> list = new ArrayList<BigDecimal>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String filtroTipoCliente = "";
		String filtroMontos = "";
		String qryArticuloRegalo = "";
		String fechaDesde = "";
		String fechaHasta = "";

		if (!validarCondicionPDV) {
			if (tipoCliente.equals(tipoClientePDV)) {
				filtroTipoCliente = " AND (UPPER(D.TIPO_CLIENTE) = '" + tipoClientePDV
						+ "' OR UPPER(D.TIPO_CLIENTE) = '" + tipoClienteAmbos + "')";
			} else if (tipoCliente.equals(tipoClienteFinal)) {
				filtroTipoCliente = " AND (UPPER(D.TIPO_CLIENTE) = '" + tipoClienteFinal
						+ "' OR UPPER(D.TIPO_CLIENTE) = '" + tipoClienteAmbos + "')";
			} else if (tipoCliente.equals(tipoClienteAmbos)) {
				filtroTipoCliente = " AND UPPER(D.TIPO_CLIENTE) IS NOT NULL";
			}
		}

		if (idArticuloRegalo != null) {
			// si lelva articulo de regalo se validan condicones pague_lleve
			if (!montoInicial.equals("") && !montoFinal.equals("")) {
				// mandan ambos montos
				filtroMontos = " AND (D.MONTO_INICIAL BETWEEN " + montoInicial + " AND " + montoFinal
						+ " OR D.MONTO_FINAL BETWEEN " + montoInicial + " AND " + montoFinal + ")";

			} else if (!montoInicial.equals("")) {
				// solo mandan monto inicial
				filtroMontos = " AND ((D.MONTO_INICIAL >= " + montoInicial + " OR D.MONTO_FINAL >= " + montoInicial
						+ ") OR D.MONTO_FINAL IS NULL)";

			} else if (!montoFinal.equals("")) {
				// solo mandan monto final
				filtroMontos = " AND ((D.MONTO_INICIAL <= " + montoFinal + " OR D.MONTO_FINAL <= " + montoFinal
						+ ") OR D.MONTO_INICIAL IS NULL)";
			}

			// query para consultar la existencia del articulo a regalar
			qryArticuloRegalo = ", (SELECT COUNT(E.ARTICULO) FROM TC_SC_ARTICULO_INV E" + " WHERE E.TCSCCATPAISID = "
					+ idPais + " AND E.ARTICULO = " + idArticuloRegalo + ") AS EXISTENCIA_ART_REGALO";
		} else {
			// no se hace consulta, solo se devuelve que existe para la validacion
			qryArticuloRegalo = ", 1";
		}

		// Obtenemos las fechas de la oferta campana
		String sql2 = "SELECT  TCSCOFERTACAMPANIAID, to_char(FECHA_DESDE, 'DD/MM/YYYY'), to_char(FECHA_HASTA, 'DD/MM/YYYY')   "
				+ " FROM TC_SC_OFERTA_CAMPANIA WHERE TCSCCATPAISID=?" + " AND TCSCOFERTACAMPANIAID =?"
				+ " AND UPPER(ESTADO) = ?";
		log.trace("Verificando fechas oferta: " + sql2);

		try {
			pstmt = conn.prepareStatement(sql2);
			pstmt.setBigDecimal(1, idPais);
			pstmt.setBigDecimal(2, new BigDecimal(idOfertaCampania));
			pstmt.setString(3, estadoAlta);

			rst = pstmt.executeQuery();

			if (rst.next()) {
				fechaDesde = (rst.getString(2) == null || "".equals(rst.getString(2)) ? "" : rst.getString(2));
				fechaHasta = (rst.getString(3) == null || "".equals(rst.getString(3)) ? "" : rst.getString(3));
			}

		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		String query = armaqueryCondicion(idOfertaCampania, idArticulo, tipoOferta, tipoCampania, tipoRuta, tipoPanel,
				estadoAlta, tipoGestion, tipoCliente, tipoClientePDV, tipoClienteFinal, tipoClienteAmbos,
				validarCondicionPDV, tipoOfertaCondicion, idPDV, idArticuloRegalo, filtroTipoCliente, filtroMontos,
				qryArticuloRegalo, fechaDesde, fechaHasta, idPais);

		log.trace("Verificando articulo: " + query);
		try (PreparedStatement pstmt1 = conn.prepareStatement(query);) {
			try (ResultSet rst1 = pstmt1.executeQuery()) {
				if (rst1.next()) {
					if (rst1.getBigDecimal(1) == null) {
						list.add(0, new BigDecimal(0));
					} else {
						list.add(0, rst1.getBigDecimal(1));
					}

					if (rst1.getBigDecimal(2) == null) {
						list.add(1, new BigDecimal(0));
					} else {
						list.add(1, rst1.getBigDecimal(2));
					}

					if (rst1.getBigDecimal(3) == null) {
						list.add(2, new BigDecimal(0));
					} else {
						list.add(2, rst1.getBigDecimal(3));
					}

					if (rst1.getBigDecimal(4) == null) {
						list.add(3, new BigDecimal(0));
					} else {
						list.add(3, rst1.getBigDecimal(4));
					}

					if (rst1.getBigDecimal(5) == null) {
						list.add(4, new BigDecimal(0));
					} else {
						list.add(4, rst1.getBigDecimal(5));
					}

					if (rst1.getBigDecimal(6) == null) {
						list.add(5, new BigDecimal(0));
					} else {
						list.add(5, rst1.getBigDecimal(6));
					}
				}
			}
		}

		return list;
	}

	public static String armaqueryCondicion(String idOfertaCampania, String idArticulo, String tipoOferta,
			String tipoCampania, String tipoRuta, String tipoPanel, String estadoAlta, String tipoGestion,
			String tipoCliente, String tipoClientePDV, String tipoClienteFinal, String tipoClienteAmbos,
			boolean validarCondicionPDV, String tipoOfertaCondicion, String idPDV, String idArticuloRegalo,
			String filtroTipoCliente, String filtroMontos, String qryArticuloRegalo, String fechaDesde,
			String fechaHasta, BigDecimal idPais) {

		String query = "SELECT " + "(SELECT COUNT(E.ARTICULO) FROM TC_SC_ARTICULO_INV E" + " WHERE E.TCSCCATPAISID = "
				+ idPais + " AND E.ARTICULO = " + idArticulo + ") AS EXISTENCIA_ART, "

				+ "(SELECT D.TCSCCONDICIONID FROM TC_SC_DET_CONDICION_OFERTA D" + " WHERE D.TCSCCONDICIONID IN "
				+ "(SELECT C.TCSCCONDICIONID FROM TC_SC_CONDICION C, TC_SC_OFERTA_CAMPANIA R"
				+ " WHERE C.TCSCOFERTACAMPANIAID IN" + " (SELECT B.TCSCOFERTACAMPANIAID FROM TC_SC_DET_PANELRUTA B"
				+ " WHERE" + " B.TCSCCATPAISID = " + idPais + " AND UPPER(B.ESTADO) = '" + estadoAlta + "'"
				+ " AND UPPER(B.TIPO) = '" + tipoRuta + "'" + " AND B.TCSCTIPOID IN"
				+ " (SELECT A.TCSCTIPOID FROM TC_SC_DET_PANELRUTA A" + " WHERE A.TCSCOFERTACAMPANIAID = "
				+ idOfertaCampania + " AND A.TCSCCATPAISID = " + idPais + " AND UPPER(A.TIPO) = '" + tipoRuta + "'"
				+ " AND UPPER(A.ESTADO) = '" + estadoAlta + "'" + ")" + ")"
				+ " AND R.TCSCOFERTACAMPANIAID=C.TCSCOFERTACAMPANIAID" + " AND UPPER(R.ESTADO)=  '" + estadoAlta + "'"
				+ " AND C.TCSCCATPAISID = " + idPais + " AND UPPER(C.ESTADO) = '" + estadoAlta + "'"
				+ " AND UPPER(C.TIPO_OFERTACAMPANIA) = '" + tipoOferta + "'" + " AND UPPER(C.TIPO_GESTION) = '"
				+ tipoGestion + "'" + " AND C.TCSCOFERTACAMPANIAID <>" + idOfertaCampania
				+ " AND ((TRUNC(R.FECHA_DESDE) <= TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY')"
				+ " AND TRUNC(R.FECHA_HASTA) >= TO_DATE('" + fechaHasta
				+ "', 'DD/MM/YYYY')) OR (TRUNC(R.FECHA_DESDE) >= TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY')"
				+ " AND TRUNC(R.FECHA_HASTA) <= TO_DATE('" + fechaHasta + "', 'DD/MM/YYYY')))" + ")"
				+ " AND D.TCSCCATPAISID = " + idPais + " AND D.ARTICULO = " + idArticulo + filtroMontos
				+ (validarCondicionPDV ? " AND D.TCSCPUNTOVENTAID = " + idPDV : "") + " AND UPPER(D.TIPO_OFERTA) = '"
				+ tipoOfertaCondicion + "'" + filtroTipoCliente + " AND UPPER(D.ESTADO) = '" + estadoAlta + "'"
				+ " AND ROWNUM=1" + ") AS OFERTA_RUTA, "

				+ "(SELECT D.TCSCCONDICIONID FROM TC_SC_DET_CONDICION_OFERTA D" + " WHERE D.TCSCCONDICIONID IN"
				+ " (SELECT C.TCSCCONDICIONID FROM TC_SC_CONDICION C, TC_SC_OFERTA_CAMPANIA R"
				+ " WHERE C.TCSCOFERTACAMPANIAID IN" + " (SELECT B.TCSCOFERTACAMPANIAID FROM TC_SC_DET_PANELRUTA B"
				+ " WHERE" + " B.TCSCCATPAISID = " + idPais + " AND UPPER(B.ESTADO) = '" + estadoAlta + "'"
				+ " AND UPPER(B.TIPO) = '" + tipoPanel + "'" + " AND B.TCSCTIPOID IN"
				+ " (SELECT A.TCSCTIPOID FROM TC_SC_DET_PANELRUTA A" + " WHERE A.TCSCOFERTACAMPANIAID = "
				+ idOfertaCampania + " AND A.TCSCCATPAISID = " + idPais + " AND UPPER(A.TIPO) = '" + tipoPanel + "'"
				+ " AND UPPER(A.ESTADO) = '" + estadoAlta + "'" + ")" + ")"
				+ " AND R.TCSCOFERTACAMPANIAID=C.TCSCOFERTACAMPANIAID" + " AND UPPER(R.ESTADO)=  '" + estadoAlta + "'"
				+ " AND C.TCSCCATPAISID = " + idPais + " AND UPPER(C.ESTADO) = '" + estadoAlta + "'"
				+ " AND UPPER(C.TIPO_OFERTACAMPANIA) = '" + tipoOferta + "'" + " AND UPPER(C.TIPO_GESTION) = '"
				+ tipoGestion + "'" + " AND C.TCSCOFERTACAMPANIAID <>" + idOfertaCampania
				+ " AND ((TRUNC(R.FECHA_DESDE) <= TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY')"
				+ " AND TRUNC(R.FECHA_HASTA) >= TO_DATE('" + fechaHasta
				+ "', 'DD/MM/YYYY')) OR (TRUNC(R.FECHA_DESDE) >= TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY')"
				+ " AND TRUNC(R.FECHA_HASTA) <= TO_DATE('" + fechaHasta + "', 'DD/MM/YYYY')))" + ")"
				+ " AND D.TCSCCATPAISID = " + idPais + " AND D.ARTICULO = " + idArticulo + filtroMontos
				+ (validarCondicionPDV ? " AND D.TCSCPUNTOVENTAID = " + idPDV : "") + " AND UPPER(D.TIPO_OFERTA) = '"
				+ tipoOfertaCondicion + "'" + filtroTipoCliente + " AND UPPER(D.ESTADO) = '" + estadoAlta + "'"
				+ " AND ROWNUM=1" + ") AS OFERTA_PANEL ";

		if (!validarCondicionPDV && idArticuloRegalo == null) {
			query += ", (SELECT D.TCSCCONDICIONID FROM TC_SC_DET_CONDICION_CAMPANIA D" + " WHERE D.TCSCCONDICIONID IN"
					+ " (SELECT C.TCSCCONDICIONID FROM TC_SC_CONDICION C , TC_SC_OFERTA_CAMPANIA R"
					+ " WHERE C.TCSCOFERTACAMPANIAID IN" + " (SELECT B.TCSCOFERTACAMPANIAID FROM TC_SC_DET_PANELRUTA B"
					+ " WHERE" + " B.TCSCCATPAISID = " + idPais + " AND UPPER(B.ESTADO) = '" + estadoAlta + "'"
					+ " AND UPPER(B.TIPO) = '" + tipoRuta + "'" + " AND B.TCSCTIPOID IN"
					+ " (SELECT A.TCSCTIPOID FROM TC_SC_DET_PANELRUTA A" + " WHERE A.TCSCOFERTACAMPANIAID = "
					+ idOfertaCampania + " AND A.TCSCCATPAISID = " + idPais + " AND UPPER(A.TIPO) = '" + tipoRuta + "'"
					+ " AND UPPER(A.ESTADO) = '" + estadoAlta + "'" + ")" + ")"
					+ " AND R.TCSCOFERTACAMPANIAID=C.TCSCOFERTACAMPANIAID" + " AND UPPER(R.ESTADO)=  '" + estadoAlta
					+ "'" + " AND C.TCSCCATPAISID = " + idPais + " AND UPPER(C.ESTADO) = '" + estadoAlta + "'"
					+ " AND UPPER(C.TIPO_OFERTACAMPANIA) = '" + tipoCampania + "'" + " AND UPPER(C.TIPO_GESTION) = '"
					+ tipoGestion + "'" + " AND C.TCSCOFERTACAMPANIAID <>" + idOfertaCampania
					+ " AND ((TRUNC(R.FECHA_DESDE) <= TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY')"
					+ " AND TRUNC(R.FECHA_HASTA) >= TO_DATE('" + fechaHasta
					+ "', 'DD/MM/YYYY')) OR (TRUNC(R.FECHA_DESDE) >= TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY')"
					+ " AND TRUNC(R.FECHA_HASTA) <= TO_DATE('" + fechaHasta + "', 'DD/MM/YYYY')))" + ")"
					+ " AND D.TCSCCATPAISID = " + idPais + " AND D.ARTICULO = " + idArticulo + filtroTipoCliente
					+ " AND UPPER(D.ESTADO) = '" + estadoAlta + "'" + " AND ROWNUM=1" + ") AS CAMPANIA_RUTA, "

					+ "(SELECT D.TCSCCONDICIONID FROM TC_SC_DET_CONDICION_CAMPANIA D" + " WHERE D.TCSCCONDICIONID IN"
					+ " (SELECT C.TCSCCONDICIONID FROM TC_SC_CONDICION C, TC_SC_OFERTA_CAMPANIA R"
					+ " WHERE C.TCSCOFERTACAMPANIAID IN" + " (SELECT B.TCSCOFERTACAMPANIAID FROM TC_SC_DET_PANELRUTA B"
					+ " WHERE" + " B.TCSCCATPAISID = " + idPais + " AND UPPER(B.ESTADO) = '" + estadoAlta + "'"
					+ " AND UPPER(B.TIPO) = '" + tipoPanel + "'" + " AND B.TCSCTIPOID IN"
					+ " (SELECT A.TCSCTIPOID FROM TC_SC_DET_PANELRUTA A" + " WHERE A.TCSCOFERTACAMPANIAID = "
					+ idOfertaCampania + " AND A.TCSCCATPAISID = " + idPais + " AND UPPER(A.TIPO) = '" + tipoPanel + "'"
					+ " AND UPPER(A.ESTADO) = '" + estadoAlta + "'" + ")" + ")"
					+ " AND R.TCSCOFERTACAMPANIAID=C.TCSCOFERTACAMPANIAID" + " AND UPPER(R.ESTADO)=  '" + estadoAlta
					+ "'" + " AND C.TCSCCATPAISID = " + idPais + " AND UPPER(C.ESTADO) = '" + estadoAlta + "'"
					+ " AND UPPER(C.TIPO_OFERTACAMPANIA) = '" + tipoCampania + "'" + " AND UPPER(C.TIPO_GESTION) = '"
					+ tipoGestion + "'" + " AND C.TCSCOFERTACAMPANIAID <>" + idOfertaCampania
					+ " AND ((TRUNC(R.FECHA_DESDE) <= TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY')"
					+ " AND TRUNC(R.FECHA_HASTA) >= TO_DATE('" + fechaHasta
					+ "', 'DD/MM/YYYY')) OR (TRUNC(R.FECHA_DESDE) >= TO_DATE('" + fechaDesde + "', 'DD/MM/YYYY')"
					+ " AND TRUNC(R.FECHA_HASTA) <= TO_DATE('" + fechaHasta + "', 'DD/MM/YYYY')))" + ")"
					+ " AND D.TCSCCATPAISID = " + idPais + " AND D.ARTICULO = " + idArticulo + filtroTipoCliente
					+ " AND UPPER(D.ESTADO) = '" + estadoAlta + "'" + " AND ROWNUM=1" + ") AS CAMPANIA_PANEL ";
		} else {
			query += ",0,0";
		}
		query += qryArticuloRegalo; // se concatena para validar condicion pague_lleve

		query += " FROM DUAL";
		return query;

	}

	public static String buscarNombreCampania(Connection conn, int idOferta, String estadoAlta, BigDecimal idPais)
			throws SQLException {
		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String nombreCampania = "";

		sql = "SELECT NOMBRE " + " FROM TC_SC_CONDICION  " + " WHERE TCSCCONDICIONID= ?" + " AND TCSCCATPAISID= ?"
				+ " AND UPPER(ESTADO) = ?";

		log.trace("Verificando articulo: " + sql);

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idOferta);
			pstmt.setBigDecimal(2, idPais);
			pstmt.setString(3, estadoAlta);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				nombreCampania = rst.getString(1);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return nombreCampania;
	}

	public static List<BigDecimal> validarTecnologiaCondicion(Connection conn, String idOfertaCampania,
			String tipoOferta, String tipoRuta, String tipoPanel, String estadoAlta, String tipoGestion,
			String tipoCliente, String tipoClientePDV, String tipoClienteFinal, String tipoClienteAmbos,
			String tipoTecnologia, String tecnologia, BigDecimal idPais) throws SQLException {
		List<BigDecimal> list = new ArrayList<BigDecimal>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String filtroTipoCliente = "";

		if (tipoCliente.equals(tipoClientePDV)) {
			filtroTipoCliente = " AND (UPPER(D.TIPO_CLIENTE) = '" + tipoClientePDV + "' OR UPPER(D.TIPO_CLIENTE) = '"
					+ tipoClienteAmbos + "')";
		} else if (tipoCliente.equals(tipoClienteFinal)) {
			filtroTipoCliente = " AND (UPPER(D.TIPO_CLIENTE) = '" + tipoClienteFinal + "' OR UPPER(D.TIPO_CLIENTE) = '"
					+ tipoClienteAmbos + "')";
		} else if (tipoCliente.equals(tipoClienteAmbos)) {
			filtroTipoCliente = " AND UPPER(D.TIPO_CLIENTE) IS NOT NULL";
		}

		String query = validarQueryTec(filtroTipoCliente);

		log.trace("Verificando tecnolog\u00EDa: " + query);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, idPais);
			pstmt.setString(2, estadoAlta);
			pstmt.setString(3, tipoRuta);
			pstmt.setBigDecimal(4, new BigDecimal(idOfertaCampania));
			pstmt.setBigDecimal(5, idPais);
			pstmt.setString(6, tipoRuta);
			pstmt.setString(7, estadoAlta);
			pstmt.setBigDecimal(8, idPais);
			pstmt.setString(9, estadoAlta);
			pstmt.setString(10, tipoOferta);
			pstmt.setString(11, tipoGestion);
			pstmt.setBigDecimal(12, idPais);
			pstmt.setString(13, tecnologia);
			pstmt.setString(14, tipoTecnologia);
			pstmt.setString(15, estadoAlta);
			pstmt.setBigDecimal(16, idPais);
			pstmt.setString(17, estadoAlta);
			pstmt.setString(18, tipoPanel);
			pstmt.setBigDecimal(19, new BigDecimal(idOfertaCampania));
			pstmt.setBigDecimal(20, idPais);
			pstmt.setString(21, tipoPanel);
			pstmt.setString(22, estadoAlta);
			pstmt.setBigDecimal(23, idPais);
			pstmt.setString(24, estadoAlta);
			pstmt.setString(25, tipoOferta);
			pstmt.setString(26, tipoGestion);
			pstmt.setBigDecimal(27, idPais);
			pstmt.setString(28, tecnologia);
			pstmt.setString(29, tipoTecnologia);
			pstmt.setString(30, estadoAlta);

			rst = pstmt.executeQuery();

			if (rst.next()) {
				list.add(0, rst.getBigDecimal(1));
				list.add(1, rst.getBigDecimal(2));
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return list;
	}

	public static String validarQueryTec(String filtroTipoCliente) {
		String query = "SELECT " + "(SELECT COUNT(1) FROM TC_SC_DET_CONDICION_OFERTA D" + " WHERE D.TCSCCONDICIONID IN "
				+ "(SELECT C.TCSCCONDICIONID FROM TC_SC_CONDICION C" + " WHERE C.TCSCOFERTACAMPANIAID IN"
				+ " (SELECT B.TCSCOFERTACAMPANIAID FROM TC_SC_DET_PANELRUTA B" + " WHERE" + " B.TCSCCATPAISID = ?"
				+ " AND UPPER(B.ESTADO) = ?" + " AND UPPER(B.TIPO) = ?" + " AND B.TCSCTIPOID IN"
				+ " (SELECT A.TCSCTIPOID FROM TC_SC_DET_PANELRUTA A" + " WHERE A.TCSCOFERTACAMPANIAID = ?"
				+ " AND A.TCSCCATPAISID =? " + " AND UPPER(A.TIPO) = ?" + " AND UPPER(A.ESTADO) =?" + ")" + ")"
				+ " AND C.TCSCCATPAISID = ?" + " AND UPPER(C.ESTADO) = ?" + " AND UPPER(C.TIPO_OFERTACAMPANIA) = ?"
				+ " AND UPPER(C.TIPO_GESTION) = ?" + ")" + " AND D.TCSCCATPAISID = ?" + " AND D.TECNOLOGIA = ?"
				+ " AND UPPER(D.TIPO_OFERTA) =?" + filtroTipoCliente + " AND UPPER(D.ESTADO) = ?" + ") AS OFERTA_RUTA, "

				+ "(SELECT COUNT(1) FROM TC_SC_DET_CONDICION_OFERTA D" + " WHERE D.TCSCCONDICIONID IN"
				+ " (SELECT C.TCSCCONDICIONID FROM TC_SC_CONDICION C" + " WHERE C.TCSCOFERTACAMPANIAID IN"
				+ " (SELECT B.TCSCOFERTACAMPANIAID FROM TC_SC_DET_PANELRUTA B" + " WHERE" + " B.TCSCCATPAISID = ?"
				+ " AND UPPER(B.ESTADO) = ?" + " AND UPPER(B.TIPO) = ?" + " AND B.TCSCTIPOID IN"
				+ " (SELECT A.TCSCTIPOID FROM TC_SC_DET_PANELRUTA A" + " WHERE A.TCSCOFERTACAMPANIAID =? "
				+ " AND A.TCSCCATPAISID = ?" + " AND UPPER(A.TIPO) = ?" + " AND UPPER(A.ESTADO) = ?" + ")" + ")"
				+ " AND C.TCSCCATPAISID = ?" + " AND UPPER(C.ESTADO) = ?" + " AND UPPER(C.TIPO_OFERTACAMPANIA) = ?"
				+ " AND UPPER(C.TIPO_GESTION) = ?" + ")" + " AND D.TCSCCATPAISID = ?" + " AND D.TECNOLOGIA = ?"
				+ " AND UPPER(D.TIPO_OFERTA) =?" + filtroTipoCliente + " AND UPPER(D.ESTADO) = ?"
				+ ") AS OFERTA_PANEL FROM DUAL";

		return query;
	}

	public static OutputCondicionOferta getOfertasRuta(String codArea) {
		String nombreMetodo = "doGet";
		String nombreClase = new CurrentClassGetter().getClassName();

		List<InputCondicionOferta> listPDV = new ArrayList<InputCondicionOferta>();

		List<InputCondicionOferta> listZona = new ArrayList<InputCondicionOferta>();
		OutputCondicionOferta output = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		try {
			output = new OutputCondicionOferta();
			output.setRespuesta(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
					nombreClase, nombreMetodo, null, codArea));

			InputCondicionOferta item = new InputCondicionOferta();
			item.setIdCondicion("2");
			item.setTipoGestion("VENTA");
			item.setTipoCondicion("ARTICULO");
			item.setNombre("Condicion PDV Punto 10");
			item.setIdOfertaCampania("5");
			item.setNombreCampania("Camapa\u00F1a Descuentos Esperanza");
			item.setEstado("ALTA");
			item.setCreadoEl("28/03/2017 17:27:28");
			item.setCreadoPor("usuario.pruebas");
			item.setModificadoEl("");
			item.setModificadoPor("");

			List<InputDetCondicionOferta> detalles = new ArrayList<InputDetCondicionOferta>();
			for (int i = 1; i < 3; i++) {
				InputDetCondicionOferta detalle = new InputDetCondicionOferta();
				detalle.setIdCondicion(i + "");
				detalle.setTipo("PDV");
				detalle.setIdArticulo(i * 2 + "");
				detalle.setTecnologia("");
				detalle.setNombreArticulo("BLACKBERRY Q10 NEGRO");
				detalle.setTipoInv("INV_TELCA");
				detalle.setTipoCliente("PDV");
				detalle.setMontoInicial("1");
				detalle.setMontoFinal("0");
				detalle.setTipoDescuento("PORCENTAJE");
				detalle.setValorDescuento("5");
				detalle.setIdPDV(i + "");
				detalle.setNombrePDV("Reforma " + i * 7);
				detalle.setZonaComercialPDV("");
				detalle.setCategoriaPDV("");

				detalle.setIdArticuloRegalo("103");
				detalle.setNombreArticuloRegalo("TARJETA RASCA $C20");
				detalle.setCantidadArticuloRegalo("1");
				detalle.setTipoDescuentoRegalo("PORCENTAJE");
				detalle.setValorDescuentoRegalo("100");

				detalle.setEstado("ALTA");
				detalle.setCreadoEl("28/03/2017 17:27:28");
				detalle.setCreadoPor("usuario.pruebas");
				detalle.setModificadoEl("");
				detalle.setModificadoPor("");

				detalles.add(detalle);
			}

			item.setDetalle(detalles);
			listPDV.add(item);

			output.setCondicionesPdv(listPDV);

			item = new InputCondicionOferta();
			item.setIdCondicion("2");
			item.setTipoGestion("VENTA");
			item.setTipoCondicion("ARTICULO");
			item.setNombre("Condicion PDV Punto 10");
			item.setIdOfertaCampania("5");
			item.setNombreCampania("Campa\u00F1a Descuentos Esperanza");
			item.setEstado("ALTA");
			item.setCreadoEl("28/03/2017 17:27:28");
			item.setCreadoPor("usuario.pruebas");
			item.setModificadoEl("");
			item.setModificadoPor("");

			detalles = new ArrayList<InputDetCondicionOferta>();
			InputDetCondicionOferta detalle = new InputDetCondicionOferta();
			detalle.setIdCondicion("91");
			detalle.setTipo("ZONA");
			detalle.setIdArticulo("130");
			detalle.setTecnologia("");
			detalle.setNombreArticulo("BLACKBERRY Q10 NEGRO");
			detalle.setTipoInv("INV_TELCA");
			detalle.setTipoCliente("PDV");
			detalle.setMontoInicial("1");
			detalle.setMontoFinal("0");
			detalle.setTipoDescuento("PORCENTAJE");
			detalle.setValorDescuento("5");
			detalle.setIdPDV("15");
			detalle.setNombrePDV("Managua Central");
			detalle.setZonaComercialPDV("Managua");
			detalle.setCategoriaPDV("");

			detalle.setIdArticuloRegalo("103");
			detalle.setNombreArticuloRegalo("TARJETA RASCA $C20");
			detalle.setCantidadArticuloRegalo("1");
			detalle.setTipoDescuentoRegalo("PORCENTAJE");
			detalle.setValorDescuentoRegalo("100");

			detalle.setEstado("ALTA");
			detalle.setCreadoEl("28/03/2017 17:27:28");
			detalle.setCreadoPor("usuario.pruebas");
			detalle.setModificadoEl("");
			detalle.setModificadoPor("");

			detalles.add(detalle);

			item.setDetalle(detalles);
			listZona.add(item);

			output.setCondicionesZonaCat(listZona);

			return output;

		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

	}
}
