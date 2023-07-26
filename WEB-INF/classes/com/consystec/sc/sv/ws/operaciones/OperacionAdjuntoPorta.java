package com.consystec.sc.sv.ws.operaciones;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import com.consystec.sc.ca.ws.input.portabilidad.InputCargaAdjuntoPorta;
import com.consystec.sc.sv.ws.orm.AdjuntoPorta;
import com.consystec.sc.sv.ws.util.JavaUtils;


public class OperacionAdjuntoPorta {
	private OperacionAdjuntoPorta(){}
	private static final Logger log = Logger.getLogger(OperacionAdjuntoGestion.class);
	
	public static BigDecimal saveAdjunto(Connection conn,  InputCargaAdjuntoPorta input)
            throws SQLException {
        PreparedStatement pstmt = null;

    
            BigDecimal idAdjuntoPorta = JavaUtils.getSecuencia(conn, AdjuntoPorta.SEQUENCE);

            String query = "INSERT INTO TC_SC_ADJUNTO_PORTA ("
		        + "tcscadjuntoportaid, "
		        + "tcscportabilidadid, "
		        + "idportamovil, "
		        + "nombre_archivo, "
		        + "extension, "
		        + "tipo_archivo, "
		        + "idattachment, "
		        +"url_repositorio, "
		        + "creado_el, "
		        + "CREADO_POR, "
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?";

            log.debug("QryInsertAdjunto: " + query);
      try {
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1, idAdjuntoPorta);
            pstmt.setBigDecimal(2, new BigDecimal(input.getIdPortabilidad()));
            pstmt.setBigDecimal(3, new BigDecimal(input.getIdPortaMovil()));
            pstmt.setString(4, input.getNombreArchivo());
            pstmt.setString(5, input.getExtension());
            pstmt.setString(6, input.getTipoArchivo());
            pstmt.setBigDecimal(7, new BigDecimal(input.getIdAttachment()));
            pstmt.setString(8, "");//url del repositorio
            pstmt.setString(9, input.getUsuario());
         

            int ret = pstmt.executeUpdate();

            if (ret > 0) {
            	
            	//enviar archvivo a repositorio 
                return idAdjuntoPorta;
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
        return null;
    }
}
