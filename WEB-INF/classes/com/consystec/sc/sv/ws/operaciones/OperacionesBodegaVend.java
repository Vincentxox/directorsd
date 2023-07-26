package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.bodegadts.InputBodegaVirtual;
import com.consystec.sc.sv.ws.metodos.CtrlBodegaVirtual;
import com.consystec.sc.sv.ws.orm.BodegaVendedor;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.VendedorPDV;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionesBodegaVend {
	private OperacionesBodegaVend(){}
    private static final Logger log = Logger.getLogger(OperacionesBodegaVend.class);

    /**
     * M\u00E9todo para crear bodegas virtuales a vendedores
     * @param estadoAlta 
     * 
     * @throws SQLException
     */
    public static void creaBodVirtualVendedor(Connection conn, List<InputBodegaVirtual> lstObj, String estadoAlta, BigDecimal idPais) throws SQLException {
        String sql = null;
        PreparedStatement pstmt = null;
        String campos[] = CtrlBodegaVirtual.obtenerCamposGetPost(Conf.METODO_POST);
        ResultSet rs = null;
        PreparedStatement pstmt2 = null;
            for (int a = 0; a < lstObj.size(); a++) {
                List<String> inserts = CtrlBodegaVirtual.obtenerInsertsPostPadre(conn, lstObj.get(a),
                        BodegaVirtual.SEQUENCE, idPais);
                sql = UtileriasBD.armarQueryInsert(BodegaVirtual.N_TABLA, campos, inserts);

                
                String generatedColumns[] = { BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID };
                try {
	                pstmt = conn.prepareStatement(sql, generatedColumns);
	
	                pstmt.executeUpdate();
	                rs = pstmt.getGeneratedKeys();
		
		            // si la bodega fue creada con exito se procede a insertar la asociaci\u00F3n de la bodega
		            if (rs != null) {
		                String insert = "INSERT INTO TC_SC_BODEGA_VENDEDOR (tcscbodegavendedorid, " +
		                		"                                   tcscbodegavirtualid, " +
		                		"                                   vendedor, " +
		                		"                                   estado, " +
		                		"                                   creado_el, " +
		                		"                                   creado_por, " +
		                		"                                   tcsccatpaisid) " +
		                		"     VALUES (TC_SC_BODEGA_VENDEDOR_SQ.NEXTVAL, " +
		                		"             ?, " +
		                		"             ?, " +
		                		"             ?, " +
		                		"             SYSDATE, " +
		                		"             ?, " +
		                		"             ?) " ;
		
		                if (rs.next()) {
		                	try{
		                    pstmt2 = conn.prepareStatement(insert);
		                    pstmt2.setBigDecimal(1, rs.getBigDecimal(1));
		                    pstmt2.setBigDecimal(2, new BigDecimal(lstObj.get(a).getIdDTS()));
		                    pstmt2.setString(3, estadoAlta);
		                    pstmt2.setString(4, lstObj.get(a).getUsuario());
		                    pstmt2.setBigDecimal(5, idPais);
		
	                        pstmt2.executeUpdate();
		                	}finally{
		                		 DbUtils.closeQuietly(pstmt2);
		                	}
	                    }
	                }
                } finally {
                	DbUtils.closeQuietly(pstmt);
                    DbUtils.closeQuietly(rs);
                }
            }
    }

    /**
     * M\u00E9todo utilizado para eliminar bodega virtual y asociaciones de bodega de
     * un vendedor
     * 
     * @throws SQLException
     **/
    public static void deleteBodVendedor(Connection conn, List<VendedorPDV> idVend) throws SQLException {
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        ResultSet rs = null;
        String SqlDelete="";
	        for (int a = 0; a < idVend.size(); a++) {
	            String qry = "SELECT TCSCBODEGAVENDEDORID, TCSCBODEGAVIRTUALID FROM TC_SC_BODEGA_VENDEDOR WHERE VENDEDOR =? ";
	            try{
		            pstmt = conn.prepareStatement(qry);
		            pstmt.setBigDecimal(1, idVend.get(a).getVendedor());
		            rs = pstmt.executeQuery();
		
		            if (rs.next()) {
		                String delete = "DELETE FROM TC_SC_BODEGA_VENDEDOR WHERE tcscbodegavendedorid =?";
		                SqlDelete=delete;
		                log.trace("delete bodegaVendedor: ---> " + SqlDelete);
		            	try{
			                pstmt2 = conn.prepareStatement(SqlDelete);
			                pstmt2.setBigDecimal(1,  rs.getBigDecimal(BodegaVendedor.CAMPO_TCSCBODEGAVENDEDORID));
			                pstmt2.executeUpdate();
		            	}finally{
		            		DbUtils.closeQuietly(pstmt2);
		            	}
			
		                delete = "DELETE FROM TC_SC_BODEGAVIRTUAL WHERE TCSCBODEGAVIRTUALID = ?";
		                SqlDelete=delete;
		                log.trace("delete bodegaVirtual: ---> " + SqlDelete);
		            	try{
			                pstmt3 = conn.prepareStatement(SqlDelete);
			                pstmt3.setBigDecimal(1,  rs.getBigDecimal(BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID));
			                pstmt3.executeUpdate();
		            	}finally{
		            		DbUtils.closeQuietly(pstmt2);
		            		DbUtils.closeQuietly(pstmt3);
		            	}
		            }
	            }finally{
	         	   DbUtils.closeQuietly(pstmt);
	         	   DbUtils.closeQuietly(rs);
	            }	
	        }
    }

    public static int getVerificarBodegaVirtual(Connection conn, String idBodegaVirtual, String estadoAlta, BigDecimal idPais)
            throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, idBodegaVirtual));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BodegaVirtual.CAMPO_ESTADO, estadoAlta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TCSCCATPAISID, ""+idPais));
        
        return UtileriasBD.selectCount(conn, BodegaVirtual.N_TABLA, condiciones);
    }

    public static Map<String, String> getDatosBodVendedor(Connection conn, String idTipo, String tipo) throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorPDV.CAMPO_IDTIPO, idTipo));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorPDV.CAMPO_TIPO, tipo));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorPDV.CAMPO_RESPONSABLE, "1"));

        String sql = UtileriasBD.armarQuerySelectField(VendedorPDV.N_TABLA, VendedorPDV.CAMPO_VENDEDOR, condiciones,
                null);

        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, BodegaVendedor.CAMPO_VENDEDOR, sql));

        String[] campos={
            BodegaVendedor.CAMPO_TCSCBODEGAVENDEDORID,
            BodegaVendedor.CAMPO_TCSCBODEGAVIRTUALID
        };

        return UtileriasBD.getSingleFirstData(conn, BodegaVendedor.N_TABLA, campos, condiciones);
    }

    public static boolean updBodVendedor(Connection conn, Map<String, String> datosBodVendedor, String responsable,
            String nivel, String usuario, String nombreBodega) throws SQLException {
        Statement stmt = conn.createStatement();
        int[] updateCounts=null;
        String sqlUpdate="";
        try{
	        String update = updBodVendedor1(datosBodVendedor, responsable, usuario) ;
	        sqlUpdate=update;
	        log.trace(sqlUpdate);
	        stmt.addBatch(sqlUpdate);
	
	        update =updBodVendedor2(datosBodVendedor,nivel,  usuario,  nombreBodega) ;
	        sqlUpdate=update;
	        log.trace(sqlUpdate);
	        stmt.addBatch(sqlUpdate);
	
	        updateCounts = stmt.executeBatch();
        }finally{
        	DbUtils.closeQuietly(stmt);
        }

        return UtileriasJava.validarBatch(1, updateCounts);
    }
    
    public static String updBodVendedor1( Map<String, String> datosBodVendedor,String responsable,
          String usuario) {
    	  String update = "UPDATE " + BodegaVendedor.N_TABLA + " SET "
	                + BodegaVendedor.CAMPO_VENDEDOR + " = " + responsable + ", "
	                + BodegaVendedor.CAMPO_MODIFICADO_POR + " = '" + usuario + "', "
	                + BodegaVendedor.CAMPO_MODIFICADO_EL + " = SYSDATE" + " WHERE "
	                + BodegaVendedor.CAMPO_TCSCBODEGAVENDEDORID + " = " + datosBodVendedor.get(BodegaVendedor.CAMPO_TCSCBODEGAVENDEDORID);
    	  
    	  return update;
    }
    public static String updBodVendedor2( Map<String, String> datosBodVendedor,
            String nivel, String usuario, String nombreBodega) {
    	  String  update = "UPDATE " + BodegaVirtual.N_TABLA + " SET "
	                + BodegaVirtual.CAMPO_NIVEL + " = " + nivel + ", "
	                + BodegaVirtual.CAMPO_NOMBRE + " = '" + nombreBodega + "', "
	                + BodegaVirtual.CAMPO_MODIFICADO_POR + " = '" + usuario + "', "
	                + BodegaVirtual.CAMPO_MODIFICADO_EL + " = SYSDATE" + " WHERE "
	                + BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID + " = " + datosBodVendedor.get(BodegaVendedor.CAMPO_TCSCBODEGAVIRTUALID);
    	  
    	  return update;
    }
    
    
    public static boolean updateBodVendedor(Connection conn, String idVendedor, String estado, String usuario) throws SQLException {
        Statement stmt = conn.createStatement();
        int[] updateCounts=null;
        List<Filtro> condiciones = new ArrayList<Filtro>();
      String sqlUpdate="";
        try{
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVendedor.CAMPO_VENDEDOR, idVendedor));
	
	        String[] camposBodVendedor = {
	                BodegaVendedor.CAMPO_TCSCBODEGAVENDEDORID,
	                BodegaVendedor.CAMPO_TCSCBODEGAVIRTUALID
	        };
	
	        Map<String, String> datosBodVendedor = UtileriasBD.getSingleFirstData(conn, BodegaVendedor.N_TABLA,
	                camposBodVendedor, condiciones);
	
	        String update = updateVend(  estado,  usuario,datosBodVendedor ) ;
	        sqlUpdate=update;
	        log.trace(sqlUpdate);
	        stmt.addBatch(sqlUpdate);
	
	        update =  updateVend2(  estado,  usuario, datosBodVendedor ) ;
	        sqlUpdate=update;
	        log.trace(sqlUpdate);
	        stmt.addBatch(sqlUpdate);
	
	        updateCounts = stmt.executeBatch();
        }finally{
        	DbUtils.closeQuietly(stmt);
        }

        return UtileriasJava.validarBatch(1, updateCounts);
    }
    
    public static String updateVend( String estado, String usuario, Map<String, String> datosBodVendedor ) {
    	String update = "UPDATE " + BodegaVendedor.N_TABLA + " SET "
                + BodegaVendedor.CAMPO_ESTADO + " = '" + estado + "', "
                + BodegaVendedor.CAMPO_MODIFICADO_POR + " = '" + usuario + "', "
                + BodegaVendedor.CAMPO_MODIFICADO_EL + " = SYSDATE WHERE "
                + BodegaVendedor.CAMPO_TCSCBODEGAVENDEDORID + " = " + datosBodVendedor.get(BodegaVendedor.CAMPO_TCSCBODEGAVENDEDORID);
    	return update;
    }
    
    
    public static String updateVend2( String estado, String usuario, Map<String, String> datosBodVendedor ) {
    	String 
        update = "UPDATE " + BodegaVirtual.N_TABLA + " SET "
                + BodegaVirtual.CAMPO_ESTADO + " = '" + estado + "', "
                + BodegaVirtual.CAMPO_MODIFICADO_POR + " = '" + usuario + "', "
                + BodegaVirtual.CAMPO_MODIFICADO_EL + " = SYSDATE WHERE "
                + BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID + " = " + datosBodVendedor.get(BodegaVendedor.CAMPO_TCSCBODEGAVIRTUALID);
    	return update;
    }
}
