package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.ms.seguridad.excepciones.ExcepcionSeguridad;
import com.consystec.sc.ca.ws.input.bodegadts.InputBodegaVirtual;
import com.consystec.sc.ca.ws.input.condicionoferta.InputDetCondicionOferta;
import com.consystec.sc.ca.ws.input.pdv.InputPDV;
import com.consystec.sc.ca.ws.input.vendedordts.InputVendedorDTS;
import com.consystec.sc.ca.ws.input.venta.ArticuloPromocionalVenta;
import com.consystec.sc.ca.ws.input.venta.ArticuloVenta;
import com.consystec.sc.ca.ws.input.venta.Descuento;
import com.consystec.sc.ca.ws.input.venta.DetallePago;
import com.consystec.sc.ca.ws.input.venta.Impuesto;
import com.consystec.sc.ca.ws.input.venta.InputGetDetalle;
import com.consystec.sc.ca.ws.input.venta.InputGetVenta;
import com.consystec.sc.ca.ws.input.venta.InputVenta;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.venta.OutputArticuloVenta;
import com.consystec.sc.ca.ws.output.venta.OutputVenta;
import com.consystec.sc.sv.ws.orm.ArticulosSidra;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.ConfiguracionFolioVirtual;
import com.consystec.sc.sv.ws.orm.DescuentosSidra;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.Exento;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.PagoDet;
import com.consystec.sc.sv.ws.orm.PagoImpuesto;
import com.consystec.sc.sv.ws.orm.PrecioArticulo;
import com.consystec.sc.sv.ws.orm.Promocionales;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.orm.Venta;
import com.consystec.sc.sv.ws.orm.VentaDet;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.JavaUtils;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.ericsson.sdr.utils.Country;

public class OperacionVentas {
	private OperacionVentas() {
	}

	private static final Logger log = Logger.getLogger(OperacionVentas.class);

	/***
	 * M\u00E9todo para verificar datos generales dela venta
	 * 
	 * @param conn
	 * @param estadoAlta
	 * @param vendedor
	 * @param idBodega
	 * @param jornada
	 * @param estadoIniciada
	 * @return
	 * @throws SQLException
	 */
	public static List<BigDecimal> validacionPrincipal(Connection conn, String estadoAlta, String vendedor,
			String idBodega, String jornada, String estadoIniciada, BigDecimal idPais, String codArea)
			throws SQLException {
		List<BigDecimal> lstDatos = new ArrayList<BigDecimal>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String tablaJornadaConParticion = ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea);
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT (SELECT COUNT (1) ");
		sql.append(" FROM " + tablaJornadaConParticion);
		sql.append(" WHERE UPPER(ESTADO) = '" + estadoIniciada.toUpperCase() + "' AND VENDEDOR = " + vendedor);
		sql.append(" AND TCSCJORNADAVENID = " + jornada + ") JORNADA, ");
		sql.append("NVL((SELECT TCSCBODEGAVIRTUALID ");
		sql.append("FROM TC_SC_VEND_DTS ");
		sql.append("WHERE VENDEDOR = " + vendedor + " AND TCSCCATPAISID = " + idPais);
		sql.append(" AND UPPER(ESTADO) = '" + estadoAlta.toUpperCase() + "'), 0) VENDEDOR, ");
		sql.append("(SELECT COUNT (1) ");
		sql.append("FROM TC_SC_BODEGA_VENDEDOR ");
		sql.append(" WHERE VENDEDOR = ");
		sql.append("(SELECT CASE");
		sql.append(" WHEN (SELECT COUNT(1) FROM " + tablaJornadaConParticion);
		sql.append(" WHERE TCSCJORNADAVENID = " + jornada + " AND JORNADA_RESPONSABLE IS NULL) > 0");
		sql.append(" THEN (SELECT VENDEDOR FROM " + tablaJornadaConParticion + " WHERE TCSCJORNADAVENID = " + jornada
				+ ")");
		sql.append(" WHEN (SELECT COUNT(1) FROM " + tablaJornadaConParticion + " WHERE JORNADA_RESPONSABLE = " + jornada
				+ ") = 0");
		sql.append(" THEN (SELECT VENDEDOR FROM " + tablaJornadaConParticion + " WHERE TCSCJORNADAVENID = ");
		sql.append("(SELECT JORNADA_RESPONSABLE FROM " + tablaJornadaConParticion);
		sql.append(" WHERE TCSCJORNADAVENID = " + jornada + "))");
		sql.append(" ELSE (SELECT VENDEDOR FROM " + tablaJornadaConParticion + " WHERE TCSCJORNADAVENID = " + jornada
				+ ") ");
		sql.append("END FROM DUAL) AND TCSCBODEGAVIRTUALID = " + idBodega + ") BODEGA_VENDEDOR FROM DUAL");

		log.debug("validaciones principales venta: " + sql.toString());
		try {
			pstmt = conn.prepareStatement(sql.toString());
			rst = pstmt.executeQuery();

			if (rst.next()) {
				lstDatos.add(0, rst.getBigDecimal(1));
				lstDatos.add(1, rst.getBigDecimal(2));
				lstDatos.add(2, rst.getBigDecimal(3));
			}
		} finally {

			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return lstDatos;
	}

	/**
	 * M\u00E9todo para validar si existe un punto de venta ingresado
	 * 
	 * @param conn
	 * @param estado
	 * @param pdv
	 * @return
	 * @throws SQLException
	 */
	public static BigDecimal existePDV(Connection conn, String estado, String pdv, BigDecimal idPais)
			throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String sql = "SELECT DECODE (TIPO_CONTRIBUYENTE, " + "               (SELECT valor "
				+ "                  FROM tc_sc_configuracion "
				+ "                 WHERE nombre = 'PequenioContribuyente' AND TCSCCATPAISID =?), 1, "
				+ "               2) " + "          existe " + "  FROM TC_SC_PUNTOVENTA  "
				+ " WHERE TCSCPUNTOVENTAID =? AND ESTADO = ? AND TCSCCATPAISID =?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1, idPais);
			pstmt.setBigDecimal(2, new BigDecimal(pdv));
			pstmt.setString(3, estado.toUpperCase());
			pstmt.setBigDecimal(4, idPais);
			rst = pstmt.executeQuery();
			log.debug("EXISTE PDV: " + sql);
			if (rst.next()) {
				ret = rst.getBigDecimal(1);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}

	/**
	 * M\u00E9todo para validar si existe un cliente final
	 * 
	 * @param conn
	 * @param estado
	 * @param pdv
	 * @return
	 * @throws SQLException
	 */
	public static BigDecimal existeCF(Connection conn, String estado, String idCliente) throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String sql = "SELECT COUNT (1) " + "  FROM TC_SC_CLIENTE" + " WHERE TCSCCLIENTEID =? AND ESTADO =?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1, new BigDecimal(idCliente));
			pstmt.setString(2, estado.toUpperCase());
			rst = pstmt.executeQuery();

			if (rst.next()) {
				ret = rst.getBigDecimal(1);
			}
		} finally {

			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}

	/**
	 * M\u00E9todo para verificar que el punto de venta este asociado al vendedor
	 * ingresado
	 * 
	 * @param conn
	 * @param estado
	 * @param pdv
	 * @param vendedor
	 * @param estadoActivo
	 * @param idJornada
	 * @return
	 * @throws SQLException
	 */
	public static BigDecimal PVDasociadoVend(Connection conn, String estado, String pdv, String vendedor,
			String estadoActivo, String idJornada, BigDecimal idPais, String codArea) throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT (SELECT COUNT (1) FROM TC_SC_RUTA_PDV ");
		sql.append("WHERE TCSCPUNTOVENTAID = " + pdv);
		sql.append(" AND TCSCRUTAID IN (SELECT TCSCRUTAID FROM TC_SC_RUTA ");
		sql.append("WHERE SECUSUARIOID = " + vendedor + " AND UPPER(ESTADO) = '" + estado.toUpperCase() + "' ");
		sql.append("AND TCSCCATPAISID = " + idPais + "))");
		sql.append(" + (SELECT COUNT(1) FROM TC_SC_PUNTOVENTA");
		sql.append(" WHERE TCSCPUNTOVENTAID = " + pdv + " AND TCSCCATPAISID = " + idPais);
		sql.append(" AND UPPER(ESTADO) = '" + estadoActivo.toUpperCase() + "'");
		sql.append(" AND TCSCDTSID = (SELECT TCSCDTSID FROM "
				+ ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea));
		sql.append(" WHERE TCSCJORNADAVENID = " + idJornada + " AND TCSCCATPAISID = " + idPais + ")) A FROM DUAL");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			rst = pstmt.executeQuery();

			if (rst.next()) {
				ret = rst.getBigDecimal(1);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return ret;
	}

	public static Inventario getArticulo(Connection conn, String estado, String serie, String idBodega, String articulo,
			String tipoInv, boolean recarga, String idJornada, String codArea, BigDecimal idPais) throws SQLException {
		Inventario objInv = new Inventario();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String serieCondicion = "";

		if (recarga) {
			idBodega = "SELECT IDBODEGA_PADRE FROM "
					+ ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", codArea)
					+ " WHERE TCSCBODEGAVIRTUALID IN (" + " SELECT CASE DESCRIPCION_TIPO "
					+ " WHEN 'PANEL' THEN (SELECT TCSCBODEGAVIRTUALID FROM TC_SC_PANEL WHERE TCSCPANELID = IDTIPO AND TCSCCATPAISID = "
					+ idPais + ") "
					+ "WHEN 'RUTA' THEN (SELECT TCSCBODEGAVIRTUALID FROM TC_SC_RUTA WHERE TCSCRUTAID = IDTIPO AND TCSCCATPAISID = "
					+ idPais + ") " + " END FROM "
					+ ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea)
					+ " WHERE TCSCJORNADAVENID = " + idJornada + ")";
		}

