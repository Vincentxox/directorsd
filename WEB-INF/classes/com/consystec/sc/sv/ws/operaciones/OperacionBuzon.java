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

import com.consystec.sc.ca.ws.input.buzon.InputBuzon;
import com.consystec.sc.sv.ws.orm.BuzonSidra;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.JavaUtils;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionBuzon {
	private OperacionBuzon(){}
    private static final Logger log = Logger.getLogger(OperacionBuzon.class);

    public static BigDecimal insertBuzon(Connection conn, BuzonSidra objeto) throws SQLException {
        BigDecimal ret = null;
        PreparedStatement pstmt = null;
		 
        String insert = " INSERT INTO TC_SC_BUZONSIDRA" 
            + "( tcscbuzonid, NOMBRE , TCSCDTSID,"
                + "NIVEL,TIPO_WORKFLOW, ESTADO ,"
                + "CREADO_EL, CREADO_POR ,"
                + "TCSCCATPAISID ,TCSCBODEGAVIRTUALID) VALUES (?,?,?,?,?,?,sysdate,?,?,?)";

        log.trace("insert buzon:" + insert);
       try{
	        pstmt = conn.prepareStatement(insert);
	        ret = JavaUtils.getSecuencia(conn, BuzonSidra.SEQUENCE);
	        pstmt.setBigDecimal(1, ret);
	        pstmt.setString(2, objeto.getNombre());
	        pstmt.setBigDecimal(3, objeto.getTcscdtsid());
	        pstmt.setBigDecimal(4, objeto.getNivel());
	        pstmt.setString(5, objeto.getTipo_workflow());
	        pstmt.setString(6, objeto.getEstado());
	        pstmt.setString(7, objeto.getCreado_por());
	        pstmt.setString(8, objeto.getTcsccatpaisid().toString());
	        pstmt.setBigDecimal(9, objeto.getTcscbodegavirtualid());
	
	        int res = pstmt.executeUpdate();
	        if (res != 1) {
	            ret = new BigDecimal(0);
	        }
       }finally{

        DbUtils.closeQuietly(pstmt);
       }
        return ret;
    }

    public static int updateBuzon(Connection conn, BuzonSidra objeto) throws SQLException {
        int ret = 0;
        PreparedStatement pstmt = null;

        String update = " UPDATE TC_SC_BUZONSIDRA SET "
                + "NOMBRE  = ?, "
                + "ESTADO  = ?, "
                + " MODIFICADO_EL = SYSDATE, "
                + " MODIFICADO_POR  = ? "
            + " WHERE TCSCBUZONID  = ?";

        log.trace("UPDATE buzon:" + update);
        log.trace("nombre buzon:" + objeto.getNombre());
        log.trace("id buzon:" + objeto.getTcscbuzonid());
        log.trace("estado buzon:" + objeto.getEstado());
        
        try{
	        pstmt = conn.prepareStatement(update);
	
	        pstmt.setString(1, objeto.getNombre());
	        pstmt.setString(2, objeto.getEstado());
	        pstmt.setString(3, objeto.getModificado_por());
	        pstmt.setBigDecimal(4, objeto.getTcscbuzonid());
	        ret = pstmt.executeUpdate();
	        DbUtils.closeQuietly(pstmt);
	
	        if (ret > 0) {
	            return objeto.getTcscbuzonid().intValue();
	        } else {
	            return ret;
	        }
        }finally{
        	DbUtils.closeQuietly(pstmt);
        }
    }

    /**
     * M\u00E9todo para verificar que el nombre de buzon a ingresar o modificar no
     * exista en el mismo distribuidor
     * @param estadoAlta 
     * @param nivel 
     */
    public static BigDecimal existeBuzon(Connection conn, String buzon, BigDecimal idBuzon, BigDecimal idDTS,
            String estadoAlta, String nivel, BigDecimal idBodegaVirtual, BigDecimal idPais) throws SQLException {
        BigDecimal ret = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        PreparedStatement pstmt1 = null;
        ResultSet rst1 = null;
        String query = "";
        String filtroDTS = "";
        String filtroBodegaVirtual = "";

      
            if (nivel == null) {
                query = "SELECT NIVEL FROM TC_SC_BUZONSIDRA WHERE TCSCBUZONID =? AND TCSCCATPAISID = ?";
                try {
	                pstmt = conn.prepareStatement(query);
	                pstmt.setBigDecimal(1, idBuzon);
	                pstmt.setBigDecimal(2,idPais );
	                rst = pstmt.executeQuery();
	
	                if (rst.next()) {
	                    nivel = rst.getString(1);
	                }
                }finally{
                    DbUtils.closeQuietly(rst);
        	        DbUtils.closeQuietly(pstmt);
                
                }
            }
        
      
            if (new Integer(nivel) == Conf.NIVEL_BUZON_TELCA) {
                filtroDTS = " AND TCSCDTSID IS NULL";
            }else if (new Integer(nivel) == Conf.NIVEL_ZONACOMERCIAL){
            	if(idBodegaVirtual==null){
            		filtroBodegaVirtual="";
            	}else{
            		filtroBodegaVirtual=" AND TCSCBODEGAVIRTUALID="+ idBodegaVirtual;
            	}
            }
            else {
                filtroDTS = " AND TCSCDTSID = " + idDTS;
            }

            query =  getQueryBuzon(   filtroDTS,     filtroBodegaVirtual);

            log.trace("valida nombre buzon:" + query);
       try{ 
            pstmt1 = conn.prepareStatement(query);
            pstmt1.setString(1,  buzon.toUpperCase());
            pstmt1.setBigDecimal(2,idBuzon);
            pstmt1.setString(3, estadoAlta.toUpperCase());
            pstmt1.setBigDecimal(4,  idPais);
            pstmt1.setBigDecimal(5, new BigDecimal(nivel));
            rst1 = pstmt1.executeQuery();

            if (rst1.next()) {
                ret = rst1.getBigDecimal(1);
            }
        } finally {
            DbUtils.closeQuietly(rst1);
            DbUtils.closeQuietly(pstmt1);
        }

        return ret;
    }
    
    public static String getQueryBuzon(  String filtroDTS,    String filtroBodegaVirtual) {
    	String   query = "SELECT COUNT(1) FROM TC_SC_BUZONSIDRA "
                + "WHERE UPPER(NOMBRE) =?"
                + " AND TCSCBUZONID NOT IN (?)"
                + " AND UPPER(ESTADO) =?"
                + " AND TCSCCATPAISID =?"
                + "	AND NIVEL = ?" 
                + filtroDTS+" "+filtroBodegaVirtual;
    	return query;
    }

    /**
     * M\u00E9todo para verificar que el buzon no tenga usuarios asociados
     */

    public static BigDecimal existeUsuarioBuzon(Connection conn, BigDecimal idBuzon, String estadoAlta, BigDecimal idPais)
            throws SQLException {
        BigDecimal ret = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        String query = "SELECT COUNT (1) " + 
		"  FROM TC_SC_USUARIO_BUZON " +
		" WHERE TCSCBUZONID = ?" +
		" AND upper(estado)=?" +
		" AND tcsccatpaisid = ?" ;

        log.trace("valida nombre buzon:" + query);
        try{
	        pstmt = conn.prepareStatement(query);
	        pstmt.setBigDecimal(1, idBuzon);
	        pstmt.setString(2, estadoAlta.toUpperCase());
	        pstmt.setBigDecimal(3, idPais);
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

    public static List<InputBuzon> getBuzones(Connection conn, List<Filtro> filtros, String nivel, BigDecimal idPais) throws SQLException {
        List<InputBuzon> lstBuzon = new ArrayList<InputBuzon>();
        InputBuzon objeto = new InputBuzon();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        SimpleDateFormat FORMATO_FECHAHORA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHAHORA);
        String sql = null;

        String tablas[] = { BuzonSidra.N_TABLA, Distribuidor.N_TABLA };
        
        String campos[] []= {
           {BuzonSidra.N_TABLA, BuzonSidra.CAMPO_TCSCBUZONID},
           {BuzonSidra.N_TABLA, BuzonSidra.CAMPO_TCSCDTSID},
           {Distribuidor.N_TABLA, Distribuidor.CAMPO_NOMBRES},
           {BuzonSidra.N_TABLA, BuzonSidra.CAMPO_TCSCBODEGAVIRTUALID},
           {null,"(SELECT NOMBRE FROM TC_SC_BODEGAVIRTUAL WHERE "
           		+ "TCSCCATPAISID="+idPais+" "
           				+ "AND TCSCBODEGAVIRTUALID="+BuzonSidra.N_TABLA+"."+BuzonSidra.CAMPO_TCSCBODEGAVIRTUALID+") AS NOMBRE_BODEGA"},
           {BuzonSidra.N_TABLA, BuzonSidra.CAMPO_NOMBRE},
           {BuzonSidra.N_TABLA, BuzonSidra.CAMPO_NIVEL},
           {BuzonSidra.N_TABLA, BuzonSidra.CAMPO_TIPO_WORKFLOW},
           {BuzonSidra.N_TABLA, BuzonSidra.CAMPO_ESTADO},
           {BuzonSidra.N_TABLA, BuzonSidra.CAMPO_CREADO_EL},
           {BuzonSidra.N_TABLA, BuzonSidra.CAMPO_CREADO_POR},
           {BuzonSidra.N_TABLA, BuzonSidra.CAMPO_MODIFICADO_EL},
           {BuzonSidra.N_TABLA, BuzonSidra.CAMPO_MODIFICADO_POR}
        };

        List<Order> orden = new ArrayList<Order>();
        orden.add(new Order(BuzonSidra.N_TABLA+"."+BuzonSidra.CAMPO_NOMBRE, Order.ASC));

        if(nivel.equals(""+Conf.NIVEL_BUZON_DTS)){
	        filtros.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.N_TABLA,
	                Distribuidor.CAMPO_TC_SC_DTS_ID, BuzonSidra.N_TABLA, BuzonSidra.CAMPO_TCSCDTSID));
        }
        
        if(nivel.equals(""+Conf.NIVEL_ZONACOMERCIAL)){
        	filtros.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.N_TABLA,
	                Distribuidor.CAMPO_TC_SC_DTS_ID, BuzonSidra.N_TABLA, BuzonSidra.CAMPO_TCSCDTSID));
        }
        
        sql = UtileriasBD.armarQueryGetMultiple(campos, tablas, filtros, orden);
        try{
	        pstmt = conn.prepareStatement(sql);
	        rst = pstmt.executeQuery();
	
	        if (rst != null) {
	            while (rst.next()) {
	                objeto = new InputBuzon();
	                objeto.setIdBuzon(rst.getBigDecimal(BuzonSidra.CAMPO_TCSCBUZONID).toString());
	                objeto.setNombre(rst.getString(BuzonSidra.CAMPO_NOMBRE));
	                objeto.setEstado(rst.getString(BuzonSidra.CAMPO_ESTADO));
	                if (rst.getString(BuzonSidra.CAMPO_TCSCDTSID) == null) {
	                    objeto.setIdDistribuidor(rst.getString(" "));
	                    objeto.setNombreDistribuidor(rst.getString(" "));
	                } else {
	                    objeto.setIdDistribuidor(rst.getString(BuzonSidra.CAMPO_TCSCDTSID));
	                    objeto.setNombreDistribuidor(rst.getString(Distribuidor.CAMPO_NOMBRES));
	                }
	
	                
	                if (rst.getString(BuzonSidra.CAMPO_TCSCBODEGAVIRTUALID) == null) {
	                    objeto.setIdBodegaVirtual("");
	                    objeto.setNombreBodega("");
	                } else {
	                    objeto.setIdBodegaVirtual(rst.getString(BuzonSidra.CAMPO_TCSCBODEGAVIRTUALID));
	                    objeto.setNombreBodega(rst.getString("NOMBRE_BODEGA"));
	                }
	                
	                objeto.setNivel(rst.getString(BuzonSidra.CAMPO_NIVEL));
	                objeto.setTipoWF(rst.getString(BuzonSidra.CAMPO_TIPO_WORKFLOW));
	
	                objeto.setCreado_el(FORMATO_FECHAHORA.format(rst.getTimestamp(BuzonSidra.CAMPO_CREADO_EL)));
	                if (rst.getDate(BuzonSidra.CAMPO_MODIFICADO_EL) == null) {
	                    objeto.setModificado_el(" ");
	                } else {
	                    objeto.setModificado_el(
	                            FORMATO_FECHAHORA.format(rst.getTimestamp(BuzonSidra.CAMPO_MODIFICADO_EL)));
	                }
	                objeto.setCreado_por(rst.getString(BuzonSidra.CAMPO_CREADO_POR));
	                objeto.setModificado_por(rst.getString(BuzonSidra.CAMPO_MODIFICADO_POR) == null ? " "
	                        : rst.getString(BuzonSidra.CAMPO_MODIFICADO_POR));
	                lstBuzon.add(objeto);
	            }
	        }
		}finally{
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}
        return lstBuzon;
    }

    /***
     * M\u00E9todo para obtener buzones de nivel 1
     * 
     * @param conn
     * @param filtros
     * @return
     * @throws SQLException
     */
    public static List<InputBuzon> getBuzonesNivel1(Connection conn, List<Filtro> filtros) throws SQLException {
        List<InputBuzon> lstBuzon = new ArrayList<InputBuzon>();
        InputBuzon objeto ;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        SimpleDateFormat FORMATO_FECHAHORA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHAHORA);
        String sql = null;

        String campos[] = {
	            BuzonSidra.CAMPO_TCSCBUZONID,
	            BuzonSidra.CAMPO_NOMBRE,
	            BuzonSidra.CAMPO_ESTADO,
	            BuzonSidra.CAMPO_CREADO_EL,
	            BuzonSidra.CAMPO_CREADO_POR,
	            BuzonSidra.CAMPO_MODIFICADO_EL,
	            BuzonSidra.CAMPO_MODIFICADO_POR,
	            BuzonSidra.CAMPO_NIVEL,
		        BuzonSidra.CAMPO_TIPO_WORKFLOW
        };

        List<Order> orden = new ArrayList<Order>();
        orden.add(new Order(BuzonSidra.CAMPO_NOMBRE, Order.ASC));

        sql = UtileriasBD.armarQuerySelect(BuzonSidra.N_TABLA, campos, filtros, orden);
        log.trace("Query buzon nivel 1:" + sql);
        try{
	        pstmt = conn.prepareStatement(sql);
	        rst = pstmt.executeQuery();
	
	        if (rst != null) {
	            while (rst.next()) {
	                objeto = new InputBuzon();
	                objeto.setIdBuzon(rst.getBigDecimal(BuzonSidra.CAMPO_TCSCBUZONID).toString());
	                objeto.setNombre(rst.getString(BuzonSidra.CAMPO_NOMBRE));
	                objeto.setEstado(rst.getString(BuzonSidra.CAMPO_ESTADO));
	                objeto.setCreado_el(FORMATO_FECHAHORA.format(rst.getTimestamp(BuzonSidra.CAMPO_CREADO_EL)));
	                if (rst.getDate(BuzonSidra.CAMPO_MODIFICADO_EL) == null) {
	                    objeto.setModificado_el(" ");
	                } else {
	                    objeto.setModificado_el(
	                            FORMATO_FECHAHORA.format(rst.getTimestamp(BuzonSidra.CAMPO_MODIFICADO_EL)));
	                }
	                objeto.setCreado_por(rst.getString(BuzonSidra.CAMPO_CREADO_POR));
	                objeto.setModificado_por(rst.getString(BuzonSidra.CAMPO_MODIFICADO_POR) == null ? " "
	                        : rst.getString(BuzonSidra.CAMPO_MODIFICADO_POR));
	                objeto.setNivel(rst.getString(BuzonSidra.CAMPO_NIVEL));
	                objeto.setTipoWF(rst.getString(BuzonSidra.CAMPO_TIPO_WORKFLOW));
	                lstBuzon.add(objeto);
	            }
	        }
        }finally{
        	DbUtils.closeQuietly(pstmt);
        	DbUtils.closeQuietly(rst);
        }
        return lstBuzon;
    }
}
