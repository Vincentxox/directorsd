package com.consystec.sc.sv.ws.operaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.sv.ws.orm.ofertafs.input.InputOfertaFS;
import com.consystec.sc.sv.ws.orm.ofertafs.output.Ofertas;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionOfertaFS {
	private OperacionOfertaFS(){}
	private static final Logger log = Logger.getLogger(OperacionOfertaFS.class);

	public static List<Ofertas> doGet(Connection conn, InputOfertaFS input) throws Exception{
		List<Ofertas> lst = new ArrayList<Ofertas>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
        String sql = "SELECT ID_PRODUCT_OFFERING ID_PRODUCT, NOMBRE NOMBRE, PRECIO PRECIO, PRECIO_MIN PRECIO_MIN, PRECIO_MAX PRECIO_MAX "
				+ "FROM TC_SC_OFERTA_SIDRA_FS WHERE ID_PRODUCT_OFFERING IN "
					+ "(SELECT TG.VALOR FROM TC_SC_CONFIGURACION TG INNER JOIN TC_SC_CONFIGURACION TA "
					+ "ON TG.VALOR = TA.VALOR "
					+ "WHERE TG.GRUPO = 'TIPO_GESTION' "
					+ "AND TG.NOMBRE = '" + input.getTipoGestion() + "' "
					+ "AND TG.ESTADO = 'ALTA' "
					+ "AND TA.GRUPO = 'TIPO_ARTICULO' "
					+ "AND TA.NOMBRE = '" + input.getTipoArticulo() + "' "
					+ "AND TG.TCSCCATPAISID = " + new ControladorBase().getIdPais(conn, input.getCodArea()) + " "
					+ "AND TA.ESTADO = 'ALTA')";
		log.trace("QUERY OBTENER OFERTAS: " + sql);

		try {
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				do {
					Ofertas oferta = new Ofertas();
					oferta.setIdProductOffering(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "ID_PRODUCT"));
					oferta.setNombre(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRE"));
					oferta.setPrecio(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "PRECIO"));
					oferta.setPrecioMin(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "PRECIO_MIN"));
					oferta.setPrecioMax(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "PRECIO_MAX"));
					lst.add(oferta);
				} while (rst.next());

			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return lst;
	}
}