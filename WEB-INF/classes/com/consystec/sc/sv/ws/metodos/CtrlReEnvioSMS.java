package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.ericsson.sc.ca.ws.input.reenviosms.movil.InputReenvioSMS;
import com.ericsson.sc.ca.ws.output.reenviosms.movil.OutputReenvioSMS;
import com.ericsson.sdr.dao.OlsDAO;

public class CtrlReEnvioSMS extends ControladorBase {

	private static final Logger log = LoggerFactory.getLogger(CtrlReEnvioSMS.class);
	private static final String BEGIN_METHOD = "BEGIN - {}";
	private static final String END_METHOD = "END - {}";

	public OutputReenvioSMS reenvioSMS(InputReenvioSMS input) {
		final String METHOD_NAME = "reenvioSMS()";
		log.info(BEGIN_METHOD, METHOD_NAME);
		log.debug("{} - input {}", METHOD_NAME, input);
		OutputReenvioSMS reenvio = new OutputReenvioSMS();
		Respuesta objRespuesta = new Respuesta();
		log.trace("inicia a validar valores...");

		log.debug("{} - requiring connection", METHOD_NAME);
		try (Connection conn = getConnLocal()) {
			
			objRespuesta = validarDatos(input, conn);
			
			OlsDAO ols = new OlsDAO();
			if (input.getIdVenta() == null
					|| ols.getVentaSincronizada(conn, new BigDecimal(input.getIdVenta())).intValue() == 0) {
				
				log.debug("{} - the sale has not been synchronized yet", METHOD_NAME);
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_SINCRONIZACION_PENDIENTE, null, this.getClass().toString(),
						METHOD_NAME, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				
			} else if (ols.getVentaPendienteEnvio(conn, new BigDecimal(input.getIdVenta())).intValue() == 1) {
				
				log.debug("{} - message ngw pending to send", METHOD_NAME);
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_ENVIO_NGW_PENDIENTE, null, this.getClass().toString(),
						METHOD_NAME, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				
			} else {
				log.debug("{} - update ngw status", METHOD_NAME);
				ols.updateStatusNGW(conn, new BigDecimal(input.getIdVenta()));
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_OK_REENVIO, null, this.getClass().toString(), METHOD_NAME,
						null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				
			}
		} catch (Exception e) {
			log.error(MessageFormat.format(
					"{0} - Se presento una excepcion al realizar el reenvio del mensaje, causa: {1}", METHOD_NAME,
					e.getMessage()), e);
			objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
					METHOD_NAME, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

		}
		reenvio.setRespuesta(objRespuesta);
		log.debug("{} - response {}", METHOD_NAME, reenvio);
		log.info(END_METHOD, METHOD_NAME);
		return reenvio;
	}

	public Respuesta validarDatos(InputReenvioSMS objDatos, Connection conn) throws SQLException {
		Respuesta objRespuesta = null;
		String nombreMetodo = "validarDatos";

		if (objDatos.getCodArea() == null || "".equals(objDatos.getCodArea().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODAREA_NULO_1, null, this.getClass().toString(), nombreMetodo,
					null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

		} else if (objDatos.getCodArea().length() != Conf.LONG_CODPAIS) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONGINVALIDA_AREA_6, null, this.getClass().toString(),
					nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

		} else {
			BigDecimal idPais = getidpais(conn, objDatos.getCodArea());
			if (idPais == null || idPais.intValue() == 0) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_AREAINVALIDA_8, null, this.getClass().toString(),
						nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
			}
		}

		if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), nombreMetodo,
					null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
		}

		if (objDatos.getToken() == null || "".equals((objDatos.getToken().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_NULO_3, null, this.getClass().toString(), nombreMetodo,
					null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
		} else {
			if (!"WEB".equalsIgnoreCase(objDatos.getToken())) {
				if (objDatos.getCodDispositivo() == null || "".equals(objDatos.getCodDispositivo().trim())) {

					objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(),
							nombreMetodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				}
			}
		}

		return objRespuesta;
	}
}
