package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.dispositivo.InputDispositivo;
import com.consystec.sc.ca.ws.input.jornada.InputJornada;
import com.consystec.sc.ca.ws.input.jornada.InputObservacionesJornada;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.remesa.InputRemesa;
import com.consystec.sc.ca.ws.input.solicitud.InputArticuloSolicitud;
import com.consystec.sc.ca.ws.input.solicitud.InputSolicitud;
import com.consystec.sc.ca.ws.input.solicitud.InputTipoSolicitud;
import com.consystec.sc.ca.ws.input.solicitud.RespuestaSolicitud;
import com.consystec.sc.ca.ws.input.venta.DetallePago;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.jornada.OutputJornada;
import com.consystec.sc.ca.ws.output.solicitud.OutputSolicitud;
import com.consystec.sc.sv.ws.metodos.CtrlJornadaMasiva;
import com.consystec.sc.sv.ws.metodos.CtrlSolicitud;
import com.consystec.sc.sv.ws.orm.Solicitud;
import com.consystec.sc.sv.ws.orm.SolicitudDet;
import com.consystec.sc.sv.ws.orm.SolicitudObservaciones;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.orm.VendedorPDV;
import com.consystec.sc.sv.ws.orm.BuzonSidra;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.ConfiguracionFolioVirtual;
import com.consystec.sc.sv.ws.orm.Cuenta;
import com.consystec.sc.sv.ws.orm.DetallePagoDeuda;
import com.consystec.sc.sv.ws.orm.Dispositivo;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.NumRecarga;
import com.consystec.sc.sv.ws.orm.Remesa;
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
public class OperacionSolicitud extends ControladorBase {
	private static final Logger log = Logger.getLogger(OperacionSolicitud.class);
	private static List<LogSidra> listaLog = new ArrayList<LogSidra>();
	private static String servicioPostSolicitud = Conf.LOG_POST_SOLICITUDES;
	private static String servicioGetSolicitud = Conf.LOG_GET_SOLICITUDES;
	private static String servicioPutSolicitud = Conf.LOG_PUT_SOLICITUDES;

