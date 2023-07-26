package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.ticket.Lineas;
import com.consystec.sc.ca.ws.input.ticket.OutputTicket;
import com.consystec.sc.sv.ws.orm.LineaVenta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionVisorTicket {
private OperacionVisorTicket(){}
	   private static final Logger log = Logger.getLogger(OperacionVisorTicket.class);
	   
	   /**
	    *M\u00E9todo para insertar las lineas que posee el ticket impreso en el m\u00F3vil
	    * */
	public static void insertaLineaTicket(Connection conn, List<Lineas> lstlineasVenta, String idVenta, String usuario, String dispositivo, BigDecimal idPais) throws SQLException {
        if (lstlineasVenta != null && !lstlineasVenta.isEmpty()) {
            String valores = "";
            PreparedStatement pstmt = null;
            String insert = "";

            try {
                List<String> listaInserts = new ArrayList<String>();

                String campos[] = {
                    LineaVenta.CAMPO_TCSCLINEAVENTAID,
                    LineaVenta.CAMPO_COD_DISPOSITIVO,
                    LineaVenta.CAMPO_TCSCVENTAID,
                    LineaVenta.CAMPO_TCSCCATPAISID,
                    LineaVenta.CAMPO_ALINEACION,
                    LineaVenta.CAMPO_IZQUIERDA,
                    LineaVenta.CAMPO_DERECHA,
                    LineaVenta.CAMPO_CENTRO,
                    LineaVenta.CAMPO_ESTILO,
                    LineaVenta.CAMPO_EXTRA,
                    LineaVenta.CAMPO_CREADO_POR,
                    LineaVenta.CAMPO_CREADO_EL
                };

                for (int a=0; a<lstlineasVenta.size(); a++) {
                    valores = UtileriasJava.setInsert(Conf.INSERT_NUMERO, ""+(a+1), Conf.INSERT_SEPARADOR_SI)
                    	+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, dispositivo, Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idVenta, Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_NUMERO,idPais+"", Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO, lstlineasVenta.get(a).getAlineacion(), Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO,  lstlineasVenta.get(a).getIzquierda(), Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO,  lstlineasVenta.get(a).getDerecha(), Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO,  lstlineasVenta.get(a).getCentro(), Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO,  lstlineasVenta.get(a).getEstilo(), Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO,  lstlineasVenta.get(a).getExtra(), Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO, usuario, Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_NO);

                    listaInserts.add(valores);
                }

                // armando insert
                insert = UtileriasBD.armarQueryInsertAll(LineaVenta.N_TABLA, campos, listaInserts);
                log.trace("insert lineas ticket:"+insert);
                pstmt = conn.prepareStatement(insert);
                pstmt.executeUpdate();

            } finally {
                DbUtils.closeQuietly(pstmt);
            }
        }
    }
	
	/***
	 * Valida si la venta ya esta registrada en BDD o si esta debe reemplazarse 
	 * Index 0: indica si la venta existe
	 * Index 1: indica si hay una cantidad de l\u00EDneas registradas en BDD
	 * 	 * */
	public static List<Integer> validarVenta(Connection conn, String idVenta, BigDecimal idPais) throws SQLException{
		List<Integer> resultado=new ArrayList<Integer>();
		String query="";
		PreparedStatement pstmt = null;
		ResultSet rst=null;
		
		query="SELECT (SELECT COUNT (1) " +
				"          FROM TC_SC_VENTA " +
				"         WHERE TCSCVENTAID =? AND TCSCCATPAISID = ?) " +
				"          venta, " +
				"       (SELECT COUNT (1) " +
				"          FROM TC_SC_LINEA_VENTA " +
				"         WHERE TCSCVENTAID = ? AND TCSCCATPAISID =?) " +
				"          lineas " +
				"  FROM DUAL "; 

		try { 
	        pstmt = conn.prepareStatement(query);
	        pstmt.setBigDecimal(1, new BigDecimal(idVenta));
	        pstmt.setBigDecimal(2, idPais);
	        pstmt.setBigDecimal(3, new BigDecimal(idVenta));
	        pstmt.setBigDecimal(4, idPais);
	        rst = pstmt.executeQuery();

            if (rst.next()) {
                resultado.add(0,  rst.getInt("venta"));
                resultado.add(1,  rst.getInt("lineas"));
            }
        } finally {

            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
		return resultado;
	}
	
	
	/**
	 * Elimina las lineas viejas de un visor para agregar la ultima version impresa
	 * */
	public static int deleteVisor (Connection conn, String idVenta, BigDecimal idPais) throws SQLException{
		String query="";
		PreparedStatement pstmt = null;
		int ret=0;
		query="DELETE FROM TC_SC_LINEA_VENTA WHERE TCSCVENTAID=? AND TCSCCATPAISID=?";
		log.trace("realizo delete:"+query);
		try{
			  pstmt = conn.prepareStatement(query);
			   pstmt.setBigDecimal(1, new BigDecimal(idVenta));
		        pstmt.setBigDecimal(2, idPais);
              ret=pstmt.executeUpdate();
		}finally{
			DbUtils.closeQuietly(pstmt);
		}
		return ret;
	}
	
	
	public static OutputTicket getLineas (Connection conn, String idVenta, String codArea, BigDecimal idPais) throws SQLException{
		OutputTicket objRespuesta = new OutputTicket();
		List<Lineas>lstLineas = new ArrayList<Lineas>();
		Lineas objLinea;
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		StringBuilder query = new StringBuilder();
		query.append("SELECT TCSCVENTAID, " );
		query.append(		"       COD_DISPOSITIVO, " );
		query.append(		"       IZQUIERDA, " );
		query.append(		"       CENTRO, " );
		query.append(		"       DERECHA, " );
		query.append(	"       ALINEACION, " );
		query.append(	"       ESTILO, " );
		query.append(	"       EXTRA " );
		query.append(    "  FROM " + ControladorBase.getParticion(LineaVenta.N_TABLA, Conf.PARTITION, "", codArea) + " " );
		query.append(	" WHERE TCSCVENTAID = "+idVenta+" AND TCSCCATPAISID ="+idPais);
		query.append(	" ORDER BY TCSCLINEAVENTAID ASC");

		
		log.trace("realizo consulta ticket:"+query.toString());
		try { 
	        pstmt = conn.prepareStatement(query.toString());
	        rst = pstmt.executeQuery();

	        while (rst.next()) {
	        	objLinea= new Lineas();
	        if(rst.getString("ALINEACION")==null || "".equals(rst.getString("ALINEACION"))){
	        	objLinea.setAlineacion("");
	        }else{
	        	objLinea.setAlineacion(rst.getString("ALINEACION"));
	        }
	        if(rst.getString("ESTILO")==null || "".equals(rst.getString("ESTILO"))){
	        	objLinea.setEstilo("");
	        }else{
	        	objLinea.setEstilo(rst.getString("ESTILO"));
	        }      
	        
	        if(rst.getString("EXTRA")==null || "".equals(rst.getString("EXTRA"))){
	        	objLinea.setExtra("");
	        }else{
              objLinea.setExtra(rst.getString("EXTRA"));
	        }
	        if(rst.getString("IZQUIERDA")==null || "".equals(rst.getString("IZQUIERDA"))){
	        	 objLinea.setIzquierda("");
	        }else{
              objLinea.setIzquierda(rst.getString("IZQUIERDA"));
	        }
	        if(rst.getString("DERECHA")==null || "".equals(rst.getString("DERECHA"))){
	        	objLinea.setDerecha("");
	        }else{
	        	objLinea.setDerecha(rst.getString("DERECHA"));
	        }
	        if(rst.getString("CENTRO")==null || "".equals(rst.getString("CENTRO"))){
	        	  objLinea.setCentro("");
	        }else{
              objLinea.setCentro(rst.getString("CENTRO"));
	        }
              
              objRespuesta.setIdVenta(rst.getString("TCSCVENTAID"));
              lstLineas.add(objLinea);
            }
	        objRespuesta.setLineas(lstLineas);
        } finally {

            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
		
		return objRespuesta;

	}
}
