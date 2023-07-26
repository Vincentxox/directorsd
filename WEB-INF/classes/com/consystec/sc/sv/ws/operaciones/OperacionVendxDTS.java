package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.output.vendedorxdts.VendedorxDTS;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;


public class OperacionVendxDTS {
private OperacionVendxDTS(){}
    private static final Logger log = Logger.getLogger(OperacionVendxDTS.class);

    /**
     * M\u00E9todo para obtener valores de vendedor al momento de loguearse
     * 
     * @throws SQLException
     * @throws NamingException
     * @throws ExcepcionSeguridad
     */
    public static List<VendedorxDTS> getValorVendedor(Connection conn,  List<Filtro> filtros, BigDecimal idPais) throws SQLException,  NamingException {
        VendedorxDTS respuesta = new VendedorxDTS();
        List<VendedorxDTS> lstVend = new ArrayList<VendedorxDTS>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String query = queryValorVendedor(filtros);
        log.trace("Query para obtener vendedores por dts: " + query);
        
        try { 
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1,idPais);
            pstmt.setBigDecimal(2,idPais);
            pstmt.setBigDecimal(3,idPais);
            pstmt.setBigDecimal(4,idPais);
            pstmt.setBigDecimal(5,idPais);
            pstmt.setBigDecimal(6,idPais);
            pstmt.setBigDecimal(7,idPais);
            pstmt.setBigDecimal(8,idPais);
            pstmt.setBigDecimal(9,idPais);
            pstmt.setString(10,Conf.GRUPO_NIVEL_BUZON_LOGIN);
            pstmt.setBigDecimal(11,idPais);
            pstmt.setString(12,Conf.GRUPO_NIVEL_BUZON_LOGIN);
            pstmt.setBigDecimal(13,idPais);
            pstmt.setBigDecimal(14,idPais);
            pstmt.setBigDecimal(15,idPais);
            pstmt.setBigDecimal(16,idPais);
            pstmt.setBigDecimal(17,idPais);
            pstmt.setBigDecimal(18,idPais);
            pstmt.setBigDecimal(19,idPais);
            pstmt.setBigDecimal(20,idPais);
            pstmt.setString(21,Conf.GRUPO_NIVEL_BUZON_LOGIN);
            pstmt.setBigDecimal(22,idPais);
            pstmt.setString(23,Conf.GRUPO_NIVEL_BUZON_LOGIN);
            pstmt.setBigDecimal(24,idPais);
            pstmt.setBigDecimal(25,idPais);
            pstmt.setBigDecimal(26,idPais);
            rst = pstmt.executeQuery();

            while (rst.next()) {
	            respuesta = new VendedorxDTS();
	            respuesta.setIdDTS(rst.getString("TCSCDTSID"));
	            respuesta.setIdBodegaVirtual(rst.getString("TCSCBODEGAVIRTUALID"));
	            respuesta.setIdBodegaVendedor(rst.getString("BODEGA_VENDEDOR"));
	            respuesta.setTipo(rst.getString("TIPO"));
	            respuesta.setIdVendedor(rst.getString("VENDEDOR"));
	            respuesta.setIdResponsable(rst.getString("IDRESPONSABLE"));
	            respuesta.setResponsable(rst.getString("RESPONSABLE"));
	            respuesta.setNombreDistribuidor(rst.getString("NOMBRES"));
	            respuesta.setNombreUsuario(ControladorBase.getNombreVendedor(conn, rst.getString("VENDEDOR"), idPais));
	            respuesta.setIdTipo(rst.getString("IDTIPO"));
	            respuesta.setNombreTipo(rst.getString("NOMBRE_TIPO"));
	            respuesta.setNumRecarga(rst.getString("NUM_RECARGA"));
	            respuesta.setPin(rst.getString("PIN"));
	            respuesta.setNumConvenio(rst.getString("NUM_CONVENIO"));
	            //AGREGADOS 30-03-17
	            respuesta.setNivelBuzon(rst.getString("NIVEL"));
	            respuesta.setNumIdentificacion(rst.getString("NUM_DOC_IDENT"));
	            respuesta.setTipoIdentificacion(rst.getString("TIPO_DOC_IDENT"));
	            respuesta.setNumTelefono(rst.getString("NUM_TELEFONO"));
	            //FIN
	            if (rst.getString("LATITUD") == null || "".equals(rst.getString("LATITUD"))) {
	                respuesta.setLatitud("");
	            } else {
	                respuesta.setLatitud(rst.getString("LATITUD"));
	            }
                if (rst.getString("LONGITUD") == null || "".equals(rst.getString("LONGITUD"))) {
                    respuesta.setLongitud("");
                } else {
                    respuesta.setLongitud(rst.getString("LONGITUD"));
                }

                lstVend.add(respuesta);
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }
        return lstVend;
    }
    
