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

import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.ControladorBase;

public class OperacionIngresoSalida {
	private OperacionIngresoSalida(){}
    private static final Logger log = Logger.getLogger(OperacionIngresoSalida.class);

	/**
	 * M\u00E9todo para verificar si existe un articulo promocional en el inventario o por lo menos en el cat\u00E1logo de art\u00EDculos de alta
	 * @throws SQLException 
	 * */
    public static List<BigDecimal> existeArtPromo(Connection conn, String idArticulo, String estadoArt,
            String estadoInv, String idBodVirtual, String tipoInv, String codArea, BigDecimal idPais) throws SQLException {
        List<BigDecimal> lista = new ArrayList<BigDecimal>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;

       
			String query = "SELECT (SELECT COUNT (*)" +
					"     FROM TC_SC_ART_PROMOCIONAL" +
					"     WHERE TCSCCATPAISID = ?"+
					"        AND TCSCARTPROMOCIONALID =? AND ESTADO = ?)" +
					"  EXISTE," +
					"     NVL((SELECT CANTIDAD" +
					"     FROM " + ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodVirtual, codArea) +
					"     WHERE TCSCCATPAISID = ?"+
					"           AND ARTICULO =? " +
					"           AND TCSCBODEGAVIRTUALID = ?" +
					"           AND TIPO_INV =?" +
					"           AND ESTADO = ?),0)" +
					"  EXISTE_INV" +
					"  FROM DUAL" ;
            log.trace("select para validar si ya existe articulo:" + query);
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1, idPais);
            pstmt.setBigDecimal(2, new BigDecimal(idArticulo));
            pstmt.setString(3, estadoArt);
            pstmt.setBigDecimal(4, idPais);
            pstmt.setBigDecimal(5, new BigDecimal(idArticulo));
            pstmt.setBigDecimal(6, new BigDecimal(idBodVirtual));
            pstmt.setString(7, tipoInv);
            pstmt.setString(8, estadoInv);
            rst = pstmt.executeQuery();

            if (rst.next()) {
                lista.add(0, rst.getBigDecimal(1));
                lista.add(1, rst.getBigDecimal(2));
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return lista;
    }
	
	/**
	 * M\u00E9todo para actualizar la cantidad de inventario de un art\u00EDculo, 
	 * tipo: si se envia 1 se suma cantidad, si se envia 2 se resta
	 * @throws SQLException 
	 * */
    public static void updateCantInventario(Connection conn, String idArticulo, int cantidad, String estadoInv,
            String idBodVirtual, String tipoInv, String usuario, int tipo, String codArea, BigDecimal idPais) throws SQLException {
        String update = "";
        PreparedStatement pstmt = null;

     
             update=queryCantInventario(idBodVirtual, tipo, codArea, idPais);
        try {
            pstmt = conn.prepareStatement(update);

            pstmt.setInt(1, cantidad);
            pstmt.setString(2, usuario);
            pstmt.setBigDecimal(3, new BigDecimal(idArticulo));
            pstmt.setBigDecimal(4, new BigDecimal(idBodVirtual));
            pstmt.setString(5, tipoInv);
            pstmt.setString(6, estadoInv);

            pstmt.executeQuery();
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(pstmt);
        }
    }

    public static String queryCantInventario( String idBodVirtual, int tipo, String codArea, BigDecimal idPais) {
    	String update="";
    	   // suma cantidad
        if (tipo == 1) {
			update = "UPDATE " + ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodVirtual, codArea) +
					"   SET CANTIDAD = CANTIDAD + ?, MODIFICADO_POR=?, MODIFICADO_EL=SYSDATE " +
					" WHERE TCSCCATPAISID = " + idPais +
					"       AND ARTICULO = ? " +
					"       AND TCSCBODEGAVIRTUALID = ? " +
					"       AND TIPO_INV = ? " +
					"       AND ESTADO = ? " ;
		}

        // resta cantidad
        if (tipo == 2) {
			update = "UPDATE " + ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodVirtual, codArea) +
					"   SET CANTIDAD = CANTIDAD - ?, MODIFICADO_POR=?, MODIFICADO_EL=SYSDATE " +
					" WHERE TCSCCATPAISID = " + idPais +
                    "       AND ARTICULO = ? " +
					"       AND TCSCBODEGAVIRTUALID = ? " +
					"       AND TIPO_INV = ? " +
					"       AND ESTADO = ? " ;
		}
        return update;
    }
	/**
	 * M\u00E9todo para eliminar un art\u00EDculo del inventario
	 * @param conn
	 * @param idArticulo
	 * @param estadoInv
	 * @param idBodVirtual
	 * @param tipoInv
	 * @throws SQLException
	 */
    public static void deleteInv(Connection conn, String idArticulo, String estadoInv, String idBodVirtual,
            String tipoInv, String codArea, BigDecimal idPais) throws SQLException {

        PreparedStatement pstmt = null;
        
            String delete = querydeleteinv(idBodVirtual, codArea, idPais);
        try {
            pstmt = conn.prepareStatement(delete);

            pstmt.setBigDecimal(1, new BigDecimal(idArticulo));
            pstmt.setBigDecimal(2, new BigDecimal(idBodVirtual));
            pstmt.setString(3, tipoInv);
            pstmt.setString(4, estadoInv);

            pstmt.executeQuery();
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }
	
    public static String querydeleteinv( String idBodVirtual, String codArea, BigDecimal idPais) {
    	 String delete = "DELETE FROM " + ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodVirtual, codArea)
         + " WHERE TCSCCATPAISID = " + idPais
         + " ARTICULO = ? AND TCSCBODEGAVIRTUALID = ? AND TIPO_INV = ? AND ESTADO = ?";
    	 return delete;
    }
	/**
	 * M\u00E9todo para eliminar un art\u00EDculo del inventario en devoluciones
	 * @param conn
	 * @param idArticulo
	 * @param estadoInv
	 * @param idBodVirtual
	 * @param tipoInv
	 * @throws SQLException
	 */
    public static void deleteInvDev(Connection conn, String idArticulo, String estadoInv, String idBodVirtual,
            String tipoInv, String idSolicitud, String codArea, BigDecimal idPais) throws SQLException {
        String delete = queyrDeleteDev(idBodVirtual, codArea, idPais);
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(delete);

            pstmt.setBigDecimal(1, new BigDecimal(idArticulo));
            pstmt.setBigDecimal(2, new BigDecimal(idBodVirtual));
            pstmt.setString(3, tipoInv);
            pstmt.setString(4, estadoInv);
            pstmt.setBigDecimal(5, new BigDecimal(idSolicitud));

            pstmt.executeQuery();
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }
public static String queyrDeleteDev(String idBodVirtual, String codArea, BigDecimal idPais) {
	  String delete = "DELETE FROM " + ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodVirtual, codArea)
      + " WHERE TCSCCATPAISID = " + idPais
      + " AND ARTICULO = ? AND TCSCBODEGAVIRTUALID = ? AND TIPO_INV = ? AND ESTADO = ? AND TCSCSOLICITUDID = ?";
	  
	  return delete;
}
    public static void insertInv(Connection conn, Inventario obj, BigDecimal idPais) throws SQLException {
        PreparedStatement pstmt = null;

 
		String insert = "INSERT INTO TC_SC_INVENTARIO (tcscinventarioinvid, articulo, tcscbodegavirtualid, descripcion,"+ 
					"cantidad, seriado, estado, creado_el, creado_por, tipo_inv, tipo_grupo_sidra, tipo_grupo, tcsccatpaisid)      VALUES (TC_SC_INVENTARIO_SQ.nextval,"+
					"     ?, " +
					"     ?, " +
					"     (SELECT DESCRIPCION FROM TC_SC_ART_PROMOCIONAL WHERE TCSCCATPAISID = ? AND TCSCARTPROMOCIONALID=?), " +
					"     ?, " +
					"     ?, " +
					"     ?, " +
					"     SYSDATE, " +
					"     ?, " +
					"     ?, " +
					"     (SELECT TIPO_GRUPO FROM TC_SC_ART_PROMOCIONAL WHERE TCSCCATPAISID = ? AND TCSCARTPROMOCIONALID=?), " +
					"     (SELECT TIPO_GRUPO FROM TC_SC_ART_PROMOCIONAL WHERE TCSCCATPAISID = ? AND TCSCARTPROMOCIONALID=?), " +
					"     ?)" ;
         try {
            pstmt = conn.prepareStatement(insert);
            pstmt.setBigDecimal(1, obj.getArticulo());
            pstmt.setBigDecimal(2, obj.getTcscbodegavirtualid());
            pstmt.setBigDecimal(3, idPais);
            pstmt.setBigDecimal(4, obj.getArticulo());
            pstmt.setBigDecimal(5, obj.getCantidad());
            pstmt.setString(6, obj.getSeriado());
            pstmt.setString(7, obj.getEstado());
            pstmt.setString(8, obj.getCreado_por());
            pstmt.setString(9, obj.getTipo_inv());
            pstmt.setBigDecimal(10, idPais);
            pstmt.setBigDecimal(11, obj.getArticulo());
            pstmt.setBigDecimal(12, idPais);
            pstmt.setBigDecimal(13, obj.getArticulo());
            pstmt.setBigDecimal(14, idPais);

            pstmt.executeQuery();
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }
}
