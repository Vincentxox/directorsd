package com.consystec.sc.ca.ws.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.consystec.sc.ca.ws.orm.MensajeVW;

public class MensajeVWDAO {

	private static final Logger logger = LoggerFactory.getLogger(MensajeVWDAO.class);

	private static final String SQL_QUERY_MSJ_DIRECTOR = " select * "
			+ "												 from TC_SC_MSJ_DIRECTOR_VW  "
			+ "											    where codresultado=? "
			+ " 											  AND estado = 1 ";

	public List<MensajeVW> select(Connection conn, BigDecimal codResultado) throws SQLException {
		final String METHOD_NAME = "select()";
		logger.info("Begin - {}", METHOD_NAME);
		List<MensajeVW> registro = new ArrayList<>();
		MensajeVW mensajeVw = new MensajeVW();
		try (PreparedStatement pstm = conn.prepareStatement(SQL_QUERY_MSJ_DIRECTOR)) {
			String queryToExecute = MessageFormat.format("{0}, params [{1}]", SQL_QUERY_MSJ_DIRECTOR, codResultado);
			logger.debug("{} - query {}", METHOD_NAME, queryToExecute);
			pstm.setBigDecimal(1, codResultado);
			try (ResultSet rst = pstm.executeQuery()) {
				while (rst.next()) {
					mensajeVw.setTcscmsjdirectorid(rst.getBigDecimal(MensajeVW.CAMPO_TCSCMSJDIRECTORID));
					mensajeVw.setCodresultado(rst.getBigDecimal("CODRESULTADO"));
					mensajeVw.setDescripcion(rst.getString("DESCRIPCION"));
					mensajeVw.setMostrar(rst.getBigDecimal("MOSTRAR"));
					mensajeVw.setTipo_excepcion(rst.getString("TIPO_EXCEPCION"));
					mensajeVw.setEstado(rst.getString("ESTADO"));
					registro.add(mensajeVw);
				}				
			}
		}
		logger.info("End - {}", METHOD_NAME);
		return registro;
	}	
}
