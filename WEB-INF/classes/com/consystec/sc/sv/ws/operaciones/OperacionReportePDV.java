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

import com.consystec.sc.ca.ws.input.reporte.InputReportePDV;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.reporte.OutputReportePDV;
import com.consystec.sc.sv.ws.metodos.CtrlReportePDV;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.orm.RutaPDV;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionReportePDV {
	private OperacionReportePDV(){}
    private static final Logger log = Logger.getLogger(OperacionReportePDV.class);

    public static OutputReportePDV doGet(Connection conn, InputReportePDV input, BigDecimal ID_PAIS) throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        OutputReportePDV output = new OutputReportePDV();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        Respuesta respuesta = new Respuesta();
        List<InputReportePDV> list = new ArrayList<InputReportePDV>();
        List<InputReportePDV> listDistribuidores = null;
        List<InputReportePDV> listDtsVacios = new ArrayList<InputReportePDV>();
        List<InputReportePDV> listPDV = null;

        String abrPais = ControladorBase.getPais(input.getCodArea());
        String tipoPDV = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELPDV, Conf.TIPO_PDV, input.getCodArea());
        String sql= queryReportePDV(input, ID_PAIS);
        log.trace("Query para obtener reportePDV: " + sql);
        
        try {
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst.next()) {
                List<InputReportePDV> listMeses = getMeses(conn, input.getFechaDesde(), input.getFechaHasta());

                do {
                    InputReportePDV item = new InputReportePDV();
                    String idPDV = "";

                    item.setIdDistribuidor(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, PuntoVenta.CAMPO_TCSCDTSID));
                    idPDV = UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PuntoVenta.CAMPO_TCSCPUNTOVENTAID);
                    item.setIdPDV(idPDV);
                    item.setNombrePDV(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PuntoVenta.CAMPO_NOMBRE));
                    item.setCategoria(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PuntoVenta.CAMPO_CATEGORIA));
                    item.setCanal(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PuntoVenta.CAMPO_CANAL));
                    item.setSubcanal(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PuntoVenta.CAMPO_SUBCANAL));
                    item.setDepartamento(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PuntoVenta.CAMPO_DEPARTAMENTO));
                    item.setMunicipio(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PuntoVenta.CAMPO_MUNICIPIO));
                    item.setDistrito(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PuntoVenta.CAMPO_DISTRITO));
                    item.setIdRuta(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Ruta.CAMPO_TC_SC_RUTA_ID));
                    item.setNombreRuta(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRERUTA"));
                    item.setIdVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Ruta.CAMPO_SEC_USUARIO_ID));
                    item.setEstado(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PuntoVenta.CAMPO_ESTADO));
                    item.setCreado_por(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PuntoVenta.CAMPO_CREADO_POR));
                    item.setCreado_el(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, PuntoVenta.CAMPO_CREADO_EL));
                    item.setVentas(getVentas(conn, idPDV, tipoPDV,  listMeses, abrPais));

                    list.add(item);
                } while (rst.next());

                listDistribuidores = getDistribuidores(conn, input.getIdDistribuidor(), input.getCodArea(), ID_PAIS);

                for (int i = 0; i < listDistribuidores.size(); i++) {
                    listPDV = new ArrayList<InputReportePDV>();

                    for (int j = 0; j < list.size(); j++) {
                        if (listDistribuidores.get(i).getIdDistribuidor().equals(list.get(j).getIdDistribuidor())) {
                            listPDV.add(list.get(j));
                        }
                    }

                    if (listPDV.size() > 0) {
                        listDistribuidores.get(i).setPuntosDeVenta(listPDV);
                        list.removeAll(listPDV);
                    } else {
                        listDtsVacios.add(listDistribuidores.get(i));
                    }
                }

                listDistribuidores.removeAll(listDtsVacios);

                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output.setDatos(listDistribuidores);

            } else {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSOS, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());
            }

            return output;

        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);

            output.setRespuesta(respuesta);
        }
    }
    
    public static String queryReportePDV(InputReportePDV input, BigDecimal ID_PAIS) {
    	   List<Filtro> condiciones = new ArrayList<Filtro>();
        String sql="SELECT "
                + "PV." + PuntoVenta.CAMPO_TCSCPUNTOVENTAID + ", "
                + "PV." + PuntoVenta.CAMPO_NOMBRE + ", "
                + "PV." + PuntoVenta.CAMPO_ESTADO + ", "
                + "PV." + PuntoVenta.CAMPO_CREADO_EL + ", "
                + "PV." + PuntoVenta.CAMPO_CREADO_POR + ", "
                + "PV." + PuntoVenta.CAMPO_TCSCDTSID + ", "
                + "PV." + PuntoVenta.CAMPO_CANAL + ", "
                + "PV." + PuntoVenta.CAMPO_SUBCANAL + ", "
                + "PV." + PuntoVenta.CAMPO_CATEGORIA + ", "
                + "PV." + PuntoVenta.CAMPO_DEPARTAMENTO + ", "
                + "PV." + PuntoVenta.CAMPO_MUNICIPIO + ", "
                + "PV." + PuntoVenta.CAMPO_DISTRITO + ", "
                + "R." + Ruta.CAMPO_TC_SC_RUTA_ID + ", "
                + "R." + Ruta.CAMPO_NOMBRE + " AS NOMBRERUTA, "
                + "R." + Ruta.CAMPO_SEC_USUARIO_ID
            + " FROM " + ControladorBase.getParticion(PuntoVenta.N_TABLA, Conf.PARTITION, "", input.getCodArea()) + " PV, " + Ruta.N_TABLA + " R, " + RutaPDV.N_TABLA + " RPV";
        
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, "R." + Ruta.CAMPO_TC_SC_RUTA_ID, "RPV." + RutaPDV.CAMPO_TCSCRUTAID));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, "R." + Ruta.CAMPO_TCSCCATPAISID, "PV." + PuntoVenta.CAMPO_TCSCCATPAISID));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, "RPV." + RutaPDV.CAMPO_TCSCPUNTOVENTAID, "PV." + PuntoVenta.CAMPO_TCSCPUNTOVENTAID));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, "R." + Ruta.CAMPO_TCSCCATPAISID, ""+ID_PAIS));
        condiciones.addAll(CtrlReportePDV.obtenerCondiciones(input, ID_PAIS));
        sql += UtileriasBD.getCondiciones(condiciones);

        sql += " ORDER BY TCSCDTSID, TCSCPUNTOVENTAID";
        
        return sql;
    }

    private static List<InputReportePDV> getMeses(Connection conn, String fechaInicio, String fechaFin)
            throws SQLException {
        ArrayList<InputReportePDV> list = new ArrayList<InputReportePDV>();

        PreparedStatement pstmt = null;
        ResultSet rst = null;

      
	        String sql =queryMeses( fechaInicio,  fechaFin);
	
            log.trace("Query meses: " + sql);
         try {
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst.next()) {
                do {
                    InputReportePDV item = new InputReportePDV();
                    item.setAnio(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "ANIO"));
                    item.setMes(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "MES").trim());

                    list.add(item);
                } while (rst.next());
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return list;
    }

    public static String queryMeses(String fechaInicio, String fechaFin) {

        String sql = "SELECT TO_CHAR(MESES, 'yyyy') ANIO, TO_CHAR(MESES, 'Month', 'NLS_DATE_LANGUAGE=SPANISH') MES "
                + "FROM ("
                    + "SELECT ADD_MONTHS(TO_DATE('" + fechaInicio + "','" + Conf.TXT_FORMATO_FECHA_INPUT + "'), ROWNUM-1) MESES "
                    + "FROM ALL_OBJECTS "
                    + "WHERE "
                        + "ROWNUM <= MONTHS_BETWEEN(TO_DATE('" + fechaFin + "','" + Conf.TXT_FORMATO_FECHA_INPUT + "'),"
                                + " ADD_MONTHS(TO_DATE('" + fechaInicio + "','" + Conf.TXT_FORMATO_FECHA_INPUT + "'), -1)) "
                    + "ORDER BY MESES)";
        return sql;
    }
    private static List<InputReportePDV> getVentas(Connection conn, String idPDV, String tipoPDV,
           List<InputReportePDV> listMeses, String abrPais) throws SQLException {
        ArrayList<InputReportePDV> list = new ArrayList<InputReportePDV>();
        ArrayList<InputReportePDV> listCompleto = new ArrayList<InputReportePDV>();

        PreparedStatement pstmt = null;
        ResultSet rst = null;

    
            String sql =  queryVentas( idPDV, tipoPDV, abrPais);

            log.trace("Query ventas reportePDV: " + sql);
          try {
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst.next()) {
                do {
                    InputReportePDV item = new InputReportePDV();
                    item.setAnio(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "ANIO"));
                    item.setMes(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "MES").trim());
                    item.setSumFacturacion(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "TOTAL"));

                    list.add(item);
                } while (rst.next());

                for (int i = 0; i < listMeses.size(); i++) {
                    for (int j = 0; j < list.size(); j++) {
                        InputReportePDV item = null;

                        if (listMeses.get(i).getAnio().equalsIgnoreCase(list.get(j).getAnio())
                                && listMeses.get(i).getMes().equalsIgnoreCase(list.get(j).getMes())) {

                            item = new InputReportePDV();
                            item.setAnio(list.get(j).getAnio());
                            item.setMes(list.get(j).getMes());
                            item.setSumFacturacion(list.get(j).getSumFacturacion());
                            listCompleto.add(item);
                            break;
                        }

                        if (j == (list.size() - 1) && item == null) {
                            item = new InputReportePDV();
                            item.setAnio(listMeses.get(i).getAnio());
                            item.setMes(listMeses.get(i).getMes());
                            item.setSumFacturacion("0");
                            listCompleto.add(item);
                        }
                    }
                }

            } else {
                for (int i = 0; i < listMeses.size(); i++) {
                    InputReportePDV item = new InputReportePDV();
                    item.setAnio(listMeses.get(i).getAnio());
                    item.setMes(listMeses.get(i).getMes());
                    item.setSumFacturacion("0");

                    listCompleto.add(item);
                }
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return listCompleto;
    }
    
    public static String queryVentas(String idPDV,String tipoPDV, String abrPais) {
        String sql = "SELECT V.IDTIPO, "
                + "TO_CHAR(V.FECHA_EMISION, 'YYYY') ANIO, "
                + "TO_CHAR(V.FECHA_EMISION, 'Month', 'NLS_DATE_LANGUAGE=SPANISH') MES, "
                + "SUM(MONTO_FACTURA) TOTAL "
            + "FROM TC_SC_VENTA PARTITION (" + abrPais + ") V "
            + "WHERE V.TIPO = '" + tipoPDV + "' "
                + "AND V.IDTIPO = " + idPDV
        + " GROUP BY V.IDTIPO, "
            + "TO_CHAR(V.FECHA_EMISION, 'YYYY'), "
            + "TO_CHAR(V.FECHA_EMISION, 'Month', 'NLS_DATE_LANGUAGE=SPANISH') "
        + "ORDER BY IDTIPO, ANIO, MES";
        
        return sql;
    } 

    private static ArrayList<InputReportePDV> getDistribuidores(Connection conn, String idDTS, String codArea, BigDecimal ID_PAIS) throws SQLException {
        ArrayList<InputReportePDV> list = new ArrayList<InputReportePDV>();

        PreparedStatement pstmt = null;
        ResultSet rst = null;

        try{
	        List<Filtro> condiciones = new ArrayList<Filtro>();
	        
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TCSCCATPAISID,
                    ""+ID_PAIS));
	        if (idDTS == null || idDTS.equals("")) {
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TC_SC_DTS_ID,
	                    "PV." + PuntoVenta.CAMPO_TCSCDTSID));
	        } else {
	            condiciones.add(
	                    UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TC_SC_DTS_ID, idDTS));
	        }
	        
	        String[] campos = {
	                UtileriasJava.setSelect(Conf.SELECT_DISTINCT, PuntoVenta.CAMPO_TCSCDTSID),
	                "(" + UtileriasBD.armarQuerySelectField(Distribuidor.N_TABLA + " D",
	                        "D." + Distribuidor.CAMPO_NOMBRES, condiciones, null) + ") AS " + Distribuidor.CAMPO_NOMBRES
	        };
	
	        String sql = UtileriasBD.armarQuerySelect(ControladorBase.getParticion(PuntoVenta.N_TABLA, Conf.PARTITION, idDTS, codArea) + " PV", campos, condiciones);
	
	        pstmt = conn.prepareStatement(sql);
	        rst = pstmt.executeQuery(sql);
	        if (rst.next()) {
	            do {
	                InputReportePDV item = new InputReportePDV();
	                item.setIdDistribuidor(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, PuntoVenta.CAMPO_TCSCDTSID));
	                item.setNombreDistribuidor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Distribuidor.CAMPO_NOMBRES));
	                
	                list.add(item);
	            } while (rst.next());
	        }
        }finally{

	        DbUtils.closeQuietly(rst);
	        DbUtils.closeQuietly(pstmt);
        }

        return list;
    }
}