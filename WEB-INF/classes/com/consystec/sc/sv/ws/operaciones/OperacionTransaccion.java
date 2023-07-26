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

import com.consystec.sc.ca.ws.input.tipotransaccion.InputTransaccionInv;
import com.consystec.sc.sv.ws.orm.TipoTransaccionInv;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.JavaUtils;
import com.consystec.sc.sv.ws.util.UtileriasBD;

public class OperacionTransaccion {
	private OperacionTransaccion(){}
    private static final Logger log = Logger.getLogger(OperacionTransaccion.class);

    public static BigDecimal insertTipoTransaccionInv(Connection conn, TipoTransaccionInv objeto) throws SQLException {
        BigDecimal ret = null;
        PreparedStatement pstmt = null;
        int res;
        
      
			String insert="INSERT INTO TC_SC_TIPOTRANSACCION (tcsctipotransaccionid, " +
					"                                   codigo_transaccion, " +
					"                                   descripcion, " +
					"                                   tipo_movimiento, " +
					"                                   tipo_afecta, " +
					"                                   estado, " +
					"                                   creado_el, " +
					"                                   creado_por, " +
					"                                   tcsccatpaisid) " 
				+") VALUES ( ?,?,?,?,?,?,sysdate,?,?)";
	
	        log.trace("insert TipoTransaccionInv:" + insert);
	      try{
	        pstmt = conn.prepareStatement(insert);
	        ret = JavaUtils.getSecuencia(conn, TipoTransaccionInv.SEQUENCE);
	        pstmt.setBigDecimal(1, ret);
	        pstmt.setString(2, objeto.getCodigo_transaccion());
	        pstmt.setString(3, objeto.getDescripcion());
	        pstmt.setString(4, objeto.getTipo_movimiento());
	        pstmt.setString(5, objeto.getTipo_afecta());
	        pstmt.setString(6, objeto.getEstado());
	        pstmt.setString(7, objeto.getCreado_por());
	        pstmt.setBigDecimal(8, objeto.getTcsccatpaisid());
	
	        res = pstmt.executeUpdate();
	        if (res != 1) {
	            ret = new BigDecimal(0);
	        }
        }finally{
        	DbUtils.closeQuietly(pstmt);
        }

        
        return ret;
    }
	
    public static BigDecimal updateTipoTransaccionInv(Connection conn, TipoTransaccionInv objeto) throws SQLException {
        BigDecimal ret = null;
        PreparedStatement pstmt = null;

      
	        String update = "UPDATE TC_SC_TIPOTRANSACCION " +
	        		"   SET descripcion = ?, " +
	        		"       estado = ?, " +
	        		"       tipo_afecta = ?, " +
	        		"       tipo_movimiento = ?, " +
	        		"       modificado_el = SYSDATE, " +
	        		"       modificado_por = ? " +
	        		" WHERE tcsctipotransaccionid = ? AND tcsccatpaisid = ? " ;
			
	        log.trace("UPDATE TipoTransaccionInv:" + update);
	        log.trace("nombre TipoTransaccionInv:" + objeto.getDescripcion());
	        log.trace("id TipoTransaccionInv:" + objeto.getTcsctipotransaccionid());
	        log.trace("estado TipoTransaccionInv:" + objeto.getEstado());
	     try{
	        pstmt = conn.prepareStatement(update);
	        pstmt.setString(1, objeto.getDescripcion());
	        pstmt.setString(2, objeto.getEstado());
	        pstmt.setString(3, objeto.getTipo_afecta());
	        pstmt.setString(4, objeto.getTipo_movimiento());
	        pstmt.setString(5, objeto.getModificado_por());
	        pstmt.setBigDecimal(6, objeto.getTcsctipotransaccionid());
	        pstmt.setBigDecimal(7, objeto.getTcsccatpaisid());
	        pstmt.executeUpdate();
        }finally{
        	DbUtils.closeQuietly(pstmt);
        }
        return ret;
    }

