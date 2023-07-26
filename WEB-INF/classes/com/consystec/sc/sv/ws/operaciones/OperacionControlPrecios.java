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

import com.consystec.sc.ca.ws.output.venta.OutputControlPrecios;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;

public class OperacionControlPrecios {
	private OperacionControlPrecios(){}
	 private static final Logger log = Logger.getLogger(OperacionControlPrecios.class);
	public static BigDecimal getCountDatos (Connection conn, List<Filtro> filtros,String codArea) throws SQLException{
		BigDecimal respuesta=null;
		
		 PreparedStatement pstmt = null;
        ResultSet rst = null;
        
			String query=generaQuery( codArea) ;
			
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
			
	        log.trace("Query para obtener count de control de precios:" + query);
	   try{
	        pstmt = conn.prepareStatement(query);
	        rst = pstmt.executeQuery();
	
	        if(rst.next()){
	        	respuesta=rst.getBigDecimal(1);
	        }
        }finally{
        	DbUtils.closeQuietly(pstmt);
        	DbUtils.closeQuietly(rst);
        }
		return respuesta;
		
	}
	public static String generaQuery(String codArea) {
		String query="SELECT COUNT(1) " +
				"  FROM TC_SC_VENTA PARTITION ("+ControladorBase.getPais(codArea)+") V, " +
				"       TC_SC_VENTA_DET DV, " +
				"       TC_SC_ARTICULO_INV A, " +
				"       TC_SC_PRECIO_ARTICULO P, " +
				"       TC_SC_JORNADA_VEND PARTITION ("+ControladorBase.getPais(codArea)+") J " +
				" WHERE     V.TCSCVENTAID = DV.TCSCVENTAID " +
				"       AND A.ARTICULO = DV.ARTICULO " +
				"       AND P.ARTICULO = DV.ARTICULO " +
				"       AND J.TCSCJORNADAVENID=V.TCSCJORNADAVENID " +
				"       AND A.ESTADO = 'ALTA' " +
				"       AND V.TCSCCATPAISID = J.TCSCCATPAISID " +
				"       AND V.TCSCCATPAISID = A.TCSCCATPAISID " +
				"       AND P.TCSCCATPAISID = A.TCSCCATPAISID " +
				"       AND P.ESTADO = 'ALTA' " ;
		return query;
	}
	
	public static List<OutputControlPrecios> getDatos(Connection conn, List<Filtro> filtros, int max, int min, String codArea) throws SQLException{
		 PreparedStatement pstmt = null;
	     ResultSet rst = null;
	     String queryFinal="";
	     List<OutputControlPrecios> lista = new ArrayList<OutputControlPrecios>();
	     OutputControlPrecios objeto = new OutputControlPrecios();
	     SimpleDateFormat FORMATO_FECHAHORA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHAHORA);
	
			String query="SELECT V.TCSCVENTAID, " +
					"       V.NUMERO, " +
					"       V.SERIE, " +
					"       V.FOLIO_SIDRA, " +
					"       V.SERIE_SIDRA, " +
					"       V.TIPO_DOCUMENTO, " +
					"       V.FECHA_EMISION, " +
					"       DV.ARTICULO, " +
					"       A.DESCRIPCION, " +
					"       DV.CANTIDAD, " +
					"       DV.SERIE SERIE_ARTICULO, " +
					"       P.PRECIO PRECIO_INICIAL, " +
					"       nvl(DV.DESCUENTO_SCL,0) DESCUENTO_SCL, " +
					"       nvl(DV.DESCUENTO_SIDRA,0) DESCUENTO_SIDRA," +
					"       DV.PRECIO_FINAL PRECIO_VENTA " +
					"  FROM TC_SC_VENTA PARTITION ("+ControladorBase.getPais(codArea)+") V, " +
					"       TC_SC_VENTA_DET DV, " +
					"       TC_SC_ARTICULO_INV A, " +
					"       TC_SC_PRECIO_ARTICULO P, " +
					"       TC_SC_JORNADA_VEND PARTITION ("+ControladorBase.getPais(codArea)+") J " +
					" WHERE     V.TCSCVENTAID = DV.TCSCVENTAID " +
					"       AND A.ARTICULO = DV.ARTICULO " +
					"       AND P.ARTICULO = DV.ARTICULO " +
					"       AND J.TCSCJORNADAVENID=V.TCSCJORNADAVENID " +
					"       AND A.ESTADO = 'ALTA' " +
					"       AND V.TCSCCATPAISID = J.TCSCCATPAISID " +
					"       AND V.TCSCCATPAISID = A.TCSCCATPAISID " +
					"       AND P.TCSCCATPAISID = A.TCSCCATPAISID " +
					"       AND P.ESTADO = 'ALTA' " ;
			
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
			  
