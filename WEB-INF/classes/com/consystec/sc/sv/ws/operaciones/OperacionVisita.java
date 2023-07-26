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

import com.consystec.sc.ca.ws.input.visita.InputVisita;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.Visita;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.JavaUtils;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionVisita {
	private OperacionVisita(){}
    private static final Logger log = Logger.getLogger(OperacionVisita.class);

    /**
     * M\u00E9todo para verificar que la jornada y el pdv exista
     * 
     * @param estadoIniciada
     * @param estadoActivo 
     */
    public static List<BigDecimal> existeJornadaPDV(Connection conn, InputVisita objDatos, String estado, 
            String estadoIniciada, String estadoActivo, BigDecimal idPais) throws SQLException {
        List<BigDecimal> ret = new ArrayList<BigDecimal>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        StringBuilder query = new StringBuilder();
       
		query.append("SELECT (SELECT COUNT (1) " );
		query.append(    		"          FROM TC_SC_JORNADA_VEND " );
		query.append(    		"         WHERE TCSCJORNADAVENID = " + objDatos.getIdJornada() + " AND  UPPER(ESTADO)='" + estadoIniciada.toUpperCase()+"' AND TCSCCATPAISID="+idPais+") " );
		query.append(    		"          JORNADA, " );
		query.append(    		"       (SELECT COUNT (1) " );
		query.append(    		"          FROM TC_SC_JORNADA_VEND " );
		query.append(    		"         WHERE TCSCJORNADAVENID = " + objDatos.getIdJornada() + " AND VENDEDOR = "+objDatos.getIdVendedor());
		query.append(    		" 		AND  UPPER(ESTADO)='" + estadoIniciada.toUpperCase() + "' AND TCSCCATPAISID="+idPais+") " );
		query.append(    		"          VENDEDOR, " );
		query.append(    		"((SELECT COUNT (1) " );
		query.append("  FROM TC_SC_RUTA_PDV " );
		query.append(    		" WHERE     TCSCPUNTOVENTAID = " + objDatos.getIdPuntoVenta() );
		query.append(    		"       AND TCSCCATPAISID = " + idPais );
		query.append("       AND TCSCRUTAID IN (SELECT TCSCRUTAID FROM TC_SC_RUTA " );
		query.append(    		"          WHERE SECUSUARIOID = " + objDatos.getIdVendedor() );
		query.append(		                " AND UPPER(ESTADO)= '" + estado.toUpperCase() + "' AND TCSCCATPAISID = " + idPais+"))");
		query.append(         " + (SELECT COUNT(1) FROM TC_SC_PUNTOVENTA");
		query.append(        " WHERE TCSCPUNTOVENTAID = " + objDatos.getIdPuntoVenta() + " AND TCSCCATPAISID = " + idPais);
		query.append(        " AND UPPER(ESTADO) = '" + estadoActivo.toUpperCase() + "'");
		query.append(        " AND TCSCDTSID = (SELECT TCSCDTSID FROM " + ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", objDatos.getCodArea()));
		query.append(        " WHERE TCSCJORNADAVENID = " + objDatos.getIdJornada() + " AND TCSCCATPAISID = " + idPais + ")))" );
		query.append(    		"          PUNTOVENTA " );
		query.append(    		"FROM DUAL");

            log.trace("valida input visita-gestion:" + query.toString());
        try {
            pstmt = conn.prepareStatement(query.toString());
            rst = pstmt.executeQuery();

            if (rst.next()) {
                ret.add(0, rst.getBigDecimal(1));
                ret.add(1, rst.getBigDecimal(2));
                ret.add(2, rst.getBigDecimal(3));
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return ret;
    }

    public static BigDecimal insertVisita(Connection conn, Visita objeto) throws SQLException {
        BigDecimal ret = null;
        PreparedStatement pstmt = null;

       
            String insert = "INSERT INTO TC_SC_VISITA (tcscvisitaid, " +
            		"                          tcscjornadavenid, " +
            		"                          tcscpuntoventaid, " +
            		"                          tcscventaid, " +
            		"                          fecha, " +
            		"                          gestion, " +
            		"                          latitud, " +
            		"                          longitud, " +
            		"                          observaciones, " +
            		"                          vendedor, " +
            		"                          causa, " +
            		"                          creado_el, " +
            		"                          creado_por, " +
            		"                          tcsccatpaisid"
            		+ ") VALUES (?,?,?,?,?,?,?,?,?,?,?,sysdate,?,?)";

            log.trace("insert Visita:" + insert);
            log.trace("FECHA:" + objeto.getFecha());
        try {
            pstmt = conn.prepareStatement(insert);
            ret = JavaUtils.getSecuencia(conn, Visita.SEQUENCE);
            pstmt.setBigDecimal(1, ret);
            pstmt.setBigDecimal(2, objeto.getTcscjornadavendid());
            pstmt.setBigDecimal(3, objeto.getTcscpuntoventaid());
            pstmt.setBigDecimal(4, objeto.getTcscventaid());
            pstmt.setTimestamp(5, objeto.getFecha());
            pstmt.setString(6, objeto.getGestion());
            pstmt.setString(7, objeto.getLatitud());
            pstmt.setString(8, objeto.getLongitud());
            pstmt.setString(9, objeto.getObservaciones());
            pstmt.setBigDecimal(10, objeto.getVendedor());
            pstmt.setString(11, objeto.getCausa());
            pstmt.setString(12, objeto.getCreado_por());
            pstmt.setBigDecimal(13, objeto.getTcsccatpaisid());
            int res = pstmt.executeUpdate();
            if (res != 1) {
                ret = new BigDecimal(0);
            }
        } finally {

            DbUtils.closeQuietly(pstmt);
        }
        return ret;
    }

    /***
     * M\u00E9todo para obtener las visitas registradas en el sistema de SIDRA
     */
    public static List<InputVisita> getVisita(Connection conn, List<Filtro> filtros, String codArea) throws SQLException {
        List<InputVisita> lista = new ArrayList<InputVisita>();
        InputVisita objVisita = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        SimpleDateFormat FORMATO_FECHAHORA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHAHORA);
        
			String query = queryVisita(filtros, codArea);

            log.trace("Query para obtener visitas:" + query);
        try{
            pstmt = conn.prepareStatement(query);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                objVisita = new InputVisita();

	            objVisita.setIdJornada(rst.getString(Visita.CAMPO_TCSCJORNADAVENDID));
	            objVisita.setIdVisita(rst.getString(Visita.CAMPO_TCSCVISITAID));
	            objVisita.setGestion(rst.getString(Visita.CAMPO_GESTION));
	            objVisita.setIdVendedor(rst.getString(Visita.CAMPO_VENDEDOR));
	            objVisita.setNombreVendedor(rst.getString("NOMBREVENDEDOR"));
	            objVisita.setIdPuntoVenta(rst.getString("TCSCPUNTOVENTAID"));
	            objVisita.setNombrePuntoVenta(rst.getString("NOMBRE"));
	            objVisita.setLatitud(rst.getString(Visita.CAMPO_LATITUD));
	            objVisita.setLongitud(rst.getString(Visita.CAMPO_LONGITUD));
	            objVisita.setCreado_por(rst.getString(Visita.CAMPO_CREADO_POR));
	            objVisita.setCreado_el(FORMATO_FECHAHORA.format(rst.getTimestamp(Visita.CAMPO_CREADO_EL)));
	            objVisita.setFecha(FORMATO_FECHAHORA.format(rst.getTimestamp(Visita.CAMPO_FECHA)));

                objVisita.setIdVenta(UtileriasJava.getValue(rst.getString("TCSCVENTAID")));
                objVisita.setFolio(UtileriasJava.getValue(rst.getString("FOLIO")));
                objVisita.setSerie(UtileriasJava.getValue(rst.getString("SERIE")));

                objVisita.setCausa(UtileriasJava.getValue(rst.getString(Visita.CAMPO_CAUSA)));
                objVisita.setObservaciones(UtileriasJava.getValue(rst.getString("OBSERVACIONES")));
                objVisita.setModificado_el(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Visita.CAMPO_MODIFICADO_EL));
                objVisita.setModificado_por(UtileriasJava.getValue(rst.getString(Visita.CAMPO_MODIFICADO_POR)));

                lista.add(objVisita);
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }
        return lista;
    }

    public static String queryVisita(List<Filtro> filtros, String codArea) {
    	String query = "SELECT V.TCSCVISITAID, "
				+ " 	V.TCSCJORNADAVENID, " +
				"       V.VENDEDOR, " +
				"       A.NOMBRE || ' ' || A.APELLIDO AS NOMBREVENDEDOR, " +
				"       V.TCSCPUNTOVENTAID, " +
				"       PV.NOMBRE, " +
				"       V.FECHA, " +
				"       V.LONGITUD, " +
				"       V.LATITUD, " +
				"       V.GESTION, " +
				"       V.TCSCVENTAID, " +
				"       (SELECT NVL(SERIE_SIDRA, SERIE) FROM TC_SC_VENTA WHERE TCSCVENTAID = V.TCSCVENTAID) AS SERIE, " +
				"       (SELECT NVL(FOLIO_SIDRA, NUMERO) FROM TC_SC_VENTA WHERE TCSCVENTAID = V.TCSCVENTAID) AS FOLIO, " +
				"       V.CAUSA, " +
				"       V.OBSERVACIONES, " +
				"       V.CREADO_POR, " +
				"       V.CREADO_EL, " +
				"       V.MODIFICADO_POR, " +
				"       V.MODIFICADO_EL " +
				"  FROM TC_SC_VISITA  PARTITION ("+ControladorBase.getPais(codArea)+") V, TC_SC_VEND_DTS A, TC_SC_PUNTOVENTA PV " +
				" WHERE V.VENDEDOR = A.VENDEDOR AND V.TCSCPUNTOVENTAID = PV.TCSCPUNTOVENTAID  AND V.TCSCCATPAISID=A.TCSCCATPAISID "  ; 

        if (!filtros.isEmpty()) {
            for (int i = 0; i < filtros.size(); i++) {
                if (filtros.get(i).getOperator().toString() == "between") {
                    query += " AND " + filtros.get(i).getField() + " " + filtros.get(i).getOperator()
                            + filtros.get(i).getValue();
                } else {

                    query += " AND " + filtros.get(i).getField() + " " + filtros.get(i).getOperator() + ""
                            + filtros.get(i).getValue() + "";
                }
            }
        }

        query += " order by " + Visita.CAMPO_CREADO_EL + " desc ";
        
        return query;
    }
    public static List<String> getDatosDTS(Connection conn, InputVisita objDatos, BigDecimal idPais)
            throws SQLException {
        List<String> ret = new ArrayList<String>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        
            String query = "SELECT TCSCDTSID, NOMBRES FROM TC_SC_DTS WHERE "
                    + "TCSCDTSID IN (SELECT TCSCDTSID FROM TC_SC_PUNTOVENTA WHERE TCSCPUNTOVENTAID = ? AND TCSCCATPAISID = ?) AND TCSCCATPAISID = ?";

            log.trace("Qry datos dts mapa: " + query);
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1, new BigDecimal(objDatos.getIdPuntoVenta()));
            pstmt.setBigDecimal(2, idPais);
            pstmt.setBigDecimal(3, idPais);
            
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
}
