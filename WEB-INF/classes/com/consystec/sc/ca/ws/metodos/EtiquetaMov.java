package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.general.InputConsultaWeb;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.etiquetamov.OutputEtiquetaMov;
import com.consystec.sc.ca.ws.output.etiquetamov.RListEtiquetas;
import com.consystec.sc.ca.ws.output.etiquetamov.RListPantalla;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;

public class EtiquetaMov extends ControladorBase {

	private static final Logger log = Logger.getLogger(EtiquetaMov.class);

	/***
	 * Validando que no vengan parametros nulos
	 */
	public Respuesta validarDatos(InputConsultaWeb objDatos) {
		Connection conn = null;
		Respuesta objRespuesta = null;
		String metodo = "validarDatos";

		if (objDatos.getCodArea() == null || "".equals(objDatos.getCodArea())) {
			objRespuesta = new Respuesta();
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODAREA_NULO_1, null, this.getClass().toString(), metodo, null,
					Conf_Mensajes.ORIGEN_SERVICIOS_OP);

		} else if (objDatos.getCodArea().length() != Conf.LONG_CODPAIS) {
			objRespuesta = new Respuesta();
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONGINVALIDA_AREA_6, null, this.getClass().toString(), metodo,
					null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
		} else {
			BigDecimal idPais = null;
			try {
				conn = getConnLocal();
				idPais = getidpais(conn, objDatos.getCodArea());
				if (idPais == null || idPais.intValue() == 0) {
					objRespuesta = new Respuesta();
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_AREAINVALIDA_8, null, this.getClass().toString(),
							metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				}
			} catch (SQLException e) {
				log.error(e, e);
				objRespuesta = new Respuesta();
				objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
						Conf_Mensajes.ORIGEN_SERVICIOS_OP);
			} finally {
				DbUtils.closeQuietly(conn);
			}
		}

		if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario())) {
			objRespuesta = new Respuesta();
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
					Conf_Mensajes.ORIGEN_SERVICIOS_OP);
		}

		return objRespuesta;
	}

	/**
	 * M\u00E9todo para obtener etiquetas por pantalla y pais
	 ***/
	public OutputEtiquetaMov obtenerEtiquetasBD(BigDecimal pais) {
		Connection conn = null;
		String metodo = "obtenerEtiquetasBD";
		String query = "";
		String query2 = "";
		// PreparedStatement pstmt = null;
		// ResultSet rst = null;
		// PreparedStatement pstmt2 = null;
		// ResultSet rst2 = null;
		int cont = 0;
		Respuesta mensajeAdv = new Respuesta();
		OutputEtiquetaMov respuesta = new OutputEtiquetaMov();
		// se crea una lista tipo Pantalla para almacenar el arreglo de
		// etiquetas que corresponde a cada pantalla

		List<RListPantalla> LstPantalla = new ArrayList<RListPantalla>();
		List<RListEtiquetas> LstEtiquetas = new ArrayList<RListEtiquetas>();

		boolean existenDatos = false;

		try {
			conn = getConnLocal();
			/* consulta para obtener las pantallas de la app */
			query2 = "SELECT distinct TCSCCATPAISID_AREA, " + "       TCSCMOVPANTALLAID, "
					+ "       TCSCMOVPANTALLAID_VAL " + "       "
					+ "  FROM TC_SC_MOV_ETIQUETA_VW WHERE TCSCCATPAISID_AREA=?";

			try (PreparedStatement pstmt2 = conn.prepareStatement(query2);) {

				pstmt2.setBigDecimal(1, pais);
				try (ResultSet rst2 = pstmt2.executeQuery()) {
					while (rst2.next()) {
						existenDatos = true;

						/* Consulta para obtener etiquetas de pantallas */
						query = "SELECT " + "       TCSCNOMETIQUETAMOVID, " + "       TCSCNOMETIQUETAMOVID_VAL, "
								+ "       TCSCVALETIQUETAMOVID, " + "       TCSCVALETIQUETAMOVID_VAL, "
								+ "       ORDEN, " + "       MOSTRAR, " + "      nvl(OBLIGATORIO,0) obligatorio "
								+ "  FROM TC_SC_MOV_ETIQUETA_VW " + " WHERE TCSCCATPAISID_AREA = ? "
								+ " AND TCSCMOVPANTALLAID=? ";
						try (PreparedStatement pstmt = conn.prepareStatement(query);) {
							pstmt.setBigDecimal(1, pais);
							pstmt.setBigDecimal(2, rst2.getBigDecimal("TCSCMOVPANTALLAID"));

							try (ResultSet rst = pstmt.executeQuery();) {
								LstEtiquetas = new ArrayList<>();
								while (rst.next()) {
									// cada registro encontrado sera almacenado en la lista tipo
									// estadoLote
									RListEtiquetas obj = new RListEtiquetas(rst.getString("TCSCNOMETIQUETAMOVID"),
											rst.getString("TCSCNOMETIQUETAMOVID_VAL"),
											rst.getString("TCSCVALETIQUETAMOVID"),
											rst.getString("TCSCVALETIQUETAMOVID_VAL"), rst.getString("ORDEN"),
											rst.getString("MOSTRAR"), rst.getString("OBLIGATORIO"));

									LstEtiquetas.add(obj);
									cont++;
								}
							}

						} finally {
							RListPantalla objPantalla = new RListPantalla(rst2.getString("TCSCCATPAISID_AREA"),
									rst2.getString("TCSCMOVPANTALLAID"), rst2.getString("TCSCMOVPANTALLAID_VAL"),
									LstEtiquetas);
							LstPantalla.add(objPantalla);
						}
					}
				}

			}

			System.out.println("CANTIDAD DE ESTADOS ENCONTRADOS:" + cont);
			System.out.println("EXISTEN DATOS:" + existenDatos);

			if (existenDatos) {
				// si se encontraron datos se arma el objeto de respuesta
				mensajeAdv = getMensaje(Conf_Mensajes.OK_ACTUALIZACION2, null, this.getClass().getName(), metodo, null,
						Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				respuesta.setRespuesta(mensajeAdv);
				respuesta.setPantallas(LstPantalla);
				return respuesta;
			} else {
				mensajeAdv = getMensaje(Conf_Mensajes.MSJ_DATOSNOENCONTRADOS_10, null, this.getClass().getName(),
						metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				respuesta.setRespuesta(mensajeAdv);
				return respuesta;
			}

		} catch (SQLException e) {
			log.error(e, e);
			mensajeAdv = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().getName(), metodo, null,
					Conf_Mensajes.ORIGEN_SERVICIOS_OP);
			respuesta.setRespuesta(mensajeAdv);
			return respuesta;
		} catch (Exception e) {
			log.error(e, e);
			mensajeAdv = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
					null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
			respuesta.setRespuesta(mensajeAdv);
			return respuesta;
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	/**
	 * M\u00E9todo para obtener etiquetas
	 */
	public OutputEtiquetaMov getEtiquetas(InputConsultaWeb objDatos) {
		OutputEtiquetaMov respuestaLabel = new OutputEtiquetaMov();
		Respuesta respuesta;

		respuesta = validarDatos(objDatos);
		if (respuesta == null) {
			respuestaLabel = obtenerEtiquetasBD(new BigDecimal(objDatos.getCodArea()));
		} else {
			respuestaLabel.setRespuesta(respuesta);
		}
		return respuestaLabel;
	}
}
