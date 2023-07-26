package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.adjuntogestion.InputAdjuntoGestion;
import com.consystec.sc.sv.ws.orm.Adjunto;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.JavaUtils;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionAdjuntoGestion {
	private OperacionAdjuntoGestion(){}
    private static final Logger log = Logger.getLogger(OperacionAdjuntoGestion.class);

	/**
	 * M\u00E9todo utilizado para subir adjuntos.
	 * 
	 * @param conn
	 * @param archivo
	 * @param input
	 * @param idPais
	 * @return
	 * @throws SQLException
	 */
    public static BigDecimal saveAdjunto(Connection conn, byte[] archivo, InputAdjuntoGestion input, BigDecimal idPais, String codArea)
            throws SQLException {
        PreparedStatement pstmt = null;

        
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, codArea);
            BigDecimal idAdjunto = JavaUtils.getSecuencia(conn, Adjunto.SEQUENCE);

            String query = "INSERT INTO TC_SC_ADJUNTO ("
		        +"tcscadjuntoid, "
		        + "TCSCCATPAISID , "
		        + "tcscgestionid, "
		        + "gestion, "
		        + "ARCHIVO, "
		        + "NOMBRE_ARCHIVO , "
		        + "TIPO_ARCHIVO , "
		        +"EXTENSION , "
		        +"TIPO_DOCUMENTO, "
		        + "ESTADO , "
		        +"CREADO_EL, "
		        +"CREADO_POR ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?)";

            log.debug("QryInsertAdjunto: " + query);
           try {   
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1, idAdjunto);
            pstmt.setBigDecimal(2, idPais);
            if (input.getGestion().equalsIgnoreCase("PORTABILIDAD"))
            {
            	pstmt.setBigDecimal(3, null);
            }
            else
            {
            	pstmt.setBigDecimal(3, new BigDecimal(input.getIdGestion()));
            }
            pstmt.setString(4, input.getGestion().toUpperCase());
            pstmt.setBytes(5, archivo);
            pstmt.setString(6, input.getNombreArchivo());
            pstmt.setString(7, input.getTipoArchivo());
            pstmt.setString(8, input.getExtension());
            pstmt.setString(9, input.getTipoDocumento());
            pstmt.setString(10, estadoAlta);
            pstmt.setString(11, input.getUsuario());

            int ret = pstmt.executeUpdate();

            if (ret > 0) {
                return idAdjunto;
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
        return null;
    }

	/**
	 * M\u00E9todo utilizado para consultar adjuntos.
	 * 
	 * @param conn
	 * @param idAdjunto
	 * @param idGestion
	 * @param gestion
	 * @return
	 * @throws SQLException
	 */
    public  static String getQueryAdjunto (boolean filtroIdAjunto,boolean filtroIdGestion,BigDecimal idAdjunto, BigDecimal idGestion,
            String gestion) {

        String query = "SELECT TCSCADJUNTOID, TCSCGESTIONID , "
            +"GESTION, ARCHIVO , NOMBRE_ARCHIVO, TIPO_ARCHIVO , "
            + " EXTENSION ,TIPO_DOCUMENTO FROM TC_SC_ADJUNTO"
        + " WHERE TCSCCATPAISID  = ?" 
        + (filtroIdAjunto ? " AND TCSCADJUNTOID = " + idAdjunto : "")
        + (filtroIdGestion ? " AND TCSCGESTIONID = " + idGestion
                + " AND UPPER(GESTION) = '" + gestion.toUpperCase() + "'" : "");
        return query;
    }
    public static InputAdjuntoGestion getAdjunto(Connection conn, BigDecimal idAdjunto, BigDecimal idGestion,
            String gestion, BigDecimal idPais) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        InputAdjuntoGestion obj = new InputAdjuntoGestion();
        boolean filtroIdAjunto = false;
        boolean filtroIdGestion = false;

        if (idAdjunto != null) {
            filtroIdAjunto = true;
        }
        if (idGestion != null) {
            filtroIdGestion = true;
        }

     
            String query = getQueryAdjunto ( filtroIdAjunto, filtroIdGestion, idAdjunto,  idGestion,
                     gestion);

            log.debug("QryGetAdjunto: " + query);
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1, idPais);

            rst = pstmt.executeQuery();

            if (rst.next()) {
                Blob bin = rst.getBlob(Adjunto.CAMPO_ARCHIVO);
                int blobLength = (int) bin.length();
                byte[] blobAsBytes = bin.getBytes(1, blobLength);

                obj.setIdAdjunto(rst.getString(Adjunto.CAMPO_TCSCADJUNTOID));
                obj.setIdGestion(rst.getString(Adjunto.CAMPO_TCSCGESTIONID));
                obj.setGestion(rst.getString(Adjunto.CAMPO_GESTION));
                obj.setArchivo(blobAsBytes);
                obj.setNombreArchivo(rst.getString(Adjunto.CAMPO_NOMBRE_ARCHIVO));
                obj.setTipoArchivo(rst.getString(Adjunto.CAMPO_TIPO_ARCHIVO));
                obj.setExtension(rst.getString(Adjunto.CAMPO_EXTENSION));
                obj.setTipoDocumento(rst.getString(Adjunto.CAMPO_TIPO_DOCUMENTO));
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }

        return obj;
    }

    
    public static boolean delImagen(Connection conn, BigDecimal idAdjunto, BigDecimal idPais) throws SQLException {
        PreparedStatement pstmt = null;
       
            String query = "DELETE FROM TC_SC_ADJUNTO"
                + " WHERE TCSCADJUNTOID  = ?"
                + " AND TCSCCATPAISID  = ?";

            log.debug("QryDelImgPdv: " + query);
           try {
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1, idAdjunto);
            pstmt.setBigDecimal(2, idPais);

            int ret = pstmt.executeUpdate();
            if (ret > 0) {
                return true;
            } else {
                return false;
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }
}
