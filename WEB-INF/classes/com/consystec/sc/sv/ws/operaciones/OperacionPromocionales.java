package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.promocionales.InputGrupoPromocionales;
import com.consystec.sc.ca.ws.input.promocionales.InputPromocionales;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.promocionales.OutputPromocionales;
import com.consystec.sc.sv.ws.metodos.CtrlPromocionales;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.Promocionales;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class OperacionPromocionales {
	private OperacionPromocionales() {
	}

	private static final Logger log = Logger.getLogger(OperacionPromocionales.class);

	/**
	 * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @return OutputPromocionales
	 * @throws SQLException
	 */
	public static OutputPromocionales doGet(Connection conn, InputPromocionales input, int metodo, BigDecimal ID_PAIS)
			throws SQLException {
		String nombreMetodo = "doGet";
		String nombreClase = new CurrentClassGetter().getClassName();

		List<InputPromocionales> list = new ArrayList<InputPromocionales>();
		List<InputGrupoPromocionales> grupos = new ArrayList<InputGrupoPromocionales>();

		Respuesta respuesta = null;
		OutputPromocionales output = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String sql = null;

		try {
			String campos[] = CtrlPromocionales.obtenerCamposGetPost(metodo);

			List<Order> orden = new ArrayList<Order>();
			orden.add(new Order(Promocionales.CAMPO_TIPO_GRUPO, Order.ASC));
			orden.add(new Order(Promocionales.CAMPO_DESCRIPCION, Order.ASC));

			List<Filtro> condiciones = CtrlPromocionales.obtenerCondiciones(input, metodo, ID_PAIS);

			sql = UtileriasBD.armarQuerySelect(Promocionales.N_TABLA, campos, condiciones, orden);
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst != null) {
				if (!rst.next()) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_ART_PROMOCIONALES_820,
							null, nombreClase, nombreMetodo, null, input.getCodArea());

					output = new OutputPromocionales();
					output.setRespuesta(respuesta);
				} else {
					respuesta = new Respuesta();
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					do {
						InputPromocionales item = new InputPromocionales();
						item.setIdArtPromocional(rst.getString(Promocionales.CAMPO_TCSCARTPROMOCIONALID));
						item.setDescripcion(rst.getString(Promocionales.CAMPO_DESCRIPCION));
						item.setTipoGrupo(rst.getString(Promocionales.CAMPO_TIPO_GRUPO));
						if (rst.getString(Promocionales.CAMPO_ID_PRODUCT_OFFERING) == null
								|| "".equals(rst.getString(Promocionales.CAMPO_ID_PRODUCT_OFFERING))) {
							item.setIdOferta("");
							item.setNombreOferta("");
						} else {
							item.setIdOferta(rst.getString(Promocionales.CAMPO_ID_PRODUCT_OFFERING));
							item.setNombreOferta(rst.getString("NOMBRE_OFERTA"));
						}
						item.setDescripcion(rst.getString(Promocionales.CAMPO_DESCRIPCION));
						item.setEstado(rst.getString(Promocionales.CAMPO_ESTADO));
						item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(Promocionales.CAMPO_CREADO_EL)));
						item.setCreado_por(rst.getString(Promocionales.CAMPO_CREADO_POR));
						item.setModificado_el(
								UtileriasJava.formatStringDate(rst.getString(Promocionales.CAMPO_MODIFICADO_EL)));
						item.setModificado_por(rst.getString(Promocionales.CAMPO_MODIFICADO_POR));

						list.add(item);
					} while (rst.next());

					orden.clear();
					orden.add(new Order(Promocionales.CAMPO_TIPO_GRUPO, Order.ASC));

					List<String> listadoGrupos = UtileriasBD.getOneField(conn,
							"DISTINCT(" + Promocionales.CAMPO_TIPO_GRUPO + ")", Promocionales.N_TABLA, null, orden);
					for (String grupoPromo : listadoGrupos) {
						InputGrupoPromocionales grupo = new InputGrupoPromocionales();
						List<InputPromocionales> articulosGrupo = new ArrayList<InputPromocionales>();
						for (InputPromocionales articulo : list) {
							if (articulo.getTipoGrupo().equals(grupoPromo)) {
								articulosGrupo.add(articulo);
							}
						}
						if (articulosGrupo.size() > 0) {
							grupo.setGrupo(grupoPromo);
							grupo.setArticulos(articulosGrupo);
							grupos.add(grupo);
						}
					}

					output = new OutputPromocionales();
					output.setRespuesta(respuesta);
					output.setGrupos(grupos);
				}
			}

		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return output;
	}

	/**
	 * Funci\u00F3n que arma el query a utilizar al trabajar en m\u00E9todo POST.
	 * 
	 * @param conn
	 * @param input
	 * @return OutputPromocionales
	 * @throws SQLException
	 */
	public static OutputPromocionales doPost(Connection conn, InputPromocionales input, BigDecimal ID_PAIS)
			throws SQLException {
		String nombreMetodo = "doPost";
		String nombreClase = new CurrentClassGetter().getClassName();
		int idRecurso = 0;
		Respuesta respuesta = null;
		OutputPromocionales output = null;

		String sql = null;
		String campos[] = CtrlPromocionales.obtenerCamposGetPost(Conf.METODO_POST);
		List<String> inserts = CtrlPromocionales.obtenerInsertsPost(conn, input, Promocionales.SEQUENCE, ID_PAIS);

		sql = UtileriasBD.armarQueryInsert(Promocionales.N_TABLA, campos, inserts);

		try {
			conn.setAutoCommit(false);
			String[] generatedColumns = { Promocionales.CAMPO_TCSCARTPROMOCIONALID };
			try (PreparedStatement pstmt = conn.prepareStatement(sql, generatedColumns);) {
				pstmt.executeUpdate();
				try (ResultSet rs = pstmt.getGeneratedKeys();) {
					if (rs.next()) {
						idRecurso = rs.getInt(1);
					}
				}
			}
			if (idRecurso > 0) {
				log.debug("id: " + idRecurso);
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_ART_PROMOCIONAL_49, null,
						nombreClase, nombreMetodo, null, input.getCodArea());

				output = new OutputPromocionales();
				output.setIdArtPromocional(idRecurso + "");
				output.setRespuesta(respuesta);
				conn.commit();
			}
		} finally {
			conn.setAutoCommit(true);

		}

		return output;
	}

	/**
	 * Funci\u00F3n que arma el query a utilizar al modificar datos en m\u00E9todos
	 * PUT y DELETE.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @return OutputPromocionales
	 * @throws SQLException
	 */
	public static OutputPromocionales doPutDel(Connection conn, InputPromocionales input, int metodo,
			BigDecimal ID_PAIS) throws SQLException {
		List<LogSidra> listaLog = new ArrayList<LogSidra>();
		String servicioPut = Conf.LOG_PUT_PROMOCIONALES;

		String nombreMetodo = "doPutDel";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta = null;
		OutputPromocionales output = null;

		QueryRunner Qr = new QueryRunner();
		conn.setAutoCommit(false);
		String sql = null;
		int res = 0;

		List<Filtro> condiciones = CtrlPromocionales.obtenerCondiciones(input, metodo, ID_PAIS);

		String existencia = UtileriasBD.verificarExistencia(conn, Promocionales.N_TABLA, condiciones);
		if (new Integer(existencia) < 1) {
			log.error("No existe el recurso.");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			output = new OutputPromocionales();
			output.setRespuesta(respuesta);

			return output;
		} else {
			String estadoBaja = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, input.getCodArea());
			String nombreArticulo = UtileriasBD.getOneRecord(conn, Promocionales.CAMPO_DESCRIPCION,
					Promocionales.N_TABLA, condiciones);

			if (metodo == Conf.METODO_DELETE
					|| (metodo == Conf.METODO_PUT && input.getEstado().equalsIgnoreCase(estadoBaja))) {
				condiciones.clear();
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID,
						ID_PAIS.toString()));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO,
						input.getIdArtPromocional()));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_TIPO_INV,
						UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPOINV, Conf.SOL_TIPOINV_SIDRA,
								input.getCodArea())));

				existencia = UtileriasBD.verificarExistencia(conn, Inventario.N_TABLA, condiciones);
				if (new Integer(existencia) > 0) {
					log.error("Tiene inventario el art\u00EDculo.");
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_TIENE_INV_114, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					output = new OutputPromocionales();
					output.setRespuesta(respuesta);

					return output;
				}
			} else if (metodo == Conf.METODO_PUT && !input.getEstado().equalsIgnoreCase(estadoBaja)
					&& !input.getDescripcion().equalsIgnoreCase(nombreArticulo)) {
				String tipoInv = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPOINV, Conf.SOL_TIPOINV_SIDRA,
						input.getCodArea());

				String camposInventario[][] = { { Inventario.CAMPO_DESCRIPCION,
						UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getDescripcion()) } };

				condiciones.clear();
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID,
						ID_PAIS.toString()));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO,
						input.getIdArtPromocional()));
				condiciones.add(
						UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_TIPO_INV, tipoInv));

				sql = UtileriasBD.armarQueryUpdate(Inventario.N_TABLA, camposInventario, condiciones);

				try {
					res = Qr.update(conn, sql);
					log.debug("Se modificaron " + res + " registros en el inventario.");

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_ARTICULO_PROMOCIONAL, servicioPut,
							input.getIdArtPromocional(), Conf.LOG_TIPO_ARTICULO_PROMOCIONAL,
							"Se modificaron " + res
									+ " registros en el inventario por cambio en el nombre del art\u00EDculo promocional ID "
									+ input.getIdArtPromocional() + ", de " + nombreArticulo + " a "
									+ input.getDescripcion() + ".",
							""));

				} catch (SQLException e) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null,
							nombreClase, nombreMetodo, "Problema al actualizar inventario.", input.getCodArea());

					log.error("Rollback, problema al actualizar inventario.");
					conn.rollback();

					output = new OutputPromocionales();
					output.setRespuesta(respuesta);

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_ARTICULO_PROMOCIONAL, servicioPut,
							input.getIdArtPromocional(), Conf.LOG_TIPO_ARTICULO_PROMOCIONAL,
							"Problema al actualizar inventario al modificar el art\u00EDculo promocional ID "
									+ input.getIdArtPromocional() + ".",
							""));

					return output;
				} finally {
					UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
				}
			}
		}

		String campos[][] = CtrlPromocionales.obtenerCamposPutDel(conn, input, metodo);
		condiciones = CtrlPromocionales.obtenerCondiciones(input, metodo, ID_PAIS);

		sql = UtileriasBD.armarQueryUpdate(Promocionales.N_TABLA, campos, condiciones);

		try {
			res = Qr.update(conn, sql);

			if (res > 0) {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_MOD_ART_PROMOCIONAL_50, null, nombreClase,
						nombreMetodo, null, input.getCodArea());

				output = new OutputPromocionales();
				output.setRespuesta(respuesta);

				conn.commit();
			} else {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null, nombreClase,
						nombreMetodo, null, input.getCodArea());

				output = new OutputPromocionales();
				output.setRespuesta(respuesta);

				log.error("Rollback.");
				conn.rollback();
			}
		} finally {
			conn.setAutoCommit(true);
		}

		return output;
	}
}