			  queryFinal = UtileriasBD.getLimit(query, min, max);
			  
			  log.trace("Query para obtener control de precios:" + queryFinal);
			try{
				pstmt = conn.prepareStatement(queryFinal);
		        rst = pstmt.executeQuery();
	
		        while(rst.next()){
		        	  objeto = new OutputControlPrecios();
	
		        	  
		        	  
		        	  objeto.setArticulo(rst.getString("ARTICULO"));
		        	  objeto.setDescripcion(rst.getString("DESCRIPCION") == null ? " " : rst.getString("DESCRIPCION"));
		        	  objeto.setCantidad(rst.getString("CANTIDAD"));
		        	  objeto.setDescuentoSCL(rst.getString("DESCUENTO_SCL"));
		        	  objeto.setDescuentoSIDRA(rst.getString("DESCUENTO_SIDRA"));
		        	  objeto.setIdVenta(rst.getString("TCSCVENTAID"));
		        	  objeto.setPrecioInicial(rst.getString("PRECIO_INICIAL"));
		        	  objeto.setPrecioVenta(rst.getString("PRECIO_VENTA"));
		        	  objeto.setTipoDocumento(rst.getString("TIPO_DOCUMENTO"));
		        	  objeto.setFecha(FORMATO_FECHAHORA.format(rst.getTimestamp("FECHA_EMISION")));
		        	  if (rst.getString("SERIE") == null || rst.getString("SERIE").equals("null")
		                      || rst.getString("SERIE").equals("")) {
		                  objeto.setSerie(" ");
		              } else {
		            	  objeto.setSerie(rst.getString("SERIE"));
		              }
		        	  
		            
		              if (rst.getString("NUMERO") == null || rst.getString("NUMERO").equals("null")
		                      || rst.getString("NUMERO").equals("")) {
		            	  objeto.setNumero("");
		              } else {
		            	  objeto.setNumero("NUMERO");
		              }
		        	  if (rst.getString("SERIE_ARTICULO") == null || rst.getString("SERIE_ARTICULO").equals("null")
		                      || rst.getString("SERIE_ARTICULO").equals("")) {
		                  objeto.setSerieArticulo("");
		              } else {
		            	  objeto.setSerieArticulo(rst.getString("SERIE_ARTICULO"));
		              }
		             
		              
		              if (rst.getString("FOLIO_SIDRA") == null || rst.getString("FOLIO_SIDRA").equals("null")
		                      || rst.getString("FOLIO_SIDRA").equals("")) {
		            	  objeto.setFolioSidra("");
		              } else {
		            	  objeto.setFolioSidra(rst.getString("FOLIO_SIDRA"));
		              }
		              
	
		              if (rst.getString("SERIE_SIDRA") == null || rst.getString("SERIE_SIDRA").equals("null")
		                      || rst.getString("SERIE_SIDRA").equals("")) {
		                  objeto.setSerieSidra("");
		              } else {
		            	  objeto.setSerieSidra(rst.getString("SERIE_SIDRA"));
		              }
	
		              lista.add(objeto);
		        }
	     }finally{
	        DbUtils.closeQuietly(pstmt);
	        DbUtils.closeQuietly(rst);
	     }
		  return lista;
	}
}
