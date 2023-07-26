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

import com.consystec.sc.ca.ws.input.reporte.InputReporteCantInvJornada;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCantInvJornada;
import com.consystec.sc.sv.ws.metodos.CtrlReporteCantInvJornada;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionReporteCantInvJornada {
	private OperacionReporteCantInvJornada(){}
    private static final Logger log = Logger.getLogger(OperacionReporteCantInvJornada.class);

    public static OutputReporteCantInvJornada doGet(Connection conn, InputReporteCantInvJornada input,
            boolean esFinJornada, BigDecimal ID_PAIS) throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        OutputReporteCantInvJornada output = new OutputReporteCantInvJornada();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        Respuesta respuesta = new Respuesta();
        String estadoIniciada = UtileriasJava.getConfig(conn, Conf.GRUPO_JORNADA_ESTADOS, Conf.JORNADA_ESTADO_INICIADA, input.getCodArea());
        String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
        String sql = "";
        String idJornada = "";
        boolean filtroDTS = false;
        String listIdBodegas = "";

        try {
            if (input.getIdDTS() != null && !input.getIdDTS().equals("") && !((input.getIdJornada() != null && !input.getIdJornada().equals(""))
                        || (input.getIdVendedor() != null && !input.getIdVendedor().equals("")))) {
                // se obtienen los articulos de todas las bodegas del dts
                sql = queryCantInv( input);
                filtroDTS = true;

            } else {
                // se obtienen los articulos de la jornada unicamente
                idJornada = CtrlReporteCantInvJornada.getIdJornada(conn, input, estadoIniciada);
                if (idJornada.equals("")) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_INICIO_JORNADA_510, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());
                    return output;
                }

                sql = "SELECT * FROM TC_SC_REPORTE_CANT_INV_HEAD WHERE TCSCJORNADAVENID = ?"
                        + " AND TCSCCATPAISID = ?";
            }
            log.trace("Qry head: " + sql);

            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, new BigDecimal(idJornada));
            pstmt.setBigDecimal(2, ID_PAIS);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (rst.next()) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CARGA_INV_INICIAL_70, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    do {
                        InputReporteCantInvJornada item = new InputReporteCantInvJornada();
                        
                        item.setIdDTS(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "TCSCDTSID"));
                        item.setNombreDTS(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRE_DTS"));
                        item.setIdJornada(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "TCSCJORNADAVENID"));
                        item.setIdVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "VENDEDOR"));
                        item.setNombreVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRE_VENDEDOR"));
                        item.setUsuarioVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "USUARIO_VENDEDOR"));
                        item.setIdRutaPanel(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "ID_PANELRUTA"));
                        item.setTipoRutaPanel(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "TIPO_PANELRUTA"));
                        item.setNombreRutaPanel(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRE_PANELRUTA"));

                        if (filtroDTS) {
                            listIdBodegas = getListBodegas(conn, input.getIdDTS(), ID_PAIS);
                            item.setArticulos(getArticulosBodegas(conn, input, listIdBodegas, ID_PAIS));

                        } else {
                            listIdBodegas = UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "TCSCBODEGAVIRTUAL");
                            item.setArticulos(getArticulosJornada(conn, input, listIdBodegas, idJornada, estadoAlta, esFinJornada, ID_PAIS));
                        }

                        if (item.getArticulos().isEmpty()) {
                            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_INV_INICIAL_847,
                                    null, nombreClase, nombreMetodo, null, input.getCodArea());
                            break;
                        } else {
                            output.setDatos(item);
                        }
                    } while (rst.next());

                } else {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_INV_INICIAL_847, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());
                }
            }
            return output;

        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
            output.setRespuesta(respuesta);
        }
    }

    
    public static String queryCantInv(InputReporteCantInvJornada input) {
    	String sql="";
     
        // se obtienen los articulos de todas las bodegas del dts
        sql = "SELECT "
                + Distribuidor.CAMPO_TC_SC_DTS_ID + ", " + Distribuidor.CAMPO_NOMBRES + " AS NOMBRE_DTS, "
                + " '' TCSCJORNADAVENID, '' VENDEDOR, '' NOMBRE_VENDEDOR, '' USUARIO_VENDEDOR,"
                + " '' ID_PANELRUTA, '' TIPO_PANELRUTA, '' NOMBRE_PANELRUTA"
            + " FROM " + Distribuidor.N_TABLA
            + " WHERE " + Distribuidor.CAMPO_TC_SC_DTS_ID + " = " + input.getIdDTS();
        

    
        return sql;
    }
    private static String getListBodegas(Connection conn, String idDTS, BigDecimal ID_PAIS) throws SQLException {
        String listIdBodegas = "";
        PreparedStatement pstmt = null;
        ResultSet rst = null;

       
	        String sql = "SELECT BV.TCSCBODEGAVIRTUALID FROM TC_SC_BODEGA_VENDEDOR BV, TC_SC_VEND_DTS V "
	                + "WHERE BV.TCSCCATPAISID = V.TCSCCATPAISID AND V.TCSCCATPAISID = ?" 
	                    + " AND BV.VENDEDOR = V.VENDEDOR AND V.TCSCDTSID = ?"
	                + " UNION SELECT TCSCBODEGAVIRTUALID FROM TC_SC_BODEGAVIRTUAL "
	                + "WHERE TCSCCATPAISID = ?"
	                    + " AND IDBODEGA_ORIGEN = (SELECT TCSCBODEGAVIRTUALID FROM TC_SC_DTS WHERE TCSCDTSID = ?" 
	                        + " AND TCSCCATPAISID = ?) "
	                    + "OR TCSCBODEGAVIRTUALID = (SELECT TCSCBODEGAVIRTUALID FROM TC_SC_DTS WHERE TCSCDTSID = ?" 
	                        + " AND TCSCCATPAISID = ?)";

            log.trace("Qry bodegas: " + sql);
         try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, ID_PAIS);
            pstmt.setBigDecimal(2,  new BigDecimal(idDTS));
            pstmt.setBigDecimal(3, ID_PAIS);
            pstmt.setBigDecimal(4,  new BigDecimal(idDTS));
            pstmt.setBigDecimal(5, ID_PAIS);
            pstmt.setBigDecimal(6,  new BigDecimal(idDTS));
            pstmt.setBigDecimal(7, ID_PAIS);
            rst = pstmt.executeQuery();

            if (rst != null) {
	            if (!rst.next()) {
	                // no hay registros
	            } else {
	                // hay registros
	                do {
                        listIdBodegas += UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "TCSCBODEGAVIRTUALID") + ",";
                    } while (rst.next());
                }
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }

        return listIdBodegas.substring(0, listIdBodegas.length() - 1);
    }

    private static List<InputReporteCantInvJornada> getArticulosJornada(Connection conn,
            InputReporteCantInvJornada input, String idBodega, String idJornada, String estadoAlta,
            boolean esFinJornada, BigDecimal ID_PAIS) throws SQLException {
        List<InputReporteCantInvJornada> list = new ArrayList<InputReporteCantInvJornada>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        PreparedStatement pstmt1 = null;
        ResultSet rst1 = null;
        String sql = null;
        String filtrosExtras = CtrlReporteCantInvJornada.getCondicionDetalle(input);

            sql =getArtJornada( idJornada,  filtrosExtras, ID_PAIS) ;

            log.trace("Qry detalle existente: " + sql);
           try{
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

                if (rst.next()) {
                    // hay registros
                    if (!esFinJornada) {
                        List<InputReporteCantInvJornada> datosRecarga = getDatosRecarga(conn, idJornada, estadoAlta, ID_PAIS);
                        list.addAll(datosRecarga);
                    }

                    do {
                        InputReporteCantInvJornada item = new InputReporteCantInvJornada();
                        item.setIdArticulo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "ARTICULO"));
                        item.setDescripcion(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRE_ART"));
                        item.setTipoInv(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "TIPO_INV"));
                        item.setTipoGrupo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "TIPO_GRUPO_SIDRA"));
                        item.setCantInicial(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_INICIAL"));
                        item.setCantReservada(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_RESERVADO"));
                        item.setCantVendida(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_VENDIDA"));
                        item.setCantProcDevolucion(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_PROC_DEV"));
                        item.setCantDevuelta(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_DEVOLUCION"));
                        item.setCantProcSiniestro(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_PROC_SINIESTRO"));
                        item.setCantSiniestrada(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_SINIESTRO"));
                        item.setCantFinal(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_DISPONIBLE"));

                        list.add(item);
                    } while (rst.next());
                }
           		}finally{
                	DbUtils.closeQuietly(pstmt);
                	DbUtils.closeQuietly(rst);
                }
                 if(list.isEmpty()) {
                    log.trace("no hay detalle existente, se obtiene bajo demanda");
                    //no tiene registros, se consultan bajo demanda
                    sql =  getArtJornada2( idJornada,  filtrosExtras, idBodega, input.getCodArea(), ID_PAIS);

                    log.trace("Qry detalle: " + sql);

                    try {
                    pstmt1 = conn.prepareStatement(sql);
                    rst1 = pstmt1.executeQuery();

                    if (rst1 != null) {
                        if (!rst1.next()) {
                            // no hay registros
                        } else {
                            // hay registros
                            do {
                                InputReporteCantInvJornada item = new InputReporteCantInvJornada();
                                item.setIdArticulo(UtileriasJava.getRstValue(rst1, Conf.TIPO_TEXTO, "ARTICULO"));
                                item.setDescripcion(UtileriasJava.getRstValue(rst1, Conf.TIPO_TEXTO, "NOMBRE_ART"));
                                item.setTipoInv(UtileriasJava.getRstValue(rst1, Conf.TIPO_TEXTO, "TIPO_INV"));
                                item.setTipoGrupo(UtileriasJava.getRstValue(rst1, Conf.TIPO_TEXTO, "TIPO_GRUPO_SIDRA"));
                                item.setCantInicial(UtileriasJava.getRstValue(rst1, Conf.TIPO_TEXTO, "CANT_INICIAL"));
                                item.setCantReservada(UtileriasJava.getRstValue(rst1, Conf.TIPO_TEXTO, "CANT_RESERVADA"));
                                item.setCantVendida(UtileriasJava.getRstValue(rst1, Conf.TIPO_TEXTO, "CANT_VENDIDA"));
                                item.setCantProcDevolucion(UtileriasJava.getRstValue(rst1, Conf.TIPO_TEXTO, "CANT_PROC_DEV"));
                                item.setCantDevuelta(UtileriasJava.getRstValue(rst1, Conf.TIPO_TEXTO, "CANT_DEVUELTA"));
                                item.setCantProcSiniestro(UtileriasJava.getRstValue(rst1, Conf.TIPO_TEXTO, "CANT_PROC_SIN"));
                                item.setCantSiniestrada(UtileriasJava.getRstValue(rst1, Conf.TIPO_TEXTO, "CANT_SINIESTRADA"));
                                item.setCantFinal(new Integer(item.getCantInicial()) - new Integer(item.getCantReservada())
                                        - new Integer(item.getCantVendida()) - new Integer(item.getCantProcDevolucion())
                                        - new Integer(item.getCantDevuelta()) - new Integer(item.getCantProcSiniestro())
                                        - new Integer(item.getCantSiniestrada()) + "");

                                list.add(item);
                            } while (rst1.next());
                        }
                    }  } finally {
                        DbUtils.closeQuietly(pstmt1);
                        DbUtils.closeQuietly(rst1);
                    }
            }
      

        return list;
    }
    public static String getArtJornada(String idJornada, String filtrosExtras, BigDecimal ID_PAIS) {
    	String sql="";
        sql = "WITH DET_ARTICULOS AS (SELECT * FROM TC_SC_CANT_INV_JORNADA WHERE TCSCCATPAISID = " + ID_PAIS
                + " AND IDJORNADA_RESPONSABLE = " + idJornada + " AND CANT_DISPONIBLE IS NOT NULL) "
            + "SELECT D.ARTICULO, D.TIPO_INV, CASE D.TIPO_INV "
                + "WHEN 'INV_TELCA' THEN (SELECT DESCRIPCION FROM TC_SC_ARTICULO_INV WHERE ARTICULO = D.ARTICULO AND TCSCCATPAISID = D.TCSCCATPAISID) "
                + "WHEN 'INV_SIDRA' THEN (SELECT DESCRIPCION FROM TC_SC_ART_PROMOCIONAL WHERE TCSCARTPROMOCIONALID = D.ARTICULO AND TCSCCATPAISID = D.TCSCCATPAISID) "
            + "END AS NOMBRE_ART, D.CANT_INICIAL, D.CANT_RESERVADO, D.CANT_VENDIDA, D.CANT_PROC_DEV, "
            + "D.CANT_DEVOLUCION, D.CANT_PROC_SINIESTRO, D.CANT_SINIESTRO, D.CANT_DISPONIBLE, D.TIPO_GRUPO_SIDRA FROM DET_ARTICULOS D"
            + filtrosExtras;
        return sql     		;
    }
    
    public static String getArtJornada2(String idJornada, String filtrosExtras,String idBodega, String codArea, BigDecimal ID_PAIS) {
    	String sql="";
    	  sql = "WITH REG_INV AS (SELECT TCSCINVENTARIOID FROM TC_SC_ID_INV_JORNADA WHERE TCSCJORNADAVENID = " + idJornada
                  + " UNION SELECT TCSCINVENTARIOINVID FROM " + ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, "", codArea)
                      + " WHERE TCSCCATPAISID = " + ID_PAIS + " AND CREADO_EL >= (SELECT CREADO_EL FROM "
                              + ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea)
                              + " WHERE TCSCCATPAISID = " + ID_PAIS
                                  + " AND TCSCJORNADAVENID = " + idJornada + ") AND TCSCBODEGAVIRTUALID = " + idBodega + "), "

                  + "DET_ARTICULOS AS (SELECT * FROM TC_SC_REPORTE_CANT_INV_DET WHERE TCSCCATPAISID = " + ID_PAIS
                      + " AND TCSCJORNADAVENID = " + idJornada + "), "

                  + "DET_ARTICULOS_SALIDAS AS (SELECT ARTICULO, TIPO_INV, NVL(CANT_VENDIDA,0) CANT_VENDIDA, NVL(CANT_DEVOLUCION,0) CANT_DEVOLUCION, "
                      + "NVL(CANT_SINIESTRO,0) CANT_SINIESTRO FROM TC_SC_CANT_INV_JORNADA "
                      + "WHERE TCSCCATPAISID = " + ID_PAIS + " AND IDJORNADA_RESPONSABLE = " + idJornada + "),"

                  + "INVENTARIO AS (SELECT ARTICULO, CANTIDAD, TIPO_INV, TIPO_GRUPO_SIDRA, ESTADO FROM " + ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, "", codArea)
                          + " WHERE TCSCCATPAISID = " + ID_PAIS + " AND TCSCINVENTARIOINVID IN (SELECT TCSCINVENTARIOID FROM REG_INV)), "


                  + "ESTADO AS (SELECT VALOR, NOMBRE FROM TC_SC_CONFIGURACION WHERE TCSCCATPAISID = " + ID_PAIS + " AND UPPER(GRUPO) = '" + Conf.GRUPO_ESTADOS_INVENTARIO + "' AND UPPER(ESTADO) = 'ALTA') "

                  + "SELECT D.ARTICULO, D.NOMBRE_ART, D.TIPO_INV, D.CANT_INICIAL, D.TIPO_GRUPO_SIDRA, "
                  + "(SELECT NVL(SUM(CANTIDAD), 0) FROM INVENTARIO WHERE ESTADO = (SELECT VALOR FROM ESTADO WHERE UPPER(NOMBRE) = '" + Conf.INV_EST_RESERVADO.toUpperCase() + "')"
                          + " AND ARTICULO = D.ARTICULO AND TIPO_INV = D.TIPO_INV) AS CANT_RESERVADA, "
                  + "(SELECT CANT_VENDIDA FROM DET_ARTICULOS_SALIDAS WHERE ARTICULO = D.ARTICULO AND TIPO_INV = D.TIPO_INV) CANT_VENDIDA, "

                  + "(SELECT NVL(SUM(CANTIDAD), 0) FROM INVENTARIO WHERE ESTADO = (SELECT VALOR FROM ESTADO WHERE UPPER(NOMBRE) = '" + Conf.INV_EST_DEVOLUCION.toUpperCase() + "')"
                          + " AND ARTICULO = D.ARTICULO AND TIPO_INV = D.TIPO_INV) AS CANT_PROC_DEV, "
           
                  + "(SELECT CANT_DEVOLUCION FROM DET_ARTICULOS_SALIDAS WHERE ARTICULO = D.ARTICULO AND TIPO_INV = D.TIPO_INV) CANT_DEVUELTA, "

                  + "(SELECT NVL(SUM(CANTIDAD), 0) FROM INVENTARIO WHERE ESTADO = (SELECT VALOR FROM ESTADO WHERE UPPER(NOMBRE) = '" + Conf.INV_EST_SINIESTRO.toUpperCase() + "')"
                          + " AND ARTICULO = D.ARTICULO AND TIPO_INV = D.TIPO_INV) AS CANT_PROC_SIN, "
               
                  + "(SELECT CANT_SINIESTRO FROM DET_ARTICULOS_SALIDAS WHERE ARTICULO = D.ARTICULO AND TIPO_INV = D.TIPO_INV) CANT_SINIESTRADA "

                  + "FROM DET_ARTICULOS D" + filtrosExtras;
    	  return sql;
    }

    private static List<InputReporteCantInvJornada> getDatosRecarga(Connection conn, String idJornada,
            String estadoAlta, BigDecimal ID_PAIS) throws SQLException {
        List<InputReporteCantInvJornada> list = new ArrayList<InputReporteCantInvJornada>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String sql = null;

       
            sql = "WITH RECARGA AS (SELECT VALOR FROM TC_SC_CONFIGURACION WHERE TCSCCATPAISID = ?"
                    + " AND GRUPO = ? AND NOMBRE = ?) "
                + "SELECT ARTICULO, 'INV_TELCA' TIPO_INV, DESCRIPCION NOMBRE_ART, NVL(J.SALDO_PAYMENT, 0) CANT_INICIAL, 0 CANT_RESERVADO, "
                    + "0 CANT_VENDIDA, 0 CANT_PROC_DEV, 0 CANT_DEVOLUCION, 0 CANT_PROC_SINIESTRO, 0 CANT_SINIESTRO, "
                    + "NVL(J.SALDO_PAYMENT_FINAL, 0) CANT_DISPONIBLE, TIPO_GRUPO_SIDRA "
                    + "FROM TC_SC_JORNADA_VEND J, (SELECT ARTICULO, DESCRIPCION, TIPO_GRUPO_SIDRA FROM TC_SC_ARTICULO_INV WHERE "
                        + "TCSCCATPAISID =? AND ESTADO =?"
                        + " AND ARTICULO = (SELECT VALOR FROM RECARGA)) A WHERE J.TCSCJORNADAVENID = ?"
                        + " AND J.TCSCCATPAISID = ?";

            log.trace("Qry datos recarga: " + sql);
            try {
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setBigDecimal(1, ID_PAIS);
	            pstmt.setString(2, Conf.GRUPO_ARTICULO_CANTIDAD);
	            pstmt.setString(3,Conf.ARTICULO_RECARGA);
	            pstmt.setBigDecimal(4, ID_PAIS);
	            pstmt.setString(5, estadoAlta);
	            pstmt.setBigDecimal(6, new BigDecimal(idJornada));
	            pstmt.setBigDecimal(7, ID_PAIS);
	            rst = pstmt.executeQuery();

                if (rst.next()) {
                    do {
                        InputReporteCantInvJornada item = new InputReporteCantInvJornada();
                        item.setIdArticulo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "ARTICULO"));
                        item.setDescripcion(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRE_ART"));
                        item.setTipoInv(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "TIPO_INV"));
                        item.setTipoGrupo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "TIPO_GRUPO_SIDRA"));
                        item.setCantInicial(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_INICIAL"));
                        item.setCantReservada(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_RESERVADO"));
                        item.setCantVendida(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_VENDIDA"));
                        item.setCantProcDevolucion(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_PROC_DEV"));
                        item.setCantDevuelta(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_DEVOLUCION"));
                        item.setCantProcSiniestro(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_PROC_SINIESTRO"));
                        item.setCantSiniestrada(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_SINIESTRO"));
                        item.setCantFinal(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_DISPONIBLE"));

                        list.add(item);
                    } while (rst.next());
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }

        return list;
    }

    private static List<InputReporteCantInvJornada> getArticulosBodegas(Connection conn, InputReporteCantInvJornada input, String listIdBodegas, BigDecimal ID_PAIS)
            throws SQLException {
        List<InputReporteCantInvJornada> list = new ArrayList<InputReporteCantInvJornada>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String sql = null;
        String filtrosExtras = CtrlReporteCantInvJornada.getCondicionDetalle(input);

       
            sql = queryArtBodegas(listIdBodegas,  filtrosExtras, ID_PAIS);

            log.trace("Qry detalle: " + sql);
            try{
            	pstmt = conn.prepareStatement(sql);
            	rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    // no hay registros
                } else {
                    // hay registros
                    do {
                        InputReporteCantInvJornada item = new InputReporteCantInvJornada();
                        item.setIdArticulo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "ARTICULO"));
                        item.setDescripcion(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "DESCRIPCION"));
                        item.setTipoInv(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "TIPO_INV"));
                        item.setTipoGrupo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "TIPO_GRUPO_SIDRA"));
                        item.setCantInicial(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_INICIAL"));
                        item.setCantReservada(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_RESERVADA"));
                        item.setCantVendida(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_VENDIDA"));
                        item.setCantProcDevolucion(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_PROC_DEV"));
                        item.setCantDevuelta(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_DEVUELTA"));
                        item.setCantProcSiniestro(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_PROC_SIN"));
                        item.setCantSiniestrada(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_SINIESTRADA"));
                        item.setCantFinal(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "CANT_DISPONIBLE"));

                        list.add(item);
                    } while (rst.next());
                }
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }
        return list;
    }
    
    public static String queryArtBodegas( String listIdBodegas, String filtrosExtras, BigDecimal ID_PAIS){
    	String sql="";
    	sql = "WITH INVENTARIO AS (SELECT ARTICULO, CANTIDAD, TIPO_INV, TIPO_GRUPO_SIDRA, ESTADO FROM TC_SC_INVENTARIO "
                + "WHERE TCSCBODEGAVIRTUALID IN (" + listIdBodegas + ")), "
            + "ESTADO AS (SELECT VALOR, NOMBRE FROM TC_SC_CONFIGURACION WHERE TCSCCATPAISID = " + ID_PAIS
                + " AND UPPER(GRUPO) = '" + Conf.GRUPO_ESTADOS_INVENTARIO + "' AND UPPER(ESTADO) = 'ALTA'), "
            + "ARTICULOS AS (SELECT DISTINCT (ARTICULO), DESCRIPCION, TIPO_INV, TIPO_GRUPO_SIDRA FROM TC_SC_INVENTARIO "
                + "WHERE TCSCBODEGAVIRTUALID IN (" + listIdBodegas + ") "
                    + "AND ARTICULO != (SELECT VALOR FROM TC_SC_CONFIGURACION WHERE TCSCCATPAISID = " + ID_PAIS
                    + " AND UPPER(GRUPO) = '" + Conf.GRUPO_ARTICULO_CANTIDAD.toUpperCase() + "' AND UPPER(NOMBRE) = '" + Conf.ARTICULO_RECARGA.toUpperCase() + "' AND UPPER(ESTADO) = 'ALTA') "
                + "GROUP BY ARTICULO, DESCRIPCION, TIPO_INV, TIPO_GRUPO_SIDRA) "
            + "SELECT A.ARTICULO, A.DESCRIPCION, A.TIPO_INV, A.TIPO_GRUPO_SIDRA, "
            + "(SELECT NVL(SUM(CANTIDAD), 0) FROM INVENTARIO WHERE ARTICULO = A.ARTICULO AND TIPO_INV = A.TIPO_INV) AS CANT_INICIAL, "
            + "(SELECT NVL(SUM(CANTIDAD), 0) FROM INVENTARIO WHERE ESTADO = (SELECT VALOR FROM ESTADO WHERE UPPER(NOMBRE) = '" + Conf.INV_EST_RESERVADO.toUpperCase() + "')"
                + " AND ARTICULO = A.ARTICULO AND TIPO_INV = A.TIPO_INV) AS CANT_RESERVADA, "
            + "(SELECT NVL(SUM(CANTIDAD), 0) FROM INVENTARIO WHERE ESTADO = (SELECT VALOR FROM ESTADO WHERE UPPER(NOMBRE) = '" + Conf.INV_EST_VENDIDO.toUpperCase() + "')"
                + " AND ARTICULO = A.ARTICULO AND TIPO_INV = A.TIPO_INV) AS CANT_VENDIDA, "
            + "(SELECT NVL(SUM(CANTIDAD), 0) FROM INVENTARIO WHERE ESTADO = (SELECT VALOR FROM ESTADO WHERE UPPER(NOMBRE) = '" + Conf.INV_EST_DEVOLUCION.toUpperCase() + "')"
                + " AND ARTICULO = A.ARTICULO AND TIPO_INV = A.TIPO_INV) AS CANT_PROC_DEV, "
            + "(SELECT NVL(SUM(CANTIDAD), 0) FROM INVENTARIO WHERE ESTADO = (SELECT VALOR FROM ESTADO WHERE UPPER(NOMBRE) = '" + Conf.INV_EST_DEVUELTO.toUpperCase() + "')"
                + " AND ARTICULO = A.ARTICULO AND TIPO_INV = A.TIPO_INV) AS CANT_DEVUELTA, "
            + "(SELECT NVL(SUM(CANTIDAD), 0) FROM INVENTARIO WHERE ESTADO = (SELECT VALOR FROM ESTADO WHERE UPPER(NOMBRE) = '" + Conf.INV_EST_SINIESTRO.toUpperCase() + "')"
                + " AND ARTICULO = A.ARTICULO AND TIPO_INV = A.TIPO_INV) AS CANT_PROC_SIN, "
            + "(SELECT NVL(SUM(CANTIDAD), 0) FROM INVENTARIO WHERE ESTADO = (SELECT VALOR FROM ESTADO WHERE UPPER(NOMBRE) = '" + Conf.INV_EST_SINIESTRADO.toUpperCase() + "')"
                + " AND ARTICULO = A.ARTICULO AND TIPO_INV = A.TIPO_INV) AS CANT_SINIESTRADA, "
            + "(SELECT NVL(SUM(CANTIDAD), 0) FROM INVENTARIO WHERE ESTADO = (SELECT VALOR FROM ESTADO WHERE UPPER(NOMBRE) = '" + Conf.INV_EST_DISPONIBLE.toUpperCase() + "')"
                + " AND ARTICULO = A.ARTICULO AND TIPO_INV = A.TIPO_INV) AS CANT_DISPONIBLE "
            + "FROM ARTICULOS A" + filtrosExtras;
    	return sql;
    }
}