package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.consystec.sc.ca.ws.input.reporte.InputReporteRecarga;
import com.consystec.sc.ca.ws.output.reporte.Recargas;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionReporteRecarga {
	private OperacionReporteRecarga(){}
	
	public static List<Recargas> doGet(Connection conn, InputReporteRecarga input, String vendedores, BigDecimal ID_PAIS) throws SQLException{
		List<Recargas> lstRecargas = new ArrayList<Recargas>();
		PreparedStatement pstmt = null;
        ResultSet rst = null;
        String sql="";
        
        try {
        	sql = queryRecargas( input,  vendedores, ID_PAIS);
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst.next()) {
            	do {
            		Recargas recargas = new Recargas();
            		String nombrePanelRuta = "";
            		String tipo = "";
            		
            		if(!"".equals(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "PANEL"))){
            			nombrePanelRuta = UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "PANEL");
            			tipo = "PANEL";
            		}else if(!"".equals(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "RUTA"))){
            			nombrePanelRuta = UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "RUTA");
            			tipo = "RUTA";
            		}
            			
            		
                	recargas.setJornada(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "JORNADA"));
                	recargas.setEstadoLiquidacionJornada(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "ESTADO_LIQUIDACION"));
                	recargas.setVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "VENDEDOR"));
                	recargas.setTotalFacturado(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "TOTAL_FACTURADO"));
                	recargas.setFecha(UtileriasJava.formatStringDate(rst.getString("FECHA")));
                	recargas.setNombreDts(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "DTS"));
                	recargas.setZona(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "ZONA"));
                	recargas.setNombrePanelRuta(nombrePanelRuta);
                	recargas.setTipo(tipo);
                	lstRecargas.add(recargas);
				} while (rst.next());
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
		return lstRecargas;
	}
	
	public static String queryRecargas(InputReporteRecarga input, String vendedores, BigDecimal ID_PAIS) {
		String sql="";
		
		sql = "SELECT A.TCSCJORNADAVENID JORNADA, " ;
    	sql = sql + "         C.ESTADO_LIQUIDACION, " ;
    	sql = sql + "         A.CREADO_POR VENDEDOR, " ;
    	sql = sql + "         ROUND (SUM (MONTO_FACTURA)) TOTAL_FACTURADO, " ;
    	sql = sql + "         TRUNC (FECHA_EMISION) FECHA, " ;
    	sql = sql + "         DT.NOMBRES DTS, " ;
    	sql = sql + "         E.NOMBRE ZONA, " ;
    	sql = sql + "         (SELECT PA.NOMBRE " ;
    	sql = sql + "            FROM TC_SC_PANEL PA, TC_SC_VEND_PANELPDV VE " ;
    	sql = sql + "           WHERE     PA.TCSCPANELID = VE.IDTIPO " ;
    	sql = sql + "                 AND VE.VENDEDOR = D.VENDEDOR " ;
    	sql = sql + "                 AND PA.TCSCCATPAISID = " + ID_PAIS;
    	sql = sql + "                 AND VE.ESTADO LIKE 'ALTA') " ;
    	sql = sql + "            PANEL, " ;
    	sql = sql + "         (SELECT NOMBRE " ;
    	sql = sql + "            FROM TC_SC_RUTA " ;
    	sql = sql + "           WHERE SECUSUARIOID = D.VENDEDOR AND TCSCCATPAISID = "+ID_PAIS+") " ;
    	sql = sql + "            RUTA " ;
    	sql = sql + "    FROM TC_SC_VENTA A " ;
    	sql = sql + "         INNER JOIN TC_SC_VENTA_DET B " ;
    	sql = sql + "            ON B.TCSCVENTAID = A.TCSCVENTAID " ;
    	sql = sql + "         INNER JOIN TC_SC_JORNADA_VEND C " ;
    	sql = sql + "            ON C.TCSCJORNADAVENID = A.TCSCJORNADAVENID " ;
    	sql = sql + "         INNER JOIN TC_SC_VEND_DTS D " ;
    	sql = sql + "            ON D.VENDEDOR = A.VENDEDOR " ;
    	sql = sql + "         INNER JOIN TC_SC_BODEGAVIRTUAL E " ;
    	sql = sql + "            ON E.TCSCBODEGAVIRTUALID = D.TCSCBODEGAVIRTUALID " ;
    	sql = sql + "         INNER JOIN TC_SC_DTS DT " ;
    	sql = sql + "            ON DT.TCSCDTSID = d.TCSCDTSID " ;
    	sql = sql + "   WHERE     TRUNC (A.FECHA_EMISION) BETWEEN TRUNC ( " ;
    	sql = sql + "                                                TO_DATE ('"+input.getFechaInicio()+"', " ;
    	sql = sql + "                                                         'yyyymmdd')) " ;
    	sql = sql + "                                         AND TRUNC ( " ;
    	sql = sql + "                                                TO_DATE ('"+input.getFechaFin()+"', " ;
    	sql = sql + "                                                         'yyyymmdd')) " ;
    	sql = sql + "         AND A.TCSCCATPAISID = "+ID_PAIS ;
    	sql = sql + "         AND A.ESTADO != 'ANULADO' " ;
    	sql = sql + "         AND B.TIPO_GRUPO_SIDRA = 'RECARGA' " ;
    	if(input.getIdTipo() != null && !"".equals(input.getIdTipo())){
    		sql = sql + "         AND a.vendedor IN ("+vendedores+")" ;
    		sql = sql + "         AND DT.TCSCDTSID =" + input.getIdDts();
    	}else{
    		if(input.getIdBodega() != null && !"".equals(input.getIdBodega())){
      			sql = sql + "    and E.TCSCBODEGAVIRTUALID = "+input.getIdBodega();
      			sql = sql + "         AND DT.TCSCDTSID =" + input.getIdDts();
      		}else if(input.getIdDts() != null && !"".equals(input.getIdDts())){
      			sql = sql + "         AND DT.TCSCDTSID = " + input.getIdDts();
      		}
    	}
    	sql = sql + "GROUP BY A.TCSCJORNADAVENID, " ;
    	sql = sql + "         A.CREADO_POR, " ;
    	sql = sql + "         TRUNC (FECHA_EMISION), " ;
    	sql = sql + "         C.ESTADO_LIQUIDACION, " ;
    	sql = sql + "         DT.NOMBRES, " ;
    	sql = sql + "         E.NOMBRE, " ;
    	sql = sql + "         D.VENDEDOR " ;
    	sql = sql + "ORDER BY FECHA ASC " ;
    	
    	return sql;
	}
}