		if (serie == null || "".equals(serie)) {
			serieCondicion = " IS NULL ";
		} else {
			serieCondicion = " = '" + serie + "' ";
		}

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * ");
		sql.append("  FROM " + ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodega, codArea));
		sql.append(" WHERE SERIE" + serieCondicion);
		sql.append("   AND ARTICULO = " + articulo);
		sql.append("   AND TIPO_INV = '" + tipoInv + "'");
		sql.append("   AND TCSCBODEGAVIRTUALID IN (" + idBodega + ")");
		sql.append(" AND UPPER(ESTADO) IN ('" + estado.toUpperCase() + "')");
		sql.append(" AND CANTIDAD > 0");

		log.debug("query inv: " + sql.toString());
		try {
			pstmt = conn.prepareStatement(sql.toString());
			rst = pstmt.executeQuery();

			if (rst.next()) {
				objInv.setArticulo(rst.getBigDecimal(Inventario.CAMPO_ARTICULO));
				objInv.setCantidad(rst.getBigDecimal(Inventario.CAMPO_CANTIDAD));
				objInv.setDescripcion(rst.getString(Inventario.CAMPO_DESCRIPCION));
				objInv.setEstado(rst.getString(Inventario.CAMPO_ESTADO));
				objInv.setEstado_comercial(rst.getString(Inventario.CAMPO_ESTADO_COMERCIAL));
				objInv.setSeriado(rst.getString(Inventario.CAMPO_SERIADO));
				objInv.setSerie(rst.getString(Inventario.CAMPO_SERIE));
				objInv.setTcscbodegavirtualid(rst.getBigDecimal(Inventario.CAMPO_TCSCBODEGAVIRTUALID));
				objInv.setTcscinventarioinvid(rst.getBigDecimal(Inventario.N_TABLA_ID));
				objInv.setTipo_inv(rst.getString(Inventario.CAMPO_TIPO_INV));
				objInv.setTipo_grupo(rst.getString(Inventario.CAMPO_TIPO_GRUPO));
				objInv.setTipo_grupo_sidra(rst.getString(Inventario.CAMPO_TIPO_GRUPO_SIDRA));
				objInv.setTecnologia(rst.getString(Inventario.CAMPO_TECNOLOGIA));
			} else {
				objInv = null;
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return objInv;
	}

	/**
	 * Verifica si la oferta comercial de la cual se obtuvo un descuento existe
	 * 
	 * @param conn
	 * @param articulo
	 * @param tipoGestionVenta
	 * @param tipoOferta
	 * @param estadoAlta
	 * @param tipoPanelRuta
	 * @param idPanelRuta
	 * @param idOfertaCampania
	 * @param tipoGestionVenta
	 * @param tipoOferta
	 * @param idOfertaCampania
	 * @param tipoDescuento
	 * @param modoOnline
	 * @param tipoCliente
	 * @param idPDV
	 * @param tecnologiaArt
	 * @param idCondicion
	 * @param
	 * @return
	 * @throws SQLException
	 */
	public static BigDecimal existeOfCampania(Connection conn, String articulo, String idOfertaCampania,
			String idPanelRuta, String tipoPanelRuta, String estadoAlta, String tipoOferta, String tipoGestionVenta,
			String tipoDescuento, boolean modoOnline, String tipoCliente, String idPDV, String tecnologia,
			String idCondicion, BigDecimal idPais, String codArea) throws SQLException {
		BigDecimal ret = BigDecimal.ZERO;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		estadoAlta = estadoAlta.toUpperCase();
		String tipoOfertaVenta = ""; // ARTICULO, PDV, ZONA, TECNOLOGIA o PAGUE_LLEVE
		String tipoCondicion = ""; // ARTICULO o PDV
		// tipoGestionVenta = ""; //VENTA o ALTA_PREPAGO
		String condicionIdPDV = "";
		String condicionTipoCliente = " AND (UPPER(C.TIPO_CLIENTE) = '" + tipoCliente.toUpperCase()
				+ "' OR UPPER(C.TIPO_CLIENTE) = 'AMBOS')";
		String condicionTecnologia = "";
		String condicionArticulo = " AND C.ARTICULO = " + articulo;
		String condicionIdCondicion = "";

		if (tipoDescuento.equalsIgnoreCase("ARTICULO")) {
			tipoCondicion = "ARTICULO";
			tipoOfertaVenta = "ARTICULO";

		} else if (tipoDescuento.equalsIgnoreCase("TECNOLOGIA")) {
			tipoCondicion = "ARTICULO";
			tipoOfertaVenta = "TECNOLOGIA";
			condicionArticulo = "";
			condicionTecnologia = " AND UPPER(C.TECNOLOGIA) = '" + tecnologia + "'";

		} else if (tipoDescuento.equalsIgnoreCase("PAGUE_LLEVE")) {
			tipoCondicion = "PAGUE_LLEVE";
			tipoOfertaVenta = "PAGUE_LLEVE";
			condicionIdCondicion = " AND C.TCSCCONDICIONID = " + idCondicion;
			condicionArticulo = " AND C.ARTICULO_REGALO = " + articulo;

		} else {
			tipoCondicion = "PDV";
			condicionTipoCliente = "";

			if (tipoDescuento.equalsIgnoreCase("PDV")) {
				tipoOfertaVenta = "PDV";
				condicionIdPDV = " AND C.TCSCPUNTOVENTAID = " + idPDV;
			} else if (tipoDescuento.equalsIgnoreCase("ZONA")) {
				tipoOfertaVenta = "ZONA";
			} else {
				return BigDecimal.ZERO;
			}
		}

		String sql = existeOfXampnia(idOfertaCampania, idPanelRuta, tipoPanelRuta, estadoAlta, tipoOferta,
				tipoGestionVenta, modoOnline, condicionIdPDV, condicionTipoCliente, condicionTecnologia,
				condicionArticulo, condicionIdCondicion, tipoCondicion, tipoOfertaVenta, idPais, codArea);

		log.debug("Query para consultar ofertas: " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				ret = rst.getBigDecimal(1);
			}

		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}

	public static String existeOfXampnia(String idOfertaCampania, String idPanelRuta, String tipoPanelRuta,
			String estadoAlta, String tipoOferta, String tipoGestionVenta, boolean modoOnline, String condicionIdPDV,
			String condicionTipoCliente, String condicionTecnologia, String condicionArticulo,
			String condicionIdCondicion, String tipoCondicion, String tipoOfertaVenta, BigDecimal idPais,
			String codArea) {
		String sql = "SELECT COUNT(1) FROM"
				+ " TC_SC_DET_PANELRUTA A, TC_SC_CONDICION B, TC_SC_DET_CONDICION_OFERTA C, TC_SC_OFERTA_CAMPANIA PARTITION ("
				+ ControladorBase.getPais(codArea) + ") D" + " WHERE A.TCSCTIPOID = " + idPanelRuta
				+ " AND UPPER(A.TIPO) = '" + tipoPanelRuta.toUpperCase() + "'"
				+ (modoOnline ? " AND UPPER(A.ESTADO) = '" + estadoAlta + "'" : "")
				+ " AND A.TCSCOFERTACAMPANIAID = B.TCSCOFERTACAMPANIAID"
				+ " AND B.TCSCOFERTACAMPANIAID = D.TCSCOFERTACAMPANIAID" + " AND A.TCSCOFERTACAMPANIAID = "
				+ idOfertaCampania + " AND UPPER(B.TIPO_OFERTACAMPANIA) = '" + tipoOferta.toUpperCase() + "'"
				+ " AND UPPER(B.TIPO_CONDICION) = '" + tipoCondicion + "'" + " AND UPPER(B.TIPO_GESTION) = '"
				+ tipoGestionVenta.toUpperCase() + "'"
				+ (modoOnline ? " AND UPPER(B.ESTADO) = '" + estadoAlta + "'" : "")
				+ " AND B.TCSCCONDICIONID = C.TCSCCONDICIONID" + " AND A.TCSCCATPAISID = B.TCSCCATPAISID "
				+ " AND C.TCSCCATPAISID = B.TCSCCATPAISID " + " AND B.TCSCCATPAISID = D.TCSCCATPAISID "
				+ " AND D.TCSCCATPAISID = " + idPais + condicionArticulo + " AND UPPER(C.TIPO_OFERTA) = '"
				+ tipoOfertaVenta + "'" + condicionTipoCliente + condicionIdCondicion + condicionIdPDV
				+ condicionTecnologia + (modoOnline ? " AND UPPER(C.ESTADO) = '" + estadoAlta + "'" : "")
				+ " AND UPPER(D.TIPO) = '" + tipoOferta.toUpperCase() + "'"
				+ (modoOnline
						? " AND UPPER(D.ESTADO) = '" + estadoAlta
								+ "' AND TRUNC(SYSDATE) BETWEEN TRUNC(D.FECHA_DESDE) AND TRUNC(D.FECHA_HASTA)"
						: "");

		return sql;
	}

	public static BigDecimal existeCampania(Connection conn, String articulo, String idOferta, String estado,
			boolean modoOnline, BigDecimal idPais) throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT COUNT(1) FROM TC_SC_DET_ARTPROMO ");
		sql.append("WHERE TCSCOFERTACAMPANIAID = " + idOferta);
		sql.append(" AND TCSCARTPROMOCIONALID = " + articulo);
		sql.append(" AND TCSCCATPAISID=" + idPais);
		sql.append((modoOnline ? " AND UPPER(ESTADO) = '" + estado.toUpperCase() + "'" : ""));
		log.debug("existe art promocional en CAMPANIA: " + sql.toString());
		try {
			pstmt = conn.prepareStatement(sql.toString());
			rst = pstmt.executeQuery();

			if (rst.next()) {
				ret = rst.getBigDecimal(1);
			}

		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}

	public static int validaSerieIndividual(Connection conn, String serie, String idBodegaVendedor, String articulo,
			String estadoAnulado, String estadoAnuladoSCL, String codArea) throws SQLException {
		int respuesta = 0;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT COUNT(1)");
		sql.append(" FROM TC_SC_VENTA_DET");
		sql.append(" WHERE articulo=" + articulo);
		sql.append(" AND tcscbodegavirtualid=" + idBodegaVendedor + "");
		sql.append(" AND serie = '" + serie + "'");
		sql.append(" AND tcscventaid NOT IN (");
		sql.append("SELECT tcscventaid");
		sql.append(" FROM " + ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea));
		sql.append(" WHERE UPPER(estado)in ('" + estadoAnulado.toUpperCase() + "','" + estadoAnuladoSCL.toUpperCase()
				+ "'))");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			rst = pstmt.executeQuery();
			log.debug(sql);

			if (rst.next()) {
				respuesta = rst.getInt(1);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return respuesta;
	}

	/***
	 * Validar si el rango de series de tarjeta rasca existe
	 * 
	 * @param conn
	 * @param rangoInicial
	 * @param rangoFinal
	 * @param articulo
	 * @param bodega
	 * @param codArea
	 * @return
	 * @throws SQLException
	 */
	public static String validarSeriesRasca(Connection conn, String rangoInicial, String rangoFinal,
			BigDecimal articulo, BigDecimal bodega, String codArea) throws SQLException {
		String respuesta = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String nombrePaquete = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.PAQUETE_SIDRA, codArea);
		int diferenciaCeros = OperacionMovimientosInventario.getDiferenciaCeros(rangoInicial);

		String sql = queryValidaSeries(rangoInicial, rangoFinal, articulo, bodega, nombrePaquete, diferenciaCeros,
				codArea);
		try {
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();
			log.debug(sql);

			if (rst.next()) {
				respuesta = rst.getString(1);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return respuesta;
	}

	public static String queryValidaSeries(String rangoInicial, String rangoFinal, BigDecimal articulo,
			BigDecimal bodega, String nombrePaquete, int diferenciaCeros, String codArea) {
		String sql = "SELECT " + nombrePaquete + ".F_TC_SC_VALIDAD_RANGO" + " (" + rangoInicial + ", " + rangoFinal
				+ ", " + bodega + ", " + articulo + ", " + diferenciaCeros + ", " + codArea + ") " + " FROM DUAL";
		return sql;
	}

	// validar series de Simcar
	// cambio realizado por jcsimon el 28/09/2017
	public static String validarSeriesSimcard(Connection conn, String rangoInicial, String rangoFinal,
			BigDecimal articulo, BigDecimal bodega, String codArea) throws SQLException {
		String respuesta = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String nombrePaquete = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.PAQUETE_SIDRA, codArea);
		int diferenciaCeros = OperacionMovimientosInventario.getDiferenciaCeros(rangoInicial);

		String sql = queryValidaSeriesSim(rangoInicial, rangoFinal, articulo, bodega, nombrePaquete, diferenciaCeros,
				codArea);
		try {
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();
			log.debug(sql);

			if (rst.next()) {
				respuesta = rst.getString(1);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return respuesta;
	}

	public static String queryValidaSeriesSim(String rangoInicial, String rangoFinal, BigDecimal articulo,
			BigDecimal bodega, String nombrePaquete, int diferenciaCeros, String codArea) {
		String sql = "SELECT " + nombrePaquete + ".F_TC_SC_VALIDAR_RANGOS_VENTAS" + " (" + rangoInicial + ", "
				+ rangoFinal + ", " + bodega + ", " + articulo + ", " + diferenciaCeros + ", " + codArea + ") "
				+ " FROM DUAL";
		return sql;
	}

	// Fin del cambio validar series de Simcar creado por jcsimon 28/09/2017
	/***
	 * Para marcar como vendidos articulos seriados en inventario
	 * 
	 * @param conn
	 * @param series
	 * @param bodega
	 * @param estadoVendido
	 * @param estadoDisponible
	 * @param articulo
	 * @throws SQLException
	 */
	public static void updateInvSerie(Connection conn, String series, String estadoVendido, String estadoDisponible,
			BigDecimal idVenta, String usuario, BigDecimal idPais) throws SQLException {
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();

		sql.append("UPDATE TC_SC_INVENTARIO SET ESTADO=?,");
		sql.append(" TCSCVENTAID=?, MODIFICADO_EL=SYSDATE,");
		sql.append(" MODIFICADO_POR=? WHERE upper(estado)=?");
		sql.append(" AND SERIE IN (" + series + ") AND TCSCCATPAISID=?");

		log.debug("update inv:" + sql.toString());
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, estadoVendido);
			pstmt.setBigDecimal(2, idVenta);
			pstmt.setString(3, usuario);
			pstmt.setString(4, estadoDisponible.toUpperCase());
			pstmt.setBigDecimal(5, idPais);
			pstmt.execute();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
	}

	/**
	 * Para restar cant de inventario a articulos no seriados
	 * 
	 * @param conn
	 * @param series
	 * @param bodega
	 * @param estadoVendido
	 * @param estadoDisponible
	 * @param articulo
	 * @throws SQLException
	 */
	public static void updateInvCant(Connection conn, String bodega, String estadoDisponible, String articulo, int cant,
			String usuario, BigDecimal idPais) throws SQLException {
		PreparedStatement pstmt = null;

		String sql = "UPDATE TC_SC_INVENTARIO SET CANTIDAD=(CANTIDAD-?), MODIFICADO_EL=SYSDATE, MODIFICADO_POR=?"
				+ " WHERE UPPER(ESTADO)=?"
				+ " AND TCSCBODEGAVIRTUALID=? AND ARTICULO=? AND SERIE IS NULL AND TCSCCATPAISID=?";
		log.debug("update inv cantidad: " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cant);
			pstmt.setString(2, usuario);
			pstmt.setString(3, estadoDisponible.toUpperCase());
			pstmt.setBigDecimal(4, new BigDecimal(bodega));
			pstmt.setBigDecimal(5, new BigDecimal(articulo));
			pstmt.setBigDecimal(6, idPais);
			pstmt.execute();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
	}

	public static void insertarInv(Connection conn, String articulo, String estadoVendido, String Disponible,
			String idBodegaVirtual, String tipoInv, String cant, BigDecimal idVenta, BigDecimal idPais)
			throws SQLException {
		PreparedStatement pstmt = null;
		String insert = "";

		insert = insertaInvq(articulo, estadoVendido, Disponible, idBodegaVirtual, tipoInv, cant, idVenta, idPais);

		log.debug("qry insert inv cant: " + insert);
		try {
			pstmt = conn.prepareStatement(insert);
			pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
	}

	public static String insertaInvq(String articulo, String estadoVendido, String Disponible, String idBodegaVirtual,
			String tipoInv, String cant, BigDecimal idVenta, BigDecimal idPais) {
		String insert = "";
		insert = " INSERT INTO " + Inventario.N_TABLA + "( " + Inventario.N_TABLA_ID + "," + Inventario.CAMPO_ARTICULO
				+ "," + Inventario.CAMPO_TCSCBODEGAVIRTUALID + "," + Inventario.CAMPO_SERIE + ","
				+ Inventario.CAMPO_DESCRIPCION + "," + Inventario.CAMPO_CANTIDAD + ","
				+ Inventario.CAMPO_ESTADO_COMERCIAL + "," + Inventario.CAMPO_SERIE_ASOCIADA + ","
				+ Inventario.CAMPO_SERIADO + "," + Inventario.CAMPO_ESTADO + "," + Inventario.CAMPO_CREADO_EL + ","
				+ Inventario.CAMPO_CREADO_POR + "," + Inventario.CAMPO_TIPO_INV + "," + Inventario.CAMPO_TIPO_GRUPO
				+ "," + Inventario.CAMPO_TIPO_GRUPO_SIDRA + "," + Inventario.CAMPO_IDVENDEDOR + ","
				+ Inventario.CAMPO_TCSCVENTAID + "," + Inventario.CAMPO_TCSCCATPAISID + ") ";

		insert += "SELECT "
				+ UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, Inventario.SEQUENCE, Conf.INSERT_SEPARADOR_SI)
				+ "       ARTICULO, " + "       TCSCBODEGAVIRTUALID, " + "       SERIE, " + "       DESCRIPCION, "
				+ cant + ", " + "       ESTADO_COMERCIAL, " + "       SERIE_ASOCIADA, " + "       SERIADO, "
				+ "       '" + estadoVendido + "', " + "       sysdate, " + "       CREADO_POR, " + "       TIPO_INV, "
				+ "       TIPO_GRUPO, " + "       TIPO_GRUPO_SIDRA, " + "       IDVENDEDOR, " + idVenta + ", " + idPais
				+ "  FROM TC_SC_INVENTARIO " + " WHERE     SERIE IS NULL " + "       AND ARTICULO = " + articulo
				+ "       AND UPPER(TIPO_INV) = '" + tipoInv.toUpperCase() + "'" + "       AND TCSCBODEGAVIRTUALID = "
				+ idBodegaVirtual + "       AND UPPER(ESTADO) = '" + Disponible.toUpperCase() + "'";

		return insert;
	}

	public static void updateInvCantJornada(Connection conn, String jornada, String articulo, int cant, String usuario,
			String tipoInv, BigDecimal idPais) throws SQLException {
		PreparedStatement pstmt = null;

		String sql = "UPDATE tc_sC_cant_inv_jornada SET  cant_vendida=cant_vendida+?, MODIFICADO_EL=SYSDATE, MODIFICADO_POR=?"
				+ " WHERE idjornada_responsable=(SELECT NVL(JORNADA_RESPONSABLE, TCSCJORNADAVENID) FROM TC_Sc_JORNADA_VEND WHERE TCSCJORNADAVENID=?) AND ARTICULO=? AND TIPO_INV=? AND TCSCCATPAISID=?";
		log.debug("update inv cantidad JORNADA: " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cant);
			pstmt.setString(2, usuario);
			pstmt.setBigDecimal(3, new BigDecimal(jornada));
			pstmt.setBigDecimal(4, new BigDecimal(articulo));
			pstmt.setString(5, tipoInv);
			pstmt.setBigDecimal(6, idPais);
			pstmt.execute();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
	}

	/**
	 * M\u00E9todo para registrar las ventas.
	 * 
	 * @param conn
	 * @param objeto
	 * @param cantDecimalesBD
	 * @return
	 * @throws SQLException
	 */
	public static BigDecimal insertVenta(Connection conn, Venta objeto, int cantDecimalesBD, BigDecimal idPais)
			throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;
		String insert = "INSERT INTO TC_SC_VENTA (tcscventaid,tcscjornadavenid,fecha_emision,fecha_pago,serie,numero,vendedor,tipo_documento,idtipo,tipo,nit,nombre,segundo_nombre,apellido,segundo_apellido,direccion,num_telefono,tipo_doccliente,num_doccliente,nombre_fiscal,registrofiscal,giro,monto_pagado,descuentos,impuestos,monto_factura,estado,exento,creado_el,creado_por,serie_sidra,folio_sidra,envio_alarma,cod_dispositivo,nombres_factura,apellidos_factura,idrango_folio,idofertacampania,desc_montoventa,latitud,longitud,tasa_cambio,cod_oficina,cod_vendedor,id_venta_movil,tcsccatpaisid) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		log.debug("insert venta: " + insert);
		log.trace("fecha venta: " + objeto.getFecha_emision());
		try {
			pstmt = conn.prepareStatement(insert);
			ret = JavaUtils.getSecuencia(conn, Venta.SEQUENCE);
			pstmt.setBigDecimal(1, ret);
			pstmt.setBigDecimal(2, objeto.getTcscjornadavenid());
			pstmt.setTimestamp(3, objeto.getFecha_emision());
			pstmt.setTimestamp(4, objeto.getFecha_pago());
			pstmt.setString(5, objeto.getSerie());
			pstmt.setString(6, objeto.getNumero());
			pstmt.setBigDecimal(7, objeto.getVendedor());
			pstmt.setString(8, objeto.getTipo_documento());
			pstmt.setBigDecimal(9, objeto.getIdtipo());
			pstmt.setString(10, objeto.getTipo());
			pstmt.setString(11, objeto.getNit());
			pstmt.setString(12, objeto.getNombre());
			pstmt.setString(13, objeto.getSegundo_nombre());
			pstmt.setString(14, objeto.getApellido());
			pstmt.setString(15, objeto.getSegundo_apellido());
			pstmt.setString(16, objeto.getDireccion());
			pstmt.setString(17, objeto.getNum_telefono());
			pstmt.setString(18, objeto.getTipo_doccliente());
			pstmt.setString(19, objeto.getNum_doccliente());
			pstmt.setString(20, objeto.getNombre_fiscal());
			pstmt.setString(21, objeto.getRegistrofiscal());
			pstmt.setString(22, objeto.getGiro());
			pstmt.setBigDecimal(23, UtileriasJava.redondearBD(objeto.getMonto_pagado(), cantDecimalesBD));
			pstmt.setBigDecimal(24, UtileriasJava.redondearBD(objeto.getDescuentos(), cantDecimalesBD));
			pstmt.setBigDecimal(25, UtileriasJava.redondearBD(objeto.getImpuestos(), cantDecimalesBD));
			pstmt.setBigDecimal(26, UtileriasJava.redondearBD(objeto.getMonto_factura(), cantDecimalesBD));
			pstmt.setString(27, objeto.getEstado());
			pstmt.setString(28, objeto.getExento());
			pstmt.setString(29, objeto.getCreado_por());
			pstmt.setString(30, objeto.getSerie_sidra());
			if (objeto.getFolio_sidra() == null) {
				pstmt.setBigDecimal(31, null);
			} else {
				pstmt.setBigDecimal(31, objeto.getFolio_sidra());
			}
			pstmt.setBigDecimal(32, BigDecimal.ZERO);
			pstmt.setString(33, objeto.getCod_dispositivo());
			pstmt.setString(34, objeto.getNombres_factura());
			pstmt.setString(35, objeto.getApellidos_factura());
			pstmt.setBigDecimal(36, objeto.getIdrango_folio());
			pstmt.setBigDecimal(37, objeto.getIdofertacampania());
			pstmt.setBigDecimal(38, UtileriasJava.redondearBD(objeto.getDesc_montoventa(), cantDecimalesBD));
			pstmt.setString(39, objeto.getLatitud());
			pstmt.setString(40, objeto.getLongitud());
			pstmt.setBigDecimal(41, objeto.getTasa_cambio());
			pstmt.setString(42, objeto.getCod_oficina());
			pstmt.setString(43, objeto.getCod_vendedor());
			pstmt.setBigDecimal(44, objeto.getId_venta_movil());
			pstmt.setBigDecimal(45, idPais);
			int res = pstmt.executeUpdate();
			if (res != 1) {
				ret = new BigDecimal(0);
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}

	/**
	 * Insert para detalle de pago de venta
	 * 
	 * @param conn
	 * @param listaObj
	 * @param idVenta
	 * @param cantDecimalesBD
	 * @throws SQLException
	 */
	public static void insertaDetPago(Connection conn, List<PagoDet> listaObj, BigDecimal idVenta, int cantDecimalesBD)
			throws SQLException {
		String valores = "";
		PreparedStatement pstmt = null;
		String insert = "";
		List<String> listaInserts = new ArrayList<String>();
		SimpleDateFormat FORMATO_FECHA_GT = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA_GT);
		log.trace("1");
		log.trace("id venta:" + idVenta);

		String campos[] = { PagoDet.CAMPO_TCSCDETPAGOID, PagoDet.CAMPO_TCSCVENTAID, PagoDet.CAMPO_FORMAPAGO,
				PagoDet.CAMPO_MONTO, PagoDet.CAMPO_NUM_REFERENCIA, PagoDet.CAMPO_NUM_AUTORIZACION, PagoDet.CAMPO_BANCO,
				PagoDet.CAMPO_MARCA_TARJETA, PagoDet.CAMPO_DIGITOS_TARJETA, PagoDet.CAMPO_NUM_CHEQUE,
				PagoDet.CAMPO_FECHA_EMISION, PagoDet.CAMPO_NUM_RESERVA, PagoDet.CAMPO_NO_CUENTA,
				PagoDet.CAMPO_CREADO_POR, PagoDet.CAMPO_CREADO_EL };
		log.trace("agrego datos a insert");
		// ARMANDO INSERTS
		if (!listaObj.isEmpty()) {
			for (PagoDet obj : listaObj) {
				valores = ""
						+ UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, PagoDet.SEQUENCE, Conf.INSERT_SEPARADOR_SI)
						+ idVenta + ", " + "'" + obj.getFormapago() + "', "
						+ UtileriasJava.redondearBD(obj.getMonto(), cantDecimalesBD) + ", "
						+ (obj.getNum_referencia() == null ? "NULL" : obj.getNum_referencia()) + ", "
						+ (obj.getNum_autorizacion() == null ? "NULL" : obj.getNum_autorizacion()) + ", "
						+ (obj.getBanco() == null ? "NULL" : "'" + obj.getBanco() + "'") + ", "
						+ (obj.getMarca_tarjeta() == null ? "NULL" : "'" + obj.getMarca_tarjeta() + "'") + ", "
						+ (obj.getDigitos_tarjeta() == null ? "NULL" : "'" + obj.getDigitos_tarjeta() + "'") + ", "
						+ (obj.getNum_cheque() == null ? "NULL" : "'" + obj.getNum_cheque() + "'") + ", "
						+ (obj.getFecha_emision() == null ? "NULL"
								: "TO_DATE('" + FORMATO_FECHA_GT.format(obj.getFecha_emision()) + "', '"
										+ Conf.TXT_FORMATO_FECHA_CORTA_GT + "')")
						+ ", " + (obj.getNum_reserva() == null ? "NULL" : obj.getNum_reserva()) + ", "
						+ (obj.getNo_cuenta() == null ? "NULL" : "'" + obj.getNo_cuenta() + "'") + ", " + "'"
						+ obj.getCreado_por() + "', " + "sysdate ";

				listaInserts.add(valores);
			}

			// armando insert
			log.trace("aqui hago el insert");
			insert = UtileriasBD.armarQueryInsertAll(PagoDet.N_TABLA, campos, listaInserts);
			log.trace("insert forma pago:" + insert);
			try {
				pstmt = conn.prepareStatement(insert);
				pstmt.executeUpdate();
			} finally {
				DbUtils.closeQuietly(pstmt);

			}

		}
	}

	/**
	 * Insert para detalle de pago de impuesto
	 * 
	 * @param conn
	 * @param listaObj
	 * @param idVenta
	 * @param cantDecimalesBD
	 * @param esDetalle
	 * @param articulo
	 * @param serie
	 * @param articuloRecarga
	 * @param numRecarga
	 * @throws SQLException
	 */
	public static void insertaDetImpuestoGlobal(Connection conn, List<PagoImpuesto> listaObj, BigDecimal idVenta,
			int cantDecimalesBD) throws SQLException {
		String valores = "";
		PreparedStatement pstmt = null;
		String insert = "";

		List<String> listaInserts = new ArrayList<String>();
		log.trace("armando de insert pagos");
		String campos[] = { PagoImpuesto.CAMPO_TCSCPAGOIMPUESTOID, PagoImpuesto.CAMPO_TCSCVENTAID,
				PagoImpuesto.CAMPO_TCSCVENTADETID, PagoImpuesto.CAMPO_IMPUESTO, PagoImpuesto.CAMPO_VALOR,
				PagoImpuesto.CAMPO_CREADO_POR, PagoImpuesto.CAMPO_CREADO_EL };

		// ARMANDO INSERTS
		if (!listaObj.isEmpty()) {
			for (PagoImpuesto obj : listaObj) {
				valores = UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, PagoImpuesto.SEQUENCE,
						Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idVenta.toString(), Conf.INSERT_SEPARADOR_SI)
						+ "NULL, "
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, obj.getImpuesto(), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_NUMERO,
								UtileriasJava.redondearBD(obj.getValor(), cantDecimalesBD).toString(),
								Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, obj.getCreado_por(), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_NO);

				listaInserts.add(valores);
			}

			// armando insert
			insert = UtileriasBD.armarQueryInsertAll(PagoImpuesto.N_TABLA, campos, listaInserts);
			try {
				pstmt = conn.prepareStatement(insert);
				pstmt.executeUpdate();
			} finally {
				DbUtils.closeQuietly(pstmt);
			}
			log.trace("termino de insertar pagos");
		}

	}

	public static List<String> insertaDetImpuesto(List<PagoImpuesto> listaObj, BigDecimal idVenta,
			BigDecimal idVentaDet, int cantDecimalesBD) throws SQLException {
		String valores = "";
		String insert = "";
		List<String> listaInserts = new ArrayList<String>();

		log.trace("armando impuestos");
		String campos[] = { PagoImpuesto.CAMPO_TCSCPAGOIMPUESTOID, PagoImpuesto.CAMPO_TCSCVENTAID,
				PagoImpuesto.CAMPO_TCSCVENTADETID, PagoImpuesto.CAMPO_IMPUESTO, PagoImpuesto.CAMPO_VALOR,
				PagoImpuesto.CAMPO_CREADO_POR, PagoImpuesto.CAMPO_CREADO_EL };

		for (PagoImpuesto obj : listaObj) {
			valores = "("
					+ UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, PagoImpuesto.SEQUENCE, Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idVenta.toString(), Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idVentaDet.toString(), Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, obj.getImpuesto(), Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_NUMERO,
							UtileriasJava.redondearBD(obj.getValor(), cantDecimalesBD).toString(),
							Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, obj.getCreado_por(), Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_NO) + ")";

			insert = UtileriasBD.armarQryInsert(PagoImpuesto.N_TABLA, campos, valores);
			listaInserts.add(insert);
		}

		return listaInserts;
	}

	private static List<String> insertaDetDescuento(Connection conn, List<DescuentosSidra> descuentosArticulo,
			BigDecimal idVenta, BigDecimal idVentaDet, int cantDecimalesBD, String articulo, String tipoGestion,
			BigDecimal idPais) throws SQLException {
		String valores = "";
		String insert = "";
		String sql = "";
		MathContext mc = new MathContext(200, RoundingMode.HALF_UP);
		List<String> listaInserts = new ArrayList<String>();

		String campos[] = { DescuentosSidra.CAMPO_TCSCDESCUENTOSIDRAID, DescuentosSidra.CAMPO_TCSCVENTAID,
				DescuentosSidra.CAMPO_TCSCVENTADETID, DescuentosSidra.CAMPO_TCSCOFERTACAMPANIAID,
				DescuentosSidra.CAMPO_VALOR, DescuentosSidra.CAMPO_CREADO_POR, DescuentosSidra.CAMPO_CREADO_EL,
				DescuentosSidra.CAMPO_ID_DESCUENTO };

		String sqlIdCondicion = "";
		for (DescuentosSidra obj : descuentosArticulo) {
			// validamos si existe id_descuento
			// creado por jcsimon 16/10/2017
			// modificado por pablo.lopez 12/01/2018
			sqlIdCondicion = "SELECT TCSCCONDICIONID " + "FROM TC_SC_CONDICION " + "WHERE TCSCOFERTACAMPANIAID = "
					+ obj.getTcscofertacampaniaid() + " and TCSCCONDICIONID = " + obj.getIdCondicion();
			if (tipoGestion != null && !"".equals(tipoGestion)) {
				sqlIdCondicion += " AND TIPO_GESTION = '" + tipoGestion + "'";
			}
			sql = "SELECT ID_DESCUENTO FROM TC_SC_DET_CONDICION_OFERTA WHERE TCSCCONDICIONID IN (" + sqlIdCondicion
					+ ") " + "AND ARTICULO = " + articulo + " " + "AND TIPO_OFERTA = '" + obj.getTipoDescuento() + "' "
					+ "AND TCSCCATPAISID = " + idPais.toString() + " " + "AND ROWNUM = 1";
			// fin validacon id descuento

			String id_descuento = UtileriasBD.executeQueryOneRecord(conn, sql);
			if (id_descuento == null || "".equals(id_descuento)) {
				id_descuento = "null";
			}

			valores = "("
					+ UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, DescuentosSidra.SEQUENCE, Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idVenta.toString(), Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idVentaDet.toString(), Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, obj.getTcscofertacampaniaid().toString(),
							Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_NUMERO,
							UtileriasJava.redondearBD(obj.getValor().divide(obj.getCantidad(), mc), cantDecimalesBD)
									.toString(),
							Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, obj.getCreado_por(), Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
					+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, id_descuento, Conf.INSERT_SEPARADOR_NO) + ")";

			insert = UtileriasBD.armarQryInsert(DescuentosSidra.N_TABLA, campos, valores);
			listaInserts.add(insert);
		}

		return listaInserts;
	}

	/**
	 * Para registrar el detalle de ventas
	 * 
	 * @param conn
	 * @param listaObj
	 * @param idVenta
	 * @param cantDecimalesBD
	 * @param articuloRecarga
	 * @throws SQLException
	 */
	public static void insertaDetVenta(Connection conn, List<VentaDet> listaObj, BigDecimal idVenta,
			int cantDecimalesBD, String codArea, BigDecimal idPais) throws SQLException {
		String valores = "";
		PreparedStatement pstmt = null;
		Statement stmt = null;
		String insert = "";
		String serieCompleta = "";
		String sql = "";
		List<String> listaInserts = new ArrayList<String>();
		BigDecimal idVentaDet = null;
		String tipoProducto = "";
		String estadoAlta = "";

		try {
			stmt = conn.createStatement();
			estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, codArea);

			String campos[] = { VentaDet.CAMPO_TCSCVENTADETID, VentaDet.CAMPO_TCSCVENTAID,
					VentaDet.CAMPO_TCSCBODEGAVIRTUALID, VentaDet.CAMPO_BODEGA_PANEL_RUTA, VentaDet.CAMPO_ARTICULO,
					VentaDet.CAMPO_TIPO_INV, VentaDet.CAMPO_TIPO_GRUPO_SIDRA, VentaDet.CAMPO_SERIE,
					VentaDet.CAMPO_SERIE_ASOCIADA, VentaDet.CAMPO_NUM_TELEFONO, VentaDet.CAMPO_CANTIDAD,
					VentaDet.CAMPO_PRECIO_UNITARIO, VentaDet.CAMPO_PRECIO_TOTAL, VentaDet.CAMPO_PRECIO_FINAL,
					VentaDet.CAMPO_IMPUESTO, VentaDet.CAMPO_DESCUENTO_SCL, VentaDet.CAMPO_DESCUENTO_SIDRA,
					VentaDet.CAMPO_TCSCOFERTACAMPANIAID, VentaDet.CAMPO_GESTION, VentaDet.CAMPO_MODALIDAD,
					VentaDet.CAMPO_TECNOLOGIA, VentaDet.CAMPO_OBSERVACIONES, VentaDet.CAMPO_ESTADO,
					VentaDet.CAMPO_CREADO_POR, VentaDet.CAMPO_ID_PRODUCT_OFFERING, VentaDet.CAMPO_CREADO_EL };
			String idProductOffering = "";
			// ARMANDO INSERTS
			if (!listaObj.isEmpty()) {
				for (VentaDet obj : listaObj) {
					log.trace("tipo_grupo_sidra:" + obj.getTipo_grupo_sidra());
					if (!"PROMOCIONALES".equalsIgnoreCase(obj.getTipo_grupo_sidra())) {
						//// ------Obtener campo id_product_offering jsimon
						List<Filtro> condicionesOf = new ArrayList<Filtro>();
						condicionesOf.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
								PrecioArticulo.CAMPO_TCSCCATPAISID, idPais.toString()));
						condicionesOf.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
								PrecioArticulo.CAMPO_ESTADO, estadoAlta));
						condicionesOf.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
								PrecioArticulo.CAMPO_ARTICULO, obj.getArticulo().toString()));
						condicionesOf.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
								PrecioArticulo.CAMPO_TIPO_GESTION, obj.getGestion()));
						idProductOffering = UtileriasBD.getOneRecord(conn, PrecioArticulo.CAMPO_ID_PRODUCT_OFFERING,
								PrecioArticulo.N_TABLA, condicionesOf);

					}
					if ("PROMOCIONALES".equalsIgnoreCase(obj.getTipo_grupo_sidra())) {
						//// ------Obtener campo id_product_offering jsimon
						log.trace("busca idproductoffering en tabla promocionales por ser art promocional");
						List<Filtro> condicionesOf = new ArrayList<Filtro>();
						condicionesOf = new ArrayList<Filtro>();
						condicionesOf.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
								Promocionales.CAMPO_TCSCCATPAISID, idPais.toString()));
						condicionesOf.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
								Promocionales.CAMPO_ESTADO, estadoAlta));
						condicionesOf.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
								Promocionales.CAMPO_TCSCARTPROMOCIONALID, obj.getArticulo().toString()));
						idProductOffering = UtileriasBD.getOneRecord(conn, Promocionales.CAMPO_ID_PRODUCT_OFFERING,
								Promocionales.N_TABLA, condicionesOf);
					}
					/// -----fin del cambio

					if (idProductOffering != null && !idProductOffering.equals("")) {// reutilizo el objeto en la
																						// respuesta para devolver el
																						// idVenta
						obj.setId_product_offering(idProductOffering);
					}
					log.trace(
							"------------------------------------------------------------------------------------------------------");
					idVentaDet = JavaUtils.getSecuencia(conn, VentaDet.SEQUENCE);
					log.trace("venta: " + idVenta + ", ventaDet: " + idVentaDet + ", bodega: "
							+ obj.getTcscbodegavirtualid() + ", bodegaPanelRuta: " + obj.getBodega_panel_ruta()
							+ ", artículo: " + obj.getArticulo());

					// *VALIDAMOS SI EL PRODUCTO ES SIMCAR creado por jcsimon
					List<Filtro> condicion = new ArrayList<Filtro>();

					condicion.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO,
							Conf.GRUPO_SIMCARD));
					condicion.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE,
							obj.getTipo_grupo_sidra()));
					condicion.add(
							UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
					condicion.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
							Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
					tipoProducto = UtileriasBD.getOneRecord(conn, Catalogo.CAMPO_VALOR, Catalogo.N_TABLA, condicion);

					// buscar la serie completa si la venta es simcard cambio realizado por jcsimon
					// 28/09/2017
					if (tipoProducto.equalsIgnoreCase(Conf.TIPO_GRUPO_SIMCAR)) {
						serieCompleta = "";
						sql = "";
						sql = "SELECT SERIE_COMPLETA FROM TC_SC_INVENTARIO WHERE TCSCCATPAISID=" + idPais.toString()
								+ " AND  TCSCBODEGAVIRTUALID= " + obj.getTcscbodegavirtualid() + " AND ARTICULO="
								+ obj.getArticulo() + " AND SUBSTR(SERIE,1,18)= '" + obj.getSerie().toString() + "'";
						serieCompleta = UtileriasBD.executeQueryOneRecord(conn, sql);

					} else if (tipoProducto == null || ("").equals(tipoProducto)) {
						serieCompleta = (obj.getSerie() == null ? "NULL" : obj.getSerie());

					}
					// FIN DEL CAMBIO

					valores = "(" + idVentaDet + ", " + idVenta + ", " + obj.getTcscbodegavirtualid() + ", "
							+ obj.getBodega_panel_ruta() + ", " + obj.getArticulo() + ", " + "'" + obj.getTipo_inv()
							+ "', " + "'" + obj.getTipo_grupo_sidra().toUpperCase() + "', "
							+ (obj.getSerie() == null ? "NULL," : "'" + serieCompleta + "', ")
							+ (obj.getSerie_asociada() == null || obj.getSerie_asociada().equals("") ? "NULL,"
									: "'" + obj.getSerie_asociada() + "', ")
							+ (obj.getNum_telefono() == null ? "NULL," : obj.getNum_telefono() + ", ")
							+ obj.getCantidad() + ", "
							+ (obj.getPrecio_unitario() == null ? "NULL,"
									: UtileriasJava.redondearBD(obj.getPrecio_unitario(), cantDecimalesBD) + ", ")
							+ (obj.getPrecio_total() == null ? "NULL,"
									: UtileriasJava.redondearBD(obj.getPrecio_total(), cantDecimalesBD) + ", ")
							+ (obj.getPrecio_final() == null ? "NULL,"
									: UtileriasJava.redondearBD(obj.getPrecio_final(), cantDecimalesBD) + ", ")
							+ (obj.getImpuesto() == null ? "NULL,"
									: UtileriasJava.redondearBD(obj.getImpuesto(), cantDecimalesBD) + ", ")
							+ (obj.getDescuento_scl() == null ? "NULL,"
									: UtileriasJava.redondearBD(obj.getDescuento_scl(), cantDecimalesBD) + ", ")
							+ (obj.getDescuento_sidra() == null ? "NULL,"
									: UtileriasJava.redondearBD(obj.getDescuento_sidra(), cantDecimalesBD) + ", ")
							+ (obj.getTcscofertacampaniaid() == null ? "NULL," : obj.getTcscofertacampaniaid() + ", ")
							+ (obj.getGestion() == null ? "NULL," : "'" + obj.getGestion() + "', ")
							+ (obj.getModalidad() == null ? "NULL," : "'" + obj.getModalidad() + "', ")
							+ (obj.getTecnologia() == null ? "NULL," : "'" + obj.getTecnologia() + "', ")
							+ (obj.getObservaciones() == null ? "NULL," : "'" + obj.getObservaciones() + "', ") + "'"
							+ obj.getEstado() + "', " + "'" + obj.getCreado_por() + "', "
							+ (obj.getId_product_offering() == null ? "NULL," : obj.getId_product_offering() + ",")
							+ "SYSDATE)";

					insert = UtileriasBD.armarQryInsert(VentaDet.N_TABLA, campos, valores);
					log.debug("insert detalle venta:" + insert);
					stmt.addBatch(insert);

					if (obj.getImpuestoArticulo() != null && !obj.getImpuestoArticulo().isEmpty()) {
						log.debug("inserts detalle pago impuestos");
						listaInserts = insertaDetImpuesto(obj.getImpuestoArticulo(), idVenta, idVentaDet,
								cantDecimalesBD);
						for (int i = 0; i < listaInserts.size(); i++) {
							log.debug(listaInserts.get(i));
							stmt.addBatch(listaInserts.get(i));
							log.trace("inserto con exito");
						}
					}

					log.trace("salio de insert");
					if ((obj.getDescuentosArticulo() != null && !obj.getDescuentosArticulo().isEmpty())
							&& obj.getDescuentosArticulo().get(0) != null) {
						log.debug("inserts detalle descuentos");
						listaInserts.clear();
						listaInserts = insertaDetDescuento(conn, obj.getDescuentosArticulo(), idVenta, idVentaDet,
								cantDecimalesBD, obj.getArticulo().toString(), obj.getGestion(), idPais);
						for (int i = 0; i < listaInserts.size(); i++) {
							log.debug(listaInserts.get(i));
							stmt.addBatch(listaInserts.get(i));
						}
					}

					log.trace("termino de insertar todo el detalle");

				}

				int[] insertsCounts = stmt.executeBatch();

				if (!UtileriasJava.validarBatch(1, insertsCounts)) {
					throw new SQLException("Ocurri\u00F3 un inconveniente al insertar el detalle de ventas.");
				}
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(stmt);
		}
	}

	/**
	 * M\u00E9todo que sirve para actualizar la informaci\u00F3n de los folios
	 * utilizados en las ventas.
	 * 
	 * @param conn
	 * @param idRangoFolio
	 * @param folio
	 * @param estadoFinalizado
	 * @param estadoEnUso
	 * @param usuario
	 * @throws SQLException
	 */
	public static void updateFolios(Connection conn, String idRangoFolio, Integer folio, String estadoFinalizado,
			String estadoEnUso, String usuario, BigDecimal idPais) throws SQLException {
		PreparedStatement pstmt = null;

		String sql = UpdateFolioq(idRangoFolio, folio, estadoFinalizado, estadoEnUso, usuario, idPais);

		log.debug("updateFolios: " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.execute();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
	}

	public static String UpdateFolioq(String idRangoFolio, Integer folio, String estadoFinalizado, String estadoEnUso,
			String usuario, BigDecimal idPais) {

		Country pais = Country.getCountryById(idPais.longValue());

		String sql = "UPDATE " + ConfiguracionFolioVirtual.N_TABLA + " SET "
				+ ConfiguracionFolioVirtual.CAMPO_ULTIMO_UTILIZADO + " = " + folio + ", "
				+ ConfiguracionFolioVirtual.CAMPO_FOLIO_SIGUIENTE + " = " + "DECODE ("
				+ ConfiguracionFolioVirtual.CAMPO_FOLIO_SIGUIENTE + ",NULL, NULL, " + "         DECODE (" + folio + ", "
				+ ConfiguracionFolioVirtual.CAMPO_NO_FINAL_FOLIO + ", NULL, "
				+ "         DECODE ( (SELECT MAX (TO_NUMBER (NUMERO)) "
				+ "                               FROM TC_SC_VENTA PARTITION (" + pais.getA2Code() + ") "
				+ "                              WHERE IDRANGO_FOLIO =  " + idRangoFolio + "), "
				+ ConfiguracionFolioVirtual.CAMPO_NO_FINAL_FOLIO + ", NULL, "
				+ "                               (SELECT MAX (TO_NUMBER (NUMERO)) "
				+ "                               FROM TC_SC_VENTA PARTITION (" + pais.getA2Code() + ") "
				+ "                              WHERE IDRANGO_FOLIO =  " + idRangoFolio + ") "
				+ "                          + 1))) ," + ConfiguracionFolioVirtual.CAMPO_CANT_UTILIZADOS + " = "
				+ ConfiguracionFolioVirtual.CAMPO_CANT_UTILIZADOS + " + 1, "
				+ ConfiguracionFolioVirtual.CAMPO_MODIFICADO_POR + " = '" + usuario + "', "
				+ ConfiguracionFolioVirtual.CAMPO_ESTADO + " = UPPER(DECODE("
				+ ConfiguracionFolioVirtual.CAMPO_CANT_UTILIZADOS + " + 1, ("
				+ ConfiguracionFolioVirtual.CAMPO_NO_FINAL_FOLIO + "-"
				+ ConfiguracionFolioVirtual.CAMPO_NO_INICIAL_FOLIO + ")+1, " + "'" + estadoFinalizado + "', '"
				+ estadoEnUso + "')), " + ConfiguracionFolioVirtual.CAMPO_MODIFICADO_EL + " = SYSDATE " + "WHERE "
				+ ConfiguracionFolioVirtual.CAMPO_TC_SC_FOLIO_ID + " = " + idRangoFolio + " AND "
				+ ConfiguracionFolioVirtual.CAMPO_TC_SC_CATPAISID + " = " + idPais;
		return sql;
	}

	/* Funciones para servicios de consulta de ventas. */
	public static OutputVenta doGet(Connection conn, InputGetVenta inputVenta, BigDecimal idPais)
			throws SQLException, ExcepcionSeguridad, NamingException {
		String nombreMetodo = "doGet";
		String nombreClase = new CurrentClassGetter().getClassName();
		InputVendedorDTS vendedor = null;
		InputBodegaVirtual bodegaVendedor = null;
		InputPDV puntoVenta = new InputPDV();
		List<InputVenta> list = new ArrayList<InputVenta>();

		Respuesta respuesta = null;
		OutputVenta output = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String tipoCF = UtileriasJava.getConfig(conn, Conf.GRUPO_TIPO_CLIENTE_VENTA, Conf.CLIENTEFINAL,
				inputVenta.getCodArea());

		String sql = queryGet(inputVenta);
		try {
			log.debug("QUERY ARMADO: " + sql);
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst != null) {
				if (!rst.next()) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_VENTAS_826, null,
							nombreClase, nombreMetodo, null, inputVenta.getCodArea());
					output = new OutputVenta();
					output.setRespuesta(respuesta);
				} else {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_GETDATOS12, null, nombreClase,
							nombreMetodo, null, inputVenta.getCodArea());

					String idVendedor = "";
					String idTipo = "";
					String tipo = "";
					String idJornada = "";

					do {
						InputVenta item = new InputVenta();
						idVendedor = rst.getString(Venta.CAMPO_VENDEDOR);
						idTipo = rst.getString(Venta.CAMPO_IDTIPO);
						tipo = rst.getString(Venta.CAMPO_TIPO);
						idJornada = rst.getString(Venta.CAMPO_TCSCJORNADAVENID);
						String zonaComercial = "";

						if (!tipo.equals(tipoCF)) {
							puntoVenta = datosPuntoVenta(idTipo, conn, inputVenta.getIdDTS(), inputVenta.getCodArea(),
									idPais);
						} else {
							puntoVenta = new InputPDV();
							zonaComercial = getZonaComercial(conn, idJornada, idPais);
						}

						item.setIdVenta(rst.getString(Venta.CAMPO_TCSCVENTAID));
						item.setIdJornada(idJornada);
						item.setFecha(UtileriasJava.formatStringDate(rst.getString(Venta.CAMPO_FECHA_EMISION)));
						item.setFolio(UtileriasJava.getValue(rst.getString(Venta.CAMPO_NUMERO)));
						item.setSerie(UtileriasJava.getValue(rst.getString(Venta.CAMPO_SERIE)));
						item.setFolioSidra(UtileriasJava.getValue(rst.getString(Venta.CAMPO_FOLIO_SIDRA)));
						item.setSerieSidra(UtileriasJava.getValue(rst.getString(Venta.CAMPO_SERIE_SIDRA)));
						item.setTipoDocumento(rst.getString(Venta.CAMPO_TIPO_DOCUMENTO));

						/* Datos ficha ciente */
						item.setNombre(UtileriasJava.getValue(rst.getString(Venta.CAMPO_NOMBRE)));
						item.setSegundoNombre(UtileriasJava.getValue(rst.getString(Venta.CAMPO_SEGUNDO_NOMBRE)));
						item.setApellido(UtileriasJava.getValue(rst.getString(Venta.CAMPO_APELLIDO)));
						item.setSegundoApellido(UtileriasJava.getValue(rst.getString(Venta.CAMPO_SEGUNDO_APELLIDO)));
						item.setDireccion(UtileriasJava.getValue(rst.getString(Venta.CAMPO_DIRECCION)));
						item.setNumTelefono(UtileriasJava.getValue(rst.getString(Venta.CAMPO_NUM_TELEFONO)));
						item.setTipoDocCliente(UtileriasJava.getValue(rst.getString(Venta.CAMPO_TIPO_DOCCLIENTE)));
						item.setNumDocCliente(UtileriasJava.getValue(rst.getString(Venta.CAMPO_NUM_DOCCLIENTE)));

						item.setNombresFacturacion(UtileriasJava.getValue(rst.getString(Venta.CAMPO_NOMBRES_FACTURA)));
						item.setApellidosFacturacion(
								UtileriasJava.getValue(rst.getString(Venta.CAMPO_APELLIDOS_FACTURA)));

						item.setTasaCambio(UtileriasJava.getValue(rst.getString(Venta.CAMPO_TASA_CAMBIO)));
						item.setIdOfertaCampania(rst.getString(Venta.CAMPO_IDOFERTACAMPANIA));
						item.setDescuentoMontoVenta(UtileriasJava
								.convertirMoneda(rst.getString(Venta.CAMPO_DESC_MONTOVENTA), item.getTasaCambio()));
						item.setDescuentoTotal(UtileriasJava.convertirMoneda(rst.getString(Venta.CAMPO_DESCUENTOS),
								item.getTasaCambio()));
						item.setEstado(rst.getString(Venta.CAMPO_ESTADO));
						item.setObservaciones(UtileriasJava.getValue(rst.getString(Venta.CAMPO_OBSERVACIONES)));
						item.setMontoFactura(UtileriasJava.convertirMoneda(rst.getString(Venta.CAMPO_MONTO_FACTURA),
								item.getTasaCambio()));
						item.setMontoPagado(UtileriasJava.convertirMoneda(rst.getString(Venta.CAMPO_MONTO_PAGADO),
								item.getTasaCambio()));
						item.setExento(rst.getString(Venta.CAMPO_EXENTO));
						item.setImpuesto(UtileriasJava.convertirMoneda(rst.getString(Venta.CAMPO_IMPUESTOS),
								item.getTasaCambio()));
						item.setEnvioAlarma(UtileriasJava.getValue(rst.getString(Venta.CAMPO_ENVIO_ALARMA)));
						item.setLatitud(UtileriasJava.getValue(rst.getString(Venta.CAMPO_LATITUD)));
						item.setLongitud(UtileriasJava.getValue(rst.getString(Venta.CAMPO_LONGITUD)));
						item.setZonaComercial(UtileriasJava.getValue(zonaComercial));

						item.setCreadoPor(rst.getString(Venta.CAMPO_CREADO_POR));
						item.setCreadoEl(UtileriasJava.formatStringDate(rst.getString(Venta.CAMPO_CREADO_EL)));
						item.setModificadoPor(UtileriasJava.getValue(rst.getString(Venta.CAMPO_MODIFICADO_POR)));
						item.setModificadoEl(
								UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Venta.CAMPO_MODIFICADO_EL));

						/* Datos para vendedor */
						item.setIdVendedor(idVendedor);
						vendedor = datosVendedor(idVendedor, conn, idPais);
						if (vendedor != null) {
							item.setNombreVendedor(vendedor.getNombres());
							item.setIdBodegaVendedor(vendedor.getIdBodegaVirtual());

							/* Datos bodega vendedor */
							bodegaVendedor = datosBodegaVirtual(vendedor.getIdBodegaVirtual(), conn,
									inputVenta.getCodArea(), idPais);
							item.setBodegaVendedor(bodegaVendedor.getNombre());
						}
						/* Datos PDV y fiscales */
						item.setTipo(tipo);
						item.setIdTipo(idTipo);
						item.setNombrePDV(UtileriasJava.getValue(puntoVenta.getNombrePDV()));
						item.setNit(rst.getString(Venta.CAMPO_NIT));
						item.setRegistroFiscal(UtileriasJava.getValue(puntoVenta.getRegistroFiscal()));
						item.setGiro(UtileriasJava.getValue(puntoVenta.getGiroNegocio()));
						item.setNombreFiscal(UtileriasJava.getValue(puntoVenta.getNombreFiscal()));
						item.setDepartamento(UtileriasJava.getValue(puntoVenta.getDepartamento()));
						item.setMunicipio(UtileriasJava.getValue(puntoVenta.getMunicipio()));

						/* Detalle Pago */
						item.setDetallePago(detallePago(item.getIdVenta(), conn, item.getTasaCambio()));

						item.setNombrePanelRuta(getNombrePanelRuta(conn, idJornada, inputVenta.getCodArea(), idPais));

						list.add(item);
					} while (rst.next());

					output = new OutputVenta();
					output.setVenta(list);
					output.setRespuesta(respuesta);
				}
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return output;
	}

	public static String queryGet(InputGetVenta inputVenta) {
		List<Order> orden = new ArrayList<Order>();
		orden.add(new Order(Venta.CAMPO_FECHA_EMISION, Order.ASC));

		String sql = "SELECT V.*, D." + Distribuidor.CAMPO_NOMBRES + " FROM "
				+ ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", inputVenta.getCodArea()) + " V, "
				+ ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", inputVenta.getCodArea()) + " J, "
				+ Distribuidor.N_TABLA + " D " + "WHERE V." + Venta.CAMPO_TCSCJORNADAVENID + " = J."
				+ Jornada.CAMPO_TCSCJORNADAVENID + " AND J." + Jornada.CAMPO_TCSCDTSID + " = D."
				+ Distribuidor.CAMPO_TC_SC_DTS_ID + " AND J." + Jornada.CAMPO_TCSCCATPAISID + " = D."
				+ Distribuidor.CAMPO_TCSCCATPAISID + " AND J." + Jornada.CAMPO_TCSCCATPAISID + " = V."
				+ Venta.CAMPO_TCSCCATPAISID + " AND D." + Distribuidor.CAMPO_TC_SC_DTS_ID + " = "
				+ inputVenta.getIdDTS();

		if (!(inputVenta.getIdVenta() == null || "".equals(inputVenta.getIdVenta().trim()))) {
			sql += " AND V." + Venta.CAMPO_TCSCVENTAID + " = " + inputVenta.getIdVenta();
		}

		if (!(inputVenta.getIdJornada() == null || "".equals(inputVenta.getIdJornada().trim()))) {
			sql += " AND V." + Venta.CAMPO_TCSCJORNADAVENID + " = " + inputVenta.getIdJornada();
		}

		if (!(inputVenta.getIdVendedor() == null || "".equals(inputVenta.getIdVendedor().trim()))) {
			sql += " AND V." + Venta.CAMPO_VENDEDOR + " = " + inputVenta.getIdVendedor();
		}

		if (!(inputVenta.getIdRutaPanel() == null || "".equals(inputVenta.getIdRutaPanel().trim()))) {
			sql += " AND J." + Jornada.CAMPO_IDTIPO + " = " + inputVenta.getIdRutaPanel();
			sql += " AND UPPER(J." + Jornada.CAMPO_DESCRIPCION_TIPO + ") = UPPER('" + inputVenta.getTipoRutaPanel()
					+ "')";
		}

		if (!(inputVenta.getIdTipo() == null || "".equals(inputVenta.getIdTipo().trim()))) {
			sql += " AND V." + Venta.CAMPO_IDTIPO + " = " + inputVenta.getIdTipo();
		}

		if (!(inputVenta.getTipoCliente() == null || "".equals(inputVenta.getTipoCliente().trim()))) {
			sql += " AND UPPER(V." + Venta.CAMPO_TIPO + ") = UPPER('" + inputVenta.getTipoCliente() + "')";
		}

		if (!(inputVenta.getTipoDocumento() == null || "".equals(inputVenta.getTipoDocumento().trim()))) {
			sql += " AND UPPER(V." + Venta.CAMPO_TIPO_DOCUMENTO + ") = UPPER('" + inputVenta.getTipoDocumento() + "')";
		}

		if (!(inputVenta.getSerie() == null || "".equals(inputVenta.getSerie().trim()))) {
			sql += " AND UPPER(V." + Venta.CAMPO_SERIE + ") = UPPER('" + inputVenta.getSerie() + "')";
		}

		if (!(inputVenta.getSerieSidra() == null || "".equals(inputVenta.getSerieSidra().trim()))) {
			sql += " AND UPPER(V." + Venta.CAMPO_SERIE_SIDRA + ") = UPPER('" + inputVenta.getSerieSidra() + "')";
		}

		if (!(inputVenta.getFolio() == null || "".equals(inputVenta.getFolio().trim()))) {
			sql += " AND V." + Venta.CAMPO_NUMERO + " = " + inputVenta.getFolio();
		}

		if (!(inputVenta.getFolioSidra() == null || "".equals(inputVenta.getFolioSidra().trim()))) {
			sql += " AND V." + Venta.CAMPO_FOLIO_SIDRA + " = " + inputVenta.getFolioSidra();
		}

		if (!(inputVenta.getNit() == null || "".equals(inputVenta.getNit().trim()))) {
			sql += " AND UPPER(V." + Venta.CAMPO_NIT + ") = UPPER('" + inputVenta.getNit() + "')";
		}

		if (!(inputVenta.getPrimerNombreCliente() == null || "".equals(inputVenta.getPrimerNombreCliente().trim()))) {
			sql += " AND UPPER(V." + Venta.CAMPO_NOMBRE + ") = UPPER('" + inputVenta.getPrimerNombreCliente() + "')";
		}

		if (!(inputVenta.getPrimerApellidoCliente() == null
				|| "".equals(inputVenta.getPrimerApellidoCliente().trim()))) {
			sql += " AND UPPER(V." + Venta.CAMPO_APELLIDO + ") = UPPER('" + inputVenta.getPrimerApellidoCliente()
					+ "')";
		}

		if (!(inputVenta.getNumTelefono() == null || "".equals(inputVenta.getNumTelefono().trim()))) {
			sql += " AND V." + Venta.CAMPO_NUM_TELEFONO + " = " + inputVenta.getNumTelefono();
		}

		if (!(inputVenta.getTipoDocumentoCliente() == null || "".equals(inputVenta.getTipoDocumentoCliente().trim()))) {
			sql += " AND UPPER(V." + Venta.CAMPO_TIPO_DOCCLIENTE + ") = UPPER('" + inputVenta.getTipoDocumentoCliente()
					+ "')";
		}

		if (!(inputVenta.getNumDocCliente() == null || "".equals(inputVenta.getNumDocCliente().trim()))) {
			sql += " AND UPPER(V." + Venta.CAMPO_NUM_DOCCLIENTE + ") = UPPER('" + inputVenta.getNumDocCliente() + "')";
		}

		if (!("".equals(inputVenta.getFechaInicio().trim()) || inputVenta.getFechaInicio() == null
				|| "".equals(inputVenta.getFechaFin().trim()) || inputVenta.getFechaFin() == null)) {
			sql += " AND TRUNC(V." + Venta.CAMPO_CREADO_EL + ") BETWEEN TRUNC(TO_DATE('" + inputVenta.getFechaInicio()
					+ "', 'yyyymmdd')) " + "AND TRUNC(TO_DATE('" + inputVenta.getFechaFin() + "' , 'yyyymmdd'))";
		}

		return sql;
	}

	private static String getZonaComercial(Connection conn, String idJornada, BigDecimal idPais) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = "WITH JORNADA AS (SELECT IDTIPO, DESCRIPCION_TIPO TIPO, TCSCCATPAISID PAIS "
				+ "FROM TC_SC_JORNADA_VEND WHERE TCSCJORNADAVENID = ?)"
				+ "SELECT NOMBRE FROM TC_SC_BODEGAVIRTUAL WHERE TCSCCATPAISID = ?"
				+ " AND TCSCBODEGAVIRTUALID = (SELECT (CASE TIPO "
				+ "WHEN 'RUTA' THEN (SELECT TCSCBODEGAVIRTUALID FROM TC_SC_RUTA WHERE TCSCRUTAID = IDTIPO AND TCSCCATPAISID = PAIS) "
				+ "ELSE (SELECT TCSCBODEGAVIRTUALID FROM TC_SC_PANEL WHERE TCSCPANELID = IDTIPO AND TCSCCATPAISID = PAIS)"
				+ "END) AS IDBODEGA FROM JORNADA)";

		log.debug("Query para obtener zona comercial: " + query);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, new BigDecimal(idJornada));
			pstmt.setBigDecimal(2, idPais);
			rst = pstmt.executeQuery();

			while (rst.next()) {
				return rst.getString("NOMBRE");
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return "";
	}

	public static List<ArticuloPromocionalVenta> articulosPromocionales(String idVenta, Connection con,
			BigDecimal idPais) throws SQLException {
		List<ArticuloPromocionalVenta> res = new ArrayList<ArticuloPromocionalVenta>();
		ArticuloPromocionalVenta item = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = "SELECT (SELECT A.DESCRIPCION FROM TC_SC_ART_PROMOCIONAL A WHERE B.ARTICULO = A.TCSCARTPROMOCIONALID"
				+ " AND A.TCSCCATPAISID = ?) AS NOMBREART, " + " B.CANTIDAD " + " FROM TC_SC_VENTA_DET B "
				+ " WHERE B.TCSCVENTAID = ?" + " AND B.TIPO_INV = (SELECT C.VALOR FROM TC_SC_CONFIGURACION C "
				+ "WHERE C.TCSCCATPAISID = ?" + " AND C.GRUPO = ? AND C.NOMBRE = ?)";

		log.debug("Query para obtener articulosPromocionales: " + query);
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setBigDecimal(1, idPais);
			pstmt.setBigDecimal(2, new BigDecimal(idVenta));
			pstmt.setBigDecimal(3, idPais);
			pstmt.setString(4, Conf.GRUPO_SOLICITUDES_TIPOINV);
			pstmt.setString(5, Conf.SOL_TIPOINV_SIDRA);

			rst = pstmt.executeQuery();

			while (rst.next()) {
				item = new ArticuloPromocionalVenta();
				item.setArticuloPromocional(rst.getString("NOMBREART"));
				item.setCantidad(rst.getString("CANTIDAD"));

				res.add(item);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return res;
	}

	public static InputVendedorDTS datosVendedor(String idVendedor, Connection con, BigDecimal idPais)
			throws SQLException {
		InputVendedorDTS res = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = "SELECT V.TCSCVENDDTSID, " + "     V.VENDEDOR, " + "       V.USUARIO, " + "       V.NOMBRE, "
				+ "       V.APELLIDO, " + "       V.TCSCBODEGAVIRTUALID " + "  FROM TC_SC_VEND_DTS V"
				+ " WHERE V.TCSCCATPAISID = ? AND V.VENDEDOR = ?";

		log.debug("Query para obtener datosVendedor: " + query);
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setBigDecimal(1, idPais);
			pstmt.setBigDecimal(2, new BigDecimal(idVendedor));
			rst = pstmt.executeQuery();

			while (rst.next()) {
				res = new InputVendedorDTS();
				res.setUsuarioVendedor(rst.getString("USUARIO"));
				res.setNombres(rst.getString("NOMBRE") + " " + rst.getString("APELLIDO"));
				res.setIdBodegaVirtual(rst.getString("TCSCBODEGAVIRTUALID"));
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return res;
	}

	public static InputBodegaVirtual datosBodegaVirtual(String idBodegaVirtual, Connection con, String codArea,
			BigDecimal idPais) throws SQLException {
		InputBodegaVirtual res = null;

		PreparedStatement pstmt = null;
		ResultSet rst = null;
		StringBuilder query = new StringBuilder();
		query.append("SELECT B.TCSCBODEGAVIRTUALID, B.NOMBRE");
		query.append(
				" FROM " + ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", codArea) + " B");
		query.append(" WHERE B.TCSCBODEGAVIRTUALID = ? AND B.TCSCCATPAISID = ?");

		log.debug("Query para obtener datosBodegaVirtual: " + query);
		try {
			pstmt = con.prepareStatement(query.toString());
			pstmt.setBigDecimal(1, new BigDecimal(idBodegaVirtual));
			pstmt.setBigDecimal(2, idPais);
			rst = pstmt.executeQuery();

			while (rst.next()) {
				res = new InputBodegaVirtual();
				res.setIdBodega(rst.getString("TCSCBODEGAVIRTUALID"));
				res.setNombre(rst.getString("NOMBRE"));
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return res;
	}

	public static InputPDV datosPuntoVenta(String idPdv, Connection con, String idDts, String codArea,
			BigDecimal idPais) throws SQLException {
		InputPDV res = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		StringBuilder query = new StringBuilder();

		query.append(
				"SELECT P.TCSCPUNTOVENTAID,      P.NOMBRE,  P.NIT,   P.REGISTRO_FISCAL,    P.NOMBRE_FISCAL,     P.DEPARTAMENTO,  P.MUNICIPIO,  P.GIRO_NEGOCIO ");
		query.append(
				"  FROM " + ControladorBase.getParticion(PuntoVenta.N_TABLA, Conf.PARTITION, idDts, codArea) + " P");
		query.append(" WHERE P.TCSCPUNTOVENTAID =? AND TCSCCATPAISID = ?");

		log.debug("Query para obtener datosPuntoVenta:" + query.toString());
		try {
			pstmt = con.prepareStatement(query.toString());
			pstmt.setBigDecimal(1, new BigDecimal(idPdv));
			pstmt.setBigDecimal(2, idPais);
			rst = pstmt.executeQuery();

			while (rst.next()) {
				res = new InputPDV();
				res.setIdPDV(rst.getString("TCSCPUNTOVENTAID"));
				res.setNombrePDV(rst.getString("NOMBRE"));
				res.setNit(rst.getString("NIT"));
				res.setRegistroFiscal(rst.getString("REGISTRO_FISCAL"));
				res.setGiroNegocio(rst.getString("GIRO_NEGOCIO"));
				res.setNombreFiscal(rst.getString("NOMBRE_FISCAL"));
				res.setDepartamento(rst.getString("DEPARTAMENTO"));
				res.setMunicipio(rst.getString("MUNICIPIO"));
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return res;
	}

	public static List<DetallePago> detallePago(String idVenta, Connection con, String tasaCambio) throws SQLException {
		List<DetallePago> res = new ArrayList<DetallePago>();
		DetallePago item = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = "SELECT D.formapago, " + "       D.monto, " + "       D.num_autorizacion, " + "       D.banco, "
				+ "       D.marca_tarjeta, " + "       D.digitos_tarjeta, " + "       D.num_cheque, "
				+ "       D.fecha_emision, " + "       D.num_reserva, " + "       D.no_cuenta "
				+ "  FROM TC_SC_DET_PAGO D " + " WHERE D.tcscventaid = ? ";

		log.debug("Query para obtener detallePago: " + query);
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setBigDecimal(1, new BigDecimal(idVenta));
			rst = pstmt.executeQuery();

			while (rst.next()) {
				item = new DetallePago();
				item.setFormaPago(rst.getString(PagoDet.CAMPO_FORMAPAGO));
				item.setMonto(UtileriasJava.convertirMoneda(rst.getString(PagoDet.CAMPO_MONTO), tasaCambio));
				item.setNumAutorizacion(
						UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, PagoDet.CAMPO_NUM_AUTORIZACION));
				item.setBanco(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PagoDet.CAMPO_BANCO));
				item.setMarcaTarjeta(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PagoDet.CAMPO_MARCA_TARJETA));
				item.setDigitosTarjeta(
						UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, PagoDet.CAMPO_DIGITOS_TARJETA));
				item.setNumeroCheque(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PagoDet.CAMPO_NUM_CHEQUE));
				item.setFechaEmision(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, PagoDet.CAMPO_FECHA_EMISION));
				item.setNumeroReserva(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, PagoDet.CAMPO_NUM_RESERVA));
				item.setCuentaCliente(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PagoDet.CAMPO_NO_CUENTA));

				res.add(item);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return res;
	}

	public static String getNombrePanelRuta(Connection conn, String idJornada, String codArea, BigDecimal idPais)
			throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		StringBuilder query = new StringBuilder();
		String result = "";
		query.append("SELECT CASE ");
		query.append(
				"WHEN DESCRIPCION_TIPO='RUTA' THEN (SELECT NOMBRE FROM TC_SC_RUTA WHERE TCSCRUTAID = J.IDTIPO AND TCSCCATPAISID=J.TCSCCATPAISID) ");
		query.append(
				"WHEN DESCRIPCION_TIPO='PANEL' THEN (SELECT NOMBRE FROM TC_SC_PANEL WHERE TCSCPANELID = J.IDTIPO AND TCSCCATPAISID=J.TCSCCATPAISID) ");
		query.append("END NOMBREPANELRUTA FROM"
				+ ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea));
		query.append(" J WHERE TCSCCATPAISID =? AND TCSCJORNADAVENID = ?");

		log.debug("Query getNombrePanelRuta: " + query.toString());
		try {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setBigDecimal(1, idPais);
			pstmt.setBigDecimal(2, new BigDecimal(idJornada));
			rst = pstmt.executeQuery();

			while (rst.next()) {
				result = rst.getString("NOMBREPANELRUTA");
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return result;
	}

	/* DETALLE DE VENTAS */
	public static OutputArticuloVenta getDetalleVenta(Connection conn, List<Filtro> condiciones,
			InputGetDetalle inputDet, BigDecimal idPais, String codArea) throws SQLException {
		String nombreMetodo = "getDetalleVenta";
		String nombreClase = new CurrentClassGetter().getClassName();
		OutputArticuloVenta output = new OutputArticuloVenta();
		Respuesta respuesta = null;
		List<Map<String, String>> datosDetalleVenta;
		List<Order> orden = new ArrayList<Order>();
		List<ArticuloVenta> list = new ArrayList<ArticuloVenta>();

		int min = (inputDet.getMin() != null && !inputDet.getMin().equals("")) ? new Integer(inputDet.getMin()) : 0;
		int max = (inputDet.getMax() != null && !inputDet.getMax().equals("")) ? new Integer(inputDet.getMax()) : 0;

		String campos[] = { VentaDet.CAMPO_TCSCVENTADETID, VentaDet.CAMPO_TCSCVENTAID,
				VentaDet.CAMPO_TCSCBODEGAVIRTUALID, VentaDet.CAMPO_ARTICULO,
				"CASE WHEN " + VentaDet.CAMPO_TIPO_GRUPO_SIDRA + " = 'BONO' THEN " + VentaDet.CAMPO_OBSERVACIONES
						+ " ELSE " + "NVL((SELECT A." + ArticulosSidra.CAMPO_DESCRIPCION + " FROM "
						+ ArticulosSidra.N_TABLA + " A WHERE A." + ArticulosSidra.CAMPO_ARTICULO + " = V."
						+ VentaDet.CAMPO_ARTICULO + " AND A." + ArticulosSidra.CAMPO_TCSCCATPAISID + " = " + idPais
						+ ")," + "(SELECT A." + Inventario.CAMPO_DESCRIPCION + " FROM " + Inventario.N_TABLA
						+ " A WHERE A." + ArticulosSidra.CAMPO_ARTICULO + " = V." + VentaDet.CAMPO_ARTICULO + " AND A."
						+ ArticulosSidra.CAMPO_TCSCCATPAISID + " = " + idPais + " AND ROWNUM = 1)) "
						+ "END AS NOM_ARTICULO",
				VentaDet.CAMPO_TIPO_INV, VentaDet.CAMPO_TIPO_GRUPO_SIDRA, VentaDet.CAMPO_SERIE,
				VentaDet.CAMPO_SERIE_ASOCIADA, VentaDet.CAMPO_NUM_TELEFONO, VentaDet.CAMPO_CANTIDAD,
				VentaDet.CAMPO_PRECIO_UNITARIO, VentaDet.CAMPO_PRECIO_TOTAL, VentaDet.CAMPO_IMPUESTO,
				VentaDet.CAMPO_DESCUENTO_SCL, VentaDet.CAMPO_DESCUENTO_SIDRA, VentaDet.CAMPO_TCSCOFERTACAMPANIAID,
				VentaDet.CAMPO_PRECIO_FINAL, VentaDet.CAMPO_GESTION, VentaDet.CAMPO_OBSERVACIONES,
				VentaDet.CAMPO_MODALIDAD,
				"(CASE WHEN " + VentaDet.CAMPO_SERIE + " IS NULL THEN 0 ELSE 1 END) AS SERIADO", VentaDet.CAMPO_ESTADO,
				VentaDet.CAMPO_CREADO_EL, VentaDet.CAMPO_CREADO_POR, VentaDet.CAMPO_MODIFICADO_EL,
				VentaDet.CAMPO_MODIFICADO_POR, };

		orden.add(new Order(VentaDet.CAMPO_ARTICULO, Order.ASC));

		datosDetalleVenta = UtileriasBD.getPaginatedData(conn, VentaDet.N_TABLA + " V", campos, condiciones, null,
				orden, min, max);

		String idVenta = "";
		String idDetalleVenta = "";

		if (datosDetalleVenta.isEmpty()) {
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_DET_VENTAS_827, null, nombreClase,
					nombreMetodo, null, codArea);

		} else {
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase,
					nombreMetodo, null, codArea);
			String tasaCambio = UtileriasBD.getOneRecord(conn, Venta.CAMPO_TASA_CAMBIO, Venta.N_TABLA, condiciones);

			for (int x = 0; x < datosDetalleVenta.size(); x++) {
				ArticuloVenta item = new ArticuloVenta();
				idVenta = inputDet.getIdVenta();
				idDetalleVenta = datosDetalleVenta.get(x).get(VentaDet.CAMPO_TCSCVENTADETID);

				item.setIdVentaDet(datosDetalleVenta.get(x).get(VentaDet.CAMPO_TCSCVENTADETID));
				item.setArticulo(datosDetalleVenta.get(x).get(VentaDet.CAMPO_ARTICULO));
				item.setDescripcion(datosDetalleVenta.get(x).get("NOM_ARTICULO"));
				item.setCantidad(datosDetalleVenta.get(x).get(VentaDet.CAMPO_CANTIDAD));
				item.setSeriado(datosDetalleVenta.get(x).get("SERIADO"));
				item.setSerie(UtileriasJava.getValue(datosDetalleVenta.get(x).get(VentaDet.CAMPO_SERIE)));
				item.setSerieAsociada(
						UtileriasJava.getValue(datosDetalleVenta.get(x).get(VentaDet.CAMPO_SERIE_ASOCIADA)));
				item.setTipoInv(datosDetalleVenta.get(x).get(VentaDet.CAMPO_TIPO_INV));
				item.setTipoGrupoSidra(datosDetalleVenta.get(x).get(VentaDet.CAMPO_TIPO_GRUPO_SIDRA));
				item.setPrecio(UtileriasJava
						.convertirMoneda(datosDetalleVenta.get(x).get(VentaDet.CAMPO_PRECIO_UNITARIO), tasaCambio));
				item.setGestion(UtileriasJava.getValue(datosDetalleVenta.get(x).get(VentaDet.CAMPO_GESTION)));
				item.setDescuentoSCL(UtileriasJava
						.convertirMoneda(datosDetalleVenta.get(x).get(VentaDet.CAMPO_DESCUENTO_SCL), tasaCambio));
				item.setNumTelefono(UtileriasJava.getValue(datosDetalleVenta.get(x).get(VentaDet.CAMPO_NUM_TELEFONO)));

				item.setDescuentoSidra(UtileriasJava
						.convertirMoneda(datosDetalleVenta.get(x).get(VentaDet.CAMPO_DESCUENTO_SIDRA), tasaCambio));
				item.setDetalleDescuentosSidra(descuentosArticulo(conn, idVenta, idDetalleVenta, tasaCambio));

				item.setImpuesto(UtileriasJava.convertirMoneda(
						UtileriasJava.getValue(datosDetalleVenta.get(x).get(VentaDet.CAMPO_IMPUESTO)), tasaCambio));
				item.setPrecioTotal(UtileriasJava
						.convertirMoneda(datosDetalleVenta.get(x).get(VentaDet.CAMPO_PRECIO_FINAL), tasaCambio));
				item.setModalidad(UtileriasJava.getValue(datosDetalleVenta.get(x).get(VentaDet.CAMPO_MODALIDAD)));
				item.setObservaciones(datosDetalleVenta.get(x).get(VentaDet.CAMPO_OBSERVACIONES));
				item.setEstado(datosDetalleVenta.get(x).get(VentaDet.CAMPO_ESTADO));

				item.setImpuestosArticulo(impuestosArticulo(conn, idVenta, idDetalleVenta, tasaCambio));

				list.add(item);
			}

			output.setDetalleVenta(list);
		}

		output.setRespuesta(respuesta);

		return output;
	}

	private static List<Descuento> descuentosArticulo(Connection conn, String idVenta, String idDetalleVenta,
			String tasaCambio) throws SQLException {
		List<Descuento> res = new ArrayList<Descuento>();
		Descuento item = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = "SELECT D.TCSCDESCUENTOSIDRAID, D.TCSCOFERTACAMPANIAID, OC.NOMBRE, D.VALOR "
				+ "FROM TC_SC_DESCUENTO_SIDRA D, TC_SC_OFERTA_CAMPANIA OC "
				+ "WHERE D.TCSCVENTAID = ? AND TCSCVENTADETID = ?"
				+ " AND D.TCSCOFERTACAMPANIAID = OC.TCSCOFERTACAMPANIAID";

		log.debug("Query para obtener descuentos: " + query);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, new BigDecimal(idVenta));
			pstmt.setBigDecimal(2, new BigDecimal(idDetalleVenta));
			rst = pstmt.executeQuery();

			while (rst.next()) {
				item = new Descuento();
				item.setIdOfertaCampania(rst.getString("NOMBRE"));
				item.setNombreOfertaCampania(rst.getString("VALOR"));
				item.setDescuento(UtileriasJava.convertirMoneda(rst.getString("VALOR"), tasaCambio));

				res.add(item);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return res;
	}

	public static List<Impuesto> impuestosArticulo(Connection conn, String idVenta, String idDetalleVenta,
			String tasaCambio) throws SQLException {
		List<Impuesto> res = new ArrayList<Impuesto>();
		Impuesto item = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = "SELECT I.IMPUESTO, I.VALOR " + "  FROM TC_SC_PAGO_IMPUESTO I"
				+ " WHERE I.TCSCVENTAID = ? AND TCSCVENTADETID = ?";

		log.debug("Query para obtener impuestosArticulo: " + query);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, new BigDecimal(idVenta));
			pstmt.setBigDecimal(2, new BigDecimal(idDetalleVenta));
			rst = pstmt.executeQuery();

			while (rst.next()) {
				item = new Impuesto();
				item.setNombreImpuesto(rst.getString("IMPUESTO"));
				item.setValor(UtileriasJava.convertirMoneda(rst.getString("VALOR"), tasaCambio));

				res.add(item);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return res;
	}

	public static List<InputDetCondicionOferta> obtenerOfertasVenta(Connection conn, String idPanelRuta,
			String tipoPanelRuta, String estadoAlta, String tipoOferta, String tipoGestionVenta, String codArea,
			BigDecimal idPais) throws SQLException {
		List<InputDetCondicionOferta> list = new ArrayList<InputDetCondicionOferta>();
		InputDetCondicionOferta item = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String tipoOfertaVenta = UtileriasJava.getConfig(conn, Conf.GRUPO_CONDICION_TIPOOFERTA,
				Conf.CONDICION_OFERTA_VENTA, codArea);
		String tipoCondicionGenerico = UtileriasJava.getConfig(conn, Conf.GRUPO_CONDICION_TIPO,
				Conf.TIPO_CONDICION_GENERICO, codArea);

		String query = "SELECT A.TCSCOFERTACAMPANIAID, " + "C.TIPO_DESCUENTO, " + "C.VALOR_DESCUENTO, "
				+ "C.MONTO_INICIAL, " + "C.MONTO_FINAL "
				+ "FROM TC_SC_DET_PANELRUTA A, TC_SC_CONDICION B, TC_SC_DET_CONDICION_OFERTA C "
				+ "WHERE A.TCSCTIPOID = ?" + " AND UPPER(A.TIPO) = ? " + "AND UPPER(A.ESTADO) =? "
				+ "AND A.TCSCOFERTACAMPANIAID = B.TCSCOFERTACAMPANIAID " + "AND UPPER(B.TIPO_OFERTACAMPANIA) =? "
				+ "AND UPPER(B.TIPO_CONDICION) = ? " + "AND UPPER(B.TIPO_GESTION) =? " + "AND UPPER(B.ESTADO) = ?"
				+ "AND B.TCSCCONDICIONID = C.TCSCCONDICIONID " + "AND UPPER(C.TIPO_OFERTA) =? "
				+ "AND UPPER(C.ESTADO) = ? " + "AND A.TCSCCATPAISID = B.TCSCCATPAISID "
				+ "AND C.TCSCCATPAISID = B.TCSCCATPAISID " + "AND A.TCSCCATPAISID = ?";

		log.debug("Query para obtener ofertas por venta: " + query);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, new BigDecimal(idPanelRuta));
			pstmt.setString(2, tipoPanelRuta.toUpperCase());
			pstmt.setString(3, estadoAlta.toUpperCase());
			pstmt.setString(4, tipoOferta.toUpperCase());
			pstmt.setString(5, tipoCondicionGenerico.toUpperCase());
			pstmt.setString(6, tipoGestionVenta.toUpperCase());
			pstmt.setString(7, estadoAlta.toUpperCase());
			pstmt.setString(8, tipoOfertaVenta.toUpperCase());
			pstmt.setString(9, estadoAlta.toUpperCase());
			pstmt.setBigDecimal(10, idPais);
			rst = pstmt.executeQuery();

			while (rst.next()) {
				item = new InputDetCondicionOferta();
				item.setIdOfertaCampania(rst.getString("TCSCOFERTACAMPANIAID"));
				item.setTipoDescuento(rst.getString("TIPO_DESCUENTO"));
				item.setValorDescuento(rst.getString("VALOR_DESCUENTO"));
				item.setMontoInicial(rst.getString("MONTO_INICIAL"));
				item.setMontoFinal(rst.getString("MONTO_FINAL"));

				list.add(item);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return list;
	}

	/***
	 * M\u00E9todo para obtener catologos de art\u00EDculos que pertenecen a telca
	 * pero son utilizados en SIDRA.
	 * 
	 * @param conn
	 * @param filtros
	 * @return
	 * @throws SQLException
	 */
	public static List<ArticulosSidra> getCatArticulo(Connection conn, List<Filtro> filtros, BigDecimal idPais)
			throws SQLException {
		List<ArticulosSidra> lstArticulos = new ArrayList<ArticulosSidra>();
		ArticulosSidra objArticulo = new ArticulosSidra();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String query = queryCatArticulo(filtros);
		log.debug("Query para obtener historico:" + query);

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, idPais);
			rst = pstmt.executeQuery();

			while (rst.next()) {
				objArticulo = new ArticulosSidra();

				objArticulo.setArticulo(rst.getBigDecimal(ArticulosSidra.CAMPO_ARTICULO));
				objArticulo.setDescripcion(rst.getString(ArticulosSidra.CAMPO_DESCRIPCION));
				objArticulo.setTipo_grupo_sidra(rst.getString(ArticulosSidra.CAMPO_TIPO_GRUPO_SIDRA));
				objArticulo.setEstado(rst.getString(ArticulosSidra.CAMPO_ESTADO));
				objArticulo.setTcscarticuloinvid(rst.getBigDecimal(ArticulosSidra.CAMPO_TCSCARTICULOINVID));

				lstArticulos.add(objArticulo);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return lstArticulos;
	}

	public static String queryCatArticulo(List<Filtro> filtros) {
		String query = "SELECT * FROM TC_SC_ARTICULO_INV WHERE TCSCCATPAISID = ?";

		if (!filtros.isEmpty()) {
			for (int i = 0; i < filtros.size(); i++) {
				if (filtros.get(i).getOperator().toString() == "between") {
					query += " AND " + filtros.get(i).getField() + " " + filtros.get(i).getOperator()
							+ filtros.get(i).getValue();
				} else {

					query += " AND " + filtros.get(i).getField() + " " + filtros.get(i).getOperator() + ""
							+ filtros.get(i).getValue() + "";
				}
			}
		}
		return query;
	}

	public static Respuesta validarImpuestosExentos(Connection conn, String listadoImpuestosExentos, String estadoAlta,
			String codArea, BigDecimal idPais) throws SQLException {
		String nombreMetodo = "validarImpuestosExentos";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta respuesta = new Respuesta();
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String sql = validaImp(listadoImpuestosExentos, estadoAlta, idPais);

		log.debug("Qry impuestos exentos: " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();
			sql = ""; // reutilizo la variable

			if (rst.next()) {
				do {
					sql += rst.getString("IMPUESTO") + ", ";
				} while (rst.next());
			}

			if (!sql.equals("")) {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_IMPUESTOS_EXENTO_811, null,
						nombreClase, nombreMetodo, sql.substring(0, sql.length() - 2), codArea);
				return respuesta;
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}
		return null;
	}

	public static String validaImp(String listadoImpuestosExentos, String estadoAlta, BigDecimal idPais) {

		String sql = "SELECT TO_CHAR(COLUMN_VALUE) AS IMPUESTO FROM TABLE(SYS.DBMS_DEBUG_VC2COLL("
				+ listadoImpuestosExentos + ")) " + "MINUS SELECT " + Catalogo.CAMPO_NOMBRE + " FROM "
				+ Catalogo.N_TABLA + " " + "WHERE UPPER(" + Catalogo.CAMPO_GRUPO + ") = '"
				+ Conf.GRUPO_IMPUESTO_PAIS.toUpperCase() + "' " + "AND UPPER(" + Catalogo.CAMPO_ESTADO + ") = '"
				+ estadoAlta.toUpperCase() + "'" + " AND " + Catalogo.CAMPO_TC_SC_CATPAIS_ID + "=" + idPais;
		return sql;
	}

	public static void insertaDetImpuestoExento(Connection conn, List<Impuesto> impuestosExento, BigDecimal idVenta,
			String usuario) throws SQLException {
		if (impuestosExento != null && !impuestosExento.isEmpty()) {
			String valores = "";
			PreparedStatement pstmt = null;
			String insert = "";

			List<String> listaInserts = new ArrayList<String>();

			String campos[] = { Exento.CAMPO_TCSCEXENTOID, Exento.CAMPO_TCSCVENTAID, Exento.CAMPO_DESCRIPCION,
					Exento.CAMPO_CREADO_POR, Exento.CAMPO_CREADO_EL };

			for (Impuesto obj : impuestosExento) {
				valores = UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, Exento.SEQUENCE, Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idVenta.toString(), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, obj.getNombreImpuesto(), Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, usuario, Conf.INSERT_SEPARADOR_SI)
						+ UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_NO);

				listaInserts.add(valores);
			}

			// armando insert
			insert = UtileriasBD.armarQueryInsertAll(Exento.N_TABLA, campos, listaInserts);
			try {
				pstmt = conn.prepareStatement(insert);
				pstmt.executeUpdate();

			} finally {
				DbUtils.closeQuietly(pstmt);
			}
		}
	}

	/*
	 * M\u00E9todo para validar que la venta enviada por el m\u00F3vil no haya sido
	 * enviada previamente y asi evitar registrar una venta duplicada en la base de
	 * datos de SIDRA
	 * 
	 * @param conn
	 * 
	 * @param fecha
	 * 
	 * @param venta
	 * 
	 * @param dispositivo
	 * 
	 * @return
	 * 
	 * @throws SQLException
	 */
	public static BigDecimal validaSincVenta(Connection conn, String fecha, String venta, String dispositivo,
			String idJornada, String codArea) throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		StringBuilder query = new StringBuilder();

		query.append("SELECT TCSCVENTAID ");
		query.append("  FROM TC_SC_VENTA PARTITION(" + ControladorBase.getPais(codArea) + ")");
		query.append(" WHERE     ID_VENTA_MOVIL =?");
		query.append("       AND COD_DISPOSITIVO = ? ");
		query.append("       AND TCSCJORNADAVENID = ?");
		query.append("       AND FECHA_EMISION = TO_DATE (?, 'YYYYMMddHH24MISS') ");
		log.trace("query valida sincroniza venta" + query.toString());
		try {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setBigDecimal(1, new BigDecimal(venta));
			pstmt.setString(2, dispositivo.toUpperCase());
			pstmt.setBigDecimal(3, new BigDecimal(idJornada));
			pstmt.setString(4, fecha);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				ret = rst.getBigDecimal(1);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}
	public static int updateDetPagoRefund(Connection conn, String idPayment) throws SQLException {
    	PreparedStatement pstmt = null;
    	String sql ="UPDATE TC_SC_DET_PAGO SET ESTADO_REEMBOLSO="+"'1'"+ "WHERE ID_PAYMENT= " +"'" +idPayment+"'";
    	log.debug("update TC_SC_DET_PAGO: " + sql);
    	try {
    		pstmt = conn.prepareStatement(sql);
    		pstmt.execute();
    	return	pstmt.getUpdateCount();
    	} finally {
    		DbUtils.closeQuietly(pstmt);
    	}
    	
    }
}
