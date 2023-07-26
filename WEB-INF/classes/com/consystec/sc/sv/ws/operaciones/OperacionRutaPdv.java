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

import com.consystec.sc.sv.ws.orm.RutaPDV;

public class OperacionRutaPdv {
	private OperacionRutaPdv(){}
    private static final Logger log = Logger.getLogger(OperacionRutaPdv.class);

	/**
	 * M\u00E9todo para determinar si pdv existe o se encuentra disponible para ser asociado a una ruta
	 * @param idPdv
	 * @param estadoActivo
	 * @return  Lista string Index 0: Valor entero que indica si existe de alta el pdv
	 * 						 Index 1: Texto de ruta, si trae nombre de ruta quiere decir que el pdv ya esta asociado a una ruta.	
	 * @throws SQLException 
	 */
    public static List<String> pdvValido(Connection conn, String idPdv, String estadoActivo, String estadoAlta, BigDecimal ID_PAIS)
            throws SQLException {
        List<String> ret = new ArrayList<String>();
        String query = "";
        PreparedStatement pstmt = null;
        ResultSet rst = null;

      
            query = "SELECT (SELECT COUNT (1) " +
					"          FROM TC_SC_PUNTOVENTA PV " +
					"         WHERE     upper(PV.ESTADO) = ? " +
					"                AND TCSCPUNTOVENTAID = ? " +
					"                AND TCSCCATPAISID = ?) " +
					"          EXISTE, " +
					"       (  SELECT R.NOMBRE AS RUTA " +
					"          FROM TC_SC_PUNTOVENTA PV, TC_sC_RUTA R, TC_SC_RUTA_PDV RP " +
					"         WHERE     upper(PV.ESTADO) =?" +
					"               AND upper(R.ESTADO)=? " +
					"               AND PV.TCSCDTSID=R.TCSCDTSID " +
					"               AND PV.TCSCPUNTOVENTAID=RP.TCSCPUNTOVENTAID " +
					"               AND RP.TCSCRUTAID=RP.TCSCRUTAID " + /**TODO confirmar por qué se necesita esta linea*/
					"               AND PV.TCSCPUNTOVENTAID=? "+ 
					"               AND PV.TCSCCATPAISID = ? "+ 
					"               AND PV.TCSCCATPAISID = R.TCSCCATPAISID) "+ 
					"          DISPONIBLE " +
					"  FROM DUAL "; 
            log.trace("query valida pdv:" + query);
            try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, estadoActivo.toUpperCase());
            pstmt.setBigDecimal(2, new BigDecimal(idPdv));
            pstmt.setBigDecimal(3, ID_PAIS);
            pstmt.setString(4, estadoActivo.toUpperCase());
            pstmt.setString(5, estadoAlta.toUpperCase());
            pstmt.setBigDecimal(6, new BigDecimal(idPdv));
            pstmt.setBigDecimal(7, ID_PAIS);
            rst = pstmt.executeQuery();

            if (rst.next()) {
                ret.add(0, rst.getString(1));
                ret.add(1, rst.getString(2));
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return ret;
    }

    /**
     * Insertar asociaciones
     * 
     * @param conn
     * @param objeto
     * @param estadoAlta
     * @throws SQLException
     */
    public static void insertAsociacion(Connection conn, RutaPDV objeto, String estadoAlta, BigDecimal ID_PAIS) throws SQLException {
        PreparedStatement pstmt = null;

 
            String insert = "INSERT INTO TC_SC_RUTA_PDV (tcscpuntoventaid, " +
            		"                            tcscrutaid, " +
            		"                            creado_el, " +
            		"                            creado_por, " +
            		"                            tcsccatpaisid, " +
            		"                            estado) " +
            		"     VALUES (?, " +
            		"             ?, " +
            		"             SYSDATE, " +
            		"             ?, " +
            		"             ?, " +
            		"             ?) "; 


            log.debug("insert ruta_pdv:" + insert);
           try {
            pstmt = conn.prepareStatement(insert);

            pstmt.setBigDecimal(1, objeto.getTcscpuntoventaid());
            pstmt.setBigDecimal(2, objeto.getTcscrutaid());
            pstmt.setString(3, objeto.getCreado_por());
            pstmt.setBigDecimal(4, ID_PAIS);
            pstmt.setString(5, estadoAlta);
            pstmt.executeUpdate();

        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }

    /**
     * M\u00E9todo para eliminar asociaciones
     * 
     * @param conn
     * @param objeto
     * @throws SQLException
     */
    public static void deleteAsociacion(Connection conn, RutaPDV objeto) throws SQLException {
        PreparedStatement pstmt = null;

     
            String query = "";

            query = "DELETE FROM TC_SC_RUTA_PDV " +
            		"      WHERE tcscrutaid = ? AND tcsccatpaisid = ? AND tcscpuntoventaid = ? " ;


            log.trace("ELIMINACION ASOCIACION:" + query);
         try {
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1,objeto.getTcscrutaid());
            pstmt.setBigDecimal(2, objeto.getTcsccatpaisid());
            pstmt.setBigDecimal(3, objeto.getTcscpuntoventaid());
            pstmt.executeUpdate();
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }
}
