package com.consystec.sc.sv.ws.operaciones;

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

import com.consystec.sc.ca.ws.input.cliente.InputCliente;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.cliente.OutputCliente;
import com.consystec.sc.sv.ws.metodos.CtrlCliente;
import com.consystec.sc.sv.ws.orm.Cliente;
import com.consystec.sc.sv.ws.orm.Distribuidor;
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
public class OperacionCliente {
	private OperacionCliente() {
	}

	private static final Logger log = Logger.getLogger(OperacionCliente.class);

	/**
	 * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
	 * 
	 * @param conn
	 * 
	 * @param input
	 * @param metodo
	 * @return OutputCliente
	 * @throws SQLException
	 */
	public static OutputCliente doGet(Connection conn, InputCliente input, int metodo) throws SQLException {
		String nombreMetodo = "doGet";
		String nombreClase = new CurrentClassGetter().getClassName();

		List<InputCliente> list = new ArrayList<InputCliente>();

		Respuesta respuesta = null;
		OutputCliente output = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		String sql = null;
		String campos[] = CtrlCliente.obtenerCamposGetPost(metodo);

		List<Filtro> condiciones = CtrlCliente.obtenerCondiciones(input, metodo);
		try {
			sql = UtileriasBD.armarQuerySelect(Cliente.N_TABLA, campos, condiciones);
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst != null) {
				if (!rst.next()) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSOS, null, nombreClase,
							nombreMetodo, null, input.getCodArea());

					output = new OutputCliente();
					output.setRespuesta(respuesta);
				} else {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
							nombreClase, nombreMetodo, null, input.getCodArea());