    public static String queryValorVendedor(List<Filtro> filtros) {
        String query = "SELECT * " +
        		"  FROM (SELECT A.TCSCDTSID, " +
        		"               D.NOMBRES, " +
        		"               A.TCSCBODEGAVIRTUALID, " +
        		"               E.LONGITUD, " +
        		"               E.LATITUD, " +
        		"               B.VENDEDOR, " +
        		"               B.RESPONSABLE, " +
        		"               (SELECT X.VENDEDOR " +
        		"                  FROM TC_SC_VEND_PANELPDV X " +
        		"                 WHERE     X.IDTIPO = B.IDTIPO " +
        		"                       AND X.RESPONSABLE = 1 " +
        		"                       AND X.ESTADO = 'ALTA' " +
        		"                       AND TCSCCATPAISID = ?) " +
        		"                  IDRESPONSABLE, " +
        		"               (SELECT TCSCBODEGAVIRTUALID " +
        		"                  FROM TC_SC_BODEGA_VENDEDOR " +
        		"                 WHERE     TCSCCATPAISID = ?"+
        		"                       AND VENDEDOR = " +
        		"                              (SELECT VENDEDOR " +
        		"                                 FROM TC_SC_VEND_PANELPDV " +
        		"                                WHERE     IDTIPO = B.IDTIPO " +
        		"                                      AND RESPONSABLE = 1 " +
        		"                                      AND ESTADO = 'ALTA' " +
        		"                                      AND TCSCCATPAISID = ?)) " +
        		"                  BODEGA_VENDEDOR, " +
        		"               'PANEL' AS TIPO, " +
        		"               B.IDTIPO, " +
        		"               A.NOMBRE NOMBRE_TIPO, " +
        		"               V.NUM_RECARGA, " +
        		"               V.PIN, " +
        		"               D.NUM_CONVENIO, " +
        		"               DECODE ( " +
        		"                  UPPER (d.TIPO), " +
        		"                  'INTERNO', (NVL ( " +
        		"                                 (SELECT cf.NUM_TELEFONO " +
        		"                                    FROM TC_sC_CLIENTEF_SCL cf " +
        		"                                   WHERE     CF.ID_CUSTOMER_ACCOUNT = " +
        		"                                                E.ID_CUSTOMER_ACCOUNT_GENERICO " +
        		"                                         AND CF.ID_CUSTOMER = " +
        		"                                                E.ID_CUSTOMER_GENERICO " +
        		"                                         AND TCSCCATPAISID = ?"+
        		"                                         AND ROWNUM = 1), " +
        		"                                 0)), " +
        		"                  'EXTERNO', (NVL ( " +
        		"                                 (SELECT cf.NUM_TELEFONO " +
        		"                                    FROM TC_sC_CLIENTEF_SCL cf " +
        		"										WHERE     CF.ID_CUSTOMER_ACCOUNT = " +
        		"                                                D.ID_CUSTOMER_ACCOUNT_GENERICO " +
        		"                                         AND CF.ID_CUSTOMER = " +
        		"                                                D.ID_CUSTOMER_GENERICO " +	  
        		"                                         AND TCSCCATPAISID = ?"+
        		"                                         AND ROWNUM = 1), " +
        		"                                 0))) " +
        		"                  NUM_TELEFONO, " +
        		"               DECODE ( " +
        		"                  UPPER (d.TIPO), " +
        		"                  'INTERNO', (NVL ( " +
        		"                                 (SELECT cf.NUM_DOC_IDENT " +
        		"                                    FROM TC_sC_CLIENTEF_SCL cf " +
         		"                                   WHERE     CF.ID_CUSTOMER_ACCOUNT = " +
        		"                                                E.ID_CUSTOMER_ACCOUNT_GENERICO " +
        		"                                         AND CF.ID_CUSTOMER = " +
        		"                                                E.ID_CUSTOMER_GENERICO " +
        		"                                         AND TCSCCATPAISID = ?"+
        		"                                         AND ROWNUM = 1), " +
        		"                                 0)), " +
        		"                  'EXTERNO', (NVL ( " +
        		"                                 (SELECT cf.NUM_DOC_IDENT " +
        		"                                    FROM TC_sC_CLIENTEF_SCL cf " +
         		"                                   WHERE     CF.ID_CUSTOMER_ACCOUNT = " +
        		"                                                D.ID_CUSTOMER_ACCOUNT_GENERICO " +
        		"                                         AND CF.ID_CUSTOMER = " +
        		"                                                D.ID_CUSTOMER_GENERICO " +
        		"                                         AND TCSCCATPAISID = ?"+
        		"                                         AND ROWNUM = 1), " +
        		"                                 0))) " +
        		"                  NUM_DOC_IDENT, " +
        		"               DECODE ( " +
        		"                  UPPER (d.TIPO), " +
        		"                  'INTERNO', NVL ( " +
        		"                                (SELECT cf.TIPO_DOC_IDENT " +
        		"                                   FROM TC_sC_CLIENTEF_SCL cf " +
         		"                                   WHERE     CF.ID_CUSTOMER_ACCOUNT = " +
        		"                                                E.ID_CUSTOMER_ACCOUNT_GENERICO " +
        		"                                         AND CF.ID_CUSTOMER = " +
        		"                                                E.ID_CUSTOMER_GENERICO " +
        		"                                        AND TCSCCATPAISID =?"+
        		"                                        AND ROWNUM = 1), " +
        		"                                0), " +
        		"                  'EXTERNO', NVL ( " +
        		"                                (SELECT cf.TIPO_DOC_IDENT " +
        		"                                   FROM TC_sC_CLIENTEF_SCL cf " +
         		"                                   WHERE     CF.ID_CUSTOMER_ACCOUNT = " +
        		"                                                D.ID_CUSTOMER_ACCOUNT_GENERICO " +
        		"                                         AND CF.ID_CUSTOMER = " +
        		"                                                D.ID_CUSTOMER_GENERICO " +
        		"                                        AND TCSCCATPAISID = ?"+
        		"                                        AND ROWNUM = 1), " +
        		"                                0)) " +
        		"                  TIPO_DOC_IDENT, " +
        		"               DECODE ( " +
        		"                  UPPER (d.TIPO), " +
        		"                  'INTERNO', (SELECT VALOR " +
        		"                                FROM TC_SC_CONFIGURACION " +
        		"                               WHERE     GRUPO = ? " +
        		"                                     AND NOMBRE = 'DTS_INTERNO' " +
        		"                                     AND ESTADO = 'ALTA' " +
        		"                                     AND TCSCCATPAISID =?), " +
        		"                  'EXTERNO', (SELECT VALOR " +
        		"                                FROM TC_SC_CONFIGURACION " +
        		"                               WHERE     GRUPO = ?" +
        		"                                     AND NOMBRE = 'DTS_EXTERNO' " +
        		"                                     AND ESTADO = 'ALTA' " +
        		"                                     AND TCSCCATPAISID =?)) " +
        		"                  NIVEL " +
        		"          FROM TC_SC_PANEL A, " +
        		"               TC_SC_VEND_PANELPDV B, " +
        		"               TC_SC_DTS D, " +
        		"               TC_SC_BODEGAVIRTUAL E, " +
        		"               TC_SC_VEND_DTS V " +
        		"         WHERE     A.TCSCCATPAISID =?"+
        		"               AND A.TCSCCATPAISID = B.TCSCCATPAISID " +
        		"               AND B.TCSCCATPAISID = D.TCSCCATPAISID " +
        		"               AND A.TCSCDTSID = D.TCSCDTSID " +
        		"               AND V.TCSCDTSID = D.TCSCDTSID " +
        		"               AND A.TCSCPANELID = B.IDTIPO " +
        		"               AND B.VENDEDOR = V.VENDEDOR " +
        		"               AND B.TIPO = 'PANEL' " +
        		"               AND A.ESTADO = 'ALTA' " +
        		"               AND E.ESTADO = 'ALTA' " +
        		"               AND D.ESTADO = 'ALTA' " +
        		"               AND B.ESTADO = 'ALTA' " +
        		"               AND V.ESTADO = 'ALTA' " +
        		"               AND E.TCSCBODEGAVIRTUALID = A.TCSCBODEGAVIRTUALID " +
        		"        UNION " +
        		"        SELECT A.TCSCDTSID, " +
        		"               D.NOMBRES, " +
        		"               A.TCSCBODEGAVIRTUALID, " +
        		"               E.LONGITUD, " +
        		"               E.LATITUD, " +
        		"               B.VENDEDOR, " +
        		"               1 RESPONSABLE, " +
        		"               B.VENDEDOR IDRESPONSABLE, " +
        		"               B.TCSCBODEGAVIRTUALID BODEGA_VENDEDOR, " +
        		"               'RUTA' AS TIPO, " +
        		"               A.TCSCRUTAID AS IDTIPO, " +
        		"               A.NOMBRE NOMBRE_TIPO, " +
        		"               V.NUM_RECARGA, " +
        		"               V.PIN, " +
        		"               D.NUM_CONVENIO, " +
        		"               DECODE ( " +
        		"                  UPPER (d.TIPO), " +
        		"                  'INTERNO', (NVL ( " +
        		"                                 (SELECT cf.NUM_TELEFONO " +
        		"                                    FROM TC_sC_CLIENTEF_SCL cf " +
         		"                                   WHERE     CF.ID_CUSTOMER_ACCOUNT = " +
        		"                                                E.ID_CUSTOMER_ACCOUNT_GENERICO " +
        		"                                         AND CF.ID_CUSTOMER = " +
        		"                                                E.ID_CUSTOMER_GENERICO " +
        		"                                         AND TCSCCATPAISID = ?"+
        		"                                         AND ROWNUM = 1), " +
        		"                                 0)), " +
        		"                  'EXTERNO', (NVL ( " +
        		"                                 (SELECT cf.NUM_TELEFONO " +
        		"                                    FROM TC_sC_CLIENTEF_SCL cf " +
         		"                                   WHERE     CF.ID_CUSTOMER_ACCOUNT = " +
        		"                                                D.ID_CUSTOMER_ACCOUNT_GENERICO " +
        		"                                         AND CF.ID_CUSTOMER = " +
        		"                                                D.ID_CUSTOMER_GENERICO " +
        		"                                         AND TCSCCATPAISID = ?"+
        		"                                         AND ROWNUM = 1), " +
        		"                                 0))) " +
        		"                  NUM_TELEFONO, " +
        		"               DECODE ( " +
        		"                  UPPER (d.TIPO), " +
        		"                  'INTERNO', (NVL ( " +
        		"                                 (SELECT cf.NUM_DOC_IDENT " +
        		"                                    FROM TC_sC_CLIENTEF_SCL cf " +
         		"                                   WHERE     CF.ID_CUSTOMER_ACCOUNT = " +
        		"                                                E.ID_CUSTOMER_ACCOUNT_GENERICO " +
        		"                                         AND CF.ID_CUSTOMER = " +
        		"                                                E.ID_CUSTOMER_GENERICO " +
        		"                                         AND TCSCCATPAISID =?"+
        		"                                         AND ROWNUM = 1), " +
        		"                                 0)), " +
        		"                  'EXTERNO', (NVL ( " +
        		"                                 (SELECT cf.NUM_DOC_IDENT " +
        		"                                    FROM TC_sC_CLIENTEF_SCL cf " +
         		"                                   WHERE     CF.ID_CUSTOMER_ACCOUNT = " +
        		"                                                D.ID_CUSTOMER_ACCOUNT_GENERICO " +
        		"                                         AND CF.ID_CUSTOMER = " +
        		"                                                D.ID_CUSTOMER_GENERICO " +
        		"                                         AND TCSCCATPAISID =?"+
        		"                                         AND ROWNUM = 1), " +
        		"                                 0))) " +
        		"                  NUM_DOC_IDENT, " +
        		"               DECODE ( " +
        		"                  UPPER (d.TIPO), " +
        		"                  'INTERNO', NVL ( " +
        		"                                (SELECT cf.TIPO_DOC_IDENT " +
        	    "									FROM TC_sC_CLIENTEF_SCL cf " +
         		"                                   WHERE     CF.ID_CUSTOMER_ACCOUNT = " +
        		"                                                E.ID_CUSTOMER_ACCOUNT_GENERICO " +
        		"                                         AND CF.ID_CUSTOMER = " +
        		"                                                E.ID_CUSTOMER_GENERICO " +
        		"                                        AND TCSCCATPAISID = ?"+
        		"                                        AND ROWNUM = 1), " +
        		"                                0), " +
        		"                  'EXTERNO', NVL ( " +
        		"                                (SELECT cf.TIPO_DOC_IDENT " +
        		"                                   FROM TC_sC_CLIENTEF_SCL cf " +
         		"                                   WHERE     CF.ID_CUSTOMER_ACCOUNT = " +
        		"                                                D.ID_CUSTOMER_ACCOUNT_GENERICO " +
        		"                                         AND CF.ID_CUSTOMER = " +
        		"                                                D.ID_CUSTOMER_GENERICO " +
        		"                                        AND TCSCCATPAISID =?"+
        		"                                        AND ROWNUM = 1), " +
        		"                                0)) " +
        		"                  TIPO_DOC_IDENT, " +
        		"               DECODE ( " +
        		"                  UPPER (d.TIPO), " +
        		"                  'INTERNO', (SELECT VALOR " +
        		"                                FROM TC_SC_CONFIGURACION " +
        		"                               WHERE     GRUPO = ?" +
        		"                                     AND NOMBRE = 'DTS_INTERNO' " +
        		"                                     AND ESTADO = 'ALTA' " +
        		"                                     AND TCSCCATPAISID =?), " +
        		"                  'EXTERNO', (SELECT VALOR " +
        		"                                FROM TC_SC_CONFIGURACION " +
        		"                               WHERE     GRUPO =? " +
        		"                                     AND NOMBRE = 'DTS_EXTERNO' " +
        		"                                     AND ESTADO = 'ALTA' " +
        		"                                     AND TCSCCATPAISID = ?)) " +
        		"                  NIVEL " +
        		"          FROM TC_SC_RUTA A, " +
        		"               TC_SC_BODEGA_VENDEDOR B, " +
        		"               TC_SC_DTS D, " +
        		"               TC_SC_BODEGAVIRTUAL E, " +
        		"               TC_SC_VEND_DTS V " +
        		"         WHERE     A.TCSCCATPAISID =?"+
        		"               AND B.TCSCCATPAISID =?"+
        		"               AND B.TCSCCATPAISID = D.TCSCCATPAISID " +
        		"               AND A.TCSCDTSID = D.TCSCDTSID " +
        		"               AND A.SECUSUARIOID = B.VENDEDOR " +
        		"               AND V.TCSCDTSID = D.TCSCDTSID " +
        		"               AND A.SECUSUARIOID = V.VENDEDOR " +
        		"               AND A.ESTADO = 'ALTA' " +
        		"               AND E.ESTADO = 'ALTA' " +
        		"               AND D.ESTADO = 'ALTA' " +
        		"               AND V.ESTADO = 'ALTA' " +
        		"               AND E.TCSCBODEGAVIRTUALID = A.TCSCBODEGAVIRTUALID) " +
        		" WHERE 1 = 1 "  ;

        if (!filtros.isEmpty()) {
            for (int i = 0; i < filtros.size(); i++) {
                    query += " AND " + filtros.get(i).getField() + " " + filtros.get(i).getOperator() + filtros.get(i).getValue();
            }
        }
        return query;
    }

