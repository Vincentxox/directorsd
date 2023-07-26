package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.reporte.InputReporteXZ;
import com.consystec.sc.ca.ws.output.reporte.DesgloseVentas;
import com.consystec.sc.ca.ws.output.reporte.EncabezadoXZ;
import com.consystec.sc.ca.ws.output.reporte.FormasPago;
import com.consystec.sc.ca.ws.output.reporte.Reporte_XZ;
import com.consystec.sc.ca.ws.output.reporte.TotalesVenta;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.orm.ReporteXZ;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.JavaUtils;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.ericsson.sdr.utils.Country;

public class OperacionReporteXZ {
	private OperacionReporteXZ() {
	}

	private static final Logger log = Logger.getLogger(OperacionReporteXZ.class);

	/***
	 * M\u00E9todo para obtener datos de reporte X a generar
	 *
	 * @param conn
	 * @param filtros
	 * @param objDatos
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public static ReporteXZ getDatosReporteX(Connection conn, List<Filtro> filtros, InputReporteXZ objDatos)
			throws SQLException, ParseException {
		ReporteXZ respuesta = new ReporteXZ();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		PreparedStatement pstmt2 = null;
		ResultSet rst2 = null;
		String queryFiltros = "";
		String efectivo = "";
		String tarjeta = "";
		String cheque = "";
		String credito = "";
		String mpos = "";
		String abrPais = ControladorBase.getPais(objDatos.getCodArea());
		// obteniendo Formas de Pago
		efectivo = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_EFECTIVO,
				objDatos.getCodArea());
		tarjeta = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_TARJETA,
				objDatos.getCodArea());
		cheque = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_CHEQUE,
				objDatos.getCodArea());
		//mpos = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_MPOS, objDatos.getCodArea());

		String estadosAnulados = UtileriasJava.getConfig(conn, Conf.ESTADOS_REPORTE_XZ, Conf.ESTADOS_REPORTE_XZ,
				objDatos.getCodArea());
		if (Country.SV.getCodArea().equals(Integer.valueOf(objDatos.getCodArea()))) {
			credito = "Credito";
		} else {

			credito = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_CREDITO,
					objDatos.getCodArea());
		}
		// obteniendo filtros para consultas
		if (!filtros.isEmpty()) {
			for (int i = 0; i < filtros.size(); i++) {
				if (filtros.get(i).getOperator().toString() == "between") {
					queryFiltros += " AND " + filtros.get(i).getField() + " " + filtros.get(i).getOperator()
							+ filtros.get(i).getValue();
				} else {

					queryFiltros += " AND " + filtros.get(i).getField() + " " + filtros.get(i).getOperator() + ""
							+ filtros.get(i).getValue() + "";
				}
			}
		}

		String query1 = queryDatosReporteX(queryFiltros, estadosAnulados, abrPais);
		log.trace("Query para obtener reportex:" + query1);

		try {
			pstmt = conn.prepareStatement(query1);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				log.trace("encontro datos iniciales de reporte");
				Double dineroGaveta = 0.0;
				Date fecha;
				Date fechaHoy = new Date();
				String fechaActual = "";

				SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHAHORA);
				fechaActual = formatoFecha.format(fechaHoy);
				fecha = UtileriasJava.parseDate(fechaActual, formatoFecha);

				// datos que se obtuvieron desde el input
				respuesta.setTcscjornadavenid(new BigDecimal(objDatos.getIdJornada()));
				respuesta.setTipo_reporte(objDatos.getTipoReporte());
				respuesta.setCod_dispositivo(objDatos.getCodDispositivo());
				respuesta.setVendedor(new BigDecimal(objDatos.getIdVendedor()));
				respuesta.setFecha(new Timestamp(fecha.getTime()));
				respuesta.setCreado_por(objDatos.getUsuario());
				// ------------------------------------------
				respuesta.setAcumulado_ventas(rst.getBigDecimal("ACUMULADO_VENTAS"));
				respuesta.setMonto_ventas_brutas(rst.getBigDecimal("VENTAS_BRUTAS"));
				respuesta.setCant_ventas_brutas(rst.getBigDecimal("cant_VENTAS_BRUTAS"));
				respuesta.setMonto_anulaciones(rst.getBigDecimal("DEVOLUCIONES"));
				respuesta.setCant_anulaciones(rst.getBigDecimal("cant_DEVOLUCIONES"));
				respuesta.setMonto_descuentos(rst.getBigDecimal("descuentos"));
				respuesta.setCant_descuentos(rst.getBigDecimal("cantidad_descuentos"));
				respuesta.setTotal_exento(rst.getBigDecimal("TOTAL_EXENTO"));
				respuesta.setTotal_gravado(rst.getBigDecimal("TOTAL_GRAVADO"));

				dineroGaveta = (respuesta.getMonto_ventas_brutas().doubleValue()
						+ respuesta.getMonto_anulaciones().doubleValue());
				respuesta.setMonto_dinero_gaveta(new BigDecimal(dineroGaveta));

				respuesta.setMonto_fondos_iniciales(new BigDecimal(0));
				respuesta.setMonto_entrega_parcial(new BigDecimal(0));
				respuesta.setCant_entrega_parcial(new BigDecimal(0));
				respuesta.setCant_pagos_terceros(new BigDecimal(0));
				respuesta.setMonto_pagos_terceros(new BigDecimal(0));
				respuesta.setMonto_no_ventas(new BigDecimal(0));
				respuesta.setCant_no_ventas(new BigDecimal(0));
				respuesta.setTotal_no_sujeto(new BigDecimal(0));
			} else {
				respuesta = null;
			}

		} finally {

			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		if (respuesta != null) {
			StringBuilder query2 = new StringBuilder();
			query2.append(
					"  SELECT V.TCSCJORNADAVENID,   V.VENDEDOR,   P.FORMAPAGO,   nvl(SUM (round(P.MONTO * TASA_CAMBIO,2)),0) MONTO, ");
			query2.append("   nvl(COUNT (P.FORMAPAGO),0) CANTIDAD  FROM TC_SC_DET_PAGO P, TC_sC_VENTA PARTITION(");
			query2.append(abrPais);
			query2.append(") V, tc_sc_jornada_Vend PARTITION(");
			query2.append(abrPais);
			query2.append(") j   WHERE V.TCSCVENTAID = P.TCSCVENTAID   AND V.ESTADO NOT IN (");
			query2.append(estadosAnulados);
			query2.append(")	and      v.tcscjornadavenid=j.tcscjornadavenid ");
			query2.append(queryFiltros);
			query2.append("GROUP BY V.TCSCJORNADAVENID, V.VENDEDOR, P.FORMAPAGO ");

			log.trace("Query para obtener forma de pago reporte x:" + query2);
			try {
				pstmt2 = conn.prepareStatement(query2.toString());
				rst2 = pstmt2.executeQuery();

				while (rst2.next()) {
					if (rst2.getString("FORMAPAGO").equalsIgnoreCase(efectivo)) {
						respuesta.setMonto_efectivo(rst2.getBigDecimal("MONTO"));
						respuesta.setCant_pago_efectivo(rst2.getBigDecimal("CANTIDAD"));
					}

					if (rst2.getString("FORMAPAGO").equalsIgnoreCase(cheque)) {
						respuesta.setMonto_cheque(rst2.getBigDecimal("MONTO"));
						respuesta.setCant_pago_cheque(rst2.getBigDecimal("CANTIDAD"));
					}

					if (rst2.getString("FORMAPAGO").equalsIgnoreCase(tarjeta)) {
						respuesta.setMonto_tarjeta(rst2.getBigDecimal("MONTO"));
						respuesta.setCant_pago_tarjeta(rst2.getBigDecimal("CANTIDAD"));
					}
					if (rst2.getString("FORMAPAGO").equalsIgnoreCase(credito)) {
						respuesta.setMonto_credito(rst2.getBigDecimal("MONTO"));
						respuesta.setCant_pago_credito(rst2.getBigDecimal("CANTIDAD"));
					}
//					if (rst2.getString("FORMAPAGO").equalsIgnoreCase(mpos)) {
//						respuesta.setMonto_mpos(rst2.getBigDecimal("MONTO"));
//						respuesta.setCant_pago_mpos(rst2.getBigDecimal("CANTIDAD"));
//					}

				}
			} finally {
				DbUtils.closeQuietly(pstmt2);
				DbUtils.closeQuietly(rst2);
			}
		}

		return respuesta;
	}

	public static String queryDatosReporteX(String queryFiltros, String estadosAnulados, String ABR_PAIS) {
		String query1 = "  SELECT V.TCSCJORNADAVENID, " + "         V.VENDEDOR, "
				+ "         nvl((SELECT SUM (round(MONTO_PAGADO * TASA_CAMBIO,2)) "
				+ "            FROM TC_SC_VENTA PARTITION(" + ABR_PAIS + ")" + "           WHERE     ESTADO NOT IN ("
				+ estadosAnulados + ") " + "                 AND TCSCJORNADAVENID = V.TCSCJORNADAVENID "
				+ "                 AND V.VENDEDOR = VENDEDOR),0) " + "            AS ACUMULADO_VENTAS, "
				+ "         (SELECT SUM (round(MONTO_PAGADO * TASA_CAMBIO,2)) "
				+ "            FROM TC_SC_VENTA PARTITION(" + ABR_PAIS + ")"
				+ "           WHERE     TCSCJORNADAVENID = V.TCSCJORNADAVENID "
				+ "                 AND V.VENDEDOR = VENDEDOR) " + "            AS VENTAS_BRUTAS, "
				+ "         (SELECT COUNT (tcscventaid) " + "            FROM TC_SC_VENTA PARTITION(" + ABR_PAIS + ")"
				+ "           WHERE     TCSCJORNADAVENID = V.TCSCJORNADAVENID "
				+ "                 AND V.VENDEDOR = VENDEDOR) " + "            cant_VENTAS_BRUTAS, "
				+ "         (SELECT NVL (SUM (round(MONTO_PAGADO * TASA_CAMBIO,2)), 0) * -1 "
				+ "            FROM TC_SC_VENTA PARTITION(" + ABR_PAIS + ")" + "           WHERE     ESTADO IN ("
				+ estadosAnulados + ") " + "                 AND TCSCJORNADAVENID = V.TCSCJORNADAVENID "
				+ "                 AND V.VENDEDOR = VENDEDOR) " + "            DEVOLUCIONES, "
				+ "         (SELECT COUNT (tcscventaid) " + "            FROM TC_SC_VENTA PARTITION(" + ABR_PAIS + ")"
				+ "           WHERE     ESTADO IN (" + estadosAnulados + ") "
				+ "                 AND TCSCJORNADAVENID = V.TCSCJORNADAVENID "
				+ "                 AND V.VENDEDOR = VENDEDOR) " + "            cant_DEVOLUCIONES, "
				+ "         (SELECT NVL (SUM (round(descuentos * TASA_CAMBIO,2)), 0) "
				+ "            FROM tC_SC_venta PARTITION(" + ABR_PAIS + ")" + "           WHERE     descuentos <> 0 "
				+ "                 AND TCSCJORNADAVENID = V.TCSCJORNADAVENID "
				+ "                 AND V.VENDEDOR = VENDEDOR) " + "            descuentos, "
				+ "         (SELECT COUNT (tcscventaid) " + "            FROM tC_SC_venta PARTITION(" + ABR_PAIS + ")"
				+ "           WHERE     descuentos <> 0 "
				+ "                 AND TCSCJORNADAVENID = V.TCSCJORNADAVENID "
				+ "                 AND V.VENDEDOR = VENDEDOR) " + "            cantidad_descuentos, "
				+ "         (SELECT (SUM (round(MONTO_PAGADO * TASA_CAMBIO,2)) ) "
				+ "            FROM TC_SC_VENTA PARTITION(" + ABR_PAIS + ")"
				+ "           WHERE     EXENTO = 'NO EXENTO' " + "				  AND  ESTADO NOT IN ("
				+ estadosAnulados + ") " + "                 AND TCSCJORNADAVENID = V.TCSCJORNADAVENID "
				+ "                 AND V.VENDEDOR = VENDEDOR) " + "            TOTAL_GRAVADO, "
				+ "        nvl((SELECT (SUM (round(MONTO_PAGADO * TASA_CAMBIO,2)) + SUM (round(DESCUENTOS * TASA_CAMBIO,2))) "
				+ "            FROM TC_SC_VENTA PARTITION(" + ABR_PAIS + ")" + "           WHERE     EXENTO = 'EXENTO' "
				+ "                 AND TCSCJORNADAVENID = V.TCSCJORNADAVENID "
				+ "                 AND V.VENDEDOR = VENDEDOR),0) " + "            TOTAL_EXENTO "
				+ "    FROM TC_SC_VENTA PARTITION(" + ABR_PAIS + ") V, " + "TC_SC_JORNADA_VEND PARTITION(" + ABR_PAIS
				+ ") J" + " 	WHERE   V.TCSCJORNADAVENID = J.TCSCJORNADAVENID  ";

		query1 += queryFiltros + " GROUP BY V.TCSCJORNADAVENID, V.VENDEDOR ";

		return query1;
	}

	/***
	 * M\u00E9todo para obtener datos de reporte Z a generar
	 *
	 * @param conn
	 * @param filtros
	 * @param objDatos
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public static List<ReporteXZ> getDatosReporteZ(Connection conn, InputReporteXZ objDatos)
			throws SQLException, ParseException {
		ReporteXZ respuesta = new ReporteXZ();
		List<ReporteXZ> lstRespuesta = new ArrayList<ReporteXZ>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		PreparedStatement pstmt2 = null;
		ResultSet rst2 = null;
		String estadosAnulados = "";
		String efectivo = "";
		String tarjeta = "";
		String cheque = "";
		String credito = "";
		String mpos = "";
		String abrPais = ControladorBase.getPais(objDatos.getCodArea());
		// obteniendo Formas de Pago
		efectivo = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_EFECTIVO,
				objDatos.getCodArea());
		tarjeta = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_TARJETA,
				objDatos.getCodArea());
		cheque = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_CHEQUE,
				objDatos.getCodArea());
		//mpos = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_MPOS, objDatos.getCodArea());
		estadosAnulados = UtileriasJava.getConfig(conn, Conf.ESTADOS_REPORTE_XZ, Conf.ESTADOS_REPORTE_XZ,
				objDatos.getCodArea());
		if (Country.SV.getCodArea().equals(Integer.valueOf(objDatos.getCodArea()))) {
			credito = "Credito";
		} else {

			credito = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_CREDITO,
					objDatos.getCodArea());
		}

		log.trace("IdJornada en armar Registro:" + objDatos.getIdJornada());

		for (int a = 0; a < objDatos.getDispositivos().length; a++) {
			String query1 = queryDatosReporteZ(estadosAnulados, objDatos, objDatos.getDispositivos()[a], abrPais);

			try {
				pstmt = conn.prepareStatement(query1);
				rst = pstmt.executeQuery();

				if (rst.next()) {
					respuesta = new ReporteXZ();
					log.trace("encontro datos iniciales de reporte, Dispositivo:" + objDatos.getDispositivos()[a]);
					Double dineroGaveta = 0.0;
					Date fecha;
					SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
					fecha = UtileriasJava.parseDate(objDatos.getFecha(), formatoFecha);

					// datos que se obtuvieron desde el input

					respuesta.setTcscjornadavenid(new BigDecimal(objDatos.getIdJornada()));
					respuesta.setTipo_reporte(objDatos.getTipoReporte().toUpperCase());
					respuesta.setCod_dispositivo(objDatos.getDispositivos()[a]);
					respuesta.setFecha(new Timestamp(fecha.getTime()));
					respuesta.setCreado_por(objDatos.getUsuario());
					// ------------------------------------------
					respuesta.setAcumulado_ventas(rst.getBigDecimal("ACUMULADO_VENTAS"));
					respuesta.setMonto_ventas_brutas(rst.getBigDecimal("VENTAS_BRUTAS"));
					respuesta.setCant_ventas_brutas(rst.getBigDecimal("cant_VENTAS_BRUTAS"));
					respuesta.setMonto_anulaciones(rst.getBigDecimal("DEVOLUCIONES"));
					respuesta.setCant_anulaciones(rst.getBigDecimal("cant_DEVOLUCIONES"));
					respuesta.setMonto_descuentos(rst.getBigDecimal("descuentos"));
					respuesta.setCant_descuentos(rst.getBigDecimal("cantidad_descuentos"));
					respuesta.setTotal_exento(rst.getBigDecimal("TOTAL_EXENTO"));
					respuesta.setTotal_gravado(rst.getBigDecimal("TOTAL_GRAVADO"));

					dineroGaveta = (respuesta.getMonto_ventas_brutas().doubleValue()
							+ respuesta.getMonto_anulaciones().doubleValue());
					respuesta.setMonto_dinero_gaveta(new BigDecimal(dineroGaveta));

					respuesta.setMonto_fondos_iniciales(new BigDecimal(0));
					respuesta.setMonto_entrega_parcial(new BigDecimal(0));
					respuesta.setCant_entrega_parcial(new BigDecimal(0));
					respuesta.setCant_pagos_terceros(new BigDecimal(0));
					respuesta.setMonto_pagos_terceros(new BigDecimal(0));
					respuesta.setMonto_no_ventas(new BigDecimal(0));
					respuesta.setCant_no_ventas(new BigDecimal(0));
					respuesta.setTotal_no_sujeto(new BigDecimal(0));

					StringBuilder query2 = new StringBuilder();
					query2.append(
							"  SELECT P.FORMAPAGO, SUM (round(P.MONTO * V.TASA_CAMBIO,2)) MONTO, COUNT (P.FORMAPAGO) CANTIDAD ");
					query2.append("    FROM TC_SC_DET_PAGO P, TC_sC_VENTA  PARTITION (");
					query2.append(ControladorBase.getPais(objDatos.getCodArea()));
					query2.append(") V, tc_sc_jornada_Vend PARTITION (");
					query2.append(ControladorBase.getPais(objDatos.getCodArea()));
					query2.append(") j  WHERE     V.TCSCVENTAID = P.TCSCVENTAID    AND V.ESTADO NOT IN (");
					query2.append(estadosAnulados);
					query2.append(
							")         AND v.tcscjornadavenid = j.tcscjornadavenid   and j.tcscjornadavenid IN    (SELECT TCSCJORNADAVENID      FROM TC_SC_JORNADA_VEND   WHERE    TCSCJORNADAVENID = ?");
					query2.append(
							"                                OR JORNADA_RESPONSABLE = ?)     AND v.cod_dispositivo = ? GROUP BY P.FORMAPAGO ");

					log.trace("Query para obtener forma de pago reporte x:" + query2);
					try {
						pstmt2 = conn.prepareStatement(query2.toString());
						pstmt2.setBigDecimal(1, new BigDecimal(objDatos.getIdJornada()));
						pstmt2.setBigDecimal(2, new BigDecimal(objDatos.getIdJornada()));
						pstmt2.setString(3, objDatos.getDispositivos()[a]);
						rst2 = pstmt2.executeQuery();

						while (rst2.next()) {
							if (rst2.getString("FORMAPAGO").equalsIgnoreCase(efectivo)) {
								respuesta.setMonto_efectivo(rst2.getBigDecimal("MONTO"));
								respuesta.setCant_pago_efectivo(rst2.getBigDecimal("CANTIDAD"));
							}

							if (rst2.getString("FORMAPAGO").equalsIgnoreCase(tarjeta)) {
								respuesta.setMonto_tarjeta(rst2.getBigDecimal("MONTO"));
								respuesta.setCant_pago_tarjeta(rst2.getBigDecimal("CANTIDAD"));
							}
							if (rst2.getString("FORMAPAGO").equalsIgnoreCase(cheque)) {
								respuesta.setMonto_cheque(rst2.getBigDecimal("MONTO"));
								respuesta.setCant_pago_cheque(rst2.getBigDecimal("CANTIDAD"));
							}
							if (rst2.getString("FORMAPAGO").equalsIgnoreCase(credito)) {
								respuesta.setMonto_credito(rst2.getBigDecimal("MONTO"));
								respuesta.setCant_pago_credito(rst2.getBigDecimal("CANTIDAD"));
							}
//							if (rst2.getString("FORMAPAGO").equalsIgnoreCase(mpos)) {
//								respuesta.setMonto_mpos(rst2.getBigDecimal("MONTO"));
//								respuesta.setCant_pago_mpos(rst2.getBigDecimal("CANTIDAD"));
//							}

						}
					} finally {
						DbUtils.closeQuietly(pstmt2);
						DbUtils.closeQuietly(rst2);
					}

					lstRespuesta.add(respuesta);
				}
			} finally {
				DbUtils.closeQuietly(rst);
				DbUtils.closeQuietly(pstmt);
			}
		}
		return lstRespuesta;
	}

	public static String queryDatosReporteZ(String estadosAnulados, InputReporteXZ objDatos, String dispositivo,
			String ABR_PAIS) {
		String query1 = "SELECT v.cod_dispositivo, " + "         nvl((SELECT SUM (round(MONTO_PAGADO * TASA_CAMBIO,2)) "
				+ "            FROM TC_sC_VENTA PARTITION (" + ABR_PAIS + ")" + "           WHERE     ESTADO NOT IN ("
				+ estadosAnulados + ") " + "                 AND cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    TCSCJORNADAVENID = " + objDatos.getIdJornada() + " "
				+ "                                OR JORNADA_RESPONSABLE = " + objDatos.getIdJornada() + ")),0) "
				+ "            AS ACUMULADO_VENTAS, " + "         (SELECT SUM (round(MONTO_PAGADO * TASA_CAMBIO,2)) "
				+ "            FROM TC_sC_VENTA PARTITION (" + ABR_PAIS + ")"
				+ "           WHERE     cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    TCSCJORNADAVENID = " + objDatos.getIdJornada() + " "
				+ "                                OR JORNADA_RESPONSABLE = " + objDatos.getIdJornada() + ")) "
				+ "            AS VENTAS_BRUTAS, " + "         (SELECT COUNT (tcscventaid) "
				+ "            FROM TC_sC_VENTA PARTITION (" + ABR_PAIS + ")"
				+ "           WHERE     cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    TCSCJORNADAVENID = " + objDatos.getIdJornada() + " "
				+ "                                OR JORNADA_RESPONSABLE = " + objDatos.getIdJornada() + ")) "
				+ "            cant_VENTAS_BRUTAS, "
				+ "         (SELECT NVL (SUM (round(MONTO_PAGADO * TASA_CAMBIO,2)), 0) * -1 "
				+ "            FROM TC_sC_VENTA PARTITION (" + ABR_PAIS + ")" + "           WHERE     ESTADO IN ("
				+ estadosAnulados + ") " + "                 AND cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    TCSCJORNADAVENID = " + objDatos.getIdJornada() + " "
				+ "                                OR JORNADA_RESPONSABLE = " + objDatos.getIdJornada() + ")) "
				+ "            DEVOLUCIONES, " + "         (SELECT COUNT (tcscventaid) "
				+ "            FROM TC_sC_VENTA PARTITION (" + ABR_PAIS + ")" + "           WHERE     ESTADO IN ("
				+ estadosAnulados + ")" + "                 AND cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    TCSCJORNADAVENID = " + objDatos.getIdJornada()
				+ "                                OR JORNADA_RESPONSABLE = " + objDatos.getIdJornada() + ")) "
				+ "            cant_DEVOLUCIONES, "
				+ "         (SELECT NVL (SUM (round(descuentos* TASA_CAMBIO,2)), 0) "
				+ "            FROM tc_SC_venta PARTITION (" + ABR_PAIS + ")" + "           WHERE     descuentos <> 0 "
				+ "                 AND cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    TCSCJORNADAVENID = " + objDatos.getIdJornada() + " "
				+ "                                OR JORNADA_RESPONSABLE = " + objDatos.getIdJornada() + ")) "
				+ "            descuentos, " + "         (SELECT COUNT (tcscventaid) "
				+ "            FROM tc_SC_venta PARTITION (" + ABR_PAIS + ")" + "           WHERE     descuentos <> 0 "
				+ "                 AND cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    TCSCJORNADAVENID = " + objDatos.getIdJornada() + " "
				+ "                                OR JORNADA_RESPONSABLE = " + objDatos.getIdJornada() + ")) "
				+ "            cantidad_descuentos, " + "         (SELECT (SUM (round(MONTO_PAGADO * TASA_CAMBIO,2)) ) "
				+ "            FROM TC_SC_VENTA PARTITION (" + ABR_PAIS + ")"
				+ "           WHERE     EXENTO = 'NO EXENTO' "
				+ "                 AND cod_dispositivo = V.cod_dispositivo " + "				  AND  ESTADO NOT IN ("
				+ estadosAnulados + ") " + "                 AND TCSCJORNADAVENID IN "
				+ "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    TCSCJORNADAVENID = " + objDatos.getIdJornada() + " "
				+ "                                OR JORNADA_RESPONSABLE = " + objDatos.getIdJornada()
				+ "))TOTAL_GRAVADO " + "            , "
				+ "         (SELECT (SUM (round(MONTO_PAGADO * TASA_CAMBIO,2)) + SUM (round(DESCUENTOS * TASA_CAMBIO,2))) "
				+ "            FROM TC_SC_VENTA PARTITION (" + ABR_PAIS + ")"
				+ "           WHERE     EXENTO = 'EXENTO' "
				+ "                 AND cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    TCSCJORNADAVENID = " + objDatos.getIdJornada() + " "
				+ "                                OR JORNADA_RESPONSABLE = " + objDatos.getIdJornada() + ")) "
				+ "            TOTAL_EXENTO " + "    FROM TC_SC_VENTA PARTITION (" + ABR_PAIS + ") V, "
				+ "tc_sc_jornada_Vend  PARTITION (" + ABR_PAIS + ") j "
				+ "   WHERE     v.tcscjornadavenid = j.tcscjornadavenid " + "         AND v.fecha_emision >= j.fecha "
				+ "         AND v.fecha_emision <= j.fecha_finalizacion " + "    and j.tcscjornadavenid IN "
				+ "                (SELECT TCSCJORNADAVENID " + "                   FROM TC_SC_JORNADA_VEND PARTITION ("
				+ ABR_PAIS + ")" + "                  WHERE TCSCJORNADAVENID =" + objDatos.getIdJornada()
				+ " OR JORNADA_RESPONSABLE =" + objDatos.getIdJornada() + ") " + "    and v.cod_dispositivo='"
				+ dispositivo + "' " + "        group by v.cod_dispositivo ";

		return query1;
	}

	/**
	 * M\u00E9todo para registrar en la bdd de SIDRA el reporte X/Z
	 *
	 * @param conn
	 * @param objeto
	 * @param fecha
	 * @return
	 * @throws SQLException
	 */
	public static BigDecimal insertReporte(Connection conn, ReporteXZ objeto, String dispositivo, String codArea,
			BigDecimal ID_PAIS) throws SQLException {
		BigDecimal ret = null;
		PreparedStatement pstmt = null;
		String estadoBaja = "";
		String estadoAlta = "";
		String tipoDoc = "";
		ReporteXZ encabezado;

		String tipoDocumento = "";
		if (codArea.equals("503"))
			//tipoDocumento = Conf.DOCUMENTO_TCK;
			tipoDocumento = "FCF";
		else if (codArea.equals("507"))
			tipoDocumento = Conf.DOCUMENTO_FFP;

		String insert = "INSERT INTO TC_SC_REPORTE_XZ (tcscreportexzid, "
				+ "                              tcscjornadavenid, " + "                              cod_dispositivo, "
				+ "                              vendedor, " + "                              fecha, "
				+ "                              acumulado_ventas, "
				+ "                              cant_ventas_brutas, "
				+ "                              monto_ventas_brutas, "
				+ "                              monto_fondos_iniciales, "
				+ "                              cant_entrega_parcial, "
				+ "                              monto_entrega_parcial, "
				+ "                              cant_pagos_terceros, "
				+ "                              monto_pagos_terceros, "
				+ "                              monto_dinero_gaveta, "
				+ "                              cant_descuentos, " + "                              monto_descuentos, "
				+ "                              cant_anulaciones, "
				+ "                              monto_anulaciones, " + "                              cant_no_ventas, "
				+ "                              monto_no_ventas, "
				+ "                              cant_pago_efectivo, "
				+ "                              monto_efectivo, " + "                              cant_pago_cheque, "
				+ "                              monto_cheque, " + "                              cant_pago_credito, "
				+ "                              monto_credito, " + "                               "
				+ "                               " + "                              total_gravado, "
				+ "                              total_exento, " + "                              total_no_sujeto, "
				+ "                              creado_el, " + "                              creado_por, "
				+ "                              tipo_reporte, " + "                              tcsccatpaisid, "
				+ "                              resolucion, " + "                              fecha_resolucion, "
				+ "                              zona, " + "                              caja, "
				+ "                              terminal, " + "                              rango_inicial, "
				+ "                              rango_final, " + "                              rango_ini_usado, "
				+ "                              rango_fin_usado, " + "cant_pago_tarjeta," + " monto_tarjeta"
				+ ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, codArea);
			estadoBaja = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, codArea);
			tipoDoc = UtileriasJava.getConfig(conn, Conf.GRUPO_TIPOS_DOCUMENTO_VENTA, tipoDocumento, codArea);

			// obteniendo encabezado
			encabezado = getEncabezado(conn, objeto.getTcscjornadavenid() + "", dispositivo, estadoAlta, estadoBaja,
					tipoDoc, codArea, ID_PAIS);

			if (encabezado == null) {
				ret = new BigDecimal(Conf_Mensajes.MSJ_NO_HAY_ENCABEZAD_XZ_925);
			} else {
				log.trace("insert ReporteXZ:" + insert);
				log.trace("FECHA:" + objeto.getFecha());
				pstmt = conn.prepareStatement(insert);
				ret = JavaUtils.getSecuencia(conn, ReporteXZ.SEQUENCE);
				pstmt.setBigDecimal(1, ret);
				pstmt.setBigDecimal(2, objeto.getTcscjornadavenid());
				pstmt.setString(3, objeto.getCod_dispositivo());
				pstmt.setBigDecimal(4, objeto.getVendedor());
				pstmt.setTimestamp(5, objeto.getFecha());
				pstmt.setBigDecimal(6, objeto.getAcumulado_ventas());
				pstmt.setBigDecimal(7, objeto.getCant_ventas_brutas());
				pstmt.setBigDecimal(8, objeto.getMonto_ventas_brutas());
				pstmt.setBigDecimal(9, objeto.getMonto_fondos_iniciales());
				pstmt.setBigDecimal(10, objeto.getCant_entrega_parcial());
				pstmt.setBigDecimal(11, objeto.getMonto_entrega_parcial());
				pstmt.setBigDecimal(12, objeto.getCant_pagos_terceros());
				pstmt.setBigDecimal(13, objeto.getMonto_pagos_terceros());
				pstmt.setBigDecimal(14, objeto.getMonto_dinero_gaveta());
				pstmt.setBigDecimal(15, objeto.getCant_descuentos());
				pstmt.setBigDecimal(16, objeto.getMonto_descuentos());
				pstmt.setBigDecimal(17, objeto.getCant_anulaciones());
				pstmt.setBigDecimal(18, objeto.getMonto_anulaciones());
				pstmt.setBigDecimal(19, objeto.getCant_no_ventas());
				pstmt.setBigDecimal(20, objeto.getMonto_no_ventas());
				pstmt.setBigDecimal(21, objeto.getCant_pago_efectivo());
				pstmt.setBigDecimal(22, objeto.getMonto_efectivo());
				pstmt.setBigDecimal(23, objeto.getCant_pago_cheque());
				pstmt.setBigDecimal(24, objeto.getMonto_cheque());
				pstmt.setBigDecimal(25, objeto.getCant_pago_credito());
				pstmt.setBigDecimal(26, objeto.getMonto_credito());
				//pstmt.setBigDecimal(27, objeto.getCant_pago_mpos());
				//pstmt.setBigDecimal(28, objeto.getMonto_mpos());
				pstmt.setBigDecimal(27, objeto.getTotal_gravado());
				pstmt.setBigDecimal(28, objeto.getTotal_exento());
				pstmt.setBigDecimal(29, objeto.getTotal_no_sujeto());
				pstmt.setString(30, objeto.getCreado_por());
				pstmt.setString(31, objeto.getTipo_reporte());
				pstmt.setBigDecimal(32, ID_PAIS);
				pstmt.setString(33, encabezado.getResolucion());
				pstmt.setTimestamp(34, encabezado.getFecha_resolucion());
				pstmt.setString(35, encabezado.getZona());
				pstmt.setString(36, encabezado.getCaja());
				pstmt.setBigDecimal(37, encabezado.getTerminal());
				pstmt.setBigDecimal(38, encabezado.getRango_inicial());
				pstmt.setBigDecimal(39, encabezado.getRango_final());
				pstmt.setBigDecimal(40, encabezado.getRango_ini_usado());
				pstmt.setBigDecimal(41, encabezado.getRango_fin_usado());
				pstmt.setBigDecimal(42, objeto.getCant_pago_tarjeta());
				pstmt.setBigDecimal(43, objeto.getMonto_tarjeta());
				int res = pstmt.executeUpdate();
				if (res != 1) {
					ret = new BigDecimal(0);
				}
			}
		} finally {
			if (pstmt != null) {
				DbUtils.closeQuietly(pstmt);
			}
		}
		return ret;
	}

	public static Reporte_XZ getReporteXZ(Connection conn, List<Filtro> filtros, BigDecimal ID_PAIS, String codArea)
			throws SQLException {
		Reporte_XZ respuestaReporte = new Reporte_XZ();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		EncabezadoXZ objEncabezado = new EncabezadoXZ();
		EncabezadoXZ objpie = new EncabezadoXZ();
		SimpleDateFormat FORMATO_FECHAHORA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHAHORA);
		String query = queryReporteX(filtros, codArea);
		log.trace("query obtener reporte X:" + query);

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, ID_PAIS);
			pstmt.setBigDecimal(2, ID_PAIS);
			pstmt.setBigDecimal(3, ID_PAIS);
			pstmt.setBigDecimal(4, ID_PAIS);
			pstmt.setBigDecimal(5, ID_PAIS);
			pstmt.setBigDecimal(6, ID_PAIS);
			pstmt.setBigDecimal(7, ID_PAIS);
			pstmt.setBigDecimal(8, ID_PAIS);
			pstmt.setBigDecimal(9, ID_PAIS);
			pstmt.setBigDecimal(10, ID_PAIS);
			pstmt.setBigDecimal(11, ID_PAIS);
			pstmt.setBigDecimal(12, ID_PAIS);
			pstmt.setBigDecimal(13, ID_PAIS);
			pstmt.setBigDecimal(14, ID_PAIS);
			pstmt.setBigDecimal(15, ID_PAIS);
			pstmt.setBigDecimal(16, ID_PAIS);
			pstmt.setBigDecimal(17, ID_PAIS);
			//pstmt.setBigDecimal(18, ID_PAIS);
			//pstmt.setBigDecimal(19, ID_PAIS);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				respuestaReporte.setIdReporte(rst.getString("TCSCREPORTEXZID"));
				respuestaReporte.setTipo(rst.getString("TIPO_REPORTE"));
				respuestaReporte.setFecha(FORMATO_FECHAHORA.format(rst.getTimestamp("FECHA")));
				respuestaReporte.setAcumuladoVentas(rst.getString("ACUMULADO_VENTAS"));
				respuestaReporte.setDispositivo(rst.getString("COD_DISPOSITIVO"));
				// seteandoDesgloceVentas
				DesgloseVentas objDesgloseVentas = new DesgloseVentas();
				List<DesgloseVentas> lstDesgloseVentas = new ArrayList<DesgloseVentas>();

				// VENTASBRUTAS
				objDesgloseVentas = new DesgloseVentas();
				objDesgloseVentas.setDescripcion(rst.getString("DESCRIPCION_VENTAS_BRUTAS"));
				objDesgloseVentas.setCantVentas(rst.getString("CANT_VENTAS_BRUTAS"));
				objDesgloseVentas.setMonto(rst.getString("MONTO_VENTAS_BRUTAS"));
				lstDesgloseVentas.add(objDesgloseVentas);

				// DEVOLUCIONES
				objDesgloseVentas = new DesgloseVentas();
				objDesgloseVentas.setDescripcion(rst.getString("DESCRIPCION_DEVOLUCIONES"));
				objDesgloseVentas.setCantVentas(rst.getString("CANT_DEVOLUCIONES"));
				objDesgloseVentas.setMonto(rst.getString("MONTO_DEVOLUCIONES"));
				lstDesgloseVentas.add(objDesgloseVentas);

				respuestaReporte.setVentas(lstDesgloseVentas);

				// TOTAL VENTAS BRUTAS
				Double totalVentasNetas = 0.0;
				totalVentasNetas = rst.getBigDecimal("MONTO_VENTAS_BRUTAS").doubleValue()
						+ rst.getBigDecimal("MONTO_DEVOLUCIONES").doubleValue();

				respuestaReporte.setTotalVentasNetas("" + totalVentasNetas);

				// TransaccionesMonetarias
				List<DesgloseVentas> lstTransacMonetarias = new ArrayList<DesgloseVentas>();

				// fondosIniciales
				objDesgloseVentas = new DesgloseVentas();
				objDesgloseVentas.setDescripcion(rst.getString("DESCRIPCION_FONDOS_INI"));
				objDesgloseVentas.setMonto(rst.getString("MONTO_FONDOS_INICIALES"));
				lstTransacMonetarias.add(objDesgloseVentas);

				// DineroGaveta
				objDesgloseVentas = new DesgloseVentas();
				objDesgloseVentas.setDescripcion(rst.getString("DESCRIPCION_DINERO_GAVETA"));
				objDesgloseVentas.setMonto(rst.getString("MONTO_DINERO_GAVETA"));
				lstTransacMonetarias.add(objDesgloseVentas);

				// EntregaParcial
				objDesgloseVentas = new DesgloseVentas();
				objDesgloseVentas.setDescripcion(rst.getString("DESCRIPCION_ENTREGA_PARCIAL"));
				objDesgloseVentas.setCantVentas(rst.getString("CANT_ENTREGA_PARCIAL"));
				objDesgloseVentas.setMonto(rst.getString("MONTO_ENTREGA_PARCIAL"));
				lstTransacMonetarias.add(objDesgloseVentas);

				// PagosTerceros
				objDesgloseVentas = new DesgloseVentas();
				objDesgloseVentas.setDescripcion(rst.getString("DESCRIPCION_PAGOS_TERCEROS"));
				objDesgloseVentas.setCantVentas(rst.getString("CANT_PAGOS_TERCEROS"));
				objDesgloseVentas.setMonto(rst.getString("MONTO_PAGOS_TERCEROS"));
				lstTransacMonetarias.add(objDesgloseVentas);

				respuestaReporte.setTransaccionesMonetarias(lstTransacMonetarias);

				// otrasTransacciones
				List<DesgloseVentas> lstOtrasTransac = new ArrayList<DesgloseVentas>();

				// Descuentos
				objDesgloseVentas = new DesgloseVentas();
				objDesgloseVentas.setDescripcion(rst.getString("DESCRIPCION_DESCUENTOS"));
				objDesgloseVentas.setCantVentas(rst.getString("CANT_DESCUENTOS"));
				objDesgloseVentas.setMonto(rst.getString("MONTO_DESCUENTOS"));
				lstOtrasTransac.add(objDesgloseVentas);

				// Anulaciones
				objDesgloseVentas = new DesgloseVentas();
				objDesgloseVentas.setDescripcion(rst.getString("DESCRIPCION_ANULACIONES"));
				objDesgloseVentas.setCantVentas(rst.getString("CANT_ANULACIONES"));
				objDesgloseVentas.setMonto(rst.getString("MONTO_ANULACIONES"));
				lstOtrasTransac.add(objDesgloseVentas);

				// No Venta
				objDesgloseVentas = new DesgloseVentas();
				objDesgloseVentas.setDescripcion(rst.getString("DESCRIPCION_NO_VENTAS"));
				objDesgloseVentas.setCantVentas(rst.getString("CANT_NO_VENTAS"));
				objDesgloseVentas.setMonto(rst.getString("MONTO_NO_VENTAS"));
				lstOtrasTransac.add(objDesgloseVentas);

				respuestaReporte.setOtrasTransacciones(lstOtrasTransac);

				// Formas Pago
				List<FormasPago> lstFormasPago = new ArrayList<FormasPago>();
				FormasPago objFormaPago = new FormasPago();
				// Efectivo
				objFormaPago.setFormaPago(rst.getString("DESCRIPCION_EFECTIVO"));
				objFormaPago.setCantidad(rst.getString("CANT_PAGO_EFECTIVO"));
				objFormaPago.setMonto(rst.getString("MONTO_EFECTIVO"));
				lstFormasPago.add(objFormaPago);

				// Cheque
				objFormaPago = new FormasPago();
				objFormaPago.setFormaPago(rst.getString("DESCRIPCION_CHEQUE"));
				objFormaPago.setCantidad(rst.getString("CANT_PAGO_CHEQUE"));
				objFormaPago.setMonto(rst.getString("MONTO_CHEQUE"));
				lstFormasPago.add(objFormaPago);

				// Crédito
				objFormaPago = new FormasPago();
				objFormaPago.setFormaPago(rst.getString("DESCRIPCION_CREDITO"));
				objFormaPago.setCantidad(rst.getString("CANT_PAGO_CREDITO"));
				objFormaPago.setMonto(rst.getString("MONTO_CREDITO"));
				lstFormasPago.add(objFormaPago);
				
				// MPOS