					do {
						InputCliente item = new InputCliente();
						item.setIdCliente(rst.getString(Cliente.CAMPO_TC_SC_CLIENTE_ID));
						item.setNombres(rst.getString(Cliente.CAMPO_NOMBRE));
						item.setApellidos(rst.getString(Cliente.CAMPO_APELLIDO));
						item.setNit(rst.getString(Cliente.CAMPO_NIT));
						item.setDocIdentificacion(rst.getString(Cliente.CAMPO_DOCUMENTO_IDENTIFICACION));
						item.setEstado(rst.getString(Cliente.CAMPO_ESTADO));
						item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(Cliente.CAMPO_CREADO_EL)));
						item.setCreado_por(rst.getString(Cliente.CAMPO_CREADO_POR));
						item.setModificado_el(
								UtileriasJava.formatStringDate(rst.getString(Cliente.CAMPO_MODIFICADO_EL)));
						item.setModificado_por(rst.getString(Cliente.CAMPO_MODIFICADO_POR));

						list.add(item);
					} while (rst.next());

					output = new OutputCliente();
					output.setRespuesta(respuesta);
					output.setCliente(list);
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
	 * @return OutputCliente
	 * @throws SQLException
	 */
	public static OutputCliente doPost(Connection conn, InputCliente input) throws SQLException {
		String nombreMetodo = "doPost";
		String nombreClase = new CurrentClassGetter().getClassName();

		ResultSet rs = null;

		Respuesta respuesta = null;
		OutputCliente output = null;

		List<Filtro> condicionesExistencia = CtrlCliente.obtenerCondicionesExistencia(conn, input, Conf.METODO_POST);

		String existencia = UtileriasBD.verificarExistencia(conn, Cliente.N_TABLA, condicionesExistencia);
		if (new Integer(existencia) > 0) {
			log.error("Ya existe el recurso.");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_EXISTENTE, null, nombreClase,
					nombreMetodo, "Ya se ha ingresado un cliente con ese número de identificaci\u00FAn.",
					input.getCodArea());

			output = new OutputCliente();
			output.setRespuesta(respuesta);

			return output;
		}
		;

		String campos[] = CtrlCliente.obtenerCamposGetPost(Conf.METODO_POST);
		List<String> inserts = CtrlCliente.obtenerInsertsPost(input, Cliente.SEQUENCE);
		int idPadre = 0;
		String sql = UtileriasBD.armarQueryInsert(Cliente.N_TABLA, campos, inserts);
		try {

			String generatedColumns[] = { Cliente.CAMPO_TC_SC_CLIENTE_ID };
			try (PreparedStatement pstmt = conn.prepareStatement(sql, generatedColumns);) {
				pstmt.executeUpdate();
				rs = pstmt.getGeneratedKeys();

				if (rs.next()) {
					idPadre = rs.getInt(1);
				}
			} finally {
				DbUtils.closeQuietly(rs);
			}
			log.debug("idPadre: " + idPadre);
			if (idPadre > 0) {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_CREADO, null, nombreClase,
						nombreMetodo, null, input.getCodArea());

				output = new OutputCliente();
				output.setIdCliente(idPadre + "");
				output.setRespuesta(respuesta);

				conn.commit();
			}
		} finally {
			conn.setAutoCommit(true);
			DbUtils.closeQuietly(conn);
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
	 * @return OutputCliente
	 * @throws SQLException
	 */
	public static OutputCliente doPutDel(Connection conn, InputCliente input, int metodo) throws SQLException {
		String nombreMetodo = "doPutDel";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta = null;
		OutputCliente output = null;

		List<Filtro> condicionesExistencia = CtrlCliente.obtenerCondicionesExistencia(conn, input, metodo);
		String existencia = UtileriasBD.verificarExistencia(conn, Cliente.N_TABLA, condicionesExistencia);
		if (new Integer(existencia) < 1) {
			log.error("No existe el recurso.");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			output = new OutputCliente();
			output.setRespuesta(respuesta);

			return output;
		} else {
			if (metodo == Conf.METODO_PUT) {
				condicionesExistencia.clear();
				condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						Cliente.CAMPO_TC_SC_CLIENTE_ID, input.getIdCliente()));

				String campos[] = { Cliente.CAMPO_NIT, Cliente.CAMPO_DOCUMENTO_IDENTIFICACION };
				List<Map<String, String>> datosSQL = UtileriasBD.getSingleData(conn, Cliente.N_TABLA, campos,
						condicionesExistencia, null);

				condicionesExistencia.remove(condicionesExistencia.size() - 1);
				condicionesExistencia.add(UtileriasJava.getCondicionDefault(Conf.FILTRO_ESTADO,
						Distribuidor.CAMPO_ESTADO, conn, input.getCodArea()));

				if (!datosSQL.get(0).get(Cliente.CAMPO_NIT).equals(input.getNit())) {
					condicionesExistencia.add(
							UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Cliente.CAMPO_NIT, input.getNit()));

					existencia = UtileriasBD.verificarExistencia(conn, Cliente.N_TABLA, condicionesExistencia);
					if (new Integer(existencia) > 0) {
						log.error("Ya existe el recurso con ese NIT.");
						respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_EXISTENTE, null,
								nombreClase, nombreMetodo, "Ya existe el NIT ingresado.", input.getCodArea());

						output = new OutputCliente();
						output.setRespuesta(respuesta);

						return output;
					}
				}

				if (condicionesExistencia.size() > 1) {
					condicionesExistencia.remove(condicionesExistencia.size() - 1);
				}
				if (!datosSQL.get(0).get(Cliente.CAMPO_DOCUMENTO_IDENTIFICACION).equals(input.getDocIdentificacion())) {
					condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
							Cliente.CAMPO_DOCUMENTO_IDENTIFICACION, input.getDocIdentificacion()));

					existencia = UtileriasBD.verificarExistencia(conn, Cliente.N_TABLA, condicionesExistencia);
					if (new Integer(existencia) > 0) {
						log.error("Ya existe el recurso con ese Documento de Identificaci\u00F3n.");
						respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_EXISTENTE, null,
								nombreClase, nombreMetodo, "Ya existe el Documento de Identificación ingresado.",
								input.getCodArea());

						output = new OutputCliente();
						output.setRespuesta(respuesta);

						return output;
					}
				}
			}
		}

		String sql = null;

		String campos[][] = CtrlCliente.obtenerCamposPutDel(input, metodo);
		List<Filtro> condiciones = CtrlCliente.obtenerCondiciones(input, metodo);

		sql = UtileriasBD.armarQueryUpdate(Cliente.N_TABLA, campos, condiciones);

		try {
			int i = 0;
			conn.setAutoCommit(false);
			QueryRunner Qr = new QueryRunner();
			i = Qr.update(conn, sql);

			if (i > 0) {
				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_MODIFICADO, null, nombreClase,
						nombreMetodo, null, input.getCodArea());

				conn.commit();
			} else {
				respuesta = new Respuesta();
				respuesta.setCodResultado("0");
			}
		} finally {
			conn.setAutoCommit(true);
			DbUtils.closeQuietly(conn);
			output = new OutputCliente();
			output.setRespuesta(respuesta);
		}

		return output;
	}
}
