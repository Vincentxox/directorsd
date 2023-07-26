package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.output.inventario.Historico;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;

public class OperacionHistoricoInv {
	private OperacionHistoricoInv(){}
    private static final Logger log = Logger.getLogger(OperacionHistoricoInv.class);

    public static String armaQueryHistorico(String codArea, BigDecimal idPais) {
    	String query="SELECT H.TCSCLOGINVSIDRAID, " +
				"       T.CODIGO_TRANSACCION, " +
				"       T.TIPO_MOVIMIENTO, " +
				"       H.BODEGA_ORIGEN, " +
				"       H.TCSCTRASLADOID, " +
				"       H.PRECIO, " +
				"       (SELECT NOMBRE " +
				"          FROM TC_SC_BODEGAVIRTUAL  PARTITION ("+ControladorBase.getPais(codArea)+")" +
				"         WHERE TCSCBODEGAVIRTUALID = H.BODEGA_ORIGEN) " +
				"          AS NOMBRE_BODEGA_ORIGEN, " +
				"       H.BODEGA_DESTINO, " +
				"       (SELECT NOMBRE " +
                "          FROM TC_SC_BODEGAVIRTUAL  PARTITION ("+ControladorBase.getPais(codArea)+")" +
                "         WHERE TCSCBODEGAVIRTUALID = H.BODEGA_DESTINO) " +
				"          AS NOMBRE_BODEGA_DESTINO, " +
				"       H.ARTICULO, " +
				"       NVL ( " +
				"          (SELECT DESCRIPCION " +
				"             FROM TC_SC_ART_PROMOCIONAL " +
				"            WHERE     TCSCARTPROMOCIONALID = H.ARTICULO " +
				"                  AND tipo_inv = 'INV_SIDRA' AND TCSCCATPAISID="+idPais+"), " +
				"          (SELECT DESCRIPCION " +
				"             FROM TC_SC_ARTICULO_INV A"+ 
				"            WHERE A.articulo = H.ARTICULO  "
						+ "AND TCSCCATPAISID="+idPais+" /*AND tipo_inv = 'INV_TELCA'*/ " +
				"           )) " +
				"          DESCRIPCION, " +
				"       H.SERIE, " +
				"       H.SERIE_FINAL, " +
				"       H.CANTIDAD, " +
				"       H.SERIE_ASOCIADA, " +
				"       H.COD_NUM, " +
				"       H.ESTADO, " +
				"       H.TIPO_INV, " +
				"       H.CREADO_POR, " +
				"       H.CREADO_EL " +
				"  FROM TC_SC_HISTORICO_INVSIDRA PARTITION ("+ControladorBase.getPais(codArea)+") H,"
						+ " TC_SC_TIPOTRANSACCION T " +
				" WHERE H.TCSCTIPOTRANSACCIONID = T.TCSCTIPOTRANSACCIONID "
				+ "	AND H.TCSCCATPAISID = T.TCSCCATPAISID"; 
    	
    	return query;
    }
    public static List<Historico> getHistorico(Connection conn, List<Filtro> filtros, String codArea, BigDecimal idPais) throws SQLException {
        List<Historico> lista = new ArrayList<Historico>();
        Historico objHist = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        SimpleDateFormat FORMATO_FECHAHORA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHAHORA);
        String query=armaQueryHistorico(codArea, idPais);
			
	
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
	     try{
	        log.trace("Query para obtener historico:" + query);
	        pstmt = conn.prepareStatement(query);
	        rst = pstmt.executeQuery();
	
	        while (rst.next()) {
	            objHist = new Historico();
	
	            objHist.setArticulo(rst.getString("ARTICULO"));
	            objHist.setDescripcion(rst.getString("DESCRIPCION") == null ? " " : rst.getString("DESCRIPCION"));
	            objHist.setBodegaDestino(rst.getString("BODEGA_DESTINO"));
	            objHist.setBodegaOrigen(rst.getString("BODEGA_ORIGEN") == null ? " " : rst.getString("BODEGA_ORIGEN"));
	            objHist.setCantidad(rst.getString("CANTIDAD"));
	            objHist.setCodigoTransaccion(rst.getString("CODIGO_TRANSACCION"));
	            objHist.setCodNum(rst.getString("COD_NUM") == null ? " " : rst.getString("COD_NUM"));
	            objHist.setEstado(rst.getString("ESTADO"));
	            objHist.setCreado_el(FORMATO_FECHAHORA.format(rst.getTimestamp("CREADO_EL")));
	            objHist.setCreado_por(rst.getString("CREADO_POR"));
	            objHist.setTipoInv(rst.getString("TIPO_INV") == null ? " " : rst.getString("TIPO_INV"));
	            objHist.setTcscloginvsidraid(rst.getString("TCSCLOGINVSIDRAID"));
	            objHist.setTipoMovimiento(rst.getString("TIPO_MOVIMIENTO"));
	            if (rst.getString("SERIE_ASOCIADA") == null || rst.getString("SERIE_ASOCIADA").equals("null")
	                    || rst.getString("SERIE_ASOCIADA").equals("")) {
	                objHist.setSerieAsociada(" ");
	            } else {
	                objHist.setSerieAsociada("SERIE_ASOCIADA");
	            }
	
	            if (rst.getString("SERIE") == null || rst.getString("SERIE").equals("null")
	                    || rst.getString("SERIE").equals("")) {
	                objHist.setSerie(" ");
	            } else {
	                objHist.setSerie(rst.getString("SERIE"));
	            }
	            
	            if (rst.getString("SERIE_FINAL") == null || rst.getString("SERIE_FINAL").equals("null")
	                    || rst.getString("SERIE_FINAL").equals("")) {
	                objHist.setSerieFinal(" ");
	            } else {
	                objHist.setSerieFinal(rst.getString("SERIE_FINAL"));
	            }
	
	            if (rst.getString("NOMBRE_BODEGA_DESTINO") == null || rst.getString("NOMBRE_BODEGA_DESTINO").equals("null")
	                    || rst.getString("NOMBRE_BODEGA_DESTINO").equals("")) {
	                objHist.setNombreBodegaDestino(" ");
	            } else {
	                objHist.setNombreBodegaDestino(rst.getString("NOMBRE_BODEGA_DESTINO"));
	            }
	
	            if (rst.getString("NOMBRE_BODEGA_ORIGEN") == null || rst.getString("NOMBRE_BODEGA_ORIGEN").equals("null")
	                    || rst.getString("NOMBRE_BODEGA_ORIGEN").equals("")) {
	                objHist.setNombreBodegaOrigen(" ");
	            } else {
	                objHist.setNombreBodegaOrigen(rst.getString("NOMBRE_BODEGA_ORIGEN"));
	            }
	            
	            if (rst.getString("TCSCTRASLADOID") == null || rst.getString("TCSCTRASLADOID").equals("null")
	                    || rst.getString("TCSCTRASLADOID").equals("")) {
	                objHist.setIdTraslado(" ");
	            } else {
	                objHist.setIdTraslado(rst.getString("TCSCTRASLADOID"));
	            }
	            
	            if (rst.getString("PRECIO") == null || rst.getString("PRECIO").equals("null")
	                    || rst.getString("PRECIO").equals("")) {
	                objHist.setPrecio(" ");
	            } else {
	                objHist.setPrecio(rst.getString("PRECIO"));
	            }
	            lista.add(objHist);
	        }
        } finally{
        	DbUtils.closeQuietly(pstmt);
        	DbUtils.closeQuietly(rst);
        }

        return lista;
    }
}