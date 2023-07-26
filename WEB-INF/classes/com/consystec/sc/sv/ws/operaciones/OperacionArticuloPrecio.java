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

import com.consystec.sc.ca.ws.input.articuloprecio.InputArticuloPrecio;
import com.consystec.sc.ca.ws.input.articuloprecio.PrecioArticulo;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.JavaUtils;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionArticuloPrecio {
	private OperacionArticuloPrecio(){}
	private static final Logger log = Logger.getLogger(OperacionArticuloPrecio.class);
	
	public static BigDecimal insertArticuloPrecio(Connection conn, PrecioArticulo objeto) throws SQLException {
        BigDecimal ret = null;
        PreparedStatement pstmt = null;
		 
        String insert = " INSERT INTO TC_SC_PRECIO_ARTICULO"
            + "( "
                + "TCSCPRECIOARTICULOID, TCSCCATPAISID,"
                +  "ARTICULO,TIPO_GESTION,"               
                + "PRECIO, COD_MONEDA,"
                + "DES_MONEDA,ESTADO,"
                + "CREADO_EL, CREADO_POR,"
                + "ID_PRODUCT_OFFERING,PRECIO_MIN,PRECIO_MAX"
            + ") VALUES (?,?,?,?,?,?,?,?,sysdate,?,?,?,?)";

        log.trace("insert precio articulo: " + insert);
		try{
		        pstmt = conn.prepareStatement(insert);
		        ret = JavaUtils.getSecuencia(conn, PrecioArticulo.SEQUENCE);
		        pstmt.setBigDecimal(1, ret);
		        pstmt.setBigDecimal(2, objeto.getTcsccatpaisid());
		        pstmt.setBigDecimal(3, objeto.getArticulo());
		        pstmt.setString(4, objeto.getTipo_gestion());
		        pstmt.setBigDecimal(5, objeto.getPrecio());
		        pstmt.setString(6, objeto.getCod_moneda());
		        pstmt.setString(7, objeto.getDes_moneda());
		        pstmt.setString(8, objeto.getEstado());
		        pstmt.setString(9, objeto.getCreado_por());
		        pstmt.setBigDecimal(10, objeto.getId_product_offering());
		        pstmt.setBigDecimal(11, objeto.getPrecio_min());
		        pstmt.setBigDecimal(12, objeto.getPrecio_max());
		
		        int res = pstmt.executeUpdate();
		        if (res != 1) {
		            ret = new BigDecimal(0);
		            log.trace("Respuesta Insert: " + ret);
		        }
		}finally{
		
		        DbUtils.closeQuietly(pstmt);
		        log.trace("Devuelve Insert: " + ret);
		}
        return ret;
    }
	
	public static boolean existeArticuloPrecio(Connection conn, InputArticuloPrecio obj, BigDecimal paisId) throws SQLException {
        boolean ret = true;
        PreparedStatement pstmt = null;
        BigDecimal rstRes = null;
        ResultSet rst = null;
        String sql = "";

        try {
            sql = "SELECT COUNT (1) " ;
            sql = sql + "  FROM TC_SC_PRECIO_ARTICULO " 
             + " WHERE     TCSCCATPAISID =? "
             + "       AND ARTICULO = ?" 
             + "       AND ID_PRODUCT_OFFERING = ?"  ;

            log.trace("EXISTE PRECIO ARTICULO: " + sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, paisId);
            pstmt.setBigDecimal(2, new BigDecimal(obj.getIdArticulo()));
            pstmt.setBigDecimal(3, new BigDecimal(obj.getIdProductOffering()));
            rst = pstmt.executeQuery();

            if (rst.next()) {
            	rstRes = rst.getBigDecimal(1);
            	if(rstRes == new BigDecimal(0))
            		ret = false;
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return ret;
    }
	
	public static boolean existeOferta(Connection conn, InputArticuloPrecio obj, BigDecimal paisId) throws SQLException {
        boolean ret = true;
        PreparedStatement pstmt = null;
        BigDecimal rstRes = null;
        ResultSet rst = null;
        String sql = "";

        try {
        	sql = " SELECT COUNT(1) " ;
        	sql = sql + "    FROM TC_SC_OFERTA_SIDRA_FS O, TC_SC_CONFIGURACION CONF " 
        	+ "   WHERE     CONF.TCSCCATPAISID =  ?"
        	+ "         AND TO_CHAR(O.ID_PRODUCT_OFFERING) = CONF.VALOR " 
        	+ "         AND CONF.GRUPO LIKE 'TIPO_ARTICULO' " 
        	+ "         AND O.ID_PRODUCT_OFFERING = ?" ;


            log.trace("EXISTE OFERTA: " + sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1,paisId);
            pstmt.setBigDecimal(2, new BigDecimal(obj.getIdProductOffering()));
            rst = pstmt.executeQuery();

            if (rst.next()) {
            	rstRes = rst.getBigDecimal(1);
            	if(rstRes == new BigDecimal(0)) {
            		ret = false;
            		}
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return ret;
    }
	
	public static boolean existeArticulo(Connection conn, InputArticuloPrecio obj, BigDecimal paisId) throws SQLException {
        boolean ret = true;
        BigDecimal rstRes = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String sql = "";

        try {
        	sql = "SELECT COUNT (1) " 
        	+ "  FROM TC_SC_ARTICULO_INV " 
        	+ " WHERE ARTICULO =? AND TCSCCATPAISID = ?";

            log.trace("EXISTE ID_ARTICULO: " + sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, new BigDecimal(obj.getIdArticulo()));
            pstmt.setBigDecimal(2, paisId);
            rst = pstmt.executeQuery();

            if (rst.next()) {
            	
            	rstRes = rst.getBigDecimal(1);
            	if(rstRes == new BigDecimal(0)) {
            		ret = false;
            	}
                
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return ret;
    }
	
	public static boolean existePrecioGestion(Connection conn, InputArticuloPrecio obj, BigDecimal paisId) throws SQLException{
		boolean ret = true;
        BigDecimal rstRes = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String sql = "";

        try {
        	sql = "SELECT COUNT (1) " ;
        	sql = sql + "  FROM TC_SC_PRECIO_ARTICULO " ;
        	sql = sql + " WHERE ARTICULO =? AND TIPO_GESTION LIKE ? AND TCSCCATPAISID = ?" ;

        	
            log.trace("EXISTE PRECIO: " + sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, new BigDecimal(obj.getIdArticulo()));
            pstmt.setString(2, obj.getTipoGestion());
            pstmt.setBigDecimal(3, paisId);
            
            rst = pstmt.executeQuery();

            if (rst.next()) {
            	rstRes = rst.getBigDecimal(1);
            	if("0".equals(rstRes.toString())) {
            		ret = false;
            	}
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return ret;
	}
	
	public static boolean existePecio(Connection conn, InputArticuloPrecio obj, BigDecimal paisId) throws SQLException{
		boolean ret = true;
        BigDecimal rstRes = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String sql = "";

        try {
        	sql = "SELECT COUNT (1)  " ;
        	sql = sql + "  FROM TC_SC_PRECIO_ARTICULO  " ;
        	sql = sql + " WHERE TCSCCATPAISID =? AND ARTICULO =? AND TIPO_GESTION =?" ;

        	
            log.trace("EXISTE PRECIO: " + sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, paisId);
            pstmt.setBigDecimal(2, new BigDecimal(obj.getIdArticulo()));
            pstmt.setString(3, obj.getTipoGestion());
            rst = pstmt.executeQuery();

            if (rst.next()) {
            	rstRes = rst.getBigDecimal(1);
            	if("0".equals(rstRes.toString())) {
            		ret = false;
            	}
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return ret;
	}
	
	public static String getQuery( InputArticuloPrecio input,BigDecimal idPais) {
		String sql="";
		sql = "SELECT INV.ARTICULO ARTICULO, " ;
		sql = sql + "         INV.DESCRIPCION NOMBRE, " ;
		sql = sql + "		  'Test' NOMBRE_OFERTA, " ;
		sql = sql + "         ART.ID_PRODUCT_OFFERING ID_PRODUCT_OFFERING, " ;
		sql = sql + "         0 PRECIO, " ;
		sql = sql + "         ART.TIPO_GESTION TIPO_GESTION, " ;
		sql = sql + "         ART.ESTADO ESTADO, " ;
		sql = sql + "         ART.VERSION VERSION, " ;
		sql = sql + "         ART.CREADO_EL, " ;
		sql = sql + "         ART.CREADO_POR, " ;
		sql = sql + "         ART.MODIFICADO_EL, " ;
		sql = sql + "         ART.MODIFICADO_POR, " ;
		sql = sql + "		  ART.PRECIO_MIN PRECIO_MIN, " ;
		sql = sql + "         ART.PRECIO_MAX PRECIO_MAX " ;
		sql = sql + "    FROM TC_SC_PRECIO_ARTICULO ART, TC_SC_ARTICULO_INV INV " ;
		sql = sql + "   WHERE  INV.TCSCCATPAISID = ART.TCSCCATPAISID AND  INV.TCSCCATPAISID =  " + idPais;
		//sql = sql + "   AND OFER.ID_PRODUCT_OFFERING = ART.ID_PRODUCT_OFFERING " ;

		
		if(input.getIdArticulo() != null && !"".equals(input.getIdArticulo().trim()))
			sql = sql + "         AND ART.ARTICULO = " + input.getIdArticulo() ;
		
		if(input.getTipoGestion() != null && !"".equals(input.getTipoGestion().trim()))
			sql = sql + "         AND ART.TIPO_GESTION LIKE '"+input.getTipoGestion()+"' " ;
		
		if(input.getIdProductOffering() != null && !"".equals(input.getIdProductOffering().trim()))
			sql = sql + "         AND ART.ID_PRODUCT_OFFERING =  " + input.getIdProductOffering();
		
		if(input.getEstado() != null && !"".equals(input.getEstado().trim()))
			sql = sql + "         AND ART.ESTADO LIKE '"+input.getEstado()+"' " ;
		
		
		sql = sql +"  AND INV.TIPO_GRUPO_SIDRA != 'BONO' " ;
		sql = sql + " AND INV.ARTICULO = ART.ARTICULO ";
		
		sql = sql + " GROUP BY INV.ARTICULO, " ;
		sql = sql + "         INV.DESCRIPCION, " ;
		sql = sql + "         ART.ID_PRODUCT_OFFERING, " ;
		sql = sql + "         ART.PRECIO, " ;
		sql = sql + "         ART.TIPO_GESTION, " ;
		sql = sql + "         ART.ESTADO, " ;
		sql = sql + "         ART.VERSION, " ;
		sql = sql + "         ART.CREADO_EL, " ;
		sql = sql + "         ART.CREADO_POR, " ;
		sql = sql + "         ART.MODIFICADO_EL, " ;
		sql = sql + "         ART.MODIFICADO_POR, " ;
		sql = sql + "         ART.PRECIO_MIN, " ;
		sql = sql + "         ART.PRECIO_MAX " ;
		//sql = sql + "         OFER.NOMBRE, ";
		//sql = sql + "         OFER.PRECIO ";
		sql = sql + " ORDER BY ART.CREADO_EL ASC " ;
		
		return sql;
	}
	public static List<InputArticuloPrecio> doGet(Connection conn, InputArticuloPrecio input, BigDecimal idPais) throws SQLException{
		List<InputArticuloPrecio> lst = new ArrayList<InputArticuloPrecio>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String sql="";

		try {
			sql =  getQuery(  input, idPais);

			log.trace("QUERY OBTENER ARTICULO PRECIO: " + sql);
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				do {
					InputArticuloPrecio art = new InputArticuloPrecio();
					
					art.setIdArticulo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "ARTICULO"));
					art.setNombreArticulo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRE"));
					art.setNombreOferta(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRE_OFERTA"));
					art.setIdProductOffering(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "ID_PRODUCT_OFFERING"));
					art.setPrecio(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "PRECIO"));
					art.setTipoGestion(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "TIPO_GESTION"));
					art.setEstado(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "ESTADO"));
					art.setVersion(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "VERSION"));
					art.setCreadoEl(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, "CREADO_EL"));
					art.setCreadoPor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CREADO_POR"));
					art.setModificadoEl(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, "MODIFICADO_EL"));
					art.setModificadoPor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "MODIFICADO_POR"));
					art.setPrecioMin(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "PRECIO_MIN"));
					art.setPrecioMax(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "PRECIO_MAX"));
					
					lst.add(art);
				} while (rst.next());
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		return lst;
	}
	
	public static int updateArticuloPrecio(Connection conn, InputArticuloPrecio objeto, BigDecimal idPais) throws SQLException {
		int ret = 0;
		PreparedStatement pstmt = null;

		String update = " UPDATE TC_SC_PRECIO_ARTICULO SET "
				+ "ARTICULO = ?, "
				+ "PRECIO = ?, "
				+ "ID_PRODUCT_OFFERING = ?, "
				+ "MODIFICADO_EL = SYSDATE, "
				+ "MODIFICADO_POR = ?, "
				+ "PRECIO_MIN = ?, "
				+ "PRECIO_MAX = ? "
				+ "WHERE  ARTICULO = ? " 
				+ "AND TIPO_GESTION = ? " 
				+ "AND TCSCCATPAISID = ?" ;

		log.trace("UPDATE articulo precio: " + update);
		log.trace("nombre articulo: " + objeto.getIdArticulo());
		log.trace("tipo gestion: " + objeto.getTipoGestion());
		log.trace("precio:" + objeto.getPrecio());
		log.trace("id product offering:" + objeto.getIdProductOffering());
		try {
			pstmt = conn.prepareStatement(update);

			pstmt.setBigDecimal(1, new BigDecimal(objeto.getIdArticulo()));
			pstmt.setBigDecimal(2, new BigDecimal(objeto.getPrecio()));
			pstmt.setBigDecimal(3, new BigDecimal(objeto.getIdProductOffering()));
			pstmt.setString(4, objeto.getUsuario());

			if(objeto.getPrecioMin() != null && !"".equals(objeto.getPrecioMin())){
				pstmt.setBigDecimal(5, new BigDecimal(objeto.getPrecioMin()));
			}else{
				pstmt.setBigDecimal(5, null);
			}

			if(objeto.getPrecioMax() != null && !"".equals(objeto.getPrecioMax())){
				pstmt.setBigDecimal(6, new BigDecimal(objeto.getPrecioMax()));
			}else{
				pstmt.setBigDecimal(6, null);
			}

			pstmt.setBigDecimal(7, new BigDecimal(objeto.getIdArticulo()));
			pstmt.setString(8, objeto.getTipoGestion());
			pstmt.setBigDecimal(9, idPais);
			ret = pstmt.executeUpdate();
			if (ret > 0) {
				return 2;
			} else {
				return ret;
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
	}
}