//				objFormaPago = new FormasPago();
//				objFormaPago.setFormaPago(rst.getString("DESCRIPCION_MPOS"));
//				objFormaPago.setCantidad(rst.getString("CANT_PAGO_MPOS"));
//				objFormaPago.setMonto(rst.getString("MONTO_MPOS"));
//				lstFormasPago.add(objFormaPago);

				// TARJETA
				objFormaPago = new FormasPago();
				objFormaPago.setFormaPago(rst.getString("DESCRIPCION_TARJETA"));
				objFormaPago.setCantidad(rst.getString("CANT_PAGO_TARJETA"));
				objFormaPago.setMonto(rst.getString("MONTO_TARJETA"));
				lstFormasPago.add(objFormaPago);

				respuestaReporte.setFormasPago(lstFormasPago);

				// TOTALES
				List<TotalesVenta> lstTotal = new ArrayList<TotalesVenta>();
				TotalesVenta objTotal = new TotalesVenta();

				// totalGravado
				objTotal.setDescripcion(rst.getString("DESCRIPCION_TOTAL_GRAVADO"));
				objTotal.setMonto(rst.getString("TOTAL_GRAVADO"));
				lstTotal.add(objTotal);

				// totalExento
				objTotal = new TotalesVenta();
				objTotal.setDescripcion(rst.getString("DESCRIPCION_TOTAL_EXENTO"));
				objTotal.setMonto(rst.getString("TOTAL_EXENTO"));
				lstTotal.add(objTotal);

				// totalNoSujeto
				objTotal = new TotalesVenta();
				objTotal.setDescripcion(rst.getString("DESCRIPCION_NO_SUJETO"));
				objTotal.setMonto(rst.getString("TOTAL_NO_SUJETO"));
				lstTotal.add(objTotal);

				respuestaReporte.setTotales(lstTotal);

				Double totalVenta = 0.0;

				totalVenta = rst.getBigDecimal("TOTAL_NO_SUJETO").doubleValue()
						+ rst.getBigDecimal("TOTAL_EXENTO").doubleValue()
						+ rst.getBigDecimal("TOTAL_GRAVADO").doubleValue();

				if (rst.getString("CAJA") == null || "".equals(rst.getString("CAJA"))) {
					objEncabezado.setCaja("");
				} else {
					objEncabezado.setCaja(rst.getString("CAJA"));
				}

				if (rst.getString(ReporteXZ.CAMPO_TERMINAL) == null
						|| "".equals(rst.getString(ReporteXZ.CAMPO_TERMINAL))) {
					objEncabezado.setTerminal("");
				} else {
					objEncabezado.setTerminal(rst.getString(ReporteXZ.CAMPO_TERMINAL));
				}

				if (rst.getString(ReporteXZ.CAMPO_ZONA) == null || "".equals(rst.getString(ReporteXZ.CAMPO_ZONA))) {
					objEncabezado.setZona("");
				} else {
					objEncabezado.setZona(rst.getString(ReporteXZ.CAMPO_ZONA));
				}

				if (rst.getString(ReporteXZ.CAMPO_RANGO_FINAL) == null
						|| "".equals(rst.getString(ReporteXZ.CAMPO_RANGO_FINAL))) {
					objEncabezado.setRangoFinal("");
				} else {
					objEncabezado.setRangoFinal(rst.getString(ReporteXZ.CAMPO_RANGO_FINAL));
				}

				if (rst.getString(ReporteXZ.CAMPO_RANGO_INICIAL) == null
						|| "".equals(rst.getString(ReporteXZ.CAMPO_RANGO_INICIAL))) {
					objEncabezado.setRangoInicial("");
				} else {
					objEncabezado.setRangoInicial(rst.getString(ReporteXZ.CAMPO_RANGO_INICIAL));
				}

				if (rst.getString(ReporteXZ.CAMPO_RESOLUCION) == null
						|| "".equals(rst.getString(ReporteXZ.CAMPO_RESOLUCION))) {
					objEncabezado.setResolucion("");
				} else {
					objEncabezado.setResolucion(rst.getString(ReporteXZ.CAMPO_RESOLUCION));
				}

				if (rst.getTimestamp("FECHA_RESOLUCION") == null) {
					objEncabezado.setFechaResolucion("");
				} else {
					objEncabezado.setFechaResolucion(FORMATO_FECHAHORA.format(rst.getTimestamp("FECHA_RESOLUCION")));
				}

				respuestaReporte.setEncabezado(objEncabezado);

				if (rst.getString(ReporteXZ.CAMPO_RANGO_INI_USADO) == null
						|| "".equals(rst.getString(ReporteXZ.CAMPO_RANGO_INI_USADO))) {
					objpie.setRangoInicial("");
				} else {
					objpie.setRangoInicial(rst.getString(ReporteXZ.CAMPO_RANGO_INI_USADO));
				}

				if (rst.getString(ReporteXZ.CAMPO_RANGO_FIN_USADO) == null
						|| "".equals(rst.getString(ReporteXZ.CAMPO_RANGO_FIN_USADO))) {
					objpie.setRangoFinal("");
				} else {
					objpie.setRangoFinal(rst.getString(ReporteXZ.CAMPO_RANGO_FIN_USADO));
				}

				respuestaReporte.setPie(objpie);
				respuestaReporte.setTotalVenta(totalVenta + "");
			} else {
				respuestaReporte = null;
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return respuestaReporte;
	}

	public static String queryReporteX(List<Filtro> filtros, String codArea) {
		String query = "SELECT R.TCSCREPORTEXZID, " + "       R.TIPO_REPORTE, " + "       R.TCSCJORNADAVENID, "
				+ "       R.COD_DISPOSITIVO, " + "       R.FECHA, " + "       R.VENDEDOR, " + " R.RESOLUCION, "
				+ " R.FECHA_RESOLUCION, " + " R.RANGO_INICIAL, " + " R.RANGO_FINAL, " + " R.CAJA, " + " R.TERMINAL, "
				+ " R.ZONA, " + " R.RANGO_INI_USADO, " + " R.RANGO_FIN_USADO, " + "       (SELECT VALOR "
				+ "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'ACUMULADO_VENTAS' AND grupo = 'REPORTE_XZ' " + "AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_ACUMULADO, " + "       nvl(R.ACUMULADO_VENTAS,0)ACUMULADO_VENTAS, "
				+ "       (SELECT VALOR " + "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'MONTO_VENTAS_BRUTAS' AND grupo = 'REPORTE_XZ' " + "AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_VENTAS_BRUTAS, " + "       nvl(R.CANT_VENTAS_BRUTAS,0)CANT_VENTAS_BRUTAS, "
				+ "       nvl(R.MONTO_VENTAS_BRUTAS,0)MONTO_VENTAS_BRUTAS, " + "       (SELECT VALOR "
				+ "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'MONTO_DEVOLUCIONES' AND grupo = 'REPORTE_XZ' " + "AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_DEVOLUCIONES, " + "       nvl(R.CANT_ANULACIONES,0) CANT_DEVOLUCIONES, "
				+ "       nvl(R.MONTO_ANULACIONES ,0)MONTO_DEVOLUCIONES, " + "       (SELECT VALOR "
				+ "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'MONTO_FONDOS_INICIALES' AND grupo = 'REPORTE_XZ' " + "AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_FONDOS_INI, "
				+ "      nvl( R.MONTO_FONDOS_INICIALES,0)MONTO_FONDOS_INICIALES, " + "       (SELECT VALOR "
				+ "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'MONTO_ENTREGA_PARCIAL' AND grupo = 'REPORTE_XZ' " + "AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_ENTREGA_PARCIAL, "
				+ "       nvl(R.CANT_ENTREGA_PARCIAL,0)CANT_ENTREGA_PARCIAL, "
				+ "       nvl(R.MONTO_ENTREGA_PARCIAL,0)MONTO_ENTREGA_PARCIAL, " + "       (SELECT VALOR "
				+ "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'MONTO_PAGOS_TERCEROS' AND grupo = 'REPORTE_XZ' " + "AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_PAGOS_TERCEROS, " + "       nvl(R.CANT_PAGOS_TERCEROS,0)CANT_PAGOS_TERCEROS, "
				+ "       nvl(R.MONTO_PAGOS_TERCEROS,0)MONTO_PAGOS_TERCEROS, " + "       (SELECT VALOR "
				+ "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'MONTO_DINERO_GAVETA' AND grupo = 'REPORTE_XZ' " + "AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_DINERO_GAVETA, " + "       nvl(R.MONTO_DINERO_GAVETA,0)MONTO_DINERO_GAVETA, "
				+ "       (SELECT VALOR " + "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'MONTO_DESCUENTOS' AND grupo = 'REPORTE_XZ' " + " AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_DESCUENTOS, " + "       nvl(R.CANT_DESCUENTOS,0)CANT_DESCUENTOS, "
				+ "       nvl(R.MONTO_DESCUENTOS,0)MONTO_DESCUENTOS, " + "       (SELECT VALOR "
				+ "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'MONTO_ANULACIONES' AND grupo = 'REPORTE_XZ' " + "AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_ANULACIONES, " + "       nvl(R.CANT_ANULACIONES,0)CANT_ANULACIONES, "
				+ "       nvl(R.MONTO_ANULACIONES,0)MONTO_ANULACIONES, " + "       (SELECT VALOR "
				+ "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'MONTO_NO_VENTAS' AND grupo = 'REPORTE_XZ' " + "AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_NO_VENTAS, " + "       nvl(R.CANT_NO_VENTAS,0)CANT_NO_VENTAS, "
				+ "       nvl(R.MONTO_NO_VENTAS,0)MONTO_NO_VENTAS, " + "       (SELECT VALOR "
				+ "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'MONTO_EFECTIVO' AND grupo = 'REPORTE_XZ' " + "AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_EFECTIVO, " + "       nvl(R.CANT_PAGO_EFECTIVO,0)CANT_PAGO_EFECTIVO, "
				+ "       nvl(R.MONTO_EFECTIVO,0)MONTO_EFECTIVO, " + "       (SELECT VALOR "
				+ "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'MONTO_CHEQUE' AND grupo = 'REPORTE_XZ' " + "AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_CHEQUE, " + "       nvl(R.CANT_PAGO_CHEQUE,0)CANT_PAGO_CHEQUE, "
				+ "      NVL(R.MONTO_CHEQUE,0) MONTO_CHEQUE, " + "       (SELECT VALOR "
				+ "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'MONTO_CREDITO' AND grupo = 'REPORTE_XZ' " + "AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_CREDITO, " + "       nvl(R.CANT_PAGO_CREDITO,0)CANT_PAGO_CREDITO, "
				+ "      NVL(R.MONTO_CREDITO,0) MONTO_CREDITO, " + "          (SELECT VALOR "
				+ "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'MONTO_TARJETA' AND grupo = 'REPORTE_XZ' " + "AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_TARJETA, " + "       nvl(R.CANT_PAGO_TARJETA,0)CANT_PAGO_TARJETA, "
				+ "      NVL(R.MONTO_TARJETA,0) MONTO_TARJETA, " + "       (SELECT VALOR "
				+ "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'TOTAL_GRAVADO' AND grupo = 'REPORTE_XZ' " + "AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_TOTAL_GRAVADO, " + "        NVL(R.TOTAL_GRAVADO,0) TOTAL_GRAVADO, "
				+ "       (SELECT VALOR " + "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'TOTAL_EXENTO' AND grupo = 'REPORTE_XZ' " + " AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_TOTAL_EXENTO, " + "      NVL(R.TOTAL_EXENTO,0) TOTAL_EXENTO, "
				+ "       (SELECT VALOR " + "          FROM tc_Sc_configuracion "
				+ "         WHERE nombre = 'TOTAL_NO_SUJETO' AND grupo = 'REPORTE_XZ' " + " AND TCSCCATPAISID=?) "
				+ "          DESCRIPCION_NO_SUJETO, " + "       nvl(R.TOTAL_NO_SUJETO,0)TOTAL_NO_SUJETO "
				+ "  FROM TC_sC_REPORTE_XZ PARTITION(" + ControladorBase.getPais(codArea) + ") R WHERE 1=1";

		// obteniendo filtros para consultas
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

	/**
	 * Verificando que la jornada enviada para el reporte X se encuentre en estado
	 * iniciada
	 *
	 * @param conn
	 * @param estado
	 * @param idJornada
	 * @return
	 * @throws SQLException
	 */
	public static BigDecimal verificaJornada(Connection conn, String estado, String idJornada, String codArea)
			throws SQLException {
		BigDecimal respuesta = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT (1)  FROM TC_sC_JORNADA_VEND PARTITION (");
		query.append(ControladorBase.getPais(codArea));
		query.append(") WHERE TCSCJORNADAVENID = ? AND upper(ESTADO) =? ");

		try {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setBigDecimal(1, new BigDecimal(idJornada));
			pstmt.setString(2, estado.toUpperCase());
			rst = pstmt.executeQuery();

			if (rst.next()) {
				respuesta = rst.getBigDecimal(1);
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return respuesta;
	}

	/**
	 * Método para verificar que las jornadas que puede manejar un dispositivo hayan
	 * sido finalizadas para poder generar el reporte Z
	 *
	 * @param conn
	 * @param estado
	 * @param idJornada
	 * @param fecha
	 * @return
	 * @throws SQLException
	 */
	public static List<BigDecimal> getJornadaFin(Connection conn, String estado, String idJornada, String fecha,
			String codArea) throws SQLException {
		List<BigDecimal> lstJornada = new ArrayList<BigDecimal>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = queryJornadaFin(estado, idJornada, fecha, codArea);
		log.trace("query para obtener jornadas finalizadas:" + query);
		try {
			pstmt = conn.prepareStatement(query);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				lstJornada.add(0, rst.getBigDecimal(1)); // cantidad de jornadas finalizadas
				lstJornada.add(1, rst.getBigDecimal(2)); // total de jornadas que podia utilizar el dispositivo

			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return lstJornada;
	}

	public static String queryJornadaFin(String estado, String idJornada, String fecha, String codArea) {
		String query = "SELECT ((SELECT COUNT (1) " + "          FROM tc_Sc_jornada_vend PARTITION("
				+ ControladorBase.getPais(codArea) + ")" + "         WHERE     jornada_responsable =" + idJornada
				+ "               AND estado = '" + estado + "' " + "               AND TRUNC (fecha_finalizacion) = "
				+ "                      TO_DATE ('" + fecha + "', 'dd/MM/yyyy') " + "        )+ "
				+ "        (SELECT COUNT (1) " + "          FROM tc_Sc_jornada_vend PARTITION("
				+ ControladorBase.getPais(codArea) + ")" + "         WHERE     tcscjornadavenid =" + idJornada
				+ "               AND estado = '" + estado + "' " + "               AND TRUNC (fecha_finalizacion) = "
				+ "                      TO_DATE ('" + fecha + "', 'dd/MM/yyyy'))) " + "          JORNADAS_FIN, "
				+ "       (  (SELECT COUNT (1) " + "             FROM tc_Sc_jornada_vend PARTITION("
				+ ControladorBase.getPais(codArea) + ")" + "            WHERE jornada_responsable =" + idJornada + ") "
				+ "        + 1) " + "          TOTAL_JORNADAS " + "  FROM DUAL ";
		return query;
	}

	/***
	 * M\u00E9todo para obtener datos de reporte Z a generar
	 *
	 * @param conn
	 * @param filtros
	 * @param objDatos
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public static List<ReporteXZ> getDatosReporteZMensual(Connection conn, InputReporteXZ objDatos, String fecha1,
			String fecha2, String estadoFin, String estadoLiq) throws SQLException, ParseException {
		ReporteXZ respuesta = new ReporteXZ();
		List<ReporteXZ> lstRespuesta = new ArrayList<ReporteXZ>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		PreparedStatement pstmt2 = null;
		ResultSet rst2 = null;
		String query1 = "";
		String query2 = "";
		String efectivo = "";
		String tarjeta = "";
		String cheque = "";
		String credito = "";
		String mpos = "";
		String estadosAnulados = "";
		String abrPais = ControladorBase.getPais(objDatos.getCodArea());
		// obteniendo Formas de Pago
		efectivo = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_EFECTIVO,
				objDatos.getCodArea());
		tarjeta = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_TARJETA,
				objDatos.getCodArea());
		cheque = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_CHEQUE,
				objDatos.getCodArea());
		//mpos = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_MPOS, objDatos.getCodArea());
		estadosAnulados = UtileriasJava.getConfig(conn, Conf.ESTADOS_REPORTE_XZ, Conf.ESTADOS_REPORTE_XZ,
				objDatos.getCodArea());
		if (Country.SV.getCodArea().equals(Integer.valueOf(objDatos.getCodArea()))) {
			credito = "Credito";
		} else {

			credito = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_CREDITO,
					objDatos.getCodArea());
		}

		log.trace("IdJornada en armar Registro:" + objDatos.getIdJornada());

		for (int a = 0; a < objDatos.getDispositivos().length; a++) {
			query1 = reporteZmensual(estadosAnulados, objDatos.getDispositivos()[a], fecha1, fecha2, estadoFin,
					estadoLiq, abrPais);
			log.trace("Query para obtener reportex:" + query1);

			try {
				pstmt = conn.prepareStatement(query1);
				rst = pstmt.executeQuery();

				if (rst.next()) {
					respuesta = new ReporteXZ();
					log.trace("encontro datos iniciales de reporte, Dispositivo:" + objDatos.getDispositivos()[a]);
					Double dineroGaveta = 0.0;
					Date fecha;
					SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
					fecha = UtileriasJava.parseDate(objDatos.getFecha(), formatoFecha);

					// datos que se obtuvieron desde el input

					respuesta.setTipo_reporte(objDatos.getTipoReporte().toUpperCase());
					respuesta.setCod_dispositivo(objDatos.getDispositivos()[a]);
					respuesta.setFecha(new Timestamp(fecha.getTime()));
					respuesta.setCreado_por(objDatos.getUsuario());
					// ------------------------------------------
					respuesta.setAcumulado_ventas(rst.getBigDecimal("ACUMULADO_VENTAS"));
					respuesta.setMonto_ventas_brutas(rst.getBigDecimal("VENTAS_BRUTAS"));
					respuesta.setCant_ventas_brutas(rst.getBigDecimal("cant_VENTAS_BRUTAS"));
					respuesta.setMonto_anulaciones(rst.getBigDecimal("DEVOLUCIONES"));
					respuesta.setCant_anulaciones(rst.getBigDecimal("cant_DEVOLUCIONES"));
					respuesta.setMonto_descuentos(rst.getBigDecimal("descuentos"));
					respuesta.setCant_descuentos(rst.getBigDecimal("cantidad_descuentos"));
					respuesta.setTotal_exento(rst.getBigDecimal("TOTAL_EXENTO"));
					respuesta.setTotal_gravado(rst.getBigDecimal("TOTAL_GRAVADO"));

					dineroGaveta = (respuesta.getMonto_ventas_brutas().doubleValue()
							+ respuesta.getMonto_anulaciones().doubleValue());
					respuesta.setMonto_dinero_gaveta(new BigDecimal(dineroGaveta));

					respuesta.setMonto_fondos_iniciales(new BigDecimal(0));
					respuesta.setMonto_entrega_parcial(new BigDecimal(0));
					respuesta.setCant_entrega_parcial(new BigDecimal(0));
					respuesta.setCant_pagos_terceros(new BigDecimal(0));
					respuesta.setMonto_pagos_terceros(new BigDecimal(0));
					respuesta.setMonto_no_ventas(new BigDecimal(0));
					respuesta.setCant_no_ventas(new BigDecimal(0));
					respuesta.setTotal_no_sujeto(new BigDecimal(0));

					query2 = reportezmensual2(estadosAnulados, objDatos.getDispositivos()[a], fecha1, fecha2, estadoFin,
							estadoLiq, objDatos.getCodArea());
					log.trace("Query para obtener forma de pago reporte x:" + query2);

					try {
						pstmt2 = conn.prepareStatement(query2);
						rst2 = pstmt2.executeQuery();

						while (rst2.next()) {
							if (rst2.getString("FORMAPAGO").equalsIgnoreCase(efectivo)) {
								respuesta.setMonto_efectivo(rst2.getBigDecimal("MONTO"));
								respuesta.setCant_pago_efectivo(rst2.getBigDecimal("CANTIDAD"));
							}

							if (rst2.getString("FORMAPAGO").equalsIgnoreCase(tarjeta)) {
								respuesta.setMonto_tarjeta(rst2.getBigDecimal("MONTO"));
								respuesta.setCant_pago_tarjeta(rst2.getBigDecimal("CANTIDAD"));
							}
							if (rst2.getString("FORMAPAGO").equalsIgnoreCase(cheque)) {
								respuesta.setMonto_cheque(rst2.getBigDecimal("MONTO"));
								respuesta.setCant_pago_cheque(rst2.getBigDecimal("CANTIDAD"));
							}
							if (rst2.getString("FORMAPAGO").equalsIgnoreCase(credito)) {
								respuesta.setMonto_credito(rst2.getBigDecimal("MONTO"));
								respuesta.setCant_pago_credito(rst2.getBigDecimal("CANTIDAD"));
							}
//							if (rst2.getString("FORMAPAGO").equalsIgnoreCase(mpos)) {
//								respuesta.setMonto_mpos(rst2.getBigDecimal("MONTO"));
//								respuesta.setCant_pago_mpos(rst2.getBigDecimal("CANTIDAD"));
//							}

						}
					} finally {
						DbUtils.closeQuietly(rst2);
						DbUtils.closeQuietly(pstmt2);
					}

					lstRespuesta.add(respuesta);
				}
			} finally {
				DbUtils.closeQuietly(rst);
				DbUtils.closeQuietly(pstmt);
			}
		}
		return lstRespuesta;
	}

	public static String reportezmensual2(String estadosAnulados, String dispositivo, String fecha1, String fecha2,
			String estadoFin, String estadoLiq, String codArea) {
		String query2 = "  SELECT P.FORMAPAGO, SUM (round(P.MONTO * V.TASA_CAMBIO,2)) MONTO, COUNT (P.FORMAPAGO) CANTIDAD "
				+ "    FROM TC_SC_DET_PAGO P, TC_sC_VENTA  PARTITION (" + ControladorBase.getPais(codArea) + ") V,"
				+ " tc_sc_jornada_Vend PARTITION (" + ControladorBase.getPais(codArea) + ") j "
				+ "   WHERE     V.TCSCVENTAID = P.TCSCVENTAID " + "  	 AND  V.ESTADO NOT IN (" + estadosAnulados
				+ ") " + "         AND v.tcscjornadavenid = j.tcscjornadavenid "
				+ "                        AND TRUNC (j.fecha) >=TO_DATE('" + fecha1 + "', 'dd/MM/yyyy') "
				+ "                        AND TRUNC (j.fecha) <=TO_DATE('" + fecha2 + "', 'dd/MM/yyyy') "
				+ "                        AND J.estado = '" + estadoFin + "' "
				+ "                        AND J.estado_liquidacion = '" + estadoLiq + "'"
				+ "         AND v.cod_dispositivo = '" + dispositivo + "' " + "GROUP BY P.FORMAPAGO ";

		return query2;
	}

	public static String reporteZmensual(String estadosAnulados, String dispositivo, String fecha1, String fecha2,
			String estadoFin, String estadoLiq, String ABR_PAIS) {

		String query1 = "SELECT v.cod_dispositivo, " + "        nvl( (SELECT SUM (round(MONTO_PAGADO * TASA_CAMBIO,2)) "
				+ "            FROM TC_sC_VENTA PARTITION (" + ABR_PAIS + ")" + "           WHERE     ESTADO NOT IN ("
				+ estadosAnulados + ") " + "                 AND cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    cod_dispositivo =  V.cod_dispositivo "
				+ "                        AND TRUNC (fecha) >=TO_DATE('" + fecha1 + "', 'dd/MM/yyyy') "
				+ "                        AND TRUNC (fecha) <= TO_DATE('" + fecha2 + "', 'dd/MM/yyyy') "
				+ "                        AND estado = '" + estadoFin + "' "
				+ "                        AND estado_liquidacion = '" + estadoLiq + "' )),0) "
				+ "            AS ACUMULADO_VENTAS, " + "         (SELECT SUM (round(MONTO_PAGADO * TASA_CAMBIO,2)) "
				+ "            FROM TC_sC_VENTA PARTITION (" + ABR_PAIS + ")"
				+ "           WHERE     cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    cod_dispositivo =  V.cod_dispositivo "
				+ "                        AND TRUNC (fecha) >= TO_DATE('" + fecha1 + "', 'dd/MM/yyyy') "
				+ "                        AND TRUNC (fecha) <= TO_DATE('" + fecha2 + "', 'dd/MM/yyyy') "
				+ "                        AND estado = '" + estadoFin + "' "
				+ "                        AND estado_liquidacion = '" + estadoLiq + "' )) "
				+ "            AS VENTAS_BRUTAS, " + "         (SELECT COUNT (tcscventaid) "
				+ "            FROM TC_sC_VENTA PARTITION (" + ABR_PAIS + ")"
				+ "           WHERE     cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    cod_dispositivo =  V.cod_dispositivo "
				+ "                        AND TRUNC (fecha) >= TO_DATE('" + fecha1 + "', 'dd/MM/yyyy') "
				+ "                        AND TRUNC (fecha) <= TO_DATE('" + fecha2 + "', 'dd/MM/yyyy') "
				+ "                        AND estado = '" + estadoFin + "' "
				+ "                        AND estado_liquidacion = '" + estadoLiq + "' )) "
				+ "            cant_VENTAS_BRUTAS, "
				+ "         (SELECT NVL (SUM (round(MONTO_PAGADO * TASA_CAMBIO,2)), 0) * -1 "
				+ "            FROM TC_sC_VENTA PARTITION (" + ABR_PAIS + ")" + "           WHERE     ESTADO IN ("
				+ estadosAnulados + ") " + "                 AND cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    cod_dispositivo =  V.cod_dispositivo "
				+ "                        AND TRUNC (fecha) >= TO_DATE('" + fecha1 + "', 'dd/MM/yyyy') "
				+ "                        AND TRUNC (fecha) <= TO_DATE('" + fecha2 + "', 'dd/MM/yyyy') "
				+ "                        AND estado = '" + estadoFin + "' "
				+ "                        AND estado_liquidacion = '" + estadoLiq + "' )) "
				+ "            DEVOLUCIONES, " + "         (SELECT COUNT (tcscventaid) "
				+ "            FROM TC_sC_VENTA PARTITION (" + ABR_PAIS + ")" + "           WHERE     ESTADO IN ("
				+ estadosAnulados + ") " + "                 AND cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    cod_dispositivo =  V.cod_dispositivo "
				+ "                        AND TRUNC (fecha) >= TO_DATE('" + fecha1 + "', 'dd/MM/yyyy') "
				+ "                        AND TRUNC (fecha) <= TO_DATE('" + fecha2 + "', 'dd/MM/yyyy') "
				+ "                        AND estado = '" + estadoFin + "' "
				+ "                        AND estado_liquidacion = '" + estadoLiq + "' )) "
				+ "            cant_DEVOLUCIONES, "
				+ "         (SELECT NVL (SUM (round(descuentos* TASA_CAMBIO,2)), 0) "
				+ "            FROM tc_SC_venta PARTITION (" + ABR_PAIS + ")" + "           WHERE     descuentos <> 0 "
				+ "                 AND cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    cod_dispositivo =  V.cod_dispositivo "
				+ "                        AND TRUNC (fecha) >= TO_DATE('" + fecha1 + "', 'dd/MM/yyyy') "
				+ "                        AND TRUNC (fecha) <= TO_DATE('" + fecha2 + "', 'dd/MM/yyyy') "
				+ "                        AND estado = '" + estadoFin + "' "
				+ "                        AND estado_liquidacion = '" + estadoLiq + "' )) "
				+ "            descuentos, " + "         (SELECT COUNT (tcscventaid) "
				+ "            FROM tc_SC_venta PARTITION (" + ABR_PAIS + ")" + "           WHERE     descuentos <> 0 "
				+ "                 AND cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    cod_dispositivo =  V.cod_dispositivo "
				+ "                        AND TRUNC (fecha) >= TO_DATE('" + fecha1 + "', 'dd/MM/yyyy') "
				+ "                        AND TRUNC (fecha) <= TO_DATE('" + fecha2 + "', 'dd/MM/yyyy') "
				+ "                        AND estado = '" + estadoFin + "' "
				+ "                        AND estado_liquidacion = '" + estadoLiq + "' )) "
				+ "            cantidad_descuentos, " + "         (SELECT (SUM (round(MONTO_PAGADO * TASA_CAMBIO,2))) "
				+ "            FROM TC_SC_VENTA PARTITION (" + ABR_PAIS + ")"
				+ "           WHERE     EXENTO = 'NO EXENTO' " + "				  AND ESTADO NOT IN (" + estadosAnulados
				+ ") " + "                 AND cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    cod_dispositivo =  V.cod_dispositivo "
				+ "                        AND TRUNC (fecha) >= TO_DATE('" + fecha1 + "', 'dd/MM/yyyy') "
				+ "                        AND TRUNC (fecha) <= TO_DATE('" + fecha2 + "', 'dd/MM/yyyy') "
				+ "                        AND estado = '" + estadoFin + "' "
				+ "                        AND estado_liquidacion = '" + estadoLiq + "' )) "
				+ "            TOTAL_GRAVADO, "
				+ "         (SELECT (SUM (round(MONTO_PAGADO * TASA_CAMBIO,2)) + SUM (round(DESCUENTOS * TASA_CAMBIO,2))) "
				+ "            FROM TC_SC_VENTA PARTITION (" + ABR_PAIS + ")"
				+ "           WHERE     EXENTO = 'EXENTO' "
				+ "                 AND cod_dispositivo = V.cod_dispositivo "
				+ "                 AND TCSCJORNADAVENID IN " + "                        (SELECT TCSCJORNADAVENID "
				+ "                           FROM TC_SC_JORNADA_VEND PARTITION (" + ABR_PAIS + ")"
				+ "                          WHERE    cod_dispositivo =  V.cod_dispositivo "
				+ "                        AND TRUNC (fecha) >= TO_DATE('" + fecha1 + "', 'dd/MM/yyyy') "
				+ "                        AND TRUNC (fecha) <= TO_DATE('" + fecha2 + "', 'dd/MM/yyyy') "
				+ "                        AND estado = '" + estadoFin + "' "
				+ "                        AND estado_liquidacion = '" + estadoLiq + "' )) "
				+ "            TOTAL_EXENTO " + "    FROM TC_SC_VENTA PARTITION (" + ABR_PAIS + ") V, "
				+ "tc_sc_jornada_Vend  PARTITION (" + ABR_PAIS + ") j "
				+ "   WHERE     v.tcscjornadavenid = j.tcscjornadavenid " + "         AND v.fecha_emision >= j.fecha "
				+ "         AND v.fecha_emision <= j.fecha_finalizacion " + "    and j.tcscjornadavenid IN "
				+ "                (SELECT TCSCJORNADAVENID " + "                   FROM TC_SC_JORNADA_VEND PARTITION ("
				+ ABR_PAIS + ")" + "                          WHERE    cod_dispositivo =  V.cod_dispositivo "
				+ "                        AND TRUNC (fecha) >=TO_DATE('" + fecha1 + "', 'dd/MM/yyyy') "
				+ "                        AND TRUNC (fecha) <=TO_DATE('" + fecha2 + "', 'dd/MM/yyyy') "
				+ "                        AND estado = '" + estadoFin + "' "
				+ "                        AND estado_liquidacion = '" + estadoLiq + "' ) " +

				"    and v.cod_dispositivo='" + dispositivo + "' " + "        group by v.cod_dispositivo ";
		return query1;
	}

	/**
	 * M\u00E9todo para verificar que las jornadas que puede manejar un dispositivo
	 * hayan sido finalizadas para poder generar el reporte Z
	 *
	 * @param conn
	 * @param estado
	 * @param idJornada
	 * @param fecha
	 * @return
	 * @throws SQLException
	 */

	public static List<BigDecimal> getJornadaFinZM(Connection conn, String estado, String codigo_dispositivo,
			String fecha, String codArea) throws SQLException {
		List<BigDecimal> lstJornada = new ArrayList<BigDecimal>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String query = queryZFIN(estado, codigo_dispositivo, fecha, codArea);
		log.trace("query para obtener jornadas finalizadas:" + query);
		try {
			pstmt = conn.prepareStatement(query);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				lstJornada.add(0, rst.getBigDecimal(1)); // cantidad de jornadas finalizadas
				lstJornada.add(1, rst.getBigDecimal(2)); // total de jornadas que podia utilizar el dispositivo

			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}

		return lstJornada;
	}

	public static String queryZFIN(String estado, String codigo_dispositivo, String fecha, String codArea) {
		String query = "SELECT ((SELECT COUNT (1) " + "          FROM tc_Sc_jornada_vend PARTITION("
				+ ControladorBase.getPais(codArea) + ")" + "         WHERE     jornada_responsable ="
				+ codigo_dispositivo + "               AND estado = '" + estado + "' "
				+ "               AND TRUNC (fecha_finalizacion) = " + "                      TO_DATE ('" + fecha
				+ "', 'dd/MM/yyyy') " + "        )+ " + "        (SELECT COUNT (1) "
				+ "          FROM tc_Sc_jornada_vend PARTITION(" + ControladorBase.getPais(codArea) + ")"
				+ "         WHERE     tcscjornadavenid =" + codigo_dispositivo + "               AND estado = '"
				+ estado + "' " + "               AND TRUNC (fecha_finalizacion) = "
				+ "                      TO_DATE ('" + fecha + "', 'dd/MM/yyyy'))) " + "          JORNADAS_FIN, "
				+ "       (  (SELECT COUNT (1) " + "             FROM tc_Sc_jornada_vend PARTITION("
				+ ControladorBase.getPais(codArea) + ")" + "            WHERE jornada_responsable ="
				+ codigo_dispositivo + ") " + "        + 1) " + "          TOTAL_JORNADAS " + "  FROM DUAL ";
		return query;
	}

	/**
	 * Método para obtener los datos de encabezado reporte X y Z
	 *
	 * @return index 0: encabezado index 1: pie
	 * @throws SQLException
	 */
	public static ReporteXZ getEncabezado(Connection conn, String jornada, String dispositivo, String estadoAlta,
			String estadoBaja, String tipoDoc, String codArea, BigDecimal ID_PAIS) throws SQLException {
		ReporteXZ objEncabezado = new ReporteXZ();
		StringBuilder query = new StringBuilder();
		String condicion = "";
		PreparedStatement pstm = null;
		ResultSet rst = null;

		if (!"".equals(jornada)) {
			condicion = " and tcscjornadavenid = " + jornada;
		}

		query.append("SELECT (SELECT MIN (numero) ");
		query.append("FROM TC_SC_VENTA PARTITION(" + ControladorBase.getPais(codArea) + ")");
		query.append("         WHERE cod_dispositivo='" + dispositivo + "' AND tipo_documento = '" + tipoDoc + "' ");
		query.append("			" + condicion + " ) ");
		query.append("          Inicio, ");
		query.append("       (SELECT MAX (numero) ");
		query.append("          FROM TC_SC_VENTA PARTITION(" + ControladorBase.getPais(codArea) + ")");
		query.append("         WHERE cod_dispositivo='" + dispositivo + "' AND tipo_documento = '" + tipoDoc + "'");
		query.append("			" + condicion + " ) ");
		query.append("          Fin, ");
		query.append("       A.* ");
		query.append("  FROM (  SELECT D.TCSCDISPOSITIVOID, ");
		query.append("                 D.CODIGO_DISPOSITIVO, ");
		query.append("                 D.CAJA_NUMERO, ");
		query.append("                 D.ZONA, ");
		query.append("                 D.RESOLUCION, ");
		query.append("                 D.FECHA_RESOLUCION, ");
		query.append("                 f.no_inicialfolio, ");
		query.append("                 f.no_finalfolio, ");
		query.append("                 MAX (f.modificado_el), ");
		query.append("                 f.estado ");
		query.append("            FROM TC_SC_FOLIO f, TC_SC_DISPOSITIVO d ");
		query.append("           WHERE     f.idtipo = d.codigo_dispositivo ");
		query.append("                 AND UPPER (F.TIPODOCUMENTO) = '" + tipoDoc + "' ");
		query.append("                 AND UPPER (f.estado) NOT IN ('" + estadoBaja + "') ");
		query.append(" AND D.CODIGO_DISPOSITIVO='" + dispositivo + "' ");
		query.append("                 AND f.tcsccatpaisid =" + ID_PAIS);
		query.append("                 AND d.estado = '" + estadoAlta + "' ");
		query.append("                 AND f.tcsccatpaisid = d.tcsccatpaisid ");
		query.append("        GROUP BY (D.TCSCDISPOSITIVOID, ");
		query.append("                  D.CODIGO_DISPOSITIVO, ");
		query.append("                  D.CAJA_NUMERO, ");
		query.append("                  D.ZONA, ");
		query.append("                  D.RESOLUCION, ");
		query.append("                  D.FECHA_RESOLUCION, ");
		query.append("                  f.no_inicialfolio, ");
		query.append("                  f.no_finalfolio, ");
		query.append("                  f.estado)) A ");
		log.trace("query para obtener encabezado de reporte:" + query.toString());

		try {
			pstm = conn.prepareStatement(query.toString());
			rst = pstm.executeQuery();

			if (rst.next()) {
				objEncabezado.setCaja(rst.getString("CAJA_NUMERO"));
				objEncabezado.setTerminal(rst.getBigDecimal("TCSCDISPOSITIVOID"));
				objEncabezado.setZona(rst.getString(ReporteXZ.CAMPO_ZONA));
				objEncabezado.setRango_final(rst.getBigDecimal("no_finalfolio"));
				objEncabezado.setRango_inicial(rst.getBigDecimal("no_inicialfolio"));
				objEncabezado.setResolucion(rst.getString(ReporteXZ.CAMPO_RESOLUCION));
				objEncabezado.setFecha_resolucion((rst.getTimestamp("FECHA_RESOLUCION")));
				objEncabezado.setRango_ini_usado(rst.getBigDecimal("Inicio"));
				objEncabezado.setRango_fin_usado(rst.getBigDecimal("Fin"));
				return objEncabezado;

			} else {
				return null;
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstm);
		}

	}
}
