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

import com.consystec.sc.ca.ws.input.cuenta.InputCuenta;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.cuenta.OutputCuenta;
import com.consystec.sc.ca.ws.output.cuenta.OutputDtsCuenta;
import com.consystec.sc.sv.ws.metodos.CtrlCuenta;
import com.consystec.sc.sv.ws.orm.Cuenta;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class OperacionCuenta {
	private OperacionCuenta() {
	}

	private static final Logger log = Logger.getLogger(OperacionCuenta.class);

	/**
	 * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
	 * 
	 * @param conn
	 * @param input
	 * @param metodo
	 * @param estadoBaja
	 * @param estadoAlta
	 * @return OutputCuenta
	 * @throws SQLException
	 */
	public static OutputCuenta doGet(Connection conn, InputCuenta input, int metodo, BigDecimal idPais)
			throws SQLException {
		String nombreMetodo = "doGet";
		String nombreClase = new CurrentClassGetter().getClassName();

		List<InputCuenta> list = new ArrayList<InputCuenta>();
		Respuesta respuesta = new Respuesta();
		OutputCuenta output = new OutputCuenta();
		OutputDtsCuenta dtsCuenta = new OutputDtsCuenta();

		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String sql = null;
		List<Filtro> condiciones = new ArrayList<Filtro>();

		try {
			if (input.getBancosAsociados() != null && input.getBancosAsociados().equals("1")) {
				// se muestran \u00FAnicamente los bancos con cuentas asociadas

				condiciones.add(new Filtro(Cuenta.CAMPO_TCSCCATPAISID, Filtro.EQ,
						idPais)); /** TODO confirmar funcionamiento de este filtro */

				List<Order> orden = new ArrayList<Order>();
				orden.add(new Order(Cuenta.CAMPO_BANCO, Order.ASC));
				List<String> listBancos = UtileriasBD.getOneField(conn,
						UtileriasJava.setSelect(Conf.SELECT_DISTINCT, Cuenta.CAMPO_BANCO), Cuenta.N_TABLA, condiciones,
						orden);

				if (listBancos.size() > 0) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					for (int i = 0; i < listBancos.size(); i++) {
						InputCuenta banco = new InputCuenta();
						banco.setBanco(listBancos.get(i));
						list.add(banco);
					}

					output.setBancos(list);
				} else {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_CUENTAS_817, null,
							nombreClase, nombreMetodo, null, input.getCodArea());
				}

				output.setRespuesta(respuesta);

			} else {
				// se muestran los datos completos de las cuentas
				String tablas[] = { Cuenta.N_TABLA, Distribuidor.N_TABLA };

				String[][] campos = CtrlCuenta.obtenerCamposGet();

				condiciones.clear();
				condiciones = CtrlCuenta.obtenerCondiciones(input, metodo, idPais);
				condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.N_TABLA,
						Distribuidor.CAMPO_TC_SC_DTS_ID, Cuenta.N_TABLA, Cuenta.CAMPO_TCSCDTSID));
				sql = UtileriasBD.armarQueryGetMultiple(campos, tablas, condiciones, null);

				pstmt = conn.prepareStatement(sql);
				rst = pstmt.executeQuery();

				if (rst != null) {
					if (!rst.next()) {
						respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_CUENTAS_707, null,
								nombreClase, nombreMetodo, null, input.getCodArea());

						output.setRespuesta(respuesta);
					} else {
						if (input.getToken() != null && input.getToken().equalsIgnoreCase("WEB")) {
							respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
									nombreClase, nombreMetodo, null, input.getCodArea());
						} else {
							respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_GETDATOS12, null, nombreClase,
									nombreMetodo, null, input.getCodArea());
						}

						do {
							dtsCuenta.setIdDts(rst.getString(Cuenta.CAMPO_TCSCDTSID));
							dtsCuenta.setNombre(rst.getString(Distribuidor.CAMPO_NOMBRES));
							InputCuenta item = new InputCuenta();
							item.setIdCuenta(rst.getBigDecimal(Cuenta.CAMPO_TCSCCTABANCARIAID).toString());
							item.setBanco(rst.getString(Cuenta.CAMPO_BANCO));
							item.setNoCuenta(rst.getString(Cuenta.CAMPO_NO_CUENTA));
							item.setTipoCuenta(rst.getString(Cuenta.CAMPO_TIPO_CUENTA));
							item.setNombreCuenta(rst.getString(Cuenta.CAMPO_NOMBRE_CUENTA));
							item.setEstado(rst.getString(Cuenta.CAMPO_ESTADO));
							item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(Cuenta.CAMPO_CREADO_EL)));
							item.setCreado_por(rst.getString(Cuenta.CAMPO_CREADO_POR));
							item.setModificado_el(
									UtileriasJava.formatStringDate(rst.getString(Cuenta.CAMPO_MODIFICADO_EL)));
							item.setModificado_por(rst.getString(Cuenta.CAMPO_MODIFICADO_POR));

							list.add(item);
						} while (rst.next());
						dtsCuenta.setCuentas(list);
						output = new OutputCuenta();
						output.setRespuesta(respuesta);
						output.setDistribuidores(dtsCuenta);
					}
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
	 * @param estadoBaja
	 * @param estadoAlta
	 * @return OutputCuenta
	 * @throws SQLException
	 */
	public static OutputCuenta doPost(Connection conn, InputCuenta input, String estadoAlta, BigDecimal idPais)
			throws SQLException {
		String nombreMetodo = "doPost";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta = null;
		OutputCuenta output = null;

		List<Filtro> condicionesExistencia = CtrlCuenta.obtenerCondicionesExistencia(input, Conf.METODO_POST,
				estadoAlta, idPais);

		String existencia = UtileriasBD.verificarExistencia(conn, Cuenta.N_TABLA, condicionesExistencia);
		if (new Integer(existencia) > 0) {
			log.error("Ya existe el recurso.");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_EXISTE_NOCUENTA_SIDRA_708, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			output = new OutputCuenta();
			output.setRespuesta(respuesta);

			return output;
		}

		// validando que el id de distribuidor enviado exista
		condicionesExistencia = new ArrayList<Filtro>();
		condicionesExistencia
				.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Cuenta.CAMPO_TCSCDTSID, input.getIdDts()));

		String existeDTS = UtileriasBD.verificarExistencia(conn, Distribuidor.N_TABLA, condicionesExistencia);

		if (new Integer(existeDTS) == 0) {
			log.error("El distribuidor enviado no existe.");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_IDDTS_NO_EXISTE_173, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			output = new OutputCuenta();
			output.setRespuesta(respuesta);

			return output;
		}

		String sql = null;
		String campos[] = CtrlCuenta.obtenerCamposGetPost();
		List<String> inserts = CtrlCuenta.obtenerInsertsPost(input, Cuenta.SEQUENCE, estadoAlta, idPais);
		int idPadre = 0;
		sql = UtileriasBD.armarQueryInsert(Cuenta.N_TABLA, campos, inserts);
		try {
			String generatedColumns[] = { Cuenta.CAMPO_TCSCCTABANCARIAID };
			try (PreparedStatement pstmt = conn.prepareStatement(sql, generatedColumns);) {
				pstmt.executeUpdate();
				try (ResultSet rs = pstmt.getGeneratedKeys();) {
					if (rs.next()) {
						idPadre = rs.getInt(1);
					}
				}
			}
			log.debug("idPadre: " + idPadre);
			if (idPadre > 0) {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_CUENTA_38, null, nombreClase,
						nombreMetodo, null, input.getCodArea());

				output = new OutputCuenta();
				output.setIdCuenta(idPadre + "");
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
	 * @param estadoBaja
	 * @param estadoAlta
	 * @return OutputCuenta
	 * @throws SQLException
	 */
	public static OutputCuenta doPut(Connection conn, InputCuenta input, int metodo, String estadoAlta,
			BigDecimal idPais) throws SQLException {
		String nombreMetodo = "doPut";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta = null;
		OutputCuenta output = null;

		List<Filtro> condicionesExistencia = CtrlCuenta.obtenerCondicionesExistencia(input, Conf.METODO_DELETE,
				estadoAlta, idPais);
		String sql = null;

		try {
			String existencia = UtileriasBD.verificarExistencia(conn, Cuenta.N_TABLA, condicionesExistencia);
			if (new Integer(existencia) < 1) {
				log.error("No existe el recurso.");
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
						nombreMetodo, null, input.getCodArea());

				output = new OutputCuenta();
				output.setRespuesta(respuesta);

				return output;
			} else {
				if (metodo == Conf.METODO_PUT) {
					condicionesExistencia.clear();
					condicionesExistencia = CtrlCuenta.obtenerCondicionesExistencia(input, metodo, estadoAlta, idPais);
					condicionesExistencia.get(0).setOperator(Filtro.NEQ);

					existencia = UtileriasBD.verificarExistencia(conn, Cuenta.N_TABLA, condicionesExistencia);
					if (new Integer(existencia) > 0) {
						log.error("Ya existe otra cuenta igual.");
						respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_EXISTE_NOCUENTA_SIDRA_708, null,
								nombreClase, nombreMetodo, null, input.getCodArea());

						output = new OutputCuenta();
						output.setRespuesta(respuesta);

						return output;
					}
				}
			}

			String campos[][] = CtrlCuenta.obtenerCamposPut(input);
			List<Filtro> condiciones = CtrlCuenta.obtenerCondiciones(input, metodo, idPais);

			sql = UtileriasBD.armarQueryUpdate(Cuenta.N_TABLA, campos, condiciones);

			int i = 0;
			conn.setAutoCommit(false);
			QueryRunner Qr = new QueryRunner();
			i = Qr.update(conn, sql);

			if (i > 0) {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_MOD_CUENTA_39, null, nombreClase,
						nombreMetodo, null, input.getCodArea());

				output = new OutputCuenta();
				output.setRespuesta(respuesta);

				conn.commit();
			} else {
				respuesta = new Respuesta();

				output = new OutputCuenta();
				output.setRespuesta(respuesta);
			}
		} finally {
			conn.setAutoCommit(true);
		}

		return output;
	}
}
