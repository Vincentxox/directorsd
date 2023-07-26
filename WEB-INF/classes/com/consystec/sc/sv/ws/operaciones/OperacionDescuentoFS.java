package com.consystec.sc.sv.ws.operaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.descuentoFS.InputDescuentoFS;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionDescuentoFS {
	private OperacionDescuentoFS(){}
	private static final Logger log = Logger.getLogger(OperacionDescuentoFS.class);
	
	public static List<InputDescuentoFS> doGet(Connection conn, InputDescuentoFS input) throws SQLException{
		List<InputDescuentoFS> lst = new ArrayList<InputDescuentoFS>();

		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String sql="";

		try {
			sql = "  SELECT O.NOMBRE NOMBRE_OFERTA, " ;
			sql = sql + "         D.ID_DESCUENTO ID_DESCUENTO, " ;
			sql = sql + "         D.ID_PRODUCT_OFFERING ID_PRODUCT_OFFERING, " ;
			sql = sql + "         D.NOMBRE_DESCUENTO NOMBRE_DESCUENTO, " ;
			sql = sql + "         D.MONTO_DESCUENTO MONTO_DECUENTO, " ;
			sql = sql + "         D.TIPO_DESCUENTO TIPO_DESCUENTO, " ;
			sql = sql + "         D.CREADO_EL CREADO_EL, " ;
			sql = sql + "         D.CREADO_POR CREADO_POR, " ;
			sql = sql + "         D.MODIFICADO_EL MODIFICADO_EL, " ;
			sql = sql + "         D.MODIFICADO_POR MODIFICADO_POR " ;
			sql = sql + "    FROM TC_SC_DESCUENTO_SIDRA_FS D, TC_SC_OFERTA_SIDRA_FS O " ;
			sql = sql + "   WHERE     D.ID_PRODUCT_OFFERING = O.ID_PRODUCT_OFFERING " ;

			if(input.getIdProductOffering() != null && !"".equals(input.getIdProductOffering()))
				sql = sql + "         AND D.ID_PRODUCT_OFFERING = " + input.getIdProductOffering()  ;
			else if(input.getIdDescuento() != null && !"".equals(input.getIdDescuento()))
				sql = sql + "         AND D.ID_DESCUENTO =  " + input.getIdDescuento();
			else if(input.getNombreDescuento() != null && !"".equals(input.getNombreDescuento()))
				sql = sql + "         AND NOMBRE_DESCUENTO = '"+input.getNombreDescuento()+"' " ;
			
			sql = sql + "GROUP BY O.NOMBRE, " ;
			sql = sql + "         D.ID_DESCUENTO, " ;
			sql = sql + "         D.ID_PRODUCT_OFFERING, " ;
			sql = sql + "         D.NOMBRE_DESCUENTO, " ;
			sql = sql + "         D.MONTO_DESCUENTO, " ;
			sql = sql + "         D.TIPO_DESCUENTO, " ;
			sql = sql + "         D.CREADO_EL, " ;
			sql = sql + "         D.CREADO_POR, " ;
			sql = sql + "         D.MODIFICADO_EL, " ;
			sql = sql + "         D.MODIFICADO_POR " ;
			sql = sql + "ORDER BY ID_PRODUCT_OFFERING ASC " ;

			log.trace("QUERY OBTENER OFERTAS: " + sql);
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				do {
					InputDescuentoFS descuentos = new InputDescuentoFS();
					
					descuentos.setIdDescuento(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "ID_DESCUENTO"));
					descuentos.setIdProductOffering(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "ID_PRODUCT_OFFERING"));
					descuentos.setNombreOferta(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRE_OFERTA"));
					descuentos.setNombreDescuento(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRE_DESCUENTO"));
					descuentos.setMontoDescuento(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "MONTO_DECUENTO"));
					String primerCaracter = descuentos.getMontoDescuento().substring(0);
					log.trace("primerCaracter:"+ primerCaracter);
					if (".".equals(primerCaracter)){
						descuentos.setMontoDescuento("0"+descuentos.getMontoDescuento());
					}
					
					descuentos.setTipoDescuento(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "TIPO_DESCUENTO"));
					descuentos.setCreadoEl(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, "CREADO_EL"));
					descuentos.setCreadoPor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CREADO_POR"));
					descuentos.setModificadoEl(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, "MODIFICADO_EL"));
					descuentos.setModificadoPor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "MODIFICADO_POR"));
					
					lst.add(descuentos);
				} while (rst.next());
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return lst;
	}
}
