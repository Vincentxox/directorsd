package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.asignacion.InputArticuloAsignacion;
import com.consystec.sc.ca.ws.input.historico.InputArticuloHistorico;
import com.consystec.sc.ca.ws.input.traslado.InputArticuloTraslado;
import com.consystec.sc.sv.ws.orm.HistoricoInv;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class OperacionMovimientosInventario extends ControladorBase {
	private static final Logger log = Logger.getLogger(OperacionMovimientosInventario.class);

	public static int getDiferenciaCeros(String serie) {
		char[] numArray = serie.toCharArray();
		int cont = 0;
		for (int i = 0; i < numArray.length; i++) {
			if (numArray[i] == '0') {
				cont++;
			} else {
				log.trace("Cantidad de 0's: " + cont);
				break;
			}
		}

		return cont;
	}

	public static String validarSeries(Connection conn, String serieInicial, String serieFinal, String idBodega,
			int tipo, String estado, String tipoInv, String codArea) throws SQLException {
		String respuesta = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String nombrePaquete = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.PAQUETE_SIDRA, codArea);
		int diferenciaCeros = OperacionMovimientosInventario.getDiferenciaCeros(serieInicial);

		String sql = queryvalidaSeries(serieInicial, serieFinal, idBodega, tipo, estado, tipoInv, codArea,
				nombrePaquete, diferenciaCeros);

		log.debug("Qry rango: " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				respuesta = rst.getString(1);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return respuesta;
	}

	public static String queryvalidaSeries(String serieInicial, String serieFinal, String idBodega, int tipo,
			String estado, String tipoInv, String codArea, String nombrePaquete, int diferenciaCeros) {
		String sql = "SELECT " + nombrePaquete + ".F_TC_SC_VALIDA_RANGO(" + serieInicial + ", " + serieFinal + ", "
				+ idBodega + ", " + tipo + ", '" + estado + "', '" + tipoInv + "', " + diferenciaCeros + "," + codArea
				+ ")" + " FROM DUAL";
		return sql;
	}

	public static String validarSeriesLoteInsert(Connection conn, String serieInicial, String serieFinal,
			String articulo, String serieAsociada, String idBodega, int tipo, String estado, String tipoInv,
			String codArea) throws SQLException {
		String respuesta = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String nombrePaquete = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.PAQUETE_SIDRA, codArea);
		int diferenciaCeros = OperacionMovimientosInventario.getDiferenciaCeros(serieInicial);

		String sql = queryvalidaSeriesLote(serieInicial, serieFinal, idBodega, tipo, estado, tipoInv, codArea,
				nombrePaquete, diferenciaCeros, articulo, serieAsociada);

		log.debug("Qry rango: " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				respuesta = rst.getString(1);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return respuesta;
	}

	public static String queryvalidaSeriesLote(String serieInicial, String serieFinal, String idBodega, int tipo,
			String estado, String tipoInv, String codArea, String nombrePaquete, int diferenciaCeros, String articulo,
			String serieAsociada) {
		String sql = "SELECT " + nombrePaquete + ".F_TC_SC_VALIDA_RANGO_LOTE_INS(" + serieInicial + ", " + serieFinal
				+ ", " + articulo + ", '" + serieAsociada + "', " + idBodega + ", " + tipo + ", '" + estado + "', '"
				+ tipoInv + "', " + diferenciaCeros + "," + codArea + ")" + " FROM DUAL";
		return sql;
	}

	public static String validarSeriesLoteUpdate(Connection conn, String serieInicial, String serieFinal,
			String articulo, String serieAsociada, String idBodega, int tipo, String estado, String tipoInv,
			String codArea) throws SQLException {
		String respuesta = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String nombrePaquete = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.PAQUETE_SIDRA, codArea);
		int diferenciaCeros = OperacionMovimientosInventario.getDiferenciaCeros(serieInicial);

		String sql = queryvalidaSeriesUpdLote(serieInicial, serieFinal, articulo, serieAsociada, idBodega, tipo, estado,
				tipoInv, codArea, nombrePaquete, diferenciaCeros);

		log.debug("Qry rango: " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				respuesta = rst.getString(1);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return respuesta;
	}

	public static String queryvalidaSeriesUpdLote(String serieInicial, String serieFinal, String articulo,
			String serieAsociada, String idBodega, int tipo, String estado, String tipoInv, String codArea,
			String nombrePaquete, int diferenciaCeros) {
		String sql = "SELECT " + nombrePaquete + ".F_TC_SC_VALIDA_RANGO_LOTE_UPD(" + serieInicial + ", " + serieFinal
				+ ", " + articulo + ", '" + serieAsociada + "', " + idBodega + ", " + tipo + ", '" + estado + "', '"
				+ tipoInv + "', " + diferenciaCeros + "," + codArea + ")" + " FROM DUAL";
		return sql;
	}

	public static String actualizarSeries(Connection conn, BigInteger serieInicial, BigInteger serieFinal,
			String idBodega, String usuario, String campoTransaccion, int idTransaccion, String estado, String tipoArt,
			String codArea, BigDecimal idPais) throws SQLException {
		Statement stmt = null;
		int[] updateCounts;
		String SqlUpdate = "";
		try {
			stmt = conn.createStatement();
			log.trace("tipoArt:" + tipoArt);
			for (BigInteger i = serieInicial; i.compareTo(serieFinal) <= 0; i = i.add(BigInteger.ONE)) {
				String update = queryActualizaSeries(idBodega, usuario, campoTransaccion, idTransaccion, estado,
						tipoArt, i, codArea, idPais);
				SqlUpdate = update;
				log.trace("update:" + SqlUpdate);
				stmt.addBatch(SqlUpdate);
			}

			updateCounts = stmt.executeBatch();
		} finally {
			DbUtils.closeQuietly(stmt);
		}

		return validarUpdates(serieInicial, updateCounts);
	}

	public static String queryActualizaSeries(String idBodega, String usuario, String campoTransaccion,
			int idTransaccion, String estado, String tipoArt, BigInteger i, String codArea, BigDecimal idPais) {
		String update = "UPDATE " + getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodega, codArea) + " SET "
				+ Inventario.CAMPO_ESTADO + " = '" + estado + "', " + Inventario.CAMPO_MODIFICADO_POR + " = '" + usuario
				+ "', " + Inventario.CAMPO_MODIFICADO_EL + " = SYSDATE, " + campoTransaccion + " = " + idTransaccion
				+ " WHERE " + Inventario.CAMPO_TCSCBODEGAVIRTUALID + " = " + idBodega;
		if (tipoArt.equalsIgnoreCase("SIMCARD")) {
			update = update + " AND TCSCCATPAISID = " + idPais + " AND SUBSTR(" + Inventario.CAMPO_SERIE
					+ ",1,18) = TO_CHAR(" + i + ")";
		} else {
			update = update + " AND TCSCCATPAISID = " + idPais + " AND LTRIM(TO_CHAR(" + Inventario.CAMPO_SERIE
					+ "), '0') = TO_CHAR(" + i + ")";
		}

		return update;
	}

	public static String insertarSeriesTraslado(Connection conn, BigInteger serieInicial, BigInteger serieFinal,
			String idArticulo, String descripcion, String idBodegaDestino, String usuario, int cerosIzquierda,
			String estadoComercial, String estadoDisponible, String tipoGrupo, String tipoGrupoSidra, String tipoInv,
			String noLote, String codBodega, BigDecimal idPais) throws SQLException {
		Statement stmt = null;
		int[] insertCounts;
		try {
			stmt = conn.createStatement();
			String ceros = "";
			for (int i = 0; i < cerosIzquierda; i++) {
				ceros += "0";
			}
			String[] campos = { Inventario.N_TABLA_ID, Inventario.CAMPO_TCSCCATPAISID,
					Inventario.CAMPO_TCSCBODEGAVIRTUALID, Inventario.CAMPO_ARTICULO, Inventario.CAMPO_DESCRIPCION,
					Inventario.CAMPO_SERIE, Inventario.CAMPO_CANTIDAD, Inventario.CAMPO_ESTADO_COMERCIAL,
					Inventario.CAMPO_SERIADO, Inventario.CAMPO_TIPO_GRUPO, Inventario.CAMPO_ESTADO,
					Inventario.CAMPO_TIPO_GRUPO_SIDRA, Inventario.CAMPO_CREADO_EL, Inventario.CAMPO_CREADO_POR,
					Inventario.CAMPO_TIPO_INV, Inventario.CAMPO_NO_LOTE, Inventario.CAMPO_COD_BODEGA };
			String insert = "";
			for (BigInteger i = serieInicial; i.compareTo(serieFinal) <= 0; i = i.add(BigInteger.ONE)) {
				insert = queryInsertSerieTraslado(idArticulo, descripcion, idBodegaDestino, usuario, estadoComercial,
						estadoDisponible, tipoGrupo, tipoGrupoSidra, tipoInv, noLote, codBodega, campos, ceros, i,
						idPais);
				System.out.println(insert);
				stmt.addBatch(insert);
			}

			insertCounts = stmt.executeBatch();
		} finally {
			DbUtils.closeQuietly(stmt);
		}

		return validarUpdates(serieInicial, insertCounts);
	}

	public static String queryInsertSerieTraslado(String idArticulo, String descripcion, String idBodegaDestino,
			String usuario, String estadoComercial, String estadoDisponible, String tipoGrupo, String tipoGrupoSidra,
			String tipoInv, String noLote, String codBodega, String[] campos, String ceros, BigInteger i,
			BigDecimal idPais) {
		String insert = "INSERT INTO " + Inventario.N_TABLA + " (" + UtileriasBD.getCampos(campos) + ") " + " VALUES("
				+ Inventario.SEQUENCE + ", " + idPais + ", " + idBodegaDestino + ", " + idArticulo + ", '" + descripcion
				+ "', '" + ceros + i.toString() + "', " + 1 + ", '" + estadoComercial + "'" + ", " + 1 + ", '"
				+ tipoGrupo + "', '" + estadoDisponible + "', '" + tipoGrupoSidra + "', " + "SYSDATE" + ", '" + usuario
				+ "'" + ", '" + tipoInv + "'" + ", '" + noLote + "', '" + codBodega + "')";

		return insert;
	}

	public static String actualizarSeriesLoteTraslado(Connection conn, BigInteger serieInicial, BigInteger serieFinal,
			String idBodegaOrigen, String idBodegaDestino, String articulo, String noLote, String usuario,
			String estado, BigDecimal idPais, String codArea) throws SQLException {

		PreparedStatement pstmt = null;
		String okupdate = "OK";

		String update = queryUpdateSerieTraslado(idBodegaOrigen, noLote, codArea);

		MathContext mc = new MathContext(200, RoundingMode.HALF_UP);
		BigDecimal cant = new BigDecimal(serieFinal).subtract(new BigDecimal(serieInicial), mc).add(BigDecimal.ONE);
		try {
			pstmt = conn.prepareStatement(update);

			pstmt.setBigDecimal(1, new BigDecimal(idBodegaDestino));
			pstmt.setString(2, usuario);
			pstmt.setBigDecimal(3, new BigDecimal(idBodegaOrigen));
			pstmt.setBigDecimal(4, idPais);
			pstmt.setString(5, estado.toUpperCase());
			pstmt.setBigDecimal(6, new BigDecimal(serieInicial));
			pstmt.setBigDecimal(7, new BigDecimal(serieFinal));
			pstmt.setBigDecimal(8, new BigDecimal(articulo));
			int res = pstmt.executeUpdate();
			log.trace("cantidadRegistros update:" + res + "," + cant);
			if (res == cant.intValue()) {
				okupdate = "OK";
			} else {
				return "No se pudieron asignar series.";
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
		}

		return okupdate;

	}

	public static String queryUpdateSerieTraslado(String idBodegaOrigen, String noLote, String codArea) {
		String update = "UPDATE " + getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaOrigen, codArea) + " SET "
				+ Inventario.CAMPO_TCSCBODEGAVIRTUALID + " = ?, " + Inventario.CAMPO_MODIFICADO_POR + " = ?, "
				+ Inventario.CAMPO_MODIFICADO_EL + " = SYSDATE"

				+ " WHERE " + Inventario.CAMPO_TCSCBODEGAVIRTUALID + " = ?" + " AND TCSCCATPAISID =?  AND UPPER("
				+ Inventario.CAMPO_ESTADO + ") = ?" + " AND TO_NUMBER(" + Inventario.CAMPO_SERIE
				+ ") >=? AND TO_NUMBER(" + Inventario.CAMPO_SERIE + ")<=?" + " AND " + Inventario.CAMPO_ARTICULO
				+ " = ?" + " AND " + Inventario.CAMPO_NO_LOTE + " = " + noLote;

		return update;
	}

	public static String actualizarSeriesTraslado(Connection conn, BigInteger serieInicial, BigInteger serieFinal,
			String idBodegaOrigen, String idBodegaDestino, String articulo, String usuario, String estado,
			String codArea, BigDecimal idPais) throws SQLException {
		Statement stmt = null;
		int[] updateCounts;
		String SqlUpdate = "";
		try {
			stmt = conn.createStatement();

			for (BigInteger i = serieInicial; i.compareTo(serieFinal) <= 0; i = i.add(BigInteger.ONE)) {
				String update = queryActualizaSeries(idBodegaOrigen, idBodegaDestino, articulo, usuario, estado, i,
						codArea, idPais);
				SqlUpdate = update;
				stmt.addBatch(SqlUpdate);
			}

			updateCounts = stmt.executeBatch();
		} finally {
			DbUtils.closeQuietly(stmt);
		}

		return validarUpdates(serieInicial, updateCounts);
	}

	public static String queryActualizaSeries(String idBodegaOrigen, String idBodegaDestino, String articulo,
			String usuario, String estado, BigInteger i, String codArea, BigDecimal idPais) {
		String update = "UPDATE " + getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaOrigen, codArea) + " SET "
				+ Inventario.CAMPO_TCSCBODEGAVIRTUALID + " = " + idBodegaDestino + ", "
				+ Inventario.CAMPO_MODIFICADO_POR + " = '" + usuario + "', " + Inventario.CAMPO_MODIFICADO_EL
				+ " = SYSDATE" + " WHERE " + Inventario.CAMPO_TCSCBODEGAVIRTUALID + " = " + idBodegaOrigen
				+ " AND TCSCCATPAISID = " + idPais + " AND UPPER(" + Inventario.CAMPO_ESTADO + ") = '"
				+ estado.toUpperCase() + "'" + " AND LTRIM(TO_CHAR(" + Inventario.CAMPO_SERIE + "), '0') = TO_CHAR(" + i
				+ ")" + " AND " + Inventario.CAMPO_ARTICULO + " = " + articulo;
		return update;
	}

	public static String actualizarSeriesAsignacion(Connection conn, BigInteger serieInicial, BigInteger serieFinal,
			String idBodegaOrigen, String idBodegaDestino, int idPadre, String idVendedor, String usuario,
			String estadoDisponible, String tipoInv, BigDecimal idPais, String codArea) throws SQLException {
		String okupdate = "OK";
		String insert = "";
		PreparedStatement pstmt = null;
		insert = queryActualizaSeriesAsig(idBodegaOrigen, codArea);

		try {

			MathContext mc = new MathContext(200, RoundingMode.HALF_UP);
			BigDecimal cant = new BigDecimal(serieFinal).subtract(new BigDecimal(serieInicial), mc).add(BigDecimal.ONE);

			pstmt = conn.prepareStatement(insert);
			pstmt.setBigDecimal(1, new BigDecimal(idBodegaDestino));
			pstmt.setBigDecimal(2, new BigDecimal(idVendedor));
			pstmt.setBigDecimal(3, new BigDecimal(idPadre));
			pstmt.setString(4, usuario);
			pstmt.setBigDecimal(5, new BigDecimal(idBodegaOrigen));
			pstmt.setBigDecimal(6, idPais);
			pstmt.setString(7, estadoDisponible.toUpperCase());
			pstmt.setString(8, tipoInv);
			pstmt.setBigDecimal(9, new BigDecimal(serieInicial));
			pstmt.setBigDecimal(10, new BigDecimal(serieFinal));
			int res = pstmt.executeUpdate();
			log.trace("cantidadRegistros update:" + res + "," + cant);
			if (res == cant.intValue()) {
				okupdate = "OK";
			} else {
				return "No se pudieron asignar series.";
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
		return okupdate;
	}

	public static String queryActualizaSeriesAsig(String idBodegaOrigen, String codArea) {
		String insert = "UPDATE " + getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaOrigen, codArea) + " SET "
				+ Inventario.CAMPO_TCSCBODEGAVIRTUALID + " = ?, " + Inventario.CAMPO_IDVENDEDOR + " = ?, "
				+ Inventario.CAMPO_TCSCASIGNACIONRESERVAID + " = ?, " + Inventario.CAMPO_MODIFICADO_POR + " = ?, "
				+ Inventario.CAMPO_MODIFICADO_EL + " = SYSDATE"

				+ " WHERE " + Inventario.CAMPO_TCSCBODEGAVIRTUALID + " = ?" + " AND TCSCCATPAISID =?  AND UPPER("
				+ Inventario.CAMPO_ESTADO + ") = ?" + " AND " + Inventario.CAMPO_TIPO_INV + " = ?" + " AND TO_NUMBER("
				+ Inventario.CAMPO_SERIE + ") >=? AND TO_NUMBER(" + Inventario.CAMPO_SERIE + ")<=?";
		return insert;

	}

	public static void actualizarCantidadLote(Connection conn, String idArticulo, String lote,
			BigInteger cantTrasladada, BigInteger serieInicio, BigInteger serieFin, String idBodegaOrigen, String usuario,
			String codArea, BigDecimal idPais) throws SQLException {
		String SqlUpdate = "";
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String update = queryUpdCantLote(idArticulo, lote, cantTrasladada, serieInicio, serieFin, idBodegaOrigen,
					usuario, codArea, idPais);
			SqlUpdate = update;
			stmt.addBatch(SqlUpdate);
			stmt.executeBatch();
		} finally {
			DbUtils.closeQuietly(stmt);
		}
	}

	public static String queryUpdCantLote(String idArticulo, String lote, BigInteger cantTrasladada, BigInteger serieInicio,
			BigInteger serieFin, String idBodegaOrigen, String usuario, String codArea, BigDecimal idPais) {
		String update = "UPDATE " + getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaOrigen, codArea) + " SET "
				+ Inventario.CAMPO_CANTIDAD + "=" + Inventario.CAMPO_CANTIDAD + "-" + cantTrasladada + ", "
				+ Inventario.CAMPO_MODIFICADO_EL + " = " + "SYSDATE" + ", " + Inventario.CAMPO_MODIFICADO_POR + " = '"
				+ usuario + "'" + " WHERE " + Inventario.CAMPO_TCSCBODEGAVIRTUALID + " = " + idBodegaOrigen + " AND "
				+ Inventario.CAMPO_ARTICULO + " = " + idArticulo + " AND " + Inventario.CAMPO_SERIE + " IS NULL"
				+ " AND " + Inventario.CAMPO_NO_LOTE + " = '" + lote + "'" + " AND TCSCCATPAISID = " + idPais;

		if ("2".equalsIgnoreCase(lote)) {
			update += " AND " + serieInicio + " BETWEEN RANGO_SERIE_INICIO AND RANGO_SERIE_FIN ";
			update += " AND " + serieFin + " BETWEEN RANGO_SERIE_INICIO AND RANGO_SERIE_FIN ";
		}

		return update;
	}

	public static String actualizarSeriesReserva(Connection conn, BigInteger serieInicial, BigInteger serieFinal,
			String idBodegaOrigen, int idPadre, String idVendedor, String usuario, String estadoDisponible,
			String estadoReservado, String tipoInv, BigDecimal idPais, String codArea) throws SQLException {
		PreparedStatement pstmt = null;
		String okupdate = "OK";
		String insert = "";

		insert = queryUpdReserva(idBodegaOrigen, codArea);

		try {

			MathContext mc = new MathContext(200, RoundingMode.HALF_UP);
			BigDecimal cant = new BigDecimal(serieFinal).subtract(new BigDecimal(serieInicial), mc).add(BigDecimal.ONE);

			pstmt = conn.prepareStatement(insert);
			pstmt.setBigDecimal(1, new BigDecimal(idPadre));
			pstmt.setBigDecimal(2, new BigDecimal(idVendedor));
			pstmt.setString(3, estadoReservado);
			pstmt.setString(4, usuario);
			pstmt.setBigDecimal(5, new BigDecimal(idBodegaOrigen));
			pstmt.setBigDecimal(6, idPais);
			pstmt.setString(7, estadoDisponible.toUpperCase());
			pstmt.setString(8, tipoInv);
			pstmt.setBigDecimal(9, new BigDecimal(serieInicial));
			pstmt.setBigDecimal(10, new BigDecimal(serieFinal));

			int res = pstmt.executeUpdate();
			log.trace("cantidadRegistros update:" + res + "," + cant);
			if (res == cant.intValue()) {
				okupdate = "OK";
			} else {
				return "No se pudieron asignar series.";
			}

		} finally {
			DbUtils.closeQuietly(pstmt);
		}
		return okupdate;
	}

	public static String queryUpdReserva(String idBodegaOrigen, String codArea) {
		String insert = "UPDATE " + getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaOrigen, codArea) + " SET "
				+ Inventario.CAMPO_TCSCASIGNACIONRESERVAID + " = ?, " + Inventario.CAMPO_IDVENDEDOR + " = ?, "
				+ Inventario.CAMPO_ESTADO + " = ?, " + Inventario.CAMPO_MODIFICADO_POR + " = ?, "
				+ Inventario.CAMPO_MODIFICADO_EL + " = SYSDATE"

				+ " WHERE " + Inventario.CAMPO_TCSCBODEGAVIRTUALID + " = ?" + " AND TCSCCATPAISID =?  AND UPPER("
				+ Inventario.CAMPO_ESTADO + ") = ?" + " AND " + Inventario.CAMPO_TIPO_INV + " = ?" + " AND TO_NUMBER("
				+ Inventario.CAMPO_SERIE + ") >=? AND TO_NUMBER(" + Inventario.CAMPO_SERIE + ")<=?";

		return insert;
	}

	private static String validarUpdates(BigInteger serieInicial, int[] updateCounts) {
		String seriesFallidas = "OK";

		for (int i = 0; i < updateCounts.length; i++) {
			if (updateCounts[i] != 1) {
				if (seriesFallidas.equals("OK")) {
					seriesFallidas = serieInicial + ", ";
				} else {
					seriesFallidas += serieInicial + ", ";
				}
			}

			serieInicial = serieInicial.add(BigInteger.ONE);
		}

		return seriesFallidas;
	}

	public static boolean insertaHistorico(Connection conn, String idBodegaOrigen, String idBodegaDestino,
			List<InputArticuloHistorico> input, BigDecimal idTipoTransaccion, String estadoAlta, String usuario,
			String codArea, BigDecimal idPais) throws SQLException {
		List<HistoricoInv> listHistorico = new ArrayList<HistoricoInv>();

		if (input.isEmpty()) {
			return true;
		}

		for (int i = 0; i < input.size(); i++) {
			InputArticuloTraslado articulo = new InputArticuloTraslado();
			articulo.setIdArticulo(input.get(i).getIdArticulo());
			articulo.setSerie(input.get(i).getSerie());
			articulo.setSerieAsociada(input.get(i).getSerieAsociada());
			articulo.setSerieFinal(input.get(i).getSerieFinal());
			articulo.setCantidad(input.get(i).getCantidad());
			articulo.setTipoInv(input.get(i).getTipoInv());

			HistoricoInv historico = getInsertHistorico(idBodegaOrigen, idBodegaDestino, articulo, idTipoTransaccion,
					estadoAlta, usuario, codArea, idPais);

			listHistorico.add(historico);
		}

		return ControladorBase.insertaHistorico(conn, listHistorico);
	}

	public static String getInsertInventario(String idBodega, int cantidad, String estado, List<Filtro> condiciones,
			String usuario, String idAsignacionReserva, String idVendedor, BigDecimal idPais) {

		String[] campos = { Inventario.N_TABLA_ID, Inventario.CAMPO_TCSCBODEGAVIRTUALID, Inventario.CAMPO_TCSCCATPAISID,
				Inventario.CAMPO_ARTICULO, Inventario.CAMPO_SERIE, Inventario.CAMPO_DESCRIPCION,
				Inventario.CAMPO_CANTIDAD, Inventario.CAMPO_ESTADO_COMERCIAL, Inventario.CAMPO_SERIE_ASOCIADA,
				Inventario.CAMPO_SERIADO, Inventario.CAMPO_TIPO_INV, Inventario.CAMPO_TCSCASIGNACIONRESERVAID,
				Inventario.CAMPO_TIPO_GRUPO, Inventario.CAMPO_TIPO_GRUPO_SIDRA, Inventario.CAMPO_IDVENDEDOR,
				Inventario.CAMPO_ESTADO, Inventario.CAMPO_CREADO_EL, Inventario.CAMPO_CREADO_POR };

		String sql = "INSERT INTO " + Inventario.N_TABLA + " (" + UtileriasBD.getCampos(campos) + ") " + "(" + "SELECT "
				+ Inventario.SEQUENCE + ", " + idBodega + ", " + idPais + "," + Inventario.CAMPO_ARTICULO + ", "
				+ Inventario.CAMPO_SERIE + ", " + Inventario.CAMPO_DESCRIPCION + ", " + cantidad + ", "
				+ Inventario.CAMPO_ESTADO_COMERCIAL + ", " + Inventario.CAMPO_SERIE_ASOCIADA + ", "
				+ Inventario.CAMPO_SERIADO + ", " + Inventario.CAMPO_TIPO_INV + ", "
				+ (idAsignacionReserva == null || idAsignacionReserva.equals("") ? "NULL" : idAsignacionReserva) + ", "
				+ Inventario.CAMPO_TIPO_GRUPO + ", " + Inventario.CAMPO_TIPO_GRUPO_SIDRA + ", "
				+ (idVendedor == null || idVendedor.equals("") ? "NULL" : idVendedor) + ", " + "'" + estado + "', "
				+ "sysdate, " + "'" + usuario + "' " + "FROM " + Inventario.N_TABLA
				+ UtileriasBD.getCondiciones(condiciones) + ")";

		log.debug("Insert nuevo de inventario: " + sql);

		return sql;
	}

	public static HistoricoInv getInsertHistoricoTraslado(String idBodegaOrigen, String idBodegaDestino,
			InputArticuloTraslado inputArticulo, BigDecimal idTipoTransaccion, String estadoAlta, String usuario,
			String idTraslado, String codArea, BigDecimal idPais) {
		InputArticuloTraslado articulo = new InputArticuloTraslado();
		articulo.setIdArticulo(inputArticulo.getIdArticulo());
		articulo.setSerie(inputArticulo.getSerie());
		articulo.setSerieAsociada(inputArticulo.getSerieAsociada());
		articulo.setSerieFinal(inputArticulo.getSerieFinal());
		articulo.setCantidad(inputArticulo.getCantidad());
		articulo.setTipoInv(inputArticulo.getTipoInv());
		articulo.setIdTraslado(idTraslado);

		return getInsertHistorico(idBodegaOrigen, idBodegaDestino, articulo, idTipoTransaccion, estadoAlta, usuario,
				codArea, idPais);
	}

	public static HistoricoInv getInsertHistoricoAsignacion(String idBodegaOrigen, String idBodegaDestino,
			InputArticuloAsignacion inputArticulo, BigDecimal idTipoTransaccion, String estadoAlta, String usuario,
			String codArea, BigDecimal idPais) {
		InputArticuloTraslado articulo = new InputArticuloTraslado();
		articulo.setIdArticulo(inputArticulo.getIdArticulo());
		articulo.setSerie(inputArticulo.getSerie());
		articulo.setSerieAsociada(inputArticulo.getSerieAsociada());
		articulo.setSerieFinal(inputArticulo.getSerieFinal());
		articulo.setCantidad(inputArticulo.getCantidad());
		articulo.setTipoInv(inputArticulo.getTipoInv());

		return getInsertHistorico(idBodegaOrigen, idBodegaDestino, articulo, idTipoTransaccion, estadoAlta, usuario,
				codArea, idPais);
	}

	public static HistoricoInv getInsertHistorico(String idBodegaOrigen, String idBodegaDestino,
			InputArticuloTraslado inputArticulo, BigDecimal idTipoTransaccion, String estadoAlta, String usuario,
			String codArea, BigDecimal idPais) {
		HistoricoInv historico = new HistoricoInv();
		historico.setTcsctipotransaccionid(idTipoTransaccion);
		historico.setBodega_origen(new BigDecimal(idBodegaOrigen));
		historico.setBodega_destino(new BigDecimal(idBodegaDestino));

		String serie = "";
		if (inputArticulo.getSerie() != null && !inputArticulo.getSerie().equals("")) {
			serie = inputArticulo.getSerie();
		} else {
			serie = "";
		}

		String idArticulo = null;
		if (inputArticulo.getIdArticulo() != null && !inputArticulo.getIdArticulo().equals("")) {
			idArticulo = inputArticulo.getIdArticulo();
		} else if (inputArticulo.getSerie() != null && !inputArticulo.getSerie().equals("")) {
			List<Filtro> condiciones = new ArrayList<Filtro>();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_SERIE,
					inputArticulo.getSerie()));
			idArticulo = "(" + UtileriasBD.armarQuerySelectField(
					getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaDestino, codArea),
					Inventario.CAMPO_ARTICULO, condiciones, null) + ")";
		}

		String serieAsociada = "";
		if (inputArticulo.getSerie() != null && !inputArticulo.getSerie().equals("")
				&& inputArticulo.getSerieAsociada() != null && !inputArticulo.getSerieAsociada().equals("")) {
			serieAsociada = inputArticulo.getSerieAsociada();
		} else {
			serieAsociada = "";
		}

		String cantidad = null;
		if ((inputArticulo.getSerie() == null || inputArticulo.getSerie().equals(""))
				&& inputArticulo.getCantidad() != null && !inputArticulo.getCantidad().equals("")) {
			cantidad = inputArticulo.getCantidad();
		} else {
			cantidad = "1";
		}

		String serieFinal = "";
		if (inputArticulo.getSerie() != null && !inputArticulo.getSerie().equals("")
				&& inputArticulo.getSerieFinal() != null && !inputArticulo.getSerieFinal().equals("")) {
			serieFinal = inputArticulo.getSerieFinal();

			cantidad = new BigInteger(inputArticulo.getSerieFinal()).subtract(new BigInteger(inputArticulo.getSerie()))
					.add(BigInteger.ONE) + "";

			serieAsociada = "";
		} else {
			serieFinal = "";
		}

		historico.setArticulo(idArticulo);
		historico.setCantidad(new BigDecimal(cantidad));
		historico.setCod_num(null);
		historico.setSerie(serie);
		historico.setSerie_final(serieFinal);
		historico.setSerie_asociada(serieAsociada);
		historico.setEstado(estadoAlta);
		historico.setTipo_inv(inputArticulo.getTipoInv());
		historico.setCreado_por(usuario);
		if (inputArticulo.getIdTraslado() != null && !"".equals(inputArticulo.getIdTraslado())) {
			historico.setTcsctrasladoid(new BigDecimal(inputArticulo.getIdTraslado()));
		}
		historico.setTcScCatPaisId(idPais);
		return historico;
	}
}