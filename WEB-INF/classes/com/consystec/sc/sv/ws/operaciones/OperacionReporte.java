package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.reporte.InputReporteEfectividadVenta;
import com.consystec.sc.ca.ws.output.reporte.ArticuloVendido;
import com.consystec.sc.ca.ws.output.reporte.ComparacionVenta;
import com.consystec.sc.ca.ws.output.reporte.ComparacionVentaArticulo;
import com.consystec.sc.ca.ws.output.reporte.CumplimientoVenta;
import com.consystec.sc.ca.ws.output.reporte.CumplimientoVisita;
import com.consystec.sc.ca.ws.output.reporte.DatosEfectividad;
import com.consystec.sc.ca.ws.output.reporte.DetalleArticulo;
import com.consystec.sc.ca.ws.output.reporte.DetalleVendido;
import com.consystec.sc.ca.ws.output.reporte.InfoPDV;
import com.consystec.sc.ca.ws.output.reporte.TarjetaVendida;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.orm.Venta;
import com.consystec.sc.sv.ws.orm.Visita;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionReporte {
private OperacionReporte(){}
    private static final Logger log = Logger.getLogger(OperacionReporte.class);
    
    public static List<CumplimientoVisita> getDatosCumplimientoVisita(Connection conn, List<Filtro> filtros, String codArea, BigDecimal ID_PAIS)
            throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        CumplimientoVisita objCumplVisita = null;
        List<CumplimientoVisita> lista = new ArrayList<CumplimientoVisita>();
  
	        String query = queryCumplimientoVisita(codArea, ID_PAIS);

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

            log.trace("Query para obtener Datos:" + query);
          try {
            pstmt = conn.prepareStatement(query);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                objCumplVisita = new CumplimientoVisita();

                objCumplVisita.setCodPvd(rst.getString("COD_PDV") == null ? " " : rst.getString("COD_PDV"));
                objCumplVisita.setCodDistribuidor(rst.getString("COD_DISTRIBUIDOR") == null ? " " : rst.getString("COD_DISTRIBUIDOR"));
                objCumplVisita.setDistribuidor(rst.getString("DISTRIBUIDOR") == null ? " " : rst.getString("DISTRIBUIDOR"));
                objCumplVisita.setCodBodega(rst.getString("COD_BODEGA") == null ? " " : rst.getString("COD_BODEGA"));
                objCumplVisita.setBodega(rst.getString("BODEGA") == null ? " " : rst.getString("BODEGA"));
                objCumplVisita.setDistrito(rst.getString("DISTRITO") == null ? " " : rst.getString("DISTRITO"));
                objCumplVisita.setDepartamento(rst.getString("DEPARTAMENTO") == null ? " " : rst.getString("DEPARTAMENTO"));
                objCumplVisita.setUsuario(rst.getString("USUARIO") == null ? " " : rst.getString("USUARIO"));
                objCumplVisita.setVendedor(rst.getString("VENDEDOR") == null ? " " : rst.getString("VENDEDOR"));
                objCumplVisita.setRuta(rst.getString("RUTA") == null ? " " : rst.getString("RUTA"));
                objCumplVisita.setNombrePdv(rst.getString("NOMBRE_PDV") == null ? " " : rst.getString("NOMBRE_PDV"));
                objCumplVisita.setDia(rst.getString("DIA") == null ? " " : rst.getString("DIA"));
                objCumplVisita.setMes(rst.getString("MES") == null ? " " : rst.getString("MES"));
                objCumplVisita.setAnio(rst.getString("ANIO") == null ? " " : rst.getString("ANIO"));
                objCumplVisita.setFecha(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, "FECHA"));
                objCumplVisita.setAsignado(rst.getString("ASIGNADO") == null ? " " : rst.getString("ASIGNADO"));
                objCumplVisita.setGestion(rst.getString("GESTION") == null ? " " : rst.getString("GESTION"));
                objCumplVisita.setObservaciones(rst.getString("OBSERVACIONES") == null ? " " : rst.getString("OBSERVACIONES"));
                lista.add(objCumplVisita);
            }
        } finally {
            // cerrar recursos
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }
        return lista;
    }

    public static String queryCumplimientoVisita(String codArea, BigDecimal ID_PAIS) {
    	   String query = "SELECT A.TCSCPUNTOVENTAID COD_PDV, " +
	        		"       A.TCSCDTSID COD_DISTRIBUIDOR, " +
	        		"       B.NOMBRES DISTRIBUIDOR, " +
	        		"       (SELECT TCSCBODEGAVIRTUALID " +
	        		"          FROM " + ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", codArea) + " " +
	        		"         WHERE TCSCBODEGAVIRTUALID = (SELECT TCSCBODEGAVIRTUALID " +
	        		"                                        FROM TC_SC_BODEGA_VENDEDOR " +
	        		"                                       WHERE TCSCCATPAISID = " + ID_PAIS +
	        		                                        " AND VENDEDOR = E.SECUSUARIOID) AND TCSCCATPAISID = " + ID_PAIS + ") " +
	        		"          COD_BODEGA, " +
	        		"       (SELECT NOMBRE " +
	        		"          FROM " + ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", codArea) + " " +
	        		"         WHERE TCSCBODEGAVIRTUALID = (SELECT TCSCBODEGAVIRTUALID " +
	        		"                                        FROM TC_SC_BODEGA_VENDEDOR " +
	        		"                                       WHERE TCSCCATPAISID = " + ID_PAIS +
	        		                                        " AND VENDEDOR = E.SECUSUARIOID) AND TCSCCATPAISID = " + ID_PAIS + ") " +
	        		"          BODEGA, " +
	        		"       A.DISTRITO, " +
	        		"       A.DEPARTAMENTO, " +
	        		"       G.USUARIO USUARIO, " +
	        		"       E.SECUSUARIOID VENDEDOR, " +
	        		"       E.NOMBRE RUTA, " +
	        		"       A.NOMBRE NOMBRE_PDV, " +
	        		"       CASE " +
	        		"          WHEN (SELECT UPPER (NOMBRE) " +
	        		"                  FROM TC_SC_DIAVISITA " +
	        		"                 WHERE     UPPER (NOMBRE) = " +
	        		"                              TRIM (UPPER (TO_CHAR (F.FECHA, 'DAY', 'NLS_DATE_LANGUAGE=SPANISH'))) " +
	        		"                       AND TCSCPUNTOVENTAID = A.TCSCPUNTOVENTAID) " +
	        		"                  IS NOT NULL " +
	        		"          THEN " +
	        		"             'SI' " +
	        		"          ELSE " +
	        		"             'NO' " +
	        		"       END " +
	        		"          ASIGNADO, " +
	        		"       TO_CHAR (F.FECHA, 'DD') DIA, " +
	        		"       TO_CHAR (F.FECHA, 'MM') MES, " +
	        		"       TO_CHAR (F.FECHA, 'YYYY') ANIO, " +
	        		"       F.FECHA FECHA, " +
	        		"       F.GESTION, " +
	        		"       F.OBSERVACIONES " +
	        		"  FROM " + ControladorBase.getParticion(PuntoVenta.N_TABLA, Conf.PARTITION, "", codArea) + " A, " +
	        		"       TC_SC_DTS B, " +
	        		"       TC_SC_RUTA E, " +
	        		"       TC_SC_RUTA_PDV D, " +
	        		"       " + ControladorBase.getParticion(Visita.N_TABLA, Conf.PARTITION, "", codArea) + " F, " +
	        		"       " + ControladorBase.getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, "", codArea) + " G " +
	        		" WHERE A.TCSCCATPAISID = " + ID_PAIS +
	        		    " AND B.TCSCCATPAISID = A.TCSCCATPAISID" +
   	        		" AND E.TCSCCATPAISID = A.TCSCCATPAISID" +
   	        		" AND D.TCSCCATPAISID = A.TCSCCATPAISID" +
   	        		" AND F.TCSCCATPAISID = A.TCSCCATPAISID" +
   	        		" AND G.TCSCCATPAISID = A.TCSCCATPAISID" +
   	        		" AND A.TCSCDTSID = B.TCSCDTSID " +
   	        		" AND B.TCSCDTSID = E.TCSCDTSID " +
   	        		" AND D.TCSCRUTAID = E.TCSCRUTAID  " +
   	        		" AND D.TCSCPUNTOVENTAID = A.TCSCPUNTOVENTAID  " +
   	        		" AND A.TCSCPUNTOVENTAID = F.TCSCPUNTOVENTAID " +
   	        		" AND E.SECUSUARIOID = G.VENDEDOR " ;
    	   
    	   return query;
    }
    public static List<CumplimientoVenta> getDatosVentaPdv(Connection conn, List<Filtro> filtros, int min, int max, String codArea, BigDecimal ID_PAIS)
            throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String queryFinal = "";
        CumplimientoVenta objCumplVenta = null;
        List<CumplimientoVenta> lista = new ArrayList<CumplimientoVenta>();

     
	        String query = "SELECT F.FECHA_EMISION FECHA, " +
	        		"       A.TCSCPUNTOVENTAID IDPUNTOVENTA, " +
	        		"       B.NOMBRES NOMBREDISTRIBUIDOR, " +
	        		"       (SELECT TCSCBODEGAVIRTUALID " +
	        		"          FROM " + ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", codArea) + " " +
	        		"         WHERE TCSCBODEGAVIRTUALID = (SELECT TCSCBODEGAVIRTUALID " +
	        		"                                        FROM TC_SC_BODEGA_VENDEDOR " +
	        		"                                       WHERE TCSCCATPAISID = " + ID_PAIS +
	        		                                        " AND VENDEDOR = C.SECUSUARIOID)) " +
	        		"          IDBODEGAVIRTUAL, " +
	        		"       (SELECT NOMBRE " +
	        		"          FROM " + ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", codArea) + " " +
	        		"         WHERE TCSCBODEGAVIRTUALID = (SELECT TCSCBODEGAVIRTUALID " +
	        		"                                        FROM TC_SC_BODEGA_VENDEDOR " +
	        		"                                       WHERE TCSCCATPAISID = " + ID_PAIS +
	        		                                        " AND VENDEDOR = C.SECUSUARIOID)) " +
	        		"          NOMBREBODEGA, " +
	        		"       A.DISTRITO, " +
	        		"       A.DEPARTAMENTO, " +
	        		"       A.MUNICIPIO, " +
	        		"       C.TCSCRUTAID IDRUTA, " +
	        		"       C.NOMBRE NOMBRERUTA, " +
	        		"       C.SECUSUARIOID IDVENDEDOR, " +
	        		"       D.USUARIO USUARIO, " +
	        		"       A.NOMBRE NOMBREPDV, " +
	        		"       G.NUM_RECARGA TELPRIMARIO, " +
	        		"       A.TIPO_PRODUCTO TIPOPRODUCTO, " +
	        		"       A.TIPO_NEGOCIO TIPONEGOCIO,  " +
	        		"       F.TCSCVENTAID IDVENTA " +
                    "  FROM " + ControladorBase.getParticion(PuntoVenta.N_TABLA, Conf.PARTITION, "", codArea) + " A," +
	        		"       TC_SC_DTS B, " +
	        		"       TC_SC_RUTA C, " +
	        	            ControladorBase.getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, "", codArea) + " D," +
	        		"       TC_SC_NUMRECARGA G, " +
	        		        ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea) + " F " +
	        		" WHERE     A.TCSCDTSID = B.TCSCDTSID " +
	        		"       AND C.TCSCDTSID = B.TCSCDTSID " +
	        		"       AND C.SECUSUARIOID = D.VENDEDOR " +
	        		"       AND G.IDTIPO = A.TCSCPUNTOVENTAID " +
	        		"       AND G.TIPO = 'PDV' " +
	        		"       AND G.ORDEN = 1 " +
	        		"       AND F.TIPO = 'PDV' " +
	        		"       AND F.IDTIPO = A.TCSCPUNTOVENTAID " +
	        		"       AND F.VENDEDOR = C.SECUSUARIOID " +
	        	    "       AND F.TCSCCATPAISID = " + ID_PAIS +
                    "       AND A.TCSCCATPAISID = F.TCSCCATPAISID " +
                    "       AND B.TCSCCATPAISID = F.TCSCCATPAISID " +
                    "       AND C.TCSCCATPAISID = F.TCSCCATPAISID " +
                    "       AND D.TCSCCATPAISID = F.TCSCCATPAISID " +
                    "       AND G.TCSCCATPAISID = F.TCSCCATPAISID";

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

            queryFinal = UtileriasBD.getLimit(query, min, max);

            log.trace("Query para obtener Datos Ventas PDV:" + queryFinal);
          try {
            pstmt = conn.prepareStatement(queryFinal);
            rst = pstmt.executeQuery();

	        while (rst.next()) {
	        	objCumplVenta = new CumplimientoVenta();
	        	objCumplVenta.setFecha(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, "fecha"));
	        	objCumplVenta.setIdPuntoVenta(rst.getString("idpuntoventa"));
	        	objCumplVenta.setNombreDistribuidor(rst.getString("nombredistribuidor"));
	        	objCumplVenta.setIdBodegaVirtual(rst.getString("idbodegavirtual"));
	        	objCumplVenta.setNombreBodega(rst.getString("nombrebodega"));
	        	objCumplVenta.setDistrito(rst.getString("DISTRITO"));
	        	objCumplVenta.setDepartamento(rst.getString("departamento"));
	        	objCumplVenta.setMunicipio(rst.getString("municipio"));
	        	objCumplVenta.setIdRuta(rst.getString("idruta"));
	        	objCumplVenta.setNombreRuta(rst.getString("nombreruta"));
	        	objCumplVenta.setIdVendedor(rst.getString("idvendedor"));
	        	objCumplVenta.setUsuario(rst.getString("usuario"));
	        	objCumplVenta.setNombrePdv(rst.getString("nombrepdv"));
	        	objCumplVenta.setTelPrimario(rst.getString("telprimario"));
	        	objCumplVenta.setTipoProducto(rst.getString("tipoproducto"));
	        	objCumplVenta.setTipoNegocio(rst.getString("tipoNegocio"));
	        	objCumplVenta.setIdVenta(rst.getString("idventa"));
	        	objCumplVenta.setArticulosVendidos(getDatosArticulo(conn, objCumplVenta.getIdPuntoVenta(), objCumplVenta.getIdVenta(), codArea, ID_PAIS));
	        	objCumplVenta.setTarjetas(getDatosTajetas(conn, objCumplVenta.getIdPuntoVenta(), objCumplVenta.getIdVenta(), codArea, ID_PAIS));

                lista.add(objCumplVenta);
            }
        } finally {
            // cerrar recursos
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);

        }
        return lista;
    }

    public static BigDecimal getContVentaPdv(Connection conn, List<Filtro> filtros, String codArea, BigDecimal ID_PAIS) throws SQLException {
        BigDecimal respuesta = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

      
	    	String query = querycantVenta(codArea, ID_PAIS);

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

            log.trace("Query para obtener CONTEO Ventas PDV:" + query);
            try {
            pstmt = conn.prepareStatement(query);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                respuesta = rst.getBigDecimal("CONTEO");
            }
        } finally {
            // cerrar recursos
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }
        return respuesta;
    }
    public static String querycantVenta(String codArea, BigDecimal ID_PAIS) {
    	String query = "  SELECT COUNT(*) CONTEO " +
                "  FROM " + ControladorBase.getParticion(PuntoVenta.N_TABLA, Conf.PARTITION, "", codArea) + " A, " +
    			"       TC_SC_DTS B, " +
        		"       TC_SC_RUTA C, " +
        		        ControladorBase.getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, "", codArea) + " D, " +
        		"       TC_SC_NUMRECARGA G,  " +
        		        ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea) + " F " +
        		" WHERE     A.TCSCDTSID = B.TCSCDTSID " +
        		"       AND C.TCSCDTSID = B.TCSCDTSID " +
        		"       AND C.SECUSUARIOID = D.VENDEDOR " +
        		"       AND G.IDTIPO = A.TCSCPUNTOVENTAID " +
        		"       AND G.TIPO = 'PDV' " +
        		"       AND G.ORDEN = 1 " +
        		"       AND F.TIPO = 'PDV' " +
        		"       AND F.IDTIPO = A.TCSCPUNTOVENTAID " +
        		"       AND F.VENDEDOR = C.SECUSUARIOID " +
                "       AND F.TCSCCATPAISID = " + ID_PAIS +
                "       AND A.TCSCCATPAISID = F.TCSCCATPAISID " +
                "       AND B.TCSCCATPAISID = F.TCSCCATPAISID " +
                "       AND C.TCSCCATPAISID = F.TCSCCATPAISID " +
                "       AND D.TCSCCATPAISID = F.TCSCCATPAISID " +
                "       AND G.TCSCCATPAISID = F.TCSCCATPAISID";
    	
    	return query;
    }

    public static List<ArticuloVendido> getDatosArticulo(Connection conn, String idPdv, String idVenta, String codArea, BigDecimal ID_PAIS)
            throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        ArticuloVendido objArticulo = null;
        List<ArticuloVendido> lstArticulo = new ArrayList<ArticuloVendido>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT C.TIPO_GRUPO_SIDRA TIPOPRODUCTO,      0 CANTIDADSUGERIDA,      0 MONTOSUGERIDO,         SUM (B.CANTIDAD) CANTIDADFACTURADA,     SUM (B.PRECIO_FINAL*A.TASA_CAMBIO) MONTOFACTURADO    FROM TC_SC_ARTICULO_INV C, ");
	    query.append(ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea));
	    query.append(" A, TC_SC_VENTA_DET B ");
	    query.append("   WHERE     A.TIPO = 'PDV'    AND A.IDTIPO =?    AND A.TCSCVENTAID =?    AND A.TCSCVENTAID = B.TCSCVENTAID  AND A.TCSCCATPAISID =?  AND C.TCSCCATPAISID = A.TCSCCATPAISID    AND B.ARTICULO = C.ARTICULO   AND C.TIPO_GRUPO_SIDRA != 'TARJETASRASCA' GROUP BY C.TIPO_GRUPO_SIDRA " );


            log.trace("Query para obtener Articulos por Venta:" + query);
            try {
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setBigDecimal(1, new BigDecimal(idPdv));
            pstmt.setBigDecimal(2, new BigDecimal(idVenta));
            pstmt.setBigDecimal(3, ID_PAIS);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                objArticulo = new ArticuloVendido();
                objArticulo.setTipoProducto(rst.getString("tipoproducto"));
                objArticulo.setCantidadSugerida(rst.getString("cantidadsugerida"));
                objArticulo.setMontoSugerido(rst.getString("montosugerido"));
                objArticulo.setCantidadFacturada(rst.getString("cantidadfacturada"));
                objArticulo.setMontoFacturado(rst.getString("montofacturado"));

                lstArticulo.add(objArticulo);
            }
        } finally {
            // cerrar recursos
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }

        return lstArticulo;
    }

    public static List<TarjetaVendida> getDatosTajetas(Connection conn, String idPdv, String idVenta, String codArea, BigDecimal ID_PAIS)
            throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        TarjetaVendida objTarjeta = null;
        List<TarjetaVendida> lstTarjeta = new ArrayList<TarjetaVendida>();
        StringBuilder query = new StringBuilder();
        query.append( "  SELECT C.ARTICULO IDARTICULO,        C.DESCRIPCION,    0 CANTIDADSUGERIDA,   0 MONTOSUGERIDO, ");
        query.append(" SUM (B.CANTIDAD) CANTIDADFACTURADA,    SUM (B.PRECIO_FINAL*A.TASA_CAMBIO) MONTOFACTURADO  FROM TC_SC_ARTICULO_INV C, ");
        query.append(ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea));
        query.append( " A, TC_SC_VENTA_DET B ");
        query.append("   WHERE     A.TIPO = 'PDV'  AND A.IDTIPO =?    AND A.TCSCVENTAID = ?    AND A.TCSCVENTAID = B.TCSCVENTAID " );
        query.append("  AND A.TCSCCATPAISID = ?  AND C.TCSCCATPAISID = A.TCSCCATPAISID  AND B.ARTICULO = C.ARTICULO  AND C.TIPO_GRUPO_SIDRA = 'TARJETASRASCA' GROUP BY C.ARTICULO, C.DESCRIPCION ");
      

            log.trace("Query para obtener Tarjetas por Venta:" + query);
         try {
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setBigDecimal(1, new BigDecimal(idPdv));
            pstmt.setBigDecimal(2, new BigDecimal(idVenta));
            pstmt.setBigDecimal(3, ID_PAIS);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                objTarjeta = new TarjetaVendida();
                objTarjeta.setIdArticulo(rst.getString("idarticulo"));
                objTarjeta.setDescripcion(rst.getString("descripcion"));
                objTarjeta.setCantidadSugerida(rst.getString("cantidadsugerida"));
                objTarjeta.setMontoSugerido(rst.getString("montosugerido"));
                objTarjeta.setCantidadFacturada(rst.getString("cantidadfacturada"));
                objTarjeta.setMontoFacturado(rst.getString("montofacturado"));

                lstTarjeta.add(objTarjeta);
            }
        } finally {
            // cerrar recursos
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }
        return lstTarjeta;
    }

    public static List<DetalleVendido> getDatosDetalleVendido(Connection conn, List<Filtro> filtros,String idJornada, String idDts, String codArea, BigDecimal ID_PAIS)
            throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        DetalleVendido objDetVendido = null;
        List<DetalleVendido> lista = new ArrayList<DetalleVendido>();
        String tipo = "";

        String idJornadaCond="";
    	if (!(idJornada == null || "".equals(idJornada.trim()))) {
			log.trace("entra a filtro ID_JORNADA");
			idJornadaCond="         AND c.TCSCJORNADAVENID = " +idJornada;
		}
    	if (!(idDts == null || "".equals(idDts.trim()))) {
			log.trace("entra a filtro ID_DISTRIBUIDOR");
			idJornadaCond+= "    AND C.TCSCDTSID="+ idDts;
		}
    	
 
	        String query =queryDetalleVendido( idJornadaCond,filtros, codArea, ID_PAIS);
	        
           
        try {
            log.trace("Query para obtener Detalle Vendido:" + query);
            pstmt = conn.prepareStatement(query);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                tipo = rst.getString("tipoproducto");
                objDetVendido = new DetalleVendido();
                objDetVendido.setTipoProducto(tipo);
                objDetVendido.setArticulos(getDetalleArticulo(conn, filtros, tipo,idJornada, codArea, ID_PAIS));

                lista.add(objDetVendido);
                tipo = "";
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }

        return lista;
    }
    public static String queryDetalleVendido(String idJornadaCond,List<Filtro> filtros, String codArea, BigDecimal ID_PAIS) {

        String query = "SELECT D.TIPO_GRUPO_SIDRA TIPOPRODUCTO, COUNT (B.ARTICULO) CANTIDAD " +
        		"    FROM " + ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea) + " A, " +
        		"         TC_SC_VENTA_DET B, " +
        		"         " + ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea) + " C, " +
        		"         TC_SC_ARTICULO_INV D " +
        		"   WHERE     A.TCSCVENTAID = B.TCSCVENTAID " +
        		"         AND A.TCSCJORNADAVENID = C.TCSCJORNADAVENID " +
                "         AND A.TCSCCATPAISID = " + ID_PAIS +
                "         AND C.TCSCCATPAISID = A.TCSCCATPAISID " + idJornadaCond+                    
                "         AND D.TCSCCATPAISID = A.TCSCCATPAISID " +
        		"         AND A.VENDEDOR = C.VENDEDOR " +
        		"         AND B.ARTICULO = D.ARTICULO ";
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

        query += " GROUP BY D.TIPO_GRUPO_SIDRA ";
        return query;
    }

    public static List<DetalleArticulo> getDetalleArticulo(Connection conn, List<Filtro> filtros, String agrupacion, String idJornada, String codArea, BigDecimal ID_PAIS)
            throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        DetalleArticulo detArticulo = null;
        List<DetalleArticulo> lista = new ArrayList<DetalleArticulo>();
        String estadosAnulados="";
        String idJornadaCond="";
        estadosAnulados=UtileriasJava.getConfig(conn, Conf.ESTADOS_ANULADOS, Conf.ESTADOS_ANULADOS, codArea);
    	if (!(idJornada == null || "".equals(idJornada.trim()))) {
			log.trace("entra a filtro ID_JORNADA  en getDetalleArticulo");
			idJornadaCond="         AND A.TCSCJORNADAVENID = " +idJornada;
		}
    
	        String query =queryDetArticulo(filtros,  agrupacion,  idJornadaCond,  estadosAnulados, codArea, ID_PAIS);


            log.trace("Query para obtener Detalle Vendido:" + query);
         try {
            pstmt = conn.prepareStatement(query);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                detArticulo = new DetalleArticulo();
                detArticulo.setIdArticulo(rst.getString("idarticulo"));
                detArticulo.setDescripcion(rst.getString("descripcion"));
                detArticulo.setCantidadVendida(rst.getString("cantidad"));
                detArticulo.setMontoTotalVendido(rst.getString("monto"));

                lista.add(detArticulo);
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }
        return lista;
    }