    /**
     * M\u00E9todo para verificar que el nombre de buzon a ingresar o modificar no exista
     * 
     */
    public static BigDecimal existeCodTipoTransaccionInv(Connection conn, String codTipoTransaccionInv, String estado,
            BigDecimal idTipoTransaccionInv, BigDecimal ID_PAIS) throws SQLException {
        BigDecimal ret = null;
        PreparedStatement pstmt = null;
	    ResultSet rst = null;
		
	   
		    String query="SELECT COUNT (1) " +
		    		"          FROM TC_SC_TipoTransaccion " +
		    		"         WHERE     UPPER (codigo_Transaccion) =? " +
		    		"               AND tcscTipoTransaccionid NOT IN (?) " +
		    		"               AND estado =? AND TCSCCATPAISID=?";
			
		    log.trace("valida codigo TipoTransaccionInv:"+ query);
		  try{
		    pstmt = conn.prepareStatement(query);
		    pstmt.setString(1, codTipoTransaccionInv.toUpperCase());
		    pstmt.setBigDecimal(2, idTipoTransaccionInv);
		    pstmt.setString(3, estado);
		    pstmt.setBigDecimal(4, ID_PAIS);
	        rst = pstmt.executeQuery();
	
	        if (rst.next()) {
	            ret = rst.getBigDecimal(1);
	
	        }
	    }finally{

	        DbUtils.closeQuietly(rst);
	        DbUtils.closeQuietly(pstmt);
	    }
        return ret;
    }

    public static List<InputTransaccionInv> getTipoTransaccionInv(Connection conn, List<Filtro> filtros)
            throws SQLException {
        List<InputTransaccionInv> lstBuzon = new ArrayList<InputTransaccionInv>();
        InputTransaccionInv objeto = new InputTransaccionInv();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        SimpleDateFormat FORMATO_FECHAHORA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHAHORA);
        
	        String sql = null;
	        String campos[] = {
	            TipoTransaccionInv.CAMPO_TCSCTIPOTRANSACCIONID,
	            TipoTransaccionInv.CAMPO_CODIGO_TRANSACCION,
	            TipoTransaccionInv.CAMPO_DESCRIPCION,
	            TipoTransaccionInv.CAMPO_TIPO_AFECTA,
	            TipoTransaccionInv.CAMPO_TIPO_MOVIMIENTO,
	            TipoTransaccionInv.CAMPO_ESTADO,
	            TipoTransaccionInv.CAMPO_CREADO_EL,
	            TipoTransaccionInv.CAMPO_CREADO_POR,
	            TipoTransaccionInv.CAMPO_MODIFICADO_EL,
	            TipoTransaccionInv.CAMPO_MODIFICADO_POR
	        };
	
	        sql = UtileriasBD.armarQuerySelect(TipoTransaccionInv.N_TABLA, campos, filtros);
	     try{
	        pstmt = conn.prepareStatement(sql);
	        rst = pstmt.executeQuery();
	
	        if (rst != null) {
	            while (rst.next()) {
	                objeto = new InputTransaccionInv();
	                objeto.setIdTipoTransaccion(rst.getBigDecimal(TipoTransaccionInv.CAMPO_TCSCTIPOTRANSACCIONID).toString());
	                objeto.setCodigoTransaccion(rst.getString(TipoTransaccionInv.CAMPO_CODIGO_TRANSACCION));
	                objeto.setDescripcion(rst.getString(TipoTransaccionInv.CAMPO_DESCRIPCION));
	                objeto.setTipoAfecta(rst.getString(TipoTransaccionInv.CAMPO_TIPO_AFECTA));
	                objeto.setEstado(rst.getString(TipoTransaccionInv.CAMPO_ESTADO));
	                objeto.setTipoMovimiento(rst.getString(TipoTransaccionInv.CAMPO_TIPO_MOVIMIENTO) == null ? " "
	                        : rst.getString(TipoTransaccionInv.CAMPO_TIPO_MOVIMIENTO));
	
	                objeto.setCreado_el(FORMATO_FECHAHORA.format(rst.getTimestamp(TipoTransaccionInv.CAMPO_CREADO_EL)));
	                if (rst.getDate(TipoTransaccionInv.CAMPO_MODIFICADO_EL) == null) {
	                    objeto.setModificado_el(" ");
	                } else {
	                    objeto.setModificado_el(FORMATO_FECHAHORA.format(rst.getTimestamp(TipoTransaccionInv.CAMPO_MODIFICADO_EL)));
	                }
	                objeto.setCreado_por(rst.getString(TipoTransaccionInv.CAMPO_CREADO_POR));
	                objeto.setModificado_por(rst.getString(TipoTransaccionInv.CAMPO_MODIFICADO_POR) == null ? " "
	                        : rst.getString(TipoTransaccionInv.CAMPO_MODIFICADO_POR));
	                lstBuzon.add(objeto);
	            }
	        }
        }finally
        {
        	DbUtils.closeQuietly(pstmt);
        	DbUtils.closeQuietly(rst);
        }
        return lstBuzon;
    }
}