	/**
	 * M\u00E9todo para validar si el dispositivo en el que se esta logueando un vendedor se encuentra asociado a la ruta o panel asignada.
	 * @param conn
	 * @param codDispositivo
	 * @param idResponsable
	 * @param idTipo
	 * @return
	 * @throws SQLException
	 */
    public static List<String> validaDispositivo(Connection conn, String codDispositivo, BigDecimal idPais) throws SQLException {
        List<String> respuesta = new ArrayList<String>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        
     
			String query = "SELECT ESTADO, NVL(RESPONSABLE,0) RESPONSABLE, NVL(TIPO_RESPONSABLE,0) TIPO_RESPONSABLE, VENDEDOR_ASIGNADO, TCSCDISPOSITIVOID "
    		        + " FROM TC_SC_DISPOSITIVO WHERE CODIGO_DISPOSITIVO = ? AND ESTADO='ALTA' AND TCSCCATPAISID = ?";
			log.debug(query);
	try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, codDispositivo);
			pstmt.setBigDecimal(2,idPais);
            rst = pstmt.executeQuery();

            if (rst.next()) {
                respuesta.add(rst.getString(1));
                respuesta.add(rst.getBigDecimal(2).toString());
                respuesta.add(rst.getString(3));
                respuesta.add(rst.getString(4));
                respuesta.add(rst.getBigDecimal("TCSCDISPOSITIVOID").toString());
                //respuesta.add(rst.getString(6));
                //respuesta.add(rst.getString(7));
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }
        return respuesta;
    }

    public static BigDecimal verificaJornada(Connection conn, String vendedor, String idTipo, String tipo,
            String estado, String codArea) throws SQLException {
        BigDecimal respuesta = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        StringBuilder query = new StringBuilder();
   
	        query.append("SELECT count(1) " );
	        query.append("  FROM " + ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea) + " " );
	        query.append(" WHERE     vendedor =?");
	        query.append("       AND idtipo =?");
	        query.append("       AND descripcion_tipo = ?" );
	        query.append("       AND ESTADO = ?" );
	     try {
	        pstmt = conn.prepareStatement(query.toString());
	        pstmt.setBigDecimal(1,  new BigDecimal(vendedor));
	        pstmt.setBigDecimal(2, new BigDecimal(idTipo));
	        pstmt.setString(3, tipo);
	        pstmt.setString(4, estado);
            rst = pstmt.executeQuery();

            if (rst.next()) {
                respuesta = rst.getBigDecimal(1);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return respuesta;
    }

    public static String getTasaCambio(String codArea) throws SQLException {
        String respuesta = "";

        if (codArea.equals("505")) {
            PreparedStatement pstmt = null;
            ResultSet rst = null;
	            	String codMoneda = UtileriasJava.getConfig(null, Conf.GRUPO_MONEDAS_PRECIO, Conf.COD_MONEDA_DOLAR, codArea);
	
		            Connection conn = null;
		           
		                
		                String query = "SELECT CAMBIO FROM GE_CONVERSION WHERE TRUNC(FEC_DESDE) = TRUNC(SYSDATE) AND COD_MONEDA = ?";
		          try {
		                conn = new ControladorBase().getConnRegional();
		                pstmt = conn.prepareStatement(query);
		                pstmt.setString(1, codMoneda);
		                rst = pstmt.executeQuery();
		
		                if (rst.next()) {
		                    respuesta = rst.getString(1);
		                }
		            } finally {
		                DbUtils.closeQuietly(pstmt);
		                DbUtils.closeQuietly(rst);
		            }
		
        } else {
            // los paises donde solo se maneja una moneda el tipo de cambio siempre es 1
            respuesta = "1";
        }

        return respuesta == null ? "" : respuesta;
    }
}
