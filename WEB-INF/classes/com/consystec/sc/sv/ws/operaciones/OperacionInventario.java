package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.inventario.InputConsultaInventario;
import com.consystec.sc.ca.ws.input.inventario.InputDatosInventario;
import com.consystec.sc.ca.ws.input.inventario.InputInventario;
import com.consystec.sc.ca.ws.input.inventario.InputInventarioArticulos;
import com.consystec.sc.ca.ws.input.inventario.InputInventarioBodegas;
import com.consystec.sc.ca.ws.input.inventario.InputInventarioGrupos;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.inventario.OutputInventario;
import com.consystec.sc.sv.ws.metodos.CtrlInventario;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Inventario;
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
public class OperacionInventario {
	private OperacionInventario() {
	}

	private static final Logger log = Logger.getLogger(OperacionInventario.class);

	/**
	 * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @return OutputDescuento
	 * @throws SQLException
	 */
	public static OutputInventario doGet(Connection conn, InputConsultaInventario input, BigDecimal idPais)
			throws SQLException {
		String nombreMetodo = "doGet";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta = null;
		OutputInventario output = null;
		String idArticulo = "";
		String idBodega = "";
		String tipoGrupo = "";
		String descripcion = "";
		String estado = "";
		String idSolicitud = "";
		String idAsignacionReserva = "";
		String tipoInv = "";
		int min = (input.getMin() != null && !input.getMin().equals("")) ? new Integer(input.getMin()) : 0;
		int max = (input.getMax() != null && !input.getMax().equals("")) ? new Integer(input.getMax()) : 0;
		int mostrarDetalle = (input.getMostrarDetalle() != null && !input.getMostrarDetalle().equals(""))
				? new Integer(input.getMostrarDetalle())
				: 1;
		int estadoAltaBool = new Integer(
				UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA_BOOL, input.getCodArea()));
		String vendedor = "NULL";

		if (input.getIdVendedor() != null && !input.getIdVendedor().trim().equals("")) {
			vendedor = input.getIdVendedor();
		}

		String campos[] = CtrlInventario.obtenerCamposGet(mostrarDetalle, estadoAltaBool, vendedor);
		List<Filtro> condiciones = CtrlInventario.obtenerCondiciones(conn, input, idPais);