public static String queryDetArticulo(List<Filtro> filtros, String agrupacion, String idJornadaCond, String estadosAnulados, String codArea, BigDecimal ID_PAIS) {

    String query = " with SUMAART AS (SELECT   " +
    		"A.TCSCVENTAID,  " +
    		"B.ARTICULO IDARTICULO,  " +
    		"         D.DESCRIPCION,  " +
    		"         SUM (B.CANTIDAD) CANTIDAD,  " +
    		"        round(SUM (B.PRECIO_FINAL*A.TASA_CAMBIO),2) MONTO,  " +
    		"         D.TIPO_GRUPO_SIDRA  " +
    		"    FROM " + ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea) + " A, " +
    		"         TC_SC_VENTA_DET B, " +
    		"         TC_SC_ARTICULO_INV D " +
    		"   WHERE     A.TCSCVENTAID = B.TCSCVENTAID " +
            "         AND A.TCSCCATPAISID = " + ID_PAIS +
            "         AND D.TCSCCATPAISID = A.TCSCCATPAISID " +idJornadaCond+
    		"         AND B.ARTICULO = D.ARTICULO " + 
    		"   	  AND D.TIPO_GRUPO_SIDRA = '" + agrupacion + "' "
    				+ "	AND A.ESTADO NOT IN ("+estadosAnulados+")" ;

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

    query += "GROUP BY A.TCSCVENTAID, B.ARTICULO, D.DESCRIPCION, D.TIPO_GRUPO_SIDRA )"+
    		"    SELECT IDARTICULO, DESCRIPCION, SUM(CANTIDAD) CANTIDAD, SUM(MONTO) MONTO, TIPO_GRUPO_SIDRA " +
    		"    FROM SUMAART " +
    		"    GROUP BY  IDARTICULO, DESCRIPCION,TIPO_GRUPO_SIDRA "; 
    
    return query;
}

    public static List<DetalleArticulo> getDatosResumenVendido(Connection conn, List<Filtro> filtros, String codArea, BigDecimal ID_PAIS)
            throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        DetalleArticulo objDetArtirculo = null;
        List<DetalleArticulo> lista = new ArrayList<DetalleArticulo>();

      
	        String query = queryResumenVendido(filtros, codArea, ID_PAIS); 
            try {
            log.trace("Query para obtener Resumen Vendido:" + query);
            pstmt = conn.prepareStatement(query);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                objDetArtirculo = new DetalleArticulo();
                objDetArtirculo.setTipoProducto(rst.getString("tipoproducto"));
                objDetArtirculo.setCantidadVendida(rst.getString("cantidad"));
                objDetArtirculo.setMontoTotalVendido(rst.getString("monto"));

                lista.add(objDetArtirculo);
            }
        } finally {
            // cerrar recursos
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }

        return lista;
    }
    public static String queryResumenVendido(List<Filtro> filtros, String codArea, BigDecimal ID_PAIS) {
        String query = "SELECT D.TIPO_GRUPO_SIDRA TIPOPRODUCTO, " +
        		"         COUNT (B.ARTICULO) CANTIDAD, " +
        		"         SUM (B.PRECIO_FINAL*A.TASA_CAMBIO) MONTO " +
        		"    FROM " + ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea) + " A, " +
        		"         TC_SC_VENTA_DET B, " +
        		"         " + ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea) + " C, " +
        		"         TC_SC_ARTICULO_INV D " +
        		"   WHERE     A.TCSCVENTAID = B.TCSCVENTAID " +
        		"         AND A.TCSCJORNADAVENID = C.TCSCJORNADAVENID " +
                "         AND A.TCSCCATPAISID = " + ID_PAIS +
                "         AND C.TCSCCATPAISID = A.TCSCCATPAISID " +
                "         AND D.TCSCCATPAISID = A.TCSCCATPAISID " +
        		"         AND A.VENDEDOR = C.VENDEDOR " +
        		"         AND B.ARTICULO = D.ARTICULO ";

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

        query += " GROUP BY D.TIPO_GRUPO_SIDRA ";
        return query;
    }

    public static List<DatosEfectividad> getDatosEfectividad(Connection conn, List<Filtro> filtros,
            List<Filtro> filFechas, InputReporteEfectividadVenta datos, String codArea, BigDecimal ID_PAIS) throws SQLException, ParseException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        DatosEfectividad objEfectividad = null;
        List<DatosEfectividad> lista = new ArrayList<DatosEfectividad>();

        String queryCabecera = queryEfectividadVenta( filtros, filFechas, codArea, ID_PAIS);
        
        log.trace("Query para obtener Reporte Efectividad Venta: " + queryCabecera);
        try {
            pstmt = conn.prepareStatement(queryCabecera);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                objEfectividad = new DatosEfectividad();
                objEfectividad.setIdDistribuidor(rst.getString("TCSCDTSID"));
                objEfectividad.setNombreDistribuidor(rst.getString("NOMBRES"));
                objEfectividad.setIdRuta(rst.getString("TCSCRUTAID"));
                objEfectividad.setNombreRuta(rst.getString("NOMBRE"));
                objEfectividad.setIdBodegaVirtual(rst.getString("TCSCBODEGAVIRTUALID"));
                objEfectividad.setNombreBodega(rst.getString("nombrebodega"));
                objEfectividad.setDpv(getListaPdv(conn, rst.getString("TCSCPUNTOVENTAID"), objEfectividad.getIdDistribuidor(), datos.getZona(), datos, codArea, ID_PAIS));

                lista.add(objEfectividad);
            }
        } finally {
            // cerrar recursos
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }

        return lista;
    }

    public static String queryEfectividadVenta(List<Filtro> filtros,
            List<Filtro> filFechas, String codArea, BigDecimal ID_PAIS) {
    	 String queryCabecera = "SELECT TCSCDTSID, " +
         		"       NOMBRES, " +
         		"       TCSCRUTAID, " +
         		"       NOMBRE, " +
         		"       TCSCBODEGAVIRTUALID, " +
         		"       NOMBREBODEGA, " +
         		"       SECUSUARIOID, " +
         		"       TCSCPUNTOVENTAID " +
         		"  FROM (SELECT D.TCSCDTSID, " +
         		"               D.NOMBRES, " +
         		"               B.TCSCRUTAID, " +
         		"               B.NOMBRE, " +
         		"               (SELECT TCSCBODEGAVIRTUALID " +
         		"                  FROM " + ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", codArea) + " " +
         		"                 WHERE TCSCBODEGAVIRTUALID = " +
         		"                          (SELECT TCSCBODEGAVIRTUALID " +
         		"                             FROM TC_SC_BODEGA_VENDEDOR " +
         		"                            WHERE TCSCCATPAISID = " + ID_PAIS + " AND VENDEDOR = B.SECUSUARIOID)) " +
         		"                  TCSCBODEGAVIRTUALID, " +
         		"               (SELECT NOMBRE " +
         		"                  FROM " + ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", codArea) + " " +
         		"                 WHERE TCSCBODEGAVIRTUALID = " +
         		"                          (SELECT TCSCBODEGAVIRTUALID " +
         		"                             FROM TC_SC_BODEGA_VENDEDOR " +
         		"                            WHERE TCSCCATPAISID = " + ID_PAIS + " AND VENDEDOR = B.SECUSUARIOID)) " +
         		"                  NOMBREBODEGA, " +
         		"               B.SECUSUARIOID, " +
         		"               C.TCSCPUNTOVENTAID " +
         		"          FROM TC_SC_DTS D, " +
         		"               TC_SC_RUTA B, " +
         		"  (SELECT R.SECUSUARIOID, C.TCSCPUNTOVENTAID " +
         		"    FROM TC_SC_RUTA_PDV C, TC_SC_RUTA R, " + ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea) + " A " +
         		"   WHERE     C.TCSCPUNTOVENTAID = A.IDTIPO " +
         		"         AND A.TIPO = 'PDV' " +
                 "         AND A.TCSCCATPAISID = " + ID_PAIS +
                 "         AND C.TCSCCATPAISID = A.TCSCCATPAISID " +
                 "         AND R.TCSCCATPAISID = A.TCSCCATPAISID " +
         		"         AND R.TCSCRUTAID = C.TCSCRUTAID " +
         		"         AND R.SECUSUARIOID = A.VENDEDOR " ;

         if (!filFechas.isEmpty()) {
             for (int i = 0; i < filFechas.size(); i++) {
                 if (filFechas.get(i).getOperator().toString() == "between") {
                     queryCabecera += " AND " + filFechas.get(i).getField() + " " + filFechas.get(i).getOperator()
                             + filFechas.get(i).getValue();
                 } else {

                     queryCabecera += " AND " + filFechas.get(i).getField() + " " + filFechas.get(i).getOperator()
                             + "" + filFechas.get(i).getValue() + "";
                 }
             }
         }

         queryCabecera += "         GROUP BY R.SECUSUARIOID, C.TCSCPUNTOVENTAID) C " +
         		"         WHERE D.TCSCDTSID = B.TCSCDTSID AND C.SECUSUARIOID = B.SECUSUARIOID" +
                     "         AND B.TCSCCATPAISID = " + ID_PAIS +
                     "         AND D.TCSCCATPAISID = B.TCSCCATPAISID " +
                     ") " +
         		" WHERE 1 = 1 "; 

         if (!filtros.isEmpty()) {
             for (int i = 0; i < filtros.size(); i++) {
                 if (filtros.get(i).getOperator().toString() == "between") {
                     queryCabecera += " AND " + filtros.get(i).getField() + " " + filtros.get(i).getOperator()
                             + filtros.get(i).getValue();
                 } else {

                     queryCabecera += " AND " + filtros.get(i).getField() + " " + filtros.get(i).getOperator() + ""
                             + filtros.get(i).getValue() + "";
                 }
             }
         }

         queryCabecera += " ORDER BY TCSCDTSID, TCSCBODEGAVIRTUALID, TCSCRUTAID, SECUSUARIOID";
         
         return queryCabecera;
    }
    private static List<InfoPDV> getListaPdv(Connection conn, String pdv, String dts, String zona, InputReporteEfectividadVenta datos, String codArea, BigDecimal ID_PAIS) throws SQLException, ParseException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        InfoPDV objPdv = null;
        List<InfoPDV> lista = new ArrayList<InfoPDV>();

      
	        String queryPdv = querypdv( pdv,  dts,  zona, datos, codArea, ID_PAIS);
            

            log.trace("Query para obtener PDV's por Efectividad Venta: " + queryPdv);
           try {
            pstmt = conn.prepareStatement(queryPdv);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                objPdv = new InfoPDV();
                objPdv.setIdPuntoVenta(rst.getString("TCSCPUNTOVENTAID"));
                objPdv.setDistrito(rst.getString("DISTRITO"));
                objPdv.setNombrePdv(rst.getString("NOMBRE"));
                objPdv.setDepartamento(rst.getString("DEPARTAMENTO"));
                objPdv.setMunicipio(rst.getString("MUNICIPIO"));
                objPdv.setTcsczonacomercialid(rst.getString("TCSCZONACOMERCIALID"));
                objPdv.setQr(rst.getString("QR"));
                objPdv.setNumeroRecarga(rst.getString("NUM_RECARGA"));
                objPdv.setNombreDuenio(rst.getString("NOMBRE_DUENIO"));
                objPdv.setDocumentoDuenio(rst.getString("DOCUMENTO_DUENIO"));
                objPdv.setComparacion(getDatosComparacionVenta(conn, objPdv.getIdPuntoVenta(), datos, codArea, ID_PAIS));

                lista.add(objPdv);
            }
        } finally {
            // cerrar recursos
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }

        return lista;
    }

    public static String querypdv(String pdv, String dts, String zona, InputReporteEfectividadVenta datos, String codArea, BigDecimal ID_PAIS) {
    	 String queryPdv = "SELECT A.TCSCDTSID, " +
	        		"       B.TCSCPUNTOVENTAID, " +
	        		"       B.DISTRITO, " +
	        		"       B.DEPARTAMENTO, " +
	        		"       B.MUNICIPIO, " +
	        		"       B.NOMBRE, " +
	        		"		B.TCSCZONACOMERCIALID, " +
	        		"		B.QR, " +
	        		"		(SELECT NOMBRES || ' ' || APELLIDOS FROM TC_SC_ENCARGADO_PDV WHERE TCSCPUNTOVENTAID = B.TCSCPUNTOVENTAID) NOMBRE_DUENIO, " + 
	        		"		(SELECT CEDULA FROM TC_SC_ENCARGADO_PDV WHERE TCSCPUNTOVENTAID = B.TCSCPUNTOVENTAID) DOCUMENTO_DUENIO, " + 
	        		"		(SELECT NUM_RECARGA FROM TC_SC_NUMRECARGA WHERE IDTIPO = B.TCSCPUNTOVENTAID AND TIPO = 'PDV' AND ORDEN = 1 AND TCSCCATPAISID = B.TCSCCATPAISID) NUM_RECARGA " +
	        		" FROM TC_SC_DTS A, " + ControladorBase.getParticion(PuntoVenta.N_TABLA, Conf.PARTITION, datos.getIdDistribuidor(), codArea) + " B " +
	        		" WHERE A.TCSCDTSID = B.TCSCDTSID " +
	        		" 		AND A.TCSCDTSID = " + dts +
                 " 		AND A.TCSCCATPAISID = " + ID_PAIS +
                 " 		AND B.TCSCCATPAISID = A.TCSCCATPAISID " +
	        		"		AND B.TCSCPUNTOVENTAID = " + pdv;

         if (zona != null && !"".equals(zona.trim())) {
             queryPdv += " AND B.TCSCZONACOMERCIALID = '" + zona + "'";
         }

         log.trace("Query para obtener PDV´s por Efectividad Venta: " + queryPdv);
         return queryPdv;
    }
    public static String diasVentas;

    private static List<ComparacionVenta> getDatosComparacionVenta(Connection conn, String pdv,
            InputReporteEfectividadVenta datos, String codArea, BigDecimal ID_PAIS) throws SQLException, ParseException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        ComparacionVenta objVenta = null;
        List<ComparacionVenta> lista = new ArrayList<ComparacionVenta>();
        HashMap<String, ComparacionVenta> mapComparacionVenta = new HashMap<String, ComparacionVenta>();
        HashMap<String, List<ComparacionVentaArticulo>> mapComparacionArticulo = new HashMap<String, List<ComparacionVentaArticulo>>();
        SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
        SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA);
        Date inicio = formatoFecha.parse(datos.getFechaInicioMesActual());
        String strInicio = FORMATO_FECHA.format(inicio);
        Date fin = formatoFecha.parse(datos.getFechaFinMesActual());
        String strFin = FORMATO_FECHA.format(fin);

        Date inicio2 = formatoFecha.parse(datos.getFechaInicioMesAnterior());
        String strInicio2 = FORMATO_FECHA.format(inicio2);
        Date fin2 = formatoFecha.parse(datos.getFechaFinMesAnterior());
        String strFin2 = FORMATO_FECHA.format(fin2);

     
	        String queryVenta = queryVenta(strInicio2, strFin2, strInicio, strFin, pdv, codArea); 

            log.trace("Query para obtener la Comparación de Venta para Efectividad Venta: " + queryVenta);
          try {
            pstmt = conn.prepareStatement(queryVenta);
            rst = pstmt.executeQuery();

            diasVentas = "";
            String dia = "";
            while (rst.next()) {
                objVenta = new ComparacionVenta();

                dia = rst.getString("diames");
                objVenta.setDiaSemana(rst.getString("diasemana"));
                objVenta.setDiaMes(dia);
                objVenta.setMes(rst.getString("mes"));
                objVenta.setAnio(rst.getString("anio"));
                objVenta.setVenta(rst.getString("venta"));
                objVenta.setCantProductoFacturadoAnterior(rst.getString("facturadoant"));
                objVenta.setCantProductoFacturadoActual(rst.getString("facturadoact"));
                objVenta.setMontoTotalFacturadoAnterior(rst.getString("totalant"));
                objVenta.setMontoTotalFacturadoActual(rst.getString("totalact"));
                objVenta.setDiferencia(rst.getString("dif"));
                objVenta.setVariacion(rst.getString("variacion"));

                mapComparacionVenta.put(dia, objVenta);
                diasVentas += dia + ",";
                dia = "";
            }
        } finally {
            // cerrar recursos
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }
        mapComparacionArticulo = getComparacionVentaArticulo(conn, strInicio, strFin, strInicio2, strFin2, pdv, mapComparacionVenta, codArea, ID_PAIS);
        lista = unirComparacionVentaArticulo(mapComparacionArticulo, mapComparacionVenta);

        return lista;
    }
    public static String queryVenta( String strInicio2, String strFin2, String strInicio, String strFin,String pdv, String codArea) {
    	  String queryVenta = "  SELECT ACTUAL.DIAMES DIAMES, " +
	        		"         ACTUAL.DIASEMANA DIASEMANA, " +
	        		"         ACTUAL.MES MES, " +
	        		"         ACTUAL.ANIO ANIO, " +
	        		"         ACTUAL.VENTA, " +
	        		"         NVL (ANTES.CANTPROD, 0) FACTURADOANT, " +
	        		"         ACTUAL.CANTPROD FACTURADOACT, " +
	        		"         NVL (ANTES.MONTOTOTAL, 0) TOTALANT, " +
	        		"         ACTUAL.MONTOTOTAL TOTALACT, " +
	        		"         CASE " +
	        		"            WHEN ANTES.MONTOTOTAL IS NULL THEN ACTUAL.MONTOTOTAL " +
	        		"            ELSE (ACTUAL.MONTOTOTAL - ANTES.MONTOTOTAL) " +
	        		"         END " +
	        		"            DIF, " +
	        		"         CASE " +
	        		"            WHEN ACTUAL.MONTOTOTAL > NVL (ANTES.MONTOTOTAL, 0) " +
	        		"            THEN " +
	        		"                 ( (ACTUAL.MONTOTOTAL - NVL (ANTES.MONTOTOTAL, 0)) * 100) " +
	        		"               / ACTUAL.MONTOTOTAL " +
	        		"            ELSE " +
	        		"                 ( (ACTUAL.MONTOTOTAL - NVL (ANTES.MONTOTOTAL, 0)) * 100) " +
	        		"               / NVL (ANTES.MONTOTOTAL, 0) " +
	        		"         END " +
	        		"            VARIACION " +
	        		"    FROM (  SELECT TRIM(TO_CHAR (A.FECHA_EMISION, 'DAY', 'NLS_DATE_LANGUAGE=SPANISH')) DIASEMANA, " +
	        		"                   TO_CHAR (A.FECHA_EMISION, 'DD') DIAMES, " +
	        		"                   TO_CHAR (A.FECHA_EMISION, 'MM') MES, " +
	        		"                   TO_CHAR (A.FECHA_EMISION, 'YYYY') ANIO, " +
	        		"                   'SI' VENTA, " +
	        		"                   SUM (B.CANTIDAD) CANTPROD, " +
	        		"                   SUM (B.PRECIO_FINAL*A.TASA_CAMBIO) MONTOTOTAL " +
	        		"              FROM " + ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea) + " A, TC_SC_VENTA_DET B " +
	        		"             WHERE     A.TCSCVENTAID = B.TCSCVENTAID " +
	        		"                   AND A.IDTIPO = " + pdv +
	        		"                   AND A.TIPO = 'PDV' " +
	        		"                   AND TRUNC (A.FECHA_EMISION) >= TRUNC (TO_DATE ('" + strInicio + "', 'DD/MM/YYYY')) " +
	        		"                   AND TRUNC (A.FECHA_EMISION) <= TRUNC (TO_DATE ('" + strFin + "', 'DD/MM/YYYY')) " +
	        		"          GROUP BY TO_CHAR (A.FECHA_EMISION, 'DD'), " +
	        		"                   TRIM(TO_CHAR (A.FECHA_EMISION, 'DAY', 'NLS_DATE_LANGUAGE=SPANISH')), " +
	        		"                   TO_CHAR (A.FECHA_EMISION, 'MM'), " +
	        		"                   TO_CHAR (A.FECHA_EMISION, 'YYYY')) ACTUAL, " +
	        		"         (  SELECT TRIM(TO_CHAR (A.FECHA_EMISION, 'DAY', 'NLS_DATE_LANGUAGE=SPANISH')) DIASEMANA2, " +
	        		"                   TO_CHAR (A.FECHA_EMISION, 'DD') DIAMES2, " +
	        		"                   TO_CHAR (A.FECHA_EMISION, 'MM') MES2, " +
	        		"                   TO_CHAR (A.FECHA_EMISION, 'YYYY') ANIO2, " +
	        		"                   'SI' VENTA, " +
	        		"                   SUM (B.CANTIDAD) CANTPROD, " +
	        		"                   SUM (B.PRECIO_FINAL*A.TASA_CAMBIO) MONTOTOTAL " +
	        		"              FROM " + ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea) + " A, TC_SC_VENTA_DET B " +
	        		"             WHERE     A.TCSCVENTAID = B.TCSCVENTAID " +
	        		"                   AND A.IDTIPO = " + pdv +
	        		"                   AND A.TIPO = 'PDV' " +
	        		"                   AND TRUNC (A.FECHA_EMISION) >= TRUNC (TO_DATE ('" + strInicio2 + "', 'DD/MM/YYYY')) " +
	        		"                   AND TRUNC (A.FECHA_EMISION) <= TRUNC (TO_DATE ('" + strFin2 + "', 'DD/MM/YYYY')) " +
	        		"          GROUP BY TO_CHAR (A.FECHA_EMISION, 'DD'), " +
	        		"                   TRIM(TO_CHAR (A.FECHA_EMISION, 'DAY', 'NLS_DATE_LANGUAGE=SPANISH')), " +
	        		"                   TO_CHAR (A.FECHA_EMISION, 'MM'), " +
	        		"                   TO_CHAR (A.FECHA_EMISION, 'YYYY')) ANTES " +
	        		"   WHERE ACTUAL.DIAMES = ANTES.DIAMES2(+) " +
	        		"ORDER BY 1 ASC " ; 
    	  
    	  return queryVenta;
    }

    public static HashMap<String, List<ComparacionVentaArticulo>> getComparacionVentaArticulo(Connection conn,
            String iActual, String fActual, String iAntes, String fAntes, String pdv,
            HashMap<String, ComparacionVenta> map, String codArea, BigDecimal ID_PAIS) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        HashMap<String, List<ComparacionVentaArticulo>> mapComparacionArticulo = new HashMap<String, List<ComparacionVentaArticulo>>();
        ComparacionVentaArticulo objArticulo = null;
        List<ComparacionVentaArticulo> lstArticulo = new ArrayList<ComparacionVentaArticulo>();
        String diaComparacion = "";
        String diaMap = "";
     
            if (map != null && !map.isEmpty()) {
	        	String queryArticulo =queryArt(iActual, fActual, iAntes, fAntes, pdv, codArea, ID_PAIS);

                log.trace("Query para obtener la Comparaci\u00F3n por dia por articulo: " + queryArticulo);
                try {
	                pstmt = conn.prepareStatement(queryArticulo);
	                rst = pstmt.executeQuery();
	
	                boolean primero = true;
	                while (rst.next()) {
	                    diaComparacion = rst.getString("diames");
	                    if (!primero && !diaComparacion.equals(diaMap)) {
	                        mapComparacionArticulo.put(diaMap, lstArticulo);
	                        lstArticulo = new ArrayList<ComparacionVentaArticulo>();
	                    }
	
	                    objArticulo = new ComparacionVentaArticulo();
	                    objArticulo.setDiames(diaComparacion);
	                    objArticulo.setIdArticulo(rst.getString("idarticulo"));
	                    objArticulo.setDescripcion(rst.getString("descripcion"));
	                    objArticulo.setTipoProducto(rst.getString("tipo_grupo_sidra"));
	                    objArticulo.setCantProductoFacturadoActual(rst.getString("facturadoact"));
	                    objArticulo.setCantProductoFacturadoAnterior(rst.getString("facturadoant"));
	                    objArticulo.setMontoTotalFacturadoActual(rst.getString("totalact"));
	                    objArticulo.setMontoTotalFacturadoAnterior(rst.getString("totalant"));
	                    objArticulo.setDiferencia(rst.getString("dif"));
	                    objArticulo.setVariacion(rst.getString("variacion"));
	                    lstArticulo.add(objArticulo);
	                    diaMap = diaComparacion;
	                    primero = false;
	                }
                } finally {
                    DbUtils.closeQuietly(pstmt);
                    DbUtils.closeQuietly(rst);
                }
            }
            mapComparacionArticulo.put(diaComparacion, lstArticulo);
        
        return mapComparacionArticulo;
    }

    public static String queryArt(  String iActual, String fActual, String iAntes, String fAntes, String pdv, String codArea, BigDecimal ID_PAIS) {
    	String queryArticulo = "SELECT ACTUAL.DIAMES DIAMES, " +
    			"         NVL (ANTES.CANTPROD2, 0) FACTURADOANT, " +
    			"         ACTUAL.CANTPROD FACTURADOACT, " +
    			"         NVL (ANTES.MONTOTOTAL2, 0) TOTALANT, " +
    			"         ACTUAL.MONTOTOTAL TOTALACT, " +
    			"         CASE " +
    			"            WHEN ANTES.MONTOTOTAL2 IS NULL THEN ACTUAL.MONTOTOTAL " +
    			"            ELSE (ACTUAL.MONTOTOTAL - ANTES.MONTOTOTAL2) " +
    			"         END " +
    			"            DIF, " +
    			"         CASE " +
    			"            WHEN ACTUAL.MONTOTOTAL > NVL (ANTES.MONTOTOTAL2, 0) " +
    			"            THEN " +
    			"                 ( (ACTUAL.MONTOTOTAL - NVL (ANTES.MONTOTOTAL2, 0)) * 100) " +
    			"               / ACTUAL.MONTOTOTAL " +
    			"            ELSE " +
    			"                 ( (ACTUAL.MONTOTOTAL - NVL (ANTES.MONTOTOTAL2, 0)) * 100) " +
    			"               / ANTES.MONTOTOTAL2 " +
    			"         END " +
    			"            VARIACION, " +
    			"            ACTUAL.IDARTICULO, " +
    			"            ACTUAL.DESCRIPCION, " +
    			"            ACTUAL.TIPO_GRUPO_SIDRA " +
    			"            FROM " +
    			"  (SELECT TO_CHAR (A.FECHA_EMISION, 'DD') DIAMES, " +
    			"         SUM (B.CANTIDAD) CANTPROD, " +
    			"         SUM (B.PRECIO_FINAL*A.TASA_CAMBIO) MONTOTOTAL, " +
    			"         B.ARTICULO IDARTICULO, " +
    			"         D.DESCRIPCION, " +
    			"         SUM (B.CANTIDAD) CANTIDAD, " +
    			"         SUM (B.PRECIO_FINAL*A.TASA_CAMBIO) MONTO, " +
    			"         D.TIPO_GRUPO_SIDRA " +
    			"    FROM " + ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea) + " A, " +
    			"         TC_SC_VENTA_DET B, " +
    			"         " + ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea) + " C, " +
    			"         TC_SC_ARTICULO_INV D " +
    			"   WHERE     A.TCSCVENTAID = B.TCSCVENTAID " +
                "         AND A.TCSCCATPAISID = " + ID_PAIS +
                "         AND C.TCSCCATPAISID = A.TCSCCATPAISID " +
                "         AND D.TCSCCATPAISID = A.TCSCCATPAISID " +
    			"         AND B.TIPO_INV = 'INV_TELCA' " +
    			"         AND A.IDTIPO = " + pdv +
    			"         AND A.TIPO = 'PDV' " +
    			"         AND A.TCSCJORNADAVENID = C.TCSCJORNADAVENID " +
    			"         AND A.VENDEDOR = C.VENDEDOR " +
    			"         AND B.ARTICULO = D.ARTICULO " +
    			"         AND TRUNC (A.FECHA_EMISION) >= TRUNC (TO_DATE ('" + iActual + "', 'DD/MM/YYYY')) " +
    			"         AND TRUNC (A.FECHA_EMISION) <= TRUNC (TO_DATE ('" + fActual + "', 'DD/MM/YYYY')) " +
    			"GROUP BY B.ARTICULO, " +
    			"         D.DESCRIPCION, " +
    			"         D.TIPO_GRUPO_SIDRA, " +
    			"         TO_CHAR (A.FECHA_EMISION, 'DD')) ACTUAL,  " +
    			"  (SELECT TO_CHAR (A.FECHA_EMISION, 'DD') DIAMES2, " +
    			"         SUM (B.CANTIDAD) CANTPROD2, " +
    			"         SUM (B.PRECIO_FINAL*A.TASA_CAMBIO) MONTOTOTAL2, " +
    			"         B.ARTICULO IDARTICULO2, " +
    			"         D.DESCRIPCION, " +
    			"         SUM (B.CANTIDAD) CANTIDAD2, " +
    			"         SUM (B.PRECIO_FINAL*A.TASA_CAMBIO) MONTO2, " +
    			"         D.TIPO_GRUPO_SIDRA " +
    			"    FROM " + ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea) + " A, " +
    			"         TC_SC_VENTA_DET B, " +
    			"         " + ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea) + " C, " +
    			"         TC_SC_ARTICULO_INV D " +
    			"   WHERE     A.TCSCVENTAID = B.TCSCVENTAID " +
                "         AND A.TCSCCATPAISID = " + ID_PAIS +
                "         AND C.TCSCCATPAISID = A.TCSCCATPAISID " +
                "         AND D.TCSCCATPAISID = A.TCSCCATPAISID " +
    			"         AND B.TIPO_INV = 'INV_TELCA' " +
    			"         AND A.IDTIPO = " + pdv + " " +
    			"         AND A.TIPO = 'PDV' " +
    			"         AND A.TCSCJORNADAVENID = C.TCSCJORNADAVENID " +
    			"         AND A.VENDEDOR = C.VENDEDOR " +
    			"         AND B.ARTICULO = D.ARTICULO " +
    			"         AND TRUNC (A.FECHA_EMISION) >= TRUNC (TO_DATE ('" + iAntes + "', 'DD/MM/YYYY')) " +
    			"         AND TRUNC (A.FECHA_EMISION) <= TRUNC (TO_DATE ('" + fAntes + "', 'DD/MM/YYYY')) " +
    			"GROUP BY B.ARTICULO, " +
    			"         D.DESCRIPCION, " +
    			"         D.TIPO_GRUPO_SIDRA, " +
    			"         TO_CHAR (A.FECHA_EMISION, 'DD')) ANTES " +
    			"WHERE ACTUAL.DIAMES = ANTES.DIAMES2(+) " +
    			"AND ACTUAL.IDARTICULO = ANTES.IDARTICULO2(+) " +
    			"ORDER BY 1 ASC " ;
    	
    	return queryArticulo;
    }
    /**
     * Este m\u00E9tedo se encarga de construir un lista de comparaci\u00F3n de ventas por
     * d\u00EDa, recibe dos hashmap, uno de comparaci\u00F3n de ventas y otro comparaci\u00F3n
     * de ventas por articulo de cada d\u00EDa.
     * 
     * Los item son extraidos de cada hashmap a trav\u00E9s de una llave que
     * representa el d\u00EDa del mes en que se realizo una venta, luego agrega a la
     * lista final un item de venta y dentro un listado de ventas de articulos.
     */
    private static List<ComparacionVenta> unirComparacionVentaArticulo(
            HashMap<String, List<ComparacionVentaArticulo>> mapArticulo, HashMap<String, ComparacionVenta> mapVenta) {
        List<ComparacionVenta> resultado = null;
        ComparacionVenta venta = null;
        List<ComparacionVentaArticulo> lstArticulo = null;

        if (mapVenta != null && !mapVenta.isEmpty()) {
            resultado = new ArrayList<ComparacionVenta>();
            String[] dia = diasVentas.split(",");

            for (int i = 0; i < dia.length; i++) {
                venta = mapVenta.get(dia[i]);
                lstArticulo = mapArticulo.get(dia[i]);
                venta.setComparacionArticulo(lstArticulo);
                resultado.add(venta);
            }
        }

        return resultado;
    }
}