	// ========================= Operaciones para consultar solicitudes
	// ===================//
	/**
	 * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @param tipoPDV
	 * @param tipoRuta
	 * @param tipoPanel
	 * @param tipoDeuda
	 * @param estadoAlta
	 * @return OutputSolicitud
	 * @throws SQLException
	 */
	public static OutputSolicitud doGet(Connection conn, InputSolicitud input, int metodo, String tipoPanel,
			String tipoRuta, String tipoPDV, String tipoDeuda, String estadoAlta, BigDecimal ID_PAIS)
			throws SQLException {
		String nombreMetodo = "doGet";
		String nombreClase = new CurrentClassGetter().getClassName();

		List<InputSolicitud> list = new ArrayList<InputSolicitud>();
		List<InputTipoSolicitud> listTipos = new ArrayList<InputTipoSolicitud>();

		Respuesta respuesta = null;
		OutputSolicitud output = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		List<Filtro> condiciones = new ArrayList<Filtro>();
		String sql = null;
		String tasaCambio = null;
		String totalDeuda = null;

		try {
			String[] campos = CtrlSolicitud.obtenerCamposGet(tipoPanel, tipoRuta, tipoPDV, ID_PAIS);

			condiciones = CtrlSolicitud.obtenerCondiciones(input, metodo, ID_PAIS);

			sql = UtileriasBD.armarQuerySelect(
					getParticion(Solicitud.N_TABLA, Conf.PARTITION, "", input.getCodArea()) + " S", campos,
					condiciones);
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst != null) {
				if (!rst.next()) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_SOLICITUDES_825, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					output = new OutputSolicitud();
					output.setRespuesta(respuesta);
				} else {
					respuesta = new Respuesta();
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					List<InputArticuloSolicitud> listArticulos = getDatosTablaHija(conn, metodo, null,
							input.getCodArea());
					Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSO_HIJO, null, nombreClase,
							nombreMetodo, null, input.getCodArea());

					do {
						InputSolicitud item = new InputSolicitud();
						String idSolicitud = rst.getString(Solicitud.CAMPO_TCSCSOLICITUDID);

						item.setIdBuzon(
								UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Solicitud.CAMPO_TCSCBUZONID));
						item.setNombreBuzon(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMB_BUZON"));
						item.setIdBuzonAnterior(
								UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Solicitud.CAMPO_BUZON_ANTERIOR));
						item.setIdSolicitud(idSolicitud);
						item.setTipoSolicitud(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Solicitud.CAMPO_TIPO_SOLICITUD));
						item.setIdDTS(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Solicitud.CAMPO_TCSCDTSID));
						item.setNombreDTS(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMB_DTS"));
						item.setIdBodega(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO,
								Solicitud.CAMPO_TCSCBODEGAVIRTUALID));
						item.setIdBodegaZona(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "IDBODEGAZONA"));
						item.setNombreBodegaZona(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBREBODEGAZONA"));
						item.setIdVendedor(
								UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Solicitud.CAMPO_IDVENDEDOR));
						item.setNombreVendedor(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, VendedorDTS.CAMPO_NOMBRE));
						item.setApellidoVendedor(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, VendedorDTS.CAMPO_APELLIDO));
						item.setUsuarioVendedor(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, VendedorDTS.CAMPO_USUARIO));
						item.setBuzonOrigen(
								UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Solicitud.CAMPO_BUZON_ORIGEN));
						item.setFecha(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Solicitud.CAMPO_FECHA));
						item.setEstado(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Solicitud.CAMPO_ESTADO));
						item.setCausaSolicitud(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Solicitud.CAMPO_CAUSA_SOLICITUD));
						item.setOrigen(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Solicitud.CAMPO_ORIGEN));
						item.setSeriado(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Solicitud.CAMPO_SERIADO));
						item.setObservaciones(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Solicitud.CAMPO_OBSERVACIONES));
						item.setCreado_el(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Solicitud.CAMPO_CREADO_EL));
						item.setCreado_por(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Solicitud.CAMPO_CREADO_POR));
						item.setModificado_el(
								UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Solicitud.CAMPO_MODIFICADO_EL));
						item.setModificado_por(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Solicitud.CAMPO_MODIFICADO_POR));

						item.setTipoSiniestro(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Solicitud.CAMPO_TIPO_SINIESTRO));
						item.setIdJornada(
								UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Solicitud.CAMPO_IDJORNADA));
						item.setIdTipo(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Solicitud.CAMPO_IDTIPO));
						item.setTipo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Solicitud.CAMPO_TIPO));
						item.setNombreTipo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRETIPO"));

						item.setOrigenCancelacion(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Solicitud.CAMPO_ORIGEN_CANCELACION));
						item.setObsCancelacion(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Solicitud.CAMPO_OBS_CANCELACION));

						if (rst.getString("NOMBRE_BODEGA") == null || rst.getString("NOMBRE_BODEGA").equals("")
								|| rst.getString("NOMBRE_BODEGA").equals("NULL")) {
							item.setNombreBodega("");
						} else {
							item.setNombreBodega(rst.getString("NOMBRE_BODEGA"));
						}

						tasaCambio = UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Solicitud.CAMPO_TASA_CAMBIO);
						if (tasaCambio == null || tasaCambio.trim().equals("")) {
							tasaCambio = "1";
							item.setTasaCambio("");
						} else {
							item.setTasaCambio(tasaCambio);
						}
						totalDeuda = UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Solicitud.CAMPO_TOTAL_DEUDA);
						if (totalDeuda.equals("")) {
							item.setTotalDeuda("");
						} else {
							item.setTotalDeuda(UtileriasJava.convertirMoneda(totalDeuda, tasaCambio));
						}

						List<InputArticuloSolicitud> articulos = new ArrayList<InputArticuloSolicitud>();

						if (item.getTipoSolicitud().equalsIgnoreCase(tipoDeuda)) {
							// si es tipo deuda se obtienen las observaciones
							item.setObsSolicitud(getObservacionesSolicitud(conn, idSolicitud));

							// se obtienen las remesas de la deuda
							item.setRemesas(getRemesas(conn, idSolicitud, estadoAlta, input.getCodArea(), ID_PAIS));

							// se obtienen el detalle de las formas de pago
							item.setDetallePagos(getDetallePago(conn, idSolicitud));

							// se setea el listado de articulos vacio
							InputArticuloSolicitud configVacio = new InputArticuloSolicitud();
							configVacio.setEstado(r.getDescripcion());
							articulos.add(configVacio);

							item.setArticulos(articulos);

						} else {
							// si no es tipo deuda se obtienen los art\u00EDculos
							for (int j = 0; j < listArticulos.size(); j++) {
								if (idSolicitud.equals(listArticulos.get(j).getIdSolicitud())) {
									listArticulos.get(j).setIdSolicitud(null);
									articulos.add(listArticulos.get(j));
								}
							}

							item.setArticulos(articulos);

							if (articulos.size() == 0) {
								InputArticuloSolicitud configVacio = new InputArticuloSolicitud();
								configVacio.setEstado(r.getDescripcion());
								articulos.add(configVacio);

								item.setArticulos(articulos);
							}
						}

						list.add(item);
					} while (rst.next());

					List<Filtro> condicionesTipo = new ArrayList<Filtro>();
					condicionesTipo.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
							Catalogo.CAMPO_TC_SC_CATPAIS_ID, ID_PAIS.toString()));
					condicionesTipo.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO,
							Conf.GRUPO_SOLICITUDES_TIPO));

					List<Order> orden = new ArrayList<Order>();
					orden.add(new Order(Catalogo.CAMPO_VALOR, Order.ASC));

					List<String> listadoTipos = UtileriasBD.getOneField(conn, Catalogo.CAMPO_NOMBRE, Catalogo.N_TABLA,
							condicionesTipo, orden);
					for (String tipo : listadoTipos) {
						InputTipoSolicitud t = new InputTipoSolicitud();
						List<InputSolicitud> solicitudesTipo = new ArrayList<InputSolicitud>();
						for (InputSolicitud solicitud : list) {
							if (solicitud.getTipoSolicitud().equalsIgnoreCase(tipo)) {
								solicitudesTipo.add(solicitud);
							}
						}
						if (solicitudesTipo.size() > 0) {
							t.setTipoSolicitud(tipo);
							t.setListaSolicitudes(solicitudesTipo);
							listTipos.add(t);
						}
					}

					output = new OutputSolicitud();
					output.setRespuesta(respuesta);
					output.setSolicitudes(listTipos);
				}
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		listaLog = new ArrayList<LogSidra>();
		String filtros = "Se consultaron solicitudes sin filtros.";
		if (condiciones.size() > 0) {
			filtros = "Se consultaron solicitudes con los filtros: " + sql.split("WHERE")[1];
		}
		listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGetSolicitud, "0",
				Conf.LOG_TIPO_NINGUNO, filtros.replace("'", "") + ". " + respuesta.getDescripcion(), ""));

		UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

		return output;
	}

	private static List<DetallePago> getDetallePago(Connection conn, String idSolicitud) throws SQLException {
		List<DetallePago> list = new ArrayList<DetallePago>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		try {
			String campos[] = { DetallePagoDeuda.CAMPO_FORMA_PAGO, DetallePagoDeuda.CAMPO_MONTO,
					DetallePagoDeuda.CAMPO_ESTADO, DetallePagoDeuda.CAMPO_CREADO_EL, DetallePagoDeuda.CAMPO_CREADO_POR,
					DetallePagoDeuda.CAMPO_MODIFICADO_EL, DetallePagoDeuda.CAMPO_MODIFICADO_POR };

			List<Filtro> condiciones = new ArrayList<Filtro>();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					DetallePagoDeuda.CAMPO_TCSCSOLICITUDID, idSolicitud));

			String sql = UtileriasBD.armarQuerySelect(DetallePagoDeuda.N_TABLA, campos, condiciones);

			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst != null) {
				if (!rst.next()) {
					list.add(new DetallePago());
				} else {
					do {
						DetallePago item = new DetallePago();
						item.setFormaPago(rst.getString(DetallePagoDeuda.CAMPO_FORMA_PAGO));
						item.setMonto(rst.getDouble(DetallePagoDeuda.CAMPO_MONTO) + "");
						item.setEstado(rst.getString(DetallePagoDeuda.CAMPO_ESTADO));
						item.setCreado_el(
								UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, DetallePagoDeuda.CAMPO_CREADO_EL));
						item.setCreado_por(rst.getString(DetallePagoDeuda.CAMPO_CREADO_POR));
						item.setModificado_el(
								UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, DetallePagoDeuda.CAMPO_MODIFICADO_EL));
						item.setModificado_por(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, DetallePagoDeuda.CAMPO_MODIFICADO_POR));

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

	/**
	 * Funci\u00F3n que obtiene los datos relacionados de la tabla hija mediante el
	 * id de la tabla padre.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @return OutputBodegaDTS
	 * @throws SQLException
	 */
	private static List<InputArticuloSolicitud> getDatosTablaHija(Connection conn, int metodo, String idSolicitud,
			String codArea) throws SQLException {
		String nombreMetodo = "getDatosTablaHija";
		String nombreClase = new CurrentClassGetter().getClassName();

		List<InputArticuloSolicitud> list = new ArrayList<InputArticuloSolicitud>();
		PreparedStatement pstmtIn = null;
		ResultSet rstIn = null;

		try {
			String[] camposInterno = CtrlSolicitud.obtenerCamposTablaHija(metodo);

			String sql = "";
			if (idSolicitud != null) {
				List<Filtro> condiciones = new ArrayList<Filtro>();
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						SolicitudDet.CAMPO_TCSCSOLICITUDID, idSolicitud));
				sql = UtileriasBD.armarQuerySelect(SolicitudDet.N_TABLA, camposInterno, condiciones);
			} else {
				sql = UtileriasBD.armarQuerySelect(SolicitudDet.N_TABLA, camposInterno, null);
			}

			pstmtIn = conn.prepareStatement(sql);
			rstIn = pstmtIn.executeQuery();

			if (rstIn != null) {
				if (!rstIn.next()) {
					log.debug("No existen registros en la tabla hija con esos par\u00E9metros.");
					InputArticuloSolicitud item = new InputArticuloSolicitud();

					Respuesta respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSO_HIJO, null,
							nombreClase, nombreMetodo, null, codArea);

					item.setEstado(respuesta.getDescripcion());
					list.add(item);
				} else {
					do {
						InputArticuloSolicitud item = new InputArticuloSolicitud();
						item.setIdSolicitudDet(rstIn.getString(SolicitudDet.CAMPO_TCSCSOLICITUDDETID));
						item.setIdSolicitud(rstIn.getString(SolicitudDet.CAMPO_TCSCSOLICITUDID));
						item.setCodDispositivo(
								UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO, SolicitudDet.CAMPO_COD_DISPOSITIVO));
						item.setIdArticulo(rstIn.getString(SolicitudDet.CAMPO_ARTICULO) == null ? "NULL"
								: rstIn.getString(SolicitudDet.CAMPO_ARTICULO));
						item.setDescripcion(rstIn.getString(SolicitudDet.CAMPO_DESCRIPCION) == null
								? "No se encontr\u00F3 el nombre del art\u00EDculo."
								: rstIn.getString(SolicitudDet.CAMPO_DESCRIPCION));
						item.setSerie(rstIn.getString(SolicitudDet.CAMPO_SERIE) == null ? "NULL"
								: rstIn.getString(SolicitudDet.CAMPO_SERIE));
						item.setSerieFinal(rstIn.getString(SolicitudDet.CAMPO_SERIE_FINAL) == null ? "NULL"
								: rstIn.getString(SolicitudDet.CAMPO_SERIE_FINAL));
						item.setSerieAsociada(
								UtileriasJava.getValue(rstIn.getString(SolicitudDet.CAMPO_SERIE_ASOCIADA)));
						item.setCantidad(rstIn.getString(SolicitudDet.CAMPO_CANTIDAD));
						item.setObservaciones(rstIn.getString(SolicitudDet.CAMPO_OBSEVACIONES));
						item.setEstado(rstIn.getString(SolicitudDet.CAMPO_ESTADO));
						item.setCreado_por(rstIn.getString(SolicitudDet.CAMPO_CREADO_POR));
						item.setCreado_el(
								UtileriasJava.formatStringDate(rstIn.getString(SolicitudDet.CAMPO_CREADO_EL)));
						item.setModificado_por(rstIn.getString(SolicitudDet.CAMPO_MODIFICADO_POR));
						item.setModificado_el(
								UtileriasJava.formatStringDate(rstIn.getString(SolicitudDet.CAMPO_MODIFICADO_EL)));

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

	private static List<InputRemesa> getRemesas(Connection conn, String idSolicitud, String estadoAlta, String codArea,
			BigDecimal ID_PAIS) throws SQLException {
		List<InputRemesa> list = new ArrayList<InputRemesa>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		try {
			String campos[] = { Remesa.CAMPO_TCSCREMESAID, Remesa.CAMPO_MONTO, Remesa.CAMPO_TASA_CAMBIO,
					Remesa.CAMPO_NO_BOLETA, Remesa.CAMPO_BANCO, Remesa.CAMPO_ESTADO, Remesa.CAMPO_CREADO_EL,
					Remesa.CAMPO_CREADO_POR, Remesa.CAMPO_MODIFICADO_EL, Remesa.CAMPO_MODIFICADO_POR };

			List<Filtro> condiciones = new ArrayList<Filtro>();
			condiciones.add(
					UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Remesa.CAMPO_TCSCSOLICITUDID, idSolicitud));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Remesa.CAMPO_TCSCCATPAISID,
					ID_PAIS.toString()));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Remesa.CAMPO_ESTADO, estadoAlta));

			String sql = UtileriasBD.armarQuerySelect(
					ControladorBase.getParticion(Remesa.N_TABLA, Conf.PARTITION, "", codArea), campos, condiciones);

			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst != null) {
				if (!rst.next()) {
					list.add(new InputRemesa());
				} else {
					do {
						InputRemesa item = new InputRemesa();
						item.setIdRemesa(rst.getString(Remesa.CAMPO_TCSCREMESAID));
						item.setTasaCambio(rst.getString(Remesa.CAMPO_TASA_CAMBIO));
						item.setMonto(
								UtileriasJava.convertirMoneda(rst.getString(Remesa.CAMPO_MONTO), item.getTasaCambio()));
						item.setNoBoleta(rst.getString(Remesa.CAMPO_NO_BOLETA));
						item.setBanco(rst.getString(Cuenta.CAMPO_BANCO));
						item.setEstado(rst.getString(Remesa.CAMPO_ESTADO));
						item.setCreado_el(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Remesa.CAMPO_CREADO_EL));
						item.setCreado_por(rst.getString(Remesa.CAMPO_CREADO_POR));
						item.setModificado_el(
								UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Remesa.CAMPO_MODIFICADO_EL));
						item.setModificado_por(
								UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Remesa.CAMPO_MODIFICADO_POR));

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

	private static List<InputObservacionesJornada> getObservacionesSolicitud(Connection conn, String idSolicitud)
			throws SQLException {
		List<InputObservacionesJornada> list = null;

		PreparedStatement pstmt = null;
		ResultSet rst = null;

		try {
			String[] campos = { SolicitudObservaciones.CAMPO_OBSERVACION, SolicitudObservaciones.CAMPO_CREADO_EL,
					SolicitudObservaciones.CAMPO_CREADO_POR };

			List<Filtro> condiciones = new ArrayList<Filtro>();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					SolicitudObservaciones.CAMPO_TCSCSOLICITUD, idSolicitud));
			String sql = UtileriasBD.armarQuerySelect(SolicitudObservaciones.N_TABLA, campos, condiciones);

			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				list = new ArrayList<InputObservacionesJornada>();
				do {
					InputObservacionesJornada item = new InputObservacionesJornada();
					item.setObservacion(rst.getString(SolicitudObservaciones.CAMPO_OBSERVACION));
					item.setCreado_el(
							UtileriasJava.formatStringDate(rst.getString(SolicitudObservaciones.CAMPO_CREADO_EL)));
					item.setCreado_por(rst.getString(SolicitudObservaciones.CAMPO_CREADO_POR));

					list.add(item);
				} while (rst.next());
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return list;
	}

	private static List<InputArticuloSolicitud> getInventarioCancelacion(Connection conn, String idSolicitud)
			throws SQLException {

		List<InputArticuloSolicitud> list = null;
		PreparedStatement pstmtIn = null;
		ResultSet rstIn = null;

		String[] camposInterno = CtrlSolicitud.obtenerCamposTablaHija(Conf.METODO_GET);

		List<Filtro> condiciones = new ArrayList<Filtro>();

		try {
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, SolicitudDet.CAMPO_TCSCSOLICITUDID,
					idSolicitud));
			condiciones
					.add(UtileriasJava.setCondicion(Conf.FILTRO_IS_NULL_AND, SolicitudDet.CAMPO_COD_DISPOSITIVO, null));

			String sql = UtileriasBD.armarQuerySelect(SolicitudDet.N_TABLA, camposInterno, condiciones);

			pstmtIn = conn.prepareStatement(sql);
			rstIn = pstmtIn.executeQuery();

			if (rstIn.next()) {
				list = new ArrayList<InputArticuloSolicitud>();

				do {
					InputArticuloSolicitud item = new InputArticuloSolicitud();
					item.setIdSolicitud(rstIn.getString(SolicitudDet.CAMPO_TCSCSOLICITUDID));
					item.setIdArticulo(rstIn.getString(SolicitudDet.CAMPO_ARTICULO));
					item.setSerie(rstIn.getString(SolicitudDet.CAMPO_SERIE));
					item.setSerieFinal(rstIn.getString(SolicitudDet.CAMPO_SERIE_FINAL));
					item.setSerieAsociada(rstIn.getString(SolicitudDet.CAMPO_SERIE_ASOCIADA));
					item.setCantidad(rstIn.getString(SolicitudDet.CAMPO_CANTIDAD));
					item.setTipoInv(rstIn.getString(SolicitudDet.CAMPO_TIPO_INV));
					item.setEstado(rstIn.getString(SolicitudDet.CAMPO_ESTADO));

					list.add(item);
				} while (rstIn.next());
			}
		} finally {
			DbUtils.closeQuietly(rstIn);
			DbUtils.closeQuietly(pstmtIn);
		}

		return list;
	}
	// ========================= Operaciones para consultar solicitudes
	// ===================//

	// ####################################################################################//

	// ========================= Operaciones para crear solicitudes
	// =======================//
	/**
	 * Funci\u00F3n que realiza las tareas para insertar los encabezados de las
	 * solicitudes.
	 * 
	 * @param conn
	 * @param input
	 * @param estadoAlta
	 * @param estadoTrue
	 * @param estadoAbierta
	 * @param estadoDisponible
	 * @param estadoDevolucion
	 * @param estadoPendiente
	 * @param estadoAceptada
	 * @param origenPC
	 * @param origenMovil
	 * @param tipoDevolucion
	 * @param tipoReserva
	 * @param tipoPedido
	 * @param tipoNumPayment
	 * @param tipoSiniestro
	 * @param tipoInvSidra
	 * @param nombreNumPayment
	 * @param tipoPDV
	 * @param tipoPanel
	 * @param tipoSiniestroTotal
	 * @param tipoSiniestroParcial
	 * @param estadoSiniestro
	 * @param estadoIniciada
	 * @param tipoSiniestroDispositivo
	 * @param banderaSolicitudTelca
	 * @param tipoDeuda
	 * @param estadoEnviado
	 * @param banderaSolicitudDTS
	 * @return
	 * @throws SQLException
	 */
	public static OutputSolicitud doPost(Connection conn, InputSolicitud input, String estadoAlta, String estadoTrue,
			String estadoAbierta, String estadoDisponible, String estadoDevolucion, String estadoPendiente,
			String estadoAceptada, String origenPC, String tipoDevolucion, String tipoReserva, String tipoPedido,
			String tipoNumPayment, String tipoSiniestro, String tipoInvSidra, String nombreNumPayment, String tipoPDV,
			String tipoPanel, String tipoSiniestroTotal, String tipoSiniestroParcial, String estadoSiniestro,
			String estadoIniciada, String tipoSiniestroDispositivo, boolean banderaSolicitudTelca, String tipoDeuda,
			String estadoEnviado, boolean banderaSolicitudDTS, String tipoRuta, String tipoArt, BigDecimal ID_PAIS)
			throws SQLException {
		listaLog = new ArrayList<LogSidra>();

		String nombreMetodo = "doPost";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta = null;
		OutputSolicitud output = new OutputSolicitud();
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		boolean insertDetalle = false;
		String detalleError = "";
		int idPadre = 0;
		String idJornadaResponsable = null;
		String idBodegaResponsable = null;

		RespuestaSolicitud respDetalle = new RespuestaSolicitud();
		RespuestaSolicitud respDispositivos = new RespuestaSolicitud();

		boolean banderaOrigenPC = input.getOrigen() != null && input.getOrigen().equalsIgnoreCase(origenPC);
		boolean banderaDevolucion = input.getTipoSolicitud() != null
				&& input.getTipoSolicitud().equalsIgnoreCase(tipoDevolucion);
		boolean banderaSiniestro = input.getTipoSolicitud() != null
				&& input.getTipoSolicitud().equalsIgnoreCase(tipoSiniestro);
		boolean banderaPayment = input.getTipoSolicitud() != null
				&& input.getTipoSolicitud().equalsIgnoreCase(tipoNumPayment);
		boolean banderaSiniestroTotal = input.getTipoSiniestro() != null
				&& input.getTipoSiniestro().equalsIgnoreCase(tipoSiniestroTotal);
		boolean banderaSiniestroParcial = input.getTipoSiniestro() != null
				&& input.getTipoSiniestro().equalsIgnoreCase(tipoSiniestroParcial);
		boolean banderaSiniestroDispositivo = input.getTipoSiniestro() != null
				&& input.getTipoSiniestro().equalsIgnoreCase(tipoSiniestroDispositivo);
		boolean banderaDeuda = input.getTipoSolicitud() != null && input.getTipoSolicitud().equalsIgnoreCase(tipoDeuda);

		try {
			conn.setAutoCommit(false);

			// mientras no sea solicitud payment, se obtienen datos de la jornada
			if (!banderaPayment && !banderaSolicitudTelca && !banderaDeuda
					&& (input.getIdVendedor() != null && !input.getIdVendedor().equals(""))) {
				OutputJornada datosJornada = doOperacionesJornada(conn, input, estadoIniciada, banderaSiniestroTotal,
						ID_PAIS);
				// si el origen es PC no es necesario que posea jornada iniciada
				if (banderaSiniestro || !banderaOrigenPC) {
					// debe tener jornada obligatorio
					if (datosJornada.getJornada() == null) {
						output.setRespuesta(datosJornada.getRespuesta());

						return output;
					} else {
						idJornadaResponsable = UtileriasJava.getValue(datosJornada.getJornada().getIdJornada());
						idBodegaResponsable = UtileriasJava.getValue(datosJornada.getJornada().getIdBodegaVendedor());
					}
				}
			} else if (banderaSiniestroTotal) {
				idBodegaResponsable = input.getIdBodega();
			}

			// se inserta encabezado de solicitud
			String campos[] = CtrlSolicitud.obtenerCamposPost();
			List<String> inserts = CtrlSolicitud.obtenerInsertsPost(conn, input, Solicitud.SEQUENCE, estadoAbierta,
					tipoNumPayment, tipoSiniestro, tipoPDV, idJornadaResponsable, banderaSolicitudTelca, estadoAceptada,
					banderaDeuda, estadoPendiente, estadoEnviado, banderaSolicitudDTS, ID_PAIS);

			sql = UtileriasBD.armarQueryInsert(Solicitud.N_TABLA, campos, inserts);

			String generatedColumns[] = { Solicitud.CAMPO_TCSCSOLICITUDID };
			try {
				pstmt = conn.prepareStatement(sql, generatedColumns);

				pstmt.executeUpdate();
				rs = pstmt.getGeneratedKeys();
				idPadre = 0;
				if (rs.next()) {
					idPadre = rs.getInt(1);
				}
				log.debug("idPadre: " + idPadre);
			} finally {
				DbUtils.closeQuietly(rs);
				DbUtils.closeQuietly(pstmt);
			}

			if (idPadre > 0) {
				// si se insert\u00F3 correctamente el encabezado, se procede a las operaciones
				// de detalles
				if (banderaSiniestro && banderaSiniestroTotal) {
					// todos los articulos del vendedor en estado disponible o devolucion y se
					// cambian a estado siniestrado

					respDetalle = OperacionSolicitud.updateSiniestroTotal(conn, idPadre, idBodegaResponsable,
							estadoDisponible, estadoSiniestro, input.getUsuario(), estadoPendiente, input.getCodArea(),
							ID_PAIS);

					insertDetalle = respDetalle.isResultado();
					detalleError = UtileriasJava.getValue(respDetalle.getDescripcion());

					if (insertDetalle) {
						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud,
								"0", Conf.LOG_TIPO_NINGUNO, "Se cambiaron los art\u00EDculos de la bodega "
										+ idBodegaResponsable + " a estado " + estadoSiniestro.toUpperCase() + ".",
								""));
					}

				} else if (banderaSiniestro && banderaSiniestroParcial) {
					// proceso de insercion de detalle en caso de ser siniestro parcial

					// proceso de articulos disponibles
					if (!input.getArticulos().isEmpty()) {
						respDetalle = doOperacionesDetalle(conn, idPadre, input, banderaOrigenPC, banderaSiniestro,
								banderaDevolucion, estadoAlta, estadoTrue, estadoDisponible, estadoDevolucion,
								estadoPendiente, estadoSiniestro, tipoDevolucion, tipoReserva, tipoPedido,
								tipoNumPayment, tipoSiniestro, tipoInvSidra, tipoPDV, nombreNumPayment, false, false,
								tipoPanel, tipoRuta, tipoArt, ID_PAIS);
					} else {
						respDetalle.setResultado(true);
					}

					// se establecen los valores de respuesta
					insertDetalle = respDetalle.isResultado();
					detalleError = respDetalle.getDescripcion();
					output = respDetalle.getDatos();

				} else if (banderaSiniestro && banderaSiniestroDispositivo) {
					// si es siniestro de dipositivos no se realiza nada con los articulos
					log.trace("es siniestro de dispositivos, no se procesaron art\u00EDculos");

					// seteando este valor simulo la insercion del detalle
					insertDetalle = true;

				} else if (banderaDeuda) {
					// si es deuda dts, no se realiza nada con los articulos
					log.trace("es deuda dts, no se procesaron art\u00EDculos");

					// se insertan las observaciones
					if (input.getObservaciones() != null && !input.getObservaciones().equals("")) {
						insertDetalle = insertarObservaciones(conn, idPadre, input.getObservaciones(),
								input.getUsuario());
					} else {
						insertDetalle = true;
					}

					if (insertDetalle) {
						// se inserta el detalle de pagos
						log.trace("se inserta detalle de pagos de la deuda");
						if (!insertarDetallePagos(conn, idPadre, input.getDetallePagos(), estadoAlta,
								input.getUsuario())) {
							insertDetalle = false;
							detalleError = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_DETPAGO_DEUDA_621,
									null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
						}
					}

				} else {
					// proceso de insercion de detalles en casos de NO ser siniestro (siempre son
					// articulos disponibles)
					respDetalle = doOperacionesDetalle(conn, idPadre, input, banderaOrigenPC, banderaSiniestro,
							banderaDevolucion, estadoAlta, estadoTrue, estadoDisponible, estadoDevolucion,
							estadoPendiente, estadoSiniestro, tipoDevolucion, tipoReserva, tipoPedido, tipoNumPayment,
							tipoSiniestro, tipoInvSidra, tipoPDV, nombreNumPayment, false, false, tipoPanel, tipoRuta,
							tipoArt, ID_PAIS);

					insertDetalle = respDetalle.isResultado();
					detalleError = respDetalle.getDescripcion();
					output = respDetalle.getDatos();
				}

				// se realizan los procesos de actualizacion de dispositivos siniestrados
				if (banderaSiniestro && insertDetalle && input.getDispositivos() != null
						&& !input.getDispositivos().isEmpty()) {
					respDispositivos = OperacionSolicitud.updateDispositivos(conn, idPadre, input, estadoAlta,
							estadoPendiente, ID_PAIS);

					if (!respDispositivos.isResultado()) {
						// ocurrio un error al realizar las operaciones
						listaLog = new ArrayList<LogSidra>();
						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud,
								"0", Conf.LOG_TIPO_NINGUNO, respDispositivos.getRespuesta().getDescripcion(),
								respDispositivos.getRespuesta().getExcepcion()));

						output = new OutputSolicitud();
						detalleError += " " + respDispositivos.getRespuesta().getDescripcion();
					}
				} else {
					log.trace("no se reportaron dispositivos");
					respDispositivos.setResultado(true);
				}

				if (insertDetalle == true && respDispositivos.isResultado()) {
					// todo es correcto
					conn.commit();

					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_SOLICITUD_65, null, nombreClase,
							nombreMetodo, detalleError.trim(), input.getCodArea());

					output.setRespuesta(respuesta);
					output.setIdSolicitud(idPadre + "");

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud,
							idPadre + "", Conf.LOG_TIPO_SOLICITUD, output.getRespuesta().getDescripcion().trim(), ""));

				} else {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
							nombreMetodo, detalleError.trim(), input.getCodArea());

					output.setRespuesta(respuesta);
					conn.rollback();
					log.error("Rollback");

					listaLog = new ArrayList<LogSidra>();
					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud, "0",
							Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion().trim(), ""));
				}
			}

		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			respuesta = new ControladorBase().getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo,
					null, input.getCodArea());

			output = new OutputSolicitud();
			output.setRespuesta(respuesta);

			conn.rollback();
			log.error("Rollback");

			listaLog = new ArrayList<LogSidra>();
			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud, "0",
					Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(), e.getMessage()));

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase,
					nombreMetodo, null, input.getCodArea());

			output = new OutputSolicitud();
			output.setRespuesta(respuesta);

			conn.rollback();
			log.error("Rollback");

			listaLog = new ArrayList<LogSidra>();
			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud, "0",
					Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(), e.getMessage()));

		} finally {
			conn.setAutoCommit(true);

			UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
		}

		return output;
	}

	private static boolean insertarDetallePagos(Connection conn, int idPadre, List<DetallePago> detallePagos,
			String estadoAlta, String usuario) throws SQLException {
		PreparedStatement pstmt = null;
		int res = 0;
		try {
			String campos[] = { DetallePagoDeuda.CAMPO_TCSCDETPAGOID, DetallePagoDeuda.CAMPO_TCSCSOLICITUDID,
					DetallePagoDeuda.CAMPO_FORMA_PAGO, DetallePagoDeuda.CAMPO_MONTO, DetallePagoDeuda.CAMPO_ESTADO,
					DetallePagoDeuda.CAMPO_CREADO_EL, DetallePagoDeuda.CAMPO_CREADO_POR };

			List<String> inserts = new ArrayList<String>();
			for (DetallePago detalle : detallePagos) {
				String insert = UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, DetallePagoDeuda.SEQUENCE,
						Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPadre + "", Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, detalle.getFormaPago(), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, detalle.getMonto(), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, estadoAlta.toUpperCase(), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, usuario, Conf.INSERT_SEPARADOR_NO);
				inserts.add(insert);
			}

			String sql = UtileriasBD.armarQueryInsertAll(DetallePagoDeuda.N_TABLA, campos, inserts);

			pstmt = conn.prepareStatement(sql);
			res = pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
		return res > 0 ? true : false;
	}

	private static boolean insertarObservaciones(Connection conn, int idPadre, String observacion, String usuario)
			throws SQLException {
		PreparedStatement pstmt = null;
		int res = 0;
		try {
			String campos[] = { SolicitudObservaciones.CAMPO_TCSCOBSSOLICITUDID,
					SolicitudObservaciones.CAMPO_TCSCSOLICITUD, SolicitudObservaciones.CAMPO_OBSERVACION,
					SolicitudObservaciones.CAMPO_CREADO_EL, SolicitudObservaciones.CAMPO_CREADO_POR };

			List<String> inserts = new ArrayList<String>();
			String insert = "("
					+ UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, SolicitudObservaciones.SEQUENCE,
							Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPadre + "", Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, observacion, Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, usuario, Conf.INSERT_SEPARADOR_NO) + ") ";
			inserts.add(insert);
			String sql = UtileriasBD.armarQueryInsert(SolicitudObservaciones.N_TABLA, campos, inserts);

			pstmt = conn.prepareStatement(sql);
			res = pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
		return res > 0 ? true : false;
	}

	/**
	 * Funci\u00F3n que ejecuta todos los procedimientos para insertar los detalles
	 * de las solicitudes.
	 * 
	 * @param conn
	 * @param idPadre
	 * @param input
	 * @param tipoDevolucion
	 * @param tipoReserva
	 * @param tipoPedido
	 * @param tipoNumPayment
	 * @param banderaOrigenPC
	 * @param estadoAlta
	 * @param estadoDisponible
	 * @param estadoDevolucion
	 * @param estadoPendiente
	 * @param estadoSiniestro
	 * @param tipoSiniestro
	 * @param siniestroDevolucion
	 * @param estadoCancelada
	 * @param cancelacion
	 * @return
	 * @throws SQLException
	 */
	private static RespuestaSolicitud doPostHijo(Connection conn, int idPadre, InputSolicitud input,
			String tipoDevolucion, String tipoReserva, String tipoPedido, String tipoNumPayment, String estadoAlta,
			String estadoDisponible, String estadoDevolucion, String estadoPendiente, String tipoInvSidra,
			String nombreNumPayment, String tipoPDV, String tipoSiniestro, String estadoSiniestro,
			boolean siniestroDevolucion, boolean esCancelacion, String tipoPanel, String tipoRuta, BigDecimal ID_PAIS)
			throws SQLException {
		RespuestaSolicitud respuesta = new RespuestaSolicitud();

		if (input.getTipoSolicitud().equalsIgnoreCase(tipoPedido)
				|| input.getTipoSolicitud().equalsIgnoreCase(tipoReserva)
				|| input.getTipoSolicitud().equalsIgnoreCase(tipoNumPayment)) {

			String[] campos = CtrlSolicitud.obtenerCamposTablaHija(Conf.METODO_POST);
			List<String> inserts = CtrlSolicitud.obtenerInsertsPostHijo(idPadre, input, SolicitudDet.SEQUENCE,
					tipoNumPayment, tipoInvSidra, nombreNumPayment, estadoPendiente, ID_PAIS);
			String sql = UtileriasBD.armarQueryInsertAll(SolicitudDet.N_TABLA, campos, inserts);

			QueryRunner Qr = new QueryRunner();
			int res = Qr.update(conn, sql);
			if (res > 0) {
				// se actualizan los estados de los numeros de recarga al ser solicitud payment
				if (input.getTipoSolicitud().equalsIgnoreCase(tipoNumPayment)) {
					OperacionSolicitud.updateNumRecarga(conn, idPadre, input, estadoAlta, estadoPendiente, tipoPDV,
							input.getUsuario(), ID_PAIS);
				}

				respuesta.setResultado(true);
				respuesta.setDescripcion("OK");

			} else {
				conn.rollback();
				log.error("Rollback");
				respuesta.setDescripcion("ERROR");
				respuesta.setResultado(false);

				listaLog = new ArrayList<LogSidra>();
				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud, "0",
						Conf.LOG_TIPO_NINGUNO, "Error al actualizar inventario en los detalles de solicitud.", ""));
			}

		} else if (input.getTipoSolicitud().equalsIgnoreCase(tipoDevolucion)
				|| input.getTipoSolicitud().equalsIgnoreCase(tipoSiniestro)) {

			// se operan los articulos
			OutputSolicitud updArticulos = doUpdArticulos(conn, idPadre, input, estadoAlta, estadoDisponible,
					estadoDevolucion, estadoPendiente, tipoDevolucion, tipoInvSidra, tipoSiniestro, estadoSiniestro,
					siniestroDevolucion, esCancelacion, tipoPanel, tipoRuta, ID_PAIS);

			// si los mensajes de respuesta (mas los listados) son correctos retorna ok
			if (updArticulos.getMensaje().equalsIgnoreCase("OK")) {
				// todo bien insertado y actualizado
				respuesta.setResultado(true);
				respuesta.setDescripcion(updArticulos.getMensaje());

				return respuesta;
			} else if (updArticulos.getMensaje().equalsIgnoreCase("GOOD")) {
				// se insertaron varios pero hay algunos que no se insertaron porque no existen
				// en bodega o no tienen stock suficiente
				// se listan lo que tuvieron fallaron y se agregan las observaciones
				respuesta.setResultado(true);
				respuesta.setDescripcion(updArticulos.getMensaje());
				respuesta.setDatos(updArticulos);

				return respuesta;
			} else if (updArticulos.getMensaje().equalsIgnoreCase("ERROR")) {
				// no se inserto nada

				respuesta.setResultado(false);
				respuesta.setDescripcion(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ARTICULOS_NO_VALIDOS_104,
						null, null, null, null, input.getCodArea()).getDescripcion());

				return respuesta;
			} else {
				// Cualquier caso de fallo

				respuesta.setResultado(false);
				respuesta.setDescripcion("ERROR: " + updArticulos.getMensaje().equalsIgnoreCase("ERROR"));
				return respuesta;
			}
		}

		return respuesta;
	}

	/**
	 * Funci\u00F3n para realizar las operaciones concernientes a los art\u00EDculos
	 * disponibles.
	 * 
	 * @param conn
	 * @param idPadre
	 * @param input
	 * @param banderaOrigenPC
	 * @param banderaSiniestro
	 * @param banderaDevolucion
	 * @param estadoAlta
	 * @param estadoTrue
	 * @param estadoDisponible
	 * @param estadoDevolucion
	 * @param estadoPendiente
	 * @param estadoSiniestro
	 * @param tipoDevolucion
	 * @param tipoReserva
	 * @param tipoPedido
	 * @param tipoNumPayment
	 * @param tipoSiniestro
	 * @param tipoInvSidra
	 * @param tipoPDV
	 * @param nombreNumPayment
	 * @param siniestroDevolucion
	 * @param esCancelacion
	 * @param tipoPanel
	 * @return
	 * @throws SQLException
	 * @throws CloneNotSupportedException
	 */
	private static RespuestaSolicitud doOperacionesDetalle(Connection conn, int idPadre, InputSolicitud input,
			boolean banderaOrigenPC, boolean banderaSiniestro, boolean banderaDevolucion, String estadoAlta,
			String estadoTrue, String estadoDisponible, String estadoDevolucion, String estadoPendiente,
			String estadoSiniestro, String tipoDevolucion, String tipoReserva, String tipoPedido, String tipoNumPayment,
			String tipoSiniestro, String tipoInvSidra, String tipoPDV, String nombreNumPayment,
			boolean siniestroDevolucion, boolean esCancelacion, String tipoPanel, String tipoRuta, String tipoArt,
			BigDecimal ID_PAIS) throws SQLException, CloneNotSupportedException {
		/*
		 * Si siniestroDevolucion=true estadoDevolucion y estadoDisponible vienen con
		 * valores diferentes para las operaciones de siniestros de devolucion quedando
		 * de la siguiente forma: Solicitudes Normales (inventario "Disponible" pasa a
		 * "En Proceso Devoluci\u00F3n/Siniestrado"):
		 * estadoDevolucion="En Proceso Devoluci\u00F3n" y estadoDisponible="Disponible"
		 * 
		 * Solicitudes de Siniestro de Devoluci\u00F3n (inventario
		 * "En Proceso Devoluci\u00F3n" pasa a "Siniestrado"):
		 * estadoDevolucion="Siniestrado" y
		 * estadoDisponible="En Proceso Devoluci\u00F3n"
		 * 
		 * estadoSiniestro no cambia en ning\u00FAn aspecto.
		 */

		String nombreMetodo = "doOperacionesDetalle";
		String nombreClase = new CurrentClassGetter().getClassName();

		InputSolicitud inputSeriado;
		InputSolicitud inputNoSeriado;
		List<InputArticuloSolicitud> articulosSeriados = new ArrayList<InputArticuloSolicitud>();
		List<InputArticuloSolicitud> articulosNoSeriados = new ArrayList<InputArticuloSolicitud>();
		RespuestaSolicitud respSolicitudSeriada = new RespuestaSolicitud();
		RespuestaSolicitud respSolicitudNoSeriada;

		RespuestaSolicitud respuesta = new RespuestaSolicitud();
		OutputSolicitud output = new OutputSolicitud();

		boolean insertSeriado = false;
		boolean insertNoSeriado = false;
		boolean insertCompletoSiniestro = true;
		String detalleError = "";

		if (banderaOrigenPC || banderaSiniestro || esCancelacion) {
			inputSeriado = (InputSolicitud) input.clone();
			inputNoSeriado = (InputSolicitud) input.clone();

			for (int i = 0; i < input.getArticulos().size(); i++) {
				if (input.getArticulos().get(i).getSerie() != null
						&& !input.getArticulos().get(i).getSerie().equals("")) {
					articulosSeriados.add(input.getArticulos().get(i));
				} else {
					articulosNoSeriados.add(input.getArticulos().get(i));
				}
			}
			inputSeriado.setArticulos(articulosSeriados);
			inputNoSeriado.setArticulos(articulosNoSeriados);

			if (inputSeriado.getArticulos().size() > 0) {
				respSolicitudSeriada = solicitudSeriada(conn, inputSeriado, idPadre, estadoDisponible, estadoDevolucion,
						estadoAlta, estadoPendiente, tipoDevolucion, tipoInvSidra, tipoSiniestro, estadoSiniestro,
						esCancelacion, tipoArt, ID_PAIS);

				if (respSolicitudSeriada.isResultado()) {
					if (banderaSiniestro && respSolicitudSeriada.getDatos().getSeries() != null
							&& !respSolicitudSeriada.getDatos().getSeries().isEmpty()) {
						insertCompletoSiniestro = false;
					}
					insertSeriado = respSolicitudSeriada.isResultado();
					detalleError = respSolicitudSeriada.getDescripcion();

					output = respSolicitudSeriada.getDatos();
					output.setDescErrorSeries(respSolicitudSeriada.getDatos().getDescErrorSeries());
					output.setSeries(respSolicitudSeriada.getDatos().getSeries());
				} else {
					if (respSolicitudSeriada.getDescripcion().startsWith("ERROR")) {
						respSolicitudSeriada
								.setDescripcion(respSolicitudSeriada.getDescripcion().replace("ERROR: ", ""));

					}

					detalleError = respSolicitudSeriada.getDescripcion();
					insertSeriado = respSolicitudSeriada.isResultado();
					if (banderaSiniestro) {
						insertCompletoSiniestro = false;
					}

					output = respSolicitudSeriada.getDatos();
				}
			} else {
				if (banderaDevolucion) {
					detalleError = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_ARTICULOS_SERIADOS_108, null,
							nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
				}
			}

			if (inputNoSeriado.getArticulos().size() > 0) {
				respSolicitudNoSeriada = solicitudNoSeriada(conn, inputNoSeriado, idPadre, estadoAlta, tipoDevolucion,
						tipoReserva, tipoPedido, tipoNumPayment, estadoDisponible, estadoDevolucion, estadoPendiente,
						tipoInvSidra, nombreNumPayment, tipoPDV, tipoSiniestro, estadoSiniestro, siniestroDevolucion,
						esCancelacion, tipoPanel, tipoRuta, ID_PAIS);

				if (respSolicitudNoSeriada.isResultado()) {
					if (banderaSiniestro && ((respSolicitudNoSeriada.getDatos().getArticulos() != null
							&& !respSolicitudNoSeriada.getDatos().getArticulos().isEmpty())
							|| (respSolicitudNoSeriada.getDatos().getExistencias() != null
									&& !respSolicitudNoSeriada.getDatos().getExistencias().isEmpty()))) {
						insertCompletoSiniestro = false;
					}
					insertNoSeriado = respSolicitudNoSeriada.isResultado();
					detalleError += " " + respSolicitudNoSeriada.getDescripcion();

					output = respSolicitudNoSeriada.getDatos();
					if (respSolicitudSeriada.getDatos() != null) {
						output.setDescErrorSeries(respSolicitudSeriada.getDatos().getDescErrorSeries());
						output.setSeries(respSolicitudSeriada.getDatos().getSeries());
					}
					output.setDescErrorArticulos(respSolicitudNoSeriada.getDatos().getDescErrorArticulos());
					output.setArticulos(respSolicitudNoSeriada.getDatos().getArticulos());
					output.setDescErrorExistencias(respSolicitudNoSeriada.getDatos().getDescErrorExistencias());
					output.setExistencias(respSolicitudNoSeriada.getDatos().getExistencias());
				} else {
					if (respSolicitudNoSeriada.getDescripcion().startsWith("ERROR")) {
						respSolicitudNoSeriada
								.setDescripcion(respSolicitudNoSeriada.getDescripcion().replace("ERROR: ", ""));

					}

					insertNoSeriado = respSolicitudNoSeriada.isResultado();
					detalleError += " " + respSolicitudNoSeriada.getDescripcion();
					if (banderaSiniestro) {
						insertCompletoSiniestro = false;
					}

					output = respSolicitudNoSeriada.getDatos();
				}
			} else {
				if (banderaDevolucion) {
					detalleError += " "
							+ new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_ARTICULOS_NO_SERIADOS_109, null,
									nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
				}
			}

		} else {
			// procedimiento separado
			if (banderaDevolucion && input.getSeriado().equals(estadoTrue)) {
				respSolicitudSeriada = solicitudSeriada(conn, input, idPadre, estadoDisponible, estadoDevolucion,
						estadoAlta, estadoPendiente, tipoDevolucion, tipoInvSidra, tipoSiniestro, estadoSiniestro,
						esCancelacion, tipoArt, ID_PAIS);
			} else {
				respSolicitudSeriada = solicitudNoSeriada(conn, input, idPadre, estadoAlta, tipoDevolucion, tipoReserva,
						tipoPedido, tipoNumPayment, estadoDisponible, estadoDevolucion, estadoPendiente, tipoInvSidra,
						nombreNumPayment, tipoPDV, tipoSiniestro, estadoSiniestro, siniestroDevolucion, esCancelacion,
						tipoPanel, tipoRuta, ID_PAIS);
			}

			insertSeriado = respSolicitudSeriada.isResultado();
			detalleError = respSolicitudSeriada.getDescripcion();
			output = respSolicitudSeriada.getDatos();
		}

		// se establecen los valores de respuesta de las operaciones
		respuesta.setDatos(output);
		respuesta.setDescripcion(detalleError.trim());
		respuesta.setResultado(insertCompletoSiniestro && (insertSeriado || insertNoSeriado));

		// se actualiza observaci\u00F3n en caso de ser necesario
		RespuestaSolicitud respObservaciones = updateObservaciones(conn, respuesta.isResultado(), detalleError, idPadre,
				input.getObservaciones(), input.getCodArea());
		respuesta.setResultado(respObservaciones.isResultado());
		respuesta.setDescripcion(respObservaciones.getDescripcion());

		return respuesta;
	}
	// ========================= Operaciones para crear solicitudes
	// =======================//

	// ####################################################################################//

	// ========================= Operaciones para modificar solicitudes
	// ===================//
	/**
	 * Funci\u00F3n que ejecuta los procedimientos de modificaci\u00F3n de las
	 * solicitudes.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @param estadoAbierta
	 * @param estadoCerrada
	 * @param tipoDevolucion
	 * @param tipoSiniestro
	 * @param tipoNumPayment
	 * @param estadoActivadoPayment
	 * @param estadoRechazadoPayment
	 * @param estadoDesactivadoPayment
	 * @param estadoCancelada
	 * @param estadoDisponible
	 * @param estadoAlta
	 * @param estadoTrue
	 * @param estadoDevolucion
	 * @param estadoSiniestro
	 * @param tipoInvSidra
	 * @param tipoPanel
	 * @param origenMovil
	 * @param origenPC
	 * @param tipoPedido
	 * @param tipoReserva
	 * @param estadoAceptada
	 * @param estadoRechazada
	 * @param estadoFinalizada
	 * @param estadoRechazadaTelca
	 * @param tipoTodasSolicitudes
	 * @param tipoDeuda
	 * @param estadoEnviado
	 * @param estadoPendiente
	 * @param estadoBaja
	 * @return
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws CloneNotSupportedException
	 */
	public static OutputSolicitud doPut(Connection conn, InputSolicitud input, String estadoAbierta,
			String tipoDevolucion, String tipoSiniestro, String tipoNumPayment, String estadoActivadoPayment,
			String estadoRechazadoPayment, String estadoCancelada, String estadoDisponible, String estadoAlta,
			String estadoTrue, String estadoDevolucion, String estadoSiniestro, String tipoInvSidra, String tipoPanel,
			String origenMovil, String origenPC, String tipoPedido, String tipoReserva, String estadoAceptada,
			String estadoRechazada, String estadoFinalizada, String estadoRechazadaTelca, String tipoTodasSolicitudes,
			String tipoDeuda, String estadoEnviado, String estadoPendiente, String tipoRuta, String estadoBaja,
			String tipoArt, BigDecimal ID_PAIS) throws SQLException, NumberFormatException, CloneNotSupportedException {
		listaLog = new ArrayList<LogSidra>();
		String nombreMetodo = "doPut";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta = null;
		OutputSolicitud output = new OutputSolicitud();
		boolean update = false;
		String origen = null;
		boolean verificarBuzon = false;
		boolean verificarVendedor = false;
		boolean banderaError = false;
		int numMensajeError = -101;
		String razonError = "";
		Integer nivelBuzonActual = null;
		boolean aceptaSolicitudDTS = false;
		boolean aceptaSolicitudZonaCom = false;
		String estadoSolicitud = input.getEstado().toUpperCase();
		String estadoDetalles = "";

		// banderas de tipos de solicitud
		boolean banderaPedido = false;
		boolean banderaReserva = false;
		boolean banderaPayment = false;
		boolean banderaDevolucion = false;
		boolean banderaSiniestro = false;
		boolean banderaDeuda = false;

		// banderas de estados a operar
		boolean banderaEstadoAceptada = estadoSolicitud.equalsIgnoreCase(estadoAceptada);
		boolean banderaEstadoRechazada = estadoSolicitud.equalsIgnoreCase(estadoRechazada);
		boolean banderaEstadoCancelada = estadoSolicitud.equalsIgnoreCase(estadoCancelada);
		boolean banderaEstadoFinalizada = estadoSolicitud.equalsIgnoreCase(estadoFinalizada);
		boolean banderaEstadoRechazadaTelca = estadoSolicitud.equalsIgnoreCase(estadoRechazadaTelca);
		boolean banderaEstadoEnviado = estadoSolicitud.equalsIgnoreCase(estadoEnviado);
		boolean banderaEstadoPendiente = estadoSolicitud.equalsIgnoreCase(estadoPendiente);

		boolean banderaNotificarDTS = false;

		try {
			conn.setAutoCommit(false);

			// se verifica que exista la solicitud
			List<Filtro> condiciones = CtrlSolicitud.obtenerCondicionesExistencia(input, ID_PAIS);
			String existencia = UtileriasBD.verificarExistencia(conn, Solicitud.N_TABLA, condiciones);
			if (new Integer(existencia) < 1) {
				log.error("No existe el recurso.");
				banderaError = true;
				numMensajeError = Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE;

				return output;
			}

			// existe la solicitud, se obtienen datos de la misma
			String camposSolicitud[] = { Solicitud.CAMPO_TIPO_SOLICITUD, Solicitud.CAMPO_ESTADO, Solicitud.CAMPO_IDTIPO,
					Solicitud.CAMPO_TIPO, Solicitud.CAMPO_TCSCBODEGAVIRTUALID, Solicitud.CAMPO_IDVENDEDOR,
					Solicitud.CAMPO_TCSCBUZONID, Solicitud.CAMPO_BUZON_ANTERIOR, Solicitud.CAMPO_TCSCDTSID,
					Solicitud.CAMPO_CREADO_POR, Solicitud.CAMPO_FECHA };
			Map<String, String> datosSolicitud = UtileriasBD.getSingleFirstData(conn,
					getParticion(Solicitud.N_TABLA, Conf.PARTITION, "", input.getCodArea()), camposSolicitud,
					condiciones);

			// se verifica el estado de la solicitud
			if (!datosSolicitud.get(Solicitud.CAMPO_ESTADO).equalsIgnoreCase(estadoAbierta)
					&& !datosSolicitud.get(Solicitud.CAMPO_ESTADO).equalsIgnoreCase(estadoAceptada)
					&& !datosSolicitud.get(Solicitud.CAMPO_ESTADO).equalsIgnoreCase(estadoPendiente)
					&& !datosSolicitud.get(Solicitud.CAMPO_ESTADO).equalsIgnoreCase(estadoEnviado)
					&& !datosSolicitud.get(Solicitud.CAMPO_ESTADO).equalsIgnoreCase(estadoRechazadaTelca)
					&& !datosSolicitud.get(Solicitud.CAMPO_ESTADO).equalsIgnoreCase(estadoRechazada)) {
				banderaError = true;
				numMensajeError = Conf_Mensajes.MSJ_SOL_ESTADO_ERRONEO;
				razonError = estadoAbierta + ", " + estadoAceptada + ", " + estadoPendiente + " o " + estadoEnviado
						+ ".";

				return output;
			}

			if (datosSolicitud.get(Solicitud.CAMPO_ESTADO).equalsIgnoreCase(estadoRechazadaTelca)
					&& !datosSolicitud.get(Solicitud.CAMPO_TIPO_SOLICITUD).equalsIgnoreCase(tipoDeuda)) {
				banderaError = true;
				numMensajeError = Conf_Mensajes.MSJ_ERROR_DEUDA_RECHAZADA_617;

				return output;
			}

			// se verifica si la solicitud se escal\u00F3
			boolean tieneBuzonAnterior = datosSolicitud.get(Solicitud.CAMPO_BUZON_ANTERIOR) != null
					&& !datosSolicitud.get(Solicitud.CAMPO_BUZON_ANTERIOR).equals("");

			// se verifica si la solicitud se va a escalar
			boolean tieneBuzonSiguiente = input.getIdBuzonSiguiente() != null
					&& !input.getIdBuzonSiguiente().equals("");

			// se obtienen los datos del buz\u00F3n actual de la solicitud
			List<Filtro> condicionesBuzon = new ArrayList<Filtro>();
			condicionesBuzon.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_TCSCBUZONID,
					datosSolicitud.get(Solicitud.CAMPO_TCSCBUZONID)));
			condicionesBuzon.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					BuzonSidra.CAMPO_TCSCCATPAIS_ID, ID_PAIS.toString()));
			condicionesBuzon
					.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BuzonSidra.CAMPO_ESTADO, estadoAlta));
			nivelBuzonActual = new Integer(
					UtileriasBD.getOneRecord(conn, BuzonSidra.CAMPO_NIVEL, BuzonSidra.N_TABLA, condicionesBuzon));

			// se establecen banderas de tipo de solicitud
			banderaPedido = datosSolicitud.get(Solicitud.CAMPO_TIPO_SOLICITUD).equalsIgnoreCase(tipoPedido);
			banderaReserva = datosSolicitud.get(Solicitud.CAMPO_TIPO_SOLICITUD).equalsIgnoreCase(tipoReserva);
			banderaPayment = datosSolicitud.get(Solicitud.CAMPO_TIPO_SOLICITUD).equalsIgnoreCase(tipoNumPayment);
			banderaDevolucion = datosSolicitud.get(Solicitud.CAMPO_TIPO_SOLICITUD).equalsIgnoreCase(tipoDevolucion);
			banderaSiniestro = datosSolicitud.get(Solicitud.CAMPO_TIPO_SOLICITUD).equalsIgnoreCase(tipoSiniestro);
			banderaDeuda = datosSolicitud.get(Solicitud.CAMPO_TIPO_SOLICITUD).equalsIgnoreCase(tipoDeuda);

			if (banderaEstadoCancelada) {
				// las solicitudes de PAYMENT NO se pueden cancelar, tampoco las solicitudes de
				// DEUDA si NO ESTAN en estado PENDIENTE
				if (banderaPayment || (banderaDeuda
						&& !datosSolicitud.get(Solicitud.CAMPO_ESTADO).equalsIgnoreCase(estadoPendiente))) {
					banderaError = true;
					numMensajeError = Conf_Mensajes.MSJ_ERROR_ESTADO_TIPO_SOLICITUD_807;
					return output;
				}

				// no se verifica buz\u00F3n siguiente, solo vendedor
				verificarVendedor = true;

			} else {
				if (banderaDeuda) {
					// se verifican procesos de deuda
					if (banderaEstadoEnviado) {
						// se verifica buzon siguiente

					} else if (banderaEstadoFinalizada) {
						tieneBuzonSiguiente = false; // seteo false en caso hubieran enviado buzon siguiente
						verificarBuzon = false;

					} else if (banderaEstadoPendiente) {
						// no se pueden operar estas solicitudes con los estados enviados
						banderaError = true;
						numMensajeError = Conf_Mensajes.MSJ_ERROR_ESTADO_TIPO_SOLICITUD_807;
						return output;
					}

				} else {
					// se verifican las dem\u00E9s solicitudes normal
					if (!banderaDevolucion && !banderaSiniestro) {
						if (banderaEstadoAceptada) {
							// se verifica buzon siguiente
							if (tieneBuzonSiguiente) {
								verificarBuzon = true;
							}

						} else if (banderaEstadoFinalizada || banderaEstadoRechazada || banderaEstadoRechazadaTelca) {
							// no debe llevar buzon
							input.setIdBuzonSiguiente(null);
						}
					} else {
						// no se pueden operar estas solicitudes con los estados enviados
						banderaError = true;
						numMensajeError = Conf_Mensajes.MSJ_ERROR_ESTADO_TIPO_SOLICITUD_807;
						return output;
					}
				}
			}

			if (!banderaDeuda) {
				switch (nivelBuzonActual) {
				case Conf.NIVEL_BUZON_TELCA:
					banderaNotificarDTS = true; // solo si esta en buzon telca se notifica al dts

					// se puede cancelar solo si NO se escal\u00F3 la solicitud
					if (!banderaDeuda && (banderaEstadoAceptada || banderaEstadoRechazada)
							|| (banderaEstadoCancelada && tieneBuzonAnterior)) {
						// error operacion no permitida en buzon
						banderaError = true;
						numMensajeError = Conf_Mensajes.MSJ_ERROR_ESTADO_BUZON_SOLICITUD_806;
						return output;
					}
					break;

				case Conf.NIVEL_BUZON_DTS:
					aceptaSolicitudDTS = banderaEstadoAceptada && !tieneBuzonSiguiente;

					if (banderaPayment && banderaEstadoFinalizada && !tieneBuzonSiguiente) {
						aceptaSolicitudDTS = true;
					}

					boolean val1 = (aceptaSolicitudDTS && (!banderaPedido && !banderaReserva));
					boolean val2 = (banderaEstadoCancelada && tieneBuzonAnterior);
					if (!banderaPayment && (!banderaDeuda && val1 || banderaEstadoFinalizada
							|| banderaEstadoRechazadaTelca || val2)) {
						// error operacion no permitida en buzon
						banderaError = true;
						numMensajeError = Conf_Mensajes.MSJ_ERROR_ESTADO_BUZON_SOLICITUD_806;
						return output;
					}
					break;

				case Conf.NIVEL_ZONACOMERCIAL:
					aceptaSolicitudZonaCom = banderaEstadoAceptada && !tieneBuzonSiguiente && !banderaDeuda;
					if ((aceptaSolicitudZonaCom && (!banderaPedido && !banderaReserva)) || banderaEstadoFinalizada
							|| banderaEstadoRechazadaTelca) {
						// error operacion no permitida en buzon
						banderaError = true;
						numMensajeError = Conf_Mensajes.MSJ_ERROR_ESTADO_BUZON_SOLICITUD_806;
						return output;
					}
					break;
				}
			}

			if (verificarBuzon) {
				// se valida que el buzon sea de nivel y tipo correcto
				condicionesBuzon.clear();
				condicionesBuzon.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						BuzonSidra.CAMPO_TCSCBUZONID, input.getIdBuzonSiguiente()));
				condicionesBuzon.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						BuzonSidra.CAMPO_TCSCCATPAIS_ID, ID_PAIS.toString()));
				if (banderaDeuda) {
					condicionesBuzon.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_NIVEL,
							Conf.NIVEL_BUZON_TELCA + ""));
				} else {
					condicionesBuzon.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_NIVEL,
							(new Integer(nivelBuzonActual) - 1) + ""));
				}
				condicionesBuzon.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR,
						BuzonSidra.CAMPO_TIPO_WORKFLOW, datosSolicitud.get(Solicitud.CAMPO_TIPO_SOLICITUD)));
				condicionesBuzon.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR,
						BuzonSidra.CAMPO_TIPO_WORKFLOW, tipoTodasSolicitudes));
				condicionesBuzon.add(
						UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BuzonSidra.CAMPO_ESTADO, estadoAlta));

				existencia = UtileriasBD.verificarExistencia(conn, BuzonSidra.N_TABLA, condicionesBuzon);
				if (new Integer(existencia) <= 0) {
					log.error("El buz\u00F3n no es de nivel o tipo correcto.");
					banderaError = true;
					numMensajeError = Conf_Mensajes.MSJ_ERROR_NIVEL_BUZON_SIGUIENTE_805;

					return output;
				}
			}

			if (verificarVendedor) {
				if (banderaDeuda) {
					// procesos de deuda
					if (!datosSolicitud.get(Solicitud.CAMPO_CREADO_POR).equalsIgnoreCase(input.getUsuario())) {
						banderaError = true;
						numMensajeError = Conf_Mensajes.MSJ_ERROR_VENDEDOR_CANCELACION_788;
						return output;
					} else {
						origen = origenPC;
					}
				} else {
					// proceso de solicitudes anteriores

					// si es origen movil, se verifica que \u00FAnicamente el creador pueda cancelar
					// la solicitud
					if (input.getIdVendedor() != null && !input.getIdVendedor().equals("")) {
						origen = origenMovil;

						// se verifica que el vendedor de la solicitud sea igual al vendedor que la
						// intenta cancelar
						if (!datosSolicitud.get(Solicitud.CAMPO_IDVENDEDOR).equals(input.getIdVendedor())) {
							banderaError = true;
							numMensajeError = Conf_Mensajes.MSJ_ERROR_VENDEDOR_CANCELACION_788;

							return output;
						}
					} else {
						origen = origenPC;
					}
				}
			}

			if (aceptaSolicitudDTS) {
				estadoSolicitud = estadoFinalizada;
			}

			if (aceptaSolicitudZonaCom) {
				estadoSolicitud = estadoFinalizada;
			}
			// si el estado es CANCELADA se modifica el encabezado de la solicitud sin
			// importar el tipo
			if (banderaEstadoCancelada) {
				update = modEncabezadoSolicitud(conn, condiciones, input.getUsuario(), estadoSolicitud, origen,
						input.getObservaciones(), null, input.getCodArea());
			} else {
				update = modEncabezadoSolicitud(conn, condiciones, input.getUsuario(), estadoSolicitud, null, null,
						input.getIdBuzonSiguiente(), input.getCodArea());
				log.trace("resultado update:" + update);
			}

			if (update) {
				log.trace("actualiza detalle solicitud");
				if (!banderaDeuda) {
					// si no es deuda se operan los detalles
					boolean updDetSolicitud = false;
					boolean updNumRecargaCierre = false;
					RespuestaSolicitud respInventario = new RespuestaSolicitud();
					RespuestaSolicitud updDispositivos = new RespuestaSolicitud();
					String msjError = "Inconveniente al actualizar detalles.";
					log.trace("no es deuda realiza actualizaciones correspondientes.");
					boolean val1 = (banderaEstadoFinalizada || banderaEstadoRechazadaTelca);
					boolean val2 = (nivelBuzonActual == Conf.NIVEL_BUZON_DTS && banderaEstadoRechazada);
					if (banderaEstadoCancelada || (nivelBuzonActual == Conf.NIVEL_BUZON_TELCA && val1) || val2
							|| aceptaSolicitudDTS) {

						if (estadoSolicitud.equalsIgnoreCase(estadoAceptada)
								|| estadoSolicitud.equalsIgnoreCase(estadoFinalizada)) {
							estadoDetalles = estadoAceptada;
						} else if (estadoSolicitud.equalsIgnoreCase(estadoRechazada)
								|| estadoSolicitud.equalsIgnoreCase(estadoRechazadaTelca)) {
							estadoDetalles = estadoRechazada;
						} else if (estadoSolicitud.equalsIgnoreCase(estadoCancelada)) {
							estadoDetalles = estadoCancelada;
						}

						log.trace("inicia a actualizar detalles");
						// se actualizan los detalles de la solicitud
						updDetSolicitud = updateDetCompletoSolicitud(conn, input.getIdSolicitud(), estadoDetalles,
								input.getUsuario());

						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO, servicioPutSolicitud,
								input.getIdSolicitud(), Conf.LOG_TIPO_SOLICITUD,
								"Se cambi\u00F3 el estado de los detalles de la solicitud a " + estadoDetalles + ".",
								""));

						// si es solicitud de payment se actualizan los numeros de recarga
						if (banderaPayment) {
							log.trace("inicia a ctualizar n\u00FAmeros de payment");
							String estadoPayment = "";
							if (estadoSolicitud.equalsIgnoreCase(estadoFinalizada)) {
								estadoPayment = estadoActivadoPayment;
							} else {
								estadoPayment = estadoRechazadoPayment;
							}

							// se procede a actualizar los numeros de recarga
							updNumRecargaCierre = updateNumRecargaCierre(conn, input.getIdSolicitud(), estadoPayment,
									input.getUsuario(), ID_PAIS);

							listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO,
									servicioPutSolicitud, datosSolicitud.get(Solicitud.CAMPO_IDTIPO), Conf.LOG_TIPO_PDV,
									"Se cambi\u00F3 el estado de los n\u00FAmeros de recarga a " + estadoPayment + ".",
									""));
						} else {
							updNumRecargaCierre = true;
						}

						// si es solicitud de devolucion o siniestro se actualiza el inventario
						if (banderaDevolucion || banderaSiniestro) {
							// se actualizan en el inventario los art\u00EDculos de la solicitud
							List<InputArticuloSolicitud> listArticulos = getInventarioCancelacion(conn,
									input.getIdSolicitud());

							if (listArticulos != null) {
								input.setArticulos(listArticulos);
								input.setIdBodega(datosSolicitud.get(Solicitud.CAMPO_TCSCBODEGAVIRTUALID));
								input.setSeriado(estadoTrue);
								input.setObservaciones("");
								input.setTipoSolicitud(datosSolicitud.get(Solicitud.CAMPO_TIPO_SOLICITUD));
								input.setIdTipo(datosSolicitud.get(Solicitud.CAMPO_IDTIPO));
								input.setTipo(datosSolicitud.get(Solicitud.CAMPO_TIPO));
								input.setIdVendedor(datosSolicitud.get(Solicitud.CAMPO_IDVENDEDOR));

								respInventario = doOperacionesDetalle(conn, new Integer(input.getIdSolicitud()), input,
										true, false, false, estadoAlta, estadoTrue, estadoDisponible, estadoDevolucion,
										"", estadoSiniestro, tipoDevolucion, "", "", tipoNumPayment, tipoSiniestro,
										tipoInvSidra, "", "", false, true, tipoPanel, tipoRuta, tipoArt, ID_PAIS);

								if (!respInventario.isResultado()) {
									output = respInventario.getDatos();
									msjError = respInventario.getDescripcion();
								}
							} else {
								respInventario.setResultado(true);
							}

						} else {
							respInventario.setResultado(true);
						}

						// si es solicitud de siniestro se verifican y actualizan los dispositivos
						if (banderaSiniestro) {
							updDispositivos = cancelarDispositivos(conn, input.getIdSolicitud(), estadoAlta,
									input.getUsuario(), input.getCodArea(), ID_PAIS);
							if (!updDispositivos.isResultado()) {
								// fallo al actualizar dispositivos
								log.error("fallo al actualizar dispositivos o folios");
							}
						} else {
							updDispositivos.setResultado(true);
						}
					} else {
						updDetSolicitud = true;
						updNumRecargaCierre = true;
						respInventario.setResultado(true);
						updDispositivos.setResultado(true);
					}

					if (!updDetSolicitud || !updNumRecargaCierre || !respInventario.isResultado()
							|| !updDispositivos.isResultado()) {
						conn.rollback();
						log.error("Rollback");

						banderaError = true;
						numMensajeError = Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO;
						razonError = msjError;

						return output;
					}
				} else {
					// operaciones especificas de deuda
					boolean insertObservaciones = true;
					boolean udpJornadas = true;
					boolean updDetPagos = true;

					if (input.getObservaciones() != null && !input.getObservaciones().equals("")) {
						insertObservaciones = insertarObservaciones(conn, new Integer(input.getIdSolicitud()),
								input.getObservaciones(), input.getUsuario());
					}

					if (banderaEstadoFinalizada) {
						// actualizar jornadas
						udpJornadas = actualizarJornadas(conn, datosSolicitud.get(Solicitud.CAMPO_TCSCDTSID),
								input.getIdSolicitud(), datosSolicitud.get(Solicitud.CAMPO_FECHA),
								datosSolicitud.get(Solicitud.CAMPO_TCSCBODEGAVIRTUALID), Conf.JORNADA_ESTADO_PAGADA,
								input.getCodArea(), ID_PAIS);

					} else if (banderaEstadoCancelada) {
						// actualizar detalle de pagos a estado baja
						updDetPagos = updateDetPagos(conn, input.getIdSolicitud(), estadoBaja, input.getUsuario());
					} else if (banderaEstadoAceptada) {
						udpJornadas = actualizarJornadas(conn, datosSolicitud.get(Solicitud.CAMPO_TCSCDTSID),
								input.getIdSolicitud(), datosSolicitud.get(Solicitud.CAMPO_FECHA),
								datosSolicitud.get(Solicitud.CAMPO_TCSCBODEGAVIRTUALID),
								Conf.JORNADA_ESTADO_PAGADA_ZONA_COMERCIAL, input.getCodArea(), ID_PAIS);
					}

					if (!insertObservaciones) {
						numMensajeError = Conf_Mensajes.MSJ_ERROR_INSERT_OBSERVACIONES_901;
					}

					if (!udpJornadas) {
						numMensajeError = Conf_Mensajes.MSJ_ERROR_JORNADAS_DEUDA_900;
					}

					if (!updDetPagos) {
						numMensajeError = Conf_Mensajes.MSJ_ERROR_UPD_DETPAGO_DEUDA_622;
					}

					if (!insertObservaciones || !udpJornadas || !updDetPagos) {
						conn.rollback();
						log.error("Rollback");

						banderaError = true;
						return output;
					}
				}

				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_MOD_SOLICITUD_66, null, nombreClase,
						nombreMetodo, null, input.getCodArea());

				if (banderaNotificarDTS) {
					CtrlSolicitud.notificarDTS(conn, datosSolicitud.get(Solicitud.CAMPO_TCSCDTSID), estadoSolicitud,
							input.getUsuario(), input.getIdSolicitud(), input.getCodArea(), ID_PAIS);
				}

				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_OPERACION, servicioPutSolicitud,
						input.getIdSolicitud(), Conf.LOG_TIPO_SOLICITUD, respuesta.getDescripcion()
								+ " Se cambi\u00F3 el estado de " + estadoAbierta + " a " + estadoSolicitud + ".",
						""));

				conn.commit();
			}

		} catch (SQLException e) {
			respuesta = new ControladorBase().getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo,
					null, input.getCodArea());

			conn.rollback();
			log.error("Rollback");

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_OPERACION, servicioPutSolicitud,
					input.getIdSolicitud(), Conf.LOG_TIPO_SOLICITUD, respuesta.getDescripcion(),
					respuesta.getExcepcion()));

		} finally {
			if (banderaError) {
				// si existe un error se arma y retorna el mensaje
				respuesta = new ControladorBase().getMensaje(numMensajeError, null, nombreClase, nombreMetodo,
						razonError.equals("") ? null : razonError, input.getCodArea());

				listaLog = new ArrayList<LogSidra>();
				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_OPERACION, servicioPutSolicitud,
						input.getIdSolicitud(), Conf.LOG_TIPO_SOLICITUD, respuesta.getDescripcion(), ""));
			}

			conn.setAutoCommit(true);
			output.setRespuesta(respuesta);
			UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
		}

		return output;
	}

	private static boolean updateDetPagos(Connection conn, String idSolicitud, String estadoBaja, String usuario)
			throws SQLException {
		List<Filtro> condiciones = new ArrayList<Filtro>();

		// se establecen los campos a actualizar
		String campos[][] = {
				{ DetallePagoDeuda.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoBaja.toUpperCase()) },
				{ DetallePagoDeuda.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
				{ DetallePagoDeuda.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) } };

		// se establecen las condiciones a cumplir
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, DetallePagoDeuda.CAMPO_TCSCSOLICITUDID,
				idSolicitud));

		String sql = UtileriasBD.armarQueryUpdate(DetallePagoDeuda.N_TABLA, campos, condiciones);
		QueryRunner Qr = new QueryRunner();
		int res = Qr.update(conn, sql);
		if (res <= 0) {
			log.error("problema al actualizar detalle de pagos.");
			return false;
		} else {
			return true;
		}
	}

	private static boolean actualizarJornadas(Connection conn, String idDTS, String idSolicitud, String fechaSolicitud,
			String idBodegaSolicitud, String estado_pagada, String codArea, BigDecimal ID_PAIS) throws SQLException {
		String estadoPagada = UtileriasJava.getConfig(conn, Conf.GRUPO_JORNADA_ESTADOS_LIQ, estado_pagada, codArea);
		List<Filtro> condiciones = new ArrayList<Filtro>();
		// se establecen los campos a actualizar
		String campos[][] = {
				{ Jornada.CAMPO_SOLICITUD_PAGO, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, idSolicitud) },
				{ Jornada.CAMPO_ESTADO_PAGO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoPagada) } };

		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TIMESTAMP_TRUNC_AND, Jornada.CAMPO_FECHA_LIQUIDACION,
				fechaSolicitud, null, Conf.TXT_FORMATO_FECHA_BD));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCDTSID, idDTS));

		if (idBodegaSolicitud != null && !idBodegaSolicitud.equals("")) {
			// se obtienen las jornadas de la bodega
			String sqlFiltroBodega = "IN (WITH RUTAPANEL AS (SELECT TCSCRUTAID IDTIPO, 'RUTA' TIPO FROM TC_SC_RUTA WHERE TCSCBODEGAVIRTUALID = "
					+ idBodegaSolicitud + " AND TCSCCATPAISID = " + ID_PAIS
					+ " UNION SELECT TCSCPANELID IDTIPO, 'PANEL' TIPO FROM TC_SC_PANEL WHERE TCSCBODEGAVIRTUALID = "
					+ idBodegaSolicitud + " AND TCSCCATPAISID = " + ID_PAIS + ")"
					+ " SELECT TCSCJORNADAVENID FROM TC_SC_JORNADA_VEND J, RUTAPANEL WHERE TCSCCATPAISID = " + ID_PAIS
					+ " AND J.IDTIPO = RUTAPANEL.IDTIPO AND J.DESCRIPCION_TIPO = RUTAPANEL.TIPO)";

			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_AVANZADO_AND, Jornada.CAMPO_TCSCJORNADAVENID,
					sqlFiltroBodega));
		}

		String sql = UtileriasBD.armarQueryUpdate(getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea), campos,
				condiciones);
		QueryRunner Qr = new QueryRunner();
		int res = Qr.update(conn, sql);
		if (res <= 0) {
			log.error("problema al actualizar jornadas liquidadas.");
			return false;
		} else {
			return true;
		}
	}

	private static RespuestaSolicitud updateObservaciones(Connection conn, boolean respuesta, String detalleError,
			int idPadre, String observaciones, String codArea) throws SQLException {
		RespuestaSolicitud output = new RespuestaSolicitud();
		String nombreMetodo = "updateObservaciones";
		String nombreClase = new CurrentClassGetter().getClassName();

		output.setResultado(respuesta);
		output.setDescripcion(detalleError.trim());

		observaciones = UtileriasJava.getValue(observaciones).trim();

		if (respuesta && !"".equals(detalleError.trim())) {
			// si se gener\u00F3 algun comentario extra se actualiza la observacion
			String camposUpdate[][] = { { Solicitud.CAMPO_OBSERVACIONES,
					UtileriasJava.setUpdate(Conf.INSERT_TEXTO, observaciones + " " + detalleError.trim()) } };

			List<Filtro> condicionesExistencia = new ArrayList<Filtro>();
			condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					Solicitud.CAMPO_TCSCSOLICITUDID, idPadre + ""));

			String sql = UtileriasBD.armarQueryUpdate(getParticion(Solicitud.N_TABLA, Conf.PARTITION, "", codArea),
					camposUpdate, condicionesExistencia);
			QueryRunner Qr = new QueryRunner();
			int res = Qr.update(conn, sql);
			if (res != 1) {
				log.error("Rollback: problema al actualizar observaciones.");

				listaLog = new ArrayList<LogSidra>();
				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud, "0",
						Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(), ""));

				output.setResultado(false);
				output.setDescripcion(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_OBSERVACIONES_125,
						null, nombreClase, nombreMetodo, null, codArea).getDescripcion());
			}
		}

		return output;
	}

	private static boolean modEncabezadoSolicitud(Connection conn, List<Filtro> condiciones, String usuario,
			String estado, String origen, String observacion, String idBuzonSiguente, String codArea)
			throws SQLException {
		QueryRunner Qr = new QueryRunner();

		String campos[][] = CtrlSolicitud.obtenerCamposPutDel(usuario, estado, origen, observacion, idBuzonSiguente);

		String sql = UtileriasBD.armarQueryUpdate(getParticion(Solicitud.N_TABLA, Conf.PARTITION, "", codArea), campos,
				condiciones);

		int res = Qr.update(conn, sql);
		log.debug("Solicitudes actualizadas: " + res);

		return (res > 0 ? true : false);
	}
	// ========================= Operaciones para modificar solicitudes
	// ===================//

	// ####################################################################################//

	// ========================= Operaciones para validar los detalles
	// ====================//
	public static Respuesta validarListadoDB(Connection conn, InputSolicitud input, String estadoAlta, String tipoPDV,
			String estadoActivadoPayment, String estadoPendiente, BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "validarListadoDB";
		String nombreClase = new CurrentClassGetter().getClassName();

		String listado = "";
		String numeros = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		PreparedStatement pstmt2 = null;
		ResultSet rst2 = null;

		for (int i = 0; i < input.getArticulos().size(); i++) {
			listado += input.getArticulos().get(i).getIdArticulo();
			if (i != input.getArticulos().size() - 1)
				listado += ",";
		}

		StringBuilder sqlA = new StringBuilder();
		sqlA.append("SELECT TO_NUMBER(COLUMN_VALUE) AS NUMEROS FROM TABLE(SYS.DBMS_DEBUG_VC2COLL(");
		sqlA.append(listado);
		sqlA.append(
				")) MINUS SELECT num_recarga  FROM TC_SC_NUMRECARGA  WHERE TCSCCATPAISID =? AND idtipo = ? AND UPPER(tipo) = ?  AND UPPER(estado) = ?");

		log.debug("Qry numeros no existentes: " + sqlA.toString());
		try {
			pstmt = conn.prepareStatement(sqlA.toString());
			pstmt.setBigDecimal(1, ID_PAIS);
			pstmt.setBigDecimal(2, new BigDecimal(input.getIdPDV()));
			pstmt.setString(3, tipoPDV.toUpperCase());
			pstmt.setString(4, estadoAlta.toUpperCase());
			rst = pstmt.executeQuery();

			if (rst.next()) {
				do {
					numeros += rst.getString("NUMEROS") + ", ";
				} while (rst.next());
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}

		if (!numeros.equals("")) {
			return new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NUMEROS_NO_EXISTEN_PDV_754, null, nombreClase,
					nombreMetodo, numeros.substring(0, numeros.length() - 2), input.getCodArea());
		}

		// ver que ninguno este en estado activado o pendiente
		sqlA = new StringBuilder();
		sqlA.append(
				"SELECT num_recarga, estado_payment FROM TC_SC_NUMRECARGA WHERE num_recarga  IN (SELECT num_recarga FROM TC_SC_NUMRECARGA WHERE TCSCCATPAISID = ? AND idtipo = ? AND UPPER(tipo) = ? AND num_recarga IN (");
		sqlA.append(listado);
		sqlA.append(") AND estado_payment IS NOT NULL)");

		log.debug("Qry estado numeros: " + sqlA.toString());
		try {
			pstmt2 = conn.prepareStatement(sqlA.toString());
			pstmt2.setBigDecimal(1, ID_PAIS);
			pstmt2.setBigDecimal(2, new BigDecimal(input.getIdPDV()));
			pstmt2.setString(3, tipoPDV.toUpperCase());

			rst2 = pstmt2.executeQuery();
			listado = "";

			if (rst2.next()) {
				do {
					if (rst2.getString(NumRecarga.CAMPO_ESTADO_PAYMENT).equalsIgnoreCase(estadoActivadoPayment)) {
						numeros += rst2.getString(NumRecarga.CAMPO_NUM_RECARGA) + ", ";

					} else if (rst2.getString(NumRecarga.CAMPO_ESTADO_PAYMENT).equalsIgnoreCase(estadoPendiente)) {
						listado += rst2.getString(NumRecarga.CAMPO_NUM_RECARGA) + ", ";
					}

				} while (rst2.next());
			}
		} finally {
			DbUtils.closeQuietly(pstmt2);
			DbUtils.closeQuietly(rst2);
		}
		if (!"".equals(numeros)) {
			return new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NUMEROS_ACTIVADOS_756, null, nombreClase,
					nombreMetodo, numeros.substring(0, numeros.length() - 2), input.getCodArea());

		} else if (!"".equals(listado)) {
			return new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NUMEROS_ESTADO_757, null, nombreClase,
					nombreMetodo, estadoPendiente + ": " + listado.substring(0, listado.length() - 2),
					input.getCodArea());
		}

		return null;
	}

	public static Respuesta validarDispositivos(Connection conn, List<InputDispositivo> dispositivos, String estadoAlta,
			String idTipo, String tipo, String codArea, BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "validarDispositivos";
		String nombreClase = new CurrentClassGetter().getClassName();

		String listado = "";
		String dispError = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		for (int i = 0; i < dispositivos.size(); i++) {
			listado += "'" + dispositivos.get(i).getCodigoDispositivo() + "'";
			if (i != dispositivos.size() - 1)
				listado += ",";
		}

		String sql = queryValidarDispositivo(estadoAlta, idTipo, tipo, listado, ID_PAIS);

		log.debug("Qry dispositivos no existentes: " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				do {
					dispError += rst.getString("DISP") + ", ";
				} while (rst.next());
			}

			if (!dispError.equals("")) {
				return new ControladorBase().getMensaje(Conf_Mensajes.MSJ_DISPOSITIVOS_INVALIDOS_772, null, nombreClase,
						nombreMetodo, dispError.substring(0, dispError.length() - 2), codArea);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return null;
	}

	public static String queryValidarDispositivo(String estadoAlta, String idTipo, String tipo, String listado,
			BigDecimal ID_PAIS) {
		String sql = "SELECT COLUMN_VALUE AS DISP " + "FROM TABLE(SYS.DBMS_DEBUG_VC2COLL(" + listado + ")) " + "MINUS"
				+ " SELECT " + Dispositivo.CAMPO_CODIGO_DISPOSITIVO + " FROM " + Dispositivo.N_TABLA
				+ " WHERE TCSCCATPAISID = " + ID_PAIS + " AND " + Dispositivo.CAMPO_RESPONSABLE + " = " + idTipo
				+ " AND UPPER(" + Dispositivo.CAMPO_TIPO_RESPONSABLE + ") = '" + tipo.toUpperCase() + "'"
				+ " AND UPPER(" + Dispositivo.CAMPO_ESTADO + ") = '" + estadoAlta.toUpperCase() + "'";
		return sql;
	}
	// ========================= Operaciones para validar los detalles
	// ====================//

	// ------------------------------------------------------------------------------------//

	// ========================= Operaciones para detalles seriados
	// =======================//
	/**
	 * Funci\u00F3n que realiza las tareas de la parte seriada de las solicitudes.
	 * 
	 * @param conn
	 * @param inputSeriado
	 * @param idPadre
	 * @param estadoDisponible
	 * @param estadoDevolucion
	 * @param estadoAlta
	 * @param estadoPendiente
	 * @param tipoDevolucion
	 * @param tipoInvSidra
	 * @param tipoSiniestro
	 * @param estadoSiniestro
	 * @param siniestroDevolucion
	 * @param estadoCancelada
	 * @param esCancelacion
	 * @return
	 * @throws SQLException
	 */
	private static RespuestaSolicitud solicitudSeriada(Connection conn, InputSolicitud input, int idPadre,
			String estadoDisponible, String estadoDevolucion, String estadoAlta, String estadoPendiente,
			String tipoDevolucion, String tipoInvSidra, String tipoSiniestro, String estadoSiniestro,
			boolean esCancelacion, String tipoArt, BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "solicitudSeriada";
		String nombreClase = new CurrentClassGetter().getClassName();
		RespuestaSolicitud respSolicitud = new RespuestaSolicitud();
		OutputSolicitud output = new OutputSolicitud();
		boolean insertHijo = false;
		String detalleError = "";
		log.trace("inicia a validar");

		List<InputArticuloSolicitud> updSeries = doUpdSeries(conn, idPadre, input, estadoDisponible, estadoDevolucion,
				estadoAlta, estadoPendiente, tipoDevolucion, tipoInvSidra, tipoSiniestro, estadoSiniestro,
				esCancelacion, ID_PAIS);

		if (updSeries.get(0).getEstado().equalsIgnoreCase("OK")) {
			insertHijo = true;
		} else if (updSeries.get(0).getEstado().equalsIgnoreCase("GOOD")) {
			if (updSeries.size() > 1) {
				updSeries.remove(0);

				detalleError += new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TITULO_SOLICITUD_SERIADA_353, null,
						nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();

				output.setDescErrorSeries(
						new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_SERIES_VALIDAS_BODEGA_342, null,
								nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion());
				output.setSeries(updSeries);
			}
			insertHijo = true;
		} else if (updSeries.get(0).getEstado().equalsIgnoreCase("ERROR")) {
			detalleError = new ControladorBase()
					.getMensaje(Conf_Mensajes.MSJ_SERIES_NO_VALIDAS_107, null, null, null, null, input.getCodArea())
					.getDescripcion();
			insertHijo = false;

		} else {
			// Cualquier caso de fallo

			detalleError = "ERROR: " + updSeries.get(0).getEstado();
			insertHijo = false;
		}

		respSolicitud.setResultado(insertHijo);
		respSolicitud.setDescripcion(detalleError);
		respSolicitud.setDatos(output);

		return respSolicitud;
	}

	/**
	 * Funci\u00F3n que realiza las inserciones de la parte seriada de las
	 * solicitudes.
	 * 
	 * @param conn
	 * @param idPadre
	 * @param input
	 * @param estadoDisponible
	 * @param estadoDevolucion
	 * @param estadoAlta
	 * @param estadoPendiente
	 * @param tipoDevolucion
	 * @param estadoSiniestro
	 * @param tipoSiniestro
	 * @param siniestroDevolucion
	 * @param estadoCancelada
	 * @param esCancelacion
	 * @return
	 * @throws SQLException
	 */
	private static List<InputArticuloSolicitud> doUpdSeries(Connection conn, int idPadre, InputSolicitud input,
			String estadoDisponible, String estadoDevolucion, String estadoAlta, String estadoPendiente,
			String tipoDevolucion, String tipoInvSidra, String tipoSiniestro, String estadoSiniestro,
			boolean esCancelacion, BigDecimal ID_PAIS) throws SQLException {
		String estado = "";
		String estadoInventario = "";
		String idArticulo = "";
		String idArticulo2 = "";
		List<Filtro> condiciones1 = new ArrayList<Filtro>();
		String tipoGrupo = "";

		RespuestaSolicitud respDetalle;

		log.trace("Inicia a actualizar series");

		if (input.getTipoSolicitud().equalsIgnoreCase(tipoDevolucion)) {
			estado = estadoPendiente;
			estadoInventario = estadoDevolucion;
		} else if (input.getTipoSolicitud().equalsIgnoreCase(tipoSiniestro)) {
			estado = estadoPendiente;
			estadoInventario = estadoSiniestro;
		} else {
			estado = estadoAlta;
		}

		if (esCancelacion) {
			// se intercambia el valor de los estados origen/destino
			estado = estadoInventario;
			estadoInventario = estadoDisponible;
			estadoDisponible = estado;
		}

		String sql = "";
		String existencia = "";

		QueryRunner Qr = new QueryRunner();

		List<Filtro> condicionesExistencia = new ArrayList<Filtro>();
		List<InputArticuloSolicitud> listItemSeriesFail = new ArrayList<InputArticuloSolicitud>();

		InputArticuloSolicitud itemSeriesFail = new InputArticuloSolicitud();
		itemSeriesFail.setEstado("OK");
		listItemSeriesFail.add(itemSeriesFail);

		for (int j = 0; j < input.getArticulos().size(); j++) {
			if (input.getArticulos().get(j).getSerieFinal() != null
					&& !input.getArticulos().get(j).getSerieFinal().equals("")) {
				// validaci\u00F3n de rango de series
				String seriesInvalidas = "";
				log.trace("long series simcard" + input.getArticulos().get(j).getSerie().length());
				if (esCancelacion && input.getArticulos().get(j).getSerie().length() == 19) {
					log.trace("recorta series:" + input.getArticulos().get(j).getSerieFinal().substring(0, 18));

					seriesInvalidas = OperacionMovimientosInventario.validarSeries(conn,
							input.getArticulos().get(j).getSerie().substring(0, 18),
							input.getArticulos().get(j).getSerieFinal().substring(0, 18), input.getIdBodega(), 0,
							estadoDisponible, input.getArticulos().get(j).getTipoInv(), input.getCodArea());
				} else {

					seriesInvalidas = OperacionMovimientosInventario.validarSeries(conn,
							input.getArticulos().get(j).getSerie(), input.getArticulos().get(j).getSerieFinal(),
							input.getIdBodega(), 0, estadoDisponible, input.getArticulos().get(j).getTipoInv(),
							input.getCodArea());
				}
				if (seriesInvalidas.equalsIgnoreCase("OK")) {

					// procesos para hacer el update de las series

					if (input.getArticulos().get(j).getIdArticulo() != null
							&& !input.getArticulos().get(j).getIdArticulo().equals("")) {
						idArticulo = input.getArticulos().get(j).getIdArticulo();
						log.trace("idArticulo:" + idArticulo);
					} else if (input.getArticulos().get(j).getSerie() != null
							&& !input.getArticulos().get(j).getSerie().equals("")) {
						List<Filtro> condiciones = new ArrayList<Filtro>();
						condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_SERIE,
								input.getArticulos().get(j).getSerie()));
						idArticulo2 = UtileriasBD.getOneRecord(conn, Inventario.CAMPO_ARTICULO, Inventario.N_TABLA,
								condiciones);

						if (idArticulo2 == null || "".equals(idArticulo2)) {
							condiciones = new ArrayList<Filtro>();
							condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND,
									"substr(" + Inventario.CAMPO_SERIE + ",1,18)",
									input.getArticulos().get(j).getSerie()));
							idArticulo2 = UtileriasBD.getOneRecord(conn, Inventario.CAMPO_ARTICULO, Inventario.N_TABLA,
									condiciones);
						}
					}
					condiciones1.clear();
					if (idArticulo == null || "".equals(idArticulo)) {
						condiciones1.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
								Inventario.CAMPO_ARTICULO, idArticulo2));
					} else {
						condiciones1.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
								Inventario.CAMPO_ARTICULO, idArticulo));
					}
					condiciones1.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
							Inventario.CAMPO_TCSCCATPAISID, "" + ID_PAIS));
					tipoGrupo = UtileriasBD.getOneRecord(conn, Inventario.CAMPO_TIPO_GRUPO_SIDRA, Inventario.N_TABLA,
							condiciones1);

					String serieInicial = "";
					String serieFinal = "";

					if (esCancelacion && input.getArticulos().get(j).getSerie().length() == 19) {
						serieInicial = input.getArticulos().get(j).getSerie().substring(0, 18);
						serieFinal = input.getArticulos().get(j).getSerieFinal().substring(0, 18);
					} else {
						serieInicial = input.getArticulos().get(j).getSerie();
						serieFinal = input.getArticulos().get(j).getSerieFinal();
					}

					String seriesActualizadas = OperacionMovimientosInventario.actualizarSeries(conn,
							new BigInteger(serieInicial), new BigInteger(serieFinal), input.getIdBodega(),
							input.getUsuario(), Inventario.CAMPO_TCSCSOLICITUDID, idPadre, estadoInventario, tipoGrupo,
							input.getCodArea(), ID_PAIS);

					if (seriesActualizadas.equalsIgnoreCase("OK")) {
						if (esCancelacion == false) {
							// Insert detalle
							respDetalle = insertDetalleSolicitud(conn, idPadre, input.getArticulos().get(j), estado,
									tipoInvSidra, input.getUsuario(), input.getOrigen(), input.getCodArea(), ID_PAIS);
							if (!respDetalle.isResultado()) {
								conn.rollback();
								log.error("Rollback.");

								listItemSeriesFail.get(0).setEstado(respDetalle.getRespuesta().getDescripcion());

								listaLog = new ArrayList<LogSidra>();
								listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD,
										servicioPostSolicitud, "0", Conf.LOG_TIPO_NINGUNO,
										listItemSeriesFail.get(0).getEstado(), ""));

								return listItemSeriesFail;
							}
						}

						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO,
								servicioPostSolicitud, idPadre + "", Conf.LOG_TIPO_SOLICITUD,
								"Cambio de " + estadoDisponible.toUpperCase() + " a " + estadoInventario.toUpperCase()
										+ " el rango de series " + input.getArticulos().get(j).getSerie() + " - "
										+ input.getArticulos().get(j).getSerieFinal(),
								""));

					} else {
						// se deshace todo porque no fueron actualizadas todas las series
						conn.rollback();
						log.error("Rollback: problema al actualizar las series.");
						Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INVENTARIO_105,
								null, null, null, null, input.getCodArea());
						listItemSeriesFail.get(0).setEstado(r.getDescripcion());

						listaLog = new ArrayList<LogSidra>();
						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud,
								"0", Conf.LOG_TIPO_NINGUNO, listItemSeriesFail.get(0).getEstado(),
								"Fallaron las series: " + seriesActualizadas));

						return listItemSeriesFail;
					}
				} else {
					listItemSeriesFail.get(0).setEstado("GOOD");
					itemSeriesFail = new InputArticuloSolicitud();
					itemSeriesFail.setSerie(input.getArticulos().get(j).getSerie());
					itemSeriesFail.setSerieFinal(input.getArticulos().get(j).getSerieFinal());
					itemSeriesFail.setEstado(input.getArticulos().get(j).getEstado());
					listItemSeriesFail.add(itemSeriesFail);
				}
			} else {
				// proceso normal de una serie
				int cantSeries = 0;
				condicionesExistencia.clear();
				condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						Inventario.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
				condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						Inventario.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodega()));

				if (input.getArticulos().get(j).getSerieAsociada() != null
						&& !input.getArticulos().get(j).getSerieAsociada().equals("")) {
					condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Inventario.CAMPO_SERIE,
							"'" + input.getArticulos().get(j).getSerie() + "','"
									+ input.getArticulos().get(j).getSerieAsociada() + "'"));
					cantSeries = 1;
				} else {
					if (input.getArticulos().get(j).getIdArticulo() != null
							&& !input.getArticulos().get(j).getIdArticulo().equals("")) {
						idArticulo = input.getArticulos().get(j).getIdArticulo();
					} else if (input.getArticulos().get(j).getSerie() != null
							&& !input.getArticulos().get(j).getSerie().equals("")) {
						List<Filtro> condiciones = new ArrayList<Filtro>();
						condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_SERIE,
								input.getArticulos().get(j).getSerie()));
						idArticulo2 = UtileriasBD.getOneRecord(conn, Inventario.CAMPO_ARTICULO, Inventario.N_TABLA,
								condiciones);

						if (idArticulo2 == null || "".equals(idArticulo2)) {
							condiciones = new ArrayList<Filtro>();
							condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND,
									"substr(" + Inventario.CAMPO_SERIE + ",1,18)",
									input.getArticulos().get(j).getSerie()));
							idArticulo2 = UtileriasBD.getOneRecord(conn, Inventario.CAMPO_ARTICULO, Inventario.N_TABLA,
									condiciones);
						}
					}
					condiciones1.clear();
					condiciones1.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ARTICULO,
							idArticulo2));
					condiciones1.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
							Inventario.CAMPO_TCSCCATPAISID, "" + ID_PAIS));
					tipoGrupo = UtileriasBD.getOneRecord(conn, Inventario.CAMPO_TIPO_GRUPO_SIDRA, Inventario.N_TABLA,
							condiciones1);

					if (tipoGrupo.equals("SIMCARD") && input.getOrigen().equalsIgnoreCase("MOVIL")) {
						condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND,
								"SUBSTR(" + Inventario.CAMPO_SERIE + ",1,18)", input.getArticulos().get(j).getSerie()));
					} else {
						condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND,
								Inventario.CAMPO_SERIE, input.getArticulos().get(j).getSerie()));
					}
				}

				condicionesExistencia.add(
						UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_ESTADO, estadoDisponible));
				condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_TIPO_INV,
						input.getArticulos().get(j).getTipoInv()));

				existencia = UtileriasBD.verificarExistencia(conn,
						getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodega(), input.getCodArea()),
						condicionesExistencia);
				if (new Integer(existencia) > cantSeries) {

					// Update
					String camposUpdate[][] = {
							{ Inventario.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoInventario) },
							{ Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
							{ Inventario.CAMPO_MODIFICADO_POR,
									UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) },
							{ Inventario.CAMPO_TCSCSOLICITUDID, (esCancelacion ? "NULL"
									: UtileriasJava.setUpdate(Conf.INSERT_NUMERO, idPadre + "")) } };

					sql = UtileriasBD.armarQueryUpdate(
							getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodega(), input.getCodArea()),
							camposUpdate, condicionesExistencia);

					int res = Qr.update(conn, sql);
					if (res == 0) {
						conn.rollback();
						log.error("Rollback: problema al actualizar el inventario.");
						Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INVENTARIO_105,
								null, null, null, null, input.getCodArea());
						listItemSeriesFail.get(0).setEstado(r.getDescripcion());

						listaLog = new ArrayList<LogSidra>();
						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud,
								"0", Conf.LOG_TIPO_NINGUNO, listItemSeriesFail.get(0).getEstado(), ""));

						return listItemSeriesFail;
					}

					if (esCancelacion == false) {
						// Insert detalle
						respDetalle = insertDetalleSolicitud(conn, idPadre, input.getArticulos().get(j), estado,
								tipoInvSidra, input.getUsuario(), input.getOrigen(), input.getCodArea(), ID_PAIS);
						if (!respDetalle.isResultado()) {
							conn.rollback();
							log.error("Rollback.");
							listItemSeriesFail.get(0).setEstado(respDetalle.getRespuesta().getDescripcion());

							listaLog = new ArrayList<LogSidra>();
							listaLog.add(
									ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud,
											"0", Conf.LOG_TIPO_NINGUNO, listItemSeriesFail.get(0).getEstado(), ""));

							return listItemSeriesFail;
						}
					}

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO,
							servicioPostSolicitud, idPadre + "", Conf.LOG_TIPO_SOLICITUD,
							"Cambio de " + estadoDisponible.toUpperCase() + " a " + estadoInventario.toUpperCase()
									+ " el art\u00EDculo con serie " + input.getArticulos().get(j).getSerie()
									+ ((cantSeries > 0)
											? " y su serie asociada " + input.getArticulos().get(j).getSerieAsociada()
											: ""),
							""));
				} else {
					listItemSeriesFail.get(0).setEstado("GOOD");
					itemSeriesFail = new InputArticuloSolicitud();
					itemSeriesFail.setSerie(input.getArticulos().get(j).getSerie());
					itemSeriesFail.setEstado(input.getArticulos().get(j).getEstado());
					listItemSeriesFail.add(itemSeriesFail);
				}
			}
		}

		// verficar si no se inserto ningun articulo para avisar
		int cantArticulosConError = (listItemSeriesFail.size() - 1);
		if (cantArticulosConError >= new Integer(input.getArticulos().size())) {
			// no se agrego ninguno
			listItemSeriesFail.clear();
			itemSeriesFail.setEstado("ERROR");
			listItemSeriesFail.add(itemSeriesFail);
			log.debug("Ninguna serie es v\u00E9lida para ingresar.");
		}

		return listItemSeriesFail;
	}
	// ========================= Operaciones para detalles seriados
	// =======================//

	// ------------------------------------------------------------------------------------//

	// ========================= Operaciones para detalles no seriados
	// ====================//
	/**
	 * Funci\u00F3n que realiza las tareas de la parte no seriada de las
	 * solicitudes.
	 * 
	 * @param conn
	 * @param input
	 * @param idPadre
	 * @param estadoAlta
	 * @param tipoDevolucion
	 * @param tipoReserva
	 * @param tipoPedido
	 * @param tipoNumPayment
	 * @param banderaOrigenPC
	 * @param estadoDisponible
	 * @param estadoDevolucion
	 * @param estadoPendiente
	 * @param tipoInvSidra
	 * @param nombreNumPayment
	 * @param tipoPDV
	 * @param tipoSiniestro
	 * @param estadoSiniestro
	 * @param siniestroDevolucion
	 * @param tipoPanel
	 * @return
	 * @throws SQLException
	 */
	private static RespuestaSolicitud solicitudNoSeriada(Connection conn, InputSolicitud input, int idPadre,
			String estadoAlta, String tipoDevolucion, String tipoReserva, String tipoPedido, String tipoNumPayment,
			String estadoDisponible, String estadoDevolucion, String estadoPendiente, String tipoInvSidra,
			String nombreNumPayment, String tipoPDV, String tipoSiniestro, String estadoSiniestro,
			boolean siniestroDevolucion, boolean esCancelacion, String tipoPanel, String tipoRuta, BigDecimal ID_PAIS)
			throws SQLException {
		RespuestaSolicitud respSolicitud = new RespuestaSolicitud();
		Respuesta r;
		OutputSolicitud output = new OutputSolicitud();
		boolean insertHijo = false;
		String detalleError = "";

		RespuestaSolicitud insertHijoCant = doPostHijo(conn, idPadre, input, tipoDevolucion, tipoReserva, tipoPedido,
				tipoNumPayment, estadoAlta, estadoDisponible, estadoDevolucion, estadoPendiente, tipoInvSidra,
				nombreNumPayment, tipoPDV, tipoSiniestro, estadoSiniestro, siniestroDevolucion, esCancelacion,
				tipoPanel, tipoRuta, ID_PAIS);

		if (insertHijoCant.isResultado()) {
			if (!insertHijoCant.getDescripcion().equalsIgnoreCase("OK")) {
				detalleError = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TITULO_SOLICITUD_NO_SERIADA_354, null,
						null, null, null, input.getCodArea()).getDescripcion();

				if (insertHijoCant.getDatos().getArticulos().size() > 0) {
					r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TITULO_ARTICULOS_NO_EXISTEN_345, null, null,
							null, null, input.getCodArea());

					output.setDescErrorArticulos(r.getDescripcion());
					detalleError += r.getDescripcion();

					for (int i = 0; i < insertHijoCant.getDatos().getArticulos().size(); i++) {
						detalleError += insertHijoCant.getDatos().getArticulos().get(i).getIdArticulo();
						if (i < insertHijoCant.getDatos().getArticulos().size() - 1) {
							detalleError += ", ";
						} else {
							detalleError += ". ";
						}
					}
				}

				if (insertHijoCant.getDatos().getExistencias().size() > 0) {
					r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TITULO_ARTICULOS_STOCK_346, null, null, null,
							null, input.getCodArea());

					output.setDescErrorExistencias(r.getDescripcion());
					detalleError += r.getDescripcion();

					for (int i = 0; i < insertHijoCant.getDatos().getExistencias().size(); i++) {
						detalleError += insertHijoCant.getDatos().getExistencias().get(i).getIdArticulo();
						if (i < insertHijoCant.getDatos().getExistencias().size() - 1) {
							detalleError += ", ";
						} else {
							detalleError += ". ";
						}
					}
				}

				output.setArticulos(insertHijoCant.getDatos().getArticulos());
				output.setExistencias(insertHijoCant.getDatos().getExistencias());
			}

			insertHijo = true;
		} else {
			insertHijo = false;
			detalleError = "ERROR: " + insertHijoCant.getDescripcion();
		}

		respSolicitud.setResultado(insertHijo);
		respSolicitud.setDescripcion(detalleError);
		respSolicitud.setDatos(output);

		return respSolicitud;
	}

	/**
	 * Funci\u00F3n que realiza las inserciones de la parte no seriada de las
	 * solicitudes.
	 * 
	 * @param conn
	 * @param idPadre
	 * @param input
	 * @param tipoBusqueda
	 * @param estadoAlta
	 * @param estadoDisponible
	 * @param estadoDevolucion
	 * @param estadoPendiente
	 * @param tipoDevolucion
	 * @param estadoSiniestro
	 * @param tipoSiniestro
	 * @param siniestroDevolucion
	 * @param tipoPanel
	 * @param estadoCancelada
	 * @param cancelacion
	 * @return
	 * @throws SQLException
	 */
	private static OutputSolicitud doUpdArticulos(Connection conn, int idPadre, InputSolicitud input, String estadoAlta,
			String estadoDisponible, String estadoDevolucion, String estadoPendiente, String tipoDevolucion,
			String tipoInvSidra, String tipoSiniestro, String estadoSiniestro, boolean siniestroDevolucion,
			boolean esCancelacion, String tipoPanel, String tipoRuta, BigDecimal ID_PAIS) throws SQLException {
		String estado = "";
		String estadoInventario = "";
		List<Filtro> condicionesExistencia = new ArrayList<Filtro>();

		if (input.getTipoSolicitud().equalsIgnoreCase(tipoDevolucion)) {
			estado = estadoPendiente;
			estadoInventario = estadoDevolucion;
		} else if (input.getTipoSolicitud().equalsIgnoreCase(tipoSiniestro)) {
			estado = estadoPendiente;
			estadoInventario = estadoSiniestro;
		} else {
			estado = estadoAlta;
		}

		String selectVendedor = "";
		if (input.getTipo().equals(tipoPanel)) {
			selectVendedor = "SELECT VENDEDOR FROM " + VendedorPDV.N_TABLA + " WHERE TCSCCATPAISID = " + ID_PAIS
					+ " AND IDTIPO = " + input.getIdTipo() + " AND UPPER(TIPO) = '" + input.getTipo().toUpperCase()
					+ "' AND RESPONSABLE = 1";
		} else if (input.getTipo().equals(tipoRuta)) {
			selectVendedor = input.getIdVendedor();
		}

		if (esCancelacion == true) {
			// se intercambia el valor de los estados origen/destino
			estado = estadoInventario;
			estadoInventario = estadoDisponible;
			estadoDisponible = estado;
		}

		String existencia = "";

		List<InputArticuloSolicitud> listItemArticulosFail = new ArrayList<InputArticuloSolicitud>();
		InputArticuloSolicitud itemArticulosFail = new InputArticuloSolicitud();

		List<InputArticuloSolicitud> listItemArticulosStock = new ArrayList<InputArticuloSolicitud>();
		InputArticuloSolicitud itemArticulosStock = new InputArticuloSolicitud();

		OutputSolicitud output = new OutputSolicitud();
		output.setMensaje("OK");

		itemArticulosStock.setDescripcion(new ControladorBase()
				.getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_STOCK_348, null, null, null, null, input.getCodArea())
				.getDescripcion());
		listItemArticulosStock.add(itemArticulosStock);

		itemArticulosFail
				.setDescripcion(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_NO_EXISTENTE_349,
						null, null, null, null, input.getCodArea()).getDescripcion());
		listItemArticulosFail.add(itemArticulosFail);

		for (int j = 0; j < input.getArticulos().size(); j++) {
			String idSolicitudDevolucion = null;
			condicionesExistencia.clear();
			condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					Inventario.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodega()));
			condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					Inventario.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
			condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO,
					input.getArticulos().get(j).getIdArticulo()));
			condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_TIPO_INV,
					input.getArticulos().get(j).getTipoInv()));
			condicionesExistencia
					.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_ESTADO, estadoDisponible));
			if (!selectVendedor.equals("")) {
				condicionesExistencia.add(
						UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Inventario.CAMPO_IDVENDEDOR, selectVendedor));
			}
			if (esCancelacion == true) {
				condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						Inventario.CAMPO_TCSCSOLICITUDID, input.getIdSolicitud()));
			}

			existencia = UtileriasBD.verificarExistencia(conn,
					getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodega(), input.getCodArea()),
					condicionesExistencia);
			if (new Integer(existencia) == 0) {
				// no existe el articulo
				output.setMensaje("GOOD");

				itemArticulosFail = new InputArticuloSolicitud();
				itemArticulosFail.setIdArticulo(input.getArticulos().get(j).getIdArticulo());
				itemArticulosFail.setEstado(input.getArticulos().get(j).getEstado());
				listItemArticulosFail.add(itemArticulosFail);

			} else {

				String existenciaArticulos = UtileriasBD.getOneRecord(conn, Inventario.CAMPO_CANTIDAD,
						getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodega(), input.getCodArea()),
						condicionesExistencia);
				int cantSolicitada = new Integer(input.getArticulos().get(j).getCantidad());
				if (cantSolicitada > new Integer(existenciaArticulos)) {
					output.setMensaje("GOOD");

					itemArticulosStock = new InputArticuloSolicitud();
					itemArticulosStock.setIdArticulo(input.getArticulos().get(j).getIdArticulo());
					itemArticulosStock.setEstado(input.getArticulos().get(j).getEstado());
					listItemArticulosStock.add(itemArticulosStock);

				} else {
					// realiza operaciones en inventario para hacer el movimiento de art\u00EDculos
					boolean resp = updateCantInventario(conn, cantSolicitada, new Integer(existenciaArticulos), idPadre,
							idSolicitudDevolucion, input.getIdBodega(), input.getArticulos().get(j), estadoInventario,
							input.getUsuario(), condicionesExistencia, selectVendedor, estadoDisponible,
							siniestroDevolucion, esCancelacion, input.getCodArea(), ID_PAIS);

					if (!resp) {
						conn.rollback();
						log.error("Rollback: Problema al insertar los datos del inventario.");
						Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INVENTARIO_105,
								null, null, null, null, input.getCodArea());
						output.setMensaje(r.getDescripcion());

						listaLog = new ArrayList<LogSidra>();
						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud,
								"0", Conf.LOG_TIPO_NINGUNO, output.getMensaje(), ""));

						return output;
					}

					if (esCancelacion == false) {
						// realiza las operaciones de los detalles
						RespuestaSolicitud respDetalle = updateDetalles(conn, cantSolicitada,
								new Integer(existenciaArticulos), idPadre, idSolicitudDevolucion,
								input.getArticulos().get(j), estadoPendiente, estado, tipoInvSidra, input.getUsuario(),
								siniestroDevolucion, input.getOrigen(), input.getCodArea(), ID_PAIS);

						if (!respDetalle.isResultado()) {
							conn.rollback();
							log.error("Rollback: Problema al insertar/modificar el detalle de solicitud.");
							output.setMensaje(respDetalle.getRespuesta().getDescripcion());

							listaLog = new ArrayList<LogSidra>();
							listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD,
									servicioPostSolicitud, "0", Conf.LOG_TIPO_NINGUNO, output.getMensaje(), ""));

							return output;
						}
					}

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO,
							servicioPostSolicitud, idPadre + "", Conf.LOG_TIPO_SOLICITUD,
							input.getArticulos().get(j).getCantidad() + " art\u00EDculos identificados con el ID "
									+ input.getArticulos().get(j).getIdArticulo() + " se cambiaron de "
									+ estadoDisponible.toUpperCase() + " a " + estadoInventario.toUpperCase() + ".",
							""));
				}
			}
		}

		// verficar si no se inserto ningun articulo para avisar que se no guarda la
		// solicitud
		listItemArticulosFail.remove(0);
		listItemArticulosStock.remove(0);
		int cantArticulosConError = (listItemArticulosFail.size() + listItemArticulosStock.size());
		if (cantArticulosConError >= new Integer(input.getArticulos().size())) {
			// no se agrego ninguno
			output.setMensaje("ERROR");
			output.setArticulos(null);
			output.setExistencias(null);
			log.error("Ning\u00FAn art\u00EDculo es valido para crear la solicitud.");
		} else {
			// se agrego al menos uno
			output.setArticulos(listItemArticulosFail);
			output.setExistencias(listItemArticulosStock);
		}

		return output;
	}

	private static RespuestaSolicitud updateDetalles(Connection conn, int cantSolicitada,
			Integer existenciaDetalleAnterior, int idSolicitudSiniestro, String idSolicitudDevolucion,
			InputArticuloSolicitud input, String estadoPendiente, String estado, String tipoInvSidra, String usuario,
			boolean siniestroDevolucion, String origen, String codArea, BigDecimal ID_PAIS) throws SQLException {
		int res = 0;

		RespuestaSolicitud respuesta = new RespuestaSolicitud();
		respuesta.setResultado(false);

		List<Filtro> condiciones = new ArrayList<Filtro>();
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, SolicitudDet.CAMPO_TCSCSOLICITUDID,
				idSolicitudSiniestro + ""));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, SolicitudDet.CAMPO_ARTICULO,
				input.getIdArticulo()));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, SolicitudDet.CAMPO_TIPO_INV,
				input.getTipoInv()));
		condiciones.add(
				UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, SolicitudDet.CAMPO_ESTADO, estadoPendiente));

		String existencia = UtileriasBD.verificarExistencia(conn, SolicitudDet.N_TABLA, condiciones);

		if (new Integer(existencia) > 0) {
			// actualizar detalle
			res = updateCantDetalleSolicitud(conn, cantSolicitada, usuario, condiciones, "+");
			if (res < 1) {
				log.error("Problema al actualizar el detalle de la solicitud.");
				respuesta.setRespuesta(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_DETALLE_779,
						null, null, null, null, codArea));

				return respuesta;
			}

		} else {
			// insertar detalle
			respuesta = insertDetalleSolicitud(conn, idSolicitudSiniestro, input, estado, tipoInvSidra, usuario, origen,
					codArea, ID_PAIS);
			if (!respuesta.isResultado()) {
				return respuesta;
			}
		}

		if (siniestroDevolucion && cantSolicitada != existenciaDetalleAnterior) {
			// si es siniestro de devolucion y la cantidad es menor implica restar la
			// cantidad del detalle anterior ya que el nuevo se acaba de insertar

			condiciones.clear();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, SolicitudDet.CAMPO_TCSCSOLICITUDID,
					idSolicitudDevolucion));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, SolicitudDet.CAMPO_ESTADO,
					estadoPendiente));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, SolicitudDet.CAMPO_ARTICULO,
					input.getIdArticulo()));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, SolicitudDet.CAMPO_CANTIDAD,
					existenciaDetalleAnterior + ""));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, SolicitudDet.CAMPO_TIPO_INV,
					input.getTipoInv()));

			// se actualiza detalle anterior
			res = updateCantDetalleSolicitud(conn, cantSolicitada, usuario, condiciones, "-");
			if (res < 1) {
				conn.rollback();
				log.error("Rollback: Problema al actualizar el detalle de la solicitud.");
				respuesta.setRespuesta(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_DETALLE_779,
						null, null, null, null, codArea));

				return respuesta;
			}
		}

		respuesta.setResultado(true);
		return respuesta;
	}
	// ========================= Operaciones para detalles no seriados
	// ====================//

	// ####################################################################################//

	// ========================= Operaciones para actualizar los detalles
	// =================//
	private static RespuestaSolicitud insertDetalleSolicitud(Connection conn, int idSolicitud,
			InputArticuloSolicitud input, String estado, String tipoInvSidra, String usuario, String origen,
			String codArea, BigDecimal ID_PAIS) throws SQLException {
		QueryRunner Qr = new QueryRunner();
		RespuestaSolicitud respuesta = new RespuestaSolicitud();
		respuesta.setResultado(true);

		String[] campos = CtrlSolicitud.obtenerCamposTablaHija(Conf.METODO_POST);
		List<String> inserts = CtrlSolicitud.obtenerInsertsPostHijoAfter(conn, idSolicitud, input,
				SolicitudDet.SEQUENCE, estado, tipoInvSidra, usuario, origen, ID_PAIS);
		String sql = UtileriasBD.armarQueryInsertAll(SolicitudDet.N_TABLA, campos, inserts);

		int res = Qr.update(conn, sql);

		if (res < 1) {
			log.error("Problema al insertar el detalle de solicitud.");
			respuesta.setRespuesta(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_INSERT_DET_SOLICITUD_106,
					null, null, null, null, codArea));
			respuesta.setResultado(false);
		}

		return respuesta;
	}

	private static boolean updateCantInventario(Connection conn, int cantSolicitada, Integer existenciaOrigen,
			int idPadre, String idSolicitudDevolucion, String idBodega, InputArticuloSolicitud input,
			String estadoInventario, String usuario, List<Filtro> condicionesExistencia, String idVendedor,
			String estadoOriginal, boolean siniestroDevolucion, boolean esCancelacion, String codArea,
			BigDecimal ID_PAIS) throws SQLException {
		List<Filtro> condicionesDestino = new ArrayList<Filtro>();
		int res = 0;
		String sql = "";
		QueryRunner Qr = new QueryRunner();
		boolean existeArticuloDestino = false;
		condicionesDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
				Inventario.CAMPO_TCSCBODEGAVIRTUALID, idBodega));
		condicionesDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID,
				ID_PAIS.toString()));
		condicionesDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO,
				input.getIdArticulo()));
		condicionesDestino.add(
				UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_TIPO_INV, input.getTipoInv()));
		condicionesDestino.add(
				UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoInventario));

		if (!esCancelacion) {
			condicionesDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					Inventario.CAMPO_TCSCSOLICITUDID, idPadre + ""));
		}

		String existencia = UtileriasBD.verificarExistencia(conn,
				getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodega, codArea), condicionesDestino);

		if (new Integer(existencia) > 0) {
			existeArticuloDestino = true;
		} else {
			existeArticuloDestino = false;
		}

		if (cantSolicitada == existenciaOrigen) {
			if (existeArticuloDestino) {
				// Actualiza inventario en destino
				res = updateInventarioOrigenDestino(conn, cantSolicitada, usuario, condicionesDestino, "+");
				if (res < 1) {
					return false;
				}

				// Elimina el articulo de bodega origen
				sql = UtileriasBD.armarQueryDelete(getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodega, codArea),
						condicionesExistencia);
				res = Qr.update(conn, sql);
				if (res < 1) {
					return false;
				}

			} else {
				// se actualiza el registro actual
				String camposUpdate[][] = {
						{ Inventario.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoInventario) },
						{ Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
						{ Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) },
						{ Inventario.CAMPO_TCSCSOLICITUDID,
								UtileriasJava.setUpdate(Conf.INSERT_NUMERO, idPadre + "") } };

				sql = UtileriasBD.armarQueryUpdate(getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodega, codArea),
						camposUpdate, condicionesExistencia);

				res = Qr.update(conn, sql);
				if (res < 1) {
					return false;
				}
			}

		} else if (cantSolicitada < existenciaOrigen) {
			// Update inventario origen
			res = updateInventarioOrigenDestino(conn, cantSolicitada, usuario, condicionesExistencia, "-");
			if (res < 1) {
				return false;
			}

			if (existeArticuloDestino) {
				// Actualiza inventario en destino
				res = updateInventarioOrigenDestino(conn, cantSolicitada, usuario, condicionesDestino, "+");
				if (res < 1) {
					return false;
				}

			} else {
				// se crea el registro nuevo
				sql = "INSERT INTO TC_SC_INVENTARIO (TCSCINVENTARIOINVID, TCSCCATPAISID, TCSCBODEGAVIRTUALID, ARTICULO, SERIE, "
						+ "DESCRIPCION, CANTIDAD, ESTADO_COMERCIAL, SERIE_ASOCIADA, SERIADO, TIPO_INV, TCSCSOLICITUDID, "
						+ "TIPO_GRUPO, TIPO_GRUPO_SIDRA, IDVENDEDOR, ESTADO, CREADO_EL, CREADO_POR) "

						+ "(SELECT TC_SC_INVENTARIO_SQ.NEXTVAL, " + ID_PAIS + ", " + idBodega + ", ARTICULO, SERIE, "
						+ "DESCRIPCION, " + cantSolicitada + ", ESTADO_COMERCIAL, SERIE_ASOCIADA, SERIADO, TIPO_INV, "
						+ idPadre + ", " + "TIPO_GRUPO, TIPO_GRUPO_SIDRA, IDVENDEDOR, '" + estadoInventario
						+ "', sysdate, '" + usuario + "' "

						+ "FROM " + getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodega, codArea)
						+ " WHERE ARTICULO = " + input.getIdArticulo() + " AND TCSCBODEGAVIRTUALID = " + idBodega
						+ " AND TCSCCATPAISID = " + ID_PAIS + " AND UPPER(ESTADO) = '" + estadoOriginal.toUpperCase()
						+ "'" + " AND UPPER(TIPO_INV) = '" + input.getTipoInv().toUpperCase() + "'";
				if (!idVendedor.equals("")) {
					sql += " AND IDVENDEDOR IN (" + idVendedor + ")";
				}

				if (siniestroDevolucion) {
					sql += " AND TCSCSOLICITUDID = " + idSolicitudDevolucion;
				}

				sql += ")";
				log.trace("Insert inventario: " + sql);

				res = Qr.update(conn, sql);
				if (res < 1) {
					return false;
				}
			}
		}

		return true;
	}

	private static int updateInventarioOrigenDestino(Connection conn, int cantidad, String usuario,
			List<Filtro> condiciones, String operador) throws SQLException {
		QueryRunner Qr = new QueryRunner();

		String camposUpdateDestino[][] = {
				{ Inventario.CAMPO_CANTIDAD,
						UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
								"(" + Inventario.CAMPO_CANTIDAD + " " + operador + " " + cantidad + ")") },
				{ Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
				{ Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) } };

		String sql = UtileriasBD.armarQueryUpdate(Inventario.N_TABLA, camposUpdateDestino, condiciones);

		return Qr.update(conn, sql);
	}

	private static int updateCantDetalleSolicitud(Connection conn, int cantidad, String usuario,
			List<Filtro> condiciones, String operador) throws SQLException {
		QueryRunner Qr = new QueryRunner();

		String camposUpdDetalle[][] = {
				{ SolicitudDet.CAMPO_CANTIDAD,
						UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
								"(" + SolicitudDet.CAMPO_CANTIDAD + " " + operador + " " + cantidad + ")") },
				{ SolicitudDet.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
				{ SolicitudDet.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) } };

		String sql = UtileriasBD.armarQueryUpdate(SolicitudDet.N_TABLA, camposUpdDetalle, condiciones);

		return Qr.update(conn, sql);
	}

	private static boolean updateDetCompletoSolicitud(Connection conn, String idSolicitud, String estado,
			String usuario) throws SQLException {
		QueryRunner qr = new QueryRunner();

		List<Filtro> condiciones = new ArrayList<Filtro>();
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, SolicitudDet.CAMPO_TCSCSOLICITUDID,
				idSolicitud));

		String[][] campos = { { SolicitudDet.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estado) },
				{ SolicitudDet.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) },
				{ SolicitudDet.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) } };

		String update = UtileriasBD.armarQueryUpdate(SolicitudDet.N_TABLA, campos, condiciones);

		int res = qr.update(conn, update);
		log.debug("Detalles actualizados: " + res);

		return (res > 0 ? true : false);
	}

	private static boolean updateNumRecarga(Connection conn, int idPadre, InputSolicitud input, String estadoAlta,
			String estadoPendiente, String tipoPDV, String usuario, BigDecimal ID_PAIS) throws SQLException {
		Statement stmt = null;
		int[] updateCounts;
		String sqlUpdate = "";
		try {
			stmt = conn.createStatement();
			for (int i = 0; i < input.getArticulos().size(); i++) {
				StringBuilder update = new StringBuilder();
				update.append("UPDATE TC_SC_NUMRECARGA SET ");
				update.append("estado_payment = '" + estadoPendiente + "', ");
				update.append("tcscsolicitudid = " + idPadre + ", ");
				update.append("modificado_por = '" + usuario + "', ");
				update.append("modificado_el = SYSDATE ");
				update.append(" WHERE TCSCCATPAISID = " + ID_PAIS + " AND idtipo = " + input.getIdPDV());
				update.append(" AND UPPER(tipo) = '" + tipoPDV.toUpperCase() + "'");
				update.append(" AND num_recarga = " + input.getArticulos().get(i).getIdArticulo());
				update.append(" AND UPPER(estado) = '" + estadoAlta.toUpperCase() + "'");
				sqlUpdate = update.toString();
				log.debug("upd numRecarga: " + sqlUpdate);
				stmt.addBatch(sqlUpdate);
			}

			updateCounts = stmt.executeBatch();
		} finally {
			DbUtils.closeQuietly(stmt);
		}

		return UtileriasJava.validarBatch(1, updateCounts);
	}

	private static boolean updateNumRecargaCierre(Connection conn, String idSolicitud, String estado, String usuario,
			BigDecimal ID_PAIS) throws SQLException {
		QueryRunner qr = new QueryRunner();

		List<Filtro> condiciones = new ArrayList<Filtro>();
		condiciones.add(
				UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, NumRecarga.CAMPO_TCSCSOLICITUDID, idSolicitud));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, NumRecarga.CAMPO_TCSCCATPAISID,
				ID_PAIS.toString()));

		String[][] campos = {
				{ NumRecarga.CAMPO_ESTADO_PAYMENT, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estado) },
				{ NumRecarga.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) },
				{ NumRecarga.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) } };

		String update = UtileriasBD.armarQueryUpdate(NumRecarga.N_TABLA, campos, condiciones);

		int res = qr.update(conn, update);
		log.debug("Detalles actualizados: " + res);

		return (res > 0 ? true : false);
	}
	// ========================= Operaciones para actualizar los detalles
	// =================//

	// ####################################################################################//

	// ========================= Operaciones para solicitudes tipo siniestro
	// ==============//
	private static RespuestaSolicitud updateDispositivos(Connection conn, int idPadre, InputSolicitud input,
			String estadoAlta, String estadoPendiente, BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "updateDispositivos";
		String nombreClase = new CurrentClassGetter().getClassName();

		RespuestaSolicitud respuesta = new RespuestaSolicitud();

		String codDispositivo = "";
		String sql = "";
		List<Integer> listCantFoliosUpd = new ArrayList<Integer>();

		String nombreDispositivo = UtileriasJava.getConfig(conn, Conf.GRUPO_VALIDACIONES_DINAMICAS,
				Conf.NOMBRE_DISPOSITIVO_SINIESTRO, input.getCodArea());
		String estadoProceso = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SINIESTROS,
				Conf.SINIESTRO_ESTADO_EN_PROCESO, input.getCodArea());

		// se listan los campos a insertar
		String[] campos = { SolicitudDet.CAMPO_TCSCSOLICITUDDETID, SolicitudDet.CAMPO_TCSCSOLICITUDID,
				SolicitudDet.CAMPO_COD_DISPOSITIVO, SolicitudDet.CAMPO_DESCRIPCION, SolicitudDet.CAMPO_CANTIDAD,
				SolicitudDet.CAMPO_ESTADO, SolicitudDet.CAMPO_CREADO_EL, SolicitudDet.CAMPO_CREADO_POR };

		// se crea el listado de condiciones a cumplirse al modificar el dispositivo
		List<Filtro> condiciones = new ArrayList<Filtro>();
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Dispositivo.CAMPO_TCSCCATPAISID,
				ID_PAIS.toString()));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Dispositivo.CAMPO_RESPONSABLE,
				input.getIdTipo()));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Dispositivo.CAMPO_TIPO_RESPONSABLE,
				input.getTipo()));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Dispositivo.CAMPO_ESTADO, estadoAlta));
		condiciones
				.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Dispositivo.CAMPO_CODIGO_DISPOSITIVO, ""));

		// se obtienen los campos y valores a actualizar del dispositivo
		String[][] camposUpdDisp = {
				{ Dispositivo.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoProceso) },
				{ Dispositivo.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) },
				{ Dispositivo.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) }, };

		// se obtienen los campos y valores a actualizar de folios asociados al
		// dispositivo
		String[][] camposUpdFolio = {
				{ ConfiguracionFolioVirtual.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoProceso) },
				{ ConfiguracionFolioVirtual.CAMPO_MODIFICADO_POR,
						UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) },
				{ ConfiguracionFolioVirtual.CAMPO_MODIFICADO_EL,
						UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) }, };

		for (int i = 0; i < input.getDispositivos().size(); i++) {
			codDispositivo = input.getDispositivos().get(i).getCodigoDispositivo();

			// se crea el listado de valores a insertar del detalle
			String inserts = "("
					+ UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, SolicitudDet.SEQUENCE, Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPadre + "", Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, codDispositivo, Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, nombreDispositivo, Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, "1", Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoPendiente, Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO) + ")";

			// se arma el query para la insercion de los detalles y se agrega al batch de
			// inserts
			sql = UtileriasBD.armarQryInsert(SolicitudDet.N_TABLA, campos, inserts);

			try (Statement stmtInsertDetalles = conn.createStatement();) {
				stmtInsertDetalles.addBatch(sql);
			}

			// se cambia el codigo del dispositivo en el listado de condiciones
			condiciones.get(4).setValue("UPPER('" + codDispositivo + "')");

			// se arma el query para actualizar y se agrega al batch de updates
			sql = UtileriasBD.armarQueryUpdate(Dispositivo.N_TABLA, camposUpdDisp, condiciones);
			try (Statement stmtUpdateDispositivos = conn.createStatement();) {
				stmtUpdateDispositivos.addBatch(sql);
			}

			// se establece el log de cambio del dispositivo
			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud, "0",
					Conf.LOG_TIPO_DISPOSITIVO,
					"Se cambi\u00F3 el estado del dispositivo " + codDispositivo + " a " + estadoProceso.toUpperCase()
							+ " por reporte de siniestro en la solicitud " + idPadre + ".",
					""));

			List<Filtro> condicionesFolios = new ArrayList<Filtro>();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,
					ID_PAIS.toString()));
			condicionesFolios.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO,
					Conf.GRUPO_ESTADOS_FOLIOS_SINIESTRO));
			String selectEstadosFoliosBaja = UtileriasBD.armarQuerySelectField(Catalogo.N_TABLA, Catalogo.CAMPO_NOMBRE,
					condicionesFolios, null);

			condicionesFolios.clear();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					ConfiguracionFolioVirtual.CAMPO_TC_SC_CATPAISID, ID_PAIS.toString()));
			condicionesFolios.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
					ConfiguracionFolioVirtual.CAMPO_ID_TIPO, codDispositivo));
			condicionesFolios.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, ConfiguracionFolioVirtual.CAMPO_ESTADO,
					selectEstadosFoliosBaja));

			listCantFoliosUpd.add(UtileriasBD.selectCount(conn, ConfiguracionFolioVirtual.N_TABLA, condicionesFolios));

			// se arma el query para actualizar y se agrega al batch de updates
			sql = UtileriasBD.armarQueryUpdate(ConfiguracionFolioVirtual.N_TABLA, camposUpdFolio, condicionesFolios);

			try (Statement stmtUpdateFolios = conn.createStatement();) {
				stmtUpdateFolios.addBatch(sql);
			}

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud, "0",
					Conf.LOG_TIPO_DISPOSITIVO,
					"Se cambi\u00F3 el estado de los folios del dispositivo c\u00F3digo " + codDispositivo + " a "
							+ estadoProceso.toUpperCase() + " por reporte de siniestro en la solicitud " + idPadre
							+ ".",
					""));
		}

		// se realizan los inserts
		int[] insertCounts;

		try (Statement stmtInsertDetalles = conn.createStatement()) {
			insertCounts = stmtInsertDetalles.executeBatch();
		}
		// se valida que todos los inserts se hayan procesado correctamente
		log.trace("se insertan los detalles");
		boolean resultado = UtileriasJava.validarBatch(1, insertCounts);
		if (resultado) {
			// se realizan los updates
			log.trace("se actualizan los dispositivos");
			int[] updateCounts = {};
			try (Statement stmtUpdateDispositivos = conn.createStatement();) {
				updateCounts = stmtUpdateDispositivos.executeBatch();
			}

			// se valida que todos los updates se hayan procesado correctamente
			resultado = UtileriasJava.validarBatch(1, updateCounts);

			if (!resultado) {
				respuesta.setRespuesta(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_DISPOSITIVOS_773,
						null, nombreClase, nombreMetodo, null, input.getCodArea()));
			}

			// se actualizan los folios del dispositivo
			log.trace("se actualizan los folios");
			try (Statement stmtUpdateFolios = conn.createStatement();) {
				updateCounts = stmtUpdateFolios.executeBatch();
			}

			// se valida que todos los updates se hayan procesado correctamente
			resultado = UtileriasJava.validarBatchRangos(listCantFoliosUpd, updateCounts);

			if (!resultado) {
				respuesta.setRespuesta(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_FOLIOS_781, null,
						nombreClase, nombreMetodo, null, input.getCodArea()));
			}

		} else {
			respuesta.setRespuesta(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_INSERT_DET_SOLICITUD_106,
					null, nombreClase, nombreMetodo, null, input.getCodArea()));
		}

		respuesta.setResultado(resultado);

		return respuesta;
	}

	private static RespuestaSolicitud cancelarDispositivos(Connection conn, String idSolicitud, String estadoAlta,
			String usuario, String codArea, BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "cancelarDispositivos";
		String nombreClase = new CurrentClassGetter().getClassName();

		RespuestaSolicitud respuesta = new RespuestaSolicitud();
		// Statement stmtUpdateDispositivos = conn.createStatement();
		Statement stmtUpdateFolios = conn.createStatement();
		String codDispositivo = "";
		String sql = "";
		String estadoFolio = "";

		List<Filtro> condiciones = new ArrayList<Filtro>();
		List<Filtro> condicionesFolios = new ArrayList<Filtro>();

		try {
			String estadoEnUso = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_FOLIOS_SINIESTRO, Conf.FOLIO_EN_USO,
					codArea);
			String estadoProceso = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SINIESTROS,
					Conf.SINIESTRO_ESTADO_EN_PROCESO, codArea);

			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, SolicitudDet.CAMPO_TCSCSOLICITUDID,
					idSolicitud));
			condiciones.add(
					UtileriasJava.setCondicion(Conf.FILTRO_IS_NOT_NULL_AND, SolicitudDet.CAMPO_COD_DISPOSITIVO, null));

			List<String> listDispositivos = UtileriasBD.getOneField(conn, SolicitudDet.CAMPO_COD_DISPOSITIVO,
					SolicitudDet.N_TABLA, condiciones, null);

			// se obtienen los campos y valores a actualizar de dispositivo
			String[][] camposUpdDisp = {
					{ Dispositivo.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoAlta) },
					{ Dispositivo.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
					{ Dispositivo.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) } };

			for (int i = 0; i < listDispositivos.size(); i++) {
				codDispositivo = listDispositivos.get(i);

				condiciones.clear();
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
						Dispositivo.CAMPO_CODIGO_DISPOSITIVO, codDispositivo));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Dispositivo.CAMPO_TCSCCATPAISID,
						ID_PAIS.toString()));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Dispositivo.CAMPO_ESTADO,
						estadoProceso));

				// se arma el query para actualizar y se agrega al batch de updates
				sql = UtileriasBD.armarQueryUpdate(Dispositivo.N_TABLA, camposUpdDisp, condiciones);

				try (Statement stmtUpdateDispositivos = conn.createStatement();) {
					stmtUpdateDispositivos.addBatch(sql);
				}

				// se establece el log de cambio del dispositivo
				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud, "0",
						Conf.LOG_TIPO_DISPOSITIVO,
						"Se cambi\u00F3 del dispositivo " + codDispositivo + " a estado " + estadoAlta.toUpperCase()
								+ " por cancelaci\u00F3n de reporte de siniestro de la solicitud " + idSolicitud + ".",
						""));

				// se obtienen los folios a actualizar
				condicionesFolios.clear();
				condicionesFolios.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						ConfiguracionFolioVirtual.CAMPO_TC_SC_CATPAISID, ID_PAIS.toString()));
				condicionesFolios.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
						ConfiguracionFolioVirtual.CAMPO_ID_TIPO, codDispositivo));
				condicionesFolios.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
						ConfiguracionFolioVirtual.CAMPO_ESTADO, estadoProceso));

				String[] campos = { ConfiguracionFolioVirtual.CAMPO_TC_SC_FOLIO_ID,
						ConfiguracionFolioVirtual.CAMPO_CANT_UTILIZADOS };
				// se obtiene el listado de datos de folios
				List<Map<String, String>> listFolios = UtileriasBD.getSingleData(conn,
						ConfiguracionFolioVirtual.N_TABLA, campos, condicionesFolios, null);

				for (int j = 0; j < listFolios.size(); j++) {
					if (new Integer(listFolios.get(j).get(ConfiguracionFolioVirtual.CAMPO_CANT_UTILIZADOS)) > 0) {
						estadoFolio = estadoEnUso;
					} else {
						estadoFolio = estadoAlta;
					}

					// se obtienen los campos y valores a actualizar de folios asociados al
					// dispositivo
					String[][] camposUpdFolio = {
							{ ConfiguracionFolioVirtual.CAMPO_ESTADO,
									UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoFolio) },
							{ ConfiguracionFolioVirtual.CAMPO_MODIFICADO_POR,
									UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) },
							{ ConfiguracionFolioVirtual.CAMPO_MODIFICADO_EL,
									UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) } };

					// se establecen las condiciones del folio a actualizar
					List<Filtro> condicionesUpdFolios = new ArrayList<Filtro>();
					condicionesUpdFolios.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
							ConfiguracionFolioVirtual.CAMPO_TC_SC_FOLIO_ID,
							listFolios.get(j).get(ConfiguracionFolioVirtual.CAMPO_TC_SC_FOLIO_ID)));
					condicionesUpdFolios.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
							ConfiguracionFolioVirtual.CAMPO_TC_SC_CATPAISID, ID_PAIS.toString()));
					condicionesUpdFolios.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
							ConfiguracionFolioVirtual.CAMPO_ID_TIPO, codDispositivo));
					condicionesUpdFolios.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
							ConfiguracionFolioVirtual.CAMPO_ESTADO, estadoProceso));

					// se arma el query para actualizar y se agrega al batch de updates
					sql = UtileriasBD.armarQueryUpdate(ConfiguracionFolioVirtual.N_TABLA, camposUpdFolio,
							condicionesUpdFolios);
					stmtUpdateFolios.addBatch(sql);

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_SOLICITUD, servicioPostSolicitud, "0",
							Conf.LOG_TIPO_DISPOSITIVO,
							"Se cambi\u00F3 el estado del folios del dispositivo c\u00F3digo " + codDispositivo + " a "
									+ estadoProceso.toUpperCase() + " por reporte de siniestro en la solicitud "
									+ idSolicitud + ".",
							""));
				}
			}

			// se realizan los updates
			log.trace("se actualizan los dispositivos");
			int[] updateCounts = null;
			try (Statement stmtUpdateDispositivos = conn.createStatement();) {
				updateCounts = stmtUpdateDispositivos.executeBatch();
			}

			// se valida que todos los updates se hayan procesado correctamente
			boolean resultado = UtileriasJava.validarBatch(1, updateCounts);
			if (!resultado) {
				respuesta.setRespuesta(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_DISPOSITIVOS_773,
						null, nombreClase, nombreMetodo, null, codArea));
			}

			// se actualizan los folios del dispositivo
			log.trace("se actualizan los folios");
			updateCounts = stmtUpdateFolios.executeBatch();

			// se valida que todos los updates se hayan procesado correctamente
			resultado = UtileriasJava.validarBatch(1, updateCounts);
			if (!resultado) {
				respuesta.setRespuesta(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_FOLIOS_781, null,
						nombreClase, nombreMetodo, null, codArea));
			}

			respuesta.setResultado(resultado);
		} finally {
			DbUtils.closeQuietly(stmtUpdateFolios);
		}

		return respuesta;
	}

	private static OutputJornada doOperacionesJornada(Connection conn, InputSolicitud input, String estadoIniciada,
			boolean banderaSiniestroTotal, BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "doOperacionesJornada";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta = null;
		InputJornada inputJornada = new InputJornada();
		OutputJornada output = new OutputJornada();

		List<Filtro> condiciones = new ArrayList<Filtro>();
		String idJornadaResponsable = null;

		// se obtienen los datos de la jornada del vendedor
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCCATPAISID,
				ID_PAIS.toString()));
		condiciones.add(
				UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_VENDEDOR, input.getIdVendedor()));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Jornada.CAMPO_ESTADO, estadoIniciada));
		idJornadaResponsable = UtileriasBD.getOneRecord(conn, Jornada.CAMPO_JORNADA_RESPONSABLE,
				getParticion(Jornada.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones);

		if (idJornadaResponsable != null && !idJornadaResponsable.equals("")) {
			condiciones.remove(1); // se quita el filtro de vendedor
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCJORNADAVENID,
					idJornadaResponsable));
		}

		String[] campos = { Jornada.CAMPO_TCSCJORNADAVENID, Jornada.CAMPO_VENDEDOR, Jornada.CAMPO_COD_DISPOSITIVO,
				Jornada.CAMPO_IDTIPO, Jornada.CAMPO_DESCRIPCION_TIPO, Jornada.CAMPO_TCSCBODEGAVIRTUAL };
		Map<String, String> datosJornada = UtileriasBD.getSingleFirstData(conn,
				getParticion(Jornada.N_TABLA, Conf.PARTITION, "", input.getCodArea()), campos, condiciones);

		if (datosJornada.isEmpty()) {
			// error, no se encontro la jornada correcta
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTEN_JORNADAS_747, null,
					nombreClase, nombreMetodo, "", input.getCodArea());
			output.setRespuesta(respuesta);

			return output;
		} else {
			if (!datosJornada.get(Jornada.CAMPO_IDTIPO).equals(input.getIdTipo())
					|| !datosJornada.get(Jornada.CAMPO_DESCRIPCION_TIPO).equalsIgnoreCase(input.getTipo())) {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_TIPOS_JORNADA_737, null,
						nombreClase, nombreMetodo, "", input.getCodArea());
				output.setRespuesta(respuesta);

				return output;
			}

			inputJornada.setIdResponsable(datosJornada.get(Jornada.CAMPO_VENDEDOR));
			inputJornada.setIdJornada(datosJornada.get(Jornada.CAMPO_TCSCJORNADAVENID));
			inputJornada.setIdBodegaVendedor(datosJornada.get(Jornada.CAMPO_TCSCBODEGAVIRTUAL));
			inputJornada.setCodDispositivo(datosJornada.get(Jornada.CAMPO_COD_DISPOSITIVO));
			log.trace("id Jornada solicitud:" + inputJornada.getIdJornada());
		}

		// si es siniestro total se cierran las jornadas
		if (banderaSiniestroTotal) {
			String obsJornada = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.OBS_JORNADA_SINIESTRO,
					input.getCodArea());

			// se cierra forzosamente jornada de los vendedores involucrados
			// se setean valores faltantes para cerrar jornadas
			inputJornada.setUsuario(input.getUsuario());
			inputJornada.setIdDistribuidor(input.getIdDTS());
			inputJornada.setObservaciones(obsJornada);
			inputJornada.setCodArea(input.getCodArea());

			OutputJornada respJornada = new CtrlJornadaMasiva().getDatos(inputJornada, Conf.METODO_PUT, true, conn);
			if (new BigDecimal(respJornada.getRespuesta().getCodResultado())
					.intValue() != Conf_Mensajes.OK_MOD_JORNADA_46) {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_JORNADA_SINIESTRO_762, null,
						nombreClase, nombreMetodo, null, input.getCodArea());
				respuesta
						.setDescripcion(respuesta.getDescripcion() + " " + respJornada.getRespuesta().getDescripcion());
				output.setRespuesta(respuesta);

				return output;
			}
		}

		// se setean y se devuelven los datos de jornada
		output.setJornada(inputJornada);

		return output;
	}

	/**
	 * Funci\u00F3n que actualiza todos los articulos del inventario cuando es una
	 * solicitud de tipo Siniestro Total.
	 * 
	 * @param conn
	 * @param idPadre
	 * @param idResponsable
	 * @param idBodegaResponsable
	 * @param estadoDisponible
	 * @param estadoDevolucion
	 * @param estadoSiniestro
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private static RespuestaSolicitud updateSiniestroTotal(Connection conn, int idPadre, String idBodegaResponsable,
			String estadoDisponible, String estadoSiniestro, String usuario, String estadoPendiente, String codArea,
			BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "updateSiniestroTotal";
		String nombreClase = new CurrentClassGetter().getClassName();

		RespuestaSolicitud respuesta = new RespuestaSolicitud();
		QueryRunner Qr = new QueryRunner();
		List<Filtro> condiciones = new ArrayList<Filtro>();

		// se continua con el proceso de siniestro total
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID,
				idBodegaResponsable));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID,
				ID_PAIS.toString()));
		condiciones
				.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Inventario.CAMPO_ESTADO, estadoDisponible));

		// se verifica que la bodega tenga inventario siniestrable
		String existencia = UtileriasBD.verificarExistencia(conn,
				getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaResponsable, codArea), condiciones);
		if ("0".equals(existencia)) {
			respuesta.setResultado(false);
			respuesta.setDescripcion(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_INVENTARIO_397, null,
					nombreClase, nombreMetodo, null, codArea).getDescripcion());

			return respuesta;
		}

		// se inserta el detalle de la solicitud con todos los articulos antes de
		// modificarlos
		String[] camposDet = CtrlSolicitud.obtenerCamposTablaHija(Conf.METODO_POST);

		String sql = "INSERT INTO " + SolicitudDet.N_TABLA + " (" + UtileriasBD.getCampos(camposDet) + ") "

				+ "WITH DATA AS (" + "SELECT " + Inventario.CAMPO_ARTICULO + ", " + Inventario.CAMPO_DESCRIPCION + ", "
				+ Inventario.CAMPO_SERIE + ", " + Inventario.CAMPO_SERIE_ASOCIADA + ", " + Inventario.CAMPO_CANTIDAD
				+ ", " + Inventario.CAMPO_TIPO_INV + " FROM "
				+ getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaResponsable, codArea)
				+ UtileriasBD.getCondiciones(condiciones) + ")"

				+ " SELECT " + SolicitudDet.SEQUENCE + ", " + idPadre + ", " + SolicitudDet.CAMPO_ARTICULO + ", "
				+ SolicitudDet.CAMPO_DESCRIPCION + ", " + SolicitudDet.CAMPO_SERIE + ", " + "NULL, "
				+ SolicitudDet.CAMPO_SERIE_ASOCIADA + ", " + SolicitudDet.CAMPO_CANTIDAD + ", "
				+ SolicitudDet.CAMPO_TIPO_INV + ", " + "'" + estadoPendiente + "', " + "SYSDATE, " + "'" + usuario
				+ "' " + "FROM DATA";

		log.debug("Insert detalle: " + sql);

		// Insert detalle
		int res = Qr.update(conn, sql);

		if (res > 0) {
			// se inserto correctamente el detalle, se procede a actualizar el inventario
			String[][] camposInv = {
					{ Inventario.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoSiniestro) },
					{ Inventario.CAMPO_TCSCSOLICITUDID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, idPadre + "") },
					{ Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) },
					{ Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) }, };

			sql = UtileriasBD.armarQueryUpdate(
					getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaResponsable, codArea), camposInv,
					condiciones);

			res = Qr.update(conn, sql);
			if (res > 0) {
				respuesta.setResultado(true);

			} else {
				respuesta.setResultado(false);
				respuesta.setDescripcion(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INVENTARIO_105,
						null, nombreClase, nombreMetodo, null, codArea).getDescripcion());
			}
		} else {
			respuesta.setResultado(false);
			respuesta.setDescripcion(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_INSERT_DET_SOLICITUD_106,
					null, nombreClase, nombreMetodo, null, codArea).getDescripcion());
		}

		return respuesta;
	}

	// ========================= Operaciones para solicitudes tipo siniestro
	// ==============//

	// ####################################################################################//
}