		// Filtro para evitar articulos de bonos del inventario
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND_NEQ, Inventario.CAMPO_TIPO_GRUPO_SIDRA,
				Conf.TIPO_GRUPO_BONO));

		List<InputInventarioBodegas> datosBodegas = getBodegas(conn, input.getIdBodega(), input.getCodArea(), idPais);

		List<Order> orden = new ArrayList<Order>();
		orden.add(new Order(Inventario.CAMPO_TIPO_GRUPO_SIDRA, Order.ASC));
		List<String> datosGrupos = UtileriasBD.getOneField(conn,
				UtileriasJava.setSelect(Conf.SELECT_DISTINCT, Inventario.CAMPO_TIPO_GRUPO_SIDRA), ControladorBase
						.getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodega(), input.getCodArea()),
				condiciones, orden);

		orden.clear();
		orden.add(new Order(Inventario.CAMPO_DESCRIPCION, Order.ASC));
		List<Map<String, String>> datosArticulos = new ArrayList<Map<String, String>>();
		List<Map<String, String>> datosInventario = new ArrayList<Map<String, String>>();
		if (mostrarDetalle == estadoAltaBool) {
			// se muestra todo normal
			String sql = UtileriasBD.armarQrySelect(ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION,
					input.getIdBodega(), input.getCodArea()), campos, condiciones, null, orden, min, max);
			Statement stm = null;
			ResultSet rst = null;
			try {
				stm = conn.createStatement();
				rst = stm.executeQuery(sql);
				if (rst.next()) {
					do {
						Map<String, String> datos = new HashMap<String, String>();
						Map<String, String> datosCopia = new HashMap<String, String>();

						for (int i = 0; i < campos.length; i++) {
							String alias;
							if (campos[i].contains(" AS ")) {
								alias = campos[i].split(" AS ")[1];
							} else {
								alias = campos[i];
							}

							datos.put(alias, rst.getString(i + 1));
							datosCopia.put(alias, rst.getString(i + 1));
						}
						datosArticulos.add(datos);
						datosInventario.add(datosCopia);
					} while (rst.next());
				}
			} finally {
				DbUtils.closeQuietly(rst);
				DbUtils.closeQuietly(stm);
			}

		} else {
			// no se muestran detalles

			String groupBy[] = { Inventario.CAMPO_ARTICULO, Inventario.CAMPO_TCSCBODEGAVIRTUALID,
					Inventario.CAMPO_DESCRIPCION, Inventario.CAMPO_SERIADO, Inventario.CAMPO_TIPO_INV,
					Inventario.CAMPO_TIPO_GRUPO_SIDRA, Inventario.CAMPO_TCSCCATPAISID };

			datosArticulos = UtileriasBD.getPaginatedData(conn, ControladorBase.getParticion(Inventario.N_TABLA,
					Conf.PARTITION, input.getIdBodega(), input.getCodArea()), campos, condiciones, groupBy, orden, min,
					max);
			Map<String, String> o = new HashMap<String, String>();
			o.put("STUB", "STUB");
			datosInventario.add(o);
		}

		boolean val1 = (datosBodegas == null || datosBodegas.size() < 1 || datosBodegas.isEmpty());
		boolean val2 = (datosGrupos == null || datosGrupos.size() < 1 || datosGrupos.isEmpty());
		boolean val3 = (datosArticulos == null || datosArticulos.size() < 1 || datosArticulos.isEmpty());
		boolean val4 = (datosInventario == null || datosInventario.size() < 1 || datosInventario.isEmpty());
		if (val1 || val2 || val3 || val4) {
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_INVENTARIO_397, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			output = new OutputInventario();
			output.setRespuesta(respuesta);
		} else {
			respuesta = new Respuesta();
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			List<InputInventario> inventario = new ArrayList<InputInventario>();
			InputInventario itemInventario = new InputInventario();

			List<InputInventarioBodegas> listBodegas = new ArrayList<InputInventarioBodegas>();
			List<InputInventarioBodegas> bodegas = new ArrayList<InputInventarioBodegas>();
			InputInventarioBodegas bodega = new InputInventarioBodegas();

			List<InputInventarioArticulos> articulos = new ArrayList<InputInventarioArticulos>();
			InputInventarioArticulos articulo = new InputInventarioArticulos();

			List<InputDatosInventario> detalles = new ArrayList<InputDatosInventario>();
			InputDatosInventario detalle = new InputDatosInventario();

			for (int i = 0; i < datosBodegas.size(); i++) {
				for (String nombreGrupo : datosGrupos) {
					bodega = new InputInventarioBodegas();

					bodega.setIdBodega(datosBodegas.get(i).getIdBodega());
					bodega.setNombreBodega(datosBodegas.get(i).getNombreBodega());
					bodega.setNivel(datosBodegas.get(i).getNivel());
					bodega.setGrupo(nombreGrupo);

					bodegas.add(bodega);
				}
			}

			// --------------------------------------------------------------------
			for (int i = 0; i < datosArticulos.size(); i++) {
				articulo = new InputInventarioArticulos();
				detalles = new ArrayList<InputDatosInventario>();

				idBodega = datosArticulos.get(i).get(Inventario.CAMPO_TCSCBODEGAVIRTUALID);
				tipoGrupo = datosArticulos.get(i).get(Inventario.CAMPO_TIPO_GRUPO_SIDRA);
				idArticulo = datosArticulos.get(i).get(Inventario.CAMPO_ARTICULO);

				descripcion = datosArticulos.get(i).get(Inventario.CAMPO_DESCRIPCION);
				estado = datosArticulos.get(i).get(Inventario.CAMPO_ESTADO);
				idSolicitud = UtileriasJava.getValue(datosArticulos.get(i).get(Inventario.CAMPO_TCSCSOLICITUDID));
				idAsignacionReserva = UtileriasJava
						.getValue(datosArticulos.get(i).get(Inventario.CAMPO_TCSCASIGNACIONRESERVAID));
				tipoInv = datosArticulos.get(i).get(Inventario.CAMPO_TIPO_INV);

				articulo.setIdBodega(idBodega);
				articulo.setGrupo(tipoGrupo);
				articulo.setIdArticulo(idArticulo);
				articulo.setDescripcion(descripcion);
				articulo.setCantidad(datosArticulos.get(i).get(Inventario.CAMPO_CANTIDAD));
				articulo.setSeriado(datosArticulos.get(i).get(Inventario.CAMPO_SERIADO));
				articulo.setEstado(estado);
				articulo.setIdSolicitud(idSolicitud);
				articulo.setIdAsignacionReserva(idAsignacionReserva);
				articulo.setTipoInv(tipoInv);
				articulo.setTecnologia(UtileriasJava.getValue(datosArticulos.get(i).get(Inventario.CAMPO_TECNOLOGIA)));
				articulo.setPrecioScl(UtileriasJava.getValue(datosArticulos.get(i).get("PRECIO")));
				articulo.setVendedor(UtileriasJava.getValue(datosArticulos.get(i).get("VENDEDOR")));

				if (mostrarDetalle == estadoAltaBool) {
					for (int j = 0; j < datosInventario.size(); j++) {
						boolean cond1 = (idArticulo.equals(datosInventario.get(j).get(Inventario.CAMPO_ARTICULO))
								&& idBodega.equals(datosInventario.get(j).get(Inventario.CAMPO_TCSCBODEGAVIRTUALID))
								&& descripcion.equals(datosInventario.get(j).get(Inventario.CAMPO_DESCRIPCION)));
						boolean cond2 = (tipoGrupo.equals(datosInventario.get(j).get(Inventario.CAMPO_TIPO_GRUPO_SIDRA))
								&& estado.equals(datosInventario.get(j).get(Inventario.CAMPO_ESTADO))
								&& tipoInv.equals(datosInventario.get(j).get(Inventario.CAMPO_TIPO_INV)));
						boolean cond3 = (idSolicitud
								.equals(datosInventario.get(j).get(Inventario.CAMPO_TCSCSOLICITUDID))
								|| idSolicitud.equals(""));
						boolean cond4 = (idAsignacionReserva
								.equals(datosInventario.get(j).get(Inventario.CAMPO_TCSCASIGNACIONRESERVAID))
								|| idAsignacionReserva.equals(""));

						detalle = new InputDatosInventario();
						if (cond1 && cond2 && cond3 && cond4) {
							detalle.setIdInventario(datosInventario.get(j).get(Inventario.N_TABLA_ID));
							detalle.setIdSolicitud(datosInventario.get(j).get(Inventario.CAMPO_TCSCSOLICITUDID));
							detalle.setIdAsignacionReserva(
									datosInventario.get(j).get(Inventario.CAMPO_TCSCASIGNACIONRESERVAID));
							detalle.setSerie(datosInventario.get(j).get(Inventario.CAMPO_SERIE));
							detalle.setEstadoComercial(datosInventario.get(j).get(Inventario.CAMPO_ESTADO_COMERCIAL));
							detalle.setSerieAsociada(datosInventario.get(j).get(Inventario.CAMPO_SERIE_ASOCIADA));
							detalle.setTipoInv(datosInventario.get(j).get(Inventario.CAMPO_TIPO_INV));
							detalle.setNumTraspasoScl(datosInventario.get(j).get(Inventario.CAMPO_NUM_TRASPASO_SCL));
							detalle.setEstado(datosInventario.get(j).get(Inventario.CAMPO_ESTADO));
							detalle.setNumTelefono(datosInventario.get(j).get(Inventario.CAMPO_NUM_TELEFONO));
							detalle.setIcc(datosInventario.get(j).get(Inventario.CAMPO_ICC));
							detalle.setImei(datosInventario.get(j).get(Inventario.CAMPO_IMEI));
							detalle.setCreado_el(UtileriasJava
									.formatStringDate(datosInventario.get(j).get(Inventario.CAMPO_CREADO_EL)));
							detalle.setCreado_por(datosInventario.get(j).get(Inventario.CAMPO_CREADO_POR));
							detalle.setModificado_el(UtileriasJava
									.formatStringDate(datosInventario.get(j).get(Inventario.CAMPO_MODIFICADO_EL)));
							detalle.setModificado_por(datosInventario.get(j).get(Inventario.CAMPO_MODIFICADO_POR));

							detalles.add(detalle);
							datosInventario.get(j).clear();
						}
					}

					if (detalles.size() > 0) {
						articulo.setDetalleArticulo(detalles);
						articulos.add(articulo);
					}
				} else {
					articulos.add(articulo);
				}
			}
			// --------------------------------------------------------------------

			// --------------------------------------------------------------------
			List<InputInventarioGrupos> grupos = new ArrayList<InputInventarioGrupos>();
			for (int i = 0; i < bodegas.size(); i++) {
				tipoGrupo = bodegas.get(i).getGrupo();
				idBodega = bodegas.get(i).getIdBodega();

				InputInventarioGrupos grupo = new InputInventarioGrupos();
				List<InputInventarioArticulos> listArticulos = new ArrayList<InputInventarioArticulos>();

				for (InputInventarioArticulos itemArticulo : articulos) {
					if (itemArticulo.getGrupo() != null && itemArticulo.getGrupo().equals(tipoGrupo)
							&& itemArticulo.getIdBodega() != null && itemArticulo.getIdBodega().equals(idBodega)) {
						itemArticulo.setIdBodega(null);
						itemArticulo.setIdSolicitud(null);
						itemArticulo.setIdAsignacionReserva(null);
						itemArticulo.setGrupo(null);
						itemArticulo.setEstado(null);

						listArticulos.add(itemArticulo);
					}
				}

				if (listArticulos.size() > 0) {
					grupo.setIdBodega(idBodega);
					grupo.setGrupo(tipoGrupo);
					grupo.setArticulos(listArticulos);
					grupos.add(grupo);
				}
			}
			// --------------------------------------------------------------------

			// --------------------------------------------------------------------
			for (int i = 0; i < datosBodegas.size(); i++) {
				idBodega = datosBodegas.get(i).getIdBodega();

				bodega = new InputInventarioBodegas();
				List<InputInventarioGrupos> listGrupos = new ArrayList<InputInventarioGrupos>();

				for (InputInventarioGrupos itemGrupo : grupos) {
					if (itemGrupo.getIdBodega() != null && itemGrupo.getIdBodega().equals(idBodega)) {
						itemGrupo.setIdBodega(null);
						listGrupos.add(itemGrupo);
					}
				}

				if (listGrupos.size() > 0) {
					bodega.setIdBodega(idBodega);
					bodega.setNombreBodega(datosBodegas.get(i).getNombreBodega());
					bodega.setNivel(datosBodegas.get(i).getNivel());
					bodega.setGrupos(listGrupos);
					listBodegas.add(bodega);
				}
			}
			// --------------------------------------------------------------------

			itemInventario.setBodegas(listBodegas);
			inventario.add(itemInventario);
			output = new OutputInventario();
			output.setRespuesta(respuesta);
			output.setInventario(inventario);
		}

		return output;
	}

	private static List<InputInventarioBodegas> getBodegas(Connection conn, String idBodega, String codArea,
			BigDecimal idPais) throws SQLException {
		String nombreMetodo = "getBodegas";
		String nombreClase = new CurrentClassGetter().getClassName();

		InputInventarioBodegas item = new InputInventarioBodegas();
		List<InputInventarioBodegas> list = new ArrayList<InputInventarioBodegas>();
		String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, codArea);

		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String sql = querygetBodegas(idBodega, codArea);

		log.debug("QryBodegas: " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, estadoAlta);
			pstmt.setBigDecimal(2, idPais);
			rst = pstmt.executeQuery();

			if (rst != null) {
				if (!rst.next()) {
					log.debug("No existen registros en la tabla con esos par\u00E9metros.");

					Respuesta respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_INVENTARIO_397,
							null, nombreClase, nombreMetodo, null, codArea);

					item.setNombreBodega(respuesta.getDescripcion());
				} else {
					do {
						item = new InputInventarioBodegas();
						item.setIdBodega(rst.getString(BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID));
						item.setNombreBodega(rst.getString(BodegaVirtual.CAMPO_NOMBRE));
						item.setNivel(rst.getString(BodegaVirtual.CAMPO_NIVEL));

						list.add(item);
					} while (rst.next());
				}
			}
		} finally {

			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return list;
	}

	public static String querygetBodegas(String idBodega, String codArea) {
		String sql = "SELECT TCSCBODEGAVIRTUALID, NOMBRE, NIVEL FROM "
				+ ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", codArea)
				+ " H  WHERE UPPER(H.ESTADO) =? AND H.TCSCCATPAISID = ?";

		sql += (idBodega != null && !idBodega.trim().equals("")) ? " AND TCSCBODEGAVIRTUALID = " + idBodega : "";
		return sql;
	}

	public static OutputInventario doGetVersionWeb(Connection conn, InputConsultaInventario input, BigDecimal idPais)
			throws SQLException {
		String nombreMetodo = "doGetVersionWeb";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta = null;
		OutputInventario output = null;
		String idArticulo = "";
		String idBodega = "";
		String tipoGrupo = "";
		String descripcion = "";
		String estado = "";
		String idSolicitud = "";
		String idAsignacionReserva = "";
		String tipoInv = "";
		int min = (input.getMin() != null && !input.getMin().equals("")) ? new Integer(input.getMin()) : 0;
		int max = (input.getMax() != null && !input.getMax().equals("")) ? new Integer(input.getMax()) : 0;
		int mostrarDetalle = (input.getMostrarDetalle() != null && !input.getMostrarDetalle().equals(""))
				? new Integer(input.getMostrarDetalle())
				: 1;
		int estadoAltaBool = new Integer(
				UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA_BOOL, input.getCodArea()));

		String vendedor = "NULL";

		if (input.getIdVendedor() != null && !input.getIdVendedor().trim().equals("")) {
			vendedor = input.getIdVendedor();
		}

		String campos[] = CtrlInventario.obtenerCamposGet(mostrarDetalle, estadoAltaBool, vendedor);
		List<Filtro> condiciones = CtrlInventario.obtenerCondiciones(conn, input, idPais);

		// Filtro para evitar articulos de bonos del inventario
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND_NEQ, Inventario.CAMPO_TIPO_GRUPO_SIDRA,
				Conf.TIPO_GRUPO_BONO));

		List<Order> orden = new ArrayList<Order>();
		orden.add(new Order(Inventario.CAMPO_CANTIDAD, Order.DESC));
		orden.add(new Order(Inventario.CAMPO_DESCRIPCION, Order.ASC));
		List<Map<String, String>> datosArticulos = new ArrayList<Map<String, String>>();
		List<Map<String, String>> datosInventario = new ArrayList<Map<String, String>>();
		if (mostrarDetalle == estadoAltaBool) {
			// se muestra todo normal
			String sql = UtileriasBD.armarQrySelect(ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION,
					input.getIdBodega(), input.getCodArea()), campos, condiciones, null, orden, min, max);
			Statement stm = null;
			ResultSet rst = null;
			try {
				stm = conn.createStatement();
				rst = stm.executeQuery(sql);
				if (rst.next()) {
					do {
						Map<String, String> datos = new HashMap<String, String>();
						Map<String, String> datosCopia = new HashMap<String, String>();

						for (int i = 0; i < campos.length; i++) {
							String alias;
							if (campos[i].contains(" AS ")) {
								alias = campos[i].split(" AS ")[1];
							} else {
								alias = campos[i];
							}

							datos.put(alias, rst.getString(i + 1));
							datosCopia.put(alias, rst.getString(i + 1));
						}
						datosArticulos.add(datos);
						datosInventario.add(datosCopia);
					} while (rst.next());
				}
			} finally {
				DbUtils.closeQuietly(rst);
				DbUtils.closeQuietly(stm);
			}

		} else {
			// no se muestran detalles

			String groupBy[] = { Inventario.CAMPO_ARTICULO, Inventario.CAMPO_TCSCBODEGAVIRTUALID,
					Inventario.CAMPO_DESCRIPCION, Inventario.CAMPO_SERIADO, Inventario.CAMPO_TIPO_INV,
					Inventario.CAMPO_TIPO_GRUPO_SIDRA, Inventario.CAMPO_TCSCCATPAISID };

			datosArticulos = UtileriasBD.getPaginatedData(conn, Inventario.N_TABLA, campos, condiciones, groupBy, orden,
					min, max);
			Map<String, String> o = new HashMap<String, String>();
			o.put("STUB", "STUB");
			datosInventario.add(o);
		}

		if (datosArticulos == null || datosArticulos.size() < 1 || datosArticulos.isEmpty() || datosInventario == null
				|| datosInventario.size() < 1 || datosInventario.isEmpty()) {
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_INVENTARIO_397, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			output = new OutputInventario();
			output.setRespuesta(respuesta);
		} else {
			respuesta = new Respuesta();
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			List<InputInventarioArticulos> articulos = new ArrayList<InputInventarioArticulos>();
			InputInventarioArticulos articulo = new InputInventarioArticulos();

			List<InputDatosInventario> detalles = new ArrayList<InputDatosInventario>();
			InputDatosInventario detalle = new InputDatosInventario();

			// --------------------------------------------------------------------
			for (int i = 0; i < datosArticulos.size(); i++) {
				articulo = new InputInventarioArticulos();
				detalles = new ArrayList<InputDatosInventario>();

				idBodega = datosArticulos.get(i).get(Inventario.CAMPO_TCSCBODEGAVIRTUALID);
				tipoGrupo = datosArticulos.get(i).get(Inventario.CAMPO_TIPO_GRUPO_SIDRA);
				idArticulo = datosArticulos.get(i).get(Inventario.CAMPO_ARTICULO);
				descripcion = datosArticulos.get(i).get(Inventario.CAMPO_DESCRIPCION);
				estado = datosArticulos.get(i).get(Inventario.CAMPO_ESTADO);
				idSolicitud = UtileriasJava.getValue(datosArticulos.get(i).get(Inventario.CAMPO_TCSCSOLICITUDID));
				idAsignacionReserva = UtileriasJava
						.getValue(datosArticulos.get(i).get(Inventario.CAMPO_TCSCASIGNACIONRESERVAID));
				tipoInv = datosArticulos.get(i).get(Inventario.CAMPO_TIPO_INV);

				articulo.setIdBodega(idBodega);
				articulo.setGrupo(tipoGrupo);
				articulo.setIdArticulo(idArticulo);
				articulo.setDescripcion(descripcion);
				articulo.setCantidad(datosArticulos.get(i).get(Inventario.CAMPO_CANTIDAD));
				articulo.setSeriado(datosArticulos.get(i).get(Inventario.CAMPO_SERIADO));
				articulo.setEstado(estado);
				articulo.setIdSolicitud(idSolicitud);
				articulo.setIdAsignacionReserva(idAsignacionReserva);
				articulo.setTipoInv(tipoInv);
				articulo.setTecnologia(UtileriasJava.getValue(datosArticulos.get(i).get(Inventario.CAMPO_TECNOLOGIA)));
				articulo.setPrecioScl(UtileriasJava.getValue(datosArticulos.get(i).get("PRECIO")));

				if (mostrarDetalle == estadoAltaBool) {
					for (int j = 0; j < datosInventario.size(); j++) {
						boolean cond1 = (idArticulo.equals(datosInventario.get(j).get(Inventario.CAMPO_ARTICULO))
								&& idBodega.equals(datosInventario.get(j).get(Inventario.CAMPO_TCSCBODEGAVIRTUALID))
								&& descripcion.equals(datosInventario.get(j).get(Inventario.CAMPO_DESCRIPCION)));

						boolean cond2 = (tipoGrupo.equals(datosInventario.get(j).get(Inventario.CAMPO_TIPO_GRUPO_SIDRA))
								&& estado.equals(datosInventario.get(j).get(Inventario.CAMPO_ESTADO))
								&& tipoInv.equals(datosInventario.get(j).get(Inventario.CAMPO_TIPO_INV)));

						boolean cond3 = (idSolicitud
								.equals(datosInventario.get(j).get(Inventario.CAMPO_TCSCSOLICITUDID))
								|| idSolicitud.equals(""));

						boolean cond4 = (idAsignacionReserva
								.equals(datosInventario.get(j).get(Inventario.CAMPO_TCSCASIGNACIONRESERVAID))
								|| idAsignacionReserva.equals(""));
						detalle = new InputDatosInventario();
						if (cond1 && cond2 && cond3 && cond4) {
							detalle.setIdInventario(datosInventario.get(j).get(Inventario.N_TABLA_ID));
							detalle.setIdSolicitud(datosInventario.get(j).get(Inventario.CAMPO_TCSCSOLICITUDID));
							detalle.setIdAsignacionReserva(
									datosInventario.get(j).get(Inventario.CAMPO_TCSCASIGNACIONRESERVAID));
							detalle.setCantidad(datosInventario.get(j).get(Inventario.CAMPO_CANTIDAD));
							detalle.setSerie(datosInventario.get(j).get(Inventario.CAMPO_SERIE));
							detalle.setEstadoComercial(datosInventario.get(j).get(Inventario.CAMPO_ESTADO_COMERCIAL));
							detalle.setSerieAsociada(datosInventario.get(j).get(Inventario.CAMPO_SERIE_ASOCIADA));
							detalle.setTipoInv(datosInventario.get(j).get(Inventario.CAMPO_TIPO_INV));
							detalle.setNumTraspasoScl(datosInventario.get(j).get(Inventario.CAMPO_NUM_TRASPASO_SCL));
							if (datosInventario.get(j).get(Inventario.CAMPO_ICC) == null
									|| "".equals(datosInventario.get(j).get(Inventario.CAMPO_ICC))) {
								detalle.setIcc("");
							} else {
								detalle.setIcc(datosInventario.get(j).get(Inventario.CAMPO_ICC));
							}

							if (datosInventario.get(j).get(Inventario.CAMPO_IMEI) == null
									|| "".equals(datosInventario.get(j).get(Inventario.CAMPO_IMEI))) {
								detalle.setImei("");
							} else {
								detalle.setImei(datosInventario.get(j).get(Inventario.CAMPO_IMEI));
							}
							detalle.setEstado(datosInventario.get(j).get(Inventario.CAMPO_ESTADO));
							detalle.setCreado_el(UtileriasJava
									.formatStringDate(datosInventario.get(j).get(Inventario.CAMPO_CREADO_EL)));
							detalle.setCreado_por(datosInventario.get(j).get(Inventario.CAMPO_CREADO_POR));
							detalle.setModificado_el(UtileriasJava
									.formatStringDate(datosInventario.get(j).get(Inventario.CAMPO_MODIFICADO_EL)));
							detalle.setModificado_por(datosInventario.get(j).get(Inventario.CAMPO_MODIFICADO_POR));

							detalles.add(detalle);
							datosInventario.get(j).clear();
						}
					}

					if (detalles.size() > 0) {
						articulo.setDetalleArticulo(detalles);
						articulos.add(articulo);
					}
				} else {
					articulos.add(articulo);
				}
			}
			// --------------------------------------------------------------------

			output = new OutputInventario();
			output.setRespuesta(respuesta);
			output.setArticulos(articulos);
		}

		return output;
	}
}
